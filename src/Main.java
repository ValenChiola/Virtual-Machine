import models.components.VM;
import utils.log.Log;

public class Main {

	public static void main(String[] args) {
		if (args.length >= 2) Log.setLevel(args[1]);
		
		new VM().start(args[0]);
	}
}
