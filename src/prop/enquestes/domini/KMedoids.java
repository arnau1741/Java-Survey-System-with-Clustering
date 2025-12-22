package prop.enquestes.domini;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import prop.enquestes.excepcions.KmeansExcepcio;

public class KMedoids {

    private final int k;
    private final int maxIterations;
    private final Random random;

    private int[] medoidIdx;
    private int[] labels;

    private boolean fet;
    private double coeficientSilhouete;

    /**
     * Constructor per l'algorisme K-Medoids.
     * @param k
     * @param maxIterations
     * @param seed
     */
    public KMedoids(int k, int maxIterations, long seed) {
        if (k <= 0)
            throw new IllegalArgumentException("k should be > 0");
        this.k = k;
        this.maxIterations = maxIterations;
        this.random = new Random(seed);
        this.fet = false;
    }

    /**
     * Constructor per l'algorisme K-Medoids amb seed aleatòria.
     * @param k
     * @param maxIterations
     */
    public KMedoids(int k, int maxIterations) {
        this(k, maxIterations, System.currentTimeMillis());
    }

    /**
     * Executa l'algorisme K-Medoids sobre les dades de l'enquesta.
     * @param data
     * @throws KmeansExcepcio
     */
    public void fit(Enquesta data) throws KmeansExcepcio {
        int n = data.getNumRespostes();
        if (n == 0)
            throw new KmeansExcepcio("There is no data");
        if (k > n)
            throw new KmeansExcepcio("k can not be greater than the number of points");

        List<Pregunta> preguntes = data.getPreguntesObj();

        // init medoids aleatoris (índexos únics)
        medoidIdx = initMedoidsRandom(n, k);

        labels = new int[n];
        Arrays.fill(labels, -1);

        for (int iter = 0; iter < maxIterations; iter++) {
            boolean changedAssign = assignClusters(data, preguntes);
            boolean changedMedoids = updateMedoids(data, preguntes);

            if (!changedAssign && !changedMedoids)
                break;
        }

        fet = true;
        coeficientSilhouete = coeficientSilhouete(data);
    }

    /**
     * Retorna les etiquetes de clúster assignades a cada punt.
     * @return Array d'etiquetes de clúster
     */
    public int[] getLabels() {
        if (labels == null)
            throw new IllegalStateException("Call fit() first.");
        return labels;
    }

    /**
     * Retorna el coeficient de Silhouete del clustering realitzat.
     * @return Coeficient de Silhouete
     */
    public double getCoeficientSilhouete() {
        if (!fet)
            throw new IllegalStateException("Call fit() first.");
        return coeficientSilhouete;
    }

    /*
     * private int[] initMedoidsRandom(int n, int k) {
     * int[] meds = new int[k];
     * Set<Integer> used = new HashSet<>();
     * int i = 0;
     * while (i < k) {
     * int idx = random.nextInt(n);
     * if (used.add(idx)) meds[i++] = idx;
     * }
     * return meds;
     * }
     */

    /**
     * Inicialitza k medoids únics triats aleatòriament entre n punts
     * utilitzant una versió parcial de l'algorisme de Fisher-Yates.
     * @param n Nombre total de punts
     * @param k Nombre de medoids a seleccionar
     * @return Array d'índexos dels medoids seleccionats
     */
    private int[] initMedoidsRandom(int n, int k) {
        // 1. Crear un array con todos los índices posibles [0, 1, ..., n-1]
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = i;
        }

        // 2. Barajar solo los primeros k elementos (Fisher-Yates parcial)
        for (int i = 0; i < k; i++) {
            // Elegimos una posición aleatoria entre el índice actual 'i' y el final 'n'
            int index = i + random.nextInt(n - i);

            // Intercambiamos (Swap) el elemento en 'i' con el elemento en 'index'
            int temp = p[index];
            p[index] = p[i];
            p[i] = temp;
        }

