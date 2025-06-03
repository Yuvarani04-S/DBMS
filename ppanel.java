import javax.swing.*;
import java.awt.*;
public class ppanel {
    ppanel(JFrame fr){
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon("C:/Users/Karthick/Downloads/TRAIN_MANIA_YOU_TUBE_CHANNEL_LOGO__1_-removebg-preview-fotor-202412042117.png");
                Image backgroundImage = backgroundIcon.getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        
        panel.setLayout(null);  
        fr.setContentPane(panel);
    }
}
