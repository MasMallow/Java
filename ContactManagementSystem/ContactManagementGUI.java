package ContactManagementSystem;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class ContactManagementGUI {

    private JFrame frame;
    private JTextField nameField, phoneField, emailField;
    private JButton addButton, viewButton, deleteButton, editButton;

    public ContactManagementGUI() {
        frame = new JFrame("Contact Manager");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 2));

        JLabel nameLabel = new JLabel("Name: ");
        nameField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone: ");
        phoneField = new JTextField();
        JLabel emailLabel = new JLabel("Email: ");
        emailField = new JTextField();

        addButton = new JButton("Add Contact");
        viewButton = new JButton("View Contact");
        deleteButton = new JButton("Delete Contact");
        editButton = new JButton("Edit Contact");

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(phoneLabel);
        frame.add(phoneField);
        frame.add(emailLabel);
        frame.add(emailField);
        frame.add(new JLabel());
        frame.add(addButton);
        frame.add(viewButton);
        frame.add(deleteButton);
        frame.add(editButton);

        addButton   .addActionListener(e -> addContact    ());
        viewButton  .addActionListener(e -> viewContacts  ());
        deleteButton.addActionListener(e -> deleteContacts());
        editButton  .addActionListener(e -> editContacts  ());
        frame.setVisible(true);
    }

    private void viewContacts() {
        JTextArea textArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(textArea);
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM contacts";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");

                textArea.append("ID: " + id + "\n");
                textArea.append("Name: " + name + "\n");
                textArea.append("Phone: " + phone + "\n");
                textArea.append("Email: " + email + "\n");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, scrollPane, "View Contacts", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "INSERT INTO contacts (name, phone, email) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Contact added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "ERROR: " + e.getMessage());
        }
    }

    private void deleteContacts() {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM contacts";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            StringBuilder contactsList = new StringBuilder();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");

                contactsList.append(id).append(": ").append(name).append(" (Phone: ").append(phone).append(", Email: ")
                        .append(email).append(")\n");
            }
            String selectedContact = JOptionPane.showInputDialog(frame,
                    "Select contact ID to delete:\n\n" + contactsList.toString());
            if (selectedContact != null && !selectedContact.trim().isEmpty()) {
                int id = Integer.parseInt(selectedContact.trim());
                String deleteQuery = "DELETE FROM contacts WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Contact deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Contact not found!");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid ID format!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error" + e.getMessage());
        }
    }

    public void editContacts() {
        System.out.println("editContacts method called");
        try (Connection conn = DatabaseConnection.connect()) {
            conn.setAutoCommit(false); // Start transaction

            // Fetch all contacts
            String query = "SELECT * FROM contacts";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery()) {

                StringBuilder contactsList = new StringBuilder();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String phone = rs.getString("phone");
                    String email = rs.getString("email");

                    contactsList.append(id).append(": ").append(name)
                            .append(" (Phone: ").append(phone)
                            .append(", Email: ").append(email).append(")\n");
                }

                // Ask user to select a contact to edit
                String selectedContact = JOptionPane.showInputDialog(frame,
                        "Select contact ID to edit:\n\n" + contactsList.toString());
                System.out.println("Selected contact ID: " + selectedContact);

                if (selectedContact != null && !selectedContact.trim().isEmpty()) {
                    int id = Integer.parseInt(selectedContact.trim());

                    // Fetch the selected contact details
                    String fetchQuery = "SELECT * FROM contacts WHERE id = ?";
                    try (PreparedStatement fetchStmt = conn.prepareStatement(fetchQuery)) {
                        fetchStmt.setInt(1, id);
                        try (ResultSet contactDetails = fetchStmt.executeQuery()) {
                            if (contactDetails.next()) {
                                String currentName = contactDetails.getString("name");
                                String currentPhone = contactDetails.getString("phone");
                                String currentEmail = contactDetails.getString("email");

                                // Create edit dialog
                                JDialog editDialog = new JDialog(frame, "Edit Contact", true);
                                editDialog.setLayout(new GridLayout(4, 2));

                                JLabel nameLabel = new JLabel("Name: ");
                                JTextField nameEditField = new JTextField(currentName);
                                JLabel phoneLabel = new JLabel("Phone: ");
                                JTextField phoneEditField = new JTextField(currentPhone);
                                JLabel emailLabel = new JLabel("Email: ");
                                JTextField emailEditField = new JTextField(currentEmail);
                                JButton okButton = new JButton("OK");

                                editDialog.add(nameLabel);
                                editDialog.add(nameEditField);
                                editDialog.add(phoneLabel);
                                editDialog.add(phoneEditField);
                                editDialog.add(emailLabel);
                                editDialog.add(emailEditField);
                                editDialog.add(new JLabel());
                                editDialog.add(okButton);

                                // OK button action listener
                                okButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        System.out.println("OK button clicked!");
                                        try {
                                            String updateQuery = "UPDATE contacts SET name = ?, phone = ?, email = ? WHERE id = ?";
                                            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                                updateStmt.setString(1, nameEditField.getText());
                                                updateStmt.setString(2, phoneEditField.getText());
                                                updateStmt.setString(3, emailEditField.getText());
                                                updateStmt.setInt(4, id);

                                                int rowsAffected = updateStmt.executeUpdate();
                                                if (rowsAffected > 0) {
                                                    conn.commit(); // Commit transaction
                                                    JOptionPane.showMessageDialog(editDialog,
                                                            "Contact updated successfully!");
                                                    editDialog.dispose();
                                                } else {
                                                    conn.rollback(); // Rollback if no update occurred
                                                    JOptionPane.showMessageDialog(editDialog,
                                                            "Failed to update contact!");
                                                }
                                            }
                                        } catch (SQLException ex) {
                                            try {
                                                conn.rollback(); // Rollback on exception
                                            } catch (SQLException rollbackEx) {
                                                rollbackEx.printStackTrace();
                                            }
                                            JOptionPane.showMessageDialog(editDialog,
                                                    "Database error: " + ex.getMessage());
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                // Debug: Check if ActionListener is attached
                                if (okButton.getActionListeners().length > 0) {
                                    System.out.println("OK button has ActionListener attached.");
                                } else {
                                    System.out.println("Warning: OK button has no ActionListener!");
                                }

                                // Additional mouse listener for debugging
                                okButton.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        System.out.println("OK button mouse clicked!");
                                    }
                                });

                                editDialog.setSize(300, 200);
                                editDialog.setLocationRelativeTo(frame);
                                System.out.println("Edit dialog created and about to be shown.");
                                editDialog.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(frame, "Contact not found!");
                            }
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid ID format!");
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("editContacts method completed");
    }

    public static void main(String[] args) {
        new ContactManagementGUI();
    }
}
