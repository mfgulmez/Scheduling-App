package model;

/**
 * Represents a scheduled course slot with attributes such as day, time,
 * course code, room, and instructor.
 * Implements Comparable to allow sorting by room name.
 */

public class Slot{

    private String day, time, courseCode, room, instructor;

	/**
     * Constructs a Slot with the specified details.
     */

	public Slot(String day, String time, String courseCode, String room, String instructor) {
		this.day = day;
		this.time = time;
		this.courseCode = courseCode;
		this.room = room;
		this.instructor = instructor;
	}

	// Getters and setters for each field
	
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	@Override
	public String toString() {
		return "Slot [day=" + day + ", time=" + time + ", courseCode=" + courseCode + ", room=" + room
				+ ", instructor=" + instructor + "]\n";
	}


}
