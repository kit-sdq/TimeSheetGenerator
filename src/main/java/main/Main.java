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
import main.UserInteraction.UserInputType;
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
        UserInteraction userInteraction;
        try {
            userInteraction = UserInteraction.parse(args);
        } catch (org.apache.commons.cli.ParseException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        // Print requested information
        if (userInteraction.isPrintRequest()) {
            userInteraction.print();
            return;
        }

        // Get content of input files
        String global;
        String month;
        try {
            global = userInteraction.getInput(UserInputType.GLOBAL);
            month = userInteraction.getInput(UserInputType.MONTH);
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

            if (userInteraction.isGui()) {
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

            userInteraction.setOutput(generator.generate());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }
    }

}
