package results;

public class FurnitureModel {
    private int orderID;
    private String productID;
    private int deskID;
    private double created;
    private String step;
    private String productType;
    private int assignedCarpenterID = -1;
    private double stepStart = -1; // of current step
    private double stepEnd = -1;   // of current step
    private double waitingBT = -1;
    private double orderCreatedAt = -1;

    public FurnitureModel(int orderID, int deskID, double created, String productType, String step, String productID) {
        this.orderID = orderID;
        this.deskID = deskID;
        this.created = created;
        this.step = step;
        this.productType = productType;
        this.productID = productID;
    }

    public double getMyOrderCreatedAt() {
        return orderCreatedAt;
    }

    public void setMyOrderCreatedAt(double orderCreatedAt) {
        this.orderCreatedAt = orderCreatedAt;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getDeskID() {
        return deskID;
    }

    public String getStep() {
        return step;
    }

    public double getCreated() {
        return created;
    }

    public String getProductType() {
        return productType;
    }

    public int getAssignedCarpenterID() {
        return assignedCarpenterID;
    }

    public double getStepStart() {
        return stepStart;
    }

    public double getStepEnd() {
        return stepEnd;
    }

    public double getWaitingBT() {
        return waitingBT;
    }

    public void setAssignedCarpenterID(int assignedCarpenterID) {
        this.assignedCarpenterID = assignedCarpenterID;
    }

    public void setStepStart(double stepStart) {
        this.stepStart = stepStart;
    }

    public void setStepEnd(double stepEnd) {
        this.stepEnd = stepEnd;
    }

    public void setWaitingBT(double waitingBT) {
        this.waitingBT = waitingBT;
    }

    public String getProductID() {
        return this.productID;
    }

}
