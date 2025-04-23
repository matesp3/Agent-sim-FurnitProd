package common;

import utils.DoubleComp;

public class Carpenter {
    public enum GROUP {
        A,B,C;
    }
    public static final int IN_STORAGE = -1;

    private final int carpenterId;
    private final GROUP group;
    private int deskID;
    private Furniture assignedProduct;
    private double productProcessingBT;
    private double productProcessingET;
    private double sumOfWorkingTime;

    public Carpenter(GROUP group, int carpenterID) {
        this.group = group;
        this.carpenterId = carpenterID;
        this.deskID = IN_STORAGE;
        this.productProcessingBT = -1;
        this.productProcessingET = -1;
        this.assignedProduct = null;
        this.sumOfWorkingTime = 0;
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
        if (this.isWorking() || this.deskID == IN_STORAGE)
            throw new RuntimeException("Carpenter is working (hasn't returned product yet) or is located in the storage");
        return this.productProcessingET - this.productProcessingBT;
    }

    /**
     * Sets new position of this carpenter.
     * @param deskID deskID of assigned product or {@code Carpenter.IN_STORAGE} constant if he is located in storage
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
     * storage.
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

    public static void main(String[] args) {
        Carpenter carpenter = new Carpenter(GROUP.A, 1);
        Order order = new Order(1, 2500);
        Furniture product = new Furniture(order, (order.getOrderID()+"-"+1), Furniture.Type.CHAIR);
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
