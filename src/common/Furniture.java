package common;

import utils.DoubleComp;
import utils.Formatter;

public class Furniture {

    public enum Type {
        TABLE, CHAIR, WARDROBE;
    }


    public enum TechStep {
        WOOD_PREPARATION, CARVING, STAINING, PAINTCOAT, ASSEMBLING, FIT_INSTALLATION //, DRYING - if added, need to be fit where it belongs chronologically
        ;
    }
    private final Order order;

    private final String productID;
    private final Type productType;
    private int deskID;

    private double stepBT;
    private double stepET;
    private double processingBT;
    private double waitingBT;
    private double timeCompleted;
    private TechStep step;
    /**
     * Technological step is automatically {@code null}.
     * @param order order to which this furniture instance belongs
     * @param productID is unique identifier of furniture product within system in format {orderID}-{nr. of product in order}
     */
    public Furniture(Order order, String productID, Type furnitureType) {
        this.order = order;
        this.productID = productID;
        this.productType = furnitureType;

        this.processingBT = -1;
        this.deskID = -1;
        this.stepBT = -1;
        this.stepET = -1;
        this.waitingBT = -1;
        this.timeCompleted = -1;
        this.step = null;
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

    public Order getOrder() {
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
    }

    public void setStepBT(double time) {
        this.stepBT = time;
    }

    public void setStepBT(TechStep next, double time) {
        this.stepCheck(next);
        this.step = next;
        this.stepBT = time;
    }

    public void setStepET(double time) {
        this.stepET = time;
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
    }

    /**
     * @return timeCompleted when was product fully completed, or {@code -1}, it is not completed yet
     */
    public double getTimeCompleted() {
        return this.timeCompleted;
    }

    /**
     * @param waitingBT time of waiting beginning for next technological step
     */
    public void setWaitingBT(double waitingBT) {
        this.waitingBT = waitingBT;
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
        return String.format("Product: [productID=%7s, orderID=%5d ;desk=%5d; step=%-16s ;waitingBT=%s; stepBT=%s; setET=%s; %-8s; [started=%s => completed=%s]",
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

    public static void main(String[] args) throws InterruptedException {
        Order order = new Order(1, 2500);
        Furniture product = new Furniture(order, ("" + order.getOrderID() + "-" + 1), Type.WARDROBE);
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
        product.setStepBT(TechStep.PAINTCOAT, 32);
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
