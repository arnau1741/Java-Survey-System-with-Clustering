import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        CtrlDomini ctrl = new CtrlDomini();
        inout io = new inout();
        int opcio = -1;

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
        io.writeln("\n[OK] Enquesta respondida correctament!\n");
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

        io.write("Introdueix el nom o ruta del fitxer: ");
        String path = io.readline();

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

/*
 
public class Main {
    public static void main(String[] args) throws Exception {
        CtrlDomini ctrl = new CtrlDomini();
        inout io = new inout();
        int opcio = 0;
        do {
            io.writeln("======Enquesta======");
            io.writeln("1. Importar enquestes");
            io.writeln("2. Exportar enquestes");
            io.writeln("3. Importar usuaris");
            io.writeln("4. Exportar usuaris");
            io.writeln("5. Sortir");
            io.write("Selecciona una opcio: ");
            opcio = io.readint();

            try {
                switch (opcio) {
                    case -1 -> io.writeln("Surt");

                    case 1 -> importarEnquesta(io, ctrl);

                    case 2 -> exportarEnquesta(io, ctrl);

                    case 3 -> importarRespostes(io, ctrl);

                    case 4 -> exportarRespostes(io, ctrl);

                    case 5 -> crearUsuari(io, ctrl);

                    case 6 -> consultarUsuari(io, ctrl);

                    case 7 -> consultarEnquesta(io, ctrl);

                    case 8 -> consultarRespostas(io, ctrl);

                    case 9 -> crearEnquesta(io, ctrl);

                    case 10 -> modificarEnquesta(io, ctrl);

                    case 11 -> esborrarEnquesta(io, ctrl);

                    case 12 -> respondreEnquesta(io, ctrl);

                    default -> io.writeln("Opció no vàlida.");
                }
            } catch (IOException e) {
                //io.writeln("Error d’E/S: " + e.getMessage());
            }
        } while(opcio != -1);
    }

    // === IMPORTAR ENQUESTA ===
    private static void importarEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        io.write("Introdueix el path del fitxer .txt d'enquesta: ");
        String path = io.readword();

        List<String> linies = readAllLines(path);
        if (linies.isEmpty()) {
            io.writeln(" Fitxer buit o inexistent.");
            return;
        }

        // Primer línia: titol;descripcio;numPreguntes
        String[] meta = linies.get(0).split(";");
        String titol = meta[0].trim();
        String descripcio = meta[1].trim();
        int numPreg = Integer.parseInt(meta[2].trim());

        List<Pregunta> preguntes = new ArrayList<>();
        int nextId = 0;

        // Línies següents: textPregunta;tipus
        for (int i = 1; i <= numPreg; i++) {
            String[] parts = linies.get(i).split(";");
            String text = parts[0].trim();
            Pregunta.Tipus tipus = Pregunta.Tipus.valueOf(parts[1].trim().toUpperCase());
            Pregunta p = new Pregunta(nextId++, text, tipus);
            preguntes.add(p);
        }

        // Creador fictici (ID=0 per simplificar)
        ctrl.importarEnquesta(titol, descripcio, 0, preguntes);
        io.writeln("Enquesta importada correctament!");
    }

    // === EXPORTAR ENQUESTA ===
    private static void exportarEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        io.write("Introdueix l'ID de l'enquesta a exportar: ");
        int id = io.readint();
        io.write("Introdueix el path on guardar (ex: sortida.txt): ");
        String path = io.readword();

        List<String> export = ctrl.exportarEnquesta(id);
        if (export == null) {
            io.writeln("Enquesta no trobada.");
            return;
        }

        writeAllLines(path, export);
        io.writeln("Enquesta exportada correctament a: " + path);
    }

    // === IMPORTAR RESPOSTES ===
    private static void importarRespostes(inout io, CtrlDomini ctrl) throws Exception {
        io.write("Introdueix l'id de l'usuari que importa les respostes: ");
        int idUsuari = io.readint();
        io.writeln("Introdueix el id de l'enquesta a la que corresponen les respostes: ");
        int idEnquesta = io.readint();
        io.write("Introdueix el path del fitxer de respostes: ");
        String path = io.readword();

        List<String> linies = readAllLines(path);

        int numPreg = Integer.parseInt(linies.get(0).trim());
        List<String> preguntes = new ArrayList<>();
        List<String> respostes = new ArrayList<>();

        // Primer la secció de preguntes
        for (int i = 1; i <= numPreg; i++) {
            preguntes.add(linies.get(i).trim());
        }

        // Ara les respostes
        for (int i = numPreg + 1; i < linies.size(); i++) {
            respostes.add(linies.get(i).trim());
        }

        ctrl.importarRespostes(idEnquesta, idUsuari, preguntes, respostes);
        io.writeln("Respostes importades correctament!");
    }

    // === EXPORTAR RESPOSTES ===
    private static void exportarRespostes(inout io, CtrlDomini ctrl) throws Exception {
        io.write("Introdueix l'ID de l'enquesta: ");
        int id = io.readint();
        io.write("Path on guardar (ex: respostes.txt): ");
        String path = io.readword();

        List<String> export = ctrl.exportarRespostes(id);
        if (export == null) {
            io.writeln("No hi ha respostes per aquesta enquesta.");
            return;
        }

        writeAllLines(path, export);
        io.writeln("Respostes exportades correctament a: " + path);
    }

    // Helpers de lectura/escriptura
    private static List<String> readAllLines(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        }
        return lines;
    }

    private static void writeAllLines(String path, List<String> lines) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (String l : lines) pw.println(l);
        }
    }

    private static void crearUsuari(inout io, CtrlDomini ctrl) throws Exception {

        io.write("Introdueix l'ID de l'usuari: ");
        int id = io.readint();
        io.write("Introdueix el nom: ");
        String nom = io.readword();
        io.write("Introdueix l'email: ");
        String email = io.readword();
        io.write("Introdueix el rol de l'usuari (ADMINISTRADOR, MODERADOR, ENQUESTADOR, ENQUESTAT): ");
        String rolStr = io.readword().toUpperCase();
        io.write("Es registrat? (true/false): ");
        boolean registrat = io.readboolean();
        Usuari usuari;
        switch (rolStr) {
            //case "ADMINISTRADOR" -> usuari = new Administrador(id, nom, true);
            //case "MODERADOR" -> usuari = new Moderador(id, nom, true);
            case "ENQUESTADOR" -> usuari = new PerfilEnquestador(id, nom, rolStr, registrat);
            case "ENQUESTAT" -> usuari = new PerfilEnquestat(id, nom, rolStr, registrat);
            default -> {
                io.writeln("Rol no vàlid.");
                return;
            }
        }
        usuari.setEmail(email);
        ctrl.getCtrlUsuari().crearPerfil(usuari);
        io.writeln("Usuari creat correctament!");
    }

    private static void consultarUsuari(inout io, CtrlDomini ctrl) throws Exception {
        io.write("Introdueix l'ID de l'usuari a consultar: ");
        int id = io.readint();
        ctrl.getCtrlUsuari().consultarPerfil(id);
    }

    private static void consultarEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        io.write("Es consulta la enquesta existent");
        ctrl.consultarEnquesta();
    }

    //Falta revisar esto
    private static void consultarRespostas(inout io, CtrlDomini ctrl) throws Exception {
        io.write("Introdueix l'ID de l'enquesta: ");
        int idEnquesta = io.readint();
        io.write("Introdueix l'ID de l'usuari: ");
        int idUsuari = io.readint();
        List<Resposta> respostes = ctrl.consultarRespostes(idEnquesta, idUsuari);
        if (respostes.isEmpty()) {
            io.writeln("No s'han trobat respostes per aquesta enquesta i usuari.");
            return;
        }
        int num = 0;
        for (Resposta r : respostes) {
            io.writeln("Pregunta ID: " + num + ", Resposta: " + r.getValorString());
            num++;
        }
    }

    private static void crearEnquesta(inout io, CtrlDomini ctrl) throws Exception {

        io.writeln("Escriu un titol per l'enquesta");
        String titol = io.readword();
        io.writeln("Escriu una descripcio per l'enquesta");
        String descripcio = io.readword();
        io.writeln("Introdueix l'ID del creador de l'enquesta");
        int idCreador = io.readint();
        io.writeln("Introdueix el nombre de preguntes que tindra l'enquesta");
        int numPreguntes = io.readint();
        List<Pregunta> preguntes = new ArrayList<>();

        for (int i = 0; i < numPreguntes; i++) {
            io.writeln("Escriu el text de la pregunta " + (i + 1));
            String textPregunta = io.readword();
            io.writeln("Escriu el tipus de la pregunta (OBERTA, TANCADA, NUMERICA, UNICA)");
            String tipusStr = io.readword().toUpperCase();
            Pregunta.Tipus tipus = null;
            try {
                tipus = Pregunta.Tipus.valueOf(tipusStr);
            } catch (IllegalArgumentException e) {
                io.writeln("Tipus de pregunta no vàlid.");
            }
            Pregunta pregunta = new Pregunta(i + 1, textPregunta, tipus);
            preguntes.add(pregunta);
        }
        ctrl.crearEnquesta(titol, descripcio, idCreador, preguntes);
        io.writeln("Enquesta creada correctament!");
    }

    private static void modificarEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("Funcionalitat de modificar enquesta no implementada encara.");
    }

    private static void esborrarEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        io.write("Introdueix l'id de l'enquesta");
        int idEnquesta = io.readint();
        ctrl.esborrarEnquesta(idEnquesta);
        io.writeln("Enquesta esborrada correctament.");
    }

    //Falta implementar esto
    private static void respondreEnquesta(inout io, CtrlDomini ctrl) throws Exception {
        io.writeln("Introdueix l'id de l'enquesta a respondre: ");
        int idEnquesta = io.readint();
        io.write("Introdueix l'id de l'usuari que respon l'enquesta: ");
        int idUsuari = io.readint();
        io.write("Introdueix les respostes de l'usuari a l'enquesta");
        List<Resposta> respostesUsuari = new ArrayList<>();
        List<Pregunta> preguntes = ctrl.getPreguntes(idEnquesta);
        List<Resposta> respostesExistents = ctrl.consultarRespostes(idEnquesta, idUsuari);

        for (Pregunta p : preguntes) {
            io.writeln("Pregunta: " + p.getText() + " (Tipus: " + p.getTipus() + ")");
            io.write("Resposta: ");
            String respostaStr = io.readword();
            Resposta resposta;
            switch (p.getTipus()) {
                case LLIURE -> resposta = new RespostaLliure(respostaStr);
                case NUMERICA -> resposta = new RespostaNumerica(Double.parseDouble(respostaStr));
                case UNICA -> resposta = new RespostaUnica(List.of(respostaStr.split(",")));
                case MULTIPLE -> resposta = new RespostaMultiple(List.of(respostaStr.split(",")));
                case ORDENADA -> resposta = new RespostaOrdenada(List.of(respostaStr.split(",")));
                default -> {
                    io.writeln("Tipus de pregunta desconegut.");
                    return;
                }
            }
            respostesUsuari.add(resposta);
        }
        ctrl.respondreEnquesta(idEnquesta, idUsuari, respostesUsuari);
        io.writeln("Enquesta respondida correctament!");
    }
}



  
  
 
 */