package controller;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.image.BufferedImage;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.io.exceptions.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.ss.usermodel.Cell;

import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.Schedule;
import model.Slot;
import view.ExcelScheduler;
import view.PDFScheduler;
import util.ParseTxt;

/**
 * Scheduler is the main controller responsible for managing file input,
 * conflict validation, schedule generation, and format preview/export.
 * 
 * It provides a JavaFX-based UI that allows users to:
 * - Select instructor TXT files
 * - Choose output format (PDF/Excel)
 * - Export and preview the generated schedule
 */

public class Scheduler{
    private static Scheduler scheduler = null;
    private Schedule schedule = null;
    private PDFScheduler pdfScheduler;
    private ExcelScheduler excelScheduler;
    private List<Path> paths;
    private String outputFormat = "PDF"; 
    private VBox root;
    Label fileLabel;
    Button fileButton, formatButton, previewButton, exportButton;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes schedule instance and UI components.
     */

    private Scheduler(){
        schedule = Schedule.getInstance();
        buildUI();
    }

    /**
     * Returns the singleton instance of Scheduler.
     */

    public static Scheduler getScheduler(){
        if(scheduler == null){
            scheduler = new Scheduler();
        }
        return scheduler;
    }
    
    /**
     * Returns the root UI component for embedding into the scene.
     */

    public Parent getRoot(){
        return root;
    }

    /**
     * Constructs the JavaFX UI with buttons and layout setup.
     * Binds button actions to their respective handler methods.
     */

    private void buildUI() {
        fileLabel = new Label("No file selected");
        fileButton = new Button("Choose TXT File");
        formatButton = new Button("Select Format");
        exportButton = new Button("Export Schedule");
        previewButton = new Button("Preview File");

        fileButton.setOnAction(e -> {
            if (this.selectFiles()) {clearPreview();}
        });

        formatButton.setOnAction(e -> {
            if(this.setFormat()){clearPreview();}
        });

        exportButton.setOnAction(e -> {
            if(this.exportSchedule()){clearPreview();}
        });

        previewButton.setOnAction(e -> {
            this.previewFile();
        });

        previewButton.setDisable(true);

        root = new VBox(15, fileButton, fileLabel, formatButton, exportButton, previewButton);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
    }

    /**
     * Prompts the user to select .txt files and parses them into slots.
     * @return true if files are selected and loaded successfully.
     */

