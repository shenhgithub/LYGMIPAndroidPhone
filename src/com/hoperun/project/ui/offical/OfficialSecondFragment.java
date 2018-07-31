/*
 * File name:  OfficialSecondFragment.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-11-5
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.offical;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoperun.mip.utils.LogUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.project.ui.baseui.baseInterface.baseUiInterface.onSearchResultListen;
import com.hoperun.project.ui.baseui.fragment.PMIPCustomFragment;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-11-5]
 */
public class OfficialSecondFragment extends PMIPCustomFragment
{
    /** activity **/
    private Activity           mActivity;
    
    public static final String docSendSuccess =
                                                  "com.hoperun.project.view.ui.offical.OfficialSecondFragment.sendsuccess";
    
    /** 公文流转中，流程型公文中该栏目的类型，0是收文，1是发文 **/
    private String             mGWtype;
    
    /** 文件父目录 **/
    private String             parentPath;
    
    /**
     * 待办事项 - 未读 listview
     */
    // private OfficalListView unHandleUnReadView = null;
    
    /**
     * 待办事项 - 已读 listview
     */
    // private OfficalListView unHandleHasReadView = null;
    
    private OfficalListView    unHandleView   = null;
    
    private OfficalListView    hasHandleView  = null;
    
    /**
     * 搜索 listview
     */
    private OfficalListView    mSearchView    = null;
    
    /** 所在模块的名字 **/
    private String             funName;
    
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
     * @param type 类型
     * @param parentPath 路径
     * @return OfficialDocumentFragment
     * @LastModifiedDate：2013-10-18
     * @author
     * @EditHistory：<修改内容><修改人>
     */
    public static OfficialSecondFragment newInstance(String type, String searchStr, String parentPath, String funName)
    {
        OfficialSecondFragment details = new OfficialSecondFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("searchStr", searchStr);
        args.putString("parentPath", parentPath);
        args.putString("funName", funName);
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
        mUnReadStatus = ConstState.ALLDOCLIST;
        // unHandleUnReadView = (OfficalListView)initOfficalListView(unHandleUnReadView, "");
        // addListView(unHandleUnReadView);
        unHandleView = (OfficalListView)initOfficalListView(unHandleView, "", "");
        addListView(unHandleView);
        
        return v;
    }
    
    // 定制整个view，哪些控件显示，哪些不显示
    @Override
    public void customThisFragmentView()
    {
        mFirst_label_name.setText("待办");
        mSecond_label_name.setText("已办");
        initReadLineLayout(TWOMODE);
        
        mFragmentBack.setBackgroundResource(R.drawable.back);
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description 初始化数据
     * 
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        this.mGWtype = getArguments().getString("type");
        
        this.parentPath = getArguments().getString("parentPath");
        
        this.mSearchStr = getArguments().getString("searchStr");
        this.funName = getArguments().getString("funName");
        mTitle_tv.setText(funName);
        LogUtil.d("", "parentPath=" + parentPath);
    }
    
