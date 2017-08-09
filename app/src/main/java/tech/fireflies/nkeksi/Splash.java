package tech.fireflies.nkeksi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.Random;


public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logoDisplay = (ImageView) findViewById(R.id.logoDisplay);

        int[] logo = {R.drawable.logo1,R.drawable.logo3};
        Random random = new Random();
        int range = random.nextInt(logo.length);
        logoDisplay.setImageResource(logo[range]);

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                    startActivity(new Intent(Splash.this,UserOrResto.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }
}
