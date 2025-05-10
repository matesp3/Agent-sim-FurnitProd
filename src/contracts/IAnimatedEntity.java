package contracts;

import OSPAnimator.IAnimator;
import animation.AnimatedEntity;

/**
 * Entity, which will have some type of visual representation in animation.
 */
public interface IAnimatedEntity {
    /**
     * Creates representation of entity, if such doesn't already exist
     * @return
     */
    AnimatedEntity initAnimatedEntity();
    /**
     * @return animated item that represents entity in animation or {@code null}, if visual representation of entity
     * is not present.
     */
    AnimatedEntity getAnimatedEntity();

    void removeAnimatedEntity(IAnimator from);
}
