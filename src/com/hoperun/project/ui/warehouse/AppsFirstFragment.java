/*
 * File name:  AppsFirstFragment.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  li_miao
 * Last modified date:  2014-3-24
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.warehouse;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.fragment.PMIPBaseSecondFragment;

/**
 * 应用仓库一级页面
 * 
 * @Description<功能详细描述>
 * 
 * @author li_miao
 * @Version [版本号, 2014-3-24]
 */
public class AppsFirstFragment extends PMIPBaseSecondFragment implements OnItemClickListener, OnClickListener
{
    /** 应用上下文 **/
    private Activity mActivity;
    
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
    public static AppsFirstFragment newInstance(String funcId, String funName, String parentsPath)
    {
        AppsFirstFragment details = new AppsFirstFragment();
        Bundle args = new Bundle();
        args.putString("funcId", funcId);
        args.putString("funName", funName);
        args.putString("parentsPath", parentsPath);
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
        View v = inflater.inflate(R.layout.office_doc_main, null);
        // loadLayout = (RelativeLayout)v.findViewById(R.id.load_layout);
        // loadImageView = (ImageView)v.findViewById(R.id.waitdialog_img);
        //
        // tv_title = (TextView)v.findViewById(R.id.office_title);
        //
        // mLvMain = (DropDownRefreshListView)v.findViewById(R.id.office_lv);
        //
        // mLvMain.setOnTouchListener(mViewLandscapeSlideListener);
        // mFragmentBack = (ImageView)v.findViewById(R.id.office_back);
        // mFragmentBack.setOnClickListener(this);
        // initData();
        return v;
    }
    
    /**
     * 重载方法
     * 
     * @param keyId
     * @return
     * @author li_miao
     */
    @Override
    public boolean onKeyDown(int keyId)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * 重载方法
     * 
     * @author li_miao
     */
    @Override
    public void animationOver()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @author li_miao
     */
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @author li_miao
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author li_miao
     */
    @Override
    public void closeThisFragment()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author li_miao
     */
    @Override
    public void theThirdFragmentClose()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param requestType
     * @param objHeader
     * @param objBody
     * @param error
     * @param errorCode
     * @author li_miao
     */
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        // TODO Auto-generated method stub
        
    }
    
}
