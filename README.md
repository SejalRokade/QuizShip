# 📚 QuizShip

**QuizShip** is a Java-based desktop application for managing online quizzes in a classroom setting. It enables teachers to create and assign quizzes to student batches, and automatically deliver them via email (with/without PDF attachments). The application supports teacher-student workflows, quiz management, PDF generation, and email notifications using the JavaMail API.

---

## ✨ Features

- 🔐 User registration with role-based handling (Teacher/Student)
- 👨‍🏫 Teacher dashboard to:
  - Create batches
  - Add questions with options and difficulty
  - Create quizzes and assign to batches
- 👩‍🎓 Student allocation to batches
- 📄 Auto-generation of quiz PDFs
- 📬 Email delivery of quizzes to students
- ✅ Welcome email on successful registration
- 🗂️ Logging of system-level errors into the database
- 🧵 Multithreading and responsive Swing GUI

---

## 💻 Tech Stack

| Technology     | Purpose                            |
|----------------|------------------------------------|
| Java (JDK 17+) | Core development                   |
| Swing          | Graphical User Interface (GUI)     |
| JDBC           | MySQL database connectivity        |
| MySQL          | Backend database                   |
| JavaMail API   | Email handling with attachments    |
| iTextPDF       | Generating PDFs of quizzes         |
| MVC + OOP      | Code organization and structure    |
| Logging        | Exception tracking and debugging   |

---

## 🛠️ Installation & Setup Guide

### Step 1: Clone the Repository

git clone https://github.com/SejalRokade/QuizShip.git


### Step 2: Open in IntelliJ

File → Open → Select project folder.



Set Project SDK to Java 17+.



Go to File → Project Structure → Project:



Project SDK: Java 17



Compiler Output: ./out (or your desired build folder)



### Step 3: Setup Database

Create a new MySQL database.



Import the SQL schema/tables:



SOURCE /path/to/schema.sql;

Update your DatabaseUtil.java:



String url = "jdbc:mysql://localhost:3306/your\_database";

String user = "your\_username";

String password = "your\_password";

### Step 4: Configure Email

If using Gmail:



Go to Google App Passwords



Generate an app password for your Gmail account



Replace this in EmailSender.java:



String senderEmail = "your\_email@gmail.com";

String senderPassword = "your\_app\_password";

## 🧾 Database Tables Overview

Table Name	Description

t\_user\_info	Stores user credentials \& roles

t\_teacher	Teacher-specific user info

t\_student	Student-specific user info

t\_batch	Batch info (batch\_id, name, teacher\_id)

t\_student\_batch	Mapping between students and batches

t\_question	Stores quiz questions

t\_quiz	Quiz metadata

t\_quiz\_batch	Mapping between quizzes and batches

quiz\_question	Mapping between quizzes and questions

t\_error\_logger	Logs any exceptions or errors



## 📁 Folder Structure

QuizShip/

├── src/

│   ├── EmailSender.java           # Handles email sending

│   ├── PDFGenerator.java          # Generates quiz PDFs

│   ├── UserDA.java                # DAO class for user operations

│   ├── DatabaseUtil.java          # Connection and error logging

│   ├── PasswordUtil.java          # Password hashing (SHA-256)

│   ├── GUIs/                      # All Swing GUI screens

│   └── ...

├── resources/

│   └── icons, logos, PDFs, etc.

├── out/                           # Compiler output

└── README.md

## 🧪 Sample Workflow

Register as Teacher or Student



Login → Teacher creates batch



Teacher adds questions



Teacher creates quiz (select topic, difficulty)



Teacher assigns quiz to batch



Quiz PDF is auto-generated and emailed to students



All activities logged, and emails validated



🧵 Multi-threading \& Responsiveness

Swing UI actions like email sending or PDF generation are handled in separate threads using:



new Thread(() -> {

&nbsp;   // email or PDF task

}).start();

## ❗ Troubleshooting

Issue	Solution

Email not sent	Check app password and SMTP settings

FileNotFoundException on PDF	Make sure the PDFs directory exists before writing

Invalid recipient address error	Ensure the email is in correct format and domain exists

Auth error 535-5.7.8	Use App Passwords



## 📬 Sample Email Sent

Subject: Welcome! You Have Been Successfully Registered as a Student



Dear Shalvi Rokade,



We are excited to welcome you to QuizShip! Your registration as a student has been successfully completed.

You will receive quiz notifications via email with instructions and deadlines.



Happy Learning!

\- Team QuizShip

## 🚀 Future Scope

✅ Result Evaluation \& Grading System



📊 Student progress dashboard (charts, analytics)



📱 Mobile version with React Native or Kotlin



☁️ Hosting on AWS/GCP with web-based access



## 👩‍💻 Developer

Sejal Rokade

Email: rokadesejal15@gmail.com

GitHub: @SejalRokade



## 📜 License

This project is under the MIT License.





Let me know if you'd like me to add:

\- Screenshots

\- GitHub badges (build passing, license, etc.)

\- Database `.sql` file content

\- Automatic email templates as `.eml` or `.txt` files



Just ask — I’ll generate that too!







