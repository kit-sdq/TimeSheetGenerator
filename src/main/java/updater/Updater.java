/* Licensed under MIT 2025. */
package updater;

import ui.ErrorHandler;
import ui.json.JSONHandler;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

// The warning is false, this cannot be a record class with this constructor layout
public class Updater {
	public static final String UPDATE_COMMAND_LINE_PARAMETER = "updater";

	private static final int JOPTIONPANE_RESULT_YES = 0;
	private static final String FORMAT_TEMPORARY_JAR_FILE = "temp-%s.jar";
	private static final String REGEX_TEMPORARY_JAR_FILE = "temp-.*\\.jar";
	private static final String JAR_DOWNLOAD_URL_TEMPLATE = "https://github.com/kit-sdq/TimeSheetGenerator/releases/download/v%s/timesheetgenerator.jar";
	private static final String JAR_DOWNLOAD_URL_TEMPLATE_ALT = "https://github.com/kit-sdq/TimeSheetGenerator/releases/download/%s/timesheetgenerator.jar";

	private static final int TIMEOUT_FILE_DELETE_MS = 10000;

	private final JFrame parentFrame;

	/**
	 * Creates a new updater instance and automatically searches for updates.
	 * Requires the JFrame as parent for popups.
	 */
	public Updater(JFrame parentFrame) {
		this.parentFrame = parentFrame;
		clearOldJarFiles();
	}

	/**
	 * Creates a new updater instance and automatically searches for updates. Does
	 * not have a parent JFrame. Also does not clean up temporary jar files. Should
	 * only be used for temporary processes during updates.
	 */
	public Updater() {
		this.parentFrame = null;
	}

	/**
	 * Checks for if an update is available. If an update is available, a popup will
	 * open up.
	 */
	public void checkForUpdates() {
		String localVersion = LocalVersionFetcher.getProgramVersion();
		String newestReleaseVersion = JSONHandler.getFieldDefaults().getNewestVersion();
		if (!VersionComparer.isNewerThan(newestReleaseVersion, localVersion))
			return;
		openPopup(localVersion, newestReleaseVersion);
	}

	private void clearOldJarFiles() {
		for (File tempJar : Objects
				.requireNonNull(new File(JSONHandler.getApplicationDataPath()).listFiles((dir, name) -> name.matches(REGEX_TEMPORARY_JAR_FILE)))) {
			if (!tempJar.delete())
				tempJar.deleteOnExit();
		}
	}

	private void openPopup(String oldVersion, String newVersion) {
		int result = JOptionPane.showConfirmDialog(parentFrame,
				"A new version of the TimesheetGenerator is available (%s -> %s)%nDo you want to update now?".formatted(oldVersion, newVersion),
				"New version available", JOptionPane.YES_NO_OPTION);
		if (result != JOPTIONPANE_RESULT_YES)
			return;
		Thread.startVirtualThread(() -> JOptionPane.showMessageDialog(parentFrame, "Downloading newest TimesheetGenerator release...", "Updating...",
				JOptionPane.INFORMATION_MESSAGE));
		try {
			File file = downloadVersion(newVersion);
			runTemporaryUpdateGenerator(file);
		} catch (IOException | URISyntaxException e) {
			ErrorHandler.showError("Error downloading release", "Failed to download newest release. Update cancelled, please try again later.");
		}
	}

	private void runTemporaryUpdateGenerator(File file) {
		try {
			File newLocalFile = replaceLocalFile(file);
			runJarFile(newLocalFile);
			System.exit(0);
		} catch (IOException | URISyntaxException e) {
			ErrorHandler.showError("Something went wrong",
					"Something went wrong while trying to perform the update. Please try again later.%n%s".formatted(e.getMessage()));
		}
	}

	private void runJarFile(File file) throws IOException {
		runJarFileWithArgs(file);
	}

	private void runJarFileWithArgs(File file, String... arguments) throws IOException {
		String[] parameters = new String[3 + arguments.length];
		parameters[0] = "java";
		parameters[1] = "-jar";
		parameters[2] = file.getAbsolutePath();
		System.arraycopy(arguments, 0, parameters, 3, arguments.length);
		Runtime.getRuntime().exec(parameters);
	}

