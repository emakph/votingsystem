/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataFiles;

/**
 *
 * @author REYMARK
 */
public class CandidateTransferObject {
    
    private int candidateId;
    private int pollId;
    private String candidateFirstName;
    private String candidateLastName;
    private String candidateTitle;

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public void setPollId(int pollId) {
        this.pollId = pollId;
    }

    public void setCandidateFirstName(String candidateFirstName) {
        this.candidateFirstName = candidateFirstName;
    }

    public void setCandidateLastName(String candidateLastName) {
        this.candidateLastName = candidateLastName;
    }

    public void setCandidateTitle(String candidateTitle) {
        this.candidateTitle = candidateTitle;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public int getPollId() {
        return pollId;
    }
    
    public String getCandidateFirstName() {
        return candidateFirstName;
    }

    public String getCandidateLastName() {
        return candidateLastName;
    }

    public String getCandidateTitle() {
        return candidateTitle;
    }

    public CandidateTransferObject() {
        
    }

    public CandidateTransferObject(int candidateId, String candidateFirstName, String candidateLastName, String candidateTitle) {
        this.candidateId = candidateId;
        this.candidateFirstName = candidateFirstName;
        this.candidateLastName = candidateLastName;
        this.candidateTitle = candidateTitle;
    }
    
    public CandidateTransferObject(int candidateId, int pollId, String candidateFirstName, String candidateLastName) {
        this.candidateId = candidateId;
        this.pollId = pollId;
        this.candidateFirstName = candidateFirstName;
        this.candidateLastName = candidateLastName;
    }
    
}
