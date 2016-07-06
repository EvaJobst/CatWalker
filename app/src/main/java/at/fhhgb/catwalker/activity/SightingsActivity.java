package at.fhhgb.catwalker.activity;

import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.TypefaceSpan;
import at.fhhgb.catwalker.data.LocalData;
import at.fhhgb.catwalker.data.Post;
import at.fhhgb.catwalker.firebase.DataModel;
import at.fhhgb.catwalker.firebase.ServiceLocator;

public class SightingsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "Google Services Test";
    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sightings);

        // Custom Font for Title
        SpannableString s = new SpannableString(getSupportActionBar().getTitle());
        s.setSpan(new TypefaceSpan(this, "Champagne_Limousines-Thick.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(s);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.i(TAG, "Map is ready!");

        HashMap posts = ServiceLocator.getDataModel().getLocalData().getAllPostsList();

        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

        boolean hasPoints = false;
        for(Object post : posts.values()) {
            Post p = (Post) post;
            double lon = ((Post) post).getLongitude();
            double lat = ((Post) post).getLatitude();
            if(lon != 0 && lat!=0) {
                LatLng latlng = new LatLng(lat, lon);
                hasPoints = true;
                latLngBuilder.include(latlng);
                mMap.addMarker(new MarkerOptions().position(latlng));
            }
        }

        if(latLngBuilder != null && hasPoints) {
            LatLngBounds bounds = latLngBuilder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


        }

        //mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Catwalker","Resume Sightings");
        DataModel model = ServiceLocator.getDataModel();
        LocalData data = model.getLocalData();
        data.restorePreferences(this);
        model.addAllListeners(data.getUserId(), data.getUniversityId());
    }

    @Override
    protected void onPause() {
        Log.d("Catwalker","Pause Sightings");
        DataModel model = ServiceLocator.getDataModel();
        LocalData data = model.getLocalData();
        model.removeAllListeners(data.getUserId(), data.getUniversityId());
        super.onPause();

    }
}
