/*
 * File name:  FrontpageActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-5-4
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hoperun.manager.components.CustomDialog;
import com.hoperun.manager.components.CustomDialogListener;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.frontpage.PicEntity;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.PicUtil;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * <一句话功能简述>首页界面
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-5-4]
 */
public class FrontpageActivity extends PMIPBaseActivity
{
    /** 获取图片列表 **/
    protected NetTask          mPicListRequst;
    
    /** 等待框布局 **/
    private RelativeLayout     loadLayout;
    
    /** 等待图片 **/
    private ImageView          loadImageView;
    
    /** 图片实体 **/
    private PicEntity          entity;
    
    /** 图片下载地址 **/
    private String             picUrl            = "";
    
    /** 图片存放地址 **/
    public static final String savePath          = Environment.getExternalStorageDirectory() + "/downloadpic/";
    
    /** 图片名称 **/
    private static String      savePicName       = savePath + "advertisement.png";
    
    /** 下载图片 **/
    private Thread             downloadPic;
    
    /** 跳转定时器 **/
    private Timer              mTimer;
    
    /** 跳转定时线程 **/
    private TimerTask          mTimerTask        = new TimerTask()
                                                 {
                                                     
                                                     @Override
                                                     public void run()
                                                     {
                                                         handler.sendEmptyMessage(0);
                                                     }
                                                 };
    
    /** 下载定时器 **/
    private Timer              downloadTimer;
    
    /** 下载定时线程 **/
    private TimerTask          downloadTimerTask = new TimerTask()
                                                 {
                                                     
                                                     @Override
                                                     public void run()
                                                     {
                                                         handler.sendEmptyMessage(1);
                                                     }
                                                 };
    
    /** 图片下载线程 **/
    private Runnable           mdownApkRunnable  = new Runnable()
                                                 {
                                                     @Override
                                                     public void run()
                                                     {
                                                         try
                                                         {
                                                             URL url = new URL(picUrl);
                                                             
                                                             HttpURLConnection conn =
                                                                 (HttpURLConnection)url.openConnection();
                                                             conn.connect();
                                                             int length = conn.getContentLength();
                                                             InputStream is = conn.getInputStream();
                                                             
                                                             File filedir = new File(savePath);
                                                             if (!filedir.exists())
                                                             {
                                                                 filedir.mkdirs();
                                                             }
                                                             File file = new File(savePicName);
                                                             if (file.exists())
                                                             {
                                                                 file.delete();
                                                             }
                                                             file.createNewFile();
                                                             FileOutputStream fos = new FileOutputStream(file);
                                                             
                                                             byte buf[] = new byte[1024];
                                                             int numread = -1;
                                                             while ((numread = is.read(buf)) != -1)
                                                             {
                                                                 fos.write(buf, 0, numread);
                                                             }
                                                             
                                                             fos.close();
                                                             is.close();
                                                         }
                                                         catch (MalformedURLException e)
                                                         {
                                                             e.printStackTrace();
                                                         }
                                                         catch (IOException e)
                                                         {
                                                             e.printStackTrace();
                                                         }
                                                         finally
                                                         {
                                                             
                                                             handler.sendEmptyMessage(3);
                                                             
                                                         }
                                                         
                                                     }
                                                 };
    
    Handler                    handler           = new Handler()
                                                 {
                                                     
                                                     @Override
                                                     public void handleMessage(Message msg)
                                                     {
                                                         super.handleMessage(msg);
                                                         switch (msg.what)
                                                         {
                                                             case 0:
                                                                 Intent intent =
                                                                     new Intent(
                                                                         FrontpageActivity.this,
                                                                         com.hoperun.project.ui.Login.LoginActivity.class);
                                                                 intent.putExtra("biaozhi", "1");
                                                                 startActivity(intent);
                                                                 FrontpageActivity.this.finish();
                                                                 break;
                                                             case 1:
                                                                 mPicListRequst.shutDownExecute();
                                                                 // Intent mintent =
                                                                 // new Intent(
                                                                 // FrontpageActivity.this,
                                                                 // com.hoperun.project.ui.Login.LoginActivity.class);
                                                                 // startActivity(mintent);
                                                                 // FrontpageActivity.this.finish();
                                                                 break;
                                                             case 3:
                                                                 bitmap = PicUtil.getBitmap(savePicName);
                                                                 if (null != bitmap)
                                                                 {
                                                                     Drawable drawable = new BitmapDrawable(bitmap);
                                                                     layout.setBackgroundDrawable(drawable);
                                                                 }
                                                                 mTimer = new Timer();
                                                                 mTimer.schedule(mTimerTask, 3000);
                                                                 break;
                                                             default:
                                                                 break;
                                                         }
                                                     }
                                                     
                                                 };
    
