import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class KynRailways {

    public KynRailways() {
        JFrame f = new JFrame("KYN Railways, Wish you a Happy Journey!");
        f.setSize(1920, 1080);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setLayout(null);
        
        JButton loginButton = createButtonWithTextAndIcon("Clknkn", "Login", 100, 200);
        JButton registerButton = createButtonWithTextAndIcon("C:/Users/Karthick/Pictures/Saved Pictures/dbms31.jpg", "Register", 350, 200);
        JButton pnrButton = new JButton("Check PNR Status");
        pnrButton.setBounds(100,100,400,50);
        pnrButton.setBackground(new Color(0, 51, 102));
        pnrButton.setForeground(Color.WHITE);
        //Font buttonFont = new Font("Arial", Font.BOLD, 18); 
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
        f.setContentPane(panel);
        f.add(loginButton);
        f.add(registerButton);
        f.add(pnrButton);

        registerButton.addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                        new showRegistrationForm();
                   }
                });
    
                loginButton.addActionListener(new ActionListener() {
                            @Override
                             public void actionPerformed(ActionEvent e) {
                                 new showLoginForm();
                             }
                         });

        
        pnrButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPNRStatus();
            }
        });
        
        f.setVisible(true);
    }

    private JButton createButtonWithTextAndIcon(String iconPath, String text, int x, int y) {
        JButton button = new JButton(text);
        button.setIcon(new ImageIcon(iconPath));
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.setBounds(x, y, 200, 200);
        button.setBackground(new Color(0, 51, 102));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return button;
    }

    private void showPNRStatus() {
        JFrame pnr = new JFrame("PNR Status");
        pnr.setSize(1200, 600);
        pnr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pnr.setLocationRelativeTo(null);
        pnr.setLayout(null);
        new ppanel(pnr);
        
        JLabel pnrLabel = new JLabel("Enter PNR Number:");
        JTextField pnrField = new JTextField(10);
        pnrLabel.setBounds(50, 50, 200, 40);
        pnrField.setBounds(250, 50, 300, 40);
        
        JButton checkStatusButton = new JButton("Check Status");
        checkStatusButton.setBounds(600, 50, 200, 50);
        checkStatusButton.setBackground(new Color(0, 51, 102));
        checkStatusButton.setForeground(Color.WHITE);
        
        pnr.add(pnrLabel);
        pnr.add(pnrField);
        pnr.add(checkStatusButton);
        addEnterKeyListener(pnrField, checkStatusButton);
        
    
        checkStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pnrNumber = pnrField.getText();
                if (!pnrNumber.isEmpty()) {
                    showPassengerDetails(pnrNumber, pnr);
                } else {
                    JOptionPane.showMessageDialog(pnr, "Please enter a valid PNR number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        pnr.setVisible(true);
    }
    
    private void showPassengerDetails(String pnrNumber, JFrame pnrFrame) {
        Connection conn = null;
        ResultSet rs = null;
    
        try {
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            conn = DriverManager.getConnection(url, "system", "12345");
            String sql = "SELECT b.NAME, b.AGE, b.CLASS, b.GENDER, b.STATUS, p.PAYM, t.DOJ, b.TRAINO, t.TRAINAME " +
                         "FROM bookings b " +
                         "JOIN payments p ON b.PNR = p.PNR " +
                         "JOIN trains t ON b.TRAINO = t.TRAINO " +
                         "WHERE b.PNR = ?";
    
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pnrNumber);
    
            rs = pstmt.executeQuery();
            String[] columns = {"Name", "Age", "Class", "Gender", "Booking Status", "Payment Mode", "Train No", "Train Name", "Train Date & Time"};
    
            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; 
                }
            };
    
            if (rs.next()) { 
                do {
                    String name = rs.getString("NAME");
                    int age = rs.getInt("AGE");
                    String classType = rs.getString("CLASS");
                    String gender = rs.getString("GENDER");
                    String status = rs.getString("STATUS");
                    String paymentMode = rs.getString("PAYM");
                    Timestamp dojTimestamp = rs.getTimestamp("DOJ");
                    String dojFormatted = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(dojTimestamp);
                    String trainNo = rs.getString("TRAINO");
                    String trainName = rs.getString("TRAINAME");
                    model.addRow(new Object[]{name, age, classType, gender, status, paymentMode, trainNo, trainName, dojFormatted});
                } while (rs.next());
    
                JTable table = new JTable(model);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                table.getColumnModel().getColumn(0).setPreferredWidth(150);  
                table.getColumnModel().getColumn(1).setPreferredWidth(50);   
                table.getColumnModel().getColumn(2).setPreferredWidth(100);  
                table.getColumnModel().getColumn(3).setPreferredWidth(100);  
                table.getColumnModel().getColumn(4).setPreferredWidth(100);  
                table.getColumnModel().getColumn(5).setPreferredWidth(150);  
                table.getColumnModel().getColumn(6).setPreferredWidth(200);  
                table.getColumnModel().getColumn(7).setPreferredWidth(100);  
                table.getColumnModel().getColumn(8).setPreferredWidth(150);  
    
                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
                scrollPane.setBounds(50, 150, 1200, 400);  
                pnrFrame.add(scrollPane);
                pnrFrame.revalidate();
            } else {
                JOptionPane.showMessageDialog(pnrFrame, "Not a Valid PNR! " + pnrNumber, "Error", JOptionPane.ERROR_MESSAGE);
            }
    
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(pnrFrame, "Error fetching data for PNR " + pnrNumber, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new KynRailways();
            }
        });
    }
}
