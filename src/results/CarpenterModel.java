package results;

public class CarpenterModel {
    private int carpenterID;
    private int deskID;
    private String assignedProductID = null;
    private String group;
    private double orderBT = -1;
    private double orderET = -1;
    private boolean working = false;
    private String orderRepresentation;

    public CarpenterModel(int carpenterID, String group) {
        this.carpenterID = carpenterID;
        this.group = group;
    }

    public int getCarpenterID() {
        return carpenterID;
    }

    public String getGroup() {
        return group;
    }

    public String getOrderRepresentation() {
        return orderRepresentation;
    }

    public double getOrderBT() {
        return orderBT;
    }

    public void setOrderBT(double orderBT) {
        this.orderBT = orderBT;
    }

    public double getOrderET() {
        return orderET;
    }

    public void setOrderET(double orderET) {
        this.orderET = orderET;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public String getAssignedProductID() {
        return assignedProductID;
    }

    public void setAssignedProductID(String  assignedProductID) {
        this.assignedProductID = assignedProductID;
    }

    public int getDeskID() {
        return deskID;
    }

    public void setDeskID(int deskID) {
        this.deskID = deskID;
    }

    public void setOrderRepresentation(String orderRepresentation) {
        this.orderRepresentation = orderRepresentation;
    }
}
