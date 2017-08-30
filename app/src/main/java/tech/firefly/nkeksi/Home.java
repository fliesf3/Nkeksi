package tech.firefly.nkeksi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class Home extends AppCompatActivity {

    CarouselView carouselView;
    RecyclerView foodList;
    FirebaseAnalytics mFirebaseAnalytics;


    MaterialSearchView materialSearchView;
    Query queryText;
    DatabaseReference indexRef;
    FirebaseRecyclerAdapter<LocationSearchModel,OnlineLocationHolder> FoodAdapter;

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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        indexRef = FirebaseDatabase.getInstance().getReference().child("unVerified");
        carouselView = (CarouselView) findViewById(R.id.slide_me);
        foodList = (RecyclerView) findViewById(R.id.food_list);

        materialSearchView = (MaterialSearchView) findViewById(R.id.search_bar);



        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryText = indexRef.orderByChild("indexLocation").startAt(newText.toLowerCase()).endAt(newText.toLowerCase()+"\uf8ff");
                FirebaseRecyclerAdapter<LocationSearchModel,OnlineLocationHolder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<LocationSearchModel, OnlineLocationHolder>(LocationSearchModel.class,
                                R.layout.single_food_view,OnlineLocationHolder.class,queryText) {
                            @Override
                            protected void populateViewHolder(OnlineLocationHolder viewHolder, LocationSearchModel model, int position) {
                                final String key = getRef(position).getKey();
                                viewHolder.setRestoImage(Home.this,model.getRestoImage());
                                viewHolder.setRestoLocation(model.getRestoLocation());
                                viewHolder.setRestoName(model.getRestoName());
                                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(Home.this,RestoDashBoard.class);
                                        i.putExtra("key",key);
                                        startActivity(i);
                                    }
                                });
                            }

                        };
                foodList.setLayoutManager(new GridLayoutManager(Home.this,2));
                foodList.setAdapter(firebaseRecyclerAdapter);


                return true;
            }
        });

        carouselView.setPageCount(3);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(R.drawable.meat);
            }
        });
        foodList.setLayoutManager(new GridLayoutManager(this,2));
        foodList.setAdapter(FoodAdapter);

    }
    public void DefaultAdapter(){
        foodList.setLayoutManager(new GridLayoutManager(this,2));
        foodList.setAdapter(FoodAdapter);
    }





    public class FoodHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        View mView;

        public FoodHolder(View itemView) {
            super(itemView);
            mView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.food_pic);
        }


    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(item);
            return true;
        }

    @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            int id = item.getItemId();

            if(id==R.id.logout){
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,GettingStartedUser.class));
            }
            if(id==R.id.search){
                startActivity(new Intent(this,MapsActivity.class));
            }

            return super.onOptionsItemSelected(item);
        }

    @Override
    public void onBackPressed() {
        if (materialSearchView.isSearchOpen()) {
            materialSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FoodAdapter =
                new FirebaseRecyclerAdapter<LocationSearchModel, OnlineLocationHolder>(LocationSearchModel.class,
                        R.layout.single_food_view,OnlineLocationHolder.class,indexRef) {
                    @Override
                    protected void populateViewHolder(OnlineLocationHolder viewHolder, LocationSearchModel model, int position) {
                        final String key = getRef(position).getKey();
                        viewHolder.setRestoImage(Home.this,model.getRestoImage());
                        viewHolder.setRestoLocation(model.getRestoLocation());
                        viewHolder.setRestoName(model.getRestoName());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Home.this,RestoDashBoard.class);
                                i.putExtra("key",key);
                                startActivity(i);
                            }
                        });
                    }

                };

    }

    public static class OnlineLocationHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView restoNames,restoLoc;
        View mView;

        public OnlineLocationHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        void setRestoImage(Context c,String restoImage){

            imageView = (ImageView) itemView.findViewById(R.id.food_pic);
            if(!restoImage.isEmpty() && restoImage!=null)
            Picasso.with(c).load(restoImage).placeholder(R.drawable.meat).into(imageView);

        }
        void setRestoLocation(String restoLocation){
            restoLoc = (TextView) itemView.findViewById(R.id.resto_loc_search);
            if(restoLocation!=null && !restoLocation.isEmpty())
            restoLoc.setText(restoLocation);
        }
        void setRestoName(String restoName){
            restoNames = (TextView)itemView.findViewById(R.id.resto_name_search);
            if(restoName!=null && !restoName.isEmpty())
            restoNames.setText(restoName);
        }
    }

}