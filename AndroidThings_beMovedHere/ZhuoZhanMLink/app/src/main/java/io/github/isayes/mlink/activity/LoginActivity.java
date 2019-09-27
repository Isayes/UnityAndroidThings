package io.github.isayes.mlink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;
import io.github.isayes.mlink.R;
import io.github.isayes.mlink.entity.UserInfo;
import io.github.isayes.mlink.util.LoginRegUtil;

/**
 * Author: HF
 * Date:   2016-05-05
 * Description:
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private final static String APPLICATION_ID = "fb52ee61e7b83bc8b4278ea1d5480d20";
    private EditText etPhoneNumber;
    private EditText etPassword;
    private String phoneNumber;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, APPLICATION_ID);
        setContentView(R.layout.activity_login);
        getToolbar();
        getWidgets();
    }

    private void getWidgets() {
        etPhoneNumber = $(R.id.id_input_account);
        etPassword = $(R.id.id_input_pwd);
        Button btn_login = $(R.id.id_btn_login);
        btn_login.setOnClickListener(this);
        TextView txt_reg = $(R.id.id_txt_reg);
        txt_reg.setOnClickListener(this);
        TextView txt_forget_pwd = $(R.id.id_txt_forget_pwd);
        txt_forget_pwd.setOnClickListener(this);
    }

    @SuppressWarnings("unchecked")
    public <TT> TT $(int id) {
        return (TT) findViewById(id);
    }

    @SuppressWarnings("ConstantConditions")
    private void getToolbar() {
        Toolbar myToolbar = $(R.id.id_toolBar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setOnMenuItemClickListener(menuItemClick);
        TextView toolTitle = $(R.id.id_page_title);
        toolTitle.setText(R.string.pageTitle_login);
    }

    public Toolbar.OnMenuItemClickListener menuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.id_setting_action:
                    // showToast("不知道原型设计师在这里放个设置图标是要做什么");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.id_btn_login:
                // 检查登录信息
                phoneNumber = etPhoneNumber.getText().toString();
                password = etPassword.getText().toString();
                String username = phoneNumber;
                if (!LoginRegUtil.isNetworkConnected(this)) {
                    showToast("没有联网");
                } else if (phoneNumber.equals("") || password.equals("")) {
                    showToast("不可能输入为空");
                    break;
                } else {
                    UserInfo user = new UserInfo();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.login(this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            showToast("登录失败，请检查手机号码和密码是否填写正确");
                        }
                    });
                }
                break;
            case R.id.id_txt_reg:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.id_txt_forget_pwd:
                showToast(getString(R.string.toast_forget_pwd));
                break;
        }
    }


    /* ------------------------------------------------------------------------------------------ */
}

