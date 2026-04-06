# BTech Student Database System
## Menu-Driven Academic Record Management

## Features

- Menu-driven system for adding, viewing, and searching student records
- Detailed student profile with `semester`, `section`, and `admission year`
- Branch selection for `CSE`, `IT`, `AI/ML`, `ECE`, `MECH` & `CIVIL`
- Subject-wise mark entry based on the selected branch
- Co-curricular activity credit for sports, projects, hackathons, event organizers, and cultural activity
- Department-wise toppers and overall university rank 1
- Search by roll number

---

## Files

```
Main.java                  <- Menu-driven entry point and interactive workflow
Student.java               <- Student profile model with academic details
ActivityStudent.java       <- Co-curricular activity credit model
GradeManager.java          <- Reports, search, toppers, and department analytics
InvalidMarksException.java <- Custom exception for invalid marks
```

---

## Main Menu

```
1. Add Student Record
2. View All Student Records
3. Search Student By Roll Number
4. Show Department Toppers
5. Show University Rank 1
6. Exit
```
