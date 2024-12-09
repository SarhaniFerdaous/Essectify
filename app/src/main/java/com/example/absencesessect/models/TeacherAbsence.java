package com.example.absencesessect.models;


public class TeacherAbsence {

    private String teacherName;
    private int absenceCount;

    public TeacherAbsence() {
        // Required for Firestore
    }

    public TeacherAbsence(String teacherName, int absenceCount) {
        this.teacherName = teacherName;
        this.absenceCount = absenceCount;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getAbsenceCount() {
        return absenceCount;
    }

    public void setAbsenceCount(int absenceCount) {
        this.absenceCount = absenceCount;
    }
}
