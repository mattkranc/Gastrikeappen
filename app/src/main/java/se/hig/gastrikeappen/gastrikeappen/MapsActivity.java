package se.hig.gastrikeappen.gastrikeappen;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback  {

    Double myLatitude = null;
    Double myLongitude = null;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mOjarenLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    boolean locSet = false;
    boolean showMes = true;
    boolean showMes2 = true;
    boolean zoomIn = true;
    final LatLng ojarenLatLng = new LatLng(60.672884, 16.837481);

    private static final int REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        /*googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(2 * 1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
*/


    }



    public void checkForLocationSettings() {

        try {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
            builder.addLocationRequest(mLocationRequest);
            SettingsClient settingsClient = LocationServices.getSettingsClient(MapsActivity.this);

            settingsClient.checkLocationSettings(builder.build())
                    .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    //Seftting is success...
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Location Permission already granted
                        //Location Permission already granted
                        zoomIn = true;
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                        zoomIn = true;


                        //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    }


               }
            })
                .addOnFailureListener(MapsActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(MapsActivity.this, REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE);
                            } catch (IntentSender.SendIntentException sie) {
                                sie.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Toast.makeText(MapsActivity.this, "Setting change is not available.Try in another device.", Toast.LENGTH_LONG).show();

                    }

                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2 * 1000); // two sek
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        final LatLng ojarenLatLng = new LatLng(60.672884, 16.837481);

        addMarker("Öjaren", ojarenLatLng);


        /*MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ojarenLatLng);
        markerOptions.title("Öjaren");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mOjarenLocationMarker = mGoogleMap.addMarker(markerOptions);
        */

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                //Location Permission already granted

                checkForLocationSettings();
                        //Toast.makeText(MapsActivity.this, "Setloc " + locSet, Toast.LENGTH_LONG).show();


                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);

                    //if location settings is granted
                //mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                //mGoogleMap.setMyLocationEnabled(true);




            } else {
                //Request Location Permission
                checkLocationPermission();
            }


        }
        else {

            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }



    }




    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;


                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();

                final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


                //move map camera

                if (zoomIn) {

                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    zoomIn = false;
                }



                circleRadius(ojarenLatLng);

                //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }
    };


    public void addMarker(String title, LatLng markerLatLng) {
        //final LatLng ojarenLatLng = new LatLng(60.672884, 16.837481);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(markerLatLng);
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mOjarenLocationMarker = mGoogleMap.addMarker(markerOptions);
    }

    public void circleRadius(LatLng markerLatLng) {
        Circle circle = mGoogleMap.addCircle(new CircleOptions().center(markerLatLng).radius(10000).strokeColor(Color.RED));
        float[] distanceToPOI = new float[2];
        Location.distanceBetween(myLatitude, myLongitude, circle.getCenter().latitude, circle.getCenter().longitude, distanceToPOI);
        if (circle.getRadius() >= distanceToPOI[0] && showMes) {

            // Innanför cirkelradien
            Toast.makeText(MapsActivity.this, "Innanför cirkeln!", Toast.LENGTH_SHORT).show();
            showMes = false;
            showMes2 = true;
        } else if (circle.getRadius() < distanceToPOI[0] && showMes2) {
            Toast.makeText(getApplicationContext(), "Utanför cirkeln!", Toast.LENGTH_SHORT).show();
            showMes = true;
            showMes2 = false;
        }

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        checkForLocationSettings();
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    // Onstart, onresume etc lifecycle methods
    /*
    @Override

    protected void onStart() {
        super.onStart();
        googleApiClient.connect();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }
*/
}
