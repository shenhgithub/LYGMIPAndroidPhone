/*
 * File name:  BusinessManagementActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-4-2
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.ywbl;

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

import com.hoperun.manager.adpter.xwzx.XwzxDocListAdapter;
import com.hoperun.manager.components.DropDownRefreshListView;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityMetaData.GetNextModuleListInfo;
import com.hoperun.mipmanager.model.entityModule.nettv.VideoListItem;
import com.hoperun.mipmanager.model.entityModule.offical.GWDocModule;
import com.hoperun.mipmanager.model.entityModule.schedule.ScheduleInfo;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.MessageIdConstants;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;
import com.hoperun.project.ui.baseui.baseInterface.IFragmentToMainActivityListen;
import com.hoperun.project.ui.baseui.fragment.PMIPCustomFragment;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-4-2]
 */
public class BusinessManagementActivity extends PMIPBaseActivity implements IFragmentToMainActivityListen,
    OnTouchListener, OnItemClickListener, OnClickListener
{
    
    /**
     * 所在模块id
     */
    private String                  funcId;
    
    /**
     * 所在模块的名字
     */
    private String                  funName;
    
    /**
     * 是否在线
     */
    private boolean                 offLine                     = false;
    
    /**
     * 用户名
     */
    private String                  user;
    
    /**
     * 该类栏目的标题
     */
    private TextView                title;
    
    /**
     * fragment管理器
     */
    private FragmentManager         mFragmentManager;
    
    private NetTask                 managementTaskCreater       = null;
    
    /** 等待框布局 **/
    private RelativeLayout          loadLayout;
    
    /** 等待图片 **/
    private ImageView               loadImageView;
    
    /**
     * 正在加载中
     */
    private boolean                 isLoading                   = false;
    
    /**
     * 返回按钮
     */
    protected ImageView             mFragmentBack;
    
    /**
     * 业务办理 一级菜单列表
     */
    private DropDownRefreshListView mBmMain;
    
    /** 新闻中心 一级栏目adpter **/
    private XwzxDocListAdapter      fileListAdapter;
    
    /** 查询回来的list数据 **/
    private MetaResponseBody        responseBuzBody;
    
    /** 所在模块的文件路径 **/
    private String                  parentsPath;
    
    /** 文件主目录 **/
    private String                  path;
    
    private FrameLayout             mThirdLevelLayout;
    
    private Animation               mRightIn;
    
    private PMIPCustomFragment      mTopFragment;
    
    private YwblSecondFragment      mYwblSecondFragment;
    
    /**
     * fragment事务管理
     */
    private FragmentTransaction     fragmentTransaction;
    
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
    
    private void initIntentData()
    {
        Intent intent = this.getIntent();
        this.parentsPath = intent.getStringExtra("parentsPath");
        this.funcId = intent.getStringExtra("funcId");
        
        this.funName = intent.getStringExtra("funName");
        path = ConstState.MIP_ROOT_DIR + parentsPath + "/";
    }
    
    private void initData()
    {
        offLine = GlobalState.getInstance().getOfflineLogin();
        user = GlobalState.getInstance().getOpenId();
        title.setText(funName);
        mFragmentBack.setOnClickListener(this);
        
        mBmMain.setOnItemClickListener(this);
        mFragmentManager = this.getSupportFragmentManager();
    }
    
    private void initView()
    {
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        title = (TextView)findViewById(R.id.office_title);
        mFragmentBack = (ImageView)findViewById(R.id.office_back);
        mBmMain = (DropDownRefreshListView)findViewById(R.id.office_lv);
        mBmMain.setOnTouchListener(mViewLandscapeSlideListener);
        mThirdLevelLayout = (FrameLayout)findViewById(R.id.main_third_layout);
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
    
    private void getBusinessList()
    {
        
        // // 如果之前已经有了“新闻中心”的分组目录，则不再从服务器获取
        if (GlobalState.getInstance().getmYwblFirstModule() != null
            && GlobalState.getInstance().getmYwblFirstModule().getBuzList() != null
            && GlobalState.getInstance().getmYwblFirstModule().getBuzList().size() != 0)
        {
            MetaResponseBody responseBuzBody = GlobalState.getInstance().getmYwblFirstModule();
            
            fileListAdapter = new XwzxDocListAdapter(this, responseBuzBody);
            
            mBmMain.setAdapter(fileListAdapter);
            return;
        }
        LogUtil.i("", "******animationOver()");
        
        if (offLine)
        {
            // 从数据库中获取相数据对象,查询所有的数据
            GetNextModuleListInfo testObjectfromDB =
                new GetNextModuleListInfo(MessageIdConstants.GETDOCMODULELIST, user, funName);
            String[] str = {user, funName};
            
            String where = GetNextModuleListInfo.l_user + " = ? and " + GetNextModuleListInfo.l_funcname + " = ?";
            
            List<HashMap<String, Object>> queryret =
                (List<HashMap<String, Object>>)testObjectfromDB.query(null, where, str, null);
            
            MetaResponseBody m = new MetaResponseBody();
            
            if (queryret != null && queryret.size() != 0)
            {
                m.setBuzList(queryret);
                GlobalState.getInstance().setmYwblFirstModule(m);
                fileListAdapter = new XwzxDocListAdapter(this, m);
                mBmMain.setAdapter(fileListAdapter);
            }
            else
            {
                Toast.makeText(this, "本地没有数据！", Toast.LENGTH_SHORT).show();
            }
            
        }
        else
        {
            
            managementTaskCreater = new HttpNetFactoryCreator(RequestTypeConstants.GETDOCMODULELIST).create();
            JSONObject body = new JSONObject();
            try
            {
                body.put("funcid", funcId);
                body.put("type", ConstState.MENU);
                NetRequestController.getNextModuleList(managementTaskCreater,
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
    
    private void showProgressDialog()
    {
        loadLayout.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable)loadImageView.getBackground();
        animationDrawable.start();
        isLoading = true;
    }
    
    private void closeProgressDialog()
    {
        loadLayout.setVisibility(View.GONE);
        isLoading = false;
    }
    
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
        setContentView(R.layout.office_doc_main);
        initIntentData();
        initView();
        initData();
        getBusinessList();
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @author chen_wei3
     */
    @Override
    public void onClick(View v)
    {
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
     * 重载方法
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @author chen_wei3
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
        String funcName = (String)item.get("funcname");
        String funcCode = (String)item.get("funccode");
        Intent intent = new Intent(this, KbclListActivity.class);
        if ("KBCL".equals(funcCode))
        {
            intent.putExtra("blokcNum", 2);
        }
        else if ("ZYCL".equals(funcCode))
        {
            intent.putExtra("blokcNum", 3);
        }
        else if ("LBCL".equals(funcCode))
        {
            intent.putExtra("blokcNum", 4);
        }
        else if ("LGCL".equals(funcCode))
        {
            intent.putExtra("blokcNum", 5);
        }
        intent.putExtra("funname", funcName);
        intent.putExtra("funcode", funcCode);
        startActivity(intent);
        // String flowtype = (String)item.get("flowType");
        // String searchKeywords = (String)item.get("searchkeywords");
        // String searchtype = (String)item.get("searchtype");
        // GWDocModule mModlue = new GWDocModule();
        // if (funcName == null)
        // {
        // funcName = "";
        // }
        //
        // if (funcCode == null)
        // {
        // funcCode = "";
        // }
        //
        // if (flowtype == null)
        // {
        // flowtype = "";
        // }
        //
        // if (searchKeywords == null)
        // {
        // searchKeywords = "";
        // }
        //
        // if (searchtype == null)
        // {
        // searchtype = "";
        // }
        // mModlue.setFunccode(funcCode);
        // mModlue.setFuncName(funcName);
        // mModlue.setFlowtype(flowtype);
        // mModlue.setSearchkeywords(searchKeywords);
        // mModlue.setSearchtype(searchtype);
        // String path2 = path;
        // fileListAdapter.setmSelectedPosition(arg2 - 1);
        // fileListAdapter.notifyDataSetChanged();
        // mThirdLevelLayout.startAnimation(mRightIn);
        // mThirdLevelLayout.setVisibility(View.VISIBLE);
        // mThirdLevelLayout.bringToFront();
        // mYwblSecondFragment = null;
        // mYwblSecondFragment =
        // YwblSecondFragment.newInstance(mModlue.getFunccode(),
        // mModlue.getSearchkeywords(),
        // path2,
        // mModlue.getFuncName());
        // fragmentTransaction = mFragmentManager.beginTransaction();
        // fragmentTransaction.replace(R.id.main_third_layout, mYwblSecondFragment);
        //
        // fragmentTransaction.commit();
        //
        // mTopFragment = mYwblSecondFragment;
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @param event
     * @return
     * @author chen_wei3
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
     * @author chen_wei3
     */
    @Override
    public void onOfficialFragmentSelected(int index, GWDocModule mModlue, String path)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author chen_wei3
     */
    @Override
    public void onOfficialFragmentCloseSelected()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param index
     * @param list_info
     * @param date
     * @author chen_wei3
     */
    @Override
    public void onSchedulePlanSelected(int index, ScheduleInfo list_info, String date)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author chen_wei3
     */
    @Override
    public void onScheduleTimeFragmentCloseSelected()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author chen_wei3
     */
    @Override
    public void onScheduleChangedListener()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author chen_wei3
     */
    @Override
    public void onSecondFragmentClose()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author chen_wei3
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
     * @author chen_wei3
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
     * @author chen_wei3
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
     * @author chen_wei3
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
     * @author chen_wei3
     */
    @Override
    public void onSendGetUnReadCount(String funId, String funcode)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author chen_wei3
     */
    @Override
    public void onCloseHomeViewFragment()
    {
        // TODO Auto-generated method stub
        
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
                case RequestTypeConstants.GETDOCMODULELIST:
                    closeProgressDialog();
                    if (objBody != null)
                    {
                        responseBuzBody = (MetaResponseBody)objBody;
                        GlobalState.getInstance().setmYwblFirstModule(responseBuzBody);
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        fileListAdapter = new XwzxDocListAdapter(this, responseBuzBody);
                        mBmMain.setAdapter(fileListAdapter);
                        new Thread(new InsertList(ret)).start();
                        
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
            GetNextModuleListInfo test1 = new GetNextModuleListInfo(MessageIdConstants.GETDOCMODULELIST, user, funName);
            String[] str = {user, funName};
            
            String where = GetNextModuleListInfo.l_user + " = ? and " + GetNextModuleListInfo.l_funcname + " = ?";
            
            test1.delete(where, str);
            
            for (int i = 0; i < listMap.size(); i++)
            {
                GetNextModuleListInfo test =
                    new GetNextModuleListInfo(MessageIdConstants.GETDOCMODULELIST, user, funName);
                test.convertToObject(listMap.get(i));
                test.insert();
            }
        }
    }
}
