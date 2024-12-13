package com.example.absencesessect.agent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absencesessect.R;
import com.example.absencesessect.models.TeacherAbsence;
import com.example.absencesessect.models.TeacherAbsenceAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AgentPanelActivity extends AppCompatActivity {

    private EditText editTeacherName, editTeacherEmail, editTextDate, editTextTime, editTextRoom, editTextClass;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private TeacherAbsenceAdapter adapter;
    private List<TeacherAbsence> absenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_panel);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        editTeacherName = findViewById(R.id.editTeacherName);
        editTeacherEmail = findViewById(R.id.editTeacherEmail);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextRoom = findViewById(R.id.editTextRoom);
        editTextClass = findViewById(R.id.editTextClass);
        recyclerView = findViewById(R.id.recyclerViewAbsences);

        // Set up RecyclerView
        absenceList = new ArrayList<>();
        adapter = new TeacherAbsenceAdapter(this, absenceList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Handle Submit Button
        findViewById(R.id.buttonSubmit).setOnClickListener(v -> submitAbsence());
    }

    private void submitAbsence() {
        String name = editTeacherName.getText().toString().trim();
        String email = editTeacherEmail.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String room = editTextRoom.getText().toString().trim();
        String className = editTextClass.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(date) ||
                TextUtils.isEmpty(time) || TextUtils.isEmpty(room) || TextUtils.isEmpty(className)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create absence object
        TeacherAbsence absence = new TeacherAbsence(name, email, date, time, room, className);

        // Save to Firestore
        db.collection("absences")
                .add(absence)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Absence added successfully!", Toast.LENGTH_SHORT).show();
                    absence.setId(documentReference.getId());
                    absenceList.add(absence);
                    adapter.notifyDataSetChanged();

                    // Clear input fields
                    editTeacherName.setText("");
                    editTeacherEmail.setText("");
                    editTextDate.setText("");
                    editTextTime.setText("");
                    editTextRoom.setText("");
                    editTextClass.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add absence", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }
}
