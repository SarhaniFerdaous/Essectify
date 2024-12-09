package com.example.absencesessect.absence;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.absencesessect.R;
import com.example.absencesessect.models.TeacherAbsence;

public class AbsenceDetailsActivity extends AppCompatActivity {

    private TextView teacherNameView, dateView, timeView, reasonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence_details);

        teacherNameView = findViewById(R.id.teacherNameView);
        dateView = findViewById(R.id.dateView);
        timeView = findViewById(R.id.timeView);
        reasonView = findViewById(R.id.reasonView);

        TeacherAbsence absence = (TeacherAbsence) getIntent().getSerializableExtra("absence");
        if (absence != null) {
            teacherNameView.setText(absence.getTeacherName());
            dateView.setText(absence.getDate());
            timeView.setText(absence.getTime());
            reasonView.setText(absence.getReason());
        }
    }
}
