package tech.fireflies.nkeksi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterSecondResto extends AppCompatActivity {

    EditText firstName, lastName, phoneText;
    EditText restoName, restAddress, restoPhoneText,adminPhone;
    Spinner locationSpinner;
    ImageButton nextButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference writeLocation;


    ProgressDialog progressDialog;
    String[] location = {"Buea", "Douala", "Yaounde", "Limbe"};
    String locationSelected = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_second_resto);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Signing Up");
        progressDialog.setMessage("Setting Up Account");

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.showOverflowMenu();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("unVerified");
        writeLocation = firebaseDatabase.getReference().child("locations");



        locationSpinner = (Spinner) findViewById(R.id.resto_location_register);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                       locationSelected = locationSpinner.getSelectedItem().toString();
                       break;
                    case 1:
                        locationSelected = locationSpinner.getSelectedItem().toString();
                        break;
                    case 2:
                        locationSelected = locationSpinner.getSelectedItem().toString();
                        break;
                    case 3:
                        locationSelected = locationSpinner.getSelectedItem().toString();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, location);
        locationSpinner.setAdapter(adapter);

        firstName = (EditText) findViewById(R.id.admin_first_name_register);
        lastName = (EditText) findViewById(R.id.admin_last_name_register);
        phoneText = (EditText) findViewById(R.id.resto_phone_number_register);

        restoName = (EditText) findViewById(R.id.resto_name_register);
        adminPhone = (EditText) findViewById(R.id.admin_phone_number_register);
        restAddress = (EditText) findViewById(R.id.resto_address_register);
        locationSpinner = (Spinner) findViewById(R.id.resto_location_register);

        nextButton = (ImageButton) findViewById(R.id.nextButton);
    }

    public void SecondRegistration(View v) {
        String first, last, phone;
        String restoNames,restoAddresses,adminPhones;
        first = firstName.getText().toString().trim();
        last = lastName.getText().toString().trim();
        phone = phoneText.getText().toString().trim();

        restoNames = restoName.getText().toString().trim();
        restoAddresses = restAddress.getText().toString().trim();
        adminPhones = adminPhone.getText().toString().trim();

        if (TextUtils.isEmpty(first) || TextUtils.isEmpty(last) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(restoNames)||TextUtils.isEmpty(restoAddresses)
                ||TextUtils.isEmpty(adminPhones)) {
            Toast.makeText(this, "Enter All Fields To Continue", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.StandUp).duration(500).playOn(nextButton);
            return;
        }
        if(locationSelected==null){
            Toast.makeText(this, "Location Null", Toast.LENGTH_SHORT).show();
            return;
        }
        if(locationSelected.equalsIgnoreCase("Buea")){
            writeLocation.child("Buea").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        if(locationSelected.equalsIgnoreCase("Douala")){
            writeLocation.child("Douala").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        if(locationSelected.equalsIgnoreCase("Yaounde")){
            writeLocation.child("Yaounde").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

        }
        if(locationSelected.equalsIgnoreCase("Limbe")){
            writeLocation.child("Limbe").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

        }

        progressDialog.show();

        HashMap<String, String> map = new HashMap<>();
        map.put("restoName",restoNames );
        map.put("restoAddress",restoAddresses);
        map.put("restoPhoneNumber", String.valueOf(phone));
        map.put("firstName", first);
        map.put("lastName", last);
        map.put("adminPhone",adminPhones);
        map.put("isResto", "true");
        map.put("isComplete", "false");
        map.put("isPaid", "false");
        map.put("restoLocation",locationSelected);
        map.put("restEmail",FirebaseAuth.getInstance().getCurrentUser().getEmail());

        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RegisterSecondResto.this, "Welcome", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
                startActivity(new Intent(RegisterSecondResto.this, Home.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterSecondResto.this, "" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