	public static String getLocalFile() throws URISyntaxException {
		return new File(Updater.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
	}

	/**
	 * Replaces the local file that is currently being executed with a different
	 * file passed as argument.
	 *
	 * @param newGeneratorFile The filepath for the new TimesheetGenerator.
	 * @return the new generator file. Should be the same as the old one.
	 * @throws URISyntaxException if acquiring the local filepath fails.
	 * @throws IOException        if the file cannot be properly replaced.
	 */
	private File replaceLocalFile(File newGeneratorFile) throws URISyntaxException, IOException {
		File localFile = new File(getLocalFile());
		if (!localFile.isFile())
			throw new IOException("Current executable is not a jar file.");
		boolean deleted = localFile.delete();
		if (!deleted) {
			// operating systems like windows do not allow this, so we need to take a
			// workaround
			runJarFileWithArgs(newGeneratorFile, UPDATE_COMMAND_LINE_PARAMETER, localFile.getAbsolutePath());
			System.exit(0);
		}
		Path newFilepath = Files.move(newGeneratorFile.toPath(), localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return newFilepath.toFile();
	}

	/**
	 * This method downloads the specified version of the TimesheetGenerator.
	 * Version must be of the format \d+(.\d+)*, without the leading 'v'. If the
	 * file cannot be found under that URL, will try without the 'v'.
	 * <p>
	 * Will save the file to a jar file matching temp-< uuid >.jar inside the
	 * directory where the configuration files are saved as well. Returns the jar
	 * file of the new timesheet generator.
	 * </p>
	 *
	 * @param version The version of the TimesheetGenerator to download.
	 * @return the downloaded file.
	 */
	private File downloadVersion(String version) throws IOException, URISyntaxException {
		File file = new File(JSONHandler.getApplicationDataPath(), FORMAT_TEMPORARY_JAR_FILE.formatted(UUID.randomUUID()));
		try {
			URL link = new URI(JAR_DOWNLOAD_URL_TEMPLATE.formatted(version)).toURL();
			boolean success = tryReadFromSite(file, link);
			if (!success) {
				URL alternativeLink = new URI(JAR_DOWNLOAD_URL_TEMPLATE_ALT.formatted(version)).toURL();
				success = tryReadFromSite(file, alternativeLink);
				if (!success)
					throw new FileNotFoundException("Could not find the latest release of the TimesheetGenerator.");
			}
		} catch (IOException | URISyntaxException e) {
			ErrorHandler.showError("Error downloading release", "Failed to download newest release. Update cancelled, please try again later.");
		}
		return file;
	}

	private boolean tryReadFromSite(File file, URL link) throws IOException, URISyntaxException {
		try (ReadableByteChannel byteChannel = Channels.newChannel(link.openStream()); FileOutputStream outputStream = new FileOutputStream(file)) {
			outputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;
	}

	// ------------ Operations for the second way of replacing the old file
	// ------------

	public void cloneCurrentFileToOriginal(String originalFilepath) {
		File originalFile = new File(originalFilepath);
		try {
			var localFile = getLocalFile();
			long startTime = System.currentTimeMillis();
			while (originalFile.exists() && !originalFile.delete()) {
				if (startTime + TIMEOUT_FILE_DELETE_MS < System.currentTimeMillis())
					throw new TimeoutException("Timed out while attempting to delete the original file.");
			}
			// Temp file will be deleted anyway
			Files.copy(new File(localFile).toPath(), originalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (URISyntaxException e) {
			ErrorHandler.showError("Something went wrong", "Failed to acquire the current executable jar.");
		} catch (TimeoutException e) {
			ErrorHandler.showError("Something went wrong", e.getMessage());
		} catch (IOException e) {
			ErrorHandler.showError("Something went wrong", "Failed to copy the TimesheetGenerator executable.");
		} finally {
			if (originalFile.exists()) {
				try {
					runJarFile(originalFile);
				} catch (IOException ignored) {
				}
			}
		}
	}
}
