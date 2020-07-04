import java.io.*;
public class PasswordField{

	public static String readPassword(String prompt){
		EraseThread et = new EraseThread(prompt);
		Thread mask = new Thread(et);
		mask.start();

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String password = " ";

		try{
			password = in.readLine();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		et.stopMasking();
		return password;
	}

}