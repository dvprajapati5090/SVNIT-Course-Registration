import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Student implements User{

    Scanner sc = new Scanner(System.in);
    private final String email;
    private String password;

    private String name;
    private int semester;
    private Double sgpa;
    private Double cgpa;

    ArrayList<Course> coursesRegistered = new ArrayList<>();
    ArrayList<Complaint> complaintsRegistered = new ArrayList<>();
    ArrayList<Course> coursesCompleted = new ArrayList<>();

    public Student(String password, String email, String name, int semester) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.semester = semester;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Double getSgpa() {
        return sgpa;
    }

    public void setSgpa(Double sgpa) {
        this.sgpa = sgpa;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public void getStudent() {
        System.out.println("Student Name : " + getName());
        System.out.println("Student Email : " + getEmail());
        System.out.println("Student Semester : " + getSemester());
        System.out.println("Student SGPA : " + getSgpa() + " , CGPA : " + getCgpa());
    }

    @Override
    public void showMenu() {

        boolean studentMenu = true;
        int choiceStudentMenu;

        while(studentMenu) {

            System.out.println("\n\n----- STUDENT MENU -----\n");

            System.out.println("NAME : " + getName()  + " | Semester : " + getSemester());
            System.out.println("1. View Available Courses");
            System.out.println("2. Register Courses");
            System.out.println("3. View Schedule");
            System.out.println("4. Track Academic Progress");
            System.out.println("5. View Completed Courses");
            System.out.println("6. Drop Courses");
            System.out.println("7. Submit Complaints");
            System.out.println("8. View Complaints");
            System.out.println("9. Back");

            System.out.print("\nEnter your choice : ");
            choiceStudentMenu = sc.nextInt();

            switch(choiceStudentMenu) {

                case 1 :
                    viewCourses();
                    break;

                case 2 :
                    registerCourse();
                    break;

                case 3 :
                    viewSchedule();
                    break;

                case 4 :
                    trackAcademic();
                    break;

                case 5 :
                    viewCompletedCourses();
                    break;

                case 6 :
                    dropCourse();
                    break;

                case 7 :
                    submitComplaint();
                    break;

                case 8 :
                    viewComplaint();
                    break;

                case 9 :
                    studentMenu = false;
                    break;
            }
        }


    }

    public void viewCourses() {

        System.out.println("\n\n COURSES AVAILABLE \n");

        String sql = "SELECT c.courseCode, c.courseName, c.credits, c.schedule, c.prerequisites, p.name AS professorName " +
                "FROM courses c " +
                "LEFT JOIN professors p ON c.professorEmail = p.email " +
                "WHERE c.semester = ? " +  // <-- Added a space right after the ?
                "AND c.courseCode NOT IN (" + // <-- Added the c. alias and a space before AND
                "    SELECT courseCode FROM student_course_records WHERE studentEmail = ?" +
                ")";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the '?' to the student's current semester
            pstmt.setInt(1, this.semester);
            pstmt.setString(2, this.email);

            ResultSet rs = pstmt.executeQuery();
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("---------------------------");
                System.out.println("Course Code : " + rs.getString("courseCode"));
                System.out.println("Course Name : " + rs.getString("courseName"));
                System.out.println("Credits     : " + rs.getInt("credits"));
                System.out.println("Professor   : " + rs.getString("professorName"));
                System.out.println("Schedule    : " + rs.getString("schedule"));
                System.out.println("Prereqs     : " + rs.getString("prerequisites"));
            }

            if (!found) {
                System.out.println("No courses available for your semester right now.");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching courses from database.");
            e.printStackTrace();
        }

        System.out.println("---------------------------");
    }

    public void registerCourse() {

        viewCourses();

        System.out.print("Enter the Course Code you want to register : ");
        String courseCodeRegister = sc.next().toUpperCase().trim();

        String checkSql = "SELECT credits FROM courses WHERE courseCode = ? AND semester = ?";

        String sumSql = "SELECT SUM(c.credits) AS totalCredits " +
                        "FROM student_course_records r " +
                        "JOIN courses c ON r.courseCode = c.courseCode " +
                        "WHERE r.studentEmail = ? AND r.status = 'REGISTERED'";

        String insertSql = "INSERT INTO student_course_records (studentEmail, courseCode, status) VALUES (?, ?, 'REGISTERED')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, courseCodeRegister);
            checkStmt.setInt(2, this.semester);
            ResultSet rsCheck = checkStmt.executeQuery();

            if (rsCheck.next()) {
                int newCourseCredits = rsCheck.getInt("credits");

                try (PreparedStatement sumStmt = conn.prepareStatement(sumSql)) {
                    sumStmt.setString(1, this.email);
                    ResultSet rsSum = sumStmt.executeQuery();

                    int currentCredits = 0;
                    if (rsSum.next()) {
                        currentCredits = rsSum.getInt("totalCredits");
                    }

                    if ((currentCredits + newCourseCredits) > 20) {
                        System.out.println("\nREGISTRATION FAILED: Credit Limit Exceeded!");
                        System.out.println("Current Credits : " + currentCredits);
                        System.out.println("Course Credits  : " + newCourseCredits);
                        System.out.println("Maximum Allowed : 20");
                        return;
                    }

                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, this.email);
                        insertStmt.setString(2, courseCodeRegister);

                        int rowsInserted = insertStmt.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("\nCourse Registered Successfully !!!");
                        }

                    } catch (SQLException e) {
                        if (e.getErrorCode() == 1062) {
                            System.out.println("\nYou are already registered for this course!");
                        } else {
                            System.out.println("\nError saving registration to database.");
                            e.printStackTrace();
                        }
                    }
                }

            } else {
                System.out.println("\nEnter a Correct Course Code for your current semester!");
            }

        } catch (SQLException e) {
            System.out.println("Error communicating with the database.");
            e.printStackTrace();
        }
    }


    public void viewSchedule() {
        System.out.println("\n\n----- VIEW SCHEDULE -----\n");

        String sql = "SELECT c.courseCode, c.courseName, c.schedule, p.name AS professorName " +
                "FROM student_course_records r " +
                "JOIN courses c ON r.courseCode = c.courseCode " +
                "LEFT JOIN professors p ON c.professorEmail = p.email " +
                "WHERE r.studentEmail = ? AND r.status = 'REGISTERED'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.email);

            ResultSet rs = pstmt.executeQuery();
            boolean hasClasses = false;

            while (rs.next()) {
                hasClasses = true;
                System.out.println("---------------------------");
                System.out.println("Day/Time : " + rs.getString("schedule"));
                System.out.println("Course   : " + rs.getString("courseCode") + " - " + rs.getString("courseName"));
                System.out.println("Professor: " + rs.getString("professorName"));
            }

            if (!hasClasses) {
                System.out.println("You have not registered for any courses yet.");
            }
            System.out.println("---------------------------\n");

        } catch (SQLException e) {
            System.out.println("Error fetching schedule from database.");
            e.printStackTrace();
        }
    }

    public void trackAcademic() {
        System.out.println("\n\n----- ACADEMIC PROGRESS -----\n");

        String sql = "SELECT sgpa, cgpa FROM students WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.email);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                double currentSgpa = rs.getDouble("sgpa");
                double currentCgpa = rs.getDouble("cgpa");

                if (currentSgpa == 0.0 && currentCgpa == 0.0) {
                    System.out.println("Grades have not been assigned yet for your courses.");
                    System.out.println("SGPA : 0.0");
                    System.out.println("CGPA : 0.0");
                } else {
                    System.out.println("Current SGPA : " + currentSgpa);
                    System.out.println("Current CGPA : " + currentCgpa);
                }
            } else {
                System.out.println("Could not find academic records for your account.");
            }

            System.out.println("\n-----------------------------");

        } catch (SQLException e) {
            System.out.println("Error fetching academic progress from the database.");
            e.printStackTrace();
        }
    }

    public void viewCompletedCourses() {
        System.out.println("\n\n----- COMPLETED COURSES -----\n");

        String sql = "SELECT c.courseCode, c.courseName, c.credits, c.semester " +
                "FROM student_course_records r " +
                "JOIN courses c ON r.courseCode = c.courseCode " +
                "WHERE r.studentEmail = ? AND r.status = 'COMPLETED'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.email);

            ResultSet rs = pstmt.executeQuery();
            boolean hasCompleted = false;

            while (rs.next()) {
                hasCompleted = true;
                System.out.println("---------------------------");
                System.out.println("Course Code : " + rs.getString("courseCode"));
                System.out.println("Course Name : " + rs.getString("courseName"));
                System.out.println("Credits     : " + rs.getInt("credits"));
                System.out.println("Semester    : " + rs.getInt("semester"));
            }

            if (!hasCompleted) {
                System.out.println("You have not completed any courses yet.");
            }
            System.out.println("---------------------------\n");

        } catch (SQLException e) {
            System.out.println("Error fetching completed courses from the database.");
            e.printStackTrace();
        }
    }

    public void dropCourse() {
        System.out.println("\n\n----- DROP A COURSE -----");

        viewSchedule();

        System.out.print("Enter the Course Code you want to drop : ");
        String courseCodeToDrop = sc.next().toUpperCase().trim();

        String sql = "DELETE FROM student_course_records " +
                "WHERE studentEmail = ? AND courseCode = ? AND status = 'REGISTERED'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.email);
            pstmt.setString(2, courseCodeToDrop);

            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("\nSuccessfully dropped course: " + courseCodeToDrop);
            } else {
                System.out.println("\nDrop failed. You are not currently registered for " + courseCodeToDrop + ".");
            }

        } catch (SQLException e) {
            System.out.println("Error removing the course from the database.");
            e.printStackTrace();
        }
    }

    public void submitComplaint() {

        System.out.println("----- REGISTER COMPLAINT -----");

        sc.nextLine();
        System.out.print("\nEnter Complaint Description : ");
        String complaintDescription = sc.nextLine();

        String sql = "INSERT INTO complaints (studentEmail, description, status) VALUES (?, ?, 'Pending')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.email);
            pstmt.setString(2, complaintDescription);
            pstmt.executeUpdate();
            System.out.println("\nComplaint submitted successfully.");

        } catch (SQLException e) {
            System.out.println("Error submitting complaint to database.");
            e.printStackTrace();
        }
    }

    public void viewComplaint() {

        System.out.println("\n----- YOUR COMPLAINTS -----");

        String sql = "SELECT description, status, resolution FROM complaints WHERE studentEmail = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.email);
            ResultSet rs = pstmt.executeQuery();
            boolean hasComplaints = false;

            while (rs.next()) {
                hasComplaints = true;
                System.out.println("------------------");
                System.out.println("Description : " + rs.getString("description"));
                System.out.println("Status      : " + rs.getString("status"));
                if (rs.getString("status").equals("Resolved")) {
                    System.out.println("Resolution  : " + rs.getString("resolution"));
                }
            }

            if (!hasComplaints) {
                System.out.println("You have not submitted any complaints yet.");
            }
            System.out.println("------------------");

        } catch (SQLException e) {
            System.out.println("Error fetching complaints from database.");
            e.printStackTrace();
        }
    }


}
