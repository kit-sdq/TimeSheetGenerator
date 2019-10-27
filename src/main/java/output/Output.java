package output;

import data.Entry;
import data.FullDocumentation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Output implements IOutput {
    
    //TODO Create enum for placeholders
    private static final String[] PLACEHOLDERS_HEADERFOOTER = {"<year>","<month>","<employeeName>","<employeeNumber>",
            "<gfub>","<department>","<maxHours>","<wage>","<vacation>","<sum>",
            "<workingHours>","<carryFrom>","<carryTo>"};
    private static final String[] PLACEHOLDERS_TABLE = {"<action>","<date>","<begin>","<end>","<break>","<time>"};
    private static final String FILE_TEMPLATE = "src/main/resources/MiLoG_Arbeitszeitdokumentation.tex";
    private static final String PATH_SAVEFILE = "src/main/resources/";  
    
    //TODO Documentation of the methods
    @Override
    public String generateLaTeX(FullDocumentation documentation) {
        String filledTex = null;
        try {
            filledTex = readLaTeXTemplate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //TODO Add GFUB field and sum
        filledTex = filledTex.replace(PLACEHOLDERS_HEADERFOOTER[0], ""+documentation.getYear());
        filledTex = filledTex.replace(PLACEHOLDERS_HEADERFOOTER[1], ""+documentation.getMonth());
        filledTex = filledTex.replace(PLACEHOLDERS_HEADERFOOTER[2], documentation.getEmployeeName());
        filledTex = filledTex.replace(PLACEHOLDERS_HEADERFOOTER[3], ""+documentation.getId());
        //GFUB missing here
        filledTex = filledTex.replace(PLACEHOLDERS_HEADERFOOTER[5], documentation.getDepartmentName());
        filledTex = filledTex.replace(PLACEHOLDERS_HEADERFOOTER[6], ""+documentation.getMaxWorkTime());
        filledTex = filledTex.replace(PLACEHOLDERS_HEADERFOOTER[7], ""+documentation.getWage());
        filledTex = filledTex.replace(PLACEHOLDERS_HEADERFOOTER[8], documentation.getVacation().toString());
        //Sum of hours missing here
        filledTex = filledTex.replace(PLACEHOLDERS_HEADERFOOTER[10], ""+documentation.getMaxWorkTime());
        filledTex = filledTex.replace(PLACEHOLDERS_HEADERFOOTER[11], documentation.getPredTranfer().toString());
        filledTex = filledTex.replace(PLACEHOLDERS_HEADERFOOTER[12], documentation.getSuccTransfer().toString());
        
        /*
         * This loop replaces the placeholders in the TeX template with the correct data.
         * If the FullDocumentation contains to many elements for the table,
         * all rows get filled and the rest of data gets lost.
         * 
         * TODO Replacing the magic strings and code duplicates (Used for testing purposes)
         */
        SimpleDateFormat datePattern = new SimpleDateFormat("dd.MM.yy");
        for (Entry entry : documentation.getEntries()) {
            filledTex = filledTex.replaceFirst(PLACEHOLDERS_TABLE[0], entry.getAction());
            filledTex = filledTex.replaceFirst(PLACEHOLDERS_TABLE[1], datePattern.format(entry.getDate()));
            filledTex = filledTex.replaceFirst(PLACEHOLDERS_TABLE[2], entry.getStart().toString());
            filledTex = filledTex.replaceFirst(PLACEHOLDERS_TABLE[3], entry.getEnd().toString());
            filledTex = filledTex.replaceFirst(PLACEHOLDERS_TABLE[4], entry.getPause().toString());
            
            //TODO getWorkingTime is not working at the moment. Complete it
            //filledTex = filledTex.replace(PLACEHOLDERS_TABLE[5], entry.getWorkingTime().toString());
            filledTex = filledTex.replaceFirst(PLACEHOLDERS_TABLE[5], "00:00");
        }
        
        /*
         * This loop fills up all not-needed rows of the table with a blank space.
         * IMPORTANT: Some kind of character is needed to make the TeX compile correctly on some TeX compilers.
         */
        for (String placeholder : PLACEHOLDERS_TABLE) {
            filledTex = filledTex.replace(placeholder, "\\thinspace");
        }
        
        //TODO Should be replaced by swings FileChooser for greater UI-Experience
        File file = new File(PATH_SAVEFILE + "MiLoG_Arbeitszeitdokumentation" + "_" + documentation.getYear() + 
                "_" + documentation.getMonth() + ".tex");
        saveStringToFile(file, filledTex);
        
        //Does it make sense to give back this string? 
        return filledTex;
    }

    /**
     * Reads the LaTeX template from disk and converts it to a string.
     * 
     * @return The LaTeX template
     * @throws IOException
     */
    private String readLaTeXTemplate() throws IOException {
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_TEMPLATE))) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        }
    }
    
    /**
     * Writes a given string to a file.
     * If the given file already exists, it gets overwritten.
     * 
     * @param file - File in which the content should be written
     * @param content - String to be written to the file
     */
    private void saveStringToFile(File file, String content) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
}
