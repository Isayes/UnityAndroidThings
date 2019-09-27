package io.github.isayes.shanbaydemo.activitys;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.github.isayes.shanbaydemo.R;
import io.github.isayes.shanbaydemo.utils.ToolbarUtil;

/**
 * Author: HF
 * Date:   2016-06-02
 * Description:
 */
public class BaseActivity extends AppCompatActivity {

    public static final int MODE_NO_NAVIGATION = 0;
    public static final int MODE_BACK_NAVIGATION = 1;

    protected Toolbar mToolbar;
    protected TextView mPageTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupData();
    }

    protected void setupData() {
        //
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void setContentView(@LayoutRes int layoutResID, int resTitleID, int pageMODE) {
        super.setContentView(layoutResID);
        setToolbarStatus();
        mToolbar = $(R.id.id_page_toolbar);
        mPageTitle = $(R.id.id_pageTitle);
        mPageTitle.setText(resTitleID);
        setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        switch (pageMODE) {
            case MODE_NO_NAVIGATION:
                // 页面没有返回箭头
                break;

            case MODE_BACK_NAVIGATION:
                // 页面有返回剪头
                mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                break;
            // 其它
        }
    }

    // 设置状态栏颜色 or 透明度
    protected void setToolbarStatus() {
        ToolbarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @SuppressWarnings("unchecked")
    public <T> T $(int resID) {
        return (T) findViewById(resID);
    }

    protected void showToast(String msg) {
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
