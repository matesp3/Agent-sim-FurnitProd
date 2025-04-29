package gui.components;

import gui.FurnitureProdForm;
import gui.models.CarpenterTableModel;
import gui.models.FurnitureOrderTableModel;
import results.FurnitProdState;
import utils.Formatter;
import utils.SwingTableColumnResizer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FurnitureProdDataViewer extends JPanel {
    private ResultViewer viewSimTime;
    private CarpenterTableModel carpTabModelA;
    private CarpenterTableModel carpTabModelB;
    private CarpenterTableModel carpTabModelC;
    private FurnitureOrderTableModel tabModelUnstarted;
    private FurnitureOrderTableModel tabModelStarted;
    private FurnitureOrderTableModel tabModelStaining;
    private FurnitureOrderTableModel tabModelAssembling;
    private FurnitureOrderTableModel tabModelFittings;

    private JTable carpTabA;
    private JTable carpTabB;
    private JTable carpTabC;
    private JTable tabUnstarted;
    private JTable tabStarted;
    private JTable tabStaining;
    private JTable tabAssembling;
    private JTable tabFittings;
    private JScrollPane mainScrollPane;
    private JPanel contentPane;

    public FurnitureProdDataViewer() {
        this.viewSimTime = new ResultViewer("Simulation Time");
        this.contentPane = this.createTables();
        this.mainScrollPane = new JScrollPane(this.contentPane);
        this.mainScrollPane.setWheelScrollingEnabled(true);
        this.add(this.mainScrollPane);
    }

    public void resizeContent(int width, int height) {
        this.mainScrollPane.setPreferredSize(new Dimension(width, height));
        SwingTableColumnResizer.setJTableColsWidth(this.tabUnstarted, (width-25)/2,
                new double[] {5,5,5,15,19,17,17,17});
        SwingTableColumnResizer.setJTableColsWidth(this.tabStarted, (width-25)/2,
                new double[] {5,5,5,15,19,17,17,17});
        SwingTableColumnResizer.setJTableColsWidth(this.tabStaining, (width-25)/2,
                new double[] {5,5,5,15,19,17,17,17});
        SwingTableColumnResizer.setJTableColsWidth(this.tabAssembling, (width-25)/2,
                new double[] {5,5,5,15,19,17,17,17});
        SwingTableColumnResizer.setJTableColsWidth(this.tabFittings, (width-25)/2,
                new double[] {5,5,5,15,19,17,17,17});
        SwingTableColumnResizer.setJTableColsWidth(this.carpTabA, width,
                new double[] {5,5,5,5,10,15,15,40});
        SwingTableColumnResizer.setJTableColsWidth(this.carpTabB, width,
                new double[] {5,5,5,5,10,15,15,40});
        SwingTableColumnResizer.setJTableColsWidth(this.carpTabC, width,
                new double[] {5,5,5,5,10,15,15,40});
    }

    private JPanel createTables() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(FurnitureProdForm.COL_BG_TAB);
        JPanel p0 = new JPanel();
        p0.setLayout(new BoxLayout(p0, BoxLayout.X_AXIS));
        p0.setBackground(FurnitureProdForm.COL_BG_TAB);
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        p1.setBackground(FurnitureProdForm.COL_BG_TAB);
        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        p2.setBackground(FurnitureProdForm.COL_BG_TAB);
        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
        p3.setBackground(FurnitureProdForm.COL_BG_TAB);

        this.carpTabModelA = new CarpenterTableModel(new ArrayList<>());
        this.carpTabA = new JTable(this.carpTabModelA);
        carpTabA.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane carpenterScrollPaneA = new JScrollPane(carpTabA);
        carpenterScrollPaneA.setPreferredSize(new Dimension(1000,150));
        carpenterScrollPaneA.setMinimumSize(new Dimension(500, 150));
        carpenterScrollPaneA.setMaximumSize(new Dimension(2000, 200));

        this.carpTabModelB = new CarpenterTableModel(new ArrayList<>());
        this.carpTabB = new JTable(this.carpTabModelB);
        carpTabB.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane carpenterScrollPaneB = new JScrollPane(carpTabB);
        carpenterScrollPaneB.setPreferredSize(new Dimension(1000,150));
        carpenterScrollPaneB.setMinimumSize(new Dimension(500, 150));
        carpenterScrollPaneB.setMaximumSize(new Dimension(2000, 200));

        this.carpTabModelC = new CarpenterTableModel(new ArrayList<>());
        this.carpTabC = new JTable(this.carpTabModelC);
        carpTabC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane carpenterScrollPaneC = new JScrollPane(carpTabC);
        carpenterScrollPaneC.setPreferredSize(new Dimension(1000,150));
        carpenterScrollPaneC.setMinimumSize(new Dimension(500, 150));
        carpenterScrollPaneC.setMaximumSize(new Dimension(2000, 200));

        this.tabModelUnstarted = new FurnitureOrderTableModel(new ArrayList<>());
        this.tabUnstarted = new JTable(this.tabModelUnstarted);
        tabUnstarted.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane orderScrollPaneUnstarted = new JScrollPane(tabUnstarted);
        orderScrollPaneUnstarted.setPreferredSize(new Dimension(500,150));
        orderScrollPaneUnstarted.setMinimumSize(new Dimension(500, 150));
        orderScrollPaneUnstarted.setMaximumSize(new Dimension(1000, 200));

        this.tabModelStarted = new FurnitureOrderTableModel(new ArrayList<>());
        this.tabStarted = new JTable(this.tabModelStarted);
        tabStarted.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane orderScrollPaneStarted = new JScrollPane(tabStarted);
        orderScrollPaneStarted.setPreferredSize(new Dimension(500,150));
        orderScrollPaneStarted.setMinimumSize(new Dimension(500, 150));
        orderScrollPaneStarted.setMaximumSize(new Dimension(1000, 200));

        this.tabModelStaining = new FurnitureOrderTableModel(new ArrayList<>());
        this.tabStaining = new JTable(this.tabModelStaining);
        tabStaining.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane orderScrollPaneStaining = new JScrollPane(tabStaining);
        orderScrollPaneStaining.setPreferredSize(new Dimension(500,150));
        orderScrollPaneStaining.setMinimumSize(new Dimension(500, 150));
        orderScrollPaneStaining.setMaximumSize(new Dimension(1000, 200));

        this.tabModelAssembling = new FurnitureOrderTableModel(new ArrayList<>());
        this.tabAssembling = new JTable(this.tabModelAssembling);
        tabAssembling.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane orderScrollPaneAssembling = new JScrollPane(tabAssembling);
        orderScrollPaneAssembling.setPreferredSize(new Dimension(500,150));
        orderScrollPaneAssembling.setMinimumSize(new Dimension(500, 150));
        orderScrollPaneAssembling.setMaximumSize(new Dimension(1000, 200));

        this.tabModelFittings = new FurnitureOrderTableModel(new ArrayList<>());
        this.tabFittings = new JTable(this.tabModelFittings);
        tabFittings.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane orderScrollPaneFitInst = new JScrollPane(tabFittings);
        orderScrollPaneFitInst.setPreferredSize(new Dimension(500,150));
        orderScrollPaneFitInst.setMinimumSize(new Dimension(500, 150));
        orderScrollPaneFitInst.setMaximumSize(new Dimension(1000, 200));

        JLabel jl1A = new JLabel("Queue 'Unstarted'");
        jl1A.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl1B = new JLabel("Queue 'Started'");
        jl1B.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl2A = new JLabel("Queue 'Staining'");
        jl2A.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl2B = new JLabel("Queue 'Assembling'");
        jl2B.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl2C = new JLabel("Queue 'Fittings inst.'");
        jl2C.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl3A = new JLabel("Carpenter's group A");
        jl3A.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl3B = new JLabel("Carpenter's group B");
        jl3B.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl3C = new JLabel("Carpenter's group C");
        jl3C.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);

        p1.add(Box.createRigidArea(new Dimension(0, 7)));
        p1.add(jl1A);
        p1.add(Box.createRigidArea(new Dimension(0, 7)));
        p1.add(orderScrollPaneUnstarted);
        p1.add(Box.createRigidArea(new Dimension(0, 7)));
        p1.add(jl1B);
        p1.add(Box.createRigidArea(new Dimension(0, 7)));
        p1.add(orderScrollPaneStarted);
        p1.add(Box.createRigidArea(new Dimension(0, 7)));

        p2.add(Box.createRigidArea(new Dimension(0, 7)));
        p2.add(jl2A);
        p2.add(Box.createRigidArea(new Dimension(0, 7)));
        p2.add(orderScrollPaneStaining);
        p2.add(Box.createRigidArea(new Dimension(0, 7)));
        p2.add(jl2B);
        p2.add(Box.createRigidArea(new Dimension(0, 7)));
        p2.add(orderScrollPaneAssembling);
        p2.add(Box.createRigidArea(new Dimension(0, 7)));
        p2.add(jl2C);
        p2.add(Box.createRigidArea(new Dimension(0, 7)));
        p2.add(orderScrollPaneFitInst);

        p3.add(jl3A);
        p3.add(carpenterScrollPaneA);
        p3.add(Box.createRigidArea(new Dimension(0, 7)));
        p3.add(jl3B);
        p3.add(Box.createRigidArea(new Dimension(0, 7)));
        p3.add(carpenterScrollPaneB);
        p3.add(Box.createRigidArea(new Dimension(0, 7)));
        p3.add(jl3C);
        p3.add(Box.createRigidArea(new Dimension(0, 7)));
        p3.add(carpenterScrollPaneC);

        p0.add(p1);
        p0.add(Box.createRigidArea(new Dimension(25,0)));
        p0.add(p2);
        this.viewSimTime.setBorder(BorderFactory.createRaisedBevelBorder());
        this.viewSimTime.setMaximumSize(new Dimension(2000, 50));
        this.viewSimTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(this.viewSimTime);
        content.add(p0);
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(p3);
        return content;
    }

    public void setEventResultsModel(FurnitProdState r) {
        this.tabModelUnstarted.setModels(r.getqUnstarted());
        this.tabModelStarted.setModels(r.getqStarted());
        this.tabModelAssembling.setModels(r.getqAssembling());
        this.tabModelStaining.setModels(r.getqStaining());
        this.tabModelFittings.setModels(r.getqFittings());
        this.viewSimTime.setValue(Formatter.getStrDateTime(r.getSimTime(), 8, 6));
        this.carpTabModelA.setModels(r.getCarpentersA());
        this.carpTabModelB.setModels(r.getCarpentersB());
        this.carpTabModelC.setModels(r.getCarpentersC());
    }

    public static void main(String[] args) {
        double secs = 0;
        for (int i = 0; i < 3600*9; i++) {
            System.out.println(Formatter.getStrDateTime(secs++, 8, 6)); // ok
        }
    }
}
