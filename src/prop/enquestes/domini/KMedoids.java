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

    public KMedoids(int k, int maxIterations, long seed) {
        if (k <= 0) throw new IllegalArgumentException("k should be > 0");
        this.k = k;
        this.maxIterations = maxIterations;
        this.random = new Random(seed);
        this.fet = false;
    }

    public KMedoids(int k, int maxIterations) {
        this(k, maxIterations, System.currentTimeMillis());
    }

    public void fit(Enquesta data) throws KmeansExcepcio {
        int n = data.getNumRespostes();
        if (n == 0) throw new KmeansExcepcio("There is no data");
        if (k > n) throw new KmeansExcepcio("k can not be greater than the number of points");

        List<Pregunta> preguntes = data.getPreguntesObj();

        // init medoids aleatoris (índexos únics)
        medoidIdx = initMedoidsRandom(n, k);

        labels = new int[n];
        Arrays.fill(labels, -1);

        for (int iter = 0; iter < maxIterations; iter++) {
            boolean changedAssign = assignClusters(data, preguntes);
            boolean changedMedoids = updateMedoids(data, preguntes);

            if (!changedAssign && !changedMedoids) break;
        }

        fet = true;
        coeficientSilhouete = coeficientSilhouete(data);
    }

    public int[] getLabels() {
        if (labels == null) throw new IllegalStateException("Call fit() first.");
        return labels;
    }

    /**
     * Retorna els medoids com a llista de punts (cada punt = List<Resposta> real).
     */
    public List<List<Resposta>> getMedoids(Enquesta data) {
        if (medoidIdx == null) throw new IllegalStateException("Call fit() first.");
        List<List<Resposta>> res = new ArrayList<>();
        for (int idx : medoidIdx) res.add(data.getRespostesUsuariMatriu(idx));
        return res;
    }

    /**
     * Retorna els índexos dels medoids dins l'enquesta.
     */
    public int[] getMedoidIndices() {
        if (medoidIdx == null) throw new IllegalStateException("Call fit() first.");
        return medoidIdx;
    }

    public double getCoeficientSilhouete() {
        if (!fet) throw new IllegalStateException("Call fit() first.");
        return coeficientSilhouete;
    }


    private int[] initMedoidsRandom(int n, int k) {
        int[] meds = new int[k];
        Set<Integer> used = new HashSet<>();
        int i = 0;
        while (i < k) {
            int idx = random.nextInt(n);
            if (used.add(idx)) meds[i++] = idx;
        }
        return meds;
    }

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
     */
    private boolean updateMedoids(Enquesta data, List<Pregunta> preguntes) {
        boolean changed = false;
        int n = data.getNumRespostes();

        // llistes d'índexos per clúster
        List<List<Integer>> clusters = new ArrayList<>();
        for (int c = 0; c < k; c++) clusters.add(new ArrayList<>());
        for (int i = 0; i < n; i++) clusters.get(labels[i]).add(i);

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
                    if (cand == other) continue;
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

    private int pickNonMedoidIndex(int n) {
        Set<Integer> meds = new HashSet<>();
        for (int x : medoidIdx) meds.add(x);

        int idx;
        do {
            idx = random.nextInt(n);
        } while (meds.contains(idx));
        return idx;
    }

    private double distIdx(Enquesta data, int i, int j, List<Pregunta> preguntes) {
        List<Resposta> a = data.getRespostesUsuariMatriu(i);
        List<Resposta> b = data.getRespostesUsuariMatriu(j);
        return distance(a, b, preguntes);
    }


    public double distanciaNumerica(RespostaNumerica a, RespostaNumerica b, double min, double max) {
        if (a == null && b == null) return 0.0;
        if (a == null || b == null) return 1.0;
        Double valA = a.getValor();
        Double valB = b.getValor();
        if (valA == null && valB == null) return 0.0;
        if (valA == null || valB == null) return max - min;
        return Math.abs(valA - valB) / (max - min);
    }

    public double distanciaOrdenada(RespostaOrdenada a, RespostaOrdenada b, int numOpcions) {
        if (a == null && b == null) return 0.0;
        if (a == null || b == null) return 1.0;
        Double ordenA = 1.0 * a.getOrdre();
        Double ordenB = 1.0 * b.getOrdre();
        if (ordenA == null || ordenB == null) return 1.0;
        Double num1 = 1.0 * numOpcions;
        return Math.abs(ordenA - ordenB) / (num1 - 1.0);
    }

    public double distanciaNoOrdenadaUnica(RespostaUnica a, RespostaUnica b) {
        if (a == null && b == null) return 0.0;
        if (a == null || b == null) return 1.0;
        Integer resA = a.getResposta();
        Integer resB = b.getResposta();
        if (resA == null && resB == null) return 0.0;
        if (resA == null || resB == null) return 1.0;
        return resA.equals(resB) ? 0.0 : 1.0;
    }

    public double distanciaNoOrdenadaMultiple(RespostaMultiple a, RespostaMultiple b) {
        if (a == null && b == null) return 0.0;
        if (a == null || b == null) return 1.0;
        List<Integer> resA = a.getRespostes();
        List<Integer> resB = b.getRespostes();
        if (resA == null && resB == null) return 0.0;
        if (resA == null || resB == null) return 1.0;

        List<Integer> union = new ArrayList<>(resA);
        for (Integer r : resB) if (!union.contains(r)) union.add(r);

        int inter = 0;
        for (Integer r : resA) if (resB.contains(r)) inter++;

        return 1.0 - ((double) inter / union.size());
    }

    private double levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i - 1][j],
                            Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j],
                            Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    public double distanciaLliure(RespostaLliure a, RespostaLliure b) {
        if (a == null && b == null) return 0.0;
        if (a == null || b == null) return 1.0;
        int lenA = a.length();
        int lenB = b.length();
        if (lenA == 0 && lenB == 0) return 0.0;
        if (lenA == 0 || lenB == 0) return 1.0;

        double maxLen = Math.max(lenA, lenB);
        double absLenDif = Math.abs(lenA - lenB);
        return (levenshteinDistance(a.getResposta(), b.getResposta()) - absLenDif) / (maxLen - absLenDif);
    }

    public double distance(List<Resposta> a, List<Resposta> b, List<Pregunta> preguntes) {
        double sum = 0.0;
        int numPreguntes = preguntes.size();
        for (int i = 0; i < numPreguntes; i++) {
            Pregunta p = preguntes.get(i);
            int tipus = p.getTipus();
            Resposta ra = a.get(i);
            Resposta rb = b.get(i);

            switch (tipus) {
                case 0: {
                    double min = p.getMinValue();
                    double max = p.getMaxValue();
                    sum += distanciaNumerica((RespostaNumerica) ra, (RespostaNumerica) rb, min, max);
                    break;
                }
                case 1:
                    sum += distanciaNoOrdenadaUnica((RespostaUnica) ra, (RespostaUnica) rb);
                    break;
                case 2: {
                    int numOpcions = p.getNumOpcions();
                    sum += distanciaOrdenada((RespostaOrdenada) ra, (RespostaOrdenada) rb, numOpcions);
                    break;
                }
                case 3:
                    sum += distanciaNoOrdenadaMultiple((RespostaMultiple) ra, (RespostaMultiple) rb);
                    break;
                case 4:
                    sum += distanciaLliure((RespostaLliure) ra, (RespostaLliure) rb);
                    break;
                default:
                    throw new IllegalArgumentException("Tipus de pregunta desconegut: " + tipus);
            }
        }
        return sum;
    }


    private double coeficientSilhouete(Enquesta data) {
        if (!fet) throw new IllegalStateException("Call fit() first.");

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
                    a += distance(point, data.getRespostesUsuariMatriu(j), preguntes);
                    sameCount++;
                }
            }
            if (sameCount > 0) a /= sameCount;

            // b(i): mínima distància mitjana a un altre clúster
            double b = Double.MAX_VALUE;
            for (int c = 0; c < k; c++) {
                if (c == cluster) continue;
                double sum = 0.0;
                int cnt = 0;
                for (int j = 0; j < n; j++) {
                    if (labels[j] == c) {
                        sum += distance(point, data.getRespostesUsuariMatriu(j), preguntes);
                        cnt++;
                    }
                }
                if (cnt > 0) b = Math.min(b, sum / cnt);
            }

            double s = (b - a) / Math.max(a, b);
            total += s;
        }

        return total / n;
    }
}