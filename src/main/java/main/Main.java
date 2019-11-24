package main;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import data.TimeSheet;
import io.FileController;
import io.IGenerator;
import io.LatexGenerator;
import parser.ParseException;
import parser.Parser;

public class Main {
    /**
     * @param args first argument global.json and second argument month.json, just use UTF8
     */
    public static void main(String[] args) {
        
        UserInput userInput = null;
        try {
          userInput = new UserInput(args);
        } catch (org.apache.commons.cli.ParseException e) {
          System.out.println(e.getMessage());
          System.exit(-1);
        }  
        
        String global;
        String month;
        try {
            global = FileController.readFileToString(userInput.getFile(UserInputFile.JSON_GLOBAL));
            month = FileController.readFileToString(userInput.getFile(UserInputFile.JSON_MONTH));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
            return;
        }
        
        TimeSheet doc;
        try {
            JSONObject globalJson = new JSONObject(global);
            JSONObject monthJson = new JSONObject(month);
            
            doc = Parser.parseTimeSheet(globalJson, monthJson);            
        } catch (JSONException | ParseException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
            return;
        }

        // TODO send doc to checker

        ClassLoader classLoader = Main.class.getClassLoader();
        try {
            String latexTemplate = FileController.readInputStreamToString(classLoader.getResourceAsStream("MiLoG_Arbeitszeitdokumentation.tex"));
            IGenerator generator = new LatexGenerator(doc, latexTemplate);
            FileController.saveStringToFile(generator.generate(), userInput.getFile(UserInputFile.OUTPUT));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        
    }
}
