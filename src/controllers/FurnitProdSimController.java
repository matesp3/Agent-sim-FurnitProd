package controllers;

import OSPAnimator.IAnimator;
import gui.FurnitureProdForm;
import simulation.MySimulation;
import utils.DoubleComp;

/**
 * Controller is used for communication with business logic (some type of Simulation).
 */
public class FurnitProdSimController {
    private final FurnitureProdForm gui;
    private MySimulation sim;
    private IAnimator animator;
    private boolean simRunning; // true if it's stopped, also
    private boolean maxSpeedOn;
    private boolean consoleLogsOn;
    private double shiftTime; // seconds
    private double sleepTime; // milliseconds

    public FurnitProdSimController(FurnitureProdForm gui) {
        this.gui = gui;
        this.sim = null;
        this.maxSpeedOn = false;
        this.consoleLogsOn = false;
        this.shiftTime = 1;      // sim-seconds
        this.sleepTime = 1;  // real-seconds
    }

    public boolean isSimRunning() {
        return this.sim != null && sim.isRunning();
    }

    public boolean simExists() {
        return this.sim != null;
    }

    public void launchSimulation(int groupA, int groupB, int groupC, int desksCount, int experiments, double simulatedDays, boolean withMaxSpeed, boolean withAnimator) {
        Runnable r = () -> {
            try {
                this.sim = new MySimulation();
                this.sim.registerDelegate(this.gui);
                this.sim.onReplicationWillStart(s ->
                {
                    if (this.sim.currentReplication() == 0)
                        return;
                    if (this.sim.animatorExists()) {
                        this.gui.unregisterAnimator();
                        this.sim.removeAnimator();
                        // --
                        this.sim.createAnimator();
                        this.animator = this.sim.animator();
                        this.animator.setSynchronizedTime(false);
                        this.gui.registerAnimator(this.animator);
                    }
                });
                this.sim.onReplicationDidFinish(s ->
                {
                    if (this.sim.currentReplication() > 10)
                        this.gui.updateAfterReplication(this.sim.getReplicationResults());
                    else
                        this.gui.updateReplicationNr(this.sim.currentReplication()+1);
                }
                );
                this.sim.onSimulationDidFinish(s -> {
                    this.gui.updateReplicationNr(this.sim.currentReplication()+1);
                    this.gui.simEnded();
                    this.sim = null;
                });
                this.sim.onAnimatorWasCreated((oldAnim, newAnim) -> {
                    System.out.println("new animator exists? "+(this.sim.animatorExists()&&newAnim !=null));
                });
                this.sim.onAnimatorWasRemoved((oldAnim)-> {
                    System.out.println("old animator exists? "+(this.sim.animatorExists()));
                });
                this.setEnabledMaxSpeed(withMaxSpeed);
                this.sim.setAmountOfDesks(desksCount);
                this.sim.setAmountOfCarpenters(groupA, groupB, groupC);
                // - - - animator
                if (withAnimator) {
                    this.sim.createAnimator();
                    this.animator = this.sim.animator();
                    this.animator.setSynchronizedTime(false);
                    this.gui.registerAnimator(this.animator);
                }
                // - - -
                this.sim.simulate(experiments, simulatedDays*8*3600);
                this.simRunning = false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        Thread t = new Thread(r, "Thread-Main");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }

    public void terminateSimulation() {
//        if (!this.simExists())
        if (!this.isSimRunning())
            return;
        Runnable r = () -> {
            this.sim.stopSimulation();
        };
        Thread t = new Thread(r, "Thread-Cancel");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }

    public void pauseSimulation() {
        this.setSimPaused(true);
    }

    public void resumeSimulation() {
        this.setSimPaused(false);
    }

    private void setSimPaused(boolean paused) {
//        if (!this.simExists())
        if (!this.isSimRunning())
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

    public void setShiftAndSleepTime(double shiftTime, double sleepTime) {
        this.shiftTime = DoubleComp.compare(shiftTime, 0) == -1 ? 0 : shiftTime;
        this.sleepTime = DoubleComp.compare(sleepTime, 0) == -1 ? 0 : sleepTime;
//        if (!this.simExists())
        if (!this.isSimRunning())
            return;
        this.changeSimSpeed();
    }

    public void setEnabledMaxSpeed(boolean enabled) {
        this.maxSpeedOn = enabled;
//        if (!this.simExists())
        if (this.sim == null) // if used isSimRunning(), it won't affect simulation, bcs instance exists, but it does not run
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

    /**
     * Creates new thread
     */
    private void changeSimSpeed() {
//        this.setShiftAndSleepTime(this.shiftTime, this.sleepTime);
        this.maxSpeedOn = false;
        Thread t = new Thread(() -> {this.sim.setSimSpeed(this.shiftTime, this.sleepTime);}, "Thread-ChangeSpeed");
        t.setDaemon(true);
        t.start();
    }

    public IAnimator createAnimator() {
//        if (!this.simExists()) {
        if (!this.isSimRunning()) { // before simulation start
            return null;
        }
        this.sim.createAnimator();
        this.animator = this.sim.animator();
        this.animator.setSynchronizedTime(true);
        return this.animator;
    }

    public void removeAnimator() {
//        if (!this.simExists()) {
        if (!this.isSimRunning()) { // before simulation start
            return;
        }
        System.out.println("Going call removeAnimator()");
        this.animator = null;
        this.sim.removeAnimator();
//        Thread t = new Thread(() -> {this.sim.removeAnimator();}, "Thread-removeAnimator");
//        t.setDaemon(true);
//        t.start();
        System.out.println("Is paused?"+this.sim.isPaused());
    }

    public void setEnabledConsoleLogs(boolean enabled) {
//        throw new UnsupportedOperationException("Not supported yet.");
        this.consoleLogsOn = enabled;
//        if (this.sim == null)
//            return;
//        Runnable r = () -> sim.setDebugMode(enabled);
//        Thread t = new Thread(r, "Thread-config consoleLogs");
//        t.setDaemon(true); // if GUI ends, simulation also
//        t.start();
    }
}
