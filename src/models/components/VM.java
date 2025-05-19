package models.components;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import models.functions.Ldh;
import models.functions.Ldl;
import models.functions.Mnemonic;
import models.functions.Mov;
import models.functions.Not;
import models.functions.Rnd;
import models.functions.Stop;
import models.functions.Swap;
import models.functions.Sys;
import models.functions.Ret;
import models.functions.Push;
import models.functions.Pop;
import models.functions.Call;
import models.functions.arithmetic.Add;
import models.functions.arithmetic.And;
import models.functions.arithmetic.Cmp;
import models.functions.arithmetic.Div;
import models.functions.arithmetic.Mul;
import models.functions.arithmetic.Or;
import models.functions.arithmetic.Shl;
import models.functions.arithmetic.Shr;
import models.functions.arithmetic.Sub;
import models.functions.arithmetic.Xor;
import models.functions.jumps.JMP;
import models.functions.jumps.JN;
import models.functions.jumps.JNN;
import models.functions.jumps.JNP;
import models.functions.jumps.JNZ;
import models.functions.jumps.JP;
import models.functions.jumps.JZ;
import utils.ArgsParser;
import utils.Converter;
import utils.log.Log;

public class VM {
	final int architecture = 32; // in bits
	final int bytesToAccess = architecture / 8;

	// Components
	public Ram ram;
	public TableSegments ts;
	public Processor processor;
	public Map<Integer, Register> registers;
	public Map<Integer, Mnemonic> mnemonics;
	byte[] code;
	byte[] constants;
	public int ksSize;
	public int dsSize;
	public int esSize;
	public int ssSize;
	public int offset;
	public int version;

	private String[] registerNames = { "CS", "DS", "ES", "SS", "KS", "IP", "SP", "BP", "CC", "AC", "EAX", "EBX",
			"ECX", "EDX", "EEX", "EFX" };

	private static VM instace;

	public VM() throws Exception {
	}

	public VM ram(Ram ram) {
		this.ram = ram;
		return this;
	}

	public VM ts(TableSegments ts) {
		this.ts = ts;
		return this;
	}

	public VM processor(Processor processor) {
		this.processor = processor;
		return this;
	}

	public VM build() throws Exception {

		if (this.ram == null)
			throw new Error("RAM not found.");

		if (this.ts == null)
			throw new Error("Table Segments not found.");

		if (this.processor == null)
			throw new Error("Processor not found.");

		VM.instace = this;

		mnemonics();

		String vmiFile = ArgsParser.getVmiFile();
		String vmxFile = ArgsParser.getVmxFile();
		if (vmiFile != null && vmxFile == null) {
			loadVmi(vmiFile);
		} else {
			setSegments(vmxFile);
			ts.init();
			ram.init();
			registers();

			// Push params to stack (Main Subrutine)
			int psSize = ts.getSize(ts.ps);
			int params = ArgsParser.getProgramParams().size();
			int argv = psSize - 4 * params;

			new Push().execute(2, params > 0 ? argv : -1); // Pointer to argv

			new Push().execute(2, params); // Number of params

			new Push().execute(2, -1); // Return address of main subrutine

		}

		return this;
	}

