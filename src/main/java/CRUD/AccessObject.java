/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CRUD;

import DataFiles.DatabaseConnector;
import DataFiles.UserTransferObject;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author REYMARK
 */
public class AccessObject {
    private Connection connection;

    public AccessObject() {
        // Establish a database connection
        try{
           connection = DatabaseConnector.getConnection() ;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in Database Connection, File: AccessObject");
        }
    }
    
    public List<UserTransferObject> getAllUsers() throws SQLException {
        List<UserTransferObject> allUsers = new ArrayList<>();

        // Execute SQL query to fetch data from the database
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

        // Iterate over the result set and create Employee objects
        while (resultSet.next()) {
            String fname = resultSet.getString("first_name");
            String lname = resultSet.getString("last_name");
            Date birthday = resultSet.getDate("birth_date");
            String gender = resultSet.getString("gender");
            String status = resultSet.getString("status");
            String municipality = resultSet.getString("municipality");
            String barangay = resultSet.getString("barangay");
            String email = resultSet.getString("email");
            String role = resultSet.getString("role");

            UserTransferObject users = new UserTransferObject(fname, lname, birthday, gender, status, municipality, barangay, email, role);
            allUsers.add(users);
        }

        return allUsers;
    }
}
