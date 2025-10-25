package model;

public class MedicineCategory {
    private int categoryId;
    private String categoryName;
     private String description;

    public MedicineCategory(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description  = description;
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "MedicineCategory [categoryId=" + categoryId + ", categoryName=" + categoryName +   ", description=" + description +"]";
    }
}
