package com.example.lengary_l.wanandroid.mvp.category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lengary_l.wanandroid.R;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{

        AppCompatTextView textAuthor;
        AppCompatTextView textTitle;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            textAuthor = itemView.findViewById(R.id.text_view_author);
            textTitle = itemView.findViewById(R.id.text_view_title);
        }
    }
}
