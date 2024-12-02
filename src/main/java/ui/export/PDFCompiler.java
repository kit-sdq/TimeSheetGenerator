/* Licensed under MIT 2024. */
package ui.export;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import ui.Time;
import ui.json.Global;
import ui.json.JSONHandler;
import ui.json.Month;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Logger;

public class PDFCompiler {

	private static final Object LOADER = new Object(){};

	private PDFCompiler() {
		throw new IllegalAccessError();
	}

	public static Optional<String> compileToPDF(Global global, Month month, File targetFile) {
		try (InputStream templateStream = LOADER.getClass().getResourceAsStream("/pdf/template.pdf")) {
			if (templateStream == null) {
				return Optional.of("Template PDF not found in resources.");
			}

			PDDocument document = Loader.loadPDF(templateStream.readAllBytes());
			return writeToPDF(document, global, month, targetFile);

		} catch (IOException e) {
			return Optional.of(e.getMessage());
		}
	}

	private static Optional<String> writeToPDF(PDDocument document, Global global, Month month, File targetFile) throws IOException {
		PDAcroForm form = document.getDocumentCatalog().getAcroForm();
		if (form == null) {
			return Optional.of("No form found in the document. Nothing we can do, sorry.");
		}

		form.getField("GF").setValue(global.getNameFormalFormat()); // Name
		form.getField("abc").setValue(String.valueOf(month.getMonth())); // Month
		form.getField("abdd").setValue(String.valueOf(month.getYear())); // Year
		form.getField("Personalnummer").setValue(String.valueOf(global.getStaffId())); // Personalnummer
		if (global.getWorkingArea().equals("gf")) {
			form.getField("GFB").setValue("On");
			form.getField("UB").setValue("Off");
		} else if (global.getWorkingArea().equals("ub")) {
			form.getField("GFB").setValue("Off");
			form.getField("UB").setValue("On");
		}
		form.getField("OE").setValue(global.getDepartment()); // Probably department
		form.getField("Std").setValue(global.getWorkingTime()); // Total hours
		form.getField("Stundensatz").setValue(String.valueOf(global.getWage()));// Wage

		form.getField("Übertrag vom Vormonat").setValue(month.getPredTransfer()); // Pred Übertrag
		form.getField("Übertrag in den Folgemonat").setValue(month.getSuccTransfer()); // Succ Übertrag

		Time timeSum = Time.parseTime(month.getPredTransfer());
		Time timeVacation = new Time();
		form.getField("monatliche SollArbeitszeit").setValue(global.getWorkingTime()); // Again hours probably

		try {
			form.getField("Ich bestätige die Richtigkeit der Angaben").setValue("%s, %s".formatted(
					DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDateTime.now()), JSONHandler.getUISettings().getAddSignature() ? global.getName() : ""));
		} catch (EOFException ignored) {
			Logger.getGlobal().warning("Could not load font for signature field when exporting to PDF. Proceeding with default.");
		}

		final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");

		for (int i = 0, fieldIndex = 1; i < month.getEntries().size(); i++) {
			Month.Entry entry = month.getEntries().get(i);
			Time time = Time.parseTime(entry.getEnd());
			time.subtractTime(Time.parseTime(entry.getStart()));
			time.subtractTime(Time.parseTime(entry.getPause()));
			timeSum.addTime(time);

			if (entry.isVacation()) {
				timeVacation.addTime(time);
				continue;
			}

			form.getField("Tätigkeit Stichwort ProjektRow%d".formatted(fieldIndex)).setValue(entry.getAction());
			form.getField("ttmmjjRow%d".formatted(fieldIndex)).setValue(dayFormatter.format(LocalDateTime.of(month.getYear(), month.getMonth(), entry.getDay(), 0, 0)));
			form.getField("hhmmRow%d".formatted(fieldIndex)).setValue(entry.getStart());
			form.getField("hhmmRow%d_2".formatted(fieldIndex)).setValue(entry.getEnd());
			form.getField("hhmmRow%d_3".formatted(fieldIndex)).setValue(entry.getPause());

			String timeFieldValue = time.toString();
			form.getField("hhmmRow%d_4".formatted(fieldIndex)).setValue(timeFieldValue);
			fieldIndex++;
		}

		form.getField("Summe").setValue(timeSum.toString()); // Total time worked
		form.getField("Urlaub anteilig").setValue(timeVacation.toString()); // Total time of Vacation

		// Save the filled document
		document.save(targetFile);
		document.close();

		return Optional.empty();
	}

}
