package com.nicerdata.bmobexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nicerdata.bmobexample.bean.Person;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Person person = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /* 初始化 Bmob SDK， 使用时第二个参数是 My Bmob Application ID */
        Bmob.initialize(this, "My Bmob Application ID");
        setContentView(R.layout.activity_main);

        Button addUser = (Button) findViewById(R.id.btn_addUser);
        Button queryUser = (Button) findViewById(R.id.btn_queryUser);
        Button modifyUser = (Button) findViewById(R.id.btn_modifyUser);
        Button deleteUser = (Button) findViewById(R.id.btn_deleteUser);
        addUser.setOnClickListener(this);
        queryUser.setOnClickListener(this);
        modifyUser.setOnClickListener(this);
        deleteUser.setOnClickListener(this);
    }


    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 添加数据
            case R.id.btn_addUser:
                person = new Person();
                person.setName("Isayes");
                person.setAddress("北京海淀");
                person.save(this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        toast("数据添加成功，返回objectId为：" + person.getObjectId());
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        toast("创建数据失败" + msg);
                    }
                });
                break;

            // 查询数据
            case R.id.btn_queryUser:
                BmobQuery<Person> bmobQuery = new BmobQuery<>();
                bmobQuery.getObject(this, "654c847cc", new GetListener<Person>() {
                    @Override
                    public void onSuccess(Person person) {
                        toast("查询成功！");
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        toast("查询失败：" + msg);
                    }
                });
                break;

            // 修改数据
            case R.id.btn_modifyUser:
                person = new Person();
                person.setAddress("北京朝阳");
                person.setName("Heat");
                person.update(this, "63fd419c96", new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        toast("更新成功" + person.getUpdatedAt());
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        toast("更新失败" + msg);
                    }
                });
                break;

            case R.id.btn_deleteUser:
                person = new Person();
                person.setObjectId("acfe2f5846");
                person.delete(this, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        toast("删除成功");
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        toast("删除失败" + msg);
                    }
                });
                break;
            default:
                break;

        }
    }
}
