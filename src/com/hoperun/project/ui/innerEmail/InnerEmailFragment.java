/*
 * File name:  InnerEmailFragment.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-2-17
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.innerEmail;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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

import com.artifex.BaseUtils.onViewBackListen;
import com.hoperun.manager.adpter.innerEmail.InnerEmailDocListAdapter;
import com.hoperun.manager.components.DropDownRefreshListView;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.mipmanager.model.entityMetaData.GetNextModuleListInfo;
import com.hoperun.mipmanager.model.entityModule.offical.GWDocModule;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.MessageIdConstants;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.fragment.PMIPBaseSecondFragment;

/**
 * 内部邮件一级菜单
 * 
 * @Description 内部邮件一级菜单
 * 
 * @author wang_ling
 * @Version [版本号, 2014-2-17]
 */
public class InnerEmailFragment extends PMIPBaseSecondFragment implements OnItemClickListener, OnClickListener
{
    /** 外层布局 **/
    private RelativeLayout           parentRl;
    
    private NewMsgView               mMsgView;
    
    /**
     * 内部邮件一级栏目adapter
     */
    private InnerEmailDocListAdapter emailListAdapter;
    
    /** 应用上下文 **/
    private Activity                 mActivity;
    
    /**
     * 该类栏目的标题
     */
    private TextView                 tv_title;
    
    /** 等待框布局 **/
    private RelativeLayout           loadLayout;
    
    /** 等待图片 **/
    private ImageView                loadImageView;
    
    /**
     * 内部邮件一级菜单列表
     */
    private DropDownRefreshListView  mLvMain;
    
    /** 所在模块的文件路径 **/
    private String                   parentsPath;
    
    /** 所在模块的id **/
    private String                   funcId;
    
    /** 所在模块的名字 **/
    private String                   funName;
    
    /** 文件主目录 **/
    private String                   path;
    
    /** 查询回来的list数据 **/
    private MetaResponseBody         responseBuzBody;
    
    /** 是否离线 true为离线，false为在线 **/
    private boolean                  offLine                  = false;
    
    /** 用户名 **/
    private String                   user;
    
    /**
     * 正在加载过程中
     */
    private boolean                  isLoading                = false;
    
    /**
     * 该页面是否可以关闭该主页面
     */
    private boolean                  isCanClose               = true;
    
    /**
     * 获取文件库一级列表的task实例
     */
    private NetTask                  mGetDocModuleHttpCreator = null;
    
    /**
     * 返回按钮
     */
    protected ImageView              mFragmentBack;
    
    protected ImageView              mFragmentSearch;
    
