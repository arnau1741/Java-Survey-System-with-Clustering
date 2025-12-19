package prop.enquestes.domini;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import prop.enquestes.excepcions.KmeansExcepcio;

public class KMeansStrategy implements ClusteringStrategy {

    private final int k;
    private final int maxIterations;
    private final KMeans.InitializationMethod initializationMethod;

    /**
     * Constructor per defecte amb mètode d'inicialització.
     * @param k
     * @param maxIterations
     * @param initializationMethod
     */
    public KMeansStrategy(int k, int maxIterations, KMeans.InitializationMethod initializationMethod) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.initializationMethod = initializationMethod;
    }

    /**
     * Constructor per defecte amb opció de KMeans++.
     * @param k
     * @param maxIterations
     * @param usePlusPlus
     */
    public KMeansStrategy(int k, int maxIterations, boolean usePlusPlus) {
        this.k = k;
        this.maxIterations = maxIterations;
        if (usePlusPlus) {
            this.initializationMethod = KMeans.InitializationMethod.KMEANS_PLUS_PLUS;
        } else {
            this.initializationMethod = KMeans.InitializationMethod.RANDOM;
        }
    }

    /**
     * Executa l'algorisme de K-Means sobre l'enquesta.
     * @param data
     * @return
     * @throws KmeansExcepcio
     */
    @Override
	
    public AbstractMap.SimpleEntry<Map<Integer, Integer>, Double> executar(Enquesta data) throws KmeansExcepcio {
        KMeans algorisme;
        algorisme = new KMeans(k, maxIterations, initializationMethod);

        algorisme.fit(data);

        int[] labels = algorisme.getLabels();

        Map<Integer, Integer> resultat = new HashMap<>();
        List<Pregunta> preguntes = data.getPreguntesObj();

        Map<Integer, Resposta> respostesP0 = preguntes.get(0).getRespostes();
        Object[] idArray = respostesP0.keySet().toArray();

        for (int i = 0; i < labels.length; i++) {
            int idUsuari = (int) idArray[i];
            resultat.put(idUsuari, labels[i]);
        }

		double coefSilhouete = algorisme.getCoeficientSilhouete();
   		return new AbstractMap.SimpleEntry<>(resultat, coefSilhouete);
    }
}
