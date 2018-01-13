package tech.firefly.nkeksi.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import me.wangyuwei.shoppoing.ShoppingView;
import tech.firefly.nkeksi.R;
import tech.firefly.nkeksi.ReviewMeal;

public class FoodDetailTemp extends AppCompatActivity {

    TextView price, rating, review, name;
    CircularImageView imageView;
    DatabaseReference cartRef;
    DatabaseReference orderRef;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    ShoppingView addToCart,orderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail_temp);


        name = (TextView) findViewById(R.id.food_name_temp);
        price = (TextView) findViewById(R.id.food_price_temps);
        rating = (TextView) findViewById(R.id.food_rating_temp);
        review = (TextView) findViewById(R.id.food_review_temp);
        imageView = (CircularImageView) findViewById(R.id.food_pic_temp);

        addToCart = (ShoppingView)findViewById(R.id.add_to_cart);


        firebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        cartRef = firebaseDatabase.getReference().child("User Cart").child(user.getUid());
        orderRef = firebaseDatabase.getReference().child("Orders").child(user.getUid());

        String nameText = getIntent().getStringExtra("name");
        String picText = getIntent().getStringExtra("pic");
        String priceText = getIntent().getStringExtra("prix");
        String keyText = getIntent().getStringExtra("key");

        name.setText(nameText);
        price.setText(priceText);
        Picasso.with(getApplicationContext()).load(picText).into(imageView);
    }

    public void AddToCart(View view) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", user.getUid());
        map.put("foodID", getIntent().getStringExtra("key"));
        map.put("foodPrice", getIntent().getStringExtra("prix"));
        cartRef.push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public void AddToOrder(View view) {

        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
        
        // you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getDefault());

        String localTime = date.format(currentLocalTime);

        Toast.makeText(this, "" + localTime, Toast.LENGTH_SHORT).show();
    }

    public void ReviewFood(View v){
        Intent i = new Intent(this,ReviewMeal.class);
        i.putExtra("foodKey",getIntent().getStringExtra("key"));
        startActivity(i);

    }
}
