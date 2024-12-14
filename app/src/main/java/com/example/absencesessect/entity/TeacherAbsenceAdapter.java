package com.example.absencesessect.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.absencesessect.R;
import com.example.absencesessect.models.Reclamation;
import com.example.absencesessect.models.TeacherAbsence;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class TeacherAbsenceAdapter extends RecyclerView.Adapter<TeacherAbsenceAdapter.TeacherAbsenceViewHolder> {

    private Context context;
    private List<TeacherAbsence> teacherAbsences;
    private FirebaseFirestore firestore;

    public TeacherAbsenceAdapter(Context context, List<TeacherAbsence> teacherAbsences) {
        this.context = context;
        this.teacherAbsences = teacherAbsences;
        this.firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public TeacherAbsenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_teacher_absence, parent, false);
        return new TeacherAbsenceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TeacherAbsenceViewHolder holder, int position) {
        TeacherAbsence absence = teacherAbsences.get(position);

        holder.classTextView.setText("Class: " + absence.getClassName());
        holder.roomTextView.setText("Room: " + absence.getRoom());
        holder.timeTextView.setText("Time: " + absence.getTime());
        holder.dateTextView.setText("Date: " + absence.getDate());

        // Button click listener to submit reclamation
        holder.submitButton.setOnClickListener(v -> {
            String reclamationText = holder.reclamationEditText.getText().toString().trim();
            if (!reclamationText.isEmpty()) {
                long timestamp = System.currentTimeMillis(); // Use current timestamp for the reclamation
                Reclamation reclamation = new Reclamation(absence.getId(), reclamationText, timestamp);

                // Save reclamation to Firebase
                firestore.collection("reclamations").add(reclamation)
                        .addOnSuccessListener(documentReference -> {
                            // You can add any success message or action after submission
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure (e.g., show a toast or log an error)
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return teacherAbsences.size();
    }

    // ViewHolder class for binding views
    public static class TeacherAbsenceViewHolder extends RecyclerView.ViewHolder {
        TextView classTextView, roomTextView, timeTextView, dateTextView;
        EditText reclamationEditText;
        Button submitButton;

        public TeacherAbsenceViewHolder(View itemView) {
            super(itemView);
            classTextView = itemView.findViewById(R.id.textViewClass);
            roomTextView = itemView.findViewById(R.id.textViewRoom);
            timeTextView = itemView.findViewById(R.id.textViewTime);
            dateTextView = itemView.findViewById(R.id.textViewDate);
            reclamationEditText = itemView.findViewById(R.id.reclamationParagraph);
            submitButton = itemView.findViewById(R.id.submitReclamationButton);
        }
    }
}
