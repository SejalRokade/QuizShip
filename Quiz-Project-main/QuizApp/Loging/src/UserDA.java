import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The UserDA class handles user-related data access operations,
 * including user authentication through login.
 */
public class UserDA {
    //#corrections: Change name of map studentBatchMap
    private Map<String, String> studentBatchDatabase = new HashMap<>();


    /**
     * Authenticates a user by checking their email and password against the database.
     *
     * @param email    The email address of the user attempting to log in.
     * @param password The password of the user attempting to log in.
     * @return A boolean indicating whether the login was successful (true) or not (false).
     * Returns true if a matching user record is found in the database; otherwise, false.
     */
    // Existing Login method
    public static boolean userLogin(String email, String password) {
        String query = "SELECT * FROM t_User_Info WHERE Email = ? AND Password = ?";

        // Hash the password the user entered
        String hashedPassword = PasswordUtil.hashPassword(password);

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);  // Use hashed password
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // returns true if a record is found
        } catch (SQLException e) {
            // Log the error using DatabaseUtil
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "userLogin", "UserDA");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Registers a new user by inserting their details into the database.
     * This method checks for existing email addresses, inserts user information
     * into the user table, and adds the user to the Teacher or Student table based on their role.
     *
     * @param name     The name of the user being registered.
     * @param email    The email address of the user, which must be unique.
     * @param password The password for the user account.
     * @param role     The role of the user, either "Teacher" or "Student".
     * @return A boolean indicating whether the registration was successful (true)
     * or failed due to an existing email or another error (false).
     */
    // Updated Register method with batch selection for students
    public static boolean userRegister(String name, String email, String password, String role) {
        int flag = 0;

        // Check if name or email is missing
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name is required.");
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email is required.");
            return false;
        }

        // Normalize email
        email = email.trim().toLowerCase();

        // Hash the password before saving it
        String hashedPassword = PasswordUtil.hashPassword(password);

        String insertUserQuery = "INSERT INTO t_User_Info (Name, Email, Password, Role) VALUES (?, ?, ?, ?)";
        String selectUserIdQuery = "SELECT User_id FROM t_User_Info WHERE Email = ?";
        String insertTeacherQuery = "INSERT INTO t_Teacher (User_id) VALUES (?)";
        String insertStudentQuery = "INSERT INTO t_Student (User_id) VALUES (?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement checkEmailStmt = conn.prepareStatement(selectUserIdQuery);
             PreparedStatement insertUserStmt = conn.prepareStatement(insertUserQuery);
             PreparedStatement selectUserIdStmt = conn.prepareStatement(selectUserIdQuery)) {

            // Check if the email already exists
            checkEmailStmt.setString(1, email);
            ResultSet emailCheckRs = checkEmailStmt.executeQuery();

            if (emailCheckRs.next()) {
                int existingId = emailCheckRs.getInt("User_id");
                System.out.println("Email already exists with ID: " + existingId + ". Please use another email.");
                return false;
            }

            // Insert into User_Info table
            insertUserStmt.setString(1, name);
            insertUserStmt.setString(2, email);
            insertUserStmt.setString(3, hashedPassword);
            insertUserStmt.setString(4, role);

            int rowsInserted = insertUserStmt.executeUpdate();
            if (rowsInserted == 0) {
                System.out.println("User registration failed. No rows inserted.");
                return false;
            }

            // Get the User ID after insertion
            selectUserIdStmt.setString(1, email);
            ResultSet userIdRs = selectUserIdStmt.executeQuery();

            if (userIdRs.next()) {
                int userId = userIdRs.getInt("User_id");

                if ("Teacher".equalsIgnoreCase(role)) {
                    try (PreparedStatement insertTeacherStmt = conn.prepareStatement(insertTeacherQuery)) {
                        insertTeacherStmt.setInt(1, userId);
                        insertTeacherStmt.executeUpdate();
                        flag = 1;
                        System.out.println("Teacher registered successfully.");
                    }
                } else if ("Student".equalsIgnoreCase(role)) {
                    try (PreparedStatement insertStudentStmt = conn.prepareStatement(insertStudentQuery)) {
                        insertStudentStmt.setInt(1, userId);
                        insertStudentStmt.executeUpdate();
                        flag = 2;
                        System.out.println("Student registered successfully.");
                    }
                }

                // Send confirmation email based on role
                if (flag == 1) {
                    return EmailSender.sendEmail(email,
                            "Welcome! You Have Been Successfully Registered as a Teacher",
                            "Dear " + name + ",\n\n" +
                                    "We are pleased to inform you that your registration as a teacher has been successfully completed!\n" +
                                    "You can now log in to your account and manage batches, design quizzes, and track student performance.\n\n" +
                                    "If you have any questions, reach out to our support team at support@quizship.com.\n\n" +
                                    "Best regards,\nTeam QuizShip");
                } else if (flag == 2) {
                    return EmailSender.sendEmail(email,
                            "Welcome! You Have Been Successfully Registered as a Student",
                            "Dear " + name + ",\n\n" +
                                    "Your registration as a student has been completed successfully.\n" +
                                    "You will receive quizzes assigned by your teacher via email with full details.\n\n" +
                                    "We wish you the best in your learning experience at QuizShip!\n\n" +
                                    "Best regards,\nTeam QuizShip");
                } else {
                    return false;
                }

            } else {
                System.out.println("Error retrieving user ID after registration.");
                return false;
            }

        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "userRegister", "UserDA");
            System.out.println("SQL Exception: " + e.getMessage());
            return false;
        }
    }


    /**
     * Assigns a new batch to a teacher based on the teacher's email address.
     * The method checks if the batch name already exists in the database
     * and inserts a new batch if it does not.
     *
     * @param email     The email address of the teacher to whom the batch will be assigned.
     * @param batchName The name of the batch to be created.
     * @return A boolean indicating whether the batch assignment was successful (true)
     * or failed due to an existing batch name or other issues (false).
     */
    // Assign batch to teacher
    public static boolean Batch(String email, String batchName) {
        String selectTeacherIdQuery = "SELECT Teacher_id FROM t_Teacher WHERE User_id = (SELECT User_id FROM t_User_Info WHERE Email = ?)";
        String checkBatchNameQuery = "SELECT COUNT(*) FROM t_Batch WHERE Batch_name = ?";
        String insertBatchQuery = "INSERT INTO t_Batch (Batch_name, Teacher_id) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement selectTeacherStmt = conn.prepareStatement(selectTeacherIdQuery);
             PreparedStatement checkBatchStmt = conn.prepareStatement(checkBatchNameQuery);
             PreparedStatement insertBatchStmt = conn.prepareStatement(insertBatchQuery)) {

            // Step 1: Get the teacher's ID based on the email
            selectTeacherStmt.setString(1, email);
            ResultSet rs = selectTeacherStmt.executeQuery();

            if (rs.next()) {
                int teacherId = rs.getInt("Teacher_id");

                // Step 2: Check if the batch name already exists
                checkBatchStmt.setString(1, batchName);
                ResultSet batchCheckRs = checkBatchStmt.executeQuery();
                if (batchCheckRs.next() && batchCheckRs.getInt(1) > 0) {
                    System.out.println("Error: Batch name already exists. Please choose a different name.");
                    return false; // Batch name exists, return false to indicate failure
                }

                // Step 3: If batch name does not exist, insert the new batch
                insertBatchStmt.setString(1, batchName);
                insertBatchStmt.setInt(2, teacherId);
                int rowsInserted = insertBatchStmt.executeUpdate();

                return rowsInserted > 0;  // Return true if the batch was inserted successfully
            }

        } catch (SQLException e) {
            // Log the error using DatabaseUtil
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "Batch", "UserDA");
            e.printStackTrace();
            return false;
        }
        return false;  // Return false if any exception occurs or no teacher is found
       // return true;
    }


    /**
     * Assigns a student to a specified batch based on the student's email address and the batch name.
     * The method retrieves the student and batch IDs from the database and inserts the relationship
     * into the Student_Batch table. It also sends a confirmation email to the student.
     *
     * @param email     The email address of the student to be assigned to the batch.
     * @param batchName The name of the batch to which the student will be assigned.
     * @return A boolean indicating whether the assignment was successful (true)
     * or failed due to issues such as non-existent student or batch (false).
     */
    // Assign a student to the selected batch
    public boolean assignBatchToStudent(String email, String batchName) {
        String selectStudentIdQuery = "SELECT Student_id FROM t_Student WHERE User_id = (SELECT User_id FROM t_User_Info WHERE Email = ?)";
        String selectBatchIdQuery = "SELECT Batch_id FROM t_Batch WHERE Batch_name = ?";
        String insertStudentBatchQuery = "INSERT INTO t_Student_Batch (Student_id, Batch_id) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement selectStudentStmt = conn.prepareStatement(selectStudentIdQuery);
             PreparedStatement selectBatchStmt = conn.prepareStatement(selectBatchIdQuery);
             PreparedStatement insertStudentBatchStmt = conn.prepareStatement(insertStudentBatchQuery)) {

            // Get Student_id from the email
            selectStudentStmt.setString(1, email);
            try (ResultSet studentRs = selectStudentStmt.executeQuery()) {
                if (studentRs.next()) {
                    int studentId = studentRs.getInt("Student_id");

                    // Get Batch_id from the batch name
                    selectBatchStmt.setString(1, batchName);
                    try (ResultSet batchRs = selectBatchStmt.executeQuery()) {
                        if (batchRs.next()) {
                            int batchId = batchRs.getInt("Batch_id");

                            // Assign the student to the selected batch
                            insertStudentBatchStmt.setInt(1, studentId);
                            insertStudentBatchStmt.setInt(2, batchId);
                            int rowsInserted = insertStudentBatchStmt.executeUpdate();

                            if (rowsInserted > 0) {
                                String subject = "Congratulations! You Have Been Successfully Allocated to a Batch" + batchName;
                                String content = "Dear Student,\n" +
                                        "\n" +
                                        "We are pleased to inform you that you have been successfully allocated to Batch "+batchName+" at QuizShip. You will now be able to access the resources, quizzes, and other materials specific to your batch.\n" +
                                        "\n" +
                                        "Your teacher will provide you with more details and allocate quizzes and assignments in due course. Keep an eye on your inbox for updates on quiz allocations, which will be sent to you as soon as they are assigned.\n" +
                                        "\n" +
                                        "If you have any questions or need further assistance, feel free to reach out to your teacher or our support team at rokadesejal15@gmail.com.\n" +
                                        "\n" +
                                        "We wish you the best in your studies and look forward to supporting your learning journey!\n" +
                                        "\n" +
                                        "Best regards,\n" +
                                        "Team QuizShip";
                                //String content = "Welcome \"" + batchName + "\" to";  // Escaping the quotes

                                EmailSender.sendEmail(email, subject, content);
                                return true;
                            }
                            return rowsInserted > 0; // Return true if insertion is successful
                        }
                    }
                }

            } catch (SQLException e) {
                DatabaseUtil dbUtil = new DatabaseUtil();
                dbUtil.logError(e, "assignBatchToStudent", "UserDA");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "assignBatchToStudent", "UserDA");
            throw new RuntimeException(e);
        }
        return false;
    }


    /**
     * Assigns a quiz to a specific batch and notifies all students in that batch via email.
     * The method checks if the quiz is already assigned to the batch, retrieves quiz details,
     * inserts the relationship into the `t_quiz_batch` table, and sends an email with the quiz PDF
     * to all students in the batch.
     *
     * @param quizId  The ID of the quiz to be assigned.
     * @param batchId The ID of the batch to which the quiz will be assigned.
     * @return A boolean indicating whether the assignment was successful (true) or failed (false).
     */
    //
    public static boolean assignQuizToBatch(int quizId, int batchId) {
        String checkExistenceQuery = "SELECT COUNT(*) FROM t_quiz_batch WHERE Quiz_id = ? AND Batch_id = ?";
        String insertQuery = "INSERT INTO t_quiz_batch (Quiz_id, Batch_id, NumberOfQuestion) VALUES (?, ?, ?)";
        String selectQuizDetailsQuery = "SELECT Quiz_name, Topic, NumberOfQuestion FROM t_Quiz WHERE Quiz_id = ?";
        //String selectStudentEmailsQuery = "SELECT Email FROM t_student WHERE Batch_id = ?";
        //String selectStudentEmailsQuery =
        //       "SELECT u.Email FROM t_user_info u " +
        //             "JOIN t_student_batch sb ON u.user_id = sb.student_id " +
        //           "WHERE sb.batch_id = ?";
        String selectStudentEmailsQuery =
                "SELECT u.Email " +
                        "FROM t_user_info u " +
                        "JOIN t_student s ON u.User_id = s.User_id " +
                        "JOIN t_student_batch sb ON s.Student_id = sb.Student_id " +
                        "WHERE sb.Batch_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkExistenceQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
             PreparedStatement selectQuizDetailsStmt = conn.prepareStatement(selectQuizDetailsQuery);
             PreparedStatement selectStudentEmailsStmt = conn.prepareStatement(selectStudentEmailsQuery)) {

            // Check if the record already exists
            checkStmt.setInt(1, quizId);
            checkStmt.setInt(2, batchId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Record already exists
                System.out.println("Quiz already allocated to this batch.");
                return false;
            }

            // Fetch quiz details
            selectQuizDetailsStmt.setInt(1, quizId);
            try (ResultSet quizRs = selectQuizDetailsStmt.executeQuery()) {
                if (quizRs.next()) {
                    String quizName = quizRs.getString("Quiz_name");
                    String topic = quizRs.getString("Topic");
                    int numberOfQuestions = quizRs.getInt("NumberOfQuestion");

                    // Insert the record with NumberOfQuestion
                    insertStmt.setInt(1, quizId);
                    insertStmt.setInt(2, batchId);

                    insertStmt.setInt(3, numberOfQuestions);
                    insertStmt.executeUpdate();

                    System.out.println("Quiz successfully allocated to batch!");

                    // Now fetch the students' emails
                    selectStudentEmailsStmt.setInt(1, batchId);
                    List<String> studentEmails = new ArrayList<>();


                    try (ResultSet emailRs = selectStudentEmailsStmt.executeQuery()) {
                        while (emailRs.next()) {
                            studentEmails.add(emailRs.getString("Email"));
                        }
                    }

                    if (studentEmails.isEmpty()) {
                        System.out.println("No students found in the batch.");
                        return false;
                    }

                    // Prepare the email content
                    String subject = "New Quiz Assigned: " + quizName;
                    String content = "Dear Student,\n" +
                            "\n" +
                            "I hope this email finds you well.\n" +
                            "\n" +
                            "You have been assigned a new quiz named: " + quizName + ". Please find the attached quiz file for your reference and completion.\n" +
                            "\n" +
                            "Should you have any questions or need any clarifications, feel free to reach out.\n" +
                            "\n" +
                            "Best regards,\n" +
                            "Team QuizShip\n" +
                            "\n";

                    // Assume the PDF is generated and stored in a known location
                    File quizPDF = new File("C:\\java\\FinalJavaProject\\PDFs\\" + quizName + ".pdf");
                    File quizPDFAns = new File("C:\\java\\FinalJavaProject\\Ans\\"+ quizName + "Ans"+".pdf");
                    if (quizPDF.exists()) {
                        // Send email to all students in the batch
                        EmailSender.sendEmailWithAttachment(studentEmails, subject, content, quizPDF);
                        EmailSender.sendEmailWithAttachmentAns(studentEmails, subject, content, quizPDFAns);
                    } else {
                        System.out.println("Quiz PDF not found at the specified location.");
                        return false;
                    }

                } else {
                    System.out.println("Quiz details not found.");
                    return false;
                }
            }

            return true;

        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "assignQuizToBatch", "UserDA");
            e.printStackTrace();
            System.out.println("Error assigning quiz to batch: " + e.getMessage());
            return false;
        }
    }


    /**
     * Retrieves the role of a user based on their email address.
     * <p>
     * This method queries the `t_User_Info` table to find the user's role
     * associated with the provided email. If the email exists in the database,
     * it returns the corresponding role; otherwise, it returns null.
     *
     * @param email The email address of the user whose role is to be fetched.
     * @return The role of the user as a String, or null if no user is found or an error occurs.
     */
    // Fetch user role by email
    public String getUserRole(String email) {
        String query = "SELECT Role FROM t_User_Info WHERE Email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Role");
            }
        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getUserRole", "UserDA");
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Retrieves a list of available batch names from the database.
     * <p>
     * This method queries the `t_Batch` table to fetch all batch names
     * that are currently available. It returns a list of these batch names
     * as strings. If no batches are found or an error occurs, an empty list
     * is returned.
     *
     * @return A List of strings containing the names of available batches.
     */
    // Fetch available batches for students
    public List<String> getAvailableBatches() {
        List<String> batchList = new ArrayList<>();
        String query = "SELECT Batch_name FROM t_Batch where Record_status = 'Active'";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                batchList.add(rs.getString("Batch_name"));
            }

        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getAvailableBatches", "UserDA");
            e.printStackTrace();
        }
        return batchList;
    }


    /**
     * Fetches the batches assigned to a student based on their email address.
     * <p>
     * This method queries the database to retrieve all batch names
     * associated with the specified student's email. It joins multiple
     * tables to find the relevant batch information. If no batches are found
     * or an error occurs, an empty list is returned.
     *
     * @param email The email address of the student whose batches are to be fetched.
     * @return A List of strings containing the names of batches assigned to the student.
     */
