package com.example.absencesessect.absence;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.absencesessect.R;
import com.example.absencesessect.models.TeacherAbsence;
import com.example.absencesessect.utils.FirestoreHelper;
import java.util.ArrayList;
import java.util.List;

public class ManageAbsencesActivity extends AppCompatActivity {

    private RecyclerView absencesRecyclerView;
    private AbsenceAdapter absenceAdapter;
    private List<TeacherAbsence> absenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_absences);

        absencesRecyclerView = findViewById(R.id.absencesRecyclerView);
        absencesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        absenceList = new ArrayList<>();
        absenceAdapter = new AbsenceAdapter(absenceList);
        absencesRecyclerView.setAdapter(absenceAdapter);

        loadAbsences();
    }

    private void loadAbsences() {
        FirestoreHelper.getAbsences(absences -> {
            absenceList.clear();
            absenceList.addAll(absences);
            absenceAdapter.notifyDataSetChanged();
        });
    }
}
