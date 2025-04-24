package results;

public class AfterEventResults extends SimResults {
    private double simTime;

    public AfterEventResults(long experimentNum, double simTime) {
        super(experimentNum);
        this.simTime = simTime;
    }

    public double getSimTime() {
        return simTime;
    }

    public void setSimTime(double simTime) {
        this.simTime = simTime;
    }
}
