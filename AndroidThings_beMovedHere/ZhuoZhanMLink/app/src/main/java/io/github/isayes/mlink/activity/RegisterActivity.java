package io.github.isayes.mlink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.listener.SaveListener;
import io.github.isayes.mlink.R;
import io.github.isayes.mlink.entity.UserInfo;
import io.github.isayes.mlink.util.LoginRegUtil;

/**
 * Author: HF
 * Date:   2016-05-05
 * Description:
 */

public class RegisterActivity extends BaseActivity {

    private EditText etInputAccount;
    private EditText etInputPassword;
    private EditText etCheckPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getToolbar();
        getWidgets();
    }

    private void getWidgets() {
        etInputAccount = $(R.id.id_input_account);
        etInputPassword = $(R.id.id_input_pwd);
        etCheckPassword = $(R.id.id_check_pwd);
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
        pageTitle.setText(R.string.pageTitle_reg);
    }

    @SuppressWarnings("unchecked")
    public <TT> TT $(int id) {
        return (TT) findViewById(id);
    }

    public void toRegister(View view) {
        String phoneNumber = etInputAccount.getText().toString();
        String password = etInputPassword.getText().toString();
        String passwordChecked = etCheckPassword.getText().toString();
        String username = phoneNumber;
        /*----------------------------------------------------------------------------------------*/
        if (!LoginRegUtil.isNetworkConnected(this)) {
            showToast("没有联网");
        } else if (username.equals("") || password.equals("") || passwordChecked.equals("")) {
            showToast("注册信息需填写完毕");
        } else if (!passwordChecked.equals(password)) {
            showToast("两次密码输入不一致");
        } else if (!LoginRegUtil.isPhoneNumberValid(phoneNumber)) {
            showToast("请输入合法密码");
        } else {
            // 提交注册信息
            UserInfo user = new UserInfo();
            user.setUsername(username);
            user.setPassword(password);
            user.setMobilePhoneNumber(phoneNumber);
            user.signUp(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    showToast("注册成功！请登录");
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    RegisterActivity.this.finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    showToast("已经注册过，请返回登录");
                }
            });
        }
    }

    private void showToast(String msg) {
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
