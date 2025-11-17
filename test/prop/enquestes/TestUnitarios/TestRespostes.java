package prop.enquestes.TestUnitarios;

// java
import org.junit.Test;
import prop.enquestes.domini.*;

import static org.junit.Assert.*;

import java.util.*;

/**
 * Test conjunt per totes les subclasses de Resposta.
 * Inclou: Numerica, Lliure, Unica, Multiple, Ordenada.
 */
public class TestRespostes {


    ///==============Casos basics===========
    @Test
    public void testRespostaNumerica() {
        RespostaNumerica rn = new RespostaNumerica(7.5);
        assertTrue(rn.EsContestat());
        assertEquals("7.5", rn.getText(null));
        assertEquals(7.5, rn.getValor(), 0.001);
    }

    @Test
    public void testRespostaNumericaNoContestada() {
        RespostaNumerica rn = new RespostaNumerica(null);
        assertFalse(rn.EsContestat());
        assertEquals("No contestat", rn.getText(null));
    }

    @Test
    public void testRespostaLliure() {
        RespostaLliure rl = new RespostaLliure("Comentari de prova");
        assertTrue(rl.EsContestat());
        assertEquals("Comentari de prova", rl.getText(null));
    }

    @Test
    public void testRespostaLliureBuida() {
        RespostaLliure rl = new RespostaLliure("");
        assertFalse(rl.EsContestat());
        assertEquals("No contestada", rl.getText(null));
    }

    @Test
    public void testRespostaLliureModificacio() {
        RespostaLliure rl = new RespostaLliure("Text inicial");
        assertTrue(rl.EsContestat());

        rl.setText("");
        assertFalse(rl.EsContestat());

        rl.setText("Nou text");
        assertTrue(rl.EsContestat());
        assertEquals("Nou text", rl.getText(null));
    }

    @Test
    public void testRespostaUnica() {
        RespostaUnica ru = new RespostaUnica(3);
        ru.setResposta(1); // Selecciona opció 1
        assertTrue(ru.EsContestat());
        assertEquals(Integer.valueOf(1), Integer.valueOf(ru.getResposta()));
        assertEquals(Integer.valueOf(3), Integer.valueOf(ru.getNumOpcions()));

        List<String> opcions = Arrays.asList("Java", "Python", "C++");
        assertEquals("Python", ru.getText(opcions));
    }

    @Test
    public void testRespostaUnicaInvalida() {
        RespostaUnica ru = new RespostaUnica(3);
        int resultat = ru.setResposta(5); // Opció fora de rang
        assertEquals(0, resultat); // Ha de fallar
        assertFalse(ru.EsContestat());
    }

    @Test
    public void testRespostaMultiple() {
        RespostaMultiple rm = new RespostaMultiple(4);
        List<Integer> seleccions = Arrays.asList(0, 2);
        rm.selecciona(seleccions);
        assertTrue(rm.EsContestat());
        assertEquals(Integer.valueOf(4), Integer.valueOf(rm.getNumOpcions()));

        List<String> opcions = Arrays.asList("Java", "Python", "C++", "JavaScript");
        assertEquals("Java, C++", rm.getText(opcions));
    }

    @Test
    public void testRespostaMultipleInvalida() {
        RespostaMultiple rm = new RespostaMultiple(3);
        List<Integer> seleccions = Arrays.asList(0, 5); // 5 és invàlid
        int resultat = rm.selecciona(seleccions);
        assertEquals(0, resultat); // Ha de fallar
        assertFalse(rm.EsContestat());
    }

    @Test
    public void testRespostaOrdenada() {
        RespostaOrdenada ro = new RespostaOrdenada(3);
        int resultat = ro.setResposta(2); // Selecciona opció 2
        assertEquals(1, resultat); // Èxit
        assertTrue(ro.EsContestat());

        List<String> opcions = Arrays.asList("Baixa", "Mitjana", "Alta");
        assertEquals("Alta", ro.getText(opcions));
    }

    @Test
    public void testRespostaOrdenadaInvalida() {
        RespostaOrdenada ro = new RespostaOrdenada(3);
        int resultat = ro.setResposta(5); // Opció fora de rang
        assertEquals(0, resultat); // Ha de fallar
        assertFalse(ro.EsContestat());
    }

    @Test
    public void testRespostaOrdenadaNoContestada() {
        RespostaOrdenada ro = new RespostaOrdenada(3);
        assertFalse(ro.EsContestat());
        assertEquals("No contestat", ro.getText(Arrays.asList("A", "B", "C")));
    }

