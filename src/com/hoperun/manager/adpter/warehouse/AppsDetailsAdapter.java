/*
 * File name:  AppsDetailsAdapter.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  li_miao
 * Last modified date:  2014-3-24
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.warehouse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.mip.utils.StringUtils;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;
import com.hoperun.project.ui.warehouse.AppsModule;
import com.hoperun.project.ui.warehouse.InstallApkReceiver;
import com.hoperun.project.ui.warehouse.ModuleSave;
import com.hoperun.project.ui.warehouse.MyApps;
import com.hoperun.project.ui.warehouse.PrefsUtils;
import com.hoperun.project.ui.warehouse.UpdateManager;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author li_miao
 * @Version [版本号, 2014-3-24]
 */
public class AppsDetailsAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context                       context;
    
    /** 数据列表 **/
    private List<HashMap<String, Object>> list;
    
    /** 帮助类 **/
    private LayoutInflater                inflater;
    
    /** 选择的item **/
    public int                            mSelectedPosition = -1;
    
    private UpdateManager                 mUpdateManager;
    
    public static List<AppsModule>        app_detail_list;
    
    public static int                     index;
    
    /** 已安装非系统应用 */
    private List<AppInfo>                 installed_list    = new ArrayList<AppsDetailsAdapter.AppInfo>();
    
    /**
     * 图片
     */
    private Handler                       imageHandle       = new Handler()
                                                            {
                                                                public void handleMessage(Message msg)
                                                                {
                                                                    notifyDataSetChanged();
                                                                };
                                                            };
    
    InstallApkReceiver                    installedReceiver;
    
    public List<HashMap<String, Object>> getList()
    {
        return list;
    }
    
    public void setList(List<HashMap<String, Object>> list)
    {
        this.list = list;
    }
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 上下文
     * @param list 列表数据
     */
    public AppsDetailsAdapter(Context context, List<HashMap<String, Object>> list)
    {
        super();
        this.context = context;
        
        this.list = list;
        getInstallList();
        this.inflater = LayoutInflater.from(this.context);
        mUpdateManager = new UpdateManager(context, imageHandle, list);// (context, imageHandle,imageHandle);
        mUpdateManager.getIcon();
        app_detail_list = new ArrayList<AppsModule>();
        for (int i = 0; i < list.size(); i++)
        {
            ModuleSave.saveAppsModuleTolist(list.get(i), app_detail_list);
        }
        installedReceiver = InstallApkReceiver.newInstallApkReceiver(imageHandle);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addDataScheme("package");
        context.registerReceiver(installedReceiver, filter);
        
        // Intent intent = new Intent();
        // intent.setAction("android.intent.action.PACKAGE_ADDED");
        // context.sendBroadcast(intent);
        
        // PackageInfo pkg = context.getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
        // // String appName = pkg.applicationInfo.loadLabel(getPackageManager()).toString();
        // String versionName = pkg.versionName;
    }
    
    public InstallApkReceiver getInstalledReceiver()
    {
        return installedReceiver;
    }
    
    public void setInstalledReceiver(InstallApkReceiver installedReceiver)
    {
        this.installedReceiver = installedReceiver;
    }
    
    /**
     * 
     * 获取选中项的位置
     * 
     * @Description 获取选中项的位置
     * 
     * @return 选中项的位置
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public int getmSelectedPosition()
    {
        return mSelectedPosition;
    }
    
    /**
     * 
     * 设置选中项的位置
     * 
     * @Description 设置选中项的位置
     * 
     * @param mSelectedPosition 选中项的位置
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setmSelectedPosition(int mSelectedPosition)
    {
        this.mSelectedPosition = mSelectedPosition;
    }
    
    /**
     * 重载方法
     * 
     * @return 返回列表的长度
     * @author shen_feng
     */
    @Override
    public int getCount()
    {
        // return list.getBuzList().get(0).get("apptypes").size();
        return list.size();
    }
    
    /**
     * 重载方法
     * 
     * @param position 位置
     * @return 返回列表的某一项
     * @author shen_feng
     */
    @Override
    public Object getItem(int position)
    {
        return list.size();
    }
    
    /**
     * 获取名称
     * 
     * @Description<功能详细描述>
     * 
     * @param position
     * @return
     * @LastModifiedDate：2013-11-4
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public String getItemName(int position)
    {
        // Object funName = list.getBuzList().get(position).get("apptypename");
        // if (funName == null)
        // {
        // return getItemName(position);
        // }
        // else
        // {
        
        return "";
        // }
        
    }
    
    /**
     * 重载方法
     * 
     * @param position 位置
     * @return 某一项的id
     * @author shen_feng
     */
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /**
     * 重载方法
     * 
     * @param position position
     * @param convertView convertView
     * @param parent parent
     * @return view
     * @author shen_feng
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        
        final ViewHold viewHold;
        
        // AppConststate.isDownloaded = false;
        if (convertView == null)
        {
            
            viewHold = new ViewHold();
            convertView = inflater.inflate(R.layout.app_detail_item, null);
            
            viewHold.app_name = (TextView)convertView.findViewById(R.id.app_detail_name);
            viewHold.app_time = (TextView)convertView.findViewById(R.id.app_detail_time);
            viewHold.app_version = (TextView)convertView.findViewById(R.id.app_detail_version);
            viewHold.app_state = (ImageButton)convertView.findViewById(R.id.apps_state);
            viewHold.app_state_text = (TextView)convertView.findViewById(R.id.apps_state_promote);
            viewHold.up_promote_text = (TextView)convertView.findViewById(R.id.up_promote_text);
            viewHold.up_promote_layout = (RelativeLayout)convertView.findViewById(R.id.up_promote_layout);
            viewHold.up_promote_icon = (ImageView)convertView.findViewById(R.id.up_promote_icon);
            viewHold.app_icon = (ImageView)convertView.findViewById(R.id.app_detail_icon);
            viewHold.app_front_promote = (TextView)convertView.findViewById(R.id.apps_front_promote);
            viewHold.app_state.setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    final String text = (String)viewHold.app_state_text.getText();
                    if (text.equals("下载"))
                    {
                        index = position;
                        
                        String attachUrl = ((String)list.get(position).get("attachurl"));
                        if (null == attachUrl || "".equals(attachUrl))
                        {
                            Toast.makeText(context, "暂未开放！", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mUpdateManager.setApkUrl((String)list.get(position).get("attachurl"));
                            mUpdateManager.setSavefilename((String)list.get(position).get("appname"));
                            mUpdateManager.setUpdate(false);
                            mUpdateManager.showDownloadDialog();
                        }
                        
                    }
                    else if (text.equals("安装"))
                    {
                        // mUpdateManager = new UpdateManager(context);
                        
                        // notifyDataSetChanged();
                        installedReceiver.setPackageName(((String)list.get(position).get("urltype")));
                        mUpdateManager.setSavefilename((String)list.get(position).get("appname"));
                        mUpdateManager.installApk();
                        app_detail_list.get(position).setInstalled(true);
                        if (isInstalled(app_detail_list.get(position).getUrltype()))
                        {
                            viewHold.up_promote_layout.setVisibility(View.VISIBLE);
                            viewHold.up_promote_text.setText("已安装");
                            viewHold.up_promote_icon.setBackgroundResource(R.drawable.y_c);
                            viewHold.app_state.setBackgroundResource(R.drawable.y_icon_3);
                            viewHold.app_state_text.setText("添加");
                        }
                        
                        // notifyDataSetChanged();
                        
                    }
                    else if (text.equals("添加"))
                    {
                        String added_local = PrefsUtils.readPrefs(context, "collect");
                        String gotosave =
                            app_detail_list.get(position).getAppname() + "@"
                                + app_detail_list.get(position).getUrltype();
                        
                        if (StringUtils.isNull(added_local))
                        {
                            added_local = gotosave;
                            PrefsUtils.writePrefs(context, "collect", added_local);
                            Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT).show();
                        }
                        else if (!StringUtils.isNull(added_local) && !added_local.contains(gotosave))
                        {
                            added_local = added_local + "," + gotosave;
                            PrefsUtils.writePrefs(context, "collect", added_local);
                            Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT).show();
                        }
                        else if (added_local.contains(gotosave))
                        {
                            Toast.makeText(context, "该项已添加！", Toast.LENGTH_SHORT).show();
                        }
                        
                        // if (AppConststate.appAddedList.size() < 1)
                        // {
                        // AppConststate.appAddedList.add(app_detail_list.get(position));
                        // Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT).show();
                        //
                        // }
                        // else
                        // {
                        // for (int i = 0; i < AppConststate.appAddedList.size(); i++)
                        // {
                        // if (AppConststate.appAddedList.get(i)
                        // .getUrltype()
                        // .equals(app_detail_list.get(position).getUrltype()))
                        // {
                        // Toast.makeText(context, "该项已添加！", Toast.LENGTH_SHORT).show();
                        // }
                        // else
                        // {
                        // AppConststate.appAddedList.add(app_detail_list.get(position));
                        // Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT).show();
                        // }
                        // }
                        // }
                        //
                        // AppConststate.appAddedList.add(app_detail_list.get(position));
                        app_detail_list.get(position).setAdded(true);
                        
                        notifyDataSetChanged();
                        MyApps.myappsadapter2.notifyDataSetChanged();
                    }
                    else if (text.equals("卸载"))
                    {
                        // mUpdateManager = new UpdateManager(context);
                        // mUpdateManager.installApk();
                        // app_detail_list.get(position).setInstalled(true);
                        //
                        String app_saved = PrefsUtils.readPrefs(context, "collect");
                        String gotosave =
                            app_detail_list.get(position).getAppname() + "@"
                                + app_detail_list.get(position).getUrltype();
                        
                        if (app_saved.startsWith(gotosave) && app_saved.contains(","))
                        {
                            app_saved = app_saved.replace(gotosave + ",", "");
                            // PrefsUtils.writePrefs("collect", label_saved);
                        }
                        else if (app_saved.startsWith(gotosave) && !app_saved.contains(","))
                        {
                            // label_saved = label_saved.replaceAll(ProjectUIID.rowTitle_list.get(i), "");
                            app_saved = app_saved.replace(gotosave, "");
                            
                            // PrefsUtils.writePrefs("collect", label_saved);
                        }
                        else
                        {
                            app_saved = app_saved.replace("," + gotosave, "");
                            // PrefsUtils.writePrefs("collect", label_saved);
                        }
                        PrefsUtils.writePrefs(context, "collect", app_saved);
                        
                        // for (int i = 0; i < AppConststate.appAddedList.size(); i++)
                        // {
                        // if (AppConststate.appAddedList.get(i)
                        // .getUrltype()
                        // .equals(app_detail_list.get(position).getUrltype()))
                        // {
                        // AppConststate.appAddedList.remove(i);
                        // Toast.makeText(context, "卸载成功！", Toast.LENGTH_SHORT).show();
                        // }
                        // else
                        // {
                        // // AppConststate.appAddedList.add(app_detail_list.get(position));
                        // }
                        // }
                        // AppConststate.appAddedList.remove(app_detail_list.get(position));
                        app_detail_list.get(position).setAdded(false);
                        
                        viewHold.up_promote_layout.setVisibility(View.VISIBLE);
                        viewHold.up_promote_text.setText("已安装");
                        viewHold.up_promote_icon.setBackgroundResource(R.drawable.y_c);
                        viewHold.app_state.setBackgroundResource(R.drawable.y_icon_3);
                        viewHold.app_state_text.setText("添加");
                        
                        notifyDataSetChanged();
                        MyApps.myappsadapter2.notifyDataSetChanged();
                        Toast.makeText(context, "卸载成功！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            viewHold.app_state_front = (ImageButton)convertView.findViewById(R.id.apps_state_front);
            viewHold.app_state_front.setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    mUpdateManager = new UpdateManager(context, imageHandle, list);
                    mUpdateManager.setApkUrl((String)list.get(index).get("attachurl"));
                    mUpdateManager.setSavefilename((String)list.get(index).get("appname"));
                    mUpdateManager.setUpdate(true);
                    mUpdateManager.checkUpdateInfo();
                    notifyDataSetChanged();
                    
                }
            });
            // viewHold.textNum = (TextView)convertView.findViewById(R.id.types_num);
            // viewHold.textId = (TextView)convertView.findViewById(R.id.types_id);
            // viewHold.mRlayout = (RelativeLayout)convertView.findViewById(R.id.file_doc_list_item_layout);
            //
            // viewHold.mImgNew = (ImageView)convertView.findViewById(R.id.file_item_new_icon);
            //
            // viewHold.mImgNew.setVisibility(View.GONE);
            
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
            
        }
        
        if (mSelectedPosition == position)
        {
            
            // viewHold.mRlayout.setBackgroundResource(R.drawable.list_2);
            
        }
        else
        {
            
            // viewHold.mRlayout.setBackgroundResource(R.drawable.list_1);
            
        }
        // mUpdateManager.setImagesaveFileName((String)list.get(position).get("appname"));
        
        String appIconUrl = ((String)list.get(position).get("appicon"));
        if (null != appIconUrl)
        {
            if (appIconUrl.equals(""))
            {
                viewHold.app_icon.setImageResource(R.drawable.ic_launcher);
            }
            else
            {
                String imagePath = UpdateManager.imagesavePath + app_detail_list.get(position).getAppname() + ".png";
                
                Bitmap bitmap = getBitmapByWidth(imagePath, 40, 10);
                if (null == bitmap)
                {
                    // mUpdateManager.setImagUrl((String)list.get(position).get("appicon"));
                    // mUpdateManager.getIcon();
                    viewHold.app_icon.setImageResource(R.drawable.ic_launcher);
                }
                else
                {
                    viewHold.app_icon.setImageBitmap(bitmap);
                }
                
            }
        }
        else
        {
            viewHold.app_icon.setImageResource(R.drawable.ic_launcher);
        }
        
        // if (bitmap != null)
        // {
        // viewHold.app_icon.setImageBitmap(bitmap);
        // }
        // else
        // {
        // viewHold.app_icon.setImageResource(R.drawable.ic_launcher);
        // }
        
        viewHold.app_name.setText((String)list.get(position).get("appname"));
        viewHold.app_time.setText("发布时间:" + ((String)list.get(position).get("createtime")).substring(0, 11));
        viewHold.app_version.setText("版本:" + (String)list.get(position).get("version"));
        
        String packageName = app_detail_list.get(position).getUrltype();
        boolean isadded = false;
        // for (int i = 0; i < AppConststate.appAddedList.size(); i++)
        // {
        // if (AppConststate.appAddedList.get(i).getUrltype().equals(app_detail_list.get(position).getUrltype()))
        // {
        // isadded = true;
        // }
        // else
        // {
        // isadded = false;
        // }
        // }
        
        String added_local = PrefsUtils.readPrefs(context, "collect");
        String gotosave = app_detail_list.get(position).getAppname() + "@" + app_detail_list.get(position).getUrltype();
        if (added_local.contains(gotosave))
        {
            isadded = true;
        }
        else
        {
            isadded = false;
        }
        
        String apkPath = UpdateManager.savePath + app_detail_list.get(position).getAppname() + ".apk";
        
        File apkFile = new File(apkPath);
        boolean isDownLoad = apkFile.exists();
        boolean isInstal = isInstalled(app_detail_list.get(position).getUrltype());
        
        String localVersion = getLocalVersion(apkPath);
        if (isInstal)// 已安装
        {
            localVersion = getVersionFromInstall(app_detail_list.get(position).getUrltype());
            viewHold.up_promote_layout.setVisibility(View.VISIBLE);
            viewHold.up_promote_text.setText("已安装");
            viewHold.up_promote_icon.setBackgroundResource(R.drawable.y_c);
            if (localVersion.equals(app_detail_list.get(position).getVersion()) && !isadded)// 3.0.0
            {
                
                viewHold.app_state.setBackgroundResource(R.drawable.y_icon_3);
                viewHold.app_state_text.setText("添加");
            }
            else if (!localVersion.equals(app_detail_list.get(position).getVersion()) && !isadded)// 3.0.0
            {
                viewHold.app_state_front.setVisibility(View.VISIBLE);
                viewHold.app_front_promote.setVisibility(View.VISIBLE);
                viewHold.app_state.setBackgroundResource(R.drawable.y_icon_3);
                viewHold.app_state_text.setText("添加");
            }
            else if (isadded)
            {
                viewHold.app_state.setBackgroundResource(R.drawable.y_icon_5);
                viewHold.app_state_text.setText("卸载");
            }
        }
        else
        {
            if (isDownLoad && localVersion.equals(app_detail_list.get(position).getVersion()))// app_detail_list.get(position).getVersion()
            {
                mUpdateManager.setSavefilename(app_detail_list.get(position).getAppname());
                viewHold.up_promote_layout.setVisibility(View.VISIBLE);
                viewHold.up_promote_text.setText("已下载");
                viewHold.app_state.setBackgroundResource(R.drawable.y_icon_2);
                viewHold.app_state_text.setText("安装");
            }
            else
            // if (isDownLoad && !localVersion.equals(app_detail_list.get(position).getVersion()))
            {
                viewHold.app_state.setBackgroundResource(R.drawable.y_icon_1);
                viewHold.app_state_text.setText("下载");
            }
            
        }
        
        // if (isInstalled(app_detail_list.get(position).getUrltype()) && !isadded)
        // {
        // viewHold.up_promote_layout.setVisibility(View.VISIBLE);
        // viewHold.up_promote_text.setText("已安装");
        // viewHold.up_promote_icon.setBackgroundResource(R.drawable.y_c);
        // viewHold.app_state.setBackgroundResource(R.drawable.y_icon_3);
        // viewHold.app_state_text.setText("添加");
        // }
        // else if (app_detail_list.get(position).isIsdownload() && !app_detail_list.get(position).isInstalled())
        // {
        // viewHold.up_promote_layout.setVisibility(View.VISIBLE);
        // viewHold.app_state.setBackgroundResource(R.drawable.y_icon_2);
        // viewHold.app_state_text.setText("安装");
        // viewHold.up_promote_text.setText("已下载");
        // }
        // else if (isadded)
        // {
        // viewHold.up_promote_layout.setVisibility(View.VISIBLE);
        // viewHold.up_promote_text.setText("已安装");
        // viewHold.up_promote_icon.setBackgroundResource(R.drawable.y_c);
        // viewHold.app_state.setBackgroundResource(R.drawable.y_icon_5);
        // viewHold.app_state_text.setText("卸载");
        // // this.notifyDataSetChanged();
        // }
        // if (getVersion(packageName) != null
        // && !getVersion(packageName).equals(app_detail_list.get(position).getVersion()))
        // {
        // viewHold.app_state_front.setVisibility(View.VISIBLE);
        // viewHold.app_front_promote.setVisibility(View.VISIBLE);
        // }
        return convertView;
    }
    
    /**
     * 
     * 获取本地apk版本号
     * 
     * @Description<功能详细描述>
     * 
     * @param archiveFilePath apk路径
     * @return
     * @LastModifiedDate：2014-4-10
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private String getLocalVersion(String archiveFilePath)
    {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        String version = "";
        if (info != null)
        {
            version = info.versionName; // 得到版本信息
            
        }
        return version;
    }
    
    /**
     * 
     * 从已安装的程序中获取版本
     * 
     * @Description<功能详细描述>
     * 
     * @param packageName
     * @return
     * @LastModifiedDate：2014-4-10
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private String getVersionFromInstall(String packageName)
    {
        PackageManager pm = context.getPackageManager();
        PackageInfo packInfo = null;
        String version = "";
        try
        {
            packInfo = pm.getPackageInfo(packageName, 0);
        }
        catch (NameNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (null != packInfo)
            
            version = packInfo.versionName;
        return version;
    }
    
    /**
     * 根据宽度从本地图片路径获取该图片的缩略图
     * 
     * @param localImagePath 本地图片的路径
     * @param width 缩略图的宽
     * @param addedScaling 额外可以加的缩放比例
     * @return bitmap 指定宽高的缩略图
     */
    private Bitmap getBitmapByWidth(String localImagePath, int width, int addedScaling)
    {
        if (TextUtils.isEmpty(localImagePath))
        {
            return null;
        }
        
        Bitmap temBitmap = null;
        
        BitmapFactory.Options outOptions = new BitmapFactory.Options();
        
        // 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中。
        outOptions.inJustDecodeBounds = true;
        
        // 加载获取图片的宽高
        BitmapFactory.decodeFile(localImagePath, outOptions);
        
        int height = outOptions.outHeight;
        
        if (outOptions.outWidth > width)
        {
            // 根据宽设置缩放比例
            outOptions.inSampleSize = outOptions.outWidth / width + 1 + addedScaling;
            outOptions.outWidth = width;
            
            // 计算缩放后的高度
            height = outOptions.outHeight / outOptions.inSampleSize;
            outOptions.outHeight = height;
        }
        
        // 重新设置该属性为false，加载图片返回
        outOptions.inJustDecodeBounds = false;
        outOptions.inPurgeable = true;
        outOptions.inInputShareable = true;
        // temBitmap = BitmapFactory.decodeFile(localImagePath, outOptions);
        temBitmap = BitmapFactory.decodeFile(localImagePath);
        return temBitmap;
    }
    
    /**
     * 根据包名判断程序是否安装 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param packageName
     * @return
     * @LastModifiedDate：2014-3-26
     * @author li_miao
     * @EditHistory：<修改内容><修改人>
     */
    public boolean isInstalled(String packageName)
    {
        PackageInfo packageInfo;
        if (null == packageName || "".equals(packageName))
        {
            return false;
        }
        
        try
        {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        }
        catch (NameNotFoundException e)
        {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null)
        {
            System.out.println("没有安装");
            return false;
            
        }
        else
        {
            
            System.out.println("已经安装");
            return true;
            
        }
        
    }
    
    /**
     * 
     * 获取已安装非系统应用列表
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-3
     * @author li_miao
     * @EditHistory：<修改内容><修改人>
     */
    public void getInstallList()
    {
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); // 用来存储获取的应用信息数据
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        
        for (int i = 0; i < packages.size(); i++)
        {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
            tmpInfo.packageName = packageInfo.packageName;
            tmpInfo.versionName = packageInfo.versionName;
            tmpInfo.versionCode = packageInfo.versionCode;
            tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
            // Only display the non-system app info
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                appList.add(tmpInfo);// 如果非系统应用，则添加至appList
                installed_list.add(tmpInfo);
            }
            
        }
        
    }
    
    /**
     * 根据包名从已安装列表中获取版本号
     * 
     * @Description<功能详细描述>
     * 
     * @param packageName
     * @return
     * @LastModifiedDate：2014-4-3
     * @author li_miao
     * @EditHistory：<修改内容><修改人>
     */
    public String getVersion(String packageName)
    {
        for (int i = 0; i < installed_list.size(); i++)
        {
            if (installed_list.get(i).packageName.equals(packageName))
            {
                return installed_list.get(i).versionName;
            }
        }
        return null;
    }
    
    public String getAppVersion(String packegeName)
    {
        PackageInfo pkg;
        try
        {
            pkg = context.getPackageManager().getPackageInfo(packegeName, 0);
            String versionName = pkg.versionName;
            return versionName;
        }
        catch (NameNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // String appName = pkg.applicationInfo.loadLabel(getPackageManager()).toString();
        return null;
    }
    
    class AppInfo
    {
        public String   appName     = "";
        
        public String   packageName = "";
        
        public String   versionName = "";
        
        public int      versionCode = 0;
        
        public Drawable appIcon     = null;
        
        public void print()
        {
            Log.v("app", "Name:" + appName + " Package:" + packageName);
            Log.v("app", "Name:" + appName + " versionName:" + versionName);
            Log.v("app", "Name:" + appName + " versionCode:" + versionCode);
        }
        
    }
    
    static class ViewHold
    {
        // 应用名称
        TextView       app_name;
        
        // 发布时间
        TextView       app_time;
        
        // 版本
        TextView       app_version;
        
        // 小按钮标识语
        TextView       up_promote_text;
        
        // 后方下载等按钮
        ImageButton    app_state;
        
        // 更新按钮
        ImageButton    app_state_front;
        
        // 提示小布局
        RelativeLayout up_promote_layout;
        
        TextView       app_state_text;
        
        // 图标图片
        ImageView      app_icon;
        
        TextView       app_front_promote;
        
        ImageView      up_promote_icon;
        // TextView textNum;
        //
        // TextView textId;
        // // 整个布局
        // RelativeLayout mRlayout;
        //
        // // 是否为新的标记
        // ImageView mImgNew;
        
    }
}
