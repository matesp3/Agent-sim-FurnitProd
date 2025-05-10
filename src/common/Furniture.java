package common;

import OSPAnimator.Anim;
import OSPAnimator.AnimImageItem;
import OSPAnimator.AnimTextItem;
import OSPAnimator.IAnimator;
import animation.AnimatedEntity;
import animation.ImgResources;
import contracts.IAnimatedEntity;
import utils.DoubleComp;
import utils.Formatter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Furniture implements IAnimatedEntity {

    public enum Type {
        TABLE, CHAIR, WARDROBE;

    }
    public enum TechStep {
        WOOD_PREPARATION("Wood Prep."), CARVING("Carving"),
        STAINING("Staining"), LACQUERING("Lacquering"),
        ASSEMBLING("Assembling"), FIT_INSTALLATION("Fit. Inst.");
        private final String description;
        TechStep(String d) {
            this.description = d;
        }

        @Override
        public String toString() {
            return this.description;
        }
    }

    public enum TIME_UNIT {
        SECONDS(1), MINUTES(60), HOURS(3600), DAYS(3600*8);
        private final double secs;

        TIME_UNIT(int secs) {
            this.secs = secs;
        }
    }
    private final Order order;
    private final String productID;
    private final Type productType;
    private final boolean lacqueringRequired;
    private int deskID;
    private double stepBT;

    private double stepET;
    private double processingBT;
    private double waitingBT;
    private double timeCompleted;
    private TechStep step;
    // anim
    private AnimatedFurniture animFurniture;

    /**
     * Technological step is automatically {@code null}.
     * @param order order to which this furniture instance belongs
     * @param productID is unique identifier of furniture product within system in format {orderID}-{nr. of product in order}
     */
    public Furniture(Order order, String productID, Type furnitureType, boolean lacquering) {
        this.order = order;
        this.productID = productID;
        this.productType = furnitureType;
        this.lacqueringRequired = lacquering;

        this.processingBT = -1;
        this.deskID = -1;
        this.stepBT = -1;
        this.stepET = -1;
        this.waitingBT = -1;
        this.timeCompleted = -1;
        this.step = null;
        this.animFurniture = null;
    }
    /**
     * @return unique identifier of order
     */
    public int getOrderID() {
        return this.order.getOrderID();
    }

    /**
     * @return time of its corresponding order creating.
     */
    public double getMyOrderCreatedAt() {
        return this.order.getCreatedAt();
    }

    public Order getMyOrder() {
        return this.order;
    }

    /**
     * @return unique identifier of furniture product within whole system
     */
    public String getProductID() {
        return this.productID;
    }

    /**
     * Sets desk ID to this product. On this desk ID will be all technological steps of this product executed
     */
    public void setDeskID(int deskID) {
        if (this.deskID > -1)
            throw new RuntimeException("DeskID has already been set (it is set only once)");
        this.deskID = deskID;
        if (this.animFurniture != null)
            this.animFurniture.renderEntity();
    }

    /**
     * @return desk ID on which are all technological steps of this product instance executed
     */
    public int getDeskID() {
        return this.deskID;
    }

    public TechStep getStep() {
        return this.step;
    }

    public double getStepBT() {
        return stepBT;
    }

    public double getStepET() {
        return stepET;
    }

    /**
     * This new {@code techStep} must be consecutive to the one retrieved by {@code getNextTechStep()}.
     */
    public void setStep(TechStep techStep) {
        this.stepCheck(techStep);
        this.step = techStep;
        if (this.animFurniture != null)
            this.animFurniture.renderEntity();
    }

    public void setStepBT(double time) {
        this.stepBT = time;
        this.stepET = -1;
        if (this.animFurniture != null)
            this.animFurniture.renderEntity();
    }

    public void setStepBT(TechStep next, double time) {
        this.stepCheck(next);
        this.step = next;
        this.setStepBT(time);
        if (this.animFurniture != null)
            this.animFurniture.renderEntity();
    }

    public void setStepET(double time) {
        this.stepET = time;
        if (this.animFurniture != null)
            this.animFurniture.renderEntity();
    }

    public boolean isLacqueringRequired() {
        return this.lacqueringRequired;
    }

    /**
     * @return time of whole product processing or <code>RuntimeException</code>, if this product hasn't been
     * completely processed yet.
     */
    public double getOverallTime() {
        if (timeCompleted < 0)
            throw new RuntimeException("product hasn't been completed yet.");
        return this.timeCompleted - this.processingBT;
    }

    public Type getProductType() {
        return this.productType;
    }

    /**
     * @param processingBT time when this furniture's creation has started
     */
    public void setProcessingBT(double processingBT) {
        if (DoubleComp.compare(this.processingBT, 0) == 1)
            throw new RuntimeException("ProcessingBT has already been set (it is set only once)");
        this.processingBT = processingBT;
        if (this.animFurniture != null)
            this.animFurniture.renderEntity();
    }

    /**
     * @return time when this product's creating started
     */
    public double getProcessingBT() {
        return this.processingBT;
    }

    /**
     * @param timeCompleted when was product is fully completed
     */
    public void setTimeCompleted(double timeCompleted) {
        this.timeCompleted = timeCompleted;
        if (this.animFurniture != null)
            this.animFurniture.renderEntity();
    }

    /**
     * @return timeCompleted when was product fully completed, or {@code -1}, it is not completed yet
     */
    public double getTimeCompleted() {
        return this.timeCompleted;
    }

    /**
     * @param waitingBT time of waiting beginning for next technological step. For invalidating, it should be used
     *                  value {@code -1} in param.
     */
    public void setWaitingBT(double waitingBT) {
        this.waitingBT = waitingBT;
        if (this.animFurniture != null)
            this.animFurniture.renderEntity();
    }

    /**
     * @return time of waiting beginning for next technological step
     */
    public double getWaitingBT() {
        return this.waitingBT;
    }

    /**
     * @return {@code true} if it has valid time of completion, else {@code false}
     */
    public boolean isCompleted() {
        return DoubleComp.compare(this.timeCompleted, 0) == 1;
    }

    /**
     * @return {@code true} if processing of this furniture product has not started yet, else {@code false}
     */
    public boolean isUnstarted() {
        return DoubleComp.compare(this.processingBT, 0) == -1;
    }

    @Override
    public String toString() {
        return String.format("Product: [productID=%7s, orderID=%5d ;desk=%5d; step=%-16s ;%-8s ;waitingBT=%s; stepBT=%s; setET=%s; [started=%s => completed=%s]",
                this.productID, this.order.getOrderID(), this.deskID, this.step, this.productType,
                Formatter.getStrDateTime(this.waitingBT, 8, 6),
                Formatter.getStrDateTime(this.stepBT, 8, 6),
                Formatter.getStrDateTime(this.stepET, 8, 6),
                Formatter.getStrDateTime(this.processingBT, 8, 6),
                Formatter.getStrDateTime(this.timeCompleted, 8, 6)
        );
    }

    private void stepCheck(TechStep newStep) {
        if (this.step == null) {
            if (newStep != TechStep.WOOD_PREPARATION)
                throw new IllegalArgumentException("WOOD_PREPARATION is very first step always.");
            return;
        }
        if (newStep == TechStep.FIT_INSTALLATION && this.productType != Type.WARDROBE)
            throw new IllegalArgumentException("FIT_INSTALLATION is used just for WARDROBE process.");
        if (newStep.ordinal() <= this.step.ordinal()) // causality check
            throw new IllegalArgumentException("Steps causality of product violated [currentStep="+this.step +";newStep="+newStep+"]");
    }

    @Override
    public AnimatedEntity initAnimatedEntity() {
        if (this.animFurniture == null)
            this.animFurniture = new AnimatedFurniture(this);
        return this.animFurniture;
    }

    @Override
    public AnimatedEntity getAnimatedEntity() {
        return this.animFurniture;
    }

    @Override
    public void removeAnimatedEntity(IAnimator from) {
        if (this.animFurniture != null) {
            this.animFurniture.unregisterEntity(from);
            this.animFurniture = null;
        }
    }

    public static class AnimatedFurniture extends AnimatedEntity {
        private AnimTextItem txtTechStep;
        private Furniture f;

        public AnimatedFurniture(Furniture f) {
            this.f = f;
            try {
                BufferedImage imgFurniture;
                switch (f.getProductType()) {
                    case TABLE -> {
                        imgFurniture = ImageIO.read(new File(ImgResources.IMG_PATH_TABLE));
                        super.setImage(imgFurniture, ImgResources.WIDTH_TABLE, ImgResources.HEIGHT_TABLE);
                    }
                    case CHAIR -> {
                        imgFurniture = ImageIO.read(new File(ImgResources.IMG_PATH_CHAIR));
                        super.setImage(imgFurniture, ImgResources.WIDTH_CHAIR, ImgResources.HEIGHT_CHAIR);
                    }
                    case WARDROBE -> {
                        imgFurniture = ImageIO.read(new File(ImgResources.IMG_PATH_WARDROBE));
                        super.setImage(imgFurniture, ImgResources.WIDTH_WARDROBE, ImgResources.HEIGHT_WARDROBE);
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.txtTechStep = new AnimTextItem(this.getStatus());
            super.setToolTip(this.getFurnitureInfo());
            this.txtTechStep.setColor(new Color(255, 242, 137));
            super.setZIndex(1);
        }

        @Override
        public void registerEntity(IAnimator animator) {
            animator.register(this.txtTechStep);
            animator.register(this); // img
        }

        @Override
        public void renderEntity() {
            this.txtTechStep.setText(this.getStatus());
            super.setToolTip(this.getFurnitureInfo());
        }

        @Override
        public void unregisterEntity(IAnimator animator) {
            animator.remove(this.txtTechStep);
            animator.remove(this);
            this.txtTechStep.remove();
            super.remove(); // img
        }

        @Override
        public void setLabelsVisible(double inTime, boolean visible) {
            this.txtTechStep.setVisible(inTime, visible);
        }

        @Override
        public void setLabelsVisible(boolean visible) {
            this.txtTechStep.setVisible(visible);
        }

        @Override
        public Anim moveTo(double startTime, double duration, double x, double y) {
            this.txtTechStep.moveTo(startTime, duration, x - 1.5, y - 14);
            return super.moveTo(startTime, duration, x, y); // img
        }

        @Override
        public Anim setPosition(double x, double y) {
            this.txtTechStep.setPosition(x - 1.5, y - 14);
            return super.setPosition(x, y); // img
        }

        @Override
        public double getWidth() {
            return Math.max(super.getWidth(), this.txtTechStep.getWidth());
        }

        @Override
        public double getHeight() {
            return super.getHeight()+this.txtTechStep.getHeight();
        }

        private String getStatus() {
            return String.format("[ID=%s] %10s", f.getProductID(), (f.step != null ? f.step.toString() : "Waiting"));
        }

        @Override
        public void setZIndex(int zIndex) {
            this.txtTechStep.setZIndex(zIndex);
            super.setZIndex(zIndex);
        }

        private String getFurnitureInfo() {
            return String.format("Furniture [%-7s]:\n * created: %s\n * orderID: %d\n * deskID: %d\n * step: %s\n * product: %-8s\n * waitingBT: %s\n * step-start: %s\n * step-end: %s\n * started: %s\n * completed: %s",
                    f.productID, Formatter.getStrDateTime(f.getMyOrderCreatedAt(), 8, 6),
                    f.order.getOrderID(), f.deskID, f.step, f.productType.toString(),
                    Formatter.getStrDateTime(f.waitingBT, 8, 6),
                    Formatter.getStrDateTime(f.stepBT, 8, 6),
                    Formatter.getStrDateTime(f.stepET, 8, 6),
                    Formatter.getStrDateTime(f.processingBT, 8, 6),
                    Formatter.getStrDateTime(f.timeCompleted, 8, 6)
            );
        }
    }

    //  -   -   -   -   -   -   M A I N -   -   -   -   -   -   -

    public static void main(String[] args) throws InterruptedException {
        Order order = new Order(1, 2500);
        Furniture product = new Furniture(order, ("" + order.getOrderID() + "-" + 1), Type.WARDROBE, true);
        System.out.println(product);
        product.setProcessingBT(4.5);
        product.setStepBT(TechStep.WOOD_PREPARATION, 4.5);
        product.setStepET(5.8);
        System.out.println(product);
        product.setStepBT(TechStep.CARVING, 17);
        product.setStepET(24);
        System.out.println(product);
        product.setStepBT(TechStep.STAINING, 27);
        product.setStepET(30);
        System.out.println(product);
        product.setStepBT(TechStep.LACQUERING, 32);
        product.setStepET(40);
        System.out.println(product);
        product.setStepBT(TechStep.ASSEMBLING, 32);
        product.setStepET(40);
        System.out.println(product);
        product.setStepBT(TechStep.FIT_INSTALLATION, 32);
        product.setStepET(40);
        product.setTimeCompleted(40);
        System.out.println(product);
        System.out.println("Complete dur:" + product.getOverallTime());
        System.out.println("Completion time:" + product.getTimeCompleted());
    }
}