// Method to fetch batches assigned to a student by email
    public List<String> getBatchByStudent(String email) {
        List<String> studentBatches = new ArrayList<>();
        String query = "SELECT t_batch.Batch_name " +
                "FROM t_batch " +
                "JOIN t_student_batch ON t_batch.Batch_id = t_student_batch.Batch_id " +
                "JOIN t_student ON t_student.Student_id = t_student_batch.Student_id " +
                "JOIN t_user_info ON t_user_info.User_id = t_student.User_id " +
                "WHERE t_user_info.Email = ?";


        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    studentBatches.add(rs.getString("Batch_name"));
                }
            }

        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getBatchByStudent", "UserDA");
            e.printStackTrace();
        }
        return studentBatches;
    }


    /**
     * Retrieves a list of all batches from the database.
     * <p>
     * This method queries the `t_Batch` table to fetch all batch names
     * currently available. It returns a list containing these batch names.
     * If no batches are found or an error occurs, an empty list is returned.
     *
     * @return A List of strings containing the names of all available batches.
     */
    // Method to get all batches
    public List<String> getAllBatches() {
        List<String> batchList = new ArrayList<>();
        String query = "SELECT Batch_name FROM t_Batch WHERE Record_status = 'Active'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                batchList.add(rs.getString("Batch_name"));
            }
        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getAllBatches", "UserDA");
            e.printStackTrace();
        }
        return batchList;
    }


    /**
     * Retrieves a list of quizzes associated with a specific batch.
     * <p>
     * This method queries the database to fetch all quiz names that are
     * linked to the given batch ID. It joins multiple tables to find
     * quizzes assigned to the batch through its teacher.
     * If no quizzes are found or an error occurs, an empty list is returned.
     *
     * @param batchId The ID of the batch for which quizzes are to be fetched.
     * @return A List of strings containing the names of quizzes for the specified batch.
     */
    // Method to get quizzes for a specific batch
    public List<String> getQuizzesForBatch(int batchId) {
        List<String> quizzes = new ArrayList<>();
        String query = "SELECT q.Quiz_name FROM t_quiz q " +
                "JOIN t_batch b ON q.Teacher_id = b.Teacher_id " +
                "JOIN t_student_batch sb ON b.Batch_id = sb.Batch_id " +
                "WHERE sb.Batch_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, batchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    quizzes.add(rs.getString("Quiz_name"));
                }
            }
        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getQuizzesForBatch", "UserDA");
            e.printStackTrace();
        }
        return quizzes;
    }


    /**
     * Retrieves a list of quizzes associated with a specific teacher.
     * <p>
     * This method queries the database to fetch all quiz names that belong
     * to the specified teacher identified by their ID. If no quizzes are found
     * or an error occurs, an empty list is returned.
     *
     * @param teacherId The ID of the teacher for whom quizzes are to be fetched.
     * @return A List of strings containing the names of quizzes for the specified teacher.
     */
    public List<String> getQuizzesForTeacher(int teacherId) {
        List<String> quizzes = new ArrayList<>();
        String query = "SELECT Quiz_name FROM t_Quiz WHERE Teacher_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, teacherId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    quizzes.add(rs.getString("Quiz_name"));
                }
            }
        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getQuizzesForTeacher", "UserDA");
            e.printStackTrace();
        }
        return quizzes;
    }


    /**
     * Retrieves detailed information about a specific batch and its associated quiz.
     * <p>
     * This method queries the database to fetch the batch name, quiz name,
     * topic, and the number of questions in the specified quiz for a given batch.
     *
     * @param batchId The ID of the batch for which information is to be fetched.
     * @param quizId  The ID of the quiz for which details are needed.
     * @return A string containing detailed information about the batch and quiz.
     */
    // Method to get detailed information about a batch and its quizzes
    public String getBatchInfo(int batchId, int quizId) {
        StringBuilder info = new StringBuilder();
        String query = "SELECT b.Batch_name, q.Quiz_name, q.Topic, COUNT(qn.Question_id) AS num_questions " +
                "FROM t_batch b " +
                "JOIN t_quiz q ON b.Teacher_id = q.Teacher_id " +
                "JOIN t_quiz_question qq ON q.Quiz_id = qq.Quiz_id " +
                "JOIN t_question qn ON qq.Question_id = qn.Question_id " +
                "WHERE b.Batch_id = ? AND q.Quiz_id = ? " +
                "GROUP BY b.Batch_name, q.Quiz_name, q.Topic";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, batchId);
            stmt.setInt(2, quizId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    info.append("Batch: ").append(rs.getString("Batch_name")).append("\n")
                            .append("Quiz: ").append(rs.getString("Quiz_name")).append("\n")
                            .append("Topic: ").append(rs.getString("Topic")).append("\n")
                            .append("Number of Questions: ").append(rs.getInt("num_questions")).append("\n");
                }
            }
        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getBatchInfo", "UserDA");
            e.printStackTrace();
        }
        return info.toString();
    }


    /**
     * Retrieves the Teacher ID associated with a given email address.
     * <p>
     * This method queries the database to find the Teacher ID for a specific user
     * identified by their email. If no matching record is found, it returns -1.
     *
     * @param email The email address of the teacher whose ID is to be fetched.
     * @return The Teacher ID associated with the email, or -1 if not found.
     */
    // Method to assign a quiz to a batch
    public int getTeacherIdByEmail(String email) {
        String query = "SELECT t_teacher.Teacher_id FROM t_teacher " +
                "JOIN t_user_info ON t_teacher.User_id = t_user_info.User_id " +
                "WHERE t_user_info.Email = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Teacher_id");
            }

        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getTeacherIdByEmail", "UserDA");
            e.printStackTrace();
        }
        return -1; // Return a default value indicating not found
    }


    /**
     * Retrieves the Batch ID associated with a given batch name.
     * <p>
     * This method queries the database to find the Batch ID for a specific
     * batch identified by its name. If no matching record is found, it returns -1.
     *
     * @param batchName The name of the batch whose ID is to be fetched.
     * @return The Batch ID associated with the batch name, or -1 if not found.
     */
    public int getBatchIdByName(String batchName) {
        String query = "SELECT Batch_id FROM t_Batch WHERE Batch_name = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, batchName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Batch_id");
            }

        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getBatchIdByName", "UserDA");
            e.printStackTrace();
        }
        return -1; // Return a default value indicating not found
    }


    /**
     * Retrieves the Quiz ID associated with a given quiz name.
     * <p>
     * This method queries the database to find the Quiz ID for a specific
     * quiz identified by its name. If no matching record is found, it returns -1.
     *
     * @param quizName The name of the quiz whose ID is to be fetched.
     * @return The Quiz ID associated with the quiz name, or -1 if not found.
     */
    public int getQuizIdByName(String quizName) {
        String query = "SELECT Quiz_id FROM t_Quiz WHERE Quiz_name = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, quizName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Quiz_id");
            }

        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getQuizIdByName", "UserDA");
            e.printStackTrace();
        }
        return -1; // Return a default value indicating not found
    }


    /**
     * Retrieves a list of questions associated with a specific quiz.
     * <p>
     * This method queries the database to get a specified number of questions
     * related to the topic of the provided quiz ID.
     *
     * @param quizId       The ID of the quiz for which to retrieve questions.
     * @param numQuestions The maximum number of questions to retrieve.
     * @return A list of questions for the specified quiz.
     */
    public List<Question> getQuestionsForQuiz(int quizId, int numQuestions) {
        List<Question> questions02 = new ArrayList<>();
        String query = "SELECT * FROM t_Question WHERE Topic = (SELECT Topic FROM t_Quiz WHERE Quiz_id = ?) LIMIT ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            stmt.setInt(2, numQuestions);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Question question1 = new Question();
                   // question1.setId(rs.getInt("Question_id"));
                    question1.setText(rs.getString("Question_text"));
                    question1.setOptionA(rs.getString("OptionA"));
                    question1.setOptionB(rs.getString("OptionB"));
                    question1.setOptionC(rs.getString("OptionC"));
                    question1.setOptionD(rs.getString("OptionD"));
                   // question.setCorrectAnswer(rs.getString("Correct_answer"));
                   // question.setTopic(rs.getString("Topic"));
                   // question.setLanguage(rs.getString("Language"));
                    questions02.add(question1);
                    //questions.add(question);
                }
            }
        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getQuestionsForQuiz", "UserDA");
            e.printStackTrace();
        }
        return questions02;
    }


    /**
     * Creates a new quiz in the database and generates a PDF with questions.
     * <p>
     * This method inserts a new quiz record into the database and fetches a specified number of random questions
     * related to the quiz's topic. It then generates a PDF containing those questions.
     *
     * @param quizName          The name of the quiz.
     * @param topic             The topic of the quiz.
     * @param teacherId         The ID of the teacher creating the quiz.
     * //@param numberOfQuestions The number of questions to fetch for the quiz.
     * @return true if the quiz was created and PDF generated successfully, false otherwise.
     */
    // Method to create a new quiz
  /*  public boolean createQuiz(String quizName, String topic, int teacherId, int numberOfQuestions) {
        String query = "INSERT INTO t_Quiz (Quiz_name, Topic, Teacher_id, NumberOfQuestion) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // insert the quiz into the database
            stmt.setString(1, quizName);
            stmt.setString(2, topic);
            stmt.setInt(3, teacherId);
            stmt.setInt(4, numberOfQuestions);
            stmt.executeUpdate();

            //featch random Question
            QuestionFetcher questionFetcher = new QuestionFetcher(conn);
            List<Question> questions = questionFetcher.fetchRandomQuestions(topic, numberOfQuestions);
            // Check if the list is empty

            // generate PDF
            PDFGenerator pdfGenerator = new PDFGenerator();
            String pdfFilePath = "C:\\java\\MY\\FINAL\\" + quizName + ".pdf";
            if (pdfGenerator.generatePDF(questions, pdfFilePath, quizName, topic)) {
                return true;
            } else {
                return false;
            }


        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for detailed error information
            System.out.println("Error creating quiz: " + e.getMessage());
            return false;
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/



    // Method to create a new quiz with different difficulty levels
    public static boolean createQuiz(String quizName, String topic, int teacherId, int numEasyQuestions, int numNormalQuestions, int numHardQuestions) {

        int totalQuestions = numEasyQuestions + numNormalQuestions + numHardQuestions;
        String query = "INSERT INTO t_Quiz (Quiz_name, Topic, Teacher_id, NumberOfQuestion) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // insert the quiz into the database
            stmt.setString(1, quizName);
            stmt.setString(2, topic);
            stmt.setInt(3, teacherId);
            stmt.setInt(4, totalQuestions); // Store total number of questions
            stmt.executeUpdate();

            // Fetch random questions based on difficulty levels
            QuestionFetcher questionFetcher = new QuestionFetcher(conn);


            System.out.println("   ");
            System.out.println("      Fetching random questions...");
            System.out.println("Topic: " + topic);
            // Fetch easy, normal, and hard questions separately
            List<Question> easyQuestions = questionFetcher.fetchRandomQuestions(topic, numEasyQuestions, "Easy");
            List<Question> normalQuestions = questionFetcher.fetchRandomQuestions(topic, numNormalQuestions, "Normal");
            List<Question> hardQuestions = questionFetcher.fetchRandomQuestions(topic, numHardQuestions, "Hard");

            // Combine all questions into one list
            List<Question> allQuestions = new ArrayList<>();
            allQuestions.addAll(easyQuestions);
            allQuestions.addAll(normalQuestions);
            allQuestions.addAll(hardQuestions);

            System.out.println("-------------------------------------");
            System.out.println("Total Questions Added :" + allQuestions.size());
            System.out.println("-------------------------------------");

            // Check if the total number of fetched questions is less than requested
            /*if (allQuestions.size() != totalQuestions) {
                System.out.println("Not enough questions available for the requested number.");
                return false;
            }*/

            // Generate PDF

            /*
            PDFGenerator pdfGenerator = new PDFGenerator();
            String pdfFilePath = "C:\\java\\MY\\ProjectDraft\\ProjectDraft\\PDFs" + quizName + ".pdf";
            if (pdfGenerator.generatePDF(allQuestions, pdfFilePath, quizName, topic)) {
                return true;
            } else {
                return false;
            }
          //  boolean b = pdfGenerator.generatePDFAns(allQuestions, pdfFilePath, quizName, topic);
          //  return b;

            //PDFGenerator pdfGenerator2 = new PDFGenerator();
           // String pdfFileAnsPath = "E:\\project\\GeneratedQuestion\\" + quizName + ".pdf";
            if(pdfGenerator.generatePDFAns(allQuestions,pdfFilePath,quizName,topic)){
                return  true;
            }else {
                return false;
            }
            */

            PDFGenerator pdfGenerator = new PDFGenerator();
            String pdfFilePath = "C:\\java\\FinalJavaProject\\PDFs\\" + quizName + ".pdf";

