package prop.enquestes.persistencia;

import prop.enquestes.controladors.CtrlDomini;
import prop.enquestes.domini.*;
import prop.enquestes.excepcions.InvalidFormatEnquesta;
import prop.enquestes.excepcions.UsuariNoValid;

import java.util.*;
import java.io.File;

public class TestPersistenciaMain {

    /**
     * Elimina el directori "datos" i tot el seu contingut per assegurar una
     */
    private static void cleanDatos() {
        deleteDir(new File("datos"));
    }

    /**
     * Elimina un directori i tot el seu contingut de manera recursiva.
     */
    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    /**
     * Programa principal per provar la persistència de dades.
     */
    public static void main(String[] args) throws UsuariNoValid, InvalidFormatEnquesta {
        cleanDatos();
        CtrlDomini ctrl1 = new CtrlDomini();

        int idAdmin = ctrl1.iniciarSessio("admin1111", "Pass1234");
        //int idAdmin = ctrl1.crearUsuariAdmin("admin1111", "Pass1234", "admin@test.com");
        int idUser = ctrl1.crearUsuariEnquestat("user1", "Pass1234", "user@test.com");

        List<String> preguntesInput = new ArrayList<>();
        preguntesInput.add("4");
        preguntesInput.add("Pregunta de prueba?");

        ctrl1.crearEnquesta("Encuesta Test", "Desc Test", idAdmin, preguntesInput);

        int idEnquesta = 0; // asumimos que será 0

        List<String> respostes = List.of("Respuesta de prueba");

        try {
            ctrl1.respondreEnquesta(idEnquesta, idUser, respostes);
        } catch (Exception e) {
        }

        ctrl1.guardarDades();

        CtrlDomini ctrl2 = new CtrlDomini();

        boolean userExists = ctrl2.getCtrlDominiMantUsuari().existeixUsuari("user1");

        boolean surveyExists = false;
        try {
            List<String> info = ctrl2.consultarEnquesta(idEnquesta);
            surveyExists = true;
        } catch (Exception e) {
        }

        if (surveyExists) {
            try {
                List<String> respostesLoaded = ctrl2.consultarRespostesEnquesta(idEnquesta);
                respostesLoaded.forEach(r -> System.out.println("  - " + r));

                boolean ok = respostesLoaded.stream().anyMatch(s -> s.contains("Respuesta de prueba"));
            } catch (Exception e) {
            }
        }
    }
}