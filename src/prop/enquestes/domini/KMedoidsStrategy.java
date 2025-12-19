package prop.enquestes.domini;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import prop.enquestes.excepcions.KmeansExcepcio;

public class KMedoidsStrategy implements ClusteringStrategy {

    private final int k;
    private final int maxIterations;
    private final Long seed;
    private final boolean usarSeed;

    /**
     * Constructor per defecte sense semilla.
     * @param k
     * @param maxIterations
     */
    public KMedoidsStrategy(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.seed = null;
        this.usarSeed = false;
    }

    /**
     * Constructor amb semilla.
     * @param k
     * @param maxIterations
     * @param seed
     */
    public KMedoidsStrategy(int k, int maxIterations, long seed) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.seed = seed;
        this.usarSeed = true;
    }

    /**
     * Executa l'algorisme de K-Medoids sobre l'enquesta.
     * @param data L'enquesta amb les dades a processar.
     * @return Un Map on la clau és l'ID de l'Usuari i el valor és l'ID del Clúster assignat.
     * @throws KmeansExcepcio Si hi ha algun error en l'execució matemàtica.
     */
    @Override
    public AbstractMap.SimpleEntry<Map<Integer, Integer>, Double> executar (Enquesta data) throws KmeansExcepcio {
        KMedoids algorisme;
        if (usarSeed) {
            algorisme = new KMedoids(k, maxIterations, seed);
        } else {
            algorisme = new KMedoids(k, maxIterations);
        }

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
