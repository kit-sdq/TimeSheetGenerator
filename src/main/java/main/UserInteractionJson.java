package main;

/**
 * Represents a user interaction where the input is given directly as command line arguments and the output is written to the console.
 */
public class UserInteractionJson extends UserInteraction {

    public UserInteractionJson(String globalJson, String monthJson) {
        this.globalJson = globalJson;
        this.monthJson = monthJson;
    }

    private final String globalJson;
    private final String monthJson;

    @Override
    public boolean isPrintRequest() {
        return false;
    }

    @Override
    public boolean isGui() {
        return false;
    }

    @Override
    public String getInput(UserInputType inputType) {
        switch (inputType) {
            case GLOBAL:
                return globalJson;
            case MONTH:
                return monthJson;
            default:
                throw new RuntimeException(); // never occurs
        }
    }

    @Override
    public void setOutput(String output) {
        System.out.println(output);
    }

}