// Generate the PDF with questions
            boolean isQuestionPdfGenerated = pdfGenerator.generatePDF(allQuestions, pdfFilePath, quizName, topic);
            System.out.println("  !!!!  Question  generater !!!!");
            if (!isQuestionPdfGenerated) {
                System.out.println("  !!!!  Question not generater !!!!");
                return false;
            }

// Generate the PDF with answers (optional, if this method generates a different PDF with answers)
            pdfFilePath ="C:\\java\\FinalJavaProject\\Ans\\"+ quizName + "Ans"+".pdf";
            boolean isAnswerPdfGenerated = pdfGenerator.generatePDFAns(allQuestions, pdfFilePath, quizName, topic);
            System.out.println("  !!!! Ans Of Question  generater !!!!");
            if (!isAnswerPdfGenerated) {
                System.out.println("  !!!! Ans. Of Question Not generater !!!!");
                return false;
            }

            return true;



        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for detailed error information
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "createQuiz", "UserDA");
            System.out.println("Error creating quiz: " + e.getMessage());
            return false;
        } catch (DocumentException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "createQuiz", "UserDA");
            throw new RuntimeException(e);
        } catch (IOException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "createQuiz", "UserDA");
            throw new RuntimeException(e);
        }
    }


    /**
     * Fetches all unique topics from the question database.
     * <p>
     * This method retrieves distinct topics that are available in the questions table,
     * ensuring that each topic is listed only once.
     *
     * @return A list of unique topics.
     */
    // Method to get all topics
    public static List<String> getAllTopics() {
        List<String> topics = new ArrayList<>();
        String query = "SELECT DISTINCT topic FROM t_question";  // Use DISTINCT to get unique topics

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                topics.add(rs.getString("topic"));  // Add each unique topic to the list
            }
        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getAllTopics", "UserDA");
            e.printStackTrace();
            System.out.println("Error fetching topics: " + e.getMessage());
        }

        return topics;  // Return the list of unique topics
    }

    // WORK IN PROGRESS
    public List<String> getBatchesByTeacher(String email) {
        List<String> teacherBatches = new ArrayList<>();
        // Query to retrieve batches associated with the teacher
        String query = "SELECT batch_name FROM t_batch WHERE teacher_id = ? AND  Record_status = 'active'"; // Only active batches

        try {Connection conn = DatabaseUtil.getConnection();
            //to get the teacher's ID from their email
            int teacherId = getTeacherIdByEmail(email);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                teacherBatches.add(rs.getString("batch_name"));
            }
        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getBatchesByTeacher", "UserDA");
            e.printStackTrace(); // Handle exceptions as needed
        }
        return teacherBatches;
    }

    // Example method to ensure the connection works
    public static boolean deactivateBatch(String batchName) {
        try {Connection conn = DatabaseUtil.getConnection();
            String sql = "UPDATE t_batch SET Record_status = 'Deactive', Record_updated_on = NOW() WHERE Batch_name = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, batchName);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;  // Returns true if the batch was successfully deactivated
        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "deactivateBatch", "UserDA");
            e.printStackTrace();
            return false;
        }
    }

    // Method to fetch batch names from the database
    public static List<String> getBatchNames() {
        List<String> batchNames = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT Batch_name FROM t_batch WHERE Record_status = 'Active'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                batchNames.add(rs.getString("Batch_name")); // Add each batch name to the list
            }
        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getBatchNames", "UserDA");
            e.printStackTrace();
        }
        return batchNames;
    }


    public String getStudentDetails(String studentEmail) {
        String query = "SELECT t_student.Student_id, t_user_info.Name, t_user_info.Email, "
                + "t_student.Batch_id, t_student.Record_status, t_student.Record_created_on "
                + "FROM t_student "
                + "JOIN t_user_info ON t_student.User_id = t_user_info.User_id "
                + "WHERE t_user_info.Email = ?";

        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentEmail);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Fetch the student details
                int studentId = rs.getInt("Student_id");
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                int batchId = rs.getInt("Batch_id");
                String recordStatus = rs.getString("Record_status");
                Timestamp createdOn = rs.getTimestamp("Record_created_on");

                // Format the student details as a string
                return String.format(
                        "Student ID: %d\nName: %s\nEmail: %s\nBatch ID: %d\nStatus: %s\nRecord Created On: %s",
                        studentId, name, email, batchId, recordStatus, createdOn
                );
            } else {
                return "No student found with the email: " + studentEmail;
            }
        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getQuizzesAssignedToBatches", "UserDA");
            return "Error fetching student details: " + e.getMessage();
        }
    }

    public List getAllBatchNames() {
        String query = "SELECT Batch_name FROM t_batch";
        List<String> batchNames = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                batchNames.add(rs.getString("Batch_name"));
            }

        } catch (SQLException e) {
            // Log the error using DatabaseUtil
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getAllBatchNames", "UserDA");
            System.out.println("Error fetching batch names: " + e.getMessage());
        }

        return batchNames;
    }

    // Method to get all quizzes assigned to batches
    public Map<String, List<String>> getQuizzesAssignedToBatches() {
        String query = "SELECT t_batch.Batch_name, t_quiz.Quiz_name "
                + "FROM t_quiz_batch "
                + "JOIN t_batch ON t_quiz_batch.Batch_id = t_batch.Batch_id "
                + "JOIN t_quiz ON t_quiz_batch.Quiz_id = t_quiz.Quiz_id";

        Map<String, List<String>> batchQuizMap = new HashMap<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String batchName = rs.getString("Batch_name");
                String quizName = rs.getString("Quiz_name");

                // Add the quiz to the corresponding batch
                batchQuizMap.computeIfAbsent(batchName, k -> new ArrayList<>()).add(quizName);
            }

        } catch (SQLException e) {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.logError(e, "getQuizzesAssignedToBatches", "UserDA");
            System.out.println("Error fetching quizzes assigned to batches: " + e.getMessage());
        }

        return batchQuizMap;
    }


}