/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author REYMARK
 */
import DataFiles.CandidateAccessObject;
import DataFiles.CandidateTransferObject;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;

public class CandidatesTableModel extends AbstractTableModel {
    private CandidateAccessObject candidateAccessObj;
    private List<CandidateTransferObject> candidates;
    private String[] columnNames = {"Candidates", "Title", "Action"};
    
    public CandidatesTableModel(JTable table) {
        candidateAccessObj = new CandidateAccessObject();
        candidates = new ArrayList<>();
    }

    public void setCandidates(List<CandidateTransferObject> candidates) {
        this.candidates = candidates;
        fireTableDataChanged();
    }
    
    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return candidates.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        CandidateTransferObject cand = candidates.get(row);
        return switch (column) {
            case 0 -> cand.getCandidateLastName() + ", " + cand.getCandidateFirstName();
            case 1 -> cand.getCandidateTitle();
            default -> null;
        };
    }
    
}
