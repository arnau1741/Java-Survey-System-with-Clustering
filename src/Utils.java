import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {
    public static String Base_path;

    /**
     * Mètode que mostra el menú principal
     * @param io objecte d'entrada/sortida
     * @throws Exception si hi ha un error d'entrada/sortida
     */
    public static void mostrarMenu(inout io) throws Exception {
        io.writeln("======================================");
        io.writeln("              ENQUESTES");
        io.writeln("======================================");
        io.writeln(" 1) Crear enquesta manualment");
        io.writeln(" 2) Crear enquesta des de fitxer");
        io.writeln(" 3) Mostrar enquesta");
        io.writeln(" 4) Mostrar enquesta amb preguntes");
        io.writeln(" 5) Mostrar respostes d'una enquesta");
        io.writeln(" 6) Respon enquesta");
        io.writeln(" 7) Exportar enquesta");
        io.writeln(" 8) Importar respostes");
        io.writeln(" 9) Exportar respostes");
        io.writeln(" 10) Consultar usuari");
        io.writeln(" 11) Modificar enquesta");
        io.writeln(" 12) Modificar resposta");
        io.writeln(" 13) Esborrar enquesta");
        io.writeln(" 14) Esborrar resposta");
        io.writeln(" 15) Clustering d'usuaris");


        /*
        io.writeln(" 16) Crear usuari");
        io.writeln(" 17) Consultar respostes");
        io.writeln(" 18) Modificar enquesta");
        io.writeln(" 19) Esborrar enquesta");
         */
        io.writeln(" 0) Sortir");
        io.writeln("======================================");
        io.write("Selecciona una opció: ");
    }

    /**
     * Llegeix una opció de menú
     * @param io objecte d'entrada/sortida
     * @return l'opció llegida
     * @throws Exception si hi ha un error d'entrada/sortida
     */
    public static int llegirOpcio(inout io) throws Exception {
        int opcio;
        try {
            opcio = io.readint();
        } catch (Exception e) {
            opcio = -1; // si entra qualsevol cosa rara
        }
        io.readline(); // consumir fi de línia
        return opcio;
    }

    // ==================== CONSULTAR ENQUESTA ====================
    /**
     * Mostra una enquesta donat el seu Id
     * @param io objecte d'entrada/sortida
     * @param ctrl controlador de domini
     * @throws Exception si hi ha un error d'entrada/sortida o l'enquesta no existeix
     */
    public static void consultarEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("Introdueix el Id de l'enquesta");
        Integer idEnquesta = io.readint();

        List<String> result = ctrl.consultarEnquesta(idEnquesta);
        for (String line : result) {
            io.writeln(line);
        }
    }

    // ==================== Funcionalitats ====================


    //=================== RESPONDRE ENQUESTA ====================
    /**
     * Permet respondre una enquesta
     * @param io objecte d'entrada/sortida
     * @param ctrl controlador de domini
     * @throws Exception si hi ha un error d'entrada/sortida o l'enquesta no existeix
     */
    public static void respondreEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("\n--- RESPONDRE ENQUESTA ---");
        io.write("Introdueix l'ID de l'enquesta: ");
        Integer idEnquesta = io.readint();
        io.readline();

        io.write("Introdueix l'ID de l'usuari que respon l'enquesta: ");
        int idUsuari = io.readint();
        io.readline();

        List<String> preguntes = ctrl.getPreguntes(idEnquesta);
        List<String> respostesUsuari = new ArrayList<>();

        io.writeln("\nRespon les següents preguntes:");
        int idx = 0;
        int size = preguntes.size();
        while(idx < size){
            String tipus = preguntes.get(idx);
            idx++;
            String enunciat = preguntes.get(idx);
            idx++;
            io.writeln("\nPregunta: " + enunciat);
            if (tipus.equals("UNICA") || tipus.equals("MULTIPLE") || tipus.equals("ORDENADA")) { //UNICA, MULTIPLE, ORDENADA
                int numOpcions = Integer.parseInt(preguntes.get(idx));
                idx++;
                io.writeln("Opcions:");
                for (int i = 0; i < numOpcions; i++) {
                    String opcio = preguntes.get(idx);
                    idx++;
                    io.writeln(" " + (i) + ") " + opcio);
                }
                io.write("Introdueix la teva resposta (números separats per comes si és múltiple): ");
                String resposta = io.readline();
                respostesUsuari.add(resposta);
            }
            else if (tipus.equals("NUMERICA")) { //NUMERICA
                io.write("Introdueix la teva resposta numèrica: ");
                String resposta = io.readline();
                respostesUsuari.add(resposta);
            }
            else if (tipus.equals("LLIURE")) { //LLIURE
                io.write("Introdueix la teva resposta lliure: ");
                String resposta = io.readline();
                respostesUsuari.add(resposta);
            }
            idx++;
        }
        ctrl.respondreEnquesta(idEnquesta, idUsuari, respostesUsuari);
        io.writeln("\n[OK] Enquesta resposta correctament!\n");
    }

    // ==================== CREACIÓ MANUAL D'ENQUESTA ====================
    /**
     * Crea una enquesta manualment
     * @param io objecte d'entrada/sortida
     * @param ctrl controlador de domini
     * @throws Exception si hi ha un error d'entrada/sortida o l'enquesta no es pot crear
     */
    public static void crearEnquestaManual(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("\n--- CREACIÓ D'ENQUESTA (MANUAL) ---");

        io.write("Títol: ");
        String titol = io.readline();

        io.write("Descripció: ");
        String descripcio = io.readline();

        io.write("ID del creador (enter): ");
        int idCreador = io.readint();
        io.readline();

        io.write("Nombre de preguntes: ");
        int numPreguntes = io.readint();
        io.readline();

        List<String> preguntes = new ArrayList<>();

        for (int i = 0; i < numPreguntes; i++) {
            int index = i + 1;

            io.writeln("\nPregunta " + index + ":");
            io.writeln(" Tipus (0=NUMÈRICA, 1=ÚNICA, 2=ORDENADA, 3=MÚLTIPLE, 4=LLIURE)");
            io.write("   Introdueix el tipus: ");
            int tipus = io.readint();
            io.readline();

            io.write("   Text de la pregunta: ");
            String textPregunta = io.readline();

            preguntes.add(Integer.toString(tipus));
            preguntes.add(textPregunta);

            if (tipus == 1 || tipus == 2 || tipus == 3) {
                io.write("   Nombre d'opcions: ");
                int numOpcions = io.readint();
                io.readline();

                preguntes.add(Integer.toString(numOpcions));
                for (int j = 0; j < numOpcions; j++) {
                    io.write("     Opció " + (j + 1) + ": ");
                    String opcio = io.readline();
                    preguntes.add(opcio);
                }
            }
        }

        ctrl.crearEnquesta(titol, descripcio, idCreador, preguntes);
        io.writeln("\n[OK] Enquesta creada correctament!\n");
    }

    // ==================== CREACIÓ DES DE FITXER ====================
    /**
     * Crea una enquesta des de fitxer
     * @param io objecte d'entrada/sortida
     * @param ctrl controlador de domini
     * @throws Exception si hi ha un error d'entrada/sortida o l'enquesta no es pot crear
     */
    public static void crearEnquestaDesDeFitxer(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("\n--- CREACIÓ D'ENQUESTA DES DE FITXER ---");
        io.writeln("Format esperat del fitxer:");
        io.writeln("  línia 1: títol");
        io.writeln("  línia 2: descripció");
        io.writeln("  línia 3: id creador (enter)");
        io.writeln("  línia 4: nombre de preguntes");
        io.writeln("  després, per cada pregunta:");
        io.writeln("    línia: tipus (0..4) (Tipus (0=NUMÈRICA, 1=ÚNICA, 2=ORDENADA, 3=MÚLTIPLE, 4=LLIURE))");
        io.writeln("    línia: text pregunta");
        io.writeln("    si tipus és 1,2,3:");
        io.writeln("       línia: nombre d'opcions");
        io.writeln("       següents línies: cada opció\n");

        io.writeln("Introdueix el nombre de fitxer d'enquesta (sense extensió): ");
        String fitxer = io.readword().trim();
        String path = Base_path + File.separator + fitxer + ".txt";
        io.writeln("Llegint fitxer: " + path);
        File f = new File(path);
        if (!f.exists()) {
            io.writeln("No s'ha trobat el fitxer!");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String titol = llegirObligatori(br, "Falta títol");
            String descripcio = llegirObligatori(br, "Falta descripció");

            int idCreador = llegirIntObligatori(br, "Falta ID creador");
            int numPreguntes = llegirIntObligatori(br, "Falta nombre de preguntes");

            List<String> preguntes = new ArrayList<>();

            for (int i = 0; i < numPreguntes; i++) {
                String liniaTipus = llegirObligatori(br, "Falta tipus de la pregunta " + (i + 1));
                int tipus = parseOrThrow(liniaTipus, "Tipus invàlid a la pregunta " + (i + 1));

                String textPregunta = llegirObligatori(br, "Falta text de la pregunta " + (i + 1));

                preguntes.add(Integer.toString(tipus));
                preguntes.add(textPregunta);

                if (tipus == 1 || tipus == 2 || tipus == 3) {
                    String liniaNumOpcions = llegirObligatori(
                            br,
                            "Falta nombre d'opcions per la pregunta " + (i + 1)
                    );
                    int numOpcions = parseOrThrow(
                            liniaNumOpcions,
                            "Nombre d'opcions invàlid a la pregunta " + (i + 1)
                    );

                    preguntes.add(Integer.toString(numOpcions));
                    for (int j = 0; j < numOpcions; j++) {
                        String opcio = llegirObligatori(
                                br,
                                "Falta l'opció " + (j + 1) + " per la pregunta " + (i + 1)
                        );
                        preguntes.add(opcio);
                    }
                }
            }

            ctrl.crearEnquesta(titol, descripcio, idCreador, preguntes);
            io.writeln("\n[OK] Enquesta creada correctament des del fitxer!\n");

        } catch (IOException e) {
            io.writeln("\n[ERROR] No s'ha pogut llegir el fitxer: " + e.getMessage() + "\n");
        } catch (IllegalArgumentException e) {
            io.writeln("\n[ERROR FORMAT FITXER] " + e.getMessage() + "\n");
        }
    }

    // ==================== IMPORTAR RESPOSTES ====================
    /**
     * Importa respostes des d'un fitxer
     * @param io objecte d'entrada/sortida
     * @param ctrl controlador de domini
     * @throws Exception si hi ha un error d'entrada/sortida o l'enquesta no existeix
     */
    public static void importarRespostes(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("\n--- IMPORTAR RESPOSTES DES DE FITXER ---");
        io.write("Introdueix l'ID de l'usuari que respon l'enquesta: ");
        int idUsuari = io.readint();
        io.readline();

        io.write("Introdueix l'ID de l'enquesta: ");
        int idEnquesta = io.readint();
        io.readline();

        io.write("Introdueix el nombre de fitxer de respostes (sense extensió): ");
        String fitxer = io.readword().trim();
        String path = Base_path + File.separator + fitxer + ".txt";
        io.writeln("Llegint fitxer: " + path);
        File f = new File(path);
        if (!f.exists()) {
            io.writeln("No s'ha trobat el fitxer!");
            return;
        }

        int numRespostesImportades = ctrl.importarRespostes(idUsuari, path, idEnquesta);
        io.writeln("\n[OK] Respostes importades correctament! Total respostes importades: " + numRespostesImportades + "\n");
    }


    // ==================== EXPORTAR ENQUESTA ====================
    /**
     * Exporta una enquesta a un fitxer
     * @param io objecte d'entrada/sortida
     * @param ctrl controlador de domini
     * @throws Exception si hi ha un error d'entrada/sortida o l'enquesta no existeix
     */
    public static void exportarEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("Introdueix l'ID de l'enquesta a exportar: ");
        int id = io.readint();

        io.write("Introdueix el fitxer on es guardar (sense extencio): ");
        String fitxer = io.readword();
        String path = Base_path + File.separator + fitxer + ".txt";
        List<String> export = ctrl.exportarEnquesta(id);
        if (export == null) {
            io.writeln("Enquesta no trobada.");
            return;
        }

        writeAllLines(path, export);
        io.writeln("Enquesta exportada correctament a: " + path);
    }

    // ==================== CONSULTAR PERFIL USUARI ====================
    /**
     * Permet consultar el perfil d'un usuari
     * @param io objecte d'entrada/sortida
     * @param ctrl controlador de domini
     * @throws Exception si hi ha un error d'entrada/sortida o l'usuari no existeix
     */
    public static void consultarPerfil(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("Introduiex l'Id del usuari");
        int idUsuari = io.readint();
        List<String> perfil = ctrl.consultarPerfil(idUsuari);
        for (String line : perfil) {
            io.writeln(line);
        }
    }

    // ==================== CONSULTAR ENQUESTA ====================
    /**
     * Permet consultar una enquesta amb les seves preguntes
     * @param io objecte d'entrada/sortida
     * @param ctrl controlador de domini
     * @throws Exception si hi ha un error d'entrada/sortida o l'enquesta no existeix
     */
    public static void consultarEnquestaAmbPreguntes(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("Introdueix l'ID de l'enquesta a consultar: ");
        int id = io.readint();
        List<String> info = ctrl.consultarEnquestaAmbPreguntes(id);
        for (String line : info) {
            io.writeln(line);
        }
    }

    // ==================== CONSULTAR RESULTATS ENQUESTA ====================
    public static void consultarRespostesEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("Introdueix l'ID de l'enquesta a consultar les respostes: ");
        int id = io.readint();
        List<String> respostes = ctrl.consultarRespostesEnquesta(id);
        for (String line : respostes) {
            io.writeln(line);
        }
    }

    // ===================== CLUSTERING =====================
    public static void clustering(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("\n--- CLUSTERING D'USUARIS ---");
        io.write("Introdueix l'ID de l'enquesta: ");
        int idEnquesta = io.readint();
        io.readline();

        io.write("Introdueix el nombre de clusters (k): ");
        int k = io.readint();
        io.readline();

        io.write("Introdueix nombre maxim d'iteracions: ");
        int maxIter = io.readint();
        io.readline();

        Map<Integer, Integer> result = ctrl.clustering(idEnquesta, k, maxIter);
        for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
            io.writeln("Usuari ID: " + entry.getKey() + " -> Cluster: " + entry.getValue());
        }
        io.writeln("\n[OK] Clustering realitzat correctament!\n");
    }

    /**
     * Escriu totes les línies a un fitxer
     * @param path del fitxer
     * @param lines llistes de línies a escriure
     * @throws IOException si hi ha un error d'entrada/sortida
     */
    private static void writeAllLines(String path, List<String> lines) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (String l : lines) pw.println(l);
        }
    }

    // ==================== ESBORRAR ENQUESTA =====================
    /**
     * Esborra l'enquesta identificada per idEnquesta d'un usuari
     * @param io objecte d'entrada/sortida
     * @param ctrl controlador de domini
     * @throws Exception
     */
    public static void esborrarEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("Introduiex l'index de l'enquesta a esborrar");
        int idEnquesta = io.readint();
        io.writeln("Introduiex l'Id de l'usuari");
        int idUsuari =io.readint();
        ctrl.eliminarEnquesta(idUsuari, idEnquesta);
    }

    // ===================== ESBORRAR RESPOSTA =====================
    /**
     * Esborra la resposta d'una enquesta d'un enquestat
     * @param io objecte d'entrada/sortida
     * @param ctrl controlador de domini
     * @throws Exception
     */
    public static void esborrarResposta(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("Introduiex l'index de l'enquesta a esborrar");
        int idEnquesta = io.readint();
        io.writeln("Introduiex l'Id de l'enquestat");
        int idEnquestat =io.readint();
        //Suposem usuari 1 perque l'implementacio es fara mes endavant
        int Usuari = 1;
        ctrl.esborrarRespostaEnquesta(Usuari, idEnquesta, idEnquestat);
    }

    // ===================== MODIFICAR ENQUESTA =====================
    /**
     * Modifica una pregunta d'una enquesta
     * @param io objecte d'entrada/sortida
     * @param ctrl controlador de domini
     * @throws Exception
     */
    public static void modificarEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        consultarEnquestaAmbPreguntes(io, ctrl);
        io.writeln("Introdueix l'index de l'enquesta a modificar");
        int idEnquesta = io.readint();

        io.writeln("Introdueix l'index de la pregunta");
        int idxPregunta = io.readint();

        List<String> novaPregunta = new ArrayList<>();

        io.writeln("Introdueix pregunta");
        String text = io.readword();

        io.writeln("Introdueix tipus (nomes numero):");
        io.writeln(" - 0:Numerica");
        io.writeln(" - 1:Unica");
        io.writeln(" - 2:Ordenada");
        io.writeln(" - 3:Multiple");
        io.writeln(" - 4:Lliure");
        String tipus = io.readword();

        novaPregunta.add(text);
        novaPregunta.add(tipus);

        if(!tipus.equals("0") || !tipus.equals("4")) {
            io.writeln("Introdueix el numero d'opcions");
            int numero = io.readint();
            io.writeln("Indica les opcions");
            for(int i = 0; i < numero; i++) {
                novaPregunta.add(io.readword());
            }
        }

        int resultat = ctrl.modificarPreguntaEnquesta(idEnquesta, idxPregunta, novaPregunta);
        if (resultat == 1) {
            io.writeln("S'ha esborrat correctament");
        }
        else {
            io.writeln("Problema: l'enquesta no s'ha esborrat");
        }

    }

    // ===================== MODIFICAR RESPOSTA =====================
    public static void modificarResposta(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("Introdueix l'id de l'enquesta");
        int idEnquesta = io.readint();

        io.writeln("Introduexi l'id de l'usuari");
        int idUsuari = io.readint();
        List<String> respostesActuals = ctrl.getRespostesEnquestaPerUsuari(idEnquesta, idUsuari);
        for (String resposta : respostesActuals) {
            io.writeln(resposta);
        }

        io.writeln();
        io.writeln("Quina pregunta vols modificar? (introdueix el número de pregunta):");
        int idxPregunta = io.readint();
        io.writeln("Introdueix la nova resposta:");
        String novaResposta = io.readword();
        int codierr = ctrl.modificarRespostaEnquesta(idEnquesta, idUsuari, idxPregunta, novaResposta);
        if (codierr == 0) {
            io.writeln("Resposta modificada correctament.");
        } else {
            io.writeln("Error en modificar la resposta. Codi d'error: " + codierr);
        }
    }

    // ===================== HELPERS FITXER =====================
    /**
     * Llegeix una línia obligatòria del BufferedReader
     * @param br bufferedReader
     * @param errorMsg errorMsg missatge d'error si no es pot llegir
     * @return la línia llegida
     * @throws IOException si hi ha un error d'entrada/sortida
     */
    private static String llegirObligatori(BufferedReader br, String errorMsg) throws IOException {
        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }
        return line.trim();
    }

    /**
     * Llegeix un enter obligatori del BufferedReader
     * @param br bufferedReader
     * @param errorMsg missatge d'error si no es pot llegir
     * @return l'enter llegit
     * @throws IOException si hi ha un error d'entrada/sortida
     */
    private static int llegirIntObligatori(BufferedReader br, String errorMsg) throws IOException {
        String line = llegirObligatori(br, errorMsg);
        return parseOrThrow(line, errorMsg);
    }

    /**
     * Parsea un enter o llença una excepció amb missatge personalitzat
     * @param s cadena a parsear
     * @param errorMsg missatge d'error si no es pot parsear
     * @return l'enter parseat
     * @throws IllegalArgumentException si no es pot parsear
     */
    private static int parseOrThrow(String s, String errorMsg) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMsg + " (valor llegit: '" + s + "')");
        }
    }
}