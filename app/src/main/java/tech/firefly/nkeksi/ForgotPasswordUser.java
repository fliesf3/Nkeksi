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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPasswordUser extends AppCompatActivity {

        EditText forgotEmail;
        ProgressDialog progressDialog;
        FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_user);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Reset Password");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.showOverflowMenu();

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Rest");
        progressDialog.setMessage("Sending You A Rest Link");

        forgotEmail = (EditText) findViewById(R.id.forgot_email);


    }

    public void SendReset(View v){
        String email = forgotEmail.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email Field Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ForgotPasswordUser.this, "Email Sent. Please Check Your Email", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
                startActivity(new Intent(ForgotPasswordUser.this,LoginUser.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPasswordUser.this, "Failed To Send Link. Check Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
    });

    }

}
