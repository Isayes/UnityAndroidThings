package io.github.isayes.okdroid.sample1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import io.github.isayes.okdroid.R;
import io.github.isayes.okdroid.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
