
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

    /// ========Casos basics============

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

    /// ==================Casos extrems===========
    ///
    ///
    ///
    ///
    @Test
    public void testCreacioPreguntaTextMassaLlarg() {
        // Text molt llarg
        String textMassaLlarg = "A".repeat(1000);
        Pregunta pLlarga = new Pregunta(textMassaLlarg, 0, null);
        assertEquals(textMassaLlarg, pLlarga.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreacioPreguntaTipusInvalid() {
        // Tipus de pregunta no existent
        new Pregunta("Pregunta invàlida", 99, null);
    }

    @Test
    public void testCreacioPreguntaTextBuit() {
        // Text buit
        Pregunta pBuit = new Pregunta("", 0, null);
        assertEquals("", pBuit.getText());
    }

    @Test
    public void testCreacioPreguntaTextNull() {
        // Text null
        Pregunta pNull = new Pregunta(null, 0, null);
        assertNull(pNull.getText());
    }

    @Test
    public void testCreacioPreguntaMoltesOpcions() {
        // Moltes opcions
        List<String> moltesOpcions = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            moltesOpcions.add("Opció " + i);
        }
        Pregunta pMoltesOpcions = new Pregunta("Test", 1, moltesOpcions);
        assertEquals(100, pMoltesOpcions.getNumOpcions());
    }

    @Test(expected = NullPointerException.class)
    public void testCreacioPreguntaSenseOpcionsQuanCalen() {
        Pregunta pSenseOpcions = new Pregunta("Test", 1, null);
        assertNull(pSenseOpcions.getOpcions());
    }

    @Test
    public void testAddRespostaUsuariNegatiu() {
        // ID d'usuari negatiu
        RespostaLliure resposta = new RespostaLliure("Resposta");
        //Si id = -1 es usuari no registrat, en altres casos
        int resultat = p.addResposta(resposta, -3);
        //Ha de ser igual degut a que no tenim excepcions d'usuaris negatius
        assertEquals(1, resultat);
        assertEquals(1, p.getNumRespostes());
    }

    @Test
    public void testAddRespostaNull() {
        // Resposta null
        int resultat = p.addResposta(null, 1);
        //Hauria de ser 1 degut a que comprovem si l'usuari a respost
        //Si la resposta es nula no importa
        assertEquals(1, resultat);
    }

    @Test
    public void testAddMoltesRespostes() {
        // Moltes respostes del mateix usuari
        for (int i = 0; i < 100; i++) {
            RespostaLliure resposta = new RespostaLliure("Resposta " + i);
            int resultat = p.addResposta(resposta, i); // Usuaris diferents
            assertEquals(1, resultat);
        }
        assertEquals(100, p.getNumRespostes());
    }

    @Test
    public void testGetRespostaModaAmbRespostes() {
        // Moda amb respostes
        RespostaUnica r1 = new RespostaUnica(3);
        r1.setResposta(0);
        p.addResposta(r1, 1);

        RespostaUnica r2 = new RespostaUnica(3);
        r2.setResposta(0);
        p.addResposta(r2, 2);

        RespostaUnica r3 = new RespostaUnica(3);
        r3.setResposta(1);
        p.addResposta(r3, 3);

        Resposta moda = p.getRespostaModa();
        assertNotNull(moda);
    }

    @Test
    public void testSetTextNull() {
        // Modificar text a null
        p.setText(null);
        assertNull(p.getText());
    }

    @After
    public void tearDown() {
        p = null;
    }
}
