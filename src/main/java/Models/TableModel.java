/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author REYMARK
 */
import DataFiles.UserTransferObject;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TableModel extends AbstractTableModel {
    private List<UserTransferObject> users;
    private String[] columnNames = {"First Name", "Last Name", "Birthday", "Gender", "Status", "Address", "Email", "Role"};

    public TableModel() {
        users = new ArrayList<>();
    }

    public void setUsers(List<UserTransferObject> users) {
        this.users = users;
        fireTableDataChanged();
    }
    
    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
        fireTableStructureChanged();
    }

    public void updateData(List<UserTransferObject> updatedData) {
        users = updatedData;
        fireTableDataChanged();
    }

    
    @Override
    public int getRowCount() {
        return users.size();
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
        UserTransferObject voters = users.get(row);
        return switch (column) {
            case 0 -> voters.getFirstname();
            case 1 -> voters.getLastname();
            case 2 -> voters.getBirthDate();
            case 3 -> voters.getGender();
            case 4 -> voters.getStatus();
            case 5 -> voters.getMunicipality();
            case 6 -> voters.getBarangay();
            case 7 -> voters.getEmail();
            case 8 -> voters.getRole();
            default -> null;
        };
    }
}
