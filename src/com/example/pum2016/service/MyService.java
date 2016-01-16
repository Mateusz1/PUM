package com.example.pum2016.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.pum2016.asynctasks.DataBaseTaskNonQuery;
import com.example.pum2016.klasy.TableOptionsObject;
import com.example.pum2016.queries.QueriesStatic;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.os.Bundle;
import android.provider.Settings;

public class MyService extends IntentService implements LocationListener {

	public MyService() {
		super("Us³uga kontroli pracownika");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i("My Test Service", "Service running");
		sendInformation();
	}
	private LocationManager mLocationManager;

	private void sendInformation() {
		Log.i("My Test Service", "Insert");
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Location location = getLastKnownLocation();
			if (location != null) {
				onLocationChanged(location);
            }
		
		}
		else {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}
	private Location getLastKnownLocation() {
	    mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
	    List<String> providers = mLocationManager.getProviders(true);
	    Location bestLocation = null;
	    for (String provider : providers) {
	        Location l = mLocationManager.getLastKnownLocation(provider);
	        if (l == null) {
	            continue;
	        }
	        if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
	            // Found best last known location: %s", l);
	            bestLocation = l;
	        }
	    }
	    return bestLocation;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.i("long",Double.toString(location.getLongitude()));
		Log.i("lat",Double.toString(location.getLatitude()));
		SendPointsToDatabase task = new SendPointsToDatabase();
		String sql = QueriesStatic.getInsertevent1()+TableOptionsObject.getInstance().getPrefix()
				+QueriesStatic.getInsertpoints2()+"("+TableOptionsObject.getInstance().getId_trasy()+","
				+location.getLongitude()+","+location.getLatitude()+",'"+getData()+"')";
		task.execute(new String[]{sql});	
		
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

	private String getData() {
		SimpleDateFormat fmt = new SimpleDateFormat("MM-dd-yyyy HH:mm");
		Date inputDate = new Date();
		fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = fmt.format(inputDate);
		return dateString;
	}

	
	private class SendPointsToDatabase extends DataBaseTaskNonQuery {
		@Override
		protected void onPostExecute(Integer result) {
			switch(result) {
			case 0:
				break;
			case 1:
				break;
			default:
				break;				
			}
		}
	}

}
