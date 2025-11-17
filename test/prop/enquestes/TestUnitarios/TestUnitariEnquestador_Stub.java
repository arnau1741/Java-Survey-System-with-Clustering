package prop.enquestes.TestUnitarios;
import org.junit.Before;
import org.junit.Test;
import prop.enquestes.domini.Enquesta;
import prop.enquestes.domini.EnquestaStub;
import prop.enquestes.domini.PerfilEnquestador;
import prop.enquestes.domini.Pregunta;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestUnitariEnquestador_Stub {

    private PerfilEnquestador enquestador;

    @Before
    public void setUp() {
        enquestador = new PerfilEnquestador(
                0, "Anna", "1234", "anna@gmail.com"
        );
    }

    private EnquestaStub stub(int id) {
        return new EnquestaStub(id);
    }

    private EnquestaStub stub(int id, List<Pregunta> preguntes) {
        return new EnquestaStub(id, preguntes);
    }

    // --------------------------
    //   TESTS DEL CONSTRUCTOR
    // --------------------------

    @Test
    public void constructorInicialitzaCorrectament() {
        assertThat(enquestador.getUsuari(), equalTo("Anna"));
        assertThat(enquestador.getId(), equalTo(0));
        assertThat(enquestador.getContrasenya(), equalTo("1234"));
        assertThat(enquestador.getEmail(), equalTo("anna@gmail.com"));
        assertThat(enquestador.isBlocked(), equalTo(false));
    }

    // --------------------------
    //   TESTS D'AFEGIR ENQUESTES
    // --------------------------

    @Test
    public void afegirEnquestaAssignadaFuncionaCorrectament() {
        List<Pregunta> preguntes = new ArrayList<>();
        Pregunta p = new Pregunta("titulo", 0, null);
        preguntes.add(p);
        Enquesta enq1 = stub(1, preguntes);
        Enquesta enq2 = stub(2, preguntes);

        enquestador.afegirEnquestaAssignada(enq1);
        enquestador.afegirEnquestaAssignada(enq2);

        assertThat(enquestador.enquestaAssignada(1), equalTo(true));
        assertThat(enquestador.enquestaAssignada(2), equalTo(true));
    }

    @Test
    public void eliminarEnquestaAssignadaFuncionaCorrectament() {
        List<Pregunta> preguntes = new ArrayList<>();
        Pregunta p = new Pregunta("titulo", 0, null);
        preguntes.add(p);
        Enquesta enq1 = stub(1, preguntes);
        enquestador.afegirEnquestaAssignada(enq1);

        enquestador.eliminarEnquestaAssignada(1);

        assertThat(enquestador.enquestaAssignada(1), equalTo(false));
        assertThat(enquestador.getEnquestesAssignades().size(), equalTo(0));
    }

    @Test
    public void eliminarDeLlistaBuida_NoProdueixErrorINoCanviaRes() {
        enquestador.eliminarEnquestaAssignada(1);
        assertThat(enquestador.getEnquestesAssignades().size(), equalTo(0));
    }

    @Test
    public void eliminarEnquestaInexistent_NoModificaLaLlista() {
        List<Pregunta> preguntes = new ArrayList<>();
        Pregunta p = new Pregunta("titulo", 0, null);
        preguntes.add(p);
        Enquesta enq1 = stub(1, preguntes);

        enquestador.afegirEnquestaAssignada(enq1);

        enquestador.eliminarEnquestaAssignada(99);

        assertThat(enquestador.getEnquestesAssignades().size(), equalTo(1));
        assertThat(enquestador.enquestaAssignada(1), equalTo(true));
    }

    @Test
    public void eliminarEnquestaAmbIdNegatiu_NoFaRes() {
        List<Pregunta> preguntes = new ArrayList<>();
        Pregunta p = new Pregunta("titulo", 0, null);
        preguntes.add(p);
        Enquesta enq1 = stub(1, preguntes);

        enquestador.afegirEnquestaAssignada(enq1);

        enquestador.eliminarEnquestaAssignada(-10);

        assertThat(enquestador.getEnquestesAssignades().size(), equalTo(1));
    }

    // --------------------------
    //   CONSULTA DE LA LLISTA
    // --------------------------

    @Test
    public void getEnquestesAssignades_RetornaLlistaEnOrdreCorrecte() {
        List<Pregunta> preguntes = new ArrayList<>();
        Pregunta p = new Pregunta("titulo", 0, null);
        preguntes.add(p);
        Enquesta enq1 = stub(1, preguntes);
        Enquesta enq2 = stub(2, preguntes);


        enquestador.afegirEnquestaAssignada(enq1);
        enquestador.afegirEnquestaAssignada(enq2);

        List<Enquesta> llista = enquestador.getEnquestesAssignades();

        assertThat(llista.size(), equalTo(2));
        assertThat(llista.get(0), equalTo(enq1));
        assertThat(llista.get(1), equalTo(enq2));
    }
}
