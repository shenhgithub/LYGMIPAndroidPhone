package com.artifex.mupdf;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Scroller;

public class ReaderView extends AdapterView<Adapter> implements GestureDetector.OnGestureListener,
    ScaleGestureDetector.OnScaleGestureListener, Runnable
{
    /** 
    * 
    */
    private static final int           MOVING_DIAGONALLY = 0;
    
    /**
     * 左侧移动
     */
    private static final int           MOVING_LEFT       = 1;
    
    /**
     * 向右移动
     */
    private static final int           MOVING_RIGHT      = 2;
    
    /**
     * 向上移动
     */
    private static final int           MOVING_UP         = 3;
    
    /**
     * 向下移动
     */
    private static final int           MOVING_DOWN       = 4;
    
    /**
     * 滑动的偏移距离
     */
    private static final int           FLING_MARGIN      = 100;
    
    /** 
    * 
    */
    private static final int           GAP               = 20;
    
    /**
     * 滑动速度
     */
    @SuppressWarnings("unused")
    private static final int           SCROLL_SPEED      = 2;
    
    /**
     * 最小缩放
     */
    private static final float         MIN_SCALE         = 1.0f;
    
    /**
     * 最大放大
     */
    private static final float         MAX_SCALE         = 5.0f;
    
    /**
     * 适配器变量
     */
    private Adapter                    mAdapter;
    
    /**
     * 当前的页数
     */
    private int                        mCurrent;                                    // Adapter's index for the current
                                                                                     // view
                                                                                     
    /**
     * 上一页页数
     */
    private int                        mPreCurrent       = 0;                       // Adapter's index for the current
                                                                                     // view
                                                                                     
    /** 
    * 
    */
    private boolean                    mResetLayout;
    
    /**
     * 记录三个pdf view
     */
    private final SparseArray<View>    mChildViews       = new SparseArray<View>(3);
    
    // Shadows the children of the adapter view
    // but with more sensible indexing
    /**
     * 缓存pdfVIew
     */
    private final LinkedList<View>     mViewCache        = new LinkedList<View>();
    
    /**
     * 正在使用
     */
    private boolean                    mUserInteracting;                            // Whether the user is interacting
                                                                                     
    /**
     * 缩放中
     */
    private boolean                    mScaling;                                    // Whether the user is currently
                                                                                     // pinch zooming
                                                                                     
    /**
     * 初始化缩放值
     */
    private float                      mScale            = 1.0f;
    
    /**
     * X轴滑动距离
     */
    private int                        mXScroll;                                    // Scroll amounts recorded from
                                                                                     // events.
                                                                                     
    /**
     * Y轴滑动距离
     */
    private int                        mYScroll;                                    // and then accounted for in
                                                                                     // onLayout
                                                                                     
    /**
     * 姿势变量
     */
    private final GestureDetector      mGestureDetector;
    
    /**
     * 缩放变量
     */
    private final ScaleGestureDetector mScaleGestureDetector;
    
    /**
     * 滚动器
     */
    private final Scroller             mScroller;
    
    /**
     * 滚动X距离
     */
    private int                        mScrollerLastX;
    
    /**
     * 滚动Y距离
     */
    private int                        mScrollerLastY;
    
    /**
     * 是否可以滚动
     */
    private boolean                    mScrollDisabled;
    
    /**
     * Handler变量，传递数据
     */
    private Handler                    mHandler;
    
    /**
     * 是否正在签批
     */
    public boolean                     isSign            = false;
    
    /**
     * 如果pdf处于签批状态，则pdf页面不可以移动，屏蔽onTouchEvent()中的处理
     */
    private boolean                    isHandSign        = false;
    
    /**
     * 设置正在签批
     * 
     * @Description<功能详细描述>
     * 
     * @param flag 标识
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setHandWriteEditorSign(boolean flag)
    {
        isHandSign = flag;
    }
    
    /**
     * <默认构造函数>
     */
    @SuppressWarnings("deprecation")
    public ReaderView(Context context, Handler handler)
    {
        super(context);
        
        mHandler = handler;
        
        mGestureDetector = new GestureDetector(this);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mScroller = new Scroller(context);
    }
    
    /**
     * <默认构造函数>
     */
    @SuppressWarnings("deprecation")
    public ReaderView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mGestureDetector = new GestureDetector(this);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mScroller = new Scroller(context);
    }
    
    /**
     * <默认构造函数>
     */
    @SuppressWarnings("deprecation")
    public ReaderView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mGestureDetector = new GestureDetector(this);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mScroller = new Scroller(context);
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return 当前页
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public int getDisplayedViewIndex()
    {
        return mCurrent;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param i 那一页
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setDisplayedViewIndex(int i)
    {
        if (0 <= i && i < mAdapter.getCount())
        {
            mCurrent = i;
            onMoveToChild(i);
            mResetLayout = true;
            requestLayout();
        }
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void moveToNext()
    {
        View v = mChildViews.get(mCurrent + 1);
        if (v != null)
            slideViewOntoScreen(v);
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void moveToPrevious()
    {
        View v = mChildViews.get(mCurrent - 1);
        if (v != null)
            slideViewOntoScreen(v);
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void resetupChildren()
    {
        for (int i = 0; i < mChildViews.size(); i++)
        {
            onChildSetup(mChildViews.keyAt(i), mChildViews.valueAt(i));
        }
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param i
     * @param v
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected void onChildSetup(int i, View v)
    {
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param i
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected void onMoveToChild(int i)
    {
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param v
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected void onSettle(View v)
    {
    };
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param v
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected void onUnsettle(View v)
    {
    };
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return 返回pdf view
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public View getDisplayedView()
    {
        return mChildViews.get(mCurrent);
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    public void run()
    {
        if (!mScroller.isFinished())
        {
            mScroller.computeScrollOffset();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            mXScroll += x - mScrollerLastX;
            mYScroll += y - mScrollerLastY;
            mScrollerLastX = x;
            mScrollerLastY = y;
            requestLayout();
            post(this);
        }
        else if (!mUserInteracting)
        {
            // End of an inertial scroll and the user is not interacting.
            // The layout is stable
            View v = mChildViews.get(mCurrent);
            postSettle(v);
            
            if (mPreCurrent != mCurrent)
            {
                MuPDFAttr.setS_Current(mCurrent + 1);
                mHandler.sendEmptyMessage(MuPDFAttr.STATE_PAGE_CHANGE);
                mPreCurrent = mCurrent;
            }
        }
    }
    
    /**
     * 重载方法
     * 
     * @param arg0 arg0
     * @return
     * @author ren_qiujing
     */
    public boolean onDown(MotionEvent arg0)
    {
        if (isHandSign)
        {
            return true;
        }
        mScroller.forceFinished(true);
        return true;
    }
    
    /**
     * 重载方法
     * 
     * @param e1 e1
     * @param e2 e2
     * @param velocityX velocityX
     * @param velocityY velocityY
     * @return 是否滑动
     * @author ren_qiujing
     */
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        
        if (isHandSign)
        {
            return true;
        }
        
        if (mScrollDisabled)
            return true;
        
        View v = mChildViews.get(mCurrent);
        if (v != null)
        {
            Rect bounds = getScrollBounds(v);
            switch (directionOfTravel(velocityX, velocityY))
            {
                case MOVING_LEFT:
                    // if (bounds.left >= 0)
                    // {
                    // // Fling off to the left bring next view onto screen
                    // // View vl = mChildViews.get(mCurrent+1);
                    // //
                    // // if (vl != null) {
                    // // slideViewOntoScreen(vl);
                    // // return true;
                    // // }
                    // }
                    break;
                case MOVING_RIGHT:
                    // if (bounds.right <= 0)
                    // {
                    // // Fling off to the right bring previous view onto screen
                    // // View vr = mChildViews.get(mCurrent-1);
                    // //
                    // // if (vr != null) {
                    // // slideViewOntoScreen(vr);
                    // // return true;
                    // // }
                    // }
                    break;
            }
            
            mScrollerLastX = mScrollerLastY = 0;
            // If the page has been dragged out of bounds then we want to spring back
            // nicely. fling jumps back into bounds instantly, so we don't want to use
            // fling in that case. On the other hand, we don't want to forgo a fling
            // just because of a slightly off-angle drag taking us out of bounds other
            // than in the direction of the drag, so we test for out of bounds only
            // in the direction of travel.
            //
            // Also don't fling if out of bounds in any direction by more than fling
            // margin
            Rect expandedBounds = new Rect(bounds);
            expandedBounds.inset(-FLING_MARGIN, -FLING_MARGIN);
            
            if (withinBoundsInDirectionOfTravel(bounds, velocityX, velocityY) && expandedBounds.contains(0, 0))
            {
                mScroller.fling(0,
                    0,
                    (int)velocityX,
                    (int)velocityY,
                    bounds.left,
                    bounds.right,
                    bounds.top,
                    bounds.bottom);
                post(this);
            }
        }
        
        return true;
    }
    
    /**
     * 重载方法
     * 
     * @param e e
     * @author ren_qiujing
     */
    public void onLongPress(MotionEvent e)
    {
    }
    
    /**
     * 重载方法
     * 
     * @param e1 e1
     * @param e2 e2
     * @param distanceX distanceX
     * @param distanceY distanceY
     * @return 是否滚动
     * @author ren_qiujing
     */
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        
        if (isHandSign)
        {
            return true;
        }
        
        if (!mScrollDisabled)
        {
            mXScroll -= distanceX;
            mYScroll -= distanceY;
            requestLayout();
        }
        return true;
    }
    
    /**
     * 重载方法
     * 
     * @param e e
     * @author ren_qiujing
     */
    public void onShowPress(MotionEvent e)
    {
    }
    
    /**
     * 重载方法
     * 
     * @param e e
     * @return 是否是单击
     * @author ren_qiujing
     */
    public boolean onSingleTapUp(MotionEvent e)
    {
        return false;
    }
    
    /**
     * 重载方法
     * 
     * @param detector detector
     * @return 是否缩放
     * @author ren_qiujing
     */
    public boolean onScale(ScaleGestureDetector detector)
    {
        
        if (isHandSign)
        {
            return true;
        }
        
        float previousScale = mScale;
        mScale = Math.min(Math.max(mScale * detector.getScaleFactor(), MIN_SCALE), MAX_SCALE);
        float factor = mScale / previousScale;
        
        View v = mChildViews.get(mCurrent);
        if (v != null)
        {
            // Work out the focus point relative to the view top left
            int viewFocusX = (int)detector.getFocusX() - (v.getLeft() + mXScroll);
            int viewFocusY = (int)detector.getFocusY() - (v.getTop() + mYScroll);
            // Scroll to maintain the focus point
            mXScroll += viewFocusX - viewFocusX * factor;
            mYScroll += viewFocusY - viewFocusY * factor;
            requestLayout();
        }
        
        return true;
    }
    
    /**
     * 重载方法
     * 
     * @param detector detector
     * @return 是否正在缩放
     * @author ren_qiujing
     */
    public boolean onScaleBegin(ScaleGestureDetector detector)
    {
        if (isHandSign)
        {
            return true;
        }
        mScaling = true;
        // Ignore any scroll amounts yet to be accounted for: the
        // screen is not showing the effect of them, so they can
        // only confuse the user
        mXScroll = mYScroll = 0;
        // Avoid jump at end of scaling by disabling scrolling
        // until the next start of gesture
        mScrollDisabled = true;
        return true;
    }
    
    /**
     * 重载方法
     * 
     * @param detector detector
     * @author ren_qiujing
     */
    public void onScaleEnd(ScaleGestureDetector detector)
    {
        if (isHandSign)
        {
            return;
        }
        mScaling = false;
    }
    
    /**
     * 重载方法
     * 
     * @param event event
     * @return 标识
     * @author ren_qiujing
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        
        if (isHandSign)
        {
            return super.onTouchEvent(event);
        }
        
        mScaleGestureDetector.onTouchEvent(event);
        
        if (!mScaling)
            mGestureDetector.onTouchEvent(event);
        
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            mUserInteracting = true;
        }
        if (event.getActionMasked() == MotionEvent.ACTION_UP)
        {
            mScrollDisabled = false;
            mUserInteracting = false;
            
            View v = mChildViews.get(mCurrent);
            if (v != null)
            {
                if (mScroller.isFinished())
                {
                    // If, at the end of user interaction, there is no
                    // current inertial scroll in operation then animate
                    // the view onto screen if necessary
                    slideViewOntoScreen(v);
                }
                
                if (mScroller.isFinished())
                {
                    // If still there is no inertial scroll in operation
                    // then the layout is stable
                    postSettle(v);
                }
            }
        }
        requestLayout();
        return true;
    }
    
    /**
     * 重载方法
     * 
     * @param widthMeasureSpec 宽
     * @param heightMeasureSpec 高
     * @author ren_qiujing
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        int n = getChildCount();
        for (int i = 0; i < n; i++)
        {
            measureView(getChildAt(i));
        }
    }
    
    /**
     * 重载方法
     * 
     * @param changed 是否变化
     * @param left 左
     * @param top 上
     * @param right 右
     * @param bottom 底
     * @author ren_qiujing
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        View cv = mChildViews.get(mCurrent);
        Point cvOffset;
        
        if (!mResetLayout)
        {
            // Move to next or previous if current is sufficiently off center
            if (cv != null && !isSign)
            {
                cvOffset = subScreenSizeOffset(cv);
                // cv.getRight() may be out of date with the current scale
                // so add left to the measured width for the correct position
                if (cv.getLeft() + cv.getMeasuredWidth() + cvOffset.x + GAP / 2 + mXScroll < getWidth() / 2
                    && mCurrent + 1 < mAdapter.getCount())
                {
                    postUnsettle(cv);
                    // post to invoke test for end of animation
                    // where we must set hq area for the new current view
                    post(this);
                    
                    mCurrent++;
                    onMoveToChild(mCurrent);
                }
                
                if (cv.getLeft() - cvOffset.x - GAP / 2 + mXScroll >= getWidth() / 2 && mCurrent > 0)
                {
                    postUnsettle(cv);
                    // post to invoke test for end of animation
                    // where we must set hq area for the new current view
                    post(this);
                    
                    mCurrent--;
                    onMoveToChild(mCurrent);
                }
            }
            
            // Remove not needed children and hold them for reuse
            int numChildren = mChildViews.size();
            int childIndices[] = new int[numChildren];
            for (int i = 0; i < numChildren; i++)
                childIndices[i] = mChildViews.keyAt(i);
            
            for (int i = 0; i < numChildren; i++)
            {
                int ai = childIndices[i];
                if (ai < mCurrent - 1 || ai > mCurrent + 1)
                {
                    View v = mChildViews.get(ai);
                    mViewCache.add(v);
                    removeViewInLayout(v);
                    mChildViews.remove(ai);
                }
            }
        }
        else
        {
            mResetLayout = false;
            mXScroll = mYScroll = 0;
            
            // Remove all children and hold them for reuse
            int numChildren = mChildViews.size();
            for (int i = 0; i < numChildren; i++)
            {
                View v = mChildViews.valueAt(i);
                postUnsettle(v);
                mViewCache.add(v);
                removeViewInLayout(v);
            }
            mChildViews.clear();
            // post to ensure generation of hq area
            post(this); // call run
        }
        
        // Ensure current view is present
        int cvLeft, cvRight, cvTop, cvBottom;
        boolean notPresent = (mChildViews.get(mCurrent) == null);
        cv = getOrCreateChild(mCurrent);
        // When the view is sub-screen-size in either dimension we
        // offset it to center within the screen area, and to keep
        // the views spaced out
        cvOffset = subScreenSizeOffset(cv);
        if (notPresent)
        {
            // Main item not already present. Just place it top left
            cvLeft = cvOffset.x;
            cvTop = cvOffset.y;
        }
        else
        {
            // Main item already present. Adjust by scroll offsets
            cvLeft = cv.getLeft() + mXScroll;
            cvTop = cv.getTop() + mYScroll;
        }
        // Scroll values have been accounted for
        mXScroll = mYScroll = 0;
        cvRight = cvLeft + cv.getMeasuredWidth();
        cvBottom = cvTop + cv.getMeasuredHeight();
        
        if (!mUserInteracting && mScroller.isFinished())
        {
            Point corr = getCorrection(getScrollBounds(cvLeft, cvTop, cvRight, cvBottom));
            cvRight += corr.x;
            cvLeft += corr.x;
            cvTop += corr.y;
            cvBottom += corr.y;
        }
        else if (cv.getMeasuredHeight() <= getHeight())
        {
            // When the current view is as small as the screen in height, clamp
            // it vertically
            Point corr = getCorrection(getScrollBounds(cvLeft, cvTop, cvRight, cvBottom));
            cvTop += corr.y;
            cvBottom += corr.y;
        }
        
        cv.layout(cvLeft, cvTop, cvRight, cvBottom);
        
        if (mCurrent > 0)
        {
            View lv = getOrCreateChild(mCurrent - 1);
            Point leftOffset = subScreenSizeOffset(lv);
            int gap = leftOffset.x + GAP + cvOffset.x;
            lv.layout(cvLeft - lv.getMeasuredWidth() - gap, (cvBottom + cvTop - lv.getMeasuredHeight()) / 2, cvLeft
                - gap, (cvBottom + cvTop + lv.getMeasuredHeight()) / 2);
        }
        
        if (mCurrent + 1 < mAdapter.getCount())
        {
            View rv = getOrCreateChild(mCurrent + 1);
            Point rightOffset = subScreenSizeOffset(rv);
            int gap = cvOffset.x + GAP + rightOffset.x;
            rv.layout(cvRight + gap, (cvBottom + cvTop - rv.getMeasuredHeight()) / 2, cvRight + rv.getMeasuredWidth()
                + gap, (cvBottom + cvTop + rv.getMeasuredHeight()) / 2);
        }
        
        invalidate();
    }
    
    /**
     * 重载方法
     * 
     * @return Adapter
     * @author ren_qiujing
     */
    @Override
    public Adapter getAdapter()
    {
        return mAdapter;
    }
    
    /**
     * 重载方法
     * 
     * @return View
     * @author ren_qiujing
     */
    @Override
    public View getSelectedView()
    {
        throw new UnsupportedOperationException("Not supported");
    }
    
    /**
     * 重载方法
     * 
     * @param adapter adapter
     * @author ren_qiujing
     */
    @Override
    public void setAdapter(Adapter adapter)
    {
        mAdapter = adapter;
        mChildViews.clear();
        removeAllViewsInLayout();
        requestLayout();
    }
    
    /**
     * 重载方法
     * 
     * @param arg0 arg0
     * @author ren_qiujing
     */
    @Override
    public void setSelection(int arg0)
    {
        throw new UnsupportedOperationException("Not supported");
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return View
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private View getCached()
    {
        if (mViewCache.size() == 0)
            return null;
        else
            return mViewCache.removeFirst();
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param i i
     * @return View
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private View getOrCreateChild(int i)
    {
        View v = mChildViews.get(i);
        if (v == null)
        {
            v = mAdapter.getView(i, getCached(), this);
            addAndMeasureChild(i, v);
        }
        onChildSetup(i, v);
        
        return v;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param i i
     * @param v v
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void addAndMeasureChild(int i, View v)
    {
        LayoutParams params = v.getLayoutParams();
        if (params == null)
        {
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        addViewInLayout(v, 0, params, true);
        mChildViews.append(i, v); // Record the view against it's adapter index
        measureView(v);
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param v v
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void measureView(View v)
    {
        // See what size the view wants to be
        v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // Work out a scale that will fit it to this view
        float scale =
            Math.min((float)getWidth() / (float)v.getMeasuredWidth(), (float)getHeight() / (float)v.getMeasuredHeight());
        // Use the fitting values scaled by our current scale factor
        v.measure(View.MeasureSpec.EXACTLY | (int)(v.getMeasuredWidth() * scale * mScale), View.MeasureSpec.EXACTLY
            | (int)(v.getMeasuredHeight() * scale * mScale));
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param left left
     * @param top top
     * @param right right
     * @param bottom bottom
     * @return Rect
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private Rect getScrollBounds(int left, int top, int right, int bottom)
    {
        int xmin = getWidth() - right;
        int xmax = -left;
        int ymin = getHeight() - bottom;
        int ymax = -top;
        
        // In either dimension, if view smaller than screen then
        // constrain it to be central
        if (xmin > xmax)
            xmin = xmax = (xmin + xmax) / 2;
        if (ymin > ymax)
            ymin = ymax = (ymin + ymax) / 2;
        
        return new Rect(xmin, ymin, xmax, ymax);
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param v v
     * @return Rect
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private Rect getScrollBounds(View v)
    {
        // There jcan be scroll amounts not yet accounted for in
        // onLayout, so add mXScroll and mYScroll to the current
        // positions when calculating the bounds.
        return getScrollBounds(v.getLeft() + mXScroll, v.getTop() + mYScroll, v.getLeft() + v.getMeasuredWidth()
            + mXScroll, v.getTop() + v.getMeasuredHeight() + mYScroll);
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param bounds bounds
     * @return Point
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private Point getCorrection(Rect bounds)
    {
        return new Point(Math.min(Math.max(0, bounds.left), bounds.right), Math.min(Math.max(0, bounds.top),
            bounds.bottom));
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param v v
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void postSettle(final View v)
    {
        // onSetjtle and onUnsettle are posted so that the calls
        // wont be executed until after the system has performed
        // layout.
        post(new Runnable()
        {
            public void run()
            {
                onSettle(v);
            }
        });
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param v v
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void postUnsettle(final View v)
    {
        post(new Runnable()
        {
            public void run()
            {
                onUnsettle(v);
            }
        });
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param v v
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void slideViewOntoScreen(View v)
    {
        Point corr = getCorrection(getScrollBounds(v));
        if (corr.x != 0 || corr.y != 0)
        {
            mScrollerLastX = mScrollerLastY = 0;
            mScroller.startScroll(0, 0, corr.x, corr.y, 400);
            post(this);
        }
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param v v
     * @return Point
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private Point subScreenSizeOffset(View v)
    {
        return new Point(Math.max((getWidth() - v.getMeasuredWidth()) / 2, 0),
            Math.max((getHeight() - v.getMeasuredHeight()) / 2, 0));
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param vx vx
     * @param vy vy
     * @return int
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private static int directionOfTravel(float vx, float vy)
    {
        if (Math.abs(vx) > 2 * Math.abs(vy))
            return (vx > 0) ? MOVING_RIGHT : MOVING_LEFT;
        else if (Math.abs(vy) > 2 * Math.abs(vx))
            return (vy > 0) ? MOVING_DOWN : MOVING_UP;
        else
            return MOVING_DIAGONALLY;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param bounds bounds
     * @param vx vx
     * @param vy vy
     * @return boolean
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private static boolean withinBoundsInDirectionOfTravel(Rect bounds, float vx, float vy)
    {
        switch (directionOfTravel(vx, vy))
        {
            case MOVING_DIAGONALLY:
                return bounds.contains(0, 0);
            case MOVING_LEFT:
                return bounds.left <= 0;
            case MOVING_RIGHT:
                return bounds.right >= 0;
            case MOVING_UP:
                return bounds.top <= 0;
            case MOVING_DOWN:
                return bounds.bottom >= 0;
            default:
                throw new NoSuchElementException();
        }
    }
    
    /**
     * 取消所有签批的view
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-11
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void clearAdaptePageView()
    {
        if (mChildViews != null)
        {
            int numChildren = mChildViews.size();
            int childIndices[] = new int[numChildren];
            for (int i = 0; i < numChildren; i++)
                childIndices[i] = mChildViews.keyAt(i);
            
            for (int i = 0; i < numChildren; i++)
            {
                int ai = childIndices[i];
                if (ai >= mCurrent - 1 && ai <= mCurrent + 1)
                {
                    if (mChildViews.get(ai) instanceof ViewGroup)
                    {
                        ViewGroup v = (ViewGroup)mChildViews.get(ai);
                        v.removeAllViews();
                        v = null;
                    }
                }
            }
        }
    }
}
