package com.example.myfirstandroidapp;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class UploadDistanceToHole extends Service implements LocationListener {
	
	private LocationManager locationManager;
	skinsGameInstance gameInstance;
	static final String UPDATE_DISTANCE = "update_distance";
	static int startId_;
	static Location lastLocation=null;
	static int numOfUpdates=0;
	
	private static double distance(double lat1, double lon1, double lat2, double lon2) {
	      double theta = lon1 - lon2;
	      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	      dist = Math.acos(dist);
	      dist = rad2deg(dist);
	      dist = dist * 60 * 1.1515;
	 
	      return (dist*1760);
	    //return (dist);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.v("UploadDistanceToHole", "onStartCommand");
		
		startId_ = startId;
		
		gameInstance = skinsGame.getInstance().getGame();
     
        //setContentView(R.layout.activity_main);
		
		if (intent != null){
    		String requestedAction = intent.getAction();
    		
    		if (requestedAction != null && requestedAction.equals(UPDATE_DISTANCE)){
    			/********** get Gps location service LocationManager object ***********/
    	        locationManager = (LocationManager) getSystemService(this.getApplicationContext().LOCATION_SERVICE);
    	         
    	        // CAL METHOD requestLocationUpdates
    	           
    	          // Parameters :
    	          //   First(provider)    :  the name of the provider with which to register
    	          //   Second(minTime)    :  the minimum time interval for notifications,
    	          //                         in milliseconds. This field is only used as a hint
    	          //                         to conserve power, and actual time between location
    	          //                         updates may be greater or lesser than this value.
    	          //   Third(minDistance) :  the minimum distance interval for notifications, in meters
    	          //   Fourth(listener)   :  a {#link LocationListener} whose onLocationChanged(Location)
    	          //                         method will be called for each location update    
    	        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
    	                3000,   // 3 sec
    	                10, this);
    	        
    	         
    	        /********* After registration onLocationChanged method  ********/
    	        /********* called periodically after each 3 sec ***********/
    	        return START_STICKY;
    		}
    		else
    		{
    			stopSelf(startId);
    			return this.START_NOT_STICKY;
    		}
		}
    		
    	return START_NOT_STICKY;
    }
     
    /************* Called after each 3 sec **********/
    @Override
    public void onLocationChanged(Location location) {
    	
    	Log.v("UploadDistanceToHole", "onLocationChanged");
        
        Location greenLocation = gameInstance.getLocationOfGreen();
        String distance_str = Integer.toString((int)distance(location.getLatitude(), location.getLongitude(), 
				greenLocation.getLatitude(), greenLocation.getLongitude()));
        CurrentMoodWidgetProvider.updateTextView(0, R.id.GetDistanceToGreen, distance_str);
        
     
        if(lastLocation!=null)
        {
        	// update hit distance
        	String hit_distance_str = Integer.toString((int)distance(location.getLatitude(), location.getLongitude(), 
        			lastLocation.getLatitude(), lastLocation.getLongitude()));
        	
        	// Display it through Remote View
        	CurrentMoodWidgetProvider.updateTextView(0, R.id.TrackLastHit, hit_distance_str);
        	
        	lastLocation = location;
        } 
        else
        {
        	lastLocation = location;
        }
        
        locationManager.removeUpdates(this);
        
        stopSelf(startId_);
        numOfUpdates++;
        Toast.makeText(getBaseContext(), "Location updated " + numOfUpdates + " times", Toast.LENGTH_LONG).show();
    }
 
    @Override
    public void onProviderDisabled(String provider) {
         
        /******** Called when User off Gps *********/
    	Log.v("UploadDistanceToHole", "onProviderDisabled"); 
        Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
    }
 
    @Override
    
    public void onProviderEnabled(String provider) {
         
        /******** Called when User on Gps  *********/
    	Log.v("UploadDistanceToHole", "onProviderEnabled"); 
        Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    	Log.v("UploadDistanceToHole", "onProviderEnabled");
    	
         
    }

    @Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
