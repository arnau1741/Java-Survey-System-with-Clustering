package prop.enquestes.domini;

import java.util.List;
import java.util.Map;

public class Estadistica {

    /**
     * Mètode principal que recorre l'enquesta i mostra estadístiques per consola
     * @param enquesta L'enquesta a analitzar
     */
    public void mostrarEstadistiques(Enquesta enquesta) {
        if (enquesta == null) {
            System.out.println("L'enquesta és nul·la.");
            return;
        }
        //push test

        System.out.println("==================================================");
        System.out.println("ESTADÍSTIQUES DE L'ENQUESTA: " + enquesta.getTitol());
        System.out.println("Total de preguntes: " + enquesta.getNumPreguntes());
        System.out.println("==================================================\n");

        int index = 1;
        for (Pregunta p : enquesta.getPreguntesObj()) {
            System.out.println("--------------------------------------------------");
            System.out.println("Pregunta " + index + ": " + p.getText());
            System.out.println("Tipus: " + p.getTipus() + " | Respostes totals: " + p.getNumRespostes());
            System.out.println("--------------------------------------------------");

            if (p.getNumRespostes() > 0) {
                switch (p.getTipus()) {
                    case 0: // NUMERICA
                        analitzarNumerica(p);
                        break;
                    case 1: // UNICA
                    case 2: // ORDENADA
                        analitzarCategoricaUnica(p);
                        break;
                    case 3: // MULTIPLE
                        analitzarCategoricaMultiple(p);
                        break;
                    case 4: // LLIURE
                        analitzarLliure(p);
                        break;
                    default:
                        System.out.println("Tipus desconegut.");
                }
            } else {
                System.out.println("   (Sense dades per analitzar)");
            }
            System.out.println("\n");
            index++;
        }
    }

    private void analitzarNumerica(Pregunta p) {
        double suma = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        int count = 0;

        for (Resposta r : p.getRespostes().values()) {
            if (r instanceof RespostaNumerica) {
                Double valor = ((RespostaNumerica) r).getValor();
                if (valor != null) {
                    suma += valor;
                    if (valor < min) min = valor;
                    if (valor > max) max = valor;
                    count++;
                }
            }
        }

        if (count > 0) {
            double mitjana = suma / count;
            System.out.printf("   * Mitjana: %.2f%n", mitjana);
            System.out.printf("   * Mínim:   %.2f%n", min);
            System.out.printf("   * Màxim:   %.2f%n", max);

            // Opcional: Desviació estàndard
            double sumQuadrats = 0;
            for (Resposta r : p.getRespostes().values()) {
                Double valor = ((RespostaNumerica) r).getValor();
                if (valor != null) sumQuadrats += Math.pow(valor - mitjana, 2);
            }
            double desviacio = Math.sqrt(sumQuadrats / count);
            System.out.printf("   * Desviació Estàndard: %.2f%n", desviacio);
        }
    }

    private void analitzarCategoricaUnica(Pregunta p) {
        List<String> opcions = p.getOpcions();
        int[] frequencies = new int[opcions.size()];
        int total = 0;

        for (Resposta r : p.getRespostes().values()) {
            Integer idx = null;
            if (r instanceof RespostaUnica) idx = ((RespostaUnica) r).getResposta();
            else if (r instanceof RespostaOrdenada) idx = ((RespostaOrdenada) r).getResposta();

            if (idx != null && idx >= 0 && idx < frequencies.length) {
                frequencies[idx]++;
                total++;
            }
        }

        System.out.println("   Distribució de respostes:");
        for (int i = 0; i < opcions.size(); i++) {
            double percentatge = (total > 0) ? (frequencies[i] * 100.0 / total) : 0.0;
            // Mostra una barra gràfica simple
            String barra = "|".repeat((int) percentatge / 2);
            System.out.printf("   [%d] %-15s : %3d vots (%.1f%%) %s%n",
                    i, opcions.get(i), frequencies[i], percentatge, barra);
        }
    }

    private void analitzarCategoricaMultiple(Pregunta p) {
        List<String> opcions = p.getOpcions();
        int[] frequencies = new int[opcions.size()];
        int totalVotsEmessos = 0; // Total de 'creus' marcades
        int totalUsuaris = 0;

        for (Resposta r : p.getRespostes().values()) {
            if (r instanceof RespostaMultiple) {
                totalUsuaris++;
                List<Integer> seleccions = ((RespostaMultiple) r).getRespostes();
                if (seleccions != null) {
                    for (Integer idx : seleccions) {
                        if (idx >= 0 && idx < frequencies.length) {
                            frequencies[idx]++;
                            totalVotsEmessos++;
                        }
                    }
                }
            }
        }

        System.out.println("   Freqüència d'elecció (Multiple):");
        for (int i = 0; i < opcions.size(); i++) {
            // Percentatge respecte al total d'usuaris que han respost (pot sumar > 100%)
            double percentatge = (totalUsuaris > 0) ? (frequencies[i] * 100.0 / totalUsuaris) : 0.0;
            System.out.printf("   [%d] %-15s : Seleccionat %d vegades (per un %.1f%% dels usuaris)%n",
                    i, opcions.get(i), frequencies[i], percentatge);
        }
    }

    private void analitzarLliure(Pregunta p) {
        System.out.println("   (Anàlisi de text no disponible automàticament)");
        System.out.println("   Mostra de les últimes 3 respostes:");

        int count = 0;
        int maxShow = 3;
        // Convertim a llista per agafar els últims o primers
        for (Resposta r : p.getRespostes().values()) {
            if (r instanceof RespostaLliure && count < maxShow) {
                String text = ((RespostaLliure) r).getResposta();
                if (text != null && !text.isBlank()) {
                    System.out.println("    - \"" + text + "\"");
                    count++;
                }
            }
        }
        if (p.getNumRespostes() > maxShow) {
            System.out.println("    ... i " + (p.getNumRespostes() - maxShow) + " més.");
        }
    }
}