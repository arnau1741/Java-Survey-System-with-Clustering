package prop.enquestes.excepcions;

public class UsuariNoValid extends Exception {
    public UsuariNoValid() {
        super("L'enquesta té un format invàlid.");
    }

    public UsuariNoValid(String message) {
        super(message);
    }

    public UsuariNoValid(String message, Throwable cause) {
        super(message, cause);
    }
}
