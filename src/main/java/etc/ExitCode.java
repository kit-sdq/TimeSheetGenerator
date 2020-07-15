package etc;

public enum ExitCode {

    NORMAL(0),
    GENERIC_ERROR(1),
    CHECKER_ERROR(2);

    private int code;

    private ExitCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
