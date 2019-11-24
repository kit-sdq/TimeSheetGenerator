package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileController {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    
    private FileController() {}

    public static String readFileToString(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator", "\n");

        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), CHARSET)) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        }
    }
    
    public static void saveStringToFile(String content, File file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), CHARSET)) {
            writer.write(content);
        }
    }
}
