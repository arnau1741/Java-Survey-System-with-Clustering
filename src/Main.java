// Main.java
import java.io.IOException;
import java.util.*;

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
                    //Importar enquesta
                    case 1 -> {
                        io.writeln("Introdueix el path de l’enquesta a importar: ");
                        String path = io.readword();
                        String resultat = ctrl.importarEnquesta(path);
                        //Escriure si el ficher es correcta
                        io.writeln(resultat);
                    }
                    //Exportar enquesta
                    case 2 -> {
                        io.writeln("Introdueix l’ID de l’enquesta a exportar: ");
                        int id = io.readint();
                        //Aquesta part es posible que no el necesitem si definim el path directament
                        io.writeln("Introduex la ruta on guardar l’enquesta: ");
                        String path = io.readword();
                        //S'haura de veure que exportem exactamen(Lista)
                        String resultat = ctrl.exportarEnquesta(path, id);
                        io.writeln(resultat);
                    }
                    //Importar respostes
                    case 3 -> {
                        io.writeln("Introdueix el path del fitxer de respostes a importar: ");
                        String path = io.readword();
                        String resultat = ctrl.importarRespostes(path);
                        io.writeln(resultat);
                    }
                    // Exportar respostes
                    case 4 -> {
                        io.writeln("Introdueix l'id de l'enquesta per exportar respostes: ");
                        int id = io.readint();
                        String path = io.readword();
                        String resultat = ctrl.exportarRespostes(path, id);
                        io.writeln(resultat);
                    }
                    //Sortir
                    case 0 -> io.writeln("Sortint...");
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


}