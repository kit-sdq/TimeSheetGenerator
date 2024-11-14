/*
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2024 Benjamin Claus
 */
package net.justonedev.kit;

import net.justonedev.kit.fileexplorer.FileChooserType;
import net.justonedev.kit.json.JSONHandler;
import net.justonedev.kit.json.Month;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileChooser {

    // Timesheet Format: Nachname_Vorname_Monat Jahr.pdf
    private static final String FORMAT = "%s_%s 20%s";

    public static String getDefaultFileName() {
        return FORMAT.formatted(JSONHandler.globalSettings.getFormattedName2(),
                getGermanMonth(Main.getCurrentMonthNumber()), Main.getYear());
    }

    public static File chooseFile(String title, FileChooserType chooserType) {
        if (false && System.getProperty("os.name").toLowerCase().contains("win")) {
            return chooseFileWindows(title);
        } else {
            return chooseFileSwing(title, chooserType);
        }
    }

    private static File chooseFileWindows(String title) {
        return null;
    }

    private static File chooseFileSwing(String title, FileChooserType chooserType) {
        JFileChooser fileChooser = getFileChooser(chooserType);


        fileChooser.setDialogTitle(title);

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            JSONHandler.otherSettings.setPath(chooserType, file);
            return file;
        } else {
            return null;
        }
    }

    public static File chooseCreateJSONFile(String title) {
        String month = Main.getCurrentMonthName();
        if (month.isBlank()) month = "month";
        return chooseCreateFile(title, FileChooserType.MONTH_PATH, "%s%s".formatted(month, Main.getYear()), "json", "JSON Files (*.json)");
    }
    public static File chooseCreateTexFile(String title) {
        return chooseCreateFile(title, FileChooserType.TEX_PATH, getDefaultFileName(), "tex", "LaTeX Files (*.tex)");
    }
    public static File chooseCreatePDFFile(String title) {
        return chooseCreateFile(title, FileChooserType.PDF_PATH, getDefaultFileName(), "pdf", "PDF Files (*.pdf)");
    }
    public static File chooseCreateFile(String title, FileChooserType chooserType, String defaultFileName, String extension, String extensionDescription) {
        JFileChooser fileChooser = getFileChooser(chooserType);
        fileChooser.setDialogTitle(title);

        // Suggest a default file name
        fileChooser.setSelectedFile(new File("%s.%s".formatted(defaultFileName, extension == null ? "" : extension)));

        // Set up file filter for .json files
        FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter(extensionDescription, extension);
        fileChooser.setFileFilter(jsonFilter);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (fileToSave.exists()) {
                int result = JOptionPane.showConfirmDialog(
                        null,
                        "The file already exists. Do you want to override it?",
                        "Existing file",
                        JOptionPane.YES_NO_OPTION
                );

                if (result != JOptionPane.YES_OPTION) {
                    return null;
                }
            }

            JSONHandler.otherSettings.setPath(chooserType, fileToSave);
            return fileToSave;
        } else {
            return null;
        }
    }

    /**
     * Gets the german name for a month from 1-12.
     * @param monthName The month number, from 1-12.
     * @return The name of the month.
     */
    private static String getGermanMonth(int monthName) {
        return switch (monthName) {
            case 1 -> "Januar";
            case 2 -> "Februar";
            case 3 -> "März";
            case 4 -> "April";
            case 5 -> "Mai";
            case 6 -> "Juni";
            case 7 -> "Juli";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "Oktober";
            case 11 -> "November";
            case 12 -> "Dezember";
            default -> "unbekannt";
        };
    }

    private static JFileChooser getFileChooser(FileChooserType chooserType) {
        JFileChooser fileChooser = new JFileChooser();
        File parent = JSONHandler.otherSettings.getPath(chooserType);
        if (parent == null) fileChooser = new JFileChooser();
        else fileChooser = new JFileChooser(parent);
        return fileChooser;
    }

}
