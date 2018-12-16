package google.com.uberclone.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import google.com.uberclone.R;
import google.com.uberclone.util.PreferenceData;
import google.com.uberclone.util.Util;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getCanonicalName();
    public final int SPLASH_TIME = 1000;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startApp();
            }
        }, SPLASH_TIME);
    }

    private void startApp() {
        if (PreferenceData.isLogin()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (isPermissionGranted()) {
                    if (Util.isGpsEnable(this)) {
                        Intent intent1 = new Intent(SplashActivity.this, MapActivity.class);
                        startActivity(intent1);
                        finish();
                    } else {
                        Intent intent1 = new Intent(SplashActivity.this, LocationServiceActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                } else {
                    Intent intent1 = new Intent(SplashActivity.this, PermissionActivity.class);
                    startActivity(intent1);
                    finish();
                }
            } else {
                if (Util.isGpsEnable(this)) {
                    Intent intent1 = new Intent(SplashActivity.this, MapActivity.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Intent intent1 = new Intent(SplashActivity.this, LocationServiceActivity.class);
                    startActivity(intent1);
                    finish();
                }
            }
        } else {
            Intent intent1 = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent1);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
    }

    private boolean isPermissionGranted() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

}
