package google.com.uberclone.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import google.com.uberclone.R;
import google.com.uberclone.util.PreferenceData;
import google.com.uberclone.util.Util;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LoginActivity extends AppCompatActivity {


    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceData.setLogin(true);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (isPermissionGranted()) {
                        if (Util.isGpsEnable(LoginActivity.this)) {
                            Intent intent1 = new Intent(LoginActivity.this, MapActivity.class);
                            startActivity(intent1);
                            finish();
                        } else {
                            Intent intent1 = new Intent(LoginActivity.this, LocationServiceActivity.class);
                            startActivity(intent1);
                            finish();
                        }
                    } else {
                        Intent intent1 = new Intent(LoginActivity.this, PermissionActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                }else {
                    if (Util.isGpsEnable(LoginActivity.this)) {
                        Intent intent1 = new Intent(LoginActivity.this, MapActivity.class);
                        startActivity(intent1);
                        finish();
                    } else {
                        Intent intent1 = new Intent(LoginActivity.this, LocationServiceActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                }

            }
        });
    }

    private boolean isPermissionGranted() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
}
