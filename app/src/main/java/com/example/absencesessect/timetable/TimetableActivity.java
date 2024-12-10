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
