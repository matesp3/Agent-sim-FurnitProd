package contracts;

import common.CarpenterGroup;

public interface ICarpenterGroup {
    CarpenterGroup getAllocator();
    void setAmountOfCarpenters(int amount);

    /**
     * @return number from interval <0,1>
     */
    double getGroupUtilization();
}
