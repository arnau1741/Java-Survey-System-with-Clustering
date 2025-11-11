//package prop.enquesta.drivers;

//import prop.enquesta.resposta.*;
//import prop.enquesta.excepcions.RespostaInvalida;
//import prop.enquesta.utils.Validacio;
import java.util.*;

public class DriverResposta {
    public static void main(String[] args) {
        System.out.println("===== DRIVER PREGUNTA + RESPOSTA =====\n");

        try {

            //Prova1: Pregunta Numèrica
            System.out.println("--- PROVA 1: PREGUNTA NUMÈRICA ---");
            Pregunta pNum = new Pregunta("Valora el curs del 0 al 10", 0, null);
            RespostaNumerica rNum = new RespostaNumerica(8.5);
            //rNum.validar();
            pNum.addResposta(rNum, 1);
            System.out.println("Pregunta: " + pNum.getText());
            System.out.println("Resposta: " + rNum.getText(null));
            System.out.println("Estat: contestat=" + rNum.EsContestat() + "\n");


            //Prova2: Pregunta lliure
            System.out.println("--- PROVA 2: PREGUNTA LLIURE ---");
            Pregunta pLliure = new Pregunta("Comenta la teva experiència", 4, null);
            RespostaLliure rLliure = new RespostaLliure("Molt bona experiència, m'ha agradat molt");
            //rLliure.validar();
            pLliure.addResposta(rLliure, 2);
            System.out.println("Pregunta: " + pLliure.getText());
            System.out.println("Resposta: " + rLliure.getText(null));
            System.out.println("Estat: contestat=" + rLliure.EsContestat() + "\n");


            //Prova 3: Pregunta Única
            System.out.println("--- PROVA 3: PREGUNTA ÚNICA ---");
            List<String> opcionsUnica = Arrays.asList("Sí", "No", "No ho sé");
            Pregunta pUnica = new Pregunta("T'ha agradat el contingut?", 1, opcionsUnica);
            RespostaUnica rUnica = new RespostaUnica(opcionsUnica.size());
            rUnica.setResposta(0); // Selecciona "Sí"
            //rUnica.validar();
            pUnica.addResposta(rUnica, 3);
            System.out.println("Pregunta: " + pUnica.getText());
            System.out.println("Opcions: " + opcionsUnica);
            System.out.println("Resposta: " + rUnica.getText(opcionsUnica));
            System.out.println("Estat: contestat=" + rUnica.EsContestat() + "\n");

            //Prova 4: Pregunta Múltiple
            System.out.println("--- PROVA 4: PREGUNTA MÚLTIPLE ---");
            List<String> opcionsMultiple = Arrays.asList("Java", "Python", "C++", "JavaScript");
            Pregunta pMultiple = new Pregunta("Quins llenguatges coneixes?", 2, opcionsMultiple);
            RespostaMultiple rMultiple = new RespostaMultiple(opcionsMultiple.size());
            rMultiple.selecciona(Arrays.asList(0, 1)); // Java i Python
            //rMultiple.validar();
            pMultiple.addResposta(rMultiple, 4);
            System.out.println("Pregunta: " + pMultiple.getText());
            System.out.println("Opcions: " + opcionsMultiple);
            System.out.println("Resposta: " + rMultiple.getText(opcionsMultiple));
            System.out.println("Estat: contestat=" + rMultiple.EsContestat() + "\n");

            //Prova 5: Pregunta Ordenada
            System.out.println("--- PROVA 5: PREGUNTA ORDENADA ---");
            List<String> opcionsOrdenada = Arrays.asList("Baixa", "Mitjana", "Alta");
            Pregunta pOrdenada = new Pregunta("Prioritza les teves necessitats", 3, opcionsOrdenada);
            RespostaOrdenada rOrdenada = new RespostaOrdenada(opcionsOrdenada.size());
            rOrdenada.setResposta(2); // Selecciona "Alta"
            //rOrdenada.validar();
            pOrdenada.addResposta(rOrdenada, 5);
            System.out.println("Pregunta: " + pOrdenada.getText());
            System.out.println("Opcions: " + opcionsOrdenada);
            System.out.println("Resposta: " + rOrdenada.getText(opcionsOrdenada));
            System.out.println("Estat: contestat=" + rOrdenada.EsContestat() + "\n");

            //Prova 6: Múltiples respostes per diferents usuaris
            System.out.println("--- PROVA 6: MÚLTIPLES RESPOSTES ---");
            Pregunta pMultiUsuari = new Pregunta("Valoració general", 4, Arrays.asList("Mala", "Regular", "Bona", "Excel·lent"));

            //Usuari 1 respon
            RespostaUnica rUsuari1 = new RespostaUnica(4);
            rUsuari1.setResposta(3); // Excel·lent
            pMultiUsuari.addResposta(rUsuari1, 101);

            //Usuari 2 respon
            RespostaUnica rUsuari2 = new RespostaUnica(4);
            rUsuari2.setResposta(2); // Bona
            pMultiUsuari.addResposta(rUsuari2, 102);

            //Usuari 3 respon
            RespostaUnica rUsuari3 = new RespostaUnica(4);
            rUsuari3.setResposta(3); // Excel·lent
            pMultiUsuari.addResposta(rUsuari3, 103);

            System.out.println("Pregunta: " + pMultiUsuari.getText());
            System.out.println("Total respostes: " + pMultiUsuari.getNumRespostes());


        } catch (Exception e) {
            //System.err.println("Error inesperat: " + e.getMessage());
            //e.printStackTrace();
        }

        System.out.println("\n===== FI DEL DRIVER MANUAL =====");
    }
}