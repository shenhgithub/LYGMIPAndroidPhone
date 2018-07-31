/*
 * File name:  InnerEmailSendFragment.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-2-17
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.innerEmail;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artifex.BaseUtils.OnInnerListViewItemListen;
import com.artifex.BaseUtils.onEmailDetailBackListen;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityMetaData.GetInnerEmailListInfo;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.project.ui.baseui.baseInterface.baseUiInterface.onSearchResultListen;
import com.hoperun.project.ui.baseui.fragment.PMIPCustomFragment;

/**
 * 内部邮件二级页面
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-2-17]
 */
public class InnerEmailSendFragment extends PMIPCustomFragment
{
    
    /** activity **/
    private Activity           mActivity;
    
    /** 1-收件箱;2-发件箱 **/
    private String             mEmailtype;
    
    /** 文件父目录 **/
    private String             parentPath;
    
    /** 所在模块的名字 **/
    private String             funName;
    
    /**
     * 用户名
     */
    private String             user;
    
    /**
     * 未读 listview
     */
    private InnerEmailListView mUnReadView  = null;
    
    /**
     * 已读 listview
     */
    private InnerEmailListView mHasReadView = null;
    
    /**
     * 已读 listview
     */
    private InnerEmailListView mSearchView  = null;
    
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
    
