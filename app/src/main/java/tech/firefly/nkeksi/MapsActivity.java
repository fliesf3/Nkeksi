package tech.firefly.nkeksi;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    boolean firstTime = true;
    DatabaseReference locationPoint;
    Double latNew,lngNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationPoint = FirebaseDatabase.getInstance().getReference().child("unVerified").child("adam");

        locationPoint.child("lat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                latNew = dataSnapshot.getValue(Double.class);
                Toast.makeText(MapsActivity.this, ""+latNew, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        locationPoint.child("long").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lngNew = dataSnapshot.getValue(Double.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Location Permission Not Granted", Toast.LENGTH_SHORT).show();
            return;
        }
        mMap.setMyLocationEnabled(true);
        if(latNew!=null&&lngNew!=null) {
            LatLng gotLoc = new LatLng(latNew, lngNew);
            mMap.addMarker(new MarkerOptions().position(gotLoc).title("Firebase Loc"));
        }else{
            Toast.makeText(this, "Lat Gotten "+latNew, Toast.LENGTH_SHORT).show();
        }

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                double latStart = mMap.getMyLocation().getLatitude();
                double lngStart = mMap.getMyLocation().getLongitude();

                float[] RESULT = new float[10];

                Toast.makeText(MapsActivity.this, "Start Lat = "+latStart+" Start Long = "+lngStart, Toast.LENGTH_SHORT).show();
                Toast.makeText(MapsActivity.this, "New Lat = "+latNew+" New Long = "+lngNew, Toast.LENGTH_SHORT).show();


                Location.distanceBetween(latStart,lngStart,latNew,latNew,RESULT);
                Toast.makeText(MapsActivity.this, "Calculated Distance = "+RESULT[0], Toast.LENGTH_SHORT).show();
                LatLng myLoc = new LatLng(latStart,lngStart);
                mMap.addMarker(new MarkerOptions().position(myLoc).title("This Is Me").visible(true));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc,16));

                return true;
            }
        });

    }
}
