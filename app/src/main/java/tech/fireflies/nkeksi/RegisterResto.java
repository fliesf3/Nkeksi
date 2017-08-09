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
import com.google.firebase.auth.ProviderQueryResult;


public class RegisterResto extends AppCompatActivity {

    EditText emailText,passwordText;
    Button register_btn;

    FirebaseAuth auth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

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

        auth = FirebaseAuth.getInstance();

        emailText = (EditText) findViewById(R.id.email_register);
        passwordText = (EditText) findViewById(R.id.password_register);
        register_btn = (Button) findViewById(R.id.registerButton);

    }
    public void RegisterButton(View v){

        final String email,password;

        email = emailText.getText().toString().trim();
        password = passwordText.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Input Email", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(500).playOn(register_btn);
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Input Password", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(500).playOn(register_btn);
            return;
        }
        if(password.length()<6){
            Toast.makeText(this, "Password Must Be At Least 6 Characters Long", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(500).playOn(register_btn);
            return;
        }

        progressDialog.show();
        YoYo.with(Techniques.RubberBand).duration(500).playOn((Button)findViewById(R.id.registerButton));

        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                finish();
                progressDialog.dismiss();
                startActivity(new Intent(RegisterResto.this,RegisterSecondResto.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                auth.fetchProvidersForEmail(email).addOnSuccessListener(new OnSuccessListener<ProviderQueryResult>() {
                    @Override
                    public void onSuccess(ProviderQueryResult providerQueryResult) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterResto.this, "Email already Exist", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
                Toast.makeText(RegisterResto.this, "Failed Check Network "+e, Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void SignInBtn(View v){
        startActivity(new Intent(this,LoginUser.class));
    }
}
