import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * The PDFGenerator class is responsible for creating a PDF document based on a list of questions.
 * It generates a quiz PDF, writing the questions, options, and the correct answer to the specified file path.
 */
public class PDFGenerator {

    /**
     * Generates a PDF file that contains a quiz based on the provided list of questions. Each question, along with
     * its multiple-choice options and correct answer, is added to the PDF. The generated file is saved to the specified file path.
     */
    public boolean generatePDF(List<Question> questions, String filePath,String quizName,String topic) throws DocumentException, IOException {
        String FileName = "Quiz_"+quizName +"_"+topic ;
        // Create a new document instance
        Document document = new Document();

        // Initialize PdfWriter to write to the specified file path
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        // Open the document to add content
        document.open();

        // Check if questions list is not empty
        if (questions == null || questions.isEmpty()) {
            document.add(new Paragraph("No questions available to generate PDF."));
            return false ;
        } else {
            // Add each question to the PDF
            for (Question question : questions) {
                document.add(new Paragraph(question.getQuestionText()));
                document.add(new Paragraph("A. " + question.getOptionA()));
                document.add(new Paragraph("B. " + question.getOptionB()));
                document.add(new Paragraph("C. " + question.getOptionC()));
                document.add(new Paragraph("D. " + question.getOptionD()));
               // document.add(new Paragraph("Correct Answer: " + question.getCorrectAnswer()));
                document.add(new Paragraph("\n"));

            }

        }

        // Close the document to finalize the PDF
        document.close();
        return true;
    }

    public boolean generatePDFAns(List<Question> questions, String filePath,String quizName,String topic) throws DocumentException, IOException {
        String FileName = "QuizAns_"+quizName +"_"+topic ;
        // Create a new document instance
        Document document = new Document();

        // Initialize PdfWriter to write to the specified file path
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        // Open the document to add content
        document.open();

        // Check if questions list is not empty
        if (questions == null || questions.isEmpty()) {
            document.add(new Paragraph("No questions available to generate PDF."));
            return false ;
        } else {
            // Add each question to the PDF
            for (Question question : questions) {
                document.add(new Paragraph(question.getQuestionText()));
                document.add(new Paragraph("A. " + question.getOptionA()));
                document.add(new Paragraph("B. " + question.getOptionB()));
                document.add(new Paragraph("C. " + question.getOptionC()));
                document.add(new Paragraph("D. " + question.getOptionD()));
                //document.add(new Paragraph("correctAnswer "+ question.getCorrectAnswer()));
                document.add(new Paragraph("Correct Answer: " + question.getCorrectAnswer()));
                document.add(new Paragraph("\n"));

            }

        }

        // Close the document to finalize the PDF
        document.close();
        return true;
    }
}
