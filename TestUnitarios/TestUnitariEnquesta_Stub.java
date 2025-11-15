import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestUnitariEnquesta_Stub {

    private Enquesta enquesta1, enquesta2;

    @Before
    public void setUp() {
        enquesta1 = new Enquesta(0, "Enquesta de prova",
                "Descripcio de prova", 99, new ArrayList<>());
        enquesta2 = new Enquesta(1, "Segona enquesta",
                "Descripcio segona enquesta", 100, new ArrayList<>());
    }

    @Test
    public void constructorInicialitzaCorrectament() {
        assertThat(enquesta1.getId(), equalTo(0));
        assertThat(enquesta1.getTitol(), equalTo("Enquesta de prova"));
        assertThat(enquesta1.getDescripcio(), equalTo("Descripcio de prova"));
        assertThat(enquesta1.getCreador(), equalTo(99));
        assertThat(enquesta1.getPreguntes(), equalTo(new ArrayList<>()));

        assertThat(enquesta2.getId(), equalTo(1));
        assertThat(enquesta2.getTitol(), equalTo("Segona enquesta"));
        assertThat(enquesta2.getDescripcio(), equalTo("Descripcio segona enquesta"));
        assertThat(enquesta2.getCreador(), equalTo(100));
        assertThat(enquesta2.getPreguntes(), equalTo(new ArrayList<>()));
    }
}