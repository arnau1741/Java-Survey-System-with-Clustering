package prop.enquestes.persistencia;

import prop.enquestes.domini.*;

import java.io.*;
import java.util.*;

public class CtrlPersistencia {

    private static final String FILE_USUARIS = "DATA/usuaris.json";
    private static final String FILE_ENQUESTES = "DATA/enquestes.json";


    // ==========================================================
    //                      USUARIS
    // ==========================================================

    public Map<Integer, Usuari> carregarUsuaris() {
        Map<Integer, Usuari> map = new HashMap<>();

        File f = new File(FILE_USUARIS);
        if (!f.exists()) return map;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            int id = -1;
            String nom = "", pass = "", email = "", rolTxt = "";

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("\"id\"")) {
                    id = Integer.parseInt(extraerValor(line));
                } else if (line.startsWith("\"nom\"")) {
                    nom = extraerValor(line);
                } else if (line.startsWith("\"pass\"")) {
                    pass = extraerValor(line);
                } else if (line.startsWith("\"email\"")) {
                    email = extraerValor(line);
                } else if (line.startsWith("\"rol\"")) {
                    rolTxt = extraerValor(line);

                    // ***** CUANDO terminamos un usuario *****
                    UsuariState rol;
                    switch (rolTxt) {
                        case "ADMIN": rol = new AdminState(); break;
                        case "ENQUESTADOR": rol = new EnquestadorState(); break;
                        default: rol = new EnquestatState(); break;
                    }

                    map.put(id, new Usuari(id, nom, pass, email, rol));
                }
            }

        } catch (Exception e) { e.printStackTrace(); }

        return map;
    }


    public void guardarUsuaris(Map<Integer, Usuari> usuaris) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_USUARIS))) {

            pw.println("[");

            int count = 0;
            for (Usuari u : usuaris.values()) {
                UsuariState us = u.getRol();
                String rol;
                if(us instanceof AdminState) rol = "ADMIN";
                else if (us instanceof EnquestadorState) rol = "ENQUESTADOR";
                else if(us instanceof  EnquestatState) rol = "ENQUESTAT";
                else rol = "MODERADOR";
                pw.println("  {");
                pw.println("    \"id\": " + u.getId() + ",");
                pw.println("    \"nom\": \"" + u.getUsuari() + "\",");
                pw.println("    \"pass\": \"" + u.getContrasenya() + "\",");
                pw.println("    \"email\": \"" + u.getEmail() + "\",");
                pw.println("    \"rol\": \"" + rol + "\"");
                pw.print("  }");

                if (++count < usuaris.size()) pw.println(",");
                else pw.println();
            }

            pw.println("]");

        } catch (Exception e) { e.printStackTrace(); }
    }



    // ==========================================================
    //                      ENQUESTES
    // ==========================================================

    public Map<Integer, Enquesta> carregarEnquestes() {
        Map<Integer, Enquesta> map = new HashMap<>();

        File f = new File(FILE_ENQUESTES);
        if (!f.exists()) return map;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;

            int id = -1;
            String titol = "", desc = "";
            int creador = -1;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("\"id\"")) {
                    id = Integer.parseInt(extraerValor(line));
                } else if (line.startsWith("\"titol\"")) {
                    titol = extraerValor(line);
                } else if (line.startsWith("\"descripcio\"")) {
                    desc = extraerValor(line);
                } else if (line.startsWith("\"creador\"")) {
                    creador = Integer.parseInt(extraerValor(line));

                    // ***** Cuando terminamos una enquesta *****
                    Enquesta e = new Enquesta(id, titol, desc, creador, new ArrayList<>());
                    map.put(id, e);
                }
            }

        } catch (Exception e) { e.printStackTrace(); }

        return map;
    }


    public void guardarEnquestes(Map<Integer, Enquesta> enquestes) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_ENQUESTES))) {

            pw.println("[");
            int count = 0;

            for (Enquesta e : enquestes.values()) {
                pw.println("  {");
                pw.println("    \"id\": " + e.getId() + ",");
                pw.println("    \"titol\": \"" + e.getTitol() + "\",");
                pw.println("    \"descripcio\": \"" + e.getDescripcio() + "\",");
                pw.println("    \"creador\": " + e.getCreador());
                pw.print("  }");

                if (++count < enquestes.size()) pw.println(",");
                else pw.println();
            }

            pw.println("]");

        } catch (Exception e) { e.printStackTrace(); }
    }



    // ==========================================================
    //                 FUNCIÓN AUXILIAR
    // ==========================================================

    private String extraerValor(String line) {
        int idx = line.indexOf(":");
        String val = line.substring(idx + 1).trim();

        val = val.replace(",", "");
        val = val.replace("\"", "");

        return val;
    }


    public void guardarFitxerText(String path, List<String> contingut) throws Exception {
        try (FileWriter fw = new FileWriter(path)) {
            for (String line : contingut) fw.write(line + "\n");
        }
    }
}
