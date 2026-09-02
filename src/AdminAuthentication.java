import java.util.Scanner;

public class AdminAuthentication {

    Scanner sc = new Scanner(System.in);

    private static final String ADMIN_PASSWORD = "admin123";

    public void start() {

        System.out.println("\n----- ADMIN LOGIN -----\n");

        System.out.print("Enter password : ");
        String inputAdminPass = sc.nextLine().trim();

        if(inputAdminPass.equals(ADMIN_PASSWORD)) {
            new Admin();
        }
        else {
            System.out.println("Login Unsuccessful, Wrong Password !");
        }

    }
}
