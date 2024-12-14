package com.example.absencesessect.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.absencesessect.R;
import com.example.absencesessect.models.TeacherAbsence;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.AbsenceViewHolder> {

    private Context context;
    private List<TeacherAbsence> absenceList;
    private FirebaseFirestore db;

    public AgentAdapter(Context context, List<TeacherAbsence> absenceList) {
        this.context = context;
        this.absenceList = absenceList;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public AbsenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_absence, parent, false);
        return new AbsenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AbsenceViewHolder holder, int position) {
        TeacherAbsence absence = absenceList.get(position);

        // Set the text for each field
        holder.classNameTextView.setText("Class: " + absence.getClassName());
        holder.roomTextView.setText("Room: " + absence.getRoom());
        holder.timeTextView.setText("Time: " + absence.getTime());
        holder.dateTextView.setText("Date: " + absence.getDate());
        holder.teacherNameTextView.setText("Teacher: " + absence.getTeacherName());

        // Set the delete button action
        holder.deleteButton.setOnClickListener(v -> deleteAbsence(absence, position));
    }

    @Override
    public int getItemCount() {
        return absenceList.size();
    }

    private void deleteAbsence(TeacherAbsence absence, int position) {
        // Delete the absence from Firestore
        db.collection("absences")
                .document(absence.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Absence deleted successfully!", Toast.LENGTH_SHORT).show();
                    absenceList.remove(position);
                    notifyItemRemoved(position);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to delete absence", Toast.LENGTH_SHORT).show();
                });
    }

    public static class AbsenceViewHolder extends RecyclerView.ViewHolder {

        TextView classNameTextView;
        TextView roomTextView;
        TextView timeTextView;
        TextView dateTextView;
        TextView teacherNameTextView;
        Button deleteButton;

        public AbsenceViewHolder(View itemView) {
            super(itemView);
            classNameTextView = itemView.findViewById(R.id.textViewClass);
            roomTextView = itemView.findViewById(R.id.textViewRoom);
            timeTextView = itemView.findViewById(R.id.textViewTime);
            dateTextView = itemView.findViewById(R.id.textViewDate);
            teacherNameTextView = itemView.findViewById(R.id.textViewTeacherName);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }

}
