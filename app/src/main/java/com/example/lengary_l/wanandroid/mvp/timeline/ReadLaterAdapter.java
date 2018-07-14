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
import com.example.lengary_l.wanandroid.data.ReadLaterArticleData;
import com.example.lengary_l.wanandroid.interfaze.OnCategoryOnClickListener;
import com.example.lengary_l.wanandroid.interfaze.OnRecyclerViewItemOnClickListener;
import com.example.lengary_l.wanandroid.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by CoderLengary
 */


public class ReadLaterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ReadLaterArticleData> mList;
    private OnRecyclerViewItemOnClickListener listener;
    private OnCategoryOnClickListener categoryListener;
    SimpleDateFormat simpleDateFormat;

    public ReadLaterAdapter(Context context, List<ReadLaterArticleData> list){
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        mList = list;
        simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    }

    public void updateData(List<ReadLaterArticleData> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
        notifyItemRemoved(list.size());
    }

    public void setItemClickListener(OnRecyclerViewItemOnClickListener listener){
        this.listener = listener;
    }

    public void setCategoryListener(OnCategoryOnClickListener listener) {
        categoryListener = listener;
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
        ReadLaterArticleData data = mList.get(position);
        normalViewHolder.textAuthor.setText(data.getAuthor());
        normalViewHolder.textTitle.setText(StringUtil.replaceInvalidChar(data.getTitle()));
        //if the text is too long, the button can not show it correctly.The solution is adding " ".
        normalViewHolder.btnCategory.setText("  "+data.getChapterName()+"  ");
        normalViewHolder.textTime.setText(simpleDateFormat.format(new Date(data.getTimestamp())));
    }



    @Override
    public int getItemCount() {
        return mList.size();
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
            this.listener = listener;
            this.categoryListener = categoryListener;
            btnCategory = itemView.findViewById(R.id.btn_category);
            btnCategory.setOnClickListener(this);
            textTitle = itemView.findViewById(R.id.text_view_title);
            textAuthor = itemView.findViewById(R.id.text_view_author);
            textTime = itemView.findViewById(R.id.text_view_time);
            cardView = itemView.findViewById(R.id.card_view_layout);
            cardView.setOnClickListener(this);
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
