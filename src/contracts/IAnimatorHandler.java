package contracts;

import OSPAnimator.IAnimator;

public interface IAnimatorHandler {
    void setAnimator(IAnimator animator);
    IAnimator getAnimator();
}
