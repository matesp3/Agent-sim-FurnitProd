package gui.models;

import results.ProductResults;
import utils.Formatter;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class FurnitureOrderTableModel extends AbstractTableModel {

    private final String[] aColNames = new String[] {
            "OrderID",
            "ProductID",
            "DeskID",
            "Product",
            "step",
            "waitingBT",
            "stepBT",
            "stepET",
            "created"};
    private final List<ProductResults> lResults;
    private final Class<?>[] aColClasses = new Class<?>[] {
            Integer.class,
            String.class,
            Integer.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class,};

    public FurnitureOrderTableModel(List<ProductResults> lResults) {
        this.lResults = lResults;
    }

    public void add(ProductResults model) {
        this.lResults.add(model);
        this.fireTableDataChanged();
    }

    public void setModels(List<ProductResults> lModels) {
        this.clear();
        if (lModels != null)
            lResults.addAll(lModels);
        this.fireTableDataChanged();
    }

    public ProductResults getModel(int index) {
        return this.lResults.get(index);
    }

    public ArrayList<ProductResults> getModels() {
        return new ArrayList<>(this.lResults);
    }

    public void setModel(int index, ProductResults model) {
        if (model == null || index < 0 || index > this.lResults.size())
            return;
        this.lResults.set(index, model);
        this.fireTableDataChanged();
    }

    public void remove(int index) {
        this.lResults.remove(index);
        this.fireTableDataChanged();
    }

    public void clear() {
        this.lResults.clear();
        this.fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return this.aColNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return this.aColClasses[columnIndex];
    }

    @Override
    public int getRowCount() {
        return this.lResults.size();
    }

    @Override
    public int getColumnCount() {
        return this.aColNames.length;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProductResults order = this.lResults.get(rowIndex);
        if (columnIndex == 0)
            return order.getOrderID();
        else if (columnIndex == 1)
            return order.getProductID();
        else if (columnIndex == 2)
            return order.getDeskID();
        else if (columnIndex == 3)
            return order.getProductType();
        else if (columnIndex == 4)
            return order.getStep();
        else if (columnIndex == 5)
            return Formatter.getStrDateTime(order.getWaitingBT(),8, 6);
        else if (columnIndex == 6)
            return Formatter.getStrDateTime(order.getStepStart(), 8, 6);
        else if (columnIndex == 7)
            return Formatter.getStrDateTime(order.getStepEnd(), 8, 6);
        else if (columnIndex == 8)
            return Formatter.getStrDateTime(order.getCreated(), 8, 6);
        return null;
    }
}
