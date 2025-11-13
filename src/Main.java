import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Main {

    //Variable per defecte per als fitxers
    private static String Base_path;

    public static void main(String[] args) {
        CtrlDomini ctrl = new CtrlDomini();
        inout io = new inout();
        int opcio = -1;
        Base_path = inicialitzarRutaBase();

        try {
            do {
                mostrarMenu(io);
                opcio = llegirOpcio(io);

                switch (opcio) {
                    case 0 -> io.writeln("\nSortint...");

                    case 1 -> crearEnquestaManual(io, ctrl);
                    case 2 -> crearEnquestaDesDeFitxer(io, ctrl);
                    case 3 -> ctrl.mostrarEnquestes();
                    case 4 -> ctrl.mostrarEnquestesAmbPreguntes();
                    case 5 -> respondreEnquesta(io, ctrl);
                    case 6 -> ctrl.mostrarEnquestesAmbPreguntesIRespostes();
                    case 7 -> exportarEnquesta(io, ctrl);
                    case 8 -> importarRespostes(io, ctrl);

                    default -> io.writeln("\n[ERROR] Opció no vàlida. Torna-ho a intentar.\n");
                }

            } while (opcio != 0);

        } catch (Exception e) {
            // Última xarxa de seguretat perquè inout llença Exception checked
            try {
                io.writeln("\n[ERROR NO CONTROLAT] " + e.getMessage());
            } catch (Exception ignored) {
                // Si ni tan sols podem escriure l'error, no fem res més
            }
        }
    }

    private static String inicialitzarRutaBase() {
        String dir = System.getProperty("user.dir");

        // Para IntelliJ: si estamos ejecutando desde out/production, ajustar la ruta
        if (dir.contains("out") && dir.contains("production")) {
            dir = new File(dir).getParentFile().getParentFile().getAbsolutePath();
        }

        File pruebasDir = new File(dir, "Pruebas");

        // Si no existe, crear la carpeta
        if (!pruebasDir.exists()) {
            pruebasDir.mkdirs();
        }

        return pruebasDir.getAbsolutePath();
    }

    // ========================= MENÚ =========================

    private static void mostrarMenu(inout io) throws Exception {
        io.writeln("======================================");
        io.writeln("              ENQUESTES");
        io.writeln("======================================");
        io.writeln(" 1) Crear enquesta manualment");
        io.writeln(" 2) Crear enquesta des de fitxer");
        io.writeln(" 3) Mostrar enquestes");
        io.writeln(" 4) Mostrar enquestes amb preguntes");
        io.writeln(" 5) Respon enquesta");
        io.writeln(" 6) Mostrar enquestes amb preguntes i respostes");
        io.writeln(" 7) Exportar enquesta");
        io.writeln(" 8) Importar respostes");
        /*
        io.writeln(" 9) Exportar respostes");
        io.writeln(" 10) Crear usuari");
        io.writeln(" 11) Consultar usuari");
        io.writeln(" 12) Consultar respostes"); ????
        io.writeln(" 13) Modificar enquesta");
        io.writeln(" 14) Esborrar enquesta");
         */
        io.writeln(" 0) Sortir");
        io.writeln("======================================");
        io.write("Selecciona una opció: ");
    }

    private static int llegirOpcio(inout io) throws Exception {
        int opcio;
        try {
            opcio = io.readint();
        } catch (Exception e) {
            opcio = -1; // si entra qualsevol cosa rara
        }
        io.readline(); // consumir fi de línia
        return opcio;
    }


    public static void respondreEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("\n--- RESPONDRE ENQUESTA ---");
        io.write("Introdueix l'ID de l'enquesta: ");
        int idEnquesta = io.readint();
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
            String enunciat = preguntes.get(idx);
            idx++;
            int tipus = Integer.parseInt(enunciat);
            enunciat = preguntes.get(idx);
            idx++;
            io.writeln("\nPregunta: " + enunciat);
            if (tipus == 1 || tipus == 2 || tipus == 3) { //UNICA, MULTIPLE, ORDENADA
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
            else if (tipus == 0) { //NUMERICA
                io.write("Introdueix la teva resposta numèrica: ");
                String resposta = io.readline();
                respostesUsuari.add(resposta);
            }
            else if (tipus == 4) { //LLIURE
                io.write("Introdueix la teva resposta lliure: ");
                String resposta = io.readline();
                respostesUsuari.add(resposta);
            }
        }
        ctrl.respondreEnquesta(idEnquesta, idUsuari, respostesUsuari);
        io.writeln("\n[OK] Enquesta resposta correctament!\n");
    }


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

    // ==================== CREACIÓ MANUAL ====================

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
            io.writeln(" Tipus (0=NUMÈRICA, 1=ÚNICA, 2=MÚLTIPLE, 3=ORDENADA, 4=LLIURE)");
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

    public static void crearEnquestaDesDeFitxer(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("\n--- CREACIÓ D'ENQUESTA DES DE FITXER ---");
        io.writeln("Format esperat del fitxer:");
        io.writeln("  línia 1: títol");
        io.writeln("  línia 2: descripció");
        io.writeln("  línia 3: id creador (enter)");
        io.writeln("  línia 4: nombre de preguntes");
        io.writeln("  després, per cada pregunta:");
        io.writeln("    línia: tipus (0..4)");
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

    // === EXPORTAR ENQUESTA ===
    private static void exportarEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("Introdueix l'ID de l'enquesta a exportar: ");
        int id = io.readint();

        //io.write("Introdueix el path on guardar (ex: sortida.txt): ");
        //String path = io.readword();
        String path = Base_path + File.separator + "sortida" + id + ".txt";
        List<String> export = ctrl.exportarEnquesta(id);
        if (export == null) {
            io.writeln("Enquesta no trobada.");
            return;
        }

        writeAllLines(path, export);
        io.writeln("Enquesta exportada correctament a: " + path);
    }

    private static void writeAllLines(String path, List<String> lines) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (String l : lines) pw.println(l);
        }
    }
    // ===================== HELPERS FITXER =====================

    private static String llegirObligatori(BufferedReader br, String errorMsg) throws IOException {
        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }
        return line.trim();
    }

    private static int llegirIntObligatori(BufferedReader br, String errorMsg) throws IOException {
        String line = llegirObligatori(br, errorMsg);
        return parseOrThrow(line, errorMsg);
    }

    private static int parseOrThrow(String s, String errorMsg) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMsg + " (valor llegit: '" + s + "')");
        }
    }
}

