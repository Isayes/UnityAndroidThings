package io.github.isayes.shanbaydemo.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import io.github.isayes.shanbaydemo.R;

/**
 * Author: HF
 * Date:   2016-06-03
 * Description:
 */

public class RecyclerSimpleAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private Context mContext;
    private List<Map<String, Object>> mDatas;
    private LayoutInflater mInflater;

    public RecyclerSimpleAdapter(Context context, List<Map<String, Object>> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_for_article_list, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        Map<String, Object> map = mDatas.get(position);
        holder.tv_title.setText(map.get("title").toString());
        // holder.tv_content.setText(map.get("content").toString());
        setUpItemEvent(holder);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    protected void setUpItemEvent(final RecyclerViewHolder holder) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, layoutPosition);
                }
            });
        }
    }
}
