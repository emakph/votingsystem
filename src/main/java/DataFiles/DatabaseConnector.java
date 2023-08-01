/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataFiles;

/**
 *
 * @author REYMARK
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/final-voting";
    private static final String USER = "postgres";
    private static final String PASS = "admin123";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
    public static void main(String[]args){
        try{
            JOptionPane.showMessageDialog(null,getConnection());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in getting the connection");
        }
    }
    
}
