package tech.fireflies.nkeksi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikhaellopez.circularimageview.CircularImageView;


public class UserProfile extends AppCompatActivity {

    int[] images = {R.drawable.burger1,R.drawable.burger2,R.drawable.chick2
            ,R.drawable.chick3,R.drawable.chick4,R.drawable.food,R.drawable.food1,R.drawable.food2,R.drawable.foodie,
            R.drawable.meat,R.drawable.meat2,R.drawable.meat3,R.drawable.meat4};
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Orders");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.showOverflowMenu();

        recyclerView = (RecyclerView) findViewById(R.id.ordered_item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new OrderAdapter());

    }

    public class OrderAdapter extends RecyclerView.Adapter<OrderHolder>{

        @Override
        public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_food_view,parent,false);
            return new OrderHolder(v);
        }

        @Override
        public void onBindViewHolder(OrderHolder holder, int position) {
            holder.imageView.setImageResource(images[position]);
        }

        @Override
        public int getItemCount() {
            return images.length;
        }
    }

    public static class OrderHolder extends RecyclerView.ViewHolder{

        CircularImageView imageView;

        public OrderHolder(View itemView) {
            super(itemView);

            imageView = (CircularImageView)itemView.findViewById(R.id.user_image_profile);

        }
    }
}
