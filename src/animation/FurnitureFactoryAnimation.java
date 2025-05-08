package animation;

import OSPAnimator.*;
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

    private static final Point2D STORAGE_UNSTARTED_POS_START = new Point2D.Double(STORAGE_Q_CARPS_A_START.getX() - 200, STORAGE_Q_CARPS_A_START.getY() + ImgResources.HEIGHT_CARPENTER+10);
    private static final Point2D STORAGE_UNSTARTED_POS_END = new Point2D.Double(STORAGE_UNSTARTED_POS_START.getX()+10, STORAGE_UNSTARTED_POS_START.getY());

    private static final Point2D STORAGE_STARTED_POS_START = new Point2D.Double(STORAGE_UNSTARTED_POS_START.getX() - 20, STORAGE_UNSTARTED_POS_START.getY() + W+10);
    private static final Point2D STORAGE_STARTED_POS_END = new Point2D.Double(STORAGE_STARTED_POS_START.getX()+10, STORAGE_STARTED_POS_START.getY());

    private IAnimator animator;

    private final CarpenterStorageQueue carpQA;
    private final CarpenterStorageQueue carpQB;
    private final CarpenterStorageQueue carpQC;
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

        this.carpQA = new CarpenterStorageQueue(50, STORAGE_Q_CARPS_A_START.getX(), STORAGE_Q_CARPS_A_START.getY());
        this.carpQB = new CarpenterStorageQueue(50, STORAGE_Q_CARPS_B_START.getX(), STORAGE_Q_CARPS_B_START.getY());
        this.carpQC = new CarpenterStorageQueue(50, STORAGE_Q_CARPS_C_START.getX(), STORAGE_Q_CARPS_C_START.getY());

        AnimTextItem txtQWaitingLabel1 = new AnimTextItem("Unstarted Orders Waiting for creating:");
        txtQWaitingLabel1.setPosition(STORAGE_UNSTARTED_POS_START.getX(), STORAGE_UNSTARTED_POS_START.getY() - 15);
        txtQWaitingLabel1.setColor(new Color(37, 119, 32));
        animator.register(txtQWaitingLabel1);
        this.initDesks();
    }

    public void enqueueFurnitureInStorage(AnimatedEntity furniture, boolean alreadyStartedOrder) {
        furniture.setPosition(STORAGE_UNSTARTED_POS_START); // first anim - bcs of nullptrException
    }

    public void placeFurnitureOnDesk(int deskId, AnimatedEntity furniture) {
        double x = getDeskBaseX(deskId) + ((ImgResources.WIDTH_DESK - furniture.getWidth()) / 3.75);
        double y = getDeskBaseY(deskId) + DESK_HOVER_OFFSET_Y - furniture.getHeight();
        furniture.setPosition(x, y);
    }

    public void moveFurnitureToDesk(int deskId, AnimatedEntity furniture, double startTime, double movingDur) {
        double x = getDeskBaseX(deskId) + ((ImgResources.WIDTH_DESK - furniture.getWidth()) / 3.75);
        double y = getDeskBaseY(deskId) + DESK_HOVER_OFFSET_Y - furniture.getHeight();
        furniture.moveTo(startTime, movingDur, x, y);
    }

    public void moveCarpenterToOtherDesk(int deskId, AnimatedEntity carpenter, double startTime, double dur) {
        double x = getDeskBaseX(deskId) - ImgResources.WIDTH_CARPENTER + 25;
        double y = getDeskBaseY(deskId) + ImgResources.HEIGHT_DESK - ImgResources.HEIGHT_CARPENTER + 15;
        carpenter.moveTo(startTime, dur, x, y);
    }

    public void moveCarpenterAToStorage(AnimatedEntity carpenter, double startTime, double movingDur) {
        this.carpQA.enqueue(carpenter, startTime, movingDur);
    }

    public void placeCarpenterAToStorage(AnimatedEntity carpenter) {
        this.carpQA.enqueue(carpenter);
    }

    public void placeCarpenterBToStorage(AnimatedEntity carpenter) {
        this.carpQB.enqueue(carpenter);
    }

    public void placeCarpenterCToStorage(AnimatedEntity carpenter) {
        this.carpQC.enqueue(carpenter);
    }

    public void moveCarpenterAToDesk(int deskID, AnimatedEntity animatedEntity, double v, double dur) {
        this.carpQA.dequeue(animatedEntity, v, dur, getDeskBaseX(deskID), getDeskBaseY(deskID));
    }

    public void moveCarpenterBToDesk(int deskID, AnimatedEntity animatedEntity, double v, double dur) {
        this.carpQB.dequeue(animatedEntity, v, dur, getDeskBaseX(deskID), getDeskBaseY(deskID));
    }

    public void moveCarpenterCToDesk(int deskID, AnimatedEntity animatedEntity, double v, double dur) {
        this.carpQC.dequeue(animatedEntity, v, dur, getDeskBaseX(deskID), getDeskBaseY(deskID));
    }

    public IAnimator getAnimator() {
        return animator;
    }

    public void setAnimator(IAnimator animator) {
        this.animator = animator;
    }

    public void clear() {
    }

    private void initDesks() {
        AnimImageItem[] animDesks = new AnimImageItem[this.desksCount];
        for (int deskId = 0; deskId < this.desksCount; deskId++) {
            animDesks[deskId] = ImgResources.createDesk(getDeskBaseX(deskId), getDeskBaseY(deskId));
            this.animator.register(animDesks[deskId]);
        }
    }

    private class CarpenterStorageQueue {
        static final double GAP = ImgResources.WIDTH_CARPENTER*5/4.0;
        final double baseX;
        final double baseY;
        AnimatedEntity[] queue;
        int free;
        CarpenterStorageQueue(int capacity, double x, double y) {
            this.free = 0;
            this.baseX = x;
            this.baseY = y;
            this.queue = new AnimatedEntity[capacity];
        }
        
        public void enqueue(AnimatedEntity e, double timeStart, double dur) {
            double x = this.calcX(this.free);
            this.queue[this.free] = e;
            e.moveTo(timeStart, dur, x, baseY);
            this.free++;
        }

        public void enqueue(AnimatedEntity e) {
            double x = this.calcX(this.free);
            this.queue[this.free] = e;
            e.setPosition(x, baseY);
            this.free++;
        }
        
        public void dequeue(AnimatedEntity e, double timeStart, double dur, double destX, double destY) {
            e.moveTo(timeStart, dur, destX, destY);
            this.moveRemaining(this.idxOf(e)+1, timeStart);
        }
        
        private int idxOf(AnimatedEntity e) {
            for (int j = 0; j < this.queue.length; j++) {
                if (this.queue[j] == e)
                    return j;
            }
            throw new RuntimeException("Entity not found in queue");
        }
        
        private void moveRemaining(int from, double timeStart) {
            double x;
            for (int i = from; i < this.free; i++) {
                x = this.calcX(i) - GAP;
                this.queue[i-1] = this.queue[i];
                this.queue[i].moveTo(timeStart, 1, x, baseY);
            }
            this.free--; // moved towards
            this.queue[free] = null;
        }
        
        private double calcX(int i) {
            return baseX + i*GAP;
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
