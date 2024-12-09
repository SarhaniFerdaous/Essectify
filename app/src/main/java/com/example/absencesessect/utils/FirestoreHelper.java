package com.example.absencesessect.utils;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.Map;

public class FirestoreHelper {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Save absence record
    public static void saveAbsence(Map<String, Object> data, FirestoreCallback callback) {
        db.collection(Constants.COLLECTION_ABSENCES)
                .add(data)
                .addOnSuccessListener(documentReference -> callback.onCallback(true))
                .addOnFailureListener(e -> callback.onCallback(false));
    }

    // Fetch absences (e.g., for the dashboard)
    public static Query getAbsences() {
        return db.collection(Constants.COLLECTION_ABSENCES)
                .orderBy("date", Query.Direction.DESCENDING);
    }

    // Interface for Firestore callbacks
    public interface FirestoreCallback {
        void onCallback(boolean success);
    }
}
