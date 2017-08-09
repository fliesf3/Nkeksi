package tech.fireflies.nkeksi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterSecondUser extends AppCompatActivity {

    EditText firstName,lastName,phoneText;
    String locationText;
    ImageButton nextButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_second_user);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Signing Up");
        progressDialog.setMessage("Setting Up Account");

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.showOverflowMenu();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("UserInfo");

        firstName = (EditText) findViewById(R.id.admin_first_name_register);
        lastName = (EditText) findViewById(R.id.admin_last_name_register);
        phoneText = (EditText) findViewById(R.id.resto_phone_number_register);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
    }
    public void SecondRegistration(View v){
        String first,last,phone;
        first = firstName.getText().toString().trim();
        last = lastName.getText().toString().trim();
        phone = phoneText.getText().toString().trim();

        if(TextUtils.isEmpty(first)||TextUtils.isEmpty(last)||TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Enter All Fields To Continue", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.StandUp).duration(500).playOn(nextButton);
            return;
        }

        progressDialog.show();

        HashMap<String,String> map = new HashMap<>();
        map.put("firstName",first);
        map.put("lastName",last);
        map.put("phoneNumber", String.valueOf(phone));
        map.put("isResto","false");
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RegisterSecondUser.this, "Welcome", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
                startActivity(new Intent(RegisterSecondUser.this,Home.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterSecondUser.this, ""+e, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
