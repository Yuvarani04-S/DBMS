import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class showLoginForm {

    private JFrame lf = new JFrame("User Login");
    private JTextField userIdField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private String userId;
    private String source;
    private String destination;

    public showLoginForm() {
        lf.setSize(600, 400);
        lf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        lf.setLocationRelativeTo(null);
        lf.setLayout(null);
        new ppanel(lf);
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setBounds(50, 50, 200, 30);
        userIdField.setBounds(250, 50, 250, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 200, 30);
        passwordField.setBounds(250, 100, 250, 30);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(250, 150, 250, 40);
        loginButton.setBackground(new Color(0, 51, 102));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));

        lf.add(userIdLabel);
        lf.add(userIdField);
        lf.add(passwordLabel);
        lf.add(passwordField);
        lf.add(loginButton);
        lf.setVisible(true);
        addEnterKeyListener(userIdField,passwordField);
        addEnterKeyListener(passwordField, loginButton);
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userId = userIdField.getText();
                String password = new String(passwordField.getPassword());

                if (validateUserCredentials(userId, password)) {
                    showBookCancelTicketFrame();  
                } else {
                    JOptionPane.showMessageDialog(lf, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
                lf.dispose();
            }
        });
    }


    private boolean validateUserCredentials(String userId, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            conn = DriverManager.getConnection(url, "system", "12345");
            String query = "SELECT * FROM users WHERE USERID = ? AND PASSWORD = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, userId);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            return rs.next();  
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    
    private void showBookCancelTicketFrame() {
        JFrame userOptionsFrame = new JFrame("User Options");
        userOptionsFrame.setSize(600, 400);
        userOptionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userOptionsFrame.setLocationRelativeTo(null);
        userOptionsFrame.setLayout(null);
        new ppanel(userOptionsFrame);
    
        JButton bookButton = new JButton("Book Ticket");
        JButton cancelButton = new JButton("Cancel Ticket");
    
        bookButton.setBounds(100, 100, 200, 40);
        bookButton.setBackground(new Color(0, 51, 102));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font("Arial", Font.BOLD, 16));
    
        cancelButton.setBounds(100, 200, 200, 40);
        cancelButton.setBackground(new Color(0, 51, 102));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 16));
    
        userOptionsFrame.add(bookButton);
        userOptionsFrame.add(cancelButton);
        userOptionsFrame.setVisible(true);
    
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBookTicketForm();
                userOptionsFrame.dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
         
                String pnr = JOptionPane.showInputDialog(userOptionsFrame, "Enter your PNR number to cancel:");
    
                if (pnr != null && !pnr.trim().isEmpty()) {
             
                    int response = JOptionPane.showConfirmDialog(userOptionsFrame, 
                            "Are you sure you want to cancel the ticket with PNR: " + pnr + "?",
                            "Confirm Cancellation",
                            JOptionPane.YES_NO_OPTION);
    
                    if (response == JOptionPane.YES_OPTION) {
                        cancelTicket(pnr);
                    }
                } else {
                    JOptionPane.showMessageDialog(userOptionsFrame, "PNR number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void cancelTicket(String pnr) {
        Connection conn = null;
        PreparedStatement stmt = null;
    
        try {
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            conn = DriverManager.getConnection(url, "system", "12345");

            String sql = "UPDATE bookings SET STATUS = ? WHERE PNR = ?";
            stmt = conn.prepareStatement(sql);
    
            stmt.setString(1, "Cancelled");
            stmt.setString(2, pnr);
    
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Your ticket with PNR " + pnr + " has been cancelled successfully.", "Cancellation Confirmed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No booking found with the provided PNR.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while cancelling the ticket.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private String travelDate;

    private void showBookTicketForm() {
        JFrame bookTicketFrame = new JFrame("Book Ticket");
        bookTicketFrame.setSize(600, 500);
        bookTicketFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        bookTicketFrame.setLocationRelativeTo(null);
        bookTicketFrame.setLayout(null);

        JLabel departureLabel = new JLabel("Departure:");
        JTextField departureField = new JTextField();
        departureLabel.setBounds(50, 100, 200, 30);
        departureField.setBounds(250, 100, 250, 30);

        JLabel destinationLabel = new JLabel("Destination:");
        JTextField destinationField = new JTextField();
        destinationLabel.setBounds(50, 150, 200, 30);
        destinationField.setBounds(250, 150, 250, 30);

        JLabel dateLabel = new JLabel("Date (dd-MM-yyyy):");
        JTextField dateField = new JTextField();
        dateLabel.setBounds(50, 200, 200, 30);
        dateField.setBounds(250, 200, 250, 30);

        JButton searchButton = new JButton("Search Trains");
        searchButton.setBounds(250, 250, 200, 40);
        searchButton.setBackground(new Color(0, 51, 102));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        bookTicketFrame.add(departureLabel);
        bookTicketFrame.add(departureField);
        bookTicketFrame.add(destinationLabel);
        bookTicketFrame.add(destinationField);
        bookTicketFrame.add(dateLabel);
        bookTicketFrame.add(dateField);
        bookTicketFrame.add(searchButton);
        bookTicketFrame.setVisible(true);
        addEnterKeyListener(departureField, destinationField);
        addEnterKeyListener(destinationField, dateField);
        addEnterKeyListener(dateField, searchButton);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                source = departureField.getText();
                destination = destinationField.getText();
                travelDate = dateField.getText(); 
                searchAndDisplayTrains(source, destination, travelDate);

                bookTicketFrame.dispose();
            }
        });
    }

    private void searchAndDisplayTrains(String source, String destination, String travelDate) {
        JFrame trainFrame = new JFrame("Available Trains");
        trainFrame.setSize(800, 500); 
        trainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        trainFrame.setLocationRelativeTo(null);
        trainFrame.setLayout(null);

        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Disables editing for all cells
            }
        };
        model.addColumn("Train No");
        model.addColumn("Train Name");
        model.addColumn("Source");
        model.addColumn("Destination");
        model.addColumn("First Class");
        model.addColumn("Second Class");
        model.addColumn("Sleeper");
        model.addColumn("Date & Time");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            conn = DriverManager.getConnection(url, "system", "12345");
            String query = "SELECT * FROM trains WHERE (SOURCE = ? OR DEST = ? OR STOP1 = ? OR STOP2 = ?) " +
                           "AND TO_CHAR(DOJ, 'DD-MM-YYYY') = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, source);
            stmt.setString(2, destination);
            stmt.setString(3, source);
            stmt.setString(4, destination);
            stmt.setString(5, travelDate); 

            rs = stmt.executeQuery();
            while (rs.next()) {
                String trainNo = rs.getString("TRAINO");
                String trainName = rs.getString("TRAINAME");
                String trainSource = rs.getString("SOURCE");
                String trainDest = rs.getString("DEST");
                int firstClassSeats = rs.getInt("FST");
                int secondClassSeats = rs.getInt("SND");
                int sleeperSeats = rs.getInt("SLP");
                Timestamp doj = rs.getTimestamp("DOJ");
                String dateTime = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(doj);

                model.addRow(new Object[]{
                    trainNo, 
                    trainName, 
                    trainSource, 
                    trainDest, 
                    firstClassSeats, 
                    secondClassSeats, 
                    sleeperSeats,
                    dateTime
                });
            }

            JTable trainTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(trainTable);
            scrollPane.setBounds(50, 50, 700, 350); 
            trainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            trainTable.getColumnModel().getColumn(0).setPreferredWidth(100); 
            trainTable.getColumnModel().getColumn(1).setPreferredWidth(100);  
            trainTable.getColumnModel().getColumn(2).setPreferredWidth(100); 
            trainTable.getColumnModel().getColumn(3).setPreferredWidth(100); 
            trainTable.getColumnModel().getColumn(4).setPreferredWidth(50); 
            trainTable.getColumnModel().getColumn(5).setPreferredWidth(50); 
            trainTable.getColumnModel().getColumn(6).setPreferredWidth(50); 
            trainTable.getColumnModel().getColumn(7).setPreferredWidth(150); 
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
            trainTable.setRowSorter(sorter);
            trainFrame.add(scrollPane);
            trainFrame.setVisible(true);

            trainTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = trainTable.getSelectedRow();
                    String trainNo = (String) model.getValueAt(row, 0);
                    showPassengerDetailsForm(trainNo); 
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void showPassengerDetailsForm(String trainNo) {
        JFrame passengerDetailsFrame = new JFrame("Passenger Details");
        passengerDetailsFrame.setSize(600, 500);
        passengerDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        passengerDetailsFrame.setLocationRelativeTo(null);
        passengerDetailsFrame.setLayout(null);
        new ppanel(passengerDetailsFrame);

        JLabel numPassengersLabel = new JLabel("Number of Passengers (Max 4):");
        numPassengersLabel.setBounds(50, 50, 200, 30);
        JTextField numPassengersField = new JTextField();
        numPassengersField.setBounds(250, 50, 250, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(250, 100, 250, 40);
        submitButton.setBackground(new Color(0, 51, 102));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));

        passengerDetailsFrame.add(numPassengersLabel);
        passengerDetailsFrame.add(numPassengersField);
        passengerDetailsFrame.add(submitButton);
        passengerDetailsFrame.setVisible(true);
        addEnterKeyListener(numPassengersField, submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numPassengers = Integer.parseInt(numPassengersField.getText());
                if (numPassengers <= 4) {
                    showIndividualPassengerDetailsForm(trainNo, numPassengers);
                } else {
                    JOptionPane.showMessageDialog(passengerDetailsFrame, "Maximum 4 passengers allowed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                passengerDetailsFrame.dispose();
            }
        });
    }

