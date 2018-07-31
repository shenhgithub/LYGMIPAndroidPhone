/*
 * File name:  UpdDialogActivity.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2013-9-27
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.Login;

import java.io.File;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.mip.utils.OsUtils;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.MainActivityNewActivity;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * 版本升级
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2013-9-27]
 */
public class UpdDialogActivity extends PMIPBaseActivity implements OnClickListener
{
    
    /**
     * 进度条与通知ui刷新的handler和msg常量
     */
    private ProgressBar   mProgress;
    
    /** 显示百分比 **/
    private TextView      percentTv;
    
    /**
     * 是否停止下载
     */
    public static boolean interceptFlag = false;
    
    // /**
    // * 进度条对话框
    // */
    // private Dialog progressDialog;
    /** 标题 **/
    private TextView      titleTv;
    
    /** 显示信息 **/
    private TextView      messageTV;
    
    /** 升级按钮 **/
    private Button        leftButton;
    
    /** 下次升级 **/
    private Button        rightButton;
    
    /**
     * 中间的按钮
     */
    private Button        midButton;
    
    /**
     * 两个按钮的布局
     */
    private LinearLayout  twobuttonLinearLayout;
    
    /**
     * 一个按钮的布局
     */
    private LinearLayout  oneButtonLinearLayout;
    
    private NetTask       mGetApk;
    
    /**
     * 更新策略
     */
    
    private String        appupdpolicy;
    
