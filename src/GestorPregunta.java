import java.util.*;

public class GestorPregunta {

    private final List<Pregunta> preguntes;

    public GestorPregunta() {
        this.preguntes = new ArrayList<>();
    }

    //Crear pregunta
    public Pregunta crearPregunta(Integer id, String text, Pregunta.Tipus tipus) {
        Pregunta nova = new Pregunta(id, text, tipus);
        preguntes.add(nova);
        return nova;
    }

    //Eliminar pregunta
    public void eliminarPregunta(Integer id) {
        preguntes.removeIf(p -> p.getId().equals(id));
    }
/*
    // Modificar pregunta
    public void modificarPregunta(Integer id, String nouText, Pregunta.Tipus nouTipus) {
        for (Pregunta p : preguntes) {
            if (p.getId().equals(id)) {
                p = new Pregunta(id, nouText, nouTipus);
                p.setResposta(null); // reset respuesta al cambiar el tipo
                return;
            }
        }
    }

    //Assignar resposta
    public void assignarResposta(Integer idPregunta, Resposta resposta) {
        for (Pregunta p : preguntes) {
            if (p.getId().equals(idPregunta)) {
                p.setResposta(resposta);
                resposta.setContestat(true);
                return;
            }
        }
    }

    //Consultar resposta d'una pregunta
    public Resposta getResposta(Integer idPregunta) {
        for (Pregunta p : preguntes) {
            if (p.getId().equals(idPregunta)) {
                return p.getResposta();
            }
        }
        return null;
    }
    */

    //Mostrar totes les preguntes
    public void mostrarPreguntes() {
        for (Pregunta p : preguntes) {
            System.out.println(p);
        }
    }

    public List<Pregunta> getPreguntes() {
        return Collections.unmodifiableList(preguntes);
    }
}
