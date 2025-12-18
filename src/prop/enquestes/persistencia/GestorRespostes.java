package prop.enquestes.persistencia;

import prop.enquestes.domini.*;
import java.io.*;
import java.util.*;

public class GestorRespostes {
    private static final String DIRECTORY = "datos/respostes/";

    /**
     * Guarda les respostes d'una pregunta d'una enquesta en un fitxer JSON.
     * @param idEnquesta
     * @param indexPregunta
     * @param respostes
     * @param tipusPregunta
     * @param opcions
     */
    public void guardarRespostes(int idEnquesta, int indexPregunta, Map<Integer, Resposta> respostes, int tipusPregunta,
            List<String> opcions) {
        File dir = new File(DIRECTORY);
        if (!dir.exists())
            dir.mkdirs();

        Map<String, Object> root = new LinkedHashMap<>();

        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<Integer, Resposta> entry : respostes.entrySet()) {
            Map<String, Object> rMap = new LinkedHashMap<>();
            rMap.put("userId", entry.getKey());
            Resposta r = entry.getValue();

            if (r instanceof RespostaNumerica) {
                rMap.put("valor", ((RespostaNumerica) r).getValor());
            } else if (r instanceof RespostaLliure) {
                rMap.put("valor", ((RespostaLliure) r).getResposta());
            } else if (r instanceof RespostaUnica) {
                rMap.put("valor", ((RespostaUnica) r).getResposta()); // Guarda index
            } else if (r instanceof RespostaMultiple) {
                rMap.put("valor", ((RespostaMultiple) r).getRespostes()); // Guarda List<Integer>
            } else if (r instanceof RespostaOrdenada) {
                rMap.put("valor", ((RespostaOrdenada) r).getResposta()); // Guarda index
            }
            list.add(rMap);
        }
        root.put("respostes", list);

        String json = JsonUtil.toJson(root);
        writeFile(DIRECTORY + "enquesta_" + idEnquesta + "_pregunta_" + indexPregunta + "_respostes.json", json);
    }

    /**
     * Carrega les respostes d'una pregunta d'una enquesta des d'un fitxer JSON.
     * @param idEnquesta
     * @param indexPregunta
     * @return Map amb les respostes en format raw (Object)
     */
    public Map<Integer, Object> carregarRespostesRaw(int idEnquesta, int indexPregunta) {
        Map<Integer, Object> result = new HashMap<>();
        File f = new File(DIRECTORY + "enquesta_" + idEnquesta + "_pregunta_" + indexPregunta + "_respostes.json");
        if (!f.exists())
            return result;

        String json = readFile(f);
        if (json == null)
            return result;

        Map<String, Object> root = (Map<String, Object>) JsonUtil.parse(json);
        List<Object> list = (List<Object>) root.get("respostes");
        if (list == null)
            return result;

        for (Object o : list) {
            Map<String, Object> rMap = (Map<String, Object>) o;
            int userId = ((Number) rMap.get("userId")).intValue();
            Object val = rMap.get("valor");

            result.put(userId, val);
        }
        return result;
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
     * @return contingut del fitxer com a String
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
