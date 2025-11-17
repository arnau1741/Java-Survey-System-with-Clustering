package prop.enquestes.TestUnitarios;//Test per la classe KMeans, inicialment nomes farem proves a les distancies

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import prop.enquestes.controladors.KMeans;
import prop.enquestes.domini.*;


import static org.junit.Assert.*;

public class TestKmeans {
    private KMeans kmeans;
    private int k;
    private int maxIterations;

    @Test
    public void testDistanciaNumerica(){
        k = 10;
        maxIterations = 100;
        RespostaNumerica a = new RespostaNumerica(0.0);
        RespostaNumerica b = new RespostaNumerica(10.0);
        double min = 0;
        double max = 10;

        kmeans = new KMeans(k, maxIterations);
        double res = kmeans.distanciaNumerica(a, b, min, max);
        assertEquals(1.0, res, 0.0);
    }

    //modificar constructora RespostaOrdenada y codigo afectado
    @Test
    public void testDisanciaOrdenada(){
        int k = 10;
        int maxIterations = 100;
        int numOpcions = 5;
        RespostaOrdenada a = new RespostaOrdenada(numOpcions);
        RespostaOrdenada b = new RespostaOrdenada(numOpcions);
        a.setResposta(2);
        b.setResposta(4);

        kmeans = new KMeans(k,maxIterations);
        double res = kmeans.distanciaOrdenada(a, b, numOpcions);
        assertEquals(0.5, res, 0.001);
    }

    @Test
    public void testDistanciaOrdenadaSameOrder() {
        int k = 2;
        int maxIterations = 100;
        kmeans = new KMeans(k, maxIterations);

        int numOpcions = 5;
        RespostaOrdenada a = new RespostaOrdenada(numOpcions);
        RespostaOrdenada b = new RespostaOrdenada(numOpcions);
        a.setResposta(2);
        b.setResposta(2);

        double res = kmeans.distanciaOrdenada(a, b, numOpcions);
        assertEquals(0.0, res, 0.0);
    }

    @Test
    public void testDistanciaNoOrdenadaUnicaNullNull(){
        //test con las dos respuestas no incializadas
        int numOpcions = 10;
        RespostaUnica a = new RespostaUnica(numOpcions);
        RespostaUnica b = new RespostaUnica(numOpcions);
        k = 2;
        maxIterations = 100;

        kmeans = new KMeans(k, maxIterations);
        double res = kmeans.distanciaNoOrdenadaUnica(a, b);
        assertEquals(0.0, res, 0.0);
    }
    @Test
    public void testDistanciaNoOrdenadaUnicaNullNonNull(){
        //test amb una resposta no inicialitzada i una inicialitzada
        int numOpcions = 10;
        RespostaUnica a = new RespostaUnica(numOpcions);
        RespostaUnica b = new RespostaUnica(numOpcions);
        b.setResposta(2);
        k = 2;
        maxIterations = 100;

        kmeans = new KMeans(k, maxIterations);
        double res = kmeans.distanciaNoOrdenadaUnica(a, b);
        assertEquals(1.0, res, 0.0);
    }
    @Test
    public void testDistanciaNoOrdenadaUnicaDif(){
        //test amb respostes inicialitzades diferents
        int numOpcions = 10;
        RespostaUnica a = new RespostaUnica(numOpcions);
        RespostaUnica b = new RespostaUnica(numOpcions);
        a.setResposta(10);
        b.setResposta(2);

        k = 2;
        maxIterations = 100;

        kmeans = new KMeans(k, maxIterations);
        double res = kmeans.distanciaNoOrdenadaUnica(a, b);
        assertEquals(1.0, res, 0.0);
    }

    @Test
    public void testDistanciaNoOrdenadaUnicaEq(){
        //test amb respostes inicialitzades diferents
        int numOpcions = 10;
        RespostaUnica a = new RespostaUnica(numOpcions);
        RespostaUnica b = new RespostaUnica(numOpcions);
        a.setResposta(7);
        b.setResposta(7);

        k = 2;
        maxIterations = 100;

        kmeans = new KMeans(k, maxIterations);
        double res = kmeans.distanciaNoOrdenadaUnica(a, b);
        assertEquals(0.0, res, 0.0);
    }

    @Test
    public void testDistanciaNoOrdenadaMultipleNullNull() {
        RespostaMultiple a = null;
        RespostaMultiple b = null;

        k = 2;
        maxIterations = 100;
        kmeans = new KMeans(k, maxIterations);

        double res = kmeans.distanciaNoOrdenadaMultiple(a, b);
        assertEquals(0.0, res, 0.0);
    }

    @Test
    public void testDistanciaNoOrdenadaMultipleNullNonNull() {
        int numOpcions = 10;
        RespostaMultiple a = null;
        RespostaMultiple b = new RespostaMultiple(numOpcions);
        List<Integer> lista = new ArrayList<>();
        lista.add(1);
        lista.add(2);
        b.selecciona(lista);

        int k = 2;
        int maxIterations = 100;
        kmeans = new KMeans(k, maxIterations);

        double res = kmeans.distanciaNoOrdenadaMultiple(a, b);
        assertEquals(1.0, res, 0.0);
    }

    @Test
    public void testDistanciaNoOrdenadaMultipleIdentical() {
        k = 2;
        maxIterations = 100;
        kmeans = new KMeans(k, maxIterations);
        int numOpcions = 10;
        RespostaMultiple a = new RespostaMultiple(numOpcions);
        List<Integer>  lista = new ArrayList<>();
        lista.add(1);
        lista.add(2);
        lista.add(3);
        a.selecciona(lista);

        RespostaMultiple b = new RespostaMultiple(numOpcions);
        List<Integer> lista1 = new ArrayList<>();
        lista1.add(1);
        lista1.add(2);
        lista1.add(3);
        b.selecciona(lista1);

        double res = kmeans.distanciaNoOrdenadaMultiple(a, b);
        assertEquals(0.0, res, 0.0);
    }

    @Test
    public void testDistanciaLliure() {
        RespostaLliure a = new RespostaLliure("hola");
        RespostaLliure b = new RespostaLliure("hola");

        k = 2;
        maxIterations = 100;
        kmeans = new KMeans(k, maxIterations);

        double res = kmeans.distanciaLliure(a, b);
        assertEquals(0.0, res, 0.001);
    }

    @Test
    public void testDistanciaLliureDifferent() {
        RespostaLliure a = new RespostaLliure("hola");
        RespostaLliure b = new RespostaLliure("adeu");

        k = 2;
        maxIterations = 100;
        kmeans = new KMeans(k, maxIterations);

        double res = kmeans.distanciaLliure(a, b);
        // Debería ser mayor que 0
        assertTrue(res > 0.0);
    }

    @Test
    public void testDistanciaLliureEmpty() {
        RespostaLliure a = new RespostaLliure("");
        RespostaLliure b = new RespostaLliure("");

        k = 2;
        maxIterations = 100;
        kmeans = new KMeans(k, maxIterations);

        double res = kmeans.distanciaLliure(a, b);
        assertEquals(0.0, res, 0.0);
    }

    @Test
    public void testDistanciaLliureOneEmpty() {
        RespostaLliure a = new RespostaLliure("");
        RespostaLliure b = new RespostaLliure("text");

        k = 2;
        maxIterations = 100;
        kmeans = new KMeans(k, maxIterations);

        double res = kmeans.distanciaLliure(a, b);
        // Debería ser 1.0 cuando una es vacía y la otra no
        assertEquals(1.0, res, 0.001);
    }

}
