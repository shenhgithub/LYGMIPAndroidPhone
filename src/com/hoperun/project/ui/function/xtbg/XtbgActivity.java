/*
 * File name:  XwzxContentShowActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-3-20
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.xtbg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.components.WaitDialog;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.mip.utils.OpenFilesTool;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * 协同办公
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-20]
 */
public class XtbgActivity extends PMIPBaseActivity implements OnClickListener
{
    
    private TextView   titleTv;
    
    private WebView    mWebView;
    
    private ImageView  mBackBtn;
    
    private String     titleStr;
    
    /**
     * 等待框
     */
    private WaitDialog waitDialog;
    
    /** 是否是url **/
    private boolean    isURL;
    
    private String     url;
    
    private String     dirPath;
    
    private boolean    isDownloading = false;
    
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
        this.setContentView(R.layout.xtbg_layout);
        
        dirPath = ConstState.MIP_ROOT_DIR + "/tmp/";
        File file = new File(dirPath);
        if (file != null && !file.exists())
        {
            file.mkdirs();
        }
        
        initIntentData();
        initView();
        initData();
    }
    
    private void initIntentData()
    {
        Intent intent = this.getIntent();
        isURL = intent.getBooleanExtra("ISURL", false);
        url = intent.getStringExtra("url");
        titleStr = intent.getStringExtra("title");
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private void initView()
    {
        mBackBtn = (ImageView)findViewById(R.id.office_back);
        mBackBtn.setOnClickListener(this);
        titleTv = (TextView)findViewById(R.id.fragment_title);
        mWebView = (WebView)findViewById(R.id.mywebview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        // ---------------------------------------------------------------
        // mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        // // 提高渲染优先级
        // mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        // mWebView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);// 默认缩放模式
        // // mWebView.setInitialScale(20);// 为25%，最小缩放等级
        // mWebView.getSettings().setBuiltInZoomControls(true);
        // mWebView.getSettings().setUseWideViewPort(true);
        // mWebView.getSettings().setLoadWithOverviewMode(true);
        
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
                view.requestFocus();
                super.onProgressChanged(view, newProgress);
            }
            
        });
        mWebView.setWebViewClient(new WebViewClient()
        {
            
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                // TODO Auto-generated method stub
                if (url.startsWith("tel:"))
                {
                    call(url);
                }
                else
                {
                    mWebView.loadUrl(url);
                }
                // mCurrentUrl = url;
                
                return true;
            }
            
        });
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        waitDialog = WaitDialog.newInstance();
        waitDialog.show(getSupportFragmentManager());
    }
    
    public static boolean checkEndsWithInStringArray(String checkItsEnd, String[] fileEndings)
    {
        for (String aEnd : fileEndings)
        {
            if (checkItsEnd.endsWith(aEnd))
                return true;
        }
        return false;
    }
    
    private void openFile(String filePath)
    {
        File currentPath = new File(filePath);
        if (currentPath != null && currentPath.isFile())
        {
            String fileName = currentPath.toString();
            Intent intent;
            if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingImage)))
            {
                intent = OpenFilesTool.getImageFileIntent(currentPath);
                startActivity(intent);
            }
            else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingWebText)))
            {
                intent = OpenFilesTool.getHtmlFileIntent(currentPath);
                startActivity(intent);
            }
            else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingPackage)))
            {
                intent = OpenFilesTool.getApkFileIntent(currentPath);
                startActivity(intent);
                
            }
            else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingAudio)))
            {
                intent = OpenFilesTool.getAudioFileIntent(currentPath);
                startActivity(intent);
            }
            else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingVideo)))
            {
                intent = OpenFilesTool.getVideoFileIntent(currentPath);
                startActivity(intent);
            }
            else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingText)))
            {
                intent = OpenFilesTool.getTextFileIntent(currentPath);
                startActivity(intent);
            }
            else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingPdf)))
            {
                intent = OpenFilesTool.getPdfFileIntent(currentPath);
                startActivity(intent);
            }
            else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingWord)))
            {
                intent = OpenFilesTool.getWordFileIntent(currentPath);
                startActivity(intent);
            }
            else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingExcel)))
            {
                intent = OpenFilesTool.getExcelFileIntent(currentPath);
                startActivity(intent);
            }
            else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingPPT)))
            {
                intent = OpenFilesTool.getPPTFileIntent(currentPath);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "无法打开，请安装相应的软件！", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "请检查文件是否存在！", Toast.LENGTH_SHORT).show();
        }
    }
    
    private NetTask mGetFileAttachRequst = null;
    
    private String  fileName             = "";
    
    // private String mUrl = "";
    
    private class MyWebViewDownLoadListener implements DownloadListener
    {
        
        @Override
        public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype,
            long contentLength)
        {
            // Uri uri = Uri.parse(url);
            // Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // startActivity(intent);
            
            if (isDownloading)
            {
                Toast.makeText(XtbgActivity.this, "正在下载文件，请稍后！", Toast.LENGTH_SHORT).show();
                return;
            }
            
            fileName = getFileName(url);
            
            File filePath = new File(dirPath + fileName);
            if (filePath != null && filePath.exists())
            {
                // startActivity(OpenFilesTool.getPdfFileIntent(filePath));
                // openFile(dirPath + fileName);
                filePath.delete();
            }
            // else
            // {
            Thread th = new Thread()
            {
                /**
                 * 重载方法
                 * 
                 * @author rqj
                 */
                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    downloadFileWithUrl(url, dirPath + fileName);
                    super.run();
                }
                
            };
            th.start();
            isDownloading = true;
            waitDialog.show(getSupportFragmentManager());
            // }
            
        }
    }
    
    public String getFileName(String url)
    {
        String filename = "";
        
        String[] arrs = url.split("&");
        String fileNameArr = "";
        if (arrs != null)
        {
            for (int i = 0; i < arrs.length; i++)
            {
                if (arrs[i].startsWith("filename="))
                {
                    
                    fileNameArr = arrs[i];
                    break;
                }
            }
        }
        
        int index_1 = fileNameArr.indexOf("=");
        filename = fileNameArr.substring(index_1 + 1, fileNameArr.length());
        
        try
        {
            filename = URLDecoder.decode(filename, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return filename;
    }
    
    public void createPath(File file)
    {
        if (file.exists())
            file.delete();
        File localFile = file.getParentFile();
        if ((localFile != null) && (!localFile.exists()))
            localFile.mkdirs();
    }
    
    private void downloadFileWithUrl(String paramString, String filePath)
    {
        try
        {
            URL localURL = new URL(paramString);
            HttpURLConnection localHttpURLConnection = (HttpURLConnection)localURL.openConnection();
            localHttpURLConnection.setConnectTimeout(30000);
            localHttpURLConnection.setReadTimeout(30000);
            InputStream localInputStream = localHttpURLConnection.getInputStream();
            FileOutputStream localFileOutputStream = null;
            
            File localFile1 = new File(filePath);
            if (localFile1 != null)
            {
                createPath(localFile1);
                localFileOutputStream = new FileOutputStream(localFile1);
            }
            byte[] arrayOfByte = new byte[1024];
            while (true)
            {
                int i = localInputStream.read(arrayOfByte);
                if (i == -1)
                {
                    localFileOutputStream.flush();
                    localFileOutputStream.close();
                    localInputStream.close();
                    break;
                }
                else
                {
                    localFileOutputStream.write(arrayOfByte, 0, i);
                }
                
            }
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
        }
        finally
        {
            isDownloading = false;
            Message localMessage = new Message();
            localMessage.what = 0;
            mDowanlodFileHandler.sendMessage(localMessage);
        }
        
    }
    
    /**
     * 
     * 打电话
     * 
     * @Description<功能详细描述>
     * 
     * @param phnum
     * @LastModifiedDate：2014-4-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void call(String phnum)
    {
        // Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phnum));
        // startActivity(intent);
        
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");
        intent.setData(Uri.parse(phnum));
        startActivity(intent);
        
        // Intent intent = new Intent();
        // intent.setAction("android.intent.action.CALL_BUTTON");
        // startActivity(intent);
        
    }
    
    private void initData()
    {
        
        mWebView.loadUrl(url);
        if (null != titleStr && !"".equals(titleStr))
        {
            titleTv.setVisibility(View.VISIBLE);
            titleTv.setText(titleStr);
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
    
    Handler mDowanlodFileHandler = new Handler()
                                 {
                                     
                                     /**
                                      * 重载方法
                                      * 
                                      * @param msg
                                      * @author rqj
                                      */
                                     @Override
                                     public void handleMessage(Message msg)
                                     {
                                         // TODO Auto-generated method stub
                                         waitDialog.dismiss();
                                         File file = new File(dirPath + fileName);
                                         if (file != null && file.exists())
                                         {
                                             startActivity(OpenFilesTool.getPdfFileIntent(file));
                                         }
                                         super.handleMessage(msg);
                                     }
                                     
                                 };
    
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
