package io.github.isayes.mytoolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 *
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupData() {
        setContentView(R.layout.activity_main, R.string.mainPageTitle, MODE_NO_NAVIGATION);
        mToolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    public void toNextPage(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    showToast("单击了编辑");
                    break;
                case R.id.action_share:
                    showToast("单击了分享");
                    break;
                case R.id.action_settings:
                    showToast("单击了设置");
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
