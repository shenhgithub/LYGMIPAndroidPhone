/*
 * File name:  LeaderScheduleFragment.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2013-11-12
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.leaderSchedule;

import android.app.Activity;
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
 * 领导日程
 * 
 * @Description 领导日程
 * 
 * @author wang_ling
 * @Version [版本号, 2013-11-12]
 */
public class LeaderScheduleFragment extends PMIPCustomFragment
{
    /** activity **/
    private Activity       mActivity;
    
    /**
     * 栏目名称
     */
    private String         funName;
    
    /** 所在模块的文件路径 **/
    private String         parentsPath;
    
    /**
     * list view 列表
     */
    private LeaderListView mLeaderListView = null;
    
    /**
     * 搜索 listview
     */
    private LeaderListView mSearchView     = null;
    
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
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static LeaderScheduleFragment newInstance(String funcId, String funName, String parentsPath,
        String searchKeywords)
    {
        LeaderScheduleFragment details = new LeaderScheduleFragment();
        Bundle args = new Bundle();
        args.putString("funcId", funcId);
        args.putString("funName", funName);
        args.putString("parentsPath", parentsPath);
        args.putString("searchKeywords", searchKeywords);
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
        
        mLeaderListView = (LeaderListView)initOfficalListView(mLeaderListView, "", "");
        addListView(mLeaderListView);
        return v;
    }
    
    // 定制整个view，哪些控件显示，哪些不显示
    @Override
    public void customThisFragmentView()
    {
        // 隐藏左侧为title的布局
        // showTitleLayout();
        // mCustom_fragment_rl.setBackgroundResource(R.drawable.frame_s);
        // mLeftImageViewTitle.setBackgroundResource(R.drawable.title_ldrc);
        
        // mUnread_rl.setVisibility(View.INVISIBLE);
        // mRead_rl.setVisibility(View.INVISIBLE);
        initReadLineLayout(0);
    }
    
    @Override
    public Object initOfficalListView(Object view, String keyWords, String typeId)
    {
        if (view == null)
        {
            view =
                new LeaderListView(mActivity, keyWords, mUnReadStatus, CreateDirPath(parentsPath),
                    mViewLandscapeSlideListener);
            ((LeaderListView)view).setResultListen(mResultlisten);
        }
        return view;
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description 初始化数据
     * 
     * @LastModifiedDate：2013-11-13
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        
        this.parentsPath = getArguments().getString("parentsPath");
        
        this.funName = getArguments().getString("funName");
        this.mSearchStr = getArguments().getString("searchKeywords");
        
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
        super.onDestroy();
    }
    
    @Override
    public void onAttach(Activity activity)
    {
        System.out.println("===========================================>========================================");
        super.onAttach(activity);
    }
    
    /**
     * 重载方法 动画效果执行完毕时调用
     * 
     * @author wang_ling
     */
    @Override
    public void animationOver()
    {
        if (mLeaderListView == null)
        {
            mLeaderListView = (LeaderListView)initOfficalListView(mLeaderListView, "", "");
        }
        mLeaderListView.getDocListRefresh("");
    }
    
    // 获取搜索list view
    @Override
    public View getSearchView(String keyWords, String typeId)
    {
        mSearchView = null;
        mSearchView = (LeaderListView)initOfficalListView(mSearchView, keyWords, "");
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
        if (mLeaderListView == null)
        {
            mLeaderListView = (LeaderListView)initOfficalListView(mLeaderListView, "", "");
            mLeaderListView.getDocListRefresh("");
        }
        addListView(mLeaderListView);
    }
    
    /**
     * 处理从pdf阅读页面返回
     * 
     * @author wang_ling
     */
    public void readViewBack(String docId)
    {
        if (mLeaderListView != null)
        {
            mLeaderListView.updateListView(docId, false);
        }
    }
    
    /**
     * 重载方法 点击“返回键”的相应函数
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
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void theThirdFragmentClose()
    {
        // TODO Auto-generated method stub
        // 该布局是二级布局，该方法不许执行
    }
    
    /**
     * 重载方法 该布局关闭
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
    
    // 该方法是用于，对“未读”、“已读”上标签的显示或隐藏
    @Override
    public void setViewResult()
    {
        // TODO Auto-generated method stub
        // 该方法是用于，对“未读”、“已读”上标签的显示或隐藏，该公文不区分“未读”、“已读”，故不执行
    }
    
    // 点击“待办”、“已办”、“未读”、“已读”标签时切换各个view
    @Override
    public void clickRadioButton(int id)
    {
        // TODO Auto-generated method stub
        // 该功能无radiobutton ，故为空
    }
}
