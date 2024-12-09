package com.example.absencesessect.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.absencesessect.models.TeacherAbsence;



import com.example.absencesessect.R;

import java.util.List;

public class TeacherAbsenceAdapter extends RecyclerView.Adapter<TeacherAbsenceAdapter.TeacherViewHolder> {

    private final List<TeacherAbsence> teacherAbsences;

    public TeacherAbsenceAdapter(List<TeacherAbsence> teacherAbsences) {
        this.teacherAbsences = teacherAbsences;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_absence, parent, false);
        return new TeacherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        TeacherAbsence teacherAbsence = teacherAbsences.get(position);
        holder.teacherName.setText(teacherAbsence.getTeacherName());
        holder.absenceCount.setText(String.format("Absences: %d", teacherAbsence.getAbsenceCount()));
    }

    @Override
    public int getItemCount() {
        return teacherAbsences.size();
    }

    public static class TeacherViewHolder extends RecyclerView.ViewHolder {
        TextView teacherName, absenceCount;

        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teacherName);
            absenceCount = itemView.findViewById(R.id.absenceCount);
        }
    }
}
