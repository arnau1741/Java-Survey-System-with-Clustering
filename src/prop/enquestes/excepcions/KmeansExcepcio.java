package prop.enquestes.excepcions;
public class KmeansExcepcio extends Exception {
    public KmeansExcepcio() {
        super("Error durant l'execució de l'algorisme KMeans.");
    }

    public KmeansExcepcio(String message) {
        super(message);
    }

    public KmeansExcepcio(String message, Throwable cause) {
        super(message, cause);
    }
    
}
