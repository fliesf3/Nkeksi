package tech.fireflies.nkeksi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class Home extends AppCompatActivity {

    CarouselView carouselView;
    RecyclerView foodList;

    int[] images = {R.drawable.burger1,R.drawable.burger2,R.drawable.chick2
            ,R.drawable.chick3,R.drawable.chick4,R.drawable.food,R.drawable.food1,R.drawable.food2,R.drawable.foodie,
            R.drawable.meat,R.drawable.meat2,R.drawable.meat3,R.drawable.meat4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Restaurants");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.showOverflowMenu();

        carouselView = (CarouselView) findViewById(R.id.slide_me);
        foodList = (RecyclerView) findViewById(R.id.food_list);

        carouselView.setPageCount(images.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(images[position]);
            }
        });
        foodList.setLayoutManager(new GridLayoutManager(this,2));
        foodList.setAdapter(new FoodAdapter(this));

    }
    public class FoodAdapter extends RecyclerView.Adapter<FoodHolder>{

        Context c;

        public FoodAdapter(Context c) {
            this.c = c;
        }

        @Override
        public FoodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_food_view,parent,false);
            return new FoodHolder(v);
        }

        @Override
        public void onBindViewHolder(FoodHolder holder, int position) {
            holder.imageView.setImageResource(images[position]);
        }

        @Override
        public int getItemCount() {
            return images.length;
        }
    }

    public class FoodHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public FoodHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.food_pic);
        }


    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            if(id==R.id.logout){
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,UserOrResto.class));
            }

            return super.onOptionsItemSelected(item);
        }

}
