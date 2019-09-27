package io.github.isayes.mlink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.github.isayes.mlink.R;

public class FeedbackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getToolbar();
    }

    @SuppressWarnings("ConstantConditions")
    private void getToolbar() {
        Toolbar toolbar = $(R.id.id_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOnMenuItemClickListener(menuItemClick);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView pageTitle = $(R.id.id_page_title);
        pageTitle.setText(R.string.pageTitle_problem_feedback);
    }

    public Toolbar.OnMenuItemClickListener menuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.id_history_action:
                    startActivity(new Intent(FeedbackActivity.this, FeedbackHistoryActivity.class));
                    break;
            }
            return true;
        }
    };

    private void showToast(String msg) {
        Toast toast = Toast.makeText(FeedbackActivity.this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @SuppressWarnings("unchecked")
    public <TT> TT $(int id) {
        return (TT) findViewById(id);
    }

    public void submitFeedback(View view) {
        startActivity(new Intent(this, FeedbackHistoryActivity.class));
    }
}
