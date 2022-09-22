package Exception;

public class MessageIsNull extends Exception {
    private String message;

    public MessageIsNull(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
