import java.util.Arrays;
import java.util.Random;

public class KMeansAdaptation {

    private final int k;
    private final int maxIterations;
    private final double tolerance;
    private final Random random;

    private double[][] centroids; 
    private int[] labels;

    public KMeansAdaptation(int k, int maxIterations, double tolerance, long seed) {
        if (k <= 0) throw new IllegalArgumentException("k should be > 0");
        this.k = k;
        this.maxIterations = maxIterations;
        this.tolerance = tolerance;
        this.random = new Random(seed);
    }

    public KMeansAdaptation(int k, int maxIterations, double tolerance) {
        this(k, maxIterations, tolerance, System.currentTimeMillis());
    }

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

    public int[] getLabels() {
        if (labels == null) {
            throw new IllegalStateException("Call fit() first.");
        }
        return labels;
    }

    public double[][] getCentroids() {
        if (centroids == null) {
            throw new IllegalStateException("Call fit() first.");
        }
        return centroids;
    }

    public int predict(double[] point) {
        if (centroids == null) {
            throw new IllegalStateException("Primero llama a fit().");
        }
        return closestCentroid(point, centroids);
    }


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

    private double squaredDistance(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return sum;
    }
}