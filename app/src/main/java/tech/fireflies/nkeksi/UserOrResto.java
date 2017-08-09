package tech.fireflies.nkeksi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class UserOrResto extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userorresto);
    }
    public void UserOnly(View v){
        startActivity(new Intent(this,GettingStartedUser.class));
    }
    public void RestoOnly(View v){
        startActivity(new Intent(this,GetStartedResto.class));
    }
}
