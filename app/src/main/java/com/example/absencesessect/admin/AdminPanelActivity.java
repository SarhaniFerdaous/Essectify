package com.example.absencesessect.admin;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.absencesessect.R;

public class AdminPanelActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        db = FirebaseFirestore.getInstance();

        LinearLayout userLayout = findViewById(R.id.absenceDetailsLayout);

        loadUsers(userLayout);
    }

    private void loadUsers(LinearLayout userLayout) {
        CollectionReference usersRef = db.collection("users");

        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    for (DocumentSnapshot document : documents) {
                        String userId = document.getId();
                        String userName = document.getString("name");
                        String userEmail = document.getString("email");
                        String userRole = document.getString("role");

                        addUserToLayout(userLayout, userId, userName, userEmail, userRole);
                    }
                }
            } else {
                Toast.makeText(this, "Error loading users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUserToLayout(LinearLayout userLayout, String userId, String userName, String userEmail, String userRole) {
        View userView = getLayoutInflater().inflate(R.layout.activity_admin_panel, userLayout, false);

        TextView userNameView = userView.findViewById(R.id.userName);
        TextView userEmailView = userView.findViewById(R.id.userEmail);
        Button deleteUserButton = userView.findViewById(R.id.deleteUserButton);
        Button showRolesButton = userView.findViewById(R.id.showRolesButton);
        ScrollView rolesScrollView = userView.findViewById(R.id.rolesScrollView);
        LinearLayout absenceLayout = userView.findViewById(R.id.absenceDetailsLayout);

        userNameView.setText("User Name: " + userName);
        userEmailView.setText("Email: " + userEmail);

        if (userRole == null || userRole.isEmpty()) {
            showRoleSelection(showRolesButton, userId);
        } else {
            showRolesButton.setVisibility(View.GONE);
        }

        deleteUserButton.setOnClickListener(v -> deleteUser(userId, userLayout, userView));

        loadAbsencesForTeacher(userId, absenceLayout);

        userLayout.addView(userView);
    }

    private void showRoleSelection(Button showRolesButton, String userId) {
        showRolesButton.setText("Assign Role");
        showRolesButton.setOnClickListener(v -> {
            // Logic for showing a dialog to select role (Admin, Teacher, Agent)
            String[] roles = {"Admin", "Teacher", "Agent"};

            // Assign role to user in Firebase
            Map<String, Object> updates = new HashMap<>();
            updates.put("role", "selectedRole"); // Replace with selected role

            db.collection("users").document(userId).update(updates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Role assigned successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error assigning role", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void deleteUser(String userId, LinearLayout userLayout, View userView) {
        db.collection("users").document(userId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userLayout.removeView(userView);
                Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error deleting user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAbsencesForTeacher(String userId, LinearLayout absenceLayout) {
        CollectionReference absencesRef = db.collection("absences");

        absencesRef.whereEqualTo("userId", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    for (DocumentSnapshot document : documents) {
                        String date = document.getString("date");
                        String time = document.getString("time");
                        String room = document.getString("room");
                        String className = document.getString("class");

                        addAbsenceToLayout(absenceLayout, date, time, room, className);
                    }
                }
            } else {
                Toast.makeText(this, "Error loading absences", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAbsenceToLayout(LinearLayout absenceLayout, String date, String time, String room, String className) {
        TextView absenceView = new TextView(this);
        absenceView.setText(String.format("Date: %s\nTime: %s\nRoom: %s\nClass: %s", date, time, room, className));
        absenceView.setPadding(8, 8, 8, 8);

        absenceLayout.addView(absenceView);
    }
}
