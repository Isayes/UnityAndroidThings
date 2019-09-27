package io.github.isayes.fast_json;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import io.github.isayes.fast_json.bean.BookInfo;

/**
 * 用 Fast-json 高效解析 JsonObject 和 JsonArray。
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
        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("info......", response);
                dealData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void dealData(String result) {
        BookInfo bookInfo = JSON.parseObject(result, BookInfo.class);
        // 如果是多本书的话，就解析成一个数组集合 ↓
        List<BookInfo> books = JSON.parseObject(result, new TypeReference<List<BookInfo>>() {
            //
        });

        BookInfo book1 = new BookInfo();
        book1.setTitle("标题1");
        BookInfo book2 = new BookInfo();
        book2.setTitle("标题2");
        BookInfo book3 = new BookInfo();
        book3.setTitle("标题3");
        // book1 转化成 json 数据的格式
        JSON.toJSON(book1);

        List<BookInfo> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
        // 列表转换成 json
        JSON.toJSON(bookList);

        Log.i("info......", bookInfo.getTitle() + ": "
                + bookInfo.getPublisher() + ": "
                + bookInfo.getTags().size());
    }
}
