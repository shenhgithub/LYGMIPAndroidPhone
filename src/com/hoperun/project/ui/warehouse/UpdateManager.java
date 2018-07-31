/*
 * File name:  UadateManager.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  li_miao
 * Last modified date:  2014-2-26
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.warehouse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hoperun.manager.adpter.warehouse.AppsDetailsAdapter;
import com.hoperun.miplygphone.R;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author li_miao
 * @Version [版本号, 2014-2-26]
 */
public class UpdateManager
{
    private Handler                       imageHandler;
    
    private Context                       mContext;
    
    // 提示语
    private String                        updateMsg     = "有新版本安装包，确定要更新版本吗?";
    
    // 返回的安装包url
    private String                        apkUrl        = "";
    
    // 返回的图片url
    private String                        imagUrl       = "";
    
    private Dialog                        noticeDialog;
    
    private Dialog                        downloadDialog;
    
    private Dialog                        updateDialog;
    
    /* 下载包安装路径 */
    // private static final String savePath = "/sdcard/连云港移动办公/updatedemo/";
    
    /* 下载包安装路径 */
    public static final String            savePath      = Environment.getExternalStorageDirectory() + "/updatedemo/";
    
    /* 下载图片路径 */
    public static final String            imagesavePath = Environment.getExternalStorageDirectory() + "/AppIcon/";
    
    // ConstState.MIP_ROOT_DIR+ GlobalState.getInstance().getOpenId() + "/AppIcon/"
    
    private List<HashMap<String, Object>> imageList;
    
    // /* 下载包安装路径 */
    // private static final String savePath = "/sdcard/";
    
    public List<HashMap<String, Object>> getImageList()
    {
        return imageList;
    }
    
    public void setImageList(List<HashMap<String, Object>> imageList)
    {
        this.imageList = imageList;
    }
    
    private static String    saveFileName      = savePath + "UpdateDemoRelease.apk";
    
    private static String    imagesaveFileName = savePath + "icon.png";
    
    // savePath + "UpdateDemoRelease.apk";
    
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar      mProgress;
    
    private static final int DOWN_UPDATE       = 1;
    
    private static final int DOWN_OVER         = 2;
    
    private static final int UPDATE_OVER       = 3;
    
    private boolean          isUpdate;
    
    private int              progress;
    
    private Thread           downLoadThread;
    
    private Thread           imagedownLoadThread;
    
    private boolean          interceptFlag     = false;
    
    private Handler          mHandler          = new Handler()
                                               {
                                                   public void handleMessage(Message msg)
                                                   {
                                                       switch (msg.what)
                                                       {
                                                           case DOWN_UPDATE:
                                                               mProgress.setProgress(progress);
                                                               break;
                                                           case DOWN_OVER:
                                                               Toast.makeText(mContext, "下载完成！", Toast.LENGTH_SHORT)
                                                                   .show();
                                                               downloadDialog.dismiss();
                                                               AppConststate.isDownloaded = true;
                                                               AppsDetailsAdapter.app_detail_list.get(AppsDetailsAdapter.index)
                                                                   .setIsdownload(true);
                                                               AppDetailWarehouse.appdetailAdapter.notifyDataSetChanged();
                                                               // AppDetailWarehouse.appdetailAdapter.notifyDataSetInvalidated();
                                                               // LayoutInflater factory =
                                                               // LayoutInflater.from(mContext);
                                                               // final View passView =
                                                               // factory.inflate(R.layout.app_detail_item,
                                                               // null);
                                                               // ImageButton app_state =
                                                               // (ImageButton)passView.findViewById(R.id.apps_state);
                                                               // app_state.setBackgroundResource(R.drawable.y_icon_2);
                                                               //
                                                               installApk();
                                                               break;
                                                           case UPDATE_OVER:
                                                               updateDialog.dismiss();
                                                               installApk();
                                                               AppDetailWarehouse.appdetailAdapter.notifyDataSetChanged();
                                                               break;
                                                           default:
                                                               break;
                                                       }
                                                   };
                                               };
    
