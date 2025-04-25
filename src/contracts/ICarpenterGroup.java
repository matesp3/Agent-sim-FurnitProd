package contracts;

import common.CarpenterGroup;

public interface ICarpenterGroup {
    CarpenterGroup getAllocator();
    void setAmountOfCarpenters(int amount);
}
