package com.example.absencesessect.timetable;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.absencesessect.R;

public class TimetableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        // Example logic: Checking functionality for loading timetable
        Toast.makeText(this, "Timetable Activity Loaded", Toast.LENGTH_SHORT).show();
    }

    // Additional methods to integrate file readers
    public void loadExcelFile(String filePath) {
        ExcelReader.readExcel(filePath, data -> {
            // Handle Excel file data here
            Toast.makeText(this, "Excel Data Loaded", Toast.LENGTH_SHORT).show();
        });
    }

    public void loadPDFFile(String filePath) {
        PDFReader.readPDF(filePath, data -> {
            // Handle PDF file data here
            Toast.makeText(this, "PDF Data Loaded", Toast.LENGTH_SHORT).show();
        });
    }
}
