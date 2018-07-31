/*
 * File name:  LeaderPiShiFragment.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2013-11-19
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.leaderPiShi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.project.ui.baseui.baseInterface.baseUiInterface.onSearchResultListen;
import com.hoperun.project.ui.baseui.fragment.PMIPCustomFragment;

/**
 * 领导批示
 * 
 * @Description 领导批示
 * 
 * @author wang_ling
 * @Version [版本号, 2013-11-19]
 */
public class LeaderPiShiFragment extends PMIPCustomFragment
{
    /** activity **/
    private Activity            mActivity;
    
    /** 请求类型 **/
    private String              type;
    
    /** 所在模块的文件路径 **/
    private String              parentsPath;
    
    /** 所在模块的id **/
    private String              funcId;
    
    /** 所在模块的id **/
    private String              funName;
    
    /**
     * 未读 listview
     */
    private LeaderPiShiListView mUnReadView  = null;
    
    /**
     * 已读 listview
     */
    private LeaderPiShiListView mHasReadView = null;
    
    /**
     * 搜索 listview
     */
    private LeaderPiShiListView mSearchView  = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.mActivity = getActivity();
    }
    
    /**
     * 
     * 实例化
     * 
     * @Description 实例化
     * 
     * @param funcId 功能id
     * @param parentsPath 路径
     * @return fragment
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static LeaderPiShiFragment newInstance(String funcId, String funName, String parentsPath,
        String searchKeywords, String type)
    {
        LeaderPiShiFragment details = new LeaderPiShiFragment();
        Bundle args = new Bundle();
        args.putString("funcId", funcId);
        args.putString("funName", funName);
        args.putString("parentsPath", parentsPath);
        args.putString("searchKeywords", searchKeywords);
        args.putString("type", type);
        details.setArguments(args);
        return details;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (container == null)
        {
            return null;
        }
        
        View v = super.onCreateView(inflater, container, savedInstanceState);
        
        initData();
        
        mUnReadView = (LeaderPiShiListView)initOfficalListView(mUnReadView, "", "");
        addListView(mUnReadView);
        return v;
    }
    
    // 定制整个view，哪些控件显示，哪些不显示
    @Override
    public void customThisFragmentView()
    {
        initReadLineLayout(TWOMODE);
    }
    
    /**
     * 
     * 实例化一个listview列表的view
     * 
     * @Description 实例化一个listview列表的view
     * 
     * @param view view
     * @param keyWords 关键字
     * @return view
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public Object initOfficalListView(Object view, String keyWords, String typeId)
    {
        if (view == null)
        {
            view =
                new LeaderPiShiListView(mActivity, keyWords, type, mUnReadStatus, CreateDirPath(parentsPath),
                    mViewLandscapeSlideListener, false);
            ((LeaderPiShiListView)view).setResultListen(mResultlisten);
        }
        return view;
    }
    
    /**
     * 处理从pdf阅读页面返回
     * 
     * @author ren_qiujing
     */
    @Override
    public void readViewBack(String docId)
    {
        if (mUnReadView != null)
        {
            mUnReadView.updateListView(docId, false);
        }
        if (mHasReadView != null)
        {
            mHasReadView.updateListView(docId, true);
        }
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description 初始化数据
     * 
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        
        this.parentsPath = getArguments().getString("parentsPath");
        
        this.funcId = getArguments().getString("funcId");
        
        this.funName = getArguments().getString("funName");
        this.mSearchStr = getArguments().getString("searchKeywords");
        
        this.type = getArguments().getString("type");
        
        parentsPath = ConstState.MIP_ROOT_DIR + parentsPath + "/";
        
        mTitle_tv.setVisibility(View.VISIBLE);
        mTitle_tv.setText(funName);
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onResume()
    {
        super.onResume();
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onDestroy()
    {
        hideSoftKey();
        super.onDestroy();
    }
    
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }
    
    /**
     * 重载方法 动画结束后的操作
     * 
     * @author wang_ling
     */
    @Override
    public void animationOver()
    {
        if (mUnReadView == null)
        {
            mUnReadView = (LeaderPiShiListView)initOfficalListView(mUnReadView, "", "");
        }
        mUnReadView.getDocListRefresh("");
    }
    
    // 点击“待办”、“已办”、“未读”、“已读”标签时切换各个view
    @Override
    public void clickRadioButton(int id)
    {
        // TODO Auto-generated method stub
        switch (id)
        {
            case R.id.fragment_first_ll:
                mUnReadStatus = ConstState.UNReadDOCLIST;
                break;
            case R.id.fragment_second_ll:
                mUnReadStatus = ConstState.HasReadDOCLIST;
                break;
            case R.id.fragment_third_ll:
                break;
            
            default:
                break;
        }
        
        disPlayListView();
    }
    
    // 获取搜索的list view
    @Override
    public View getSearchView(String keyWords, String typeId)
    {
        mSearchView = null;
        mSearchView = (LeaderPiShiListView)initOfficalListView(mSearchView, keyWords, typeId);
        mSearchView.getDocListRefresh("");
        
        mSearchView.setSearchResultListen(new onSearchResultListen()
        {
            
            @Override
            public void setOnSearchResultListen(int count)
            {
                // TODO Auto-generated method stub
                mSearchResultTextCount.setText(mActivity.getString(R.string.search_key_word_result, "" + count));
            }
        });
        
        return mSearchView;
    }
    
    /**
     * 
     * 根据标识为判断显示哪个listview
     * 
     * @Description 根据标识为判断显示哪个listview
     * 
     * @LastModifiedDate：2013-11-13
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public void disPlayListView()
    {
        if (mUnReadStatus.equals(ConstState.UNReadDOCLIST))
        {
            if (mUnReadView == null)
            {
                mUnReadView = (LeaderPiShiListView)initOfficalListView(mUnReadView, "", "");
                mUnReadView.getDocListRefresh("");
            }
            addListView(mUnReadView);
        }
        else
        {
            if (mHasReadView == null)
            {
                mHasReadView = (LeaderPiShiListView)initOfficalListView(mHasReadView, "", "");
                mHasReadView.getDocListRefresh("");
            }
            addListView(mHasReadView);
        }
        showReadViewFlag();
        hideSoftKey();
    }
    
    // 显示 “未读”、“已读”上的标签
    @Override
    public void setViewResult()
    {
        showReadViewFlag();
    }
    
    // 显示 “未读”、“已读”上的标签
    public void showReadViewFlag()
    {
        // TODO Auto-generated method stub
        // if (mUnReadStatus.equals(ConstState.UNReadDOCLIST))
        // {
        // if (mUnReadView != null)
        // {
        // if (mUnReadView.getDocCount() == 0)
        // {
        // unreadIv.setVisibility(View.GONE);
        // }
        // else
        // {
        // unreadIv.setVisibility(View.VISIBLE);
        // }
        // }
        // }
        // else
        // {
        // if (mHasReadView != null)
        // {
        // if (mHasReadView.getDocCount() == 0)
        // {
        // readIv.setVisibility(View.GONE);
        // }
        // else
        // {
        // readIv.setVisibility(View.VISIBLE);
        // }
        // }
        // }
    }
    
    BroadcastReceiver docSendSuccesBroadcase = new BroadcastReceiver()
                                             {
                                                 
                                                 @Override
                                                 public void onReceive(Context context, Intent intent)
                                                 {
                                                     if (intent != null)
                                                     {
                                                         String docId = intent.getStringExtra("docid");
                                                         
                                                         if (mHasReadView != null)
                                                         {
                                                             mHasReadView.updateListView(docId, false);
                                                         }
                                                         
                                                         if (mUnReadView != null)
                                                         {
                                                             mUnReadView.updateListView(docId, false);
                                                         }
                                                     }
                                                 }
                                                 
                                             };
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     * 
     */
    @Override
    public void theThirdFragmentClose()
    {
        // 该布局是二级布局，该方法不许执行
    }
    
    /**
     * 重载方法 返回键监听
     * 
     * @return
     * @author wang_ling
     */
    @Override
    public boolean onKeyDown(int keyId)
    {
        // TODO Auto-generated method stub
        if (keyId == KeyEvent.KEYCODE_BACK)
        {
            closeThisFragment();
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 重载方法 关闭二级布局
     * 
     * @author wang_ling
     */
    @Override
    public void closeThisFragment()
    {
        // TODO Auto-generated method stub
        if (mFragmentTMainActivityListener != null)
        {
            mFragmentTMainActivityListener.onSecondFragmentClose();
        }
    }
    
}