package com.example.nobulijava.model;

public class ReportRecipientObj {
    private String name;
    private String email;

    public ReportRecipientObj(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public ReportRecipientObj() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return '\n' + name + '\n' + "Email: " + email + '\n';
    }
}
