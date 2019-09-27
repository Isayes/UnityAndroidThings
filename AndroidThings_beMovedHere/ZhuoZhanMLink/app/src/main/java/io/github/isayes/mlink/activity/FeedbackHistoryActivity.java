package io.github.isayes.mlink.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.isayes.mlink.R;
import io.github.isayes.mlink.util.ListViewUtil;

public class FeedbackHistoryActivity extends BaseActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private SimpleAdapter mSimpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_history);
        getMyToolbar();
        getMyWidgets();
    }

    @SuppressWarnings("ConstantConditions")
    private void getMyToolbar() {
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
        pageTitle.setText(R.string.pageTitle_fb_history);
    }

    @SuppressWarnings("unchecked")
    public <TT> TT $(int id) {
        return (TT) findViewById(id);
    }

    private void getMyWidgets() {

        setMySwipeLayout();
        mListView = $(R.id.id_fb_history_list);
        mSimpleAdapter = new SimpleAdapter(
                this,
                getData(),
                R.layout.item_feedback_history,
                new String[]{"title", "device", "time"},
                new int[]{
                        R.id.id_fb_history_item_problem_title,
                        R.id.id_fb_history_item_device_model,
                        R.id.id_fb_history_item_time
                });
        mListView.setAdapter(mSimpleAdapter);
        mListView.setLayoutAnimation(ListViewUtil.setListAnim());
    }

    private void setMySwipeLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipeLayout);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色…… ↓
        assert mSwipeRefreshLayout != null;
        // 设置进度动画的颜色 ↓
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        // 设置进度圈的大小，只有两个值：DEFAULT、LARGE ↓
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeRefreshLayout.setPadding(0, 10, 0, 0);

        // 设置手势滑动监听器 ↓
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //resultList.clear();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getData();
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            /* | */
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSimpleAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }

    };

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("title", getString(R.string.problem_page_adaptation));
        map.put("device", getString(R.string.problem_device_model));
        map.put("time", getString(R.string.problem_fb_time));
        list.add(map);

        map = new HashMap<>();
        map.put("title", "异常退出");
        map.put("device", "iPhone 5");
        map.put("time", getString(R.string.problem_fb_time));
        list.add(map);

        map = new HashMap<>();
        map.put("title", "功能缺陷");
        map.put("device", getString(R.string.problem_device_model));
        map.put("time", getString(R.string.problem_fb_time));
        list.add(map);

        map = new HashMap<>();
        map.put("title", "业务逻辑问题");
        map.put("device", getString(R.string.problem_device_model));
        map.put("time", getString(R.string.problem_fb_time));
        list.add(map);

        map = new HashMap<>();
        map.put("title", "建议");
        map.put("device", "iPhone 6 plus");
        map.put("time", getString(R.string.problem_fb_time));
        list.add(map);
        return list;
    }
}
