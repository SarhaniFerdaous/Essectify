package com.example.absencesessect.models;

public class TeacherAbsence {
    private String teacherName;
    private String date;
    private String reason;
    private String time;
    private int absenceCount;


    public TeacherAbsence() {}

    public TeacherAbsence(String teacherName, String date, String reason, String time) {
        this.teacherName = teacherName;
        this.date = date;
        this.reason = reason;
        this.time = time;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAbsenceCount() {
        return absenceCount;
    }

    public void setAbsenceCount(int absenceCount) {
        this.absenceCount = absenceCount;
    }
}
