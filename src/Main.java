import java.io.File;


public class Main {

    //Variable per defecte per als fitxers

    public static void main(String[] args) {
        CtrlDomini ctrl = new CtrlDomini();
        inout io = new inout();
        int opcio = -1;
        Utils.Base_path = inicialitzarRutaBase();

        try {
            do {
                Utils.mostrarMenu(io);
                opcio = Utils.llegirOpcio(io);

                switch (opcio) {
                    case 0 -> io.writeln("\nSortint...");
                    case 1 -> Utils.crearEnquestaManual(io, ctrl); //prueba hecha
                    case 2 -> Utils.crearEnquestaDesDeFitxer(io, ctrl);
                    case 3 -> Utils.consultarEnquesta(io, ctrl); //prueba hecha
                    case 4 -> Utils.consultarEnquestaAmbPreguntes(io, ctrl); //prueba hecha
                    case 5 -> Utils.consultarRespostesEnquesta(io, ctrl);
                    case 6 -> Utils.respondreEnquesta(io, ctrl);
                    case 7 -> Utils.exportarEnquesta(io, ctrl);
                    case 8 -> Utils.importarRespostes(io, ctrl);
                    case 9 -> io.writeln("--");//Utils.exportarResposta(io, ctrl);
                    case 10 -> Utils.consultarPerfil(io, ctrl);
                    case 11 -> Utils.modificarEnquesta(io, ctrl); //prueba hecha
                    case 12 -> Utils.modificarResposta(io, ctrl);
                    case 13 -> Utils.esborrarEnquesta(io,ctrl); //prueba hecha
                    case 14 -> Utils.esborrarResposta(io,ctrl); //prueba hecha
                    case 15 -> Utils.clustering(io, ctrl);
                    case 16 -> ctrl.mostrarEnquestesAmbPreguntesIRespostes();
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





}

