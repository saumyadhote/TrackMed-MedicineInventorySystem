import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// Main Hospital Dashboard
public class HospitalDashboard extends JFrame {

    private JPanel contentPanel; // center panel to switch views

    public HospitalDashboard() {
        setTitle("Hospital Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Top menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> System.exit(0));
        menuFile.add(logoutItem);
        menuBar.add(menuFile);
        setJMenuBar(menuBar);

        // Left navigation panel
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(6, 1, 5, 5));
        JButton btnBrowse = new JButton("Browse Medicines");
        JButton btnCart = new JButton("Shopping Cart");
        JButton btnOrders = new JButton("Order History");
        JButton btnReports = new JButton("Reports");

        navPanel.add(btnBrowse);
        navPanel.add(btnCart);
        navPanel.add(btnOrders);
        navPanel.add(btnReports);

        // Center content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // Main layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(navPanel, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        // Panel switching actions
        btnBrowse.addActionListener(e -> switchPanel(new BrowseMedicinesPanel()));
        btnCart.addActionListener(e -> switchPanel(new ShoppingCartPanel()));
        btnOrders.addActionListener(e -> switchPanel(new OrderHistoryPanel()));
        btnReports.addActionListener(e -> switchPanel(new ReportsPanel()));

        // Load default panel
        switchPanel(new BrowseMedicinesPanel());
    }

    private void switchPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HospitalDashboard().setVisible(true);
        });
    }
}
