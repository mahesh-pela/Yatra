package com.example.yatra.Model;

public class HelperClass {
    String fullname, email, password, conpassword;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConpassword() {
        return conpassword;
    }

    public void setConpassword(String conpassword) {
        this.conpassword = conpassword;
    }
    public HelperClass(String fullname, String email, String password, String conpassword) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.conpassword = conpassword;
    }

    public HelperClass(){

    }
}
