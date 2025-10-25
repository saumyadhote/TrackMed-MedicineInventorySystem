import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class OrderHistoryPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilter;
    private int hospitalId; // replace with logged-in hospital ID

    public OrderHistoryPanel(int hospitalId) {
        this.hospitalId = hospitalId;
        setLayout(new BorderLayout());

        // Top panel for filter and refresh
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusFilter = new JComboBox<>(new String[]{"ALL", "PENDING", "APPROVED", "SHIPPED", "DELIVERED", "CANCELLED"});
        JButton refreshButton = new JButton("Refresh");
        topPanel.add(new JLabel("Filter by Status:"));
        topPanel.add(statusFilter);
        topPanel.add(refreshButton);
        add(topPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"Order ID", "Date", "Status", "Total", "Supplier"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make table non-editable
            }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewDetailsButton = new JButton("View Details");
        bottomPanel.add(viewDetailsButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load initial data
        loadOrders("ALL");

        // Action listeners
        refreshButton.addActionListener(e -> loadOrders((String) statusFilter.getSelectedItem()));

        viewDetailsButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int orderId = (int) tableModel.getValueAt(selectedRow, 0);
                new OrderDetailsDialog(SwingUtilities.getWindowAncestor(this), orderId).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an order first!");
            }
        });
    }

    private void loadOrders(String status) {
        tableModel.setRowCount(0); // clear table
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT o.order_id, o.order_date, o.order_status, o.total_amount, u.full_name AS supplier " +
                         "FROM orders o JOIN users u ON o.supplier_id = u.user_id " +
                         "WHERE o.hospital_id = ?";
            if (!status.equals("ALL")) sql += " AND o.order_status = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, hospitalId);
            if (!status.equals("ALL")) ps.setString(2, status);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("order_id"));
                row.add(rs.getTimestamp("order_date"));
                row.add(rs.getString("order_status"));
                row.add(rs.getDouble("total_amount"));
                row.add(rs.getString("supplier"));
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading orders from database.");
        }
    }
}
