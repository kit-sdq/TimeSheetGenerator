package net.justonedev.kit.export;

import net.justonedev.kit.FileChooser;
import net.justonedev.kit.Main;
import net.justonedev.kit.TexCompiler;

import java.io.File;

public class FileExporter {

    public static File printTex() {
        TempFiles tempFiles = TempFiles.generateNewTemp();
        if (tempFiles == null) {
            Main.showSimpleDialog("Could not create month.json file. If you have unsaved changes, try saving.");
            return null;
        }

        File texFile = FileChooser.chooseCreateTexFile("Compile to Tex");

        TexCompiler.compileToTex(tempFiles.getMonthFile(), texFile);

        if (!texFile.exists()) {
            Main.showSimpleDialog("Tex file creation failed!");
        }

        tempFiles.close();
        return texFile;
    }

    public static File printPDF() {
        TempFiles tempFiles = TempFiles.generateNewTemp();
        if (tempFiles == null) return null;

        File pdfFile = FileChooser.chooseCreatePDFFile("Print to PDF");
        TexCompiler.validateContents(tempFiles);

        PDFCompiler.compileToPDF(tempFiles, pdfFile);

        if (!pdfFile.exists()) {
            Main.showSimpleDialog("PDF file creation failed! Perhaps try to compile to tex?");
            return pdfFile;
        }

        tempFiles.close();
        return pdfFile;
    }
}
