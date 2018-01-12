package com.rakshith.uvita.api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Rakshith on 12/27/2017.
 */

public class RestClient {


    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, String header, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", header);
        client.get(url, params, responseHandler);
    }

    public static void get(String url,String header, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", header);
        client.get(url,responseHandler);
    }

    public static void post(String url, String header, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", header);
        client.post(url, params, responseHandler);
    }
}
