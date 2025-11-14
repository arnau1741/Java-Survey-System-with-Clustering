import java.util.Arrays;
import java.util.Random;

public class KMeansAdaptation {

    private final int k;
    private final int maxIterations;
    private final double tolerance;
    private final Random random;

    private double[][] centroids; 
    private int[] labels;

    /**
     * Constructor per KMeansAdaptation
     * @param k nombre de clústers
     * @param maxIterations nombre màxim d'iteracions
     * @param tolerance tolerància per a la convergència
     * @param seed llavor per al generador de nombres aleatoris
     */
    public KMeansAdaptation(int k, int maxIterations, double tolerance, long seed) {
        if (k <= 0) throw new IllegalArgumentException("k should be > 0");
        this.k = k;
        this.maxIterations = maxIterations;
        this.tolerance = tolerance;
        this.random = new Random(seed);
    }

    /**
     * Constructor per KMeansAdaptation sense llavor aleatòria
     * @param k nombre de clústers
     * @param maxIterations nombre màxim d'iteracions
     * @param tolerance tolerància per a la convergència
     */
    public KMeansAdaptation(int k, int maxIterations, double tolerance) {
        this(k, maxIterations, tolerance, System.currentTimeMillis());
    }

    /**
     * Ajusta el model K-Means a les dades proporcionades
     * @param data dades d'entrada
     */
    public void fit(double[][] data) {
        int n = data.length;
        if (n == 0) throw new IllegalArgumentException("There is no data");
        int dim = data[0].length;

        centroids = initCentroidsRandom(data, k);

        labels = new int[n];
        Arrays.fill(labels, -1);

        for (int iter = 0; iter < maxIterations; iter++) {
            boolean changed = assignClusters(data);
            double shift = updateCentroids(data, dim);

            if (!changed || shift < tolerance) {
                break;
            }
        }
    }

    /**
     * Retorna les etiquetes assignades després de l'ajust
     * @return etiquetes dels clústers
     */
    public int[] getLabels() {
        if (labels == null) {
            throw new IllegalStateException("Call fit() first.");
        }
        return labels;
    }

    /**
     * Retorna els centroides dels clústers després de l'ajust
     * @return centroides dels clústers
     */
    public double[][] getCentroids() {
        if (centroids == null) {
            throw new IllegalStateException("Call fit() first.");
        }
        return centroids;
    }

    /**
     * Prediu l'etiqueta del clúster per a un nou punt de dades
     * @param point punt de dades
     * @return etiqueta del clúster assignada
     */
    public int predict(double[] point) {
        if (centroids == null) {
            throw new IllegalStateException("Primero llama a fit().");
        }
        return closestCentroid(point, centroids);
    }

    /**
     * Inicialitza els centroides seleccionant k punts aleatoris de les dades
     * @param data dades d'entrada
     * @param k nombre de clústers
     * @return centroides inicials
     */
    private double[][] initCentroidsRandom(double[][] data, int k) {
        int n = data.length;
        if (k > n) {
            throw new IllegalArgumentException("k can not be greater than the number of points");
        }

        double[][] init = new double[k][];
        boolean[] used = new boolean[n];

        for (int i = 0; i < k; i++) {
            int idx;
            do {
                idx = random.nextInt(n);
            } while (used[idx]);
            used[idx] = true;
            init[i] = Arrays.copyOf(data[idx], data[idx].length);
        }
        return init;
    }

    /**
     * Assigna cada punt de dades al clúster més proper
     * @param data dades d'entrada
     * @return true si alguna etiqueta ha canviat, false en cas contrari
     */
    private boolean assignClusters(double[][] data) {
        boolean changed = false;
        for (int i = 0; i < data.length; i++) {
            int newLabel = closestCentroid(data[i], centroids);
            if (newLabel != labels[i]) {
                labels[i] = newLabel;
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Troba l'índex del centroid més proper a un punt donat
     * @param point punt de dades
     * @param centroids centroides dels clústers
     * @return índex del centroid més proper
     */
    private int closestCentroid(double[] point, double[][] centroids) {
        int bestIndex = 0;
        double bestDist = squaredDistance(point, centroids[0]);

        for (int c = 1; c < centroids.length; c++) {
            double dist = squaredDistance(point, centroids[c]);
            if (dist < bestDist) {
                bestDist = dist;
                bestIndex = c;
            }
        }
        return bestIndex;
    }

    /**
     * Actualitza els centroides basant-se en les assignacions actuals
     * @param data dades d'entrada
     * @param dim dimensionalitat de les dades
     * @return desplaçament total dels centroides
     */
    private double updateCentroids(double[][] data, int dim) {
        double[][] newCentroids = new double[k][dim];
        int[] counts = new int[k];

        for (int i = 0; i < data.length; i++) {
            int cluster = labels[i];
            counts[cluster]++;
            double[] point = data[i];
            for (int d = 0; d < dim; d++) {
                newCentroids[cluster][d] += point[d];
            }
        }

        for (int c = 0; c < k; c++) {
            if (counts[c] == 0) {
                int idx = random.nextInt(data.length);
                newCentroids[c] = Arrays.copyOf(data[idx], dim);
            } else {
                for (int d = 0; d < dim; d++) {
                    newCentroids[c][d] /= counts[c];
                }
            }
        }

        double shift = 0.0;
        for (int c = 0; c < k; c++) {
            shift += squaredDistance(centroids[c], newCentroids[c]);
        }

        centroids = newCentroids;
        return shift;
    }

    /**
     * Calcula la distància quadrada entre dos punts
     * @param a 
     * @param b
     * @return distància quadrada
     */
    private double squaredDistance(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return sum;
    }
}