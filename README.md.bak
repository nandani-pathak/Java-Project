# Student Grade Manager
## Unit II — Java Fundamentals Project

---

## How to Run in VS Code

1. Open the `StudentGradeManager` folder in VS Code
2. Make sure you have the **Java Extension Pack** installed
3. Open terminal and run:

```bash
cd src
javac *.java
java Main
```

---

## What This Project Covers (Unit II Syllabus)

| Concept               | Where Used                                      |
|-----------------------|-------------------------------------------------|
| Classes & Instances   | `Student.java` — fields, constructor, methods   |
| Inheritance           | `HonoursStudent extends Student`                |
| Method Overriding     | `getGrade()`, `display()` in HonoursStudent     |
| Arrays                | `double[] marks` — stores subject marks         |
| Wrapper Classes       | `Double.parseDouble()`, `Integer.parseInt()`    |
| Arithmetic Operators  | total, percentage calculations                  |
| Logical Operators     | `val < 0 || val > 100` in validation            |
| Control of Flow       | `for` loops, `if-else` in grade/topper logic    |
| throw & throws        | `InvalidMarksException` thrown in GradeManager  |
| User Defined Exception| `InvalidMarksException.java`                    |
| StringBuffer          | `generateReport()` builds the report string     |
| Class Member Modifiers| `private`, `public`, `static` used throughout   |

---

## Files

```
src/
├── Main.java                  ← Entry point, runs everything
├── Student.java               ← Base class
├── HonoursStudent.java        ← Subclass (inheritance)
├── GradeManager.java          ← Logic: validate, report, topper
└── InvalidMarksException.java ← Custom exception
```

---

## Sample Run

```
How many students do you want to enter? 2

--- Student 1 ---
Name       : Nikko
Roll No    : 101
Is Honours student? (yes/no): no
Enter marks for 3 subjects: 88,92,79

--- Student 2 ---
Name       : Rahul
Roll No    : 102
Is Honours student? (yes/no): yes
Enter marks for 3 subjects: 75,80,85
Enter bonus marks: 5

🏆 Topper: Rahul (86.7%)
✔  Passed : 2 / 2
```

---

## How to Explain to Your Teacher

1. **Start with `Student.java`** — show encapsulation (private fields + getters)
2. **Show `HonoursStudent.java`** — explain `extends` and `@Override`
3. **Show `InvalidMarksException.java`** — user-defined exception extending `Exception`
4. **Show `GradeManager.java`** — StringBuffer, throw/throws, logical operators
5. **Run `Main.java`** — show the full flow live
