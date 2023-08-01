/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package finals.finalvoting;

import CustomCards.ModelCard;
import CustomMenu.ModelSidebar;
import CustomTable.TableActionCellEditor;
import CustomTable.TableActionCellRenderer;
import CustomTable.TableActionEvent;
import DataFiles.*;
import java.util.Calendar;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.*;

/**
 *
 * @author REYMARK
 */
public class AdminDashboard extends javax.swing.JFrame {
    private static UserTransferObject user;
    private UserAccessObject userAccessObj;
    private CandidateAccessObject candidatesAccessObject;
    private List<CandidateTransferObject> candidates;
    private List<UserTransferObject> allUser;
    private DefaultTableModel candidateModel;
    private DefaultTableModel allUserModel;
    private DefaultTableModel adminTableModel;
    /**
     * Creates new form Dashboard
     * @param user
     */
    
    public AdminDashboard(UserTransferObject user) {
        initComponents();
        AdminDashboard.user = user;
        candidatesAccessObject = new CandidateAccessObject();
        userAccessObj = new UserAccessObject();
        
        String fullname = user.getFirstname() + " " + user.getLastname();
        dashboardAccountName.setText(fullname);
        
        // breadcrumbs
        
        breadcrumbsDashboard.setText(fullname + " / Dashboard");
        breadcrumbsUsers.setText(fullname + " / All Users");
        breadcrumbsCandidates.setText(fullname + " / Candidates");
        breadcrumbsVotingResults.setText(fullname + " / Voting Results");
        breadcrumbsSettings.setText(fullname + " / Settings");
        
        
        /************************************************/
        /*                                              */
        /*            DASHBOARD STARTS HERE             */
        /*                                              */
        /************************************************/
        
        // Sidebar Navigation
        listMenu1.addItem(new ModelSidebar("images/1", "Dashboard", ModelSidebar.MenuType.MENU));
        listMenu1.addItem(new ModelSidebar("images/2", "Users", ModelSidebar.MenuType.MENU));
        listMenu1.addItem(new ModelSidebar("images/33", "Candidates", ModelSidebar.MenuType.MENU));
        listMenu1.addItem(new ModelSidebar("images/4", "Voting Results", ModelSidebar.MenuType.MENU));
        listMenu1.addItem(new ModelSidebar("images/5", "Settings", ModelSidebar.MenuType.MENU));
        
        // Populating data in Notification list
        UserAccessObject usersForNotif = new UserAccessObject();
        latestList.setModel(usersForNotif.populateJList("email", "users"));
        
        // Gradient Cards
        gradientCard1.setData(new ModelCard(new ImageIcon(getClass().getResource("/images/2.png")), "Record Count", String.valueOf(userAccessObj.recordCountForUser()), "Overall Total of Users"));
        gradientCard2.setData(new ModelCard(new ImageIcon(getClass().getResource("/images/admin-20.png")), "Registered Voters", String.valueOf(userAccessObj.recordCountForUser("role", "voter")), "Overall Total of Voters"));
        gradientCard4.setData(new ModelCard(new ImageIcon(getClass().getResource("/images/candidates-20.png")), "Registered Candidates", String.valueOf(candidatesAccessObject.recordCountForCandidates()), "Overall Total of Candidates"));
        
        
        /**********************************************/
        /*                                            */
        /*  DASHBOARD > ALL USERS TABLE STARTS HERE   */
        /*                                            */
        /**********************************************/
        
        allUserModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make columns uneditable
                return column != 0 && column != 1 && column != 2 && column != 3 && column != 4 && column != 5 && column != 6 && column != 7 && column != 8;
            }
        };
        
        allUserModel.setColumnIdentifiers(new Object[]{"First Name", "Last Name", "Birthday", "Gender", "Status", "Municipality", "Barangay", "Email", "Role", "Actions"});
        UserAccessObject allUserAccessObj = new UserAccessObject();
        allUser = allUserAccessObj.getAllUsers();
        
        // POPULATING TABLE WITH DATA FROM DATABASE
        for(UserTransferObject eachUser : allUser){
            if(eachUser.getEmail().equals(user.getEmail())){
                continue;
            }
            allUserModel.addRow(new Object[]{eachUser.getFirstname(), eachUser.getLastname(), eachUser.getBirthDate(), eachUser.getGender(), eachUser.getStatus(), eachUser.getMunicipality(), eachUser.getBarangay(), eachUser.getEmail(), eachUser.getRole()});
        }
        usersTable.setModel(allUserModel);
        usersTable.getColumnModel().getColumn(9).setCellRenderer(new TableActionCellRenderer());
        
        // EVENT HANDLER FOR BUTTONS INSIDE 
        TableActionEvent eventForAllUser = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                // Opening the update dialog
                updateUserDialog.setLocationRelativeTo(mainPanel);
                updateUserDialog.setVisible(true);
                
                // Get selected row and set to fields inside the dialog
                String userEmail = allUserModel.getValueAt(usersTable.getSelectedRow(), 7).toString();
                UserAccessObject updateUserAccessObject = new UserAccessObject();
                List<UserTransferObject> freshUserData = updateUserAccessObject.getAllUsers();
                
                for(UserTransferObject voter : freshUserData){
                    if(userEmail.equals(voter.getEmail())){
                        voterUpdateFirstName.setText(voter.getFirstname());
                        voterUpdateLastName.setText(voter.getLastname());
                        voterUpdateBirthday.setDate(voter.getBirthDate());
                        voterUpdateGender.setSelectedItem(voter.getGender());
                        voterUpdateStatus.setSelectedItem(voter.getStatus());
                        voterUpdateMunicipality.setSelectedItem(voter.getMunicipality());
                        
                        // Populate barangays in JComboBox
                        voterUpdateBarangay.removeAllItems();
                        LocationHandler locationHandler = new LocationHandler();
                        if (voterUpdateMunicipality.getSelectedItem().equals("Default")) {
                            voterUpdateBarangay.addItem("Default");
                        } else if(voterUpdateMunicipality.getSelectedItem().equals("Agdangan")){
                            for (String barangayLocation : locationHandler.getAgdanganBarangay()) {
                                voterUpdateBarangay.addItem(barangayLocation);
                            }
                        } else if (voterUpdateMunicipality.getSelectedItem().equals("Unisan")) {
                            for (String barangayLocation : locationHandler.getUnisanBarangay()) {
                                voterUpdateBarangay.addItem(barangayLocation);
                            }
                        } else if (voterUpdateMunicipality.getSelectedItem().equals("Padre Burgos")) {
                            for (String barangayLocation : locationHandler.getPadreBurgosBarangay()) {
                                voterUpdateBarangay.addItem(barangayLocation);
                            }
                        }
                        
                        voterUpdateBarangay.setSelectedItem(voter.getBarangay());
                        voterUpdateEmail.setText(voter.getEmail());
                        voterUpdateRole.setSelectedItem(voter.getRole());
                    }
                }
            }

            @Override
            public void onDelete(int row) {
                if (usersTable.isEditing()) {
                    usersTable.getCellEditor().stopCellEditing();
                }
                DefaultTableModel deleteModel = (DefaultTableModel) usersTable.getModel();
                String userEmail = allUserModel.getValueAt(usersTable.getSelectedRow(), 7).toString();
                UserTransferObject deleteCandidateObject = new UserTransferObject();
                
                deleteCandidateObject.setEmail(userEmail);
                int deleteDialog = JOptionPane.showConfirmDialog(mainPanel, "Delete Candidate with email " + deleteCandidateObject.getEmail() + "?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                switch (deleteDialog) {
                    case JOptionPane.YES_OPTION -> {
                        JOptionPane.showMessageDialog(mainPanel, "User with ID #" + deleteCandidateObject.getEmail() + " is successfully removed.");
                        allUserAccessObj.deleteUser(deleteCandidateObject);
                        deleteModel.removeRow(row);
                        gradientCard1.setData(new ModelCard(new ImageIcon(getClass().getResource("/images/2.png")), "Record Count", String.valueOf(userAccessObj.recordCountForUser()), "Overall Total of Users"));
                        if(deleteCandidateObject.getRole().equals("admin")){
                            gradientCard2.setData(new ModelCard(new ImageIcon(getClass().getResource("/images/admin-20.png")), "Registered Voters", String.valueOf(userAccessObj.recordCountForUser("role", "voter")), "Overall Total of Voters"));
                        }
                    }
                    case JOptionPane.NO_OPTION -> {
                    }
                    case JOptionPane.CLOSED_OPTION -> {
                        JOptionPane.showMessageDialog(mainPanel, "Dialog was closed without making a choice.");
                    }
                    default -> {
                    }
                }
            }
        };
        
        usersTable.getColumnModel().getColumn(9).setCellEditor(new TableActionCellEditor(eventForAllUser));
        
        /**********************************************/
        /*                                            */
        /*   DASHBOARD > ALL USERS TABLE ENDS HERE    */
        /*                                            */
        /**********************************************/
        
        
        /************************************************/
        /*                                              */
        /*   DASHBOARD > CANDIDATES TABLE STARTS HERE   */
        /*                                              */
        /************************************************/
        
        // Action handler for custom buttons inside cells
        candidateModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make column 1, column2, and column 3 uneditable
                return column != 0 && column != 1 && column != 2;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Integer.class : Object.class;
            }
        };
        candidateModel.setColumnIdentifiers(new Object[]{"ID", "Candidate Name", "Title", "Actions"});
        // Set minimum width for each column
        
        CandidateAccessObject candAccessObj = new CandidateAccessObject();
        
        // ASSIGNING CANDIDATES FROM DATABASE TO LIST
        candidates = candAccessObj.getAllCandidates();
        
        // POPULATING TABLE WITH DATA FROM DATABASE
        for(CandidateTransferObject candi : candidates){
            candidateModel.addRow(new Object[]{candi.getCandidateId(), candi.getCandidateLastName() + ", " + candi.getCandidateFirstName(), candi.getCandidateTitle()});
        }
        
        candidatesTable.setModel(candidateModel);
        candidatesTable.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRenderer());
        
        // EVENT HANDLER FOR BUTTONS INSIDE 
        TableActionEvent eventForCandidate = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                // Opening the update dialog
                updateCandidatesDialog.setLocationRelativeTo(mainPanel);
                updateCandidatesDialog.setVisible(true);
                
                // Assigning values into title combobox
                candAccessObj.populatePolls(updateCDTitleComBox);
                
                // Get selected row and set to fields inside the dialog
                int candidateID = (int) candidateModel.getValueAt(candidatesTable.getSelectedRow(), 0);
                CandidateAccessObject updateCandidateAccessObject = new CandidateAccessObject();
                List<CandidateTransferObject> freshCandidateData = updateCandidateAccessObject.getAllCandidates();
                
                for(CandidateTransferObject eachRow: freshCandidateData){
                    if(eachRow.getCandidateId()== candidateID){
                        updateCDIDField.setText(String.valueOf(eachRow.getCandidateId()));
                        updateCDFirstNameField.setText(eachRow.getCandidateFirstName());
                        updateCDLastNameField.setText(eachRow.getCandidateLastName());
                        updateCDTitleComBox.setSelectedItem(eachRow.getCandidateTitle());
                        
                        break;
                    }
                }
            }

            @Override
            public void onDelete(int row) {
                if (candidatesTable.isEditing()) {
                    candidatesTable.getCellEditor().stopCellEditing();
                }
                DefaultTableModel deleteModel = (DefaultTableModel) candidatesTable.getModel();
                int candidateID = (int)candidateModel.getValueAt(candidatesTable.getSelectedRow(), 0);
                CandidateTransferObject deleteCandidateObject = new CandidateTransferObject();
                
                deleteCandidateObject.setCandidateId(candidateID);
                
                int deleteDialog = JOptionPane.showConfirmDialog(mainPanel, "Delete Candidate with ID #" + deleteCandidateObject.getCandidateId() + "?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                switch (deleteDialog) {
                    case JOptionPane.YES_OPTION -> {
                        JOptionPane.showMessageDialog(mainPanel, "User with ID #" + deleteCandidateObject.getCandidateId() + " is successfully removed.");
                        candAccessObj.deleteCandidate(deleteCandidateObject);
                        deleteModel.removeRow(row);
                    }
                    case JOptionPane.NO_OPTION -> {
                    }
                    case JOptionPane.CLOSED_OPTION -> {
                        JOptionPane.showMessageDialog(mainPanel, "Dialog was closed without making a choice.");
                    }
                    default -> {
                    }
                }
            }
        };
        candidatesTable.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(eventForCandidate));
        
        /* CUSTOM CANDIDATES TABLE ENDS HERE */
        
        /*************************************************/
        /*                                               */
        /*      POLLS PAGE CUSTOMIZATION STARTS HERE     */
        /*                                               */
        /*************************************************/
        
        candidatesAccessObject.populatePolls(piechartCombox);
        
        // Set minimum width for each column
        
        DefaultTableModel resultTableModel = new DefaultTableModel();
        VoteAccessObject voteAccessObject = new VoteAccessObject();
        
        testlabel.setText(String.valueOf(voteAccessObject.voteCounter(1)));
        
        // ASSIGNING CANDIDATES FROM DATABASE TO LIST
        //candidates = voteAccessObject.getAllCandidates();
        
        
        // POPULATING TABLE WITH DATA FROM DATABASE
        for(CandidateTransferObject candi : candidates){
            resultTableModel.addRow(new Object[]{candi.getCandidateId(), candi.getCandidateLastName() + ", "+ candi.getCandidateFirstName(), candi.getCandidateTitle()});
        }
        
        votingResultTable.setModel(resultTableModel);
        
        /*************************************************/
        /*                                               */
        /*       POLLS PAGE CUSTOMIZATION ENDS HERE      */
        /*                                               */
        /*************************************************/
        
        
        /****************************************************/
        /*                                                  */
        /*      SETTINGS PAGE CUSTOMIZATION STARTS HERE     */
        /*                                                  */
        /****************************************************/
        
        CandidateAccessObject settingsAccessObject = new CandidateAccessObject();
        
        addCandidateFirstnameField.setHint("First Name");
        addCandidateLastnameField.setHint("Last Name");
        settingsAccessObject.populatePolls(addCandidateTitleCombox);
        
        // Show all data of logged-in Admin
        settingsAdminValueLabel.setText(String.valueOf(user.getUserID()));
        settingsFirstNameValueLabel.setText(user.getFirstname());
        settingsLastNameValueLabel.setText(user.getLastname());
        settingsBirthdateValueLabel.setText(user.getBirthDate().toString());
        settingsGenderValueLabel.setText(user.getGender());
        settingsStatusValueLabel.setText(user.getStatus());
        settingsMunicipalityValueLabel.setText(user.getMunicipality());
        settingsBarangayValueLabel.setText(user.getBarangay());
        settingsEmailValueLabel.setText(user.getEmail());
        settingsPasswordValueLabel.setText(user.getPassword());
        
        // Show all admin
        adminTableModel = new DefaultTableModel();
        UserAccessObject adminObject = new UserAccessObject();
        adminTableModel.addColumn("Admin ID");
        adminTableModel.addColumn("Name");
        adminTableModel.addColumn("Email");
        
        List<UserTransferObject> admins = adminObject.getAllAdmin();
        
        // POPULATING ADMIN TABLE WITH DATA FROM DATABASE
        for(UserTransferObject eachAdmin : admins){
            adminTableModel.addRow(new Object[]{eachAdmin.getUserID(), eachAdmin.getLastname() + ", " + eachAdmin.getFirstname(), eachAdmin.getEmail()});
        }
        adminsTable.setModel(adminTableModel);
        
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        updateCandidatesDialog = new javax.swing.JDialog();
        updateCandidatesPanel = new javax.swing.JPanel();
        updateCDFirstNameLabel = new javax.swing.JLabel();
        updateCDLastNameLabel = new javax.swing.JLabel();
        updateCDFirstNameField = new javax.swing.JTextField();
        updateCDLastNameField = new javax.swing.JTextField();
        updateCDButton = new javax.swing.JButton();
        updateCDTitleLabel = new javax.swing.JLabel();
        updateCDTitleComBox = new javax.swing.JComboBox<>();
        backCDButton = new javax.swing.JButton();
        updateCDIDLabel = new javax.swing.JLabel();
        updateCDIDField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        updateUserDialog = new javax.swing.JDialog();
        updateUserPanel = new javax.swing.JPanel();
        voterUpdateFirstName = new javax.swing.JTextField();
        voterUpdateBirthday = new com.toedter.calendar.JDateChooser();
        jLabel20 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        voterUpdateEmail = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        voterUpdateGender = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        voterUpdateLastName = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        voterUpdateStatus = new javax.swing.JComboBox<>();
        updateUserDialogSaveButton = new javax.swing.JButton();
        voterUpdateRole = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        voterUpdateMunicipality = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        voterUpdateBarangay = new javax.swing.JComboBox<>();
        updateUserDialogBackButton = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        addUserDialog = new javax.swing.JDialog();
        addUserMainPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        addUserFirstnameField = new CustomTextField.MyTextField();
        addUserLastnameField = new CustomTextField.MyTextField();
        addUserBirthdateChooser = new com.toedter.calendar.JDateChooser();
        addUserStatusCombox = new javax.swing.JComboBox<>();
        addUserGenderCombox = new javax.swing.JComboBox<>();
        addUserBarangayCombox = new javax.swing.JComboBox<>();
        addUserMunicipalityCombox = new javax.swing.JComboBox<>();
        addUserEmailField = new CustomTextField.MyTextField();
        addUserPasswordField = new CustomTextField.MyTextField();
        addUserRoleCombox = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        AddUserSaveButton = new javax.swing.JButton();
        addUserBackButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        leftSide = new javax.swing.JPanel();
        logoutButton = new javax.swing.JLabel();
        dashboardAccountName = new javax.swing.JLabel();
        listMenu1 = new CustomMenu.ListMenu<>();
        dashboardMainIcon = new javax.swing.JLabel();
        votingTextIcon = new javax.swing.JLabel();
        systemTextIcon = new javax.swing.JLabel();
        rightSide = new javax.swing.JPanel();
        dashboardPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        latestList = new javax.swing.JList<>();
        registrationNotificationTitle = new javax.swing.JLabel();
        dashboardPageTitle = new javax.swing.JLabel();
        breadcrumbsDashboard = new javax.swing.JLabel();
        registrationNotificationTitle1 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        usersPanel = new javax.swing.JPanel();
        usersPageTitle = new javax.swing.JLabel();
        breadcrumbsUsers = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        gradientCard1 = new CustomCards.GradientCard();
        gradientCard2 = new CustomCards.GradientCard();
        gradientCard4 = new CustomCards.GradientCard();
        usersPanelCardLayout = new javax.swing.JPanel();
        viewUsersPanel = new javax.swing.JPanel();
        usersAddUserButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        usersTable = new javax.swing.JTable();
        candidatesPanel = new javax.swing.JPanel();
        breadcrumbsCandidates = new javax.swing.JLabel();
        candidataesPageTitle = new javax.swing.JLabel();
        pollsCandidateScrollPanel = new javax.swing.JScrollPane();
        candidatesTable = new javax.swing.JTable();
        addCandidatePanel = new javax.swing.JPanel();
        addCandidateHereLabel = new javax.swing.JLabel();
        addCandidateFirstnameField = new CustomTextField.MyTextField();
        addCandidateLastnameField = new CustomTextField.MyTextField();
        addCandidateTitleCombox = new javax.swing.JComboBox<>();
        addCandidateButton = new javax.swing.JButton();
        clearCandidateButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        votingPanel = new javax.swing.JPanel();
        piechartCombox = new javax.swing.JComboBox<>();
        pollsLabelForChart = new javax.swing.JLabel();
        breadcrumbsVotingResults = new javax.swing.JLabel();
        votingResultsPageTitle = new javax.swing.JLabel();
        pieChart1 = new CustomChart.PieChart();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        votingResultTable = new javax.swing.JTable();
        testlabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        settingsPanel = new javax.swing.JPanel();
        settingsPageTitle = new javax.swing.JLabel();
        breadcrumbsSettings = new javax.swing.JLabel();
        settingsCardLayout = new javax.swing.JPanel();
        showDetailsPanel = new javax.swing.JPanel();
        settingsFirstNameLabel = new javax.swing.JLabel();
        settingsFirstNameValueLabel = new javax.swing.JLabel();
        settingsLastNameLabel = new javax.swing.JLabel();
        settingsLastNameValueLabel = new javax.swing.JLabel();
        settingsAdminIDLabel = new javax.swing.JLabel();
        settingsAdminValueLabel = new javax.swing.JLabel();
        settingsBirthdateLabel = new javax.swing.JLabel();
        settingsBirthdateValueLabel = new javax.swing.JLabel();
        settingsGenderLabel = new javax.swing.JLabel();
        settingsGenderValueLabel = new javax.swing.JLabel();
        settingsStatusLabel = new javax.swing.JLabel();
        settingsStatusValueLabel = new javax.swing.JLabel();
        settingsMunicipalityLabel = new javax.swing.JLabel();
        settingsMunicipalityValueLabel = new javax.swing.JLabel();
        settingsBarangayLabel = new javax.swing.JLabel();
        settingsBarangayValueLabel = new javax.swing.JLabel();
        settingsEmailLabel = new javax.swing.JLabel();
        settingsEmailValueLabel = new javax.swing.JLabel();
        settingsPasswordLabel = new javax.swing.JLabel();
        settingsPasswordValueLabel = new javax.swing.JLabel();
        settingsUpdateDetailsButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        editDetailsPanel = new javax.swing.JPanel();
        settingsAdminID1 = new javax.swing.JLabel();
        settingsFirstNameLabel1 = new javax.swing.JLabel();
        settingsLastNameLabel1 = new javax.swing.JLabel();
        settingsBirthdateLabel1 = new javax.swing.JLabel();
        settingsGenderLabel1 = new javax.swing.JLabel();
        settingsStatusLabel1 = new javax.swing.JLabel();
        settingsMunicipalityLabel1 = new javax.swing.JLabel();
        settingsBarangayLabel1 = new javax.swing.JLabel();
        settingsEmailLabel1 = new javax.swing.JLabel();
        settingsPasswordLabel1 = new javax.swing.JLabel();
        settingsAdminIDTextField = new javax.swing.JTextField();
        settingsFirstNameTextField = new javax.swing.JTextField();
        settingsLastNameTextField = new javax.swing.JTextField();
        settingsEmailTextField = new javax.swing.JTextField();
        settingsPasswordTextField = new javax.swing.JTextField();
        settingsBirthdateChooser = new com.toedter.calendar.JDateChooser();
        settingsGenderCombox = new javax.swing.JComboBox<>();
        settingsStatusCombox = new javax.swing.JComboBox<>();
        settingsMunicipalityCombox = new javax.swing.JComboBox<>();
        settingsBarangayCombox = new javax.swing.JComboBox<>();
        settingsSaveChangesButton = new javax.swing.JButton();
        settingsCancelChangesButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        adminsTable = new javax.swing.JTable();

        updateCandidatesDialog.setTitle("Voting System - Admin Dashboard > Update User");
        updateCandidatesDialog.setAlwaysOnTop(true);
        updateCandidatesDialog.setResizable(false);
        updateCandidatesDialog.setSize(new java.awt.Dimension(400, 580));

        updateCandidatesPanel.setBackground(new java.awt.Color(28, 33, 32));
        updateCandidatesPanel.setPreferredSize(new java.awt.Dimension(400, 580));
        updateCandidatesPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        updateCDFirstNameLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        updateCDFirstNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        updateCDFirstNameLabel.setText("First Name");
        updateCandidatesPanel.add(updateCDFirstNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, 240, 30));

        updateCDLastNameLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        updateCDLastNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        updateCDLastNameLabel.setText("Last Name");
        updateCandidatesPanel.add(updateCDLastNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 280, 240, 30));

        updateCDFirstNameField.setBackground(new java.awt.Color(71, 74, 73));
        updateCDFirstNameField.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        updateCDFirstNameField.setForeground(new java.awt.Color(255, 255, 255));
        updateCDFirstNameField.setBorder(null);
        updateCDFirstNameField.setCaretColor(new java.awt.Color(255, 255, 255));
        updateCDFirstNameField.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        updateCDFirstNameField.setSelectionColor(new java.awt.Color(255, 255, 255));
        updateCandidatesPanel.add(updateCDFirstNameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 240, 240, 30));

        updateCDLastNameField.setBackground(new java.awt.Color(71, 74, 73));
        updateCDLastNameField.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        updateCDLastNameField.setForeground(new java.awt.Color(255, 255, 255));
        updateCDLastNameField.setBorder(null);
        updateCDLastNameField.setCaretColor(new java.awt.Color(255, 255, 255));
        updateCDLastNameField.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        updateCDLastNameField.setSelectionColor(new java.awt.Color(255, 255, 255));
        updateCandidatesPanel.add(updateCDLastNameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 310, 240, 30));

        updateCDButton.setBackground(new java.awt.Color(255, 87, 87));
        updateCDButton.setFont(new java.awt.Font("Poppins SemiBold", 0, 14)); // NOI18N
        updateCDButton.setForeground(new java.awt.Color(255, 255, 255));
        updateCDButton.setText("Update");
        updateCDButton.setBorder(null);
        updateCDButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        updateCDButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCDButtonActionPerformed(evt);
            }
        });
        updateCandidatesPanel.add(updateCDButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 450, 120, 40));

        updateCDTitleLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        updateCDTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        updateCDTitleLabel.setText("Title");
        updateCandidatesPanel.add(updateCDTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 240, 30));

        updateCDTitleComBox.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        updateCDTitleComBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default" }));
        updateCandidatesPanel.add(updateCDTitleComBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 380, 240, 30));

        backCDButton.setFont(new java.awt.Font("Poppins SemiBold", 0, 12)); // NOI18N
        backCDButton.setText("Back");
        backCDButton.setBorder(null);
        backCDButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backCDButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backCDButtonActionPerformed(evt);
            }
        });
        updateCandidatesPanel.add(backCDButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 450, 110, 40));

        updateCDIDLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        updateCDIDLabel.setForeground(new java.awt.Color(255, 255, 255));
        updateCDIDLabel.setText("Candidate ID");
        updateCandidatesPanel.add(updateCDIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 140, 240, 30));

        updateCDIDField.setEditable(false);
        updateCDIDField.setBackground(new java.awt.Color(71, 74, 73));
        updateCDIDField.setForeground(new java.awt.Color(255, 255, 255));
        updateCDIDField.setBorder(null);
        updateCDIDField.setCaretColor(new java.awt.Color(255, 255, 255));
        updateCDIDField.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        updateCDIDField.setSelectionColor(new java.awt.Color(255, 255, 255));
        updateCandidatesPanel.add(updateCDIDField, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, 240, 30));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/votinglogo2-medium.png"))); // NOI18N
        updateCandidatesPanel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, -1, -1));

        jLabel24.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("VOTING SYSTEM");
        updateCandidatesPanel.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, -1, 40));

        javax.swing.GroupLayout updateCandidatesDialogLayout = new javax.swing.GroupLayout(updateCandidatesDialog.getContentPane());
        updateCandidatesDialog.getContentPane().setLayout(updateCandidatesDialogLayout);
        updateCandidatesDialogLayout.setHorizontalGroup(
            updateCandidatesDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(updateCandidatesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        updateCandidatesDialogLayout.setVerticalGroup(
            updateCandidatesDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(updateCandidatesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        updateCandidatesPanel.getAccessibleContext().setAccessibleName("");
        updateCandidatesPanel.getAccessibleContext().setAccessibleDescription("");

        updateUserDialog.setTitle("Voting System - Admin Dashboard > Update User");
        updateUserDialog.setBackground(new java.awt.Color(28, 28, 57));
        updateUserDialog.setResizable(false);
        updateUserDialog.setSize(new java.awt.Dimension(500, 750));

        updateUserPanel.setBackground(new java.awt.Color(28, 33, 32));
        updateUserPanel.setMinimumSize(new java.awt.Dimension(500, 750));
        updateUserPanel.setPreferredSize(new java.awt.Dimension(500, 750));
        updateUserPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        voterUpdateFirstName.setBackground(new java.awt.Color(71, 74, 73));
        voterUpdateFirstName.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        voterUpdateFirstName.setForeground(new java.awt.Color(255, 255, 255));
        voterUpdateFirstName.setBorder(null);
        voterUpdateFirstName.setCaretColor(new java.awt.Color(255, 255, 255));
        voterUpdateFirstName.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        voterUpdateFirstName.setSelectionColor(new java.awt.Color(255, 255, 255));
        updateUserPanel.add(voterUpdateFirstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 160, 190, 30));
        updateUserPanel.add(voterUpdateBirthday, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 240, 190, 30));

        jLabel20.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Role:");
        updateUserPanel.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 520, 100, 30));

        jLabel16.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Address");
        updateUserPanel.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 360, 100, 30));

        voterUpdateEmail.setBackground(new java.awt.Color(71, 74, 73));
        voterUpdateEmail.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        voterUpdateEmail.setForeground(new java.awt.Color(255, 255, 255));
        voterUpdateEmail.setBorder(null);
        voterUpdateEmail.setCaretColor(new java.awt.Color(255, 255, 255));
        voterUpdateEmail.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        voterUpdateEmail.setSelectionColor(new java.awt.Color(255, 255, 255));
        updateUserPanel.add(voterUpdateEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 480, 190, 30));

        jLabel14.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Civil Status:");
        updateUserPanel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 320, 100, 30));

        voterUpdateGender.setBackground(new java.awt.Color(71, 74, 73));
        voterUpdateGender.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        voterUpdateGender.setForeground(new java.awt.Color(255, 255, 255));
        voterUpdateGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Male", "Female", "I'd Rather Not Say" }));
        voterUpdateGender.setBorder(null);
        updateUserPanel.add(voterUpdateGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 280, 190, 30));

        jLabel12.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Birth Date:");
        updateUserPanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 240, 100, 30));

        jLabel13.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Gender:");
        updateUserPanel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, 100, 30));

        voterUpdateLastName.setBackground(new java.awt.Color(71, 74, 73));
        voterUpdateLastName.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        voterUpdateLastName.setForeground(new java.awt.Color(255, 255, 255));
        voterUpdateLastName.setBorder(null);
        voterUpdateLastName.setCaretColor(new java.awt.Color(255, 255, 255));
        voterUpdateLastName.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        voterUpdateLastName.setSelectionColor(new java.awt.Color(255, 255, 255));
        updateUserPanel.add(voterUpdateLastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 200, 190, 30));

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Last Name:");
        updateUserPanel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, 100, 30));

        jLabel19.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Email:");
        updateUserPanel.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 480, 100, 30));

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("First Name:");
        updateUserPanel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 100, 30));

        voterUpdateStatus.setBackground(new java.awt.Color(71, 74, 73));
        voterUpdateStatus.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        voterUpdateStatus.setForeground(new java.awt.Color(255, 255, 255));
        voterUpdateStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Single", "Married", "Divorced", "Separated", "Widowed" }));
        voterUpdateStatus.setBorder(null);
        updateUserPanel.add(voterUpdateStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 320, 190, 30));

        updateUserDialogSaveButton.setBackground(new java.awt.Color(255, 103, 103));
        updateUserDialogSaveButton.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        updateUserDialogSaveButton.setForeground(new java.awt.Color(255, 255, 255));
        updateUserDialogSaveButton.setText("Save Changes");
        updateUserDialogSaveButton.setBorder(null);
        updateUserDialogSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateUserDialogSaveButtonActionPerformed(evt);
            }
        });
        updateUserPanel.add(updateUserDialogSaveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 600, 150, 40));

        voterUpdateRole.setBackground(new java.awt.Color(71, 74, 73));
        voterUpdateRole.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        voterUpdateRole.setForeground(new java.awt.Color(255, 255, 255));
        voterUpdateRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "voter", "admin" }));
        voterUpdateRole.setBorder(null);
        updateUserPanel.add(voterUpdateRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 520, 190, 30));

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Municipality:");
        updateUserPanel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 400, 100, 30));

        voterUpdateMunicipality.setBackground(new java.awt.Color(71, 74, 73));
        voterUpdateMunicipality.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        voterUpdateMunicipality.setForeground(new java.awt.Color(255, 255, 255));
        voterUpdateMunicipality.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Agdangan", "Padre Burgos", "Unisan" }));
        voterUpdateMunicipality.setBorder(null);
        voterUpdateMunicipality.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                voterUpdateMunicipalityItemStateChanged(evt);
            }
        });
        updateUserPanel.add(voterUpdateMunicipality, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 400, 190, 30));

        jLabel23.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Barangay:");
        updateUserPanel.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 440, 100, 30));

        voterUpdateBarangay.setBackground(new java.awt.Color(71, 74, 73));
        voterUpdateBarangay.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        voterUpdateBarangay.setForeground(new java.awt.Color(255, 255, 255));
        voterUpdateBarangay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default" }));
        voterUpdateBarangay.setBorder(null);
        updateUserPanel.add(voterUpdateBarangay, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 440, 190, 30));

        updateUserDialogBackButton.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        updateUserDialogBackButton.setText("Back");
        updateUserDialogBackButton.setBorder(null);
        updateUserDialogBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateUserDialogBackButtonActionPerformed(evt);
            }
        });
        updateUserPanel.add(updateUserDialogBackButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 600, 150, 40));

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/votinglogo2-medium.png"))); // NOI18N
        updateUserPanel.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, -1, -1));

        jLabel26.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("VOTING SYSTEM");
        updateUserPanel.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, -1, 40));

        javax.swing.GroupLayout updateUserDialogLayout = new javax.swing.GroupLayout(updateUserDialog.getContentPane());
        updateUserDialog.getContentPane().setLayout(updateUserDialogLayout);
        updateUserDialogLayout.setHorizontalGroup(
            updateUserDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
            .addGroup(updateUserDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(updateUserPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        updateUserDialogLayout.setVerticalGroup(
            updateUserDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 870, Short.MAX_VALUE)
            .addGroup(updateUserDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(updateUserDialogLayout.createSequentialGroup()
                    .addComponent(updateUserPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        addUserDialog.setTitle("Voting System - Admin Dashboard > Add User");
        addUserDialog.setSize(new java.awt.Dimension(520, 750));

        addUserMainPanel.setBackground(new java.awt.Color(28, 33, 32));
        addUserMainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/votinglogo2-medium.png"))); // NOI18N
        addUserMainPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, -1, -1));

        jLabel7.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("VOTING SYSTEM");
        addUserMainPanel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, 220, 30));

        addUserFirstnameField.setCaretColor(new java.awt.Color(255, 255, 255));
        addUserFirstnameField.setPrefixIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-name-20.png"))); // NOI18N
        addUserFirstnameField.setSelectedTextColor(new java.awt.Color(28, 33, 32));
        addUserMainPanel.add(addUserFirstnameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, 170, 40));

        addUserLastnameField.setCaretColor(new java.awt.Color(255, 255, 255));
        addUserLastnameField.setPrefixIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-name-20.png"))); // NOI18N
        addUserLastnameField.setSelectedTextColor(new java.awt.Color(28, 33, 32));
        addUserMainPanel.add(addUserLastnameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 170, 170, 40));
        addUserMainPanel.add(addUserBirthdateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, 170, 40));

        addUserStatusCombox.setBackground(new java.awt.Color(71, 74, 73));
        addUserStatusCombox.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        addUserStatusCombox.setForeground(new java.awt.Color(255, 255, 255));
        addUserStatusCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Single", "Married", "Divorced", "Separated", "Widowed" }));
        addUserStatusCombox.setBorder(null);
        addUserMainPanel.add(addUserStatusCombox, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 170, 40));

        addUserGenderCombox.setBackground(new java.awt.Color(71, 74, 73));
        addUserGenderCombox.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        addUserGenderCombox.setForeground(new java.awt.Color(255, 255, 255));
        addUserGenderCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Male", "Female", "Non-Binary" }));
        addUserGenderCombox.setBorder(null);
        addUserMainPanel.add(addUserGenderCombox, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 260, 170, 40));

        addUserBarangayCombox.setBackground(new java.awt.Color(71, 74, 73));
        addUserBarangayCombox.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        addUserBarangayCombox.setForeground(new java.awt.Color(255, 255, 255));
        addUserBarangayCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default" }));
        addUserBarangayCombox.setBorder(null);
        addUserMainPanel.add(addUserBarangayCombox, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 440, 170, 40));

        addUserMunicipalityCombox.setBackground(new java.awt.Color(71, 74, 73));
        addUserMunicipalityCombox.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        addUserMunicipalityCombox.setForeground(new java.awt.Color(255, 255, 255));
        addUserMunicipalityCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Agdangan", "Padre Burgos", "Unisan" }));
        addUserMunicipalityCombox.setBorder(null);
        addUserMunicipalityCombox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                addUserMunicipalityComboxItemStateChanged(evt);
            }
        });
        addUserMainPanel.add(addUserMunicipalityCombox, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 350, 170, 40));

        addUserEmailField.setCaretColor(new java.awt.Color(255, 255, 255));
        addUserEmailField.setPrefixIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-email-20.png"))); // NOI18N
        addUserEmailField.setSelectedTextColor(new java.awt.Color(28, 33, 32));
        addUserMainPanel.add(addUserEmailField, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 500, 360, -1));

        addUserPasswordField.setCaretColor(new java.awt.Color(255, 255, 255));
        addUserPasswordField.setPrefixIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-password-20.png"))); // NOI18N
        addUserPasswordField.setSelectedTextColor(new java.awt.Color(28, 33, 32));
        addUserMainPanel.add(addUserPasswordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 560, 360, -1));

        addUserRoleCombox.setBackground(new java.awt.Color(71, 74, 73));
        addUserRoleCombox.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        addUserRoleCombox.setForeground(new java.awt.Color(255, 255, 255));
        addUserRoleCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "voter", "admin" }));
        addUserRoleCombox.setBorder(null);
        addUserMainPanel.add(addUserRoleCombox, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 440, 170, 40));

        jLabel15.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("BIrthdate:");
        addUserMainPanel.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, 170, 40));

        jLabel28.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Gender:");
        addUserMainPanel.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 220, 170, 40));

        jLabel29.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Civil Status:");
        addUserMainPanel.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 310, 170, 40));

        jLabel30.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Municipality:");
        addUserMainPanel.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 310, 170, 40));

        jLabel31.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Barangay:");
        addUserMainPanel.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 400, 170, 40));

        jLabel32.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Role:");
        addUserMainPanel.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 400, 170, 40));

        AddUserSaveButton.setBackground(new java.awt.Color(255, 103, 103));
        AddUserSaveButton.setFont(new java.awt.Font("Poppins SemiBold", 0, 14)); // NOI18N
        AddUserSaveButton.setForeground(new java.awt.Color(255, 255, 255));
        AddUserSaveButton.setText("Save");
        AddUserSaveButton.setBorder(null);
        AddUserSaveButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddUserSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddUserSaveButtonActionPerformed(evt);
            }
        });
        addUserMainPanel.add(AddUserSaveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 640, 170, 40));

        addUserBackButton.setFont(new java.awt.Font("Poppins SemiBold", 0, 14)); // NOI18N
        addUserBackButton.setText("Back");
        addUserBackButton.setBorder(null);
        addUserBackButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addUserBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserBackButtonActionPerformed(evt);
            }
        });
        addUserMainPanel.add(addUserBackButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 640, 170, 40));

        javax.swing.GroupLayout addUserDialogLayout = new javax.swing.GroupLayout(addUserDialog.getContentPane());
        addUserDialog.getContentPane().setLayout(addUserDialogLayout);
        addUserDialogLayout.setHorizontalGroup(
            addUserDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addUserMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
        );
        addUserDialogLayout.setVerticalGroup(
            addUserDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addUserMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Voting System - Admin Dashboard");
        setResizable(false);

        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        leftSide.setBackground(new java.awt.Color(28, 33, 32));
        leftSide.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logoutButton.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        logoutButton.setForeground(new java.awt.Color(255, 255, 255));
        logoutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-logout-20.png"))); // NOI18N
        logoutButton.setText("Logout");
        logoutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutButtonMouseClicked(evt);
            }
        });
        leftSide.add(logoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 710, 170, 30));

        dashboardAccountName.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        dashboardAccountName.setForeground(new java.awt.Color(255, 255, 255));
        dashboardAccountName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-account-20.png"))); // NOI18N
        leftSide.add(dashboardAccountName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 680, 170, 30));

        listMenu1.setOpaque(false);
        listMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listMenu1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                listMenu1MouseEntered(evt);
            }
        });
        leftSide.add(listMenu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 210, 250));

        dashboardMainIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/votinglogo2-medium.png"))); // NOI18N
        leftSide.add(dashboardMainIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        votingTextIcon.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        votingTextIcon.setForeground(new java.awt.Color(255, 255, 255));
        votingTextIcon.setText("VOTING");
        leftSide.add(votingTextIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 100, 20));

        systemTextIcon.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        systemTextIcon.setForeground(new java.awt.Color(255, 255, 255));
        systemTextIcon.setText("SYSTEM");
        leftSide.add(systemTextIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 100, 30));

        mainPanel.add(leftSide, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 210, 760));

        rightSide.setBackground(new java.awt.Color(255, 204, 255));
        rightSide.setLayout(new java.awt.CardLayout());

        dashboardPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        latestList.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jScrollPane2.setViewportView(latestList);

        dashboardPanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 480, 860, 250));

        registrationNotificationTitle.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        registrationNotificationTitle.setText("Meet Our Team");
        dashboardPanel.add(registrationNotificationTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 320, 38));

        dashboardPageTitle.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        dashboardPageTitle.setText("Dashboard");
        dashboardPanel.add(dashboardPageTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 240, 60));

        breadcrumbsDashboard.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        dashboardPanel.add(breadcrumbsDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 200, 20));

        registrationNotificationTitle1.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        registrationNotificationTitle1.setText(" Registration Notifications");
        dashboardPanel.add(registrationNotificationTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 440, 320, 38));

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reymark.png"))); // NOI18N
        dashboardPanel.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 180, 180));

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/chylzea.png"))); // NOI18N
        dashboardPanel.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 170, 180, 180));

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/kayzell.png"))); // NOI18N
        dashboardPanel.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 170, 180, 180));

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cyrill2.png"))); // NOI18N
        dashboardPanel.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 170, 180, 180));

        jLabel33.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel33.setText("Calzado, Chylzea");
        dashboardPanel.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 370, 140, 30));

        jLabel38.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel38.setText("Legion, Kayzell");
        dashboardPanel.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 370, 120, 30));

        jLabel39.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel39.setText("Calzado, Cyrill");
        dashboardPanel.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 370, 120, 30));

        jLabel40.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel40.setText("Bautista, Reymark");
        dashboardPanel.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 370, 150, 30));

        jLabel41.setText("Programmer / Designer / Database");
        dashboardPanel.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 200, -1));

        jLabel42.setText("Designer / Docs");
        dashboardPanel.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 400, -1, -1));

        jLabel43.setText("Designer / Docs");
        dashboardPanel.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 400, -1, -1));

        jLabel44.setText("Designer / Docs");
        dashboardPanel.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 400, -1, -1));

        rightSide.add(dashboardPanel, "card1");

        usersPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        usersPageTitle.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        usersPageTitle.setText("Users");
        usersPanel.add(usersPageTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 280, 60));

        breadcrumbsUsers.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        usersPanel.add(breadcrumbsUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 200, 20));

        jLayeredPane1.setLayout(new java.awt.GridLayout(1, 0, 15, 0));

        gradientCard1.setColor1(new java.awt.Color(255, 87, 87));
        gradientCard1.setColor2(new java.awt.Color(176, 50, 57));
        jLayeredPane1.add(gradientCard1);

        gradientCard2.setColor1(new java.awt.Color(132, 210, 246));
        gradientCard2.setColor2(new java.awt.Color(19, 60, 85));
        jLayeredPane1.add(gradientCard2);

        gradientCard4.setColor1(new java.awt.Color(222, 160, 255));
        gradientCard4.setColor2(new java.awt.Color(119, 72, 243));
        jLayeredPane1.add(gradientCard4);

        usersPanel.add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 880, 170));

        usersPanelCardLayout.setLayout(new java.awt.CardLayout());

        viewUsersPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        usersAddUserButton.setBackground(new java.awt.Color(255, 103, 103));
        usersAddUserButton.setFont(new java.awt.Font("Poppins SemiBold", 0, 14)); // NOI18N
        usersAddUserButton.setForeground(new java.awt.Color(255, 255, 255));
        usersAddUserButton.setText("ADD USER");
        usersAddUserButton.setBorder(null);
        usersAddUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usersAddUserButtonActionPerformed(evt);
            }
        });
        viewUsersPanel.add(usersAddUserButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 20, 110, 40));

        jScrollPane1.setBorder(null);

        usersTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        usersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "First Name", "Last Name", "Birthday", "Gender", "Status", "Address", "Email", "Role", "Actions"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        usersTable.setFillsViewportHeight(true);
        usersTable.setFocusable(false);
        usersTable.setGridColor(new java.awt.Color(28, 33, 32));
        usersTable.setName(""); // NOI18N
        usersTable.setRowHeight(40);
        usersTable.setSelectionBackground(new java.awt.Color(255, 87, 87));
        usersTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        usersTable.setShowGrid(false);
        jScrollPane1.setViewportView(usersTable);

        viewUsersPanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 880, 360));

        usersPanelCardLayout.add(viewUsersPanel, "card2");

        usersPanel.add(usersPanelCardLayout, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 880, 440));

        rightSide.add(usersPanel, "card5");

        candidatesPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        breadcrumbsCandidates.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        candidatesPanel.add(breadcrumbsCandidates, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 200, 20));

        candidataesPageTitle.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        candidataesPageTitle.setText("Candidates");
        candidatesPanel.add(candidataesPageTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 250, 60));

        candidatesTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        candidatesTable.setForeground(new java.awt.Color(28, 33, 32));
        candidatesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Candidate Name", "Title", "Actions"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        candidatesTable.setFillsViewportHeight(true);
        candidatesTable.setFocusable(false);
        candidatesTable.setGridColor(new java.awt.Color(28, 33, 32));
        candidatesTable.setRowHeight(40);
        candidatesTable.setSelectionBackground(new java.awt.Color(255, 87, 87));
        candidatesTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        pollsCandidateScrollPanel.setViewportView(candidatesTable);

        candidatesPanel.add(pollsCandidateScrollPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 150, 490, 580));

        addCandidatePanel.setBackground(new java.awt.Color(28, 33, 32));
        addCandidatePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        addCandidateHereLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        addCandidateHereLabel.setForeground(new java.awt.Color(255, 255, 255));
        addCandidateHereLabel.setText("Add Candidate Here");
        addCandidatePanel.add(addCandidateHereLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 170, 20));

        addCandidateFirstnameField.setCaretColor(new java.awt.Color(255, 255, 255));
        addCandidateFirstnameField.setPrefixIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-name-20.png"))); // NOI18N
        addCandidateFirstnameField.setSelectedTextColor(new java.awt.Color(28, 33, 32));
        addCandidatePanel.add(addCandidateFirstnameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 240, -1));

        addCandidateLastnameField.setCaretColor(new java.awt.Color(255, 255, 255));
        addCandidateLastnameField.setPrefixIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-name-20.png"))); // NOI18N
        addCandidateLastnameField.setSelectedTextColor(new java.awt.Color(28, 33, 32));
        addCandidatePanel.add(addCandidateLastnameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 240, -1));

        addCandidateTitleCombox.setBackground(new java.awt.Color(71, 74, 73));
        addCandidateTitleCombox.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        addCandidateTitleCombox.setForeground(new java.awt.Color(255, 255, 255));
        addCandidateTitleCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default" }));
        addCandidateTitleCombox.setBorder(null);
        addCandidatePanel.add(addCandidateTitleCombox, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 240, 40));

        addCandidateButton.setBackground(new java.awt.Color(255, 87, 87));
        addCandidateButton.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        addCandidateButton.setForeground(new java.awt.Color(255, 255, 255));
        addCandidateButton.setText("ADD");
        addCandidateButton.setBorder(null);
        addCandidateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addCandidateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCandidateButtonActionPerformed(evt);
            }
        });
        addCandidatePanel.add(addCandidateButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 110, 40));

        clearCandidateButton.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        clearCandidateButton.setText("CLEAR");
        clearCandidateButton.setBorder(null);
        clearCandidateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clearCandidateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearCandidateButtonActionPerformed(evt);
            }
        });
        addCandidatePanel.add(clearCandidateButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 280, 120, 40));

        candidatesPanel.add(addCandidatePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 340, 350));

        jPanel3.setBackground(new java.awt.Color(28, 33, 32));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/giphy res.gif"))); // NOI18N
        jLabel5.setText("jLabel5");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 280, 160));

        candidatesPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 530, 340, 200));

        rightSide.add(candidatesPanel, "card2");

        votingPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        piechartCombox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                piechartComboxActionPerformed(evt);
            }
        });
        votingPanel.add(piechartCombox, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 400, 120, -1));

        pollsLabelForChart.setText("Polls:");
        votingPanel.add(pollsLabelForChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 400, 40, 20));

        breadcrumbsVotingResults.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        votingPanel.add(breadcrumbsVotingResults, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 270, 20));

        votingResultsPageTitle.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        votingResultsPageTitle.setText("Voting Result");
        votingPanel.add(votingResultsPageTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 16, 300, -1));
        votingPanel.add(pieChart1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 410, 290));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        votingResultTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Candidate Name", "President", "Votes Garnered"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(votingResultTable);

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -1, 390, 560));

        votingPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 170, 390, 560));

        testlabel.setText("test");
        votingPanel.add(testlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 190, -1, -1));

        jLabel1.setText("jLabel1");
        votingPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 150, -1, -1));

        rightSide.add(votingPanel, "card3");

        settingsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        settingsPageTitle.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        settingsPageTitle.setText("Settings");
        settingsPanel.add(settingsPageTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 16, 280, -1));

        breadcrumbsSettings.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        settingsPanel.add(breadcrumbsSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 200, 20));

        settingsCardLayout.setLayout(new java.awt.CardLayout());

        showDetailsPanel.setBackground(new java.awt.Color(28, 33, 32));
        showDetailsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        settingsFirstNameLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsFirstNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsFirstNameLabel.setText("First Name:");
        showDetailsPanel.add(settingsFirstNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 200, 30));

        settingsFirstNameValueLabel.setBackground(new java.awt.Color(71, 74, 73));
        settingsFirstNameValueLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsFirstNameValueLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsFirstNameValueLabel.setOpaque(true);
        showDetailsPanel.add(settingsFirstNameValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 200, 30));

        settingsLastNameLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsLastNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsLastNameLabel.setText("Last Name:");
        showDetailsPanel.add(settingsLastNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 150, 200, 30));

        settingsLastNameValueLabel.setBackground(new java.awt.Color(71, 74, 73));
        settingsLastNameValueLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsLastNameValueLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsLastNameValueLabel.setOpaque(true);
        showDetailsPanel.add(settingsLastNameValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 180, 200, 30));

        settingsAdminIDLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsAdminIDLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsAdminIDLabel.setText("Admin ID");
        showDetailsPanel.add(settingsAdminIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 200, 30));

        settingsAdminValueLabel.setBackground(new java.awt.Color(71, 74, 73));
        settingsAdminValueLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsAdminValueLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsAdminValueLabel.setOpaque(true);
        showDetailsPanel.add(settingsAdminValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 200, 30));

        settingsBirthdateLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsBirthdateLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsBirthdateLabel.setText("Birthdate:");
        showDetailsPanel.add(settingsBirthdateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 200, 30));

        settingsBirthdateValueLabel.setBackground(new java.awt.Color(71, 74, 73));
        settingsBirthdateValueLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsBirthdateValueLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsBirthdateValueLabel.setOpaque(true);
        showDetailsPanel.add(settingsBirthdateValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 200, 30));

        settingsGenderLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsGenderLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsGenderLabel.setText("Gender:");
        showDetailsPanel.add(settingsGenderLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 200, 30));

        settingsGenderValueLabel.setBackground(new java.awt.Color(71, 74, 73));
        settingsGenderValueLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsGenderValueLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsGenderValueLabel.setOpaque(true);
        showDetailsPanel.add(settingsGenderValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 200, 30));

        settingsStatusLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsStatusLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsStatusLabel.setText("Civil Status:");
        showDetailsPanel.add(settingsStatusLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 290, 200, 30));

        settingsStatusValueLabel.setBackground(new java.awt.Color(71, 74, 73));
        settingsStatusValueLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsStatusValueLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsStatusValueLabel.setOpaque(true);
        showDetailsPanel.add(settingsStatusValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, 200, 30));

        settingsMunicipalityLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsMunicipalityLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsMunicipalityLabel.setText("Municipality:");
        showDetailsPanel.add(settingsMunicipalityLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 200, 30));

        settingsMunicipalityValueLabel.setBackground(new java.awt.Color(71, 74, 73));
        settingsMunicipalityValueLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsMunicipalityValueLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsMunicipalityValueLabel.setOpaque(true);
        showDetailsPanel.add(settingsMunicipalityValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 200, 30));

        settingsBarangayLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsBarangayLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsBarangayLabel.setText("Barangay:");
        showDetailsPanel.add(settingsBarangayLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 360, 200, 30));

        settingsBarangayValueLabel.setBackground(new java.awt.Color(71, 74, 73));
        settingsBarangayValueLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsBarangayValueLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsBarangayValueLabel.setOpaque(true);
        showDetailsPanel.add(settingsBarangayValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 390, 200, 30));

        settingsEmailLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsEmailLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsEmailLabel.setText("Email:");
        showDetailsPanel.add(settingsEmailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 200, 30));

        settingsEmailValueLabel.setBackground(new java.awt.Color(71, 74, 73));
        settingsEmailValueLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsEmailValueLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsEmailValueLabel.setOpaque(true);
        showDetailsPanel.add(settingsEmailValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 200, 30));

        settingsPasswordLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsPasswordLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsPasswordLabel.setText("Password:");
        showDetailsPanel.add(settingsPasswordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 430, 200, 30));

        settingsPasswordValueLabel.setBackground(new java.awt.Color(71, 74, 73));
        settingsPasswordValueLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsPasswordValueLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsPasswordValueLabel.setOpaque(true);
        showDetailsPanel.add(settingsPasswordValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 460, 200, 30));

        settingsUpdateDetailsButton.setBackground(new java.awt.Color(255, 87, 87));
        settingsUpdateDetailsButton.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        settingsUpdateDetailsButton.setForeground(new java.awt.Color(255, 255, 255));
        settingsUpdateDetailsButton.setText("Update Details");
        settingsUpdateDetailsButton.setBorder(null);
        settingsUpdateDetailsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        settingsUpdateDetailsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsUpdateDetailsButtonActionPerformed(evt);
            }
        });
        showDetailsPanel.add(settingsUpdateDetailsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 520, 420, 40));

        jLabel3.setFont(new java.awt.Font("Poppins", 3, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("*** VIEW MODE ***");
        showDetailsPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, -1, -1));

        jLabel27.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Admin Account Details");
        showDetailsPanel.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 160, 20));

        settingsCardLayout.add(showDetailsPanel, "card2");

        editDetailsPanel.setBackground(new java.awt.Color(28, 33, 32));
        editDetailsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        settingsAdminID1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsAdminID1.setForeground(new java.awt.Color(255, 255, 255));
        settingsAdminID1.setText("Admin ID");
        editDetailsPanel.add(settingsAdminID1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 200, 30));

        settingsFirstNameLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsFirstNameLabel1.setForeground(new java.awt.Color(255, 255, 255));
        settingsFirstNameLabel1.setText("First Name:");
        editDetailsPanel.add(settingsFirstNameLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 200, 30));

        settingsLastNameLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsLastNameLabel1.setForeground(new java.awt.Color(255, 255, 255));
        settingsLastNameLabel1.setText("Last Name:");
        editDetailsPanel.add(settingsLastNameLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 150, 200, 30));

        settingsBirthdateLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsBirthdateLabel1.setForeground(new java.awt.Color(255, 255, 255));
        settingsBirthdateLabel1.setText("Birthdate:");
        editDetailsPanel.add(settingsBirthdateLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 200, 30));

        settingsGenderLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsGenderLabel1.setForeground(new java.awt.Color(255, 255, 255));
        settingsGenderLabel1.setText("Gender:");
        editDetailsPanel.add(settingsGenderLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 200, 30));

        settingsStatusLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsStatusLabel1.setForeground(new java.awt.Color(255, 255, 255));
        settingsStatusLabel1.setText("Civil Status:");
        editDetailsPanel.add(settingsStatusLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 290, 200, 30));

        settingsMunicipalityLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsMunicipalityLabel1.setForeground(new java.awt.Color(255, 255, 255));
        settingsMunicipalityLabel1.setText("Municipality:");
        editDetailsPanel.add(settingsMunicipalityLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 200, 30));

        settingsBarangayLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsBarangayLabel1.setForeground(new java.awt.Color(255, 255, 255));
        settingsBarangayLabel1.setText("Barangay:");
        editDetailsPanel.add(settingsBarangayLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 360, 200, 30));

        settingsEmailLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsEmailLabel1.setForeground(new java.awt.Color(255, 255, 255));
        settingsEmailLabel1.setText("Email:");
        editDetailsPanel.add(settingsEmailLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 200, 30));

        settingsPasswordLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        settingsPasswordLabel1.setForeground(new java.awt.Color(255, 255, 255));
        settingsPasswordLabel1.setText("Password:");
        editDetailsPanel.add(settingsPasswordLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 430, 200, 30));

        settingsAdminIDTextField.setEditable(false);
        settingsAdminIDTextField.setBackground(new java.awt.Color(71, 74, 73));
        settingsAdminIDTextField.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        settingsAdminIDTextField.setForeground(new java.awt.Color(255, 255, 255));
        settingsAdminIDTextField.setBorder(null);
        settingsAdminIDTextField.setCaretColor(new java.awt.Color(255, 255, 255));
        settingsAdminIDTextField.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        settingsAdminIDTextField.setSelectionColor(new java.awt.Color(255, 255, 255));
        editDetailsPanel.add(settingsAdminIDTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 200, 30));

        settingsFirstNameTextField.setBackground(new java.awt.Color(71, 74, 73));
        settingsFirstNameTextField.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        settingsFirstNameTextField.setForeground(new java.awt.Color(255, 255, 255));
        settingsFirstNameTextField.setBorder(null);
        settingsFirstNameTextField.setCaretColor(new java.awt.Color(255, 255, 255));
        settingsFirstNameTextField.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        settingsFirstNameTextField.setSelectionColor(new java.awt.Color(255, 255, 255));
        editDetailsPanel.add(settingsFirstNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 200, 30));

        settingsLastNameTextField.setBackground(new java.awt.Color(71, 74, 73));
        settingsLastNameTextField.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        settingsLastNameTextField.setForeground(new java.awt.Color(255, 255, 255));
        settingsLastNameTextField.setBorder(null);
        settingsLastNameTextField.setCaretColor(new java.awt.Color(255, 255, 255));
        settingsLastNameTextField.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        settingsLastNameTextField.setSelectionColor(new java.awt.Color(255, 255, 255));
        editDetailsPanel.add(settingsLastNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 180, 200, 30));

        settingsEmailTextField.setBackground(new java.awt.Color(71, 74, 73));
        settingsEmailTextField.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        settingsEmailTextField.setForeground(new java.awt.Color(255, 255, 255));
        settingsEmailTextField.setBorder(null);
        settingsEmailTextField.setCaretColor(new java.awt.Color(255, 255, 255));
        settingsEmailTextField.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        settingsEmailTextField.setSelectionColor(new java.awt.Color(255, 255, 255));
        editDetailsPanel.add(settingsEmailTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 200, 30));

        settingsPasswordTextField.setBackground(new java.awt.Color(71, 74, 73));
        settingsPasswordTextField.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        settingsPasswordTextField.setForeground(new java.awt.Color(255, 255, 255));
        settingsPasswordTextField.setBorder(null);
        settingsPasswordTextField.setCaretColor(new java.awt.Color(255, 255, 255));
        settingsPasswordTextField.setSelectedTextColor(new java.awt.Color(51, 51, 51));
        settingsPasswordTextField.setSelectionColor(new java.awt.Color(255, 255, 255));
        editDetailsPanel.add(settingsPasswordTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 460, 200, 30));
        editDetailsPanel.add(settingsBirthdateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 200, 30));

        settingsGenderCombox.setBackground(new java.awt.Color(71, 74, 73));
        settingsGenderCombox.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        settingsGenderCombox.setForeground(new java.awt.Color(255, 255, 255));
        settingsGenderCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Male", "Female", "Non-Binary" }));
        settingsGenderCombox.setBorder(null);
        editDetailsPanel.add(settingsGenderCombox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 200, 30));

        settingsStatusCombox.setBackground(new java.awt.Color(71, 74, 73));
        settingsStatusCombox.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        settingsStatusCombox.setForeground(new java.awt.Color(255, 255, 255));
        settingsStatusCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Single", "Married", "Divorced", "Separated", "Widowed" }));
        settingsStatusCombox.setBorder(null);
        editDetailsPanel.add(settingsStatusCombox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, 200, 30));

        settingsMunicipalityCombox.setBackground(new java.awt.Color(71, 74, 73));
        settingsMunicipalityCombox.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        settingsMunicipalityCombox.setForeground(new java.awt.Color(255, 255, 255));
        settingsMunicipalityCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Agdangan", "Padre Burgos", "Unisan" }));
        settingsMunicipalityCombox.setBorder(null);
        settingsMunicipalityCombox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                settingsMunicipalityComboxItemStateChanged(evt);
            }
        });
        editDetailsPanel.add(settingsMunicipalityCombox, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 200, 30));

        settingsBarangayCombox.setBackground(new java.awt.Color(71, 74, 73));
        settingsBarangayCombox.setFont(new java.awt.Font("Poppins", 0, 13)); // NOI18N
        settingsBarangayCombox.setForeground(new java.awt.Color(255, 255, 255));
        settingsBarangayCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default" }));
        settingsBarangayCombox.setBorder(null);
        editDetailsPanel.add(settingsBarangayCombox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 390, 200, 30));

        settingsSaveChangesButton.setBackground(new java.awt.Color(255, 87, 87));
        settingsSaveChangesButton.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        settingsSaveChangesButton.setForeground(new java.awt.Color(255, 255, 255));
        settingsSaveChangesButton.setText("Save Changes");
        settingsSaveChangesButton.setBorder(null);
        settingsSaveChangesButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        settingsSaveChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsSaveChangesButtonActionPerformed(evt);
            }
        });
        editDetailsPanel.add(settingsSaveChangesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 520, 200, 40));

        settingsCancelChangesButton.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        settingsCancelChangesButton.setText("Back");
        settingsCancelChangesButton.setBorder(null);
        settingsCancelChangesButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        settingsCancelChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsCancelChangesButtonActionPerformed(evt);
            }
        });
        editDetailsPanel.add(settingsCancelChangesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 520, 200, 40));

        jLabel2.setFont(new java.awt.Font("Poppins", 3, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("*** EDIT MODE ***");
        editDetailsPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(316, 30, 110, -1));

        jLabel17.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Admin Account Details");
        editDetailsPanel.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 160, 20));

        settingsCardLayout.add(editDetailsPanel, "card3");

        settingsPanel.add(settingsCardLayout, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 460, 610));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel45.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel45.setText("O:ther Administrators");
        jPanel1.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 170, 20));

        adminsTable.setAutoCreateRowSorter(true);
        adminsTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        adminsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Admin ID", "Name", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        adminsTable.setFocusable(false);
        adminsTable.setSelectionBackground(new java.awt.Color(255, 87, 87));
        adminsTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane4.setViewportView(adminsTable);
        if (adminsTable.getColumnModel().getColumnCount() > 0) {
            adminsTable.getColumnModel().getColumn(0).setMinWidth(60);
            adminsTable.getColumnModel().getColumn(1).setMinWidth(100);
        }

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 360, 510));

        settingsPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 130, 400, 610));

        rightSide.add(settingsPanel, "card4");

        mainPanel.add(rightSide, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 920, 760));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void piechartComboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_piechartComboxActionPerformed
        if(piechartCombox.getSelectedIndex() >= 0){
            candidatesAccessObject.showData(piechartCombox.getSelectedItem().toString(), pieChart1);
        }
    }//GEN-LAST:event_piechartComboxActionPerformed

    private void listMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listMenu1MouseClicked
        // TODO add your handling code here:
        switch (listMenu1.getSelectedIndex()) {
            case 0 -> {
                rightSide.removeAll();
                rightSide.add(dashboardPanel);
                rightSide.repaint();
                rightSide.revalidate();
            }
            case 1 -> {
                rightSide.removeAll();
                rightSide.add(usersPanel);
                rightSide.repaint();
                rightSide.revalidate();
            }
            case 2 -> {
                rightSide.removeAll();
                rightSide.add(candidatesPanel);
                rightSide.repaint();
                rightSide.revalidate();
            }
            case 3 -> {
                rightSide.removeAll();
                rightSide.add(votingPanel);
                rightSide.repaint();
                rightSide.revalidate();
            }
            case 4 -> {
                rightSide.removeAll();
                rightSide.add(settingsPanel);
                rightSide.repaint();
                rightSide.revalidate();
            }
            default -> {
            }
        }
    }//GEN-LAST:event_listMenu1MouseClicked

    private void listMenu1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listMenu1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_listMenu1MouseEntered

    private void updateCDButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCDButtonActionPerformed
        CandidateAccessObject updateAccessObject = new CandidateAccessObject();
        CandidateTransferObject candidateData = new CandidateTransferObject();
        
        if (updateCDFirstNameField.getText().isEmpty() ||
            updateCDLastNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(updateCandidatesDialog, "Update didn't work. Empty field detected.", "Error!", JOptionPane.ERROR_MESSAGE);
        } else {
            // Get current values from the table for comparison
            String currentCandidateName = candidateModel.getValueAt(candidatesTable.getSelectedRow(), 1).toString();
            String currentCandidateTitle = candidateModel.getValueAt(candidatesTable.getSelectedRow(), 2).toString();
            
            if ((updateCDLastNameField.getText() + ", " + updateCDFirstNameField.getText()).equals(currentCandidateName) &&
                    updateCDTitleComBox.getSelectedItem().equals(currentCandidateTitle)){
                    JOptionPane.showMessageDialog(updateCandidatesDialog, "No changes detected.", "Notice.", JOptionPane.INFORMATION_MESSAGE);
            } else {
                candidateData.setCandidateId(Integer.parseInt(updateCDIDField.getText()));
                candidateData.setCandidateFirstName(updateCDFirstNameField.getText());
                candidateData.setCandidateLastName(updateCDLastNameField.getText());
                candidateData.setPollId(updateCDTitleComBox.getSelectedIndex()+1);
                    
                if (updateAccessObject.updateCandidate(candidateData)){
                    
                    // If all conditons are true, proceed on updating the table
                    String updatedCandidateName = updateCDLastNameField.getText() + ", " + updateCDFirstNameField.getText();
                
                    DefaultTableModel updateModelCandidateModel = (DefaultTableModel) candidatesTable.getModel();
                    updateModelCandidateModel.setValueAt(updatedCandidateName, candidatesTable.getSelectedRow(), 1);
                    updateModelCandidateModel.setValueAt(updateCDTitleComBox.getSelectedItem().toString(), candidatesTable.getSelectedRow(), 2);
                    
                    JOptionPane.showMessageDialog(updateCandidatesDialog, "Updated Successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(updateCandidatesDialog, "Update didn't work.", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
    }//GEN-LAST:event_updateCDButtonActionPerformed

    private void backCDButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backCDButtonActionPerformed
        updateCandidatesDialog.setVisible(false);
    }//GEN-LAST:event_backCDButtonActionPerformed

    private void settingsSaveChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsSaveChangesButtonActionPerformed
       UserAccessObject updateUserAccessObject = new UserAccessObject();
        UserTransferObject updatedUserData = new UserTransferObject();
        
        // Check for empty fields
        if (settingsFirstNameTextField.getText().isEmpty() ||
            settingsLastNameTextField.getText().isEmpty() ||
            settingsEmailTextField.getText().isEmpty() ||
            settingsBirthdateChooser.getDate() == null ||
            settingsGenderCombox.getSelectedItem().toString().equals("Default") ||
            settingsStatusCombox.getSelectedItem().toString().equals("Default") ||
            settingsMunicipalityCombox.getSelectedItem().toString().equals("Default") ||
            settingsBarangayCombox.getSelectedItem().toString().equals("Default") ||
            settingsPasswordTextField.getText().isEmpty()) {
            
            JOptionPane.showMessageDialog(mainPanel, "Can't save empty fields.", "Error!", JOptionPane.ERROR_MESSAGE);
        } else {

            String currentUserEmail = user.getEmail();
            UserTransferObject updateUserTransferObject = updateUserAccessObject.getAllUserData(currentUserEmail);
            
            if (settingsFirstNameTextField.getText().equals(updateUserTransferObject.getFirstname()) &&
                settingsLastNameTextField.getText().equals( updateUserTransferObject.getLastname()) &&
                new java.sql.Date(settingsBirthdateChooser.getDate().getTime()).equals(updateUserTransferObject.getBirthDate()) &&
                settingsGenderCombox.getSelectedItem().toString().equals(updateUserTransferObject.getGender()) &&
                settingsStatusCombox.getSelectedItem().toString().equals(updateUserTransferObject.getStatus()) &&
                settingsMunicipalityCombox.getSelectedItem().toString().equals(updateUserTransferObject.getMunicipality()) &&
                settingsBarangayCombox.getSelectedItem().toString().equals(updateUserTransferObject.getBarangay()) &&
                settingsEmailTextField.getText().equals(updateUserTransferObject.getEmail()) &&
                settingsPasswordTextField.getText().equals(updateUserTransferObject.getPassword())){
                
                JOptionPane.showMessageDialog(mainPanel, "No changes have been made.", "Notice!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                java.util.Date birthdate = settingsBirthdateChooser.getDate();
                updatedUserData.setFirstname(settingsFirstNameTextField.getText());
                updatedUserData.setLastname(settingsLastNameTextField.getText());
                updatedUserData.setEmail(settingsEmailTextField.getText());
                updatedUserData.setBirthDate(new java.sql.Date(birthdate.getTime()));
                updatedUserData.setGender(settingsGenderCombox.getSelectedItem().toString());
                updatedUserData.setStatus(settingsStatusCombox.getSelectedItem().toString());
                updatedUserData.setMunicipality(settingsMunicipalityCombox.getSelectedItem().toString());
                updatedUserData.setBarangay(settingsBarangayCombox.getSelectedItem().toString());
                updatedUserData.setPassword(settingsPasswordTextField.getText());
                
                settingsFirstNameValueLabel.setText(updatedUserData.getFirstname());
                settingsLastNameValueLabel.setText(updatedUserData.getLastname());
                settingsBirthdateValueLabel.setText(String.valueOf(updatedUserData.getBirthDate()));
                settingsGenderValueLabel.setText(updatedUserData.getGender());
                settingsStatusValueLabel.setText(updatedUserData.getStatus());
                settingsMunicipalityValueLabel.setText(updatedUserData.getMunicipality());
                settingsBarangayValueLabel.setText(updatedUserData.getBarangay());
                settingsEmailTextField.setText(updatedUserData.getEmail());
                settingsPasswordTextField.setText(updatedUserData.getPassword());
                
                if (updateUserAccessObject.updateAdmin(updatedUserData)){
                   JOptionPane.showMessageDialog(mainPanel, "Updated Successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Update didn't work.", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_settingsSaveChangesButtonActionPerformed

    private void settingsCancelChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsCancelChangesButtonActionPerformed
        // TODO add your handling code here:
        settingsCardLayout.removeAll();
        settingsCardLayout.add(showDetailsPanel);
        settingsCardLayout.repaint();
        settingsCardLayout.revalidate();
    }//GEN-LAST:event_settingsCancelChangesButtonActionPerformed

    private void settingsUpdateDetailsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsUpdateDetailsButtonActionPerformed
        // Set values in all input elements
        settingsAdminIDTextField.setText(String.valueOf(user.getUserID()));
        settingsFirstNameTextField.setText(user.getFirstname());
        settingsLastNameTextField.setText(user.getLastname());
        settingsBirthdateChooser.setDate(user.getBirthDate());
        settingsGenderCombox.setSelectedItem(user.getGender());
        settingsStatusCombox.setSelectedItem(user.getStatus());
        settingsMunicipalityCombox.setSelectedItem(user.getMunicipality());
        
        // Populate barangays in JComboBox
        settingsBarangayCombox.removeAllItems();
        LocationHandler locationHandler = new LocationHandler();
        if (settingsMunicipalityCombox.getSelectedItem().equals("Default")) {
            settingsBarangayCombox.addItem("Default");
        } else if(settingsMunicipalityCombox.getSelectedItem().equals("Agdangan")){
            for (String barangayLocation : locationHandler.getAgdanganBarangay()) {
                settingsBarangayCombox.addItem(barangayLocation);
            }
        } else if (settingsMunicipalityCombox.getSelectedItem().equals("Unisan")) {
            for (String barangayLocation : locationHandler.getUnisanBarangay()) {
                settingsBarangayCombox.addItem(barangayLocation);
            }
        } else if (settingsMunicipalityCombox.getSelectedItem().equals("Padre Burgos")) {
            for (String barangayLocation : locationHandler.getPadreBurgosBarangay()) {
                settingsBarangayCombox.addItem(barangayLocation);
            }
        }
        
        settingsBarangayCombox.setSelectedItem(user.getBarangay());
        settingsEmailTextField.setText(user.getEmail());
        settingsPasswordTextField.setText(user.getPassword());
        
        settingsCardLayout.removeAll();
        settingsCardLayout.add(editDetailsPanel);
        settingsCardLayout.repaint();
        settingsCardLayout.revalidate();
    }//GEN-LAST:event_settingsUpdateDetailsButtonActionPerformed

    private void updateUserDialogBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateUserDialogBackButtonActionPerformed
        updateUserDialog.setVisible(false);
    }//GEN-LAST:event_updateUserDialogBackButtonActionPerformed

    private void updateUserDialogSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateUserDialogSaveButtonActionPerformed
        UserAccessObject updateUserAccessObject = new UserAccessObject();
        UserTransferObject updatedUserData = new UserTransferObject();
        
        // Check for empty fields
        if (voterUpdateFirstName.getText().isEmpty() ||
            voterUpdateLastName.getText().isEmpty() ||
            voterUpdateEmail.getText().isEmpty() ||
            voterUpdateBirthday.getDate() == null ||
            voterUpdateGender.getSelectedItem().toString().equals("Default") ||
            voterUpdateStatus.getSelectedItem().toString().equals("Default") ||
            voterUpdateMunicipality.getSelectedItem().toString().equals("Default") ||
            voterUpdateBarangay.getSelectedItem().toString().equals("Default") ||
            voterUpdateRole.getSelectedItem().toString().equals("Default")) {
            
            JOptionPane.showMessageDialog(updateUserDialog, "Can't save empty fields.", "Error!", JOptionPane.ERROR_MESSAGE);
        } else {

            String currentUserEmail = usersTable.getValueAt(usersTable.getSelectedRow(), 7).toString();
            UserTransferObject updateUserTransferObject = updateUserAccessObject.getAllUserData(currentUserEmail);
            
            if (voterUpdateFirstName.getText().equals(updateUserTransferObject.getFirstname()) &&
                voterUpdateLastName.getText().equals( updateUserTransferObject.getLastname()) &&
                new java.sql.Date(voterUpdateBirthday.getDate().getTime()).equals(updateUserTransferObject.getBirthDate()) &&
                voterUpdateGender.getSelectedItem().toString().equals(updateUserTransferObject.getGender()) &&
                voterUpdateStatus.getSelectedItem().toString().equals(updateUserTransferObject.getStatus()) &&
                voterUpdateMunicipality.getSelectedItem().toString().equals(updateUserTransferObject.getMunicipality()) &&
                voterUpdateBarangay.getSelectedItem().toString().equals(updateUserTransferObject.getBarangay()) &&
                voterUpdateEmail.getText().equals(updateUserTransferObject.getEmail()) &&
                voterUpdateRole.getSelectedItem().toString().equals(updateUserTransferObject.getRole())){
                
                JOptionPane.showMessageDialog(updateUserDialog, "No changes have been made.", "Notice!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                
                java.util.Date birthdate = voterUpdateBirthday.getDate();
                updatedUserData.setFirstname(voterUpdateFirstName.getText());
                updatedUserData.setLastname(voterUpdateLastName.getText());
                updatedUserData.setEmail(voterUpdateEmail.getText());
                updatedUserData.setBirthDate(new java.sql.Date(birthdate.getTime()));
                updatedUserData.setGender(voterUpdateGender.getSelectedItem().toString());
                updatedUserData.setStatus(voterUpdateStatus.getSelectedItem().toString());
                updatedUserData.setMunicipality(voterUpdateMunicipality.getSelectedItem().toString());
                updatedUserData.setBarangay(voterUpdateBarangay.getSelectedItem().toString());
                updatedUserData.setRole(voterUpdateRole.getSelectedItem().toString());
                
                if (updateUserAccessObject.updateUser(updatedUserData)){
                    // If all conditons are true, proceed on updating the table
                
                    DefaultTableModel updateUserTableModel = (DefaultTableModel) usersTable.getModel();
                    updateUserTableModel.setValueAt(voterUpdateFirstName.getText(), usersTable.getSelectedRow(), 0);
                    updateUserTableModel.setValueAt(voterUpdateLastName.getText(), usersTable.getSelectedRow(), 1);
                    updateUserTableModel.setValueAt(new java.sql.Date(voterUpdateBirthday.getDate().getTime()), usersTable.getSelectedRow(), 2);
                    updateUserTableModel.setValueAt(voterUpdateGender.getSelectedItem().toString(), usersTable.getSelectedRow(), 3);
                    updateUserTableModel.setValueAt(voterUpdateStatus.getSelectedItem().toString(), usersTable.getSelectedRow(), 4);
                    updateUserTableModel.setValueAt(voterUpdateMunicipality.getSelectedItem().toString(), usersTable.getSelectedRow(), 5);
                    updateUserTableModel.setValueAt(voterUpdateBarangay.getSelectedItem().toString(), usersTable.getSelectedRow(), 6);
                    updateUserTableModel.setValueAt(voterUpdateEmail.getText(), usersTable.getSelectedRow(), 7);
                    updateUserTableModel.setValueAt(voterUpdateRole.getSelectedItem().toString(), usersTable.getSelectedRow(), 8);
                    
                    JOptionPane.showMessageDialog(updateUserDialog, "Updated Successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(updateUserDialog, "Update didn't work.", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_updateUserDialogSaveButtonActionPerformed

    private void addCandidateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCandidateButtonActionPerformed
        CandidateAccessObject addCandidateAccessObject = new CandidateAccessObject();
        
        if(addCandidateFirstnameField.getText().isEmpty() || 
           addCandidateLastnameField.getText().isEmpty() ||
           String.valueOf(addCandidateTitleCombox.getSelectedItem()).equals("Default") ){
            
            JOptionPane.showMessageDialog(mainPanel, "Can't process empty fields.", "Error!", JOptionPane.ERROR_MESSAGE);
        } else {
            String newCandidateFirstname = addCandidateFirstnameField.getText();
            String newCandidateLastname = addCandidateLastnameField.getText();
            String newCandidateTitle = addCandidateTitleCombox.getSelectedItem().toString();
            int newCandidateTitleInt = addCandidateAccessObject.getPollId(newCandidateTitle);
            
            if (addCandidateAccessObject.registerCandidate(newCandidateTitleInt, newCandidateFirstname, newCandidateLastname)){
                
                List<CandidateTransferObject> freshCandidates = addCandidateAccessObject.getAllCandidates();
                
                // Set the row count to 0
                candidateModel.setRowCount(0);
                
                for(CandidateTransferObject candidate : freshCandidates){
                    candidateModel.addRow(new Object[]{candidate.getCandidateId(), candidate.getCandidateLastName() + ", "+ candidate.getCandidateFirstName(), candidate.getCandidateTitle()});
                }
                
                JOptionPane.showMessageDialog(mainPanel, "Candidate Inserted Successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Insertion failed.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        
    }//GEN-LAST:event_addCandidateButtonActionPerformed

    private void clearCandidateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearCandidateButtonActionPerformed
        addCandidateFirstnameField.setText("");
        addCandidateLastnameField.setText("");
        addCandidateTitleCombox.setSelectedItem("Default");
    }//GEN-LAST:event_clearCandidateButtonActionPerformed

    private void usersAddUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usersAddUserButtonActionPerformed
        // Opening the Add User dialog
        addUserDialog.setLocationRelativeTo(mainPanel);
        addUserDialog.setVisible(true);
        
        addUserFirstnameField.setHint("First Name");
        addUserLastnameField.setHint("Last Name");
        addUserEmailField.setHint("Email");
        addUserPasswordField.setHint("Passwrod");
    }//GEN-LAST:event_usersAddUserButtonActionPerformed

    private void AddUserSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddUserSaveButtonActionPerformed
        
        if(validateForm()){
            UserAccessObject addUserAccessObject = new UserAccessObject();
            UserTransferObject addUserData = new UserTransferObject();
            
            java.util.Date birthDate = addUserBirthdateChooser.getDate();

            addUserData.setFirstname(addUserFirstnameField.getText());
            addUserData.setLastname(addUserLastnameField.getText());
            addUserData.setBirthDate(new java.sql.Date(birthDate.getTime()));
            addUserData.setGender(addUserGenderCombox.getSelectedItem().toString());
            addUserData.setStatus(addUserStatusCombox.getSelectedItem().toString());
            addUserData.setMunicipality(addUserMunicipalityCombox.getSelectedItem().toString());
            addUserData.setBarangay(addUserBarangayCombox.getSelectedItem().toString());
            addUserData.setEmail(addUserEmailField.getText());
            addUserData.setPassword(addUserPasswordField.getText());
            addUserData.setRole(addUserRoleCombox.getSelectedItem().toString());
            
            if(addUserAccessObject.registerUser(addUserData)){
                
                List<UserTransferObject> freshUsers = addUserAccessObject.getAllUsers();
                
                // Set the row count to 0
                allUserModel.setRowCount(0);
                
                for(UserTransferObject eachUser : freshUsers){
                    allUserModel.addRow(new Object[]{eachUser.getFirstname(), eachUser.getLastname(), eachUser.getBirthDate(), eachUser.getGender(), eachUser.getStatus(), eachUser.getMunicipality(), eachUser.getBarangay(), eachUser.getEmail(), eachUser.getRole()});
                }
                gradientCard1.setData(new ModelCard(new ImageIcon(getClass().getResource("/images/2.png")), "Record Count", String.valueOf(userAccessObj.recordCountForUser()), "Overall Total of Users"));
                if(addUserData.getRole().equals("admin")){
                    gradientCard2.setData(new ModelCard(new ImageIcon(getClass().getResource("/images/admin-20.png")), "Registered Voters", String.valueOf(userAccessObj.recordCountForUser("role", "voter")), "Overall Total of Voters"));
                }
                JOptionPane.showMessageDialog(addUserDialog, "You have been registered successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_AddUserSaveButtonActionPerformed

    private void addUserMunicipalityComboxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_addUserMunicipalityComboxItemStateChanged
        // TODO add your handling code here:
        addUserBarangayCombox.removeAllItems();
        LocationHandler locationHandler = new LocationHandler();
        if (addUserMunicipalityCombox.getSelectedItem().equals("Default")) {
            addUserBarangayCombox.addItem("Default");
        } else if(addUserMunicipalityCombox.getSelectedItem().equals("Agdangan")){
            for (String barangayLocation : locationHandler.getAgdanganBarangay()) {
                addUserBarangayCombox.addItem(barangayLocation);
            }
        } else if (addUserMunicipalityCombox.getSelectedItem().equals("Unisan")) {
            for (String barangaLocation : locationHandler.getUnisanBarangay()) {
                addUserBarangayCombox.addItem(barangaLocation);
            }
        } else if (addUserMunicipalityCombox.getSelectedItem().equals("Padre Burgos")) {
            for (String barangaLocation : locationHandler.getPadreBurgosBarangay()) {
                addUserBarangayCombox.addItem(barangaLocation);
            }
        }
    }//GEN-LAST:event_addUserMunicipalityComboxItemStateChanged

    private void addUserBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserBackButtonActionPerformed
        addUserDialog.setVisible(false);
    }//GEN-LAST:event_addUserBackButtonActionPerformed

    private void voterUpdateMunicipalityItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_voterUpdateMunicipalityItemStateChanged
              
        voterUpdateBarangay.removeAllItems();
        LocationHandler locationHandler = new LocationHandler();
        if (voterUpdateMunicipality.getSelectedItem().equals("Default")) {
            voterUpdateBarangay.addItem("Default");
        } else if(voterUpdateMunicipality.getSelectedItem().equals("Agdangan")){
            for (String barangayLocation : locationHandler.getAgdanganBarangay()) {
                voterUpdateBarangay.addItem(barangayLocation);
            }
        } else if (voterUpdateMunicipality.getSelectedItem().equals("Unisan")) {
            for (String barangaLocation : locationHandler.getUnisanBarangay()) {
                voterUpdateBarangay.addItem(barangaLocation);
            }
        } else if (voterUpdateMunicipality.getSelectedItem().equals("Padre Burgos")) {
            for (String barangaLocation : locationHandler.getPadreBurgosBarangay()) {
                voterUpdateBarangay.addItem(barangaLocation);
            }
        }
    }//GEN-LAST:event_voterUpdateMunicipalityItemStateChanged

    private void settingsMunicipalityComboxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_settingsMunicipalityComboxItemStateChanged
        settingsBarangayCombox.removeAllItems();
        LocationHandler locationHandler = new LocationHandler();
        if (settingsMunicipalityCombox.getSelectedItem().equals("Default")) {
            settingsBarangayCombox.addItem("Default");
        } else if(settingsMunicipalityCombox.getSelectedItem().equals("Agdangan")){
            for (String barangayLocation : locationHandler.getAgdanganBarangay()) {
                settingsBarangayCombox.addItem(barangayLocation);
            }
        } else if (settingsMunicipalityCombox.getSelectedItem().equals("Unisan")) {
            for (String barangaLocation : locationHandler.getUnisanBarangay()) {
                settingsBarangayCombox.addItem(barangaLocation);
            }
        } else if (settingsMunicipalityCombox.getSelectedItem().equals("Padre Burgos")) {
            for (String barangaLocation : locationHandler.getPadreBurgosBarangay()) {
                settingsBarangayCombox.addItem(barangaLocation);
            }
        }
    }//GEN-LAST:event_settingsMunicipalityComboxItemStateChanged

    private void logoutButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutButtonMouseClicked
        int logoutDialog = JOptionPane.showConfirmDialog(mainPanel, "Are you sure you want to logout?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        switch (logoutDialog) {
            case JOptionPane.YES_OPTION -> {
                Login login = new Login();
                login.setVisible(true);
                this.dispose();
            }
            case JOptionPane.NO_OPTION -> {
            }
            case JOptionPane.CLOSED_OPTION -> {
                JOptionPane.showMessageDialog(mainPanel, "Dialog was closed without making a choice.");
            }
            default -> {
            }
        }
        
    }//GEN-LAST:event_logoutButtonMouseClicked
    
    public boolean validateForm() {
        UserAccessObject userAccessObject = new UserAccessObject();
        
        // Check for empty fields
        if (addUserFirstnameField.getText().isEmpty() ||
            addUserLastnameField.getText().isEmpty() ||
            addUserBirthdateChooser.getDate() == null ||
            addUserGenderCombox.getSelectedItem().toString().equals("Default") ||
            addUserStatusCombox.getSelectedItem().toString().equals("Default") ||
            addUserMunicipalityCombox.getSelectedItem().toString().equals("Default") ||
            addUserBarangayCombox.getSelectedItem().toString().equals("Default") ||
            addUserRoleCombox.getSelectedItem().toString().equals("Default") ||
            addUserEmailField.getText().isEmpty() ||
            addUserPasswordField.getText().isEmpty()) {
            
            JOptionPane.showMessageDialog(addUserDialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check if age is below 18
        if (addUserBirthdateChooser.getDate() != null) {
            // Get the current date
            Calendar currentDate = Calendar.getInstance();

            // Get the selected date
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.setTime(addUserBirthdateChooser.getDate());

            // Calculate the age
            int age = currentDate.get(Calendar.YEAR) - selectedCalendar.get(Calendar.YEAR);
            if (currentDate.get(Calendar.DAY_OF_YEAR) < selectedCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            
            if (age < 18 ){
                JOptionPane.showMessageDialog(addUserDialog, "Age below 18 cannot register.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        

        // Check if the email already exists (you need to implement this part based on your backend/database logic)
        if (userAccessObject.emailExists(addUserEmailField.getText())) {
            JOptionPane.showMessageDialog(addUserDialog, "Email already exists. Please use a different email.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validation successful
        return true;
    }
    
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new AdminDashboard(user).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddUserSaveButton;
    private javax.swing.JButton addCandidateButton;
    private CustomTextField.MyTextField addCandidateFirstnameField;
    private javax.swing.JLabel addCandidateHereLabel;
    private CustomTextField.MyTextField addCandidateLastnameField;
    private javax.swing.JPanel addCandidatePanel;
    private javax.swing.JComboBox<String> addCandidateTitleCombox;
    private javax.swing.JButton addUserBackButton;
    private javax.swing.JComboBox<String> addUserBarangayCombox;
    private com.toedter.calendar.JDateChooser addUserBirthdateChooser;
    private javax.swing.JDialog addUserDialog;
    private CustomTextField.MyTextField addUserEmailField;
    private CustomTextField.MyTextField addUserFirstnameField;
    private javax.swing.JComboBox<String> addUserGenderCombox;
    private CustomTextField.MyTextField addUserLastnameField;
    private javax.swing.JPanel addUserMainPanel;
    private javax.swing.JComboBox<String> addUserMunicipalityCombox;
    private CustomTextField.MyTextField addUserPasswordField;
    private javax.swing.JComboBox<String> addUserRoleCombox;
    private javax.swing.JComboBox<String> addUserStatusCombox;
    private javax.swing.JTable adminsTable;
    private javax.swing.JButton backCDButton;
    private javax.swing.JLabel breadcrumbsCandidates;
    private javax.swing.JLabel breadcrumbsDashboard;
    private javax.swing.JLabel breadcrumbsSettings;
    private javax.swing.JLabel breadcrumbsUsers;
    private javax.swing.JLabel breadcrumbsVotingResults;
    private javax.swing.JLabel candidataesPageTitle;
    private javax.swing.JPanel candidatesPanel;
    private javax.swing.JTable candidatesTable;
    private javax.swing.JButton clearCandidateButton;
    private javax.swing.JLabel dashboardAccountName;
    private javax.swing.JLabel dashboardMainIcon;
    private javax.swing.JLabel dashboardPageTitle;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JPanel editDetailsPanel;
    private CustomCards.GradientCard gradientCard1;
    private CustomCards.GradientCard gradientCard2;
    private CustomCards.GradientCard gradientCard4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<String> latestList;
    private javax.swing.JPanel leftSide;
    private CustomMenu.ListMenu<String> listMenu1;
    private javax.swing.JLabel logoutButton;
    private javax.swing.JPanel mainPanel;
    private CustomChart.PieChart pieChart1;
    private javax.swing.JComboBox<String> piechartCombox;
    private javax.swing.JScrollPane pollsCandidateScrollPanel;
    private javax.swing.JLabel pollsLabelForChart;
    private javax.swing.JLabel registrationNotificationTitle;
    private javax.swing.JLabel registrationNotificationTitle1;
    private javax.swing.JPanel rightSide;
    private javax.swing.JLabel settingsAdminID1;
    private javax.swing.JLabel settingsAdminIDLabel;
    private javax.swing.JTextField settingsAdminIDTextField;
    private javax.swing.JLabel settingsAdminValueLabel;
    private javax.swing.JComboBox<String> settingsBarangayCombox;
    private javax.swing.JLabel settingsBarangayLabel;
    private javax.swing.JLabel settingsBarangayLabel1;
    private javax.swing.JLabel settingsBarangayValueLabel;
    private com.toedter.calendar.JDateChooser settingsBirthdateChooser;
    private javax.swing.JLabel settingsBirthdateLabel;
    private javax.swing.JLabel settingsBirthdateLabel1;
    private javax.swing.JLabel settingsBirthdateValueLabel;
    private javax.swing.JButton settingsCancelChangesButton;
    private javax.swing.JPanel settingsCardLayout;
    private javax.swing.JLabel settingsEmailLabel;
    private javax.swing.JLabel settingsEmailLabel1;
    private javax.swing.JTextField settingsEmailTextField;
    private javax.swing.JLabel settingsEmailValueLabel;
    private javax.swing.JLabel settingsFirstNameLabel;
    private javax.swing.JLabel settingsFirstNameLabel1;
    private javax.swing.JTextField settingsFirstNameTextField;
    private javax.swing.JLabel settingsFirstNameValueLabel;
    private javax.swing.JComboBox<String> settingsGenderCombox;
    private javax.swing.JLabel settingsGenderLabel;
    private javax.swing.JLabel settingsGenderLabel1;
    private javax.swing.JLabel settingsGenderValueLabel;
    private javax.swing.JLabel settingsLastNameLabel;
    private javax.swing.JLabel settingsLastNameLabel1;
    private javax.swing.JTextField settingsLastNameTextField;
    private javax.swing.JLabel settingsLastNameValueLabel;
    private javax.swing.JComboBox<String> settingsMunicipalityCombox;
    private javax.swing.JLabel settingsMunicipalityLabel;
    private javax.swing.JLabel settingsMunicipalityLabel1;
    private javax.swing.JLabel settingsMunicipalityValueLabel;
    private javax.swing.JLabel settingsPageTitle;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JLabel settingsPasswordLabel;
    private javax.swing.JLabel settingsPasswordLabel1;
    private javax.swing.JTextField settingsPasswordTextField;
    private javax.swing.JLabel settingsPasswordValueLabel;
    private javax.swing.JButton settingsSaveChangesButton;
    private javax.swing.JComboBox<String> settingsStatusCombox;
    private javax.swing.JLabel settingsStatusLabel;
    private javax.swing.JLabel settingsStatusLabel1;
    private javax.swing.JLabel settingsStatusValueLabel;
    private javax.swing.JButton settingsUpdateDetailsButton;
    private javax.swing.JPanel showDetailsPanel;
    private javax.swing.JLabel systemTextIcon;
    private javax.swing.JLabel testlabel;
    private javax.swing.JButton updateCDButton;
    private javax.swing.JTextField updateCDFirstNameField;
    private javax.swing.JLabel updateCDFirstNameLabel;
    private javax.swing.JTextField updateCDIDField;
    private javax.swing.JLabel updateCDIDLabel;
    private javax.swing.JTextField updateCDLastNameField;
    private javax.swing.JLabel updateCDLastNameLabel;
    private javax.swing.JComboBox<String> updateCDTitleComBox;
    private javax.swing.JLabel updateCDTitleLabel;
    private javax.swing.JDialog updateCandidatesDialog;
    private javax.swing.JPanel updateCandidatesPanel;
    private javax.swing.JDialog updateUserDialog;
    private javax.swing.JButton updateUserDialogBackButton;
    private javax.swing.JButton updateUserDialogSaveButton;
    private javax.swing.JPanel updateUserPanel;
    private javax.swing.JButton usersAddUserButton;
    private javax.swing.JLabel usersPageTitle;
    private javax.swing.JPanel usersPanel;
    private javax.swing.JPanel usersPanelCardLayout;
    private javax.swing.JTable usersTable;
    private javax.swing.JPanel viewUsersPanel;
    private javax.swing.JComboBox<String> voterUpdateBarangay;
    private com.toedter.calendar.JDateChooser voterUpdateBirthday;
    private javax.swing.JTextField voterUpdateEmail;
    private javax.swing.JTextField voterUpdateFirstName;
    private javax.swing.JComboBox<String> voterUpdateGender;
    private javax.swing.JTextField voterUpdateLastName;
    private javax.swing.JComboBox<String> voterUpdateMunicipality;
    private javax.swing.JComboBox<String> voterUpdateRole;
    private javax.swing.JComboBox<String> voterUpdateStatus;
    private javax.swing.JPanel votingPanel;
    private javax.swing.JTable votingResultTable;
    private javax.swing.JLabel votingResultsPageTitle;
    private javax.swing.JLabel votingTextIcon;
    // End of variables declaration//GEN-END:variables
}
