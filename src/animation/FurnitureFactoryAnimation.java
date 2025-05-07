package animation;

import OSPAnimator.AnimImageItem;
import OSPAnimator.AnimQueue;
import OSPAnimator.AnimTextItem;
import OSPAnimator.IAnimator;
import contracts.IAnimatorHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

/**
 * Handles creating desks entities, registering them to specified {@code animator} and moving other items onto them
 * and leaving them also.
 */
public class FurnitureFactoryAnimation implements IAnimatorHandler {
    private static final int W = getMax(new int[]{ImgResources.WIDTH_CARPENTER, ImgResources.WIDTH_CHAIR, ImgResources.WIDTH_TABLE, ImgResources.WIDTH_WARDROBE, ImgResources.WIDTH_TABLE});
    private static final int H = getMax(new int[]{ImgResources.HEIGHT_CARPENTER, ImgResources.HEIGHT_CHAIR, ImgResources.HEIGHT_TABLE, ImgResources.HEIGHT_WARDROBE, ImgResources.HEIGHT_TABLE});
    private static final int DESKS_PER_ROW = 8;
    private static final int DESKS_DIST_X = ImgResources.WIDTH_DESK + + ImgResources.WIDTH_CARPENTER + 25;
    private static final int DESKS_DIST_Y = ImgResources.HEIGHT_DESK + 35;
    private static final int DESK_HOVER_OFFSET_Y = 45; // from top of desk image
    private static final Point2D BASE_POS = new Point2D.Double(0,0);
    private static final Point2D BASE_STORAGE_POS = new Point2D.Double(BASE_POS.getX() + DESKS_DIST_X*DESKS_PER_ROW,-100);

    private static final Point2D STORAGE_Q_CARPS_C_START = new Point2D.Double(BASE_STORAGE_POS.getX() + 850,BASE_STORAGE_POS.getY() + ImgResources.HEIGHT_CARPENTER / 2.0);
    private static final Point2D STORAGE_Q_CARPS_C_END = new Point2D.Double(STORAGE_Q_CARPS_C_START.getX()+1, STORAGE_Q_CARPS_C_START.getY());

    private static final Point2D STORAGE_Q_CARPS_B_START = new Point2D.Double(BASE_STORAGE_POS.getX() + 725, BASE_STORAGE_POS.getY() + ImgResources.HEIGHT_CARPENTER);
    private static final Point2D STORAGE_Q_CARPS_B_END = new Point2D.Double(STORAGE_Q_CARPS_B_START.getX()+1, STORAGE_Q_CARPS_B_START.getY());

    private static final Point2D STORAGE_Q_CARPS_A_START = new Point2D.Double(BASE_STORAGE_POS.getX() + 600,BASE_STORAGE_POS.getY() + ImgResources.HEIGHT_CARPENTER*3/2.0);
    private static final Point2D STORAGE_Q_CARPS_A_END = new Point2D.Double(STORAGE_Q_CARPS_A_START.getX()+1, STORAGE_Q_CARPS_A_START.getY());

    private static final Point2D STORAGE_FURNITURE_POS_START = new Point2D.Double(STORAGE_Q_CARPS_A_START.getX() - 200, STORAGE_Q_CARPS_A_START.getY() + ImgResources.HEIGHT_CARPENTER*3/2.0);
    private static final Point2D STORAGE_FURNITURE_POS_END = new Point2D.Double(STORAGE_FURNITURE_POS_START.getX()+10, STORAGE_FURNITURE_POS_START.getY());

    private IAnimator animator;

    private AnimQueue qCarpsInStorageA;
    private AnimQueue qCarpsInStorageB;
    private AnimQueue qCarpsInStorageC;
    private AnimQueue qWaitingFurniture;
    private int desksCount;

