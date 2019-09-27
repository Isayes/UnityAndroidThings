package io.github.isayes.okdroid.sample1;

import android.os.Bundle;
import android.widget.TextView;

import io.github.isayes.okdroid.R;
import io.github.isayes.okdroid.base.BaseActivity;
import io.github.isayes.okdroid.base.CustomApplication;

public class ProfileActivity extends BaseActivity {

    private TextView mProfileLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 让所有继承于 BaseActivity 的子类的 onCreate() 都直接交给父类 BaseActivity 去操作，
        // 让父类进行一系列的先判断，不让子类随随便便地进行初始化
    }

    @Override
    protected void setupData() {
        setContentView(R.layout.activity_profile);
        mProfileLabel = $(R.id.id_mProfileLabel);
        mProfileLabel.setText(CustomApplication.mTestNullPointer.toString());
    }

    @SuppressWarnings("unchecked")
    private <TT> TT $(int resId) {
        return (TT) findViewById(resId);
    }

}
