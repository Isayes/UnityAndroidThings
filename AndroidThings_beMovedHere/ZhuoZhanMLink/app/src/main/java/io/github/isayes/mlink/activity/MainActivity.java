package io.github.isayes.mlink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import io.github.isayes.mlink.R;
import io.github.isayes.mlink.entity.TaskInfo;
import io.github.isayes.mlink.util.ListViewUtil;
import io.github.isayes.mlink.widget.MyMainAdapter;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private long exitTime = 0L;
    private MyMainAdapter myMainAdapter;
    private ListView mListView;
    private List<TaskInfo> mTaskInfoList = new ArrayList<>();
    private ImageView ivToCenter;
    private ImageView ivGetTask;
    private ImageView ivExchangeScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTaskData();
        getMyWidgets();
    }

    private void getTaskData() {
        BmobQuery<TaskInfo> query = new BmobQuery<>();
        query.order("-updatedAt");
        query.findObjects(this, new FindListener<TaskInfo>() {
            @Override
            public void onSuccess(List<TaskInfo> list) {
                mTaskInfoList = list;
                // 通知 Adapter 数据更新
                myMainAdapter.refresh((ArrayList<TaskInfo>) mTaskInfoList);
            }

            @Override
            public void onError(int i, String s) {
                showToast("获取数据失败");
            }
        });
    }

    private void getMyWidgets() {
        /*----------------------------------------------------------------------------------------*/
        mListView = $(R.id.id_main_listView);
        myMainAdapter = new MyMainAdapter(MainActivity.this, mTaskInfoList);
        mListView.setDivider(null);
        mListView.setAdapter(myMainAdapter);
        mListView.setLayoutAnimation(ListViewUtil.setListAnim());
        /*----------------------------------------------------------------------------------------*/
        ivToCenter = $(R.id.id_to_MyCenter);
        ivToCenter.setOnClickListener(this);
        ivGetTask = $(R.id.id_go_to_get_task);
        ivGetTask.setOnClickListener(this);
        ivExchangeScores = $(R.id.id_go_to_exchange_scores);
        ivExchangeScores.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_to_MyCenter:
                startActivity(new Intent(this, MyCenterActivity.class));
                break;
            case R.id.id_go_to_get_task:
                startActivity(new Intent(this, GetTaskActivity.class));
                break;
            case R.id.id_go_to_exchange_scores:
                startActivity(new Intent(this, ExchangeActivity.class));
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public <TT> TT $(int id) {
        return (TT) findViewById(id);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 5000) {
                showToast("再按一次退出！");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
