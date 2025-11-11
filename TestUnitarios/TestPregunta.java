
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class TestPregunta {

    private Pregunta p;

    @Before
    public void setUp() {
        List<String> opcions = Arrays.asList("Java", "Python", "C++");
        p = new Pregunta("Quin és el teu llenguatge preferit?", 2, opcions);
    }

    @Test
    public void testCreacioPregunta() {
        assertEquals("Quin és el teu llenguatge preferit?", p.getText());
        assertEquals(2, p.getTipus());
        assertEquals(Arrays.asList("Java", "Python", "C++"), p.getOpcions());
    }

    @Test
    public void testCreacioPreguntaNumerica() {
        Pregunta pNum = new Pregunta("Edat?", 0, null);
        assertEquals("Edat?", pNum.getText());
        assertEquals(0, pNum.getTipus());
        assertNull(pNum.getOpcions());
    }

    @Test
    public void testCreacioPreguntaLliure() {
        List<String> opcions = Arrays.asList("Java", "Python", "C++");
        Pregunta pLliure = new Pregunta("Comentari?", 1, opcions);
        assertEquals("Comentari?", pLliure.getText());
        assertEquals(1, pLliure.getTipus());
        assertEquals(Arrays.asList("Java", "Python", "C++"), pLliure.getOpcions());

    }

    @Test
    public void testModificarText() {
        p.setText("Nou text de pregunta");
        assertEquals("Nou text de pregunta", p.getText());
    }

    @Test
    public void testNumOpcions() {
        assertEquals(3, p.getNumOpcions());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNumOpcionsPreguntaNumerica() {
        Pregunta pNum = new Pregunta("Test", 0, null);
        pNum.getNumOpcions(); // Ha de llançar excepció
    }

    @Test
    public void testAddResposta() {
        RespostaLliure resposta = new RespostaLliure("Resposta de prova");
        int resultat = p.addResposta(resposta, 1);
        assertEquals(1, resultat);
        assertEquals(1, p.getNumRespostes());
    }

    @Test
    public void testAddRespostaDuplicada() {
        RespostaLliure resposta1 = new RespostaLliure("Resposta 1");
        RespostaLliure resposta2 = new RespostaLliure("Resposta 2");

        int resultat1 = p.addResposta(resposta1, 1);
        int resultat2 = p.addResposta(resposta2, 1); // Mateix usuari

        assertEquals(1, resultat1);
        assertEquals(0, resultat2); // Ha de fallar per usuari duplicat
        assertEquals(1, p.getNumRespostes()); // Només una resposta
    }

    @Test
    public void testToString() {
        String resultat = p.toString();
        assertTrue(resultat.contains("Pregunta Text: Quin és el teu llenguatge preferit?"));
    }

    @After
    public void tearDown() {
        p = null;
    }
}
