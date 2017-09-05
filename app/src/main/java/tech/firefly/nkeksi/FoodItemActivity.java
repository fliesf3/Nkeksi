package tech.firefly.nkeksi;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ramotion.foldingcell.*;

import java.util.HashMap;

import me.wangyuwei.shoppoing.ShoppingView;


public class FoodItemActivity extends AppCompatActivity {

    RecyclerView foodDetailList;
    String restoKey;
    String foodKey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("currKey"));
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.showOverflowMenu();

        restoKey = getIntent().getStringExtra("prevKey");
        foodKey = getIntent().getStringExtra("currKey");


        foodDetailList = (RecyclerView) findViewById(R.id.foodDetailList);

        FirebaseRecyclerAdapter<FoodItemModel, FoodDetailHolder> adapter =
                new FirebaseRecyclerAdapter<FoodItemModel, FoodDetailHolder>(FoodItemModel.class,
                        R.layout.single_menu_view, FoodDetailHolder.class,
                        FirebaseDatabase.getInstance().getReference().child("Menu")
                                .child(restoKey).child(foodKey)) {
                    @Override
                    protected void populateViewHolder(final FoodDetailHolder viewHolder, FoodItemModel model, int position) {
                        final String foodListKey = getRef(position).getKey();
                        viewHolder.setItem(model.getItem());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setPrice(model.getPrice());
                        viewHolder.setPic(FoodItemActivity.this, model.getPic());
                        viewHolder.mSv1.setOnShoppingClickListener(new ShoppingView.ShoppingClickListener() {
                            @Override
                            public void onAddClick(int num) {
                                HashMap<String, Object> addMap = new HashMap<>();
                                addMap.put("restoUID", restoKey);
                                addMap.put("quantity", num);
                                addMap.put("foodID", foodListKey);
                                addMap.put("foodCategoryKey",foodKey);
                                if (num > 0) {
                                    FirebaseDatabase.getInstance().getReference().child("User Cart")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(foodListKey).setValue(addMap);
                                }

                                if (num == 0) {
                                    FirebaseDatabase.getInstance().getReference().child("User Cart")
                                            .child(foodListKey).removeValue();
                                }
                            }

                            @Override
                            public void onMinusClick(int num) {
                                HashMap<String, Object> addMap = new HashMap<>();
                                addMap.put("restoUID", restoKey);
                                addMap.put("quantity", num);
                                addMap.put("singleFoodID", foodListKey);
                                addMap.put("foodCategoryKey",foodKey);
                                if (num > 0) {
                                    FirebaseDatabase.getInstance().getReference().child("User Cart")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(foodListKey).setValue(addMap);
                                }
                                if (num == 0) {
                                    FirebaseDatabase.getInstance().getReference().child("User Cart")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(foodListKey).removeValue();
                                }
                            }
                        });
                    }
                };
        foodDetailList.setLayoutManager(new LinearLayoutManager(this));
        foodDetailList.setAdapter(adapter);

    }

    public static class FoodDetailHolder extends RecyclerView.ViewHolder {

        TextView name, desc, pricing, emailV, phoneV;
        CircularImageView imageView;
        ImageView viewImage;
        View mView;
        ShoppingView mSv1;

        public FoodDetailHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mSv1 = (ShoppingView) itemView.findViewById(R.id.dish_count);
        }

        void setItem(String item) {
            name = (TextView) itemView.findViewById(R.id.dish_name);
            name.setText(item);

        }

        void setPic(final Context c, final String pic) {
            imageView = (CircularImageView) itemView.findViewById(R.id.dish_image);
            Picasso.with(c).load(pic).placeholder(R.drawable.defaultloader).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(c).load(pic).placeholder(R.drawable.defaultloader).into(imageView);
                }
            });
        }

        void setPrice(String price) {
            pricing = (TextView) itemView.findViewById(R.id.dish_price);
            pricing.setText(price);
        }

        void setDescription(String description) {
            desc = (TextView) itemView.findViewById(R.id.dish_description);
            desc.setText(description);
        }

        void setPhone(String phone, String someKey) {
            phoneV = itemView.findViewById(R.id.dish_phone);
            DatabaseReference phoneRef = FirebaseDatabase.getInstance().getReference()
                    .child("unVerified").child(someKey).child("restauPhoneNumber");
            phoneRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null)
                        phoneV.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        void setEmail(String email, String someKey) {
            emailV = itemView.findViewById(R.id.dish_email);
            DatabaseReference phoneRef = FirebaseDatabase.getInstance().getReference()
                    .child("unVerified").child(someKey).child("restoEmail");
            phoneRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null)
                        emailV.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
