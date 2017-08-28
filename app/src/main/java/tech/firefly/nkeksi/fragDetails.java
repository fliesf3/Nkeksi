package tech.firefly.nkeksi;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class fragDetails extends Fragment {

    ImageView restoImage;
    TextView restoName, restoAddress,restoDesc;
    DatabaseReference restoRefDash;
    String key = "";

    public fragDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        key = getActivity().getIntent().getStringExtra("key");

        restoImage = (ImageView) view.findViewById(R.id.resto_image_wall);
        restoName = (TextView) view.findViewById(R.id.resto_name_dash);
        restoAddress = (TextView) view.findViewById(R.id.resto_address_dash);
        restoDesc = (TextView)view.findViewById(R.id.resto_desc_dash);

        if (key != null && !key.isEmpty()) {
            restoRefDash = FirebaseDatabase.getInstance().getReference().child("unVerified").child(key);



            restoRefDash.child("restoName").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue(String.class) != null && !dataSnapshot.getValue(String.class).isEmpty())
                        restoName.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            restoRefDash.child("restoAddress").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(String.class)!=null && !dataSnapshot.getValue(String.class).isEmpty())
                    restoAddress.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            restoRefDash.child("restoImage").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String link = dataSnapshot.getValue(String.class);
                    if (link != null && !link.isEmpty()) {
                        Picasso.with(getActivity()).load(link).networkPolicy(NetworkPolicy.OFFLINE).into(restoImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getActivity()).load(link).into(restoImage);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            restoRefDash.child("restaurant_description").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(String.class)!=null && !dataSnapshot.getValue(String.class).isEmpty())
                    restoDesc.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
