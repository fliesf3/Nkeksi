package tech.firefly.nkeksi;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.wangyuwei.shoppoing.ShoppingView;
import tech.firefly.nkeksi.model.CartItemModel;

public class CartDetail extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    RecyclerView cartList;
    DatabaseReference cartRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cart");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.showOverflowMenu();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        cartRef = database.getReference().child("User Cart").child(auth.getCurrentUser().getUid());

        initList();

    }

    private void initList() {
        cartList  = (RecyclerView) findViewById(R.id.cart_order_list);



        FirebaseRecyclerAdapter<CartItemModel,CartHolder> adapter = new
                FirebaseRecyclerAdapter<CartItemModel, CartHolder>(CartItemModel.class,R.layout.single_cart_item
                ,CartHolder.class,cartRef) {
                    @Override
                    protected void populateViewHolder(final CartHolder viewHolder, final CartItemModel model, int position) {
                        viewHolder.setFoodName(model.getFoodName());
                        viewHolder.setFoodPrice(model.getFoodPrice());
                        viewHolder.setFoodDesc(model.getFoodDesc());
                        viewHolder.setFoodPic(getApplicationContext(),model.getFoodPic());
                        viewHolder.setQuantity(model.getQuantity());
                        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseDatabase.getInstance().getReference().child("User Cart")
                                        .child(auth.getCurrentUser().getUid())
                                        .child(model.getFoodID()).removeValue();
                                Toast.makeText(CartDetail.this, "Deleted "+model.getFoodName(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        viewHolder.setFoodID(model.getFoodID());
                        viewHolder.setFoodCategoryKey(model.getFoodCategoryKey());
                        viewHolder.setRestoUID(model.getRestoUID());

                        viewHolder.updateCount.setOnShoppingClickListener(new ShoppingView.ShoppingClickListener() {
                            @Override
                            public void onAddClick(int num) {
                                final HashMap<String, Object> addMap = new HashMap<>();
                                addMap.put("restoUID", model.getRestoUID());
                                addMap.put("quantity", num);
                                addMap.put("foodID", model.getFoodID());
                                addMap.put("foodCategoryKey",model.getFoodCategoryKey());
                                addMap.put("foodPrice",model.getFoodPrice());
                                addMap.put("foodDesc",model.getFoodDesc());
                                addMap.put("foodName",model.getFoodName());
                                addMap.put("foodPic",model.getFoodPic());

                                if (num > 0) {
                                    FirebaseDatabase.getInstance().getReference().child("User Cart")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(model.getFoodID()).setValue(addMap);
                                }

                                if (num == 0) {
                                    FirebaseDatabase.getInstance().getReference().child("User Cart")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(model.getFoodID()).removeValue();
                                }
                            }

                            @Override
                            public void onMinusClick(int num) {
                                final HashMap<String, Object> addMap = new HashMap<>();
                                addMap.put("restoUID", model.getRestoUID());
                                addMap.put("quantity", num);
                                addMap.put("foodID", model.getFoodID());
                                addMap.put("foodCategoryKey",model.getFoodCategoryKey());
                                addMap.put("foodPrice",model.getFoodPrice());
                                addMap.put("foodDesc",model.getFoodDesc());
                                addMap.put("foodName",model.getFoodName());
                                addMap.put("foodPic",model.getFoodPic());

                                if (num > 0) {
                                    FirebaseDatabase.getInstance().getReference().child("User Cart")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(model.getFoodID()).setValue(addMap);
                                }

                                if (num == 0) {
                                    FirebaseDatabase.getInstance().getReference().child("User Cart")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(model.getFoodID()).removeValue();
                                }
                            }
                        });
                        //int sum=0;
                        /*for(int i = 0;i<viewHolder.restoUIDArray.size();i++){

                            sum = sum + ((model.getQuantity())*(model.getFoodPrice()));
                        }
                        viewHolder.totalBtn.setText(String.valueOf(sum));*/

                        /*cartRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                
                                for(int i=0;i<dataSnapshot.getChildrenCount();i++){
                                    viewHolder.restoUIDArray.add(i,model.getRestoUID());
                                    Toast.makeText(CartDetail.this, ""+viewHolder.restoUIDArray.get(i), Toast.LENGTH_SHORT).show();
                                }
                                //Toast.makeText(CartDetail.this, "Total = "+viewHolder.restoUIDArray.size(), Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/


                    }
                };
        //Toast.makeText(this, "Size = "+adapter.getItemCount(), Toast.LENGTH_SHORT).show();
        cartList.setLayoutManager(new LinearLayoutManager(CartDetail.this));
        cartList.setAdapter(adapter);
    }

    public static class CartHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView price,name,desc;
        Button delete,totalBtn,orderBtn;
        ShoppingView count,updateCount;
        CircularImageView imageView;

        List<String> restoUIDArray = new ArrayList<>();
        List<String> foodCatArray = new ArrayList<>();
        List<String> foodIdArray = new ArrayList<>();

        public CartHolder(View itemView) {
            super(itemView);

            mView = itemView;

            totalBtn = itemView.findViewById(R.id.cart_total_btn);

            orderBtn = itemView.findViewById(R.id.cart_order_btn);

            updateCount = itemView.findViewById(R.id.cart_dish_count);

            delete = itemView.findViewById(R.id.cart_dish_delete);
        }

        void setFoodPrice(int foodPrice){
            price = mView.findViewById(R.id.cart_dish_price);
            price.setText(String.valueOf(foodPrice));
        }
        void setFoodDesc(String foodDesc){
            desc = mView.findViewById(R.id.cart_dish_description);
            desc.setText(foodDesc);
        }
        void setQuantity(int quantity){
            count = mView.findViewById(R.id.cart_dish_count);
            count.setTextNum(quantity);
        }
        void setFoodName(String foodName){
            name = mView.findViewById(R.id.cart_dish_name);
            name.setText(foodName);
        }
        void setFoodPic(Context c, String foodPic){
            imageView = mView.findViewById(R.id.cart_dish_image);
            Picasso.with(c).load(foodPic).into(imageView);
        }

        void setFoodID(String foodID){
            foodIdArray.add(foodID);
        }
        void setFoodCategoryKey(String foodCategoryKey){
            foodCatArray.add(foodCategoryKey);
        }
        void setRestoUID(String restoUID){
            restoUIDArray.add(restoUID);
        }
    }
}
