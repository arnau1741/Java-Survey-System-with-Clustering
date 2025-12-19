package prop.enquestes.controladors;
import java.util.*;

import prop.enquestes.domini.Enquesta;
import prop.enquestes.domini.Pregunta;
import prop.enquestes.excepcions.EnquestaNoExisteixException;
import prop.enquestes.excepcions.FileNotFound;
import prop.enquestes.excepcions.InvalidFormatEnquesta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CtrlDominiMantEnquesta {
    private Map<Integer, Enquesta> enquestes;
    private Integer ultimIdEnquesta;

    /**
     * Constructor de la classe CtrlDominiMantEnquesta
     */
    public CtrlDominiMantEnquesta() {
        enquestes = new HashMap<>();
    }

    /**
     * Persistencia
     * @param enquestes
     */
    public void setEnquestes(Map<Integer, Enquesta> enquestes) {
        this.enquestes = enquestes;
        int max = -1;
        for(Integer key : enquestes.keySet()) {
            max = Math.max(max, key);
        }
        this.ultimIdEnquesta = max + 1;
    }


    /**
     * Afegeix una enquesta a la col·lecció d'enquestes
     * @param enquesta a afegir
     * @throws IllegalArgumentException si l'enquesta és nul·la
     */
    private void addEnquesta(Enquesta enquesta) {
        if (enquesta != null) {
            enquestes.put(enquesta.getId(), enquesta);
        } else {
            throw new IllegalArgumentException("La encuesta no puede ser nula.");
        }
    }

    /**
     * Elimina una enquesta de la col·lecció d'enquestes
     * @param idEnquesta de l'enquesta a eliminar
     * @throws EnquestaNoExisteixException si no existeix l'enquesta amb l'id donat
     */
    public void eliminarEnquesta(int idEnquesta) throws EnquestaNoExisteixException {
        if (enquestes.containsKey(idEnquesta)) {
            enquestes.remove(idEnquesta);
        }
        else {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
    }

    /**
     * Obtén el número total de encuestas
     * @return número d'enquestes
     */
    public int getNumEnquestes() {
        return enquestes.size();
    }

    /**
     * Obtenir un id nou per a una enquesta
     * @return id nou
     */
    public int getIdEnquestaNova() {
        int tmp = ultimIdEnquesta;
        ultimIdEnquesta++;
        return tmp;
    }

    /**
     * Obtenir una enquesta donat el seu id
     * @param idEnquesta de l'enquesta a obtenir
     * @return enquesta amb l'id donat
     * @throws NoSuchElementException si no existeix l'enquesta amb l'id donat
     */
    public Enquesta getEnquesta(int idEnquesta) {
        Enquesta enq = enquestes.get(idEnquesta);
        if(enq == null) {
            throw new NoSuchElementException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        return enq;
    }

    /**
     * Obtenir les preguntes d'una enquesta donat el seu id
     * @param idEnquesta de l'enquesta
     * @return llista de preguntes de l'enquesta, llista buida si no es troba l'enquesta
     * @throws EnquestaNoExisteixException si no existeix l'enquesta amb l'id donat
     */
    public List<String> getPreguntesEnquesta(int idEnquesta) throws EnquestaNoExisteixException {
        Enquesta enq = enquestes.get(idEnquesta);
        if (enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        return enq.getPreguntes(); // Devuelve una lista vacía si no se encuentra la encuesta
    }

    /**
     * Funcio per mostrar totes les enquestes
     */
    public void mostrarEnquestes() {
        if (enquestes.isEmpty()) {
            System.out.println("No hay encuestas disponibles.");
        } else {
            for (Enquesta e : enquestes.values()) {
                System.out.println(e);
            }
        }
    }

    /**
     * Obte totes les enquestes existents
     * @return un map d'enquestes amb primer parametre com l'id i el segon com l'enquesta
     */
    public Map<Integer,Enquesta> getEnquestesObj() {
        return enquestes;
    }

    /**
     *  Crea una nova enquesta al repositori del map enquestes
     * @param titol de l'enquesta
     * @param descripcio de l'enquesta
     * @param idCreador de l'enquesta
     * @param preguntes de l'enquesta
     * @return el numero de preguntes que te la nova enquesta
     * @throws InvalidFormatEnquesta Si el format de l'enquesta és invàlid
     */
    public int novaEnquesta(String titol, String descripcio, int idCreador, List<String> preguntes) throws InvalidFormatEnquesta {
        try{
            List<Pregunta> preguntesObj = transformaPreguntesAObj(preguntes);
            int id = getIdEnquestaNova();
            Enquesta novaEnquesta;
            novaEnquesta = new Enquesta(id, titol, descripcio, idCreador, preguntesObj);
            addEnquesta(novaEnquesta);
            return preguntesObj.size();
        } catch (IllegalArgumentException e) {
            throw new InvalidFormatEnquesta("No s'ha pogut crear l'enquesta: " + e.getMessage());
        }
    }

    /**
     *  Funcio que importa una enquesta d'un fitxer txt
     * @param idUsuari que importa l'enquesta
     * @param path que conte el fitxer de l'enquesta
     * @return retorna el numero de preguntes que te la nova enquesta importada
     * @throws InvalidFormatEnquesta Si el format de l'enquesta és invàlid
     * @throws FileNotFound Si no es troba el fitxer a la ruta especificada
     */
    public int importarEnquesta(int idUsuari, String path) throws InvalidFormatEnquesta, FileNotFound {
        // llegir fitxer
        List<String> enquestaTxt = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                enquestaTxt.add(line);
            }
        } catch (IOException e) {
            throw new FileNotFound("No s'ha pogut trobar el fitxer a la ruta especificada: " + path);
        }

        // extreure titol, descripcio i preguntes
        String titol = enquestaTxt.get(0);
        String descripcio = enquestaTxt.get(1);
        List<String> preguntesTxt = new ArrayList<>();
        for (int i = 2; i < enquestaTxt.size(); i++) {
            preguntesTxt.add(enquestaTxt.get(i));
        }

        // crear enquesta
        int numPreguntes = novaEnquesta(titol, descripcio, idUsuari, preguntesTxt);
        return numPreguntes;
    }

    /**
     * Funcio per a transformar les preguntes en format string a objectes Pregunta
     * @param preguntes Llista de preguntes en format text
     * @return Llista de preguntes en format objecte Pregunta
     * @throws InvalidFormatEnquesta Si el format de l'enquesta és invàlid
     */
    //////////////////////// Funcions auxiliars ///////////////////////////////
    //Transforma les preguntes en format text a objectes Pregunta
    private List<Pregunta> transformaPreguntesAObj (List<String> preguntes) throws InvalidFormatEnquesta {
        List <Pregunta> preguntesObj = new ArrayList<>();
        int size = preguntes.size();
        int idx = 0;
        while (idx < size) {
            String enunciat = preguntes.get(idx);
            int tipus;
            try{
                tipus = Integer.parseInt(enunciat);
            } catch (NumberFormatException e) {
                throw new InvalidFormatEnquesta("Format invàlid de l'enquesta: tipus de pregunta no és un enter.");
            }
            idx++;
            enunciat = preguntes.get(idx);
            idx++;
            if (tipus == 1 || tipus == 2 || tipus == 3) { //UNICA, ORDENADA, MULTIPLE
                int numOpcions;
                try{
                    numOpcions = Integer.parseInt(preguntes.get(idx));
                } catch (NumberFormatException e) {
                    throw new InvalidFormatEnquesta("Format invàlid de l'enquesta: nombre d'opcions no és un enter.");
                }  
                idx++;
                List<String> opcions = new ArrayList<>();
                for (int i = 0; i < numOpcions; i++) {
                    String opcio = preguntes.get(idx);
                    opcions.add(opcio);
                    idx++;
                }
                Pregunta p = new Pregunta(enunciat, tipus, opcions);
                preguntesObj.add(p);
            }
            else if (tipus == 0 || tipus == 4) { //NUMERICA, LLIURE
                Pregunta p = new Pregunta(enunciat, tipus, null);
                preguntesObj.add(p);
            }
            else{
                throw new InvalidFormatEnquesta("Format invàlid de l'enquesta: tipus de pregunta desconegut.");
            }
        }
        return preguntesObj;
    }

    /**
     * Funcio per a modificar una pregunta d'una enquesta
     * @param idEnquesta identificador de l'enquesta per la pregunta que vol modificar
     * @param idxPregunta index de la pregunta a modificar
     * @param novaPregunta llista de strings amb la nova pregunta
     * @return 1 si s'ha modificat correctament
     * @throws EnquestaNoExisteixException si l'enquesta no existeix
     * @throws InvalidFormatEnquesta si el format de la nova pregunta es invàlid
     * @throws IndexOutOfBoundsException si l'índex de la pregunta està fora de rang
     */
    public int modificarPreguntaEnquesta(int idEnquesta, int idxPregunta, List<String> novaPregunta) throws EnquestaNoExisteixException,InvalidFormatEnquesta {
        //borrar todas las respuestas
        Enquesta enq = getEnquesta(idEnquesta);
        if(enq == null) {
            throw new EnquestaNoExisteixException("L'enquesta amb id " + idEnquesta + " no existeix.");
        }
        List<Pregunta> preguntes = enq.getPreguntesObj();
        for (Pregunta p : preguntes) {
            p.eliminarTotesRespostes();
        }

        //crear la nueva pregunta
        List<Pregunta> preguntesObj = transformaPreguntesAObj(novaPregunta);
        //assignar la nueva pregunta a la enquesta
        try {
            enq.canviarPregunta(idxPregunta, preguntesObj.get(0));
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidFormatEnquesta("Índex de pregunta fora de rang: " + idxPregunta);
        }   
        //setearla como nueva pregunta

        return 1; // Èxit
    }

    /**
     * Retorna una llista amb tots els ids de les enquestes existents
     * @return llista d'ids d'enquestes
     */
    public List<Integer> getIdsEnquestes() {
        return new ArrayList<>(enquestes.keySet());
    }

    /**
     * Retorna una llista amb tots els títols de les enquestes existents
     * @return llista de títols d'enquestes
     */
    public List<String> getTitolsEnquestes() {
        List<String> titols = new ArrayList<>();
        for (Enquesta e : enquestes.values()) {
            titols.add(e.getTitol());
        }
        return titols;
    }

    /**
     * Retorna l'última enquesta creada
     * @return l'última enquesta creada, o null si no hi ha enquestes
     */
    public Enquesta getUltimaEnquestaCreada() {
        if(!enquestes.isEmpty()) {
            return enquestes.get(ultimIdEnquesta-1);
        }
        return null;
    }
}
