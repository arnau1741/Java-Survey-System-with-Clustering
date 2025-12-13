package prop.enquestes.domini;
import java.util.*;

public final class ClusterResult<T> {
    private final List<T> medoids;
    private final Map<T, List<T>> clusters;
    private final double cost;

    public ClusterResult(List<T> medoids, Map<T, List<T>> clusters, double cost) {
        this.medoids = List.copyOf(medoids);
        this.clusters = Collections.unmodifiableMap(new LinkedHashMap<>(clusters));
        this.cost = cost;
    }

    public List<T> getMedoids() { return medoids; }
    public Map<T, List<T>> getClusters() { return clusters; }
    public double getCost() { return cost; }
}