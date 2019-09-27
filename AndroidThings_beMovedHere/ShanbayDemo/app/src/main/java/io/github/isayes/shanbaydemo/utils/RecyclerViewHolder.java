package io.github.isayes.shanbaydemo.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.github.isayes.shanbaydemo.R;

/**
 * Author: HF
 * Date:   2016-06-03
 * Description:
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView tv_title;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        tv_title = (TextView) itemView.findViewById(R.id.id_item_title);
    }
}
