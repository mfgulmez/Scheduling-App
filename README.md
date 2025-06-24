# Scheduling App

A Java-based scheduling application that allows users to generate and export schedules (e.g., classes, work shifts) in PDF or Excel format. Built with JavaFX for GUI interaction and utilizes MVC and Strategy design patterns for extensibility and maintainability.

---

## Features

-  Load schedule data from plain `.txt` files
-  Export generated schedules to **PDF** or **Excel**
-  MVC architecture for clean separation of concerns
-  Strategy pattern for flexible file export mechanisms
-  JavaFX GUI for interactive file selection and format choice
-  Input validation and conflict checking

##  Technologies Used

- Java SE
- JavaFX (GUI)
- Apache POI (Excel support)
- iText (PDF support)
- Java I/O (File handling)
- MVC & Strategy patterns


## Project Structure

```plaintext
SchedulingApp/
├── controller/
│ └── Scheduler.java # Main controller/view class (JavaFX)
├── model/
│ └── Slot.java # Represents a single schedule slot
├── strategy/
│ ├── ExportStrategy.java # Interface for exporting
│ ├── ExportPDF.java # Concrete strategy for PDF export
│ └── ExportExcel.java # Concrete strategy for Excel export
├── util/
│ └── ParseTxt.java # Utility to parse text file input
├── output/ # Folder for generated PDF/Excel files
└── Main.java # Entry point of the application
```
