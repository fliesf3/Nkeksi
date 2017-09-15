package tech.firefly.nkeksi.model;

/**
 * Created by RamsonB on 09-Sep-17.
 */

public class CartItemModel {

    private int foodPrice;
    private String foodDesc;
    private int quantity;
    private String foodName;
    private String foodPic;

    private String restoUID;
    private String foodCategoryKey;
    private String foodID;

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPic() {
        return foodPic;
    }

    public void setFoodPic(String foodPic) {
        this.foodPic = foodPic;
    }

    public String getRestoUID() {
        return restoUID;
    }

    public void setRestoUID(String restoUID) {
        this.restoUID = restoUID;
    }

    public String getFoodCategoryKey() {
        return foodCategoryKey;
    }

    public void setFoodCategoryKey(String foodCategoryKey) {
        this.foodCategoryKey = foodCategoryKey;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }
}
