package com.example.lengary_l.wanandroid.mvp.timeline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;
import com.example.lengary_l.wanandroid.interfaze.OnCategoryOnClickListener;
import com.example.lengary_l.wanandroid.interfaze.OnRecyclerViewItemOnClickListener;
import com.example.lengary_l.wanandroid.util.StringUtil;

import java.util.List;

/**
 * Created by CoderLengary
 */


public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FavoriteArticleDetailData> mList;
    private Context context;
    private LayoutInflater inflater;
    private OnRecyclerViewItemOnClickListener listener;
    private OnCategoryOnClickListener categoryListener;

    public FavoritesAdapter(Context context, List<FavoriteArticleDetailData> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    public void updateData(List<FavoriteArticleDetailData> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
        notifyItemRemoved(list.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_article, parent, false);
        return new NormalViewHolder(view,listener,categoryListener);
}

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        FavoriteArticleDetailData data = mList.get(position);
        normalViewHolder.textAuthor.setText(data.getAuthor());
        normalViewHolder.textTitle.setText(StringUtil.replaceInvalidChar(data.getTitle()));
        //if the text is too long, the button can not show it correctly.The solution is adding " ".
        normalViewHolder.btnCategory.setText("  "+data.getChapterName()+"  ");
        normalViewHolder.textTime.setText(data.getNiceDate());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public void setItemClickListener(OnRecyclerViewItemOnClickListener listener){
        this.listener = listener;
    }

    public void setCategoryListener(OnCategoryOnClickListener listener) {
        categoryListener = listener;
    }
    class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnRecyclerViewItemOnClickListener listener;
        OnCategoryOnClickListener categoryListener;
        CardView cardView;
        AppCompatButton btnCategory;
        AppCompatTextView textTitle;
        AppCompatTextView textAuthor;
        AppCompatTextView textTime;



        public NormalViewHolder(View itemView, final OnRecyclerViewItemOnClickListener listener, final OnCategoryOnClickListener categoryListener) {
            super(itemView);
            btnCategory = itemView.findViewById(R.id.btn_category);
            btnCategory.setOnClickListener(this);
            textTitle = itemView.findViewById(R.id.text_view_title);
            textAuthor = itemView.findViewById(R.id.text_view_author);
            textTime = itemView.findViewById(R.id.text_view_time);
            cardView = itemView.findViewById(R.id.card_view_layout);
            cardView.setOnClickListener(this);
            this.listener = listener;
            this.categoryListener = categoryListener;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.card_view_layout:
                    listener.onClick(view,getAdapterPosition());
                    break;

                case R.id.btn_category:
                    categoryListener.onClick(view,getAdapterPosition());
                    break;
                default:break;

            }
        }
    }
}
