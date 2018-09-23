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

package com.example.lengary_l.wanandroid.CustomComponent.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lengary_l.wanandroid.CustomComponent.banner.ViewPager.CustomBannerViewPager;
import com.example.lengary_l.wanandroid.CustomComponent.banner.interfaze.ImageLoader;
import com.example.lengary_l.wanandroid.CustomComponent.banner.interfaze.OnBannerListener;
import com.example.lengary_l.wanandroid.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoderLengary
 */
public class CustomBanner extends FrameLayout implements ViewPager.OnPageChangeListener {


    //指示器
    private int indicatorWidth;
    private int indicatorHeight;
    private int indicatorLeftMargin;
    private int indicatorRightMargin;
    private int defaultWidth;
    private int selectIndicatorRes ;
    private int unselectIndicatorRes ;
    private List<ImageView> indicatorViews;
    private int indicatorGravity ;

    //指示器容器
    private LinearLayout indicatorContainer;

    //ViewPager
    private CustomBannerViewPager viewPager;

    //滑动时间
    private int delayTime ;
    private int scrollDuration ;

    //自动滑动和手动滑动
    private boolean isAutoPlay ;
    private boolean isManuallyScrollable ;

    //图片加载器
    private ImageLoader imageLoader;

    //图片
    private List<String> imageUrls;
    private List<View> imageViews;
    private int count;
    private int scaleType;

    //点击监听器
    private OnBannerListener listener;

    //滑动监听器
    private ViewPager.OnPageChangeListener onPageChangeListener;


    private Context context;
    private BannerPagerAdapter adapter;
    private int currentItem;

    private FrameLayout bannerDetailLayout;
    private int screenHeight;

    private WeakHandler handler = new WeakHandler();

    private static final String TAG = "CustomBanner";
    public CustomBanner(@NonNull Context context) {
        this(context, null);
    }

