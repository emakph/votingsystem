/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataFiles;

/**
 *
 * @author REYMARK
 */
public class CastVote {

    public String getPresident() {
        return president;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public String getVicePresident() {
        return vicePresident;
    }

    public void setVicePresident(String vicePresident) {
        this.vicePresident = vicePresident;
    }

    public String getSecretary() {
        return secretary;
    }

    public void setSecretary(String secretary) {
        this.secretary = secretary;
    }

    public String getTreasurer() {
        return treasurer;
    }

    public void setTreasurer(String treasurer) {
        this.treasurer = treasurer;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getPioMale() {
        return pioMale;
    }

    public void setPioMale(String pioMale) {
        this.pioMale = pioMale;
    }

    public String getPioFemale() {
        return pioFemale;
    }

    public void setPioFemale(String pioFemale) {
        this.pioFemale = pioFemale;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public CastVote(String president, String vicePresident, String secretary, String treasurer, String auditor, String pioMale, String pioFemale, String representative) {
        this.president = president;
        this.vicePresident = vicePresident;
        this.secretary = secretary;
        this.treasurer = treasurer;
        this.auditor = auditor;
        this.pioMale = pioMale;
        this.pioFemale = pioFemale;
        this.representative = representative;
    }

    public CastVote() {
        
    }
    
    private String president;
    private String vicePresident;
    private String secretary;
    private String treasurer;
    private String auditor;
    private String pioMale;
    private String pioFemale;
    private String representative;
}
