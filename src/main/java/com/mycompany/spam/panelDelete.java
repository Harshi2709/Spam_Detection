package com.mycompany.spam;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class panelDelete extends JPanel {
    
    private JButton btdelete;
    private JLabel lblid3;
    public JTextField tfid3;
    private Font f4;
    
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/spam_detection";
    private static final String DB_USER = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = "harshitha@27"; // Replace with your DB password
    
    public panelDelete() {
        initializeComponents();
        setupLayout();
        addEventListeners();
    }
    
    private void initializeComponents() {
        f4 = new Font(Font.SERIF, Font.PLAIN, 15);
        setVisible(false);
        
        btdelete = new JButton("Delete");
        lblid3 = new JLabel("ID :");
        tfid3 = new JTextField(15);
        tfid3.setText(""); // Clear the placeholder text
        
        // Set fonts
        tfid3.setFont(f4);
        lblid3.setFont(f4);
        btdelete.setFont(f4);
    }
    
    private void setupLayout() {
        setLayout(null); // Using absolute positioning as in original
        
        // Add components to panel
        add(lblid3);
        add(tfid3);
        add(btdelete);
        
        // Set bounds for components
        lblid3.setBounds(20, 20, 50, 25);
        tfid3.setBounds(80, 20, 150, 25);
        btdelete.setBounds(250, 20, 80, 25);
        
        // Set panel bounds
        setBounds(20, 150, 350, 80);
        setAutoscrolls(true);
    }
    
    private void addEventListeners() {
        btdelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRecord();
            }
        });
    }
    
    private void deleteRecord() {
        String idText = tfid3.getText().trim();
        
        // Validate input
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter an Email ID to delete.", 
                "Input Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = Integer.parseInt(idText);
            
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete email record with ID: " + id + " from inbox?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                performDeletion(id);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid numeric ID.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performDeletion(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            // Establish database connection
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Prepare delete statement for inbox table
            String sql = "DELETE FROM inbox WHERE i_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            // Execute deletion
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                    "Email record with ID " + id + " has been successfully deleted from inbox.",
                    "Deletion Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear the text field after successful deletion
                tfid3.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                    "No email record found with ID: " + id + " in inbox table.",
                    "Record Not Found",
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Database error occurred: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            // Close resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    // Method to show/hide the panel
    public void toggleVisibility() {
        setVisible(!isVisible());
    }
    
    // Method to clear the input field
    public void clearInput() {
        tfid3.setText("");
    }
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1L;
}