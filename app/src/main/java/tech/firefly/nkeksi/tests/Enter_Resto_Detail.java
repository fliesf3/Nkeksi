package tech.firefly.nkeksi.tests;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import tech.firefly.nkeksi.R;

public class Enter_Resto_Detail extends AppCompatActivity {

    EditText availableText, itemNameText, restoNameText, priceText,
            descriptionText, picText, mealCatText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_resto_detail);

        availableText = (EditText) findViewById(R.id.availabitity_temp);
        itemNameText = (EditText) findViewById(R.id.item_name_temp);
        restoNameText = (EditText) findViewById(R.id.name_temp);
        priceText = (EditText) findViewById(R.id.price_temp);
        descriptionText = (EditText) findViewById(R.id.description_temp);
        picText = (EditText) findViewById(R.id.pic_temp);
        mealCatText = (EditText) findViewById(R.id.meal_cat_temp);
    }

    public void SaveInfoTemp(View v) {
        String availability = availableText.getText().toString();
        String itemName = itemNameText.getText().toString();
        final String restoName = restoNameText.getText().toString();
        String price = priceText.getText().toString();
        String description = descriptionText.getText().toString();
        String pic = picText.getText().toString();
        String cat = mealCatText.getText().toString();

        HashMap<String, Object> saveMap = new HashMap<>();
        saveMap.put("availability", Boolean.valueOf(availability));
        saveMap.put("item", itemName);
        saveMap.put("restoName", restoName);
        saveMap.put("price", Integer.parseInt(price));
        saveMap.put("description", description);
        saveMap.put("pic", pic);
        saveMap.put("cat", cat);

        FirebaseDatabase.getInstance().getReference().child("TempMenu").push().setValue(saveMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        availableText.setText("");
                        itemNameText.setText("");
                        restoNameText.setText("");
                        priceText.setText("");
                        descriptionText.setText("");
                        picText.setText("");
                        mealCatText.setText("");
                        Toast.makeText(Enter_Resto_Detail.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Enter_Resto_Detail.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
