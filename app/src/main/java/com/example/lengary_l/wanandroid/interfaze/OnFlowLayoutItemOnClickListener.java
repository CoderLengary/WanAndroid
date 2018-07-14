package com.example.lengary_l.wanandroid.interfaze;

import android.view.View;

import com.example.lengary_l.wanandroid.data.CategoryDetailData;
import com.zhy.view.flowlayout.FlowLayout;

import java.util.List;

/**
 * Created by CoderLengary
 */


public interface OnFlowLayoutItemOnClickListener {

    void onClick(View view, int position, FlowLayout parent , List<CategoryDetailData> children);
}
