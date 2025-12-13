package prop.enquestes.domini;
import java.util.*;
import java.util.stream.Collectors;


public final class KMedoidsPAM<T> {

    private final Distance<T> distance;
    private final Random rng;

    public KMedoidsPAM(Distance<T> distance, long seed) {
        this.distance = Objects.requireNonNull(distance);
        this.rng = new Random(seed);
    }

    public ClusterResult<T> fit(List<T> data, int k, int maxIters) {
        validate(data, k, maxIters);

        // 1) Inicialización: k medoids aleatorios distintos
        List<T> medoids = initRandomMedoids(data, k);

        // 2) Loop PAM: asignación + búsqueda del mejor swap global
        double bestCost = Double.POSITIVE_INFINITY;
        Map<T, List<T>> bestClusters = Map.of();

        for (int iter = 0; iter < maxIters; iter++) {
            Assignment<T> asg = assign(data, medoids);
            bestCost = asg.totalCost;
            bestClusters = asg.clusters;

            Swap<T> bestSwap = findBestSwap(data, medoids, bestCost);
            if (!bestSwap.improves) {
                break;
            }

            medoids = applySwap(medoids, bestSwap.medoidOut, bestSwap.medoidIn);
        }


    }


    private void validate(List<T> data, int k, int maxIters) {
        if (data == null || data.isEmpty()) throw new IllegalArgumentException("data vacío");
        if (k <= 0 || k > data.size()) throw new IllegalArgumentException("k inválido");
        if (maxIters <= 0) throw new IllegalArgumentException("maxIters inválido");
    }

    private List<T> initRandomMedoids(List<T> data, int k) {
        List<T> copy = new ArrayList<>(data);
        Collections.shuffle(copy, rng);
        return new ArrayList<>(copy.subList(0, k));
    }

    private record Assignment<T>(Map<T, List<T>> clusters, double totalCost) {}

    private Assignment<T> assign(List<T> data, List<T> medoids) {
        Map<T, List<T>> clusters = new LinkedHashMap<>();
        for (T m : medoids) clusters.put(m, new ArrayList<>());

        double cost = 0.0;

        for (T x : data) {
            T nearest = null;
            double best = Double.POSITIVE_INFINITY;
            for (T m : medoids) {
                double d = distance.d(x, m);
                if (d < best) {
                    best = d;
                    nearest = m;
                }
            }
            clusters.get(nearest).add(x);
            cost += best;
        }

        return new Assignment<>(clusters, cost);
    }

    private record Swap<T>(boolean improves, T medoidOut, T medoidIn, double newCost) {}

    private Swap<T> findBestSwap(List<T> data, List<T> medoids, double currentCost) {
        Set<T> medoidSet = new HashSet<>(medoids);
        List<T> nonMedoids = data.stream().filter(x -> !medoidSet.contains(x)).collect(Collectors.toList());

        boolean improves = false;
        T bestOut = null;
        T bestIn = null;
        double bestCost = currentCost;

        for (T mOut : medoids) {
            for (T mIn : nonMedoids) {
                List<T> candidate = applySwap(medoids, mOut, mIn);
                double candidateCost = assign(data, candidate).totalCost; 

                if (candidateCost < bestCost) {
                    bestCost = candidateCost;
                    bestOut = mOut;
                    bestIn = mIn;
                    improves = true;
                }
            }
        }

        return new Swap<>(improves, bestOut, bestIn, bestCost);
    }

    private List<T> applySwap(List<T> medoids, T out, T in) {
    }
}
