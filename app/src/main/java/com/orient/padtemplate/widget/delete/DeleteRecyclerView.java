package com.orient.padtemplate.widget.delete;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.orient.padtemplate.base.recyclerview.DeleteRecyclerAdapter;


/**
 * 侧滑删除的RecyclerView
 *
 * @Auther wangjie on 2017/12/2.
 */

public class DeleteRecyclerView extends RecyclerView {

    // 滑动四种状态 关闭、正在关闭、正在打开、打开
    private int status = CLOSE;
    public static final int CLOSE = 1;
    public static final int CLOSING = 2;
    public static final int OPENING = 3;
    public static final int OPEN = 4;
    // 滑动速度的临界值
    public static final int VELOCITY = 100;
    // 默认的滑动时间
    public static final int DEFAULT_TIME = 200;

    // 上下文
    private Context mContext;
    // 速度的监听类
    private VelocityTracker mVelocityTracker;
    // 滑动的处理
    private Scroller mScroller;
    private int mPosition;
    // 删除的View
    private View mDeleteView;
    // 得到的子View
    private View mItemView;
    // 删除图片的高度
    private int mMaxLength;
    //是否是水平滑动
    private boolean isHorMoving;
    //是否是垂直滑动
    private boolean isVerMoving;
    //是否开始滑动
    private boolean isStartScroll;
    // 上次的坐标点
    private int mLastX;
    private int mLastY;

    // 监听器事件
    private OnItemClickListener listener;

    public DeleteRecyclerView(Context context) {
        this(context, null);
    }

    public DeleteRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeleteRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;
        mScroller = new Scroller(mContext, new LinearInterpolator());
        // 构建滑动速度检测的类
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(e);
        //获取当前坐标
        // x,y其实是距离当前View的左边和上边的距离
        int x = (int) e.getX();
        int y = (int) e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //删除图片还没打开的状态
                if (status == CLOSE) {
                    //寻找对应坐标点下的V
                    View view = findChildViewUnder(x, y);
                    if (view == null) {
                        return false;
                    }
                    //通过baseviewholder获取对应的子View，详情可以看代码
                    DeleteRecyclerAdapter.DeleteViewHolder viewHolder = (DeleteRecyclerAdapter.DeleteViewHolder) getChildViewHolder(view);

                    mItemView = viewHolder.itemLayout;
                    mPosition = viewHolder.getAdapterPosition();
                    mDeleteView = viewHolder.itemDelete;
                    mMaxLength = mDeleteView.getWidth();

                    mDeleteView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //返回原点
                            mItemView.scrollTo(0, 0);
                            status = CLOSE;
                            if (listener != null)
                                listener.onDeleteClick(mPosition);
                        }
                    });
                    //当删除图片已经完全显示的时候
                } else if (status == OPEN) {
                    //从当前view的偏移点mItemView.getScrollX()，位移-mMaxLength长度单位
                    // 时间DEFAULT_TIMEms，向左移动为正数
                    mScroller.startScroll(mItemView.getScrollX(), 0, -mMaxLength, 0, DEFAULT_TIME);
                    //刷新下一帧动画
                    invalidate();
                    status = CLOSE;
                    return false;
                } else {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //获取上次的落点与当前的坐标之间的差值
                int dx = mLastX - x;
                int dy = mLastY - y;

                int scrollX = mItemView.getScrollX();
                //水平滑动距离大于垂直距离
                if (Math.abs(dx) > Math.abs(dy)) {
                    isHorMoving = true;
                    //向左滑动，直至显示删除图片，向左滑动的最大距离不超过删除图片的宽度
                    if (scrollX + dx >= mMaxLength) {
                        mItemView.scrollTo(mMaxLength, 0);
                        return true;
                        //向右滑动，直至删除图片不显示，向右滑动的最大距离不超过初始位置
                    } else if (scrollX + dx <= 0) {
                        mItemView.scrollTo(0, 0);
                        return true;
                    }

                    //如果在图片还未完全显示的状态下，那么手指滑动多少，图片就移动多少
                    mItemView.scrollBy(dx, 0);
                    //mItemView.scrollTo(dx+scrollX,0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isHorMoving && !isVerMoving && listener != null) {
                    listener.onItemClick(mItemView, mPosition);
                }
                isHorMoving = false;

                mVelocityTracker.computeCurrentVelocity(1000);//计算手指滑动的速度
                float xVelocity = mVelocityTracker.getXVelocity();//水平方向速度（向左为负）
                float yVelocity = mVelocityTracker.getYVelocity();//垂直方向速度

                int upScrollX = mItemView.getScrollX();
                int deltaX = 0;

                //向右滑动速度为正数
                //滑动速度快的状态下抬起手指，计算所需偏移量
                if (Math.abs(xVelocity) > Math.abs(yVelocity) && Math.abs(xVelocity) >= VELOCITY) {
                    //向右隐藏
                    if (xVelocity >= VELOCITY) {
                        deltaX = -upScrollX;
                        status = CLOSING;
                    } else if (xVelocity <= -VELOCITY) {
                        deltaX = mMaxLength - upScrollX;
                        status = OPENING;
                    }
                    //滑动速度慢的状态下抬起手指，如果滑动距离大于1/2的图片宽度，计算偏移量
                    //不够的话恢复原点
                } else {
                    if (upScrollX >= mMaxLength / 2) {
                        deltaX = mMaxLength - upScrollX;
                        status = OPENING;
                    } else {
                        deltaX = -upScrollX;
                        status = CLOSING;
                    }
                }

                mScroller.startScroll(upScrollX, 0, deltaX, 0, DEFAULT_TIME);
                isStartScroll = true;
                invalidate();
                mVelocityTracker.clear();
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.onTouchEvent(e);
    }

    @Override
    public void computeScroll() {
        // 滚动是否完成
        // true标识还没有完成
        if (mScroller.computeScrollOffset()) {
            mItemView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else if (isStartScroll) {
            isStartScroll = false;
            if (status == CLOSING)
                status = CLOSE;
            if (status == OPENING)
                status = OPEN;

        }
        super.computeScroll();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        isVerMoving = state == SCROLL_STATE_DRAGGING;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
