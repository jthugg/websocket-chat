package neo.chat.application.service.exception;

public abstract class ApplicationException extends RuntimeException {

    public ApplicationException() {}

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public abstract String getMessage();

}
