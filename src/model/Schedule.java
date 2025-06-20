package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class representing the overall schedule.
 * Holds a list of scheduled slots and provides access to
 * predefined days and time slots.
 */

public class Schedule {
    private static final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private static final String[] timeSlots = {
        "08:45-09:30", "09:45-10:30", "10:45-11:30", "11:45-12:30",
        "13:30-14:15", "14:30-15:15", "15:30-16:15", "16:30-17:15"
    };

    private static Schedule instance; 
    private final List<Slot> slots;   

    /**
     * Private constructor to enforce singleton usage.
     */

    private Schedule() {
        this.slots = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of Schedule.
     * Initializes it if not already created.
     */

    public static Schedule getInstance() {
        if (instance == null) {
            System.out.println("-- Schedule is being instantiated --");
            instance = new Schedule();
        }
        return instance;
    }

    // Getters and setters for each field

    public List<Slot> getSlots() {
        return slots;
    }

    public String[] getDays() {
        return days;
    }

    public String[] getTimeSlots() {
        return timeSlots;
    }
}
