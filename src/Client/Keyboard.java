package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Keyboard {

	private static InputStreamReader input = new InputStreamReader(System.in);
	private static BufferedReader br = new BufferedReader(input);

	public static String StringReader() {
		String app = null;

		try {
			app = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return app;
	}

	public static String StringReader(String msg) {
		String app = null;
		System.out.println(msg);
		try {
			app = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return app;
	}

	public static int IntReader() {
		String app = null;
		int number = 0;
		int i = 0;
		while (i == 0) {
			try {
				try {
					app = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				number = Integer.parseInt(app);
				i = 1;
			} catch (NumberFormatException e) {
				System.out.println("Deve essere inserito un numero\nRiprova: ");
			}
		}
		return number;
	}

	public static int IntReader(String msg) {
		String app = null;
		int number = 0;
		System.out.println(msg);
		int i = 0;
		while (i == 0) {
			try {
				try {
					app = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				number = Integer.parseInt(app);
				i = 1;
			} catch (NumberFormatException e) {
				System.out.println("Deve essere inserito un numero\nRiprova: ");
			}
		}
		return number;
	}

	public static double DoubleReader() {
		String app = null;
		double number = 0;
		int i = 0;
		while (i == 0) {
			try {
				try {
					app = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				number = Double.parseDouble(app);
				i = 1;
			} catch (NumberFormatException e) {
				System.out.println("Deve essere inserito un numero\nRiprova");
			}
		}
		return number;
	}

	public static double DoubleReader(String msg) {
		String app = null;
		double number = 0;
		int i = 0;
		System.out.println(msg);
		while (i == 0) {
			try {
				try {
					app = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				number = Double.parseDouble(app);
				i = 1;
			} catch (NumberFormatException e) {
				System.out.println("Deve essere inserito un numero\nRiprova");
			}
		}
		return number;
	}

}
