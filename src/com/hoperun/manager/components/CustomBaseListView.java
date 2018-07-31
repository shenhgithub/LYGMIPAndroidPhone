package com.hoperun.manager.components;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.hoperun.mip.utils.LogUtil;
import com.hoperun.miplygphone.R;

public class CustomBaseListView extends DropDownRefreshListView
{
    
    private View                         mFootView;
    
    private FragmentActivity             mActivity;
    
    private mCustomBaseListViewInterface mListener;
    
    private int                          mLastItem;
    
    private int                          mAllListCount;
    
    /**
     * 无刷新
     */
    public final static int              IDLE               = -1;
    
    /**
     * 全屏刷新，有loading框
     */
    public final static int              REFRESH            = 0;
    
    /**
     * 上拉刷新
     */
    public final static int              UPREFRESH          = 1;
    
    /**
     * 下拉刷新
     */
    public final static int              DOWNREFRESH        = 2;
    
    /**
     * 正在获取标识，分别为：IDLE REFRESH UPREFRESH DOWNREFRESH
     */
    protected int                        isRefreshListState = IDLE;
    
    /**
     * @param context
     * @param attrs
     */
    public CustomBaseListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mActivity = (FragmentActivity)context;
        initView();
    }
    
    /**
     * @param context
     */
    public CustomBaseListView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
        mActivity = (FragmentActivity)context;
        initView();
    }
    
    public void initView()
    {
        mFootView = LayoutInflater.from(mActivity).inflate(R.layout.infor_listview_footer, null);
        mFootView.setVisibility(View.GONE);
        
        // if (getFooterViewsCount() == 0) {
        // addFooterView(mFootView);
        // mFootView.setVisibility(View.GONE);
        // }
        
        setonRefreshListener(new OnRefreshListener()
        {
            public void onRefresh()
            {
                if (isRefreshListState == IDLE)
                {
                    if (mListener != null)
                    {
                        mListener.onDownRefreshList();
                    }
                }
                else
                {
                    onRefreshComplete();
                }
            }
        });
        
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        // TODO Auto-generated method stub
        // LogUtil.i("", "onScroll====" + getLastVisiblePosition() + ";mAllListCount=" +mAllListCount);
        setFirstItemIndex(firstVisibleItem);
        if (visibleItemCount == totalItemCount)
        {
            if (mFootView != null)
            {
                mFootView.setVisibility(View.GONE);
            }
        }
        else
        {
            mLastItem = firstVisibleItem + visibleItemCount - 2; // 1.FooterView;2.HeadView
        }
    }
    
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        // TODO Auto-generated method stub
        // 最后一个item的数等于数据的总数时，进行更新
        if (mLastItem == mAllListCount && scrollState == OnScrollListener.SCROLL_STATE_IDLE)
        {
            LogUtil.i("", "拉到最底部");
            
            if (mListener != null && isRefreshListState == IDLE)
            {
                if (mFootView != null)
                {
                    mFootView.setVisibility(View.VISIBLE);
                }
                mListener.onUpRefreshList();
            }
            else if (isRefreshListState == DOWNREFRESH || isRefreshListState == REFRESH)
            {
                Toast.makeText(mActivity, "正在刷新列表请等待！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    public interface mCustomBaseListViewInterface
    {
        public void onDownRefreshList();
        
        public void onUpRefreshList();
    }
    
    public int getmLastItem()
    {
        return mLastItem;
    }
    
    public void setmLastItem(int mLastItem)
    {
        this.mLastItem = mLastItem;
    }
    
    public int getmAllListCount()
    {
        return mAllListCount;
    }
    
    public void setmAllListCount(int mAllListCount)
    {
        
        this.mAllListCount = mAllListCount;
    }
    
    public void removeFootview()
    {
        
        if (getFooterViewsCount() > 0)
        {
            removeFooterView(mFootView);
        }
    }
    
    public void addFootview()
    {
        if (getFooterViewsCount() == 0)
        {
            addFooterView(mFootView);
            mFootView.setVisibility(View.GONE);
        }
    }
    
    public void setFootviewVisiable(boolean flag)
    {
        if (flag)
        {
            mFootView.setVisibility(View.VISIBLE);
        }
        else
        {
            mFootView.setVisibility(View.GONE);
        }
        
    }
    
    public mCustomBaseListViewInterface getmListener()
    {
        return mListener;
    }
    
    public void setmListener(mCustomBaseListViewInterface mListener)
    {
        this.mListener = mListener;
    }
    
    public int getIsRefreshListState()
    {
        return isRefreshListState;
    }
    
    public void setIsRefreshListState(int isRefreshListState)
    {
        this.isRefreshListState = isRefreshListState;
    }
    
    public static void restoreListView(CustomBaseListView listview)
    {
        if (listview.getIsRefreshListState() == CustomBaseListView.UPREFRESH)
        {
            listview.setFootviewVisiable(false);
            
        }
        else
        {
            if (listview.isDone())
            {
                listview.onRefreshComplete();
            }
        }
        listview.setIsRefreshListState(CustomBaseListView.IDLE);
    }
    
    // public void refreshListview(CustomBaseListView listview , )
    // {
    // if (mMyReportListview.getIsRefreshListState() == CustomBaseListView.UPREFRESH) {
    // if (mListCount < ConstState.getGZHBCountRows) {
    // mMyReportListview.removeFootview();
    // }
    //
    // mMyReportList = mMyReportGongZuoHbAdapter.getList();
    // for (int i = 0; i < mLists.size(); i++) {
    // mMyReportList.add(mLists.get(i));
    // }
    // // =========添加处理方法
    // } else {
    // mMyReportListview.onRefreshComplete();
    // if (mListCount >= ConstState.getGZHBCountRows) {
    //
    // mMyReportListview.addFootview();
    // } else {
    // mMyReportListview.removeFootview();
    // }
    //
    // mMyReportList.clear();
    // mMyReportList = mLists;
    // }
    //
    // if (mMyReportList != null
    // && mMyReportGongZuoHbAdapter != null
    // && mMyReportList.size() > 0) {
    // mMyReportGongZuoHbAdapter.setList(mMyReportList);
    // mMyReportGongZuoHbAdapter.notifyDataSetChanged();
    //
    // mMyReportListview.setmAllListCount(mMyReportList
    // .size());
    // mMyReportRLNodata.setVisibility(View.GONE);
    //
    // } else {
    // mMyReportListview.setmAllListCount(0);
    // Toast.makeText(mActivity, "没有数据！",
    // Toast.LENGTH_LONG);
    // mMyReportRLNodata.setVisibility(View.VISIBLE);
    // }
    // mMyReportListview
    // .setIsRefreshListState(CustomBaseListView.IDLE);
    // }
    
}
