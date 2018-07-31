/*
 * File name:  BaseListView.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-12-6
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.baseui;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.artifex.pdfReader.DocFlowEditImageActivity;
import com.hoperun.manager.components.DropDownRefreshListView;
import com.hoperun.manager.components.DropDownRefreshListView.OnRefreshListener;
import com.hoperun.manager.components.CustomHandler.CustomHanler;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.mip.utils.StringUtils;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.project.ui.baseui.baseInterface.IFragmentToMainActivityListen;
import com.hoperun.project.ui.baseui.baseInterface.baseUiInterface.OnResultListen;
import com.hoperun.project.ui.baseui.baseInterface.baseUiInterface.onSearchResultListen;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-12-6]
 */
abstract public class BaseListView extends RelativeLayout implements OnScrollListener, OnItemClickListener
{
    protected Activity                      mActivtiy;
    
    protected IFragmentToMainActivityListen mListener;
    
    /**
     * 获取请求列表
     */
    protected NetTask                       mGetDocListRequst;
    
    /**
     * 获取实体文件
     */
    protected NetTask                       mGetFileContentRequst;
    
    /** 是否是主页 **/
    protected Boolean                       isHomePage;
    
    /** 是否离线 true为离线，false为在线 **/
    protected boolean                       offLine = true;
    
    /** 用户名 **/
    protected String                        user    = "";
    
    /** 搜索关键字 **/
    protected String                        mSearchkeyWords;
    
    protected String                        mSearchTypeId;
    
    public String getmSearchkeyWords()
    {
        return mSearchkeyWords;
    }
    
    public void setmSearchkeyWords(String mSearchkeyWords)
    {
        this.mSearchkeyWords = mSearchkeyWords;
    }
    
    /** 公文流转中，流程型公文中该栏目的类型，0是发文 ，1是收文 **/
    protected String                  mType;
    
    /**
     * 请求列表时，待办/已办 标识
     */
    protected String                  mHandleType          = ConstState.UNHADLEDOCLIST;
    
    /**
     * 请求列表时，已读/未读 标识 请求报文时，请求的是全部列表（“0”）or 已读列表（“0”） or 未读列表（“1”）
     */
    protected String                  mReadStatus          = ConstState.UNReadDOCLIST;
    
    /**
     * 文件存储的目录路径
     */
    protected String                  mParentDirPath       = "";
    
    /** 分页一次请求多少条记录 **/
    protected String                  pageSize             = "10";
    
    /** 列表list **/
    protected DropDownRefreshListView mLvMain;
    
    protected RelativeLayout          mNoListDataRL;
    
    protected ImageView               mNoListDataIm;
    
    /** list列表上拉刷新的footview **/
    protected View                    mFootView;
    
    /** 等待框布局 **/
    protected RelativeLayout          loadLayout;
    
    /** pdf下载进度条 **/
    protected RelativeLayout          pdfLoadLayout;
    
    /** 等待图片 **/
    protected ImageView               loadImageView;
    
    /**
     * 最后一条列表数据的创建时间
     */
    protected String                  mRefreshLastTime;
    
    /**
     * 无刷新
     */
    protected final int               IDLE                 = -1;
    
    /**
     * 正在获取标识，分别为：IDLE REFRESH UPREFRESH DOWNREFRESH
     */
    protected int                     isGetingDocList      = IDLE;
    
    /**
     * 列表的数量
     */
    protected int                     mCount               = 0;
    
    protected int                     mLastItem            = 0;
    
    protected OnTouchListener         mViewLandscapeSlideListener;
    
    private int                       selectIndex          = -1;
    
    protected boolean                 isDownling           = false;
    
    protected OnResultListen          mOnResultListener;
    
    protected onSearchResultListen    mOnSearchResultListener;
    
    protected String                  mSelectedDocId       = "";
    
    protected String                  mSelectedScheduleUrl = "";
    
    protected String                  mSelectedPath        = "";
    
    protected String                  mSelectedFileTitle   = "";
    
    protected String                  mSelectedFlag        = "";
    
    protected String                  mSelectedLockId      = "";
    
    protected String                  mSelectedLockName    = "";
    
    protected boolean                 isForHomePop         = false;
    