    public static InnerEmailSendFragment newInstance(String type, String searchStr, String parentPath, String funName)
    {
        InnerEmailSendFragment details = new InnerEmailSendFragment();
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
        mUnReadView = (InnerEmailListView)initOfficalListView(mUnReadView, "", "");
        addListView(mUnReadView);
        return v;
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
        user = GlobalState.getInstance().getOpenId();
        this.mEmailtype = getArguments().getString("type");
        
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
        if (keyId == KeyEvent.KEYCODE_BACK)
        {
            // if (mUnReadView != null)
            // {
            // if (mUnReadView.getmDetailInfoView() != null
            // && mUnReadView.getmDetailInfoView().getVisibility() == View.VISIBLE)
            // {
            // mUnReadView.removeView(mUnReadView.getmDetailInfoView());
            // mUnReadView.setmDetailInfoView(null);
            // }
            // }
            // else if (mHasReadView != null)
            // {
            // if (mHasReadView.getmDetailInfoView() != null
            // && mHasReadView.getmDetailInfoView().getVisibility() == View.VISIBLE)
            // {
            // mHasReadView.removeView(mHasReadView.getmDetailInfoView());
            // mHasReadView.setmDetailInfoView(null);
            // }
            // }
            // else if (mSearchView != null)
            // {
            // if (mSearchView.getmDetailInfoView() != null
            // && mSearchView.getmDetailInfoView().getVisibility() == View.VISIBLE)
            // {
            // mSearchView.removeView(mSearchView.getmDetailInfoView());
            // mSearchView.setmDetailInfoView(null);
            // }
            // }
            if (mDetailInfoView != null)
            {
                mParentRL.removeView(mDetailInfoView);
                mDetailInfoView = null;
            }
            else
            {
                closeThisFragment();
            }
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
     * @author wang_ling
     */
    @Override
    public void animationOver()
    {
        if (mUnReadView == null)
        {
            mUnReadView = (InnerEmailListView)initOfficalListView(mUnReadView, "", "");
        }
        mUnReadView.getDocListRefresh("");
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void customThisFragmentView()
    {
        // 收件箱
        if (mEmailtype.equals("1"))
        {
            initReadLineLayout(TWOMODE);
        }
        // 发件箱
        else if (mEmailtype.equals("2"))
        {
            initReadLineLayout(NOMODE);
        }
        
        mFragmentBack.setBackgroundResource(R.drawable.back);
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void disPlayListView()
    {
        if (mUnReadStatus.equals(ConstState.UNReadDOCLIST))
        {
            if (mUnReadView == null)
            {
                mUnReadView = (InnerEmailListView)initOfficalListView(mUnReadView, "", "");
                mUnReadView.getDocListRefresh("");
            }
            addListView(mUnReadView);
        }
        else
        {
            if (mHasReadView == null)
            {
                mHasReadView = (InnerEmailListView)initOfficalListView(mHasReadView, "", "");
                mHasReadView.getDocListRefresh("");
            }
            addListView(mHasReadView);
        }
        hideSoftKey();
    }
    
    /**
     * 重载方法
     * 
     * @param keyWords
     * @return
     * @author wang_ling
     */
    @Override
    public View getSearchView(String keyWords, String typeId)
    {
        mSearchView = null;
        mSearchView = (InnerEmailListView)initOfficalListView(mSearchView, keyWords, "");
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
     * 重载方法
     * 
     * @param id
     * @author wang_ling
     */
    @Override
    public void readViewBack(String id)
    {
        if (mUnReadView != null)
        {
            mUnReadView.updateListView(id, false);
        }
        if (mHasReadView != null)
        {
            mHasReadView.updateListView(id, true);
        }
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
     * 详细邮件信息页面
     */
    private DetailInfoView    mDetailInfoView;
    
    onEmailDetailBackListen   mViewBackListener = new onEmailDetailBackListen()
                                                {
                                                    
                                                    @Override
                                                    public void onViewBackCancel()
                                                    {
                                                        if (mDetailInfoView != null)
                                                        {
                                                            mParentRL.removeView(mDetailInfoView);
                                                            mDetailInfoView = null;
                                                        }
                                                        
                                                        if (mUnReadView != null)
                                                        {
                                                            if (mUnReadView.getmType().equals("1")
                                                                && mUnReadView.getmReadStatus()
                                                                    .equals(ConstState.UNReadDOCLIST))
                                                            {
                                                                mUnReadView.dealListView();// /02-20
                                                            }
                                                        }
                                                    }
                                                    
                                                    @Override
                                                    public void onViewBackItemClick(
                                                        List<HashMap<String, Object>> buzList, String msgId,
                                                        String msgtitle)
                                                    {
                                                        if (mUnReadView != null)
                                                        {
                                                            mUnReadView.saveFileInfo(buzList,
                                                                user,
                                                                "",
                                                                "",
                                                                msgId,
                                                                msgtitle,
                                                                ConstState.NBYJ);
                                                        }
                                                        if (mHasReadView != null)
                                                        {
                                                            mHasReadView.saveFileInfo(buzList,
                                                                user,
                                                                "",
                                                                "",
                                                                msgId,
                                                                msgtitle,
                                                                ConstState.NBYJ);
                                                        }
                                                        if (mSearchView != null)
                                                        {
                                                            mSearchView.saveFileInfo(buzList,
                                                                user,
                                                                "",
                                                                "",
                                                                msgId,
                                                                msgtitle,
                                                                ConstState.NBYJ);
                                                        }
                                                    }
                                                };
    
    OnInnerListViewItemListen mItemListen       = new OnInnerListViewItemListen()
                                                {
                                                    
                                                    @Override
                                                    public void OnItemBackListen(GetInnerEmailListInfo info,
                                                        String mType, String path)
                                                    {
                                                        if (mDetailInfoView != null)
                                                        {
                                                            mParentRL.removeView(mDetailInfoView);
                                                        }
                                                        mDetailInfoView = null;
                                                        mDetailInfoView =
                                                            new DetailInfoView(mActivity, 100, info, mType, path);
                                                        mDetailInfoView.setMviewbacklistener(mViewBackListener);
                                                        mParentRL.addView(mDetailInfoView);
                                                    }
                                                    
                                                };
    
    /**
     * 重载方法
     * 
     * @param view
     * @param keyWords
     * @return
     * @author wang_ling
     */
    @Override
    public Object initOfficalListView(Object view, String keyWords, String typeId)
    {
        
        if (view == null)
        {
            view =
                new InnerEmailListView(mActivity, keyWords, mEmailtype, mUnReadStatus, CreateDirPath(parentPath),
                    mViewLandscapeSlideListener, false);
            ((InnerEmailListView)view).setResultListen(mResultlisten);
            ((InnerEmailListView)view).setItembackListen(mItemListen);
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
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void closeThisFragment()
    {
        if (mFragmentTMainActivityListener != null)
        {
            mFragmentTMainActivityListener.onOfficialFragmentCloseSelected();
        }
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
