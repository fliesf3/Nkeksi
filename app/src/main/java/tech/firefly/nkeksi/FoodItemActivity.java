package tech.firefly.nkeksi;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by RamsonB on 01-Sep-17.
 */

public class FoodItemActivity extends AppCompatActivity {

    RecyclerView foodDetailList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("currKey"));
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.showOverflowMenu();

        String prevKey = getIntent().getStringExtra("prevKey");
        String newKey = getIntent().getStringExtra("currKey");

        Toast.makeText(this, "Prev = ", Toast.LENGTH_SHORT).show();

        foodDetailList = (RecyclerView) findViewById(R.id.foodDetailList);

        FirebaseRecyclerAdapter<FoodItemModel,FoodDetailHolder> adapter =
                new FirebaseRecyclerAdapter<FoodItemModel, FoodDetailHolder>(FoodItemModel.class,
                        R.layout.single_menu_view,FoodDetailHolder.class,
                        FirebaseDatabase.getInstance().getReference().child("Menu")
                .child(prevKey).child(newKey)) {
                    @Override
                    protected void populateViewHolder(FoodDetailHolder viewHolder, FoodItemModel model, int position) {
                        viewHolder.setItem(model.getItem());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setPrice(model.getPrice());
                        viewHolder.setPic(FoodItemActivity.this,model.getPic());
                    }
                };
        foodDetailList.setLayoutManager(new LinearLayoutManager(this));
        foodDetailList.setAdapter(adapter);

    }

    public static class FoodDetailHolder extends RecyclerView.ViewHolder{

        TextView name,desc,pricing;
        CircularImageView imageView;


        View mView;

        public FoodDetailHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setItem(String item){
            name = (TextView) itemView.findViewById(R.id.dish_name);
            name.setText(item);

        }
        void setPic(final Context c, final String pic){
            imageView = (CircularImageView)itemView.findViewById(R.id.dish_image);
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
        void setPrice(String price){
            pricing = (TextView)itemView.findViewById(R.id.dish_price);
            pricing.setText(price);
        }
        void setDescription(String description){
            desc = (TextView)itemView.findViewById(R.id.dish_description);
            desc.setText(description);
        }
    }
}
