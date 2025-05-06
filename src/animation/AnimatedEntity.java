package animation;

import OSPAnimator.AnimItem;
import OSPAnimator.IAnimator;

public abstract class AnimatedEntity extends AnimItem {
    public abstract void registerEntity(IAnimator animator);
    public abstract void renderEntity();
    public abstract void removeEntity();
}
