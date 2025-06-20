package controller;
import model.Slot;

/**
 * Strategy class to compare two Slot objects based on multiple criterias
 * that makes the query unique among others
 */

public class CompareStrategy {
    
    /**
     * Checks whether two slots are compatible by comparing their course,
     * instructor, and room assignments using their respective strategies.
     *
     * @param slot The first slot to compare.
     * @param otherSlot The second slot to compare.
     * @return true if all strategy comparisons return true; false otherwise.
     */

    protected static boolean compareSlots(Slot slot, Slot otherSlot){
        return CourseStrategy.compareSlots(slot, otherSlot) && InstructorStrategy.compareSlots(slot, otherSlot)
            && RoomStrategy.compareSlots(slot, otherSlot);
    }
    
}
