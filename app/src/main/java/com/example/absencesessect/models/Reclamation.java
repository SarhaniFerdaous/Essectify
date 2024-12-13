package com.example.absencesessect.models;

public class Reclamation {
    private String absenceId;
    private String text;
    private long timestamp;

    public Reclamation(String absenceId, String text, long timestamp) {
        this.absenceId = absenceId;
        this.text = text;
        this.timestamp = timestamp;
    }

    // Getters and setters (if needed)
}
