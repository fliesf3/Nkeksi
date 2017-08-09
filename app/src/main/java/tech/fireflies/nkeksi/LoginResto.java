package tech.fireflies.nkeksi;

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

public class LoginResto extends AppCompatActivity {

    EditText emailText,passText;
    Button signResto;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    DatabaseReference checkIfResto;
    DatabaseReference checkIfRestoSecond;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_resto);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sign In");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.showOverflowMenu();

        auth = FirebaseAuth.getInstance();
        checkIfResto = FirebaseDatabase.getInstance().getReference().child("UserInfo");
        checkIfRestoSecond  = FirebaseDatabase.getInstance().getReference().child("unVerified");

        emailText = (EditText) findViewById(R.id.email_signin_resto);
        passText = (EditText) findViewById(R.id.pass_signin_resto);
        signResto = (Button) findViewById(R.id.signInButtonResto);

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
            YoYo.with(Techniques.Shake).duration(500).playOn(signResto);
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Input Password", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(500).playOn(signResto);
            return;
        }
        if(password.length()<6){
            Toast.makeText(this, "Password Must Be At Least 6 Characters Long", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(500).playOn(signResto);
            return;
        }

        progressDialog.show();
        YoYo.with(Techniques.RubberBand).duration(500).playOn(signResto);

        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialog.dismiss();
                Toast.makeText(LoginResto.this, "Success", Toast.LENGTH_SHORT).show();
                checkIfResto.child(auth.getCurrentUser().getUid()).child("isResto").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String val = dataSnapshot.getValue(String.class);
                        if(val!=null){
                           if(val.equalsIgnoreCase("true")){
                                Toast.makeText(LoginResto.this, "Yay You Are A Restaurant", Toast.LENGTH_SHORT).show();

                            }
                            if(val.equalsIgnoreCase("false")){
                               Toast.makeText(LoginResto.this, "Yay You Are A User", Toast.LENGTH_SHORT).show();
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
                        String val = dataSnapshot.getValue(String.class);
                        if(val!=null){
                            if(val.equalsIgnoreCase("true")){
                                Toast.makeText(LoginResto.this, "Yay You Are A Restaurant", Toast.LENGTH_SHORT).show();

                            }
                            if(val.equalsIgnoreCase("false")){
                                Toast.makeText(LoginResto.this, "Yay You Are A User", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                finish();
                startActivity(new Intent(LoginResto.this,Home.class));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginResto.this, ""+e, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
