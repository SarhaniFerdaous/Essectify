package com.example.absencesessect.models;

public class TeacherAbsence {
    private String id;  // Firestore document ID
    private String teacherName;
    private String teacherEmail;
    private String date;
    private String time;
    private String room;
    private String className;


    public TeacherAbsence() {
    }

    public TeacherAbsence(String teacherName, String teacherEmail, String date, String time, String room, String className) {
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
        this.date = date;
        this.time = time;
        this.room = room;
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
