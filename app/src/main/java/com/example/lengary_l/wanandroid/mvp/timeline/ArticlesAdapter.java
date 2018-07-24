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
import com.example.lengary_l.wanandroid.interfaze.OnCategoryOnClickListener;
import com.example.lengary_l.wanandroid.interfaze.OnRecyclerViewItemOnClickListener;
import com.example.lengary_l.wanandroid.util.StringUtil;

import java.util.List;

/**
 * Created by CoderLengary
 */


public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ArticleDetailData> mList;
    private OnRecyclerViewItemOnClickListener listener;
    private OnCategoryOnClickListener categoryListener;

    public static final int HEADER_VIEW = 0;
    public static final int NORMAL_VIEW = 1;
    private View mHeaderView;

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

    public void setItemClickListener(OnRecyclerViewItemOnClickListener listener){
        this.listener = listener;
    }

    public void setCategoryListener(OnCategoryOnClickListener listener) {
        categoryListener = listener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW) {
            return new NormalViewHolder(mHeaderView, null, null);
        }
        View view = inflater.inflate(R.layout.item_article, parent, false);
        return new NormalViewHolder(view,listener,categoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == HEADER_VIEW) {
            return;
        }
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        ArticleDetailData data = mList.get(getRealPosition(position));
        normalViewHolder.textAuthor.setText(data.getAuthor());
        normalViewHolder.textTitle.setText(StringUtil.replaceInvalidChar(data.getTitle()));
        //if the text is too long, the button can not show it correctly.The solution is adding " ".
        normalViewHolder.btnCategory.setText("  "+data.getChapterName()+"  ");
        normalViewHolder.textTime.setText(data.getNiceDate());
    }

    private int getRealPosition(int position) {
        if (null != mHeaderView) {
            return position - 1;
        }
        return position;
    }


    @Override
    public int getItemCount() {
        return mList.size()+1;
    }


    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return NORMAL_VIEW;
        if (position == 0) return HEADER_VIEW;
        return NORMAL_VIEW;
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
            if (itemView == mHeaderView) {
                return;
            }
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
                    listener.onClick(view,getRealPosition(getAdapterPosition()));
                    break;

                case R.id.btn_category:
                    categoryListener.onClick(view,getRealPosition(getAdapterPosition()));
                    break;
                default:break;

            }
        }
    }
}
