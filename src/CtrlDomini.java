import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

public class CtrlDomini {
    //Tiene que estar todos los gestores creados
    private GestorEnquesta gestorEnquesta;
    private GestorUsuaris gestorUsuaris;

    public CtrlDomini() {
        //Crea todas las instancias de los gestores
        gestorEnquesta = new GestorEnquesta();
        gestorUsuaris = new GestorUsuaris();
    }

    //Getters
    public GestorEnquesta getCtrlEnquesta() {
        return gestorEnquesta;
    }
    public GestorUsuaris getCtrlUsuari() {
        return gestorUsuaris;
    }

    //Setters
    public void setCtrlEnquesta(GestorEnquesta gestorEnquesta) {
        this.gestorEnquesta = gestorEnquesta;
    }
    public void setCtrlUsuari(GestorUsuaris gestorUsuaris) {
        this.gestorUsuaris = gestorUsuaris;
    }

    //Caso de uso 1 - Respondre enquesta
    public void respondreEnquesta(int idEnquesta, int idUsuari) {
        //empezamos seleccionando el usuario que responde
        GestorUsuaris gu = getCtrlUsuari();
        gu.llistarUsuaris();
        //idUsuari deberia ser seleccionado por el scanner en la capa de presentacion
        Usuari u = gu.seleccionarUsuari(idUsuari);

        //empezamos mostrando id's de las encuestas
        GestorEnquesta ge = getCtrlEnquesta();
        ge.llistarEnquestes();
        //idEnquesta deberia ser seleccionado por el scanner en la capa de presentacion
        Enquesta enq = ge.getEnquestaPerID(idEnquesta);

        boolean participa = enq.participa(u);
        int x = -1;
        if(!participa){
            //crear nueva fila en la matriz que devolverá la fila x
            x = enq.afegirFilaRespostes();
            //añadir usuario a la lista de participantes en la posicion x
            enq.afegirParticipant(u, x);
        }
        //Muestra las preguntas en orden para ser respondidas por el usuario
        enq.mostrarPreguntes();

        //lógica para añadir respuestas del usuario a la encuesta
        Pregunta[] preguntes = enq.getPreguntes(x);
        Scanner sc = new Scanner(System.in);
        for(int i = 0; i < preguntes.length; i++){
            Pregunta pre = preguntes[i];
            System.out.println("\nPregunta " + (i + 1) + ": " + pre.getText());
            //usuario da la respuesta
            Resposta res = null;
            try {
                switch (pre.getTipus()) {
                    case NUMERICA -> {
                        System.out.print("Introdueix un número (0-10): ");
                        double v = Double.parseDouble(sc.nextLine().trim());
                        RespostaNumerica rn = new RespostaNumerica(pre, v);
                        res = rn;
                    }
                    case LLIURE -> {
                        System.out.print("Resposta lliure: ");
                        String text = sc.nextLine();
                        RespostaLliure rl = new RespostaLliure(pre, text);
                        res = rl;
                    }
                    case UNICA -> {
                        System.out.print("Introdueix opcions separades per comes (si n’hi ha): ");
                        String entrada = sc.nextLine();
                        List<String> opcions = Arrays.stream(entrada.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .toList();

                        System.out.println("Opcions disponibles: " + opcions);
                        System.out.print("Selecciona una: ");
                        String opcio = sc.nextLine().trim();

                        RespostaUnica ru = new RespostaUnica(pre, opcions);
                        ru.seleccionar(opcio);
                        res = ru;
                    }
                    case MULTIPLE -> {
                        System.out.print("Introdueix opcions separades per comes (si n’hi ha): ");
                        String entrada = sc.nextLine();
                        List<String> opcions = Arrays.stream(entrada.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .toList();

                        System.out.println("Selecciona diverses opcions (separades per comes): ");
                        String respostaUsuari = sc.nextLine();
                        List<String> seleccionades = Arrays.stream(respostaUsuari.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .toList();

                        RespostaMultiple rm = new RespostaMultiple(pre, opcions);
                        for (String s : seleccionades) rm.seleccionar(s);
                        res = rm;
                    }
                    case ORDENADA -> {
                        System.out.print("Introdueix opcions separades per comes: ");
                        String entrada = sc.nextLine();
                        List<String> opcions = Arrays.stream(entrada.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .toList();

                        System.out.print("Introdueix l’ordre separat per comes: ");
                        String ordre = sc.nextLine();
                        List<String> ordreLlista = Arrays.stream(ordre.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .toList();

                        RespostaOrdenada ro = new RespostaOrdenada(pre, ordreLlista);
                        res = ro;
                    }
                }
                pre.setResposta(res);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //asignar la respuesta a la matriz de respuestas de la encuesta
            enq.setPreguntaResposta(x, i, pre);
        }
    }

    //Caso de uso 2 - Crear enquesta
    public void crearEnquesta(int idUsuari, List<Pregunta> pregunta) {
        //seleccionamos el usuario creador
        GestorUsuaris gu = getCtrlUsuari();
        gu.llistarUsuaris();
        Usuari u = gu.seleccionarUsuari(idUsuari);

        GestorEnquesta ges = getCtrlEnquesta();
        //creamos la enquesta
        //introducimos los datos en la capa de prentacion (id, titol, descripcio, creador)
        int id = ges.returnSize();
        String titol = "me la pela";
        String descripcio = "una enquesta que me la pela";
        Enquesta enq = new Enquesta(id, titol, descripcio, u);
        ges.afegirEnquesta(enq);

        //enq.afegirParticipant(u, enq.afegirFilaRespostes());
        enq.participants.addFirst(new Pair<>(u, 0)); //afegim el creador com a participant per defecte

        //crear la fila de preguntas con respuestas respuestas
        enq.afegirFilaRespostes();

        //añadir preguntas a la encuesta
        for(Pregunta p: pregunta) enq.respostes.get(id).add(p);
    }


    /*
    public void consultarEstadistiques(int idEnquesta) {
        GestorEnquesta ge = getCtrlEnquesta();
        Enquesta enq = ge.getEnquestaPerID(idEnquesta);
        enq.mostrarEstadistiques();
    }
    */

}

