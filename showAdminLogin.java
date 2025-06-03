import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
public class showAdminLogin{
    private Connection connectDB() throws SQLException {
    return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "12345");
}
public showAdminLogin() {
    JFrame jf=new JFrame("Admin Login");
    jf.setSize(1920, 1080);
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon backgroundIcon = new ImageIcon("C:/Users/Karthick/Downloads/background.jpg");
            Image backgroundImage = backgroundIcon.getImage();
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    };
    
    
    panel.setLayout(null);  
    jf.setContentPane(panel);
    jf.setVisible(true);
    JFrame adminFrame = new JFrame("Admin Login");
    adminFrame.setSize(400, 300);
    adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    adminFrame.setLocationRelativeTo(null);
    adminFrame.setLayout(null);
    
    JLabel adminIdLabel = new JLabel("Admin ID:");
    JLabel passwordLabel = new JLabel("Password:");
    
    JTextField adminIdField = new JTextField();
    JPasswordField adminPasswordField = new JPasswordField();
    
    adminIdLabel.setBounds(50, 50, 200, 30);
    adminIdField.setBounds(250, 50, 100, 30);
    passwordLabel.setBounds(50, 100, 200, 30);
    adminPasswordField.setBounds(250, 100, 100, 30);
    
    JButton loginButton = new JButton("Login");
    loginButton.setBounds(250, 150, 100, 40);
    loginButton.setBackground(new Color(0, 51, 102));
    loginButton.setForeground(Color.WHITE);

    adminFrame.add(adminIdLabel);
    adminFrame.add(adminIdField);
    adminFrame.add(passwordLabel);
    adminFrame.add(adminPasswordField);
    adminFrame.add(loginButton);
    addEnterKeyListener(adminIdField, adminPasswordField);
    addEnterKeyListener(adminPasswordField, loginButton);
    
    loginButton.addActionListener(e -> {
        if ("Admin".equals(adminIdField.getText()) && "Admin".equals(new String(adminPasswordField.getPassword()))) {
            showAdminMenu();
            adminFrame.dispose(); 
        } else {
            JOptionPane.showMessageDialog(adminFrame, "Invalid credentials.");
        }
    });
    
    adminFrame.setVisible(true);
}

private void showAdminMenu() {
    JFrame menuFrame = new JFrame("Admin Menu");
    menuFrame.setSize(800, 600);
    menuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    menuFrame.setLocationRelativeTo(null);
    
    JMenuBar menuBar = new JMenuBar();

    JMenu trainsMenu = new JMenu("Trains");
    JMenu passengersMenu = new JMenu("Passengers");
    JMenu usersMenu = new JMenu("Users");
    JMenu paymentsMenu = new JMenu("Payments");

    JMenuItem viewTrainsItem = new JMenuItem("View Trains");
    JMenuItem viewPassengersItem = new JMenuItem("View Passengers");
    JMenuItem viewUsersItem = new JMenuItem("View Users");
    JMenuItem viewPaymentsItem = new JMenuItem("View Payments");

    trainsMenu.add(viewTrainsItem);
    passengersMenu.add(viewPassengersItem);
    usersMenu.add(viewUsersItem);
    paymentsMenu.add(viewPaymentsItem);

    menuBar.add(trainsMenu);
    menuBar.add(passengersMenu);
    menuBar.add(usersMenu);
    menuBar.add(paymentsMenu);

    menuFrame.setJMenuBar(menuBar);

    viewTrainsItem.addActionListener(e -> displayTrainDetails());
    viewPassengersItem.addActionListener(e -> displayPassengerDetails());
    viewUsersItem.addActionListener(e -> displayUserDetails());
    viewPaymentsItem.addActionListener(e -> displayPaymentDetails());

    menuFrame.setVisible(true);
}
//Train Details inga irku!
private void displayTrainDetails() {
    try (Connection conn = connectDB()) {
        String query = "SELECT * FROM trains";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        JTable trainTable = new JTable();
        DefaultTableModel model = new DefaultTableModel(new String[]{"Train No", "Train Name", "Source", "Stop1", "Stop2", "Destination", "First Class", "Second Class", "Sleeper", "Date of Journey"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Disables editing for all cells
            }
        };
        trainTable.setModel(model);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        trainTable.setRowSorter(sorter);

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("TRAINO"),
                rs.getString("TRAINAME"),
                rs.getString("SOURCE"),
                rs.getString("STOP1"),
                rs.getString("STOP2"),
                rs.getString("DEST"),
                rs.getInt("FST"),
                rs.getInt("SND"),
                rs.getInt("SLP"),
                rs.getDate("DOJ")
            });
        }

        JScrollPane scrollPane = new JScrollPane(trainTable);
        JFrame trainFrame = new JFrame("Train Details");
        trainFrame.setSize(800, 400);
        trainFrame.add(scrollPane);
        trainFrame.setVisible(true);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//PAssengers 
