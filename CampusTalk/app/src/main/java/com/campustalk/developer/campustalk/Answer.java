package com.campustalk.developer.campustalk;

/**
 * Created by PATEL POOJA on 21/04/2016.
 */
public class Answer {
    String answerId;
    String answer;
    String semester;
    String department;
    String name;
    String date;

    public Answer() {
    }

    public Answer(String answerId, String answer, String semester, String department, String name,String date) {
        this.answerId = answerId;
        this.answer = answer;
        this.semester = semester;
        this.department = department;
        this.name = name;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
