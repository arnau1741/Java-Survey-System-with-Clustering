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
    private List<Pregunta> preguntes;

    // posem les dates com atributs, creem una classe Data i es relaciona?
    // private LocalDateTime dataCreacio;
    // private LocalDateTime dataFinalitzacio;

    // Constructora

    /**
     * Funcio constructora de la classe Enquesta
     * @param id de l'enquesta
     * @param titol de l'enquesta
     * @param descripcio de l'enquesta
     * @param idCreador de l'usuari
     * @param preguntes de l'enquesta
     */
    public Enquesta(Integer id, String titol, String descripcio, Integer idCreador, List<Pregunta> preguntes) {
        this.id = id;
        this.titol = titol;
        this.descripcio = descripcio;
        this.idCreador = idCreador;
        this.preguntes = preguntes;
    }

    // converteix les respostes de String a objectes Resposta

    /**
     * Converteix una llista de respostes en format String a una llista d'objectes Resposta
     * @param respostesStr llista de respostes en format String
     * @return llista d'objectes Resposta
     * @throws IllegalArgumentException si alguna resposta no és vàlida segons el tipus de pregunta
     */
    public List<Resposta> stringARespostes (List<String> respostesStr) throws IllegalArgumentException {
        List<Resposta> respostesObj = new ArrayList<>();
        // for (String r : respostesStr) {
        for (int index = 0; index < respostesStr.size(); index++) {
            String r = respostesStr.get(index);
            // int index = respostesStr.indexOf(r);
            int tipusPregunta = preguntes.get(index).getTipus();

            if (tipusPregunta == 0) {// NUMERICA
                try {
                    RespostaNumerica respostaNumerica = new RespostaNumerica(Double.parseDouble(r));
                    respostesObj.add(respostaNumerica);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Resposta numèrica invàlida: " + r);
                }
            } else if (tipusPregunta == 1) { // UNICA
                int numOpcions = preguntes.get(index).getNumOpcions();
                RespostaUnica respostaUnica = new RespostaUnica(numOpcions);
                respostaUnica.setResposta(Integer.parseInt(r));
                respostesObj.add(respostaUnica);
            } else if (tipusPregunta == 2) { // ORDENADA
                int numOpcions = preguntes.get(index).getNumOpcions();
                RespostaOrdenada respostaOrdenada = new RespostaOrdenada(numOpcions);
                respostaOrdenada.setResposta(Integer.parseInt(r));
                respostesObj.add(respostaOrdenada);
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
                respostesObj.add(respostaMultiple);
            } else if (tipusPregunta == 4) { // LLIURE
                RespostaLliure respostaLliure = new RespostaLliure(r);
                respostesObj.add(respostaLliure);
            } else {
                throw new IllegalArgumentException("Tipus de pregunta desconegut: " + tipusPregunta);
            }
        }
        return respostesObj;
    }

    // retorna els textos de les preguntes

    /**
     * Retorna una llista de textos de les preguntes de l'enquesta
     * @return llista de textos de les preguntes
     */
    public List<String> getPreguntes(){
        List<String> tipusToString = new ArrayList<>();
        tipusToString.add("NUMÈRICA");
        tipusToString.add("ÚNICA");
        tipusToString.add("MÚLTIPLE");
        tipusToString.add("ORDENADA");
        tipusToString.add("LLIURE");

        List<String> textsPreguntes = new ArrayList<>();
        for (Pregunta p : preguntes) {
            int tipus = p.getTipus(); // NUMERICA, UNICA, MULTIPLE, ORDENADA, LLIURE
            //textsPreguntes.add(Integer.toString(tipus));
            textsPreguntes.add(tipusToString.get(tipus));
            textsPreguntes.add(p.getText());
            if (tipus == 1) { // UNICA
                // Afegir les opcions de la pregunta UNICA
                List<String> opcions = p.getOpcions();
                int nombreOpcions = opcions.size();
                textsPreguntes.add(Integer.toString(nombreOpcions));
                textsPreguntes.addAll(opcions);
            }
            else if (tipus == 2) { // MULTIPLE
                // Afegir les opcions de la pregunta MULTIPLE
                List<String> opcions = p.getOpcions();
                int nombreOpcions = opcions.size();
                textsPreguntes.add(Integer.toString(nombreOpcions));
                textsPreguntes.addAll(opcions);
            }
            else if(tipus == 3){ // ORDENADA
                // Afegir les opcions de la pregunta ORDENADA
                List<String> opcions = p.getOpcions();
                int nombreOpcions = opcions.size();
                textsPreguntes.add(Integer.toString(nombreOpcions));
                textsPreguntes.addAll(opcions);
            }
            textsPreguntes.add("- - -");
        }
        return textsPreguntes;
    }

    /**
     * Afegeix les respostes d'un usuari a les preguntes de l'enquesta
     * @param idUsuari de l'usuari
     * @param respostes de l'usuari
     */
    public void afegeixResposta(int idUsuari, List<Resposta> respostes){
        int size = preguntes.size();
        for (int i = 0; i < size; i++) {
            Pregunta p = preguntes.get(i);
            Resposta r = respostes.get(i);
            p.addResposta(r, idUsuari);
        }
    }

    // Getters

    /**
     * Retorna l'id de l'enquesta
     * @return id de l'enquesta
     */
    public Integer getId() { return id;}

    /**
     * Retorna el títol de l'enquesta
     * @return títol de l'enquesta
     */
    public String getTitol() { return titol;}

    /**
     * Retorna la descripció de l'enquesta
     * @return descripció de l'enquesta
     */
    public String getDescripcio() { return descripcio;}

    /**
     * Retorna l'id del creador de l'enquesta
     * @return id del creador de l'enquesta
     */
    public Integer getCreador() { return idCreador;}

    /**
     * Retorna la llista de preguntes de l'enquesta
     * @return llista de preguntes de l'enquesta
     */
    public List<Pregunta> getPreguntesObj() { return Collections.unmodifiableList(preguntes); }

    /**
     * Retorna el nombre de preguntes de l'enquesta
     * @return nombre de preguntes de l'enquesta
     */
    public int getNumPreguntes() { return preguntes.size(); }

    /**
     * Retorna el nombre de respostes per pregunta de l'enquesta
     * @return nombre de respostes per pregunta
     */
    public int getNumRespostes() {
        if (preguntes.isEmpty()) return 0;
        Pregunta primeraPregunta = preguntes.getFirst();
        return primeraPregunta.getNumRespostes();
    }

    /**
     * Retorna les respostes d'un usuari a l'enquesta
     * @param idUsuari de l'usuari
     * @return llista de respostes de l'usuari
     */
    public List<Resposta> getRespostesUsuari(int idUsuari) {
        List<Resposta> respostesUsuari = new ArrayList<>();
        for (Pregunta p : preguntes) {
            Map<Integer, Resposta> respostesMap = p.getRespostes();
            Resposta r = respostesMap.get(idUsuari);
            respostesUsuari.add(r);
        }
        return respostesUsuari;
    }

    /**
     * Retorna les respostes d'un usuari a l'enquesta segons la fila de la matriu
     * @param filaMatriu de la matriu
     * @return llista de respostes de l'usuari
     */
    public List<Resposta> getRespostesUsuariMatriu(int filaMatriu) {
        List<Resposta> respostesUsuari = new ArrayList<>();
        Integer idUsuari = null;
        for (Pregunta p : preguntes) {
            Map<Integer, Resposta> respostesMap = p.getRespostes();
            if (idUsuari == null) {
                // Obtenir l'ID de l'usuari corresponent a la fila de la matriu si no ha estat obtingut encara
                int count = 0;
                for (Integer key : respostesMap.keySet()) {
                    if (count == filaMatriu) {
                        idUsuari = key;
                        break;
                    }
                    count++;
                }
            }
            Resposta r = respostesMap.get(idUsuari);
            respostesUsuari.add(r);
        }
        return respostesUsuari;
    }

    /**
     * Canvia una pregunta de l'enquesta per una nova pregunta
     * @param idxPregunta índex de la pregunta a canviar
     * @param novaPregunta nova pregunta a afegir
     * @throws IndexOutOfBoundsException si l'índex de la pregunta està fora de rang
     */
    public void canviarPregunta(int idxPregunta, Pregunta novaPregunta) {
        if (idxPregunta < 0 || idxPregunta >= preguntes.size()) {
            throw new IndexOutOfBoundsException("Índex de pregunta fora de rang: " + idxPregunta);
        }
        preguntes.set(idxPregunta, novaPregunta);
    }

    /*
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
    }*/

    /**
     * Mostra la informació bàsica de l'enquesta
     */
    void mostrarEnquesta1() {
        System.out.println("Enquesta ID: " + id);
        System.out.println("Títol: " + titol);
        System.out.println("Descripció: " + descripcio);
        System.out.println("Creador ID: " + idCreador);
    }

    /**
     * Mostra les preguntes de l'enquesta
     */
    void mostrarEnquesta2(){
        int numPreguntes = preguntes.size();
        System.out.println("Número de preguntes: " + numPreguntes);
        for (int i = 0; i < numPreguntes; i++) {
            Pregunta p = preguntes.get(i);
            System.out.println("Pregunta " + (i+1) + ": " + p.getText() + " (Tipus: " + p.getTipus() + ")");
        }
    }
}