/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataFiles;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class VoteAccessObject {
    private Connection connection;
    
    public VoteAccessObject() {
        // Establish a database connection
        // ...
        try{
           connection = DatabaseConnector.getConnection() ;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public List<VoteTransferObject> getAllVotes(){
        List<VoteTransferObject> allVotes = new ArrayList<>();
        String query = "SELECT * FROM votes";
        try(PreparedStatement pstmt = connection.prepareStatement(query)){
            ResultSet rs = pstmt.executeQuery();

            // Iterate over the result set and create Employee objects
            while (rs.next()) {
                int voteId = rs.getInt("vote_id");
                int pollId = rs.getInt("poll_id");
                int userId = rs.getInt("user_id");
                int optionId = rs.getInt("option_id");

                VoteTransferObject vote = new VoteTransferObject(voteId, pollId, userId, optionId);
                allVotes.add(vote);
            }
            
            return allVotes;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return null;
    }
    
    public int countVotes(int poll_id, int candidateID){
        String query = "SELECT COUNT(*) FROM votes WHERE poll_id = ? AND option_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setInt(1, poll_id);
            pstmt.setInt(2, candidateID);
            
            ResultSet rs = pstmt.executeQuery();
            
            return rs.getInt("count");
        } catch (SQLException ex) {
            return 0;
        }
    }
    
    // Inserting data into database by batch
    public boolean castVote(List<VoteTransferObject> votes){
        String sql = "INSERT INTO votes (poll_id, option_id, user_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            // Iterate over the rows and insert them
            for (VoteTransferObject vote : votes) {
                preparedStatement.setInt(1, vote.getPollId());
                preparedStatement.setInt(2, vote.getOption_id());
                preparedStatement.setInt(3, vote.getUser_id());
                
                preparedStatement.addBatch(); // Add the row to the batch
            }
            preparedStatement.executeBatch();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error in castVote method. File: VoteAccessObject");
        }
        return false;
    }
    
    // Method to check eligibility of the voter
    public boolean isEligible(int userID){
        String sql = "SELECT user_id FROM votes WHERE user_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error occurred during the getAllCandidates method. File: CandidateAccessObject.");
        }
        return false;
    }
    
    public int voteCounter(int optionID){
        String sql = "SELECT COUNT(option_id) FROM votes WHERE option_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, optionID);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()){
                return rs.getInt("count");
            }
        } catch(SQLException ex) {
            
        }
        return 0;
    }
}