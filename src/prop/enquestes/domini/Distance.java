package prop.enquestes.domini;

import java.util.ArrayList;
import java.util.List;

public class Distance {

    /**
     * Calcula la distància normalitzada entre dues respostes numèriques
     */
    public static double distanciaNumerica(RespostaNumerica a, RespostaNumerica b, double min, double max) {
        if (a == null && b == null)
            return 0.0;
        if (a == null || b == null)
            return 1.0;
        Double valA = a.getValor();
        Double valB = b.getValor();
        if (valA == null && valB == null)
            return 0.0;
        if (valA == null || valB == null)
            return max - min;

        return Math.abs(valA - valB) / (max - min);
    }

    /**
     * Calcula la distància normalitzada entre dues respostes ordenades
     */
    public static double distanciaOrdenada(RespostaOrdenada a, RespostaOrdenada b, int numOpcions) {
        if (a == null && b == null)
            return 0.0;
        if (a == null || b == null)
            return 1.0;
        Double ordenA = 1.0 * a.getOrdre();
        Double ordenB = 1.0 * b.getOrdre();
        if (ordenA == null || ordenB == null)
            return 1.0;
        Double num1 = 1.0 * numOpcions;
        return Math.abs(ordenA - ordenB) / (num1 - 1.0);
    }

    /**
     * Calcula la distància entre dues respostes no ordenades úniques
     */
    public static double distanciaNoOrdenadaUnica(RespostaUnica a, RespostaUnica b) {
        if (a == null && b == null)
            return 0.0;
        if (a == null || b == null)
            return 1.0;
        Integer resA = a.getResposta();
        Integer resB = b.getResposta();
        if (resA == null && resB == null)
            return 0.0;
        if (resA == null || resB == null)
            return 1.0;
        return resA.equals(resB) ? 0.0 : 1.0; // Usar equals para comparar objetos Integer
    }

    /**
     * Calcula la distància entre dues respostes no ordenades múltiples
     */
    public static double distanciaNoOrdenadaMultiple(RespostaMultiple a, RespostaMultiple b) {
        if (a == null && b == null)
            return 0.0;
        if (a == null || b == null)
            return 1.0;
        List<Integer> resA = a.getRespostes();
        List<Integer> resB = b.getRespostes();

        if (resA == null && resB == null)
            return 0.0;
        if (resA == null || resB == null)
            return 1.0;

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
        if (union.isEmpty())
            return 0.0; // Avoid division by zero
        return 1.0 - ((double) interseccioCount / union.size());
    }

    /**
     * Calcula la distància de levenshtein entre dos Strings
     */
    private static double levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j; // Deletion
                } else if (j == 0) {
                    dp[i][j] = i; // Insertion
                } else if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i - 1][j], // Deletion
                            Math.min(dp[i][j - 1], // Insertion
                                    dp[i - 1][j - 1])); // Substitution
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], // Deletion
                            Math.min(dp[i][j - 1], // Insertion
                                    dp[i - 1][j - 1])); // Substitution
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    /**
     * Calcula la distància entre dues respostes lliures
     */
    public static double distanciaLliure(RespostaLliure a, RespostaLliure b) {
        if (a == null && b == null)
            return 0.0;
        if (a == null || b == null)
            return 1.0;
        int lenA = a.length();
        int lenB = b.length();
        if (lenA == 0 && lenB == 0)
            return 0.0;
        if (lenA == 0 || lenB == 0)
            return 1.0;

        double maxLen = Math.max(lenA, lenB);
        double absLenDif = Math.abs(lenA - lenB);

        return (levenshteinDistance(a.getResposta(), b.getResposta()) - absLenDif) / (maxLen - absLenDif);
    }

    /**
     * Calcula la distància total entre dues llistes de respostes
     */
    public static double distance(List<Resposta> a, List<Resposta> b, List<Pregunta> preguntes) {
        double sum = 0.0;
        int numPreguntes = preguntes.size();
        for (int i = 0; i < numPreguntes; i++) {
            Pregunta p = preguntes.get(i);
            int tipus = p.getTipus();
            Resposta ra = a.get(i);
            Resposta rb = b.get(i);
            switch (tipus) {
                case 0: // NUMERICA
                    double min = p.getMinValue();
                    double max = p.getMaxValue();
                    sum += distanciaNumerica((RespostaNumerica) ra, (RespostaNumerica) rb, min, max);
                    break;
                case 1: // UNICA
                    sum += distanciaNoOrdenadaUnica((RespostaUnica) ra, (RespostaUnica) rb);
                    break;
                case 2: // ORDENADA
                    int numOpcions = p.getNumOpcions();
                    sum += distanciaOrdenada((RespostaOrdenada) ra, (RespostaOrdenada) rb, numOpcions);
                    break;
                case 3: // MULTIPLE
                    sum += distanciaNoOrdenadaMultiple((RespostaMultiple) ra, (RespostaMultiple) rb);
                    break;
                case 4: // LLIURE
                    sum += distanciaLliure((RespostaLliure) ra, (RespostaLliure) rb);
                    break;
                default:
                    throw new IllegalArgumentException("Tipus de pregunta desconegut: " + tipus);
            }
        }

        return sum;
    }
}
