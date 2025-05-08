package gui.components;

import gui.FurnitureProdForm;
import results.FurnitProdState;
import utils.Formatter;
import utils.SwingTableColumnResizer;

import javax.swing.*;
import java.awt.*;

public class FurnitureProdAnimViewer extends JPanel {
    private static final Color ANIM_BG = new Color(187, 181, 181);
    private JPanel headerPane;
    private JPanel animPane;
    private JComponent animation = null;
    private JPanel emptySpace = new JPanel();
    private ResultViewer viewOrdersCreated;
    private ResultViewer viewUsedDesks;
    private ResultViewer viewSimTime;
    private ResultViewer viewOrdersCompleted;

    public FurnitureProdAnimViewer() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.viewSimTime = new ResultViewer("Simulation Time", Formatter.getStrDateTime(0, 8, 6));
        this.viewUsedDesks = new ResultViewer("Used Desks", "0");
        this.viewOrdersCreated = new ResultViewer("Orders Created", "0");
        this.viewOrdersCompleted = new ResultViewer("Orders Completed", "0");

        this.headerPane = this.createHeader();
        this.animPane = new JPanel();
        this.animPane.setLayout(null);

        this.add(headerPane);
        this.add(animPane);
    }

    public void addAnimPane(JComponent animationPane) {
        this.animation = animationPane;
        this.animPane.add(this.animation);
        this.animation.setBounds(0, 0, this.animPane.getWidth(), this.animPane.getHeight());
        this.animation.setBackground(ANIM_BG);
    }

    public void removeAnimPane() {
        if (this.animation != null) {
            this.animPane.remove(this.animation);
            this.animation = null;
            // --v both must be called to do the effect
            this.animPane.revalidate();
            this.animPane.repaint();
        }
    }

    public void resizeContent(int width, int height) {
        this.animPane.setPreferredSize(new Dimension(width, height));
    }

    public void setEventResultsModel(FurnitProdState r) {
        this.viewSimTime.setValue(Formatter.getStrDateTime(r.getSimTime(), 8, 6));
        this.viewUsedDesks.setValue(r.getCurrentlyUsedDesks());
        this.viewOrdersCreated.setValue(r.getCurrentlyCreatedOrders());
        this.viewOrdersCompleted.setValue(r.getCurrentlyCompletedOrders());
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBackground(FurnitureProdForm.COL_BG_TAB);

        this.viewOrdersCreated.setBorder(BorderFactory.createRaisedBevelBorder());
        this.viewOrdersCreated.setMaximumSize(new Dimension(2000, 50));
        this.viewOrdersCreated.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.viewOrdersCreated.setBackground(header.getBackground());

        this.viewUsedDesks.setBorder(BorderFactory.createRaisedBevelBorder());
        this.viewUsedDesks.setMaximumSize(new Dimension(2000, 50));
        this.viewUsedDesks.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.viewUsedDesks.setBackground(header.getBackground());

        this.viewSimTime.setBorder(BorderFactory.createRaisedBevelBorder());
        this.viewSimTime.setMaximumSize(new Dimension(2000, 50));
        this.viewSimTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.viewSimTime.setBackground(header.getBackground());

        this.viewOrdersCompleted.setBorder(BorderFactory.createRaisedBevelBorder());
        this.viewOrdersCompleted.setMaximumSize(new Dimension(2000, 50));
        this.viewOrdersCompleted.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.viewOrdersCompleted.setBackground(header.getBackground());

        header.add(this.viewOrdersCreated);
        header.add(this.viewUsedDesks);
        header.add(this.viewSimTime);
        header.add(this.viewOrdersCompleted);
        return header;
    }
}
