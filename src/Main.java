
import simulation.MySimulation;
import utils.Formatter;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {
    public static void main(String[] args) {
//        GregorianCalendar bt = new GregorianCalendar();
//        GregorianCalendar et = new GregorianCalendar();
//        et.set(Calendar.YEAR, 2026);
//        et.set(Calendar.MONTH, Calendar.AUGUST);
//        et.set(Calendar.DAY_OF_MONTH, 22);
//        System.out.println(
//                (et.getTimeInMillis()-bt.getTimeInMillis()) / 1000 / 3600 / 24
//                );
        MySimulation sim = new MySimulation();
        sim.setAmountOfDesks(20);
        sim.simulate(1, 3600*3);
//        sim.setSimSpeed();
    }
}