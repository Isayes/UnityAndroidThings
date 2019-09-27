package io.github.isayes.okdroid.sample1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import io.github.isayes.okdroid.R;
import io.github.isayes.okdroid.base.BaseActivity;
import io.github.isayes.okdroid.base.CustomApplication;

/**
 * HomeActivity 的启动模式为 "singleTask"
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private Button mHomeProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // 初始化操作
    @Override
    protected void setupData() {
        setContentView(R.layout.activity_home);
        mHomeProfileBtn = $(R.id.id_mHomeProfileBtn);
        mHomeProfileBtn.setOnClickListener(this);
        CustomApplication.mTestNullPointer = new ArrayList<>();
        CustomApplication.mTestNullPointer.add("profile");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getStringExtra("action");
        if ("force_kill".equals(action)) {
            // 在 ProfileActivity 被强杀了就重新走应用的流程
            protectApp();
        }
    }

    @Override
    protected void protectApp() {
        // 回到 WelcomeActivity
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @SuppressWarnings("unchecked")
    private <T> T $(int resId) {
        return (T) findViewById(resId);
    }
}
