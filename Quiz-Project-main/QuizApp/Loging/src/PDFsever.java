import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * The PDFsever class is responsible for generating a quiz in PDF format.
 * It fetches random questions from a database and generates a PDF file based on the provided topic,
 * number of questions, and other metadata.
 */
public class PDFsever {

    private Connection connection;

    /**
     * Constructor to initialize the PDFsever with a database connection.
     */
    public PDFsever(Connection connection) {
        this.connection = connection;
    }

    /**
     * Generates a quiz in PDF format by fetching random questions based on the specified topic and number of questions.
     * The generated PDF is saved at the specified file path.
     */
    public void generateQuiz(String topic, int numberOfQuestions, String fileName, String quizName,String difficulty) {
        String pdfPath = "C:\\java\\FinalJavaProject\\PDFs" + fileName;

        try {
            // Fetch random questions
            QuestionFetcher fetcher = new QuestionFetcher(connection);
            List<Question> questions = fetcher.fetchRandomQuestions(topic, numberOfQuestions,difficulty);

            // Generate PDF
            PDFGenerator pdfGenerator = new PDFGenerator();
            pdfGenerator.generatePDF(questions, pdfPath,quizName,topic);

            System.out.println("Quiz PDF generated successfully at: " + pdfPath);

        } catch (SQLException | DocumentException | IOException e) {
            e.printStackTrace();

            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
