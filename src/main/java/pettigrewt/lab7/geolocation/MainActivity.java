package pettigrewt.lab7.geolocation;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtain reference to LocationManager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //set location criteria
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);


        //obtain reference to location provider
        String provider = locationManager.getBestProvider(criteria,true);
        Location location = locationManager.getLastKnownLocation(provider);
        updateWithNewLocation(location);

        //add automated location updates
        locationManager.requestLocationUpdates(provider,2000,10,new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateWithNewLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    private void updateWithNewLocation(Location location){
        TextView myLocationText = (TextView) findViewById(R.id.myLocationText);

        String latLongString = "No location found";
        String addressString = "No address found";

        if(location !=null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            latLongString = "Lat:" +latitude + "\nLong:" + longitude;

            Geocoder gc= new Geocoder(this, Locale.getDefault());

            if(!Geocoder.isPresent()){
                addressString = "No geocoder available";
            }else{
                try{
                    List<Address> addresses = gc.getFromLocation(latitude,longitude,1);
                    StringBuilder sb = new StringBuilder();

                    if(addresses.size() > 0){
                        Address address = addresses.get(0);

                        for(int i = 0; i < address.getMaxAddressLineIndex(); i++){
                            sb.append(address.getAddressLine(i)).append("\n");
                            sb.append(address.getLocality()).append("\n");
                            sb.append(address.getPostalCode()).append("\n");
                            sb.append(address.getCountryName()).append("\n");
                        }

                        addressString = sb.toString();
                    }
                }catch (IOException e){
                    Log.d("Track me","IO Exception thrown");

                }
            }
        }

        myLocationText.setText("Your current position is:\n" + latLongString + "\n\n" + addressString);

    }


}
