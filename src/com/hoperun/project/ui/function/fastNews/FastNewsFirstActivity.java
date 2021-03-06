/*
 * File name:  FastNewsFirstActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-3-26
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.fastNews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.adpter.fastNews.FastNewsListAdapter;
import com.hoperun.manager.components.DropDownRefreshListView;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityMetaData.GetFastNewsListInfo;
import com.hoperun.mipmanager.model.entityModule.nettv.VideoListItem;
import com.hoperun.mipmanager.model.entityModule.offical.GWDocModule;
import com.hoperun.mipmanager.model.entityModule.schedule.ScheduleInfo;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;
import com.hoperun.project.ui.baseui.baseInterface.IFragmentToMainActivityListen;
import com.hoperun.project.ui.baseui.fragment.PMIPCustomFragment;

/**
 * 今日快报一级页面
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-26]
 */
public class FastNewsFirstActivity extends PMIPBaseActivity implements IFragmentToMainActivityListen, OnTouchListener,
    OnItemClickListener, OnClickListener
{
    /** 快报新闻 一级栏目adpter **/
    private FastNewsListAdapter     fileListAdapter;
    
    private FastNewsSecondFragment  mFastNewsSecondFragment;
    
    /**
     * fragment事务管理
     */
    private FragmentTransaction     fragmentTransaction;
    
    private FrameLayout             mThirdLevelLayout;
    
    /**
     * fragment管理器
     */
    private FragmentManager         mFragmentManager;
    
    private PMIPCustomFragment      mTopFragment;
    
    /**
     * 该类栏目的标题
     */
    private TextView                tv_title;
    
    /** 等待框布局 **/
    private RelativeLayout          loadLayout;
    
    /** 等待图片 **/
    private ImageView               loadImageView;
    
    /**
     * 新闻中心 一级菜单列表
     */
    private DropDownRefreshListView mLvMain;
    
    /** 所在模块的文件路径 **/
    private String                  parentsPath;
    
    /** 所在模块的id **/
    private String                  funcId;
    
    /** 所在模块的名字 **/
    private String                  funName;
    
    /** 文件主目录 **/
    private String                  path;
    
    /** 查询回来的list数据 **/
    private MetaResponseBody        responseBuzBody;
    
    /** 是否离线 true为离线，false为在线 **/
    private boolean                 offLine                     = false;
    
    /** 用户名 **/
    private String                  user;
    
    /**
     * 正在加载过程中
     */
    private boolean                 isLoading                   = false;
    
    /**
     * 该页面是否可以关闭该主页面
     */
    private boolean                 isCanClose                  = true;
    
    /**
     * 返回按钮
     */
    protected ImageView             mFragmentBack;
    
    private Animation               mRightIn;
    
    private NetTask                 mGetDocModuleHttpCreator    = null;
    
    /**
     * 触摸滑动监听
     */
    protected OnTouchListener       mViewLandscapeSlideListener = new OnTouchListener()
                                                                {
                                                                    
                                                                    @Override
                                                                    public boolean onTouch(View v, MotionEvent event)
                                                                    {
                                                                        // TODO Auto-generated method stub
                                                                        return false;
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
    @SuppressWarnings({"unchecked", "unused"})
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETDOCMODULELIST:
                    closeProgressDialog();
                    if (objBody != null)
                    {
                        responseBuzBody = (MetaResponseBody)objBody;
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        
                        // List<HashMap<String, Object>> bizData =
                        // (List<HashMap<String, Object>>)ret.get(0).get("webitems");
                        List<HashMap<String, Object>> wetitem =
                            (List<HashMap<String, Object>>)ret.get(0).get("webitems");
                        if (wetitem != null)
                        {
                            // List<HashMap<String, Object>> wetitem =
                            // (List<HashMap<String, Object>>)bizData.get(0).get("webitems");
                            GlobalState.getInstance().setmFastNewsModule(wetitem);
                            if (null == wetitem)
                            {
                                wetitem = new ArrayList<HashMap<String, Object>>();
                            }
                            
                            fileListAdapter = new FastNewsListAdapter(this, wetitem);
                            
                            mLvMain.setAdapter(fileListAdapter);
                            
                            new Thread(new InsertList(wetitem)).start();
                        }
                        
                    }
                    else
                    {
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
     * @param savedInstanceState
     * @author wang_ling
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.office_doc_main);
        initIntentData();
        initView();
        
        initData();
        getDocModuleList();
    }
    
    private void getDocModuleList()
    {
        // // 如果之前已经有了“新闻快报”的分组目录，则不再从服务器获取
        if (GlobalState.getInstance().getmFastNewsModule() != null
            && GlobalState.getInstance().getmFastNewsModule().size() > 0)
        {
            List<HashMap<String, Object>> wetitem = GlobalState.getInstance().getmFastNewsModule();
            
            fileListAdapter = new FastNewsListAdapter(this, wetitem);
            
            mLvMain.setAdapter(fileListAdapter);
            return;
        }
        LogUtil.i("", "******animationOver()");
        
        if (offLine)
        {
            // 从数据库中获取相数据对象,查询所有的数据
            GetFastNewsListInfo testObjectfromDB = new GetFastNewsListInfo(user, "", "");
            String[] str = {user, ""};
            
            String where = GetFastNewsListInfo.l_user + " = ? and " + GetFastNewsListInfo.l_parentid + " = ?";
            
            List<HashMap<String, Object>> queryret =
                (List<HashMap<String, Object>>)testObjectfromDB.query(null, where, str, null);
            
            MetaResponseBody m = new MetaResponseBody();
            
            if (queryret != null && queryret.size() != 0)
            {
                GlobalState.getInstance().setmFastNewsModule(queryret);
                fileListAdapter = new FastNewsListAdapter(this, queryret);
                mLvMain.setAdapter(fileListAdapter);
            }
            else
            {
                Toast.makeText(this, "本地没有数据！", Toast.LENGTH_SHORT).show();
            }
            
        }
        else
        {
            mGetDocModuleHttpCreator = new HttpNetFactoryCreator(RequestTypeConstants.GETDOCMODULELIST).create();
            JSONObject body = new JSONObject();
            try
            {
                body.put("token", GlobalState.getInstance().getToken());
                body.put("listId", "");
                NetRequestController.getFastNewsList(mGetDocModuleHttpCreator,
                    mHandler,
                    RequestTypeConstants.GETDOCMODULELIST,
                    body);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            showProgressDialog();
        }
    }
    
    private void initIntentData()
    {
        Intent intent = this.getIntent();
        this.parentsPath = intent.getStringExtra("parentsPath");
        
        this.funcId = intent.getStringExtra("funcId");
        
        this.funName = intent.getStringExtra("funName");
        
        path = ConstState.MIP_ROOT_DIR + parentsPath + "/";
    }
    
    private void initView()
    {
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        mThirdLevelLayout = (FrameLayout)findViewById(R.id.main_third_layout);
        
        tv_title = (TextView)findViewById(R.id.office_title);
        
        mLvMain = (DropDownRefreshListView)findViewById(R.id.office_lv);
        
        mLvMain.setOnTouchListener(mViewLandscapeSlideListener);
        mFragmentBack = (ImageView)findViewById(R.id.office_back);
        mRightIn = AnimationUtils.loadAnimation(this, R.anim.fromrightin);
        mRightIn.setAnimationListener(new AnimationListener()
        {
            
            @Override
            public void onAnimationStart(Animation animation)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onAnimationRepeat(Animation animation)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onAnimationEnd(Animation animation)
            {
                if (mTopFragment != null)
                {
                    mTopFragment.animationOver();
                }
            }
        });
        
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-3-26
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        offLine = GlobalState.getInstance().getOfflineLogin();
        user = GlobalState.getInstance().getOpenId();
        tv_title.setText(funName);
        mFragmentBack.setOnClickListener(this);
        
        mLvMain.setOnItemClickListener(this);
        mFragmentManager = this.getSupportFragmentManager();
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
            default:
                break;
        }
    }
    
    /**
     * 重载方法
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @author wang_ling
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        // 点击时间间隔太短则不触发
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        
        HashMap<String, String> item = (HashMap<String, String>)fileListAdapter.getItem(arg2 - 1);
        
        String funcName = (String)item.get("name");
        String funcCode = (String)item.get("id");
        // String flowtype = (String)item.get("flowType");
        // String searchKeywords = (String)item.get("searchkeywords");
        // String searchtype = (String)item.get("searchtype");
        
        if (funcName == null)
        {
            funcName = "";
        }
        
        if (funcCode == null)
        {
            funcCode = "";
        }
        
        String path2 = path;
        
        fileListAdapter.setmSelectedPosition(arg2 - 1);
        fileListAdapter.notifyDataSetChanged();
        mThirdLevelLayout.startAnimation(mRightIn);
        mThirdLevelLayout.setVisibility(View.VISIBLE);
        mThirdLevelLayout.bringToFront();
        
        mFastNewsSecondFragment = null;
        mFastNewsSecondFragment = FastNewsSecondFragment.newInstance(funcCode, "", path2, funcName);
        
        fragmentTransaction = mFragmentManager.beginTransaction();
        
        fragmentTransaction.replace(R.id.main_third_layout, mFastNewsSecondFragment);
        
        fragmentTransaction.commit();
        
        mTopFragment = mFastNewsSecondFragment;
        
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
        if (keyCode == KeyEvent.KEYCODE_BACK && isCanClose)
        {
            
            if (mTopFragment != null)
            {
                mTopFragment.onKeyDown(keyCode);
            }
            else
            {
                this.finish();
            }
            if (isLoading)
            {
                closeNetRequest(RequestTypeConstants.GETDOCMODULELIST);
                closeProgressDialog();
            }
            
            return true;
        }
        else
        {
            return false;
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
    
    /**
     * 
     * 保存列表
     * 
     * @author shen_feng
     * @Version [版本号, 2013-10-9]
     */
    private class InsertList implements Runnable
    {
        private List<HashMap<String, Object>> listMap;
        
        public InsertList(List<HashMap<String, Object>> listMap)
        {
            this.listMap = listMap;
        }
        
        @Override
        public void run()
        {
            GetFastNewsListInfo test1 = new GetFastNewsListInfo(user, "", "");
            String[] str = {user, ""};
            
            String where = GetFastNewsListInfo.l_user + " = ? and " + GetFastNewsListInfo.l_parentid + " = ?";
            
            test1.delete(where, str);
            
            for (int i = 0; i < listMap.size(); i++)
            {
                GetFastNewsListInfo test = new GetFastNewsListInfo(user, "", "");
                test.convertToObject(listMap.get(i));
                test.insert();
            }
        }
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @param event
     * @return
     * @author wang_ling
     */
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * 重载方法
     * 
     * @param index
     * @param mModlue
     * @param path
     * @author wang_ling
     */
    @Override
    public void onOfficialFragmentSelected(int index, GWDocModule mModlue, String path)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onOfficialFragmentCloseSelected()
    {
        if (mThirdLevelLayout != null)
        {
            mThirdLevelLayout.removeAllViews();
        }
        mTopFragment = null;
        fileListAdapter.setmSelectedPosition(-1);
        fileListAdapter.notifyDataSetChanged();
    }
    
    /**
     * 重载方法
     * 
     * @param index
     * @param list_info
     * @param date
     * @author wang_ling
     */
    @Override
    public void onSchedulePlanSelected(int index, ScheduleInfo list_info, String date)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onScheduleTimeFragmentCloseSelected()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onScheduleChangedListener()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onSecondFragmentClose()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
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
     * @author wang_ling
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
     * @author wang_ling
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
     * @author wang_ling
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
     * @author wang_ling
     */
    @Override
    public void onSendGetUnReadCount(String funId, String funcode)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onCloseHomeViewFragment()
    {
        // TODO Auto-generated method stub
        
    }
    
}