    protected Handler                 bHandler             = new Handler()
                                                           {
                                                               
                                                               @SuppressWarnings("unchecked")
                                                               @Override
                                                               public void handleMessage(Message msg)
                                                               {
                                                                   // TODO Auto-generated method stub
                                                                   super.handleMessage(msg);
                                                                   
                                                                   switch (msg.what)
                                                                   {
                                                                       case ConstState.GETFILELIST:
                                                                           
                                                                           if (isGetingDocList == ConstState.UPREFRESH)
                                                                           {
                                                                               mFootView.setVisibility(View.GONE);
                                                                           }
                                                                           else
                                                                           {
                                                                               if (mLvMain.isDone())
                                                                               {
                                                                                   mLvMain.onRefreshComplete();
                                                                               }
                                                                           }
                                                                           
                                                                           reFreshList(msg.obj);
                                                                           closeProgressDialog();
                                                                           isGetingDocList = IDLE;
                                                                           break;
                                                                       
                                                                       case ConstState.GETFILECONTENT:
                                                                           boolean retDBData = (Boolean)msg.obj;
                                                                           
                                                                           getFileContentEnd(retDBData);
                                                                           break;
                                                                       
                                                                       case ConstState.REFRESHLIST:
                                                                           reFreshList(msg.obj);
                                                                           break;
                                                                       
                                                                       default:
                                                                           break;
                                                                   }
                                                                   
                                                               }
                                                               
                                                           };
    
    protected CustomHanler            mHandler             = new CustomHanler()
                                                           {
                                                               
                                                               @Override
                                                               public void PostHandle(int requestType,
                                                                   Object objHeader, Object objBody, boolean error,
                                                                   int errorCode)
                                                               {
                                                                   // TODO Auto-generated method stub
                                                                   onPostHandle(requestType,
                                                                       objHeader,
                                                                       objBody,
                                                                       error,
                                                                       errorCode);
                                                               }
                                                               
                                                           };
    
