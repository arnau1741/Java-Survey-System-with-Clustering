import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class TestUnitariEnquesta_Mock {

    private Enquesta enquesta;

    @Mock
    private Pregunta preguntaMock1;

    @Mock
    private Pregunta preguntaMock2;

    @Mock
    private Resposta respostaMock1;

    @Mock
    private Resposta respostaMock2;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        List<Pregunta> preguntes = new ArrayList<>();
        preguntes.add(preguntaMock1);
        preguntes.add(preguntaMock2);

        enquesta = new Enquesta(1, "Enquesta de satisfacció", "Valora el servei", 0, preguntes);

        when(preguntaMock1.getText()).thenReturn("Pregunta 1");
        when(preguntaMock2.getText()).thenReturn("Pregunta 2");
        when(preguntaMock1.getTipus()).thenReturn(0);
        when(preguntaMock2.getTipus()).thenReturn(1);
        when(preguntaMock2.getNumOpcions()).thenReturn(3);
        when(preguntaMock2.getOpcions()).thenReturn(Arrays.asList("Opció 1", "Opció 2", "Opció 3"));
    }

    @Test
    public void testGetters() {
        assertThat(enquesta.getId(), equalTo(1));
        assertThat(enquesta.getTitol(), equalTo("Enquesta de satisfacció"));
        assertThat(enquesta.getDescripcio(), equalTo("Valora el servei"));
        assertThat(enquesta.getCreador(), equalTo(0));
        assertThat(enquesta.getNumPreguntes(), equalTo(2));
    }

    @Test
    public void testStringARespostesNumerica() {
        List<String> respostesStr = Arrays.asList("25.5", "2");
        when(preguntaMock1.getTipus()).thenReturn(0);
        when(preguntaMock2.getTipus()).thenReturn(1);
        when(preguntaMock2.getNumOpcions()).thenReturn(3);

        List<Resposta> resultat = enquesta.stringARespostes(respostesStr);
        assertThat(resultat.size(), equalTo(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStringARespostesNumericaInvalida() {
        List<String> respostesStr = Arrays.asList("texto_invalido", "2");
        when(preguntaMock1.getTipus()).thenReturn(0);
        when(preguntaMock2.getTipus()).thenReturn(1);
        when(preguntaMock2.getNumOpcions()).thenReturn(3);

        enquesta.stringARespostes(respostesStr);
    }

    @Test
    public void testStringARespostesLliure() {
        List<String> respostesStr = Arrays.asList("30.0", "comentari");
        when(preguntaMock1.getTipus()).thenReturn(0);
        when(preguntaMock2.getTipus()).thenReturn(4);

        List<Resposta> resultat = enquesta.stringARespostes(respostesStr);
        assertThat(resultat.size(), equalTo(2));
    }

    @Test
    public void testGetPreguntesFormatString() {
        when(preguntaMock1.getTipus()).thenReturn(0);
        when(preguntaMock2.getTipus()).thenReturn(1);
        when(preguntaMock1.getText()).thenReturn("Pregunta 1");
        when(preguntaMock2.getText()).thenReturn("Pregunta 2");
        when(preguntaMock2.getOpcions()).thenReturn(Arrays.asList("A", "B", "C"));

        List<String> preguntes = enquesta.getPreguntes();
        assertThat(preguntes.contains("NUMERICA"), equalTo(true));
        assertThat(preguntes.contains("UNICA"), equalTo(true));
        assertThat(preguntes.contains("Pregunta 1"), equalTo(true));
        assertThat(preguntes.contains("3"), equalTo(true));
        assertThat(preguntes.contains("- - -"), equalTo(true));
    }

    @Test
    public void testAfegeixResposta() {
        Map<Integer, Resposta> mapa1 = new HashMap<>();
        Map<Integer, Resposta> mapa2 = new HashMap<>();
        when(preguntaMock1.getRespostes()).thenReturn(mapa1);
        when(preguntaMock2.getRespostes()).thenReturn(mapa2);

        List<String> respostes = Arrays.asList("25.5", "2");
        enquesta.afegeixResposta(5, respostes);

        verify(preguntaMock1, times(1)).addResposta(any(Resposta.class), eq(5));
        verify(preguntaMock2, times(1)).addResposta(any(Resposta.class), eq(5));
    }

    @Test
    public void testAfegeixRespostaMultiplesUsuaris() {
        Map<Integer, Resposta> mapa1 = new HashMap<>();
        Map<Integer, Resposta> mapa2 = new HashMap<>();
        when(preguntaMock1.getRespostes()).thenReturn(mapa1);
        when(preguntaMock2.getRespostes()).thenReturn(mapa2);

        List<String> respostes1 = Arrays.asList("25.5", "2");
        List<String> respostes2 = Arrays.asList("30.0", "1");

        enquesta.afegeixResposta(5, respostes1);
        enquesta.afegeixResposta(6, respostes2);

        verify(preguntaMock1, times(2)).addResposta(any(Resposta.class), anyInt());
        verify(preguntaMock2, times(2)).addResposta(any(Resposta.class), anyInt());
    }

    @Test
    public void testGetRespostesUsuari() {
        Map<Integer, Resposta> mapa1 = new HashMap<>();
        Map<Integer, Resposta> mapa2 = new HashMap<>();
        mapa1.put(5, respostaMock1);
        mapa2.put(5, respostaMock2);

        when(preguntaMock1.getRespostes()).thenReturn(mapa1);
        when(preguntaMock2.getRespostes()).thenReturn(mapa2);

        List<Resposta> resultat = enquesta.getRespostesUsuari(5);
        assertThat(resultat.size(), equalTo(2));
        assertThat(resultat.get(0), equalTo(respostaMock1));
        assertThat(resultat.get(1), equalTo(respostaMock2));
    }

    @Test
    public void testGetRespostesUsuariNoExistent() {
        Map<Integer, Resposta> mapaVuit1 = new HashMap<>();
        Map<Integer, Resposta> mapaVuit2 = new HashMap<>();

        when(preguntaMock1.getRespostes()).thenReturn(mapaVuit1);
        when(preguntaMock2.getRespostes()).thenReturn(mapaVuit2);

        List<Resposta> resultat = enquesta.getRespostesUsuari(999);
        assertThat(resultat.size(), equalTo(2));
        assertThat(resultat.get(0), equalTo(null));
        assertThat(resultat.get(1), equalTo(null));
    }

    @Test
    public void testCanviarPregunta() {
        Pregunta novaPregunta = mock(Pregunta.class);
        when(novaPregunta.getText()).thenReturn("Nova pregunta");

        enquesta.canviarPregunta(0, novaPregunta);

        List<Pregunta> preguntesObj = enquesta.getPreguntesObj();
        assertThat(preguntesObj.get(0), equalTo(novaPregunta));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCanviarPreguntaIndexFueraDeRango() {
        Pregunta novaPregunta = mock(Pregunta.class);
        enquesta.canviarPregunta(99, novaPregunta);
    }

    @Test
    public void testGetPreguntesObj() {
        List<Pregunta> preguntesObj = enquesta.getPreguntesObj();
        assertThat(preguntesObj.size(), equalTo(2));
        assertThat(preguntesObj.contains(preguntaMock1), equalTo(true));
        assertThat(preguntesObj.contains(preguntaMock2), equalTo(true));
    }

    @Test
    public void testGetNumRespostes() {
        when(preguntaMock1.getNumRespostes()).thenReturn(3);
        assertThat(enquesta.getNumRespostes(), equalTo(3));
        verify(preguntaMock1, times(1)).getNumRespostes();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNumRespostesEnquestaVacia() {
        List<Pregunta> preguntesVacias = new ArrayList<>();
        Enquesta enqVacia = new Enquesta(3, "Buit", "Buit", 1, preguntesVacias);
    }
}
