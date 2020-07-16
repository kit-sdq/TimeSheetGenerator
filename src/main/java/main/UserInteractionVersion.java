package main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import i18n.ResourceHandler;

/**
 * Represents a user interaction where version information is shown.
 */
public class UserInteractionVersion extends UserInteraction {

    @Override
    public boolean isPrintRequest() {
        return true;
    }

    @Override
    public boolean isGui() {
        return false;
    }

    @Override
    public void print() {
        String version = null;
        String buildTime = null;
        String branch = null;
        String commit = null;

        // read the project properties from the resources
        try {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("project.properties");

            if (inputStream != null) {
                // parse the properties
                Properties properties = new Properties();
                properties.load(inputStream);

                // the build properties are encoded in the buildInfo property
                String[] buildProperties = properties.getProperty("buildInfo").split(";");
                if (buildProperties.length >= 4) {
                    version = buildProperties[0];
                    // if a string starts with a "$" the placeholder has not been replaced by the actual value
                    buildTime = buildProperties[1];
                    if (buildTime.startsWith("$")) {
                        buildTime = null;
                    }
                    branch = buildProperties[2];
                    if (branch.startsWith("$")) {
                        branch = null;
                    }
                    commit = buildProperties[3];
                    if (commit.startsWith("$")) {
                        commit = null;
                    }
                }
            }
        } catch (IOException e) {}

        if (version == null) { // general error
            System.out.println(ResourceHandler.getMessage("error.userinput.versionNotFound"));
        } else {
            System.out.println(ResourceHandler.getMessage("command.output.version", version));

            // false when the buildnumber-maven-plugin is not executed (usually when the code is not run directly from the jar)
            if (buildTime != null && branch != null && commit != null) {
                System.out.println();
                System.out.println(ResourceHandler.getMessage("command.output.buildInfo", branch, commit, buildTime));
            }
        }
    }

}
