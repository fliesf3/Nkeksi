package tech.firefly.nkeksi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by user on 23-Jul-17.
 */

public class LoginUser extends AppCompatActivity {

    EditText emailText,passText;
    Button signUser;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    DatabaseReference checkIfResto;
    DatabaseReference checkIfRestoSecond;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sign In");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.showOverflowMenu();

        auth = FirebaseAuth.getInstance();
        checkIfResto = FirebaseDatabase.getInstance().getReference().child("UserInfo");
        checkIfRestoSecond  = FirebaseDatabase.getInstance().getReference().child("unVerified");

        emailText = (EditText) findViewById(R.id.email_signin_user);
        passText = (EditText) findViewById(R.id.pass_signin_user);
        signUser = (Button) findViewById(R.id.signInButtonUser);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Signing Up");
        progressDialog.setMessage("Setting Up Account");

    }

    public void SignInButton(View v){

        final String email,password;

        email = emailText.getText().toString().trim();
        password = passText.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Input Email", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(500).playOn(signUser);
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Input Password", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(500).playOn(signUser);
            return;
        }
        if(password.length()<6){
            Toast.makeText(this, "Password Must Be At Least 6 Characters Long", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(500).playOn(signUser);
            return;
        }

        progressDialog.show();
        YoYo.with(Techniques.RubberBand).duration(500).playOn(signUser);

        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialog.dismiss();
                Toast.makeText(LoginUser.this, "Success", Toast.LENGTH_SHORT).show();
                checkIfResto.child(auth.getCurrentUser().getUid()).child("isResto").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue(Boolean.class)!=null){
                            if(dataSnapshot.getValue(Boolean.class)){
                                Toast.makeText(LoginUser.this, "Yay You Are A Restaurant", Toast.LENGTH_SHORT).show();

                            }
                            if(!dataSnapshot.getValue(Boolean.class)){
                                Toast.makeText(LoginUser.this, "Yay You Are A User", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                checkIfRestoSecond.child(auth.getCurrentUser().getUid()).child("isResto").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue(Boolean.class)!=null){
                            if(dataSnapshot.getValue(Boolean.class)){
                                Toast.makeText(LoginUser.this, "Yay You Are A Restaurant", Toast.LENGTH_SHORT).show();

                            }
                            if(!dataSnapshot.getValue(Boolean.class)){
                                Toast.makeText(LoginUser.this, "Yay You Are A User", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(LoginUser.this,DeliveryAddress.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginUser.this, ""+e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void SendResetU(View v){
        startActivity(new Intent(this,ForgotPasswordUser.class));
    }
}