	private void loadVmi(String vmiFile) throws IOException {
		byte[] data = Files.readAllBytes(Paths.get(vmiFile));

		version = data[5];
		ram = new Ram((data[6] << 8) | data[7]);

		registers = new HashMap<>();
		for (int i = 0; i < 16; i++)
			registers.put(i, new Register(
					registerNames[i], (data[8 + i * 4] << 24) | (data[9 + i * 4] << 16)
							| (data[10 + i * 4] << 8) | data[11 + i * 4]));

		ts.cs = registers.get(0).getValue() >>> 16;
		ts.ds = registers.get(1).getValue() >>> 16;
		ts.es = registers.get(2).getValue() >>> 16;
		ts.ss = registers.get(3).getValue() >>> 16;
		ts.ks = registers.get(4).getValue() >>> 16;

		for (int i = 0; i < 8; i++)
			ts.setValue(i, (data[64 + i * 4] << 8) | (data[65 + i * 4]), (data[66 + i * 4] << 8)
					| data[67 + i * 4]);

		for (int i = 0; i < ram.getCapacity(); i++)
			ram.getMemory()[i] = data[104 + i];

		code = new byte[ts.getSize(ts.cs)];
		System.arraycopy(ram.getMemory(), ts.getBase(ts.cs), code, 0, ts.getSize(ts.cs));

		constants = new byte[ts.getSize(ts.ks)];
		System.arraycopy(ram.getMemory(), ts.getBase(ts.ks), constants, 0, ts.getSize(ts.ks));

		try {
			printRegisters();
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
	}

	public static VM getInstance() {
		return instace;
	}

	public void start() throws Exception {

		if (ArgsParser.isDissasemblerEnabled())
			disassembler();

		execute();

	}

	public void executeNextOperation() throws Exception {
		Register IP = registers.get(5);
		int IpValue = IP.getValue();

		int instruction = ram.getValue(IpValue, 1);

		int BBytes = ((instruction & 0xC0) >> 6);
		int ABytes = ((instruction & 0x30) >> 4);
		int operation = (instruction & 0x1F);

		int B = ram.getValue(IpValue + 1, BBytes);
		int A = ram.getValue((IpValue + BBytes) + 1, ABytes);

		IP.setValue((IpValue + ABytes + BBytes) + 1);

		// Log.debug("Instruction: " + String.format("%8s ",
		// Integer.toBinaryString(instruction & 0xFF)));
		// Log.debug("Abytes: " + String.format("%2s ", Integer.toBinaryString(ABytes &
		// 0x3)));
		// Log.debug("A: " + String.format("%24s ", Integer.toBinaryString(A &
		// 0xFFFFFF)));
		// Log.debug("Bbytes: " + String.format("%2s ", Integer.toBinaryString(BBytes &
		// 0x3)));
		// Log.debug("B: " + String.format("%24s ", Integer.toBinaryString(B &
		// 0xFFFFFF)));
		Log.debug("Operation: " + String.format("%02X ", operation));

		Mnemonic mnemonic = mnemonics.get(operation);

		if (mnemonic == null)
			throw new Exception("Mnemonic " + operation + " not found :/");

		mnemonic._execute(ABytes, BBytes, A, B);

		printRegisters();
		printMemory();
		printStack();
	}

	private void execute() throws Exception {
		Register IP = registers.get(5);
		int ii = 0;

		int csSize = ts.getSize(ts.cs);
		while (IP.getValue(3) >= 0 && IP.getValue(3) < csSize) {
			Log.debug("======     ITERACIÓN " + (ii + 1) + "	  ======");

			executeNextOperation();

			Log.debug("====== FIN DE ITERACIÓN " + (ii + 1) + " ======");
			ii++;
		}
	}

	private void disassembler() throws Exception {
		System.out.println("--------------------------------");

		int maxSeparation = 20;

		// Constants
		if (constants != null) {
			int i = 0;
			while (i < constants.length) {
				int address = i;
				StringBuilder bytes = new StringBuilder();
				StringBuilder string = new StringBuilder();

				int j = i;
				while (j < constants.length && constants[j] != '\0') {
					bytes.append(String.format("%02X ", constants[j]));
					string.append(Converter.numberToString(constants[j], 0x02, 1));
					i++;
					j++;
				}
				i++;

				if (bytes.length() > maxSeparation)
					maxSeparation = bytes.length();

				System.out.println("  [" + String.format("%04X", address) + "] " + String.format(
						"%-" + maxSeparation + "s", bytes.toString()) + " | "
						+ string.toString());
			}

		}

		int IpValue = ts.cs << 16;
		int csSize = ts.getSize(ts.cs);

		while ((IpValue & 0xFFFF) < csSize) {

			int instruction = ram.getValue(IpValue, 1);

			int BBytes = ((instruction & 0xC0) >> 6);
			int ABytes = ((instruction & 0x30) >> 4);
			int operation = (instruction & 0x1F);

			int B = ram.getValue(IpValue + 1, BBytes);
			int A = ram.getValue((IpValue + BBytes) + 1, ABytes);

			int increment = ABytes + BBytes + 1;

			Mnemonic mnemonic = mnemonics.get(operation);

			if (mnemonic == null)
				System.out.println("Mnemonic " + operation + " not found :/");
			else {
				String BOperand = getDisassemblerOperand(BBytes, B);
				String AOperand = getDisassemblerOperand(ABytes, A);

				byte[] aux = new byte[increment];
				System.arraycopy(code, IpValue & 0xFFFF, aux, 0, increment);

				String bytes = "";
				for (byte b : aux)
					bytes += String.format("%02X", b) + " ";

				boolean isEntryPoint = (IpValue & 0xFFFF) == 0;

				String firstPart = (isEntryPoint ? "> ["
						: "  [")
						+ String.format("%04X", processor.logicToPhysic(IpValue, increment))
						+ "] "
						+ String.format(
								"%-" + maxSeparation + "s", bytes)
						+ " | "
						+ mnemonic.getName() + " ";

				String secondPart = ((operation >= 0x01 && operation <= 0x07) // Jumps
						? "<" + String.format("%04X",
								Integer.valueOf(BOperand)) + ">"
						: BOperand);

				if (ABytes == 0 && BBytes == 0)
					System.out.println(firstPart);
				else if (ABytes == 0)
					System.out.println(firstPart + secondPart);
				else
					System.out.println(firstPart + AOperand + ", " + BOperand);
			}

			IpValue += increment;
		}
		System.out.println("--------------------------------");
	}

	private String getDisassemblerOperand(int type, int value) {
		if (type == 0 || type == 2) // Immediate
			return "" + value;

		if (type == 1) { // Register
			int registerAddress = (value & 0xFF) >> 4;
			int identifier = (value & 0xC) >> 2;

			Register register = registers.get(registerAddress);
			if (register == null)
				throw new Error("Register not found.");

			return register.getName(identifier);
		}

		if (type == 3) {
			int registerCode = (value & 0xF0) >> 4;
			if (registerCode <= 4) {// acceder a memoria directamente
				int bytesToRead = bytesToAccess - (value & 3);
				String modifier = bytesToRead == 1 ? "b" : bytesToRead == 2 ? "w" : "l";

				int offset = (value & 0xFFFF00) >> 8;

				return modifier + "[" + offset + "]";
			} else { // puntero a memoria

				Register register = registers.get(registerCode);
				if (register == null)
					throw new Error("Register not found.");

				return "[" + register.getName() + "]";
			}
		}
		return "";
	}

	/**
	 * Handles writing data to a specific location based on the provided type.
	 *
	 * @param address The address where the data should be written.
	 * @param value   The value to be written at the specified address.
	 * @param type    The type of data to be written:
	 *                0 - No operation (throws an error),
	 *                1 - Write to a register,
	 *                2 - Immediate value (throws an error),
	 *                3 - Write to memory.
	 * @throws Exception
	 */
	public void dataWriteHandler(int address, int value, int type) throws Exception {
		if (type <= 0 || type == 2 || type >= 4)
			throw new Exception("Cannot write to this type (" + type + ").");

		else if (type == 1) {// Registro
			int registerAddress = (address & 0xFF) >> 4;

			// El bit 5 y 6 para ax, ah o al.
			int identifier = (address & 0xC) >> 2;
			registers.get(registerAddress).setValue(value, identifier);
		} else if (type == 3) {
			int bytesToWrite = bytesToAccess - (address & 3);
			int registerCode = (address & 0xFF) >> 4;
			Register register = registers.get(registerCode);
			if (register == null)
				throw new Exception("Register not found.");
			int registerValue = register.getValue();
			if (registerCode <= 4) {
				int segment = registerValue;
				int offset = (address & 0xFFFF00) >> 8;
				ram.setValue(segment | offset, value, bytesToWrite);
			} else
				ram.setValue(registerValue, value, bytesToWrite);
		}
	}

	/**
	 * Handles reading data from a specific location based on the provided type.
	 *
	 * @param value The value or address to be read.
	 * @param type  The type of data to be read:
	 *              0 - No operation (throws an error),
	 *              1 - Read from a register,
	 *              2 - Immediate value,
	 *              3 - Read from memory.
	 * @return The data read from the specified location.
	 * @throws Exception
	 */
	public int dataReadHandler(int value, int type) throws Exception {
		if (type <= 0 || type >= 4)
			throw new Exception("Cannot read from this type (" + type + ").");

		if (type == 1) { // registro
			int registerAddress = (value & 0xFF) >> 4;
			int identifier = (value & 0xC) >> 2; // AX, AH, AL

			return registers.get(registerAddress).getValue(identifier);
		}
		if (type == 2) // inmediato
			return ((value & 0xFFFF) << 16) >> 16; // por las dudas

		// memoria
		int bytesToRead = bytesToAccess - (value & 3);
		int registerCode = (value & 0xFF) >> 4;
		Register register = registers.get(registerCode);
		if (register == null)
			throw new Exception("Register not found.");
		int registerValue = register.getValue();
		if (registerCode <= 4) {
			int segment = registerValue;
			int offset = (value & 0xFFFF00) >> 8;
			return ram.getValue(segment | offset, bytesToRead);
		} else
			return ram.getValue(registerValue, bytesToRead);
	}

	private void setSegments(String pathname) throws Exception {
		byte[] content;

		try {
			content = Files.readAllBytes(Paths.get(pathname));
		} catch (IOException e) {
			throw new Exception("File not found");
		}

		if (content.length == 0)
			throw new Exception("File is empty or not found");

		version = content[5];
		int csSize = (content[6] << 8) | (((int) content[7] << 24) >>> 24);
		code = new byte[csSize];
		if (version == 1) {
			System.arraycopy(content, 8, code, 0, csSize);
		} else if (version == 2) {
			dsSize = (content[8] << 8) | (((int) content[9] << 24) >>> 24);
			esSize = (content[10] << 8) | (((int) content[11] << 24) >>> 24);
			ssSize = (content[12] << 8) | (((int) content[13] << 24) >>> 24);
			ksSize = (content[14] << 8) | (((int) content[15] << 24) >>> 24);
			offset = (content[16] << 8) | (((int) content[17] << 24) >>> 24);
			System.arraycopy(content, 18, code, 0, csSize);
			constants = new byte[ksSize];
			System.arraycopy(content, 18 + csSize, constants, 0, ksSize);
		} else {
			throw new Exception("Invalid version");
		}
		System.out.println("--------------------------------");
		System.out.println("Version: " + version);
		System.out.println("Offset: " + offset);
		System.out.println("CodeSize: " + csSize);
		System.out.println("--------------------------------");
	}

	private void registers() {
		registers = new HashMap<>();
		registers.put(0, new Register("CS", ts.cs << 16));
		registers.put(1, new Register("DS", ts.ds << 16));
		registers.put(2, new Register("ES", ts.es << 16));
		registers.put(3, new Register("SS", ts.ss << 16));
		registers.put(4, new Register("KS", ts.ks << 16));
		registers.put(5, new Register("IP", ts.cs << 16 | offset & 0xFFFF));
		registers.put(6, new Register("SP", ts.ss << 16 | ts.getSize(ts.ss)));
		registers.put(7, new Register("BP"));
		registers.put(8, new Register("CC"));
		registers.put(9, new Register("AC"));
		registers.put(10, new Register("EAX"));
		registers.put(11, new Register("EBX"));
		registers.put(12, new Register("ECX"));
		registers.put(13, new Register("EDX"));
		registers.put(14, new Register("EEX"));
		registers.put(15, new Register("EFX"));
	}

	private void mnemonics() {
		mnemonics = new HashMap<>();
		mnemonics.put(0x00, new Sys());
		mnemonics.put(0x01, new JMP());
		mnemonics.put(0x02, new JZ());
		mnemonics.put(0x03, new JP());
		mnemonics.put(0x04, new JN());
		mnemonics.put(0x05, new JNZ());
		mnemonics.put(0x06, new JNP());
		mnemonics.put(0x07, new JNN());
		mnemonics.put(0x08, new Not());
		mnemonics.put(0x0B, new Push());
		mnemonics.put(0x0C, new Pop());
		mnemonics.put(0x0D, new Call());
		mnemonics.put(0x0E, new Ret());
		mnemonics.put(0x0F, new Stop());
		mnemonics.put(0x10, new Mov());
		mnemonics.put(0x11, new Add());
		mnemonics.put(0x12, new Sub());
		mnemonics.put(0x13, new Swap());
		mnemonics.put(0x14, new Mul());
		mnemonics.put(0x15, new Div());
		mnemonics.put(0x16, new Cmp());
		mnemonics.put(0x17, new Shl());
		mnemonics.put(0x18, new Shr());
		mnemonics.put(0x19, new And());
		mnemonics.put(0x1A, new Or());
		mnemonics.put(0x1B, new Xor());
		mnemonics.put(0x1C, new Ldl());
		mnemonics.put(0x1D, new Ldh());
		mnemonics.put(0x1E, new Rnd());
	}

	private void printRegisters() throws Exception {

		for (Register register : registers.values())
			Log.debug(register.getName() + ": " + String.format("%08X ", register.getValue()));
	}

	private void printMemory() throws Exception {
		for (int i = 0; i < 15; i++)
			Log.debug("[" + i + "]: " + String.format("%02X ", ram.getValue(ts.ds << 16 | i, 1)));
	}

	private void printStack() throws Exception {
		Log.debug("--------------Stack-----------------");
		int stackSize = ts.getSize(ts.ss);
		for (int i = stackSize - 40; i < stackSize; i += 4)
			Log.debug("[" + i + "]: " + String.format("%08X ", ram.getValue(ts.ss << 16 | i, 4)));
		Log.debug("------------------------------------");
	}

}
