package model;

public class ShoppingCart {
    private int cartId;
    private int hospitalId;
    private int medicineId;
    private int quantity;

    public ShoppingCart(int cartId, int hospitalId, int medicineId, int quantity) {
        this.cartId = cartId;
        this.hospitalId = hospitalId;
        this.medicineId = medicineId;
        this.quantity = quantity;
    }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getHospitalId() { return hospitalId; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "ShoppingCart [cartId=" + cartId + ", hospitalId=" + hospitalId +
               ", medicineId=" + medicineId + ", quantity=" + quantity + "]";
    }
}
