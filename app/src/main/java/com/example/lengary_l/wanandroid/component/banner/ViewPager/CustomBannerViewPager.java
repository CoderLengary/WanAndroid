/*
 * Copyright (c) 2018 CoderLengary
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.lengary_l.wanandroid.component.banner.ViewPager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by CoderLengary
 */
public class CustomBannerViewPager extends ViewPager {

    //允许手动滑动
    private boolean mManuallyScrollable = true;

    public CustomBannerViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomBannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setScrollable(boolean manuallyScrollable) {
        mManuallyScrollable = manuallyScrollable;
    }

    //只有可以手动滑动，且有子项才可以执行onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mManuallyScrollable) {
            if (getChildCount() == 0 && getCurrentItem() == 0) {
                return false;
            }
            return super.onTouchEvent(ev);
        }
        return false;

    }

    //只有可以手动滑动，且有子项才可以拦截事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mManuallyScrollable) {
            if (getChildCount() == 0 && getCurrentItem() == 0) {
                return false;
            }
            return super.dispatchTouchEvent(ev);
        }
        return false;
    }
}
