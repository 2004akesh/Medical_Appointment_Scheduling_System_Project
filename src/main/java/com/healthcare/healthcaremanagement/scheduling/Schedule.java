package com.healthcare.healthcaremanagement.scheduling;

public class Schedule {
    private String id;
    private String doctorName;
    private String availableDays;
    private String startTime;
    private String endTime;
    private String roomNumber;
    private int maxPatients;
    private int currentPatients;
    private String status;

    public Schedule() {}

    public Schedule(String id, String doctorName, String availableDays,
                    String startTime, String endTime, String roomNumber,
                    int maxPatients, int currentPatients, String status) {
        this.id = id;
        this.doctorName = doctorName;
        this.availableDays = availableDays;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomNumber = roomNumber;
        this.maxPatients = maxPatients;
        this.currentPatients = currentPatients;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getAvailableDays() { return availableDays; }
    public void setAvailableDays(String availableDays) { this.availableDays = availableDays; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public int getMaxPatients() { return maxPatients; }
    public void setMaxPatients(int maxPatients) { this.maxPatients = maxPatients; }

    public int getCurrentPatients() { return currentPatients; }
    public void setCurrentPatients(int currentPatients) { this.currentPatients = currentPatients; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}