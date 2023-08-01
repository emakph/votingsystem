package DataFiles;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author REYMARK
 */
import CustomChart.*;
import java.awt.Color;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class CandidateAccessObject {
    private Connection connection;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CandidateAccessObject() {
        // Establish a database connection
        // ...
        try{
           connection = DatabaseConnector.getConnection() ;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    private Color getColor(int index){
        Color[] color = new Color[]{new Color(53,184,204), new Color(223,89,136), new Color(248,110,105), new Color(253,156,76), new Color(254,204,91), new Color(149,214,146)};
        return color[index];
    }
    
    public void populatePolls(JComboBox<String> comboBox) {
        // Clear existing items in the JComboBox
        comboBox.removeAllItems();

        // Create a SQL query using the selected value
        //String query = "SELECT column_name FROM Polls WHERE condition = '" + selectedValue + "'";
        String query = "SELECT * FROM polls ORDER BY poll_id";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                String item = resultSet.getString("title");
                comboBox.addItem(item);

            }

        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error while processing populatePolls. File : CandidateAccessObject.");
         }
    }
    
    public void populateCandidates(JComboBox<String> comboBox, int pollId) {
        // Clear existing items in the JComboBox
        comboBox.removeAllItems();
        comboBox.addItem("Default");

        // Create a SQL query using the selected value
        String query = "SELECT CONCAT(lastname, ', ', firstname) AS candidate_name, poll_id FROM options WHERE poll_id = ? ORDER BY candidate_name";

        try (PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setInt(1, pollId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String candidateName = rs.getString("candidate_name");
                comboBox.addItem(candidateName);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error while processing populateCandidates. File : CandidateAccessObject.");
        }
    }
    
    public void showData(String title, PieChart chart){
        chart.clearData();
            String sql = """
                     SELECT p.title AS poll_title, CONCAT(o.lastname, ', ', o.firstname) AS candidate_name, COUNT(v.vote_id) AS vote_count
                     FROM Options o
                     JOIN Polls p ON o.poll_id = p.poll_id
                     LEFT JOIN Votes v ON o.option_id = v.option_id
                     WHERE p.title = ?
                     GROUP BY p.title, CONCAT(o.lastname, ', ', o.firstname)""";
        try(PreparedStatement pstmt = DatabaseConnector.getConnection().prepareStatement(sql)){
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            int index=0;
            while(rs.next()){
                String candidate_title = rs.getString("candidate_name");
                int vote_count = rs.getInt("vote_count");
                chart.addData(new ModelPieChart(candidate_title, vote_count, getColor(index++)));
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error while processing showData. File : CandidateAccessObject.");
        }
    }
    
    public int getPollId(String selectedValue){
        String query = "SELECT poll_id FROM polls WHERE title = ?";
        
        try(PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setString(1, selectedValue);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return rs.getInt("poll_id");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error while processing getPollId. File : CandidateAccessObject.");
        }
        
        return 0;
    }
            
    public int getCandidateId(String selectedValue){
        String query = "SELECT option_id FROM options WHERE CONCAT(lastname, ', ',firstname) = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setString(1, selectedValue);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return rs.getInt("option_id");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, " Error while processing getCandidateId. File : CandidateAccessObject.");
        }
        return 0;
    }
    
    public int recordCountForCandidates(String columnName, String value){
        String countRecordQuery = "SELECT COUNT(*) FROM options WHERE " + columnName + " = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(countRecordQuery)){
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
    
    public int recordCountForCandidates(){
        String countRecordQuery = "SELECT COUNT(*) FROM options";
        try(PreparedStatement preparedStatement = connection.prepareStatement(countRecordQuery)){
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error in recordCountForUser method. File: UserAccessObject");
        }
        return 0;
    }
    
    public List<CandidateTransferObject> getAllCandidates() {
        List<CandidateTransferObject> candidates = new ArrayList<>();
        String query = """
                        SELECT options.option_id, options.firstname, options.lastname, polls.title 
                        FROM options
                        INNER JOIN polls
                        ON polls.poll_id = options.poll_id
                        ORDER BY options.lastname""";

        // Execute SQL query to fetch data from the database
        try(PreparedStatement pstmt = connection.prepareStatement(query)){
            ResultSet rs = pstmt.executeQuery();
            
            // Iterate over the result set and create Employee objects
            while (rs.next()) {
                int candidateId = rs.getInt("option_id");
                String candidateFirstName = rs.getString("firstname");
                String candidateLastName = rs.getString("lastname");
                String title = rs.getString("title");    

                CandidateTransferObject cand = new CandidateTransferObject(candidateId, candidateFirstName, candidateLastName, title);
                candidates.add(cand);
            }

            return candidates;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error occurred during the getAllCandidates method. File: CandidateAccessObject.");
        }

        return null;
    }
    
    public boolean registerCandidate(int pollID, String firstname, String lastname) {
        String registerQuery = "INSERT INTO options (poll_id, firstname, lastname) VALUES (?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(registerQuery)){
            pstmt.setInt(1, pollID);
            pstmt.setString(2, firstname);
            pstmt.setString(3, lastname);
            
            pstmt.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error occurred during the registerCandidate method. File: CandidateAccessObject.");
        }
        return false;
    }
    
    public boolean updateCandidate(CandidateTransferObject candidateData){
        
        // Validate input data
        if (candidateData == null || candidateData.getCandidateId() <= 0) {
            System.err.println("Invalid candidate data provided for update.");
            return false;
        }
        
        String updateQuery = "UPDATE options SET poll_id= ?, firstname = ?, lastname = ? WHERE option_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            pstmt.setInt(1, candidateData.getPollId());
            pstmt.setString(2, candidateData.getCandidateFirstName());
            pstmt.setString(3, candidateData.getCandidateLastName());
            pstmt.setInt(4, candidateData.getCandidateId());
            pstmt.executeUpdate();
            
            return true;
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Error occurred during the updateCandidate method. File: CandidateAccessObject.");
            return false;
        }
    } 
    
    
    
    public boolean deleteCandidate(CandidateTransferObject candidateData) {
        
        // Validate input data
        if (candidateData == null || candidateData.getCandidateId() <= 0) {
            System.err.println("Invalid candidate data provided for update.");
            return false;
        }
        
        String deleteQuery = "DELETE FROM options WHERE option_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(deleteQuery)){
            pstmt.setInt(1, candidateData.getCandidateId());
            pstmt.executeUpdate();
            
            return true;
            
        } catch(SQLException ex) { 
            ex.printStackTrace();
            System.err.println("Error occurred during the updateCandidate method. File: CandidateAccessObject.");
            
            return false;
        }
    }
}