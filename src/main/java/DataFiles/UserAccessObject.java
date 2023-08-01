/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataFiles;

/**
 *
 * @author REYMARK
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.*;

public class UserAccessObject {
    private static Connection connection;

    public UserAccessObject() {
        // Establish a database connection
        try{
           connection = DatabaseConnector.getConnection() ;
        } catch (SQLException e) {
           JOptionPane.showMessageDialog(null, "Error in UserAccessObject constructor. File: UserAccessObject");
        }
    }
    
    public List<UserTransferObject> getAllUsers(){
        List<UserTransferObject> voters = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id";
        // Execute SQL query to fetch data from the database
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();

            // Iterate over the result set and create Employee objects
            while (rs.next()) {
                int userID = rs.getInt("user_id");
                String fname = rs.getString("first_name");
                String lname = rs.getString("last_name");
                Date birthday = rs.getDate("birth_date");
                String gender = rs.getString("gender");
                String status = rs.getString("status");
                String municipality = rs.getString("municipality");
                String barangay = rs.getString("barangay");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Date registrationDate = rs.getDate("registration_date");
                String role = rs.getString("role");
                
                UserTransferObject users = new UserTransferObject(userID, fname, lname, birthday, gender, status, municipality, barangay, email, password, registrationDate, role);
                voters.add(users);
            }
            return voters;
        
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error occurred during the updateCandidate method. File: CandidateAccessObject.");
        }
        return null;
    }
    
    public List<UserTransferObject> getAllAdmin(){
        List<UserTransferObject> voters = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role ='admin' ORDER BY user_id";
        // Execute SQL query to fetch data from the database
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();

            // Iterate over the result set and create Employee objects
            while (rs.next()) {
                int userID = rs.getInt("user_id");
                String fname = rs.getString("first_name");
                String lname = rs.getString("last_name");
                Date birthday = rs.getDate("birth_date");
                String gender = rs.getString("gender");
                String status = rs.getString("status");
                String municipality = rs.getString("municipality");
                String barangay = rs.getString("barangay");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Date registrationDate = rs.getDate("registration_date");
                String role = rs.getString("role");
                
                UserTransferObject users = new UserTransferObject(userID, fname, lname, birthday, gender, status, municipality, barangay, email, password, registrationDate, role);
                voters.add(users);
            }
            return voters;
        
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error occurred during the updateCandidate method. File: CandidateAccessObject.");
        }
        return null;
    }
    
    
    public boolean emailExists(String email) {
        String sql = "SELECT email FROM users WHERE email = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return true;
                }
            }
        } catch (SQLException ex){
            
        }
        return false;
    }
    
    public boolean registerUser(UserTransferObject users){
        String sql = "INSERT INTO users (first_name, last_name, birth_date, gender, status, municipality, barangay, email, password, registration_date, role)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, users.getFirstname());
            pstmt.setString(2, users.getLastname());
            pstmt.setDate(3, users.getBirthDate());
            pstmt.setString(4, users.getGender());
            pstmt.setString(5, users.getStatus());
            pstmt.setString(6, users.getMunicipality());
            pstmt.setString(7, users.getBarangay());
            pstmt.setString(8, users.getEmail());
            pstmt.setString(9, users.getPassword());
            pstmt.setString(10, users.getRole());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
            
        }
        return false;
    }
    
    public boolean validateUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in validateUser method. File: UserAccessObject");
            
        }
        return false;
    }
    
//    public UserTransferObject validateUser(String email, String password) {
//        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, email);
//            stmt.setString(2, password);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    // User exists and credentials match
//                    int id = rs.getInt("user_id");
//                    String fname = rs.getString("first_name");
//                    String email1 = rs.getString("email");
//                    String role = rs.getString("role");
//                    
//                    return new UserTransferObject(id, fname, email1, role);
//                }
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error in validateUser method. File: UserAccessObject");
//            // Handle the exception appropriately
//        }
//
//        // User does not exist or credentials do not match
//        return null;
//    }
    
    public void populatePolls(JComboBox<String> comboBox, String columnLabel) {
        try {
            // Clear existing items in the JComboBox
            comboBox.removeAllItems();
            
            // Create a SQL query using the selected value
            //String query = "SELECT column_name FROM Polls WHERE condition = '" + selectedValue + "'";
            String query = "SELECT * FROM users ORDER BY user_id";
                    
            try (Statement statement = UserAccessObject.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {
                
                while (resultSet.next()) {
                    String item = resultSet.getString(columnLabel);
                    comboBox.addItem(item);
                    
                }
                
            }
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, "Error on populateCandidates File: UserAccessObject");
        }
    }
    
    public UserTransferObject getAllUserData(String email, String pass) {
        
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, pass);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // User exists and credentials match
                    int id = rs.getInt("user_id");
                    String fname = rs.getString("first_name");
                    String lname = rs.getString("last_name");
                    Date birthDate = rs.getDate("birth_date");
                    String gender = rs.getString("gender");
                    String status = rs.getString("status");
                    String municipality = rs.getString("municipality");
                    String barangay = rs.getString("barangay");
                    String email1 = rs.getString("email");
                    String password = rs.getString("password");
                    Date reg_date = rs.getDate("registration_date");
                    String role = rs.getString("role");
                    
                    return new UserTransferObject(id, fname, lname, birthDate, gender, status, municipality, barangay, email1, password, reg_date, role);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in getAllData method. File: UserAccessObject");
        }

        // User does not exist or credentials do not match
        return null;
    }
    
    public UserTransferObject getAllUserData(String email) {
        
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // User exists and credentials match
                    int id = rs.getInt("user_id");
                    String fname = rs.getString("first_name");
                    String lname = rs.getString("last_name");
                    Date birthDate = rs.getDate("birth_date");
                    String gender = rs.getString("gender");
                    String status = rs.getString("status");
                    String municipality = rs.getString("municipality");
                    String barangay = rs.getString("barangay");
                    String email1 = rs.getString("email");
                    String password = rs.getString("password");
                    Date reg_date = rs.getDate("registration_date");
                    String role = rs.getString("role");
                    
                    return new UserTransferObject(id, fname, lname, birthDate, gender, status, municipality, barangay, email1, password, reg_date, role);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in getAllData method. File: UserAccessObject");
        }

        // User does not exist or credentials do not match
        return null;
    }
    
    public DefaultListModel<String> populateJList(String columnName, String tableName){
        DefaultListModel<String> model = new DefaultListModel<>();
        String query = "SELECT " + columnName + " FROM " + tableName + " ORDER BY user_id DESC";
        try(PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            ResultSet resultSet = pstmt.executeQuery();

            model.clear();
            while (resultSet.next()) {
                String value = resultSet.getString(columnName);
                model.addElement("New user with email " + value + " has been registered succesfully!");
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error in populateJList method. File: UserAccessObject");
        }
        return model;
    }
    
    public boolean updateUser(UserTransferObject user){
        
        String updateQuery = "UPDATE users SET first_name=?, last_name=?, birth_date=?, gender=?, status=?, municipality=?, barangay=?, email=?, role=? WHERE email = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);){
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setDate(3, user.getBirthDate());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setString(5, user.getStatus());
            preparedStatement.setString(6, user.getMunicipality());
            preparedStatement.setString(7, user.getBarangay());
            preparedStatement.setString(8, user.getEmail());
            preparedStatement.setString(9, user.getRole());
            preparedStatement.setString(10,user.getEmail());
            preparedStatement.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error in updateUser method. File: UserAccessObject");
        }
        return false;
    }
    
    public boolean updateAdmin(UserTransferObject user){
        
        String updateQuery = "UPDATE users SET first_name=?, last_name=?, birth_date=?, gender=?, status=?, municipality=?, barangay=?, email=?, password=? WHERE email = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);){
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setDate(3, user.getBirthDate());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setString(5, user.getStatus());
            preparedStatement.setString(6, user.getMunicipality());
            preparedStatement.setString(7, user.getBarangay());
            preparedStatement.setString(8, user.getEmail());
            preparedStatement.setString(9, user.getPassword());
            preparedStatement.setString(10,user.getEmail());
            preparedStatement.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error in updateUser method. File: UserAccessObject");
        }
        return false;
    }
    
    public int recordCountForUser(String columnName, String value){
        String countRecordQuery = "SELECT COUNT(*) FROM users WHERE " + columnName + " = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(countRecordQuery);
            preparedStatement.setString(1, value);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error in recordCountForUser method. File: UserAccessObject");
        }
        return 0;
    }
    
    public int recordCountForUser(){
        String countRecordQuery = "SELECT COUNT(*) FROM users";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(countRecordQuery);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error in recordCountForUser method. File: UserAccessObject");
        }
        return 0;
    }
    
    public boolean deleteUser(UserTransferObject userData) {
        
        // Validate input data
        if (userData == null || !emailExists(userData.getEmail())) {
            System.err.println("Invalid user data provided for update.");
            return false;
        }
        
        String deleteQuery = "DELETE FROM users WHERE email = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(deleteQuery)){
            pstmt.setString(1, userData.getEmail());
            pstmt.executeUpdate();
            
            return true;
            
        } catch(SQLException ex) { 
            ex.printStackTrace();
            System.err.println("Error occurred during the updateCandidate method. File: CandidateAccessObject.");
            
            return false;
        }
    }
}

