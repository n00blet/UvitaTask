package com.rakshith.uvita.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rakshith.uvita.R;
import com.rakshith.uvita.model.Doctor;
import com.rakshith.uvita.model.DoctorData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rakshith P on 10/01/2018.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private List<Doctor> mDoctors;
    private Context mContext;
    private DoctorData doctorData;


    public TestAdapter(Context context, DoctorData data) {
        Log.d("Doc data","+" + data.getLastKey());
        this.mContext = context;
        this.doctorData = data;
        this.mDoctors = data.getDoctors();
    }

    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_row_list, parent, false);
        return new TestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestAdapter.ViewHolder holder, int position) {


        Doctor doctor = mDoctors.get(position);

        if (doctor.getName() != null) {
            holder.mDoctorName.setText(doctor.getName());
        }

        if (doctor.getAddress() != null) {
            holder.mDoctorAddress.setText(doctor.getAddress());
        }
        if (doctor.getPhotoId() != null) {

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
        ImageView mDoctorImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
