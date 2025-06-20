package view;

import java.io.FileOutputStream;
import java.util.List;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import model.Slot;

/**
 * Concrete Strategy that generates a weekly course schedule in PDF format.
 * 
 * Uses iText PDF library to construct a readable table representation of the schedule.
 */

public class PDFScheduler implements BasicScheduler{
 
    /**
     * Generates a PDF file named "schedule.pdf" with the weekly schedule table.
     *
     * @param days       List of weekdays to appear as column headers
     * @param timeSlots  List of time slots as row headers
     * @param slots      List of Slot objects containing scheduled course data
     */

    @Override
    public void generateSchedule(String[] days, String[] timeSlots, List<Slot> slots) {
                       
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream( "resources\\output\\schedule.pdf"));
            PdfDocument pdf = new PdfDocument(writer);
            pdf.getDocumentInfo().setTitle("Schedule");
            Document document = new Document(pdf, PageSize.A4.rotate());

            Table table = new Table(days.length + 1);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            PdfFont helveticaBold = PdfFontFactory.createFont("Helvetica-Bold");
            PdfFont helvetica = PdfFontFactory.createFont("Helvetica");

            DeviceRgb coral = new DeviceRgb(255, 128, 128);
            DeviceRgb gray = new DeviceRgb(217, 217, 217);

            Cell cornerCell = new Cell();
            cornerCell.setFont(helveticaBold);
            cornerCell.setFontSize(12);
            cornerCell.setBackgroundColor(gray);
            cornerCell.setBorder(new SolidBorder(1));
            cornerCell.setPaddingLeft(10);
            cornerCell.setPaddingRight(10);
            cornerCell.add(new Paragraph("Hours"));
            table.addHeaderCell(cornerCell);

            for(int i = 0; i < days.length; i++){
                Cell dayCell = new Cell();
                dayCell.setFont(helveticaBold);
                dayCell.setFontSize(12);
                dayCell.setBackgroundColor(coral);
                dayCell.setBorder(new SolidBorder(1));
                dayCell.setPaddingLeft(30);
                dayCell.setPaddingRight(30);
                dayCell.add(new Paragraph(days[i]));
                table.addHeaderCell(dayCell);
            }
            
            
            for(int i = 0; i < timeSlots.length; i++){
       
                for(int j = 0; j < days.length + 1; j++){
                    if(j == 0){
                        Cell timeCell = new Cell();
                        timeCell.setFont(helveticaBold);
                        timeCell.setFontSize(12);
                        timeCell.setBackgroundColor(gray);
                        timeCell.setBorder(new SolidBorder(1));
                        timeCell.setTextAlignment(TextAlignment.CENTER);
                        timeCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
                        timeCell.add(new Paragraph(timeSlots[i]));
                        table.addCell(timeCell);

                    }
                    else{
                        int index = 0;
                        String day = days[j - 1];
                        String timeSlot = timeSlots[i].split("-")[0];
                        Paragraph paragraph = new Paragraph();
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
                                paragraph.add(new Text(s.getRoom() + "     ").setFont(helvetica));
                                paragraph.add(new Text(course + "     ").setFont(helveticaBold));
                                paragraph.add(new Text(instructor + "\n").setFont(helvetica));
                                slots.remove(index);
                            }
                            else{
                                index++;
                            }
                            
                        }
                        if(!paragraph.isEmpty()){
                            Cell slotCell = new Cell();
                            slotCell.setBorder(new SolidBorder(1));
                            slotCell.setFontSize(12);
                            slotCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
                            slotCell.setTextAlignment(TextAlignment.CENTER);
                            slotCell.setPaddingTop(10);
                            slotCell.setPaddingBottom(10);
                            slotCell.add(paragraph);
                            table.addCell(slotCell);
                        }
                        else{
                            Cell emptyCell = new Cell();
                            emptyCell.setBorder(new SolidBorder(1));
                            table.addCell(emptyCell);
                            
                        }
                    }
                }

            }

            document.add(table);
            System.out.println("PDF file "+ pdf.getDocumentInfo().getTitle() + ".pdf created successfully.");
            document.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}