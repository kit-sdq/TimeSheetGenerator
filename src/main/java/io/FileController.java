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

public class FileController {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    
    private FileController() {}

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
    
    public static String readFileToString(File file) throws IOException {
        return readInputStreamToString(new FileInputStream(file));
    }
    
    public static String readURLToString(URL url) throws IOException {
        return readInputStreamToString(url.openStream());
    }
    
    public static void saveStringToFile(String content, File file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), CHARSET)) {
            writer.write(content);
        }
    }
}
