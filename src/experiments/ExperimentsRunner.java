package experiments;

import OSPABA.Simulation;
import results.FurnitProdRepStats;
import simulation.MySimulation;
import utils.DoubleComp;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExperimentsRunner {
    public static final String RUN_CONFIGURATIONS_PATH = System.getProperty("user.dir") + "\\src\\experiments\\"+"run_configs.csv";
    public static final String CONFIG_RESULTS_PATH = System.getProperty("user.dir") + "\\src\\experiments\\"+"config_results.csv"; // priebezne
    public static final String SORTED_RESULTS_PATH = System.getProperty("user.dir") + "\\src\\experiments\\"+"sorted_results.csv"; // naraz flushne
    public static final String DELIM = ";";
    private static final String RESULTS_HEADER = "[0]ConfigNr;[1]Ts;[2]ExecutedReps;[3]CountA;[4]CountB;[5]CountC;[6]DesksCount;[7]SimDays;[8]CI-Order'sTimeInSystem;" +
            "[9]CI-UtilizationA;[10]CI-UtilizationB;[11]CI-UtilizationC;[12]AVG-CreatedOrders;[13]AVG-CompletedOrders";
    private static final int MAX_REPS = 1_000;
    private static final int IDX_COUNT_A = 0;
    private static final int IDX_COUNT_B = 1;
    private static final int IDX_COUNT_C = 2;
    private static final int IDX_COUNT_DESKS = 3;
    private static final int IDX_DAYS_NR = 4;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Comparator<Results> RESULTS_CMP = (r1, r2) -> {
        int cmp0 = DoubleComp.compare(r1.lOrderTimeInSystem, r2.lOrderTimeInSystem);
        int cmp1 = Integer.compare((r1.countA+r1.countB+r1.countC), (r2.countA+r2.countB+r2.countC));
        int cmp2 = Integer.compare(r1.desksCount, r2.desksCount);
        return (cmp0 != 0) ? cmp0 : ((cmp1 != 0) ? cmp1 : cmp2);
    };
    private int[][] configs;
    private Results[] results;
    private int currentConfig = -1;
    private MySimulation mySim = null;
    private FileWriter fw = null;

    public static class Results {
        /** [0] */
        int configNr;
        /** [1] */
        String timestamp;
        /** [2] */
        int executedReps;
        /** [3] */
        int countA;
        /** [4] */
        int countB;
        /** [5] */
        int countC;
        /** [6] */
        int desksCount;
        /** [7] */
        double simDays;
        /** [8l] */
        double lOrderTimeInSystem = -1;
        /** [8h] */
        double hOrderTimeInSystem = -1;
        /** [9l] */
        double lUtilizationA = -1;
        /** [9h] */
        double hUtilizationA = -1;
        /** [10l] */
        double lUtilizationB = -1;
        /** [10h] */
        double hUtilizationB = -1;
        /** [11l]*/
        double lUtilizationC = -1;
        /** [11h] */
        double hUtilizationC = -1;
        /** [12] */
        double avgCreatedOrders = -1;
        /** [13] */
        double avgCompletedOrders = -1;
        /** [14] */
        double avgUsedDesks = -1;

        public Results(int configNr, String timestamp, int reps, int countA, int countB, int countC, int desksCount, double simDays) {
            this.configNr = configNr;
            this.timestamp = timestamp;
            this.executedReps = reps;
            this.countA = countA;
            this.countB = countB;
            this.countC = countC;
            this.desksCount = desksCount;
            this.simDays = simDays;
        }

        public void setlOrderTimeInSystem(double lOrderTimeInSystem) {
            this.lOrderTimeInSystem = lOrderTimeInSystem;
        }

        public void sethOrderTimeInSystem(double hOrderTimeInSystem) {
            this.hOrderTimeInSystem = hOrderTimeInSystem;
        }

        public void setlUtilizationA(double lUtilizationA) {
            this.lUtilizationA = lUtilizationA;
        }

        public void sethUtilizationA(double hUtilizationA) {
            this.hUtilizationA = hUtilizationA;
        }

        public void setlUtilizationB(double lUtilizationB) {
            this.lUtilizationB = lUtilizationB;
        }

        public void sethUtilizationB(double hUtilizationB) {
            this.hUtilizationB = hUtilizationB;
        }

        public void setlUtilizationC(double lUtilizationC) {
            this.lUtilizationC = lUtilizationC;
        }

        public void sethUtilizationC(double hUtilizationC) {
            this.hUtilizationC = hUtilizationC;
        }

        public void setAvgCreatedOrders(double avgCreatedOrders) {
            this.avgCreatedOrders = avgCreatedOrders;
        }

        public void setAvgCompletedOrders(double avgCompletedOrders) {
            this.avgCompletedOrders = avgCompletedOrders;
        }

        public void setAvgUsedDesks(double avgUsedDesks) {
            this.avgUsedDesks = avgUsedDesks;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(configNr);
            sb.append(DELIM);
            sb.append(timestamp);
            sb.append(DELIM);
            sb.append(executedReps);
            sb.append(DELIM);
            sb.append(countA);
            sb.append(DELIM);
            sb.append(countB);
            sb.append(DELIM);
            sb.append(countC);
            sb.append(DELIM);
            sb.append(desksCount);
            sb.append(DELIM);
            sb.append(String.format("%.1f", simDays));
            sb.append(DELIM);
            sb.append(String.format("<%.02f|%.02f>", lOrderTimeInSystem, hOrderTimeInSystem));
            sb.append(DELIM);
            sb.append(String.format("<%.02f|%.02f>", lUtilizationA, hUtilizationA));
            sb.append(DELIM);
            sb.append(String.format("<%.02f|%.02f>", lUtilizationB, hUtilizationB));
            sb.append(DELIM);
            sb.append(String.format("<%.02f|%.02f>", lUtilizationC, hUtilizationC));
            sb.append(DELIM);
            sb.append(String.format("%.02f", avgCreatedOrders));
            sb.append(DELIM);
            sb.append(String.format("%.02f", avgCompletedOrders));
            return sb.toString();
        }
    }

    private void endSimIfConditionsMet(Simulation simulation) {
        if (this.mySim.currentReplication() < 30) // bcs of confidence interval
            return;
        FurnitProdRepStats r = this.mySim.getReplicationResults();
        int cmpRes = DoubleComp.compare(r.getOrderTimeInSystem().getHalfWidth(), r.getOrderTimeInSystem().getMean() / 100.0);
        if (cmpRes < 1) { // h <= 1% of time
            this.mySim.stopSimulation();
        }
    }

    private void convertResults(Simulation simulation) {
        FurnitProdRepStats r = this.mySim.getReplicationResults();
        Results results = new Results(this.currentConfig+1, getFormattedTimestamp(), (int)r.getReplicationNum()+1,
                this.configs[this.currentConfig][IDX_COUNT_A], this.configs[this.currentConfig][IDX_COUNT_B],
                this.configs[this.currentConfig][IDX_COUNT_C], this.configs[this.currentConfig][IDX_COUNT_DESKS],
                this.configs[this.currentConfig][IDX_DAYS_NR]);
        double mean, h;
        mean = r.getOrderTimeInSystem().getMean();
        h = r.getOrderTimeInSystem().getHalfWidth();
        results.setlOrderTimeInSystem(mean - h);
        results.sethOrderTimeInSystem(mean + h);

        mean = r.getUtilizationGroupA().getMean();
        h = r.getUtilizationGroupA().getHalfWidth();
        results.setlUtilizationA(mean - h);
        results.sethUtilizationA(mean + h);

        mean = r.getUtilizationGroupB().getMean();
        h = r.getUtilizationGroupB().getHalfWidth();
        results.setlUtilizationB(mean - h);
        results.sethUtilizationB(mean + h);

        mean = r.getUtilizationGroupC().getMean();
        h = r.getUtilizationGroupC().getHalfWidth();
        results.setlUtilizationC(mean - h);
        results.sethUtilizationC(mean + h);

        results.setAvgCreatedOrders(r.getAvgCreatedOrdersCount().getValue());
        results.setAvgCompletedOrders(r.getAvgCompletedOrdersCount().getValue());
        results.setAvgUsedDesks(r.getAvgUsedDesksCount().getValue());

        this.results[this.currentConfig] = results;
        try {
            this.fw.write('\n' + results.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void runConfigurations() {
        this.mySim = new MySimulation();
        this.mySim.setMaxSimSpeed();
        this.mySim.onReplicationDidFinish(this::endSimIfConditionsMet);
        this.mySim.onSimulationDidFinish(this::convertResults);
        this.openFileWriter();
        try {
            // - - - start
            for (this.currentConfig = 0; this.currentConfig < this.configs.length; this.currentConfig++) {
                printMessage("Starting simulation for "+ (this.currentConfig+1) +". configuration...");
                this.mySim.setAmountOfDesks(this.configs[this.currentConfig][IDX_COUNT_DESKS]);
                this.mySim.setAmountOfCarpenters(
                        this.configs[this.currentConfig][IDX_COUNT_A],
                        this.configs[this.currentConfig][IDX_COUNT_B],
                        this.configs[this.currentConfig][IDX_COUNT_C]
                );
                this.mySim.simulate(MAX_REPS, this.configs[this.currentConfig][IDX_DAYS_NR] * 8 * 3600);
                printMessage("Configuration no."+ (this.currentConfig+1) +" ended!\n");
            }
            System.out.println();
            printMessage( "Results are written in: "+CONFIG_RESULTS_PATH );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeFileWriter();
        }
        this.writeSortedResults();
    }

    private void openFileWriter() {
        try {
            printMessage("Opening file writer...");
            this.fw = new FileWriter(CONFIG_RESULTS_PATH);
            printMessage("Ok.. File writer opened\n");
            this.fw.write(RESULTS_HEADER);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeFileWriter() {
        try {
            printMessage("Closing file writer...");
            this.fw.close();
            printMessage("Ok.. File writer closed\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfigurations(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // omit first line - descriptions
            line = line.trim().split(DELIM)[0];
            int rows;
            try {
                rows = Integer.parseInt(line);
            }
            catch (NumberFormatException e) {  // assumption - first row were descriptions only
                rows = Integer.parseInt( br.readLine().trim().split(DELIM)[0] ); // second row must be then row count
            }
            this.configs = new int[rows][5];
            this.results = new Results[rows];
            int row = 0;
            while ((line = br.readLine()) != null) {
                String[] params = line.trim().split(";");
                this.configs[row][IDX_COUNT_A]      = Integer.parseInt(params[0]);
                this.configs[row][IDX_COUNT_B]      = Integer.parseInt(params[1]);
                this.configs[row][IDX_COUNT_C]      = Integer.parseInt(params[2]);
                this.configs[row][IDX_COUNT_DESKS]  = Integer.parseInt(params[3]);
                this.configs[row][IDX_DAYS_NR]      = Integer.parseInt(params[4]);
                row++;
            }
        } catch (IOException e) {
            printMessage(" !!! ERROR while loading configurations !!!");
            throw new RuntimeException(e);
        }
    }

    private void writeSortedResults() {
        printMessage("Sorting results ascending by Order's Time In System...");
        this.sortResults();
        try (FileWriter filWr = new FileWriter(SORTED_RESULTS_PATH)) {
            filWr.write("    * * * * * S O R T E D   R E S U L T S * * * * *   (ascending by [8]Order's Time In System)  ");
            filWr.write('\n'+RESULTS_HEADER);
            for (int i = 0; i < this.results.length; i++) {
                filWr.write('\n' + this.results[i].toString() );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        printMessage( "Sorted results are written in: "+SORTED_RESULTS_PATH );
    }

    private void sortResults() {
        Arrays.sort(this.results, RESULTS_CMP);
    }

    /**
     * @return formatted current sys-time
     */
    private static String getFormattedTimestamp() {
        return LocalDateTime.now().format(FORMATTER);
    }

    private static void printMessage(String message) {
        System.out.println("    (i) -   "+message);
    }

    public static void main(String[] args) {
        ExperimentsRunner runner = new ExperimentsRunner();
        runner.loadConfigurations( new File(RUN_CONFIGURATIONS_PATH) );
        runner.runConfigurations();
    }
}
