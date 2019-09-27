package io.github.isayes.mytoolbar;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    // 页面无 Navigation 的模式
    public static final int MODE_NO_NAVIGATION = 0;
    // 页面有返回 Navigation 的模式
    public static final int MODE_BACK_NAVIGATION = 1;

    protected Toolbar mToolbar;
    protected TextView mPageTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupData();
    }

    // 布局数据初始化
    protected void setupData() {
        //
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    // 重载 setContentView
    public void setContentView(@LayoutRes int layoutResID, int resTitleID, int pageMODE) {
        super.setContentView(layoutResID);
        setToolbarStatus();
        mToolbar = $(R.id.id_toolbar);
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
                mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
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

    // 设置状态栏颜色
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
