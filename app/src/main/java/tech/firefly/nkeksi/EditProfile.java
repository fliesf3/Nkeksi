package tech.firefly.nkeksi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;


public class EditProfile extends AppCompatActivity {

    EditText firstName,lastName,phoneText;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;
    CircularImageView imageView;
    Button save;
    StorageReference userStoreRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        imageView = (CircularImageView)findViewById(R.id.user_image_edit);
        firstName = (EditText)findViewById(R.id.first_name_edit);
        lastName = (EditText)findViewById(R.id.last_name_edit);
        phoneText = (EditText)findViewById(R.id.phone_edit);
        save = (Button)findViewById(R.id.save_edit);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference().child("UserInfo").child(user.getUid());
        userStoreRef = FirebaseStorage.getInstance().getReference().child("Profile").child(user.getUid());

        userRef.child("firstName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                firstName.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userRef.child("lastName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lastName.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userRef.child("phoneNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                phoneText.setText(String.valueOf(dataSnapshot.getValue(Long.class)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userRef.child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class)!=null)
                Picasso.with(EditProfile.this).load(dataSnapshot.getValue(String.class)).into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void SaveDetails(View v){
        String first,last;
        Long phone;
        first = firstName.getText().toString().trim();
        last = lastName.getText().toString().trim();
        phone = Long.parseLong(phoneText.getText().toString().trim());

        if(TextUtils.isEmpty(first)||TextUtils.isEmpty(last)|| phone ==null){
            YoYo.with(Techniques.Shake).duration(500).playOn(save);
            Toast.makeText(this, "Fill All Fields Before Continuing", Toast.LENGTH_SHORT).show();
            return;
        }


        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(first+" "+last).build();
        user.updateProfile(request);

        userRef.child("firstName").setValue(first);
        userRef.child("lastName").setValue(last);
        userRef.child("phoneNumber").setValue(phone).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditProfile.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(EditProfile.this,UserProfile.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Could Not Save Check Network", Toast.LENGTH_SHORT).show();
            }
        });




    }

    public void ChangePic(View v){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode == RESULT_OK){
            final Uri uri = data.getData();
            Bitmap thumbBit = null;
            try {
                thumbBit = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumbBit.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                UploadTask uploadTask = userStoreRef.putBytes(bytes);
                final ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
                progressDialog.setTitle("Uploading Pic");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(uri).build();
                user.updateProfile(request);

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage(progress + "" + "% Uploaded");
                        progressDialog.show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String link = taskSnapshot.getDownloadUrl().toString();
                        firebaseDatabase.getReference().child("UserInfo").child(user.getUid()).child("image").setValue(link);
                        Toast.makeText(EditProfile.this, "Photo Changed successfully", Toast.LENGTH_SHORT).show();
                        imageView.setImageURI(uri);
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Profile Pic Not Changed Try Again", Toast.LENGTH_SHORT).show();

                    }
                });

            } catch (Exception e) {
                Toast.makeText(this, "Error "+e, Toast.LENGTH_SHORT).show();
            }


        }
    }
}
