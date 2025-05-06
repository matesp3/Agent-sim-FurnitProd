package common;

import OSPAnimator.Anim;
import OSPAnimator.AnimImageItem;
import animation.AnimatedEntity;
import animation.ImgResources;
import contracts.IAnimatedEntity;
import utils.DoubleComp;

public class Carpenter implements IAnimatedEntity {

    public enum GROUP {
        A,B,C;
    }
    public static final int IN_STORAGE = -1;
    public static final int TRANSFER_STORAGE = -2;
    public static final int TRANSFER_DESKS = -3;

    private final int carpenterId;
    private final GROUP group;
    private int deskID;
    private Furniture assignedProduct;
    private double productProcessingBT;
    private double productProcessingET;
    private double sumOfWorkingTime;
    // anim
    private AnimatedCarpenter animCarpenter;

    public Carpenter(GROUP group, int carpenterID) {
        this.group = group;
        this.carpenterId = carpenterID;
        this.deskID = IN_STORAGE;
        this.productProcessingBT = -1;
        this.productProcessingET = -1;
        this.assignedProduct = null;
        this.sumOfWorkingTime = 0;
        this.animCarpenter = new AnimatedCarpenter(this);
    }

    public void reset() {
        this.deskID = IN_STORAGE;
        this.productProcessingBT = -1;
        this.productProcessingET = -1;
        this.assignedProduct = null;
        this.sumOfWorkingTime = 0;
    }

    /**
     * Carpenter receives {@code product} for which will be executed specific technological step.
     * @param timeOfStart simulation time of assigning {@code product} to this carpenter's instance
     * @throws RuntimeException if Carpenter is already working
     * @throws IllegalArgumentException if order is null
     */
    public void receiveProduct(Furniture product, double timeOfStart) {
        if (this.isWorking())
            throw new RuntimeException("Carpenter is still working.. Cannot start processing of new order");
        if (product == null)
            throw new IllegalArgumentException("New product for processing not provided (product=null)");
        this.productProcessingET = -1;
        this.productProcessingBT = timeOfStart;
        this.assignedProduct = product;
//        product.setStepBT(timeOfStart);
    }

    /**
     * Carpenter returns product after executing specific technological step.
     * @param timeOfEnd simulation time of completing work on current product by this carpenter's instance
     */
    public Furniture returnProduct(double timeOfEnd) {
        if (!this.isWorking())
            throw new RuntimeException("Carpenter is not working, so he cannot end processing of order..");
        if (DoubleComp.compare(this.productProcessingBT, timeOfEnd) == 1) {
            throw new IllegalArgumentException("Order processing beginning > time of end of processing");
        }
        this.productProcessingET = timeOfEnd;
        this.sumOfWorkingTime += (this.productProcessingET - this.productProcessingBT);
        Furniture productToReturn = this.assignedProduct;
        this.assignedProduct = null;
        return productToReturn;
    }

    /**
     * Starts executing of assigned order's next step (which is assumed to be set!) in time specified by parameter
     * {@code timeOfStart}.
     */
    public void startExecutingStep(double timeOfStart) {
        if (!this.isWorking())
            throw new RuntimeException("Carpenter is not working, so he cannot start executing tech. step..");
        this.assignedProduct.setStepBT(timeOfStart);
    }

    /**
     * Ends executing of assigned order's next step (which is assumed to be set!) in time specified by parameter
     * {@code timeOfEnd}.
     */
    public void endExecutingStep(double timeOfEnd) {
        if (!this.isWorking())
            throw new RuntimeException("Carpenter is not working, so he cannot start executing tech. step..");
        this.assignedProduct.setStepET(timeOfEnd);
    }

    /**
     * @return amount of time of work on lastly processed product
     * @throws RuntimeException if carpenter is working right now or was not working at all
     */
    public double getLastWorkDuration() throws RuntimeException {
        if (this.isWorking() || this.deskID < 0)
            throw new RuntimeException("Carpenter is working (hasn't returned product yet) or is located in the storage");
        return this.productProcessingET - this.productProcessingBT;
    }

    /**
     * Sets new position of this carpenter.
     * @param deskID deskID of assigned product or {@code Carpenter.IN_STORAGE} constant if he is located in storage.
     *               If he is moving, it can be set one of these options: {@code Carpenter.TRANSFER_STORAGE}, {@code Carpenter.TRANSFER_DESKS}
     */
    public void setCurrentDeskID(int deskID) {
        this.deskID = deskID;
    }

    /**
     * @return group ID, in which is carpenter working.
     */
    public GROUP getGroup() {
        return this.group;
    }

