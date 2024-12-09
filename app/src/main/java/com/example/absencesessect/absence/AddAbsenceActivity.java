package com.example.absencesessect.absence;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.absencesessect.R;
import com.example.absencesessect.utils.FirestoreHelper;
import java.util.HashMap;
import java.util.Map;

public class AddAbsenceActivity extends AppCompatActivity {

    private EditText teacherNameInput, dateInput, timeInput, reasonInput;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_absence);

        teacherNameInput = findViewById(R.id.teacherNameInput);
        dateInput = findViewById(R.id.dateInput);
        timeInput = findViewById(R.id.timeInput);
        reasonInput = findViewById(R.id.reasonInput);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> saveAbsence());
    }

    private void saveAbsence() {
        String teacherName = teacherNameInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String time = timeInput.getText().toString().trim();
        String reason = reasonInput.getText().toString().trim();

        if (teacherName.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> absenceData = new HashMap<>();
        absenceData.put("teacherName", teacherName);
        absenceData.put("date", date);
        absenceData.put("time", time);
        absenceData.put("reason", reason);

        FirestoreHelper.saveAbsence(absenceData, success -> {
            if (success) {
                Toast.makeText(this, "Absence saved successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save absence!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
