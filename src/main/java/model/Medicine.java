package model;

public class Medicine {
    private int medicineId;
    private String name;
    private String category;
    private String expiryDate;
    private int quantity;
    private double price;

    public Medicine(int medicineId, String name, String category, String expiryDate, int quantity, double price) {
        this.medicineId = medicineId;
        this.name = name;
        this.category = category;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "Medicine [medicineId=" + medicineId + ", name=" + name + ", category=" + category +
               ", expiryDate=" + expiryDate + ", quantity=" + quantity + ", price=" + price + "]";
    }
}
