import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestUnitariEnquestador_Stub {

    private PerfilEnquestador enquestador;

    @Before
    public void setUp() {
        enquestador = new PerfilEnquestador(
                0,"Anna", "1234",
                "anna@gmail.com");
    }

    @Test
    public void constructorInicialitzaCorrectament() {
        assertThat(enquestador.getUsuari(), equalTo("Anna"));
        assertThat(enquestador.getId(), equalTo(0));
        assertThat(enquestador.getContrasenya(), equalTo("1234"));
        assertThat(enquestador.getEmail(), equalTo("anna@gmail.com"));
        assertThat(enquestador.isBlocked(), equalTo(false));
    }

    @Test
    public void afegirEnquestaAssignadaFuncionaCorrectament() {
        //id, titol, descripcio, idcreador, preguntes
        Enquesta enquesta = new EnquestaStub(1);
        Enquesta enquesta2 = new EnquestaStub(2);
        enquestador.afegirEnquestaAssignada(enquesta);
        enquestador.afegirEnquestaAssignada(enquesta2);
        assertThat(enquestador.enquestaAssignada(1), equalTo(true));
        assertThat(enquestador.enquestaAssignada(2), equalTo(true));
    }

    @Test
    public void eliminarEnquestaAssignadaFuncionaCorrectament() {
        Enquesta enquesta = new Enquesta(1, "Titol1", "Desc1", 2, new java.util.ArrayList<>());
        enquestador.afegirEnquestaAssignada(enquesta);
        enquestador.eliminarEnquestaAssignada(1);
        assertThat(enquestador.enquestaAssignada(1), equalTo(false));
    }

    @Test
    public void getEnquestesAssignadesFuncionaCorrectament() {
        Enquesta enquesta1 = new EnquestaStub(1);
        Enquesta enquesta2 = new EnquestaStub(2);
        enquestador.afegirEnquestaAssignada(enquesta1);
        enquestador.afegirEnquestaAssignada(enquesta2);
        assertThat(enquestador.getEnquestesAssignades().size(), equalTo(2));
        assertThat(enquestador.getEnquestesAssignades().get(0), equalTo(enquesta1));
        assertThat(enquestador.getEnquestesAssignades().get(1), equalTo(enquesta2));
    }

    //Casos extrems
    @Test
    public void testEliminarDeLlistaBuida() {
        enquestador.eliminarEnquestaRealitzada(1);
        assertThat(enquestador.getEnquestesAssignades().size(), equalTo(0));
    }

    @Test
    public void testEliminarEnquestaInexistent() {
        // Afegeix una enquesta
        Enquesta enquesta = new EnquestaStub(1);
        enquestador.afegirEnquestaAssignada(enquesta);

        // Intenta eliminar una que no existeix
        enquestador.eliminarEnquestaAssignada(99);

        // Comprova que la llista no ha canviat i l'enquesta original segueix allà
        assertThat(enquestador.getEnquestesAssignades().size(), equalTo(1));
        assertThat(enquestador.enquestaAssignada(1), equalTo(true));
    }
}
