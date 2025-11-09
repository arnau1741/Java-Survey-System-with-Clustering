// import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

/*
 * Classe Enquesta
 * Es una Lista de preguntes i una Matriu de respostes 
 * Las respuestas de los usuarios no registrados es -1 y 
 * las filas que usa en la matriz de respuestas
 * se guardan en una lista aparte (noRegistratAnswers)
 * Las filas de usuarios registrados se guardan en un mapa
 * userToAnswerId
 */



public class Enquesta {
    // Representa una enquesta concreta: id, titol, descripcio, creador, preguntes, respostes, participants
    private Integer id;
    private String titol;
    private String descripcio;
    private Integer idCreador;


    public List<List<Resposta>> respostes; // Matriu de respostes per a cada pregunta
    public List<Pregunta> preguntes;

    //Par usuari <> Fila Matriu que li correspon
    private Map<Integer, Integer> userToAnswerId; // Map d'usuari a fila de respostes

    private List<Integer> noRegistratAnswers; // Llista de files de respostes per usuaris no registrats

    // posem les dates com atributs, creem una classe Data i es relaciona?
    // private LocalDateTime dataCreacio;
    // private LocalDateTime dataFinalitzacio;

    // Constructora
    public Enquesta(Integer id, String titol, String descripcio, Integer idCreador, List<Pregunta> preguntes) {
        this.id = id;
        this.titol = titol;
        this.descripcio = descripcio;
        this.idCreador = idCreador;

        this.preguntes = preguntes;
        this.respostes = new ArrayList<>();
        this.userToAnswerId = new HashMap<>();
        this.noRegistratAnswers = new ArrayList<>();
        // this.dataCreacio = LocalDateTime.now();
        //this.visible = false;
    }


    private List<Resposta> stringARespostes (List<String> respostesStr) throws IllegalArgumentException {
        List<Resposta> respostesObj = new ArrayList<>();
        for (String r : respostesStr) {
            int index = respostesStr.indexOf(r);
            int tipusPregunta = preguntes.get(index).getTipus();
            if (tipusPregunta == 0) {// NUMERICA
                try {
                    RespostaNumerica respostaNumerica = new RespostaNumerica(Double.parseDouble(r));
                    respostesObj.add(respostaNumerica);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Resposta numèrica invàlida: " + r);
                }
            } else if (tipusPregunta == 1) { // LLIURE
                RespostaLliure respostaLliure = new RespostaLliure(r);
                respostesObj.add(respostaLliure);
            } else if (tipusPregunta == 2) { // UNICA
                
                int numOpcions = preguntes.get(index).getNumOpcions();
                RespostaUnica respostaUnica = new RespostaUnica(numOpcions);
                respostaUnica.setResposta(Integer.parseInt(r));
                respostesObj.add(respostaUnica);
            } else if (tipusPregunta == 3) {// MULTIPLE
                int numOpcions = preguntes.get(index).getNumOpcions();
                RespostaMultiple respostaMultiple = new RespostaMultiple(numOpcions);
                // Convertir String a
                List<Integer> seleccionades = new ArrayList<>();
                String[] parts = r.split(","); // Suponemos que las opciones están separadas por comas
                for (String part : parts) {
                    seleccionades.add(Integer.parseInt(part.trim()));
                }
                respostaMultiple.selecciona(seleccionades);
            } else if (tipusPregunta == 4) { // ORDENADA
                int numOpcions = preguntes.get(index).getNumOpcions();
                RespostaOrdenada respostaOrdenada = new RespostaOrdenada(numOpcions);
                respostaOrdenada.setResposta(Integer.parseInt(r));
                respostesObj.add(respostaOrdenada);
            } else {
                throw new IllegalArgumentException("Tipus de pregunta desconegut: " + tipusPregunta);
            }
        }
        return respostesObj;
    }