    private RelativeLayout     layout;
    
    private Bitmap             bitmap;
    
    /**
     * 重载方法
     * 
     * @param savedInstanceState
     * @author chen_wei3
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.frontpage);
        layout = (RelativeLayout)findViewById(R.id.frontpage);
        bitmap = PicUtil.getBitmap(savePicName);
        if (null != bitmap)
        {
            Drawable drawable = new BitmapDrawable(bitmap);
            layout.setBackgroundDrawable(drawable);
        }
        iniView();
        sendGetPicListRequest();
        
    }
    
    private void iniView()
    {
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
    }
    
    /**
     * 
     * <一句话功能简述>获取广告图片列表
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-4
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void sendGetPicListRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("", "");
            mPicListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETPICLIST).create();
            NetRequestController.getPicList(mPicListRequst,
                mHandler,
                RequestTypeConstants.GETPICLIST,
                body,
                "getPhonePicList");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
        downloadTimer = new Timer();
        downloadTimer.schedule(downloadTimerTask, 10000);
    }
    
    /**
     * 重载方法
     * 
     * @param requestType
     * @param objHeader
     * @param objBody
     * @param error
     * @param errorCode
     * @author chen_wei3
     */
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETPICLIST:
                    closeProgressDialog();
                    MetaResponseBody responseBuzBody = (MetaResponseBody)objBody;
                    List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                    if (ret != null && ret.size() > 0)
                    {
                        
                        if (!ret.get(0).get("webitems").equals(""))
                        {
                            
                            List<HashMap<String, Object>> ret1 =
                                (List<HashMap<String, Object>>)ret.get(0).get("webitems");
                            for (int i = 0; i < ret1.size(); i++)
                            {
                                entity = new PicEntity("", "");
                                entity.convertToObject(ret1.get(i));
                                
                                if ("720*1280.JPG".equals(entity.getStringKeyValue(PicEntity.name)))
                                {
                                    if (!GlobalState.getInstance()
                                        .getVercode()
                                        .equals(entity.getStringKeyValue(PicEntity.vercode)))
                                    {
                                        picUrl = entity.getStringKeyValue(PicEntity.url);
                                        downloadPic = new Thread(mdownApkRunnable);
                                        downloadPic.start();
                                        GlobalState.getInstance()
                                            .setVercode(entity.getStringKeyValue(PicEntity.vercode));
                                    }
                                    else
                                    {
                                        mTimer = new Timer();
                                        mTimer.schedule(mTimerTask, 3000);
                                    }
                                }
                                
                            }
                            
                        }
                        else
                        {
                            Intent intent = new Intent(this, com.hoperun.project.ui.Login.LoginActivity.class);
                            intent.putExtra("biaozhi", "1");
                            startActivity(intent);
                            finish();
                        }
                    }
                    else
                    {
                        Intent intent = new Intent(this, com.hoperun.project.ui.Login.LoginActivity.class);
                        intent.putExtra("biaozhi", "1");
                        startActivity(intent);
                        finish();
                    }
                    
                    break;
                default:
                    break;
            }
        }
        else
        {
            closeProgressDialog();
            // Intent intent = new Intent(this, com.hoperun.project.ui.Login.LoginActivity.class);
            // startActivity(intent);
            // finish();
            mTimer = new Timer();
            mTimer.schedule(mTimerTask, 2000);
        }
    }
    
    /**
     * 
     * <一句话功能简述>显示等待框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-4
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void showProgressDialog()
    {
        loadLayout.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable)loadImageView.getBackground();
        animationDrawable.start();
    }
    
    /**
     * 
     * <一句话功能简述>关闭等待框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-4
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void closeProgressDialog()
    {
        loadLayout.setVisibility(View.GONE);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            final CustomDialog dialog =
                CustomDialog.newInstance(this.getResources().getString(R.string.main_exit_app), this.getResources()
                    .getString(R.string.Login_Cancel), this.getResources().getString(R.string.Login_Confirm));
            dialog.show(FrontpageActivity.this.getSupportFragmentManager(), "ExitDialog");
            
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
        return false;
    }
}
