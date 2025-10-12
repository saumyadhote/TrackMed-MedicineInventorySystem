package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {
    // Fields matching database columns
    private int orderId;
    private int hospitalId;
    private int supplierId;
    private Timestamp orderDate;
    private String orderStatus;  // PENDING, APPROVED, SHIPPED, DELIVERED, CANCELLED
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String specialInstructions;
    private Timestamp approvedDate;
    private Timestamp shippedDate;
    private Timestamp deliveredDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields for display
    private String hospitalName;
    private String supplierName;
    
    // Constructors
    public Order() {
    }
    
    public Order(int hospitalId, int supplierId, String shippingAddress) {
        this.hospitalId = hospitalId;
        this.supplierId = supplierId;
        this.shippingAddress = shippingAddress;
        this.orderStatus = "PENDING";
        this.totalAmount = BigDecimal.ZERO;
        this.orderDate = new Timestamp(System.currentTimeMillis());
    }
    
    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getHospitalId() {
        return hospitalId;
    }
    
    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }
    
    public int getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }
    
    public Timestamp getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }
    
    public String getOrderStatus() {
        return orderStatus;
    }
    
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public String getSpecialInstructions() {
        return specialInstructions;
    }
    
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
    
    public Timestamp getApprovedDate() {
        return approvedDate;
    }
    
    public void setApprovedDate(Timestamp approvedDate) {
        this.approvedDate = approvedDate;
    }
    
    public Timestamp getShippedDate() {
        return shippedDate;
    }
    
    public void setShippedDate(Timestamp shippedDate) {
        this.shippedDate = shippedDate;
    }
    
    public Timestamp getDeliveredDate() {
        return deliveredDate;
    }
    
    public void setDeliveredDate(Timestamp deliveredDate) {
        this.deliveredDate = deliveredDate;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getHospitalName() {
        return hospitalName;
    }
    
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
    
    public String getSupplierName() {
        return supplierName;
    }
    
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", hospitalName='" + hospitalName + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", totalAmount=" + totalAmount +
                ", orderDate=" + orderDate +
                '}';
    }
}
