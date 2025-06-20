package controller;
import model.Slot;

/**
 * Strategy class to validate course availability by checking
 * for scheduling conflicts based on course code, day, and time.
 */

public class CourseStrategy {

    /**
     * Determines whether two slots conflict based on course assignment.
     * A conflict occurs if both slots have the same course scheduled
     * at the same day and time.
     *
     * @param slot The current slot to check.
     * @param otherSlot The other slot to compare against.
     * @return false if a conflict exists; true otherwise.
     */
	
	protected static boolean compareSlots(Slot slot, Slot otherSlot) {
		if(slot.getDay().equals(otherSlot.getDay()) && slot.getTime().equals(otherSlot.getTime())
		   && slot.getCourseCode().equals(otherSlot.getCourseCode())) {
			System.err.println("\033[31mCourse is not available: " + slot.getCourseCode() + " for " + slot.toString() + "\033[0m");
			return false;
		}
		return true;
	}

}
