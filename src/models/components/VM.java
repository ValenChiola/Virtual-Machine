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
import models.functions.Sys;

public class VM {
	final int architecture = 32; // in bits
	final int bytesToAccess = architecture / 8;

	// Components
	public Ram ram;
	public TableSegments ts;
	public Processor processor;
	public Map<Integer, Register> registers;
	public Map<Integer, Mnemonic> mnemonics;

	public VM() {
		ram = new Ram(this);
		ts = new TableSegments(this);
		processor = new Processor(this);
		registers = new HashMap<>();
		mnemonics = new HashMap<>();
	}

	public void start(String pathname) {
		try {

			byte[] code = getCode(pathname);

			// Program loading :P
			ts.init(code);
			ram.init(code);
			registers();
			mnemonics();

			// Execute operations :/
			execute(code);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void execute(byte[] code) {
		Register IP = registers.get(5);
		int ii = 0;
		while (IP.getValue() < code.length) {
			//System.out.println("======     ITERACIÓN " + (ii + 1) + "	  ======");
			int IpValue = IP.getValue();

			int instruction = ram.getValue(IpValue, 1);

			int BBytes = ((instruction & 0xC0) >> 6);
			int ABytes = ((instruction & 0x30) >> 4);
			int operation = (instruction & 0x1F);

			int B = 0;
			int A = 0;

			for (int i = 1; i < BBytes + 1; i++)
				B = (B << 8) | ram.getValue(IpValue + i, 1) & 0xFFFFFF;

			for (int i = 1; i < ABytes + 1; i++)
				A = (A << 8) | ram.getValue(IpValue + BBytes + i, 1) & 0xFFFFFF;

			IP.setValue(ts.getBaseShifted(0) | (IpValue + (ABytes + BBytes + 1))); // Segment | Offset

			// System.out.println("Instruction: " + String.format("%8s ", Integer.toBinaryString(instruction & 0xFF)));
			// System.out.println("Abytes: " + String.format("%2s ", Integer.toBinaryString(ABytes & 0x3)));
			// System.out.println("A: " + String.format("%24s ", Integer.toBinaryString(A & 0xFFFFFF)));
			// System.out.println("Bbytes: " + String.format("%2s ", Integer.toBinaryString(BBytes & 0x3)));
			// System.out.println("B: " + String.format("%24s ", Integer.toBinaryString(B & 0xFFFFFF)));
			//System.out.println("Operation: " + String.format("%02X ", operation));

			Mnemonic mnemonic = mnemonics.get(operation);

			if (mnemonic == null)
				throw new Error("Mnemonic " + operation + " not found :/");

			mnemonic._execute(ABytes, BBytes, A, B);

			// System.out.println("CC: " + String.format("%32s ", Integer.toBinaryString(dataReadHandler(0X80, 1))));
			// System.out.println("IP: " + String.format("%08X ", dataReadHandler(0x50, 1)));
			// System.out.println("EAX: " + String.format("%08X ", dataReadHandler(0xA0, 1)));
			// System.out.println("ECX: " + String.format("%08X ", dataReadHandler(0xC0, 1)));
			// System.out.println("EDX: " + String.format("%08X ", dataReadHandler(0xD0, 1)));
			// System.out.println("EFX: " + String.format("%08X ", dataReadHandler(0xF0, 1)));
			// System.out.println("[0]/DS: " + String.format("%02X ", dataReadHandler(0x10, 3)));
			// System.out.println("[4]: " + String.format("%02X ", dataReadHandler(0x410, 3)));
			// System.out.println("====== FIN DE ITERACIÓN " + (ii + 1) + " ======");
			ii++;
		}
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
	 */
	public void dataWriteHandler(int address, int value, int type) {
		if (type <= 0 || type >= 4)
			throw new Error("Estás haciendo cualquiera flaco.");

		else if (type == 1) {// Registro
			int registerAddress = (address & 0xFF) >> 4;

			// El bit 5 y 6 para ax, ah o al.
			int identifier = (address & 0xC) >> 2;
			registers.get(registerAddress).setValue(value, identifier);
		} else if (type == 2)
			throw new Error("Estás haciendo cualquiera flaco."); // Inmediato

		else if (type == 3) {
			int registerCode = (address & 0xFF) >> 4;
			if (registerCode <= 1) {
				int segment = (address & 0xFF) << 12;
				int offset = (address & 0xFFFF00) >> 8;
				ram.setValue(segment + offset, value);
			} else {
				Register register = registers.get(registerCode);
				if (register == null)
					throw new Error("Register not found.");
				
				int logicAddress = register.getValue();
				ram.setValue(logicAddress, value);
			}
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
	 */
	public int dataReadHandler(int value, int type) {
		if (type <= 0 || type >= 4)
			throw new Error("Estás haciendo cualquiera flaco.");

		if (type == 1) { // registro
			int registerAddress = (value & 0xFF) >> 4;
			int identifier = (value & 0xC) >> 2; // AX, AH, AL

			return registers.get(registerAddress).getValue(identifier);
		}
		if (type == 2) // inmediato
			return ((value & 0xFFFF) << 16) >> 16; // por las dudas

		// memoria
		int registerCode = (value & 0xF0) >> 4;
		if (registerCode <= 1) {//acceder a memoria directamente
			int segment = (value & 0xFF) << 12;
			int offset = (value & 0xFFFF00) >> 8;
			return ram.getValue(segment | offset);
		} else { //puntero a memoria
			Register register = registers.get(registerCode);
			if (register == null)
				throw new Error("Register not found.");

			int logicAddress = register.getValue();
			return ram.getValue(logicAddress);
		}
	}

	private byte[] getCode(String pathname) throws IOException {
		if (!pathname.endsWith(".vmx"))
			throw new Error("File not supported");
			
		byte[] content = Files.readAllBytes(Paths.get(pathname));
		int codeSize = (content[6] << 8) | content[7];
		byte[] code = new byte[codeSize];
		System.arraycopy(content, 8, code, 0, codeSize);
			
		System.out.println("--------------------------------");
		// System.out.println("Header");
		// for (int i = 0; i < 8; i++) {
		// 	System.out.print(String.format("%02X ", content[i]));
		// }
		//System.out.println();
		System.out.println("CS - CodeSize: " + codeSize);
		for (int i = 0; i < codeSize; i++) {
			System.out.print(String.format("%02X ", code[i]));
		}
		System.out.println();
		System.out.println("--------------------------------");

		return code;
	}

	private void registers() {
		// Init registers :)
		registers.put(0, new Register(0x00000000));
		registers.put(1, new Register(0x00010000));// Dirección lógica hacia DS
		registers.put(5, new Register(0x00000000));
		registers.put(8, new Register());
		registers.put(9, new Register());
		registers.put(10, new Register());
		registers.put(11, new Register());
		registers.put(12, new Register());
		registers.put(13, new Register());
		registers.put(14, new Register());
		registers.put(15, new Register());
	}

	private void mnemonics() {
		// Init Mnemonics :o
		mnemonics.put(0x00, new Sys(this));
		mnemonics.put(0x01, new JMP(this));
		mnemonics.put(0x02, new JZ(this));
		mnemonics.put(0x03, new JP(this));
		mnemonics.put(0x04, new JN(this));
		mnemonics.put(0x05, new JNZ(this));
		mnemonics.put(0x06, new JNP(this));
		mnemonics.put(0x07, new JNN(this));
		mnemonics.put(0x08, new Not(this));
		mnemonics.put(0x0F, new Stop(this));
		mnemonics.put(0x10, new Mov(this));
		mnemonics.put(0x11, new Add(this));
		mnemonics.put(0x12, new Sub(this));
		mnemonics.put(0x13, new Swap(this));
		mnemonics.put(0x14, new Mul(this));
		mnemonics.put(0x15, new Div(this));
		mnemonics.put(0x16, new Cmp(this));
		mnemonics.put(0x17, new Shl(this));
		mnemonics.put(0x18, new Shr(this));
		mnemonics.put(0x19, new And(this));
		mnemonics.put(0x1A, new Or(this));
		mnemonics.put(0x1B, new Xor(this));
		mnemonics.put(0x1C, new Ldl(this));
		mnemonics.put(0x1D, new Ldh(this));
		mnemonics.put(0x1E, new Rnd(this));
	}

}
