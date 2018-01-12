package com.rakshith.uvita.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rakshith P on 11/01/2018.
 */

public class CustomImageLoader extends ImageLoader {
    /**
     * Constructs a new ImageLoader.
     *
     * @param queue      The RequestQueue to use for making image requests.
     * @param imageCache The cache to use as an L1 cache.
     */
    private Context mContext;
    private String mAuth;

    public CustomImageLoader(RequestQueue queue, ImageCache imageCache) {
        super(queue, imageCache);
    }

    public CustomImageLoader(Context aContext, RequestQueue queue, ImageCache imageCache, String aAuth) {
        super(queue, imageCache);
        mContext = aContext;
        mAuth = aAuth;
    }

    @Override
    protected Request<Bitmap> makeImageRequest(String requestUrl, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, final String cacheKey) {
        return new ImageRequest(requestUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                onGetImageSuccess(cacheKey, response);
            }
        }, maxWidth, maxHeight, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onGetImageError(cacheKey, error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization",mAuth);
                return params;
            }
        };
    }
}

