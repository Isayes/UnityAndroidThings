package com.mlab.mlabdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.mlab.mlabdemo.fragment.Tab01Fragment;
import com.mlab.mlabdemo.fragment.Tab02Fragment;
import com.mlab.mlabdemo.fragment.Tab03Fragment;
import com.mlab.mlabdemo.fragment.Tab04Fragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments;//数据源

    private TextView tab01Txt;
    private TextView tab02Txt;
    private TextView tab03Txt;
    private TextView tab04Txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_main);
        initView();
        initEvent();
        setTab(0);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        tab01Txt = (TextView) findViewById(R.id.id_tab01);
        tab02Txt = (TextView) findViewById(R.id.id_tab02);
        tab03Txt = (TextView) findViewById(R.id.id_tab03);
        tab04Txt = (TextView) findViewById(R.id.id_tab04);

        mFragments = new ArrayList<>();
        Fragment mTab01 = new Tab01Fragment();
        Fragment mTab02 = new Tab02Fragment();
        Fragment mTab03 = new Tab03Fragment();
        Fragment mTab04 = new Tab04Fragment();
        mFragments.add(mTab01);
        mFragments.add(mTab02);
        mFragments.add(mTab03);
        mFragments.add(mTab04);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };

        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int currentTab = mViewPager.getCurrentItem();
                setTab(currentTab);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initEvent() {
        tab01Txt.setOnClickListener(this);
        tab02Txt.setOnClickListener(this);
        tab03Txt.setOnClickListener(this);
        tab04Txt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            /* TAB页卡 */
            case R.id.id_tab01:
                setTab(0);
                tab01Txt.setTextColor(Color.YELLOW);
                break;
            case R.id.id_tab02:
                setTab(1);
                tab02Txt.setTextColor(Color.YELLOW);
                break;
            case R.id.id_tab03:
                setTab(2);
                tab03Txt.setTextColor(Color.YELLOW);
                break;
            case R.id.id_tab04:
                setTab(3);
                tab04Txt.setTextColor(Color.YELLOW);
                break;
            default:
                break;
        }
    }

    private void setTab(int currentTab) {
        tab01Txt.setTextColor(Color.WHITE);
        tab02Txt.setTextColor(Color.WHITE);
        tab03Txt.setTextColor(Color.WHITE);
        tab04Txt.setTextColor(Color.WHITE);
        mViewPager.setCurrentItem(currentTab);
        switch (currentTab) {
            case 0:
                tab01Txt.setTextColor(Color.YELLOW);
                break;
            case 1:
                tab02Txt.setTextColor(Color.YELLOW);
                break;
            case 2:
                tab03Txt.setTextColor(Color.YELLOW);
                break;
            case 3:
                tab04Txt.setTextColor(Color.YELLOW);
                break;
        }
    }
}
