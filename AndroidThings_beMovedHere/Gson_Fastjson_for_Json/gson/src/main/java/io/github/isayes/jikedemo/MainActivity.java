package io.github.isayes.jikedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import io.github.isayes.jikedemo.bean.BookInfo;

/**
 * 对 Gson 的基本使用
 * https://api.douban.com/v2/book/1220562
 * 用 Gson 高效解析 JsonObject 和 JsonArray。
 */
public class MainActivity extends AppCompatActivity {

    private String URL = "https://api.douban.com/v2/book/1220562";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
    }

    private void getData() {

        // 建立请求
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("info......", response);
                // 对结果进行解析
                dealData(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // 将请求加入到 Volley 队列里面
        new Volley().newRequestQueue(getApplicationContext()).add(request);
    }

    // 对返回的 Json 数据结构进行解析，转换成一个实体
    // 如果结果正确，应该会是“ 满月之夜白鲸现:青岛出版社:8 ”
    private void dealData(String result) {
        Gson gson = new Gson();
        BookInfo bookInfo = gson.fromJson(result, BookInfo.class);
        Log.i("info......", bookInfo.getTitle() + ":" + bookInfo.getPublisher() + ":" + bookInfo.getTags().size());
    }
}






























