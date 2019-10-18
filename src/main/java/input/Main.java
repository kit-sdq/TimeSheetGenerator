package input;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    /**
     * @param args first argument global.json and second argument month.json, just use UTF8
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Provide two json files.");
        }
        //TODO check input further
        try {
            String global = readFile(args[0], StandardCharsets.UTF_8);
            String month = readFile(args[0], StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.out.println("Error reading file");
            return;
        }
        // TODO send to parser and get back FullDocumentation object o

        // TODO send o to checker

        // TODO if valid send o to pdf generation in the output package
    }

    private static String readFile(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
