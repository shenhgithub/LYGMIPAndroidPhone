/*
 * File name:  AppWarehouse.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  li_miao
 * Last modified date:  2014-3-21
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoperun.manager.adpter.warehouse.AppTypesAdapter;
import com.hoperun.manager.adpter.warehouse.MyAppAdapter;
import com.hoperun.manager.adpter.warehouse.MyappsAdapter;
import com.hoperun.manager.components.CustomDialog;
import com.hoperun.manager.components.CustomDialogListener;
import com.hoperun.manager.components.HeadView;
import com.hoperun.manager.components.WaitDialog;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.sqlUtils.Table.TUSERINFO;
import com.hoperun.mip.utils.AesUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.project.ui.Login.LoginActivity;
import com.hoperun.project.ui.Login.PersonSetView;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * 我的应用页面
 * 
 * @Description<功能详细描述>
 * 
 * @author li_miao
 * @Version [版本号, 2014-3-21]
 */
public class MyApps extends PMIPBaseActivity implements OnClickListener, OnItemClickListener, OnTouchListener
{
    /** 等待对话框 **/
    private WaitDialog                    waitDialog;
    
    /** 返回body **/
    private MetaResponseBody              responseBuzBody;
    
    private ListView                      app_list;
    
    private AppTypesAdapter               appadapter;
    
    public static MyappsAdapter           myappsadapter;
    
    public static MyAppAdapter            myappsadapter2;
    
    private TextView                      title;
    
    protected HeadView                    mHeadView;
    
    /** 滑动效果 **/
    private View.OnTouchListener          mViewLandscapeSlideListener;
    
    /**
     * 布局1，用于存放下一级页面
     */
    private FrameLayout                   mFramLayout1;
    
    /**
     * 布局2，用于存放下下一级页面
     */
    private FrameLayout                   mFramLayout2;
    
    /**
     * 主栏目布局
     */
    private ListView                      mLvMenu;
    
    private RelativeLayout                mNoDataRl;
    
