/*
 * File name:  FaseNewsFragement.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2014-4-2
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.fastNews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.hoperun.mipmanager.model.entityModule.offical.GWDocModule;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.fragment.PMIPBaseSecondFragment;
import com.hoperun.project.ui.function.lowcport.LowcPortShowActivity;
import com.hoperun.project.ui.xwzx.XwzxDetailListActivity;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2014-4-2]
 */
public class FaseNewsFragement extends PMIPBaseSecondFragment implements OnItemClickListener, OnClickListener
{
    /** 快报新闻 一级栏目adpter **/
    private FastNewsListAdapter           fileListAdapter;
    
    /**
     * 该类栏目的标题
     */
    private TextView                      tv_title;
    
    /** 等待框布局 **/
    private RelativeLayout                loadLayout;
    
    /** 等待图片 **/
    private ImageView                     loadImageView;
    
    /**
     * 新闻中心 一级菜单列表
     */
    private DropDownRefreshListView       mLvMain;
    
    /** 所在模块的文件路径 **/
    private String                        parentsPath;
    
    /** 所在模块的id **/
    private String                        funcId;
    
    /** 所在模块的名字 **/
    private String                        funName;
    
    /** 文件主目录 **/
    private String                        path;
    
    /** 查询回来的list数据 **/
    private MetaResponseBody              responseBuzBody;
    
    /** 是否离线 true为离线，false为在线 **/
    private boolean                       offLine                  = false;
    
    /** 用户名 **/
    private String                        user;
    
    /**
     * 正在加载过程中
     */
    private boolean                       isLoading                = false;
    
    /**
     * 该页面是否可以关闭该主页面
     */
    private boolean                       isCanClose               = true;
    
    /**
     * 返回按钮
     */
    protected ImageView                   mFragmentBack;
    
    protected RelativeLayout              mBackRL;
    
    private Animation                     mRightIn;
    
    private NetTask                       mGetDocModuleHttpCreator = null;
    
    /** 获取安全日报等模块 **/
    private NetTask                       mGetModuleTwo            = null;
    
    private FragmentActivity              mActivity;
    
    /** 返回结果 **/
    private List<HashMap<String, Object>> resultList               = new ArrayList<HashMap<String, Object>>();
    