private void displayPassengerDetails() {
    try (Connection conn = connectDB()) {
        String query = "SELECT b.NAME, b.AGE, b.PNR, b.CLASS, b.STATUS, t.TRAINAME, b.SRC, b.DEST " +
                       "FROM bookings b JOIN trains t ON b.TRAINO = t.TRAINO";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        JTable passengerTable = new JTable();
        DefaultTableModel model = new DefaultTableModel(new String[]{"Name", "Age", "PNR", "Class", "Status", "Train Name", "Source", "Destination"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Disables editing for all cells
            }
        };
        passengerTable.setModel(model);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        passengerTable.setRowSorter(sorter);

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("NAME"),
                rs.getInt("AGE"),
                rs.getString("PNR"),
                rs.getString("CLASS"),
                rs.getString("STATUS"),
                rs.getString("TRAINAME"),
                rs.getString("SRC"),
                rs.getString("DEST")
            });
        }

        JScrollPane scrollPane = new JScrollPane(passengerTable);
        JFrame passengerFrame = new JFrame("Passenger Details");
        passengerFrame.setSize(800, 400);
        passengerFrame.add(scrollPane);
        passengerFrame.setVisible(true);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
// Users Details inga iruku!
private void displayUserDetails() {
    try (Connection conn = connectDB()) {
        String query = "SELECT * FROM users";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        JTable userTable = new JTable();
        DefaultTableModel model = new DefaultTableModel(new String[]{"User ID", "Name", "Age", "Phone", "Email", "Aadhar", "Gender"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Disables editing for all cells
            }
        };
        userTable.setModel(model);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        userTable.setRowSorter(sorter);

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("USERID"),
                rs.getString("NAME"),
                rs.getInt("AGE"),
                rs.getString("PHONE"),
                rs.getString("EMAIL"),
                rs.getString("AADHAR"),
                rs.getString("GENDER")
            });
        }

        JScrollPane scrollPane = new JScrollPane(userTable);
        JFrame userFrame = new JFrame("User Details");
        userFrame.setSize(800, 400);
        userFrame.add(scrollPane);
        userFrame.setVisible(true);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
// Payments Details Query inga iruku!
private void displayPaymentDetails() {
    try (Connection conn = connectDB()) {
        String query = "SELECT * FROM payments";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        JTable paymentTable = new JTable();
        DefaultTableModel model = new DefaultTableModel(new String[]{"Payment ID", "PNR", "Payment Method", "Status"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        paymentTable.setModel(model);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        paymentTable.setRowSorter(sorter);

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("PAYMENTID"),
                rs.getString("PNR"),
                rs.getString("PAYM"),
                rs.getString("STATUS")
            });
        }

        JScrollPane scrollPane = new JScrollPane(paymentTable);
        JFrame paymentFrame = new JFrame("Payment Details");
        paymentFrame.setSize(800, 400);
        paymentFrame.add(scrollPane);
        paymentFrame.setVisible(true);

    } catch (SQLException e) {
        e.printStackTrace();
    }
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
public static void main(String[] args) {
    new showAdminLogin();
}
}