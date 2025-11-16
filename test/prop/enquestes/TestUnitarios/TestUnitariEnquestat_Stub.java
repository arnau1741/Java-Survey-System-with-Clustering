import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestUnitariEnquestat_Stub {

    private PerfilEnquestat enquestat, enquestat2;

    @Before
    public void setUp() {
        enquestat = new PerfilEnquestat(0,"Anna","1234","anna@gmail.com");
        enquestat2 = new PerfilEnquestat(1,"Maria","abcd","maria@gmail.com");
    }

    private EnquestaStub stub(int id) {
        return new EnquestaStub(id);
    }

    // ---------------------------------------------------------------------
    //                           TESTS DEL CONSTRUCTOR
    // ---------------------------------------------------------------------

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

    // ---------------------------------------------------------------------
    //                     TESTS D'AFEGIR ENQUESTES REALITZADES
    // ---------------------------------------------------------------------

    @Test
    public void afegirEnquestaRealitzadaFuncionaCorrectament() {
        Enquesta en1 = stub(1);
        Enquesta en2 = stub(2);

        enquestat.afegirEnquestaRealitzada(en1);

        assertThat(enquestat.haRealitzatEnquesta(1), equalTo(true));
        assertThat(enquestat.haRealitzatEnquesta(2), equalTo(false));

        enquestat.afegirEnquestaRealitzada(en2);
        assertThat(enquestat.haRealitzatEnquesta(2), equalTo(true));
    }

    // ---------------------------------------------------------------------
    //                     TESTS D'ELIMINAR ENQUESTES REALITZADES
    // ---------------------------------------------------------------------

    @Test
    public void eliminarEnquestaRealitzadaFuncionaCorrectament() {
        Enquesta en1 = stub(1);
        Enquesta en2 = stub(2);

        enquestat.afegirEnquestaRealitzada(en1);
        enquestat.afegirEnquestaRealitzada(en2);

        assertThat(enquestat.haRealitzatEnquesta(1), equalTo(true));
        assertThat(enquestat.haRealitzatEnquesta(2), equalTo(true));

        enquestat.eliminarEnquestaRealitzada(1);
        assertThat(enquestat.haRealitzatEnquesta(1), equalTo(false));
        assertThat(enquestat.haRealitzatEnquesta(2), equalTo(true));

        enquestat.eliminarEnquestaRealitzada(2);
        assertThat(enquestat.haRealitzatEnquesta(2), equalTo(false));
    }

    @Test
    public void eliminarDeLlistaBuida_NoProdueixCanvisNiErrors() {
        enquestat.eliminarEnquestaRealitzada(1);
        assertThat(enquestat.getEnquestesRealitzades().size(), equalTo(0));
    }

    @Test
    public void eliminarEnquestaInexistent_NoModificaLaLlista() {
        Enquesta en1 = stub(1);
        enquestat.afegirEnquestaRealitzada(en1);

        enquestat.eliminarEnquestaRealitzada(99);

        assertThat(enquestat.getEnquestesRealitzades().size(), equalTo(1));
        assertThat(enquestat.haRealitzatEnquesta(1), equalTo(true));
    }

    @Test
    public void eliminarEnquestaAmbIdNegatiu_NoFaRes() {
        Enquesta en1 = stub(1);
        enquestat.afegirEnquestaRealitzada(en1);

        enquestat.eliminarEnquestaRealitzada(-5);

        assertThat(enquestat.getEnquestesRealitzades().size(), equalTo(1));
    }

    // ---------------------------------------------------------------------
    //                           CONSULTA DE LLISTA
    // ---------------------------------------------------------------------

    @Test
    public void getEnquestesRealitzades_RetornaLlistaAmbOrdreCorrecte() {
        Enquesta en1 = stub(1);
        Enquesta en2 = stub(2);

        enquestat.afegirEnquestaRealitzada(en1);
        enquestat.afegirEnquestaRealitzada(en2);

        List<Enquesta> llista = enquestat.getEnquestesRealitzades();

        assertThat(llista.size(), equalTo(2));
        assertThat(llista.get(0), equalTo(en1));
        assertThat(llista.get(1), equalTo(en2));
    }
}
