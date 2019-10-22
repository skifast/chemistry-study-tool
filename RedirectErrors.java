import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Scanner;


/*-----NOTE: THIS CLASS IS BORROWED FROM THE INTERNET-------*/
public class RedirectErrors {
	
	public static void main(String[] args) throws Exception {
		System.err.println("This goes to the console");
		PrintStream console = System.err;

		File file = new File("err.txt");
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setErr(ps);

		System.err.println("This goes to err.txt");

		try {
			throw new Exception("Exception goes to err.txt too");
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.setErr(console);
		System.err.println("This also goes to the console");
	}
	
	
}
