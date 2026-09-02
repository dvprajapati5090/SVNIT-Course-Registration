# SVNIT Course Registration System

A **Java console-based Course Registration System** built as an OOP assignment for SVNIT.
The system supports three types of users — **Students**, **Professors**, and **Administrators** —
each with their own role-specific features, backed by a **MySQL database** via **JDBC**.

---

## Tech Stack

| Technology   | Details                          |
|--------------|----------------------------------|
| Language     | Java                             |
| Database     | MySQL                            |
| Connectivity | JDBC (mysql-connector-j-9.6.0.jar) |
| IDE          | IntelliJ IDEA                    |

---

## Features

### Student
- Sign up / Login (stored in MySQL)
- View available courses for their semester
- Register for courses (with a credit limit of 20 credits)
- View class schedule
- Track Academic Progress (SGPA / CGPA)
- View completed courses
- Drop a registered course
- Submit and view complaints

### Professor
- Sign up / Login (stored in MySQL)
- View assigned courses
- Manage course details (syllabus, schedule, credits, prerequisites, enrollment limit)
- View registered students with their academic records

### Admin
- Password-protected login
- Manage Course Catalog (Add / View / Remove courses)
- Manage Student Records (View / Add / Update / Remove)
- Assign Professors to Courses
- Handle Complaints (View all / View pending / Resolve)

---

## Database Schema

Database name: `svnit_registration`

```sql
CREATE TABLE students (
    email     VARCHAR(255) PRIMARY KEY,
    password  VARCHAR(255),
    name      VARCHAR(255),
    semester  INT,
    sgpa      DOUBLE DEFAULT 0.0,
    cgpa      DOUBLE DEFAULT 0.0
);

CREATE TABLE professors (
    email     VARCHAR(255) PRIMARY KEY,
    password  VARCHAR(255),
    name      VARCHAR(255)
);

CREATE TABLE courses (
    courseCode      VARCHAR(50) PRIMARY KEY,
    courseName      VARCHAR(255),
    semester        INT,
    schedule        VARCHAR(255),
    credits         INT,
    prerequisites   VARCHAR(255),
    courseLimit     INT,
    syllabus        TEXT,
    professorEmail  VARCHAR(255),
    FOREIGN KEY (professorEmail) REFERENCES professors(email)
);

CREATE TABLE student_course_records (
    studentEmail  VARCHAR(255),
    courseCode    VARCHAR(50),
    status        ENUM('REGISTERED', 'COMPLETED') DEFAULT 'REGISTERED',
    PRIMARY KEY (studentEmail, courseCode),
    FOREIGN KEY (studentEmail) REFERENCES students(email) ON DELETE CASCADE,
    FOREIGN KEY (courseCode)   REFERENCES courses(courseCode) ON DELETE CASCADE
);

CREATE TABLE complaints (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    studentEmail  VARCHAR(255) NOT NULL,
    description   TEXT NOT NULL,
    status        VARCHAR(20) DEFAULT 'Pending',
    resolution    TEXT DEFAULT NULL,
    FOREIGN KEY (studentEmail) REFERENCES students(email) ON DELETE CASCADE
);
```

---

## Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/dvprajapati5090/SVNIT-Course-Registration.git
cd SVNIT-Course-Registration
```

### 2. Set up MySQL Database
- Open MySQL Workbench (or any MySQL client)
- Create a new database (you can name it anything, e.g., `svnit_registration`)
- Run all the SQL statements from the **Database Schema** section above to create the tables

### 3. Configure Database Credentials
Edit `src/DatabaseConnection.java`:
```java
// Change "svnit_registration" to whatever name you gave your database in Step 2
private static final String URL = "jdbc:mysql://localhost:3306/svnit_registration";

// Change "root" to your MySQL username
private static final String USER = "root";

// Change "root" to your MySQL password
private static final String PASSWORD = "root";
```

### 4. Add JDBC Driver
- The `Library/mysql-connector-j-9.6.0.jar` is included in the repo
- In IntelliJ IDEA: `File -> Project Structure -> Modules -> Dependencies -> + -> JARs` -> select the jar

### 5. Run
- Run `src/Main.java`
- Admin password: `admin123`

---

## OOP Concepts Demonstrated

| Concept              | Where Used                                              |
|----------------------|---------------------------------------------------------|
| Interface/Abstraction | `User` interface with `showMenu()`                 |
| Polymorphism         | `Student` and `Professor` implement `showMenu()` differently |
| Encapsulation        | All entity classes use `private` fields + getters/setters |
| Composition          | `Course` holds a `Professor`; `Student` holds lists of courses |
| Enum                 | `ComplaintStatus` (Pending / Resolved)               |
| Static Members       | `DatabaseConnection.getConnection()`                 |

---

## Project Structure

```
src/
+-- Main.java                     Entry point
+-- Login.java                    User type selector
+-- User.java                     Interface (Student and Professor contract)
+-- Student.java                  Student entity + all student operations
+-- Professor.java                Professor entity + all professor operations
+-- Admin.java                    Admin dashboard (full CRUD)
+-- StudentAuthentication.java    Student login/signup via JDBC
+-- ProfessorAuthentication.java  Professor login/signup via JDBC
+-- AdminAuthentication.java      Admin password authentication
+-- Course.java                   Course data model
+-- Complaint.java                Complaint data model + enum
+-- DataBase.java                 Static store (legacy)
+-- DatabaseConnection.java       JDBC connection factory

Library/
+-- mysql-connector-j-9.6.0.jar   MySQL JDBC Driver
```

---

## Author

**Dev Prajapati** - SVNIT OOPs Assignment