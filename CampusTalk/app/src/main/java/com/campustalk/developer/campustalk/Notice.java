package com.campustalk.developer.campustalk;

/**
 * Created by khushali on 12/04/2016.
 */
public class Notice {

    int noticeID;
    String title;
    String description;
    String date;
    String filePath;
    String semester;
    String noticeType;

    public Notice(int noticeID, String title, String description, String date, String filePath, String semester, String noticeType) {
        this.noticeID = noticeID;
        this.title = title;
        this.description = description;
        this.date = date;
        this.filePath = filePath;
        this.semester = semester;
        this.noticeType = noticeType;
    }

    public int getNoticeID() {
        return noticeID;
    }

    public void setNoticeID(int noticeID) {
        this.noticeID = noticeID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }
}
