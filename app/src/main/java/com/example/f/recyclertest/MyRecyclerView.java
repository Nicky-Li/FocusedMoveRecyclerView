package com.example.f.recyclertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

import java.io.InputStream;

/**
 * Created by f on 2016/9/12.
 */
public class MyRecyclerView extends RecyclerView implements BaseRecyclerAdapter.OnItemFocusChangedListner {

    private Context context;
    //焦点bitmap跟随scroller移动
    private Scroller scroller;
    //焦点处的bitmap
    private Bitmap focusedBitmap;
    //上一次获取焦点的View
    private View preFocusedView;
    //焦点bitmap生成的bitmap（原始大小，不变）
    private Rect focusedBitmapRect;
    //当前焦点bitmap所在位置的rect（随着scroller移动）
    private RectF curFocusedViewRectF;
    //当前获取焦点的View
    private View curFocusedView;
    //recyclerView在滑动的时候需重置scroller的终点位置
    private boolean isRecyclerScrolling;
    //焦点View在上层还是下层，默认下层
    private boolean isFocusedFront;
    //焦点大小比每一项大多少
    private int padding = 2;

    public MyRecyclerView(Context context) {
        super(context);
        initData(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    private void initData(Context context) {
        this.context = context;
        scroller = new Scroller(this.context);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                isRecyclerScrolling = true;
                if (curFocusedViewRectF == null) {
                    return;
                }
                //重置scroller的终点
                scroller.setFinalX(scroller.getFinalX() - dx);
                scroller.setFinalY(scroller.getFinalY() - dy);

                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFocusedFront) {
            return;
        }
        drawFocused(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!isFocusedFront) {
            return;
        }

        drawFocused(canvas);
    }



    private void drawFocused(Canvas canvas) {
        if (curFocusedViewRectF == null || focusedBitmap == null) {
            return;
        }
        canvas.drawBitmap(focusedBitmap, focusedBitmapRect, curFocusedViewRectF, null);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
//        Log.d("MyRecyclerView", "computeScroll");
        if (focusedBitmap == null) {
            return;
        }
        if (preFocusedView == null) {
            preFocusedView = curFocusedView;
        }
        if (scroller.computeScrollOffset()) {

            int distanceX = scroller.getFinalX() - scroller.getStartX();
            int distanceY = scroller.getFinalY() - scroller.getStartY();
            int currX = scroller.getCurrX();
            int currY = scroller.getCurrY();

            boolean isXMoreThanY = Math.abs(distanceX) >= Math.abs(distanceY);
            int distance = isXMoreThanY ? distanceX : distanceY;

            float percent;
            if (distance == 0 && isRecyclerScrolling) {
                //recyclerview在滑动的时候可能scroller.getFinalX() = scroller.getStartX()造成distanceX为0（要排除这种情况）
                percent = 1;
            } else if (distance == 0) {
                percent = 0;
            } else {
                //当前滑动的百分比
                percent = isXMoreThanY ? (currX - scroller.getStartX()) / (float) distanceX : (currY - scroller.getStartY()) / (float) distanceY;

            }
            //焦点bitmap的右边距
            float right = currX + preFocusedView.getWidth() + (curFocusedView.getWidth() - preFocusedView.getWidth()) * percent;
            //焦点bitmap的下边距
            float bottom = currY + preFocusedView.getHeight() + (curFocusedView.getHeight() - preFocusedView.getHeight()) * percent;

            //更新焦点bitmap的位置
            curFocusedViewRectF = new RectF(currX - padding, currY - padding, right + padding, bottom + padding);

            postInvalidate();
        } else {
            preFocusedView = curFocusedView;
        }
//        curFocusedViewRectF.set();
//        Log.d("MyRecyclerView", "computeScroll");
    }

    @Override
    protected boolean awakenScrollBars() {
//        Log.d("MyRecyclerView", "awakenScrollBars");
        return super.awakenScrollBars();

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);


    }

    /**
     * 设置焦点bitmap
     *
     * @param drawableId
     */
    public void setFocusDrawableId(int drawableId) {

        InputStream inputStream = getContext().getResources().openRawResource(drawableId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        focusedBitmap = BitmapFactory.decodeStream(inputStream, null, options);
        focusedBitmapRect = new Rect(0, 0, focusedBitmap.getWidth(), focusedBitmap.getHeight());
    }

    public boolean isFocusedFront() {
        return isFocusedFront;
    }

    public void setFocusedFront(boolean focusedFront) {
        isFocusedFront = focusedFront;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    int startX = 0, startY = 0;

    long lastTime;

    @Override
    public void onItemFocusChanged(boolean isFocused, final View focusedView) {
        if (isFocused) {

            //isFocused可能为两次
            if (System.currentTimeMillis() - lastTime < 140) {
                return;
            }
            lastTime = System.currentTimeMillis();

            isRecyclerScrolling = false;
//            Log.d("MyRecyclerView", "focusChanged");
            preFocusedView = curFocusedView;
            curFocusedView = focusedView;

            if (preFocusedView != null) {
                startX = (int) preFocusedView.getX();
                startY = (int) preFocusedView.getY();
                getFocusRectF(focusedView);
//                matrix.mapRect(new RectF(startX, startY, endX, endY));
            } else {

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startX = (int) focusedView.getX();
                        startY = (int) focusedView.getY();
                        getFocusRectF(focusedView);
                    }
                }, 300);

            }


        }
    }

    private void getFocusRectF(View focusedView) {


        int endX = (int) focusedView.getX();
        int endY = (int) focusedView.getY();

        scroller.startScroll(startX, startY, endX - startX, endY - startY, 500);
        curFocusedViewRectF = new RectF(endX - padding, endY - padding, endX + focusedView.getWidth() + padding, endY + focusedView.getHeight() + padding);
        postInvalidate();
    }
}
