/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataFiles;

import java.sql.Date;

/**
 *
 * @author REYMARK
 */
public class UserTransferObject {
    
    private int userID;
    private String firstname;
    private String lastname;
    private Date birthDate;
    private String gender;
    private String status;
    private String municipality;
    private String barangay;
    private String email;
    private String password;
    private Date registrationDate;
    private String role;
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }
    
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public UserTransferObject(){
        
    }
    
    public UserTransferObject(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public UserTransferObject(int userID, String firstname, String email, String role) {
        this.userID = userID;
        this.firstname = firstname;
        this.email = email;
        this.role = role;
    }

    public UserTransferObject(String firstname, String lastname, Date birthDate, String gender, String status, String municipality, String barangay, String email, String role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.status = status;
        this.municipality = municipality;
        this.barangay = barangay;
        this.email = email;
        this.role = role;
    }

    public UserTransferObject(int userID, String firstname, String lastname, Date birthDate, String gender, String status, String municipality, String barangay, String email, String password, Date registrationDate, String role) {
        this.userID = userID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.status = status;
        this.municipality = municipality;
        this.barangay = barangay;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.role = role;
    }
    
}