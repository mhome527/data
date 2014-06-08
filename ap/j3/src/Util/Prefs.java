package sjpn3.vn.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences
 */
public class Prefs {
    SharedPreferences mPrefs;
    Context mContext;
    private static final String SHAREDPREFERENCES_NAME = "SjpN3";

    /**
     * constructor
     *
     * @param ctx
     */
    public Prefs(Context ctx) {
        // TODO Auto-generated constructor stub
        this.mContext = ctx;
        mPrefs = ctx.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * set value
     *
     * @function name: void putBooleanValue()
     * @param value
     * @param KEY
     */
    public void putBooleanValue(boolean value, String KEY) {
        mPrefs.edit().putBoolean(KEY, value).commit();
    }

    /**
     * get value
     *
     * @function name: boolean getBooleanValue()
     * @param defvalue
     * @param KEY
     * @return value
     */
    public boolean getBooleanValue(boolean defvalue, String KEY) {
        return mPrefs.getBoolean(KEY, defvalue);
    }

    /**
     * set string value
     *
     * @function name: void putStringValue()
     * @param value
     * @param KEY
     */
    public void putStringValue(String value, String KEY) {
        mPrefs.edit().putString(KEY, value).commit();
    }

    /**
     * get string value
     *
     * @function name: String getStringValue()
     * @param defvalue
     * @param KEY
     * @return value
     */
    public String getStringValue(String defvalue, String KEY) {
        return mPrefs.getString(KEY, defvalue);
    }

    /**
     * set integer value
     *
     * @function name: void putIntValue()
     * @param value
     * @param KEY
     */
    public void putIntValue(int value, String KEY) {
        mPrefs.edit().putInt(KEY, value).commit();
    }

    /**
     * get integer value
     *
     * @function name: int getIntValue()
     * @param defvalue
     * @param KEY
     * @return value
     */
    public int getIntValue(int defvalue, String KEY) {
        return mPrefs.getInt(KEY, defvalue);
    }

    /**
     * set long value
     *
     * @function name: void putLongValue()
     * @param value
     * @param KEY
     */
    public void putLongValue(long value, String KEY) {
        mPrefs.edit().putLong(KEY, value).commit();
    }

    /**
     * get long value
     *
     * @function name: long getLongValue()
     * @param defvalue
     * @param KEY
     * @return value
     */
    public long getLongValue(long defvalue, String KEY) {
        return mPrefs.getLong(KEY, defvalue);
    }

    /**
     * set float value
     *
     * @function name: void putFloatValue()
     * @param value
     * @param KEY
     */
    public void putFloatValue(float value, String KEY) {
        mPrefs.edit().putFloat(KEY, value).commit();
    }

    /**
     * get float value
     *
     * @function name: float getFloatValue()
     * @param defvalue
     * @param KEY
     * @return value
     */
    public float getFloatValue(float defvalue, String KEY) {
        return mPrefs.getFloat(KEY, defvalue);
    }

    /**
     * remove value
     *
     * @function name: void removeValue()
     * @param KEY
     */
    public void removeValue(String KEY) {
        mPrefs.edit().remove(KEY).commit();
    }
}
   
