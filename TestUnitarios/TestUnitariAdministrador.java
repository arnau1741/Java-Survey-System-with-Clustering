import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matcher.*;

public class TestUnitariAdministrador {

    private PerfilAdministrador admin, admin2;

    @Before
    public void setUp() {
        admin = new PerfilAdministrador(
                0,"Anna", "1234",
                "anna@gmail.com");
        admin2 = new PerfilAdministrador(
                1,"Joan", "5678",
                "joan@gmail.com");
    }

    @Test
    public void testConstructor() {
        assertThat(admin.getId(), equalTo(0));
        assertThat(admin.getUsuari(), equalTo("Anna"));
        assertThat(admin.getContrasenya(), equalTo("1234"));
        assertThat(admin.getEmail(), equalTo("anna@gmail.com"));
        assertThat(admin.isBlocked(), equalTo(false));
    }

    @Test
    public void testAfegirEnquestaAdministrada() {
        Enquesta enquesta = new Enquesta(1, "Titol1", "Desc1", 2, new java.util.ArrayList<>());
        Enquesta enquesta2 = new Enquesta(2, "Titol2", "Desc2", 3, new java.util.ArrayList<>());
        admin.afegirEnquestaAdministrada(enquesta);
        assertThat(admin.enquestaAdministrada(1), equalTo(true));
        assertThat(admin.enquestaAdministrada(2), equalTo(false));
        admin.afegirEnquestaAdministrada(enquesta2);
        assertThat(admin.enquestaAdministrada(2), equalTo(true));
    }

    @Test
    public void testEliminarEnquestaAdministrada() {
        Enquesta enquesta = new Enquesta(1, "Titol1", "Desc1", 2, new java.util.ArrayList<>());
        Enquesta enquesta2 = new Enquesta(2, "Titol2", "Desc2", 3, new java.util.ArrayList<>());
        admin.afegirEnquestaAdministrada(enquesta);
        admin.afegirEnquestaAdministrada(enquesta2);
        admin.eliminarEnquestaAdministrada(1);
        assertThat(admin.enquestaAdministrada(1), equalTo(false));
        assertThat(admin.enquestaAdministrada(2), equalTo(true));
        admin.eliminarEnquestaAdministrada(2);
        assertThat(admin.enquestaAdministrada(2), equalTo(false));
    }

    @Test
    public void testGetEnquestesAdministrades() {
        Enquesta enquesta = new Enquesta(1, "Titol1", "Desc1", 2, new java.util.ArrayList<>());
        Enquesta enquesta2 = new Enquesta(2, "Titol2", "Desc2", 3, new java.util.ArrayList<>());
        admin.afegirEnquestaAdministrada(enquesta);
        admin.afegirEnquestaAdministrada(enquesta2);
        java.util.List<Enquesta> enquestes = admin.getEnquestesAdministrades();
        assertThat(enquestes.size(), equalTo(2));
        assertThat(enquestes.get(0), equalTo(enquesta));
        assertThat(enquestes.get(1), equalTo(enquesta2));
    }
}
