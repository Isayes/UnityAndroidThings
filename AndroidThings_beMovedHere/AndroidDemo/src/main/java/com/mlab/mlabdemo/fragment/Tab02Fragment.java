package com.mlab.mlabdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mlab.mlabdemo.activity.BDMapActivity;
import com.mlab.mlabdemo.activity.BopanActivity;
import com.mlab.mlabdemo.activity.PhoneInfoActivity;
import com.mlab.mlabdemo.activity.R;
import com.mlab.mlabdemo.activity.VoiceActivity;

public class Tab02Fragment extends Fragment implements View.OnClickListener {

    private TextView titlePhoneInfo;
    private TextView titleIpIsp;
    private TextView titlebtnboPan;
    private TextView titleBDMap;
    private TextView titleVoice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_tab02, container, false);
        initButtonView(view);
        return view;
    }

    private void initButtonView(View view) {
        titlebtnboPan = (TextView) view.findViewById(R.id.id_txt_bopan);
        titlebtnboPan.setOnClickListener(this);
//        titleIpIsp = (TextView) view.findViewById(R.id.id_txt_ipisp);
//        titleIpIsp.setOnClickListener(this);
        titlePhoneInfo = (TextView) view.findViewById(R.id.id_txt_info);
        titlePhoneInfo.setOnClickListener(this);
        titleBDMap = (TextView) view.findViewById(R.id.id_txt_bdmap);
        titleBDMap.setOnClickListener(this);
        titleVoice = (TextView) view.findViewById(R.id.id_txt_voice);
        titleVoice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_txt_info:
                Intent toPhoneInfo = new Intent();
                toPhoneInfo.setClass(getActivity(), PhoneInfoActivity.class);
                startActivityForResult(toPhoneInfo, 1);
                break;
//            case R.id.id_txt_ipisp:
//                Intent toIpIsp = new Intent();
//                toIpIsp.setClass(getActivity(), IpIspActivity.class);
//                startActivityForResult(toIpIsp, 2);
//                break;
            case R.id.id_txt_bopan:
                Intent toBoPan = new Intent();
                toBoPan.setClass(getActivity(), BopanActivity.class);
                startActivityForResult(toBoPan, 3);
                break;
            case R.id.id_txt_bdmap:
                Intent toBDMap = new Intent();
                toBDMap.setClass(getActivity(), BDMapActivity.class);
                startActivityForResult(toBDMap, 4);
                break;
            case R.id.id_txt_voice:
                Intent toVoice = new Intent();
                toVoice.setClass(getActivity(), VoiceActivity.class);
                startActivityForResult(toVoice, 5);
                break;
            default:
                break;
        }
    }
}