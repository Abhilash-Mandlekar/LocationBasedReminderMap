package com.locationbasedreminder.chinmay.maps4;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode.GeoPoint;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by CHINMAY on 05-09-2015.
 */
public class AddressLoc extends Activity
{

    EditText etSearch;
    Button bSearch , bStop;
    TextView tvLatLong;
    MediaPlayer alarmTune;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_loc);

        alarmTune = MediaPlayer.create(this,R.raw.london);
        alarmTune.start();
        alarmTune.start();

        etSearch = (EditText)findViewById(R.id.etSearch);
        bSearch = (Button)findViewById(R.id.bSearch);
        tvLatLong = (TextView)findViewById(R.id.tvLatLong);
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String addr=bSearch.getText().toString();
                 LatLng p2=getLocationFromAddress(addr);
                //float dist = Location.distanceBetween(21,81,22,82);
                 tvLatLong.setText("Your Lati,long -->"+p2.latitude+"  "+p2.longitude);
            }
        });

        bStop =(Button)findViewById(R.id.bStop);
        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmTune.stop();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        alarmTune.stop();

    }

    public LatLng getLocationFromAddress(String addr)
    {
        Geocoder coder =new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        LatLng pos1=null;
        //GeoPoint p1=null;
        try
        {
            addresses = coder.getFromLocationName(addr,5,19.9374,77.5278,21.7896,80.6625);    //  UPGRDE ABLE

        }catch(IOException e){
            addresses=null;
        }
        if(addresses != null)
        {
            Address loc = addresses.get(0);
          // p1=new GeoPoint(1,loc.getLatitude(),loc.getLongitude());
               pos1= new LatLng(loc.getLatitude(),loc.getLongitude());

        }
        return pos1;
        }


}