private void showIndividualPassengerDetailsForm(String trainNo, int numPassengers) {
    String pnr = generatePNR();
    List<PassengerDetails> passengers = new ArrayList<>(); 
    
    for (int i = 0; i < numPassengers; i++) {
        JFrame passengerDetailsFormFrame = new JFrame("Passenger " + (i + 1) + " Details");
        passengerDetailsFormFrame.setSize(400, 300);
        passengerDetailsFormFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        passengerDetailsFormFrame.setLocationRelativeTo(null);
        passengerDetailsFormFrame.setLayout(new GridLayout(5, 2));
        
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        String[] genderOptions = {"Male", "Female", "Other"};
        JComboBox<String> genderComboBox = new JComboBox<>(genderOptions);
        String[] classOptions = {"First Class", "Second Class", "Sleeper"};
        JComboBox<String> classComboBox = new JComboBox<>(classOptions);
        passengerDetailsFormFrame.add(new JLabel("Name:"));
        passengerDetailsFormFrame.add(nameField);
        passengerDetailsFormFrame.add(new JLabel("Age:"));
        passengerDetailsFormFrame.add(ageField);
        passengerDetailsFormFrame.add(new JLabel("Gender:"));
        passengerDetailsFormFrame.add(genderComboBox);
        passengerDetailsFormFrame.add(new JLabel("Class:"));
        passengerDetailsFormFrame.add(classComboBox);
        
        JButton submitDetailsButton = new JButton("Submit Details");
        submitDetailsButton.setBackground(new Color(0, 51, 102));
        submitDetailsButton.setForeground(Color.WHITE);
        submitDetailsButton.setFont(new Font("Arial", Font.BOLD, 16));
        passengerDetailsFormFrame.add(submitDetailsButton);
        addEnterKeyListener(nameField, ageField);
        addEnterKeyListener(genderComboBox, classComboBox);
        addEnterKeyListener(classComboBox, submitDetailsButton);
        
        passengerDetailsFormFrame.setVisible(true);
        submitDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int trainno = Integer.parseInt(trainNo);
                int userid = Integer.parseInt(userId); 
                String name = nameField.getText();
                String age = ageField.getText();
                String gender = (String) genderComboBox.getSelectedItem();
                String classType = (String) classComboBox.getSelectedItem();
                
                int ageValue = Integer.parseInt(age);
                int bookid = generateBookingID();
                passengers.add(new PassengerDetails(bookid, pnr, trainno, userid, classType, name, ageValue, source, destination, gender));

                passengerDetailsFormFrame.dispose(); 
                if (passengers.size() == numPassengers) {
                    for (PassengerDetails passenger : passengers) {
                        insertBooking(passenger);
                    }
                    
                    
                    //JOptionPane.showMessageDialog(null, "All passengers' details entered successfully!","Booking Status", JOptionPane.INFORMATION_MESSAGE);
                    
                    showPayment(pnr);
                }
            }
        });
    }
}

