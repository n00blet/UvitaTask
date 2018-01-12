package com.rakshith.uvita;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.rakshith.uvita.api.CustomImageLoader;
import com.rakshith.uvita.cache.LruBitmapCache;
import com.rakshith.uvita.common.AppConstants;

/**
 * Created by  Rakshith on 08/01/2018.
 */

public class UvitaApp extends Application {

    public static UvitaApp mInstance;
    public static Context applicationContext;
    SharedPreferences prefs;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private CustomImageLoader mCustomImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        applicationContext = getApplicationContext();
        prefs = applicationContext.getSharedPreferences(AppConstants.USER_PREFS, Context.MODE_PRIVATE);

    }

    public static synchronized UvitaApp getInstance() {
        return mInstance;
    }


    // Checking for all possible internet providers
    public boolean isConnectingToInternet() {

        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    //Volley Image Loader
    public RequestQueue getRequestQueue() {

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }

    public CustomImageLoader getmCustomImageLoader() {
        getRequestQueue();
        String access_token = prefs.getString("access_token", null);
        String bearer = "Bearer " + access_token;
        if (mCustomImageLoader == null) {
            Log.d("Bearer","+" + bearer);
            mCustomImageLoader = new CustomImageLoader(getApplicationContext(), this.mRequestQueue, new LruBitmapCache(),bearer);
        }
        return this.mCustomImageLoader;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public void clearSharedPrefData() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(AppConstants.USER_PREFS);
        editor.clear();
        editor.commit();
        getApplicationContext().getSharedPreferences(AppConstants.USER_PREFS, 0).edit().clear().commit();
    }

    public void createPref(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit(); // commit changes
    }
}
