package prop.enquestes.excepcions;
public class FileNotFound extends Exception {
    public FileNotFound() {
        super("El fitxer no s'ha trobat.");
    }

    public FileNotFound(String message) {
        super(message);
    }

    public FileNotFound(String message, Throwable cause) {
        super(message, cause);
    }
    
}
