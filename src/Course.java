public class Course {

    private final String courseCode;
    private String courseName;
    private int semester;
    private Professor professorName;
    private String schedule;
    private String prerequisites;
    private int courseLimit;
    private String syllabus;
    private int credits;

    public Course(String courseCode, String courseName, int semester, String schedule, int credits, String prerequisites, int courseLimit, String syllabus) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.semester = semester;
        this.schedule = schedule;
        this.credits = credits;
        this.syllabus = syllabus;
        this.courseLimit = courseLimit;
        this.prerequisites = prerequisites;
    }

    public void getCourse() {
        System.out.println("Course Code : " + getCourseCode());
        System.out.println("Course Name : " + getCourseName());
        System.out.println("Course Semester : " + getSemester());
        if(this.professorName != null) {
            System.out.println("Course Professor : " + getProfessorName());
        }
        System.out.println("Course Credits : " + getCredits());
        System.out.println("Course Prerequisites : " + getPrerequisites());
        System.out.println("Course Syllabus : " + getSyllabus());
        System.out.println("Course Schedule : " + getSchedule());
        System.out.println("Course Limit : " + getCourseLimit());
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getProfessorName() {
        return professorName.getName();
    }

    public void setProfessorName(Professor professorName) {
        this.professorName = professorName;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public int getCourseLimit() {
        return courseLimit;
    }

    public void setCourseLimit(int courseLimit) {
        this.courseLimit = courseLimit;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }
}
