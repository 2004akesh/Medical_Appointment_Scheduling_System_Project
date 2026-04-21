package com.healthcare.healthcaremanagement.feedback;

public class Feedback {
    private String id;
    private String patientName;
    private String doctorName;
    private String message;
    private int rating; // 1-5
    private String date;

    public Feedback() {}

    public Feedback(String id, String patientName, String doctorName,
                    String message, int rating, String date) {
        this.id = id;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.message = message;
        this.rating = rating;
        this.date = date;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}