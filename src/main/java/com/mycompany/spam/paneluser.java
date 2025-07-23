package com.mycompany.spam;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class paneluser extends JPanel {
    
    private JLabel lblemail;
    private JLabel lblname;
    private JLabel lbldob;
    private JLabel lblphn;
    private JLabel lblpass;
    private JLabel lbluserid;
    
    private JTextField tfemail;
    private JTextField tfname;
    private JTextField tfphn;
    private JTextField tfdob;
    private JTextField tfpass;
    private JTextField tfuserid;
    
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnSearch;
    private JButton btnClear;
    
    private Font labelFont;
    
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/spam_detection";
    private static final String DB_USER = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = "harshitha@27"; // Replace with your DB password
    
    public paneluser() {
        initializeComponents();
        setupLayout();
        addEventListeners();
    }
    
    private void initializeComponents() {
        labelFont = new Font(Font.SERIF, Font.BOLD, 14);
        
        // Initialize labels
        lbluserid = new JLabel("User ID:");
        lblemail = new JLabel("Email:");
        lblname = new JLabel("UserName:");
        lbldob = new JLabel("DOB:");
        lblphn = new JLabel("Phone:");
        lblpass = new JLabel("Password:");
        
        // Initialize text fields
        tfuserid = new JTextField(10);
        tfemail = new JTextField(10);
        tfname = new JTextField(10);
        tfdob = new JTextField(10);
        tfphn = new JTextField(10);
        tfpass = new JTextField(10);
        
        // Initialize buttons
        btnSave = new JButton("Save");
        btnUpdate = new JButton("Update");
        btnSearch = new JButton("Search");
        btnClear = new JButton("Clear");
        
        // Set fonts
        setComponentFonts();
    }
    
    private void setComponentFonts() {
        lbluserid.setFont(labelFont);
        lblemail.setFont(labelFont);
        lblname.setFont(labelFont);
        lbldob.setFont(labelFont);
        lblphn.setFont(labelFont);
        lblpass.setFont(labelFont);
    }
    
    private void setupLayout() {
        // Using GridLayout for organized appearance
        this.setLayout(new GridLayout(8, 2, 5, 5));
        addComponents();
    }
    
    private void addComponents() {
        add(lbluserid);
        add(tfuserid);
        add(lblemail);
        add(getTfemail());
        add(lblname);
        add(getTfname());
        add(lbldob);
        add(getTfdob());
        add(lblphn);
        add(getTfphn());
        add(lblpass);
        add(getTfpass());
        
        // Add buttons
        add(btnSave);
        add(btnUpdate);
        add(btnSearch);
        add(btnClear);
    }
    
    private void addEventListeners() {
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUser();
            }
        });
        
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });
        
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchUser();
            }
        });
        
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
    }
    
    private void saveUser() {
        if (!validateInput()) {
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            String sql = "INSERT INTO users (email, username, dob, phone, password) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, tfemail.getText().trim());
            pstmt.setString(2, tfname.getText().trim());
            pstmt.setString(3, tfdob.getText().trim());
            pstmt.setString(4, tfphn.getText().trim());
            pstmt.setString(5, tfpass.getText().trim());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                    "User saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }
            
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this,
                    "Email already exists! Please use a different email.",
                    "Duplicate Email",
                    JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            ex.printStackTrace();
        } finally {
            closeResources(pstmt, conn);
        }
    }
    
    private void updateUser() {
        String userIdText = tfuserid.getText().trim();
        
        if (userIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter User ID to update.",
                "Input Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput()) {
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            int userId = Integer.parseInt(userIdText);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            String sql = "UPDATE users SET email=?, username=?, dob=?, phone=?, password=? WHERE user_id=?";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, tfemail.getText().trim());
            pstmt.setString(2, tfname.getText().trim());
            pstmt.setString(3, tfdob.getText().trim());
            pstmt.setString(4, tfphn.getText().trim());
            pstmt.setString(5, tfpass.getText().trim());
            pstmt.setInt(6, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                    "User updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No user found with ID: " + userId,
                    "User Not Found",
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid numeric User ID.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Database error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            closeResources(pstmt, conn);
        }
    }
    
    private void searchUser() {
        String userIdText = tfuserid.getText().trim();
        
        if (userIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter User ID to search.",
                "Input Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            int userId = Integer.parseInt(userIdText);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            String sql = "SELECT * FROM users WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Populate fields with retrieved data
                tfemail.setText(rs.getString("email"));
                tfname.setText(rs.getString("username"));
                tfdob.setText(rs.getString("dob"));
                tfphn.setText(rs.getString("phone"));
                tfpass.setText(rs.getString("password"));
                
                JOptionPane.showMessageDialog(this,
                    "User found and data loaded!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No user found with ID: " + userId,
                    "User Not Found",
                    JOptionPane.WARNING_MESSAGE);
                clearDataFields();
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid numeric User ID.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Database error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            closeResources(rs, pstmt, conn);
        }
    }
    
    private boolean validateInput() {
        if (tfemail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (tfname.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (tfpass.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Basic email validation
        String email = tfemail.getText().trim();
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void clearFields() {
        tfuserid.setText("");
        clearDataFields();
    }
    
    private void clearDataFields() {
        tfemail.setText("");
        tfname.setText("");
        tfdob.setText("");
        tfphn.setText("");
        tfpass.setText("");
    }
    
    private void closeResources(PreparedStatement pstmt, Connection conn) {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void closeResources(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // Getter methods (keeping your original structure)
    public JTextField getTfemail() {
        return tfemail;
    }
    
    public JTextField getTfname() {
        return tfname;
    }
    
    public JTextField getTfdob() {
        return tfdob;
    }
    
    public JTextField getTfphn() {
        return tfphn;
    }
    
    public JTextField getTfpass() {
        return tfpass;
    }
    
    public JTextField getTfuserid() {
        return tfuserid;
    }
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1L;
}