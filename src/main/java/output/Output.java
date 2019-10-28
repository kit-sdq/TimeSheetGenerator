package output;

import data.Entry;
import data.FullDocumentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Output implements IOutput {
    
    private static final String FILE_TEMPLATE = "src/main/resources/MiLoG_Arbeitszeitdokumentation.tex";
    private static final String PATH_SAVEFILE = "src/main/resources/";  
    
    //TODO Documentation of the methods
    @Override
    public String generateLaTeX(FullDocumentation documentation) throws IOException {
        String filledTex = null;
            filledTex = readLaTeXTemplate();

 
        /*
         * This loop replaces the document-public placeholders in the TeX template with the correct data.
         */
        for (DocumentPlaceholder dp : DocumentPlaceholder.values()) {
            filledTex = filledTex.replace(dp.getPlaceholder(), dp.getSubstitute(documentation));
        }
        
        /*
         * This loop replaces the placeholders in the TeX template with the correct data.
         * If the FullDocumentation contains to many elements for the table,
         * all rows get filled and the rest of data gets lost.
         */
        for (Entry entry : documentation.getEntries()) {
            for (TablePlaceholder tp : TablePlaceholder.values()) {
                filledTex = filledTex.replaceFirst(tp.getPlaceholder(), tp.getSubstitute(entry));
            }
        }
        
        /*
         * This loop fills up all not-needed rows of the table with a blank space.
         * IMPORTANT: Some kind of character is needed to make the TeX compile correctly on some TeX compilers.
         */
        for (TablePlaceholder tp : TablePlaceholder.values()) {
            filledTex = filledTex.replace(tp.getPlaceholder(), "\\thinspace");
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
