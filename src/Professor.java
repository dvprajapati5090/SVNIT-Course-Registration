import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Professor implements User {

    Scanner sc = new Scanner(System.in);

    private final String email;
    private final String password;

    private final String name;

    public Professor(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    @Override
    public void showMenu() {

        boolean professorMenu = true;
        int choiceProfessorMenu;

        while(professorMenu) {

            System.out.println("\n----- PROFESSOR MENU -----\n");

            System.out.println("NAME : " + getName());
            System.out.println("1. View Registered Courses");
            System.out.println("2. Manage Courses");
            System.out.println("3. View Registered Students");
            System.out.println("4. Log Out");

            System.out.print("\nEnter your choice : ");
            choiceProfessorMenu = sc.nextInt();

            switch(choiceProfessorMenu) {

                case 1 :
                    viewAssignedCourses();
                    break;

                case 2 :
                    manageCourse();
                    break;

                case 3 :
                    viewRegisteredStudents();
                    break;

                case 4 :
                    professorMenu = false;
                    break;
            }
        }
    }

    public void viewAssignedCourses() {

        System.out.println("\n\n----- COURSES ASSIGNED -----\n");

        String sql = "SELECT courseCode, courseName, credits, schedule, semester FROM courses WHERE professorEmail = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the ? to the logged-in professor's email
            pstmt.setString(1, this.email);

            ResultSet rs = pstmt.executeQuery();
            boolean hasCourses = false;

            while (rs.next()) {
                hasCourses = true;
                System.out.println("---------------------------");
                System.out.println("Course Code : " + rs.getString("courseCode"));
                System.out.println("Course Name : " + rs.getString("courseName"));
                System.out.println("Credits     : " + rs.getInt("credits"));
                System.out.println("Semester    : " + rs.getInt("semester"));
                System.out.println("Schedule    : " + rs.getString("schedule"));
            }

            if (!hasCourses) {
                System.out.println("You have not been assigned to teach any courses yet.");
            }
            System.out.println("---------------------------\n\n");

        } catch (SQLException e) {
            System.out.println("Error fetching assigned courses from the database.");
            e.printStackTrace();
        }
    }

    public void manageCourse() {

        viewAssignedCourses();

        System.out.print("Enter the code of course you want to manage : ");
        String manageCourseCode = sc.next().toUpperCase().trim();

        String checkSql = "SELECT * FROM courses WHERE courseCode = ? AND professorEmail = ?";

        String currentSyllabus = "";
        String currentSchedule = "";
        int currentCredits = 0;
        String currentPrereqs = "";
        int currentLimit = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkSql)) {

            pstmt.setString(1, manageCourseCode);
            pstmt.setString(2, this.email);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                currentSyllabus = rs.getString("syllabus");
                currentSchedule = rs.getString("schedule");
                currentCredits = rs.getInt("credits");
                currentPrereqs = rs.getString("prerequisites");
                currentLimit = rs.getInt("courseLimit");
            } else {
                System.out.println("\nInvalid Course Code or you are not assigned to this course!\n");
                return;
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database.");
            e.printStackTrace();
            return;
        }

        boolean managingCourse = true;

        while (managingCourse) {
            System.out.println("\n--- Managing " + manageCourseCode + " ---");
            System.out.println("1. Update Course Syllabus");
            System.out.println("2. Update Class Timings");
            System.out.println("3. Update Course Credits");
            System.out.println("4. Update Course Prerequisites");
            System.out.println("5. Update Course Enrollment Limits");
            System.out.println("6. Back to Professor Menu");

            System.out.print("\nEnter your Choice : ");
            int manageCourseChoice = sc.nextInt();

            switch (manageCourseChoice) {

                case 1:
                    System.out.println("\nCurrent Syllabus : " + currentSyllabus);
                    sc.nextLine();
                    System.out.print("Enter New Syllabus : ");
                    String newSyllabus = sc.nextLine();

                    if (updateCourseFieldString("syllabus", newSyllabus, manageCourseCode)) {
                        currentSyllabus = newSyllabus;
                        System.out.println("\nCourse Syllabus Updated Successfully in Database.\n");
                    }
                    break;

                case 2:
                    System.out.println("\nCurrent Class Timings : " + currentSchedule);
                    sc.nextLine();
                    System.out.print("Enter New Class Timings : ");
                    String newSchedule = sc.nextLine();

                    if (updateCourseFieldString("schedule", newSchedule, manageCourseCode)) {
                        currentSchedule = newSchedule;
                        System.out.println("\nClass Timings Updated Successfully in Database.\n");
                    }
                    break;

                case 3:
                    System.out.println("\nCurrent Course Credits : " + currentCredits);
                    System.out.print("Enter New Course Credits : ");
                    int newCredits = sc.nextInt();

                    if (updateCourseFieldInt("credits", newCredits, manageCourseCode)) {
                        currentCredits = newCredits;
                        System.out.println("\nCourse Credits Updated Successfully in Database.\n");
                    }
                    break;

                case 4:
                    System.out.println("\nCurrent Prerequisites : " + currentPrereqs);
                    sc.nextLine();
                    System.out.print("Enter New Prerequisites : ");
                    String newPrereqs = sc.nextLine();

                    if (updateCourseFieldString("prerequisites", newPrereqs, manageCourseCode)) {
                        currentPrereqs = newPrereqs;
                        System.out.println("\nCourse Prerequisites Updated Successfully in Database.\n");
                    }
                    break;

                case 5:
                    System.out.println("\nCurrent Enrollment Limits : " + currentLimit);
                    System.out.print("Enter New Limits : ");
                    int newLimit = sc.nextInt();

                    if (updateCourseFieldInt("courseLimit", newLimit, manageCourseCode)) {
                        currentLimit = newLimit;
                        System.out.println("\nCourse Enrollment Limits Updated Successfully in Database.\n");
                    }
                    break;

                case 6:
                    managingCourse = false;
                    break;

                default:
                    System.out.println("Enter Valid Choice !");
                    break;
            }
        }
    }


    private boolean updateCourseFieldString(String columnName, String newValue, String courseCode) {

        String sql = "UPDATE courses SET " + columnName + " = ? WHERE courseCode = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setString(2, courseCode);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Database error during update.");
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateCourseFieldInt(String columnName, int newValue, String courseCode) {
        String sql = "UPDATE courses SET " + columnName + " = ? WHERE courseCode = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newValue);
            pstmt.setString(2, courseCode);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Database error during update.");
            e.printStackTrace();
            return false;
        }
    }

    public void viewRegisteredStudents() {

        String sql = "SELECT c.courseCode, s.name AS studentName, s.sgpa AS studentSgpa, s.cgpa AS studentCgpa " +
                     "FROM student_course_records r " +
                     "JOIN courses c ON r.courseCode = c.courseCode " +
                     "JOIN students s ON r.studentEmail = s.email " +
                     "WHERE c.professorEmail = ? AND r.status = 'REGISTERED' " +
                     "ORDER BY c.courseCode, s.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.email);

            ResultSet rs = pstmt.executeQuery();
            boolean hasStudents = false;

            String currentCourse = "";

            while (rs.next()) {
                hasStudents = true;
                String dbCourseCode = rs.getString("courseCode");

                if (!dbCourseCode.equals(currentCourse)) {
                    System.out.println("\n----- COURSE CODE : " + dbCourseCode + " -----");
                    currentCourse = dbCourseCode;
                }

                System.out.println("Student Name : " + rs.getString("studentName"));
                System.out.println("Student SGPA : " + rs.getString("studentSgpa"));
                System.out.println("Student CGPA : " + rs.getString("studentCgpa"));
            }

            if (!hasStudents) {
                System.out.println("No students are currently registered for your courses.");
            }
            System.out.println("\n---------------------------\n");

        } catch (SQLException e) {
            System.out.println("Error fetching enrolled students from the database.");
            e.printStackTrace();
        }
    }
}