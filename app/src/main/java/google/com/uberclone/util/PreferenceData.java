package google.com.uberclone.util;

public class PreferenceData {

    public static void setTimeStem(String timeStemp) {
        SharedPreferenceUtil.putValue(Const.TIME_STEMP, timeStemp);
    }

    public static String isTimeStemp() {
        return (SharedPreferenceUtil.contains(Const.TIME_STEMP)
                && (SharedPreferenceUtil.getString(Const.TIME_STEMP, null) != null) ?
                SharedPreferenceUtil.getString(Const.TIME_STEMP, null) : "");
    }

    public static void setLogin(boolean isLogin) {
        SharedPreferenceUtil.putValue(Const.IS_LOGIN, isLogin);
    }

    public static boolean isLogin() {
        return (SharedPreferenceUtil.getBoolean(Const.IS_LOGIN,false));
    }

    public static void setPermissionDenied(boolean isPermissionDenied) {
        SharedPreferenceUtil.putValue(Const.IS_PERMISSION_DENIED, isPermissionDenied);
    }

    public static boolean isPermissionDenied() {
        return (SharedPreferenceUtil.getBoolean(Const.IS_PERMISSION_DENIED,false));
    }

    public static void setPrvLat(String prvlat) {
        SharedPreferenceUtil.putValue(Const.PRV_LAT, prvlat);
    }

    public static void setPrvLong(String prvlong) {
        SharedPreferenceUtil.putValue(Const.PRV_LONG, prvlong);
    }

    public static String getPrvlat() {
        return (SharedPreferenceUtil.getString(Const.PRV_LAT,""));
    }

    public static String getPrvlong() {
        return (SharedPreferenceUtil.getString(Const.PRV_LONG,""));
    }
}
