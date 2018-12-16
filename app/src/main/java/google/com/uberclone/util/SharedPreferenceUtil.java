package google.com.uberclone.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Creates SharedPreference for the application. and provides access to it
 */
public class SharedPreferenceUtil {

    private static SharedPreferences sharedPreferences = null;

    private static SharedPreferences.Editor editor = null;

    public static void init(Context mcontext) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(mcontext);
            editor = sharedPreferences.edit();
        }
    }

    public static void clear() {
        editor.clear();
        save();
    }

    public static SharedPreferences.Editor getPrefranceEditor() {
        return editor;

    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key
     * @param value
     */
    public static void putValue(String key, String value) {
        editor.putString(key, value);
        save();
    }

    /**
     * Remove  Key and its Values into SharedPreference map.
     *
     * @param key
     */
    public static void remove(String key) {
        if (sharedPreferences.contains(key)) {
            editor.remove(key);
            save();
        }
    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key
     * @param value
     */
    public static void putValue(String key, int value) {
        editor.putInt(key, value);
        save();
    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key
     * @param value
     */
    public static void putValue(String key, long value) {
        editor.putLong(key, value);
        save();
    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key
     * @param value
     */
    public static void putValue(String key, boolean value) {
        editor.putBoolean(key, value);
        save();
    }

    /**
     * saves the values from the editor to the SharedPreference
     */
    public static void save() {
        editor.commit();
        editor.apply();
    }

    /**
     * returns a values associated with a Key default value ""
     *
     * @return String
     */
    public static String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * returns a values associated with a Key default value -1
     *
     * @return String
     */
    public static int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * returns a values associated with a Key default value -1
     *
     * @return String
     */
    public static long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    /**
     * returns a values associated with a Key default value false
     *
     * @return String
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * Checks if key is exist in SharedPreference
     *
     * @param key
     * @return boolean
     */
    public static boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * returns map of all the key value pair available in SharedPreference
     *
     * @return Map<String, ?>
     */
    public static Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    public static boolean saveArray(Context ctx, String name, ArrayList<String> data) {
        editor.putInt(name + "_size", data.size());
        for (int i = 0; i < data.size(); i++) {
            editor.remove(name + "_" + i);
            editor.putString(name + "_" + i, data.get(i));
        }
        return editor.commit();
    }

    public static ArrayList<String> loadArray(String name) {
        ArrayList<String> dataoutput = new ArrayList<String>();
        int size = sharedPreferences.getInt(name + "_size", 0);
        for (int i = 0; i < size; i++) {
            dataoutput.add(sharedPreferences.getString(name + "_" + i, null));
        }
        return dataoutput;
    }


}
