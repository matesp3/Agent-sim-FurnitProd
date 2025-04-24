package contracts;

import OSPRNG.RNG;
import OSPRNG.TriangularRNG;

public interface IFittingsInstaller {
    void setFitInstGenerator(RNG<Double> durationGenerator);
}
