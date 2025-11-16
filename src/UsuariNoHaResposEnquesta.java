public class UsuariNoHaResposEnquesta extends Exception {
    public UsuariNoHaResposEnquesta() {
        super("L'usuari no ha respost l'enquesta.");
    }

    public UsuariNoHaResposEnquesta(String message) {
        super(message);
    }

    public UsuariNoHaResposEnquesta(String message, Throwable cause) {
        super(message, cause);
    }
    
}
