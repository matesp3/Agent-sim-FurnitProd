package animation;

import OSPAnimator.AnimImageItem;
import OSPAnimator.AnimItem;
import OSPAnimator.IAnimator;

public abstract class AnimatedEntity extends AnimImageItem {
    public abstract void registerEntity(IAnimator animator);
    public abstract void renderEntity();
    public abstract void unregisterEntity();
    public abstract void setLabelsVisible(double inTime, boolean visible);
    public abstract void setLabelsVisible(boolean visible);
}
