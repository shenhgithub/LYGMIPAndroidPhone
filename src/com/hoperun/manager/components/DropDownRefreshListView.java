/**
 * Author : hoperun
 * Copyright (c) 2011 hoperun.
 * All rights reserved
 */
package com.hoperun.manager.components;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hoperun.mip.utils.LogUtil;
import com.hoperun.miplygphone.R;

/**
 * 
 * listView 增加上拉下拉刷新功能
 * 
 * @author shen_feng
 * @Version [版本号, 2013-9-22]
 */
public class DropDownRefreshListView extends ListView implements OnScrollListener
{
    /** 标记 **/
    private static final String TAG                = "listview";
    
    /****/
    private final static int    RELEASE_To_REFRESH = 0;
    
    /****/
    private final static int    PULL_To_REFRESH    = 1;
    
    /** 正在刷新 **/
    private final static int    REFRESHING         = 2;
    
    /** 什么都不做 **/
    private final static int    DONE               = 3;
    
    /** 正在下载 **/
    private final static int    LOADING            = 4;
    
    /****/
    private final static int    RATIO              = 3;
    
    /** 布局扩张器 **/
    private LayoutInflater      inflater;
    
    /** 顶部的view **/
    private View                headView;
    
    /** 提示tv **/
    private TextView            tipsTextview;
    
    /** 最后更新时间tv **/
    private TextView            lastUpdatedTextView;
    
    /** 箭头 **/
    private ImageView           arrowImageView;
    
    /** 进度条 **/
    private ProgressBar         progressBar;
    
    /** 动画 **/
    private RotateAnimation     animation;
    
    /****/
    private RotateAnimation     reverseAnimation;
    
    /****/
    private boolean             isRecored;
    
    /** 宽度 **/
    private int                 headContentWidth;
    
    /** 高度 **/
    private int                 headContentHeight;
    
    /** 起始点y坐标 **/
    private int                 startY;
    
    /****/
    private int                 firstItemIndex;
    
    /****/
    private int                 state;
    
    /****/
    private boolean             isBack;
    
    /** 刷新监听器 **/
    private OnRefreshListener   refreshListener;
    
    /** 是否刷新 ture：刷新 **/
    private boolean             isRefreshable;
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 应用上下文
     */
    public DropDownRefreshListView(Context context)
    {
        super(context);
        init(context);
    }
    
