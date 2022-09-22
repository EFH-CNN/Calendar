package Exception;

public class InvalidTimeOfScheduleItem extends Exception {
    private String message;

    public InvalidTimeOfScheduleItem(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
