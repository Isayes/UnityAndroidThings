package io.github.isayes.fast_json.bean;

import android.nfc.Tag;

import java.util.ArrayList;

/**
 * Author: HF
 * Date:   2016-05-09
 * Description:
 */

public class BookInfo {
    private String title;
    private String publisher;
    private String summary;
   private ArrayList<TagInfo> tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ArrayList<TagInfo> getTags() {
        return tags;
    }

    public void setTags(ArrayList<TagInfo> tags) {
        this.tags = tags;
    }
}
