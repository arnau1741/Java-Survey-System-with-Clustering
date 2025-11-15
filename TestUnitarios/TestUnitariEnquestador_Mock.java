import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class TestUnitariEnquestador_Mock {

    private PerfilEnquestador enquestador;

    @Mock
    private Enquesta enquestaMock1;

    @Mock
    private Enquesta enquestaMock2;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        enquestador = new PerfilEnquestador(
                0, "Anna", "1234",
                "anna@gmail.com");

        when(enquestaMock1.getId()).thenReturn(1);
        when(enquestaMock2.getId()).thenReturn(2);
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
        enquestador.afegirEnquestaAssignada(enquestaMock1);
        enquestador.afegirEnquestaAssignada(enquestaMock2);
        assertThat(enquestador.enquestaAssignada(1), equalTo(true));
        assertThat(enquestador.enquestaAssignada(2), equalTo(true));
    }

    @Test
    public void eliminarEnquestaAssignadaFuncionaCorrectament() {
        enquestador.afegirEnquestaAssignada(enquestaMock1);
        enquestador.eliminarEnquestaAssignada(1);
        assertThat(enquestador.enquestaAssignada(1), equalTo(false));
    }

    @Test
    public void getEnquestesAssignadesFuncionaCorrectament() {
        enquestador.afegirEnquestaAssignada(enquestaMock1);
        enquestador.afegirEnquestaAssignada(enquestaMock2);
        assertThat(enquestador.getEnquestesAssignades().size(), equalTo(2));
        assertThat(enquestador.getEnquestesAssignades().get(0), equalTo(enquestaMock1));
        assertThat(enquestador.getEnquestesAssignades().get(1), equalTo(enquestaMock2));
    }

    //Casos extrems
    @Test
    public void testEliminarDeLlistaBuida() {
        enquestador.eliminarEnquestaRealitzada(1);
        assertThat(enquestador.getEnquestesAssignades().size(), equalTo(0));
    }

    @Test
    public void testEliminarEnquestaInexistent() {
        enquestador.afegirEnquestaAssignada(enquestaMock1);
        enquestador.eliminarEnquestaAssignada(99);
        assertThat(enquestador.getEnquestesAssignades().size(), equalTo(1));
        assertThat(enquestador.enquestaAssignada(1), equalTo(true));
    }
}