    public CustomBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CustomBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        imageUrls = new ArrayList<>();
        imageViews = new ArrayList<>();
        indicatorViews = new ArrayList<>();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        defaultWidth = dm.widthPixels/80;
        screenHeight = dm.heightPixels;
        indicatorGravity = Gravity.CENTER;
        initViews(context, attrs);
    }

    //数据和界面初始化
    private void initViews(@NonNull Context context, @Nullable AttributeSet attrs) {
        initAttr(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.banner, this, true);

        indicatorContainer = view.findViewById(R.id.circle_indicator_container);
        viewPager = view.findViewById(R.id.custom_view_pager);

        setViewPagerScrollDuration();
    }



    private void initAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomBanner);
        indicatorWidth = typedArray.getDimensionPixelOffset(R.styleable.CustomBanner_indicatorWidth, defaultWidth);
        indicatorHeight = typedArray.getDimensionPixelOffset(R.styleable.CustomBanner_indicatorHeight, defaultWidth);
        indicatorLeftMargin = typedArray.getDimensionPixelOffset(R.styleable.CustomBanner_indicatorLeftMargin, BannerConfig.MARGINING_SIZE);
        indicatorRightMargin = typedArray.getDimensionPixelOffset(R.styleable.CustomBanner_indicatorRightMargin, BannerConfig.MARGINING_SIZE);
        selectIndicatorRes = typedArray.getResourceId(R.styleable.CustomBanner_selectIndicatorRes, R.drawable.gray_radius);
        unselectIndicatorRes = typedArray.getResourceId(R.styleable.CustomBanner_unselectIndicatorRes, R.drawable.white_radius);

        delayTime = typedArray.getInt(R.styleable.CustomBanner_delayTime, BannerConfig.DELAY_TIME);
        scrollDuration = typedArray.getInt(R.styleable.CustomBanner_scrollDuration, BannerConfig.SCROLL_DURATION);

        isAutoPlay = typedArray.getBoolean(R.styleable.CustomBanner_isAutoPlay, BannerConfig.IS_AUTO_PLAY);
        isManuallyScrollable = typedArray.getBoolean(R.styleable.CustomBanner_isManuallyScrollable, BannerConfig.IS_SCROLL);

        scaleType = typedArray.getInteger(R.styleable.CustomBanner_scaleType, 1);
        typedArray.recycle();
    }

    private CustomBanner setIndicatorGravity(int type) {
        switch (type) {
            case BannerConfig.LEFT:
                indicatorGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                break;

            case BannerConfig.CENTER:
                indicatorGravity = Gravity.CENTER;
                break;

            case BannerConfig.RIGHT:
                indicatorGravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;

            default:
                indicatorGravity = Gravity.CENTER;
        }
        return this;
    }

    //ViewPager滑动的时长是很短的，我们需要通过反射自定义时长
    private void setViewPagerScrollDuration() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            //mScroller是私有的变量
            mField.setAccessible(true);

            BannerScroller scroller = new BannerScroller(viewPager.getContext());
            scroller.setDuration(scrollDuration);
            //设置当前的viewPager的Scroller是我们定义的scroller
            mField.set(viewPager, scroller);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public CustomBanner setDelayTime(int time) {
        delayTime = time;
        return this;
    }

    public CustomBanner setScrollDuration(int time) {
        scrollDuration = time;
        return this;
    }

    public CustomBanner isAutoPlay(boolean e) {
        isAutoPlay = e;
        return this;
    }



    public CustomBanner setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public CustomBanner setImages(List<String> imageUrls) {

        this.imageUrls = imageUrls;
        count = imageUrls.size();
        return this;
    }


    private void displayImages(List<String> imageUrls) {
        imageViews.clear();
        if (!imageUrls.isEmpty()) {
            for (int i = 0; i < count + 2; i++) {
                ImageView view;
                if (imageLoader != null) {
                     view = imageLoader.createImageView(context);
                }else {
                    view = new ImageView(context);
                }
                setScaleType(view);

                //由于普通循环会产生闪屏现象，所以用以下方法解决
                //多生成两张图片，位置为0的图片是urls的最后一个，位置为最后的图片是urls的第一个
                //中间的其它图片就是url里面的所有图片
                //所以如果urls有3个【图片地址0，图片地址1，图片地址2】
                //那么最终生成的图片有5个【图片地址2, 图片地址0，图片地址1，图片地址2, 图片地址0】
                //当右滑动到最后一个图片的时候，currentItem立刻指向第二张图片(虽然位置不同，但是图片是相同的)
                //当左滑动到第一张图片的时候，currentItem立刻指向倒数第二张图片(同理)
                //这样就巧妙造成了“循环滑动”的假象
                String url;
                if (i == 0) {
                    url = imageUrls.get(count - 1);
                } else if (i == count + 1){
                    url = imageUrls.get(0);
                } else {
                    url = imageUrls.get(i - 1);
                }
                imageLoader.displayImage(context, url, view);
                imageViews.add(view);

            }
        }
    }

    //特别注意：当urls有三个子项的时候
    //当前viewPager的Position: 0   1 2 3   4
    //应当返回的Url的Position : 2   0 1 2   0
    private int getRealUrlPosition(int fakePosition) {
        if (fakePosition == 0) {
            return count - 1;
        }else if (fakePosition == (count+1)) {
            return 0;
        }else {
            return fakePosition - 1;
        }
    }

    //特别注意：当urls有三个子项的时候
    //当前viewPager的Position    : 0   1 2 3    4
    //应当返回的真正的页面Position : 3   1 2 3   1
    private int getRealCurrentPosition(int fakePosition) {
        if (fakePosition == 0) {
            return count;
        }else if (fakePosition == (count + 1)) {
            return 1;
        }else {
            return fakePosition;
        }
    }

    private void setScaleType(ImageView view) {
        switch (scaleType) {
            case 0:
                view.setScaleType(ImageView.ScaleType.CENTER);
                break;
            case 1:
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case 2:
                view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
            case 3:
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                break;
            case 4:
                view.setScaleType(ImageView.ScaleType.FIT_END);
                break;
            case 5:
                view.setScaleType(ImageView.ScaleType.FIT_START);
                break;
            case 6:
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
            case 7:
                view.setScaleType(ImageView.ScaleType.MATRIX);
                break;
        }
    }

    private void displayIndicator() {
        indicatorViews.clear();
        indicatorContainer.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorWidth, indicatorHeight);
            params.leftMargin = indicatorLeftMargin;
            params.rightMargin = indicatorRightMargin;
            if (i == 0) {
                imageView.setImageResource(R.drawable.gray_radius);
            }else {
                imageView.setImageResource(R.drawable.white_radius);
            }
            indicatorViews.add(imageView);
            indicatorContainer.addView(imageView, params);
        }
    }


    public CustomBanner setOnBannerListener(OnBannerListener listener) {
        this.listener = listener;
        return this;
    }

    public CustomBanner setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        return this;
    }


    public CustomBanner start() {

        displayImages(imageUrls);
        displayIndicator();
        setIndicatorContainerGravity();
        setViewPager();
        if (isAutoPlay) {
            startAutoPlay();
        }
        return this;
    }

    public void startAutoPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);
    }

    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }


    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {

                //手动左滑到0，会停止自动滑动，同时currentItem会被重置为3，手指离开
                //开始自动滑动，currentItem+1=4 4%4+1=1 currentItem = 1 currentItem = 4 与 =1显示的图片是一样的。smoothScroll为false，无动画效果
                //当是其他情况的时候，smoothScroll为true，动画效果显现出来
                currentItem = currentItem % (count + 1) + 1;
                if (currentItem == 1) {
                    viewPager.setCurrentItem(currentItem, false);
                    handler.post(task);
                }else {
                    viewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task, delayTime);
                }

            }

        }
    };

    //处理手动触摸逻辑，手指按下就停止滑动，手指放开就开始滑动
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            }else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void setIndicatorContainerGravity() {
        indicatorContainer.setGravity(indicatorGravity);
    }

    private void setViewPager() {
        if (adapter == null) {

            adapter = new BannerPagerAdapter();
            viewPager.addOnPageChangeListener(this);
        }
        currentItem = 1;
        viewPager.setAdapter(adapter);
        viewPager.setFocusable(true);
        viewPager.setCurrentItem(currentItem);
        if (isManuallyScrollable && count > 1) {
            viewPager.setScrollable(true);
        } else {
            viewPager.setScrollable(false);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(getRealUrlPosition(position), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
        int realPosition = getRealUrlPosition(position);
        for (int i =0; i < count; i++) {
            indicatorViews.get(i).setImageResource(i == realPosition ? R.drawable.gray_radius : R.drawable.white_radius);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }

        //当currentItem为0的时候，会被重置为3
        //当currentItem为4的时候，会被重置为1
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE://无操作
                viewPager.setCurrentItem(getRealCurrentPosition(currentItem), false);
                break;
            case ViewPager.SCROLL_STATE_DRAGGING://开始滑动
                viewPager.setCurrentItem(getRealCurrentPosition(currentItem), false);
                break;

            case ViewPager.SCROLL_STATE_SETTLING://停止滑动
                break;
        }

    }


    /*
     ViewPager里面对每个页面的管理是key-value形式的，
     也就是说每个page都有个对应的id（id是object类型），
     需要对page操作的时候都是通过id来完成的
     */
    class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        //判断当前page的id是不是object
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        //返回每个page的id，这里我们选择直接把view作为id
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {

            View view = imageViews.get(position);
            container.addView(view);
            if (listener != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onBannerClick(getRealUrlPosition(position));
                    }
                });
            }
            return view;
        }

        //超过limit需要执行删除页面逻辑，我们要移除view，只要把当前页面的ID object传入就好(Object 就是 view)
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }




}