    public static FaseNewsFragement newInstance(String parentspath, String funcId, String funName)
    {
        FaseNewsFragement details = new FaseNewsFragement();
        
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
        // return super.onCreateView(inflater, container, savedInstanceState);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.office_doc_main, null);
        
        initIntentData();
        initView(view);
        
        initData();
        getDocModuleList();
        
        return view;
    }
    
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
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void closeThisFragment()
    {
        // TODO Auto-generated method stub
        
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
    
    /**
     * 重载方法
     * 
     * @param requestType
     * @param objHeader
     * @param objBody
     * @param error
     * @param errorCode
     * @author ren_qiujing
     */
    @SuppressWarnings({"unused", "unchecked"})
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        // TODO Auto-generated method stub
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETDOCMODULELIST:
                    responseBuzBody = (MetaResponseBody)objBody;
                    if (responseBuzBody != null && responseBuzBody.getRetError().equals("0"))
                    {
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        List<HashMap<String, Object>> wetitem =
                            (List<HashMap<String, Object>>)ret.get(0).get("webitems");
                        if (!ret.get(0).get("webitems").equals(""))
                        {
                            if (null == wetitem)
                            {
                                wetitem = new ArrayList<HashMap<String, Object>>();
                            }
                            resultList.addAll(wetitem);
                            // GlobalState.getInstance().setmFastNewsModule(wetitem);
                            // fileListAdapter = new FastNewsListAdapter(mActivity, wetitem);
                            //
                            // mLvMain.setAdapter(fileListAdapter);
                            //
                            // new Thread(new InsertList(wetitem)).start();
                            
                        }
                        else
                        {
                            Toast.makeText(mActivity, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                        }
                        getModuleListTwo();
                    }
                    else
                    {
                        closeProgressDialog();
                        Toast.makeText(mActivity, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    
                    break;
                // 获取安全日报等模块
                case RequestTypeConstants.SCHEDULE_ALL_REQUEST:
                    closeProgressDialog();
                    responseBuzBody = (MetaResponseBody)objBody;
                    if (responseBuzBody != null && responseBuzBody.getRetError().equals("0"))
                    {
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        if (ret != null && ret.size() > 0)
                        {
                            resultList.addAll(0, ret);
                        }
                        GlobalState.getInstance().setmFastNewsModule(resultList);
                        fileListAdapter = new FastNewsListAdapter(mActivity, resultList);
                        
                        mLvMain.setAdapter(fileListAdapter);
                        
                        new Thread(new InsertList(resultList)).start();
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
     * 获取安全日报等模块
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-23
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void getModuleListTwo()
    {
        mGetModuleTwo = new HttpNetFactoryCreator(RequestTypeConstants.SCHEDULE_ALL_REQUEST).create();
        JSONObject body = new JSONObject();
        try
        {
            body.put("funcid", funcId);
            body.put("type", ConstState.MENU);
            NetRequestController.getNextModuleList(mGetModuleTwo,
                mHandler,
                RequestTypeConstants.SCHEDULE_ALL_REQUEST,
                body);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    
    private void getDocModuleList()
    {
        // // 如果之前已经有了“新闻快报”的分组目录，则不再从服务器获取
        if (GlobalState.getInstance().getmFastNewsModule() != null
            && GlobalState.getInstance().getmFastNewsModule().size() > 0)
        {
            List<HashMap<String, Object>> wetitem = GlobalState.getInstance().getmFastNewsModule();
            
            fileListAdapter = new FastNewsListAdapter(mActivity, wetitem);
            
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
                fileListAdapter = new FastNewsListAdapter(mActivity, queryret);
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
                mActivity.finish();
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
        
        String funCode = item.get("funccode");
        if (null != funCode && funCode.equals(ConstState.XWZX_AQRB))
        {
            itemClickTwo(item);
        }
        else
        {
            itemtClickOne(item);
        }
        
    }
    
    /**
     * 
     * 跳转到安全日报
     * 
     * @Description<功能详细描述>
     * 
     * @param item
     * @LastModifiedDate：2014-4-23
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void itemClickTwo(HashMap<String, String> item)
    {
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
        
        // String path2 = path + funcName + "/";
        String path2 = path;
        
        Bundle bd = new Bundle();
        bd.putSerializable("module", mModlue);
        
        Intent intent = new Intent();
        intent.putExtra("module", bd);
        intent.putExtra("path", path2);
        
        intent.setClass(mActivity, XwzxDetailListActivity.class);
        startActivity(intent);
    }
    
    /**
     * 
     * 跳转到今日快报中其他
     * 
     * @Description<功能详细描述>
     * 
     * @param item
     * @LastModifiedDate：2014-4-23
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void itemtClickOne(HashMap<String, String> item)
    {
        String funcName = (String)item.get("name");
        String funcId = (String)item.get("id");
        String url = (String)item.get("url");
        if (funcName == null)
        {
            funcName = "";
        }
        
        if (funcId == null)
        {
            funcId = "";
        }
        
        String path2 = path;
        
        // Intent intent = new Intent();
        // intent.putExtra("funcCode", funcId);
        // intent.putExtra("path", path2);
        // intent.putExtra("funcName", funcName);
        //
        // intent.setClass(mActivity, FaseNewsDetailListActivity.class);
        // startActivity(intent);
        
        if (url != null && !"".equals(url))
        {
            Intent intent = new Intent(mActivity, LowcPortShowActivity.class);// XwzxContentShowActivity
            intent.putExtra("url", url);
            intent.putExtra("ISURL", true);
            intent.putExtra("title", funcName);
            mActivity.startActivity(intent);
        }
        else
        {
            Toast.makeText(mActivity, "url为空", Toast.LENGTH_LONG).show();
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
            if (mFragmentTMainActivityListener != null)
            {
                mFragmentTMainActivityListener.onSecondFragmentClose();
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
    
}
