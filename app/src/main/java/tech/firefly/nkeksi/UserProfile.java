package tech.firefly.nkeksi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;


public class UserProfile extends AppCompatActivity {

    ImageView userWall;
    CircularImageView userDP;
    TextView userName,userLocation;
    TextView userNameDetail,userPhoneDetail,userEmailDetail,userOrderDetail;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseUser = auth.getCurrentUser();

        userWall = (ImageView)findViewById(R.id.user_wall);
        userDP = (CircularImageView)findViewById(R.id.user_image_profile);
        userName = (TextView)findViewById(R.id.user_name);
        userLocation = (TextView)findViewById(R.id.user_location);
        userNameDetail = (TextView)findViewById(R.id.user_name_detail);
        userEmailDetail = (TextView)findViewById(R.id.user_email_detail);
        userPhoneDetail = (TextView)findViewById(R.id.user_phone_detail);
        userOrderDetail = (TextView)findViewById(R.id.user_order_detail);

    }


}
