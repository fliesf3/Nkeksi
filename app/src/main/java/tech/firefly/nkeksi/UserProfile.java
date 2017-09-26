package tech.firefly.nkeksi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;


public class UserProfile extends AppCompatActivity {

    ImageView userWall;
    CircularImageView userDP;
    TextView userName,userLocation;
    TextView userNameDetail,userPhoneDetail,userEmailDetail,userOrderDetail;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseUser firebaseUser;
    DatabaseReference userRef;
    FloatingActionButton floatingActionButton;

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
        floatingActionButton = (FloatingActionButton)findViewById(R.id.user_edit_profile);

        userRef = database.getReference().child("UserInfo").child(firebaseUser.getUid());
        userEmailDetail.setText(firebaseUser.getEmail());
        userRef.child("firstName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                userRef.child("lastName").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        userName.setText(dataSnapshot.getValue(String.class)+" "+snapshot.getValue(String.class));
                        userNameDetail.setText(dataSnapshot.getValue(String.class)+" "+snapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Toast.makeText(UserProfile.this, "Name: "+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userRef.child("phoneNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userPhoneDetail.setText(String.valueOf(dataSnapshot.getValue(Long.class)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this,EditProfile.class));
            }
        });

    }


}
