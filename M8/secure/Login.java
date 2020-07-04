package secure;
import java.util.*;
import java.io.*;
class User {
    public String password;
    public String hint;
    public String answer;
    User(String password, String hint, String answer) {
        this.password = password;
        this.hint = hint;
        this.answer = answer;
    }
    public String getpassword() {
        return password;
    }
    public String gethint() {
        return hint;
    }
    public String getanswer() {
        return answer;
    }
}

public class LoginHelper {
    static Map<String, User> hash = new HashMap<String, User>();
    static Scanner sc = new Scanner(System.in);
    private LoginHelper() {

    }
    public static void main(String[] args) {
        System.out.print("Do you want to LogIn Or SignUp??");
        String enter = sc.nextLine();
        if ("LogIn".equals(enter)) {
            try{
                File userfile = new File("userdata.txt");
                Scanner filescan = new Scanner(userfile);
                while (filescan.hasNextLine()) {
                    String[] details = filescan.nextLine().split(",");
                    User user = new User(details[1], details[2], details[3]);
                    hash.put(details[0], user);
                }
            } catch (Exception io) {
                System.out.println("File doesn't exist");
            }
            logIn();
        } else {
            signUp();
        }   
    }

    public static void signUp() {
        String details = "";
        System.out.print("Enter your name: ");
        String username = sc.nextLine().toLowerCase(Locale.ENGLISH);
        details = details + username;
        System.out.println();
        String password = "";
        while(true) {
            System.out.print("Enter your password: ");
            password = PasswordFieldHelper.readPassword("");
            if (PasswordFieldHelper.passvalidation(password)) {
                break;
            } else {
                System.out.println("Not a valid password please enter again");
            }    
        }
        
        while(true) {
            System.out.println("Please re-enter your password: ");
            String confirm = PasswordFieldHelper.readPassword("");
            if (password.equals(confirm)) {
                break;
            } else {
                System.out.println("Entered different passwords...");   
            }
        }
        details = details + "," + password;
        System.out.println();
        System.out.println("Please enter a security question: ");
        String hint = sc.nextLine().toLowerCase(Locale.ENGLISH);
        details = details + "," + hint;
        System.out.println();
        System.out.println("Enter the answer: ");
        String answer = sc.nextLine().toLowerCase(Locale.ENGLISH);
        details = details + "," + answer + "\n";
        appendStrToFile("userdata.txt", details);
        System.out.println("Stored Your details....");
    }

    public static void appendStrToFile(String fileName, String str) {
        try {
            BufferedWriter out = new BufferedWriter(
                new FileWriter(fileName, true));
            out.write(str);
            out.close();
        } catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }

    public static void logIn() {
        int guess = 0;
        System.out.print("UserName: ");
        String username = sc.nextLine().toLowerCase(Locale.ENGLISH);
        System.out.println();
        while(true) {
            System.out.print("Password: ");
            String password = PasswordFieldHelper.readPassword("");
            if(!hash.containsKey(username)) {
                System.out.println("User doesn't exist");
                logIn();
            } else {
                if (hash.get(username).getpassword().equals(password)) {
                    System.out.println("USER AUTHENTICATED: ACCESS GRANTED");
                    break;
                } else {
                    if (guess > 2) {
                        System.out.println("USER AUTHENTICATION FAILED: ACCESS DENIED");
                        return;
                    }
                    guess++;
                    System.out.println("Select a choice: ");
                    System.out.println("1. Did you forget your password? ");
                    System.out.println("2. Would you like to re-enter the password: ");
                    System.out.println("Enter your choice: ");
                    String choice = sc.nextLine();
                    if ("1".equals(choice)) {
                        continue;
                    } else {
                        if (!forgotpassword(username)) {
                            System.out.println("USER AUTHENTICATION FAILED: ACCESS DENIED");
                            return;
                        } else {
                            System.out.println("Update successfull!!!");
                            return;
                        }
                    }
                }
            }
        }
    }

    public static boolean forgotpassword(String name) {
        System.out.println(hash.get(name).gethint());
        System.out.print("answer: ");
        String answer = sc.nextLine().toLowerCase(Locale.ENGLISH);
        if (answer.equals(hash.get(name).getanswer())) {
            System.out.print("Please Enter New password: ");
            String newpassword = PasswordFieldHelper.readPassword("");
            System.out.println();
            while (true) {
                System.out.print("Reenter your password: ");
                String confirmpass = PasswordFieldHelper.readPassword("");
                if (newpassword.equals(confirmpass)) {
                    updatingfilecontent("userdata.txt", name, newpassword);
                    break;
                } else {
                    System.out.println("Passwords Mismatch!!!");
                }
            }
            return true;
        } else {
            System.out.println("Failed to update password");
            return false;
        }
    }
    public static void updatingfilecontent(String filename, String name, String newpassword) {
        try {
            FileInputStream fstream = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strline;
            StringBuilder fileContent = new StringBuilder();
            while ((strline = br.readLine()) != null) {
                String[] tokens = strline.split(",");
                if (tokens[0].equals(name)) {
                    tokens[1] = newpassword;
                    String updatedline = tokens[0] + "," + tokens[1] + "," + tokens[2] + "," + tokens[3];
                    fileContent.append(updatedline + "\n");
                } else {
                    fileContent.append(strline + "\n");
                }
            }
            FileWriter fstreamWrite = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstreamWrite);
            out.write(fileContent.toString());
            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}