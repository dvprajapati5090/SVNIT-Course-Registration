import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Admin {

    Scanner sc = new Scanner(System.in);

    public Admin() {

        boolean adminMenu = true;

        while (adminMenu) {

            System.out.println("\n\n------ ADMIN INTERFACE ------");
            System.out.println("\n1. Manage Course Catalog");
            System.out.println("2. Manage Student Records");
            System.out.println("3. Assign Professor to Courses");
            System.out.println("4. Handle Complaints");
            System.out.println("5. Logout\n");

            System.out.print("Enter your Choice : ");
            int adminChoice = sc.nextInt();

            switch (adminChoice) {

                case 1:
                    manageCourseCatalog();
                    break;

                case 2:
                    manageStudentRecords();
                    break;

                case 3:
                    assignProfessors();
                    break;

                case 4:
                    handleComplaints();
                    break;

                case 5:
                    adminMenu = false;
                    break;

            }
        }
    }

    public void manageCourseCatalog() {

        boolean managingCourse = true;

        while (managingCourse) {

            System.out.println("\n\n----- COURSE CATALOG -----\n");
            System.out.println("1. View Courses");
            System.out.println("2. Add Course");
            System.out.println("3. Remove Course");
            System.out.println("4. Back to Main Menu");
            System.out.print("\nEnter your Choice : ");
            int courseChoice = sc.nextInt();

            switch (courseChoice) {

                case 1:
                    String viewSql = "SELECT * FROM courses";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(viewSql);
                         ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            System.out.println("-------------------------");
                            System.out.println("Course Code        : " + rs.getString("courseCode"));
                            System.out.println("Course Name        : " + rs.getString("courseName"));
                            System.out.println("Course Semester    : " + rs.getInt("semester"));
                            System.out.println("Course Professor   : " + rs.getString("professorEmail"));
                            System.out.println("Course Credits     : " + rs.getInt("credits"));
                            System.out.println("Course Prerequisites : " + rs.getString("prerequisites"));
                            System.out.println("Course Syllabus    : " + rs.getString("syllabus"));
                            System.out.println("Course Schedule    : " + rs.getString("schedule"));
                            System.out.println("Course Limit       : " + rs.getInt("courseLimit"));
                        }
                    } catch (SQLException e) { e.printStackTrace(); }
                    break;

                case 2:
                    System.out.println("\n\n----- ADD COURSE -----");
                    sc.nextLine();
                    System.out.print("\nEnter Course Code : ");
                    String inputCourseCode = sc.nextLine().toUpperCase().trim();
                    System.out.print("Enter Course Name : ");
                    String inputCourseName = sc.nextLine();
                    System.out.print("Enter Course Semester : ");
                    int inputCourseSemester = sc.nextInt();
                    System.out.print("Enter Course Credits : ");
                    int inputCourseCredits = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Course Prerequisites : ");
                    String inputCoursePrerequisites = sc.nextLine();
                    System.out.print("Enter Course Syllabus : ");
                    String inputCourseSyllabus = sc.nextLine();
                    System.out.print("Enter Course Schedule : ");
                    String inputCourseSchedule = sc.nextLine();
                    System.out.print("Enter Course Student Limit : ");
                    int inputCourseLimit = sc.nextInt();

                    String insertCourseSql = "INSERT INTO courses (courseCode, courseName, semester, schedule, credits, prerequisites, courseLimit, syllabus) VALUES (?,?,?,?,?,?,?,?)";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(insertCourseSql)) {
                        pstmt.setString(1, inputCourseCode);
                        pstmt.setString(2, inputCourseName);
                        pstmt.setInt(3, inputCourseSemester);
                        pstmt.setString(4, inputCourseSchedule);
                        pstmt.setInt(5, inputCourseCredits);
                        pstmt.setString(6, inputCoursePrerequisites);
                        pstmt.setInt(7, inputCourseLimit);
                        pstmt.setString(8, inputCourseSyllabus);
                        pstmt.executeUpdate();
                        System.out.println("\nCourse Added Successfully to Database!!!");
                    } catch (SQLException e) {
                        System.out.println("Database Error.");
                        e.printStackTrace();
                    }
                    break;

                case 3:
                    String viewForRemoveSql = "SELECT * FROM courses";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(viewForRemoveSql);
                         ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            System.out.println("-------------------------");
                            System.out.println("Course Code        : " + rs.getString("courseCode"));
                            System.out.println("Course Name        : " + rs.getString("courseName"));
                            System.out.println("Course Semester    : " + rs.getInt("semester"));
                            System.out.println("Course Professor   : " + rs.getString("professorEmail"));
                            System.out.println("Course Credits     : " + rs.getInt("credits"));
                        }
                    } catch (SQLException e) { e.printStackTrace(); }

                    sc.nextLine();
                    System.out.print("\nEnter Code of Course you want to remove : ");
                    String dropCode = sc.nextLine().toUpperCase().trim();
                    String deleteSql = "DELETE FROM courses WHERE courseCode = ?";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                        pstmt.setString(1, dropCode);
                        if (pstmt.executeUpdate() > 0) {
                            System.out.println("Course Removed Successfully.");
                        } else {
                            System.out.println("Enter correct course code!");
                        }
                    } catch (SQLException e) { e.printStackTrace(); }
                    break;

                case 4:
                    managingCourse = false;
                    break;

                default:
                    break;

            }
        }
    }

    public void manageStudentRecords() {

        boolean managingStudents = true;

        while (managingStudents) {

            System.out.println("\n\n----- MANAGE STUDENTS DATA -----\n");
            System.out.println("1. View Students");
            System.out.println("2. Update Student Details");
            System.out.println("3. Add New Student");
            System.out.println("4. Remove Student");
            System.out.println("5. Back to Main Menu");

            System.out.print("\nEnter your choice : ");
            int choiceStudentRecord = sc.nextInt();

            switch (choiceStudentRecord) {

                case 1:
                    System.out.println("\n\n----- REGISTERED STUDENTS -----\n");
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM students");
                         ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            System.out.println("----------------");
                            System.out.println("Student Name     : " + rs.getString("name"));
                            System.out.println("Student Email    : " + rs.getString("email"));
                            System.out.println("Student Semester : " + rs.getInt("semester"));
                            System.out.println("SGPA : " + rs.getDouble("sgpa") + " , CGPA : " + rs.getDouble("cgpa"));
                            System.out.println("----------------");
                        }
                    } catch (SQLException e) { e.printStackTrace(); }
                    break;

                case 2:
                    System.out.println("\n\n----- REGISTERED STUDENTS -----\n");
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM students");
                         ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            System.out.println("----------------");
                            System.out.println("Student Name     : " + rs.getString("name"));
                            System.out.println("Student Email    : " + rs.getString("email"));
                            System.out.println("Student Semester : " + rs.getInt("semester"));
                            System.out.println("SGPA : " + rs.getDouble("sgpa") + " , CGPA : " + rs.getDouble("cgpa"));
                            System.out.println("----------------");
                        }
                    } catch (SQLException e) { e.printStackTrace(); }

                    sc.nextLine();
                    System.out.print("\nEnter the Email of the Student to update : ");
                    String emailStudent = sc.nextLine().trim();

                    System.out.println("\n1. Update Student Name");
                    System.out.println("2. Update Student Semester");
                    System.out.println("3. Update Student SGPA");
                    System.out.println("4. Update Student CGPA");
                    System.out.println("5. Set New Password");
                    System.out.print("\nEnter your choice : ");
                    int updateChoice = sc.nextInt();
                    sc.nextLine();

                    try (Connection conn = DatabaseConnection.getConnection()) {
                        PreparedStatement pstmt = null;

                        if (updateChoice == 1) {
                            System.out.print("Enter New Name : ");
                            pstmt = conn.prepareStatement("UPDATE students SET name = ? WHERE email = ?");
                            pstmt.setString(1, sc.nextLine());
                        } else if (updateChoice == 2) {
                            System.out.print("Enter New Semester : ");
                            pstmt = conn.prepareStatement("UPDATE students SET semester = ? WHERE email = ?");
                            pstmt.setInt(1, sc.nextInt());
                        } else if (updateChoice == 3) {
                            System.out.print("Enter New SGPA : ");
                            pstmt = conn.prepareStatement("UPDATE students SET sgpa = ? WHERE email = ?");
                            pstmt.setDouble(1, sc.nextDouble());
                        } else if (updateChoice == 4) {
                            System.out.print("Enter New CGPA : ");
                            pstmt = conn.prepareStatement("UPDATE students SET cgpa = ? WHERE email = ?");
                            pstmt.setDouble(1, sc.nextDouble());
                        } else if (updateChoice == 5) {
                            System.out.print("Enter New Password : ");
                            pstmt = conn.prepareStatement("UPDATE students SET password = ? WHERE email = ?");
                            pstmt.setString(1, sc.nextLine());
                        }

                        if (pstmt != null) {
                            pstmt.setString(2, emailStudent);
                            if (pstmt.executeUpdate() > 0) {
                                System.out.println("Update Successful!");
                            } else {
                                System.out.println("Student not found.");
                            }
                        }
                    } catch (SQLException e) { e.printStackTrace(); }
                    break;

                case 3:
                    System.out.println("\n\n----- ADD NEW STUDENT -----\n");
                    sc.nextLine();
                    System.out.print("Enter Student Name : ");
                    String name = sc.nextLine();
                    System.out.print("Enter Student Semester : ");
                    int sem = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Student Email : ");
                    String email = sc.nextLine();
                    System.out.print("Enter Student Password : ");
                    String pass = sc.nextLine();

                    String insertStudentSql = "INSERT INTO students (email, password, name, semester) VALUES (?,?,?,?)";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(insertStudentSql)) {
                        pstmt.setString(1, email);
                        pstmt.setString(2, pass);
                        pstmt.setString(3, name);
                        pstmt.setInt(4, sem);
                        pstmt.executeUpdate();
                        System.out.println("\nStudent Added Successfully!");
                    } catch (SQLException e) { e.printStackTrace(); }
                    break;

                case 4:
                    System.out.println("\n\n----- REMOVE STUDENT -----\n");
                    System.out.println("Registered Student Details :-\n");
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM students");
                         ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            System.out.println("----------------");
                            System.out.println("Student Name     : " + rs.getString("name"));
                            System.out.println("Student Email    : " + rs.getString("email"));
                            System.out.println("Student Semester : " + rs.getInt("semester"));
                            System.out.println("----------------");
                        }
                    } catch (SQLException e) { e.printStackTrace(); }

                    sc.nextLine();
                    System.out.print("\nEnter the Student Email you want to remove : ");
                    String stEmail = sc.nextLine().trim();
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement("DELETE FROM students WHERE email = ?")) {
                        pstmt.setString(1, stEmail);
                        if (pstmt.executeUpdate() > 0) {
                            System.out.println("Student Removed Successfully.");
                        } else {
                            System.out.println("Student not found.");
                        }
                    } catch (SQLException e) { e.printStackTrace(); }
                    break;

                case 5:
                    managingStudents = false;
                    break;

            }
        }
    }

    public void assignProfessors() {

        boolean assigningMenu = true;

        while (assigningMenu) {

            System.out.println("\n\n----- ASSIGN PROFESSOR TO COURSE -----\n");
            System.out.println("1. Assign Professor");
            System.out.println("2. Back to Main Menu");
            System.out.print("\nEnter your Choice : ");
            int assignChoice = sc.nextInt();

            if (assignChoice == 2) {
                assigningMenu = false;
                break;
            }

            System.out.println("\n--- Available Courses ---\n");
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT courseCode, courseName, professorEmail FROM courses");
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String prof = rs.getString("professorEmail");
                    System.out.println("Code : " + rs.getString("courseCode") +
                                       " | Name : " + rs.getString("courseName") +
                                       " | Professor : " + (prof != null ? prof : "* Not Assigned *"));
                }
            } catch (SQLException e) { e.printStackTrace(); }

            sc.nextLine();
            System.out.print("\nEnter the Course Code to assign a Professor : ");
            String code = sc.nextLine().toUpperCase().trim();

            System.out.println("\n--- Available Professors ---\n");
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT email, name FROM professors");
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Email : " + rs.getString("email") + " | Name : " + rs.getString("name"));
                }
            } catch (SQLException e) { e.printStackTrace(); }

            System.out.print("\nEnter the Professor's Email to assign : ");
            String profEmail = sc.nextLine().trim();

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("UPDATE courses SET professorEmail = ? WHERE courseCode = ?")) {
                pstmt.setString(1, profEmail);
                pstmt.setString(2, code);
                if (pstmt.executeUpdate() > 0) {
                    System.out.println("\nProfessor assigned successfully!");
                } else {
                    System.out.println("\nInvalid Course Code.");
                }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void handleComplaints() {

        boolean complaintsMenu = true;

        while (complaintsMenu) {

            System.out.println("\n\n----- COMPLAINT INTERFACE -----\n");
            System.out.println("1. View All Complaints");
            System.out.println("2. View Pending Complaints");
            System.out.println("3. Resolve a Pending Complaint");
            System.out.println("4. Back to Main Menu");
            System.out.print("\nEnter your choice : ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    String allSql = "SELECT id, studentEmail, description, status, resolution FROM complaints";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(allSql);
                         ResultSet rs = pstmt.executeQuery()) {
                        boolean found = false;
                        while (rs.next()) {
                            found = true;
                            System.out.println("------------------");
                            System.out.println("ID          : " + rs.getInt("id"));
                            System.out.println("Student     : " + rs.getString("studentEmail"));
                            System.out.println("Description : " + rs.getString("description"));
                            System.out.println("Status      : " + rs.getString("status"));
                            if ("Resolved".equals(rs.getString("status"))) {
                                System.out.println("Resolution  : " + rs.getString("resolution"));
                            }
                        }
                        if (!found) System.out.println("No complaints found.");
                    } catch (SQLException e) { e.printStackTrace(); }
                    break;

                case 2:
                    String pendingSql = "SELECT id, studentEmail, description FROM complaints WHERE status = 'Pending'";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(pendingSql);
                         ResultSet rs = pstmt.executeQuery()) {
                        boolean found = false;
                        while (rs.next()) {
                            found = true;
                            System.out.println("------------------");
                            System.out.println("ID          : " + rs.getInt("id"));
                            System.out.println("Student     : " + rs.getString("studentEmail"));
                            System.out.println("Description : " + rs.getString("description"));
                        }
                        if (!found) System.out.println("No pending complaints.");
                    } catch (SQLException e) { e.printStackTrace(); }
                    break;

                case 3:
                    String showPendingSql = "SELECT id, studentEmail, description FROM complaints WHERE status = 'Pending'";
                    boolean hasPending = false;
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(showPendingSql);
                         ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            hasPending = true;
                            System.out.println("------------------");
                            System.out.println("ID          : " + rs.getInt("id"));
                            System.out.println("Student     : " + rs.getString("studentEmail"));
                            System.out.println("Description : " + rs.getString("description"));
                        }
                    } catch (SQLException e) { e.printStackTrace(); }

                    if (!hasPending) {
                        System.out.println("No pending complaints to resolve.");
                        break;
                    }

                    System.out.print("\nEnter the ID of the complaint you want to resolve : ");
                    int resolveId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Resolution : ");
                    String resolution = sc.nextLine();

                    String resolveSql = "UPDATE complaints SET status = 'Resolved', resolution = ? WHERE id = ? AND status = 'Pending'";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(resolveSql)) {
                        pstmt.setString(1, resolution);
                        pstmt.setInt(2, resolveId);
                        if (pstmt.executeUpdate() > 0) {
                            System.out.println("\nComplaint Resolved Successfully!");
                        } else {
                            System.out.println("\nInvalid ID or complaint is already resolved.");
                        }
                    } catch (SQLException e) { e.printStackTrace(); }
                    break;

                case 4:
                    complaintsMenu = false;
                    break;

                default:
                    System.out.println("Enter a valid choice!");
                    break;

            }
        }
    }
}
