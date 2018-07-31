/*
 * File name:  AppDetailWarehouse.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  li_miao
 * Last modified date:  2014-3-26
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.adpter.warehouse.AppsDetailsAdapter;
import com.hoperun.manager.components.DropDownRefreshListView;
import com.hoperun.manager.components.DropDownRefreshListView.OnRefreshListener;
import com.hoperun.manager.components.WaitDialog;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.utils.StringUtils;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * 应用仓库详细页面
 * 
 * @Description<功能详细描述>
 * 
 * @author li_miao
 * @Version [版本号, 2014-3-26]
 */
public class AppDetailWarehouse extends PMIPBaseActivity implements OnClickListener, OnItemClickListener,
    OnTouchListener, OnScrollListener
{
    // ----------------add 20140411---------------------
    /** list列表上拉刷新的footview **/
    protected View                   mFootView;
    
    /**
     * 无刷新
     */
    protected final int              IDLE            = -1;
    
    /**
     * 正在获取标识，分别为：IDLE REFRESH UPREFRESH DOWNREFRESH
     */
    protected int                    isGetingDocList = IDLE;
    
    // ----------------add 20140411---------------------
    /**
     * 列表
     */
    private DropDownRefreshListView  appdetail_list;
    
    /**
     * 返回按钮
     */
    private ImageButton              btn_back;
    
    /**
     * 搜索按钮
     */
    private ImageButton              btn_search;
    
    /**
     * 搜索布局
     */
    private RelativeLayout           search_layout;
    
    /**
     * 搜索布局中的搜索按钮
     */
    private Button                   mSearchButton;
    
    /**
     * 搜索内容输入框
     */
    private EditText                 mSearchEditText;
    
    /**
     * 后方下载按钮
     */
    private ImageButton              app_state;
    
    /** 等待对话框 **/
    private WaitDialog               waitDialog;
    
    /** 返回body **/
    private MetaResponseBody         responseBuzBody;
    
    public static AppsDetailsAdapter appdetailAdapter;
    
    private ImageButton              btn_download;
    
    private UpdateManager            mUpdateManager;
    
    /** apptype **/
    private String                   apptype;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.appdetailwarehouse);
        initIntentData();
        initView();
        initListener();
        getDocListRefresh("");
    }
    
    /**
     * 
     * 初始化传递的数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-11
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initIntentData()
    {
        apptype = this.getIntent().getStringExtra("id");
        
    }
    
    /**
     * 
     * 初始化view
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-11
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initView()
    {
        appdetail_list = (DropDownRefreshListView)findViewById(R.id.app_detail_list);
        btn_back = (ImageButton)findViewById(R.id.app_back);
        btn_search = (ImageButton)findViewById(R.id.app_search);
        search_layout = (RelativeLayout)findViewById(R.id.search_layout);
        mSearchButton = (Button)findViewById(R.id.app_search_btn);
        mSearchEditText = (EditText)findViewById(R.id.search_edt);
        mFootView = LayoutInflater.from(this).inflate(R.layout.infor_listview_footer, null);
        mFootView.setVisibility(View.GONE);
        if (appdetail_list.getFooterViewsCount() == 0)
        {
            appdetail_list.addFooterView(mFootView);
            mFootView.setVisibility(View.GONE);
        }
        isGetingDocList = IDLE;
    }
    
    /**
     * 
     * 初始化监听
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-11
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initListener()
    {
        btn_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
        appdetail_list.setOnItemClickListener(this);
        appdetail_list.setonRefreshListener(new OnRefreshListener()
        {
            public void onRefresh()
            {
                if (!isGetingList())
                {
                    // getAppList("");
                    getDocListDownRefresh("");
                }
                else
                {
                    if (appdetail_list.isDone())
                    {
                        appdetail_list.onRefreshComplete();
                    }
                }
            }
        });
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
            waitDialog = WaitDialog.newInstance();
            waitDialog.show(getSupportFragmentManager(), "waitDialog");
            getAppList(c_time);
            
        }
    }
    
    /**
     * 
     * 获取应用列表
     * 
     * @Description<功能详细描述>
     * 
     * @param time
     * @LastModifiedDate：2014-4-11
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void getAppList(String time)
    {
        if (null == time || "".equals(time))
        {
            time = StringUtils.getCurrentTime();
        }
        JSONObject body = new JSONObject();
        
        try
        {
            body.put("appType", apptype);
            body.put("size", "10");
            body.put("queryDate", time);
        }
        
        catch (org.json.JSONException e)
        {
            e.printStackTrace();
        }
        NetRequestController.getAppsByType(mHandler, RequestTypeConstants.GETAPPSBYTYPE_REQUEST, body);
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    protected void onDestroy()
    {
        if (appdetailAdapter != null && null != appdetailAdapter.getInstalledReceiver())
        {
            this.unregisterReceiver(appdetailAdapter.getInstalledReceiver());
        }
        super.onDestroy();
    }
    
    /**
     * 重载方法
     * 
     * @param arg0
     * @param arg1
     * @return
     * @author li_miao
     */
    @Override
    public boolean onTouch(View arg0, MotionEvent arg1)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * 重载方法
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @author li_miao
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        // mUpdateManager = new UpdateManager(this);
        // mUpdateManager.showDownloadDialog();
        LayoutInflater factory = LayoutInflater.from(this);
        final View passView = factory.inflate(R.layout.app_detail_item, null);
        app_state = (ImageButton)passView.findViewById(R.id.apps_state);
        app_state.setOnClickListener(this);
        
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @author li_miao
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.app_back:
                this.finish();
                break;
            case R.id.apps_state:
                mUpdateManager = new UpdateManager(this, null, null);
                mUpdateManager.checkUpdateInfo();
                break;
            case R.id.app_search:
                if (search_layout.getVisibility() == View.VISIBLE)
                {
                    search_layout.setVisibility(View.GONE);
                }
                else
                {
                    search_layout.setVisibility(View.VISIBLE);
                }
                
                break;
            case R.id.app_search_btn:
                String mSearchWords = mSearchEditText.getText().toString().trim();
                if (mSearchWords == null || mSearchWords.equals(""))
                {
                    Toast.makeText(this, "请输入关键字进行查询", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // searchDocList(mSearchWords, "", true);
                }
                
                break;
        }
        
    }
    
    public void refreshView()
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final View passView = factory.inflate(R.layout.app_detail_item, null);
        final TextView text = (TextView)passView.findViewById(R.id.up_promote_text);
        app_state = (ImageButton)passView.findViewById(R.id.apps_state);
        app_state.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                text.setText("李苗");
            }
        });
    }
    
    /**
     * 重载方法
     * 
     * @param requestType
     * @param objHeader
     * @param objBody
     * @param error
     * @param errorCode
     * @author li_miao
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
            
            // 按照分类获取APP列表
                case RequestTypeConstants.GETAPPSBYTYPE_REQUEST:
                    
                    responseBuzBody = (MetaResponseBody)objBody;
                    String ret = responseBuzBody.getRetError();
                    
                    if (null != responseBuzBody && "0".equals(ret))
                    {
                        List<HashMap<String, Object>> list = responseBuzBody.getBuzList();
                        List<HashMap<String, Object>> map = new ArrayList<HashMap<String, Object>>();
                        
                        if (!"".equals(list.get(0).get("apps")))
                        {
                            map = (ArrayList<HashMap<String, Object>>)list.get(0).get("apps");
                            
                            dealgetBackDocList(map);
                            
                        }
                        else
                        {
                            List<HashMap<String, Object>> newList = new ArrayList<HashMap<String, Object>>();
                            appdetailAdapter = new AppsDetailsAdapter(this, newList);
                            appdetail_list.setAdapter(appdetailAdapter);
                            appdetailAdapter.notifyDataSetChanged();
                            mFootView.setVisibility(View.GONE);
                            if (appdetail_list.getFooterViewsCount() > 0)
                            {
                                // setListViewAdapter(-1);
                                appdetail_list.removeFooterView(mFootView);
                            }
                            closeWaitDialog();
                            isGetingDocList = IDLE;
                            Toast.makeText(this, "没有更多的数据", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                    else
                    {
                        doGetDocListError(-1);
                    }
                    break;
                default:
                    break;
            }
            
        }
        else
        {
            // waitDialog.dismiss();
            // if (!(errorCode == ConstState.CANCEL_THREAD))
            // {
            // Toast.makeText(this, "请求服务器数据失败！", Toast.LENGTH_SHORT).show();
            // }
            doGetDocListError(errorCode);
        }
    }
    
    /**
     * 
     * 关闭等待框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void closeWaitDialog()
    {
        if (null != waitDialog && waitDialog.isShowing())
        {
            waitDialog.dismiss();
        }
    }
    
    /**
     * 
     * 处理返回报文
     * 
     * @Description<功能详细描述>
     * 
     * @param ret
     * @LastModifiedDate：2014-4-11
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void dealgetBackDocList(List<HashMap<String, Object>> ret)
    {
        
        // 列表增量更新
        if (isGetingDocList == ConstState.UPREFRESH)
        {
            
            if (ret == null || ret.size() == 0)
            {// 没有数据
                mFootView.setVisibility(View.GONE);
                if (appdetail_list.getFooterViewsCount() > 0)
                {
                    // setListViewAdapter(-1);
                    appdetail_list.removeFooterView(mFootView);
                }
                closeWaitDialog();
                isGetingDocList = IDLE;
                Toast.makeText(this, "没有更多的数据", Toast.LENGTH_SHORT).show();
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
            if (appdetail_list.getFooterViewsCount() == 0)
            {
                appdetail_list.addFooterView(mFootView);
                mFootView.setVisibility(View.GONE);
                
            }
            
            if (ret == null || ret.size() == 0)
            {// 没有数据
                if (appdetail_list.getFooterViewsCount() > 0)
                {
                    if (appdetail_list == null)
                    {
                        LogUtil.i("", "mLvMain= null");
                    }
                    if (mFootView == null)
                    {
                        LogUtil.i("", "mFootView= null");
                    }
                    
                    LogUtil.i("", "mLvMain.getFooterViewsCount() =" + appdetail_list.getFooterViewsCount());
                    // setListViewAdapter(0);
                    appdetail_list.setAdapter(appdetailAdapter);
                    
                    appdetail_list.removeFooterView(mFootView);
                }
                if (appdetail_list.isDone())
                {
                    appdetail_list.onRefreshComplete();
                }
                
                closeWaitDialog();
                isGetingDocList = IDLE;
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
                // setNoListDataView(0);
            }
            else
            {
                dealBackNetData(ret, false);
                // setNoListDataView(1);
            }
        }
    }
    
    protected int    mCount = 0;
    
    /**
     * 最后一条列表数据的创建时间
     */
    protected String mRefreshLastTime;
    
    private void dealBackNetData(List<HashMap<String, Object>> ret, boolean isDown)
    {
        if (isGetingDocList == ConstState.UPREFRESH)
        {
            mFootView.setVisibility(View.GONE);
        }
        else
        {
            if (appdetail_list.isDone())
            {
                appdetail_list.onRefreshComplete();
            }
        }
        
        List<HashMap<String, Object>> newList = new ArrayList<HashMap<String, Object>>();
        
        // 上拉刷新
        if (isDown)
        {
            if (appdetailAdapter != null && appdetailAdapter.getList() != null)
            {
                for (int i = 0; i < appdetailAdapter.getList().size(); i++)
                {
                    newList.add(appdetailAdapter.getList().get(i));
                }
            }
        }
        for (int i = 0; i < ret.size(); i++)
        {
            newList.add(ret.get(i));
        }
        // 当前列表中的列表数量
        mCount = newList.size();
        if (mCount > 0)
        {
            // 获取最后一条的时间
            mRefreshLastTime = (String)newList.get(mCount - 1).get("createTime");
        }
        
        appdetailAdapter = new AppsDetailsAdapter(this, newList);
        appdetail_list.setAdapter(appdetailAdapter);
        appdetailAdapter.notifyDataSetChanged();
        closeWaitDialog();
        isGetingDocList = IDLE;
    }
    
    /**
     * 
     * 获取列表失败
     * 
     * @Description<功能详细描述>
     * 
     * @param errorCode
     * @LastModifiedDate：2014-4-11
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void doGetDocListError(int errorCode)
    {
        if (isGetingDocList == ConstState.UPREFRESH)
        {
            mFootView.setVisibility(View.GONE);
            if (errorCode != ConstState.CANCEL_THREAD)
            {
                Toast.makeText(this, "获取列表失败！", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if (appdetail_list.isDone())
            {
                appdetail_list.onRefreshComplete();
            }
            if (errorCode != ConstState.CANCEL_THREAD)
            {
                Toast.makeText(this, "获取列表失败！", Toast.LENGTH_SHORT).show();
            }
        }
        
        closeWaitDialog();
        isGetingDocList = IDLE;
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
            Toast.makeText(this, "正在刷新列表请等待！", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            return false;
        }
    }
    
    private int mLastItem = 0;
    
    /**
     * 重载方法
     * 
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     * @author wang_ling
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        appdetail_list.setFirstItemIndex(firstVisibleItem);
        if (visibleItemCount == totalItemCount)
        {
            mFootView.setVisibility(View.GONE);
        }
        else
        {
            mLastItem = firstVisibleItem + visibleItemCount - 2; // 1.FooterView;2.HeadView
        }
        
    }
    
    /**
     * 重载方法
     * 
     * @param view
     * @param scrollState
     * @author wang_ling
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
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
                Toast.makeText(this, "正在刷新列表请等待！", Toast.LENGTH_SHORT).show();
            }
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
            getAppList(c_time);
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
            getAppList(c_time);
        }
    }
    
}
