import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class showRegistrationForm {
    showRegistrationForm() {
        JFrame rf = new JFrame("User Registration");
        rf.setSize(600, 500);
        rf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rf.setLocationRelativeTo(null);
        rf.setLayout(null);
        new ppanel(rf);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        nameLabel.setBounds(50, 50, 200, 30);
        nameField.setBounds(250, 50, 250, 30);

        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField();
        ageLabel.setBounds(50, 100, 200, 30);
        ageField.setBounds(250, 100, 250, 30);

        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField();
        phoneLabel.setBounds(50, 150, 200, 30);
        phoneField.setBounds(250, 150, 250, 30);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        emailLabel.setBounds(50, 200, 200, 30);
        emailField.setBounds(250, 200, 250, 30);

        JLabel aadharLabel = new JLabel("Aadhar Number:");
        JTextField aadharField = new JTextField();
        aadharLabel.setBounds(50, 250, 200, 30);
        aadharField.setBounds(250, 250, 250, 30);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        passwordLabel.setBounds(50, 300, 200, 30);
        passwordField.setBounds(250, 300, 250, 30);

        JLabel genderLabel = new JLabel("Gender:");
        JComboBox<String> genderComboBox = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        genderLabel.setBounds(50, 350, 200, 30);
        genderComboBox.setBounds(250, 350, 250, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(250, 400, 250, 40);
        submitButton.setBackground(new Color(0, 51, 102));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));

        rf.add(nameLabel);
        rf.add(nameField);
        rf.add(ageLabel);
        rf.add(ageField);
        rf.add(phoneLabel);
        rf.add(phoneField);
        rf.add(emailLabel);
        rf.add(emailField);
        rf.add(aadharLabel);
        rf.add(aadharField);
        rf.add(passwordLabel);
        rf.add(passwordField);
        rf.add(genderLabel);
        rf.add(genderComboBox);
        rf.add(submitButton);

        rf.setVisible(true);

        addEnterKeyListener(nameField, ageField);
        addEnterKeyListener(ageField, phoneField);
        addEnterKeyListener(phoneField, emailField);
        addEnterKeyListener(emailField, aadharField);
        addEnterKeyListener(aadharField, passwordField);
        addEnterKeyListener(passwordField, genderComboBox);
        addEnterKeyListener(genderComboBox, submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String ageText = ageField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();
                String aadhar = aadharField.getText();
                String password = new String(passwordField.getPassword());
                String gender = (String) genderComboBox.getSelectedItem();

                // Check if all fields are filled
                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || aadhar.isEmpty()) {
                    JOptionPane.showMessageDialog(rf, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validate email format
                if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                    JOptionPane.showMessageDialog(rf, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (aadhar.length() != 12 || !aadhar.matches("\\d{12}")) {
                    JOptionPane.showMessageDialog(rf, "Aadhar number must be exactly 12 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

               
                if (phone.length() != 10 || !phone.matches("\\d{10}")) {
                    JOptionPane.showMessageDialog(rf, "Phone number must be exactly 10 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                
                int age = 0;
                try {
                    age = Integer.parseInt(ageText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(rf, "Please enter a valid age.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

               
                if (password.length() > 10) {
                    JOptionPane.showMessageDialog(rf, "Password must be 10 characters or fewer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Random rand = new Random();
                int userId = rand.nextInt(90000) + 10000; 

                
                Connection con = null;
                PreparedStatement pst = null;

                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    String url = "jdbc:oracle:thin:@localhost:1521:orcl";
                     con = DriverManager.getConnection(url, "system", "12345");

                    pst = con.prepareStatement(
                        "INSERT INTO users (USERID, NAME, AGE, PHONE, EMAIL, AADHAR, PASSWORD, GENDER) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                    );
                    pst.setInt(1, userId); 
                    pst.setString(2, name);
                    pst.setInt(3, age);  
                    pst.setString(4, phone);  
                    pst.setString(5, email); 
                    pst.setString(6, aadhar);  
                    pst.setString(7, password);  
                    pst.setString(8, gender);  

                    int rowsInserted = pst.executeUpdate();
                    //con.commit();

                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(rf, "Registration successful! Your User ID is: " + userId, "Success", JOptionPane.INFORMATION_MESSAGE);
                        rf.dispose();
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(rf, "Error inserting data into the database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    try {
                        if (pst != null) pst.close();
                        if (con != null) con.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void addEnterKeyListener(JComponent from, JComponent to) {
        from.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    to.requestFocus();
                }
            }
        });
    }
    private void addEnterKeyListener(JComponent field, final JButton button) {
        field.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    button.doClick();
                }
            }
        });
    }

    private void addEnterKeyListener(JTextField field1, JTextField field2) {
        field1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    field2.requestFocus();
                }
            }
        });
    }
}
