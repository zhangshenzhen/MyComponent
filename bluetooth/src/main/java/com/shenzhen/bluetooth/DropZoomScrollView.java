package com.shenzhen.bluetooth;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * 下拉放大头部图片 适应方式 直接包裹整个布即可
 */
public class DropZoomScrollView extends ScrollView implements View.OnTouchListener {
    //今日工作总结：1,梳理属于自定义填充布局架构，和app头像裁剪功能
    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;

    private View dropZoomView;
    private int dropZoomViewWidth;
    private int dropZoomViewHeight;

    public DropZoomScrollView(Context context) {
        super(context);
    }

    public DropZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DropZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        if (getChildAt(0) != null) {
            ViewGroup vg = (ViewGroup) getChildAt(0);
            if (vg.getChildAt(0) != null) {
                dropZoomView = vg.getChildAt(0);
                setOnTouchListener(this);

            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (dropZoomViewWidth <= 0 || dropZoomViewHeight <= 0) {
            dropZoomViewWidth = dropZoomView.getMeasuredWidth();
            dropZoomViewHeight = dropZoomView.getMeasuredHeight();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                //手指离开后恢复图片
                mScaling = false;
                replyImage();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScaling) {
                    if (getScrollY() == 0) {
                        mFirstPosition = event.getY();// 滚动到顶部时记录位置，否则正常返回
                    } else {
                        break;
                    }
                }
                int distance = (int) ((event.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数
                if (distance < 0) { // 当前位置比记录位置要小，正常返回
                    break;
                }

                // 处理放大
                mScaling = true;
                setZoom(1 + distance);
                return true; // 返回true表示已经完成触摸事件，不再处理
        }
        return false;
    }

    private float mReplyRate = 0.5f;//回调系数，越大，回调越慢

    // 回弹动画 (使用了属性动画)
    public void replyImage() {
        float distance = dropZoomView.getMeasuredWidth() - dropZoomViewWidth;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(distance, 0f).setDuration((long) (distance * (mReplyRate / 2)));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setZoom((Float) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();

    }

    //缩放
    public void setZoom(float zoom) {

        if (dropZoomViewWidth <= 0 || dropZoomViewWidth <= 0) {
            return;
        }
        ViewGroup.LayoutParams lp = dropZoomView.getLayoutParams();
        lp.width = (int) (dropZoomViewWidth + zoom);
        lp.height = (int) (dropZoomViewHeight * ((dropZoomViewWidth + zoom) / dropZoomViewWidth));
        ((MarginLayoutParams) lp).setMargins(-(lp.width - dropZoomViewWidth) / 2, 0, -(lp.width - dropZoomViewWidth) / 2, 0);
        dropZoomView.setLayoutParams(lp);
    }
}

