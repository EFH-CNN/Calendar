package Exception;

public class IsFormatException extends Exception {
    private String message;

    public IsFormatException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