    abstract public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode);
    
    /**
     * 
     * 构造函数
     * 
     * @Description 构造函数
     * @param context 上下文
     * @param attrs AttributeSet
     * @param defStyle defStyle
     * @EditHistory：<修改内容><修改人>
     */
    public BaseListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * 
     * 构造函数
     * 
     * @Description 构造函数
     * @param context 上下文
     * @param attrs AttributeSet
     * @EditHistory：<修改内容><修改人>
     */
    public BaseListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * 
     * 构造函数
     * 
     * @Description 构造函数
     * @param context 上下文
     * @EditHistory：<修改内容><修改人>
     */
    public BaseListView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * 
     * 构造函数
     * 
     * @Description 构造函数
     * @param activity 当前activity
     * @param keyWords 搜索关键字
     * @param gwType 公文流转中，流程型公文中该栏目的类型，0是发文 ，1是收文
     * @param handleType 请求列表时，待办/已办 标识
     * @param readStatus 请求列表时，已读/未读 标识 请求报文时，请求的是全部列表（“0”）or 已读列表（“0”） or 未读列表（“1”）
     * @param parentDirPath 文件存储的目录路径
     * @param listen listen
     * @param isHomePage 是否是主页
     * @param isActivity 是否在activity中使用
     * @EditHistory：<修改内容><修改人>
     */
    public BaseListView(Activity activity, String keyWords, String gwType, String handleType, String readStatus,
        String parentDirPath, OnTouchListener listen, Boolean isHomePage, String typeId, boolean isActivity)
    {
        super(activity);
        // TODO Auto-generated constructor stub
        mActivtiy = activity;
        if (!isActivity)
        {
            mListener = (IFragmentToMainActivityListen)activity;
        }
        
        mSearchkeyWords = keyWords;
        mSearchTypeId = typeId;
        mType = gwType;
        mHandleType = handleType;
        mReadStatus = readStatus;
        mParentDirPath = parentDirPath;
        mViewLandscapeSlideListener = listen;
        this.isHomePage = isHomePage;
        
        initView();
        initData();
        initListener();
    }
    
    /**
     * 获取 mType
     * 
     * @return 返回 mType
     * @author wang_ling
     */
    public String getmType()
    {
        return mType;
    }
    
    /**
     * 设置 mType
     * 
     * @param mType 对mType进行赋值
     * @author wang_ling
     */
    public void setmType(String mType)
    {
        this.mType = mType;
    }
    
    /**
     * 获取 mReadStatus
     * 
     * @return 返回 mReadStatus
     * @author wang_ling
     */
    public String getmReadStatus()
    {
        return mReadStatus;
    }
    
    /**
     * 设置 mReadStatus
     * 
     * @param mReadStatus 对mReadStatus进行赋值
     * @author wang_ling
     */
    public void setmReadStatus(String mReadStatus)
    {
        this.mReadStatus = mReadStatus;
    }
    
    /**
     * 
     * 初始化view
     * 
     * @Description 初始化view
     * 
     * @EditHistory：<修改内容><修改人>
     */
    private void initView()
    {
        LayoutInflater.from(mActivtiy).inflate(R.layout.officiallistview_layout, this, true);
        
        mLvMain = (DropDownRefreshListView)findViewById(R.id.office_lv_undo);
        
        if (isHomePage)
        {
            mFootView = LayoutInflater.from(mActivtiy).inflate(R.layout.home_infor_listview_footer, null);
        }
        else
        {
            mFootView = LayoutInflater.from(mActivtiy).inflate(R.layout.infor_listview_footer, null);
        }
        mFootView.setVisibility(View.GONE);
        
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        pdfLoadLayout = (RelativeLayout)findViewById(R.id.pdfload_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        
        mNoListDataRL = (RelativeLayout)findViewById(R.id.no_datarl);
        mNoListDataIm = (ImageView)findViewById(R.id.no_dataim);
        
        mNoListDataRL.setVisibility(View.INVISIBLE);
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description 初始化数据
     * 
     * @EditHistory：<修改内容><修改人>
     */
    public void initData()
    {
        offLine = GlobalState.getInstance().getOfflineLogin();
        
        user = GlobalState.getInstance().getOpenId();
        
        if (mLvMain.getFooterViewsCount() == 0)
        {
            mLvMain.addFooterView(mFootView);
            mFootView.setVisibility(View.GONE);
        }
        
        initListViewAdapter();
        // List<GetDocListInfo> mDocList = new ArrayList<GetDocListInfo>();
        // if (isHomePage)
        // {
        // mHomeDaiBanAdapter = new HomeDaiBanAdapter(mActivtiy, mDocList, user);
        // mLvMain.setAdapter(mHomeDaiBanAdapter);
        // }
        // else
        // {
        // mdocListAdapter = new OfficalDocSecondAdapter(mActivtiy, mDocList, user, mGwType);
        // mLvMain.setAdapter(mdocListAdapter);
        // }
        
        isGetingDocList = IDLE;
    }
    
    /**
     * 
     * 初始化监听
     * 
     * @Description 初始化监听
     * 
     * @EditHistory：<修改内容><修改人>
     */
    public void initListener()
    {
        mLvMain.setOnItemClickListener(this);
        mLvMain.setOnScrollListener(this);
        mLvMain.setonRefreshListener(new OnRefreshListener()
        {
            public void onRefresh()
            {
                if (!isGetingList() && !offLine)
                {
                    getDocListDownRefresh("");
                }
                else
                {
                    if (mLvMain.isDone())
                    {
                        mLvMain.onRefreshComplete();
                    }
                }
            }
        });
        mLvMain.setOnTouchListener(mViewLandscapeSlideListener);
    }
    
    /**
     * 
     * 设置mOnResultListener监听
     * 
     * @Description 设置mOnResultListener监听
     * @param listen OnResultListen对象
     * @EditHistory：<修改内容><修改人>
     */
    public void setResultListen(OnResultListen listen)
    {
        mOnResultListener = listen;
    }
    
    /**
     * 
     * 设置mOnSearchResultListener监听
     * 
     * @Description 设置mOnSearchResultListener监听
     * @param listen onSearchResultListen对象
     * @EditHistory：<修改内容><修改人>
     */
    public void setSearchResultListen(onSearchResultListen listen)
    {
        mOnSearchResultListener = listen;
    }
    
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        // TODO Auto-generated method stub
        // 点击时间间隔太短则不触发
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        
        if (isGetingDocList != IDLE)
        {
            Toast.makeText(mActivtiy, "正在刷新列表请等待！", Toast.LENGTH_SHORT).show();
            return;
        }
        
        LogUtil.i("", "onItemClick == " + "arg2 =" + arg2);
        selectIndex = arg2 - 1;
        LogUtil.i("", "onItemClick == " + "selectIndex =" + selectIndex);
        
        onClickLisItem(selectIndex);
    }
    
    /**
     * 判断是否正在获取列表
     * 
     * @Description<功能详细描述>
     * 
     * @return true 是； false 否
     * @LastModifiedDate：2013-11-7
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private boolean isGetingList()
    {
        if (isGetingDocList != IDLE)
        {
            Toast.makeText(mActivtiy, "正在刷新列表请等待！", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 获取列表，且显示loading进度条
     * 
     * @Description<功能详细描述>
     * @param c_time 查询时间
     * @LastModifiedDate：2013-11-7
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void getDocListRefresh(String c_time)
    {
        if (!isGetingList())
        {
            isGetingDocList = ConstState.REFRESH;
            showProgressDialog();
            getDocList(c_time);
            
        }
    }
    
    /**
     * 上拉刷新
     * 
     * @Description<功能详细描述>
     * @param c_time 查询时间
     * @LastModifiedDate：2013-11-7
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void getDocListUpRefresh(String c_time)
    {
        if (!isGetingList())
        {
            isGetingDocList = ConstState.UPREFRESH;
            getDocList(c_time);
        }
    }
    
    /**
     * 下拉刷新
     * 
     * @Description<功能详细描述>
     * @param c_time 查询时间
     * @LastModifiedDate：2013-11-7
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void getDocListDownRefresh(String c_time)
    {
        if (!isGetingList())
        {
            isGetingDocList = ConstState.DOWNREFRESH;
            getDocList(c_time);
        }
    }
    
    /**
     * 
     * 获取列表
     * 
     * @Description 获取列表
     * @param c_time 查询时间
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void getDocList(String c_time)
    {
        if (offLine)
        {
            if (c_time == null || c_time.equals(""))
            {
                c_time = "2099-12-31 00:00:00";
            }
            getDocListFromDBData(c_time);
        }
        else
        {
            if (c_time == null || c_time.equals(""))
            {
                c_time = StringUtils.getCurrentTime3();
            }
            getDocListFromNet(c_time);
        }
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        // TODO Auto-generated method stub
        // LogUtil.i("", "onScroll firstVisibleItem=" + firstVisibleItem);
        mLvMain.setFirstItemIndex(firstVisibleItem);
        if (visibleItemCount == totalItemCount)
        {
            mFootView.setVisibility(View.GONE);
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
        if (mLastItem == mCount && scrollState == OnScrollListener.SCROLL_STATE_IDLE)
        {
            LogUtil.i("", "拉到最底部");
            
            if (isGetingDocList == IDLE)
            {
                mFootView.setVisibility(View.VISIBLE);
                getDocListUpRefresh(mRefreshLastTime);
            }
            else if (isGetingDocList == ConstState.DOWNREFRESH)
            {
                Toast.makeText(mActivtiy, "正在刷新列表请等待！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * 处理返回的报文
     * 
     * @Description<功能详细描述>
     * 
     * @param ret
     * @LastModifiedDate：2013-11-8
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected void dealgetBackDocList(List<HashMap<String, Object>> ret)
    {
        // 列表增量更新
        if (isGetingDocList == ConstState.UPREFRESH)
        {
            
            if (ret == null || ret.size() == 0)
            {// 没有数据
                mFootView.setVisibility(View.GONE);
                if (mLvMain.getFooterViewsCount() > 0)
                {
                    setListViewAdapter(-1);
                    mLvMain.removeFooterView(mFootView);
                }
                closeProgressDialog();
                isGetingDocList = IDLE;
                Toast.makeText(mActivtiy, "没有更多的数据", Toast.LENGTH_SHORT).show();
            }
            else
            {
                LogUtil.d("size", ret.size() + "");
                dealBackNetData(ret, true);
            }
        }
        // 先清除列表数据，再刷新
        else
        {
            if (mLvMain.getFooterViewsCount() == 0)
            {
                mLvMain.addFooterView(mFootView);
                mFootView.setVisibility(View.GONE);
                
            }
            
            if (ret == null || ret.size() == 0)
            {// 没有数据
                if (mLvMain.getFooterViewsCount() > 0)
                {
                    if (mLvMain == null)
                    {
                        LogUtil.i("", "mLvMain= null");
                    }
                    if (mFootView == null)
                    {
                        LogUtil.i("", "mFootView= null");
                    }
                    
                    LogUtil.i("", "mLvMain.getFooterViewsCount() =" + mLvMain.getFooterViewsCount());
                    setListViewAdapter(0);
                    
                    mLvMain.removeFooterView(mFootView);
                }
                if (mLvMain.isDone())
                {
                    mLvMain.onRefreshComplete();
                }
                
                if (mOnResultListener != null)
                {
                    mOnResultListener.setOnResultListen();
                }
                
                closeProgressDialog();
                isGetingDocList = IDLE;
                Toast.makeText(mActivtiy, "没有数据", Toast.LENGTH_SHORT).show();
                setNoListDataView(0);
            }
            else
            {
                dealBackNetData(ret, false);
                setNoListDataView(1);
            }
        }
    }
    
    /**
     * 处理网络返回的列表数据
     * 
     * @Description<功能详细描述>
     * 
     * @param ret
     * @param isDown
     * @LastModifiedDate：2013-11-6
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void dealBackNetData(final List<HashMap<String, Object>> ret, final boolean isDown)
    {
        Thread th = new Thread()
        {
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                if (ret != null && ret.size() > 0)
                {
                    Object obj = dealDatafromNetWork(ret);
                    
                    addDataToListAndRefresh(obj, isDown);
                }
                super.run();
            }
            
        };
        th.start();
        
    }
    
    /**
     * 将处理后的列表数据，加载到相应的listview上
     * 
     * @Description<功能详细描述>
     * 
     * @param lists
     * @param isDown
     * @LastModifiedDate：2013-11-6
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected void addDataToListAndRefresh(Object obj, boolean isDown)
    {
        if (obj == null)
        {
            return;
        }
        Object newObj = addDataToListRefresh(obj, isDown);
        
        Message msg = new Message();
        msg.what = ConstState.GETFILELIST;
        msg.obj = newObj;
        
        bHandler.sendMessage(msg);
        
    }
    
    /**
     * doget获取dol列表失败
     * 
     * @Description<功能详细描述>
     * 
     * @EditHistory：<修改内容><修改人>
     */
    protected void doGetDocListError(int errorCode)
    {
        if (isGetingDocList == ConstState.UPREFRESH)
        {
            mFootView.setVisibility(GONE);
            if (errorCode != ConstState.CANCEL_THREAD)
            {
                Toast.makeText(mActivtiy, "获取列表失败！", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if (mLvMain.isDone())
            {
                mLvMain.onRefreshComplete();
            }
            if (errorCode != ConstState.CANCEL_THREAD)
            {
                Toast.makeText(mActivtiy, "获取列表失败！", Toast.LENGTH_SHORT).show();
            }
        }
        
        if (mOnResultListener != null)
        {
            mOnResultListener.setOnResultListen();
        }
        
        closeProgressDialog();
        isGetingDocList = IDLE;
    }
    
    /**
     * 
     * 显示等待框
     * 
     * @Description 显示等待框
     * 
     * @LastModifiedDate：2013-10-22
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    protected void showProgressDialog()
    {
        loadLayout.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable)loadImageView.getBackground();
        animationDrawable.start();
    }
    
    /**
     * 
     * 关闭等待框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-22
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    protected void closeProgressDialog()
    {
        loadLayout.setVisibility(View.GONE);
    }
    
    protected PopupWindow mPopupWindow;
    
    protected ProgressBar mDownloadfilePro;
    
    /**
     * 
     * 主页下载pdf进度条
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-29
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    protected void showDownloadProgressHome()
    {
        isDownling = true;
        pdfLoadLayout.setVisibility(View.VISIBLE);
        
        mDownloadfilePro = (ProgressBar)this.findViewById(R.id.downloadprogress);
        
        mDownloadfilePro.setProgress(0);
        
        Button mBtnCancel = (Button)this.findViewById(R.id.cancel_download);
        
        mBtnCancel.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // 点击时间间隔太短则不触发
                if (OnClickUtil.isMostPost())
                {
                    return;
                }
                // mGetFileContentRequst.shutDownExecute();
                stopGetFileContentNetTask();
                if (isDownling)
                {
                    
                    // String docid = mSelectedInfo.getStringKeyValue(GetDocListInfo.docid);
                    // String filePath = mParentDirPath + docid + ".pdf";
                    //
                    // FileUtil.deleteFile(filePath);
                    isDownling = false;
                }
                pdfLoadLayout.setVisibility(View.GONE);
            }
        });
    }
    
    /**
     * 
     * 关闭下载框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-3-21
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    protected void closeDownLoadProgress()
    {
        if (null != mPopupWindow)
        {
            mPopupWindow.dismiss();
        }
    }
    
    /**
     * 
     * 显示下载进度条
     * 
     * @LastModifiedDate：2013-10-9
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    protected void showDownloadProgress()
    {
        isDownling = true;
        View view = LayoutInflater.from(mActivtiy).inflate(R.layout.downloadfileprogress, null);
        
        // 输入框一样长，高度自适应
        mPopupWindow =
            new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        
        mPopupWindow.showAtLocation(mLvMain, Gravity.CENTER, 0, 0);
        
        mDownloadfilePro = (ProgressBar)view.findViewById(R.id.downloadprogress);
        
        mDownloadfilePro.setProgress(0);
        
        Button mBtnCancel = (Button)view.findViewById(R.id.cancel_download);
        
        mBtnCancel.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // 点击时间间隔太短则不触发
                if (OnClickUtil.isMostPost())
                {
                    return;
                }
                // mGetFileContentRequst.shutDownExecute();
                stopGetFileContentNetTask();
                if (isDownling)
                {
                    // String docid = mSelectedInfo.getStringKeyValue(GetDocListInfo.docid);
                    //
                    // String filePath = mParentDirPath + docid + ".pdf";
                    //
                    // FileUtil.deleteFile(filePath);
                    isDownling = false;
                }
                mPopupWindow.dismiss();
                
            }
        });
        
        mPopupWindow.setOnDismissListener(new OnDismissListener()
        {
            
            public void onDismiss()
            {
                // TODO Auto-generated method stub
                
            }
        });
    }
    
    public void saveFileInfo(List<HashMap<String, Object>> buzList, final String username, final String type,
        final String handleType, final String messageItemGuid, final String fileTitle, final String funtionId)
    {
        final String filename = (String)buzList.get(0).get("filename").toString().trim();
        
        Thread th = new Thread()
        {
            
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                int index = filename.lastIndexOf(";");
                String filetype = "";
                if (index == -1)
                {
                    mSelectedPath = mParentDirPath + filename;
                }
                else
                {
                    String mFilename = filename.substring(0, index);
                    mSelectedPath = mParentDirPath + mFilename;
                    filetype = filename.substring(index + 1, filename.length());
                }
                boolean retDBData =
                    DBDataObjectWrite.DocSaveFileRecord(username,
                        type,
                        handleType,
                        messageItemGuid,
                        fileTitle,
                        mSelectedPath,
                        funtionId,
                        filetype);
                
                Message msg = new Message();
                msg.what = ConstState.GETFILECONTENT;
                msg.obj = retDBData;
                
                bHandler.sendMessage(msg);
                super.run();
            }
        };
        th.start();
    }
    
    /**
     * 打开列表的某项，处理列表，将未读中的列表移除，添加到已读，修改本地数据库
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-15
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void dealListView()
    {
        Thread th = new Thread()
        {
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                updataDatabase();
                if (mOnResultListener != null)
                {
                    mOnResultListener.updateListView(mSelectedDocId);
                }
            }
            
        };
        th.start();
        
    }
    
    /**
     * 停止获取文件任务
     * 
     * @Description<功能详细描述>
     * 
     * @EditHistory：<修改内容><修改人>
     */
    public void stopGetFileContentNetTask()
    {
        // TODO Auto-generated method stub
        if (mGetFileContentRequst != null)
            mGetFileContentRequst.shutDownExecute();
    }
    
    /**
     * 
     * 打开PDF
     * 
     * @param filePath 打开pdf文件的路径, 其value值得类型为 String
     * @param filtTitle 打开的pdf文件的文件名称, 其value值得类型为 String
     * @param fileId 打开的pdf文件的文件ID, 其value值得类型为 String
     * @param type 区分公文类型，收文/发文/普阅文等
     * @param handleType 区分"待办/已办"
     * @param lockflag 锁的类型：-1 没有锁机值， 0 没有被锁，1 被锁
     * @param lockId 锁住人的id
     * @param lockName 锁住该文件的人的名称
     * @param hasInfoFlag 是否有信息显示，true 有； false 无信息显示；, 其value值得类型为 boolean
     * @param signFlag 签批的类型：0：不可以签批；1：签批： 2：签署 , 其value值得类型为 int
     * @param sendFlag 发送的类型：0：不可以发送；1：公文流转发送；2：通用发送； 其value值得类型为 int
     * @LastModifiedDate：2013-10-10
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    protected void intentOpenFile(String filePath, String filtTitle, String fileId, String type, String handleType,
        String lockflag, String lockId, String lockName, boolean hasInfoFlag, int signFlag, int sendFlag,
        boolean hasAttach)
    {
        
        Intent intent = new Intent();
        intent.setClass(mActivtiy, DocFlowEditImageActivity.class);
        
        // 文件路径
        intent.putExtra(ConstState.PDF_PATH, filePath);
        
        // 文件名
        intent.putExtra(ConstState.PDF_FILENAME, filtTitle);
        
        // 文件id
        intent.putExtra(ConstState.PDF_FILEMESSAGEID, fileId);
        
        intent.putExtra(ConstState.PDF_TYPE, type);
        
        intent.putExtra(ConstState.PDF_HANDLETYPE, handleType);
        
        intent.putExtra(ConstState.PDF_LOCKFLAG, lockflag);
        
        intent.putExtra(ConstState.PDF_LOCKID, lockId);
        
        intent.putExtra(ConstState.PDF_LOCKNAME, lockName);
        
        intent.putExtra(ConstState.PDF_HASINFO, hasInfoFlag);
        
        intent.putExtra(ConstState.PDF_SIGNFLAG, signFlag);
        
        intent.putExtra(ConstState.PDF_SENDFLAG, sendFlag);
        intent.putExtra(ConstState.PDF_HASATTACH, hasAttach);
        mActivtiy.startActivity(intent);
        
    }
    
    public void setNoListDataView(int count)
    {
        if (count == 0)
        {
            mNoListDataRL.setVisibility(View.VISIBLE);
            
            if (isForHomePop())
            {
                
            }
            else
            {
                mNoListDataIm.setBackgroundResource(R.drawable.list_nodata);
            }
            
        }
        else
        {
            mNoListDataRL.setVisibility(View.INVISIBLE);
        }
    }
    
    public boolean isForHomePop()
    {
        return isForHomePop;
    }
    
    public void setForHomePop(boolean isForHomePop)
    {
        this.isForHomePop = isForHomePop;
    }
    
    // 第一次初始化listview的Adapter
    public abstract void initListViewAdapter();
    
    // 根据获取list结果来设置listview的Adtapter
    public abstract void setListViewAdapter(int selectCount);
    
    // 从网络获取list数据
    public abstract void getDocListFromDBData(String time);
    
    // 从本地数据库获取list数据
    public abstract void getDocListFromNet(String time);
    
    // 处理从网络获取的数据
    public abstract Object dealDatafromNetWork(final List<HashMap<String, Object>> ret);
    
    // 将得到的数据添加到listview 中，并刷新列表
    public abstract Object addDataToListRefresh(Object obj, boolean isDown);
    
    // 刷新列表
    public abstract void reFreshList(Object obj);
    
    // 点击列表中某一项
    public abstract void onClickLisItem(int position);
    
    // 获取列表中的某一条
    public abstract boolean getFileContent(String docid, String docUrl, String path);
    
    // 下载文件结束
    public abstract void getFileContentEnd(boolean flag);
    
    // 打开文件时，需要更新数据库
    public abstract void updataDatabase();
    
    // 更新listview列表
    public abstract void updateListView(String docId, boolean flag);
    
    // 得到列表中的数量
    public abstract int getDocCount();
}
