import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LowStockMonitor {
    private final NotificationManager notifier;
    private final Timer checkTimer;
    private static final int CHECK_INTERVAL = 300000; // 5 minutes

    public LowStockMonitor(JFrame parentFrame) {
        this.notifier = NotificationManager.getInstance();
        this.notifier.setParentFrame(parentFrame);
        
        // Create timer to periodically check stock levels
        checkTimer = new Timer(CHECK_INTERVAL, e -> checkStockLevels());
        checkTimer.setRepeats(true);
    }

    public void start() {
        checkTimer.start();
        // Do initial check
        checkStockLevels();
    }

    public void stop() {
        checkTimer.stop();
    }

    private void checkStockLevels() {
        DefaultTableModel model = TrackMedApp.getSharedModel();
        if (model == null) return;

        for (int row = 0; row < model.getRowCount(); row++) {
            try {
                int quantity = Integer.parseInt(model.getValueAt(row, 3).toString());
                int threshold = Integer.parseInt(model.getValueAt(row, 4).toString());
                String medicineName = model.getValueAt(row, 1).toString();

                if (quantity <= threshold) {
                    notifier.showNotification(
                        "Low stock alert: " + medicineName + " (Qty: " + quantity + ")",
                        NotificationManager.NotificationType.WARNING
                    );
                }
            } catch (NumberFormatException ex) {
                // Skip invalid rows
                continue;
            }
        }
    }

    public void checkSingleItem(String medicineName, int quantity, int threshold) {
        if (quantity <= threshold) {
            notifier.showNotification(
                "Low stock alert: " + medicineName + " (Qty: " + quantity + ")",
                NotificationManager.NotificationType.WARNING
            );
        }
    }
}