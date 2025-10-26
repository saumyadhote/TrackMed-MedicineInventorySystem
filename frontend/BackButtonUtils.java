package frontend;

import java.awt.*;
import javax.swing.*;

public class BackButtonUtils {
    public static JPanel createTopPanel(String title, JFrame currentFrame, Runnable onBack) {
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        
        // Back button
        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> {
            if (currentFrame != null) {
                currentFrame.dispose();
            }
            if (onBack != null) {
                onBack.run();
            }
        });
        
        // Title
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        
        // Layout
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonWrapper.add(backBtn);
        
        topPanel.add(buttonWrapper, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        return topPanel;
    }
}