package com.example.nobulijava.model;

public class UserObj {
    private String email;
    private boolean isAdmin;
    private String userID;


    public UserObj(String email, boolean isAdmin, String userID) {
        this.email = email;
        this.isAdmin = isAdmin;
        this.userID = userID;
    }

    public UserObj(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "UserObj{" +
                "email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", userID='" + userID + '\'' +
                '}';
    }
}
