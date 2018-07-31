/*
 * File name:  XwzxSecondFragment.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-3-18
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.xwzx;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.hoperun.manager.adpter.xwzx.XwzxSearchAdpter;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityMetaData.NewsTypeInfo;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.project.ui.baseui.baseInterface.baseUiInterface.onSearchResultListen;
import com.hoperun.project.ui.baseui.fragment.PMIPCustomFragment;

/**
 * 新闻中心 二级菜单
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-18]
 */
public class XwzxSecondFragment extends PMIPCustomFragment
{
    /** activity **/
    private Activity           mActivity;
    
    public static final String docSendSuccess =
                                                  "com.hoperun.project.view.ui.offical.OfficialSecondFragment.sendsuccess";
    
    /** 公文流转中，流程型公文中该栏目的类型，0是收文，1是发文 **/
    private String             mGWtype;
    
    /** 文件父目录 **/
    private String             parentPath;
    
    /** 今日关注 **/
    private XwzxListView       unHandleView   = null;
    
    /** 全部新闻 **/
    private XwzxListView       hasHandleView  = null;
    
    /**
     * 搜索 listview
     */
    private XwzxListView       mSearchView    = null;
    
    /** 所在模块的名字 **/
    private String             funName;
    
    /** 所在模块的code **/
    private String             funCode;
    
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
    
    public static XwzxSecondFragment newInstance(String type, String searchStr, String parentPath, String funName)
    {
        XwzxSecondFragment details = new XwzxSecondFragment();
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
        // if (mGWtype.equals(ConstState.XWZX_XWXX) && mUnHandleStatus.equals(ConstState.HASHANDLEDOCLIST))
        // {
        // mSearch_funnel_ll.setVisibility(View.VISIBLE);
        // }
        // else
        // {
        mSearch_funnel_ll.setVisibility(View.GONE);
        // }
        mUnReadStatus = ConstState.ALLDOCLIST;
        unHandleView = (XwzxListView)initOfficalListView(unHandleView, "", "");
        addListView(unHandleView);
        
        animationOver();
        return v;
    }
    
    /**
     * 重载方法
     * 
     * @param mSearchStr
     * @author wang_ling
     */
    @Override
    protected void showFunnel(String mSearchStr)
    {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.search_funnel_layout, null);
        
        ListView lv = (ListView)view.findViewById(R.id.search_funnel_lv);
        List<NewsTypeInfo> typeList = hasHandleView.getNewsTypeList();
        final XwzxSearchAdpter adatper = new XwzxSearchAdpter(getActivity(), typeList, mSearch_funnel_ll.getHeight());
        lv.setAdapter(adatper);
        
        lv.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                // TODO Auto-generated method stub
                mPopupWindow.dismiss();
                
                mSearchResultLayout.setVisibility(View.VISIBLE);
                
