package tech.firefly.nkeksi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Random;


public class Splash extends AppCompatActivity {

    TextView appTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logoDisplay = (ImageView) findViewById(R.id.logoDisplay);
        appTitle = (TextView) findViewById(R.id.app_title);

        YoYo.with(Techniques.RotateInDownLeft).duration(1500).playOn(appTitle);
        YoYo.with(Techniques.Shake).duration(1000).delay(1500).playOn(appTitle);

        int[] logo = {R.drawable.splash,R.drawable.splash1,R.drawable.splash2};
        Random random = new Random();
        int range = random.nextInt(logo.length);
        logoDisplay.setImageResource(logo[range]);

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                    startActivity(new Intent(Splash.this,GettingStartedUser.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }
}
