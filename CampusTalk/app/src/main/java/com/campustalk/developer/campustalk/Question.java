package com.campustalk.developer.campustalk;

/**
 * Created by PATEL POOJA on 20/04/2016.
 */
public class Question {
    String questionId;
    String questionTitle;
    String name;
    String date;
    String department;
    String semester;

    public Question() {
    }

    public Question(String questionId, String questionTitle, String name, String date, String department, String semester) {
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.name = name;
        this.date = date;
        this.department = department;
        this.semester=semester;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        date = date;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        department = department;
    }
}