    /**
     * @param animator animator, to which all desk entities will be registered
     * @param desksCount amount of desks in furniture factory
     */
    public FurnitureFactoryAnimation(IAnimator animator, int desksCount) {
        try {
            AnimImageItem imgStorage = new AnimImageItem(ImageIO.read(new File(ImgResources.IMG_PATH_STORAGE)));
            animator.register(imgStorage);
            imgStorage.setPosition(BASE_STORAGE_POS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.animator = animator;
        this.desksCount = desksCount;
        this.qCarpsInStorageA = new AnimQueue(animator, STORAGE_Q_CARPS_A_START, STORAGE_Q_CARPS_A_END, 0);
        this.qCarpsInStorageA.setVisible(true); // visibility of queue stats
        this.qCarpsInStorageB = new AnimQueue(animator, STORAGE_Q_CARPS_B_START, STORAGE_Q_CARPS_B_END, 0);
        this.qCarpsInStorageB.setVisible(true); // visibility of queue stats
        this.qCarpsInStorageC = new AnimQueue(animator, STORAGE_Q_CARPS_C_START, STORAGE_Q_CARPS_C_END, 0);
        this.qCarpsInStorageC.setVisible(true); // visibility of queue stats
        this.qWaitingFurniture = new AnimQueue(animator, STORAGE_FURNITURE_POS_START, STORAGE_FURNITURE_POS_END, 0);
        AnimTextItem txtQWaitingLabel = new AnimTextItem("Waiting for creating:");
        txtQWaitingLabel.setPosition(STORAGE_FURNITURE_POS_START.getX(), STORAGE_FURNITURE_POS_START.getY() - 20);
        txtQWaitingLabel.setColor(new Color(37, 119, 32));
        animator.register(txtQWaitingLabel);
        this.initDesks();
    }

    public void enqueueFurnitureInStorage(AnimatedEntity furniture) {
        furniture.setPosition(STORAGE_FURNITURE_POS_START); // first anim - bcs of nullptrException
        this.qWaitingFurniture.insert(furniture);
    }

    public void moveFurnitureOnDesk(int deskId, double timeOfArrivalToDesk, AnimatedEntity furniture) {
        double x = getDeskBaseX(deskId) + ((ImgResources.WIDTH_DESK - furniture.getWidth()) / 3.75);
        double y = getDeskBaseY(deskId) + DESK_HOVER_OFFSET_Y - furniture.getHeight();
        furniture.setPosition(x, y);
    }

    public void leaveDesk(int deskId) {

    }

    public void moveCarpenterToDesk(int deskId, double timeOfArrivalToDesk, AnimatedEntity carpenter) {
        double x = getDeskBaseX(deskId) - ImgResources.WIDTH_CARPENTER + 25;
        double y = getDeskBaseY(deskId) + ImgResources.HEIGHT_DESK - ImgResources.HEIGHT_CARPENTER + 15;
        carpenter.setPosition(x, y);
    }

    public void placeCarpenterAToStorage(AnimatedEntity carpenter) {
        this.enqueueCarpenterInStorage(carpenter, this.qCarpsInStorageA);
    }

    public void placeCarpenterBToStorage(AnimatedEntity carpenter) {
        this.enqueueCarpenterInStorage(carpenter, this.qCarpsInStorageB);
    }

    public void placeCarpenterCToStorage(AnimatedEntity carpenter) {
        this.enqueueCarpenterInStorage(carpenter, this.qCarpsInStorageC);
    }

    public void takeCarpenterAFromStorage(AnimatedEntity carpenter) {
        this.dequeueCarpenterFromStorage(carpenter, this.qCarpsInStorageA);
    }

    public void takeCarpenterBFromStorage(AnimatedEntity carpenter) {
        this.dequeueCarpenterFromStorage(carpenter, this.qCarpsInStorageB);
    }

    public void takeCarpenterCFromStorage(AnimatedEntity carpenter) {
        this.dequeueCarpenterFromStorage(carpenter, this.qCarpsInStorageC);
    }

    public IAnimator getAnimator() {
        return animator;
    }

    public void setAnimator(IAnimator animator) {
        this.animator = animator;
    }

    private void enqueueCarpenterInStorage(AnimatedEntity animatedCarpenter, AnimQueue queue) {
        animatedCarpenter.setLabelsVisible(false);
        animatedCarpenter.setPosition(0,0);
        queue.insert(animatedCarpenter);
    }

    private void dequeueCarpenterFromStorage(AnimatedEntity animatedCarpenter, AnimQueue queue) {
        animatedCarpenter.setLabelsVisible(true);
        animatedCarpenter.setPosition(0,0);
        queue.remove(animatedCarpenter);
    }

    private void initDesks() {
        AnimImageItem[] animDesks = new AnimImageItem[this.desksCount];
        for (int deskId = 0; deskId < this.desksCount; deskId++) {
            animDesks[deskId] = ImgResources.createDesk(getDeskBaseX(deskId), getDeskBaseY(deskId));
            this.animator.register(animDesks[deskId]);
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
