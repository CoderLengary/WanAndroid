package com.example.lengary_l.wanandroid.mvp.categories;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.CategoryDetailData;
import com.example.lengary_l.wanandroid.interfaze.OnFlowLayoutItemOnClickListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Created by CoderLengary
 */


public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CategoryDetailData> list;
    private LayoutInflater inflater;
    private OnFlowLayoutItemOnClickListener listener;

    public CategoriesAdapter(Context context, List<CategoryDetailData> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(this.context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_categories, parent, false));
    }

    public void updateData(List<CategoryDetailData> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
        notifyItemRemoved(list.size());
    }

    public void setOnFlowLayoutItemOnClickListener(OnFlowLayoutItemOnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryDetailData data = list.get(position);
        final List<CategoryDetailData> children = data.getChildren();
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.textView.setText(data.getName());
        myViewHolder.flowLayout.setAdapter(new TagAdapter<CategoryDetailData>(children) {

            @Override
            public View getView(FlowLayout parent, int position, CategoryDetailData child) {
                TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flow_layout
                        , myViewHolder.flowLayout, false);
                if (child==null){
                    return null;
                }
                textView.setText(child.getName());

                myViewHolder.flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        listener.onClick(view, position, parent ,children);
                        return true;
                    }
                });
                return textView;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TagFlowLayout flowLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text_category_title);
            flowLayout = itemView.findViewById(R.id.flow_layout);
        }
    }
}
