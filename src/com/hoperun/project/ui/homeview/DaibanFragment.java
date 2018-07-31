/*
 * File name:  DaBanFragment.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2014-2-24
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.homeview;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mipmanager.model.entityModule.Login.LoginModule;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.project.ui.baseui.fragment.PMIPCustomFragment;
import com.hoperun.project.ui.offical.OfficalListView;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2014-2-24]
 */
public class DaibanFragment extends PMIPCustomFragment
{
    Activity        mActivity;
    
    OfficalListView mDaibanOfficailListview;
    
    OfficalListView mSearchOfficailListView;
    
    LoginModule     module;
    
    /**
     * 请求的模块
     */
    private String  mFunId;
    
    /** 请求列表的类型 ， 收文 **/
    private String  mType;
    
    /** 文件父目录 **/
    private String  parentPath;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
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
    public static DaibanFragment newInstance(String funId, String type, String searchStr, String parentPath)
    {
        DaibanFragment details = new DaibanFragment();
        Bundle args = new Bundle();
        args.putString("funid", funId);
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
        mUnReadStatus = ConstState.ALLDOCLIST;
        
        mDaibanOfficailListview = (OfficalListView)initOfficalListView(mDaibanOfficailListview, "", "");
        addListView(mDaibanOfficailListview);
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
        this.mFunId = getArguments().getString("funid");
        
        this.mType = getArguments().getString("type");
        
        this.parentPath = getArguments().getString("parentPath");
        
        this.mSearchStr = getArguments().getString("searchStr");
        
        LogUtil.d("", "parentPath=" + parentPath);
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
    public void animationOver()
    {
        // TODO Auto-generated method stub
        if (mDaibanOfficailListview == null)
        {
            mDaibanOfficailListview = (OfficalListView)initOfficalListView(mDaibanOfficailListview, "", "");
            
        }
        mDaibanOfficailListview.getDocListRefresh("");
    }
    
    @Override
    public void customThisFragmentView()
    {
        // TODO Auto-generated method stub
        mTitle_tv.setText("待办事宜");
        mFirstLL.setVisibility(View.GONE);
        mSecondLL.setVisibility(View.GONE);
        mThirdLL.setVisibility(View.GONE);
        
        mSearchButtonLayout.setVisibility(View.GONE);
    }
    
    @Override
    public void disPlayListView()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public View getSearchView(String keyWords, String typeId)
    {
        // TODO Auto-generated method stub
        mSearchOfficailListView = null;
        mSearchOfficailListView = (OfficalListView)initOfficalListView(mSearchOfficailListView, mSearchStr, typeId);
        return mSearchOfficailListView;
    }
    
    @Override
    public void readViewBack(String id)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void setViewResult()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public Object initOfficalListView(Object view, String keyWords, String typeId)
    {
        // TODO Auto-generated method stub
        
        if (view == null)
        {
            view =
                new OfficalListView(mActivity, keyWords, mType, mUnHandleStatus, mUnReadStatus,
                    CreateDirPath(getParentPath()), mViewLandscapeSlideListener, false);
            ((OfficalListView)view).setResultListen(mResultlisten);
        }
        return view;
    }
    
    @Override
    public void clickRadioButton(int id)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void closeThisFragment()
    {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        if (mFragmentTMainActivityListener != null)
            mFragmentTMainActivityListener.onCloseHomeViewFragment();
    }
    
    @Override
    public void theThirdFragmentClose()
    {
        // TODO Auto-generated method stub
        
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
        String mDirPath = "";
        if (mFunId.equals(ConstState.HOME_DB))
        {
            mDirPath = parentPath + ConstState.GWLZ + "/" + ConstState.UNHANDLE_DIRNAME;
        }
        
        File file = new File(mDirPath);
        if (!file.exists())
        {
            if (file.mkdirs())
            {
                return mDirPath;
            }
            else
            {
                return "";
            }
        }
        
        return mDirPath;
    }
}
