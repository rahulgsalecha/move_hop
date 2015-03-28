package com.example.test1;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    MockLocationProvider mock;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    
    protected LocationManager locationManager;
    
    protected Button retrieveLocationButton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ((getApplication().getApplicationInfo().flags & 
        	    ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
        mock = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, this);
        
        //Set test location
        mock.pushLocation(-12.35, 23.45);
        }
        
        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                MINIMUM_TIME_BETWEEN_UPDATES, 
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MyLocationListener()
        );
        
    retrieveLocationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentLocation();
            }
    });        
        
    }    

    protected void showCurrentLocation() {
    	
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
       
        if (location != null) {
            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(MainActivity.this, message,
                    Toast.LENGTH_LONG).show();
        }
        
        try {
        	  List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), 
        			  location.getLongitude(), 1);

        	  if(addresses != null) {
        	   Address returnedAddress = addresses.get(0);
        	   StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
        	   for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
        	    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
        	   }
        	   Toast.makeText(MainActivity.this,strReturnedAddress.toString(),Toast.LENGTH_LONG).show();
        	  }
        	  else{
        		  Toast.makeText(MainActivity.this,"No Address returned!",Toast.LENGTH_LONG).show();
        	  }
        	 } catch (IOException e) {
        	  // TODO Auto-generated catch block
        	  e.printStackTrace();
        	  Toast.makeText(MainActivity.this,"Cannot Get Address",Toast.LENGTH_LONG).show();
        }

    }   

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(MainActivity.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(MainActivity.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(MainActivity.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }

        
    }
    
    public class MockLocationProvider {
  	  String providerName;
  	  Context ctx;
  	 
  	  public MockLocationProvider(String name, Context ctx) {
  	    this.providerName = name;
  	    this.ctx = ctx;
  	 
  	    LocationManager lm = (LocationManager) ctx.getSystemService(
  	      Context.LOCATION_SERVICE);
  	    lm.addTestProvider(providerName, false, false, false, false, false, 
  	      true, true, 0, 5);
  	    lm.setTestProviderEnabled(providerName, true);
  	  }
  	 
  	  public void pushLocation(double lat, double lon) {
  	    LocationManager lm = (LocationManager) ctx.getSystemService(
  	      Context.LOCATION_SERVICE);
  	 
  	    Location mockLocation = new Location(providerName);
  	    mockLocation.setLatitude(lat);
  	    mockLocation.setLongitude(lon); 
  	    mockLocation.setAltitude(0); 
  	    mockLocation.setTime(System.currentTimeMillis()); 
  	    lm.setTestProviderLocation(providerName, mockLocation);
  	  }
  	 
  	  public void shutdown() {
  	    LocationManager lm = (LocationManager) ctx.getSystemService(
  	      Context.LOCATION_SERVICE);
  	    lm.removeTestProvider(providerName);
  	  }
  	}
    
    
    protected void onDestroy() {
    	   mock.shutdown();
    	   super.onDestroy();
    }
}


