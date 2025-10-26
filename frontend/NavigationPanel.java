import java.awt.*;
import javax.swing.*;

public class NavigationPanel extends JPanel {
    @FunctionalInterface
    public interface BackAction {
        void goBack();
    }

    public NavigationPanel(String backButtonText, BackAction onBack) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton backBtn = new JButton(backButtonText);
        backBtn.setPreferredSize(new Dimension(130, 30));
        add(backBtn);
        
        backBtn.addActionListener(e -> onBack.goBack());
    }

    public NavigationPanel(BackAction onBack) {
        this("Back", onBack);
    }
}