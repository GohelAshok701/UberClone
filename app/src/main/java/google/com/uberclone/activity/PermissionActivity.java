package google.com.uberclone.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import google.com.uberclone.R;
import google.com.uberclone.util.PreferenceData;
import google.com.uberclone.util.Util;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private TextView tv_permission;
    private Button btn_permission;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        tv_permission = findViewById(R.id.tv_permission);
        btn_permission = findViewById(R.id.btn_permission);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        btn_permission.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_permission:
                if (!PreferenceData.isPermissionDenied()) {
                    requestPermissionRuntime();
                } else {
                    gotoSettingForLocationPermission("Please to to setting and allow location permission");
                }
                break;
        }
    }

    private void requestPermissionRuntime() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted) {
                        if (Util.isGpsEnable(this)) {
                            Intent intent1 = new Intent(this, MapActivity.class);
                            startActivity(intent1);
                            finish();
                        } else {
                            Intent intent1 = new Intent(this, LocationServiceActivity.class);
                            startActivity(intent1);
                            finish();
                        }
                    } else {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
                                return;
                            } else {
                                PreferenceData.setPermissionDenied(true);
                                setPermissionLocationText(2);
                            }
                        }

                    }
                }
                break;

            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPermissionGranted()) {
            if (Util.isGpsEnable(this)) {
                Intent intent1 = new Intent(this, MapActivity.class);
                startActivity(intent1);
                finish();
            } else {
                Intent intent1 = new Intent(this, LocationServiceActivity.class);
                startActivity(intent1);
                finish();
            }
        } else {
            if (PreferenceData.isPermissionDenied()) {
                setPermissionLocationText(2);
            } else {
                setPermissionLocationText(1);
            }
        }
    }

    public void setPermissionLocationText(int isFrom) {
        if (isFrom == 1) {
            btn_permission.setText("Please allow permission");
        } else if (isFrom == 2) {
            btn_permission.setText("Please go to setting and allow location permission");
        }
    }

    private void gotoSettingForLocationPermission(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean isPermissionGranted() {
        int result = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

}
