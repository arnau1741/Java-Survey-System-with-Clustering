//Test per la classe KMeans, inicialment nomes farem proves a les distancies

import org.junit.Test;
import static org.junit.Assert.*;

public class TestKmeans {
    private KMeans kmeans;

    @Test
    public void testDistanciaNumerica(){
        int k = 10;
        int maxIterations = 100;
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

    public void testDistanciaNoOrdenadaUnica(){
    }

    public void testDistanciaNoOrdenadaMultiple(){

    }


    
    
}
