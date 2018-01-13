package tech.firefly.nkeksi.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import cn.pedant.SweetAlert.SweetAlertDialog;
import tech.firefly.nkeksi.ui.HomeTemp;
import tech.firefly.nkeksi.user.DeliveryAddress;
import tech.firefly.nkeksi.R;

public class GettingStartedUser extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth;
    SignInButton googleBtn;
    private static final int RC_SIGN_IN = 123;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    DBCheck();
                }
                else{
                    setContentView(R.layout.activity_getting_started_user);
                    houseKeeping();
                }
            }
        };


    }

    private void DBCheck() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DeathSwitchApp").child("v1*3");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Boolean.class) != null) {

                    if (dataSnapshot.getValue(Boolean.class)) {
                        SweetAlertDialog pDialog = new SweetAlertDialog(GettingStartedUser.this,
                                SweetAlertDialog.ERROR_TYPE);
                        pDialog.setTitleText("Oops...").setCanceledOnTouchOutside(false);
                        pDialog.setCancelable(false);
                        pDialog.setContentText("App Version Too Old!!")
                                .setConfirmText("Update")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("market://details?id=tech.firefly.nkeksi"));
                                        startActivity(intent);
                                    }
                                })
                                .show();

                    }
                    if (!dataSnapshot.getValue(Boolean.class)) {
                        setContentView(R.layout.activity_getting_started_user);
                        houseKeeping();
                        startActivity(new Intent(GettingStartedUser.this, DeliveryAddress.class));
                        finish();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void houseKeeping() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleBtn = (SignInButton) findViewById(R.id.googleSignIn);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                //Toast.makeText(GettingStartedUser.this, "Sign in", Toast.LENGTH_SHORT).show();

            }

        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(GettingStartedUser.this, "Welcome Back " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(GettingStartedUser.this, RegisterSecondUser.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GettingStartedUser.this, "Failed " + e, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void RegisterBtn(View v) {
        startActivity(new Intent(this, RegisterUser.class));
        YoYo.with(Techniques.RubberBand).duration(500).playOn((Button) findViewById(R.id.registerBtn));
    }

    public void SignInBtn(View v) {
        startActivity(new Intent(this, LoginUser.class));
        YoYo.with(Techniques.RubberBand).duration(500).playOn((Button) findViewById(R.id.signInBtn));
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

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
