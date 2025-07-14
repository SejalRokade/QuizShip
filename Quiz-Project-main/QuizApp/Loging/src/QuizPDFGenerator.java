import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

public class QuizPDFGenerator {

    public static File generateQuizPDF(String quizName, java.util.List<String> questions, String batchName) throws IOException {
        String fileName = quizName + "_" + batchName + ".pdf";
        File pdfFile = new File(fileName);

        // Initialize PDF writer and document
        PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Add quiz and batch information
        document.add(new Paragraph("Quiz Name: " + quizName));
        document.add(new Paragraph("Batch: " + batchName));
        document.add(new Paragraph("\nQuestions:"));

        // Add list of questions
        List questionList = new List();
        for (String question : questions) {
            questionList.add(new ListItem(question));
        }
        document.add(questionList);

        // Close the document
        document.close();
        System.out.println("PDF file generated: " + pdfFile.getAbsolutePath());
        return pdfFile;
    }
}
