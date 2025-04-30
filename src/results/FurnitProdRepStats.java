package results;

import OSPStat.Stat;

import java.util.ArrayList;
import java.util.List;

public class FurnitProdRepStats extends SimResults {
    private final List<StatResult.ConfInterval> stats = new ArrayList<>(17);

    private StatResult.ConfInterval unsOrdersCount = new StatResult.ConfInterval("Amount of waiting for 'order processing'", -1, -1, "[qty]");
    private StatResult.ConfInterval unsProductsCount = new StatResult.ConfInterval("Amount of waiting for 'furniture processing'", -1, -1, "[qty]");
    private StatResult.ConfInterval stainingCount = new StatResult.ConfInterval("Amount of waiting for 'staining'", -1, -1, "[qty]");
    private StatResult.ConfInterval assemblingCount = new StatResult.ConfInterval("Amount of waiting for 'assembling'", -1, -1, "[qty]");
    private StatResult.ConfInterval fittingsCount = new StatResult.ConfInterval("Amount of waiting for 'fittings inst.'", -1, -1, "[qty]");

    private StatResult.ConfInterval unsOrdersTime = new StatResult.ConfInterval("Waiting time for 'order processing'", -1, -1, "[s]");
    private StatResult.ConfInterval unsProductsTime = new StatResult.ConfInterval("Waiting time for 'furniture processing'", -1, -1, "[s]");
    private StatResult.ConfInterval stainingTime = new StatResult.ConfInterval("Waiting time for 'staining'", -1, -1, "[s]");
    private StatResult.ConfInterval assemblingTime = new StatResult.ConfInterval("Waiting time for 'assembling'", -1, -1, "[s]");
    private StatResult.ConfInterval fittingsTime = new StatResult.ConfInterval("Waiting time for 'fittings inst.'", -1, -1, "[s]");

    private StatResult.ConfInterval utilizationGroupA = new StatResult.ConfInterval("Utilization of group A:", -1, -1, "%");
    private StatResult.ConfInterval utilizationGroupB = new StatResult.ConfInterval("Utilization of group B:", -1, -1, "%");
    private StatResult.ConfInterval utilizationGroupC = new StatResult.ConfInterval("Utilization of group C:", -1, -1, "%");

    private StatResult.ConfInterval orderTimeInSystem = new StatResult.ConfInterval("Order's time in system", -1, -1, "[s]");
    private StatResult.ConfInterval allocatedDesksCount;

    public FurnitProdRepStats(long experimentNum) {
        super(experimentNum);
        this.stats.add(this.unsOrdersCount);
        this.stats.add(this.unsProductsCount);
        this.stats.add(this.stainingCount);
        this.stats.add(this.assemblingCount);
        this.stats.add(this.fittingsCount);

        this.stats.add(this.unsOrdersTime);
        this.stats.add(this.unsProductsTime);
        this.stats.add(this.stainingTime);
        this.stats.add(this.assemblingTime);
        this.stats.add(this.fittingsTime);

        this.stats.add(this.utilizationGroupA);
        this.stats.add(this.utilizationGroupB);
        this.stats.add(this.utilizationGroupC);

        this.stats.add(orderTimeInSystem);
    }

    public StatResult.ConfInterval getUnsProductsCount() {
        return unsProductsCount;
    }

    public List<StatResult.ConfInterval> getStats() {
        return this.stats;
    }

    public void setUnsProductsCount(Stat s) {
        this.unsProductsCount.setCI(s.mean(), s.confidenceInterval_95()[1] - s.mean());
    }

    public StatResult.ConfInterval getUnsOrdersCount() {
        return unsOrdersCount;
    }

    public void setUnsOrdersCount(Stat s) {
        this.unsOrdersCount.setCI(s.mean(), s.confidenceInterval_95()[1] - s.mean());
    }

    public StatResult.ConfInterval getAssemblingCount() {
        return assemblingCount;
    }

    public void setAssemblingCount(Stat s) {
        this.assemblingCount.setCI(s.mean(), s.confidenceInterval_95()[1] - s.mean());
    }

    public StatResult.ConfInterval getStainingCount() {
        return stainingCount;
    }

    public void setStainingCount(Stat s) {
        this.stainingCount.setCI(s.mean(), s.confidenceInterval_95()[1] - s.mean());
    }

    public StatResult.ConfInterval getFittingsCount() {
        return fittingsCount;
    }

    public void setFittingsCount(Stat s) {
        this.fittingsCount.setCI(s.mean(), s.confidenceInterval_95()[1] - s.mean());
    }

    public StatResult.ConfInterval getUnsOrdersTime() {
        return unsOrdersTime;
    }

    public void setUnsOrdersTime(Stat s) {
        this.unsOrdersTime.setCI(s.mean(), s.confidenceInterval_95()[1] - s.mean());
    }

    public StatResult.ConfInterval getUnsProductsTime() {
        return unsProductsTime;
    }

    public void setUnsProductsTime(Stat s) {
        this.unsProductsTime.setCI(s.mean(), s.confidenceInterval_95()[1] - s.mean());
    }

    public StatResult.ConfInterval getAssemblingTime() {
        return assemblingTime;
    }

    public void setAssemblingTime(Stat s) {
        this.assemblingTime.setCI(s.mean(), s.confidenceInterval_95()[1] - s.mean());
    }

    public StatResult.ConfInterval getStainingTime() {
        return stainingTime;
    }

    public void setStainingTime(Stat s) {
        this.stainingTime.setCI(s.mean(), s.confidenceInterval_95()[1] - s.mean());
    }

    public StatResult.ConfInterval getFittingsTime() {
        return fittingsTime;
    }

    public void setFittingsTime(Stat s) {
        this.fittingsTime.setCI(s.mean(), s.confidenceInterval_95()[1] - s.mean());
    }

    public StatResult.ConfInterval getUtilizationGroupA() {
        return utilizationGroupA;
    }

    public void setUtilizationGroupA(Stat s) {
        this.utilizationGroupA.setCI(s.mean()*100, (s.confidenceInterval_95()[1] - s.mean())*100);
    }

    public StatResult.ConfInterval getUtilizationGroupB() {
        return utilizationGroupB;
    }

    public void setUtilizationGroupB(Stat s) {
        this.utilizationGroupB.setCI(s.mean()*100, (s.confidenceInterval_95()[1] - s.mean())*100);
    }

    public StatResult.ConfInterval getUtilizationGroupC() {
        return utilizationGroupC;
    }

    public void setUtilizationGroupC(Stat s) {
        this.utilizationGroupC.setCI(s.mean()*100, (s.confidenceInterval_95()[1] - s.mean())*100);
    }

    public StatResult.ConfInterval getOrderTimeInSystem() {
        return orderTimeInSystem;
    }

    public void setOrderTimeInSystem(Stat s) {
        this.orderTimeInSystem.setCI(s.mean(), s.confidenceInterval_95()[1] - s.mean());
    }

}
