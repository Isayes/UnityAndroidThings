package io.github.isayes.okdroid.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.isayes.okdroid.sample1.HomeActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 判断如果被强杀，就回到 HomeActivity 中去，否则可以初始化
        if (CustomApplication.mAppStatus == -1) {
            // 重走应用流程
            protectApp();
        } else {
            setupData();
        }

    }

    // 在这里做初始化操作
    protected void setupData() {

    }

    // 使用 protected 让子类可以重写该方法
    protected void protectApp() {
        // 重新走应用的流程是一个正确的做法，因为应用被强杀了还保存 Activity 的栈信息是不合理的
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("action", "force_kill");
        startActivity(intent);
    }

}
