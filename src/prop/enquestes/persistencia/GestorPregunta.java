package prop.enquestes.persistencia;

import prop.enquestes.domini.*;

import java.io.*;
import java.util.*;

public class GestorPregunta {
    private static final String DIRECTORY = "datos/pregunta/";

    /**
     * Guarda la pregunta p de l'enquesta idEnquesta amb index indexPregunta.
     * @param idEnquesta
     * @param indexPregunta
     * @param p
     */
    public void guardarPregunta(int idEnquesta, int indexPregunta, Pregunta p) {
        File dir = new File(DIRECTORY);
        if (!dir.exists())
            dir.mkdirs();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("text", p.getText());
        map.put("tipus", p.getTipus());

        if (p.getTipus() != 0 && p.getTipus() != 4) { // No NUMERICA o LLIURE
            map.put("opcions", p.getOpcions());
        }

        String json = JsonUtil.toJson(map);
        writeFile(DIRECTORY + "enquesta_" + idEnquesta + "_pregunta_" + indexPregunta + ".json", json);
    }

    /**
     * Carrega la pregunta de l'enquesta idEnquesta amb index indexPregunta.
     * @param idEnquesta
     * @param indexPregunta
     * @return Pregunta carregada o null si no existeix.
     */
    public Pregunta carregarPregunta(int idEnquesta, int indexPregunta) {
        File f = new File(DIRECTORY + "enquesta_" + idEnquesta + "_pregunta_" + indexPregunta + ".json");
        if (!f.exists())
            return null;

        String json = readFile(f);
        if (json == null)
            return null;

        Map<String, Object> map = (Map<String, Object>) JsonUtil.parse(json);
        String text = (String) map.get("text");
        int tipus = ((Number) map.get("tipus")).intValue();
        List<String> opcions = new ArrayList<>();

        if (map.containsKey("opcions")) {
            List<Object> rawList = (List<Object>) map.get("opcions");
            for (Object o : rawList)
                opcions.add((String) o);
        }

        return new Pregunta(text, tipus, opcions);
    }

    /**
     * Escriu el contingut a un fitxer.
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
     * @return Contingut del fitxer o null en cas d'error.
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
