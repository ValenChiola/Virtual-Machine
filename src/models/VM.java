package models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class VM {
	final int architecture = 32; // in bits
	final int bytesToAccess = architecture / 8;

	// Components
	Ram ram;
	TableSegments ts;
	Processor processor;
	Map<Integer, Register> registers;
	Map<Integer, Mnemonic> mnemonics;

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

			ram.init(code);
			ts.init(code);

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

			execute(code);

			for (byte element : code) {
				System.out.print(String.format("%02X ", element));
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void execute(byte[] code) {
		Register IP = registers.get(5);
		while (IP.getValue() < code.length) {
			int IpValue = IP.getValue();

			int instruction = code[IpValue];

			int aux = 0xC0 >> 2;

			int Bbytes = ((instruction & 0xC0) >> 6);
			int Abytes = ((instruction & 0x30) >> 4);
			int operation = (instruction & 0x1F);

			int B = 0;
			int A = 0;

			for (int i = 1; i < Bbytes + 1; i++)
				B = (B << 8) + code[IpValue + i];

			for (int i = 1; i < Abytes + 1; i++)
				A = (A << 8) + code[IpValue + Bbytes + i];

			IP.setValue(IpValue + (Abytes + Bbytes + 1));

			System.out.println("-------------------------------------");
			System.out.println(String.format("%8s ", Integer.toBinaryString(aux)));// .replace(' ', '0'));
			System.out.println(String.format("%8s ", Integer.toBinaryString(Bbytes)));// .replace(' ', '0'));
			System.out.println(String.format("%8s ", Integer.toBinaryString(Abytes)));// .replace(' ', '0'));
			System.out.println(String.format("%8s ", Integer.toBinaryString(A)));// .replace(' ', '0'));
			System.out.println(String.format("%8s ", Integer.toBinaryString(B)));// .replace(' ', '0'));
			System.out.println(String.format("%8s ", Integer.toBinaryString(operation)));// .replace(' ', '0'));
			System.out.println(String.format("%8s ", Integer.toBinaryString(instruction)));// .replace(' ',
																							// '0'));
			System.out.println("-------------------------------------");

			Mnemonic mnemonic = mnemonics.get(operation);

			if (mnemonic != null)
				mnemonic._execute(Abytes, Bbytes, A, B);

		}
	}

	public void dataWriteHandler(int address, int value, int type) {
		if (type == 0)
			throw new Error("Malardo"); // nada

		else if (type == 1)
			registers.get(address).setValue(value); // Registro

		else if (type == 2)
			throw new Error("Malardo"); // Inmediato

		else if (type == 3)
			ram.setDSValue(address, value); // Memoria
	}

	public int dataReadHandler(int value, int type) {
		if (type == 0) // nada
			throw new Error("Nazi mal");

		if (type == 1) // registro
			return registers.get(value).getValue();

		if (type == 2) // inmediato
			return value;

		// memoria
		return ram.getDSValue(value);

	}

}
