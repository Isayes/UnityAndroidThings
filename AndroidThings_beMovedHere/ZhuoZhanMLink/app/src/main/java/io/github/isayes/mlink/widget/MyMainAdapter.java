package io.github.isayes.mlink.widget;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.isayes.mlink.R;
import io.github.isayes.mlink.activity.FeedbackActivity;
import io.github.isayes.mlink.entity.TaskInfo;

/**
 * Author: HF
 * Date:   2016-05-06
 * Description:
 */

public class MyMainAdapter extends BaseAdapter {

    private Context mContext;
    private List<TaskInfo> mList;

    public MyMainAdapter(Context mContext, List mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            view = mLayoutInflater.inflate(R.layout.item_main_task_accepted, null);
            holder.ivAppLogo = $(R.id.id_app_logo, view);
            holder.tvAppName = $(R.id.id_task_app_name, view);
            holder.tvCompanyName = $(R.id.id_task_company_name, view);
            holder.tvTaskTodo = $(R.id.id_task_todo, view);
            holder.tvAppDescription = $(R.id.id_task_app_description, view);
            holder.btnAppOpen = $(R.id.id_app_open, view);
            holder.btnAppFeedback = $(R.id.id_app_feedback, view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        TaskInfo taskInfo = mList.get(position);
        taskInfo.getAppLogoPic();
        holder.tvAppName.setText(taskInfo.getAppName());
        holder.tvCompanyName.setText(taskInfo.getCompanyName());
        holder.tvTaskTodo.setText(taskInfo.getTaskTodo());
        holder.tvAppDescription.setText(taskInfo.getAppDescription());

        holder.btnAppOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(mContext.getString(R.string.toastClicked));
            }
        });
        holder.btnAppFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, FeedbackActivity.class));
            }
        });
        return view;
    }

    @SuppressWarnings("unchecked")
    public <TT> TT $(int id, View view) {
        return (TT) view.findViewById(id);
    }

    private void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public class ViewHolder {
        ImageView ivAppLogo;
        TextView tvAppName;
        TextView tvCompanyName;
        TextView tvTaskTodo;
        TextView tvAppDescription;
        Button btnAppOpen;
        Button btnAppFeedback;
    }

    // 刷新列表中的数据
    public void refresh(ArrayList<TaskInfo> list) {
        mList = list;
        notifyDataSetChanged();
    }
}


