package prop.enquestes.domini;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import prop.enquestes.excepcions.KmeansExcepcio;

public class KMedoidsStrategy implements ClusteringStrategy {

    private final int k;
    private final int maxIterations;
    private final Long seed;
    private final boolean usarSeed;

    public KMedoidsStrategy(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.seed = null;
        this.usarSeed = false;
    }

    public KMedoidsStrategy(int k, int maxIterations, long seed) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.seed = seed;
        this.usarSeed = true;
    }

    @Override
    public Map<Integer, Integer> executar(Enquesta data) throws KmeansExcepcio {
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
        return resultat;
    }
}