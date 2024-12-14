package com.example.absencesessect.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absencesessect.R;
import com.example.absencesessect.entity.AdminAdapter;
import com.example.absencesessect.entity.TeacherAbsenceAdapter;
import com.example.absencesessect.models.TeacherAbsence;
import com.example.absencesessect.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

public class AdminPanelActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerViewUsers;
    private RecyclerView recyclerViewTeacherAbsences;
    private AdminAdapter adminAdapter;
    private TeacherAbsenceAdapter absenceAdapter;

    private EditText editTextName, editTextEmail;
    private Spinner spinnerRole;
    private Button buttonCreateUser;

    private ListenerRegistration usersListener;
    private ListenerRegistration absencesListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewTeacherAbsences = findViewById(R.id.recyclerViewTeacherAbsences);
        recyclerViewTeacherAbsences.setLayoutManager(new LinearLayoutManager(this));

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerRole = findViewById(R.id.spinnerRole);
        buttonCreateUser = findViewById(R.id.buttonCreateUser);


        loadUsers();

        buttonCreateUser.setOnClickListener(v -> createUser());

        Button timetableButton = findViewById(R.id.buttonTimetable);
        timetableButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, TimetableActivity.class);
            startActivity(intent);
        });
    }

    private void loadUsers() {

        usersListener = db.collection("users")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading users", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (querySnapshot != null) {
                        List<User> users = querySnapshot.toObjects(User.class);
                        adminAdapter = new AdminAdapter(AdminPanelActivity.this, users);
                        recyclerViewUsers.setAdapter(adminAdapter);
                    }
                });
    }

    private void createUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = db.collection("users").document().getId();
        User newUser = new User(userId, name, email, role);

        db.collection("users").add(newUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error creating user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showTeacherAbsences(String teacherEmail) {

        if (absencesListener != null) {
            absencesListener.remove();
        }

        absencesListener = db.collection("absences")
                .whereEqualTo("teacherEmail", teacherEmail)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading absences", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (querySnapshot != null) {
                        List<TeacherAbsence> absences = querySnapshot.toObjects(TeacherAbsence.class);
                        absenceAdapter = new TeacherAbsenceAdapter(AdminPanelActivity.this, absences);
                        recyclerViewTeacherAbsences.setAdapter(absenceAdapter);
                        recyclerViewTeacherAbsences.setVisibility(View.VISIBLE);  // Show absences RecyclerView
                    } else {
                        recyclerViewTeacherAbsences.setVisibility(View.GONE);  // Hide if no absences found
                        Toast.makeText(this, "No absences found for this teacher", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(AdminPanelActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (usersListener != null) {
            usersListener.remove();
        }
        if (absencesListener != null) {
            absencesListener.remove();
        }
    }
}
