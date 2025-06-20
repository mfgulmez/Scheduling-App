package view;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import model.Slot;

/**
 * Concrete Strategy that generates a weekly course schedule in Excel format (.xlsx).
 * 
 * Implements the generateSchedule method to write the provided schedule data into a spreadsheet.
 */

public class ExcelScheduler implements BasicScheduler{

    /**
     * Generates an Excel file named "schedule.xlsx" with the given schedule.
     *
     * @param days       List of weekdays to appear as column headers (e.g., Monday–Friday)
     * @param timeSlots  List of time slots (e.g., 08:45–09:30) as row headers
     * @param slots      List of Slot objects containing scheduled course data
     */

    @Override
    public void generateSchedule(String[] days, String[] timeSlots, List<Slot> slots) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("schedule");

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);     
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER); 

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);     
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); 

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell cornerCell = header.createCell(0);
        cornerCell.setCellValue("Hours");
        cornerCell.setCellStyle(headerStyle);
        
        sheet.setColumnWidth(0, 4000);
        for(int i = 0; i < days.length; i++){
            sheet.setColumnWidth(i + 1, 6000);
            Cell dayCell = header.createCell(i+1);
            dayCell.setCellValue(days[i]);
            dayCell.setCellStyle(headerStyle);
        }

        for(int i = 0; i < timeSlots.length; i++){
            Row row = sheet.createRow(i + 1);
            for(int j = 0; j < days.length + 1; j++){
                if(j == 0){
                    Cell timeSlotCell = row.createCell(0);
                    timeSlotCell.setCellStyle(cellStyle);
                    timeSlotCell.setCellValue(timeSlots[i]);
                }
                else{
                    String timeSlot = timeSlots[i].split("-")[0];
                    String day = days[j - 1];
                    int index = 0;
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    String courses = "";
                    while(index < slots.size()){
                        Slot s = slots.get(index);
                        String course = s.getCourseCode().split("\\(")[0];
                        String instructor = "";
                        
                        
                        for(char c: s.getInstructor().toCharArray()){
                            if(Character.isUpperCase(c)){
                                instructor += c;
                            }
                        }
                        if (s.getDay().equalsIgnoreCase(day) && s.getTime().equals(timeSlot)) {
                            courses += " " + s.getRoom() + "    "
                                    +  course + "    "
                                    +  instructor + " "
                                       + "\n";
                            slots.remove(index);
                        }
                        else{
                            index++;
                        }
                    }
                    if(courses.length() > 0){
                        cell.setCellValue(courses);
                    }
                }


            }
        }
        try (FileOutputStream outputStream = new FileOutputStream( "resources\\output\\schedule.xlsx")) {
            workbook.write(outputStream);
            workbook.close();
            System.out.println("Excel file "+ sheet.getSheetName() + ".xlsx created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