                searchDocList("", adatper.getItem(arg2).getId(), true);
                
            }
            
        });
        
        int popViewWith = -1;
        int popViewHeight = -1;
        
        if (typeList != null && typeList.size() >= 5)
        {
            popViewHeight = mSearch_funnel_ll.getHeight() * 5;
        }
        else if (typeList != null && typeList.size() >= 3)
        {
            popViewHeight = mSearch_funnel_ll.getHeight() * typeList.size();
        }
        else
        {
            popViewHeight = mSearch_funnel_ll.getHeight() * typeList.size();
        }
        popViewWith = mSearch_funnel_ll.getWidth() * 2;
        
        // 输入框一样长，高度自适应
        mPopupWindow = new PopupWindow(view, popViewWith, popViewHeight);
        
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAsDropDown(mSearch_funnel_ll, 0, -1);
    }
    
    /**
     * 重载方法
     * 
     * @param keyWords
     * @param typeId
     * @param flag
     * @author wang_ling
     */
    @Override
    protected void searchDocList(String keyWords, String typeId, boolean flag)
    {
        if (flag)
        {
            mSearchRL.setVisibility(View.GONE);
            mSearchResultLayout.setVisibility(View.VISIBLE);
            String typeName = "";
            // if (mGWtype.equals(ConstState.XWZX_XWXX) && mUnHandleStatus.equals(ConstState.HASHANDLEDOCLIST))
            // {
            // List<NewsTypeInfo> typeList = hasHandleView.getNewsTypeList();
            //
            // if (!"".equals(typeId))
            // {
            // for (NewsTypeInfo temp : typeList)
            // {
            // if (temp.getId().equals(typeId))
            // {
            // typeName = temp.getName();
            // }
            // }
            // }
            // }
            // else
            // {
            typeName = "";
            // }
            mSearchResultText.setText(getActivity().getString(R.string.search_key_word,
                "\"" + keyWords + typeName + "\""));
            mSearchResultTextCount.setText(getActivity().getString(R.string.search_key_word_result, "0"));
            
            addListView(getSearchView(keyWords, typeId));
            
            hideSoftKey();
        }
        else
        {
            mSearchEditText.setText("");
            mSearchRL.setVisibility(View.VISIBLE);
            mSearchResultLayout.setVisibility(View.GONE);
            disPlayListView();
        }
    }
    
    private void initData()
    {
        this.mGWtype = getArguments().getString("type");
        
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
     * @author wang_ling
     */
    @Override
    public void animationOver()
    {
        // TODO Auto-generated method stub
        if (unHandleView == null)
        {
            unHandleView = (XwzxListView)initOfficalListView(unHandleView, "", "");
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
        // if (mGWtype.equals(ConstState.XWZX_XWXX))
        // {
        // // 新闻信息
        // mFirstLabelName.setText("今日关注");
        // mSecondLabelName.setText("全部新闻");
        // initReadLineLayout(TWOMODE);
        // }
        // else
        // {
        initReadLineLayout(NOMODE);
        // }
        
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
                unHandleView = (XwzxListView)initOfficalListView(unHandleView, "", "");
                unHandleView.getDocListRefresh("");
            }
            addListView(unHandleView);
        }
        else
        {
            if (hasHandleView == null)
            {
                hasHandleView = (XwzxListView)initOfficalListView(hasHandleView, "", "");
                hasHandleView.getDocListRefresh("");
                hasHandleView.getNewsType();
            }
            addListView(hasHandleView);
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
        mSearchView = (XwzxListView)initOfficalListView(mSearchView, keyWords, typeId);
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
        // TODO Auto-generated method stub
        if (mUnHandleStatus.equals(ConstState.UNHADLEDOCLIST))
        {
            if (unHandleView != null)
            {
                unHandleView.updateListView(id, true);
            }
        }
        else
        {
            if (hasHandleView != null)
            {
                hasHandleView.updateListView(id, true);
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
     * @return
     * @author wang_ling
     */
    // @Override
    // public Object initOfficalListView(Object view, String keyWords)
    // {
    // if (view == null)
    // {
    // view =
    // new XwzxListView(mActivity, keyWords, mGWtype, mUnHandleStatus, mUnReadStatus,
    // CreateDirPath(getParentPath()), mViewLandscapeSlideListener, false, "");
    // ((XwzxListView)view).setResultListen(mResultlisten);
    // }
    // return view;
    // }
    @Override
    public Object initOfficalListView(Object view, String keyWords, String typeId)
    {
        if (view == null)
        {
            view =
                new XwzxListView(mActivity, keyWords, mGWtype, mUnHandleStatus, mUnReadStatus,
                    CreateDirPath(getParentPath()), mViewLandscapeSlideListener, false, typeId, false);
            ((XwzxListView)view).setResultListen(mResultlisten);
        }
        return view;
    }
    
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
        // if (mGWtype.equals(ConstState.XWZX_XWXX))
        // {
        // if (mUnHandleStatus.equals(ConstState.UNHADLEDOCLIST))
        // {
        // return parentPath + ConstState.TODAYCARE_DIRNAME;
        // }
        // else
        // {
        // return parentPath + ConstState.ALLNEWS_DIRNAME;
        // }
        // }
        // else
        // {
        return parentPath;
        // }
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
        switch (id)
        {
        // 今日关注
            case R.id.fragment_first_ll:
            case R.id.label_first_ll:
                mUnHandleStatus = ConstState.UNHADLEDOCLIST;
                mUnReadStatus = ConstState.ALLDOCLIST;
                break;
            // 全部新闻
            case R.id.fragment_second_ll:
            case R.id.label_second_ll:
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
        // if (mGWtype.equals(ConstState.XWZX_XWXX) && mUnHandleStatus.equals(ConstState.HASHANDLEDOCLIST))
        // {
        // mSearch_funnel_ll.setVisibility(View.VISIBLE);
        // }
        // else
        // {
        mSearch_funnel_ll.setVisibility(View.GONE);
        // }
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
        mActivity.finish();
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
