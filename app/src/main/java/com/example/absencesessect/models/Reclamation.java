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

    // Getter for absenceId
    public String getAbsenceId() {
        return absenceId;
    }

    // Setter for absenceId
    public void setAbsenceId(String absenceId) {
        this.absenceId = absenceId;
    }

    // Getter for text
    public String getText() {
        return text;
    }

    // Setter for text
    public void setText(String text) {
        this.text = text;
    }

    // Getter for timestamp
    public long getTimestamp() {
        return timestamp;
    }

    // Setter for timestamp
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
