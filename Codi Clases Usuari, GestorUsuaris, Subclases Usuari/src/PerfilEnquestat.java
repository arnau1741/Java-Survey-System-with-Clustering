public class PerfilEnquestat extends Usuari
{
    // Constructor
    public PerfilEnquestat(int id, String usuari, String email) {
        super(id, usuari, email);
    }

    //para el test de pruebas de las subclases
    private String especialitat;

    public void setEspecialitat(String especialitat) {
        this.especialitat = especialitat;
        //se podrian hacer mas cosas al ser abstracta y seria diferente en cada subclase
    }

    public String getEspecialitat() {
        return especialitat;
    }
}
