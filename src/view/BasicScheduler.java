package view;
import java.util.List;

import model.Slot;

/**
 * Abstract base class for schedule format generators (Strategy Pattern).
 * 
 * Concrete implementations (e.g., PDFScheduler, ExcelScheduler) extend this class
 * and implement the generateSchedule method for different output formats.
 */

public interface BasicScheduler {

    /**
     * Abstract method to be implemented by subclasses to generate schedules.
     * 
     * @param days       List of days (e.g., Monday to Friday)
     * @param timeSlots  List of time slots in the schedule (e.g., 08:45–09:30)
     * @param slots      List of Slot objects representing scheduled courses
     */

    public void generateSchedule(String[] days, String[] timeSlots, List<Slot> slots);
}