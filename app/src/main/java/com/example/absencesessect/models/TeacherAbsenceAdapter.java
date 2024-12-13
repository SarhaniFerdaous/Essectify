package com.example.absencesessect.models;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.absencesessect.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TeacherAbsenceAdapter extends BaseTeacherAbsenceAdapter {

    private FirebaseFirestore db;

    public TeacherAbsenceAdapter(Context context, List<TeacherAbsence> absenceList) {
        super(context, absenceList);
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_absence, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseTeacherAbsenceAdapter.ViewHolder holder, int position) {
        TeacherAbsence absence = absenceList.get(position);

        // Bind common fields using the base adapter method
        bindCommonFields(holder, absence);

        // Bind teacher-specific fields
        ViewHolder viewHolder = (ViewHolder) holder;  // Cast to TeacherAbsenceAdapter.ViewHolder
        viewHolder.teacherName.setText(absence.getTeacherName());
        viewHolder.teacherEmail.setText(absence.getTeacherEmail());

        // Handle delete button functionality
        viewHolder.deleteButton.setOnClickListener(v -> deleteAbsence(absence));
    }

    private void deleteAbsence(TeacherAbsence absence) {
        new android.app.AlertDialog.Builder(context)
                .setTitle("Delete Absence")
                .setMessage("Are you sure you want to delete this absence?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.collection("absences")
                            .document(absence.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                absenceList.remove(absence);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Absence deleted successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Failed to delete absence", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            });
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static class ViewHolder extends BaseTeacherAbsenceAdapter.ViewHolder {
        TextView teacherName, teacherEmail;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.textViewTeacherName);
            teacherEmail = itemView.findViewById(R.id.textViewTeacherEmail);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}


