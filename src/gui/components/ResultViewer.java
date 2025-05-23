package gui.components;

import gui.FurnitureProdForm;

import javax.swing.*;

public class ResultViewer extends JPanel {
    private JLabel desc;
    private JLabel value;

    public ResultViewer(String desc) {
        this(desc, null);
    }

    public ResultViewer(String desc, String value) {
        desc = desc == null || desc.isEmpty() ? "Value:" : desc+":";
        this.desc = new JLabel(desc);
        this.desc.setForeground(FurnitureProdForm.COL_TEXT_FONT_2);

        this.value = new JLabel(value == null ? "" : value);
        this.value.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);

        this.add(this.desc);
        this.add(this.value);
    }

    public void setDescription(String desc) {
        this.desc.setText(desc);
    }

    public void setValue(String value) {
        this.value.setText(value);
    }

    public void setValue(boolean value) {
        this.value.setText(Boolean.toString(value));
    }

    public void setValue(int value) {
        this.value.setText(Integer.toString(value));
    }

    public void setValue(long value) {
        this.value.setText(Long.toString(value));
    }

    public void setValue(double value, int precision) {
        precision = Math.max(0, precision);
        this.value.setText(String.format(("%."+precision+"f"), value));
    }


}
