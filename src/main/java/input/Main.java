package input;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import data.FullDocumentation;
import output.IOutput;
import output.Output;
import parser.IParser;
import parser.ParseException;
import parser.Parser;

public class Main {
    /**
     * @param args first argument global.json and second argument month.json, just use UTF8
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Provide two json files.");
            System.exit(-1);
        }
        //TODO check input further
        
        
        String global;
        String month;
        
        try {
            global = readFile(args[0], StandardCharsets.UTF_8);
            month = readFile(args[1], StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.out.println("Error reading file");
            return;
        }
        
        // TODO send to parser and get back FullDocumentation object o
        // Work in Progress
        IParser jsonParser = new Parser();
        FullDocumentation doc = null;
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

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
