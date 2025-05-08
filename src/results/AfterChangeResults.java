package results;

public class AfterChangeResults extends SimResults {
    private double simTime;

    public AfterChangeResults(long experimentNum, double simTime) {
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
