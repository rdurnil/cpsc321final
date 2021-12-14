package com.durnil.rie.cpsc321final;

import java.util.Date;

public class Workout {
    private int id;
    private String workoutName;
    private String type;
    private String date;
    private String time; // formatted
    private double distance; // in miles

    public Workout(int id, String workoutName, String type, String date, String time, double distance) {
        this.id = id;
        this.workoutName = workoutName;
        this.type = type;
        this.date = date;
        this.time = time;
        this.distance = distance;
    }

    public Workout(String workoutName, String type, String date, String time, double distance) {
        this.id = -1;
        this.workoutName = workoutName;
        this.type = type;
        this.date = date;
        this.time = time;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
