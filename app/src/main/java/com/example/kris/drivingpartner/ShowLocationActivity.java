package com.example.kris.drivingpartner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;

/**
 * Created by kris on 8/25/16.
 */
public class ShowLocationActivity extends Activity implements LocationListener {

    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;
    public Location loc;

    boolean drunkTest;

    SharedPreferences sp;

    final int DELAY = 8000;
    String laman = null;
    final Handler handler = new Handler();
    final Context context = this;
    String position = null;
    String post = "Pengemudi sedang mabuk, lokasi tak dapat ditemukan";
    String phoneNo;   // = "089664521292";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);

        phoneNo = sp.getString("phone", "081381912899");

        drunkTest = getIntent().getExtras().getBoolean("drunkResult");

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);


        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
            send(position);


        } else {
            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");
            send(post);

        }


    }


    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 200, 1, this);

    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        float lat = (float) (location.getLatitude());
        float lng = (float) (location.getLongitude());
        String lati,longi;
        lati = Float.toString(lat);
        longi = Float.toString(lng);

        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));

        position = position(lati,longi);



    }




    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }







    public String position(String a, String b){

        String post = "Pengguna sedang mabuk, lokasi Pengguna sekarang " +
                "di sekitar :\n Latitude : " + a + ", Longitude :" + b;
        return post;
    }

    public void send(String msg){

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Intent intent = new Intent(context, SensorActivity.class);
        intent.putExtra("drunkReport", drunkTest);
        startActivity(intent);
        finish();

    }

}
