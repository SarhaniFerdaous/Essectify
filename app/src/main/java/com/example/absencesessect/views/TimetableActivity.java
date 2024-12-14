package com.example.absencesessect.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.absencesessect.R;
import com.example.absencesessect.utils.ExcelReader;
import com.example.absencesessect.utils.PDFReader;

public class TimetableActivity extends AppCompatActivity {

    private Button uploadExcelButton;
    private Button uploadPDFButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        uploadExcelButton = findViewById(R.id.uploadExcelButton);
        uploadPDFButton = findViewById(R.id.uploadPDFButton);

        uploadExcelButton.setOnClickListener(v -> {

            loadExcelFile("path/to/excel/file");
        });

        uploadPDFButton.setOnClickListener(v -> {

            loadPDFFile("path/to/pdf/file");
        });

        Toast.makeText(this, "Timetable Activity Loaded", Toast.LENGTH_SHORT).show();
    }

    public void loadExcelFile(String filePath) {
        ExcelReader.readExcel(filePath, data -> {

            Toast.makeText(this, "Excel Data Loaded", Toast.LENGTH_SHORT).show();
        });
    }

    public void loadPDFFile(String filePath) {
        PDFReader.readPDF(filePath, data -> {

            Toast.makeText(this, "PDF Data Loaded", Toast.LENGTH_SHORT).show();
        });
    }
}
