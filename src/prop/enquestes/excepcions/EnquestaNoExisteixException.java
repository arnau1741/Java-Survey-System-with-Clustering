package prop.enquestes.excepcions;
public class EnquestaNoExisteixException extends Exception {
    public EnquestaNoExisteixException() {
        super("L'enquesta no existeix.");
    }

    public EnquestaNoExisteixException(String message) {
        super(message);
    }

    public EnquestaNoExisteixException(String message, Throwable cause) {
        super(message, cause);
    }
}