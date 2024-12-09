package com.example.absencesessect.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.absencesessect.dashboard.DashboardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RoleBasedRedirect extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() == null) {
            // Redirect to login if no user is logged in
            startActivity(new Intent(RoleBasedRedirect.this, LoginActivity.class));
            finish();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
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

                        // Redirect based on role
                        switch (role) {
                            case "admin":
                                startActivity(new Intent(RoleBasedRedirect.this, DashboardActivity.class));
                                break;
                            case "agent":
                                startActivity(new Intent(RoleBasedRedirect.this, DashboardActivity.class));
                                break;
                            case "teacher":
                                startActivity(new Intent(RoleBasedRedirect.this, DashboardActivity.class));
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
