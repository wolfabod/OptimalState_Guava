package com.example.optimalstate;

public class UserData {
    private String fullName;
    private String email;
    private boolean provider;

    public UserData() {
        // Default constructor required for Firestore
    }

    public UserData(String fullName, String email, boolean provider) {
        this.fullName = fullName;
        this.email = email;
        this.provider = provider;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isProvider() {
        return provider;
    }

    public void setProvider(boolean provider) {
        this.provider = provider;
    }
}
