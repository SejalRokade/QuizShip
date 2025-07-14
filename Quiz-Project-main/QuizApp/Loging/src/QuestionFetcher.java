import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionFetcher {

    private Connection connection;

    /**
     * Constructor to initialize the QuestionFetcher with a database connection.
     */
    public QuestionFetcher(Connection connection) {
        this.connection = connection;
    }

    /**
     * Fetches a specified number of random questions from the database that belong to a given topic.
     * Only questions with an active record status will be retrieved.
     */


    /*

    public List<Question> fetchRandomQuestions(String topic, int numberOfQuestions) throws SQLException {
        System.out.println("      Fetching random questions...");
        System.out.println("Topic: " + topic);
        System.out.println("Number of Questions: " + numberOfQuestions);
        String query = "SELECT Question_text, OptionA, OptionB, OptionC, OptionD, Correct_answer " +
                "FROM t_question " +
                "WHERE Topic = ? AND Record_status = 'Active' " +
                "ORDER BY RAND() LIMIT ?";  // 2 placeholders

        List<Question> questions01 = new ArrayList<>(); // Initialize the list to return

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, topic);
            stmt.setInt(2, numberOfQuestions);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("      Executing query...");

                if (!rs.isBeforeFirst()) {  // Check if ResultSet is empty
                    System.out.println("No data found in ResultSet.");
                }

                while (rs.next()) {
                    String questionText = rs.getString("Question_text");
                    String optionA = rs.getString("OptionA");
                    String optionB = rs.getString("OptionB");
                    String optionC = rs.getString("OptionC");
                    String optionD = rs.getString("OptionD");
                   // String correctAnswer = rs.getString("Correct_answer");


                    // Create the Question object and add it to the list
                    Question question = new Question(questionText, optionA, optionB, optionC, optionD);
                    questions01.add(question);

                }
                System.out.println("Total Questions Added : " + questions01.size());

            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for detailed error information
            System.out.println("SQL Error: " + e.getMessage());
        }

        // Return the list of questions, which may be empty if no questions were found
        return questions01;
    }


    */


    public List<Question> fetchRandomQuestions(String topic, int numberOfQuestions, String difficulty) throws SQLException {

       // System.out.println("Number of Questions: " + numberOfQuestions);

        List<Question> questions = new ArrayList<>();

       // String query = "SELECT * FROM t_Question WHERE Topic = ? AND  Difficulty_level = ? ORDER BY RAND() LIMIT ?";
        String query = "SELECT Question_text, OptionA, OptionB, OptionC, OptionD, Correct_answer " +
                "FROM t_question " +
                "WHERE Topic = ? AND Difficulty_level = ?  AND Record_status = 'Active' " +
                "ORDER BY RAND() LIMIT ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, topic);
            stmt.setString(2, difficulty); // Difficulty level ("Easy", "Normal", "Hard")
            stmt.setInt(3, numberOfQuestions); // Number of questions to fetch

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("      Executing query...");
               /* if (!rs.isBeforeFirst()) {  // Check if ResultSet is empty
                    System.out.println("No data found in ResultSet.");
                }*/

                while (rs.next()) {
                    // Assuming you have a Question constructor that matches your result set
                    Question question = new Question(
                           // rs.getInt("QuestionID"),
                            rs.getString("Question_text"),
                            rs.getString("OptionA"),
                            rs.getString("OptionB"),
                            rs.getString("OptionC"),
                            rs.getString("OptionD"),
                            rs.getString("Correct_answer")

                    );
                    System.out.println("....");
                    System.out.println(question);
                    System.out.println("......");
                    questions.add(question);
                }

            }
        }catch (SQLException e){
            e.printStackTrace(); // Print stack trace for detailed error information
            System.out.println("SQL Error: " + e.getMessage());
        }

        return questions;
    }

}
