import java.util.Arrays;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class KMeans{

    private final int k;
    private final int maxIterations;
    private final Random random;

    private List<List<Resposta>> centroids; 
    private int[] labels;

    public KMeans(int k, int maxIterations, long seed) {
        if (k <= 0) throw new IllegalArgumentException("k should be > 0");
        this.k = k;
        this.maxIterations = maxIterations;
        this.random = new Random(seed);
    }

    public KMeans(int k, int maxIterations) {
        this(k, maxIterations, System.currentTimeMillis());
    }

    public void fit(Enquesta data) {
        int n = data.getNumRespostes();
        if (n == 0) throw new IllegalArgumentException("There is no data");
        int dim = data.getNumPreguntes();

        centroids = initCentroidsRandom(data, k);

        labels = new int[n];
        Arrays.fill(labels, -1);

        for (int iter = 0; iter < maxIterations; iter++) {
            boolean changed = assignClusters(data);
             updateCentroids(data, dim);

            if (!changed) {
                break;
            }
        }
    }

    public int[] getLabels() {
        if (labels == null) {
            throw new IllegalStateException("Call fit() first.");
        }
        return labels;
    }

    public List<List<Resposta>> getCentroids() {
        if (centroids == null) {
            throw new IllegalStateException("Call fit() first.");
        }
        return centroids;
    }

    public int predict(List<Resposta> point) {
        if (centroids == null) {
            throw new IllegalStateException("Primero llama a fit().");
        }
        return closestCentroid(point, centroids);
    }


    private  List<List<Resposta>> initCentroidsRandom(Enquesta data, int k) {
        int n = data.getNumRespostes();
        if (k > n) {
            throw new IllegalArgumentException("k can not be greater than the number of points");
        }
        List<List<Resposta>> centroids = new ArrayList<>();
        List<Integer> chosenIndices = new ArrayList<>();
        while (centroids.size() < k) {
            int index = random.nextInt(n);
            if (!chosenIndices.contains(index)) {
                chosenIndices.add(index);
                centroids.add(data.getRespostesUsuariMatriu(index));
            }
        }
        return centroids;
    }

    private boolean assignClusters(Enquesta data) {
        boolean changed = false;
        int numRespostes = data.getNumRespostes();
        for (int i = 0; i < numRespostes; i++) {
            List<Resposta> point = data.getRespostesUsuariMatriu(i);
            int newLabel = closestCentroid(point, centroids);
            if (newLabel != labels[i]) {
                labels[i] = newLabel;
                changed = true;
            }
        }
        return changed;
    }

    private int closestCentroid(List<Resposta> point, List<List<Resposta>> centroids) {
        int bestIndex = 0;
        double bestDist = distance(point, centroids.get(0));

        for (int c = 1; c < centroids.size(); c++) {
            double dist = distance(point, centroids.get(c));
            if (dist < bestDist) {
                bestDist = dist;
                bestIndex = c;
            }
        }
        return bestIndex;
    }

    private void updateCentroids(Enquesta data, int dim) {
        // inicia centroides vacíos
        List<List<Resposta>> newCentroids = new ArrayList<>();
        int[] counts = new int[k];
        int numRespostes = data.getNumRespostes();

        //Parte numerica
        for (int i = 0; i < numRespostes; i++) {
            int cluster = labels[i];
            counts[cluster]++;
            List<Resposta> point = data.getRespostesUsuariMatriu(i);
            for (int d = 0; d < dim; d++) {
                if (data.getPreguntesObj().get(d).getTipus() == 0){
                    //numerica
                    RespostaNumerica rPoint = (RespostaNumerica) point.get(d);
                    RespostaNumerica rCentroid = (RespostaNumerica) newCentroids.get(cluster).get(d);
                    if (rCentroid == null) {
                        rCentroid = new RespostaNumerica(rPoint.getValor());
                        newCentroids.get(cluster).set(d, rCentroid);
                    }
                    else{
                        rCentroid.setValor(
                            rCentroid.getValor() + rPoint.getValor()
                        );
                    }  
                }
            }
        }

        int numPreguntes = data.getNumPreguntes();
        for (int j = 0; j<numPreguntes; j++){
            int tipus = data.getPreguntesObj().get(j).getTipus();
            if (tipus == 1 || tipus == 2 || tipus == 3){
                //usar moda
                for (int c = 0; c < k; c++) {
                    Map<Resposta, Integer> freqMap = new java.util.HashMap<>();
                    for (int i = 0; i < numRespostes; i++) {
                        if (labels[i] == c) {
                            List<Resposta> point = data.getRespostesUsuariMatriu(i);
                            Resposta rPoint = point.get(j);
                            freqMap.put(rPoint, freqMap.getOrDefault(rPoint, 0) + 1);
                        }
                    }
                    Resposta moda = null;
                    int maxFreq = -1;
                    for (Map.Entry<Resposta, Integer> entry : freqMap.entrySet()) {
                        if (entry.getValue() > maxFreq) {
                            maxFreq = entry.getValue();
                            moda = entry.getKey();
                        }
                    }
                    newCentroids.get(c).set(j, moda);
                }
            }
        }
        //calcula mediana de la parte numerica
        for (int c = 0; c < k; c++) {
            for (int d = 0; d < dim; d++) {
                if (data.getPreguntesObj().get(d).getTipus() == 0){
                    //numerica
                    RespostaNumerica rCentroid = (RespostaNumerica) newCentroids.get(c).get(d);
                    if (rCentroid != null) {
                        rCentroid.setValor(rCentroid.getValor() / counts[c]);
                    }
                }
            }
        }

        centroids = newCentroids;
    }

    private double distance(List<Resposta> a, List<Resposta> b) {
        double sum = 0.0;
        return sum;
    }
}