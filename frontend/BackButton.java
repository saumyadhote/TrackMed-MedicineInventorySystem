package frontend;

import java.awt.*;
import javax.swing.*;

public class BackButton extends JButton {
    public BackButton(JFrame currentFrame, Runnable onBack) {
        super("← Back");
        addActionListener(e -> {
            if (currentFrame != null) {
                currentFrame.dispose();
            }
            if (onBack != null) {
                onBack.run();
            }
        });
        setPreferredSize(new Dimension(100, 30));
    }
}