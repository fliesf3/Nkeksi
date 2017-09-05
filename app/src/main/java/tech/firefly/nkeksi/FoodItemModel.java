package tech.firefly.nkeksi;

/**
 * Created by RamsonB on 22-Aug-17.
 */

public class FoodItemModel {

    private String description;
    private String item;
    private String pic;
    private String price;
    private String email;
    private String phone;


    public FoodItemModel(){

    }

    public FoodItemModel(String description, String item, String pic, String price, String email, String phone) {
        this.description = description;
        this.item = item;
        this.pic = pic;
        this.price = price;
        this.email = email;
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
