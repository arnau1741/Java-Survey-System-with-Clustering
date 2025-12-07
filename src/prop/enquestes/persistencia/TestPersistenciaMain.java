package prop.enquestes.persistencia;

import prop.enquestes.controladors.CtrlDomini;
import prop.enquestes.domini.*;

import java.util.*;
import java.io.File;

public class TestPersistenciaMain {

    private static void cleanDatos() {
        deleteDir(new File("datos"));
    }

    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    public static void main(String[] args) {
        System.out.println("== TEST DE PERSISTENCIA ==");

        cleanDatos();

        System.out.println("\n--- EJECUCIÓN 1 ---");
        CtrlDomini ctrl1 = new CtrlDomini();

        System.out.println("Creando usuarios...");
        int idAdmin = ctrl1.crearUsuariAdmin("admin1111", "Pass1234", "admin@test.com");
        int idUser = ctrl1.crearUsuariEnquestat("user1", "Pass1234", "user@test.com");

        System.out.println("Creando encuesta...");
        List<String> preguntesInput = new ArrayList<>();
        preguntesInput.add("4");
        preguntesInput.add("Pregunta de prueba?");

        int resCrear = ctrl1.crearEnquesta("Encuesta Test", "Desc Test", idAdmin, preguntesInput);
        System.out.println("Resultado creación: " + resCrear);

        int idEnquesta = 0; // asumimos que será 0

        System.out.println("Añadiendo respuesta...");
        List<String> respostes = List.of("Respuesta de prueba");

        try {
            ctrl1.respondreEnquesta(idEnquesta, idUser, respostes);
            System.out.println("Respuesta registrada.");
        } catch (Exception e) {
            System.out.println("Error al responder: " + e.getMessage());
        }

        System.out.println("Guardando datos...");
        ctrl1.guardarDades();

        System.out.println("Ejecución 1 terminada.\n");

        System.out.println("--- EJECUCIÓN 2 ---");
        CtrlDomini ctrl2 = new CtrlDomini();

        boolean userExists = ctrl2.getCtrlDominiMantUsuari().existeixUsuari("user1");
        System.out.println("Usuario cargado? " + userExists);

        boolean surveyExists = false;
        try {
            List<String> info = ctrl2.consultarEnquesta(idEnquesta);
            System.out.println("Encuesta cargada: " + info);
            surveyExists = true;
        } catch (Exception e) {
            System.out.println("No se pudo cargar la encuesta.");
        }

        if (surveyExists) {
            try {
                List<String> respostesLoaded = ctrl2.consultarRespostesEnquesta(idEnquesta);
                System.out.println("Respuestas cargadas:");
                respostesLoaded.forEach(r -> System.out.println("  - " + r));

                boolean ok = respostesLoaded.stream().anyMatch(s -> s.contains("Respuesta de prueba"));
                System.out
                        .println(ok ? "Respuestas persistidas correctamente." : "No se han encontrado las respuestas.");
            } catch (Exception e) {
                System.out.println("Error al consultar respuestas.");
            }
        }
    }
}