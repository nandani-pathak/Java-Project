# BTech Student Database System
## Menu-Driven Academic Record Management

---

## How to Run in VS Code

1. Open the project folder in VS Code
2. Make sure you have the **Java Extension Pack** installed
3. Open terminal and run:

```bash
javac *.java
java Main
```

---

## Features

- Menu-driven system for adding, viewing, searching, and analyzing student records
- Detailed student profile with `semester`, `section`, `email`, `phone`, and `admission year`
- Branch selection for `CSE`, `IT`, `AI/ML`, `ECE`, `MECH` & `CIVIL`
- Subject-wise mark entry based on the selected branch
- Co-curricular activity credit for sports, projects, hackathons, event organizers, and cultural activity
- Department-wise toppers and overall university rank 1
- Search by roll number and department-wise record viewing
- Academic summary dashboard with averages, pass counts, and activity statistics

---

## Files

```
Main.java                  <- Menu-driven entry point and interactive workflow
Student.java               <- Student profile model with academic and contact details
ActivityStudent.java       <- Co-curricular activity credit model
GradeManager.java          <- Reports, search, toppers, and department analytics
InvalidMarksException.java <- Custom exception for invalid marks
```

---

## Main Menu

```text
1. Add Student Record
2. View All Student Records
3. Search Student By Roll Number
4. View Department Records
5. Show Department Toppers
6. Show University Rank 1
7. Show Academic Summary Dashboard
8. Exit
```

---

## Teacher Impression Points

- It behaves like a real mini college ERP instead of a single-run marks calculator.
- It stores complete student identity and academic details together.
- It supports both academic performance and co-curricular achievement.
- It provides topper logic, search, filtering, and department-level analytics.
