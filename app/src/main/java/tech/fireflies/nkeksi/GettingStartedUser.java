package tech.fireflies.nkeksi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GettingStartedUser extends AppCompatActivity {
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started_user);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(user !=null ){
                    startActivity(new Intent(GettingStartedUser.this,Home.class));
                    finish();
                }
            }
        };
    }

    public void RegisterBtn(View v){
        startActivity(new Intent(this,RegisterUser.class));
        YoYo.with(Techniques.RubberBand).duration(500).playOn((Button)findViewById(R.id.registerBtn));
    }

    public void SignInBtn(View v){
        startActivity(new Intent(this,LoginUser.class));
        YoYo.with(Techniques.RubberBand).duration(500).playOn((Button)findViewById(R.id.signInBtn));
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

}
