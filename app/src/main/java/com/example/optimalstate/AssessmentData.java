package com.example.optimalstate;

import com.google.firebase.Timestamp;
import java.util.List;

public class AssessmentData {

    private Timestamp date;   // The timestamp of the assessment
    private String time;      // The time the assessment was taken
    private List<String> feelings; // The feelings selected during the assessment
    private String fullName; // The full name of the user
    private String userId;   // The user ID (email, uid, or another identifier)

    // Constructor to initialize the AssessmentData object
    public AssessmentData(Timestamp date, String time, List<String> feelings, String fullName, String userId) {
        this.date = date;
        this.time = time;
        this.feelings = feelings;
        this.fullName = fullName;
        this.userId = userId;
    }

    // Getters and Setters for each field
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getFeelings() {
        return feelings;
    }

    public void setFeelings(List<String> feelings) {
        this.feelings = feelings;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
