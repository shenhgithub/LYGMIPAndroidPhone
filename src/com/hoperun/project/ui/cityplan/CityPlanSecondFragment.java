/*
 * File name:  OfficialSecondFragment.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-11-5
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.cityplan;

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
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-11-5]
 */
public class CityPlanSecondFragment extends PMIPCustomFragment
{
    /** activity **/
    private Activity         mActivity;
    
    /** 功能ID **/
    private String           funcId;
    
    /** 文件父目录 **/
    private String           parentPath;
    
    private CityPlanListView nettvView   = null;
    
    /**
     * 搜索 listview
     */
    private CityPlanListView mSearchView = null;
    
    /** 所在模块的名字 **/
    private String           funName;
    
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
    public static CityPlanSecondFragment newInstance(String type, String searchStr, String parentPath, String funName,
        String funcid)
    {
        CityPlanSecondFragment details = new CityPlanSecondFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("searchStr", searchStr);
        args.putString("parentPath", parentPath);
        args.putString("funName", funName);
        args.putString("funcId", funcid);
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
        nettvView = (CityPlanListView)initOfficalListView(nettvView, "", "");
        addListView(nettvView);
        
        return v;
    }
    
    // 定制整个view，哪些控件显示，哪些不显示
    @Override
    public void customThisFragmentView()
    {
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
        this.parentPath = getArguments().getString("parentPath");
        
        this.mSearchStr = getArguments().getString("searchStr");
        this.funName = getArguments().getString("funName");
        this.funcId = getArguments().getString("funcId");
        mTitle_tv.setText(funName);
        LogUtil.d("", "parentPath=" + parentPath);
        mSearchButtonLayout.setVisibility(View.GONE);
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
    }
    
    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
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
        if (nettvView == null)
        {
            nettvView = (CityPlanListView)initOfficalListView(nettvView, "", "");
        }
        nettvView.getDocListRefresh("");
        
    }
    
    // 打开文件后的监听返回，用户处理列表的移除或加载
    @Override
    public void readViewBack(String docId)
    {
        if (nettvView != null)
        {
            nettvView.updateListView(docId, true);
        }
    }
    
    // 点击“待办”、“已办”、“未读”、“已读”标签时切换各个view
    @Override
    public void clickRadioButton(int id)
    {
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
                new CityPlanListView(mActivity, keyWords, "", "", "", CreateDirPath(getParentPath()),
                    mViewLandscapeSlideListener, false, funcId);
            ((CityPlanListView)view).setResultListen(mResultlisten);
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
        mFirst_label_number.setText("");
        mSecond_label_number.setText("");
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
        // if (mUnHandleStatus.equals(ConstState.UNHADLEDOCLIST))
        // {
        // // if (mUnReadStatus.equals(ConstState.UNReadDOCLIST))
        // // {
        // // if (unHandleUnReadView == null)
        // // {
        // // unHandleUnReadView = (OfficalListView)initOfficalListView(unHandleUnReadView, "");
        // // unHandleUnReadView.getDocListRefresh("");
        // // }
        // // addListView(unHandleUnReadView);
        // // }
        // // else
        // // {
        // // if (unHandleHasReadView == null)
        // // {
        // // unHandleHasReadView = (OfficalListView)initOfficalListView(unHandleHasReadView, "");
        // // unHandleHasReadView.getDocListRefresh("");
        // // }
        // // addListView(unHandleHasReadView);
        // // }
        // if (unHandleView == null)
        // {
        // unHandleView = (OfficalListView)initOfficalListView(unHandleView, "");
        // unHandleView.getDocListRefresh("");
        // }
        // addListView(unHandleView);
        // }
        // else
        // {
        // if (hasHandleView == null)
        // {
        // hasHandleView = (OfficalListView)initOfficalListView(hasHandleView, "");
        // hasHandleView.getDocListRefresh("");
        // }
        // addListView(hasHandleView);
        // }
        
        if (nettvView == null)
        {
            nettvView = (CityPlanListView)initOfficalListView(nettvView, "", "");
            nettvView.getDocListRefresh("");
        }
        addListView(nettvView);
        showReadViewFlag();
        hideSoftKey();
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
        
        return parentPath;
    }
    
    // 获取搜索view
    @Override
    public View getSearchView(String keyWords, String typeId)
    {
        mSearchView = null;
        mSearchView = (CityPlanListView)initOfficalListView(mSearchView, keyWords, "");
        mSearchView.getDocListRefresh("");
        mSearchView.setSearchResultListen(new onSearchResultListen()
        {
            @Override
            public void setOnSearchResultListen(int count)
            {
                mSearchResultTextCount.setText(getActivity().getString(R.string.search_key_word_result, "" + count));
            }
        });
        return mSearchView;
    }
    
    // 监听返回键
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
    
    @Override
    public void theThirdFragmentClose()
    {
        
    }
    
    // 关闭本级fragment
    @Override
    public void closeThisFragment()
    {
        if (mFragmentTMainActivityListener != null)
            mFragmentTMainActivityListener.onOfficialFragmentCloseSelected();
    }
}
