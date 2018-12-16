package google.com.uberclone.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

import google.com.uberclone.R;
import google.com.uberclone.database.AppDatabase;
import google.com.uberclone.database.LocatioModel;
import google.com.uberclone.util.PreferenceData;
import google.com.uberclone.util.Util;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, GpsStatus.Listener {


    private AppDatabase database;
    private static final String TAG = MapActivity.class.getCanonicalName();
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Location location;
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    private int TIME = 30000;
    final Handler handler = new Handler();
    private boolean isHendler = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        database = Room.databaseBuilder(this, AppDatabase.class, "db-contacts")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.addGpsStatusListener(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        buildApiClient();
    }

    protected synchronized void buildApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(final Location location) {
        this.location = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.e("lat", "" + location.getLatitude());
        Log.e("lat", "" + location.getLongitude());

        if (location != null) {
            if (!isHendler) {
                isHendler = true;
                PreferenceData.setPrvLat("" + location.getLatitude());
                PreferenceData.setPrvLong("" + location.getLongitude());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        handler.postDelayed(this, TIME);
                        locationDestance();
                    }
                }, TIME);
            }
        }

    }

    private void locationDestance() {
        if (PreferenceData.getPrvlat().length() > 0 && PreferenceData.getPrvlong().length() > 0 && location != null) {
            Location startPoint = new Location("locationA");
            startPoint.setLatitude(Double.parseDouble(PreferenceData.getPrvlat()));
            startPoint.setLongitude(Double.parseDouble(PreferenceData.getPrvlong()));

            Location endPoint = new Location("locationB");
            endPoint.setLatitude(location.getLatitude());
            endPoint.setLongitude(location.getLongitude());

            double distance = startPoint.distanceTo(endPoint);
            Log.d("DETAIL DISTNCE", "" + distance);

            LocatioModel locatioModel = new LocatioModel();
            locatioModel.setMeter("" + new DecimalFormat("##.##").format(distance));
            database.locationDAO().insert(locatioModel);
            database.locationDAO().getLocationDetails();

            Log.d("DETAIL", database.locationDAO().getLocationDetails().toString());
            Log.d("DETAIL SUM", "" + new DecimalFormat("##.##").format(Double.parseDouble(database.locationDAO().getSum()) / 1000));

            if (Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(database.locationDAO().getSum()) / 1000)) > 0.7) {
                addNotification();
            }

            PreferenceData.setPrvLat("" + location.getLatitude());
            PreferenceData.setPrvLong("" + location.getLongitude());
        }

    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_map_icon)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(this, MapActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onGpsStatusChanged(int event) {
        switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
                Log.e(TAG, "onGpsStatusChanged started");
                break;

            case GpsStatus.GPS_EVENT_STOPPED:
                Log.e(TAG, "onGpsStatusChanged stopped");
                if (Util.isGpsEnable(this)) {
                } else {
                    Intent intent = new Intent(this, LocationServiceActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;

            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Log.e(TAG, "onGpsStatusChanged first fix");
                break;

            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                Log.e(TAG, "onGpsStatusChanged status");
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.locationDAO().deleteTable();
    }
}
