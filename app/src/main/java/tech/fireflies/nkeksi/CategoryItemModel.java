package tech.fireflies.nkeksi;

/**
 * Created by RamsonB on 22-Aug-17.
 */

public class CategoryItemModel {

    private String description;
    private String item;
    private String pic;
    private String price;

    public CategoryItemModel(){

    }

    public CategoryItemModel(String description, String item, String pic, String price) {
        this.description = description;
        this.item = item;
        this.pic = pic;
        this.price = price;
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
}
