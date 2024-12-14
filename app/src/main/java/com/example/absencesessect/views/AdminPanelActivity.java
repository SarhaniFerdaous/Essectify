package com.example.absencesessect.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absencesessect.R;
import com.example.absencesessect.entity.AdminAdapter;
import com.example.absencesessect.entity.TeacherAbsenceAdapter;
import com.example.absencesessect.models.TeacherAbsence;
import com.example.absencesessect.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AdminPanelActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerViewUsers;
    private RecyclerView recyclerViewTeacherAbsences;
    private AdminAdapter adminAdapter;
    private TeacherAbsenceAdapter absenceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        db = FirebaseFirestore.getInstance();

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewTeacherAbsences = findViewById(R.id.recyclerViewTeacherAbsences);
        recyclerViewTeacherAbsences.setLayoutManager(new LinearLayoutManager(this));

        loadUsers();

        Button timetableButton = findViewById(R.id.buttonTimetable);
        timetableButton.setOnClickListener(v -> {
            // Redirect to TimetableActivity
            Intent intent = new Intent(AdminPanelActivity.this, TimetableActivity.class);
            startActivity(intent);
        });
    }

    private void loadUsers() {
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    List<User> users = querySnapshot.toObjects(User.class);
                    adminAdapter = new AdminAdapter(AdminPanelActivity.this, users);
                    recyclerViewUsers.setAdapter(adminAdapter);
                }
            } else {
                Toast.makeText(this, "Error loading users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showTeacherAbsences(String teacherEmail) {
        // Load absences for the selected teacher and show the absence RecyclerView
        db.collection("absences")
                .whereEqualTo("teacherEmail", teacherEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            List<TeacherAbsence> absences = querySnapshot.toObjects(TeacherAbsence.class);
                            absenceAdapter = new TeacherAbsenceAdapter(AdminPanelActivity.this, absences);
                            recyclerViewTeacherAbsences.setAdapter(absenceAdapter);
                            recyclerViewTeacherAbsences.setVisibility(View.VISIBLE);  // Show absences RecyclerView
                        } else {
                            recyclerViewTeacherAbsences.setVisibility(View.GONE);  // Hide if no absences found
                            Toast.makeText(this, "No absences found for this teacher", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error loading absences", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
