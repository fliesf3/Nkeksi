package tech.firefly.nkeksi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;

public class DeliveryAddress extends AppCompatActivity {

    MaterialSpinner delivery_drop;
    EditText quarterText;
    String town = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);

        delivery_drop = (MaterialSpinner) findViewById(R.id.town_drop_delivery);
        delivery_drop.setItems("Choose City", "Yaounde");
        quarterText = (EditText) findViewById(R.id.quarter_text);


        delivery_drop.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                town = item;
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        delivery_drop.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });


    }

    public void SaveDeliveryAddress(View v) {
        if (TextUtils.isEmpty(quarterText.getText().toString())) {
            Toast.makeText(this, "Enter Quarter", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(500).playOn(v);
            return;
        }
        if (TextUtils.isEmpty(town) || town.equalsIgnoreCase("Choose City")) {
            Toast.makeText(this, "Pick A Town", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(500).playOn(v);
            return;
        }

        HashMap<String,Object> map = new HashMap<>();
        map.put("town",town);
        map.put("quarter",quarterText.getText().toString().trim());
        YoYo.with(Techniques.RubberBand).duration(500).playOn(v);
        FirebaseDatabase.getInstance().getReference().child("User Cart")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("Info").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(DeliveryAddress.this,HomeTemp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DeliveryAddress.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
