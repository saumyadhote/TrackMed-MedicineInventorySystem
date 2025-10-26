import java.awt.*;
import javax.swing.*;

public class NotificationManager {
    private static NotificationManager instance;
    private JFrame parentFrame;

    private NotificationManager() {}

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void setParentFrame(JFrame frame) {
        this.parentFrame = frame;
    }

    public void showNotification(String message, NotificationType type) {
        if (parentFrame == null) return;

        JDialog dialog = new JDialog(parentFrame, "Notification", false);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Create notification panel
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Icon based on type
        ImageIcon icon = getIconForType(type);
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            panel.add(iconLabel, BorderLayout.WEST);
        }
        
        // Message
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(messageLabel, BorderLayout.CENTER);
        
        // Add to dialog
        dialog.add(panel, BorderLayout.CENTER);
        
        // Style based on type
        panel.setBackground(getBackgroundForType(type));
        messageLabel.setForeground(getForegroundForType(type));
        
        // Size and position
        dialog.pack();
        dialog.setSize(dialog.getWidth() + 50, dialog.getHeight()); // Add some padding
        
        // Position at top-right corner
        Rectangle parentBounds = parentFrame.getBounds();
        int x = parentBounds.x + parentBounds.width - dialog.getWidth() - 20;
        int y = parentBounds.y + 40;
        dialog.setLocation(x, y);
        
        // Show and auto-hide
        dialog.setVisible(true);
        
        // Auto-hide after 5 seconds
        Timer timer = new Timer(5000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
    }

    private Color getBackgroundForType(NotificationType type) {
        switch (type) {
            case WARNING: return new Color(255, 243, 205);
            case ERROR: return new Color(248, 215, 218);
            case SUCCESS: return new Color(212, 237, 218);
            default: return new Color(229, 246, 253);
        }
    }

    private Color getForegroundForType(NotificationType type) {
        switch (type) {
            case WARNING: return new Color(133, 100, 4);
            case ERROR: return new Color(114, 28, 36);
            case SUCCESS: return new Color(21, 87, 36);
            default: return new Color(12, 84, 96);
        }
    }

    private ImageIcon getIconForType(NotificationType type) {
        String iconPath = switch (type) {
            case WARNING -> "/resources/icons/warning.png";
            case ERROR -> "/resources/icons/error.png";
            case SUCCESS -> "/resources/icons/success.png";
            default -> "/resources/icons/info.png";
        };

        try {
            return new ImageIcon(getClass().getResource(iconPath));
        } catch (Exception e) {
            return null;
        }
    }

    public enum NotificationType {
        INFO,
        WARNING,
        ERROR,
        SUCCESS
    }
}