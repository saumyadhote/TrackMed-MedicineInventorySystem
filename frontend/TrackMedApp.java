import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TrackMedApp {
    private static DefaultTableModel sharedModel;
    private static LowStockMonitor stockMonitor;

    public static DefaultTableModel getSharedModel() {
        return sharedModel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] columnNames = {"Medication ID", "Name", "Expiry Date", "Quantity", "Reorder Threshold"};
            Object[][] data = {};
            sharedModel = new DefaultTableModel(data, columnNames);
            new LoginFrame();
        });
    }

    // login
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

            final JFrame self = this;
            loginButton.addActionListener(e -> {
                String user = usernameField.getText();
                String pass = new String(passwordField.getPassword());

                if (user.equals("user") && pass.equals("1234")) {
                    JOptionPane.showMessageDialog(self, "Login Successful!");
                    Navigation.push(self);
                    self.dispose();
                    new UserDashboardFrame();
                } else if (user.equals("customer") && pass.equals("5555")) {
                    JOptionPane.showMessageDialog(self, "Customer Login Successful!");
                    Navigation.push(self);
                    self.dispose();
                    new CustomerViewFrame();
                } else {
                    JOptionPane.showMessageDialog(self, "Invalid Credentials");
                }
            });
        }
    }

    // user
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
            String[] btnNames = {"Browse Medicines", "My Cart", "Order History", "Logout"};
            Dimension btnSize = new Dimension(200, 50);

            final JFrame self = this;

            for (String name : btnNames) {
                JButton btn = new JButton(name);
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                btn.setMaximumSize(btnSize);
                sidePanel.add(btn);
                sidePanel.add(Box.createRigidArea(new Dimension(0, 5)));

                if (name.equals("Browse Medicines")) {
                    btn.addActionListener(e -> {
                        Navigation.push(self);
                        self.dispose();
                        new MainAppFrame();
                    });
                }

                if (name.equals("Logout")) {
                    btn.addActionListener(e -> {
                        Navigation.clear();
                        self.dispose();
                        new LoginFrame();
                    });
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

            // Add back button
            JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton backBtn = new JButton("← Back");
            backBtn.setPreferredSize(new Dimension(100, 30));
            backBtn.addActionListener(e -> {
                dispose();
                new UserDashboardFrame();
            });
            topBar.add(backBtn);
            add(topBar, BorderLayout.NORTH);

            // Top panel with title and back button
            JPanel topPanel = new JPanel(new BorderLayout(5, 5));
            JButton backBtn = new JButton("Back");
            backBtn.setPreferredSize(new Dimension(80, 30));
            backBtn.addActionListener(e -> {
                dispose();
                new UserDashboardFrame();
            });
            
            JLabel titleLabel = new JLabel("Welcome to TrackMed", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
            
            topPanel.add(backBtn, BorderLayout.WEST);
            topPanel.add(titleLabel, BorderLayout.CENTER);
            add(topPanel, BorderLayout.NORTH);

            model = sharedModel;
            table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            JButton viewBtn = new JButton("View Inventory");
            JButton addBtn = new JButton("Add New");
            JButton editBtn = new JButton("Edit");
            JButton deleteBtn = new JButton("Delete");

            Dimension btnSize = new Dimension(130, 30);
            viewBtn.setPreferredSize(btnSize);
            addBtn.setPreferredSize(btnSize);
            editBtn.setPreferredSize(btnSize);
            deleteBtn.setPreferredSize(btnSize);

            buttonPanel.add(viewBtn);
            buttonPanel.add(addBtn);
            buttonPanel.add(editBtn);
            buttonPanel.add(deleteBtn);

            JButton backBtn = new JButton("Back");
            backBtn.setPreferredSize(btnSize);
            buttonPanel.add(backBtn);
            add(buttonPanel, BorderLayout.SOUTH);

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
                model.addRow(new Object[]{
                    medId.getText(), name.getText(), expiry.getText(), qty.getText(), reorder.getText()
                });
            }

            // Add back button
            JButton backBtn = new JButton("Back");
            backBtn.setPreferredSize(new Dimension(130, 30));
            buttonPanel.add(backBtn);
            backBtn.addActionListener(e -> {
                dispose();
                new LoginFrame();
            });
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

    // ---------------- CUSTOMER VIEW ----------------
    static class CustomerViewFrame extends JFrame {
        private DefaultTableModel localModel;
        private JTable table;
        private LowStockMonitor stockMonitor;
        private JButton addBtn;
        private JButton editBtn;
        private JButton saveBtn;
        private JButton backBtn;

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
                try {
                    int quantity = Integer.parseInt(qty.getText());
                    int threshold = Integer.parseInt(reorder.getText());
                    
                    localModel.addRow(new Object[]{
                        medId.getText(), name.getText(), expiry.getText(),
                        quantity, threshold
                    });

                    // Check if this new item is low on stock
                    stockMonitor.checkSingleItem(name.getText(), quantity, threshold);
                    
                    // Update shared model
                    sharedModel.addRow(new Object[]{
                        medId.getText(), name.getText(), expiry.getText(),
                        quantity, threshold
                    });
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Please enter valid numbers for Quantity and Reorder Threshold",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void editEntry() {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an item to edit");
                return;
            }

            JTextField medId = new JTextField(localModel.getValueAt(selectedRow, 0).toString());
            JTextField name = new JTextField(localModel.getValueAt(selectedRow, 1).toString());
            JTextField expiry = new JTextField(localModel.getValueAt(selectedRow, 2).toString());
            JTextField qty = new JTextField(localModel.getValueAt(selectedRow, 3).toString());
            JTextField reorder = new JTextField(localModel.getValueAt(selectedRow, 4).toString());

            Object[] fields = {
                "Medication ID:", medId,
                "Name:", name,
                "Expiry Date:", expiry,
                "Quantity:", qty,
                "Reorder Threshold:", reorder
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Edit Medication", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int quantity = Integer.parseInt(qty.getText());
                    int threshold = Integer.parseInt(reorder.getText());
                    
                    // Update local model
                    localModel.setValueAt(medId.getText(), selectedRow, 0);
                    localModel.setValueAt(name.getText(), selectedRow, 1);
                    localModel.setValueAt(expiry.getText(), selectedRow, 2);
                    localModel.setValueAt(quantity, selectedRow, 3);
                    localModel.setValueAt(threshold, selectedRow, 4);

                    // Check stock level after edit
                    stockMonitor.checkSingleItem(name.getText(), quantity, threshold);
                    
                    // Update shared model
                    for (int i = 0; i < sharedModel.getRowCount(); i++) {
                        if (sharedModel.getValueAt(i, 0).equals(medId.getText())) {
                            sharedModel.setValueAt(medId.getText(), i, 0);
                            sharedModel.setValueAt(name.getText(), i, 1);
                            sharedModel.setValueAt(expiry.getText(), i, 2);
                            sharedModel.setValueAt(quantity, i, 3);
                            sharedModel.setValueAt(threshold, i, 4);
                            break;
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Please enter valid numbers for Quantity and Reorder Threshold",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        public CustomerViewFrame() {
            setTitle("TrackMed - Customer Inventory View");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 600);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            // Initialize the stock monitor
            this.stockMonitor = new LowStockMonitor(this);
            
            // Initialize model with editability control
            this.localModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 3; // Only quantity is directly editable
                }
            };

            // Set up columns
            for (int col = 0; col < sharedModel.getColumnCount(); col++) {
                localModel.addColumn(sharedModel.getColumnName(col));
            }

            // Copy data from shared model
            for (int row = 0; row < sharedModel.getRowCount(); row++) {
                Object[] rowData = new Object[sharedModel.getColumnCount()];
                for (int col = 0; col < sharedModel.getColumnCount(); col++) {
                    rowData[col] = sharedModel.getValueAt(row, col);
                }
                localModel.addRow(rowData);
            }

            // Create and set up the table
            table = new JTable(localModel);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            // Cleanup and remove old methods since functionality is now in button listeners
            
            // Create action buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            addBtn = new JButton("Add New Item");
            editBtn = new JButton("Edit Selected");
            saveBtn = new JButton("Save All Changes");
            backBtn = new JButton("← Back");

            // Set common button size
            addBtn.setPreferredSize(new Dimension(130, 30));
            editBtn.setPreferredSize(new Dimension(130, 30));
            saveBtn.setPreferredSize(new Dimension(130, 30));
            backBtn.setPreferredSize(new Dimension(130, 30));

            buttonPanel.add(backBtn);
            buttonPanel.add(addBtn);
            buttonPanel.add(editBtn);
            buttonPanel.add(saveBtn);
            add(buttonPanel, BorderLayout.SOUTH);

            Dimension btnSize = new Dimension(130, 30);
            addBtn.setPreferredSize(btnSize);
            editBtn.setPreferredSize(btnSize);
            saveBtn.setPreferredSize(btnSize);
            backBtn.setPreferredSize(btnSize);

            buttonPanel.add(backBtn);
            buttonPanel.add(addBtn);
            buttonPanel.add(editBtn);
            buttonPanel.add(saveBtn);
            add(buttonPanel, BorderLayout.SOUTH);

            // Add button actions
            backBtn.addActionListener(e -> {
                dispose();
                EventQueue.invokeLater(() -> {
                    LoginFrame frame = new LoginFrame();
                    frame.setVisible(true);
                });
            });

            addBtn.addActionListener(e -> {
                JDialog dialog = new JDialog(this, "Add New Medicine", true);
                dialog.setLayout(new GridLayout(6, 2, 5, 5));
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(this);

                // Create input fields
                JTextField nameField = new JTextField();
                JTextField categoryField = new JTextField();
                JTextField priceField = new JTextField();
                JTextField quantityField = new JTextField();
                JTextField thresholdField = new JTextField();

                // Add components to dialog
                dialog.add(new JLabel("Name:"));
                dialog.add(nameField);
                dialog.add(new JLabel("Category:"));
                dialog.add(categoryField);
                dialog.add(new JLabel("Price:"));
                dialog.add(priceField);
                dialog.add(new JLabel("Quantity:"));
                dialog.add(quantityField);
                dialog.add(new JLabel("Threshold:"));
                dialog.add(thresholdField);

                JButton submitBtn = new JButton("Add");
                submitBtn.addActionListener(evt -> {
                    try {
                        // Validate inputs
                        String name = nameField.getText().trim();
                        String category = categoryField.getText().trim();
                        double price = Double.parseDouble(priceField.getText().trim());
                        int quantity = Integer.parseInt(quantityField.getText().trim());
                        int threshold = Integer.parseInt(thresholdField.getText().trim());

                        if (name.isEmpty() || category.isEmpty()) {
                            throw new IllegalArgumentException("Name and category cannot be empty");
                        }

                        // Generate new ID
                        String newId = "MED" + (localModel.getRowCount() + 1);

                        // Add to local model
                        localModel.addRow(new Object[]{newId, name, category, quantity, threshold, price});

                        // Add to shared model
                        sharedModel.addRow(new Object[]{newId, name, category, quantity, threshold, price});

                        // Check stock level
                        stockMonitor.checkSingleItem(name, quantity, threshold);

                        dialog.dispose();
                        NotificationManager.getInstance().showNotification(
                            "New medicine added successfully!",
                            NotificationManager.NotificationType.SUCCESS
                        );
                    } catch (NumberFormatException ex) {
                        NotificationManager.getInstance().showNotification(
                            "Please enter valid numbers for price, quantity, and threshold",
                            NotificationManager.NotificationType.ERROR
                        );
                    } catch (IllegalArgumentException ex) {
                        NotificationManager.getInstance().showNotification(
                            ex.getMessage(),
                            NotificationManager.NotificationType.ERROR
                        );
                    }
                });

                dialog.add(submitBtn);
                dialog.add(new JButton("Cancel") {{ addActionListener(e -> dialog.dispose()); }});
                dialog.setVisible(true);
            });

            editBtn.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    NotificationManager.getInstance().showNotification(
                        "Please select a medicine to edit",
                        NotificationManager.NotificationType.WARNING
                    );
                    return;
                }

                JDialog dialog = new JDialog(this, "Edit Medicine", true);
                dialog.setLayout(new GridLayout(6, 2, 5, 5));
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(this);

                // Get current values
                String currentId = table.getValueAt(selectedRow, 0).toString();
                String currentName = table.getValueAt(selectedRow, 1).toString();
                String currentCategory = table.getValueAt(selectedRow, 2).toString();
                int currentQuantity = Integer.parseInt(table.getValueAt(selectedRow, 3).toString());
                int currentThreshold = Integer.parseInt(table.getValueAt(selectedRow, 4).toString());
                double currentPrice = Double.parseDouble(table.getValueAt(selectedRow, 5).toString());

                // Create input fields with current values
                JTextField nameField = new JTextField(currentName);
                JTextField categoryField = new JTextField(currentCategory);
                JTextField priceField = new JTextField(String.valueOf(currentPrice));
                JTextField quantityField = new JTextField(String.valueOf(currentQuantity));
                JTextField thresholdField = new JTextField(String.valueOf(currentThreshold));

                // Add components to dialog
                dialog.add(new JLabel("Name:"));
                dialog.add(nameField);
                dialog.add(new JLabel("Category:"));
                dialog.add(categoryField);
                dialog.add(new JLabel("Price:"));
                dialog.add(priceField);
                dialog.add(new JLabel("Quantity:"));
                dialog.add(quantityField);
                dialog.add(new JLabel("Threshold:"));
                dialog.add(thresholdField);

                JButton submitBtn = new JButton("Save");
                submitBtn.addActionListener(evt -> {
                    try {
                        // Validate inputs
                        String name = nameField.getText().trim();
                        String category = categoryField.getText().trim();
                        double price = Double.parseDouble(priceField.getText().trim());
                        int quantity = Integer.parseInt(quantityField.getText().trim());
                        int threshold = Integer.parseInt(thresholdField.getText().trim());

                        if (name.isEmpty() || category.isEmpty()) {
                            throw new IllegalArgumentException("Name and category cannot be empty");
                        }

                        // Update local model
                        table.setValueAt(name, selectedRow, 1);
                        table.setValueAt(category, selectedRow, 2);
                        table.setValueAt(quantity, selectedRow, 3);
                        table.setValueAt(threshold, selectedRow, 4);
                        table.setValueAt(price, selectedRow, 5);

                        // Find and update in shared model
                        for (int i = 0; i < sharedModel.getRowCount(); i++) {
                            if (sharedModel.getValueAt(i, 0).equals(currentId)) {
                                sharedModel.setValueAt(name, i, 1);
                                sharedModel.setValueAt(category, i, 2);
                                sharedModel.setValueAt(quantity, i, 3);
                                sharedModel.setValueAt(threshold, i, 4);
                                sharedModel.setValueAt(price, i, 5);
                                break;
                            }
                        }

                        // Check stock level
                        stockMonitor.checkSingleItem(name, quantity, threshold);

                        dialog.dispose();
                        NotificationManager.getInstance().showNotification(
                            "Medicine updated successfully!",
                            NotificationManager.NotificationType.SUCCESS
                        );
                    } catch (NumberFormatException ex) {
                        NotificationManager.getInstance().showNotification(
                            "Please enter valid numbers for price, quantity, and threshold",
                            NotificationManager.NotificationType.ERROR
                        );
                    } catch (IllegalArgumentException ex) {
                        NotificationManager.getInstance().showNotification(
                            ex.getMessage(),
                            NotificationManager.NotificationType.ERROR
                        );
                    }
                });

                dialog.add(submitBtn);
                dialog.add(new JButton("Cancel") {{ addActionListener(e -> dialog.dispose()); }});
                dialog.setVisible(true);
            });

            saveBtn.addActionListener(e -> {
                // Update shared model and check stock levels
                for (int row = 0; row < localModel.getRowCount(); row++) {
                    String medId = localModel.getValueAt(row, 0).toString();
                    String name = localModel.getValueAt(row, 1).toString();
                    int quantity = Integer.parseInt(localModel.getValueAt(row, 3).toString());
                    int threshold = Integer.parseInt(localModel.getValueAt(row, 4).toString());

                    // Update in shared model
                    for (int i = 0; i < sharedModel.getRowCount(); i++) {
                        if (sharedModel.getValueAt(i, 0).equals(medId)) {
                            for (int col = 0; col < sharedModel.getColumnCount(); col++) {
                                sharedModel.setValueAt(localModel.getValueAt(row, col), i, col);
                            }
                            break;
                        }
                    }

                    // Check stock level
                    stockMonitor.checkSingleItem(name, quantity, threshold);
                }

                NotificationManager.getInstance().showNotification(
                    "All changes saved successfully!",
                    NotificationManager.NotificationType.SUCCESS
                );
            });

            // Start monitoring stock levels
            stockMonitor.start();

            // Add back button
            JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton backBtn = new JButton("← Back");
            backBtn.setPreferredSize(new Dimension(100, 30));
            backBtn.addActionListener(e -> {
                dispose();
                new LoginFrame();
            });
            topBar.add(backBtn);
            add(topBar, BorderLayout.NORTH);

            JLabel titleLabel = new JLabel("Inventory View - Customer Access", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
            add(titleLabel, BorderLayout.NORTH);

            setVisible(true);
        }
    }
}
