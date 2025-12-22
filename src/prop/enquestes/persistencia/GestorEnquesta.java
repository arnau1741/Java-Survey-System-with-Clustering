package prop.enquestes.persistencia;

import prop.enquestes.domini.*;
import java.io.*;
import java.util.*;

public class GestorEnquesta {
    private static final String DIRECTORY = "datos/enquesta/";
    private GestorPregunta gestorPregunta;
    private GestorRespostes gestorRespostes;

    /**
     * Constructor de la classe GestorEnquesta de la capa de persistencia.
     */
    public GestorEnquesta() {
        this.gestorPregunta = new GestorPregunta();
        this.gestorRespostes = new GestorRespostes();
    }

    /**
     * Guarda les enquestes al sistema de fitxers en format JSON.
     * @param enquestes
     */
    public void guardarEnquestes(Map<Integer, Enquesta> enquestes) {
        File dir = new File(DIRECTORY);
        if (!dir.exists())
            dir.mkdirs();

        for (Enquesta enq : enquestes.values()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", enq.getId());
            map.put("titol", enq.getTitol());
            map.put("descripcio", enq.getDescripcio());
            map.put("idCreador", enq.getCreador());
            map.put("numPreguntes", enq.getNumPreguntes());

            String json = JsonUtil.toJson(map);
            writeFile(DIRECTORY + "enquesta_" + enq.getId() + ".json", json);

            List<Pregunta> preguntes = enq.getPreguntesObj();
            for (int i = 0; i < preguntes.size(); i++) {
                Pregunta p = preguntes.get(i);
                gestorPregunta.guardarPregunta(enq.getId(), i, p);
                gestorRespostes.guardarRespostes(enq.getId(), i, p.getRespostes(), p.getTipus(), p.getOpcions());
            }
        }
    }

    /**
     * Carrega les enquestes des del sistema de fitxers en format JSON.
     * @return Map&lt;Integer, Enquesta&gt;
     */
    public Map<Integer, Enquesta> carregarEnquestes() {
        Map<Integer, Enquesta> enquestes = new HashMap<>();
        File dir = new File(DIRECTORY);
        if (!dir.exists())
            return enquestes;

        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null)
            return enquestes;

        for (File f : files) {
            String json = readFile(f);
            if (json == null)
                continue;

            Map<String, Object> map = (Map<String, Object>) JsonUtil.parse(json);
            if (map == null)
                continue;

            int id = ((Number) map.get("id")).intValue();
            String titol = (String) map.get("titol");
            String descripcio = (String) map.get("descripcio");
            int idCreador = ((Number) map.get("idCreador")).intValue();
            int numPreguntes = ((Number) map.get("numPreguntes")).intValue();

            List<Pregunta> preguntes = new ArrayList<>();
            for (int i = 0; i < numPreguntes; i++) {
                Pregunta p = gestorPregunta.carregarPregunta(id, i);
                if (p != null) {
                    preguntes.add(p);
                    // Load answers
                    Map<Integer, Object> rawRespostes = gestorRespostes.carregarRespostesRaw(id, i);

                    for (Map.Entry<Integer, Object> entry : rawRespostes.entrySet()) {
                        int userId = entry.getKey();
                        Object val = entry.getValue();

                        Resposta r = crearResposta(p.getTipus(), val);
                        if (r != null) {
                            try {
                                p.addResposta(r, userId);
                            } catch (Exception e) {
                                // ignora
                            }
                        }
                    }
                }
            }

            if (!preguntes.isEmpty()) {
                Enquesta enq = new Enquesta(id, titol, descripcio, idCreador, preguntes);
                enquestes.put(id, enq);
            }
        }
        return enquestes;
    }

    /**
     * Crea una resposta a partir del tipus i el valor.
     * @param tipus
     * @param val
     * @return Resposta
     */
    private Resposta crearResposta(int tipus, Object val) {
        //if (val == null)
        //    return null;

        // 0: NUMERICA, 1: UNICA, 2: ORDENADA, 3: MULTIPLE, 4: LLIURE
        try {
            if (tipus == 0) { // NUMERICA
                if (val instanceof Number || val == null) {
                    return new RespostaNumerica(val == null ? null : ((Number) val).doubleValue());
                }
            } else if (tipus == 1) { // UNICA
                if (val instanceof Number) {
                    RespostaUnica r = new RespostaUnica();
                    r.setResposta(((Number) val).intValue());
                    return r;
                }
            } else if (tipus == 2) { // ORDENADA
                if (val instanceof Number) {
                    RespostaOrdenada r = new RespostaOrdenada();
                    r.setResposta(((Number) val).intValue());
                    return r;
                }
            } else if (tipus == 3) { // MULTIPLE
                if (val instanceof List) {
                    List<?> list = (List<?>) val;
                    List<Integer> ints = new ArrayList<>();
                    for (Object o : list) {
                        if (o instanceof Number)
                            ints.add(((Number) o).intValue());
                    }
                    RespostaMultiple r = new RespostaMultiple();
                    r.selecciona(ints);
                    return r;
                }
            } else if (tipus == 4) { // LLIURE
                if (val instanceof String) {
                    return new RespostaLliure((String) val);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
     * @return String
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
