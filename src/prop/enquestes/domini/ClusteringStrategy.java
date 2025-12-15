package prop.enquestes.domini;

import java.util.Map;
import prop.enquestes.excepcions.KmeansExcepcio;

public interface ClusteringStrategy {
    /**
     * Executa l'algorisme de clustering configurat sobre l'enquesta.
     * @param data L'enquesta amb les dades a processar.
     * @return Un Map on la clau és l'ID de l'Usuari i el valor és l'ID del Clúster assignat.
     * @throws KmeansExcepcio Si hi ha algun error en l'execució matemàtica.
     */
    Map<Integer, Integer> executar(Enquesta data) throws KmeansExcepcio;
}