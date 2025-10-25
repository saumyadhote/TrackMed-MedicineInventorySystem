package model;

public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int medicineId;
    private int quantity;
    private double price;

    public OrderItem(int orderItemId, int orderId, int medicineId, int quantity, double price) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "OrderItem [orderItemId=" + orderItemId + ", orderId=" + orderId + 
               ", medicineId=" + medicineId + ", quantity=" + quantity + ", price=" + price + "]";
    }
}
