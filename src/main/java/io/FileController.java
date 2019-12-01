package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * The FileController class provides functionality for file handling.
 */
public class FileController {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    
    private FileController() {}

    /**
     * This method returns a {@link String} read from an {@link InputStream}.
     * @param inStream - The stream the {@link String} is read from.
     * @return a {@link String} read from the {@link InputStream}
     * @throws IOException if an I/O error occurs.
     */
    public static String readInputStreamToString(InputStream inStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator", "\n");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, CHARSET))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        }
    }
    
    /**
     * This method returns a {@link String} read from a {@link File}.
     * @param file - The file the {@link String} is read from.
     * @return a {@link String} read from the {@link File}
     * @throws IOException if an I/O error occurs.
     */
    public static String readFileToString(File file) throws IOException {
        return readInputStreamToString(new FileInputStream(file));
    }
    
    /**
     * This method returns a {@link String} read from an {@link URL}.
     * @param url - The url the {@link String} is read from.
     * @return a {@link String} read from the {@link URL}
     * @throws IOException if an I/O error occurs.
     */
    public static String readURLToString(URL url) throws IOException {
        return readInputStreamToString(url.openStream());
    }
    
    /**
     * This method saves a {@link String} to a {@link File}.
     * @param content - The {@link String} to be saved.
     * @param file - The {@link File} to save the content to.
     * @return a {@link String} read from the {@link URL}
     * @throws IOException if an I/O error occurs.
     */
    public static void saveStringToFile(String content, File file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), CHARSET)) {
            writer.write(content);
        }
    }
}
