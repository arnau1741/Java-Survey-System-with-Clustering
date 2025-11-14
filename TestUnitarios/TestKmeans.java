//Test per la classe KMeans, inicialment nomes farem proves a les distancies

import org.junit.Test;
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
    public void testDisanciaOrdenada(){
        int k = 10;
        int maxIterations = 100;
        RespostaOrdenada a = new RespostaOrdenada(10);
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


    public void testDistanciaNoOrdenadaMultiple(){

    }


    
    
}
