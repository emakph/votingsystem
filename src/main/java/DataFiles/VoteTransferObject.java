/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataFiles;

/**
 *
 * @author REYMARK
 */
public class VoteTransferObject {
    private int voteId;
    private int pollId;
    private int user_id;
    public int option_id;

    public VoteTransferObject() {
        
    }

    public VoteTransferObject(int pollId, int user_id, int option_id) {
        this.pollId = pollId;
        this.user_id = user_id;
        this.option_id = option_id;
    }

    
    public VoteTransferObject(int voteId, int pollId, int user_id, int option_id) {
        this.voteId = voteId;
        this.pollId = pollId;
        this.user_id = user_id;
        this.option_id = option_id;
    }
    
    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public int getPollId() {
        return pollId;
    }

    public void setPollId(int pollId) {
        this.pollId = pollId;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }
}
