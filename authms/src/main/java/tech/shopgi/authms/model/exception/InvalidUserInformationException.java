package tech.shopgi.authms.model.exception;

public class InvalidUserInformationException extends Exception {

    public InvalidUserInformationException() {
        super("Invalid username or password.");
    }

    public InvalidUserInformationException(String message) {
        super(message);
    }
}