    /**
     * 重载方法
     * 
     * @author shen_feng
     */
    @Override
    public void onResume()
    {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(OfficialSecondFragment.docSendSuccess);
        
        mActivity.registerReceiver(docSendSuccesBroadcase, filter);
    }
    
    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        mActivity.unregisterReceiver(docSendSuccesBroadcase);
        hideSoftKey();
        super.onDestroy();
    }
    
    /**
     * fragment进入时动态画面结束时请求函数
     * 
     * @return
     * @author ren_qiujing
     */
    @Override
    public void animationOver()
    {
        if (unHandleView == null)
        {
            unHandleView = (OfficalListView)initOfficalListView(unHandleView, "", "");
        }
        unHandleView.getDocListRefresh("");
        
    }
    
    // 打开文件后的监听返回，用户处理列表的移除或加载
    @Override
    public void readViewBack(String docId)
    {
        // TODO Auto-generated method stub
        if (mUnHandleStatus.equals(ConstState.UNHADLEDOCLIST))
        {
            // if (unHandleUnReadView != null)
            // {
            // unHandleUnReadView.updateListView(docId, false);
            // }
            // if (unHandleHasReadView != null)
            // {
            // unHandleHasReadView.updateListView(docId, true);
            // }
            if (unHandleView != null)
            {
                unHandleView.updateListView(docId, true);
            }
        }
        else
        {
            if (hasHandleView != null)
            {
                hasHandleView.updateListView(docId, true);
            }
            
        }
    }
    
    // 点击“待办”、“已办”、“未读”、“已读”标签时切换各个view
    @Override
    public void clickRadioButton(int id)
    {
        // TODO Auto-generated method stub
        
        switch (id)
        {
        // 待办
            case R.id.fragment_first_ll:
                mUnHandleStatus = ConstState.UNHADLEDOCLIST;
                mUnReadStatus = ConstState.ALLDOCLIST;
                break;
            // 已办
            case R.id.fragment_second_ll:
                mUnHandleStatus = ConstState.HASHANDLEDOCLIST;
                mUnReadStatus = ConstState.ALLDOCLIST;
                break;
            // 不显示
            case R.id.fragment_third_ll:
                // mUnHandleStatus = ConstState.HASHANDLEDOCLIST;
                // mUnReadStatus = ConstState.ALLDOCLIST;
                break;
            
            default:
                break;
        }
        disPlayListView();
        
    }
    
    /**
     * 实例化一个listview列表的view
     * 
     * @Description<功能详细描述>
     * 
     * @param view
     * @return
     * @LastModifiedDate：2013-11-7
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public Object initOfficalListView(Object view, String keyWords, String typeId)
    {
        if (view == null)
        {
            view =
                new OfficalListView(mActivity, keyWords, mGWtype, mUnHandleStatus, mUnReadStatus,
                    CreateDirPath(getParentPath()), mViewLandscapeSlideListener, false);
            ((OfficalListView)view).setResultListen(mResultlisten);
        }
        return view;
    }
    
    // 显示、隐藏 "已读"、“未读”上的标签
    @Override
    public void setViewResult()
    {
        // TODO Auto-generated method stub
        showReadViewFlag();
    }
    
    // 显示、隐藏 "已读"、“未读”上的标签
    public void showReadViewFlag()
    {
        // 当前文档个数提示
        // if (hasHandleView != null || unHandleUnReadView == null || unHandleHasReadView == null || hasHandleView ==
        // null)
        // {
        //
        // }
        // else
        // {
        //
        // }
        // mFirst_label_number.setText("");
        // mSecond_label_number.setText("");
        // if (mUnReadStatus.equals(ConstState.UNReadDOCLIST))
        // {
        // if (unHandleUnReadView != null)
        // {
        // if (unHandleUnReadView.getDocCount() == 0)
        // {
        // unreadIv.setVisibility(View.INVISIBLE);
        // }
        // else
        // {
        // unreadIv.setVisibility(View.VISIBLE);
        // }
        // }
        // }
        // else
        // {
        // if (unHandleHasReadView != null)
        // {
        // if (unHandleHasReadView.getDocCount() == 0)
        // {
        // readIv.setVisibility(View.INVISIBLE);
        // }
        // else
        // {
        // readIv.setVisibility(View.VISIBLE);
        // }
        // }
        // }
    }
    
    /**
     * 根据标识为判断显示哪个listview
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-8
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    
    @Override
    public void disPlayListView()
    {
        if (mUnHandleStatus.equals(ConstState.UNHADLEDOCLIST))
        {
            // if (mUnReadStatus.equals(ConstState.UNReadDOCLIST))
            // {
            // if (unHandleUnReadView == null)
            // {
            // unHandleUnReadView = (OfficalListView)initOfficalListView(unHandleUnReadView, "");
            // unHandleUnReadView.getDocListRefresh("");
            // }
            // addListView(unHandleUnReadView);
            // }
            // else
            // {
            // if (unHandleHasReadView == null)
            // {
            // unHandleHasReadView = (OfficalListView)initOfficalListView(unHandleHasReadView, "");
            // unHandleHasReadView.getDocListRefresh("");
            // }
            // addListView(unHandleHasReadView);
            // }
            if (unHandleView == null)
            {
                unHandleView = (OfficalListView)initOfficalListView(unHandleView, "", "");
                unHandleView.getDocListRefresh("");
            }
            addListView(unHandleView);
        }
        else
        {
            if (hasHandleView == null)
            {
                hasHandleView = (OfficalListView)initOfficalListView(hasHandleView, "", "");
                hasHandleView.getDocListRefresh("");
            }
            addListView(hasHandleView);
        }
        showReadViewFlag();
        hideSoftKey();
    }
    
    BroadcastReceiver docSendSuccesBroadcase = new BroadcastReceiver()
                                             {
                                                 
                                                 @Override
                                                 public void onReceive(Context context, Intent intent)
                                                 {
                                                     // TODO Auto-generated method stub
                                                     if (intent != null)
                                                     {
                                                         String docId = intent.getStringExtra("docid");
                                                         
                                                         if (mUnHandleStatus.equals(ConstState.UNHADLEDOCLIST))
                                                         {
                                                             // if (unHandleHasReadView != null)
                                                             // {
                                                             // unHandleHasReadView.updateListView(docId, false);
                                                             // }
                                                             //
                                                             // if (unHandleUnReadView != null)
                                                             // {
                                                             // unHandleUnReadView.updateListView(docId, false);
                                                             // }
                                                             if (unHandleView != null)
                                                             {
                                                                 unHandleView.updateListView(docId, false);
                                                             }
                                                         }
                                                         else
                                                         {
                                                             
                                                             if (hasHandleView != null)
                                                             {
                                                                 hasHandleView.updateListView(docId, false);
                                                             }
                                                             
                                                         }
                                                     }
                                                 }
                                                 
                                             };
    
    /**
     * 该模块存储文件的地址
     * 
     * @Description<功能详细描述>
     * 
     * @return
     * @LastModifiedDate：2013-12-5
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public String getParentPath()
    {
        if (mUnHandleStatus.equals(ConstState.UNHADLEDOCLIST))
        {
            return parentPath + ConstState.UNHANDLE_DIRNAME;
        }
        else
        {
            return parentPath + ConstState.HASHANDLE_DIRNAME;
        }
    }
    
    // 获取搜索view
    @Override
    public View getSearchView(String keyWords, String typeId)
    {
        mSearchView = null;
        mSearchView = (OfficalListView)initOfficalListView(mSearchView, keyWords, typeId);
        mSearchView.getDocListRefresh("");
        mSearchView.setSearchResultListen(new onSearchResultListen()
        {
            @Override
            public void setOnSearchResultListen(int count)
            {
                // TODO Auto-generated method stub
                mSearchResultTextCount.setText(getActivity().getString(R.string.search_key_word_result, "" + count));
            }
        });
        return mSearchView;
    }
    
    // 监听返回键
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
    
    @Override
    public void theThirdFragmentClose()
    {
        // TODO Auto-generated method stub
        
    }
    
    // 关闭本级fragment
    @Override
    public void closeThisFragment()
    {
        // TODO Auto-generated method stub
        if (mFragmentTMainActivityListener != null)
            mFragmentTMainActivityListener.onOfficialFragmentCloseSelected();
    }
}
