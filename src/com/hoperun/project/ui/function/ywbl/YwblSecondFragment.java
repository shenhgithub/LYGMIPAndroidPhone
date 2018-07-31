/*
 * File name:  YwblSecondFragment.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-4-2
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.ywbl;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoperun.mip.utils.LogUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.fragment.PMIPCustomFragment;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-4-2]
 */
public class YwblSecondFragment extends PMIPCustomFragment
{
    
    private Activity     mActivity;
    
    /** 公文流转中，流程型公文中该栏目的类型，0是收文，1是发文 **/
    private String       mGWtype;
    
    /** 文件父目录 **/
    private String       parentPath;
    
    /** 所在模块的名字 **/
    private String       funName;
    
    /** 所在模块的code **/
    private String       funCode;
    
    /** 靠泊处理列表 **/
    private YwblListView mListview;
    
    /**
     * 重载方法
     * 
     * @param savedInstanceState
     * @author chen_wei3
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.mActivity = getActivity();
    }
    
    public static YwblSecondFragment newInstance(String type, String searchStr, String parentPath, String funName)
    {
        YwblSecondFragment details = new YwblSecondFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
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
     * @author chen_wei3
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
        mListview = (YwblListView)initOfficalListView(mListview, "", "");
        addListView(mListview);
        return v;
    }
    
    private void initData()
    {
        this.mGWtype = getArguments().getString("type");
        
        this.parentPath = getArguments().getString("parentPath");
        
        this.mSearchStr = getArguments().getString("searchStr");
        this.funName = getArguments().getString("funName");
        LogUtil.d("", "parentPath=" + parentPath);
    }
    
    /**
     * 重载方法
     * 
     * @param keyId
     * @return
     * @author chen_wei3
     */
    @Override
    public boolean onKeyDown(int keyId)
    {
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
     * 重载方法
     * 
     * @author chen_wei3
     */
    @Override
    public void animationOver()
    {
        if (mListview == null)
        {
            mListview = (YwblListView)initOfficalListView(mListview, "", "");
        }
        mListview.getDocListRefresh("");
        
    }
    
    /**
     * 重载方法
     * 
     * @author chen_wei3
     */
    @Override
    public void customThisFragmentView()
    {
        initReadLineLayout(NOMODE);
        mFragmentBack.setBackgroundResource(R.drawable.back);
    }
    
    /**
     * 重载方法
     * 
     * @author chen_wei3
     */
    @Override
    public void disPlayListView()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param keyWords
     * @param typeId
     * @return
     * @author chen_wei3
     */
    @Override
    public View getSearchView(String keyWords, String typeId)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 重载方法
     * 
     * @param id
     * @author chen_wei3
     */
    @Override
    public void readViewBack(String id)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author chen_wei3
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
     * @param typeId
     * @return
     * @author chen_wei3
     */
    @Override
    public Object initOfficalListView(Object view, String keyWords, String typeId)
    {
        if (view == null)
        {
            view =
                new YwblListView(mActivity, keyWords, mGWtype, mUnHandleStatus, mUnReadStatus, "",
                    mViewLandscapeSlideListener, false, typeId, false);
            ((YwblListView)view).setResultListen(mResultlisten);
        }
        return view;
    }
    
    /**
     * 重载方法
     * 
     * @param id
     * @author chen_wei3
     */
    @Override
    public void clickRadioButton(int id)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author chen_wei3
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
     * @author chen_wei3
     */
    @Override
    public void theThirdFragmentClose()
    {
        // TODO Auto-generated method stub
        
    }
    
}
