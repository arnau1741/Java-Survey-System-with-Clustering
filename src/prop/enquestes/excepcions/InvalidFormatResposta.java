package prop.enquestes.excepcions;
public class InvalidFormatResposta extends Exception {
    public InvalidFormatResposta() {
        super("El format de la resposta és invàlid.");
    }

    public InvalidFormatResposta(String message) {
        super(message);
    }

    public InvalidFormatResposta(String message, Throwable cause) {
        super(message, cause);
    }
    
}
