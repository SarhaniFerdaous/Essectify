package com.example.absencesessect.models;

public class FileData {
    private String fileContent;
    private String fileType;
    private Object uploadTime;

    public FileData(String fileContent, String fileType, Object uploadTime) {
        this.fileContent = fileContent;
        this.fileType = fileType;
        this.uploadTime = uploadTime;
    }

    public String getFileContent() {
        return fileContent;
    }

    public String getFileType() {
        return fileType;
    }

    public Object getUploadTime() {
        return uploadTime;
    }
}
