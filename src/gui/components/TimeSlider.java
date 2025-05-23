package gui.components;

import controllers.FurnitProdSimController;
import gui.FurnitureProdForm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class TimeSlider extends JPanel {

    private class SpeedMode {
        private String repr;
        private double simUnits;
        private long sleep;
        public SpeedMode(String repr, double simUnits, long sleep) {
            this.repr = repr;
            this.simUnits = simUnits;
            this.sleep = sleep;
        }
    }
    private SpeedMode[] modes;
    private final JLabel resultScale;
    private JSlider sliderForSecs;

    public TimeSlider(FurnitProdSimController controller, boolean sliderHorizontal,Color bg) {
            if (controller == null)
                throw new NullPointerException();
        this.setLayout(new BoxLayout(this, sliderHorizontal ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS));

        this.resultScale = new JLabel();
        this.resultScale.setHorizontalAlignment(JLabel.CENTER);
        this.resultScale.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);

        this.sliderForSecs = new JSlider(sliderHorizontal ? JSlider.HORIZONTAL : JSlider.VERTICAL, 1, 100, 65);
//        this.initSpeedModes();
//        controller.setShiftTime(this.modes[sliderForSecs.getValue()].simUnits); // initialization
//        controller.setSleepTime(this.modes[sliderForSecs.getValue()].sleep);
        controller.setShiftAndSleepTime(sliderForSecs.getValue()*3,
                Math.max(1-(sliderForSecs.getValue()/100.0), 0.01)); // initial speed set
        this.resultScale.setText(formatOutput(sliderForSecs.getValue()));

        this.sliderForSecs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
//                    controller.setShiftTime(modes[sliderForSecs.getValue()].simUnits);
//                    controller.setSleepTime(modes[sliderForSecs.getValue()].sleep);
                    controller.setShiftAndSleepTime(sliderForSecs.getValue()*3,
                            Math.max(1-(sliderForSecs.getValue()/100.0), 0.01));
                    resultScale.setText(formatOutput(sliderForSecs.getValue()));
                }
            }
        });
        this.sliderForSecs.setMinorTickSpacing(14);
        this.sliderForSecs.setPaintTicks(true);
        this.sliderForSecs.setPaintLabels(true);

        this.add(this.sliderForSecs);
        this.add(Box.createRigidArea(sliderHorizontal ? new Dimension(20,0) : new Dimension(0,10)));
        this.add(this.resultScale);

        if (bg != null) {
            this.setBackground(bg);
            this.sliderForSecs.setBackground(bg);
        }
    }

    private void initSpeedModes() {
        this.modes = new SpeedMode[14];
        this.modes[0] = new SpeedMode("1s", 1.0, 975); // 975 - 1.0/60.0
        this.modes[1] = new SpeedMode("3s", 1.5, 500); // 150
        this.modes[2] = new SpeedMode("5s", 1.2, 245); // 245
        this.modes[3] = new SpeedMode("15s", 4.5, 300); // 300
        this.modes[4] = new SpeedMode("30s", 5.4, 165); // 162 - 0.09
        this.modes[5] = new SpeedMode("1min", 6, 95); // 94 - 0.1
        this.modes[6] = new SpeedMode("3min", 50, 250); // 250
        this.modes[7] = new SpeedMode("5min", 75, 250);
        this.modes[8] = new SpeedMode("15min", 231, 250);
        this.modes[9] = new SpeedMode("30min", 435, 250);
        this.modes[10] = new SpeedMode("1h", 900, 250);
        this.modes[11] = new SpeedMode("4h", 3000, 250);
        this.modes[12] = new SpeedMode("8h", 6000, 250);
        this.modes[13] = new SpeedMode("1week", 48_000, 250);
    }

    private String formatOutput(int mode) {
        return String.format("Sim-speed:  %d sim-sec/%.2f real-sec",  sliderForSecs.getValue()*3,
                Math.max(1-(sliderForSecs.getValue()/100.0), 0.01));
    }
}