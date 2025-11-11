import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matcher.*;

public class TestUnitariEnquestador {

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
        Enquesta enquesta = new Enquesta(1, "Titol1", "Desc1", 2, new java.util.ArrayList<>());
        Enquesta enquesta2 = new Enquesta(2, "Titol2", "Desc2", 3, new java.util.ArrayList<>());
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
        Enquesta enquesta1 = new Enquesta(1, "Titol1", "Desc1", 2, new java.util.ArrayList<>());
        Enquesta enquesta2 = new Enquesta(2, "Titol2", "Desc2", 3, new java.util.ArrayList<>());
        enquestador.afegirEnquestaAssignada(enquesta1);
        enquestador.afegirEnquestaAssignada(enquesta2);
        assertThat(enquestador.getEnquestesAssignades().size(), equalTo(2));
        assertThat(enquestador.getEnquestesAssignades().get(0), equalTo(enquesta1));
        assertThat(enquestador.getEnquestesAssignades().get(1), equalTo(enquesta2));
    }
}
