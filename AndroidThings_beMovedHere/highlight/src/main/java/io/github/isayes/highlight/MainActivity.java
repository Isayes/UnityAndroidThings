package io.github.isayes.highlight;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;
import java.util.Locale;

/**
 * Author: HF
 * Date:   2016-06-07
 * Description: 单词高亮的 Demo
 */
public class MainActivity extends AppCompatActivity {

    private String content;
    private TextView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentView = (TextView) findViewById(R.id.id_content);
        // content = ArticleContent.trim().replaceAll("[\\u4e00-\\u9fa5]+","");
        content = ArticleContent.trim();
        getWordClicked(contentView, content);
    }

    // Learn from http://stackoverflow.com/questions/8612652/select-a-word-on-a-tap-in-textview-edittext
    public void getWordClicked(TextView contentTextView, String content) {
        contentTextView.setMovementMethod(LinkMovementMethod.getInstance());
        contentTextView.setText(content, TextView.BufferType.SPANNABLE);
        Spannable spans = (Spannable) contentTextView.getText();
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(content);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            String possibleWord = content.substring(start, end);
            if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                ClickableSpan clickSpan = getClickableSpan(possibleWord);
                spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public static ClickableSpan getClickableSpan(final String word) {
        return new ClickableSpan() {

            final String mWord;

            {
                mWord = word;
            }

            @Override
            public void onClick(View widget) {
                // TODO 一些点击事件，比如弹出单词解释
                TextView textView = (TextView) widget;
                String wordClicked = textView.getText().subSequence(textView.getSelectionStart(), textView.getSelectionEnd()).toString();
                Log.i("mWord == wordClicked ?", " → " + wordClicked.equals(mWord));
                // 两个 toast 内容相同
                Toast.makeText(widget.getContext(), wordClicked, Toast.LENGTH_SHORT).show();
                //Toast.makeText(widget.getContext(), mWord, Toast.LENGTH_SHORT).show();

                /*SpannableStringBuilder style = new SpannableStringBuilder(textView.getText().toString());
                style.setSpan(new ForegroundColorSpan(Color.RED), textView.getSelectionStart(), textView.getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(style);*/
            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE);
                ds.setUnderlineText(true); // false 移除下划线
            }
        };
    }

    private static final String ArticleContent = "   First listen and then answer the following question.\n" +
            "听录音，然后回答以下问题。\n" +
            "\n" +
            "Why are legends handed down by storytellers useful?\n" +
            "\n" +
            "    We can read of things that happened 5,000 years ago in the Near East, where people first learned to write. But there are some parts of the word where even now people cannot write. The only way that they can preserve their history is to recount it as sagas -- legends handed down from one generation of another. These legends are useful because they can tell us something about migrations of people who lived long ago, but none could write down what they did. Anthropologists wondered where the remote ancestors of the Polynesian peoples now living in the Pacific Islands came from. The sagas of these people explain that some of them came from Indonesia about 2,000 years ago.\n" +
            "    But the first people who were like ourselves lived so long ago that even their sagas, if they had any, are forgotten. So archaeologists have neither history nor legends to help them to find out where the first 'modern men' came from.\n" +
            "    Fortunately, however, ancient men made tools of stone, especially flint, because this is easier to shape than other kinds. They may also have used wood and skins, but these have rotted away. Stone does not decay, and so the tools of long ago have remained when even the bones of the men who made them have disappeared without trace.\n" +
            "                              ROBIN PLACE Finding fossil man\n" +
            "\n" +
            "\n" +
            "New words and expressions 生词和短语\n" +
            "\n" +
            "    fossil man (title)\n" +
            "adj. 化石人\n" +
            "    recount\n" +
            "v.  叙述\n" +
            "    saga\n" +
            "n.  英雄故事\n" +
            "    legend\n" +
            "n.  传说，传奇\n" +
            "    migration\n" +
            "n.  迁移，移居\n" +
            "    anthropologist\n" +
            "n.  人类学家\n" +
            "    archaeologist\n" +
            "n.  考古学家\n" +
            "    ancestor\n" +
            "n.  祖先\n" +
            "    Polynesian\n" +
            "adj.波利尼西亚（中太平洋之一群岛）的\n" +
            "    Indonesia\n" +
            "n.  印度尼西亚\n" +
            "    flint\n" +
            "n.  燧石\n" +
            "    rot\n" +
            "n.  烂掉\n" +
            "\n" +
            "参考译文\n" +
            "    我们从书籍中可读到5,000 年前近东发生的事情，那里的人最早学会了写字。但直到现在,世界上有些地方，人们还不会书写。 他们保存历史的唯一办法是将历史当作传说讲述，由讲述人一代接一代地将史实描述为传奇故事口传下来。人类学家过去不清楚如今生活在太平洋诸岛上的波利尼西亚人的祖先来自何方，当地人的传说却告诉人们：其中一部分是约在2,000年前从印度尼西亚迁来的。\n" +
            "    但是，和我们相似的原始人生活的年代太久远了，因此，有关他们的传说既使有如今也失传了。于是，考古学家们既缺乏历史记载，又无口头传说来帮助他们弄清最早的“现代人”是从哪里来的。\n" +
            "    然而， 幸运的是，远古人用石头制作了工具，特别是用燧石，因为燧石较之其他石头更容易成形。他们也可能用过木头和兽皮，但这类东西早已腐烂殆尽。石头是不会腐烂的。因此，尽管制造这些工具的人的骨头早已荡然无存，但远古时代的石头工具却保存了下来。\n";

}
