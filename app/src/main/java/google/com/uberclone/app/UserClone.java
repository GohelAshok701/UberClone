package google.com.uberclone.app;

import android.app.Application;

import google.com.uberclone.util.SharedPreferenceUtil;

/**
 * Created by Ashok on 9/26/2018.
 */

public class UserClone extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferenceUtil.init(this);
        SharedPreferenceUtil.save();
    }
}
