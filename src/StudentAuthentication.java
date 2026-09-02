import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentAuthentication {

    Scanner sc = new Scanner(System.in);

    public void start() {
        System.out.println("\n\n----- STUDENT LOGIN/SIGN-UP PAGE -----\n");
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
        System.out.println("\n\n----- STUDENT LOGIN -----\n");
        System.out.print("Enter Email-id : ");
        String inputEmail = sc.nextLine().trim();
        System.out.print("Enter Password : ");
        String inputPassword = sc.nextLine().trim();

        String sql = "SELECT * FROM students WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inputEmail);
            pstmt.setString(2, inputPassword);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nLogin Successful!");

                String dbName = rs.getString("name");
                int dbSemester = rs.getInt("semester");

                Student loggedInStudent = new Student(inputPassword, inputEmail, dbName, dbSemester);

                System.out.println("\n\nWelcome, " + loggedInStudent.getName());
                loggedInStudent.showMenu();

            } else {
                System.out.println("Invalid Credentials or User does not exist!");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred connecting to the database.");
            e.printStackTrace();
        }
    }

    public void handleSignup() {

        System.out.println("\n\n----- STUDENT REGISTRATION -----\n");
        System.out.print("Enter Student Name: ");
        String inputName = sc.nextLine();
        System.out.print("Enter Email-id : ");
        String inputEmail = sc.nextLine().trim();
        System.out.print("Enter Password : ");
        String inputPassword = sc.nextLine().trim();
        System.out.print("Enter Semester : ");
        int inputSemester = sc.nextInt();

        sc.nextLine();

        String sql = "INSERT INTO students (email, password, name, semester) VALUES (?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inputEmail);
            pstmt.setString(2, inputPassword);
            pstmt.setString(3, inputName);
            pstmt.setInt(4, inputSemester);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("\nRegistration Successful!");

                Student newStudent = new Student(inputPassword, inputEmail, inputName, inputSemester);
                System.out.println("Welcome, " + newStudent.getName());
                newStudent.showMenu();
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