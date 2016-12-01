package com.mcx.hoveringlist.widgets;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * ScrollView
 * Created by machenxi on 2016/10/8.
 */
public class ScrollViewWithList extends ScrollView implements View.OnLayoutChangeListener {

    private float xDistance;
    private float yDistance;
    private float xLast;
    private float yLast;
    private boolean isInterceptTouchEvent = true;
    private OnScrollListener onScrollListener;
    private static final int ATTACH_DISTANCE = 20;
    private OnAttachStateChangedListener mAttachStateChangedListener;
    private ListView mScrollableView;
	/**
     * 有List的Container
     */
    private View mListContainer;

    public ScrollViewWithList(Context context) {
        this(context, null);
    }

    public ScrollViewWithList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollViewWithList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnLayoutChangeListener(this);
    }

    public void setAttachStateChangedListener(OnAttachStateChangedListener attachStateChangedListener) {
        mAttachStateChangedListener = attachStateChangedListener;
    }

    public void setListContainer(View listContainer) {
        mListContainer = listContainer;
    }

    public void setInterceptTouchEvent(boolean interceptTouchEvent) {
        isInterceptTouchEvent = interceptTouchEvent;
    }

    public boolean isInterceptTouchEvent() {
        return isInterceptTouchEvent;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScroll(t);
        }
        int containerTop = mListContainer.getTop();

        if (t >= containerTop - ATTACH_DISTANCE) {  //吸附（若要做吸附效果，在回滚的时候需要首先scrollTo到可吸附的距离之上）
            scrollTo(0, containerTop);
            setInterceptTouchEvent(false);
            if (mAttachStateChangedListener != null) {
                mAttachStateChangedListener.onAttachStateChanged(true);
            }
        } else {
            setInterceptTouchEvent(true);
            if (mAttachStateChangedListener != null) {
                mAttachStateChangedListener.onAttachStateChanged(false);
            }
        }
    }

    public void backToTop() {
        scrollTo(0, mListContainer.getTop() - ATTACH_DISTANCE - 1);
        smoothScrollTo(0, 0);
        if (mScrollableView != null) {
            stopFling(mScrollableView);
            mScrollableView.setSelection(0);
            if (mAttachStateChangedListener != null) {
                mAttachStateChangedListener.onAttachStateChanged(false);
            }
            setInterceptTouchEvent(true);
        }
    }

    public void stopFling(View view) {
        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void setScrollableView(final ListView scrollableView) {
        mScrollableView = scrollableView;
        if (scrollableView != null) {
            scrollableView.setOnScrollListener(new ListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {}

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (isInterceptTouchEvent && view.getSelectedItemPosition() > 0) {
                        scrollableView.setSelection(0);
                    }
                }
            });
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (!isInterceptTouchEvent ) {
            int height = getChildAt(0).getMeasuredHeight();
            scrollTo(0, height);
        }
    }

    public interface OnScrollListener {
        void onScroll(int scrollY);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isInterceptTouchEvent) {
                    float xCur = ev.getX();
                    float yCur = ev.getY();
                    xDistance += Math.abs(xCur - xLast);
                    yDistance += Math.abs(yCur - yLast);
                    xLast = xCur;
                    yLast = yCur;
                } else {
                    return false;
                }
                break;
        }
        if (xDistance > yDistance) {
            return false;
        }
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            if (isInterceptTouchEvent) {
                return super.onTouchEvent(ev);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public interface OnAttachStateChangedListener {
        void onAttachStateChanged(boolean attached);
    }
}

