/*
 * File name:  XwzxFragment.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2014-4-2
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.ygsj;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.hoperun.mip.utils.SystemTools;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityMetaData.GetNextModuleListInfo;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.MessageIdConstants;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.fragment.PMIPBaseSecondFragment;

/**
 * 员工社交
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2014-4-2]
 */
public class YgsjFragment extends PMIPBaseSecondFragment implements OnItemClickListener, OnClickListener
{
    /** 员工社交一级栏目adpter **/
    private XwzxDocListAdapter      fileListAdapter;
    
    /**
     * 该类栏目的标题
     */
    private TextView                tv_title;
    
    /** 等待框布局 **/
    private RelativeLayout          loadLayout;
    
    /** 等待图片 **/
    private ImageView               loadImageView;
    
    /**
     * 员工社交 一级菜单列表
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
    private boolean                 offLine                  = false;
    
    /** 用户名 **/
    private String                  user;
    
    /**
     * 正在加载过程中
     */
    private boolean                 isLoading                = false;
    
    /**
     * 该页面是否可以关闭该主页面
     */
    private boolean                 isCanClose               = true;
    
    /**
     * 获取员工社交一级列表的task实例
     */
    private NetTask                 mGetDocModuleHttpCreator = null;
    
    /**
     * 返回按钮
     */
    protected ImageView             mFragmentBack;
    
    protected RelativeLayout        mBackRL;
    
    protected ImageView             mFragmentSearch;
    
    private FragmentActivity        mActivity;
    
    private RelativeLayout          mHeaderRL;
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void animationOver()
    {
        // TODO Auto-generated method stub
        
    }
    
    public static YgsjFragment newInstance(String parentspath, String funcId, String funName)
    {
        YgsjFragment details = new YgsjFragment();
        
        Bundle bd = new Bundle();
        bd.putString("parentsPath", parentspath);
        bd.putString("funcId", funcId);
        bd.putString("funName", funName);
        
        details.setArguments(bd);
        
        return details;
    }
    
    @Override
    public void onAttach(Activity activity)
    {
        // TODO Auto-generated method stub
        mActivity = (FragmentActivity)activity;
        super.onAttach(activity);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(mActivity).inflate(R.layout.office_doc_main, null);
        
        initIntentData();
        initView(view);
        
        initData();
        getDocModuleList();
        
        return view;
    }
    
    private void getDocModuleList()
    {
        // // 如果之前已经有了“新闻中心”的分组目录，则不再从服务器获取
        if (GlobalState.getInstance().getmYgsjFirstModule() != null
            && GlobalState.getInstance().getmYgsjFirstModule().getBuzList() != null
            && GlobalState.getInstance().getmYgsjFirstModule().getBuzList().size() != 0)
        {
            MetaResponseBody responseBuzBody = GlobalState.getInstance().getmYgsjFirstModule();
            
            fileListAdapter = new XwzxDocListAdapter(mActivity, responseBuzBody);
            
            mLvMain.setAdapter(fileListAdapter);
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
                GlobalState.getInstance().setmYgsjFirstModule(m);
                fileListAdapter = new XwzxDocListAdapter(mActivity, m);
                mLvMain.setAdapter(fileListAdapter);
            }
            else
            {
                Toast.makeText(mActivity, "本地没有数据！", Toast.LENGTH_SHORT).show();
            }
            
        }
        else
        {
            mGetDocModuleHttpCreator = new HttpNetFactoryCreator(RequestTypeConstants.GETDOCMODULELIST).create();
            JSONObject body = new JSONObject();
            try
            {
                body.put("funcid", funcId);
                body.put("type", ConstState.MENU);
                NetRequestController.getNextModuleList(mGetDocModuleHttpCreator,
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
        // Intent intent = this.getIntent();
        Bundle bd = getArguments();
        this.parentsPath = bd.getString("parentsPath");
        
        this.funcId = bd.getString("funcId");
        
        this.funName = bd.getString("funName");
        
        path = ConstState.MIP_ROOT_DIR + parentsPath + "/";
    }
    
    private void initView(View view)
    {
        loadLayout = (RelativeLayout)view.findViewById(R.id.load_layout);
        loadImageView = (ImageView)view.findViewById(R.id.waitdialog_img);
        
        tv_title = (TextView)view.findViewById(R.id.office_title);
        
        mLvMain = (DropDownRefreshListView)view.findViewById(R.id.office_lv);
        
        mLvMain.setOnTouchListener(mViewLandscapeSlideListener);
        mFragmentBack = (ImageView)view.findViewById(R.id.office_back);
        mBackRL = (RelativeLayout)view.findViewById(R.id.office_back_rl);
        mBackRL.setVisibility(View.GONE);
        
        mHeaderRL = (RelativeLayout)view.findViewById(R.id.header);
        mHeaderRL.setOnTouchListener(mViewLandscapeSlideListener);
        
    }
    
    private void initData()
    {
        offLine = GlobalState.getInstance().getOfflineLogin();
        user = GlobalState.getInstance().getOpenId();
        tv_title.setText(funName);
        
        mLvMain.setOnItemClickListener(this);
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
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETDOCMODULELIST:
                    closeProgressDialog();
                    if (objBody != null)
                    {
                        responseBuzBody = (MetaResponseBody)objBody;
                        GlobalState.getInstance().setmYgsjFirstModule(responseBuzBody);
                        
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        
                        fileListAdapter = new XwzxDocListAdapter(mActivity, responseBuzBody);
                        
                        mLvMain.setAdapter(fileListAdapter);
                        
                        new Thread(new InsertList(ret)).start();
                        
                    }
                    else
                    {
                        Toast.makeText(mActivity, "服务器没有数据！", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mActivity, "请求服务器数据失败！", Toast.LENGTH_SHORT).show();
            }
        }
        
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
    public boolean onKeyDown(int keyCode)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && isCanClose)
        {
            
            closeThisFragment();
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
                closeThisFragment();
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
    @SuppressWarnings("unchecked")
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
        String packageName = "";
        if (funcName == null)
        {
            funcName = "";
        }
        
        if (funcCode == null)
        {
            funcCode = "";
        }
        // 云之家
        if (funcCode.equals("YZJ"))
        {
            packageName = "com.kdweibo.client";
        }
        // 移动RTX
        else if (funcCode.equals("RTX"))
        {
            packageName = "com.emoa.mobile";
        }
        // 桥头堡论坛
        else if (funcCode.equals("QTB"))
        {
            packageName = "";
        }
        // 短消息
        else if (funcCode.equals("DXX"))
        {
            packageName = "";
        }
        boolean isInstal = SystemTools.isInstalled(mActivity, packageName);
        if (isInstal)
        {
            SystemTools.startApp(mActivity, packageName);
        }
        else
        {
            Toast.makeText(mActivity, "暂未安装请到我的应用中下载安装该应用", Toast.LENGTH_SHORT).show();
        }
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
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void closeThisFragment()
    {
        // TODO Auto-generated method stub
        if (mFragmentTMainActivityListener != null)
        {
            mFragmentTMainActivityListener.onSecondFragmentClose();
        }
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void theThirdFragmentClose()
    {
        // TODO Auto-generated method stub
        
    }
    
}
