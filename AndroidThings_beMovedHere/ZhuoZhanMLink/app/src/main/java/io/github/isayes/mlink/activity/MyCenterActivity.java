package io.github.isayes.mlink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.github.isayes.mlink.R;

public class MyCenterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycenter);
        getToolbar();
        setMyEvents();
    }

    private void setMyEvents() {

        // 积分获取记录
        TextView recordOfGettingScores = $(R.id.id_item_recordOfGettingScores);
        recordOfGettingScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyCenterActivity.this, ScoreGetActivity.class));
            }
        });

        // 积分消费记录
        TextView recordOfConsumingScores = $(R.id.id_item_recordOfConsumingScores);
        recordOfConsumingScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyCenterActivity.this, ScoreConsumeActivity.class));
            }
        });

        // 积分排行榜
        TextView listOfScoresRink = $(R.id.id_item_listOfScoresRink);
        listOfScoresRink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyCenterActivity.this, ScoreRankActivity.class));
            }
        });

        // 积分商城
        TextView marketOfScores = $(R.id.id_item_marketOfScores);
        marketOfScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyCenterActivity.this, ScoreMarketActivity.class));
            }
        });

        // 分享
        TextView share = $(R.id.id_item_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content));
                shareIntent.setType("text/plain");
                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_to)));
            }
        });

        // 关于
        TextView about = $(R.id.id_item_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyCenterActivity.this, AboutActivity.class));
            }
        });

        // 注销
        Button logOut = $(R.id.id_btn_logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void getToolbar() {
        Toolbar toolbar = $(R.id.id_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView pageTitle = $(R.id.id_page_title);
        pageTitle.setText(R.string.pageTitle_myCenter);
    }

    @SuppressWarnings("unchecked")
    public <TT> TT $(int id) {
        return (TT) findViewById(id);
    }
}
