import java.util.Arrays;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;

public class KMeans{

    private final int k;
    private final int maxIterations;
    private final Random random;

    private List<List<Resposta>> centroids; 
    private int[] labels;

    /**
     * Constructor per a KMeans
     * @param k nombre de clústers
     * @param maxIterations nombre màxim d'iteracions
     * @param seed llavor per a la generació aleatòria
     */
    public KMeans(int k, int maxIterations, long seed) {
        if (k <= 0) throw new IllegalArgumentException("k should be > 0");
        this.k = k;
        this.maxIterations = maxIterations;
        this.random = new Random(seed);
    }

    /**
     * Constructor per a KMeans sense llavor aleatòria basada en el temps actual
     * @param k nombre de clústers
     * @param maxIterations nombre màxim d'iteracions
     */
    public KMeans(int k, int maxIterations) {
        this(k, maxIterations, System.currentTimeMillis());
    }

    /**
     * Ajusta el model KMeans a les dades proporcionades
     * @param data les dades d'entrada (Enquesta)
     */
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

    /**
     * Obté les etiquetes assignades a cada punt després de l'ajust
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
    public int predict(List<Resposta> point) {
        if (centroids == null) {
            throw new IllegalStateException("Primero llama a fit().");
        }

        return closestCentroid(point, centroids);
    }*/

    /**
     * Inicialitza els centroides seleccionant k punts aleatoris de les dades
     * @param data les dades d'entrada (Enquesta)
     * @param k nombre de clústers
     * @return matriu de centroides
     * @throws IllegalArgumentException si k és més gran que el nombre de punts
     */
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

