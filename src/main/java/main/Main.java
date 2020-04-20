package main;

import java.io.IOException;

import javax.swing.JOptionPane;

import checker.CheckerError;
import checker.CheckerException;
import checker.CheckerReturn;
import checker.IChecker;
import checker.MiLoGChecker;
import data.TimeSheet;
import i18n.ResourceHandler;
import io.FileController;
import io.IGenerator;
import io.LatexGenerator;
import main.UserInput.Request;
import parser.ParseException;
import parser.Parser;

/**
 * Main class of the application containing the main method as entry point for the application
 */
public class Main {

    /**
     * Main entry point for the application
     * @param args command line arguments that are passed to the apache cli library
     */
    public static void main(String[] args) {
        // Initialize and parse user input
        UserInput userInput = new UserInput(args);
        Request request;
        try {
            request = userInput.parse();
        } catch (org.apache.commons.cli.ParseException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        // If requested: Print help and return
        if (request == Request.HELP) {
            userInput.printHelp();
            return;
        }
        // If requested: Print version and return
        if (request == Request.VERSION) {
            userInput.printVersion();
            return;
        }

        // Get content of input files
        String global;
        String month;
        try {
            global = FileController.readFileToString(userInput.getFile(UserInputFile.JSON_GLOBAL));
            month = FileController.readFileToString(userInput.getFile(UserInputFile.JSON_MONTH));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        // Initialize time sheet
        TimeSheet timeSheet;
        try {
            timeSheet = Parser.parseTimeSheetJson(global, month);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        // Check time sheet
        IChecker checker = new MiLoGChecker(timeSheet);
        CheckerReturn checkerReturn;
        try {
            checkerReturn = checker.check();
        } catch (CheckerException e) { // exception does not mean that the time sheet is invalid, but that the process of checking failed
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }
        // Print all errors in case the time sheet is invalid
        if (checkerReturn == CheckerReturn.INVALID) {
            for (CheckerError error : checker.getErrors()) {
                System.out.println(error.getErrorMessage());
            }
            
            if (userInput.isGui()) {
                StringBuilder errorList = new StringBuilder();
                for (CheckerError error : checker.getErrors()) {
                    errorList.append(error.getErrorMessage() + System.lineSeparator());
                }
                
                JOptionPane.showMessageDialog(null, errorList.toString(), ResourceHandler.getMessage("gui.errorListWindowTitle"), JOptionPane.ERROR_MESSAGE);
            }
            
            return;
        }

        // Generate and save output file
        ClassLoader classLoader = Main.class.getClassLoader();
        try {
            String latexTemplate = FileController.readInputStreamToString(classLoader.getResourceAsStream("MiLoG_Template.tex"));
            IGenerator generator = new LatexGenerator(timeSheet, latexTemplate);
            FileController.saveStringToFile(generator.generate(), userInput.getFile(UserInputFile.OUTPUT));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }
    }

}
