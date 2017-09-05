package tech.firefly.nkeksi.app;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by RamsonB on 02-Sep-17.
 */

public class CacheApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(FirebaseAuth.getInstance()!=null)
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
