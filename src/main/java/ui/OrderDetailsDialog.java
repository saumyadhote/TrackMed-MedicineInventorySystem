import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class OrderDetailsDialog extends JDialog {

    public OrderDetailsDialog(Window parent, int orderId) {
        super(parent, "Order Details - #" + orderId, ModalityType.APPLICATION_MODAL);
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Top panel: order info
        JPanel orderInfoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        add(orderInfoPanel, BorderLayout.NORTH);

        // Table for order items
        DefaultTableModel itemsModel = new DefaultTableModel(new String[]{"Medicine", "Quantity", "Unit Price", "Subtotal"}, 0);
        JTable itemsTable = new JTable(itemsModel);
        add(new JScrollPane(itemsTable), BorderLayout.CENTER);

        // Table for status history
        DefaultTableModel statusModel = new DefaultTableModel(new String[]{"Old Status", "New Status", "Changed By", "Date"}, 0);
        JTable statusTable = new JTable(statusModel);
        add(new JScrollPane(statusTable), BorderLayout.SOUTH);

        // Load data from database
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Order general info
            PreparedStatement psOrder = conn.prepareStatement(
                    "SELECT o.order_id, o.order_date, o.order_status, o.total_amount, h.full_name AS hospital, s.full_name AS supplier " +
                    "FROM orders o " +
                    "JOIN users h ON o.hospital_id = h.user_id " +
                    "JOIN users s ON o.supplier_id = s.user_id " +
                    "WHERE o.order_id = ?");
            psOrder.setInt(1, orderId);
            ResultSet rsOrder = psOrder.executeQuery();
            if (rsOrder.next()) {
                orderInfoPanel.add(new JLabel("Order ID:"));
                orderInfoPanel.add(new JLabel(String.valueOf(rsOrder.getInt("order_id"))));
                orderInfoPanel.add(new JLabel("Date:"));
                orderInfoPanel.add(new JLabel(rsOrder.getTimestamp("order_date").toString()));
                orderInfoPanel.add(new JLabel("Status:"));
                orderInfoPanel.add(new JLabel(rsOrder.getString("order_status")));
                orderInfoPanel.add(new JLabel("Total:"));
                orderInfoPanel.add(new JLabel(String.valueOf(rsOrder.getDouble("total_amount"))));
                orderInfoPanel.add(new JLabel("Supplier:"));
                orderInfoPanel.add(new JLabel(rsOrder.getString("supplier")));
            }

            // Order items
            PreparedStatement psItems = conn.prepareStatement(
                    "SELECT m.medicine_name, oi.quantity, oi.unit_price, oi.subtotal " +
                    "FROM order_items oi JOIN medicines m ON oi.medicine_id = m.medicine_id " +
                    "WHERE oi.order_id = ?");
            psItems.setInt(1, orderId);
            ResultSet rsItems = psItems.executeQuery();
            while (rsItems.next()) {
                itemsModel.addRow(new Object[]{
                        rsItems.getString("medicine_name"),
                        rsItems.getInt("quantity"),
                        rsItems.getDouble("unit_price"),
                        rsItems.getDouble("subtotal")
                });
            }

            // Status history
            PreparedStatement psStatus = conn.prepareStatement(
                    "SELECT old_status, new_status, changed_by, change_date " +
                    "FROM order_status_history WHERE order_id = ?");
            psStatus.setInt(1, orderId);
            ResultSet rsStatus = psStatus.executeQuery();
            while (rsStatus.next()) {
                statusModel.addRow(new Object[]{
                        rsStatus.getString("old_status"),
                        rsStatus.getString("new_status"),
                        rsStatus.getInt("changed_by"),
                        rsStatus.getTimestamp("change_date")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading order details.");
        }
    }
}

