package prop.enquesta.test;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

import prop.enquesta.resposta.*;
import prop.enquesta.excepcions.RespostaInvalida;

/**
 * Test conjunt per totes les subclasses de Resposta.
 * Inclou: Numerica, Lliure, Unica, Multiple, Ordenada.
 */
public class TestRespostes {

    @Test
    public void testRespostaNumerica() throws RespostaInvalida {
        RespostaNumerica rn = new RespostaNumerica(7.5);
        rn.validar();
        assertTrue(rn.EsContestat());
        assertEquals("7.5", rn.getText(null));
        assertEquals(7.5, rn.getValor(), 0.001);
    }

    @Test(expected = RespostaInvalida.class)
    public void testRespostaNumericaInvalida() throws RespostaInvalida {
        RespostaNumerica rn = new RespostaNumerica(15.0); // Fora de rang 0-10
        rn.validar();
    }

    @Test
    public void testRespostaNumericaNoContestada() {
        RespostaNumerica rn = new RespostaNumerica(null);
        assertFalse(rn.EsContestat());
        assertEquals("No contestat", rn.getText(null));
    }

    @Test
    public void testRespostaLliure() throws RespostaInvalida {
        RespostaLliure rl = new RespostaLliure("Comentari de prova");
        rl.validar();
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
    public void testRespostaUnica() throws RespostaInvalida {
        RespostaUnica ru = new RespostaUnica(3);
        ru.setResposta(1); // Selecciona opció 1
        ru.validar();
        assertTrue(ru.EsContestat());
        assertEquals(1, ru.getResposta());
        assertEquals(3, ru.getNumOpcions());

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
        assertEquals(4, rm.getNumOpcions());

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
}