/*
 * File name:  XwzxContentShowActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-3-20
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.xwzx;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.manager.components.WaitDialog;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-20]
 */
public class XwzxContentShowActivity extends PMIPBaseActivity implements OnClickListener
{
    private TextView   authorDestv;
    
    private TextView   authorTv;
    
    private TextView   titleTv;
    
    private WebView    mWebView;
    
    private ImageView  mBackBtn;
    
    private String     authorStr;
    
    private String     contentStr;
    
    private String     titleStr;
    
    /**
     * 等待框
     */
    private WaitDialog waitDialog;
    
    /** 是否是url **/
    private boolean    isURL;
    
    private String     url;
    
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
        this.setContentView(R.layout.xwzx_xwxxdetail);
        initIntentData();
        initView();
        initData();
    }
    
    @Deprecated
    private void enlargeImg()
    {
        contentStr = contentStr.replaceAll("<IMG", "<IMG width='95%'");
        contentStr = contentStr.replaceAll("<img", "<img width='95%'");
    }
    
    private void initIntentData()
    {
        Intent intent = this.getIntent();
        isURL = intent.getBooleanExtra("ISURL", false);
        url = intent.getStringExtra("url");
        authorStr = intent.getStringExtra("author");
        contentStr = intent.getStringExtra("content");
        titleStr = intent.getStringExtra("title");
        enlargeImg();
        contentStr = "<div style=\"font-family:'黑体-简';font-size:50px;line-height:65px\">" + contentStr + "</div>";
        // contentStr = "<img style=\"width:'90%'\">" + contentStr + "</img>";
        // contentStr =
        // "<div style=\"font-family:'黑体-简';font-size:50px;line-height:65px\"><img style=\"width:'90%'\">"
        // + contentStr + "</div>";
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private void initView()
    {
        mBackBtn = (ImageView)findViewById(R.id.office_back);
        mBackBtn.setOnClickListener(this);
        authorDestv = (TextView)findViewById(R.id.office_title);
        authorTv = (TextView)findViewById(R.id.author_tv);
        titleTv = (TextView)findViewById(R.id.fragment_title);
        mWebView = (WebView)findViewById(R.id.mywebview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        // ---------------------------------------------------------------
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        // 提高渲染优先级
        mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        mWebView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);// 默认缩放模式
        // mWebView.setInitialScale(40);// 为25%，最小缩放等级
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        
        // if (!isURL)
        // {
        // mWebView.getSettings().setDefaultFontSize(50);
        // }
        
        // ---------------------------------------------------------------
        // LayoutAlgorithm是一个枚举用来控制页面的布局，有三个类型：
        // 1.NARROW_COLUMNS：可能的话使所有列的宽度不超过屏幕宽度
        // 2.NORMAL：正常显示不做任何渲染
        // 3.SINGLE_COLUMN：把所有内容放大webview等宽的一列中
        mWebView.setWebChromeClient(new WebChromeClient()
        {
            
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                // TODO Auto-generated method stub
                if (newProgress == 100)
                {
                    if (waitDialog != null && waitDialog.isShowing())
                    {
                        waitDialog.dismiss();
                    }
                }
                HitTestResult hitTestResult = mWebView.getHitTestResult();
                // if (hitTestResult.equals(hitTestResult.IMAGE_TYPE))
                // {
                //
                // }
                view.requestFocus();
                super.onProgressChanged(view, newProgress);
            }
            
        });
        if (isURL)
        {
            mWebView.setWebViewClient(new WebViewClient()
            {
                
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    // TODO Auto-generated method stub
                    mWebView.loadUrl(url);
                    // mCurrentUrl = url;
                    return true;
                }
                
            });
        }
        waitDialog = WaitDialog.newInstance();
        waitDialog.show(getSupportFragmentManager());
    }
    
    private void initData()
    {
        
        if (isURL)
        {
            authorDestv.setVisibility(View.GONE);
            authorTv.setVisibility(View.GONE);
            mWebView.loadUrl(url);
            if (null != titleStr && !"".equals(titleStr))
            {
                titleTv.setVisibility(View.VISIBLE);
                titleTv.setText(titleStr);
            }
        }
        else
        {
            // authorTv.setText(authorStr);
            authorDestv.setVisibility(View.GONE);
            authorTv.setVisibility(View.GONE);
            if (null != titleStr && !"".equals(titleStr))
            {
                titleTv.setVisibility(View.VISIBLE);
                titleTv.setText(titleStr);
            }
            mWebView.loadDataWithBaseURL(null, contentStr, "text/html", "UTF-8", "");
        }
        
    }
    
    /**
     * 重载方法
     * 
     * @param arg0
     * @author wang_ling
     */
    @Override
    public void onClick(View arg0)
    {
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        switch (arg0.getId())
        {
            case R.id.office_back:
                this.finish();
                break;
        }
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
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        // TODO Auto-generated method stub
        
    }
    
}
