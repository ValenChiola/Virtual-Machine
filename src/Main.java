import models.components.VM;

public class Main {
	public static void main(String[] args) {
		try {
			new VM().start(args[0]);
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
