package tech.firefly.nkeksi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import tech.firefly.nkeksi.R;
import tech.firefly.nkeksi.login.GettingStartedUser;
import tech.firefly.nkeksi.model.FoodHomeModel;
import tech.firefly.nkeksi.user.CartDetail;
import tech.firefly.nkeksi.user.UserProfile;

public class HomeTemp extends AppCompatActivity {

    RecyclerView foodRecycler;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference tempRef;
    CarouselView carouselView;

    TextView txtViewCount;

    MaterialSearchView materialSearchView;
    FirebaseAuth auth;

    FloatingActionMenu floatMenu;
    com.github.clans.fab.FloatingActionButton profileFAB;
    com.github.clans.fab.FloatingActionButton logoutFAB;
    com.github.clans.fab.FloatingActionButton settingFAB;
    String sampleNetworkImageURLs[]={
            "https://firebasestorage.googleapis.com/v0/b/final-nkeksi.appspot.com/o/menuUploads%2Fjojos%20menu%2FGoat-meat-and-plantain-pepper-soup-14.jpg?alt=media&token=d21038bb-edf4-4ef1-a1a8-80ac758c1aa4",
            "https://firebasestorage.googleapis.com/v0/b/final-nkeksi.appspot.com/o/menuUploads%2Fmaranathas%20menu%2FFufuZandZEruZCameroon.jpg?alt=media&token=352318e0-7e9e-41fb-af99-27936f3cc7b1",
            "https://firebasestorage.googleapis.com/v0/b/final-nkeksi.appspot.com/o/menuUploads%2Fjojos%20menu%2Firish%20potato%20porridge.jpg?alt=media&token=a75959a9-8998-4417-97e5-628f54736a49",
            "https://firebasestorage.googleapis.com/v0/b/final-nkeksi.appspot.com/o/menuUploads%2Fmaranathas%20menu%2F1449685338_7.jpg?alt=media&token=f7138a31-b20e-42c8-b3ed-e2026cfe88eb"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_temp);

        foodRecycler = (RecyclerView)findViewById(R.id.food_list);
        firebaseDatabase = FirebaseDatabase.getInstance();
        tempRef = firebaseDatabase.getReference().child("TempMenu");
        auth = FirebaseAuth.getInstance();

        carouselView = (CarouselView)findViewById(R.id.slide_me);

        materialSearchView = (MaterialSearchView) findViewById(R.id.search_bar);

        floatMenu = (FloatingActionMenu) findViewById(R.id.home_fab_menu);
        profileFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.profileFAB);
        settingFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.settingFAB);
        logoutFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.logoutFAB);

        floatMenu.setMenuButtonColorNormal(getResources().getColor(R.color.colorPrimary));
        floatMenu.setMenuButtonColorPressed(getResources().getColor(R.color.colorPrimaryDark));
        profileFAB.setColorNormal(getResources().getColor(R.color.colorPrimary));
        profileFAB.setColorPressed(getResources().getColor(R.color.colorPrimaryDark));
        settingFAB.setColorNormal(getResources().getColor(R.color.colorPrimary));
        settingFAB.setColorPressed(getResources().getColor(R.color.colorPrimaryDark));
        logoutFAB.setColorNormal(getResources().getColor(R.color.colorPrimary));
        logoutFAB.setColorPressed(getResources().getColor(R.color.colorPrimaryDark));
        logoutFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeTemp.this, "Coming Soon", Toast.LENGTH_SHORT).show();
                floatMenu.close(true);
            }
        });
        settingFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeTemp.this, "Under Development...", Toast.LENGTH_SHORT).show();
                floatMenu.close(true);
            }
        });
        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeTemp.this,UserProfile.class));
                floatMenu.close(true);
            }
        });


        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Meals");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.showOverflowMenu();

        carouselView.setPageCount(sampleNetworkImageURLs.length);
        // To set simple images
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(final int position, final ImageView imageView) {

                Picasso.with(getApplicationContext()).load(sampleNetworkImageURLs[position])
                        .placeholder(R.drawable.defaultloader).networkPolicy(NetworkPolicy.OFFLINE)
                        .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getApplicationContext()).load(sampleNetworkImageURLs[position])
                                .placeholder(R.drawable.defaultloader).into(imageView);
                    }
                });

                //imageView.setImageResource(sampleImages[position]);
            }
        };

        carouselView.setImageListener(imageListener);


        FirebaseRecyclerAdapter<FoodHomeModel,ShowFoodHolder> adapter =
                new FirebaseRecyclerAdapter<FoodHomeModel, ShowFoodHolder>(FoodHomeModel.class,R.layout.single_home_temp,
                        ShowFoodHolder.class,tempRef) {
                    @Override
                    protected void populateViewHolder(ShowFoodHolder viewHolder, final FoodHomeModel model, int position) {
                        final String foodKey = getRef(position).getKey();
                        viewHolder.setPic(model.getPic(),getApplicationContext());
                        viewHolder.setPrice(model.getPrice());
                        viewHolder.setItem(model.getItem());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                floatMenu.close(true);
                                Intent i = new Intent(getApplicationContext(),FoodDetailTemp.class);
                                i.putExtra("name",model.getItem());
                                i.putExtra("prix",String.valueOf(model.getPrice()));
                                i.putExtra("pic",model.getPic());
                                i.putExtra("key",foodKey);
                                startActivity(i);
                            }
                        });
                    }
                };
        foodRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        foodRecycler.setNestedScrollingEnabled(false);
        foodRecycler.setAdapter(adapter);
        tempRef.keepSynced(true);
    }



    public static class ShowFoodHolder extends RecyclerView.ViewHolder{

        View mView;
        KenBurnsView imageView;
        Button button;
        TextView textView;

        public ShowFoodHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        void setPic(final String pic, final Context context){
            imageView = (KenBurnsView) itemView.findViewById(R.id.image_home_temp);
            Picasso.with(context).load(pic).placeholder(R.drawable.defaultloader).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(pic).placeholder(R.drawable.defaultloader).into(imageView);
                }
            });

        }
        void setPrice(int price){
            button = itemView.findViewById(R.id.price_home_temp);
            button.setText("XAF "+String.valueOf(price));
        }
        void setItem(String item){
            textView = itemView.findViewById(R.id.name_home_temp);
            textView.setText(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        materialSearchView.setMenuItem(item);


        final View notificaitons = menu.findItem(R.id.shop_cart).getActionView();

        txtViewCount = (TextView) notificaitons.findViewById(R.id.txtCount);

        FirebaseDatabase.getInstance().getReference().child("User Cart").child(auth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String countAll = String.valueOf(dataSnapshot.getChildrenCount());
                        if(dataSnapshot.getChildrenCount()<=0){
                            txtViewCount.setVisibility(View.GONE);
                        }else {
                            txtViewCount.setVisibility(View.VISIBLE);
                            txtViewCount.setText(countAll);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        notificaitons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CartDetail.class));
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, GettingStartedUser.class));
        }
        /*if (id == R.id.search) {
            startActivity(new Intent(this, MapsActivity.class));
        }*/


        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.shop_cart){
                    startActivity(new Intent(HomeTemp.this,CartDetail.class));
                }
                return false;
            }
        });

        return super.onOptionsItemSelected(item);
    }

}