    /// ===================Casos extrems==============
    ///
    ///
    ///
    @Test
    public void testRespostaNumericaValorExtrem() {
        // Valors numèrics extrems
        RespostaNumerica rnMax = new RespostaNumerica(Double.MAX_VALUE);
        assertTrue(rnMax.EsContestat());
        assertEquals(String.valueOf(Double.MAX_VALUE), rnMax.getText(null));

        RespostaNumerica rnMin = new RespostaNumerica(Double.MIN_VALUE);
        assertTrue(rnMin.EsContestat());
        assertEquals(String.valueOf(Double.MIN_VALUE), rnMin.getText(null));

        RespostaNumerica rnNegatiu = new RespostaNumerica(-1000.0);
        assertTrue(rnNegatiu.EsContestat());
        assertEquals("-1000.0", rnNegatiu.getText(null));
    }

    @Test
    public void testRespostaNumericaValorZero() {
        // Valor zero
        RespostaNumerica rnZero = new RespostaNumerica(0.0);
        assertTrue(rnZero.EsContestat());
        assertEquals("0.0", rnZero.getText(null));
    }

    @Test
    public void testRespostaNumericaCanviValorExtrem() {
        // Canvi de valor extrem
        RespostaNumerica rn = new RespostaNumerica(5.0);
        rn.setValor(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, rn.getValor(), 0.001);
        assertTrue(rn.EsContestat());
    }

    @Test
    public void testRespostaLliureTextMassaLlarg() {
        // Text molt llarg
        String textMassaLlarg = "A".repeat(10000);
        RespostaLliure rl = new RespostaLliure(textMassaLlarg);
        assertTrue(rl.EsContestat());
        assertEquals(textMassaLlarg, rl.getText(null));
    }

    @Test
    public void testRespostaLliureTextNomésEspais() {
        // Text només amb espais
        RespostaLliure rl = new RespostaLliure("   ");
        assertFalse(rl.EsContestat());
        assertEquals("No contestada", rl.getText(null));
    }

    @Test
    public void testRespostaLliureTextCaractersSpecials() {
        // Text amb caràcters especials
        // tenim pensat fer una excepcio si facilita al Kmeans, si no, no es fa
        String textEspecial = "Resposta amb ñ, ç, àèìòù i símbols: !@#$%^&*()";
        RespostaLliure rl = new RespostaLliure(textEspecial);
        assertTrue(rl.EsContestat());
        assertEquals(textEspecial, rl.getText(null));
    }

    @Test
    public void testRespostaUnicaPrimeraIOpcions() {
        // Seleccionar primera i última opció
        RespostaUnica ru = new RespostaUnica(5);

        ru.setResposta(0); // Primera opció
        assertTrue(ru.EsContestat());
        assertEquals(Integer.valueOf(0), Integer.valueOf(ru.getResposta()));

        ru.setResposta(4); // Última opció
        assertTrue(ru.EsContestat());
        assertEquals(Integer.valueOf(4), Integer.valueOf(ru.getResposta()));
    }

    @Test
    public void testRespostaUnicaZeroOpcions() {
        // Zero opcions (cas límit)
        RespostaUnica ru = new RespostaUnica(0);
        int resultat = ru.setResposta(0);
        //Ha de fallar perque esta fora del limit
        assertEquals(0, resultat);
        assertFalse(ru.EsContestat());
    }

    @Test
    public void testRespostaMultipleTotesOpcions() {
        // Seleccionar totes les opcions
        RespostaMultiple rm = new RespostaMultiple(5);
        List<Integer> totes = Arrays.asList(0, 1, 2, 3, 4);
        int resultat = rm.selecciona(totes);
        assertEquals(1, resultat);
        assertTrue(rm.EsContestat());
    }

    @Test
    public void testRespostaMultipleLlistaBuida() {
        // Llista buida de seleccions
        RespostaMultiple rm = new RespostaMultiple(3);
        List<Integer> buida = new ArrayList<>();
        int resultat = rm.selecciona(buida);
        // En ser buida no s'executa i, per tant, es cert i s'executa amb exit
        assertEquals(1, resultat);
        assertFalse(rm.EsContestat()); // No hi ha seleccions
        assertTrue(rm.getRespostes().isEmpty());
    }

    @Test
    public void testRespostaMultipleOpcionsDuplicades() {
        // Opcions duplicades
        RespostaMultiple rm = new RespostaMultiple(3);
        List<Integer> duplicades = Arrays.asList(0, 0, 1, 1);
        int resultat = rm.selecciona(duplicades);
        assertEquals(1, resultat);
        assertTrue(rm.EsContestat());
    }
}
