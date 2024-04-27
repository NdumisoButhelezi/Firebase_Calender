package com.example.firebasecalender;

public class Booking {
    private String id;
    private String date;
    private String time;
    private String reason;
    private String status;
    private String patientId;

    // Default constructor required for calls to DataSnapshot.getValue(Booking.class)
    public Booking() {}

    public Booking(String date, String time, String reason, String patientId) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.status = status;
        this.patientId = patientId;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getReason() {
        return reason;
    }

    public String getPatientId() {
        return patientId;
    }
}
