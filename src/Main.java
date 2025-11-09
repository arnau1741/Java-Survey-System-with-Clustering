// Main.java
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

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

                    default -> io.writeln("Opció no vàlida.");
                }
            } catch (IOException e) {
                //io.writeln("Error d’E/S: " + e.getMessage());
            }
        } while(opcio != -1);

        //Import
        //Recibir un fichero (Path) -> Abrimos fichero
        //sc = new Scanner(System.in);
        //System.out.print("Introdueix la ruta del fitxer d'importació: ");
        //String rutaImport = sc.nextLine();

        //Llamar a funcion para tratar
        //Obtener datos y llamar a controlador de dominio

        //Export
        //Llamo al controlador de dominio para obtener datos
        //Abrir pipe y guardar datos en fichero
        //Guardarlo en la ruta establecida

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
}