    /**
     * Assigna cada punt al clúster més proper
     * @param data les dades d'entrada (Enquesta)
     * @return true si alguna etiqueta ha canviat, false en cas contrari
     */
    private boolean assignClusters(Enquesta data) {
        boolean changed = false;
        int numRespostes = data.getNumRespostes();
        for (int i = 0; i < numRespostes; i++) {
            List<Resposta> point = data.getRespostesUsuariMatriu(i);
            List<Pregunta> preguntes = data.getPreguntesObj();
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
     * @param point punt de dades
     * @param centroids matriu de centroides
     * @param preguntes les preguntes de l'enquesta
     * @return índex del centroid més proper
     */
    private int closestCentroid(List<Resposta> point, List<List<Resposta>> centroids, List<Pregunta> preguntes) {
        int bestIndex = 0;
        double bestDist = distance(point, centroids.get(0), preguntes);

        for (int c = 1; c < centroids.size(); c++) {
            double dist = distance(point, centroids.get(c), preguntes);
            if (dist < bestDist) {
                bestDist = dist;
                bestIndex = c;
            }
        }
        return bestIndex;
    }

    /**
     * Actualitza els centroides basant-se en les assignacions actuals
     * @param data les dades d'entrada (Enquesta)
     * @param dim dimensionalitat de les dades
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

//////////////////////// Funcions distancia locals ///////////////////////////////
    /**
     * Calcula la distància normalitzada entre dues respostes numèriques
     * @param a resposta numèrica a
     * @param b resposta numèrica b
     * @param min valor mínim possible
     * @param max valor màxim possible
     * @return distància normalitzada entre a i b
     */
    public double distanciaNumerica(RespostaNumerica a, RespostaNumerica b, double min, double max) {
        if (a == null && b == null) {
            return 0.0; // Distancia cero si ambas respuestas son nulas
        }
        if (a == null || b == null) {
            return max - min; // Distancia máxima si alguna respuesta es nula
        }
        return Math.abs(a.getValor() - b.getValor()) / (max - min);
    }

    /**
     * Calcula la distància normalitzada entre dues respostes ordenades
     * @param a resposta ordenada a
     * @param b resposta ordenada b
     * @param numOpcions nombre d'opcions possibles
     * @return distància normalitzada entre a i b
     */
    public double distanciaOrdenada (RespostaOrdenada a, RespostaOrdenada b, int numOpcions) {
        if (a == null && b == null) {
            return 0.0; // Distancia cero si ambas respuestas son nulas
        }
        if (a == null || b == null) {
            return 1.0; // Distancia máxima si alguna respuesta es nula
        }
        Integer ordenA = a.getOrdre();
        Integer ordenB = b.getOrdre();
        return Math.abs(ordenA - ordenB)/(numOpcions - 1);
    }

    /**
     * Calcula la distància entre dues respostes no ordenades úniques
     * @param a resposta única a
     * @param b resposta única b
     * @return distància entre a i b
     */
    public double distanciaNoOrdenadaUnica(Resposta a, Resposta b) {
        if (a == null && b == null) {
            return 0.0; // Distancia cero si ambas respuestas son nulas
        }
        if (a == null || b == null) {
            return 1.0; // Distancia máxima si alguna respuesta es nula
        }
        return a.equals(b) ? 0.0 : 1.0;
    }

    /**
     * Calcula la distància entre dues respostes no ordenades múltiples
     * @param a resposta múltiple a
     * @param b resposta múltiple b
     * @return distància entre a i b
     */
    public double distanciaNoOrdenadaMultiple(RespostaMultiple a, RespostaMultiple b) {
        if (a == null && b == null) {
            return 0.0; // Distancia cero si ambas respuestas son nulas
        }
        if (a == null || b == null) {
            return 1.0; // Distancia máxima si alguna respuesta es nula
        }

        // Calcular coeficient de Jaccard
        List<Integer> resA = a.getRespostes();
        List<Integer> resB = b.getRespostes();
        List<Integer> union = new ArrayList<>(resA);
        for (Integer r : resB) {
            if (!union.contains(r)) {
                union.add(r);
            }
        }
        int interseccioCount = 0;
        for (Integer r : resA) {
            if (resB.contains(r)) {
                interseccioCount++;
            }
        }
        return 1.0 - ((double) interseccioCount / union.size());
    }
    /*
    private double distanciaLliure(Resposta a, Resposta b) {
        return 0.0; // Ignorar preguntes de tipus LLIURE en el càlcul de la distància
    }
    */
        

    /**
     * Calcula la distància total entre dues llistes de respostes
     * @param a llista de respostes a
     * @param b llista de respostes b
     * @param preguntes llista de preguntes corresponents
     * @return distància total entre a i b
     */
    public double distance(List<Resposta> a, List<Resposta> b, List<Pregunta> preguntes) {
        double sum = 0.0;
        int numPreguntes = preguntes.size();
        for (int i = 0; i < numPreguntes; i++) {
            Pregunta p = preguntes.get(i);
            int tipus = p.getTipus();
            Resposta ra = a.get(i);
            Resposta rb = b.get(i);
            switch (tipus) {
                case 0: // NUMERICA
                    // Calcular min i max per a la pregunta usant pregunta p.getMinValue() i p.getMaxValue()
                    double min = p.getMinValue();
                    double max = p.getMaxValue();
                    sum += distanciaNumerica((RespostaNumerica) ra, (RespostaNumerica) rb, min, max);
                    break;
                case 1: // UNICA
                    sum += distanciaNoOrdenadaUnica(ra, rb);
                    break;
                case 2: // MULTIPLE
                    sum += distanciaNoOrdenadaMultiple((RespostaMultiple) ra, (RespostaMultiple) rb);
                    break;
                case 3: // ORDENADA
                    int numOpcions = p.getNumOpcions();
                    sum += distanciaOrdenada((RespostaOrdenada) ra, (RespostaOrdenada) rb, numOpcions);
                    break;
                case 4: // LLIURE
                    // Ignorar preguntes de tipus LLIURE en el càlcul de la distancia
                    break;
                default:
                    throw new IllegalArgumentException("Tipus de pregunta desconegut: " + tipus);
            }
        }
                    
        return sum;
    }
}