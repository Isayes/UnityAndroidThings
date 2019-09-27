package io.github.isayes.shanbaydemo.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.util.List;
import java.util.Map;

import io.github.isayes.shanbaydemo.R;
import io.github.isayes.shanbaydemo.utils.Globals;
import io.github.isayes.shanbaydemo.utils.RecyclerSimpleAdapter;
import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;
import it.gmariotti.recyclerview.itemanimator.ScaleInOutItemAnimator;

/**
 * Author: HF
 * Date:   2016-06-02
 * Description: 目录页面
 */
public class MainActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerSimpleAdapter mAdapter;
    private List<Map<String, Object>> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupData() {
        setContentView(R.layout.activity_main, R.string.NewConcept4, MODE_NO_NAVIGATION);
        getData();
        getView();
        Log.i("===================", Globals.data.size() + "============");
    }

    private void getData() {
        mDatas = Globals.data;
    }

    @SuppressWarnings("unchecked")
    private void getView() {
        mRecyclerView = $(R.id.id_recyclerView);
        mAdapter = new RecyclerSimpleAdapter(this, mDatas);
        AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(mAdapter, mRecyclerView);
        mRecyclerView.setAdapter(animatorAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new ScaleInOutItemAnimator(mRecyclerView));
        mAdapter.setOnItemClickListener(new RecyclerSimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String articleContent = mDatas.get(position).get("content").toString();
                String articleTitle = mDatas.get(position).get("title").toString();
                Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                intent.putExtra("content", articleContent);
                intent.putExtra("title", articleTitle);
                startActivity(intent);
            }
        });
    }

    private long exitTime = 0L;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 提醒用户需要点击两次返回才退出应用
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 20000) {
                showToast(getString(R.string.double_click_exit));
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
