package prop.enquestes.TestUnitarios;

import org.junit.Before;
import org.junit.Test;
import prop.enquestes.domini.Enquesta;
import prop.enquestes.domini.EnquestaStub;
import prop.enquestes.domini.PerfilAdministrador;
import prop.enquestes.domini.Pregunta;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestUnitariAdministrador_Stub {

    private PerfilAdministrador admin;

    @Before
    public void setUp() {
        admin = new PerfilAdministrador(
                0, "Anna", "1234", "anna@gmail.com"
        );
    }

    // --- Helpers per reduir codi repetit ---
    private EnquestaStub stub(int id) {
        return new EnquestaStub(id);
    }
    private EnquestaStub stub(int id, List<Pregunta> preguntes) {
        return new EnquestaStub(id, preguntes);
    }

    @Test
    public void testConstructor_CorrectInitialization() {
        assertThat(admin.getId(), equalTo(0));
        assertThat(admin.getUsuari(), equalTo("Anna"));
        assertThat(admin.getContrasenya(), equalTo("1234"));
        assertThat(admin.getEmail(), equalTo("anna@gmail.com"));
        assertThat(admin.isBlocked(), equalTo(false));
    }

    @Test
    public void testAfegirEnquestaAdministrada_AddsCorrectly() {
        List<Pregunta> preguntes = new ArrayList<>();
        Pregunta p = new Pregunta("titulo", 0, null);
        preguntes.add(p);
        Enquesta enq1 = stub(1, preguntes);
        Enquesta enq2 = stub(2, preguntes);

        admin.afegirEnquestaAdministrada(enq1);

        assertThat("Enquesta 1 hauria d'existir",
                admin.enquestaAdministrada(1), equalTo(true));
        assertThat("Enquesta 2 no hauria d'existir encara",
                admin.enquestaAdministrada(2), equalTo(false));

        admin.afegirEnquestaAdministrada(enq2);

        assertThat(admin.enquestaAdministrada(2), equalTo(true));
    }

    @Test
    public void testEliminarEnquestaAdministrada_RemovesCorrectly() {
        List<Pregunta> preguntes = new ArrayList<>();
        Pregunta p = new Pregunta("titulo", 0, null);
        preguntes.add(p);
        Enquesta enq1 = stub(1, preguntes);
        Enquesta enq2 = stub(2, preguntes);

        admin.afegirEnquestaAdministrada(enq1);
        admin.afegirEnquestaAdministrada(enq2);

        admin.eliminarEnquestaAdministrada(1);

        assertThat(admin.enquestaAdministrada(1), equalTo(false));
        assertThat(admin.enquestaAdministrada(2), equalTo(true));

        admin.eliminarEnquestaAdministrada(2);

        assertThat(admin.enquestaAdministrada(2), equalTo(false));
    }

    @Test
    public void testGetEnquestesAdministrades_ReturnsListInOrder() {
        List<Pregunta> preguntes = new ArrayList<>();
        Pregunta p = new Pregunta("titulo", 0, null);
        preguntes.add(p);
        Enquesta enq1 = stub(1, preguntes);
        Enquesta enq2 = stub(2, preguntes);

        admin.afegirEnquestaAdministrada(enq1);
        admin.afegirEnquestaAdministrada(enq2);

        List<Enquesta> enquestes = admin.getEnquestesAdministrades();

        assertThat("Nombre d'enquestes incorrecte",
                enquestes.size(), equalTo(2));

        // Comprovar ordre i referències exactes
        assertThat(enquestes.get(0), equalTo(enq1));
        assertThat(enquestes.get(1), equalTo(enq2));
    }

    // --- Casos extrems i robustesa ---

    @Test
    public void testEliminarDeLlistaBuida_DoesNothing() {
        admin.eliminarEnquestaAdministrada(1);
        assertThat(admin.getEnquestesAdministrades().size(), equalTo(0));
    }

    @Test
    public void testEliminarEnquestaInexistent_DoesNotModifyList() {
        List<Pregunta> preguntes = new ArrayList<>();
        Pregunta p = new Pregunta("titulo", 0, null);
        preguntes.add(p);
        Enquesta enq1 = stub(1, preguntes);
        admin.afegirEnquestaAdministrada(enq1);

        admin.eliminarEnquestaAdministrada(99);

        assertThat("La llista no hauria de canviar",
                admin.getEnquestesAdministrades().size(), equalTo(1));
        assertThat(admin.enquestaAdministrada(1), equalTo(true));
    }

    @Test
    public void testEliminarAmbIdNegatiu_DoesNothing() {
        List<Pregunta> preguntes = new ArrayList<>();
        Pregunta p = new Pregunta("titulo", 0, null);
        preguntes.add(p);
        Enquesta enq1 = stub(1, preguntes);
        admin.afegirEnquestaAdministrada(enq1);

        admin.eliminarEnquestaAdministrada(-10);

        assertThat(admin.getEnquestesAdministrades().size(), equalTo(1));
    }
}
