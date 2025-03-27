package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import utils.Converter;

public class VM {
	final int architecture = 32; // in bits
	final int bytesToAccess = architecture / 8;

	// Components
	RAM ram;
	TableSegments ts;
	Register[] registers;
	Processor processor;

	public VM() {
		ram = new RAM();
		ts = new TableSegments();
		registers = new Register[16];
		processor = new Processor(this);
	}

	public void start(String pathname) {
		// leer archivo

		try {

			if (!pathname.endsWith(".vmx"))
				throw new Error("File not supported");

			byte[] content = Files.readAllBytes(Paths.get(pathname));
			int codeSize = (content[6] << 8) + content[7];
			byte[] code = new byte[codeSize - 8];

			for (int i = 0; i < codeSize - 8; i++)
				code[i] = content[i + 8];

			ram.init(code);

			// leer cabecera

			// meter resto del codigo en el CS (RAM)
			// armar el table segements en base al CS

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
