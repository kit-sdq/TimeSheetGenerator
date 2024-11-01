package net.justonedev.kit.export;

import net.justonedev.kit.FileChooser;
import net.justonedev.kit.Main;

import java.io.File;
import java.util.Optional;

public class FileExporter {

    public static void printTex() {
        TempFiles tempFiles = TempFiles.generateNewTemp();
        if (tempFiles == null) {
            Main.showSimpleDialog("Could not create month.json file. If you have unsaved changes, try saving.");
            return;
        }

        File texFile = FileChooser.chooseCreateTexFile("Compile to Tex");

        TexCompiler.compileToTex(tempFiles.getMonthFile(), texFile);

        if (!texFile.exists()) {
            Main.showSimpleDialog("Tex file creation failed!");
        }

        tempFiles.close();
        Main.showSimpleDialog("Compiled to %s".formatted(texFile.getName()));
    }

    public static void printPDF() {
        TempFiles tempFiles = TempFiles.generateNewTemp();
        if (tempFiles == null) return;

        File pdfFile = FileChooser.chooseCreatePDFFile("Print to PDF");
        Optional<String> error = TexCompiler.validateContents(tempFiles);

        if (error.isPresent()) {
            Main.showSimpleDialog("%s%s%s".formatted("Error: Invalid Timesheet:", System.lineSeparator(), error.get()));
            return;
        }

        PDFCompiler.compileToPDF(tempFiles, pdfFile);

        if (!pdfFile.exists()) {
            Main.showSimpleDialog("PDF file creation failed! Perhaps try to compile to tex?");
            return;
        }

        tempFiles.close();
        Main.showSimpleDialog("Compiled to %s".formatted(pdfFile.getName()));
    }
}
