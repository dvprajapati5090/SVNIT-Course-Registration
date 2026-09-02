import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProfessorAuthentication {

    Scanner sc = new Scanner(System.in);

    public void start() {
        System.out.println("\n\n----- PROFESSOR LOGIN/SIGN-UP PAGE -----\n");
        System.out.println("1. Login");
        System.out.println("2. SignUp");
        System.out.println("3. Back to Previous Page");
        System.out.print("\nEnter your choice : ");
        int choice = sc.nextInt();
        sc.nextLine(); // Clear buffer

        if (choice == 1) {
            handleLogin();
        }
        else if(choice == 2) {
            handleSignup();
        }
        else {
            return;
        }
    }

    public void handleLogin() {
        System.out.println("\n\n----- PROFESSOR LOGIN -----\n");
        System.out.print("Enter Email-id : ");
        String inputEmail = sc.nextLine();
        System.out.print("Enter Password : ");
        String inputPassword = sc.nextLine();

        String sql = "SELECT * FROM professors WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inputEmail);
            pstmt.setString(2, inputPassword);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nLogin Successful!\n");

                String dbName = rs.getString("name");
                String dbEmail = rs.getString("email");
                String dbPassword = rs.getString("password");



                Professor loggedInProfessor = new Professor(dbEmail,dbPassword,dbName);
                System.out.println("Welcome, " + loggedInProfessor.getName());

                loggedInProfessor.showMenu();

            } else {
                System.out.println("Invalid Credentials or User does not exist!");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred connecting to the database.");
            e.printStackTrace();
        }
    }

    public void handleSignup() {

        System.out.println("\n\n----- PROFESSOR REGISTRATION -----\n");
        System.out.print("Enter Professor Name : ");
        String inputName = sc.nextLine();
        System.out.print("Enter Email-id : ");
        String inputEmail = sc.nextLine().trim();
        System.out.print("Enter Password : ");
        String inputPassword = sc.nextLine().trim();

        String sql = "INSERT INTO professors (email, password, name) VALUES (?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inputEmail);
            pstmt.setString(2, inputPassword);
            pstmt.setString(3, inputName);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("\nRegistration Successful!\n");

                Professor newProfessor = new Professor(inputEmail, inputPassword, inputName);
                System.out.println("Welcome, " + newProfessor.getName());
                newProfessor.showMenu();
            }
            else {
                System.out.println("Registration Failed!");
            }

        } catch (SQLException e) {

            if(e.getErrorCode() == 1062) { // 1062 is the MySQL error code for Duplicate Entry
                System.out.println("\nError: An account with that Email-id already exists!");
            } else {
                System.out.println("An error occurred connecting to the database.");
                e.printStackTrace();
            }
        }
    }

}