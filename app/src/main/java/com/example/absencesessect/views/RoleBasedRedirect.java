package com.example.absencesessect.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.absencesessect.R;


public class RoleBasedRedirect extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_based_redirect);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loadingIndicator = findViewById(R.id.loading_indicator); // Loading progress bar

        if (mAuth.getCurrentUser() == null) {
            // Redirect to login if no user is logged in
            startActivity(new Intent(RoleBasedRedirect.this, LoginActivity.class));
            finish();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        // Show loading indicator
        loadingIndicator.setVisibility(View.VISIBLE);

        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    // Hide loading indicator when the task completes
                    loadingIndicator.setVisibility(View.GONE);

                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        String role = document.getString("role");

                        if (role == null) {
                            Toast.makeText(this, "No role assigned. Contact admin.", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(RoleBasedRedirect.this, LoginActivity.class));
                            finish();
                            return;
                        }

                        // Redirect based on the user's role
                        switch (role) {
                            case "admin":
                                startActivity(new Intent(RoleBasedRedirect.this, AdminPanelActivity.class));
                                break;
                            case "agent":
                                startActivity(new Intent(RoleBasedRedirect.this, AgentPanelActivity.class));
                                break;
                            case "teacher":
                                startActivity(new Intent(RoleBasedRedirect.this, TeacherPanelActivity.class));
                                break;
                            default:
                                Toast.makeText(this, "Unknown role. Contact admin.", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(RoleBasedRedirect.this, LoginActivity.class));
                                break;
                        }

                        finish();
                    } else {
                        Toast.makeText(this, "Error fetching user role.", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(RoleBasedRedirect.this, LoginActivity.class));
                        finish();
                    }
                });
    }
}
