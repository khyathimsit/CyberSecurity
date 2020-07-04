package secure;
import java.io.*;
public final class PasswordFieldHelper {
  private PasswordFieldHelper() {

  }
  public static String readPassword (String prompt) {
    EraseThread et = new EraseThread(prompt);
      Thread mask = new Thread(et);
      mask.start();

      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      String password = "";

      try {
          password = in.readLine();
      } catch (IOException ioe) {
          ioe.printStackTrace();
      }
      et.stopMasking();
      return password;
   }
   public static boolean passvalidation(String password) {
    String passwd = password;
    String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
    return passwd.matches(pattern);
  }
}