    /**
     * 重载方法
     * 
     * @param savedInstanceState
     * @author wang_ling
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.mActivity = getActivity();
    }
    
    /**
     * 
     * 实例化
     * 
     * @Description 实例化
     * 
     * @param funcId 功能id
     * @param parentsPath 路径
     * @return fragment
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static InnerEmailFragment newInstance(String funcId, String funName, String parentsPath)
    {
        InnerEmailFragment details = new InnerEmailFragment();
        Bundle args = new Bundle();
        args.putString("funcId", funcId);
        args.putString("funName", funName);
        args.putString("parentsPath", parentsPath);
        details.setArguments(args);
        return details;
    }
    
    /**
     * 重载方法
     * 
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * @author wang_ling
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (container == null)
        {
            return null;
        }
        View v = inflater.inflate(R.layout.office_doc_main, null);
        parentRl = (RelativeLayout)v.findViewById(R.id.parentRl);
        loadLayout = (RelativeLayout)v.findViewById(R.id.load_layout);
        loadImageView = (ImageView)v.findViewById(R.id.waitdialog_img);
        
        tv_title = (TextView)v.findViewById(R.id.office_title);
        
        mLvMain = (DropDownRefreshListView)v.findViewById(R.id.office_lv);
        
        mLvMain.setOnTouchListener(mViewLandscapeSlideListener);
        mFragmentBack = (ImageView)v.findViewById(R.id.office_back);
        mFragmentBack.setOnClickListener(this);
        initData();
        return v;
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onResume()
    {
        super.onResume();
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description 初始化数据
     * 
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        offLine = GlobalState.getInstance().getOfflineLogin();
        
        user = GlobalState.getInstance().getOpenId();
        
        this.parentsPath = getArguments().getString("parentsPath");
        
        this.funcId = getArguments().getString("funcId");
        
        this.funName = getArguments().getString("funName");
        
        path = ConstState.MIP_ROOT_DIR + parentsPath + "/";
        
        tv_title.setText(funName);
        mLvMain.setOnItemClickListener(this);
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    
    /**
     * 重载方法
     * 
     * @param activity
     * @author wang_ling
     */
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }
    
    /**
     * 重载方法
     * 
     * @param keyId
     * @return
     * @author wang_ling
     */
    @Override
    public boolean onKeyDown(int keyId)
    {
        if (keyId == KeyEvent.KEYCODE_BACK && isCanClose)
        {
            
            if (mMsgView != null && mMsgView.getVisibility() == View.VISIBLE)
            {
                if (mMsgView.getmLinkerView() != null && mMsgView.getmLinkerView().getVisibility() == View.VISIBLE)
                {
                    mMsgView.removeView(mMsgView.getmLinkerView());
                    mMsgView.setmLinkerView(null);
                }
                else if (mMsgView.getMaddAttachView() != null
                    && mMsgView.getMaddAttachView().getVisibility() == View.VISIBLE)
                {
                    mMsgView.removeView(mMsgView.getMaddAttachView());
                    mMsgView.setMaddAttachView(null);
                }
                else
                {
                    parentRl.removeView(mMsgView);
                    mMsgView = null;
                }
                return true;
            }
            else
            {
                if (isLoading)
                {
                    closeNetRequest(RequestTypeConstants.GETDOCMODULELIST);
                    closeProgressDialog();
                }
                closeThisFragment();
                return true;
            }
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void animationOver()
    {
        getDocModuleList();
    }
    
    /**
     * 
     * 获取内部邮件一级功能模块
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void getDocModuleList()
    {
        // 如果之前已经有了“内部邮件”的分组目录，则不再从服务器获取
        if (GlobalState.getInstance().getmNbyjFirstModule() != null
            && GlobalState.getInstance().getmNbyjFirstModule().getBuzList() != null
            && GlobalState.getInstance().getmNbyjFirstModule().getBuzList().size() != 0)
        {
            MetaResponseBody responseBuzBody = GlobalState.getInstance().getmNbyjFirstModule();
            
            emailListAdapter = new InnerEmailDocListAdapter(mActivity, responseBuzBody);
            
            mLvMain.setAdapter(emailListAdapter);
            return;
        }
        LogUtil.i("", "******animationOver()");
        
        if (offLine)
        {
            // 从数据库中获取相数据对象,查询所有的数据
            GetNextModuleListInfo testObjectfromDB =
                new GetNextModuleListInfo(MessageIdConstants.GETINNEREMAILMODULELIST, user, funName);
            String[] str = {user, funName};
            
            String where = GetNextModuleListInfo.l_user + " = ? and " + GetNextModuleListInfo.l_funcname + " = ?";
            List<HashMap<String, Object>> queryret =
                (List<HashMap<String, Object>>)testObjectfromDB.query(null, where, str, null);
            
            MetaResponseBody m = new MetaResponseBody();
            
            if (queryret != null && queryret.size() != 0)
            {
                m.setBuzList(queryret);
                GlobalState.getInstance().setmNbyjFirstModule(m);
                emailListAdapter = new InnerEmailDocListAdapter(mActivity, m);
                mLvMain.setAdapter(emailListAdapter);
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
    
    /**
     * 
     * 停止请求
     * 
     * @Description<功能详细描述>
     * 
     * @param requestType 请求的id
     * @LastModifiedDate：2013-11-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void closeNetRequest(int requestType)
    {
        NetRequestController.stopCurrentNetTask(mGetDocModuleHttpCreator);
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
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        // 点击时间间隔太短则不触发
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        
        // 点击栏目模快
        
        HashMap<String, String> item = (HashMap<String, String>)emailListAdapter.getItem(arg2 - 1);
        
        String funcName = (String)item.get("funcname");
        String funcCode = (String)item.get("funccode");
        String flowtype = (String)item.get("flowType");
        String searchKeywords = (String)item.get("searchkeywords");
        String searchtype = (String)item.get("searchtype");
        
        GWDocModule mModlue = new GWDocModule();
        
        if (funcName == null)
        {
            funcName = "";
        }
        
        if (funcCode == null)
        {
            funcCode = "";
        }
        
        if (flowtype == null)
        {
            flowtype = "";
        }
        
        if (searchKeywords == null)
        {
            searchKeywords = "";
        }
        
        if (searchtype == null)
        {
            searchtype = "";
        }
        
        mModlue.setFunccode(funcCode);
        mModlue.setFuncName(funcName);
        mModlue.setFlowtype(flowtype);
        mModlue.setSearchkeywords(searchKeywords);
        mModlue.setSearchtype(searchtype);
        
        String path2 = path + funcName + "/";
        if ("SJX".equals(mModlue.getFunccode()) || "FJX".equals(mModlue.getFunccode()))
        {
            
            mFragmentTMainActivityListener.onOfficialFragmentSelected(arg2 - 1, mModlue, path2);
        }
        // 发送邮件
        else
        {
            if (mMsgView != null)
            {
                parentRl.removeView(mMsgView);
            }
            mMsgView = null;
            mMsgView = new NewMsgView(mActivity, 100);
            mMsgView.setMviewbacklistener(mViewBacklisten);
            parentRl.addView(mMsgView);
            mMsgView.bringToFront();
            mMsgView.invalidate();
        }
        emailListAdapter.setmSelectedPosition(arg2 - 1);
        emailListAdapter.notifyDataSetChanged();
        
    }
    
    onViewBackListen mViewBacklisten = new onViewBackListen()
                                     {
                                         
                                         @Override
                                         public void onViewBackListener(int type)
                                         {
                                             switch (type)
                                             {
                                                 case ConstState.SEND_CANCEL:
                                                 case ConstState.SEND_SUCCESS:
                                                     if (mMsgView != null)
                                                     {
                                                         parentRl.removeView(mMsgView);
                                                         mMsgView = null;
                                                     }
                                                     break;
                                                 
                                                 default:
                                                     break;
                                             }
                                         }
                                     };
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void closeThisFragment()
    {
        if (mFragmentTMainActivityListener != null)
        {
            mFragmentTMainActivityListener.onSecondFragmentClose();
        }
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void theThirdFragmentClose()
    {
        emailListAdapter.setmSelectedPosition(-1);
        emailListAdapter.notifyDataSetInvalidated();
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
                        GlobalState.getInstance().setmNbyjFirstModule(responseBuzBody);
                        
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        emailListAdapter = new InnerEmailDocListAdapter(mActivity, responseBuzBody);
                        
                        mLvMain.setAdapter(emailListAdapter);
                        
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
     * 
     * 保存列表
     * 
     * @Description 保存列表
     * @author wang_ling
     * @Version [版本号, 2013-11-18]
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
            GetNextModuleListInfo test1 =
                new GetNextModuleListInfo(MessageIdConstants.GETINNEREMAILMODULELIST, user, funName);
            String[] str = {user, funName};
            
            String where = GetNextModuleListInfo.l_user + " = ? and " + GetNextModuleListInfo.l_funcname + " = ?";
            
            test1.delete(where, str);
            
            for (int i = 0; i < listMap.size(); i++)
            {
                GetNextModuleListInfo test =
                    new GetNextModuleListInfo(MessageIdConstants.GETINNEREMAILMODULELIST, user, funName);
                test.convertToObject(listMap.get(i));
                test.insert();
            }
        }
    }
}
