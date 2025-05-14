import models.components.Processor;
import models.components.Ram;
import models.components.TableSegments;
import models.components.VM;
import utils.ArgsParser;
import utils.log.Log;

public class Main {

	public static void main(String[] args) {

		try {

			ArgsParser.build(args);

			Log.debug(ArgsParser.getInstance().toString());

			new VM()
					.ram(new Ram(ArgsParser.getMemory()))
					.processor(new Processor())
					.ts(new TableSegments())
					.build()
					.start();

		} catch (Exception e) {
			if (ArgsParser.getLogLevel().equals("debug"))
				e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

}
