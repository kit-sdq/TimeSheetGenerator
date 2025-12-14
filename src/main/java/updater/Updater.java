package updater;

import ui.ErrorHandler;
import ui.json.JSONHandler;

import javax.swing.*;
import java.io.File;
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

public record Updater(JFrame parentFrame) {

    private static final int JOPTIONPANE_RESULT_YES = 0;
    private static final String FORMAT_TEMPORARY_JAR_FILE = "temp-%s.jar";
    private static final String REGEX_TEMPORARY_JAR_FILE = "temp-.*\\.jar";
    private static final String JAR_DOWNLOAD_URL_TEMPLATE = "https://github.com/kit-sdq/TimeSheetGenerator/releases/download/v%s/timesheetgenerator.jar";
    private static final String JAR_DOWNLOAD_URL_TEMPLATE_2 = "https://github.com/kit-sdq/TimeSheetGenerator/releases/download/%s/timesheetgenerator.jar";

    /**
     * Creates a new updater instance and automatically searches for updates.
     * Requires the Userinterface and JFrame as parent for the popup.
     */
    public Updater(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        clearOldJarFiles();
        checkForUpdates();
    }

    /**
     * Checks for if an update is available. If an update is available, a popup will open up.
     */
    public void checkForUpdates() {
        String localVersion = LocalVersionFetcher.getProgramVersion();
        String newestReleaseVersion = JSONHandler.getFieldDefaults().getNewestVersion();
        //if (!VersionComparer.isNewerThan(newestReleaseVersion, localVersion)) return;
        openPopup(localVersion, newestReleaseVersion);
    }

    private void clearOldJarFiles() {
        for (File tempJar : Objects.requireNonNull(new File(JSONHandler.getApplicationDataPath())
                .listFiles((dir, name) -> name.matches(REGEX_TEMPORARY_JAR_FILE)))) {
            if (!tempJar.delete()) tempJar.deleteOnExit();
        }
    }

    private void openPopup(String oldVersion, String newVersion) {
        int result = JOptionPane.showConfirmDialog(parentFrame, "A new version of the TimesheetGenerator is available (%s -> %s)%nDo you want to update now?".formatted(oldVersion, newVersion), "New version available", JOptionPane.YES_NO_OPTION);
        if (result != JOPTIONPANE_RESULT_YES) return;
        Thread.startVirtualThread(() -> JOptionPane.showMessageDialog(parentFrame, "Downloading newest TimesheetGenerator release...", "Updating...", JOptionPane.INFORMATION_MESSAGE));
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
            Runtime.getRuntime().exec(new String[]{"java", "-jar", newLocalFile.getAbsolutePath()});
            System.exit(0);
        } catch (IOException | URISyntaxException e) {
            ErrorHandler.showError("Something went wrong", "Something went wrong while trying to perform the update. Please try again later.%n%s".formatted(e.getMessage()));
        }
    }

    private String getLocalFile() throws URISyntaxException {
        return new File(Updater.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath();
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
        if (!localFile.isFile()) throw new IOException("Current executable is not a jar file.");
        boolean deleted = localFile.delete();
        // todo if windows does not allow this, we need to implement the workaround anyway
        if (!deleted) throw new IOException("Failed to delete the old timesheet generator.");
        Path newFilepath = Files.move(newGeneratorFile.toPath(), localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return newFilepath.toFile();
    }

    /**
     * This method downloads the specified version of the TimesheetGenerator.
     * Version must be of the format \d+(.\d+)*, without the leading 'v'. If the file cannot
     * be found under that URL, will try without the 'v'.
     * <p>
     * Will save the file to a jar file matching temp-< uuid >.jar inside the directory where
     * the configuration files are saved as well. Returns the jar file of the new timesheet generator.
     * </p>
     *
     * @param version The version of the TimesheetGenerator to download.
     * @return the downloaded file.
     */
    private static File downloadVersion(String version) throws IOException, URISyntaxException {
        File file = new File(JSONHandler.getApplicationDataPath(), FORMAT_TEMPORARY_JAR_FILE.formatted(UUID.randomUUID()));
        try {
            URL url = new URI(JAR_DOWNLOAD_URL_TEMPLATE.formatted(version)).toURL();
            try (ReadableByteChannel byteChannel = Channels.newChannel(url.openStream());
                 FileOutputStream outputStream = new FileOutputStream(file)) {
                outputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
            }
        } catch (IOException | URISyntaxException e) {
            ErrorHandler.showError("Error downloading release", "Failed to download newest release. Update cancelled, please try again later.");
        }
        return file;
    }
}
