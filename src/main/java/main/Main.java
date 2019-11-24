package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import data.TimeSheet;
import io.IOutput;
import io.Output;
import parser.IParser;
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
            global = readFile(userInput.getFile(UserInputFile.JSON_GLOBAL), StandardCharsets.UTF_8);
            month = readFile(userInput.getFile(UserInputFile.JSON_MONTH), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.out.println("Error reading file");
            return;
        }
        
        // TODO send to parser and get back FullDocumentation object o
        // Work in Progress
        IParser jsonParser = new Parser();
        TimeSheet doc = null;
        try {
            doc = jsonParser.parse(global, month);            
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        
        // TODO send o to checker

        // TODO if valid send o to pdf generation in the output package
        IOutput output = new Output();
        try {
            System.out.println(output.generateLaTeX(doc));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    private static String readFile(File file, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, encoding);
    }
}
