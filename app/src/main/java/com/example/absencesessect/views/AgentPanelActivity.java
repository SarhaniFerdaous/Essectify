package com.example.absencesessect.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absencesessect.R;
import com.example.absencesessect.models.TeacherAbsence;
import com.example.absencesessect.entity.AgentAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgentPanelActivity extends AppCompatActivity {

    private EditText editTeacherName, editTeacherEmail, editTextDate, editTextTime, editTextRoom, editTextClass;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private AgentAdapter adapter;
    private List<TeacherAbsence> absenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_panel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();

        editTeacherName = findViewById(R.id.editTeacherName);
        editTeacherEmail = findViewById(R.id.editTeacherEmail);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextRoom = findViewById(R.id.editTextRoom);
        editTextClass = findViewById(R.id.editTextClass);
        recyclerView = findViewById(R.id.recyclerViewAbsences);

        absenceList = new ArrayList<>();
        adapter = new AgentAdapter(this, absenceList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.buttonSubmit).setOnClickListener(v -> submitAbsence());

        editTextDate.setOnClickListener(v -> showDatePicker());

        editTextTime.setOnClickListener(v -> showTimePicker());


        fetchAbsences();
    }

    private void fetchAbsences() {
        db.collection("absences")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<TeacherAbsence> absences = queryDocumentSnapshots.toObjects(TeacherAbsence.class);
                    absenceList.clear();
                    absenceList.addAll(absences);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load absences", Toast.LENGTH_SHORT).show());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth1) -> {
            String date = year1 + "-" + (month1 + 1) + "-" + dayOfMonth1;
            editTextDate.setText(date);
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String time = hourOfDay + ":" + String.format("%02d", minute1);
            editTextTime.setText(time);
        }, hour, minute, true);

        timePickerDialog.show();
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

        TeacherAbsence absence = new TeacherAbsence(name, email, date, time, room, className);

        db.collection("absences")
                .add(absence)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Absence added successfully!", Toast.LENGTH_SHORT).show();
                    absence.setId(documentReference.getId());
                    absenceList.add(absence);
                    adapter.notifyDataSetChanged();

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

    public void deleteAbsence(final TeacherAbsence absence) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this absence?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.collection("absences")
                            .document(absence.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Absence deleted", Toast.LENGTH_SHORT).show();
                                absenceList.remove(absence);
                                adapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete absence", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(AgentPanelActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
