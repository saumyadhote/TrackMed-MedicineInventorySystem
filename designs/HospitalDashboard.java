import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

// Medicine class with stock monitoring
class Medicine {
    private String name;
    private int stock;
    private int lowStockThreshold;

    public Medicine(String name, int stock, int lowStockThreshold) {
        this.name = name;
        this.stock = stock;
        this.lowStockThreshold = lowStockThreshold;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
        checkLowStock();
    }

    private void checkLowStock() {
        if (stock <= lowStockThreshold) {
            notifySupplier();
        }
    }

    private void notifySupplier() {
        // Simple popup notification
        JOptionPane.showMessageDialog(null,
            "Stock of " + name + " is low (" + stock + " units). Notify supplier!");
        // TODO: Replace with email/SMS notification
    }
}

// Panel to browse medicines
class BrowseMedicinesPanel extends JPanel {
    private List<Medicine> medicines;
    private List<JLabel> stockLabels;

    public BrowseMedicinesPanel() {
        medicines = new ArrayList<>();
        stockLabels = new ArrayList<>();

        // Sample medicines
        medicines.add(new Medicine("Paracetamol", 50, 10));
        medicines.add(new Medicine("Amoxicillin", 30, 5));
        medicines.add(new Medicine("Ibuprofen", 20, 5));

        setLayout(new GridLayout(medicines.size(), 2, 5, 5));

        for (Medicine med : medicines) {
            JLabel nameLabel = new JLabel(med.getName() + " - Stock: " + med.getStock());
            stockLabels.add(nameLabel);

            JButton buyButton = new JButton("Buy 1");
            buyButton.addActionListener(e -> {
                if (med.getStock() > 0) {
                    med.setStock(med.getStock() - 1);
                    nameLabel.setText(med.getName() + " - Stock: " + med.getStock());
                } else {
                    JOptionPane.showMessageDialog(null, med.getName() + " is out of stock!");
                }
            });

            add(nameLabel);
            add(buyButton);
        }
    }
}

// Placeholder panels
class ShoppingCartPanel extends JPanel {
    public ShoppingCartPanel() {
        add(new JLabel("Shopping Cart Panel"));
    }
}

class OrderHistoryPanel extends JPanel {
    public OrderHistoryPanel() {
        add(new JLabel("Order History Panel"));
    }
}

class ReportsPanel extends JPanel {
    public ReportsPanel() {
        add(new JLabel("Reports Panel"));
    }
}

// Main Dashboard
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
        SwingUtilities.invokeLater(() -> new HospitalDashboard().setVisible(true));
    }
}
