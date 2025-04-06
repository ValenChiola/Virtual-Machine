import models.components.VM;
import utils.log.Log;

public class Main {

	public static void main(String[] args) {
		try {
			parseArgs(args);
			new VM().start(args[0]);
		} catch (Exception e) {
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
