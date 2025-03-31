package models.components;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import models.functions.Mnemonic;
import models.functions.Mov;
import models.functions.Xor;
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

			if (!pathname.endsWith(".vmx"))
				throw new Error("File not supported");

			byte[] content = Files.readAllBytes(Paths.get(pathname));
			int codeSize = (content[6] << 8) + content[7];
			byte[] code = new byte[codeSize];
			System.arraycopy(content, 8, code, 0, codeSize);

			ts.init(code);
			ram.init(code);

			// Init registers :)
			registers.put(0, new Register(ts.getBase(ts.getCSKey())));
			registers.put(1, new Register(ts.getBase(ts.getDSKey())));
			registers.put(5, new Register(ts.getBase(ts.getCSKey())));
			registers.put(8, new Register());
			registers.put(9, new Register());
			registers.put(10, new Register());
			registers.put(11, new Register());
			registers.put(12, new Register());
			registers.put(13, new Register());
			registers.put(14, new Register());
			registers.put(15, new Register());

			// Init Mnemonics
			mnemonics.put(0x10, new Mov(this));
			mnemonics.put(0x1B, new Xor(this));
			mnemonics.put(0x00, new Sys(this));

			System.out.println("--------------------------------");
			System.out.println("Header");
			// muestra header
			for (int i = 0; i < 8; i++) {
				System.out.print(String.format("%02X ", content[i]));
			}
			System.out.println();
			System.out.println("CS - CodeSize: " + codeSize);
			// muestra CS
			for (int i = 0; i < codeSize; i++) {
				System.out.print(String.format("%02X ", code[i]));
			}
			System.out.println();
			System.out.println("--------------------------------");

			execute(code);
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void execute(byte[] code) {
		Register IP = registers.get(5);
		//while (IP.getValue() < code.length) {
		for (int ii = 0; ii < 7; ii++) {
			System.out.println("======     ITERACIÓN " + ii + "	  ======");
			int IpValue = IP.getValue();

			int instruction = code[IpValue];

			int Bbytes = ((instruction & 0xC0) >> 6);
			int Abytes = ((instruction & 0x30) >> 4);
			int operation = (instruction & 0x1F);

			int B = 0;
			int A = 0;

			for (int i = 1; i < Bbytes + 1; i++)
				B = (B << 8) + code[IpValue + i] & 0xFFFFFF;

			for (int i = 1; i < Abytes + 1; i++)
				A = (A << 8) + code[IpValue + Bbytes + i] & 0xFFFFFF;

			IP.setValue(IpValue + (Abytes + Bbytes + 1));

			System.out.println("Abytes: " +String.format("%2s ", Integer.toBinaryString(Abytes & 0x3)));
			System.out.println("A: " +String.format("%24s ", Integer.toBinaryString(A & 0xFFFFFF)));
			System.out.println("Bbytes: "+String.format("%2s ", Integer.toBinaryString(Bbytes & 0x3)));
			System.out.println("B: " +String.format("%24s ", Integer.toBinaryString(B & 0xFFFFFF)));
			System.out.println("Operation: " +String.format("%02X ", operation));
			
			Mnemonic mnemonic = mnemonics.get(operation);
			
			if (mnemonic == null)
			throw new Error("Comando inexistente :/");
			
			mnemonic._execute(Abytes, Bbytes, A, B);
			
			System.out.println("--- Cambios --------------------");
			System.out.println("EFX: " + dataReadHandler(0xF0, 1));
			System.out.println("[4]: " + dataReadHandler(0x410, 3));
			System.out.println("EDX: " + dataReadHandler(0xD0, 1));
			System.out.println("CH: " + dataReadHandler(0xC0, 1));
			System.out.println("AL: " + dataReadHandler(0xA0, 1));
			System.out.println("DS: " + dataReadHandler(0x10, 3));
			System.out.println("====== FIN DE ITERACIÓN " + ii + " ======");
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
	  }
		else if (type == 2)
			throw new Error("Estás haciendo cualquiera flaco."); // Inmediato

		else if (type == 3) {  // TODO: para usar esta funcion en memoria acordarse que literalmente guarda de a 4 bytes
			int segment = (address & 0xFF) << 12; // DS
			int offset = (address & 0xFFFF00) >> 8;
			ram.setValue(segment + offset, value); // Memoria
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
		} if (type == 2) // inmediato
			return value & 0xFFFF; // por las dudas

		// memoria
		int segment = (value & 0xFF) << 12; // DS
		int offset = (value & 0xFFFF00) >> 8;
		return ram.getValue(segment + offset); 
	}

}
