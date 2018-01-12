package com.rakshith.uvita.common;

import butterknife.BindView;

/**
 * Created by Rakshith on 12/27/2017.
 */

public class AppConstants {
    public static final String BASE_URL = "https://api.uvita.eu";
    public static final String AUTH = BASE_URL + "/oauth/token";
    public static final String GETDOCTORS =BASE_URL + "/api/users/me/doctors";
    public static final String GETDOCTORIMAGE = BASE_URL + "/api/users/me/files";
    public static final String USER_PREFS = "user_data";
}
