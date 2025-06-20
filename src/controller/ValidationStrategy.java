package controller;
import model.Slot;

/**
 * Strategy class for validating the format and values of a Slot's day and time.
 * Ensures that both fields conform to allowed constants.
 */

public class ValidationStrategy{
    /**
     * Checks whether a given slot has a valid day and time.
     * Valid days are Monday through Sunday, and valid times match predefined slots.
     *
     * @param slot The slot to validate.
     * @return true if both day and time are valid; false otherwise.
     */
    protected static boolean checkSlot(Slot slot){
        String day = slot.getDay().toLowerCase();
        
        boolean validDay = day.equals("monday") || day.equals("tuesday") || day.equals("wednesday") || 
                           day.equals("thursday") || day.equals("friday") || day.equals("saturday") || 
                           day.equals("sunday");
        
        boolean validTime = slot.getTime().equals("08:45") || slot.getTime().equals("09:45") 
                            || slot.getTime().equals("10:45") || slot.getTime().equals("11:45") 
                            || slot.getTime().equals("13:30") || slot.getTime().equals("14:30") 
                            || slot.getTime().equals("15:30") || slot.getTime().equals("16:30");

        if(!validDay){
            System.err.println(slot.getDay() + " is not valid for " + slot.toString() + "!");
        }

        if(!validTime){
            System.err.println(slot.getTime() + " is not valid for " + slot.toString() + "!");
        }

        return validDay && validTime;
    }
}