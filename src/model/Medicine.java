package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Medicine {
    // Fields matching database columns
    private int medicineId;
    private String medicineName;
    private Integer categoryId;
    private int supplierId;
    private String description;
    private String manufacturer;
    private BigDecimal unitPrice;
    private int stockQuantity;
    private int reorderLevel;
    private String unitOfMeasure;
    private boolean requiresPrescription;
    private String batchNumber;
    private Date manufacturingDate;
    private Date expiryDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean isActive;
    
    // Additional fields for display (not in database)
    private String categoryName;
    private String supplierName;
    
    // Constructors
    public Medicine() {
    }
    
    public Medicine(String medicineName, int supplierId, BigDecimal unitPrice, int stockQuantity) {
        this.medicineName = medicineName;
        this.supplierId = supplierId;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.isActive = true;
        this.reorderLevel = 10;
        this.unitOfMeasure = "Units";
    }
    
    // Getters and Setters
    public int getMedicineId() {
        return medicineId;
    }
    
    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }
    
    public String getMedicineName() {
        return medicineName;
    }
    
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
    
    public Integer getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    public int getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }
    
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public int getReorderLevel() {
        return reorderLevel;
    }
    
    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }
    
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }
    
    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
    
    public boolean isRequiresPrescription() {
        return requiresPrescription;
    }
    
    public void setRequiresPrescription(boolean requiresPrescription) {
        this.requiresPrescription = requiresPrescription;
    }
    
    public String getBatchNumber() {
        return batchNumber;
    }
    
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }
    
    public Date getManufacturingDate() {
        return manufacturingDate;
    }
    
    public void setManufacturingDate(Date manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }
    
    public Date getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
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
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getSupplierName() {
        return supplierName;
    }
    
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    
    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId=" + medicineId +
                ", medicineName='" + medicineName + '\'' +
                ", unitPrice=" + unitPrice +
                ", stockQuantity=" + stockQuantity +
                ", supplierName='" + supplierName + '\'' +
                '}';
    }
}