    private boolean selectFiles(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select TXT Files");
        File initialDir = new File("resources\\instructors").getAbsoluteFile();         
        if (initialDir.exists() && initialDir.isDirectory()) {
            chooser.setInitialDirectory(initialDir);                 
        }

        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("TXT Files", "*.txt")
        );
        List<File> files = chooser.showOpenMultipleDialog(null);
        if (files != null && !files.isEmpty()) {
            this.paths = files.stream()
                    .map(File::toPath)
                    .collect(Collectors.toList());
                    
            this.fileLabel.setVisible(false);
            this.fileLabel.setManaged(false);
            schedule.getSlots().clear();
    
            this.handleSlots(paths);
            return true;
        }
        return false;
    }

    /**
     * Opens a dialog to allow the user to choose between PDF or Excel output format.
     * @return true if a format is selected.
     */

    private boolean setFormat(){
        ChoiceDialog<String> dialog = new ChoiceDialog<>("PDF", "PDF", "Excel");
        dialog.setTitle("Select Output Format");
        dialog.setHeaderText("Choose output file format:");
        dialog.setContentText("Format:");
            var result = dialog.showAndWait();

        if (result.isPresent()) {
            outputFormat = result.get();
            return true;  
        }
        return false;
    }

    /**
     * Generates the schedule in the selected format (PDF or Excel).
     * @return true if export is successful.
     */

    private boolean exportSchedule(){
        if (this.paths == null) {
            showAlert("No file selected", "Please choose a .txt file first.");
            return false;
        }
        previewButton.setDisable(false);

        if (schedule.getSlots().isEmpty()) {
            handleSlots(paths);
        }

        switch(outputFormat){
            case "PDF":
                pdfScheduler = new PDFScheduler();
                pdfScheduler.generateSchedule(schedule.getDays(), schedule.getTimeSlots(), schedule.getSlots());
                return true;
            case "Excel":
                excelScheduler = new ExcelScheduler();
                excelScheduler.generateSchedule(schedule.getDays(), schedule.getTimeSlots(), schedule.getSlots());
                return true;
            default:
                return false;

        }
        
    }

    /**
     * Reads slot data from input files, validates them, and checks for conflicts.
     * Only conflict-free and valid slots are added to the schedule.
     */
    
    public void handleSlots(List<Path> files){
        List<Slot> slots = schedule.getSlots();
        
        for(Path dir: files){
            List<Slot> extractedSlots =  ParseTxt.parse(dir.toString());
            List<Slot> validSlots = new ArrayList<Slot>();
            for(int i = 0; i < extractedSlots.size(); i++){
                Slot slot = extractedSlots.get(i);
                if (ValidationStrategy.checkSlot(slot)) {
                    boolean conflict = false;

                    for (int j = i + 1; j < extractedSlots.size(); j++) {
                        if (!CompareStrategy.compareSlots(slot, extractedSlots.get(j))) {
                            conflict = true;
                            break;
                        }
                    }

                    if (!conflict) {
                        for (Slot otherSlot : slots) {
                            if (!CompareStrategy.compareSlots(slot, otherSlot)) {
                                conflict = true;
                                break;
                            }
                        }
                    }

                    if (!conflict) {
                        validSlots.add(slot);
                    }
                }

            }
            slots.addAll(validSlots);
        }
    }

    /**
     * Previews the generated schedule file in the UI depending on selected format.
     */

    private void previewFile(){
        File previewFile = null;
        switch(outputFormat){
            case "PDF":
                previewFile = new File("resources/output/schedule.pdf");
                if (previewFile.exists()) {
                    generatePDFPreview(previewFile);
                } else {
                    showAlert("File not found", "PDF file does not exist yet.");
                }
                break;
            case "Excel":
                previewFile = new File("resources/output/schedule.xlsx");
                if (previewFile.exists()) {
                    try {
                        TableView<String[]> tableView;
                        try {
                            tableView = generateExcelPreview(previewFile);
                            root.getChildren().add(tableView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
    
                    } 
                    catch (IOException e) {
                        showAlert("Error", "Failed to load Excel preview.");
                    }
                }
                else {
                    showAlert("File not found", "Excel file does not exist yet.");
                }
                break;
        }
        previewButton.setDisable(true);
    }

    /**
     * Clears any previously generated previews from the UI.
     */

    private void clearPreview() {
        root.getChildren().removeIf(node -> node instanceof ScrollPane || node instanceof TableView);
    }

    /**
     * Renders a PDF file as a series of images in a scrollable pane.
     */

    private void generatePDFPreview(File previewFile) {
        try {
            root.getChildren().removeIf(node -> node instanceof ScrollPane);
            root.getChildren().removeIf(node -> node instanceof TableView);
            
            FileInputStream fis = new FileInputStream(previewFile);
            PDDocument document = PDDocument.load(fis);
            PDFRenderer renderer = new PDFRenderer(document);

            VBox pdfPageContainer = new VBox(10);
            pdfPageContainer.setAlignment(Pos.CENTER);

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage bim = renderer.renderImageWithDPI(i, 150);
                Image fxImage = SwingFXUtils.toFXImage(bim, null);
                ImageView imageView = new ImageView(fxImage);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(600); // adjust as needed
                pdfPageContainer.getChildren().add(imageView);
            }

            ScrollPane scrollPane = new ScrollPane(pdfPageContainer);
            scrollPane.setPrefViewportHeight(500);
            scrollPane.setFitToWidth(true);

            root.getChildren().add(scrollPane);

            document.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to preview PDF.");
        }
    }

    /**
     * Renders an Excel file in a table view.
     */

    private TableView<String[]> generateExcelPreview(File file) {
        try{
            root.getChildren().removeIf(node -> node instanceof ScrollPane);
            root.getChildren().removeIf(node -> node instanceof TableView);

            TableView<String[]> tableView = new TableView<>();
            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            int maxColumns = 0;
            for (Row row : sheet) {
                if (row.getLastCellNum() > maxColumns) {
                    maxColumns = row.getLastCellNum();
                }
            }

            for (int i = 0; i < maxColumns; i++) {
                final int colIndex = i;
                TableColumn<String[], String> column = new TableColumn<>("Column " + (i + 1));
                column.setCellValueFactory(cellData -> {
                    String[] row = cellData.getValue();
                    return new SimpleStringProperty(colIndex < row.length ? row[colIndex] : "");
                });
                tableView.getColumns().add(column);
            }

            for (Row row : sheet) {
                String[] rowData = new String[maxColumns];
                for (int i = 0; i < maxColumns; i++) {
                    Cell cell = row.getCell(i);
                    rowData[i] = (cell != null) ? getCellValueAsString(cell) : "";
                }
                tableView.getItems().add(rowData);
            }

            workbook.close();
            fis.close();

            tableView.setPrefHeight(400); 
            return tableView;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Utility method to convert a cell value to string for display.
     */

    private String getCellValueAsString(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell) ?
                    cell.getDateCellValue().toString() :
                    Double.toString(cell.getNumericCellValue());
            case BOOLEAN -> Boolean.toString(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    /**
     * Displays a popup alert dialog with the given title and message.
     */

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}