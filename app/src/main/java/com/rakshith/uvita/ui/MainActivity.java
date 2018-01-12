package com.rakshith.uvita.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rakshith.uvita.R;
import com.rakshith.uvita.UvitaApp;
import com.rakshith.uvita.api.RestClient;
import com.rakshith.uvita.common.AppConstants;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.etUserName)
    EditText mUser;
    @BindView(R.id.etPass)
    EditText mPass;
    UvitaApp mController;

    private String password, email;

    @BindView(R.id.login)
    Button getToken;

    @OnClick(R.id.login)
    void getAuthToken() {
        validateUser();
    }


    private void validateUser() {
        password = mPass.getText().toString();
        email = mUser.getText().toString();

        if (email.equals("") || email.equals(null) || password.equals("") || password.equals(null)) {
            Toast.makeText(getApplicationContext(), "Please enter valid data", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                try {
                    if (mController.isConnectingToInternet())
                        getToken(email, password);
                    else
                        Toast.makeText(getApplicationContext(), getString(R.string.internet_issue), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void getToken(final String email, final String password) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("grant_type", "password");
        paramMap.put("username", email);
        paramMap.put("password", password);
        RequestParams params = new RequestParams(paramMap);
        Log.d("params", "::" + params);
        Log.d("paramMap", "::" + paramMap);
        String basic = "Basic                                                           aXBob25lOmlwaG9uZXdpbGxub3RiZXRoZXJlYW55bW9yZQ==";
        RestClient.post(AppConstants.AUTH, basic, params, new JsonHttpResponseHandler() {


                    ProgressDialog pd;


                    @Override
                    public void onStart() {
                        pd = new ProgressDialog(MainActivity.this);
                        pd.setMessage("Please wait while we login");
                        pd.show();
                    }

                    @Override
                    public void onFinish() {
                        if (pd.isShowing())
                            pd.dismiss();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        String access_token, refresh_token;
                        if (response != null) {
                            try {
                                access_token = response.getString("access_token");
                                if (access_token != null && !access_token.equals(""))
                                    mController.createPref("access_token", access_token);
                                refresh_token = response.getString("refresh_token");
                                if (refresh_token != null && !refresh_token.equals(""))
                                    mController.createPref("refresh_token", refresh_token);

                                startActivity(new Intent(MainActivity.this, SearchDoctorAcitivity.class));
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                        Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mController = (UvitaApp) getApplicationContext();
    }


}
