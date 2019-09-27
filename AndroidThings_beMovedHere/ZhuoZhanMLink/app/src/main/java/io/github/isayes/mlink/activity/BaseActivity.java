package io.github.isayes.mlink.activity;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import io.github.isayes.mlink.R;
import io.github.isayes.mlink.util.ToolbarUtil;

/**
 * Author: HF
 * Date:   2016-05-05
 * Description:
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    private void setStatusBar() {
        ToolbarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }
}
