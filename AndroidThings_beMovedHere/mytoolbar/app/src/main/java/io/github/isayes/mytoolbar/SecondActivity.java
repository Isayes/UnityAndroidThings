package io.github.isayes.mytoolbar;

import android.os.Bundle;

public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupData() {
        setContentView(R.layout.activity_second, R.string.secondPageTitle, MODE_BACK_NAVIGATION);
    }
}
