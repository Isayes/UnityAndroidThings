package com.mlab.mlabdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mlab.mlabdemo.activity.R;

public class Tab01Fragment extends Fragment implements View.OnClickListener {

    private TextView txtbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_tab01, container, false);
        txtbtn = (TextView) view.findViewById(R.id.txt_btn);
        txtbtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), PhoneInfoActivity.class);
//        startActivityForResult(intent, 1);
    }

}