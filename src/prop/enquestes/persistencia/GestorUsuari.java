package prop.enquestes.persistencia;

import prop.enquestes.domini.*;

import java.io.*;
import java.util.*;

public class GestorUsuari {
    private static final String DIRECTORY = "datos" + File.separator + "usuari" + File.separator;

    /**
     * Guarda els usuaris en fitxers JSON.
     * @param usuaris
     */
    public void guardarUsuaris(Map<Integer, Usuari> usuaris) {
        File dir = new File(DIRECTORY);
        if (!dir.exists())
            dir.mkdirs();

        for (Usuari u : usuaris.values()) {
            if (u.getId() < 0)
                continue;

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", u.getId());
            map.put("nomUsuari", u.getUsuari());
            map.put("password", u.getContrasenya());
            map.put("email", u.getEmail());
            String roleType = "ENQUESTAT";
            if (u.esAdmin()){
                roleType = "ADMIN";

                AdminState admin = (AdminState) u.getRol();
                Map<Integer, Enquesta> enquestesAdministrades = admin.getEnquestesAdministrades();
                List<Integer> ids = new ArrayList<>(enquestesAdministrades.keySet());
                map.put("enquestesAdministrades", ids);

                Map<Integer, Enquesta> enquestesRealitzades = admin.getEnquestesRealitzades();
                List<Integer> idsRealitzades = new ArrayList<>(enquestesRealitzades.keySet());
                map.put("enquestesRealitzades", idsRealitzades);
            }
            else if (u.esEnquestador()){
                roleType = "ENQUESTADOR";

                EnquestadorState enquestador = (EnquestadorState) u.getRol();
                Map<Integer, Enquesta> enquestesAssignades = enquestador.getEnquestesAssignades();
                List<Integer> ids = new ArrayList<>(enquestesAssignades.keySet());
                map.put("enquestesAssignades", ids);
            }
            else if(u.esModerador())
                roleType = "MODERADOR";

            map.put("rol", roleType);

            String json = JsonUtil.toJson(map);
            writeFile(DIRECTORY + "usuari_" + u.getId() + ".json", json);
        }
    }

    /**
     * Carrega els usuaris des de fitxers JSON.
     * @return Map d'usuaris carregats.
     */
    public Map<Integer, Usuari> carregarUsuaris(Map<Integer, Enquesta> totesEnquestes) {
        Map<Integer, Usuari> usuaris = new HashMap<>();
        File dir = new File(DIRECTORY);
        if (!dir.exists())
            return usuaris;

        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null)
            return usuaris;

        for (File f : files) {
            String json = readFile(f);
            if (json == null)
                continue;

            Map<String, Object> map = (Map<String, Object>) JsonUtil.parse(json);
            if (map == null)
                continue;

            int id = ((Number) map.get("id")).intValue();
            String nom = (String) map.get("nomUsuari");
            String pass = (String) map.get("password");
            String email = (String) map.get("email");
            String rolStr = (String) map.get("rol");

            UsuariState rolState;
            switch (rolStr) {
                case "ADMIN":
                    List<Integer> idsAdministrades = new ArrayList<>();
                    idsAdministrades = (List<Integer>) map.get("enquestesAdministrades");
                    List<Integer> idsRealitzades = new ArrayList<>();
                    idsRealitzades = (List<Integer>) map.get("enquestesRealitzades");
                    Map<Integer, Enquesta> enquestesAdministrades = new HashMap<>();
                    for (Integer idEnquesta : idsAdministrades) {
                        Enquesta e = totesEnquestes.get(idEnquesta);
                        if (e != null) {
                            enquestesAdministrades.put(idEnquesta, e);
                        }
                    }
                    Map<Integer, Enquesta> enquestesRealitzades = new HashMap<>();
                    for (Integer idEnquesta : idsRealitzades) {
                        Enquesta e = totesEnquestes.get(idEnquesta);
                        if (e != null) {
                            enquestesRealitzades.put(idEnquesta, e);
                        }
                    }
                    rolState = new AdminState(enquestesAdministrades, enquestesRealitzades);
                    break;
                case "ENQUESTADOR":
                    List<Integer> idsAssignades = new ArrayList<>();
                    idsAssignades = (List<Integer>) map.get("enquestesAssignades");
                    Map<Integer, Enquesta> enquestesAssignades = new HashMap<>();
                    for (Integer idEnquesta : idsAssignades) {
                        Enquesta e = totesEnquestes.get(idEnquesta);
                        if (e != null) {
                            enquestesAssignades.put(idEnquesta, e);
                        }
                    }
                    rolState = new EnquestadorState(enquestesAssignades);
                    break;
                case "MODERADOR":
                    rolState = new ModeradorState();
                    break;
                default:
                    List<Integer> idsRealitzadesEnquestat = new ArrayList<>();
                    idsRealitzades = (List<Integer>) map.get("enquestesRealitzades");
                    Map<Integer, Enquesta> enquestesRealitzadesEnquestat = new HashMap<>();
                    for (Integer idEnquesta : idsRealitzades) {
                        Enquesta e = totesEnquestes.get(idEnquesta);
                        if (e != null) {
                            enquestesRealitzadesEnquestat.put(idEnquesta, e);
                        }
                    }
                    rolState = new EnquestatState(enquestesRealitzadesEnquestat);
                    break;
            }

            Usuari u = new Usuari(id, nom, pass, email, rolState);
            usuaris.put(id, u);
        }
        return usuaris;
    }

    /**
     * Escriu el contingut en un fitxer.
     * @param path
     * @param content
     */
    private void writeFile(String path, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Llegeix el contingut d'un fitxer.
     * @param file
     * @return Contingut del fitxer.
     */
    private String readFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
