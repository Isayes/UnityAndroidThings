package io.github.isayes.shanbaydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.isayes.shanbaydemo.activitys.MainActivity;
import io.github.isayes.shanbaydemo.utils.Globals;
import io.github.isayes.shanbaydemo.utils.ToolbarUtil;
import io.github.isayes.shanbaydemo.utils.GetWordsList;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ToolbarUtil.setTransparent(this);
        Thread thread = new Thread() {

            @Override
            public void run() {
                GetWordsList getWordList = GetWordsList.getInstance();
                try {
                    InputStream is = getAssets().open("new_concept_4.txt");
                    Globals.data = loadInputStream(is);
                    getWordList.Load(getAssets().open("nce4_words.txt"));
                    sleep(2000);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                WelcomeActivity.this.finish();
            }
        };
        thread.start();
    }

    public List<Map<String, Object>> loadInputStream(InputStream is) throws IOException {

        List<Map<String, Object>> values = new ArrayList<>();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(is, "utf-8")
        );

        String line;
        StringBuffer title = new StringBuffer();
        StringBuffer content = new StringBuffer();
        boolean titleFlag = false;
        boolean makeNewLine = true;
        while ((line = br.readLine()) != null) {
            if (line.equals("START_ARTICLE_TITLE_HFlag")) {
                // 读文章标题的开始行
                titleFlag = true;
            } else if (line.equals("START_ARTICLE_CONTENT_HFlag")) {
                // 读文章内容的开始行
                titleFlag = false;
            } else if (line.equals("END_ARTICLE_CONTENT_HFlag")) {
                // 读文章内容的结束行，数据读完了，整合
                Map<String, Object> map = new HashMap<>();
                map.put("title", title.toString());
                map.put("content", content.toString());
                values.add(map);

                // 清空标题和文章内容的变量
                title = new StringBuffer();
                content = new StringBuffer();
                makeNewLine = true;
            } else {
                // 内容是多行的，读取数据
                if (titleFlag) {
                    title.append(line);
                    // 如果像 content 一样加 title.append("\n"); 就会出现多一行空白不能垂直居中，所以我使用下面方法
                    while (makeNewLine) {
                        title.append("\n");
                        makeNewLine = false;
                    }
                } else {
                    content.append(line);
                    content.append("\n");
                }
            }
        }

        return values;
    }
}