    private List<HashMap<String, String>> myappsList = new ArrayList<HashMap<String, String>>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.my_apps);
        // PrefsUtils.writePrefs(this, "collect", "");
        title = (TextView)findViewById(R.id.header_title);
        title.setText(GlobalState.getInstance().getUserName());
        initView();
        initData();
        // getInstalledApp();
    }
    
    public void initView()
    {
        
        mHeadView = (HeadView)findViewById(R.id.mobile_header);
        app_list = (ListView)findViewById(R.id.myappslistview);
        app_list.setOnItemClickListener(this);
        // for (int i = 0; i < 3; i++)
        // {
        // HashMap<String, String> map = new HashMap<String, String>();
        // map.put("myappName", "江苏统计");
        // map.put("myPackage", "jj");
        //
        // if (i == 2)
        // {
        // map.put("myappName", "添加应用");
        // map.put("myPackage", "jj");
        // }
        // myappsList.add(map);
        // }
        
        // myappsadapter = new MyappsAdapter(this, AppConststate.appAddedList);
        // app_list.setAdapter(myappsadapter);
        myappsadapter2 = new MyAppAdapter(this, PrefsUtils.readPrefs(this, "collect"));
        app_list.setAdapter(myappsadapter2);
        
    }
    
    public void initData()
    {
        waitDialog = WaitDialog.newInstance();
        // mFragmentManager = this.getSupportFragmentManager();
        // openId = GlobalState.getInstance().getOpenId();
        mHeadView.setTitle(GlobalState.getInstance().getUserName());
        mHeadView.setLeftOnclickLisen(mPersonSetListener);
        mHeadView.setRightOnclickLisen(mExistOnclickListen);
        
    }
    
    private OnClickListener mPersonSetListener  = new OnClickListener()
                                                {
                                                    
                                                    @Override
                                                    public void onClick(View v)
                                                    {
                                                        Intent intent = new Intent();
                                                        intent.setClass(MyApps.this, PersonSetView.class);
                                                        startActivity(intent);
                                                    }
                                                    
                                                };
    
    private OnClickListener mExistOnclickListen = new OnClickListener()
                                                {
                                                    
                                                    @Override
                                                    public void onClick(View v)
                                                    {
                                                        // TODO Auto-generated method stub
                                                        final CustomDialog dialog =
                                                            CustomDialog.newInstance("确定注销登录？", "取消", "确定");
                                                        
                                                        dialog.show(getSupportFragmentManager(), "logoutDialog");
                                                        
                                                        dialog.setLeftListener(new CustomDialogListener()
                                                        {
                                                            
                                                            @Override
                                                            public void Onclick()
                                                            {
                                                                // MainActivity.this.finish();
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                        dialog.setRightListener(new CustomDialogListener()
                                                        {
                                                            
                                                            @Override
                                                            public void Onclick()
                                                            {
                                                                dialog.dismiss();
                                                                GlobalState.getInstance().clearValues();
                                                                Intent it =
                                                                    new Intent(MyApps.this, LoginActivity.class);
                                                                it.putExtra("biaozhi", "0");
                                                                startActivity(it);
                                                                
                                                                MyApps.this.finish();
                                                            }
                                                        });
                                                        
                                                    }
                                                };
    
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
        String listStr = PrefsUtils.readPrefs(this, "collect");
        if (!"".equals(listStr))
        {
            if (arg2 < listStr.split(",").length)
            {
                String str = PrefsUtils.readPrefs(this, "collect").split(",")[arg2];
                startApk2(str.split("@")[1]);
            }
            // else if (arg2 == listStr.split(",").length)
            // {
            // String url =
            // "http://218.92.115.51/portal/m/index_sso.jsp?userId=" + GlobalState.getInstance().getOpenId();
            //
            // Intent oaintent = new Intent(this, XwzxContentShowActivity.class);
            // oaintent.putExtra("ISURL", true);
            // oaintent.putExtra("url", url);
            // oaintent.putExtra("title", "协同办公");
            // this.startActivity(oaintent);
            // }
            else
            {
                Intent mIntent = new Intent();
                mIntent.setClass(MyApps.this, AppWarehouse.class);
                startActivity(mIntent);
            }
        }
        else
        {
            // if (arg2 == 0)
            // {
            // String url =
            // "http://218.92.115.51/portal/m/index_sso.jsp?userId=" + GlobalState.getInstance().getOpenId();
            //
            // Intent oaintent = new Intent(this, XwzxContentShowActivity.class);
            // oaintent.putExtra("ISURL", true);
            // oaintent.putExtra("url", url);
            // oaintent.putExtra("title", "协同办公");
            // this.startActivity(oaintent);
            // }
            // else
            // {
            Intent mIntent = new Intent();
            mIntent.setClass(MyApps.this, AppWarehouse.class);
            startActivity(mIntent);
            // }
        }
        
        // if (arg2 == PrefsUtils.readPrefs(this, "collect").split(",").length
        // && !PrefsUtils.readPrefs(this, "collect").equals(""))
        // {
        // if (arg2 == PrefsUtils.readPrefs(this, "collect").split(",").length)
        // {
        // String url =
        // "http://218.92.115.51/portal/m/index_sso.jsp?userId=" + GlobalState.getInstance().getOpenId();
        //
        // Intent oaintent = new Intent(this, XwzxContentShowActivity.class);
        // oaintent.putExtra("ISURL", true);
        // oaintent.putExtra("url", url);
        // oaintent.putExtra("title", "协同办公");
        // this.startActivity(oaintent);
        // }
        // else
        // {
        // Intent mIntent = new Intent();
        // mIntent.setClass(MyApps.this, AppWarehouse.class);
        // startActivity(mIntent);
        // }
        // }
        // else if (PrefsUtils.readPrefs(this, "collect").equals(""))
        // {
        // if (arg2 == 0)
        // {
        //
        // String url =
        // "http://218.92.115.51/portal/m/index_sso.jsp?userId=" + GlobalState.getInstance().getOpenId();
        //
        // Intent oaintent = new Intent(this, XwzxContentShowActivity.class);
        // oaintent.putExtra("ISURL", true);
        // oaintent.putExtra("url", url);
        // oaintent.putExtra("title", "协同办公");
        // this.startActivity(oaintent);
        // }
        // else
        // {
        // Intent mIntent = new Intent();
        // mIntent.setClass(MyApps.this, AppWarehouse.class);
        // startActivity(mIntent);
        // }
        // }
        // else if (!PrefsUtils.readPrefs(this, "collect").equals(""))
        // {
        // // startApk2("com.hoperun.gacloud");
        // // startApk2("com.mcu.iVMS");
        // // startApk2(AppConststate.appAddedList.get(arg2).getUrltype());
        // String str = PrefsUtils.readPrefs(this, "collect").split(",")[arg2];
        // startApk2(str.split("@")[1]);
        // }
        
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
     * @author li_miao
     */
    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        myappsadapter2 = new MyAppAdapter(this, PrefsUtils.readPrefs(this, "collect"));
        app_list.setAdapter(myappsadapter2);
        // myappsadapter2.notifyDataSetChanged();
    }
    
    /**
     * 获取系统安装程序
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-3-25
     * @author li_miao
     * @EditHistory：<修改内容><修改人>
     */
    public void getInstalledApp()
    {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packs = pm.getInstalledApplications(0);
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        List<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        for (PackageInfo pi : packages)
        
        {
            
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("icon", pi.applicationInfo.loadIcon(pm));
            map.put("appName", pi.applicationInfo.loadLabel(pm));
            map.put("className", pi.applicationInfo.className);
            map.put("packageName", pi.applicationInfo.packageName);
            items.add(map);
            if (pi.applicationInfo.loadLabel(pm).equals("平安港口"))
            {
                System.out.println("这里是平安港口包名获取" + pi.applicationInfo.packageName);
            }
            
        }
        System.out.println("这里是平安港口包名获取" + items.size());
    }
    
    public void startApk(String packageName, String className)
    {
        Intent mIntent = new Intent();
        ComponentName comp = new ComponentName(packageName, className);
        mIntent.setComponent(comp);
        mIntent.setAction("android.intent.action.VIEW");
        startActivity(mIntent);
        
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
        
        try
        {
            packageInfo = this.getPackageManager().getPackageInfo(packageName, 0);
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
     * 根据包名启动APK
     * 
     * @Description<功能详细描述>
     * 
     * @param packageName
     * @LastModifiedDate：2014-3-25
     * @author li_miao
     * @EditHistory：<修改内容><修改人>
     */
    public void startApk2(String packageName)
    {
        
        PackageInfo pi = null;
        try
        {
            pi = getPackageManager().getPackageInfo(packageName, 0);
        }
        catch (NameNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // // --------------------add 2014-04-16 云之家单点登录------------------------------------
        // if ("com.kdweibo.client".equals(packageName))
        // {
        // callKdweiboApplictionUseBasicAuth(packageName);
        // }
        // // --------------------add 2014-04-16------------------------------------------------
        //
        // else
        if ("com.oceansoft.elearning".equals(packageName))// 云学堂
        {
            callYunXueTangAppliction(packageName);
        }
        else
        {
            
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(pi.packageName);
            PackageManager pm = getPackageManager();
            List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
            ResolveInfo ri = null;
            if (null != apps.iterator())
            {
                
                ri = apps.iterator().next();
            }
            
            if (ri != null)
            {
                
                String packagename = ri.activityInfo.packageName;
                
                String className = ri.activityInfo.name;
                
                Intent intent = new Intent(Intent.ACTION_MAIN);
                
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                
                ComponentName cn = new ComponentName(packageName, className);
                
                intent.setComponent(cn);
                
                startActivity(intent);
            }
        }
        
    }
    
    /**
     * 打开云之家客户端实现免输入用户名密码的Basic登录，如果上次登录成功登录，且用户名与上次登录同，则不会重新登录，主界面刷新有有效性检查机制。 传递参数：userName,password
     * 
     * @return 成功：会注销已登录的用户，并使用授权码指定的用户登录；失败：弹出相应的提示，并支持回掉到第三方调用页面
     * 
     * @since 2.2.0
     * @author winters_huang@kingdee.com
     */
    public void callKdweiboApplictionUseBasicAuth(String packageName)
    {
        String username = GlobalState.getInstance().getOpenId();
        ContentValues values =
            GlobalState.getInstance().getFromUerInfoTable(username, ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        String password = "";
        if (null != values)
        {
            password = (String)values.get(TUSERINFO.PASSWORD);
            password = AesUtil.decrypt(password, NetRequestController.MOA_deviceKey);
        }
        Intent intent = new Intent();
        intent.putExtra("auth", "true"); // 是否需要登录，默认为false，如果传true，则先注销原有授权再重新登录，不能实现数据缓存
        intent.putExtra("source", "ssodemo"); // 云之家客户端根据此来源实现专有逻辑，如网络初始化
        intent.putExtra("user_name", username); // Basic 认证的用户名// "test@kdweibo.cn"
        intent.putExtra("password", password); // Basic 认证的密码
        intent.putExtra("callback", "ssodemo://main_view"); // 登录失败会调界面，请定义页面的scheme
        // 某些私有云部署项目启动页面activities.ACT_Start 不一定在packageName目录，按需调整
        intent.setComponent(new ComponentName(packageName, packageName + ".activities.ACT_Start"));
        startActivity(intent);
    }
    
    /**
     * 
     * 云学堂
     * 
     * @Description<功能详细描述>
     * 
     * @param packageName
     * @LastModifiedDate：2014-4-17
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void callYunXueTangAppliction(String packageName)
    {
        Intent intent = new Intent();
        
        // intent.setAction("com.oceansoft.elearning.ExternalLogin");
        ComponentName cn =
            new ComponentName("com.oceansoft.elearning", "com.oceansoft.module.guide.ExternalLoginActivity");
        intent.setComponent(cn);
        
        intent.putExtra("userName", GlobalState.getInstance().getOpenId());// "suncongwen"
        intent.putExtra("appKey", "BA660DBD-7650-4A83-AED9-CAE5889B38F3");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        
        startActivity(intent);
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
        
    }
    
    /**
     * 重载方法
     * 
     * @param arg0
     * @param arg1
     * @return
     * @author li_miao
     */
    @Override
    public boolean onTouch(View arg0, MotionEvent arg1)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * 重载方法
     * 
     * @param keyCode
     * @param event
     * @return
     * @author wang_ling
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitDialog();
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /** 退出应用对话框 **/
    private CustomDialog dialog;
    
    /**
     * 
     * 退出应用
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-3-27
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void exitDialog()
    {
        dialog =
            CustomDialog.newInstance(this.getResources().getString(R.string.main_exit_app), this.getResources()
                .getString(R.string.Login_Cancel), this.getResources().getString(R.string.Login_Confirm));
        dialog.show(this.getSupportFragmentManager(), "ExitDialog");
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
}
