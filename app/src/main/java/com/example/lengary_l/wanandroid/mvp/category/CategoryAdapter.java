package com.example.lengary_l.wanandroid.mvp.category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.interfaze.OnRecyclerViewItemOnClickListener;
import com.example.lengary_l.wanandroid.util.StringUtil;

import java.util.List;

/**
 * Created by CoderLengary
 */


public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ArticleDetailData> list;
    private OnRecyclerViewItemOnClickListener listener;

    public CategoryAdapter(Context context,List<ArticleDetailData> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(this.context);
    }

    public void setItemClickListener(OnRecyclerViewItemOnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_category_article, parent, false);
        return new CategoryViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        ArticleDetailData data = list.get(position);
        categoryViewHolder.textAuthor.setText(data.getAuthor());
        categoryViewHolder.textTitle.setText(StringUtil.replaceInvalidChar(data.getTitle()));
    }

    public void updateData(List<ArticleDetailData> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
        notifyItemRemoved(list.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private OnRecyclerViewItemOnClickListener listener;
        AppCompatTextView textAuthor;
        AppCompatTextView textTitle;
        public CategoryViewHolder(View itemView,OnRecyclerViewItemOnClickListener listener) {
            super(itemView);
            textAuthor = itemView.findViewById(R.id.text_view_author);
            textTitle = itemView.findViewById(R.id.text_view_title);
            this.listener = listener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getLayoutPosition());
        }
    }
}