    public Integer afegeixResposta(int idUsuari, List<String> respostes){
        List<Resposta> respostesObj = stringARespostes(respostes);
        Integer filaMatriu = userToAnswerId.size() + noRegistratAnswers.size();
        this.respostes.add(respostesObj);
        if (idUsuari >= 0) userToAnswerId.put(idUsuari, filaMatriu);
        else if (idUsuari == -1) noRegistratAnswers.add(filaMatriu);
        else throw new IllegalArgumentException("L'id de l'usuari no pot ser menor que -1.");
        return filaMatriu;


    }

    public boolean participa(int id) {
        if (id < 0) throw new IllegalArgumentException("L'id de l'usuari no pot ser negatiu.");

        for (Integer userId : userToAnswerId.keySet()) {
            if (userId.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public Integer obtenFilaIdUsuari(int idUsuari) {
        return userToAnswerId.get(idUsuari);
    }



    public List<String> getPreguntes(){
        List<String> textsPreguntes = new ArrayList<>();
        for (Pregunta p : preguntes) {
            textsPreguntes.add(p.getText());
            int tipus = p.getTipus(); //NUMERICA, LLIURE, UNICA, MULTIPLE, ORDENADA
            if (tipus == 2) { //UNICA
                // Afegir les opcions de la pregunta UNICA
                // Suposant que la classe Pregunta té un mètode getOpcions()
                List<String> opcions = p.getOpcions();
                String opcionsStr = String.join(", ", opcions);
                textsPreguntes.add("-" + opcionsStr);
            }
            else if (tipus == 3) { //MULTIPLE
                // Afegir les opcions de la pregunta MULTIPLE
                // Suposant que la classe Pregunta té un mètode getOpcions()
                List<String> opcions = p.getOpcions();
                String opcionsStr = String.join(", ", opcions);
                textsPreguntes.add("-" + opcionsStr);
            }
            else if(tipus == 4){ //ORDENADA
                // Afegir les opcions de la pregunta ORDENADA
                // Suposant que la classe Pregunta té un mètode getOpcions()
                List<String> opcions = p.getOpcions();
                String opcionsStr = String.join(", ", opcions);
                textsPreguntes.add("-" + opcionsStr);
            }
        }
        return textsPreguntes;

    }

    public List<Resposta> getRespostesUsuari(Integer filaMatriu) {
        return Collections.unmodifiableList(respostes.get(filaMatriu));
    }

    public void setResposta(int idUsuari, List<Resposta> respostesUsuari){
        Integer filaMatriu = userToAnswerId.size();
        respostes.add(respostesUsuari);
        if (idUsuari >= 0) userToAnswerId.put(idUsuari, filaMatriu);
        else if (idUsuari == -1) noRegistratAnswers.add(filaMatriu);
        else throw new IllegalArgumentException("L'id de l'usuari no pot ser menor que -1.");
    }

    // Getters
    public Integer getId() { return id;}
    public String getTitol() { return titol;}
    public String getDescripcio() { return descripcio;}
    public Integer getCreador() { return idCreador;}

    // Setters
    public void setTitol(String titol) { this.titol = titol; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }
    public void setCreador(Integer idCreador) { this.idCreador = idCreador; }
    public void setId(int id) { this.id = id; }
    public void setPreguntes(List<Pregunta> preguntes) { this.preguntes = preguntes; }

    // public LocalDateTime getDataCreacio() { return dataCreacio; }
    // public LocalDateTime getDataFinalitzacio() { return dataFinalitzacio; }

    public List<Integer> getParticipants() {
        return new ArrayList<>(userToAnswerId.keySet());
    }


    public static Integer esNatural(String r) {
        try {
            Integer res = Integer.parseInt(r);  // Intenta convertir el String a un entero
            if (res >= 0) return res;  // Si no lanza una excepción, es un entero válido
            else return -1;
        } catch (NumberFormatException e) {
            return -1;  // Si lanza una excepción, no es un número entero válido
        }
    }
}