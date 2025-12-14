package prop.enquestes.domini;

import prop.enquestes.domini.Enquesta;
import prop.enquestes.excepcions.KmeansExcepcio;

public class Algoritme {

    //private final StrategyAlgorisme estrategia;

    public Algoritme(String tipus, int k, int maxIter) {

        switch (tipus.toUpperCase()) {
            case "KMEANS":
                //estrategia = new KMeansStrategy(k, maxIter);
                break;

            case "KMEANS++":
                //estrategia = new KMeansPlusPlusStrategy(k, maxIter);
                break;

            default:
                throw new IllegalArgumentException("Algorisme no suportat: " + tipus);
        }
    }

    public void executar(Enquesta enquesta) throws KmeansExcepcio {
        //estrategia.fit(enquesta);
    }

    public int[] getLabels() {
        //return estrategia.getLabels();
        return null;
    }

    public double getSilhouette() {
        //return estrategia.getCoeficientSilhouete();
        return 0;
    }
}