    /** 是否从登录提示过来 **/
    private boolean       fromLogin;
    
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        switch (requestType)
        {
            case RequestTypeConstants.APKDOWNLOAD_REQUEST:
                if (error)
                {
                    // if (!interceptFlag)
                    // {
                    installApk();
                    // }
                    this.finish();
                }
                break;
            // 设置进度条
            case RequestTypeConstants.PROGRESS_BAR:
                if (error)
                {
                    if (objBody != null)
                    {
                        int progress = (Integer)objBody;
                        mProgress.setProgress(progress);
                        percentTv.setText(progress + "%");
                    }
                }
                break;
            default:
                break;
        }
        
    }
    
    /**
     * 重载方法
     * 
     * @param savedInstanceState savedInstanceState
     * @author wang_ling
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.apkcustomdialog);
        appupdpolicy = this.getIntent().getStringExtra("Appupdpolicy");
        fromLogin = this.getIntent().getBooleanExtra("Login", false);
        initView();
    }
    
    /**
     * 
     * 初始化视图
     * 
     * @Description 初始化视图
     * 
     * @LastModifiedDate：2013-10-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initView()
    {
        twobuttonLinearLayout = (LinearLayout)findViewById(R.id.bottom_2btn_layout);
        oneButtonLinearLayout = (LinearLayout)findViewById(R.id.bottom_1btn_layout);
        titleTv = (TextView)findViewById(R.id.title);
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText(this.getResources().getString(R.string.UpdDialog_title));
        messageTV = (TextView)findViewById(R.id.dialog_message);
        leftButton = (Button)findViewById(R.id.left_btn);
        rightButton = (Button)findViewById(R.id.right_btn);
        midButton = (Button)findViewById(R.id.mid_btn);
        if ("1".equals(appupdpolicy))
        {
            twobuttonLinearLayout.setVisibility(View.GONE);
            oneButtonLinearLayout.setVisibility(View.VISIBLE);
        }
        else if ("0".equals(appupdpolicy))
        {
            twobuttonLinearLayout.setVisibility(View.VISIBLE);
            oneButtonLinearLayout.setVisibility(View.GONE);
        }
        // messageTV.setTextColor(R.color.black);
        if ("0".equals(appupdpolicy))
        {
            messageTV.setText(getResources().getString(R.string.UpdDialog_checkVersion)
                + GlobalState.getInstance().getNewVersion() + "\n"
                + getResources().getString(R.string.UpdDialog_updateNow));
            // messageTV.setText(GlobalState.getInstance().getUpDateLog());
        }
        else if ("1".equals(appupdpolicy))
        {
            messageTV.setText(getResources().getString(R.string.UpdDialog_new_version1));
        }
        // leftButton.setTextColor(R.color.black);
        leftButton.setText(getResources().getString(R.string.UpdDialog_update));
        // rightButton.setTextColor(R.color.black);
        rightButton.setText(getResources().getString(R.string.UpdDialog_next_update));
        midButton.setText(getResources().getString(R.string.UpdDialog_next_update));
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        midButton.setOnClickListener(this);
        if (OsUtils.getSDKVersion() > 11)
        {
            this.setFinishOnTouchOutside(false);
        }
    }
    
    /**
     * 重载方法
     * 
     * @param v 视图
     * @author wang_ling
     */
    @Override
    public void onClick(View v)
    {
        // 点击时间间隔太短则不触发
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        switch (v.getId())
        {
            case R.id.left_btn:
                // Intent intent = new Intent(UpdDialogActivity.this, DownLoadNewAppSer.class);
                // startService(intent);
                
                if (fromLogin)
                {
                    Intent intent = new Intent();
                    intent.setClass(UpdDialogActivity.this, MainActivityNewActivity.class);
                    startActivity(intent);
                }
                this.finish();
                
                break;
            case R.id.right_btn:
                
                showDownloadDialog();
                break;
            case R.id.mid_btn:
                showDownloadDialog();
                break;
            default:
                break;
        }
        
    }
    
    /**
     * 
     * 进度条对话框
     * 
     * @Description 进度条对话框
     * 
     * @LastModifiedDate：2013-10-10
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void showDownloadDialog()
    {
        this.setContentView(R.layout.download_progressbar_dialog);
        // LayoutInflater factory = LayoutInflater.from(this);
        // final View dialogviView = factory.inflate(R.layout.download_progressbar_dialog, null);
        
        mProgress = (ProgressBar)this.findViewById(R.id.download_file_progressBar);
        percentTv = (TextView)this.findViewById(R.id.percent);
        Button cancelBtn = (Button)this.findViewById(R.id.cancel);
        // progressDialog = new Dialog(this, R.style.DialogStyle);
        // progressDialog.setContentView(dialogviView);
        // progressDialog.setCancelable(false);
        
        // Window dialogWindow = progressDialog.getWindow();
        // WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // dialogWindow.setGravity(Gravity.CENTER);// Gravity.LEFT | Gravity.TOP
        // dialogWindow.setAttributes(lp);
        
        cancelBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // progressDialog.dismiss();
                
                setInterceptFlag(true);
                // NetRequestController.stopCurrentRequest(RequestTypeConstants.APKDOWNLOAD_REQUEST);
                mGetApk.shutDownExecute();
                
                UpdDialogActivity.this.finish();
            }
        });
        // progressDialog.show();// 显示对话框
        mGetApk = new HttpNetFactoryCreator(RequestTypeConstants.APKDOWNLOAD_REQUEST).create();
        NetRequestController.sendDownloadApk(mGetApk, mHandler, RequestTypeConstants.APKDOWNLOAD_REQUEST, null);
        
    }
    
    /**
     * 
     * 对是否停止赋值
     * 
     * @Description 对是否停止赋值
     * 
     * @param flag 是否停止赋值
     * @LastModifiedDate：2013-10-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void setInterceptFlag(Boolean flag)
    {
        interceptFlag = flag;
    }
    
    /**
     * 安装apk
     * 
     */
    private void installApk()
    {
        File apkfile = new File(ConstState.NewAPKPath());// GlobalState.getInstance().getMIP_BASE_UPDATE_VERSION_ADDRESS()
        if (!apkfile.exists())
        {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        this.startActivity(i);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            return true;
        }
        return super.onKeyDown(keyCode, event);
        
    }
    
}
