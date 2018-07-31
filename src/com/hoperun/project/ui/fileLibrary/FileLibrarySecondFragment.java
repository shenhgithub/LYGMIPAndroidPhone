/*
 * File name:  FileLibrarySecondFragment.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2013-11-18
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.fileLibrary;

import android.app.Activity;
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
 * 文档库二级页面
 * 
 * @Description 文档库二级页面
 * 
 * @author wang_ling
 * @Version [版本号, 2013-11-18]
 */
public class FileLibrarySecondFragment extends PMIPCustomFragment
{
    /** activity **/
    private Activity            mActivity;
    
    /** 文档库中，流程型公文中该栏目的类型，2是期刊，3是通知，5是归档文件 **/
    private String              mWDKtype;
    
    /** 文件父目录 **/
    private String              parentPath;
    
    /**
     * 未读 listview
     */
    private FileLibraryListView mUnReadView  = null;
    
    /**
     * 已读 listview
     */
    private FileLibraryListView mHasReadView = null;
    
    /**
     * 搜索 listview
     */
    private FileLibraryListView mSearchView  = null;
    
    /** 所在模块的名字 **/
    private String              funName;
    
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
    public static FileLibrarySecondFragment newInstance(String type, String searchStr, String parentPath, String funName)
    {
        FileLibrarySecondFragment details = new FileLibrarySecondFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("searchStr", searchStr);
        args.putString("parentPath", parentPath);
        args.putString("funName", funName);
        details.setArguments(args);
        return details;
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
    public static FileLibrarySecondFragment newInstance(String type, String searchStr, String parentPath)
    {
        FileLibrarySecondFragment details = new FileLibrarySecondFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("searchStr", searchStr);
        args.putString("parentPath", parentPath);
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
        
        mUnReadView = (FileLibraryListView)initOfficalListView(mUnReadView, "", "");
        addListView(mUnReadView);
        return v;
    }
    
    @Override
    public void customThisFragmentView()
    {
        // TODO Auto-generated method stub
        initReadLineLayout(0);
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
        this.mWDKtype = getArguments().getString("type");
        
        this.parentPath = getArguments().getString("parentPath");
        
        this.mSearchStr = getArguments().getString("searchStr");
        
        this.funName = getArguments().getString("funName");
        mTitle_tv.setText(funName);
        
        LogUtil.d("", "parentPath=" + parentPath);
    }
    
    // 实例化 listview
    @Override
    public Object initOfficalListView(Object view, String keyWords, String typeId)
    {
        // TODO Auto-generated method stub
        if (view == null)
        {
            view =
                new FileLibraryListView(mActivity, keyWords, mWDKtype, CreateDirPath(parentPath),
                    mViewLandscapeSlideListener, false);
            ((FileLibraryListView)view).setResultListen(mResultlisten);
        }
        return view;
    }
    
    /**
     * 重载方法动画结束后的执行方法
     * 
     * @author wang_ling
     */
    @Override
    public void animationOver()
    {
        if (mUnReadView == null)
        {
            mUnReadView = (FileLibraryListView)initOfficalListView(mUnReadView, "", "");
        }
        mUnReadView.getDocListRefresh("");
    }
    
    // 获取搜索 listview
    @Override
    public View getSearchView(String keyWords, String typeId)
    {
        // TODO Auto-generated method stub
        mSearchView = null;
        mSearchView = (FileLibraryListView)initOfficalListView(mSearchView, keyWords, typeId);
        mSearchView.getDocListRefresh("");
        
        mSearchView.setSearchResultListen(new onSearchResultListen()
        {
            
            @Override
            public void setOnSearchResultListen(int count)
            {
                // TODO Auto-generated method stub
            }
        });
        return mSearchView;
    }
    
    // 显示listview
    @Override
    public void disPlayListView()
    {
        // TODO Auto-generated method stub
        if (mUnReadStatus.equals("0"))
        {
            if (mUnReadView == null)
            {
                mUnReadView = (FileLibraryListView)initOfficalListView(mUnReadView, "", "");
                mUnReadView.getDocListRefresh("");
            }
            addListView(mUnReadView);
        }
        else
        {
            if (mHasReadView == null)
            {
                mHasReadView = (FileLibraryListView)initOfficalListView(mHasReadView, "", "");
                mHasReadView.getDocListRefresh("");
            }
            addListView(mHasReadView);
        }
    }
    
    /**
     * 处理从pdf阅读页面返回
     * 
     * @author wang_ling
     */
    public void readViewBack(String docId)
    {
        if (mUnReadView != null)
        {
            mUnReadView.updateListView(docId, false);
        }
        if (mSearchView != null)
        {
            mSearchView.updateListView(docId, false);
        }
        
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
    
    // 关闭本级fragment
    @Override
    public void closeThisFragment()
    {
        // TODO Auto-generated method stub
        if (mFragmentTMainActivityListener != null)
            mFragmentTMainActivityListener.onOfficialFragmentCloseSelected();
    }
    
    @Override
    public void theThirdFragmentClose()
    {
        // 该布局是二级布局，该方法不许执行
    }
    
    // 显示、隐藏 "已读"、“未读”上的标签
    @Override
    public void setViewResult()
    {
        // TODO Auto-generated method stub
        // 显示、隐藏 "已读"、“未读”上的标签, 该功能上没有"已读"、“未读”功能，为空
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
}
