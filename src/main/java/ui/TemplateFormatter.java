package ui;

import ui.json.Global;
import ui.json.JSONHandler;

public class TemplateFormatter {

    /**
     * Formats a custom template String by inserting all data from the global settings
     * or the given {@link UserInterface}. The formatting rules can be read in the README or below:
     *
     * <p>
     * <ul>
     *   <li><code>%FIRST%</code>: First- and middle names, separated by space</li>
     *   <li><code>%FIRST_U%</code>: First- and middle names, separated by underscores</li>
     *   <li><code>%LAST%</code>: Lastname</li>
     *   <li><code>%MM%</code>: Month number from 01–12</li>
     *   <li><code>%MM_GER%</code>: German month name</li>
     *   <li><code>%MM_ENG%</code>: English month name</li>
     *   <li><code>%YY%</code>: Year (2 digits)</li>
     *   <li><code>%YYYY%</code>: Year (4 digits)</li>
     * </ul>
     * </p>
     * @param template The string template.
     * @param userInterface The ui instance that provides data like the current month or year.
     * @return The complete formatted string with all placeholders replaced by the actual data.
     */
    public static String formatTemplate(String template, UserInterface userInterface) {
        Global global = JSONHandler.getGlobalSettings();
        return template.replace("%FIRST_U%", global.getFirstnameUnderscoreFormat()).replace("%FIRST%", global.getFirstname())
                .replace("%LAST%", global.getLastname()).replace("%MM%", "%02d".formatted(userInterface.getCurrentMonthNumber()))
                .replace("%MM_GER%", userInterface.getCurrentMonth().getGermanName()).replace("%MM_ENG%", userInterface.getCurrentMonthName())
                .replace("%YY%", userInterface.getYear()).replace("%YYYY%", userInterface.getFullYear());
    }
}