    /**
     * @param context 应用上下文
     * @param attrs 参数
     */
    public DropDownRefreshListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }
    
    /**
     * 
     * 初始化
     * 
     * @Description 初始化
     * 
     * @param context 上下文
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void init(Context context)
    {
        
        inflater = LayoutInflater.from(context);
        headView = (View)inflater.inflate(R.layout.head, null);
        arrowImageView = (ImageView)headView.findViewById(R.id.head_arrowImageView);
        arrowImageView.setMinimumWidth(70);
        arrowImageView.setMinimumHeight(50);
        progressBar = (ProgressBar)headView.findViewById(R.id.head_progressBar);
        tipsTextview = (TextView)headView.findViewById(R.id.head_tipsTextView);
        lastUpdatedTextView = (TextView)headView.findViewById(R.id.head_lastUpdatedTextView);
        
        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();
        headContentWidth = headView.getMeasuredWidth();
        
        headView.setPadding(0, -1 * headContentHeight, 0, 0);
        headView.invalidate();
        
        LogUtil.v("size", "width:" + headContentWidth + " height:" + headContentHeight);
        
        addHeaderView(headView, null, false);
        setOnScrollListener(this);
        
        animation =
            new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);
        
        reverseAnimation =
            new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);
        
        state = DONE;
        isRefreshable = false;
    }
    
    /**
     * @return the firstItemIndex
     */
    public int getFirstItemIndex()
    {
        return firstItemIndex;
    }
    
    /**
     * @param firstItemIndex the firstItemIndex to set
     */
    public void setFirstItemIndex(int firstItemIndex)
    {
        this.firstItemIndex = firstItemIndex;
    }
    
    /**
     * 
     * 重载方法
     * 
     * @param arg0 arg0
     * @param firstVisiableItem 可见的第一项
     * @param arg2 arg2
     * @param arg3 arg3
     */
    public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2, int arg3)
    {
        firstItemIndex = firstVisiableItem;
    }
    
    /**
     * 
     * 重载方法
     * 
     * @param arg0 arg0
     * @param arg1 arg1
     */
    public void onScrollStateChanged(AbsListView arg0, int arg1)
    {
        System.out.println("----状态改变--------");
    }
    
    /**
     * 
     * 重载方法
     * 
     * @param event 触摸事件
     * @return 是否有触摸事件
     */
    public boolean onTouchEvent(MotionEvent event)
    {
        
        if (isRefreshable)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    if (firstItemIndex == 0 && !isRecored)
                    {
                        isRecored = true;
                        startY = (int)event.getY();
                    }
                    break;
                
                case MotionEvent.ACTION_UP:
                    
                    if (state != REFRESHING && state != LOADING)
                    {
                        // if (state == DONE)
                        // {
                        // // 什么都不做
                        // }
                        if (state == PULL_To_REFRESH)
                        {
                            state = DONE;
                            changeHeaderViewByState();
                        }
                        if (state == RELEASE_To_REFRESH)
                        {
                            state = REFRESHING;
                            changeHeaderViewByState();
                            onRefresh();
                        }
                    }
                    
                    isRecored = false;
                    isBack = false;
                    
                    break;
                
                case MotionEvent.ACTION_MOVE:
                    int tempY = (int)event.getY();
                    
                    if (!isRecored && firstItemIndex == 0)
                    {
                        LogUtil.v(TAG, "在move时候记录下位置");
                        isRecored = true;
                        startY = tempY;
                    }
                    
                    if (state != REFRESHING && isRecored && state != LOADING)
                    {
                        
                        if (state == RELEASE_To_REFRESH)
                        {
                            
                            setSelection(0);
                            
                            // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                            if (((tempY - startY) / RATIO < headContentHeight) && (tempY - startY) > 0)
                            {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                                
                                LogUtil.v(TAG, "由松开刷新状态转变到下拉刷新状态");
                            }
                            // 一下子推到顶了
                            else if (tempY - startY <= 0)
                            {
                                state = DONE;
                                changeHeaderViewByState();
                                
                                LogUtil.v(TAG, "由松开刷新状态转变到done状态");
                            }
                            // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
                            // else
                            // {
                            // // 不用进行特别的操作，只用更新paddingTop的值就行了
                            // }
                        }
                        // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                        if (state == PULL_To_REFRESH)
                        {
                            
                            setSelection(0);
                            
                            // 下拉到可以进入RELEASE_TO_REFRESH的状态
                            if ((tempY - startY) / RATIO >= headContentHeight)
                            {
                                state = RELEASE_To_REFRESH;
                                isBack = true;
                                changeHeaderViewByState();
                                
                                LogUtil.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
                            }
                            // 上推到顶了
                            else if (tempY - startY <= 0)
                            {
                                state = DONE;
                                changeHeaderViewByState();
                                
                                LogUtil.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
                            }
                        }
                        
                        // done状态下
                        if (state == DONE)
                        {
                            if (tempY - startY > 0)
                            {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                            }
                        }
                        
                        // 更新headView的size
                        if (state == PULL_To_REFRESH)
                        {
                            headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);
                            
                        }
                        
                        // 更新headView的paddingTop
                        if (state == RELEASE_To_REFRESH)
                        {
                            headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);
                        }
                        
                    }
                    
                    break;
                default:
                    
                    break;
            }
        }
        
        return super.onTouchEvent(event);
    }
    
    /**
     * 
     * 当状态改变时候，调用该方法，以更新界面
     * 
     * @Description 当状态改变时候，调用该方法，以更新界面
     * 
     * @LastModifiedDate：2013-10-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void changeHeaderViewByState()
    {
        switch (state)
        {
            case RELEASE_To_REFRESH:
                arrowImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                
                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);
                
                tipsTextview.setText("松开刷新");
                
                LogUtil.v(TAG, "当前状态，松开刷新");
                break;
            case PULL_To_REFRESH:
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.VISIBLE);
                // 是由RELEASE_To_REFRESH状态转变来的
                if (isBack)
                {
                    isBack = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(reverseAnimation);
                    
                    tipsTextview.setText("下拉刷新");
                }
                else
                {
                    tipsTextview.setText("下拉刷新");
                }
                LogUtil.v(TAG, "当前状态，下拉刷新");
                break;
            
            case REFRESHING:
                
                headView.setPadding(0, 0, 0, 0);
                
                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
                tipsTextview.setText("正在刷新...");
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                
                LogUtil.v(TAG, "当前状态,正在刷新...");
                break;
            case DONE:
                headView.setPadding(0, -1 * headContentHeight, 0, 0);
                
                progressBar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(R.drawable.bluearrow_up);
                tipsTextview.setText("下拉刷新");
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                
                LogUtil.v(TAG, "当前状态，done");
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     * 设置刷新监听器
     * 
     * @Description 设置刷新监听器
     * 
     * @param refreshListener 刷新监听器
     * @LastModifiedDate：2013-10-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setonRefreshListener(OnRefreshListener refreshListener)
    {
        this.refreshListener = refreshListener;
        isRefreshable = true;
    }
    
    /**
     * 
     * 接口
     * 
     * @Description 接口
     * 
     * @author wang_ling
     * @Version [版本号, 2013-10-25]
     */
    public interface OnRefreshListener
    {
        /**
         * 
         * 刷新
         * 
         * @Description 刷新
         * 
         * @LastModifiedDate：2013-10-25
         * @author wang_ling
         * @EditHistory：<修改内容><修改人>
         */
        public void onRefresh();
    }
    
    /**
     * 
     * 刷新完成
     * 
     * @Description 刷新完成
     * 
     * @param context 应用上下文
     * @LastModifiedDate：2013-10-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void onRefreshCompleteForContacts(Context context)
    {
        state = DONE;
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        String date =
            context.getSharedPreferences("ContactsVersion", 0).getString("syncTime", format.format(new Date()));
        lastUpdatedTextView.setText("最近更新:" + date);
        changeHeaderViewByState();
    }
    
    /**
     * 
     * 刷新完成
     * 
     * @Description 刷新完成
     * 
     * @LastModifiedDate：2013-10-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void onRefreshComplete()
    {
        state = DONE;
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        String date = format.format(new Date());
        lastUpdatedTextView.setText("最近更新:" + date);
        changeHeaderViewByState();
    }
    
    /**
     * 
     * 刷新
     * 
     * @Description 刷新
     * 
     * @LastModifiedDate：2013-10-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void onRefresh()
    {
        if (refreshListener != null)
        {
            refreshListener.onRefresh();
        }
    }
    
    /**
     * 
     * 计算子视图的大小
     * 
     * @Description 计算子视图的大小
     * 
     * @param child 子视图
     * @LastModifiedDate：2013-10-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void measureView(View child)
    {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null)
        {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0)
        {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        }
        else
        {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }
    
    /**
     * 
     * 设置适配器
     * 
     * @Description 设置适配器
     * 
     * @param adapter 适配器
     * @LastModifiedDate：2013-10-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setAdapter(BaseAdapter adapter)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        String date = format.format(new Date());
        lastUpdatedTextView.setText("最近更新:" + date);
        super.setAdapter(adapter);
    }
    
    /**
     * 
     * 判断是否什么都不做
     * 
     * @Description 判断是否什么都不做
     * 
     * @return 是否什么都不做
     * @LastModifiedDate：2013-10-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public boolean isDone()
    {
        return state != DONE;
    }
}
