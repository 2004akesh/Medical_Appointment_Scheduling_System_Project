package com.healthcare.healthcaremanagement.scheduling;

public class Schedule {
    private String id;
    private String doctorName;
    private String availableDays;
    private String startTime;
    private String endTime;
    private String roomNumber;
    private int currentPatients;
    private String status;
    private int slotDuration;

    public Schedule() {}

    public Schedule(String id, String doctorName, String availableDays,
                    String startTime, String endTime, String roomNumber,
                    int currentPatients, String status, int slotDuration) {
        this.id = id;
        this.doctorName = doctorName;
        this.availableDays = availableDays;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomNumber = roomNumber;
        this.currentPatients = currentPatients;
        this.status = status;
        this.slotDuration = slotDuration;
    }

    // Auto calculate max patients
    public int getMaxPatients() {
        try {
            java.time.LocalTime start = java.time.LocalTime.parse(
                    startTime.toUpperCase(),
                    java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
            java.time.LocalTime end = java.time.LocalTime.parse(
                    endTime.toUpperCase(),
                    java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
            int totalMinutes = (int) java.time.Duration.between(start, end).toMinutes();
            return totalMinutes / slotDuration;
        } catch (Exception e) {
            return 0;
        }
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

    public int getCurrentPatients() { return currentPatients; }
    public void setCurrentPatients(int currentPatients) { this.currentPatients = currentPatients; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getSlotDuration() { return slotDuration; }
    public void setSlotDuration(int slotDuration) { this.slotDuration = slotDuration; }
}