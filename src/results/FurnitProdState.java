package results;

import common.Carpenter;
import common.Furniture;
import common.Order;

import java.util.*;

public class FurnitProdState extends AfterChangeResults {
    private final List<CarpenterModel> carpentersA = new ArrayList<>();
    private final List<CarpenterModel> carpentersB = new ArrayList<>();
    private final List<CarpenterModel> carpentersC = new ArrayList<>();
    private final List<FurnitureModel> qUnstarted = new ArrayList<>();
    private final List<FurnitureModel> qStarted = new ArrayList<>();
    private final List<FurnitureModel> qStaining = new ArrayList<>();
    private final List<FurnitureModel> qAssembling = new ArrayList<>();
    private final List<FurnitureModel> qFittings = new ArrayList<>();
    private final List<StatResult.Simple> stats = new ArrayList<>(17);

    private StatResult.Simple unsOrdersCount = new StatResult.Simple("Amount of waiting for 'order processing'", -1, "[qty]");
    private StatResult.Simple unsProductsCount = new StatResult.Simple("Amount of waiting for 'furniture processing'", -1, "[qty]");
    private StatResult.Simple stainingCount = new StatResult.Simple("Amount of waiting for 'staining", -1, "[qty]");
    private StatResult.Simple assemblingCount = new StatResult.Simple("Amount of waiting for 'assembling'", -1, "[qty]");
    private StatResult.Simple fittingsInstCount = new StatResult.Simple("Amount of waiting for 'fittings inst.'", -1, "[qty]");

    private StatResult.Simple unsOrdersTime = new StatResult.Simple("Waiting time for 'order processing'", -1, "[s]");
    private StatResult.Simple unsProductsTime = new StatResult.Simple("Waiting time for 'furniture processing'", -1, "[s]");
    private StatResult.Simple stainingTime = new StatResult.Simple("Waiting time for 'staining'", -1, "[s]");
    private StatResult.Simple assemblingTime = new StatResult.Simple("Waiting time for 'assembling'", -1, "[s]");
    private StatResult.Simple fittingInstTime = new StatResult.Simple("Waiting time for 'fittings inst.'", -1, "[s]");

    private StatResult.Simple utilzA = new StatResult.Simple("Utilization of group A:", -1, "%");
    private StatResult.Simple utilzB = new StatResult.Simple("Utilization of group B:", -1, "%");
    private StatResult.Simple utilzC = new StatResult.Simple("Utilization of group C:", -1, "%");

    private StatResult.Simple orderTimeInSystem = new StatResult.Simple("Order's time in system", -1, "[s]");

    public FurnitProdState(long experimentNum, double simTime) {
        super(experimentNum, simTime);
        this.stats.add(unsOrdersCount);
        this.stats.add(unsProductsCount);
        this.stats.add(assemblingCount);
        this.stats.add(stainingCount);
        this.stats.add(fittingsInstCount);
        this.stats.add(unsOrdersTime);
        this.stats.add(unsProductsTime);
        this.stats.add(stainingTime);
        this.stats.add(assemblingTime);
        this.stats.add(fittingInstTime);
        this.stats.add(utilzA);
        this.stats.add(utilzB);
        this.stats.add(utilzC);
        this.stats.add(orderTimeInSystem);
    }

    public void carpentersAllocation(int a, int b, int c) {
        while (a > this.carpentersA.size()) {
            this.carpentersA.add(null);
        }
        while (b > this.carpentersB.size()) {
            this.carpentersB.add(null);
        }
        while (c > this.carpentersC.size()) {
            this.carpentersC.add(null);
        }
    }

    public List<StatResult.Simple> getStats() {
        return stats;
    }

    public StatResult.Simple getUnsProductsTime() {
        return unsProductsTime;
    }

    public void setUnsProductsTime(double unsProductsTime) {
        this.unsProductsTime.setValue(unsProductsTime);
    }

    public StatResult.Simple getUnsOrdersCount() {
        return unsOrdersCount;
    }

    public void setUnsOrdersCount(double unsOrdersCount) {
        this.unsOrdersCount.setValue(unsOrdersCount);
    }

    public StatResult.Simple getUnsProductsCount() {
        return unsProductsCount;
    }

    public void setUnsProductsCount(double unsProductsCount) {
        this.unsProductsCount.setValue(unsProductsCount);
    }

    public StatResult.Simple getAssemblingCount() {
        return assemblingCount;
    }

    public void setAssemblingCount(double assemblingCount) {
        this.assemblingCount.setValue(assemblingCount);
    }

    public StatResult.Simple getStainingCount() {
        return stainingCount;
    }

    public void setStainingCount(double stainingCount) {
        this.stainingCount.setValue(stainingCount);
    }

    public StatResult.Simple getFittingsInstCount() {
        return fittingsInstCount;
    }

    public void setFittingsInstCount(double fittingsInstCount) {
        this.fittingsInstCount.setValue(fittingsInstCount);
    }

    public StatResult.Simple getUnsOrdersTime() {
        return unsOrdersTime;
    }

    public void setUnsOrdersTime(double unsOrdersTime) {
        this.unsOrdersTime.setValue(unsOrdersTime);
    }

