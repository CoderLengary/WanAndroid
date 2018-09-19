package com.example.lengary_l.wanandroid.interfaze;

import com.example.lengary_l.wanandroid.data.CategoryDetailData;

import java.util.List;

/**
 * Created by CoderLengary
 */


public interface OnFlowLayoutItemOnClickListener {

    void onClick(int position, List<CategoryDetailData> children);
}
