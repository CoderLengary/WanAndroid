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

public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;


    public ArticlesAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(this.context);
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
        normalViewHolder.textAuthor.setText("Author");
        normalViewHolder.textTitle.setText("我所理解的模块化，对模块化的理解与发掘");
        normalViewHolder.btnCategory.setText("模块化");
    }

    @Override
    public int getItemCount() {
        return 20;
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
