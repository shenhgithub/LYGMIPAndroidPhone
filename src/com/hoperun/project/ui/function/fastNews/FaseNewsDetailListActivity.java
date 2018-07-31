/*
 * File name:  FaseNewsDetailListActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2014-4-2
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.fastNews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.nettv.VideoListItem;
import com.hoperun.mipmanager.model.entityModule.offical.GWDocModule;
import com.hoperun.mipmanager.model.entityModule.schedule.ScheduleInfo;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;
import com.hoperun.project.ui.baseui.baseInterface.IFragmentToMainActivityListen;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2014-4-2]
 */
public class FaseNewsDetailListActivity extends PMIPBaseActivity implements IFragmentToMainActivityListen,
    OnTouchListener
{
    
    private FastNewsSecondFragment mFragment;
    
    /**
     * fragment事务管理
     */
    private FragmentTransaction    fragmentTransaction;
    
    /**
     * fragment管理器
     */
    private FragmentManager        mFragmentManager;
    
    private FrameLayout            mFrameLayout;
    
    /**
     * 重载方法
     * 
     * @param requestType
     * @param objHeader
     * @param objBody
     * @param error
     * @param errorCode
     * @author ren_qiujing
     */
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xwzxdetaillistlayout);
        
        mFrameLayout = (FrameLayout)findViewById(R.id.framge_1);
        
        initData();
    }
    
    public void initData()
    {
        Intent intent = getIntent();
        
        String funcCode = intent.getStringExtra("funcCode");
        String path = intent.getStringExtra("path");
        String funcName = intent.getStringExtra("funcName");
        
        mFragmentManager = getSupportFragmentManager();
        mFragment = null;
        mFragment = FastNewsSecondFragment.newInstance(funcCode, "", path, funcName);
        
        fragmentTransaction = mFragmentManager.beginTransaction();
        
        fragmentTransaction.replace(R.id.framge_1, mFragment);
        
        fragmentTransaction.commit();
        
    }
    
    /**
     * 重载方法
     * 
     * @param index
     * @param mModlue
     * @param path
     * @author ren_qiujing
     */
    @Override
    public void onOfficialFragmentSelected(int index, GWDocModule mModlue, String path)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void onOfficialFragmentCloseSelected()
    {
        // TODO Auto-generated method stub
        finish();
    }
    
    /**
     * 重载方法
     * 
     * @param index
     * @param list_info
     * @param date
     * @author ren_qiujing
     */
    @Override
    public void onSchedulePlanSelected(int index, ScheduleInfo list_info, String date)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void onScheduleTimeFragmentCloseSelected()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void onScheduleChangedListener()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void onSecondFragmentClose()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void onPersonSetBind()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param bitmap
     * @author ren_qiujing
     */
    @Override
    public void onPersonSetHeader(Bitmap bitmap)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param isFull
     * @author ren_qiujing
     */
    @Override
    public void onNetTVShowFullScreen(boolean isFull)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param item
     * @author ren_qiujing
     */
    @Override
    public void onVideoMonitorFragmentSelected(VideoListItem item)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param funId
     * @param funcode
     * @author ren_qiujing
     */
    @Override
    public void onSendGetUnReadCount(String funId, String funcode)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void onCloseHomeViewFragment()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @param event
     * @return
     * @author ren_qiujing
     */
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
}
