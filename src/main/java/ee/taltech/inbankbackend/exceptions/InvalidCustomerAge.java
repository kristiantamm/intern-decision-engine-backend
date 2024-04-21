package ee.taltech.inbankbackend.exceptions;

/**
 * Thrown when customers age doesn't meet the requirements.
 */
public class InvalidCustomerAge extends Throwable{
    private final String message;
    private final Throwable cause;

    public InvalidCustomerAge(String message) {
        this(message, null);
    }

    public InvalidCustomerAge(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
