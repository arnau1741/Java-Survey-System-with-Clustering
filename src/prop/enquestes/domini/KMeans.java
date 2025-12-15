package prop.enquestes.domini;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import prop.enquestes.excepcions.KmeansExcepcio;

public class KMeans {

    public enum InitializationMethod {
        RANDOM, KMEANS_PLUS_PLUS
    }

    private final int k;
    private final int maxIterations;
    private final Random random;
    private final InitializationMethod initializationMethod;

    private List<List<Resposta>> centroids;
    private int[] labels;
    private boolean fet;
    private double coeficientSilhouete;

    /**
     * Constructor per a KMeans
     * 
     * @param k             nombre de clústers
     * @param maxIterations nombre màxim d'iteracions
     * @param seed          llavor per a la generació aleatòria
     */
    public KMeans(int k, int maxIterations, long seed, InitializationMethod initializationMethod) {
        if (k <= 0)
            throw new IllegalArgumentException("k should be > 0");
        this.k = k;
        this.maxIterations = maxIterations;
        this.random = new Random(seed);
        this.initializationMethod = initializationMethod;
        this.fet = false;
    }

    public KMeans(int k, int maxIterations, long seed) {
        this(k, maxIterations, seed, InitializationMethod.RANDOM);
    }

    /**
     * Constructor per a KMeans sense llavor aleatòria basada en el temps actual
     * 
     * @param k             nombre de clústers
     * @param maxIterations nombre màxim d'iteracions
     */
    public KMeans(int k, int maxIterations) {
        this(k, maxIterations, System.currentTimeMillis());
    }

    /**
     * Ajusta el model KMeans a les dades proporcionades
     * 
     * @param data les dades d'entrada (Enquesta)
     */
    public void fit(Enquesta data) throws KmeansExcepcio {
        int n = data.getNumRespostes();
        if (n == 0)
            throw new KmeansExcepcio("There is no data");
        int dim = data.getNumPreguntes();
        if (k > n)
            throw new KmeansExcepcio("k can not be greater than the number of points");

        List<Pregunta> preguntes = data.getPreguntesObj();

        if (initializationMethod == InitializationMethod.KMEANS_PLUS_PLUS) {
            centroids = initCentroidsPlusPlus(data, k);
        } else {
            centroids = initCentroidsRandom(data, k);
        }

        labels = new int[n];
        Arrays.fill(labels, -1);

        for (int iter = 0; iter < maxIterations; iter++) {
            boolean changed = assignClusters(data, preguntes);
            updateCentroids(data, dim);

            if (!changed) {
                break;
            }
        }
        fet = true;
        coeficientSilhouete = coeficientSilhouete(data);
    }

    /**
     * Obté les etiquetes assignades a cada punt després de l'ajust
     * 
     * @return array d'etiquetes
     * @throws IllegalStateException si fit() no s'ha cridat encara
     */
    public int[] getLabels() {
        if (labels == null) {
            throw new IllegalStateException("Call fit() first.");
        }
        return labels;
    }

    /**
     * Obté els centroides després de l'ajust
     * 
     * @return llista de centroides
     * @throws IllegalStateException si fit() no s'ha cridat encara
     */
    public List<List<Resposta>> getCentroids() {
        if (centroids == null) {
            throw new IllegalStateException("Call fit() first.");
        }
        return centroids;
    }
    /*
     * public int predict(List<Resposta> point) {
     * if (centroids == null) {
     * throw new IllegalStateException("Primero llama a fit().");
     * }
     * 
     * return closestCentroid(point, centroids);
     * }
     */

