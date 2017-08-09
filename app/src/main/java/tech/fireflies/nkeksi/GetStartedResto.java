package tech.fireflies.nkeksi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by user on 07-Aug-17.
 */

public class GetStartedResto extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started_resto);
    }

    public void SignInBtnResto(View v){
        startActivity(new Intent(this,LoginResto.class));
    }

    public void RegisterBtnResto(View v){
        startActivity(new Intent(this,RegisterResto.class));
    }
}
