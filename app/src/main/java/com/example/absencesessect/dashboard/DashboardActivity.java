package com.example.absencesessect.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absencesessect.R;
import com.example.absencesessect.absence.AddAbsenceActivity;
import com.example.absencesessect.utils.FirestoreHelper;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.absencesessect.models.TeacherAbsence;


import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TeacherAbsenceAdapter adapter;
    private List<TeacherAbsence> teacherAbsenceList;
    private FirebaseFirestore db;
    private Button addAbsenceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = findViewById(R.id.recyclerView);
        addAbsenceButton = findViewById(R.id.addAbsenceButton);

        teacherAbsenceList = new ArrayList<>();
        adapter = new TeacherAbsenceAdapter(teacherAbsenceList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Load data from Firestore
        loadTeacherAbsences();

        // Navigate to Add Absence screen
        addAbsenceButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AddAbsenceActivity.class);
            startActivity(intent);
        });
    }

    private void loadTeacherAbsences() {
        db.collection("absences")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        teacherAbsenceList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TeacherAbsence absence = document.toObject(TeacherAbsence.class);
                            teacherAbsenceList.add(absence);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(DashboardActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
