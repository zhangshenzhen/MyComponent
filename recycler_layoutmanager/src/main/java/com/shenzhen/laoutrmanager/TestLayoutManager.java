package com.shenzhen.laoutrmanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class TestLayoutManager extends RecyclerView.LayoutManager {
    private int totalHeight = 0;
    private int verticalScrollOffset = 0;

    /* 第一步： 实现抽象方法
     */
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /* 第二步：重写onLayoutChildren
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //super.onLayoutChildren(recycler, state);
        if (getItemCount() <= 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }
        // 跳过preLayout，preLayout主要用于支持动画
        if (state.isPreLayout()) {
            return;
        }
        //在布局之前，经所有的子View先detach 掉，放入到scrap 缓存中
        detachAndScrapAttachedViews(recycler);
        //定义数值方向的偏移量
        int offsetY = 0;
        //遍历子View
        for (int i = 0; i < getItemCount(); i++) {
            //这里就是从缓存中取出来的
            View view = recycler.getViewForPosition(i);
            //将View添加到recycleView 中
            addView(view);  //系统api
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);
            //最后将View 布局
            int left = (getWidth() - width) / 2;
            int right = getWidth() - left;
            int top = offsetY;
            int bottom = offsetY + height;

            //定位布局
            layoutDecorated(view, left, top, right, bottom);
            //将竖直方向偏移量增大height
            offsetY += height;

            totalHeight += height;

        }

    }

    @Override
    public boolean canScrollVertically() {
        //return super.canScrollVertically();
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //实际要滑动的距离
        int travel = dy;

        //如果滑动到最顶部
        if (verticalScrollOffset + dy < 0) {
            travel = -verticalScrollOffset;
        } else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {//如果滑动到最底部
            travel = totalHeight - getVerticalSpace() - verticalScrollOffset;
        }

        //将竖直方向的偏移量+travel
        verticalScrollOffset += travel;

        // 平移容器内的item
        offsetChildrenVertical(-travel);
        return travel;
    }

    /*
     * 获取RecyclerView在垂直方向上的可用空间，即去除了padding后的高度
     */

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }
}
