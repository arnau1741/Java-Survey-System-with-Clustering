//package prop.enquesta.drivers;

//import prop.enquesta.excepcions.RespostaInvalida;
//import prop.enquesta.resposta.*;
//import prop.enquesta.utils.Validacio;

import java.io.*;
import java.util.*;

public class DriverInteractiu {

    private static final Scanner sc = new Scanner(System.in);
    private static List<Pregunta> preguntes = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("===== DRIVER INTERACTIU PREGUNTA-RESPOSTA =====");
        System.out.print("Vols crear una nova enquesta (c) o respondre una existent (r)? ");
        String mode = sc.nextLine().trim().toLowerCase();

        try {
            if (mode.equals("c")) {
                crearEnquesta();
                System.out.print("Vols respondre l'enquesta ara mateix? (s/n): ");
                String resposta = sc.nextLine().trim().toLowerCase();
                if (resposta.equals("s")) {
                    respondreEnquesta();
                }
            } else if (mode.equals("r")) {
                respondreEnquesta();
            } else {
                System.out.println("Opció no vàlida.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Crear una nova enquesta interactivament
    private static void crearEnquesta() throws Exception {
        System.out.println("\n===== CREACIÓ D'ENQUESTA =====");
        System.out.print("Quantes preguntes vols crear? ");
        int numPreguntes = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < numPreguntes; i++) {
            System.out.println("\n--- Pregunta " + (i + 1) + " ---");

            System.out.print("Text de la pregunta: ");
            String text = sc.nextLine();

            System.out.println("Tipus de pregunta:");
            System.out.println("0. NUMÈRICA");
            System.out.println("1. UNICA");
            System.out.println("2. MULTIPLE");
            System.out.println("3. ORDENADA");
            System.out.println("4. LLIURE");
            System.out.print("Selecciona el tipus (0-4): ");
            int tipus = Integer.parseInt(sc.nextLine());

            List<String> opcions = new ArrayList<>();
            if (tipus >= 1 && tipus <= 3) { // UNICA, MULTIPLE, ORDENADA
                System.out.print("Quantes opcions vols? ");
                int numOpcions = Integer.parseInt(sc.nextLine());

                for (int j = 0; j < numOpcions; j++) {
                    System.out.print("Opció " + (j + 1) + ": ");
                    opcions.add(sc.nextLine());
                }
            }

            Pregunta pregunta = new Pregunta(text, tipus, opcions);
            preguntes.add(pregunta);
            System.out.println("✓ Pregunta creada correctament");
        }

        System.out.println("\n✓ Enquesta creada amb " + preguntes.size() + " preguntes");
    }

    // Respondre l'enquesta actual
    private static void respondreEnquesta() throws Exception {
        if (preguntes.isEmpty()) {
            System.out.println("No hi ha preguntes per respondre. Crea una enquesta primer.");
            return;
        }

        System.out.println("\n===== RESPON L'ENQUESTA =====");
        System.out.print("Introdueix el teu ID d'usuari: ");
        int idUsuari = Integer.parseInt(sc.nextLine());

        List<Resposta> respostesUsuari = new ArrayList<>();

        for (int i = 0; i < preguntes.size(); i++) {
            Pregunta pregunta = preguntes.get(i);
            System.out.println("\nPregunta " + (i + 1) + ": " + pregunta.getText());
            System.out.println("Tipus: " + getNomTipus(pregunta.getTipus()));

            if (pregunta.getOpcions() != null && !pregunta.getOpcions().isEmpty()) {
                System.out.println("Opcions: " + pregunta.getOpcions());
            }

            Resposta resposta = obtenirRespostaUsuari(pregunta);
            if (resposta != null) {
                try {
                    resposta.validar();
                    int resultat = pregunta.addResposta(resposta, idUsuari);
                    if (resultat == 1) {
                        respostesUsuari.add(resposta);
                        System.out.println("Resposta registrada: " + resposta.getText(pregunta.getOpcions()));
                    } else {
                        System.out.println("Ja havies respost a aquesta pregunta");
                    }
                } catch (RespostaInvalida e) {
                    System.out.println("Resposta invàlida: " + e.getMessage());
                    i--;
                }
            }
        }

        guardarResultats(respostesUsuari, idUsuari);
        mostrarEstadistiques();
    }

    // Obtenir resposta de l'usuari segons el tipus de pregunta
    private static Resposta obtenirRespostaUsuari(Pregunta pregunta) {
        int tipus = pregunta.getTipus();
        List<String> opcions = pregunta.getOpcions();

        try {
            switch (tipus) {
                case 0: // NUMÈRICA
                    System.out.println("Introdueix un número (0-10): ");
                    double valor = Double.parseDouble(sc.nextLine());
                    return new RespostaNumerica(valor);

                case 1: // unica
                    System.out.println("Selecciona una opció (número): ");
                    for (int j = 0; j < opcions.size(); j++) {
                        System.out.println((j + 1) + ". " + opcions.get(j));
                    }
                    int opcioUnica = Integer.parseInt(sc.nextLine()) - 1;
                    RespostaUnica resUnica = new RespostaUnica(opcions.size());
                    resUnica.setResposta(opcioUnica);
                    return resUnica;


                case 2: // MULTIPLE
                    System.out.println("Selecciona les opcions (ex: 1,3,4): ");
                    for (int j = 0; j < opcions.size(); j++) {
                        System.out.println((j + 1) + ". " + opcions.get(j));
                    }
                    String[] seleccions = sc.nextLine().split(",");
                    List<Integer> opcionsMultiples = new ArrayList<>();
                    for (String sel : seleccions) {
                        opcionsMultiples.add(Integer.parseInt(sel.trim()) - 1);
                    }
                    RespostaMultiple resMultiple = new RespostaMultiple(opcions.size());
                    resMultiple.selecciona(opcionsMultiples);
                    return resMultiple;

                case 3: // ORDENADA
                    System.out.println("Selecciona una opció (número): ");
                    for (int j = 0; j < opcions.size(); j++) {
                        System.out.println((j + 1) + ". " + opcions.get(j));
                    }
                    int opcioOrdenada = Integer.parseInt(sc.nextLine()) - 1;
                    RespostaOrdenada resOrdenada = new RespostaOrdenada(opcions.size());
                    resOrdenada.setResposta(opcioOrdenada);
                    return resOrdenada;

                case 4: // Lliure
                    System.out.println("Escriu la teva resposta: ");
                    String text = sc.nextLine();
                    return new RespostaLliure(text);


                default:
                    System.out.println("Tipus de pregunta desconegut");
                    return null;
            }
        } catch (Exception e) {
            System.out.println("Error en la resposta: " + e.getMessage());
            return null;
        }
    }

    // Guardar resultats
    private static void guardarResultats(List<Resposta> respostes, int idUsuari) throws IOException {
        PrintWriter out = new PrintWriter("resultats_usuari_" + idUsuari + ".txt");
        out.println("===== RESULTATS DE L'ENQUESTA =====");
        out.println("Usuari: " + idUsuari);
        out.println("Data: " + new Date());
        out.println();

        for (int i = 0; i < respostes.size(); i++) {
            Pregunta p = preguntes.get(i);
            Resposta r = respostes.get(i);
            out.println((i + 1) + ". " + p.getText());
            out.println("   Resposta: " + r.getText(p.getOpcions()));
            out.println();
        }

        out.close();
        System.out.println("\n✓ Resultats guardats a 'resultats_usuari_" + idUsuari + ".txt'");
    }

    // Mostrar estadístiques
    private static void mostrarEstadistiques() {
        System.out.println("\n===== ESTADÍSTIQUES =====");
        System.out.println("Total de preguntes: " + preguntes.size());

        for (int i = 0; i < preguntes.size(); i++) {
            Pregunta p = preguntes.get(i);
            System.out.println("\nPregunta " + (i + 1) + ": " + p.getText());
            System.out.println("  - Tipus: " + getNomTipus(p.getTipus()));
            System.out.println("  - Respostes rebudes: " + p.getNumRespostes());

            if (p.getTipus() == 2 || p.getTipus() == 3) { // UNICA o MULTIPLE
                try {
                    Resposta moda = p.getRespostaModa();
                    System.out.println("  - Moda: " + moda.getText(p.getOpcions()));
                } catch (UnsupportedOperationException e) {
                    // No es pot calcular la moda per aquest tipus
                }
            }
        }
    }

    private static String getNomTipus(int tipus) {
        switch (tipus) {
            case 0: return "NUMÈRICA";
            case 1: return "UNICA";
            case 2: return "MÚLTIPLE";
            case 3: return "ORDENADA";
            case 4: return "LLIURE";
            default: return "DESCONEGUT";
        }
    }
}
