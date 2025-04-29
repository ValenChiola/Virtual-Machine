import models.components.Ram;
import models.components.VM;
import utils.log.Log;

public class Main {

	public static void main(String[] args) {

		try {
			parseArgs(args);

			new VM(args[0])
					.ram(new Ram(16384))
					.build()
					.start();

			// new VM().start(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	static void parseArgs(String[] args) throws Exception {
		if (args.length == 0)
			throw new Exception("File not found!");

		if (args.length >= 2)
			Log.setLevel(args[1]);
	}
}
