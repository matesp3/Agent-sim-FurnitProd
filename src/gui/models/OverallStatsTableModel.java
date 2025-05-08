package gui.models;

import results.FurnitProdRepStats;
import results.StatResult;
import utils.Formatter;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class OverallStatsTableModel extends AbstractTableModel {
    private List<StatResult.ConfInterval> lResults;
    private final String[] aColNames = new String[] {
            "Stat name",
            "95% Confidence interval: <left-bound | MEAN | right-bound>",
            "Unit" };
    private final Class<?>[] aColClasses = new Class<?>[] {
            String.class,
            String.class,
            String.class };

    public OverallStatsTableModel(List<StatResult.ConfInterval> lVisits) {
        this.lResults = lVisits;
    }

    public void add(StatResult.ConfInterval model) {
        this.lResults.add(model);
        this.fireTableDataChanged();
    }

    public void setModels(List<StatResult.ConfInterval> lModels) {
        this.clear();
        if (lModels != null)
            lResults.addAll(lModels);
        this.fireTableDataChanged();
    }

    public void updateTable(FurnitProdRepStats r) {
        this.lResults = r.getStats();
        this.fireTableDataChanged();
    }

    public StatResult.ConfInterval getModel(int index) {
        return this.lResults.get(index);
    }

    public ArrayList<StatResult.ConfInterval> getModels() {
        return new ArrayList<>(this.lResults);
    }

    public void setModel(int index, StatResult.ConfInterval model) {
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
        StatResult.ConfInterval stat = this.lResults.get(rowIndex);
        if (columnIndex == 0)
            return stat.getDescription();
        else if (columnIndex == 1)
            return Formatter.getStrCI(stat.getHalfWidth(), stat.getMean(), 2, 1.0);
        else if (columnIndex == 2)
            return stat.getUnit();
        return null;
    }
}
