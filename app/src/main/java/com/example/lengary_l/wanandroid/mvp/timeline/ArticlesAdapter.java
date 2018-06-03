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
import com.example.lengary_l.wanandroid.data.ArticleDetailData;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ArticleDetailData> mList;

    public ArticlesAdapter(Context context, List<ArticleDetailData> list){
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        mList = list;
    }

    public void updateData(List<ArticleDetailData> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
        notifyItemRemoved(list.size());
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_card_view, parent, false);
        return new NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        ArticleDetailData data = mList.get(position);
        normalViewHolder.textAuthor.setText(data.getAuthor());
        normalViewHolder.textTitle.setText(data.getTitle());
        //if the text is too long, the button can not show it correctly.The solution is adding " ".
        normalViewHolder.btnCategory.setText("  "+data.getSuperChapterName()+"  ");
        normalViewHolder.textTime.setText(data.getNiceDate());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class NormalViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        AppCompatButton btnCategory;
        AppCompatTextView textTitle;
        AppCompatTextView textAuthor;
        AppCompatTextView textTime;



        public NormalViewHolder(View itemView) {
            super(itemView);
            btnCategory = itemView.findViewById(R.id.btn_category);
            textTitle = itemView.findViewById(R.id.text_view_title);
            textAuthor = itemView.findViewById(R.id.text_view_author);
            textTime = itemView.findViewById(R.id.text_view_time);
            cardView = itemView.findViewById(R.id.card_view_layout);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }
}
