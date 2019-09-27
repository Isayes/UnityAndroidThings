package io.github.isayes.shanbaydemo.utils;

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
 * Date:   2016-06-08
 * Description:
 */

public class TextViewUtil {

    public static void getWordClickedHighLight(TextView contentTextView, String content) {
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
                Toast.makeText(widget.getContext(), wordClicked, Toast.LENGTH_SHORT).show();
            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
    }

}
