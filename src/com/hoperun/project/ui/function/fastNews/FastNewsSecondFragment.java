/*
 * File name:  FastNewsSecondFragment.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-3-26
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.fastNews;

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
 * 今日快报二级fragment
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-26]
 */
public class FastNewsSecondFragment extends PMIPCustomFragment
{
    /** activity **/
    private Activity         mActivity;
    
    /** 文件父目录 **/
    private String           parentPath;
    
    /** 父菜单项ID **/
    private String           funId;
    
    /** 所在模块的名字 **/
    private String           funName;
    
    /** 今日快报 **/
    private FastNewsListView unHandleView = null;
    
    /**
     * 搜索 listview
     */
    private FastNewsListView mSearchView  = null;
    
    /**
     * 重载方法
     * 
     * @param savedInstanceState
     * @author wang_ling
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.mActivity = getActivity();
    }
    
    public static FastNewsSecondFragment newInstance(String funId, String searchStr, String parentPath, String funName)
    {
        FastNewsSecondFragment details = new FastNewsSecondFragment();
        Bundle args = new Bundle();
        args.putString("funId", funId);
        args.putString("searchStr", searchStr);
        args.putString("parentPath", parentPath);
        args.putString("funName", funName);
        details.setArguments(args);
        return details;
    }
    
    /**
     * 重载方法
     * 
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * @author wang_ling
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (container == null)
        {
            return null;
        }
        initData();
        View v = super.onCreateView(inflater, container, savedInstanceState);
        
        mTitle_tv.setText(funName);
        mSearch_funnel_ll.setVisibility(View.GONE);
        mUnReadStatus = ConstState.ALLDOCLIST;
        unHandleView = (FastNewsListView)initOfficalListView(unHandleView, "", "");
        addListView(unHandleView);
        
        animationOver();
        
        return v;
    }
    
    private void initData()
    {
        this.funId = getArguments().getString("funId");
        
        this.parentPath = getArguments().getString("parentPath");
        
        this.mSearchStr = getArguments().getString("searchStr");
        this.funName = getArguments().getString("funName");
        // mTitle_tv.setText(funName);
        LogUtil.d("", "parentPath=" + parentPath);
    }
    
    /**
     * 重载方法
     * 
     * @param keyId
     * @return
     * @author wang_ling
     */
    @Override
    public boolean onKeyDown(int keyId)
    {
        boolean flag = false;
        if (keyId == KeyEvent.KEYCODE_BACK)
        {
            closeThisFragment();
            flag = true;
        }
        else
        {
            flag = false;
        }
        
        return flag;
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void animationOver()
    {
        if (unHandleView == null)
        {
            unHandleView = (FastNewsListView)initOfficalListView(unHandleView, "", "");
        }
        unHandleView.getDocListRefresh("");
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void customThisFragmentView()
    {
        initReadLineLayout(NOMODE);
        mSearchRL.setVisibility(View.GONE);
        // mFragmentBack.setBackgroundResource(R.drawable.back);
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void disPlayListView()
    {
        if (mUnHandleStatus.equals(ConstState.UNHADLEDOCLIST))
        {
            if (unHandleView == null)
            {
                unHandleView = (FastNewsListView)initOfficalListView(unHandleView, "", "");
                unHandleView.getDocListRefresh("");
            }
            addListView(unHandleView);
        }
        hideSoftKey();
    }
    
    /**
     * 重载方法
     * 
     * @param keyWords
     * @param typeId
     * @return
     * @author wang_ling
     */
    @Override
    public View getSearchView(String keyWords, String typeId)
    {
        mSearchView = null;
        mSearchView = (FastNewsListView)initOfficalListView(mSearchView, keyWords, typeId);
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
    
    /**
     * 重载方法
     * 
     * @param id
     * @author wang_ling
     */
    @Override
    public void readViewBack(String id)
    {
        if (mUnHandleStatus.equals(ConstState.UNHADLEDOCLIST))
        {
            if (unHandleView != null)
            {
                unHandleView.updateListView(id, true);
            }
        }
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void setViewResult()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param view
     * @param keyWords
     * @param typeId 帅选的id
     * @return
     * @author wang_ling
     */
    @Override
    public Object initOfficalListView(Object view, String keyWords, String typeId)
    {
        if (view == null)
        {
            view =
                new FastNewsListView(mActivity, keyWords, funId, mUnHandleStatus, mUnReadStatus, parentPath,
                    mViewLandscapeSlideListener, false, typeId);
            ((FastNewsListView)view).setResultListen(mResultlisten);
        }
        return view;
    }
    
    /**
     * 重载方法
     * 
     * @param id
     * @author wang_ling
     */
    @Override
    public void clickRadioButton(int id)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void closeThisFragment()
    {
        if (mFragmentTMainActivityListener != null)
            mFragmentTMainActivityListener.onOfficialFragmentCloseSelected();
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void theThirdFragmentClose()
    {
        // TODO Auto-generated method stub
        
    }
    
}
