package com.rakshith.uvita.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rakshith.uvita.R;
import com.rakshith.uvita.UvitaApp;
import com.rakshith.uvita.adapter.DoctorListAdapter;
import com.rakshith.uvita.api.RestClient;
import com.rakshith.uvita.common.AppConstants;
import com.rakshith.uvita.model.Doctor;
import com.rakshith.uvita.model.DoctorData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Rakshith P on 08/01/2018.
 */

public class SearchDoctorAcitivity extends AppCompatActivity {


    private SharedPreferences pref;
    private UvitaApp mController;
    private String access_token, refresh_token;
    private FusedLocationProviderClient mFusedLocationClient;
    private ArrayList<Doctor> mDoctorsList;
    private DoctorListAdapter mDoctors;


    @BindView(R.id.search_button)
    ImageButton mSearch;
    @BindView(R.id.search_user)
    EditText mSearchText;
    @BindView(R.id.recyclerview)
    RecyclerView mList;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;

    @OnClick(R.id.search_button)
    void searchDoc() {
        String cityname = mSearchText.getText().toString();
        if (cityname.equals("") || cityname.equals(null)) {
            Toast.makeText(getApplicationContext(), "Please enter city name", Toast.LENGTH_SHORT).show();
        } else {
            mDoctorsList.clear();
            getGeoCoordinates(cityname);
        }
    }

    private void getGeoCoordinates(String cityname) {
        if (Geocoder.isPresent()) {
            try {

                Geocoder gc = new Geocoder(this);
                List<Address> addresses = gc.getFromLocationName(cityname, 5); // get the found Address Objects
                Log.d("List", "##" + addresses.size());

                for (Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        Log.d("Lat", "###" + a.getLatitude());
                        Log.d("Long", "###" + a.getLongitude());
                        loading = true;
                        getNearbyDoctors(Double.toString(a.getLatitude()), Double.toString(a.getLongitude()), "null");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);

        int MyVersion = Build.VERSION.SDK_INT;
        mController = (UvitaApp) getApplicationContext();
        pref = mController.getSharedPreferences(AppConstants.USER_PREFS, Context.MODE_PRIVATE);
        access_token = pref.getString("access_token", null);
        refresh_token = pref.getString("refresh_token", null);
        mLayoutManager = new LinearLayoutManager(this);
        mDoctorsList = new ArrayList<>();
        setupRecyclerView(mList);

        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            } else {
                getDeviceLocation();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkIfAlreadyhavePermission())
            getDeviceLocation();
    }

    //Location permission

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    getDeviceLocation();
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getDeviceLocation() {
        Log.d("longitude", "true");
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                String latitude = Double.toString(location.getLatitude());
                                String longitude = Double.toString(location.getLongitude());
                                Log.d("Lat", "+" + latitude);
                                Log.d("longitude", "+" + longitude);
                                getNearbyDoctors(latitude, longitude, "null");
                            }
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mDoctors = new DoctorListAdapter(SearchDoctorAcitivity.this, new DoctorData());
        recyclerView.setAdapter(mDoctors);
    }


    private void getNearbyDoctors(final String latitude, final String longitude, final String last_key) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("lat", latitude);
        paramMap.put("lng", longitude);
        paramMap.put("sort", "distance");
        if (!last_key.equalsIgnoreCase("null"))
            paramMap.put("lastKey", last_key);

        access_token = pref.getString("access_token", null);
        String bearer = "Bearer " + access_token;
        RequestParams params = new RequestParams(paramMap);
        RestClient.get(AppConstants.GETDOCTORS, bearer, params, new JsonHttpResponseHandler() {

            ProgressDialog pd;
            DoctorData data;


            @Override
            public void onStart() {
                pd = new ProgressDialog(SearchDoctorAcitivity.this);
                pd.setMessage("Please wait");
                pd.show();
            }

            @Override
            public void onFinish() {
                if (pd.isShowing())
                    pd.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {

                    try {

                        data = new DoctorData();
                        JSONArray doctors = response.getJSONArray("doctors");
                        String last_keyy = response.getString("lastKey");
                        Log.d("last kezz", "+" + last_keyy);
                        if (!last_keyy.equalsIgnoreCase("null"))
                            data.setLastKey(last_keyy);

                        int length = doctors.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject each_doctor = doctors.getJSONObject(i);
                            Doctor doctor = new Doctor();
                            doctor.setName(each_doctor.getString("name"));
                            doctor.setAddress(each_doctor.getString("address"));
                            doctor.setRating(each_doctor.getString("rating"));
                            String photo_id = each_doctor.getString("photoId");
                            if (photo_id != null)
                                doctor.setPhotoId(photo_id);

                            mDoctorsList.add(doctor);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (mDoctorsList.size() > 0 && last_key.equalsIgnoreCase("null")) {
                    hideKeyboard(SearchDoctorAcitivity.this);
                    data.setDoctors(mDoctorsList);
                    mDoctors = new DoctorListAdapter(SearchDoctorAcitivity.this, data);
                    mList.setAdapter(mDoctors);
                    mDoctors.notifyDataSetChanged();
                } else {
                    loading = true;
                    hideKeyboard(SearchDoctorAcitivity.this);
                    mDoctors.notifyDataSetChanged();
                }
                mList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);


                        if (dy > 0) //check for scroll down
                        {


                            visibleItemCount = mLayoutManager.getChildCount();
                            totalItemCount = mLayoutManager.getItemCount();
                            pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                            if (loading) {

                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    loading = false;

                                    if (data.getLastKey() == null) {
                                        Toast.makeText(getApplicationContext(), "End of list", Toast.LENGTH_SHORT).show();


                                    } else {

                                        getNearbyDoctors(latitude, longitude, data.getLastKey());
                                    }
                                    //Do pagination.. i.e. fetch new data
                                }
                            }
                        }
                    }
                });

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (errorResponse != null) {
                    try {
                        String error = errorResponse.getString("error");
                        if (error.equalsIgnoreCase("Unauthorized"))
                            startActivity(new Intent(SearchDoctorAcitivity.this, MainActivity.class));
                        finish();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });

    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
