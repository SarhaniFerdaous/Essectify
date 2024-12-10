package com.example.absencesessect.utils;

import com.example.absencesessect.models.TeacherAbsence;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FirestoreHelper {

    public static void getAbsences(Consumer<List<TeacherAbsence>> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("absences")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<TeacherAbsence> absences = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        TeacherAbsence absence = document.toObject(TeacherAbsence.class);
                        absences.add(absence);
                    }
                    callback.accept(absences);
                })
                .addOnFailureListener(e -> callback.accept(new ArrayList<>()));
    }

    public static void saveAbsence(Map<String, Object> absenceData, Consumer<Boolean> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("absences")
                .add(absenceData)
                .addOnSuccessListener(documentReference -> callback.accept(true)) // Success
                .addOnFailureListener(e -> callback.accept(false)); // Failure
    }
}
