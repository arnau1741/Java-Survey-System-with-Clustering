import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matcher.*;

public class TestUnitariEnquestat {

    private PerfilEnquestat enquestat, enquestat2;

    @Before
    public void setUp() {
        enquestat = new PerfilEnquestat(
                0,"Anna", "1234",
                "anna@gmail.com");
        enquestat2 = new PerfilEnquestat(
                1,"Maria", "abcd",
                "maria@gmail.com");
    }

    @Test
    public void constructorInicialitzaCorrectament() {
        assertThat(enquestat.getUsuari(), equalTo("Anna"));
        assertThat(enquestat.getId(), equalTo(0));
        assertThat(enquestat.getContrasenya(), equalTo("1234"));
        assertThat(enquestat.getEmail(), equalTo("anna@gmail.com"));
        assertThat(enquestat.isBlocked(), equalTo(false));

        assertThat(enquestat2.getUsuari(), equalTo("Maria"));
        assertThat(enquestat2.getId(), equalTo(1));
        assertThat(enquestat2.getContrasenya(), equalTo("abcd"));
        assertThat(enquestat2.getEmail(), equalTo("maria@gmail.com"));
        assertThat(enquestat2.isBlocked(), equalTo(false));
    }

    @Test
    public void afegirEnquestaRealitzadaFuncionaCorrectament() {
        //id, titol, descripcio, idcreador, preguntes
        Enquesta enquesta = new Enquesta(1, "Titol1", "Desc1", 2, new java.util.ArrayList<>());
        Enquesta enquesta2 = new Enquesta(2, "Titol2", "Desc2", 3, new java.util.ArrayList<>());
        enquestat.afegirEnquestaRealitzada(enquesta);
        assertThat(enquestat.haRealitzatEnquesta(1), equalTo(true));
        assertThat(enquestat.haRealitzatEnquesta(2), equalTo(false));
        enquestat.afegirEnquestaRealitzada(enquesta2);
        assertThat(enquestat.haRealitzatEnquesta(2), equalTo(true));
    }

    @Test
    public void eliminarEnquestaRealitzadaFuncionaCorrectament() {
        Enquesta enquesta = new Enquesta(1, "Titol1", "Desc1", 2, new java.util.ArrayList<>());
        Enquesta enquesta2 = new Enquesta(2, "Titol2", "Desc2", 3, new java.util.ArrayList<>());
        enquestat.afegirEnquestaRealitzada(enquesta);
        enquestat.afegirEnquestaRealitzada(enquesta2);
        assertThat(enquestat.haRealitzatEnquesta(1), equalTo(true));
        assertThat(enquestat.haRealitzatEnquesta(2), equalTo(true));
        enquestat.eliminarEnquestaRealitzada(1);
        assertThat(enquestat.haRealitzatEnquesta(1), equalTo(false));
        assertThat(enquestat.haRealitzatEnquesta(2), equalTo(true));
        enquestat.eliminarEnquestaRealitzada(2);
        assertThat(enquestat.haRealitzatEnquesta(2), equalTo(false));
    }

    @Test
    public void haRealitzatEnquestaFuncionaCorrectament() {
        Enquesta enquesta = new Enquesta(1, "Titol1", "Desc1", 2, new java.util.ArrayList<>());
        assertThat(enquestat.haRealitzatEnquesta(1), equalTo(false));
        enquestat.afegirEnquestaRealitzada(enquesta);
        assertThat(enquestat.haRealitzatEnquesta(1), equalTo(true));
    }
}
