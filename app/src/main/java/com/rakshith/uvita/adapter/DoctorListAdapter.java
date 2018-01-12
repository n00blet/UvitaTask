package com.rakshith.uvita.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.loopj.android.http.BinaryHttpResponseHandler;

import com.rakshith.uvita.R;
import com.rakshith.uvita.UvitaApp;
import com.rakshith.uvita.api.CustomImageLoader;
import com.rakshith.uvita.api.RestClient;
import com.rakshith.uvita.cache.LruBitmapCache;
import com.rakshith.uvita.common.AppConstants;
import com.rakshith.uvita.model.Doctor;
import com.rakshith.uvita.model.DoctorData;


import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Rakshith on 1/9/2018.
 */

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder> {

    private List<Doctor> mDoctors;
    private Context mContext;
    private DoctorData doctorData;
    private SharedPreferences pref;
    private CustomImageLoader customImageLoader;


    public DoctorListAdapter(Context context, DoctorData data) {
        this.mContext = context;
        this.doctorData = data;
        this.mDoctors = data.getDoctors();
        customImageLoader = UvitaApp.getInstance().getmCustomImageLoader();

    }

    @Override
    public DoctorListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_row_list, parent, false);
        return new DoctorListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DoctorListAdapter.ViewHolder holder, int position) {


        Doctor doctor = mDoctors.get(position);

        if (doctor.getName() != null) {
            holder.mDoctorName.setText(doctor.getName());
        }

        if (doctor.getAddress() != null) {
            holder.mDoctorAddress.setText(doctor.getAddress());
        }
        if (doctor.getPhotoId() != null) {
            String url = AppConstants.GETDOCTORIMAGE + "/" + doctor.getPhotoId();

            holder.mDoctorImage.setDefaultImageResId(R.mipmap.ic_launcher_round);
            holder.mDoctorImage.setImageUrl(url, customImageLoader);
        }


    }


    @Override
    public int getItemCount() {
        return mDoctors == null ? 0 : mDoctors.size();
    }

    public List<Doctor> getmDoctors() {
        return mDoctors;
    }

    public void setmDoctors(List<Doctor> mDoctors) {
        this.mDoctors = mDoctors;
    }

    public DoctorData getDoctorData() {
        return doctorData;
    }

    public void setDoctorData(DoctorData doctorData) {
        this.doctorData = doctorData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.doctor_name)
        TextView mDoctorName;
        @Nullable
        @BindView(R.id.doctor_address)
        TextView mDoctorAddress;
        @BindView(R.id.doctor_image)
        NetworkImageView mDoctorImage;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
