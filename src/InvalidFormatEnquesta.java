public class InvalidFormatEnquesta extends Exception {
    public InvalidFormatEnquesta() {
        super("L'enquesta té un format invàlid.");
    }

    public InvalidFormatEnquesta(String message) {
        super(message);
    }

    public InvalidFormatEnquesta(String message, Throwable cause) {
        super(message, cause);
    }
    
}
