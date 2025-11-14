import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestUnitariEnquestat_Stub {

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
        Enquesta enquesta = new EnquestaStub(1);
        Enquesta enquesta2 = new EnquestaStub(2);

        enquestat.afegirEnquestaRealitzada(enquesta);
        assertThat(enquestat.haRealitzatEnquesta(1), equalTo(true));
        assertThat(enquestat.haRealitzatEnquesta(2), equalTo(false));

        enquestat.afegirEnquestaRealitzada(enquesta2);
        assertThat(enquestat.haRealitzatEnquesta(2), equalTo(true));
    }

    @Test
    public void eliminarEnquestaRealitzadaFuncionaCorrectament() {
        Enquesta enquesta = new EnquestaStub(1);
        Enquesta enquesta2 = new EnquestaStub(2);

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
    public void getEnquestesRealitzadesFuncionaCorrectament() {
        Enquesta enquesta1 = new EnquestaStub(1);
        Enquesta enquesta2 = new EnquestaStub(2);

        enquestat.afegirEnquestaRealitzada(enquesta1);
        enquestat.afegirEnquestaRealitzada(enquesta2);

        assertThat(enquestat.getEnquestesRealitzades().size(), equalTo(2));
        assertThat(enquestat.getEnquestesRealitzades().get(0).getId(), equalTo(1));
        assertThat(enquestat.getEnquestesRealitzades().get(1).getId(), equalTo(2));
    }

    @Test
    public void testEliminarDeLlistaBuida() {
        enquestat.eliminarEnquestaRealitzada(1);
        assertThat(enquestat.getEnquestesRealitzades().size(), equalTo(0));
    }

    @Test
    public void testEliminarEnquestaInexistent() {
        Enquesta enquesta = new EnquestaStub(1);
        enquestat.afegirEnquestaRealitzada(enquesta);

        enquestat.eliminarEnquestaRealitzada(99);

        assertThat(enquestat.getEnquestesRealitzades().size(), equalTo(1));
        assertThat(enquestat.haRealitzatEnquesta(1), equalTo(true));
    }
}
