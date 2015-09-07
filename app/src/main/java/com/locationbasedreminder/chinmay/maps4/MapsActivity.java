package com.locationbasedreminder.chinmay.maps4;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.location.LocationServices;
import android.location.LocationManager;
import android.content.Context;
import android.location.LocationListener;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
         ConnectionCallbacks, OnConnectionFailedListener{

    GoogleMap mMap; // Might be null if Google Play services APK is not available.
    boolean mReady=false;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    TextView tvCurrentPos;
    MarkerOptions dragableMarker,myCurrentLocMarker;
    Location myCurrentLoc;
    LocationManager locationManager;
    String dragableMarkerTitle="dragable";
    Location dest;
    float dist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        final Button map1=(Button) findViewById(R.id.bMap);
        map1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReady) {
                    //map1.setText("jy7676");
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }

            }
        });

        final Button sat=(Button) findViewById(R.id.bSatellite);
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReady) {
                    // sat.setText("sat is work");
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }


            }
        });

        final Button addr=(Button) findViewById(R.id.bAddress);
        addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSearchActivity = new Intent("com.locationbasedreminder.chinmay.maps4.ADDRESSLOC");
                startActivity(openSearchActivity);

            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LatLng seattle = new LatLng(42.6204,-122.3491);
        MarkerOptions get_pos = new MarkerOptions()
                .position(seattle)
                .title("Seattle")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_setting_dark));
        get_pos.draggable(true);
        LatLng current_pos = get_pos.getPosition();
         tvCurrentPos = (TextView)findViewById(R.id.tvCurrentPos);
        tvCurrentPos.setText(current_pos.latitude+"  "+current_pos.longitude);
tvCurrentPos.setText("before error");
       // getLocation();
        //this code is nice
    myCurrentLoc = getLocation();
        dest=new Location("temp");
        dest.setLatitude(myCurrentLoc.getLatitude()-0.5);
        dest.setLongitude(myCurrentLoc.getLongitude()-0.5);
        myCurrentLocMarker = new MarkerOptions().position(new LatLng(myCurrentLoc.getLatitude(),myCurrentLoc.getLongitude()))
                                                .title("you are here");

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                tvCurrentPos.setText("changedLoc :" + location.getLatitude() + " " + location.getLongitude());
                myCurrentLoc.setLatitude(location.getLatitude());
                myCurrentLoc.setLongitude(location.getLongitude());
                myCurrentLocMarker = new MarkerOptions().position(new LatLng(myCurrentLoc.getLatitude(),myCurrentLoc.getLongitude()))
                        .title("you are here");
                dist = myCurrentLoc.distanceTo(dest);
                if(dist<1000)
                {
                    Intent openSearchActivity = new Intent("com.locationbasedreminder.chinmay.maps4.ADDRESSLOC");
                    startActivity(openSearchActivity);

                }


            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        if(myCurrentLoc !=null)
        tvCurrentPos.setText(myCurrentLoc.getLongitude()+"  "+ myCurrentLoc.getLatitude());
        else
            tvCurrentPos.setText("location is null");
         dragableMarker = new MarkerOptions().position(new LatLng(myCurrentLoc.getLatitude()-0.5, myCurrentLoc.getLongitude()-0.5))
                .title(dragableMarkerTitle).draggable(true);
        GoogleMap myMap =mapFragment.getMap();

        myMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {



            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                             LatLng dragedTo= marker.getPosition();

                 dest = new Location("Destination");
                dest.setLatitude(dragedTo.latitude);
                dest.setLongitude(dragedTo.longitude);


                  tvCurrentPos.setText("Draged to: "+dist);
             dragableMarkerTitle=String.valueOf(dist);
                dragableMarker.title(dragableMarkerTitle);

            }
        });

        // google's code
     /*   mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();                       */
       // tvCurrentPos.setText("after error");

//mapFragment.getMapAsync(this);
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }*/

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
   /* private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    /*private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
*/
    public void onMapReady(GoogleMap googleMap) {
      mReady=true;
        mMap=googleMap;
        LatLng nagpur=new LatLng(myCurrentLoc.getLatitude(), myCurrentLoc.getLongitude());
        CameraPosition target=CameraPosition.builder().target(nagpur).zoom(14).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition((target)));

        mMap.addMarker(dragableMarker);
        mMap.addMarker(myCurrentLocMarker);
    }

    @Override
    public void onConnected(Bundle bundle) {
        tvCurrentPos.setText("onConnected called");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        System.out.println(mLastLocation);
        if (mLastLocation != null) {
            tvCurrentPos.setText(mLastLocation.getLongitude()+"  "+mLastLocation.getLatitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        tvCurrentPos.setText("connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        tvCurrentPos.setText("connection failed");

    }

    public Location getLocation() {
         locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocationGPS != null) {
                //return lastKnownLocationGPS;
               return lastKnownLocationGPS;
            } else {
                Location loc =  locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                System.out.println("1::"+loc);//----getting null over here
                System.out.println("2::"+loc.getLatitude());
               return loc;
            }
        } else {
            return null;
        }






    }

    private void makeUseOfNewLocation(Location location)
    {

        tvCurrentPos.setText("new pos "+location.getLatitude()+"  "+location.getLongitude());
    }

}
/*
<fragment xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools" android:layout_width="match_parent"
    android:layout_height="match_parent" android:id="@+id/map" tools:context=".MapsActivity"
    android:name="com.google.android.gms.maps.MapFragment"
    tools:layout="@layout/abc_action_bar_title_item" />
 */