    public UpdateManager(Context context, Handler handler, List<HashMap<String, Object>> list)
    {
        this.mContext = context;
        this.imageHandler = handler;
        this.imageList = list;
    }
    
    // 外部接口让主Activity调用
    public void checkUpdateInfo()
    {
        showNoticeDialog();
    }
    
    private void showNoticeDialog()
    {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("下载", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                showUpdateDownloadDialog();
            }
        });
        builder.setNegativeButton("以后再说", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }
    
    public void showUpdateDownloadDialog()
    {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");
        
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar)v.findViewById(R.id.progress);
        
        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        updateDialog = builder.create();
        updateDialog.show();
        
        downloadApk();
    }
    
    public void showDownloadDialog()
    {
        interceptFlag = false;
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("下载安装包");
        
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar)v.findViewById(R.id.progress);
        
        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();
        
        downloadApk();
    }
    
    private Runnable mdownApkRunnable  = new Runnable()
                                       {
                                           @Override
                                           public void run()
                                           {
                                               try
                                               {
                                                   URL url = new URL(apkUrl);
                                                   
                                                   HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                                                   conn.connect();
                                                   int length = conn.getContentLength();
                                                   InputStream is = conn.getInputStream();
                                                   
                                                   File file = new File(savePath);
                                                   if (!file.exists())
                                                   {
                                                       file.mkdir();
                                                   }
                                                   String apkFile = saveFileName;
                                                   File ApkFile = new File(apkFile);
                                                   FileOutputStream fos = new FileOutputStream(ApkFile);
                                                   
                                                   int count = 0;
                                                   byte buf[] = new byte[1024];
                                                   
                                                   do
                                                   {
                                                       int numread = is.read(buf);
                                                       count += numread;
                                                       progress = (int)(((float)count / length) * 100);
                                                       // 更新进度
                                                       mHandler.sendEmptyMessage(DOWN_UPDATE);
                                                       if (numread <= 0)
                                                       {
                                                           if (!isUpdate)
                                                           {
                                                               mHandler.sendEmptyMessage(DOWN_OVER);
                                                           }
                                                           else
                                                           {
                                                               // 下载完成通知安装
                                                               mHandler.sendEmptyMessage(UPDATE_OVER);
                                                           }
                                                           
                                                           break;
                                                       }
                                                       fos.write(buf, 0, numread);
                                                   }
                                                   while (!interceptFlag);// 点击取消就停止下载.
                                                   
                                                   if (interceptFlag)
                                                   {
                                                       if (ApkFile.exists())
                                                       {
                                                           ApkFile.delete();
                                                       }
                                                       conn.disconnect();
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
                                               
                                           }
                                       };
    
    private Runnable downImageRunnable = new Runnable()
                                       {
                                           @Override
                                           public void run()
                                           {
                                               if (null != imageList)
                                               {
                                                   for (int i = 0; i < imageList.size(); i++)
                                                   {
                                                       String appicon = (String)imageList.get(i).get("appicon");
                                                       String fileName = (String)imageList.get(i).get("appname");
                                                       imagUrl = appicon;
                                                       imagesaveFileName = imagesavePath + fileName + ".png";
                                                       if (null != imagUrl && !"".equals(imagUrl))
                                                       {
                                                           if (!new File(imagesaveFileName).exists())
                                                           {
                                                               if (downLoadImage())
                                                               {
                                                                   imageHandler.sendEmptyMessage(200);
                                                               }
                                                           }
                                                       }
                                                   }
                                               }
                                               
                                           }
                                       };
    
    private boolean downLoadImage()
    {
        try
        {
            URL url = new URL(imagUrl);
            
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            int length = conn.getContentLength();
            InputStream is = conn.getInputStream();
            
            File file = new File(imagesavePath);
            if (!file.exists())
            {
                file.mkdir();
            }
            // if (null != imageList)
            // {
            // for (int i = 0; i < imageList.size(); i++)
            // {
            // String appicon = (String)imageList.get(i).get("appicon");
            // String fileName = (String)imageList.get(i).get("appname");
            // if (imagUrl.equals(appicon))
            // {
            // imagesaveFileName = imagesavePath + fileName + ".png";
            // break;
            // }
            // }
            // }
            String apkFile = imagesaveFileName;
            File ApkFile = new File(apkFile);
            FileOutputStream fos = new FileOutputStream(ApkFile);
            
            int count = 0;
            byte buf[] = new byte[1024];
            int numread;
            do
            {
                numread = is.read(buf);
                count += numread;
                progress = (int)(((float)count / length) * 100);
                if (numread <= 0)
                {
                    break;
                }
                fos.write(buf, 0, numread);
            }
            while (numread >= 0);// 点击取消就停止下载.
            
            fos.close();
            is.close();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * 下载apk
     * 
     * @param url
     */
    
    private void downloadApk()
    {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }
    
    public void getIcon()
    {
        downloadImage();
    }
    
    private void downloadImage()
    {
        imagedownLoadThread = new Thread(downImageRunnable);
        imagedownLoadThread.start();
    }
    
    /**
     * 安装apk
     * 
     * @param url
     */
    public void installApk()
    {
        // AppDetailWarehouse.appdetailAdapter.notifyDataSetChanged();
        File apkfile = new File(saveFileName);
        if (!apkfile.exists())
        {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
        AppDetailWarehouse.appdetailAdapter.notifyDataSetChanged();
    }
    
    /**
     * 获取 apkUrl
     * 
     * @return 返回 apkUrl
     * @author li_miao
     */
    public String getApkUrl()
    {
        return apkUrl;
    }
    
    /**
     * 设置 apkUrl
     * 
     * @param apkUrl 对apkUrl进行赋值
     * @author li_miao
     */
    public void setApkUrl(String apkUrl)
    {
        this.apkUrl = apkUrl;
    }
    
    /**
     * 获取 savepath
     * 
     * @return 返回 savepath
     * @author li_miao
     */
    public static String getSavepath()
    {
        return savePath;
    }
    
    /**
     * 获取 savefilename
     * 
     * @return 返回 savefilename
     * @author li_miao
     */
    public static String getSavefilename()
    {
        return saveFileName;
    }
    
    public void setSavefilename(String appName)
    {
        this.saveFileName = savePath + appName + ".apk";
    }
    
    /**
     * 获取 imagesaveFileName
     * 
     * @return 返回 imagesaveFileName
     * @author li_miao
     */
    public static String getImagesaveFileName()
    {
        return imagesaveFileName;
    }
    
    /**
     * 设置 imagesaveFileName
     * 
     * @param imagesaveFileName 对imagesaveFileName进行赋值
     * @author li_miao
     */
    public static void setImagesaveFileName(String imagesaveFileName)
    {
        UpdateManager.imagesaveFileName = imagesavePath + imagesaveFileName + ".png";
    }
    
    /**
     * 获取 imagUrl
     * 
     * @return 返回 imagUrl
     * @author li_miao
     */
    public String getImagUrl()
    {
        return imagUrl;
    }
    
    /**
     * 设置 imagUrl
     * 
     * @param imagUrl 对imagUrl进行赋值
     * @author li_miao
     */
    public void setImagUrl(String imagUrl)
    {
        this.imagUrl = imagUrl;
    }
    
    /**
     * 获取 isUpdate
     * 
     * @return 返回 isUpdate
     * @author li_miao
     */
    public boolean isUpdate()
    {
        return isUpdate;
    }
    
    /**
     * 设置 isUpdate
     * 
     * @param isUpdate 对isUpdate进行赋值
     * @author li_miao
     */
    public void setUpdate(boolean isUpdate)
    {
        this.isUpdate = isUpdate;
    }
    
}
