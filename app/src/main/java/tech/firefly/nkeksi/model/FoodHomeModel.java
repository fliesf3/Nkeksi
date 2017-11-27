package tech.firefly.nkeksi.model;



public class FoodHomeModel {
    private String pic;
    private int price;
    private String item;

    public FoodHomeModel(){

    }

    public FoodHomeModel(String pic, int price, String item) {
        this.pic = pic;
        this.price = price;
        this.item = item;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