    /**
     * @return unique identifier of carpenter
     */
    public int getCarpenterId() {
        return this.carpenterId;
    }

    /**
     * @return ID of desk where carpenter is standing right now or {@code Carpenter.IN_STORAGE} value if he is in wood
     * storage. If he's currently moving, one of these options is present:{@code Carpenter.TRANSFER_STORAGE}, {@code Carpenter.TRANSFER_DESKS}
     */
    public int getCurrentDeskID() {
        return this.deskID;
    }

    /**
     * @return product that is currently being processed by this carpenter's instance
     */
    public Furniture getAssignedProduct() {
        return this.assignedProduct;
    }

    /**
     * @return time of beginning of lastly processing product
     */
    public double getWorkBT() {
        return this.productProcessingBT;
    }

    /**
      * @return time of end of lastly processed product
     */
    public double getWorkET() {
        return this.productProcessingET;
    }

    /**
     * @return <code>true</code> if he is processing (if he owns) some instance of product
     */
    public boolean isWorking() {
        return this.assignedProduct != null;
    }

    /**
     * @param simEndTime signalizes the very last moment, up when should be sum of work time retrieved. WARNING! If
     *                   carpenter is working and has started this work later than {@code simEndTime}, then this method
     *                   produces incorrect result.
     * @return amount of overall time, that carpenter has worked from the start of simulation till this moment.
     */
    public double getSumOfWorkingTime(double simEndTime) {
        return this.isWorking() ? (this.sumOfWorkingTime + (simEndTime-this.productProcessingBT)) // trim last working duration
                                : this.sumOfWorkingTime;
    }

    /**
     * @return {@code true}, if {@code deskID} of carpenter = {@code Carpenter.IN_STORAGE}
     */
    public boolean isInStorage() {
        return this.deskID == IN_STORAGE;
    }

    /**
     * @return amount of overall time, that carpenter has worked (and completed his work, also) from the start of
     * simulation.
     */
    public double getSumOfCompletedWorkingTime() {
        return this.sumOfWorkingTime;
    }

    @Override
    public String toString() {
        return String.format("Carp{%s;carpID=%d;desk=%d;productID=%s}", this.group, this.carpenterId, this.deskID,
                this.isWorking() ? this.assignedProduct.getProductID() : null);
    }

    @Override
    public AnimatedEntity getAnimatedEntity() {
        return null;
    }

    public static class AnimatedCarpenter extends AnimatedEntity {
        private final AnimImageItem imgCarpenter;
        private final Carpenter c;

        public AnimatedCarpenter(Carpenter carpenter) {
            this.c = carpenter;
            this.imgCarpenter = switch (carpenter.group) {
                case A -> ImgResources.createCarpenterA();
                case B -> ImgResources.createCarpenterB();
                case C -> ImgResources.createCarpenterC();
            };
        }

        @Override
        public void renderEntity() {
            // tato metoda sa bude volat z logiky a zoberie si hodnoty atributov - zabezpeci sa aktualizacia textu
            // o PRESUN entit sa budu starat MANAZERI. Atributy budu aktualizovat metody zdedene z AnimatedEntity
        }

        @Override
        public void removeEntity() {

        }

        @Override
        public Anim moveTo(double startTime, double duration, double x, double y) {
            return super.moveTo(startTime, duration, x, y);
        }

        @Override
        public Anim setPosition(double x, double y) {
            return super.setPosition(x, y);
        }

        @Override
        public double getWidth() {
            return super.getWidth();
        }

        @Override
        public double getHeight() {
            return super.getHeight();
        }
    }










    public static void main(String[] args) {
        Carpenter carpenter = new Carpenter(GROUP.A, 1);
        Order order = new Order(1, 2500);
        Furniture product = new Furniture(order, (order.getOrderID()+"-"+1), Furniture.Type.CHAIR, true);
        product.setDeskID(1);
        carpenter.setCurrentDeskID(1);
        System.out.println(carpenter.getGroup());
        System.out.println(carpenter.isWorking());
        System.out.println(carpenter.getWorkBT());
        carpenter.receiveProduct(product,5.0);
        System.out.println(carpenter.getWorkBT());
        System.out.println(carpenter.isWorking());
//        System.out.println(carpenter.getLastlyProcessedOrderDuration()); // ok
        carpenter.returnProduct(56.4);
        System.out.println(carpenter.getWorkET());
        System.out.println(carpenter.isWorking());
        System.out.println(carpenter.getLastWorkDuration());
        carpenter.receiveProduct(product,60.0);
        System.out.println(carpenter.getWorkBT());
        System.out.println(carpenter.isWorking());
         // ok
    }
}
