package io.github.isayes.shanbaydemo.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.actinarium.aligned.TextView;

import io.github.isayes.shanbaydemo.R;
import io.github.isayes.shanbaydemo.utils.GetWordsList;
import io.github.isayes.shanbaydemo.utils.TextViewUtil;

/**
 * Author: HF
 * Date:   2016-06-02
 * Description: 文章页面
 */
public class ArticleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private TextView mArticleTextView;
    private RatingBar mRatingBar;
    private Switch mHighlightSwitcher;
    private String mArticleContent;
    private RelativeLayout mPanel;

    @Override
    protected void setupData() {
        setContentView(R.layout.activity_article, R.string.NewConcept4, MODE_BACK_NAVIGATION);
        mToolbar.setOnMenuItemClickListener(onMenuItemClick);
        mPanel = $(R.id.id_panel);
        getArticleContent();
        TextViewUtil.getWordClickedHighLight(mArticleTextView, mArticleContent);
        highlightKeywordsInArticle();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 当前 Activity 不处于可见或不在前台就不显示下滑的 panel
        mPanel.setVisibility(View.GONE);
    }

    // 一个点击事件
    public void goToJustAct(View view) {
        mButtonClicked = true;
        String articleContent = mArticleTextView.getText().toString();
        String articleTitle = getArticleTitle();
        Intent intent = new Intent(ArticleActivity.this, JustActivity.class);
        intent.putExtra("just_content", articleContent);
        intent.putExtra("just_title", articleTitle);
        startActivity(intent);
    }

    // Toolbar 菜单 start -------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private boolean panelFlag = true;

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_search:
                    if (panelFlag) {
                        mPanel.setVisibility(View.VISIBLE);
                        panelFlag = false;
                    } else {
                        if (mButtonClicked) {
                            mPanel.setVisibility(View.VISIBLE);
                            mButtonClicked = false; // 还原
                        } else {
                            mPanel.setVisibility(View.GONE);
                            panelFlag = true;
                        }
                    }
                    break;
            }
            return true;
        }
    };

    // --------------------------------------------------------------------------- Toolbar 菜单 end

    // 获取内容 start ------------------------------------------------------------------------------

    private String getArticleTitle() {
        String oldTitle = getIntent().getStringExtra("title");
        return doSplit(oldTitle);
    }

    private String doSplit(String oldTitle) {
        oldTitle = oldTitle.substring(oldTitle.indexOf("·") + 2);
        return oldTitle.replaceAll("[a-zA-Z\\s+]", "") + "\n" + oldTitle.replaceAll("[^a-zA-Z\\s+]", "");
    }

    private void getArticleContent() {
        mPageTitle.setText(getArticleTitle());
        mArticleTextView = $(R.id.id_tv_article);
        mArticleTextView.setText(getIntentData());
        mArticleContent = mArticleTextView.getText().toString().trim();
    }

    private String getIntentData() {
        String content = getIntent().getStringExtra("content");
        // 解决首篇文章开头多了一行空白的 bug
        String contentStart = content.substring(0, 2).replaceAll("[\t\n]", "");
        content = contentStart + content.substring(2);
        return content;
    }

    // -------------------------------------------------------------------------------- 获取内容 end

    // 高亮文章关键字处理 start ---------------------------------------------------------------------
    private boolean mButtonClicked = false;

    public void closePanel(View view) {
        mPanel.setVisibility(View.GONE);
        mButtonClicked = true;
        mRatingBar.setRating(0);
        //mArticleTextView.setText(getIntentData());
        TextViewUtil.getWordClickedHighLight(mArticleTextView, mArticleContent);
    }

    private void highlightKeywordsInArticle() {
        mRatingBar = $(R.id.id_ratingBar);
        mRatingBar.setStepSize(1.0f);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rate, boolean b) {
                showToast(getString(R.string.now_highlight_level) + rate);
                if (rate > 0) {
                    new Thread(new HighlightRunnable((int) rate)).start();
                }
            }
        });

        mHighlightSwitcher = $(R.id.id_switch);
        mHighlightSwitcher.setEnabled(false);
        mHighlightSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    mHighlightSwitcher.setEnabled(false);
                    mRatingBar.setRating(0);
                    mButtonClicked = true;
                    mArticleTextView.setText(getIntentData());
                }
            }
        });
    }

    private static final int FINISH = 1;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FINISH) {
                mArticleTextView.setText(styled);
                mHighlightSwitcher.setChecked(true);
                mHighlightSwitcher.setEnabled(true);
            }
        }
    };

    private GetWordsList getWords = GetWordsList.getInstance();
    SpannableStringBuilder styled;

    class HighlightRunnable implements Runnable {

        int rating;

        public HighlightRunnable(int rating) {
            this.rating = rating;
        }

        @Override
        public void run() {
            styled = new SpannableStringBuilder(mArticleContent);

            switch (rating) {
                case 5:
                    for (String word : getWords.wordsOfLevel_5) {
                        int start = mArticleContent.indexOf(word);
                        int end = start + word.length();
                        if (start < 0) {
                            continue;
                        }
                        styled.setSpan(new BackgroundColorSpan(Color.argb(255,31,157,133)),
                                start,
                                end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        );
                    }
                case 4:
                    for (String word : getWords.wordsOfLevel_4) {
                        int start = mArticleContent.indexOf(word);
                        int end = start + word.length();
                        if (start < 0) {
                            continue;
                        }
                        styled.setSpan(new BackgroundColorSpan(Color.argb(255,31,157,133)),
                                start,
                                end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        );
                    }
                case 3:
                    for (String word : getWords.wordsOfLevel_3) {
                        int start = mArticleContent.indexOf(word);
                        int end = start + word.length();
                        if (start < 0) {
                            continue;
                        }
                        styled.setSpan(new BackgroundColorSpan(Color.argb(255,31,157,133)),
                                start,
                                end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        );
                    }
                case 2:
                    for (String word : getWords.wordsOfLevel_2) {
                        int start = mArticleContent.indexOf(word);
                        int end = start + word.length();
                        if (start < 0) {
                            continue;
                        }
                        styled.setSpan(new BackgroundColorSpan(Color.argb(255,31,157,133)),
                                start,
                                end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        );
                    }
                case 1:
                    for (String word : getWords.wordsOfLevel_1) {
                        int start = mArticleContent.indexOf(word);
                        int end = start + word.length();
                        if (start < 0) {
                            continue;
                        }
                        styled.setSpan(
                                new BackgroundColorSpan(Color.argb(255,31,157,133)),
                                start,
                                end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        );
                    }
            }
            handler.sendEmptyMessage(FINISH);
        }

    }

    // ----------------------------------------------------------------------- 高亮文章关键字处理 end
}
