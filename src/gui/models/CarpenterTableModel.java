package gui.models;

import common.Carpenter;
import results.CarpenterModel;
import utils.Formatter;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CarpenterTableModel extends AbstractTableModel {
    private final List<CarpenterModel> lResults;
    private final String[] aColNames = new String[] {
            "ID",
            "Group",
            "Working",
            "DeskID",
            "ProductID",
            "ProcBT",
            "ProcET",
            "Product's details"
    };
    private final Class<?>[] aColClasses = new Class<?>[] {
            Integer.class,
            String.class,
            Boolean.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class
    };

    public CarpenterTableModel(List<CarpenterModel> lResults) {
        this.lResults = lResults;
    }

    public void add(CarpenterModel model) {
        this.lResults.add(model);
        this.fireTableDataChanged();
    }

    public void setModels(List<CarpenterModel> lModels) {
        this.clear();
        if (lModels != null)
            lResults.addAll(lModels);
        this.fireTableDataChanged();
    }

    public CarpenterModel getModel(int index) {
        return this.lResults.get(index);
    }

    public ArrayList<CarpenterModel> getModels() {
        return new ArrayList<>(this.lResults);
    }

    public void setModel(int index, CarpenterModel model) {
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
        CarpenterModel c = this.lResults.get(rowIndex);
        if (columnIndex == 0)
            return c.getCarpenterID();
        if (columnIndex == 1)
            return c.getGroup();
        if (columnIndex == 2)
            return c.isWorking();
        if (columnIndex == 3)
            return switch (c.getDeskID()) {
                case Carpenter.IN_STORAGE -> "In Storage";
                case Carpenter.TRANSFER_STORAGE-> "Storage-Transfer";
                case Carpenter.TRANSFER_DESKS -> "Desks-Transfer";
                default -> c.getDeskID();
            };
        if (columnIndex == 4)
            return c.getAssignedProductID() == null ? "Not assigned" : c.getAssignedProductID();
        if (columnIndex == 5)
            return Formatter.getStrDateTime(c.getOrderBT(), 8, 6);
        if (columnIndex == 6)
            return Formatter.getStrDateTime(c.getOrderET(), 8, 6);
        if (columnIndex == 7)
            return c.getOrderRepresentation();
        return null;
    }
}