    public StatResult.Simple getAssemblingTime() {
        return assemblingTime;
    }

    public void setAssemblingTime(double assemblingTime) {
        this.assemblingTime.setValue(assemblingTime);
    }

    public StatResult.Simple getStainingTime() {
        return stainingTime;
    }

    public void setStainingTime(double stainingTime) {
        this.stainingTime.setValue(stainingTime);
    }

    public StatResult.Simple getFittingInstTime() {
        return fittingInstTime;
    }

    public void setFittingInstTime(double fittingInstTime) {
        this.fittingInstTime.setValue(fittingInstTime);
    }

    /**
     * @return utilization in %
     */
    public StatResult.Simple getUtilzA() {
        return utilzA;
    }

    /**
     * @param utilzA in %
     */
    public void setUtilzA(double utilzA) {
        this.utilzA.setValue(utilzA);
    }

    /**
     * @return utilization in %
     */
    public StatResult.Simple getUtilzB() {
        return utilzB;
    }

    /**
     * @param utilzB in %
     */
    public void setUtilzB(double utilzB) {
        this.utilzB.setValue(utilzB);
    }

    /**
     * @return utilization in %
     */
    public StatResult.Simple getUtilzC() {
        return utilzC;
    }

    /**
     * @param utilzC in %
     */
    public void setUtilzC(double utilzC) {
        this.utilzC.setValue(utilzC);
    }

    public StatResult.Simple getOrderTimeInSystem() {
        return orderTimeInSystem;
    }

    public void setOrderTimeInSystem(double orderTimeInSystem) {
        this.orderTimeInSystem.setValue(orderTimeInSystem);
    }

    public List<CarpenterModel> getCarpentersA() {
        return carpentersA;
    }

    public void setModelsCarpentersA(Carpenter[] carpenters) {
        this.setNewCarpenters(carpenters, this.carpentersA);
    }

    public List<CarpenterModel> getCarpentersB() {
        return carpentersB;
    }

    public void setModelsCarpentersB(Carpenter[] carpenters) {
        this.setNewCarpenters(carpenters, this.carpentersB);
    }

    public List<CarpenterModel> getCarpentersC() {
        return carpentersC;
    }

    public void setModelsCarpentersC(Carpenter[] carpenters) {
        this.setNewCarpenters(carpenters, this.carpentersC);
    }

    public List<FurnitureModel> getqUnstarted() {
        return qUnstarted;
    }

    public void addToqUnstarted(Furniture f) {
        this.qUnstarted.add(this.rawToModel(f));
    }

    public List<FurnitureModel> getqStarted() {
        return qStarted;
    }

    public void addToqStarted(Furniture f) {
        this.qStarted.add(this.rawToModel(f));
    }

    public List<FurnitureModel> getqAssembling() {
        return qAssembling;
    }

    public void addToqAssembling(Furniture f) {
        this.qAssembling.add(this.rawToModel(f));
    }

    public List<FurnitureModel> getqStaining() {
        return qStaining;
    }

    public void addToqStaining(Furniture f) {
        this.qStaining.add(this.rawToModel(f));
    }

    public List<FurnitureModel> getqFittings() {
        return qFittings;
    }

    public void addToqFittings(Furniture f) {
        this.qFittings.add(this.rawToModel(f));
    }

    public void clearProducts() {
        this.qUnstarted.clear();
        this.qStarted.clear();
        this.qStaining.clear();
        this.qAssembling.clear();
        this.qFittings.clear();
    }

    private void setNewCarpenters(Carpenter[] input, List<CarpenterModel> output) {
        for (int i = 0; i < input.length; i++) {
            output.set(i, this.rawToModel(input[i])); // size is the same for whole simulation
        }
    }

    private void setNewProducts(Queue<Order> input, List<FurnitureModel> output) {
        output.clear();
        for (Order order : input) {
            for (Furniture f : order.getProducts()) {
                output.add(this.rawToModel(f)); // size of structure may vary each time
            }
        }
    }

    private CarpenterModel rawToModel(Carpenter raw) {
        CarpenterModel r = new CarpenterModel(raw.getCarpenterId(), raw.getGroup().toString());
        r.setDeskID(raw.getCurrentDeskID());
        r.setAssignedProductID(raw.getAssignedProduct() == null ? "-1" : raw.getAssignedProduct().getProductID());
        r.setOrderBT(raw.getWorkBT());
        r.setOrderET(raw.getWorkET());
        r.setOrderRepresentation(raw.getAssignedProduct() == null ? "" : raw.getAssignedProduct().toString());
        r.setWorking(raw.isWorking());
        return r;
    }

    private FurnitureModel rawToModel(Furniture raw) {
        FurnitureModel r = new FurnitureModel(raw.getOrderID(), raw.getDeskID(), raw.getProcessingBT(),
                raw.getProductType().toString(), raw.getStep() == null ? null : raw.getStep().toString(), raw.getProductID());
        r.setWaitingBT(raw.getWaitingBT());
        r.setStepStart(raw.getStepBT());
        r.setStepEnd(raw.getStepET());
        r.setMyOrderCreatedAt(raw.getMyOrderCreatedAt());
        return r;
    }


}
