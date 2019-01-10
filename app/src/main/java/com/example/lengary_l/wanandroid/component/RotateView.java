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

package com.example.lengary_l.wanandroid.component;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.util.BitmapUtil;

/**
 * Created by CoderLengary
 */
public class RotateView extends android.support.v7.widget.AppCompatImageView {

    private int canvasDegree;

    public int getCanvasDegree() {
        return canvasDegree;
    }

    public void setCanvasDegree(int canvasDegree) {
        this.canvasDegree = canvasDegree;
        invalidate();
    }

    public int getLeftCameraDegree() {
        return leftCameraDegree;
    }

    public void setLeftCameraDegree(int leftCameraDegree) {
        this.leftCameraDegree = leftCameraDegree;
        invalidate();
    }

    public int getRightCameraDegree() {
        return rightCameraDegree;
    }

    public void setRightCameraDegree(int rightCameraDegree) {
        this.rightCameraDegree = rightCameraDegree;
        invalidate();
    }

    private int leftCameraDegree;
    private int rightCameraDegree;





    private Bitmap bitmap;
    Paint paint;
    Camera camera;
    private int bitmapWidth;
    private int bitmapHeight;
    AnimatorSet animatorSet;
    public RotateView(Context context) {
        super(context);
    }

    public RotateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RotateView);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) array.getDrawable(R.styleable.RotateView_viewBackgound2);
        array.recycle();

        if (bitmapDrawable != null) {

            bitmap = bitmapDrawable.getBitmap();
            byte[] bytes = BitmapUtil.bitmap2Bytes(bitmap);
            bitmap = BitmapUtil.resizeBitmapBytes(bytes);
        }else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }

        camera = new Camera();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float newZ = -metrics.density * 6;
        camera.setLocation(0, 0, newZ);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ObjectAnimator rotateAnimator = ObjectAnimator.ofInt(this, "canvasDegree", 0, 270);
        ObjectAnimator leftCameraAnimator = ObjectAnimator.ofInt(this, "leftCameraDegree", 0, 45);
        ObjectAnimator rightCameraAnimator = ObjectAnimator.ofInt(this, "rightCameraDegree", 0, -45);
        rotateAnimator.setDuration(2000);
        leftCameraAnimator.setDuration(1000);
        rightCameraAnimator.setDuration(1000);
        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(rightCameraAnimator, rotateAnimator, leftCameraAnimator);
        animatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animatorSet.cancel();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        int x = width/2 - bitmapWidth/2;
        int y = height/2 - bitmapHeight/2;


        //右半部分绘制
        camera.save();
        canvas.save();
        canvas.translate(width/2, height/2);
        canvas.rotate(-canvasDegree);
        camera.rotateY(rightCameraDegree);
        camera.applyToCanvas(canvas);
        canvas.clipRect(0, -height, width, height);
        canvas.rotate(canvasDegree);
        canvas.translate(-width/2, -height/2);
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();
        camera.restore();

        //左半部分绘制
        camera.save();
        canvas.save();
        canvas.translate(width/2, height/2);
        canvas.rotate(-canvasDegree);
        camera.rotateY(leftCameraDegree);
        camera.applyToCanvas(canvas);
        canvas.clipRect(-width, -height, 0, height);
        canvas.rotate(canvasDegree);
        canvas.translate(-width/2, -height/2);
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();
        camera.restore();



    }
}
