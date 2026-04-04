# BTech Student Database System
## Branch-wise Academic Record Management

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

- Branch selection for `CSE`, `IT`, `AI/ML`, `ECE`, `MECH`, `CIVIL`, and `OTHER`
- Subject-wise mark entry based on the selected branch
- Individual student profile display with department and subject marks
- Separate topper for each department
- Overall university topper across all BTech students
- Summary table that looks like a central student database report
- Input validation for roll numbers and marks

---

## Files

```
Main.java                  <- Entry point and interactive data entry flow
Student.java               <- Student model with branch and subjects
HonoursStudent.java        <- Legacy class kept for compatibility
GradeManager.java          <- Department subjects, report, topper logic
InvalidMarksException.java <- Custom exception
```

---

## Sample Run

```
How many students do you want to enter? 2

--- Student 1 ---
Name       : Nikko
Roll No    : 101
Select Branch:
1. CSE
2. IT
3. AI/ML
4. OTHER
Enter choice : 1
Enter marks for CSE subjects:
DSA : 88
Java : 92
OOPs : 79
CPP : 81

--- Student 2 ---
Name       : Rahul
Roll No    : 102
Select Branch:
1. CSE
2. IT
3. AI/ML
4. OTHER
Enter choice : 3
Enter marks for AI/ML subjects:
Python : 75
Probability : 80
Artificial Intelligence : 85
Machine Learning : 89

CSE Topper : Nikko | Roll No: 101 | Percentage: 85.0%
AI/ML Topper : Rahul | Roll No: 102 | Percentage: 82.3%
UNIVERSITY RANK 1 : Nikko | Branch: CSE | Roll No: 101 | Percentage: 85.0%
Passed : 2 / 2
```

---

## How to Explain to Your Teacher

1. **Start with `Student.java`** - explain how branch, subjects, and marks are stored
2. **Show `GradeManager.java`** - explain department-wise subjects and topper calculation
3. **Show `InvalidMarksException.java`** - explain custom exception handling
4. **Run `Main.java`** - show how the user selects a department and enters marks
5. **Explain the output** - department toppers, university rank 1, and the central report are displayed
