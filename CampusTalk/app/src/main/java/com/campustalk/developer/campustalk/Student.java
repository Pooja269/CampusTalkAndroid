package com.campustalk.developer.campustalk;

/**
 * Created by khushali on 22/04/2016.
 */
public class Student {

    String studname;
    String gender;
    String dob;
    String imagePath;
    String enrollment;
    String passingYear;
    String phone;
    String altPhone;
    String email;
    String projectDetails;
    String trainingDetails;
    String otherDetails;
    String studdepartment;
    String studsemester;

    public Student() {
    }

    public Student( String studname, String gender, String dob, String imagePath, String enrollment, String passingYear, String phone, String altPhone, String email, String projectDetails, String trainingDetails, String otherDetails, String studdepartment, String studsemester) {
        this.studname = studname;
        this.gender = gender;
        this.dob = dob;
        this.imagePath = imagePath;
        this.enrollment = enrollment;
        this.passingYear = passingYear;
        this.phone = phone;
        this.altPhone = altPhone;
        this.email = email;
        this.projectDetails = projectDetails;
        this.trainingDetails = trainingDetails;
        this.otherDetails = otherDetails;
        this.studdepartment = studdepartment;
        this.studsemester = studsemester;
    }


    public String getStudname() {
        return studname;
    }

    public void setStudname(String studname) {
        this.studname = studname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(String passingYear) {
        this.passingYear = passingYear;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAltPhone() {
        return altPhone;
    }

    public void setAltPhone(String altPhone) {
        this.altPhone = altPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(String projectDetails) {
        this.projectDetails = projectDetails;
    }

    public String getTrainingDetails() {
        return trainingDetails;
    }

    public void setTrainingDetails(String trainingDetails) {
        this.trainingDetails = trainingDetails;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public String getStuddepartment() {
        return studdepartment;
    }

    public void setStuddepartment(String studdepartment) {
        this.studdepartment = studdepartment;
    }

    public String getStudsemester() {
        return studsemester;
    }

    public void setStudsemester(String studsemester) {
        this.studsemester = studsemester;
    }
}
