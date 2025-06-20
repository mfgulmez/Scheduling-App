package controller;
import model.Slot;

/**
 * Strategy class to validate room availability by checking
 * for scheduling conflicts based on room, day, and time.
 */

public class RoomStrategy{

    /**
     * Determines whether two slots conflict based on room assignment.
     * A conflict occurs if both slots have the same room scheduled
     * at the same day and time.
     *
     * @param slot The current slot to check.
     * @param otherSlot The other slot to compare against.
     * @return false if a conflict exists; true otherwise.
     */

	protected static boolean compareSlots(Slot slot, Slot otherSlot) {
		if(slot.getDay().equals(otherSlot.getDay()) && slot.getTime().equals(otherSlot.getTime())
		   && slot.getRoom().equals(otherSlot.getRoom())) {
			System.err.println("\033[31mRoom is not available: " + slot.getRoom() + " for " + slot.toString() + "\033[0m");
			return false;
		}
		return true;
	}

}
