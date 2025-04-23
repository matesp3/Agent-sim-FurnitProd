package controllers;

import OSPABA.Simulation;
import gui.FurnitureProdForm;
import simulation.Id;
import simulation.MySimulation;
import utils.DoubleComp;

/**
 * Controller is used for communication with business logic (some type of Simulation).
 */
public class FurnitProdSimController {
    private final FurnitureProdForm gui;
    private MySimulation sim;
    private boolean simRunning; // true if it's stopped, also
    private boolean maxSpeedOn;
    private boolean consoleLogsOn;
    private double shiftTime; // seconds
    private double sleepTime; // milliseconds

    public FurnitProdSimController(FurnitureProdForm gui) {
        this.gui = gui;
        this.sim = null;
        this.simRunning = false;
        this.maxSpeedOn = true;
        this.consoleLogsOn = false;
        this.shiftTime = 10;      // sim-seconds
        this.sleepTime = 0.1;  // real-seconds
    }

    public boolean isSimRunning() {
        return this.simRunning;
    }

    public void launchSimulation(int groupA, int groupB, int groupC, int experiments, double simulatedDays) {
        Runnable r = () -> {
            try {
//                this.sim = new FurnitureProductionSim(experiments, groupA, groupB, groupC, simulatedDays*8*3600);// 3600s*8hod*sim_dni [secs] OLD
                this.sim = new MySimulation();// 3600s*8hod*sim_dni [secs] NEW
                this.sim.registerDelegate(this.gui);
//                --
//                this.sim.setShiftTime(this.shiftTime); // even if maxSpeed is enabled, due to caching
//                this.sim.setSleepTime(this.sleepTime);
//                if (maxSpeedOn)
//                    this.sim.setEnabledMaxSimSpeed(true);
////                --
//                this.sim.setDebugMode(this.consoleLogsOn);
//                this.sim.setSimSpeed(3600, 750);
                this.changeSimSpeed();
                this.sim.onReplicationDidFinish(e -> System.out.println(e.currentReplication()) );
//                this.sim.setMaxSimSpeed();
                this.sim.simulate(experiments, simulatedDays*8*3600);
                this.simRunning = false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        Thread t = new Thread(r, "Thread-Main");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
        this.simRunning = true;
    }

    public void changeSimSpeed() {
//        this.setShiftAndSleepTime(this.shiftTime, this.sleepTime);
        sim.setSimSpeed(this.shiftTime, this.sleepTime);
    }

    public void terminateSimulation() {
        if (this.sim == null || !this.simRunning)
            return;
        Runnable r = () -> sim.stopSimulation();
        Thread t = new Thread(r, "Thread-Cancel");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
        this.simRunning = false;
    }

    public void pauseSimulation() {
        this.setSimPaused(true);
    }

    public void resumeSimulation() {
        this.setSimPaused(false);
    }

    private void setSimPaused(boolean paused) {
        if (this.sim == null || !this.simRunning)
            return;
        Runnable r = () -> {
            if (paused)
                this.sim.pauseSimulation();
            else
                this.sim.resumeSimulation();
        };
        Thread t = new Thread(r, "Thread-"+(paused ? "Pause" : "Resume"));
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }

    public void setSleepTime(long millis) {
//        this.sleepTime = millis < 0 ? 0 : millis;
//        if (this.sim == null)
//            return;
//        Runnable r = () -> sim.setSleepTime(millis);
//        Thread t = new Thread(r, "Thread-config sleepTime");
//        t.setDaemon(true); // if GUI ends, simulation also
//        t.start();
    }

    public void setShiftTime(double time) {
        this.shiftTime = time;
//        this.shiftTime = DoubleComp.compare(time, 0) == -1 ? 0 : time;
//        if (this.sim == null)
//            return;
//        Runnable r = () -> sim.setShiftTime(time);
//        Thread t = new Thread(r, "Thread-config shiftTime");
//        t.setDaemon(true); // if GUI ends, simulation also
//        t.start();
    }

    public void setShiftAndSleepTime(double shiftTime, double sleepTime) {
        this.shiftTime = DoubleComp.compare(shiftTime, 0) == -1 ? 0 : shiftTime;
        this.sleepTime = DoubleComp.compare(sleepTime, 0) == -1 ? 0 : sleepTime;
        if (this.sim == null)
            return;
        this.changeSimSpeed();
//        Runnable r = () -> sim.setSimSpeed(shiftTime, sleepTime);
//        Thread t = new Thread(r, "Thread-config shiftTime");
//        t.setDaemon(true); // if GUI ends, simulation also
//        t.start();
    }

    public void setEnabledMaxSpeed(boolean enabled) {
        this.maxSpeedOn = enabled;
        if (this.sim == null)
            return;
        Runnable r = () -> {
            if (enabled)
                this.sim.setMaxSimSpeed();
            else
                this.sim.setSimSpeed(this.shiftTime, this.sleepTime);
        };
        Thread t = new Thread(r, "Thread-config maxSpeed");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }

    public void setEnabledConsoleLogs(boolean enabled) {
//        this.consoleLogsOn = enabled;
//        if (this.sim == null)
//            return;
//        Runnable r = () -> sim.setDebugMode(enabled);
//        Thread t = new Thread(r, "Thread-config consoleLogs");
//        t.setDaemon(true); // if GUI ends, simulation also
//        t.start();
    }
}
