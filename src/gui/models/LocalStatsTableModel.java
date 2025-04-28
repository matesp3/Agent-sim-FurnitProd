package gui.models;

import results.FurnitProdState;
import results.StatResult;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class LocalStatsTableModel extends AbstractTableModel {
    private List<StatResult.Simple> lResults;
    private final String[] aColNames = new String[] {
            "Stat name",
            "Value",
            "Unit" };
    private final Class<?>[] aColClasses = new Class<?>[] {
            String.class,
            String.class,
            String.class };

    public LocalStatsTableModel(List<StatResult.Simple> lStats) {
        this.lResults = lStats;
    }

    public void updateTable(FurnitProdState r) {
        this.lResults = r.getStats();
        this.fireTableDataChanged();
    }

    public void add(StatResult.Simple model) {
        this.lResults.add(model);
        this.fireTableDataChanged();
    }

    public void setModels(List<StatResult.Simple> lModels) {
        if (lModels != null)
            this.lResults = lModels;
        else
            this.lResults.clear();
        this.fireTableDataChanged();
    }

    public StatResult.Simple getModel(int index) {
        return this.lResults.get(index);
    }

    public ArrayList<StatResult.Simple> getModels() {
        return new ArrayList<>(this.lResults);
    }

    public void setModel(int index, StatResult.Simple model) {
        if (model == null || index < 0 || index > this.lResults.size())
            return;
        this.lResults.set(index, model);
        this.fireTableRowsUpdated(index, index);
    }

    public void remove(int index) {
        this.lResults.remove(index);
        fireTableRowsDeleted(index, index);
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
        StatResult.Simple stat = this.lResults.get(rowIndex);
        if (columnIndex == 0)
            return stat.getDescription();
        else if (columnIndex == 1)
            return stat.getValue();
        else if (columnIndex == 2)
            return stat.getUnit();
        return null;
    }
}