        // 3. Los primeros k elementos del array 'p' son nuestra selección aleatoria
        // única
        return Arrays.copyOf(p, k);
    }

    /**
     * Assigna cada punt al clúster del medoid més proper.
     * @return true si alguna etiqueta ha canviat, false en cas contrari
     */
    private boolean assignClusters(Enquesta data, List<Pregunta> preguntes) {
        boolean changed = false;
        int n = data.getNumRespostes();

        for (int i = 0; i < n; i++) {
            int best = 0;
            double bestDist = distIdx(data, i, medoidIdx[0], preguntes);

            for (int m = 1; m < k; m++) {
                double d = distIdx(data, i, medoidIdx[m], preguntes);
                if (d < bestDist) {
                    bestDist = d;
                    best = m;
                }
            }

            if (labels[i] != best) {
                labels[i] = best;
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Per cada clúster, tria com a nou medoid el punt del clúster
     * que minimitza sum(dist(p, q)) per tots q del clúster.
     * @return true si algun medoid ha canviat, false en cas contrari
     */
    private boolean updateMedoids(Enquesta data, List<Pregunta> preguntes) {
        boolean changed = false;
        int n = data.getNumRespostes();

        // llistes d'índexos per clúster
        List<List<Integer>> clusters = new ArrayList<>();
        for (int c = 0; c < k; c++)
            clusters.add(new ArrayList<>());
        for (int i = 0; i < n; i++)
            clusters.get(labels[i]).add(i);

        for (int c = 0; c < k; c++) {
            List<Integer> pts = clusters.get(c);

            // clúster buit: re-seed amb un punt aleatori que NO sigui medoid
            if (pts.isEmpty()) {
                int newIdx = pickNonMedoidIndex(n);
                if (medoidIdx[c] != newIdx) {
                    medoidIdx[c] = newIdx;
                    changed = true;
                }
                continue;
            }

            int bestIdx = medoidIdx[c];
            double bestCost = Double.POSITIVE_INFINITY;

            // prova cada candidat del clúster com a medoid
            for (int cand : pts) {
                double cost = 0.0;
                for (int other : pts) {
                    if (cand == other)
                        continue;
                    cost += distIdx(data, cand, other, preguntes);
                }
                if (cost < bestCost) {
                    bestCost = cost;
                    bestIdx = cand;
                }
            }

            if (bestIdx != medoidIdx[c]) {
                medoidIdx[c] = bestIdx;
                changed = true;
            }
        }

        return changed;
    }

    /**
     * Tria un índex aleatori que no sigui medoid.
     * @param n
     * @return Índex triat
     */
    private int pickNonMedoidIndex(int n) {
        Set<Integer> meds = new HashSet<>();
        for (int x : medoidIdx)
            meds.add(x);

        List<Integer> candidates = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!meds.contains(i))
                candidates.add(i);
        }
        if (candidates.isEmpty())
            throw new IllegalStateException("No non-medoid candidates available. This should not happen.");

        int randomIndex = random.nextInt(candidates.size());
        return candidates.get(randomIndex);
    }

    /**
     * Calcula la distància entre dos punts donats els seus índexos.
     * @param data
     * @param i
     * @param j
     * @param preguntes
     * @return Distància entre els punts i i j
     */
    private double distIdx(Enquesta data, int i, int j, List<Pregunta> preguntes) {
        List<Resposta> a = data.getRespostesUsuariMatriu(i);
        List<Resposta> b = data.getRespostesUsuariMatriu(j);
        return Distance.distance(a, b, preguntes);
    }

    /**
     * Calcula el coeficient de Silhouete per a l'enquesta donada.
     * @param data
     * @return Coeficient de Silhouete
     */
    private double coeficientSilhouete(Enquesta data) {
        if (!fet)
            throw new IllegalStateException("Call fit() first.");

        int n = data.getNumRespostes();
        List<Pregunta> preguntes = data.getPreguntesObj();
        double total = 0.0;

        for (int i = 0; i < n; i++) {
            List<Resposta> point = data.getRespostesUsuariMatriu(i);
            int cluster = labels[i];

            // a(i): distància mitjana dins el mateix clúster
            double a = 0.0;
            int sameCount = 0;
            for (int j = 0; j < n; j++) {
                if (i != j && labels[j] == cluster) {
                    a += Distance.distance(point, data.getRespostesUsuariMatriu(j), preguntes);
                    sameCount++;
                }
            }
            if (sameCount > 0)
                a /= sameCount;

            // b(i): mínima distància mitjana a un altre clúster
            double b = Double.MAX_VALUE;
            for (int c = 0; c < k; c++) {
                if (c == cluster)
                    continue;
                double sum = 0.0;
                int cnt = 0;
                for (int j = 0; j < n; j++) {
                    if (labels[j] == c) {
                        sum += Distance.distance(point, data.getRespostesUsuariMatriu(j), preguntes);
                        cnt++;
                    }
                }
                if (cnt > 0)
                    b = Math.min(b, sum / cnt);
            }

            double s = (b - a) / Math.max(a, b);
            total += s;
        }

        return total / n;
    }
}