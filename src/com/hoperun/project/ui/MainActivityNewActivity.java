/*
 * File name:  MainActivityNewActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-3-24
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.hoperun.manager.components.CustomDialog;
import com.hoperun.manager.components.CustomDialogListener;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.utils.StringUtils;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.Login.QianDaoActivity;
import com.hoperun.project.ui.function.FunctionActivity;
import com.hoperun.project.ui.newMsg.MsgCenterActivity;
import com.hoperun.project.ui.warehouse.MyApps;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-24]
 */
public class MainActivityNewActivity extends TabActivity implements OnCheckedChangeListener
{
    /** 切换按钮group **/
    private RadioGroup      mainTab;
    
    /** button **/
    private RadioButton     button;
    
    /** activity切换 **/
    private TabHost         tabHost;
    
    /** 跳转到功能模块intent **/
    private Intent          ifunction;
    
    /** 跳转到签到模块intent **/
    private Intent          iSign;
    
    /** 跳转到我的应用intent **/
    private Intent          iMyApp;
    
    /** 跳转到最新消息intent **/
    private Intent          iNewMsg;
    
    /**
     * fragment管理器
     */
    private FragmentManager mFragmentManager;
    
    private int             mCurrentIndex;
    
    /**
     * 重载方法
     * 
     * @param savedInstanceState
     * @author wang_ling
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main1);
        
        initData();
        initView();
        initTab();
    }
    
    /**
     * 初始化数据
     */
    public void initData()
    {
        
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        GlobalState.getInstance().setmScreen_With(dm.widthPixels);
        GlobalState.getInstance().setmScreen_Height(dm.heightPixels);
        GlobalState.getInstance().setmDensityDpi(dm.densityDpi);
        LogUtil.i("", "*********initData getmScreen_With=" + GlobalState.getInstance().getmScreen_With());
        LogUtil.i("", "*********initData getmScreen_Height=" + GlobalState.getInstance().getmScreen_Height());
        LogUtil.i("", "*********initData getmDensityDpi=" + GlobalState.getInstance().getmDensityDpi());
    }
    
    private void initTab()
    {
        Intent intent = this.getIntent();
        String uriStr = intent.getDataString();
        if (StringUtils.isNull(uriStr))
        {
            button = (RadioButton)mainTab.getChildAt(0);
            button.setChecked(true);
        }
        else
        {
            button = (RadioButton)mainTab.getChildAt(4);
            button.setChecked(true);
            tabHost.setCurrentTab(2);
        }
    }
    
    private void initView()
    {
        
        mainTab = (RadioGroup)findViewById(R.id.main_tab);
        mainTab.setOnCheckedChangeListener(this);
        tabHost = getTabHost();
        
        ifunction = new Intent(this, FunctionActivity.class);
        tabHost.addTab(tabHost.newTabSpec("ifunction")
            .setIndicator(getResources().getString(R.string.main_function),
                getResources().getDrawable(R.drawable.function_selector))
            .setContent(ifunction));
        
        iSign = new Intent(this, QianDaoActivity.class);
        tabHost.addTab(tabHost.newTabSpec("iSign")
            .setIndicator(getResources().getString(R.string.main_sign),
                getResources().getDrawable(R.drawable.function_selector))
            .setContent(iSign));
        
        iMyApp = new Intent(this, MyApps.class);
        tabHost.addTab(tabHost.newTabSpec("iMyApp")
            .setIndicator(getResources().getString(R.string.main_myapp),
                getResources().getDrawable(R.drawable.function_selector))
            .setContent(iMyApp));
        
        iNewMsg = new Intent(this, MsgCenterActivity.class);// NewMsgActivity
        tabHost.addTab(tabHost.newTabSpec("iNewMsg")
            .setIndicator(getResources().getString(R.string.main_myapp),
                getResources().getDrawable(R.drawable.function_selector))
            .setContent(iNewMsg));
    }
    
    /**
     * 重载方法
     * 
     * @param group
     * @param checkedId
     * @author wang_ling
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        switch (checkedId)
        {
            case R.id.radio_button0:
                tabHost.setCurrentTab(0);
                break;
            case R.id.radio_button1:
                tabHost.setCurrentTab(1);
                break;
            case R.id.radio_button2:
                tabHost.setCurrentTab(2);
                break;
            case R.id.radio_button3:
                tabHost.setCurrentTab(3);
                
                break;
        }
    }
    
    private CustomDialog dialog;
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            
            dialog =
                CustomDialog.newInstance(this.getResources().getString(R.string.main_exit_app), this.getResources()
                    .getString(R.string.Login_Cancel), this.getResources().getString(R.string.Login_Confirm));
            // dialog.show(MainActivityNewActivity.this.getFragmentManager(), "ExitDialog");
            // dialog.show(this.getFragmentManager(), "ExitDialog");
            dialog.setRightListener(new CustomDialogListener()
            {
                
                @Override
                public void Onclick()
                {
                    dialog.dismiss();
                    GlobalState.getInstance().exitApplication();
                }
            });
            
            dialog.setLeftListener(new CustomDialogListener()
            {
                
                @Override
                public void Onclick()
                {
                    dialog.dismiss();
                    
                }
            });
            
        }
        return true;
    }
    
}
