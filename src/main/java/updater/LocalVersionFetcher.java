/* Licensed under MIT 2025. */
package updater;

import ui.export.PDFCompiler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LocalVersionFetcher {

	private static final String NULL_VERSION = "0.0.0";
	private static final String VERSION_ATTRIBUTE_NAME = "generatorVersion";
	private static final Pattern MAVEN_VERSION_FILTER_REGEX = Pattern.compile("^(\\d++\\.\\d++\\.\\d++).*$");

	private LocalVersionFetcher() {
		// Don't allow instances of this class
	}

	public static String getProgramVersion() {
		try (InputStream templateStream = PDFCompiler.class.getResourceAsStream("/project.properties")) {
			var lines = readInputStreamToLines(templateStream);

			for (String line : lines) {
				if (!line.contains("="))
					continue;
				String[] keyValuePair = line.split("=");
				if (keyValuePair[0].equals(VERSION_ATTRIBUTE_NAME)) {
					return formatVersion(keyValuePair[1]);
				}
			}
		} catch (IOException ignored) {
			// In any case where we don't find the version attribute, we return the default
			// value
			// No explicit error handling required
		}
		return NULL_VERSION;
	}

	private static List<String> readInputStreamToLines(InputStream inputStream) throws IOException {
		List<String> lines = new ArrayList<>();
		if (inputStream == null) {
			return lines;
		}
		StringBuilder lineBuilder = new StringBuilder();
		for (byte _byte : inputStream.readAllBytes()) {
			// '\n' is the line separator for this file
			if (_byte == '\n') {
				lines.add(lineBuilder.toString());
				lineBuilder.setLength(0);
				continue;
			}
			lineBuilder.append((char) _byte);
		}
		if (!lineBuilder.isEmpty())
			lines.add(lineBuilder.toString());
		return lines;
	}

	private static String formatVersion(String mavenVersion) {
		Matcher matcher = MAVEN_VERSION_FILTER_REGEX.matcher(mavenVersion);
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return NULL_VERSION;
	}

}
