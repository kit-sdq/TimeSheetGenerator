/*
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2024 Benjamin Claus
 */
package net.justonedev.kit.export;

import net.justonedev.kit.Time;
import net.justonedev.kit.json.Global;
import net.justonedev.kit.json.Month;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PDFCompiler {

    private static PDFCompiler singleton;

    private PDFCompiler() { }

    private static PDFCompiler getSingleton() {
        if (singleton == null) singleton = new PDFCompiler();
        return singleton;
    }

    static Optional<String> compileToPDF(Global global, Month month, File targetFile) {
        return getSingleton()._compileToPDF(global, month, targetFile);
    }

    private Optional<String> _compileToPDF(Global global, Month month, File targetFile) {
        try (InputStream templateStream = getClass().getResourceAsStream("/pdf/template.pdf")) {
            if (templateStream == null) {
                return Optional.of("Template PDF not found in resources.");
            }

            PDDocument document = PDDocument.load(templateStream);
            return writeToPDF(document, global, month, targetFile);

        } catch (IOException e) {
            return Optional.of(e.getMessage());
        }
    }

    private Optional<String> writeToPDF(PDDocument document, Global global, Month month, File targetFile) throws IOException {

        /* This code is for if, theoretically, we'd need more pages.
            As far as I know, though, it is not supposed to be > 1 page.

        int pages = (int) Math.max(1, Math.ceil((double) month.getEntries().size() / MAX_ENTRIES_PER_PAGE));
        PDPage page = document.getPage(0);
        // PDF has only 1 page

        for (int i = document.getNumberOfPages(); i < pages; ++i) {
            document.addPage(page);
        }
         */

        PDAcroForm form = document.getDocumentCatalog().getAcroForm();
        if (form == null) {
            return Optional.of("No form found in the document. Nothing we can do, sorry.");
        }

        form.getField("GF").setValue(global.getFormattedName());                // Name
        form.getField("abc").setValue(String.valueOf(month.getMonth()));        // Month
        form.getField("abdd").setValue(String.valueOf(month.getYear()));        // Year
        form.getField("Personalnummer").setValue(String.valueOf(global.getStaffId()));        // Personalnummer
        if (global.getWorkingArea().equals("gf")) {
            form.getField("GFB").setValue("On");
            form.getField("UB").setValue("Off");
        } else if (global.getWorkingArea().equals("ub")) {
            form.getField("GFB").setValue("Off");
            form.getField("UB").setValue("On");
        }
        form.getField("OE").setValue(global.getDepartment());                   // Probably department
        form.getField("Std").setValue(global.getWorkingTime());                 // Total hours
        form.getField("Stundensatz").setValue(String.valueOf(global.getWage()));// Wage

        form.getField("Übertrag vom Vormonat").setValue(month.getPredTransfer());   // Pred Übertrag
        form.getField("Übertrag in den Folgemonat").setValue(month.getSuccTransfer());   // Succ Übertrag

        Time timeSum = Time.parseTime(month.getPredTransfer());
        Time timeVacation = new Time();
        form.getField("monatliche SollArbeitszeit").setValue(global.getWorkingTime());  // Again hours probably

        form.getField("Ich bestätige die Richtigkeit der Angaben").setValue("%s, %s"
                .formatted(DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDateTime.now()), global.getName()));

        final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");

        for (int m = 1; m <= month.getEntries().size(); ++m) {
            Month.Entry entry = month.getEntries().get(m - 1);
            form.getField("Tätigkeit Stichwort ProjektRow%d".formatted(m)).setValue(entry.getAction());
            form.getField("ttmmjjRow%d".formatted(m)).setValue(dayFormatter.format(
                    LocalDateTime.of(month.getYear(), month.getMonth(), entry.getDay(), 0, 0)));
            form.getField("hhmmRow%d".formatted(m)).setValue(entry.getStart());
            form.getField("hhmmRow%d_2".formatted(m)).setValue(entry.getEnd());
            form.getField("hhmmRow%d_3".formatted(m)).setValue(entry.getPause());

            Time time = Time.parseTime(entry.getEnd());
            time.subtractTime(Time.parseTime(entry.getStart()));
            time.subtractTime(Time.parseTime(entry.getPause()));

            if (entry.isVacation()) {
                timeVacation.addTime(time);
                form.getField("hhmmRow%d_4".formatted(m)).setValue("U");                // U = Urlaub, engl.: Vacation
            } else {
                form.getField("hhmmRow%d_4".formatted(m)).setValue(time.toString());
            }
            timeSum.addTime(time);

        }

        form.getField("Summe").setValue(timeSum.toString());                    // Total time worked
        form.getField("Urlaub anteilig").setValue(timeVacation.toString());     // Total time of Vacation

        // Save the filled document
        document.save(targetFile);
        document.close();

        return Optional.empty();
    }

}
