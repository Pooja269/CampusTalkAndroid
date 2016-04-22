package com.campustalk.developer.campustalk;

/**
 * Created by shahdravya01 on 20-Apr-16.
 */
public class Blog {

    String blogId;
    String blogname;
    String blogDate;
    String blogEnrollment;
    String blogTitle;
    String blogCategory;
    String blogImagePath;

    public Blog(){}

    public Blog(String blogId, String blogname, String blogDate, String blogEnrollment, String blogTitle, String blogCategory, String blogImagePath) {
        this.blogId = blogId;
        this.blogname = blogname;
        this.blogDate = blogDate;
        this.blogEnrollment = blogEnrollment;
        this.blogTitle = blogTitle;
        this.blogCategory = blogCategory;
        this.blogImagePath = blogImagePath;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getBlogname() {
        return blogname;
    }

    public void setBlogname(String blogname) {
        this.blogname = blogname;
    }

    public String getBlogDate() {
        return blogDate;
    }

    public void setBlogDate(String blogDate) {
        this.blogDate = blogDate;
    }

    public String getBlogEnrollment() {
        return blogEnrollment;
    }

    public void setBlogEnrollment(String blogEnrollment) {
        this.blogEnrollment = blogEnrollment;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogCategory() {
        return blogCategory;
    }

    public void setBlogCategory(String blogCategory) {
        this.blogCategory = blogCategory;
    }

    public String getBlogImagePath() {
        return blogImagePath;
    }

    public void setBlogImagePath(String blogImagePath) {
        this.blogImagePath = blogImagePath;
    }
}