    /**
     * Inicialitza els centroides seleccionant k punts aleatoris de les dades
     * 
     * @param data les dades d'entrada (Enquesta)
     * @param k    nombre de clústers
     * @return matriu de centroides
     * @throws IllegalArgumentException si k és més gran que el nombre de punts
     */
    private List<List<Resposta>> initCentroidsRandom(Enquesta data, int k) {
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

    /**
     * Inicialitza els centroides utilitzant l'algorisme KMeans++
     * 
     * @param data les dades d'entrada (Enquesta)
     * @param k    nombre de clústers
     * @return matriu de centroides
     */
    private List<List<Resposta>> initCentroidsPlusPlus(Enquesta data, int k) {
        int n = data.getNumRespostes();
        if (k > n)
            throw new IllegalArgumentException("k can not be greater than the number of points");

        List<List<Resposta>> centroids = new ArrayList<>();
        List<Pregunta> preguntes = data.getPreguntesObj();

        // 1. Escollir un centre aleatoriament entre els punts de dades
        int firstIndex = random.nextInt(n);
        centroids.add(data.getRespostesUsuariMatriu(firstIndex));

        //2. Per a cada punt de dades x, calcular D(x), la distància entre x i el centre 
        // més proper que ja ha estat escollit.

        double[] distSq = new double[n];
        // Inicialitzar distancies al quadrat amb valors alts
        Arrays.fill(distSq, Double.MAX_VALUE);

        for (int i = 1; i < k; i++) {
            double sumDistSq = 0.0;

            //Actualitza les distàncies per a tots els punts en relació amb el centroid més recent
            // (centroids.get(i-1))
            List<Resposta> lastCentroid = centroids.get(i - 1);
            for (int j = 0; j < n; j++) {
                List<Resposta> point = data.getRespostesUsuariMatriu(j);
                double d = Distance.distance(point, lastCentroid, preguntes);
                double d2 = d * d;

                if (d2 < distSq[j]) {
                    distSq[j] = d2;
                }
                sumDistSq += distSq[j];
            }

            // 3. Escollir un nou punt de dades aleatoriament com a nou centre, utilitzant una distribució de probabilitat ponderada
            // on un punt x és escollit amb una probabilitat proporcional a D(x)^2.
            double r = random.nextDouble() * sumDistSq;
            double cumulative = 0.0;
            int nextCentroidIndex = -1;

            for (int j = 0; j < n; j++) {
                cumulative += distSq[j];
                if (cumulative >= r) {
                    nextCentroidIndex = j;
                    break;
                }
            }
            if (nextCentroidIndex == -1) {
                // Fallback (hauria de passar rarament a causa de la precisió)
                nextCentroidIndex = n - 1;
            }

            centroids.add(data.getRespostesUsuariMatriu(nextCentroidIndex));
        }

        return centroids;
    }

    /**
     * Assigna cada punt al clúster més proper
     * 
     * @param data      les dades d'entrada (Enquesta)
     * @param preguntes les preguntes de l'enquesta
     * @return true si alguna etiqueta ha canviat, false en cas contrari
     */
    private boolean assignClusters(Enquesta data, List<Pregunta> preguntes) {
        boolean changed = false;
        int numRespostes = data.getNumRespostes();
        for (int i = 0; i < numRespostes; i++) {
            List<Resposta> point = data.getRespostesUsuariMatriu(i);
            // List<Pregunta> preguntes = data.getPreguntesObj();
            int newLabel = closestCentroid(point, centroids, preguntes);
            if (newLabel != labels[i]) {
                labels[i] = newLabel;
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Troba l'índex del centroid més proper a un punt donat
     * 
     * @param point     punt de dades
     * @param centroids matriu de centroides
     * @param preguntes les preguntes de l'enquesta
     * @return índex del centroid més proper
     */
    private int closestCentroid(List<Resposta> point, List<List<Resposta>> centroids, List<Pregunta> preguntes) {
        int bestIndex = 0;
        double bestDist = Distance.distance(point, centroids.get(0), preguntes);

        for (int c = 1; c < centroids.size(); c++) {
            double dist = Distance.distance(point, centroids.get(c), preguntes);
            if (dist < bestDist) {
                bestDist = dist;
                bestIndex = c;
            }
        }
        return bestIndex;
    }

    /**
     * Actualitza els centroides basant-se en les assignacions actuals
     * 
     * @param data les dades d'entrada (Enquesta)
     * @param dim  dimensionalitat de les dades
     */
    private void updateCentroids(Enquesta data, int dim) {
        // inicia centroides vacíos
        List<List<Resposta>> newCentroids = new ArrayList<>();
        for (int c = 0; c < k; c++) {
            List<Resposta> row = new ArrayList<>(Collections.nCopies(dim, null));
            newCentroids.add(row);
        }
        int[] counts = new int[k];
        int numRespostes = data.getNumRespostes();

        // Parte numerica
        for (int i = 0; i < numRespostes; i++) {
            int cluster = labels[i];
            counts[cluster]++;
            List<Resposta> point = data.getRespostesUsuariMatriu(i);
            for (int d = 0; d < dim; d++) {
                if (data.getPreguntesObj().get(d).getTipus() == 0) {
                    // numerica
                    RespostaNumerica rPoint = (RespostaNumerica) point.get(d);
                    if (rPoint.getValor() == null) {
                        continue; // Saltar si la respuesta es nula
                    }
                    RespostaNumerica rCentroid = (RespostaNumerica) newCentroids.get(cluster).get(d);
                    if (rCentroid == null) {
                        rCentroid = new RespostaNumerica(rPoint.getValor());
                        newCentroids.get(cluster).set(d, rCentroid);
                    } else {
                        rCentroid.setValor(
                                rCentroid.getValor() + rPoint.getValor());
                    }
                }
            }
        }

        int numPreguntes = data.getNumPreguntes();
        for (int j = 0; j < numPreguntes; j++) {
            int tipus = data.getPreguntesObj().get(j).getTipus();
            if (tipus == 1 || tipus == 2 || tipus == 3) {
                // usar moda
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
        // calcula mediana de la parte numerica
        for (int c = 0; c < k; c++) {
            for (int d = 0; d < dim; d++) {
                if (data.getPreguntesObj().get(d).getTipus() == 0) {
                    // numerica

                    RespostaNumerica rCentroid = (RespostaNumerica) newCentroids.get(c).get(d);
                    if (rCentroid != null) {
                        rCentroid.setValor(rCentroid.getValor() / counts[c]);
                    } else {
                        // no hi ha respostes per aquest clúster
                        int idx = random.nextInt(numRespostes);
                        List<Resposta> randomPoint = data.getRespostesUsuariMatriu(idx);
                        newCentroids.get(c).set(d, randomPoint.get(d));
                    }
                }
            }
        }

        centroids = newCentroids;
    }

    /**
     * Calcula el coeficient de Silhouete per a l'enquesta donada
     * 
     * @param data l'enquesta amb les respostes
     * @return coeficient de Silhouete
     */
    private double coeficientSilhouete(Enquesta data) {
        if (!fet) {
            throw new IllegalStateException("Call fit() first.");
        }
        int n = data.getNumRespostes();
        double totalSilhouete = 0.0;
        List<Pregunta> preguntes = data.getPreguntesObj();
        for (int i = 0; i < n; i++) {
            List<Resposta> point = data.getRespostesUsuariMatriu(i);
            int cluster = labels[i];

            // Calcular a(i)
            double a = 0.0;
            int sameClusterCount = 0;
            for (int j = 0; j < n; j++) {
                if (i != j && labels[j] == cluster) {
                    List<Resposta> otherPoint = data.getRespostesUsuariMatriu(j);
                    a += Distance.distance(point, otherPoint, preguntes);
                    sameClusterCount++;
                }
            }
            if (sameClusterCount > 0) {
                a /= sameClusterCount;
            }

            // Calcular b(i)
            double b = Double.MAX_VALUE;
            for (int c = 0; c < k; c++) {
                if (c != cluster) {
                    double distSum = 0.0;
                    int otherClusterCount = 0;
                    for (int j = 0; j < n; j++) {
                        if (labels[j] == c) {
                            List<Resposta> otherPoint = data.getRespostesUsuariMatriu(j);
                            distSum += Distance.distance(point, otherPoint, preguntes);
                            otherClusterCount++;
                        }
                    }
                    if (otherClusterCount > 0) {
                        double avgDist = distSum / otherClusterCount;
                        if (avgDist < b) {
                            b = avgDist;
                        }
                    }
                }
            }

            // Calcular s(i)
            double maxAB = Math.max(a, b);
            double s = 0.0;

            // Usamos un epsilon (ej. 1e-10) para evitar dividir por casi cero
            if (maxAB > 1e-10) {
                s = (b - a) / maxAB;
            }

            // Si maxAB es 0, s se queda en 0.0 (correcto para puntos idénticos/solapados)

            totalSilhouete += s;
        }
        return totalSilhouete / n;
    }

    /**
     * Getter del coeficient de Silhouete
     * 
     * @return coeficient de Silhouete
     * @throws IllegalStateException si fit() no s'ha cridat encara
     */
    public double getCoeficientSilhouete() {
        if (!fet) {
            throw new IllegalStateException("Call fit() first.");
        }
        return coeficientSilhouete;
    }
}