package com.healthcare.healthcaremanagement.appointment;

public class Appointment {
    private String id;
    private String patientName;
    private String doctorName;
    private String date;
    private String timeSlot;
    private String status;
    private int urgency; // 1=High, 2=Medium, 3=Low (for priority queue)

    public Appointment() {}

    public Appointment(String id, String patientName, String doctorName,
                       String date, String timeSlot, String status, int urgency) {
        this.id = id;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
        this.urgency = urgency;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getUrgency() { return urgency; }
    public void setUrgency(int urgency) { this.urgency = urgency; }
}