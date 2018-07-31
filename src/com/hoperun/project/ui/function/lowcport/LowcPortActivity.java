/*
 * File name:  DevSuperviseActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-3-26
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.lowcport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * 低碳港口
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-26]
 */
public class LowcPortActivity extends PMIPBaseActivity implements OnClickListener
{
    
    /** 是否离线 true为离线，false为在线 **/
    private boolean          offLine                  = false;
    
    /**
     * 该类栏目的标题
     */
    private TextView         tv_title;
    
    private WebView          mWebView;
    
    /** 等待框布局 **/
    private RelativeLayout   loadLayout;
    
    /** 等待图片 **/
    private ImageView        loadImageView;
    
    /** 所在模块的名字 **/
    private String           funName;
    
    /** 所在模块的code **/
    private String           funCode;
    
    /**
     * 正在加载过程中
     */
    private boolean          isLoading                = false;
    
    /**
     * 该页面是否可以关闭该主页面
     */
    private boolean          isCanClose               = true;
    
    /**
     * 获取新闻中心一级列表的task实例
     */
    private NetTask          mGetDocModuleHttpCreator = null;
    
    /**
     * 返回按钮
     */
    protected ImageView      mFragmentBack;
    
    /** 查询回来的list数据 **/
    private MetaResponseBody responseBuzBody;
    
    /**
     * 重载方法
     * 
     * @param savedInstanceState
     * @author wang_ling
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devicesupervise_main);
        initIntentData();
        initView();
        
        initData();
        getDocModuleList();
    }
    
    private void initIntentData()
    {
        Intent intent = this.getIntent();
        
        this.funCode = intent.getStringExtra("funcCode");
        
        this.funName = intent.getStringExtra("funName");
        
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private void initView()
    {
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        
        tv_title = (TextView)findViewById(R.id.office_title);
        
        mFragmentBack = (ImageView)findViewById(R.id.office_back);
        mWebView = (WebView)findViewById(R.id.mywebview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        
        // ---------------------------------------------------------------
        // mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        // // 提高渲染优先级
        // mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        // mWebView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);// 默认缩放模式
        // mWebView.setInitialScale(100);// 为25%，最小缩放等级
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        
        mWebView.setWebChromeClient(new WebChromeClient()
        {
            
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                // TODO Auto-generated method stub
                if (newProgress == 100)
                {
                    // if (waitDialog != null && waitDialog.isShowing())
                    // {
                    // waitDialog.dismiss();
                    // }
                    closeProgressDialog();
                }
                
                super.onProgressChanged(view, newProgress);
            }
            
        });
        
    }
    
    private void initData()
    {
        offLine = GlobalState.getInstance().getOfflineLogin();
        tv_title.setText(funName);
        mFragmentBack.setOnClickListener(this);
    }
    
    private void getDocModuleList()
    {
        
        mGetDocModuleHttpCreator = new HttpNetFactoryCreator(RequestTypeConstants.GETDOCMODULELIST).create();
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("listId", "");
            if (funCode.equals(ConstState.SBYX))
            {
                NetRequestController.getDevSuperviseList(mGetDocModuleHttpCreator,
                    mHandler,
                    RequestTypeConstants.GETDOCMODULELIST,
                    body);
            }
            else
            {
                NetRequestController.getEngSuperviseList(mGetDocModuleHttpCreator,
                    mHandler,
                    RequestTypeConstants.GETDOCMODULELIST,
                    body);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 重载方法
     * 
     * @param requestType
     * @param objHeader
     * @param objBody
     * @param error
     * @param errorCode
     * @author wang_ling
     */
    @SuppressWarnings({"unchecked", "unused"})
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETDOCMODULELIST:
                    
                    if (objBody != null)
                    {
                        responseBuzBody = (MetaResponseBody)objBody;
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        
                        // List<HashMap<String, Object>> bizData =
                        // (List<HashMap<String, Object>>)ret.get(0).get("bizdata");
                        List<HashMap<String, Object>> wetitem =
                            (List<HashMap<String, Object>>)ret.get(0).get("webitems");
                        if (wetitem != null)
                        {
                            
                            if (null == wetitem)
                            {
                                wetitem = new ArrayList<HashMap<String, Object>>();
                            }
                            
                            String url = (String)wetitem.get(0).get("url");
                            if (url != null && !"".equals(url))
                            {
                                mWebView.loadUrl(url);
                            }
                            else
                            {
                                closeProgressDialog();
                            }
                        }
                        else
                        {
                            closeProgressDialog();
                        }
                        
                    }
                    else
                    {
                        closeProgressDialog();
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    
                    break;
                
                default:
                    break;
            }
            
        }
        else
        {
            closeProgressDialog();
            if (!(errorCode == ConstState.CANCEL_THREAD))
            {
                Toast.makeText(this, "请求服务器数据失败！", Toast.LENGTH_SHORT).show();
            }
        }
        
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @author wang_ling
     */
    @Override
    public void onClick(View v)
    {
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        
        switch (v.getId())
        {
        
            case R.id.office_back:
                this.finish();
                break;
            default:
                break;
        }
        
    }
    
    /**
     * 
     * 显示等待框
     * 
     * @Description 显示等待框
     * 
     * @LastModifiedDate：2013-10-22
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void showProgressDialog()
    {
        loadLayout.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable)loadImageView.getBackground();
        animationDrawable.start();
        isLoading = true;
    }
    
    private void closeNetRequest(int requestType)
    {
        NetRequestController.stopCurrentNetTask(mGetDocModuleHttpCreator);
    }
    
    /**
     * 
     * 关闭等待框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-22
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void closeProgressDialog()
    {
        loadLayout.setVisibility(View.GONE);
        isLoading = false;
    }
}
