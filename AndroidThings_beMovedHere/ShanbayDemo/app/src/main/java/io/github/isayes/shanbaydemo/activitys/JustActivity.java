package io.github.isayes.shanbaydemo.activitys;

import android.os.Bundle;

import io.github.isayes.shanbaydemo.R;
import io.github.isayes.shanbaydemo.utils.TextViewUtil;
import io.github.isayes.shanbaydemo.views.JustifyTextView;

/**
 * Author: HF
 * Date:   2016-06-07
 * Description: 文章两端对齐的展示
 */
public class JustActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupData() {
        setContentView(R.layout.activity_just, R.string.NewConcept4, MODE_BACK_NAVIGATION);
        JustifyTextView tv_article = $(R.id.id_tv);
        mPageTitle.setText(getIntent().getStringExtra("just_title"));
        String content = getIntent().getStringExtra("just_content")/*.replaceAll("[^\\x00-\\xff]+", "")*/;
        tv_article.setText(content);
        TextViewUtil.getWordClickedHighLight(tv_article, content);
    }
}
