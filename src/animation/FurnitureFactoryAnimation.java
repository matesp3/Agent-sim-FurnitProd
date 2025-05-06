package animation;

import OSPAnimator.AnimImageItem;
import OSPAnimator.AnimItem;
import OSPAnimator.IAnimator;
import contracts.IAnimatorHandler;

import java.awt.geom.Point2D;

/**
 * Handles creating desks entities, registering them to specified {@code animator} and moving other items onto them
 * and leaving them also.
 */
public class FurnitureFactoryAnimation implements IAnimatorHandler {
    private static final int W = getMax(new int[]{ImgResources.WIDTH_CARPENTER, ImgResources.WIDTH_CHAIR, ImgResources.WIDTH_TABLE, ImgResources.WIDTH_WARDROBE, ImgResources.WIDTH_TABLE});
    private static final int H = getMax(new int[]{ImgResources.HEIGHT_CARPENTER, ImgResources.HEIGHT_CHAIR, ImgResources.HEIGHT_TABLE, ImgResources.HEIGHT_WARDROBE, ImgResources.HEIGHT_TABLE});

    private static final int DESKS_DIST_X = ImgResources.WIDTH_DESK + 25;
    private static final int DESKS_DIST_Y = ImgResources.HEIGHT_DESK + 15;
    private static final Point2D BASE_POS = new Point2D.Double(200,50);

    private IAnimator animator;

    private AnimImageItem[] animDesks = null;
    private int desksCount;

    /**
     * @param animator animator, to which all desk entities will be registered
     * @param desksCount amount of desks in furniture factory
     */
    public FurnitureFactoryAnimation(IAnimator animator, int desksCount) {
        this.animator = animator;
        this.desksCount = desksCount;
        this.initDesks();
    }

    public void moveToDesk(int deskId, double timeOfArrivalToDesk, AnimItem animItem) {

    }

    public void leaveDesk(int deskId) {

    }

    public void placeCarpenterToStorage(AnimImageItem animatedCarpenter) {

    }

    public IAnimator getAnimator() {
        return animator;
    }

    public void setAnimator(IAnimator animator) {
        this.animator = animator;
    }

    private void initDesks() {
        this.animDesks = new AnimImageItem[this.desksCount];
        double x,y;
        for (int i = 0; i < this.desksCount; i++) {
            x = BASE_POS.getX() + DESKS_DIST_X * (i%5);
            y = BASE_POS.getY() + DESKS_DIST_Y * (i%5);
            this.animDesks[i] = ImgResources.createDesk(x, y);
            this.animator.register(this.animDesks[i]);
        }
    }

    private static int getMax(int[] values) {
        int max = 0;
        for (int v : values) {
            if (v > max) max = v;
        }
        return max;
    }
}
