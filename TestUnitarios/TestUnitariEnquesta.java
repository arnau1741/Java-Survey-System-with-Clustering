
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class TestUnitariEnquesta {

    private Enquesta e;

    @Before
    public void setUp() {
        List<Pregunta> preguntes = new ArrayList<>();

       preguntes.add(new Pregunta("Edat?", 0, new ArrayList<>()));
       preguntes.add(new Pregunta("Color preferit?", 1, Arrays.asList("Groc", "Blau", "Vermell", "Verd")));
       preguntes.add(new Pregunta("Aficions?", 3, Arrays.asList("Cinema", "Esport", "Lectura")));
       preguntes.add(new  Pregunta("Comentaris?", 4, new ArrayList<>()));

        e = new Enquesta(1, "Enquesta de prova", "Test unitari", 99, preguntes);
    }

    @Test
    public void testGetters() {
        assertEquals(Integer.valueOf(1), e.getId());
        assertEquals("Enquesta de prova", e.getTitol());
        assertEquals("Test unitari", e.getDescripcio());
        assertEquals(Integer.valueOf(99), e.getCreador());
        assertEquals(4, e.getNumPreguntes());
        assertEquals(0, e.getNumRespostes());
    }

    @Test
    public void testStringARespostes() {
        List<String> respostesStr = Arrays.asList("25.5", "1", "0,2", "Cap comentari");
        List<Resposta> respostes = e.stringARespostes(respostesStr);

        assertEquals(4, respostes.size());
        assertTrue(respostes.get(0) instanceof RespostaNumerica);
        assertTrue(respostes.get(1) instanceof RespostaUnica);
        assertTrue(respostes.get(2) instanceof RespostaMultiple);
        assertTrue(respostes.get(3) instanceof RespostaLliure);
    }

    @Test
    public void testGetPreguntes() {
        List<String> preguntesTxt = e.getPreguntes();
        assertEquals(17, preguntesTxt.size());
    }

    @Test
    public void testAfegeixResposta() {
        List<Resposta> respostes = new ArrayList<>();
        respostes.add(new RespostaNumerica(25.5));
        // UNICA
        RespostaUnica rUnica = new RespostaUnica(e.getPreguntesObj().get(1).getNumOpcions());
        rUnica.setResposta(1);
        respostes.add(rUnica);
        // respostes.add(new RespostaUnica(1));
        // MULTIPLE
        RespostaMultiple rMultiple = new RespostaMultiple(e.getPreguntesObj().get(2).getNumOpcions());
        rMultiple.selecciona(Arrays.asList(0, 2)); // selecciona opcions 0 i 2
        respostes.add(rMultiple);
        // respostes.add(new RespostaMultiple(Arrays.asList(0, 2)));
        respostes.add(new RespostaLliure("Cap comentari"));

        e.afegeixResposta(1, respostes);
        assertEquals(1, e.getNumRespostes());
    }

    @Test
    public void testGetRespostesUsuari() {
        List<Resposta> respostes = new ArrayList<>();
        respostes.add(new RespostaNumerica(25.5));
        // UNICA
        RespostaUnica rUnica = new RespostaUnica(e.getPreguntesObj().get(1).getNumOpcions());
        rUnica.setResposta(1);
        respostes.add(rUnica);
        // respostes.add(new RespostaUnica(1));
        // MULTIPLE
        RespostaMultiple rMultiple = new RespostaMultiple(e.getPreguntesObj().get(2).getNumOpcions());
        rMultiple.selecciona(Arrays.asList(0, 2)); // selecciona opcions 0 i 2
        respostes.add(rMultiple);
        // respostes.add(new RespostaMultiple(Arrays.asList(0, 2)));
        respostes.add(new RespostaLliure("Cap comentari"));

        e.afegeixResposta(1, respostes);
        List<Resposta> obtenides = e.getRespostesUsuari(1);

        assertEquals(4, obtenides.size());
        assertTrue(obtenides.get(0) instanceof RespostaNumerica);
        assertTrue(obtenides.get(1) instanceof RespostaUnica);
        assertTrue(obtenides.get(2) instanceof RespostaMultiple);
        assertTrue(obtenides.get(3) instanceof RespostaLliure);
    }
}
