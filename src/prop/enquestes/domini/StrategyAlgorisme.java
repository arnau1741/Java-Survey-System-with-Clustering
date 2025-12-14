package prop.enquestes.domini;

import prop.enquestes.excepcions.KmeansExcepcio;

public interface StrategyAlgorisme {

    void fit(Enquesta enquesta) throws KmeansExcepcio;

    int[] getLabels();

    double getCoeficientSilhouete();
}