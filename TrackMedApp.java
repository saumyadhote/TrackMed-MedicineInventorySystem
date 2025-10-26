
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TrackMedApp {

    private static DefaultTableModel sharedModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] columnNames = {"Medication ID", "Name", "Expiry Date", "Quantity", "Reorder Threshold"};
            Object[][] data = {};
            sharedModel = new DefaultTableModel(data, columnNames);
            new LoginFrame();
        });
    }

    // ---------------- LOGIN SCREEN ----------------
    static class LoginFrame extends JFrame {

        public LoginFrame() {
            setTitle("TrackMed - Login");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 300);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel titleLabel = new JLabel("Login Here", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(titleLabel, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            gbc.gridx = 0;
            panel.add(new JLabel("Username:"), gbc);
            JTextField usernameField = new JTextField(15);
            gbc.gridx = 1;
            panel.add(usernameField, gbc);

            gbc.gridy = 2;
            gbc.gridx = 0;
            panel.add(new JLabel("Password:"), gbc);
            JPasswordField passwordField = new JPasswordField(15);
            gbc.gridx = 1;
            panel.add(passwordField, gbc);

            JButton loginButton = new JButton("Login");
            loginButton.setPreferredSize(new Dimension(80, 25));
            gbc.gridy = 3;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(loginButton, gbc);

            add(panel);
            setVisible(true);

            loginButton.addActionListener(e -> {
                String user = usernameField.getText();
                String pass = new String(passwordField.getPassword());

                if (user.equals("user") && pass.equals("1234")) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    dispose();
                    new UserDashboardFrame();
                } else if (user.equals("customer") && pass.equals("5555")) {
                    JOptionPane.showMessageDialog(this, "Customer Login Successful!");
                    dispose();
                    new CustomerViewFrame();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Credentials");
                }
            });
        }
    }

    // ---------------- USER DASHBOARD ----------------
    static class UserDashboardFrame extends JFrame {

        public UserDashboardFrame() {
            setTitle("TrackMed - Dashboard");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(900, 600);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            // Menu Bar
            JMenuBar menuBar = new JMenuBar();
            String[] menuNames = {"Dashboard", "Medicines", "Orders", "Reports"};
            for (String name : menuNames) {
                JMenu menu = new JMenu(name);
                menuBar.add(menu);
            }
            setJMenuBar(menuBar);

            // Side Panel
            JPanel sidePanel = new JPanel();
            sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
            String[] btnNames = {"Browse Medicines", "My Cart", "Order History", "Reports", "Logout"};
            Dimension btnSize = new Dimension(200, 50);

            for (String name : btnNames) {
                JButton btn = new JButton(name);
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                btn.setMaximumSize(btnSize);
                sidePanel.add(btn);
                sidePanel.add(Box.createRigidArea(new Dimension(0, 5)));

                switch (name) {
                    case "Browse Medicines":
                        btn.addActionListener(e -> {
                            dispose();
                            new MainAppFrame();
                        });
                        break;
                    case "My Cart":
                        btn.addActionListener(e -> {
                            dispose();
                            new MyCartFrame();
                        });
                        break;
                    case "Order History":
                        btn.addActionListener(e -> {
                            dispose();
                            new OrderHistoryFrame();
                        });
                        break;
                    case "Reports":
                        btn.addActionListener(e -> {
                            dispose();
                            new ReportsFrame();
                        });
                        break;
                    case "Logout":
                        btn.addActionListener(e -> {
                            dispose();
                            new LoginFrame();
                        });
                        break;
                }
            }

            add(sidePanel, BorderLayout.WEST);

            JPanel center = new JPanel();
            center.setBackground(Color.WHITE);
            add(center, BorderLayout.CENTER);

            setVisible(true);
        }
    }

    // ---------------- MAIN APP (INVENTORY) ----------------
    static class MainAppFrame extends JFrame {

        private JTable table;
        private DefaultTableModel model;

        public MainAppFrame() {
            setTitle("TrackMed - Inventory Management");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(700, 500);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            JLabel titleLabel = new JLabel("Welcome to TrackMed", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
            add(titleLabel, BorderLayout.NORTH);

            model = sharedModel;
            table = new JTable(model);
            add(new JScrollPane(table), BorderLayout.CENTER);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            JButton viewBtn = new JButton("View Inventory");
            JButton addBtn = new JButton("Add New");
            JButton editBtn = new JButton("Edit");
            JButton deleteBtn = new JButton("Delete");
            JButton backBtn = new JButton("Back to Dashboard");

            Dimension btnSize = new Dimension(130, 30);
            viewBtn.setPreferredSize(btnSize);
            addBtn.setPreferredSize(btnSize);
            editBtn.setPreferredSize(btnSize);
            deleteBtn.setPreferredSize(btnSize);
            backBtn.setPreferredSize(new Dimension(150, 30));

            buttonPanel.add(viewBtn);
            buttonPanel.add(addBtn);
            buttonPanel.add(editBtn);
            buttonPanel.add(deleteBtn);
            buttonPanel.add(backBtn);

            add(buttonPanel, BorderLayout.SOUTH);

            // Button actions
            viewBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Displaying full inventory..."));
            addBtn.addActionListener(e -> addNewEntry());
            editBtn.addActionListener(e -> editEntry());
            deleteBtn.addActionListener(e -> deleteEntry());
            backBtn.addActionListener(e -> {
                dispose();
                new UserDashboardFrame();
            });

            setVisible(true);
        }

        private void addNewEntry() {
            JTextField medId = new JTextField();
            JTextField name = new JTextField();
            JTextField expiry = new JTextField();
            JTextField qty = new JTextField();
            JTextField reorder = new JTextField();

            Object[] fields = {
                "Medication ID:", medId,
                "Name:", name,
                "Expiry Date:", expiry,
                "Quantity:", qty,
                "Reorder Threshold:", reorder
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Add New Medication", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                model.addRow(new Object[]{medId.getText(), name.getText(), expiry.getText(), qty.getText(), reorder.getText()});
            }
        }

        private void editEntry() {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a row to edit.");
                return;
            }

            JTextField medId = new JTextField(model.getValueAt(selectedRow, 0).toString());
            JTextField name = new JTextField(model.getValueAt(selectedRow, 1).toString());
            JTextField expiry = new JTextField(model.getValueAt(selectedRow, 2).toString());
            JTextField qty = new JTextField(model.getValueAt(selectedRow, 3).toString());
            JTextField reorder = new JTextField(model.getValueAt(selectedRow, 4).toString());

            Object[] fields = {
                "Medication ID:", medId,
                "Name:", name,
                "Expiry Date:", expiry,
                "Quantity:", qty,
                "Reorder Threshold:", reorder
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Edit Medication", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                model.setValueAt(medId.getText(), selectedRow, 0);
                model.setValueAt(name.getText(), selectedRow, 1);
                model.setValueAt(expiry.getText(), selectedRow, 2);
                model.setValueAt(qty.getText(), selectedRow, 3);
                model.setValueAt(reorder.getText(), selectedRow, 4);
            }
        }

        private void deleteEntry() {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a row to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Delete selected record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.removeRow(selectedRow);
            }
        }
    }

    // ---------------- MY CART ----------------
    static class MyCartFrame extends JFrame {

        public MyCartFrame() {
            setTitle("TrackMed - My Cart");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(700, 500);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            JLabel titleLabel = new JLabel("My Cart", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
            add(titleLabel, BorderLayout.NORTH);

            JTable table = new JTable(new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Medication ID", "Name", "Quantity", "Price"}
            ));
            add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JButton checkoutBtn = new JButton("Checkout");
            JButton backBtn = new JButton("Back to Dashboard");
            bottomPanel.add(checkoutBtn);
            bottomPanel.add(backBtn);
            add(bottomPanel, BorderLayout.SOUTH);

            backBtn.addActionListener(e -> {
                dispose();
                new UserDashboardFrame();
            });

            setVisible(true);
        }
    }

    // ---------------- ORDER HISTORY ----------------
    static class OrderHistoryFrame extends JFrame {

        public OrderHistoryFrame() {
            setTitle("TrackMed - Order History");
            setSize(700, 500);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            JLabel titleLabel = new JLabel("Order History", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
            add(titleLabel, BorderLayout.NORTH);

            JTable table = new JTable(new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Order ID", "Date", "Status", "Total"}
            ));
            add(new JScrollPane(table), BorderLayout.CENTER);

            JButton backBtn = new JButton("Back to Dashboard");
            JPanel bottomPanel = new JPanel();
            bottomPanel.add(backBtn);
            add(bottomPanel, BorderLayout.SOUTH);

            backBtn.addActionListener(e -> {
                dispose();
                new UserDashboardFrame();
            });

            setVisible(true);
        }
    }

    // ---------------- REPORTS ----------------
    static class ReportsFrame extends JFrame {

        public ReportsFrame() {
            setTitle("TrackMed - Reports");
            setSize(700, 500);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            JLabel titleLabel = new JLabel("Reports", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
            add(titleLabel, BorderLayout.NORTH);

            JTextArea reportArea = new JTextArea("Reports will be displayed here...");
            reportArea.setEditable(false);
            add(new JScrollPane(reportArea), BorderLayout.CENTER);

            JButton backBtn = new JButton("Back to Dashboard");
            JPanel bottomPanel = new JPanel();
            bottomPanel.add(backBtn);
            add(bottomPanel, BorderLayout.SOUTH);

            backBtn.addActionListener(e -> {
                dispose();
                new UserDashboardFrame();
            });

            setVisible(true);
        }
    }

    // ---------------- CUSTOMER VIEW ----------------
    static class CustomerViewFrame extends JFrame {

        public CustomerViewFrame() {
            setTitle("TrackMed - Customer Inventory View");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(700, 500);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            JLabel titleLabel = new JLabel("Inventory View - Customer Access", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
            add(titleLabel, BorderLayout.NORTH);

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 3; // Only quantity editable
                }
            };

            // Copy columns from shared model
            for (int col = 0; col < sharedModel.getColumnCount(); col++) {
                model.addColumn(sharedModel.getColumnName(col));
            }

            // Copy data from shared model
            for (int row = 0; row < sharedModel.getRowCount(); row++) {
                Object[] rowData = new Object[sharedModel.getColumnCount()];
                for (int col = 0; col < sharedModel.getColumnCount(); col++) {
                    rowData[col] = sharedModel.getValueAt(row, col);
                }
                model.addRow(rowData);
            }

            JTable table = new JTable(model);

            // Low stock highlighting
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    try {
                        int quantity = Integer.parseInt(table.getValueAt(row, 3).toString());
                        if (quantity <= 5) { // low stock threshold
                            c.setBackground(Color.PINK);
                        } else {
                            c.setBackground(Color.WHITE);
                        }
                    } catch (NumberFormatException ex) {
                        c.setBackground(Color.WHITE); // fallback if value not numeric
                    }
                    return c;
                }
            });

            add(new JScrollPane(table), BorderLayout.CENTER);

            // Bottom panel with buttons
            JButton saveBtn = new JButton("Save Quantity Changes");
            JButton requestRestockBtn = new JButton("Request Restock");
            JButton backBtn = new JButton("Back to Login");

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            bottomPanel.add(saveBtn);
            bottomPanel.add(requestRestockBtn);
            bottomPanel.add(backBtn);
            add(bottomPanel, BorderLayout.SOUTH);

            // Save quantity changes
            saveBtn.addActionListener(e -> {
                for (int row = 0; row < model.getRowCount(); row++) {
                    sharedModel.setValueAt(model.getValueAt(row, 3), row, 3);
                }
                JOptionPane.showMessageDialog(this, "Quantities updated successfully.");
            });

            // Request restock for low stock items
            requestRestockBtn.addActionListener(e -> {
                boolean restockSent = false;
                for (int row = 0; row < model.getRowCount(); row++) {
                    try {
                        int quantity = Integer.parseInt(model.getValueAt(row, 3).toString());
                        if (quantity <= 5) {
                            String medName = model.getValueAt(row, 0).toString();
                            JOptionPane.showMessageDialog(this, "Restock request sent for " + medName);
                            restockSent = true;
                            // TODO: Integrate backend/email API to notify supplier
                        }
                    } catch (NumberFormatException ex) {
                        // Ignore non-numeric cells
                    }
                }
                if (!restockSent) {
                    JOptionPane.showMessageDialog(this, "No items need restock.");
                }
            });

            // Back to login
            backBtn.addActionListener(e -> {
                dispose();
                new LoginFrame();
            });

            setVisible(true);
        }
    }

}
