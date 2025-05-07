package animation;

import OSPAnimator.AnimImageItem;
import OSPAnimator.AnimQueue;
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
    private static final int DESKS_PER_ROW = 6;
    private static final int DESKS_DIST_X = ImgResources.WIDTH_DESK + + ImgResources.WIDTH_CARPENTER + 25;
    private static final int DESKS_DIST_Y = ImgResources.HEIGHT_DESK + 15;
    private static final int DESK_HOVER_OFFSET_Y = 45; // from top of desk image
    private static final Point2D BASE_POS = new Point2D.Double(300,200);

    private static final Point2D STORAGE_Q_CARPS_A_START = new Point2D.Double(0,0);
    private static final Point2D STORAGE_Q_CARPS_A_END = new Point2D.Double(100, 100);

    private static final Point2D STORAGE_Q_CARPS_B_START = new Point2D.Double(0,ImgResources.HEIGHT_CARPENTER);
    private static final Point2D STORAGE_Q_CARPS_B_END = new Point2D.Double(200, ImgResources.HEIGHT_CARPENTER*2);

    private static final Point2D STORAGE_Q_CARPS_C_START = new Point2D.Double(0,ImgResources.HEIGHT_CARPENTER*2);
    private static final Point2D STORAGE_Q_CARPS_C_END = new Point2D.Double(200, ImgResources.HEIGHT_CARPENTER*3);

    private IAnimator animator;

    private AnimQueue qCarpsInStorageA;
//    private AnimQueue qCarpsInStorageB;
//    private AnimQueue qCarpsInStorageC;
    private AnimImageItem[] animDesks = null;
    private int desksCount;

    /**
     * @param animator animator, to which all desk entities will be registered
     * @param desksCount amount of desks in furniture factory
     */
    public FurnitureFactoryAnimation(IAnimator animator, int desksCount) {
        this.animator = animator;
        this.desksCount = desksCount;
        this.qCarpsInStorageA = new AnimQueue(animator, STORAGE_Q_CARPS_A_START, STORAGE_Q_CARPS_A_END, 100);
        this.qCarpsInStorageA.setVisible(true); // visibility of queue stats
        this.initDesks();
    }

    public void moveCarpenterToDesk(int deskId, double timeOfArrivalToDesk, AnimatedEntity carpenter) {
        double x = getDeskBaseX(deskId) - ImgResources.WIDTH_CARPENTER + 25;
        double y = getDeskBaseY(deskId) + ImgResources.HEIGHT_DESK - ImgResources.HEIGHT_CARPENTER + 15;
        carpenter.setPosition(x, y);
    }

    public void moveFurnitureOnDesk(int deskId, double timeOfArrivalToDesk, AnimatedEntity furniture) {
        double x = getDeskBaseX(deskId) + (ImgResources.WIDTH_DESK - furniture.getWidth()) / 2;
        double y = getDeskBaseY(deskId) + DESK_HOVER_OFFSET_Y - furniture.getHeight();
        furniture.setPosition(x, y);
    }

    public void leaveDesk(int deskId) {

    }

    public void placeCarpenterToStorage(AnimatedEntity animatedCarpenter) {
        animatedCarpenter.setLabelsVisible(false);
        this.qCarpsInStorageA.insert(animatedCarpenter);
    }

    public IAnimator getAnimator() {
        return animator;
    }

    public void setAnimator(IAnimator animator) {
        this.animator = animator;
    }

    private void initDesks() {
        this.animDesks = new AnimImageItem[this.desksCount];
        for (int deskId = 0; deskId < this.desksCount; deskId++) {
            this.animDesks[deskId] = ImgResources.createDesk(getDeskBaseX(deskId), getDeskBaseY(deskId));
            this.animator.register(this.animDesks[deskId]);
        }
    }

    /**
     * @param deskId number of desk, by which is computed position of desk
     * @return Top-left corner position X of desk picture
     */
    private static double getDeskBaseX(int deskId) {
        return BASE_POS.getX() + DESKS_DIST_X * (deskId % DESKS_PER_ROW);
    }

    /**
     * @param deskId number of desk, by which is computed position of desk
     * @return Top-left corner position Y of desk picture
     */
    private static double getDeskBaseY(int deskId) {
        return BASE_POS.getY() + DESKS_DIST_Y * (deskId / DESKS_PER_ROW);
    }

    private static int getMax(int[] values) {
        int max = 0;
        for (int v : values) {
            if (v > max) max = v;
        }
        return max;
    }
}
