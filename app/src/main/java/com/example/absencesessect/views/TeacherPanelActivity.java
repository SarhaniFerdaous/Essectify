package com.example.absencesessect.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absencesessect.R;
import com.example.absencesessect.models.TeacherAbsence;
import com.example.absencesessect.entity.TeacherAbsenceAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherPanelActivity extends AppCompatActivity {

    private static final String TAG = "TeacherPanelActivity";

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private EditText reclamationParagraph;
    private Button submitReclamationButton;
    private RecyclerView recyclerViewOwnAbsences;
    private ProgressBar loadingProgressBar;
    private TextView loadingAbsencesText;

    private TeacherAbsenceAdapter absencesAdapter;
    private List<TeacherAbsence> absenceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_panel);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        reclamationParagraph = findViewById(R.id.reclamationParagraph);
        submitReclamationButton = findViewById(R.id.submitReclamationButton);
        recyclerViewOwnAbsences = findViewById(R.id.recyclerViewOwnAbsences);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        loadingAbsencesText = findViewById(R.id.loadingAbsencesText);


        recyclerViewOwnAbsences.setLayoutManager(new LinearLayoutManager(this));
        absencesAdapter = new TeacherAbsenceAdapter(this, absenceList);
        recyclerViewOwnAbsences.setAdapter(absencesAdapter);


        submitReclamationButton.setOnClickListener(v -> submitReclamation());


        loadTeacherAbsences();
    }

    private void submitReclamation() {
        String reclamationText = reclamationParagraph.getText().toString().trim();

        if (TextUtils.isEmpty(reclamationText)) {
            Toast.makeText(this, "Please write your reclamation!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> reclamationData = new HashMap<>();
        reclamationData.put("reclamationText", reclamationText);
        reclamationData.put("userName", currentUser.getDisplayName());
        reclamationData.put("userEmail", currentUser.getEmail());
        reclamationData.put("userUID", currentUser.getUid());
        reclamationData.put("timestamp", System.currentTimeMillis());

        firestore.collection("reclamation")
                .add(reclamationData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Reclamation submitted successfully!", Toast.LENGTH_SHORT).show();
                    reclamationParagraph.setText(""); // Clear input field
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error submitting reclamation", e);
                    Toast.makeText(this, "Failed to submit reclamation. Try again!", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadTeacherAbsences() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userEmail = currentUser.getEmail();


        loadingProgressBar.setVisibility(View.VISIBLE);
        loadingAbsencesText.setVisibility(View.VISIBLE);


        firestore.collection("absences")
                .whereEqualTo("teacherEmail", userEmail)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {

                    loadingProgressBar.setVisibility(View.GONE);
                    loadingAbsencesText.setVisibility(View.GONE);

                    if (task.isSuccessful() && task.getResult() != null) {
                        absenceList.clear();

                        if (task.getResult().isEmpty()) {
                            Toast.makeText(this, "No absences found.", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        for (DocumentSnapshot document : task.getResult()) {
                            TeacherAbsence absence = document.toObject(TeacherAbsence.class);
                            if (absence != null) {
                                absenceList.add(absence);
                            }
                        }

                        absencesAdapter.notifyDataSetChanged();
                    } else {
                        Exception exception = task.getException();
                        if (exception != null) {
                            Log.e(TAG, "Error loading absences: ", exception);
                            Toast.makeText(this, "Failed to load absences. Check logs for details.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Unknown error occurred while loading absences");
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(TeacherPanelActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
