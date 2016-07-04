package at.fhhgb.catwalker.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.activity.NewPostActivity;

public class FragmentLocation extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
    MapView mView;
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest lr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate and return the layout
        View v = inflater.inflate(R.layout.fragment_location, container, false);

        // Set up Map
        mView = (MapView) v.findViewById(R.id.new_mapView);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
        mGoogleApiClient = new GoogleApiClient.Builder(mView.getContext())
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
        }

        mView.onCreate(savedInstanceState);
        mView.getMapAsync(this);
        return v;
        }

@Override
public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        }

@Override
public void onStart() {
    super.onStart();
}

@Override
public void onResume() {
    super.onResume();
    mView.onResume();
}

@Override
public void onPause() {
    super.onPause();
    mView.onPause();
}

@Override
public void onDestroy() {
    super.onDestroy();
    mView.onDestroy();
}

@Override
public void onLowMemory() {
    super.onLowMemory();
    mView.onLowMemory();
}

public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mGoogleApiClient.connect();
}


@Override
public void onConnected(@Nullable Bundle bundle) {
    if(ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        mMap.setMyLocationEnabled(true);
        lr = new LocationRequest().setInterval(1000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);;
        //Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //todo: fix null pointer exception
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, lr, this);
    }
}

@Override
public void onConnectionSuspended(int i) {}

@Override
public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

@Override
public void onLocationChanged(Location location) {
    NewPostActivity.loc = location;

    if(mMap.getCameraPosition().zoom < 3) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
    }
}
}