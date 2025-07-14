import java.util.*;

public class Login {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDA userDA = new UserDA();
        boolean exitOuterLoop = false;
        while (!exitOuterLoop) {
            try {
                System.out.println("Welcome! Please choose an option:");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Enter your choice (1/2/3): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        handleLogin(scanner, userDA);
                        break;
                    case 2:
                        handleRegistration(scanner, userDA);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        scanner.close();
                        return; // Exit the loop and the program
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            } catch (Exception e) {
                System.out.println("----");
                DatabaseUtil dbUtil = new DatabaseUtil();
                dbUtil.logError(e, "main", "Loging");
                System.out.println("An error occurred: " + e.getMessage());
                scanner.nextLine(); // Clear invalid input
            }
        }
    }


    /**
     * Handles user login by prompting for email and password, validating credentials,
     * and directing users to their respective options based on their role (Teacher/Student).
     */

    private static void handleLogin(Scanner scanner, UserDA userDA) {
        System.out.print("Enter your email: ");
        String teacher_Email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (userDA.userLogin(teacher_Email, password)) {
            System.out.println("Login successful!");
            String role = userDA.getUserRole(teacher_Email);

            if ("Teacher".equalsIgnoreCase(role)) {
                handleTeacherOptions(scanner, userDA, teacher_Email);

            }
            /*else if ("Student".equalsIgnoreCase(role)) {
                handleStudentOptions(userDA, email);
            }*/
            else {
                System.out.println("Unknown role.");
            }
        } else {
            System.out.println("Invalid credentials.");
        }

    }


    /**
     * Displays a menu for teacher-specific options and handles user interactions
     * until the teacher chooses to log out.
     *
     * @param scanner The Scanner object for reading user input.
     * @param userDA  The UserDA object for accessing user data and performing operations.
     * @param email   The email of the teacher currently logged in, used for context in operations.
     */
    /*private static void handleTeacherOptions(Scanner scanner, UserDA userDA, String email) {
        boolean exitAsTeacher = false;  // Flag to control loop for teacher options

        while (!exitAsTeacher) {
            System.out.println("1. Create Batch");
            System.out.println("2. Create Quiz");
            System.out.println("3. Allocate Quiz");
            System.out.println("4. Add Student to Batch");
            System.out.println("5. Deactivate Batch");
            System.out.println("6.Logout");
            // Ask for teacher's choice inside the loop
            System.out.print("Enter your choice (1/2/3/4/5): ");
            int teacherChoice = scanner.nextInt();
            scanner.nextLine(); // consume leftover newline after nextInt()

            switch (teacherChoice) {
                case 1:
                    createBatch(scanner, userDA, email); // Create a batch
                    break;
                case 2:
                    createQuiz(scanner, userDA, email);  // Create a quiz
                    break;
                case 3:
                    allocateQuiz(scanner, userDA, email); // Allocate a quiz
                    break;
                case 4:
                    addStudentToBatch(scanner, userDA); // New method to add a student
                    break;
                case 5:
                    deactivateBatch(scanner, userDA, email); // New method to delete a batch
                    break;
                case 6:
                    exitAsTeacher = true;  // Exit the loop if teacher chooses to log out
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
            if (!exitAsTeacher) {
                System.out.println("\nAction completed. Returning to the menu...");
            }
        }
    }*/
    private static void handleTeacherOptions(Scanner scanner, UserDA userDA, String email) {
        boolean exitAsTeacher = false;  // Flag to control loop for teacher options
        String studentName = null,  studentEmail = null, selectedBatch = null;

        while (!exitAsTeacher) {
            System.out.println("1. Create Batch");
            System.out.println("2. Create Quiz");
            System.out.println("3. Allocate Quiz");
            System.out.println("4. Add Student to Batch");
            System.out.println("5. Deactivate Batch");
            System.out.println("6. Display Student Details");   // New case
            System.out.println("7. Display All Batch Names");   // New case
            System.out.println("8. Display All Quizzes Assigned to Batches"); // New case
            System.out.println("9. Logout");                    // Update logout to case 9

            // Ask for teacher's choice inside the loop
            System.out.print("Enter your choice (1-9): ");
            int teacherChoice = scanner.nextInt();
            scanner.nextLine(); // consume leftover newline after nextInt()

            switch (teacherChoice) {
                case 1:
                    createBatch(scanner, userDA, email); // Create a batch
                    break;
                case 2:
                    createQuiz(scanner, userDA, email);  // Create a quiz
                    break;
                case 3:
                    allocateQuiz(scanner, userDA, email); // Allocate a quiz
                    break;
                case 4:
                    addStudentToBatch(studentName, studentEmail, selectedBatch , userDA); // New method to add a student
                    break;
                case 5:
                    deactivateBatch(scanner, userDA, email); // New method to deactivate a batch
                    break;
                case 6:
                    displayStudentDetails(scanner, userDA);  // New case for displaying student details
                    break;
                case 7:
                    displayAllBatchNames(userDA);  // New case for displaying all batch names
                    break;
                case 8:
                    displayAllQuizzesAssignedToBatches(userDA); // New case for displaying quizzes assigned to batches
                    break;
                case 9:
                    exitAsTeacher = true;  // Exit the loop if teacher chooses to log out
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
            if (!exitAsTeacher) {
                System.out.println("\nAction completed. Returning to the menu...");
            }
        }
    }

// Define the new methods below as per your requirements

    // Method to display student details
    private static void displayStudentDetails(Scanner scanner, UserDA userDA) {
        System.out.print("Enter the student's email to view details: ");
        String studentEmail = scanner.nextLine();

        // Call the UserDA method to get the student details
        String studentDetails = userDA.getStudentDetails(studentEmail);

        // Print the details or the error message
        System.out.println(studentDetails);
    }

    // Method to display all batch names
    public static void displayAllBatchNames(UserDA userDA) {
        List<String> batchNames = (List<String>) userDA.getAllBatchNames();

        if (batchNames.isEmpty()) {
            System.out.println("No batches found.");
        } else {
            System.out.println("All Batch Names:");
            for (String batchName : batchNames) {
                System.out.println("- " + batchName);
            }
        }
    }

    // Method to display all quizzes assigned to batches
    // Method to display all quizzes assigned to batches
    public static void displayAllQuizzesAssignedToBatches(UserDA userDA) {
        Map<String, List<String>> batchQuizMap = userDA.getQuizzesAssignedToBatches();

        if (batchQuizMap.isEmpty()) {
            System.out.println("No quizzes assigned to any batch.");
        } else {
            System.out.println("Quizzes Assigned to Batches:");
            for (Map.Entry<String, List<String>> entry : batchQuizMap.entrySet()) {
                String batchName = entry.getKey();
                List<String> quizNames = entry.getValue();

                System.out.println("Batch: " + batchName);
                System.out.println("Quizzes: " + String.join(", ", quizNames));
                System.out.println("----------------------------");
            }
        }
    }


    //----------------------------------------------------------------------------------------------------------------------------------------------------
// Method to add a student to a batch without terminal input
    public static boolean addStudentToBatch(String studentName, String studentEmail, String selectedBatch, UserDA userDA) {
        String password = "0"; // Handle password if needed
        // Fetch available batches
        List<String> availableBatches = userDA.getAvailableBatches();

        // Validate the selected batch against available batches
        if (selectedBatch != null && availableBatches.contains(selectedBatch)) {
            // Check if the student is already registered
            if (userDA.userRegister(studentName, studentEmail, password, "Student")) {
                // Student successfully registered, now assign batch
                if (userDA.assignBatchToStudent(studentEmail, selectedBatch)) {
                    System.out.println("Student " + studentName + " successfully added to batch: " + selectedBatch);
                    return true;
                } else {
                    System.out.println("Error in assigning batch.");
                    return false;
                }
            } else {
                // If already registered, assign them to the new batch
                System.out.println("Student is already registered. Assigning to a new batch...");
                if (userDA.assignBatchToStudent(studentEmail, selectedBatch)) {
                    System.out.println("Student " + studentName + " successfully assigned to batch: " + selectedBatch);
                    return true;
                } else {
                    System.out.println("Error in assigning batch.");
                    return false;
                }
            }
        } else {
            // Invalid batch selection or no available batches
            System.out.println("Invalid or unavailable batch selection.");
            return false;
        }
    }



    /**
     * Prompts the teacher to select and deactivate one of their batches.
     *
     * @param scanner The Scanner object used for reading user input.
     * @param userDA  The UserDA object used to access and manage batch data.
     * @param email   The email of the teacher requesting batch deactivation.
     */
    /*private static void deactivateBatch(Scanner scanner, UserDA userDA, String email) {
        // Fetch batches associated with the teacher
        List<String> batches = userDA.getBatchesByTeacher(email);

        // Check if the teacher has any active batches
        if (!batches.isEmpty()) {
            System.out.println("Available batches:");
            for (int i = 0; i < batches.size(); i++) {
                System.out.println((i + 1) + ". " + batches.get(i));
            }

            // Prompt the teacher to select a batch for deactivation
            System.out.print("Select a batch to deactivate by entering the number: ");
            int batchChoice = scanner.nextInt();
            scanner.nextLine(); // consume the leftover newline

            // Validate the batch selection
            if (batchChoice > 0 && batchChoice <= batches.size()) {
                String selectedBatch = batches.get(batchChoice - 1);

                // Confirm the action with the teacher
                System.out.print("Are you sure you want to deactivate the batch '" + selectedBatch + "'? (yes/no): ");
                String confirmation = scanner.nextLine();

                if ("yes".equalsIgnoreCase(confirmation)) {
                    // Call UserDA's deactivateBatch method to deactivate the batch
                    if (userDA.deactivateBatch(selectedBatch)) {
                        System.out.println("Batch '" + selectedBatch + "' successfully deactivated.");
                    } else {
                        System.out.println("Error: Failed to deactivate the batch.");
                    }
                } else {
                    System.out.println("Batch deactivation canceled.");
                }
            } else {
                System.out.println("Invalid batch selection.");
            }
        } else {
            System.out.println("No batches available to deactivate.");
        }
    }*/
    private static void deactivateBatch(Scanner scanner, UserDA userDA, String email) {
        // Fetch batches associated with the teacher
        List<String> batches = userDA.getBatchesByTeacher(email);

        // Check if the teacher has any active batches
        if (!batches.isEmpty()) {
            System.out.println("Available batches:");
            for (int i = 0; i < batches.size(); i++) {
                System.out.println((i + 1) + ". " + batches.get(i));
            }

            // Prompt the teacher to select multiple batches for deactivation
            System.out.print("Select batches to deactivate by entering the numbers separated by commas (e.g., 1,3,5): ");
            String input = scanner.nextLine();

            // Split the input into an array of batch numbers
            String[] batchChoices = input.split(",");

            // Loop through the selected batches
            for (String batchChoiceStr : batchChoices) {
                try {
                    int batchChoice = Integer.parseInt(batchChoiceStr.trim());

                    // Validate the batch selection
                    if (batchChoice > 0 && batchChoice <= batches.size()) {
                        String selectedBatch = batches.get(batchChoice - 1);

                        // Confirm the action with the teacher
                        System.out.print("Are you sure you want to deactivate the batch '" + selectedBatch + "'? (yes/no): ");
                        String confirmation = scanner.nextLine();

                        if ("yes".equalsIgnoreCase(confirmation)) {
                            // Call UserDA's deactivateBatch method to deactivate the batch
                            if (userDA.deactivateBatch(selectedBatch)) {
                                System.out.println("Batch '" + selectedBatch + "' successfully deactivated.");
                            } else {
                                System.out.println("Error: Failed to deactivate the batch.");
                            }
                        } else {
                            System.out.println("Batch deactivation canceled for '" + selectedBatch + "'.");
                        }
                    } else {
                        System.out.println("Invalid batch selection: " + batchChoice);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input: " + batchChoiceStr + " is not a number.");
                }
            }
        } else {
            System.out.println("No batches available to deactivate.");
        }
    }


    /**
     * Prompts the user to enter a name for a new batch and attempts to create it.
     *
     * @param scanner The Scanner object used for reading user input.
     * @param userDA  The UserDA object used to access user data and perform the batch creation operation.
     * @param email   The email of the user creating the batch, used for context in the batch creation process.
     */
    public static void createBatch(Scanner scanner, UserDA userDA, String email) {
        boolean b = false;
        while (!b) {
            System.out.println("Enter a name for your new batch: ");
            String batchName = scanner.next();

            if (userDA.Batch(email, batchName)) {
                System.out.println("Batch Successfully created!");
                b = true;  // Exit the loop after successful batch creation
            } else {
                System.out.println("Error: Batch creation failed. Please try again.");
            }
        }
    }


    /**
     * Prompts the user to create a new quiz by entering its name, topic, and number of questions.
     *
     * @param scanner The Scanner object used for reading user input.
     * @param userDA  The UserDA object used to access user data and perform quiz creation operations.
     * @param email   The email of the teacher creating the quiz, used to fetch the teacher's ID.
     */
    /*private static void createQuiz(Scanner scanner, UserDA userDA, String email) {
        System.out.print("Enter quiz name: ");
        String quizName = scanner.nextLine();
        System.out.print("Enter quiz topic: ");
        String topic = displayTopics(scanner, userDA); // Get the selected topic
        //String topic = scanner.nextLine();
        System.out.print("Enter number of questions: ");
        int numQuestions = scanner.nextInt();
        scanner.nextLine(); // consume newline

        int teacherId = userDA.getTeacherIdByEmail(email); // Method to get teacherId by email

        if (userDA.createQuiz(quizName, topic, teacherId, numQuestions)) {
            System.out.println("Quiz successfully created!");
        } else {
            System.out.println("Error in creating quiz.");
        }

    }*/
    private static void createQuiz(Scanner scanner, UserDA userDA, String email) {
        System.out.print("Enter quiz name: ");
        String quizName = scanner.nextLine();
        System.out.print("Enter quiz topic: ");
        String topic = displayTopics(scanner, userDA); // Get the selected topic

        // Ask the user for the number of questions for each difficulty level
        System.out.print("Enter number of easy questions: ");
        int numEasyQuestions = scanner.nextInt();
        System.out.print("Enter number of medium questions: ");
        int numNormalQuestions = scanner.nextInt();
        System.out.print("Enter number of hard questions: ");
        int numHardQuestions = scanner.nextInt();
        scanner.nextLine(); // consume newline

        int teacherId = userDA.getTeacherIdByEmail(email); // Method to get teacherId by email

        // Pass these values to createQuiz method
        if (userDA.createQuiz(quizName, topic, teacherId, numEasyQuestions, numNormalQuestions, numHardQuestions)) {
            System.out.println("Quiz successfully created!");
        } else {
            System.out.println("Error in creating quiz.");
        }
    }


    /**
     * Displays a list of available topics and prompts the user to select one.
     *
     * @param scanner The Scanner object used for reading user input.
     * @param userDA  The UserDA object used to access user data and retrieve topics.
     * @return The selected topic as a String. If no topics are available, it returns
     * the user-provided input.
     */
    // Method to display topics and return the selected topic
    private static String displayTopics(Scanner scanner, UserDA userDA) {
        List<String> uniqueTopics = userDA.getAllTopics(); // Call the method to get unique topics
        String topic = ""; // Initialize topic variable

        if (!uniqueTopics.isEmpty()) {
            System.out.println("Available topics:");
            for (int i = 0; i < uniqueTopics.size(); i++) {
                System.out.println((i + 1) + ". " + uniqueTopics.get(i));
            }

            while (true) { // Loop until a valid topic is selected
                System.out.print("Select a topic by entering the number: ");
                String userInput = scanner.nextLine();

                try {
                    int topicChoice = Integer.parseInt(userInput);
                    if (topicChoice > 0 && topicChoice <= uniqueTopics.size()) {
                        topic = uniqueTopics.get(topicChoice - 1); // Select the topic from the list
                        break; // Valid selection; exit the loop
                    } else {
                        System.out.println("Invalid selection. Please choose a valid number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer number."); // Custom message for non-integer input
                }
            }
        } else {
            System.out.println("No topics available.");
            topic = scanner.nextLine(); // If no topics are available, read the input
        }

        return topic; // Return the selected or entered topic
    }


    /**
     * Allocates a selected quiz to a chosen batch for a teacher.
     *
     * @param scanner The Scanner object used for reading user input.
     * @param userDA  The UserDA object used to access user data and manage quizzes and batches.
     * @param email   The email of the teacher performing the allocation, used to retrieve their ID.
     */
    private static void allocateQuiz(Scanner scanner, UserDA userDA, String email) {
        int teacherId = userDA.getTeacherIdByEmail(email);
        List<String> batches = userDA.getAllBatches(); // Ensure this returns teacher-specific batches

        if (!batches.isEmpty()) {
            System.out.println("Available batches:");
            for (int i = 0; i < batches.size(); i++) {
                System.out.println((i + 1) + ". " + batches.get(i));
            }
            System.out.print("Select a batch by entering the number: ");
            int batchChoice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (batchChoice > 0 && batchChoice <= batches.size()) {
                String selectedBatch = batches.get(batchChoice - 1);
                int batchId = userDA.getBatchIdByName(selectedBatch);

                List<String> quizzes = userDA.getQuizzesForTeacher(teacherId);
                if (!quizzes.isEmpty()) {
                    System.out.println("Available quizzes:");
                    for (int i = 0; i < quizzes.size(); i++) {
                        System.out.println((i + 1) + ". " + quizzes.get(i));
                    }
                    System.out.print("Select a quiz by entering the number: ");
                    int quizChoice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    if (quizChoice > 0 && quizChoice <= quizzes.size()) {
                        String selectedQuiz = quizzes.get(quizChoice - 1);
                        int quizId = userDA.getQuizIdByName(selectedQuiz);

                        // Perform quiz allocation
                        // Exit after showing the error
                        if (userDA.assignQuizToBatch(quizId, batchId)) {
                            //System.out.println("Quiz successfully allocated to batch!00");

                            // Exit the method or loop after successful allocation
                            return;  // Use return to ensure that it doesn't ask for another allocation
                        } else {
                            System.out.println("Error in allocating quiz.");
                            return;  // Use return to ensure that it doesn't ask for another allocation
                        }
                    } else {
                        System.out.println("Invalid quiz choice.");
                    }
                } else {
                    System.out.println("No quizzes available for this teacher.");
                }
            } else {
                System.out.println("Invalid batch choice.");
            }
        } else {
            System.out.println("No batches available.");
        }
    }


    /**
     * Handles the options available to a student, displaying their assigned batches and associated quizzes.
     *
     * @param userDA The UserDA object used to access user data, including batches and quizzes.
     * @param email  The email of the student whose options are being handled.
     */
    private static void handleStudentOptions(UserDA userDA, String email) {
        // Get list of batches assigned to the student
        List<String> batches = userDA.getBatchByStudent(email);

        if (!batches.isEmpty()) {
            System.out.println("You are currently assigned to the following batch(es):");

            // Display each batch
            for (String batch : batches) {
                System.out.println(batch);

                // Get quizzes assigned to this batch and display them
                displayAssignedQuizzes(userDA, batch);
            }
        } else {
            System.out.println("You are not assigned to any batch.");
        }
    }


    /**
     * Displays the quizzes assigned to a specified batch.
     *
     * @param userDA    The UserDA object used to access user data and quizzes.
     * @param batchName The name of the batch for which quizzes are to be displayed.
     */
    private static void displayAssignedQuizzes(UserDA userDA, String batchName) {
        // Get the batch ID based on the batch name
        int batchId = userDA.getBatchIdByName(batchName);

        // Get quizzes for the batch
        List<String> quizzes = userDA.getQuizzesForBatch(batchId);

        // Use a Set to ensure each quiz is printed only once
        Set<String> uniqueQuizzes = new HashSet<>(quizzes);

        if (!uniqueQuizzes.isEmpty()) {
            System.out.println("Quizzes assigned to batch " + batchName + ":");

            // Display each unique quiz
            for (String quiz : uniqueQuizzes) {
                System.out.println("- " + quiz);
            }
        } else {
            System.out.println("No quizzes are assigned to batch " + batchName);
        }
    }


    /**
     * Handles the registration process for users, either as a Student or Teacher.
     *
     * @param scanner The Scanner object for capturing user input.
     * @param userDA  The UserDA object used to access user data and perform registrations.
     */
    private static void handleRegistration(Scanner scanner, UserDA userDA) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your role (Teacher): ");
        String role = scanner.nextLine();

/*
        if ("Student".equalsIgnoreCase(role)) {
            // Register the student first
            if (userDA.userRegister(name, email, role)) {
                // Display available batches for students
                List<String> availableBatches = userDA.getAvailableBatches();
                if (!availableBatches.isEmpty()) {
                    System.out.println("Available batches:");
                    for (int i = 0; i < availableBatches.size(); i++) {
                        System.out.println((i + 1) + ". " + availableBatches.get(i));
                    }

                    int batchChoice = -1;
                    boolean validChoice = false;
                    while (!validChoice) {
                        System.out.print("Select a batch by entering the number: ");
                        if (scanner.hasNextInt()) {
                            batchChoice = scanner.nextInt();
                            scanner.nextLine(); // consume newline
                            if (batchChoice > 0 && batchChoice <= availableBatches.size()) {
                                validChoice = true;
                            } else {
                                System.out.println("Invalid batch number. Please enter a number between 1 and " + availableBatches.size());
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a valid number.");
                            scanner.nextLine(); // clear invalid input
                        }
                    }

                    String selectedBatch = availableBatches.get(batchChoice - 1);

                    // Assign student to selected batch
                    if (userDA.assignBatchToStudent(email, selectedBatch)) {
                        System.out.println("Registration successful and assigned to batch: " + selectedBatch);
                    } else {
                        System.out.println("Error assigning to batch.");
                    }
                } else {
                    System.out.println("No batches available for registration.");
                }
            } else {
                System.out.println("Error in registration.");
            }


    } else {
            // If not a student, just register

            */
        if (userDA.userRegister(name, email,password, role)) {
            System.out.println("****  Registration Successful!!!  ****");
        } else {
            System.out.println("Error in registration.");
        }
    }



}


