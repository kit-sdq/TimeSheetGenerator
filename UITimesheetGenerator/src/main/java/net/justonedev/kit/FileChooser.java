/*
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2024 Benjamin Claus
 */
package net.justonedev.kit;

import net.justonedev.kit.json.JSONHandler;
import net.justonedev.kit.json.Month;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileChooser {

    // Timesheet Format: Nachname_Vorname_Monat Jahr.pdf
    private static final String FORMAT = "%s_%s 20%s";

    public static String getDefaultFileName() {
        return FORMAT.formatted(JSONHandler.globalSettings.getFormattedName2(),
                getGermanMonth(Main.getCurrentMonthNumber()), Main.getYear());
    }

    public static File chooseFile(String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    public static File chooseCreateJSONFile(String title) {
        String month = Main.getCurrentMonthName();
        if (month.isBlank()) month = "month";
        return chooseCreateFile(title, "%s%s".formatted(month, Main.getYear()), "json", "JSON Files (*.json)");
    }
    public static File chooseCreateTexFile(String title) {
        return chooseCreateFile(title, getDefaultFileName(), "tex", "LaTeX Files (*.tex)");
    }
    public static File chooseCreatePDFFile(String title) {
        return chooseCreateFile(title, getDefaultFileName(), "pdf", "PDF Files (*.pdf)");
    }
    public static File chooseCreateFile(String title, String defaultFileName, String extension, String extensionDescription) {
        JFileChooser fileChooser = new JFileChooser();
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

}
