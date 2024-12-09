package com.example.absencesessect.timetable;

import android.util.Log;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.IOException;

public class PDFReader {

    public interface PDFCallback {
        void onPDFRead(String data);
    }

    public static void readPDF(String filePath, PDFCallback callback) {
        StringBuilder pdfContent = new StringBuilder();
        try {
            PdfReader reader = new PdfReader(filePath);

            // Extract text from each page
            int pageCount = reader.getNumberOfPages();
            for (int i = 1; i <= pageCount; i++) {
                pdfContent.append(PdfTextExtractor.getTextFromPage(reader, i)).append("\n");
            }

            reader.close();
            callback.onPDFRead(pdfContent.toString());
        } catch (IOException e) {
            Log.e("PDFReader", "Error reading PDF file", e);
        }
    }
}