class PassengerDetails {
    int bookid;
    String pnr;
    int trainno;
    int userid;
    String classType;
    String name;
    int age;
    String src;
    String dest;
    String gender;

    public PassengerDetails(int bookid, String pnr, int trainno, int userid, String classType, String name, 
                            int age, String src, String dest, String gender) {
        this.bookid = bookid;
        this.pnr = pnr;
        this.trainno = trainno;
        this.userid = userid;
        this.classType = classType;
        this.name = name;
        this.age = age;
        this.src = src;
        this.dest = dest;
        this.gender = gender;
    }
}

private void insertBooking(PassengerDetails passenger) {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String classType = "";
    switch (passenger.classType) {
        case "First Class":
            classType = "fst";
            break;
        case "Second Class":
            classType = "snd";
            break;
        case "Sleeper":
            classType = "slp";
            break;
        default:
            classType = "Unknown"; 
            break;
    }

    try {
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        conn = DriverManager.getConnection(url, "system", "12345");
        String checkSeatsQuery = "SELECT " + classType + " FROM trains WHERE TRAINO = ?";
        stmt = conn.prepareStatement(checkSeatsQuery);
        stmt.setInt(1, passenger.trainno);
        rs = stmt.executeQuery();
        
        int availableSeats = 0;
        if (rs.next()) {
            availableSeats = rs.getInt(classType); 
        }

        String status = (availableSeats > 0) ? "Confirmed" : "Waitlisted";
        
        if (status.equals("Confirmed")) {
            String updateSeatsQuery = "UPDATE trains SET " + classType + " = " + classType + " - 1 WHERE TRAINO = ?";
            stmt = conn.prepareStatement(updateSeatsQuery);
            stmt.setInt(1, passenger.trainno);
            stmt.executeUpdate();
        } else {
            String waitlistQuery = "INSERT INTO waitlist (WAITLISTID, PNR, TRAINNO, BOOKINGDATE, STATUS, POSITION, USERID) "
                    + "VALUES (WAITLIST_SEQ.NEXTVAL, ?, ?, SYSDATE, 'Waitlisted', (SELECT COUNT(*) + 1 FROM waitlist WHERE TRAINNO = ? AND STATUS = 'Waitlisted'), ?)";
            stmt = conn.prepareStatement(waitlistQuery);
            stmt.setString(1, passenger.pnr);
            stmt.setInt(2, passenger.trainno);
            stmt.setInt(3, passenger.trainno);
            stmt.setInt(4, passenger.userid);
            int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Booking " + status + " for " + passenger.name, "Booking Status", JOptionPane.INFORMATION_MESSAGE);
        }
        }

        String sql = "INSERT INTO bookings(BOOKID, PNR, TRAINO, USERID, CLASS, NAME, AGE, SRC, DEST, GENDER, STATUS) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, passenger.bookid);
        stmt.setString(2, passenger.pnr);
        stmt.setInt(3, passenger.trainno);
        stmt.setInt(4, passenger.userid);
        stmt.setString(5, passenger.classType);
        stmt.setString(6, passenger.name);
        stmt.setInt(7, passenger.age);
        stmt.setString(8, passenger.src);
        stmt.setString(9, passenger.dest);
        stmt.setString(10, passenger.gender);
        stmt.setString(11, status);

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Booking " + status + " for " + passenger.name, "Booking Status", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error occurred while booking.", "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

    public void showPayment(String pnr) {
        JFrame pay = new JFrame("Payment Gateway");
        pay.setLocationRelativeTo(null);
        JLabel lab = new JLabel("Proceeding to Payment!");
        String[] options = {"NetBanking", "CreditCard", "DebitCard", "PhonePay"};
        JComboBox<String> com = new JComboBox<>(options);
        pay.setSize(500, 200);
        pay.setLayout(null);
        pay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        lab.setBounds(20, 20, 200, 30);
        String ppnr=pnr;
        com.setBounds(20, 60, 150, 30);
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setBounds(20, 100, 150, 30);

        pay.add(lab);
        pay.add(com);
        pay.add(confirmButton);
        pay.setVisible(true);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) com.getSelectedItem();
                JOptionPane.showMessageDialog(pay, "Selected Payment Method: " + selectedOption);
                insertPayment(selectedOption,ppnr);  
                pay.dispose();  
            }
        });
    }

    private void insertPayment(String paymentMethod,String ppnr) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            conn = DriverManager.getConnection(url, "system", "12345");

            String sql = "INSERT INTO payments(PAYMENTID, PNR, PAYM, STATUS) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);

            
            int bookid = generateBookingID();  
            //String pnr = generatePNR(); 
            String status = "Paid";  

            stmt.setInt(1, bookid);  
            stmt.setString(2, ppnr);  
            stmt.setString(3, paymentMethod);  
            stmt.setString(4, status); 

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                
                JOptionPane.showMessageDialog(null, "Payment successful! Your PNR number is: " + ppnr, "Payment Confirmed", JOptionPane.INFORMATION_MESSAGE);
                //JOptionPane.showMessageDialog(null, "Booking " + status + " for " + ppnr, "Booking Status", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Payment failed. Please try again.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while processing payment.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String generatePNR() {
        Random random = new Random();
        return "PNR" + (100000 + random.nextInt(900000));
    }

    private int generateBookingID() {
        Random random = new Random();
        return 10000 + random.nextInt(90000);
    }
    
    private void addEnterKeyListener(JTextField field, final JButton button) {
        field.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    button.doClick();
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