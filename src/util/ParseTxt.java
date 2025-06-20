package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import model.Slot;

/**
 * Utility class for parsing instructor schedule data from .txt files.
 * Each line in the file is expected to follow the format:
 * day;time;courseCode;room
 * The instructor's name is inferred from the filename.
 */

public class ParseTxt {
    
    /**
     * Parses a .txt file into a list of Slot objects.
     *
     * @param path Path to the input .txt file.
     * @return List of parsed Slot objects, or null if an error occurs.
     */
    
    public static List<Slot> parse(String path){
        try {
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path))){
                Path fileName = Paths.get(path).getFileName();
                String instructor = fileName.toString().split("\\.")[0];
                String line;
                List<Slot> extractedSlots = new ArrayList<Slot>();
                
                while((line = bufferedReader.readLine()) != null){
                    String[] features = line.split(";");
                    extractedSlots.add(new Slot(features[0], features[1], features[2], features[3], instructor));
                }
                return extractedSlots;
            }
            
        }
        catch (FileNotFoundException e) {
            System.err.println("File in " + path + " couldn't be found");
        } catch (IOException e) {
            System.err.println("There was an error during reading the file " + e.getLocalizedMessage());
        }
        return null;
        
    }
}
