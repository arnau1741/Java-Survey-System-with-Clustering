public class enquestaNoExisteixException extends Exception {
    public enquestaNoExisteixException() {
        super("L'enquesta no existeix.");
    }

    public enquestaNoExisteixException(String message) {
        super(message);
    }

    public enquestaNoExisteixException(String message, Throwable cause) {
        super(message, cause);
    }
}