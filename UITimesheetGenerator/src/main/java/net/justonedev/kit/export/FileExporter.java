/*
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2024 Benjamin Claus
 */
package net.justonedev.kit.export;

import net.justonedev.kit.FileChooser;
import net.justonedev.kit.Main;
import net.justonedev.kit.json.JSONHandler;

import java.io.File;
import java.util.Optional;

public class FileExporter {

    public static void printTex() {
        TempFiles tempFiles = TempFiles.generateNewTemp();
        if (tempFiles == null) {
            Main.showSimpleDialog("Could not create month.json file. If you have unsaved changes, try saving.");
            return;
        }

        Optional<String> error = TexCompiler.validateContents(tempFiles);
        if (error.isPresent()) {
            error(error.get());
            return;
        }

        File texFile = FileChooser.chooseCreateTexFile("Compile to Tex");
        if (texFile == null) return;    // Cancelled

        TexCompiler.compileToTex(tempFiles.getMonthFile(), texFile);

        if (!texFile.exists()) {
            error("Tex file creation failed!");
        }

        tempFiles.close();
    }

    public static void printPDF() {
        TempFiles tempFiles = TempFiles.generateNewTemp();
        if (tempFiles == null) return;

        Optional<String> error = TexCompiler.validateContents(tempFiles);
        if (error.isPresent()) {
            error(error.get());
            return;
        }

        File pdfFile = FileChooser.chooseCreatePDFFile("Print to PDF");
        if (pdfFile == null) return;    // Cancelled

        error = PDFCompiler.compileToPDF(JSONHandler.globalSettings, Main.getCurrentMonth(), pdfFile);

        if (error.isPresent()) {
            error(error.get());
            return;
        }

        if (!pdfFile.exists()) {
            Main.showSimpleDialog("PDF file creation failed! Perhaps try to compile to tex?");
            return;
        }

        tempFiles.close();
    }

    private static void error(String error) {
        Main.showSimpleDialog("%s%s%s".formatted("Error: Invalid Timesheet:", System.lineSeparator(), error));
    }
}
