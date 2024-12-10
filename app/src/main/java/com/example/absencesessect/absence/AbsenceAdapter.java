package com.example.absencesessect.absence;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absencesessect.R;
import com.example.absencesessect.models.TeacherAbsence;

import java.util.List;

public class AbsenceAdapter extends RecyclerView.Adapter<AbsenceAdapter.AbsenceViewHolder> {

    private List<TeacherAbsence> absenceList;

    public AbsenceAdapter(List<TeacherAbsence> absenceList) {
        this.absenceList = absenceList;
    }

    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_teacher_absence, parent, false);
        return new AbsenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {
        TeacherAbsence absence = absenceList.get(position);
        holder.teacherNameTextView.setText(absence.getTeacherName());
        holder.dateTextView.setText(absence.getDate());
        holder.reasonTextView.setText(absence.getReason());
    }

    @Override
    public int getItemCount() {
        return absenceList.size();
    }

    static class AbsenceViewHolder extends RecyclerView.ViewHolder {
        TextView teacherNameTextView, dateTextView, reasonTextView;

        public AbsenceViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherNameTextView = itemView.findViewById(R.id.teacherName);
            dateTextView = itemView.findViewById(R.id.date);
            reasonTextView = itemView.findViewById(R.id.reason);
        }
    }
} 