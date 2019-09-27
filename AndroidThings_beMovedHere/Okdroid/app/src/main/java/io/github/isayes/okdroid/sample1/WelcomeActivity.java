package io.github.isayes.okdroid.sample1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import io.github.isayes.okdroid.R;
import io.github.isayes.okdroid.base.BaseActivity;
import io.github.isayes.okdroid.base.CustomApplication;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 把状态变为 0 能使父类不会走 protectApp()
        CustomApplication.mAppStatus = 0;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupData() {
        setContentView(R.layout.activity_welcome);
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        }
    };
}
