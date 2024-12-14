package com.example.absencesessect.entity;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.absencesessect.R;
import com.example.absencesessect.models.TeacherAbsence;

import java.util.List;

public abstract class BaseTeacherAbsenceAdapter extends RecyclerView.Adapter<BaseTeacherAbsenceAdapter.ViewHolder> {
    protected Context context;
    protected List<TeacherAbsence> absenceList;

    public BaseTeacherAbsenceAdapter(Context context, List<TeacherAbsence> absenceList) {
        this.context = context;
        this.absenceList = absenceList;
    }

    @Override
    public int getItemCount() {
        return absenceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, room, className;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textViewDate);
            time = itemView.findViewById(R.id.textViewTime);
            room = itemView.findViewById(R.id.textViewRoom);
            className = itemView.findViewById(R.id.textViewClass);
        }
    }

    public void bindCommonFields(ViewHolder holder, TeacherAbsence absence) {
        // Bind the common fields from the TeacherAbsence object to the ViewHolder
        holder.date.setText(absence.getDate());
        holder.time.setText(absence.getTime());
        holder.room.setText(absence.getRoom());
        holder.className.setText(absence.getClassName());
    }

}
