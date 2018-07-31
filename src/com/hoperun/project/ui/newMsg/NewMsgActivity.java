/*
 * File name:  NewMsgActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-3-26
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.newMsg;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.hoperun.manager.components.CustomDialog;
import com.hoperun.manager.components.CustomDialogListener;
import com.hoperun.manager.components.HeadView;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.Login.LoginActivity;
import com.hoperun.project.ui.Login.PersonSetView;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;
import com.hoperun.project.ui.baseui.baseInterface.baseUiInterface.OnResultListen;
import com.hoperun.project.ui.xwzx.XwzxListView;

/**
 * 最新消息
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-26]
 */
public class NewMsgActivity extends PMIPBaseActivity implements OnItemClickListener
{
    /**
     * 头部
     */
    protected HeadView       mHeadView;
    
    /**
     * 装载listview 的布局
     */
    protected RelativeLayout mOfficalListViewRL;
    
    /** 今日关注 **/
    private XwzxListView     unHandleView        = null;
    
    /**
     * 类型-集团要闻
     */
    private String           xwzxType            = "";
    
    /**
     * 今日要闻
     */
    private String           mHandleType         = "";
    
    /**
     * 请求列表处于已读、未读的标识，0:未读，1:已读,2:全部
     */
    protected String         mUnReadStatus       = ConstState.UNReadDOCLIST;
    
    /** 路径 **/
    private String           parentPath          = "";
    
    private OnClickListener  mPersonSetListener  = new OnClickListener()
                                                 {
                                                     
                                                     @Override
                                                     public void onClick(View v)
                                                     {
                                                         Intent intent = new Intent();
                                                         intent.setClass(NewMsgActivity.this, PersonSetView.class);
                                                         startActivity(intent);
                                                     }
                                                     
                                                 };
    
    private OnClickListener  mExistOnclickListen = new OnClickListener()
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
                                                                     new Intent(NewMsgActivity.this,
                                                                         LoginActivity.class);
                                                                 
                                                                 startActivity(it);
                                                                 
                                                                 NewMsgActivity.this.finish();
                                                             }
                                                         });
                                                         
                                                     }
                                                 };
    
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
        setContentView(R.layout.newmsg_main);
        initView();
        initData();
        getDocList();
        // getMessageList();
    }
    
    public void initView()
    {
        
        mHeadView = (HeadView)findViewById(R.id.mobile_header);
        mOfficalListViewRL = (RelativeLayout)findViewById(R.id.custom_listview);
        
    }
    
    public void initData()
    {
        
        mHeadView.setTitle(GlobalState.getInstance().getUserName());
        mHeadView.setLeftOnclickLisen(mPersonSetListener);
        mHeadView.setRightOnclickLisen(mExistOnclickListen);
        
        mUnReadStatus = ConstState.ALLDOCLIST;
        xwzxType = ConstState.ZXXX;
        mHandleType = ConstState.UNHADLEDOCLIST;
        parentPath =
            ConstState.MIP_ROOT_DIR + GlobalState.getInstance().getOpenId() + "/" + ConstState.XWZX + "/"
                + ConstState.TODAYCARE_DIRNAME;
        // ==============
        unHandleView = (XwzxListView)initOfficalListView(unHandleView, "", "");
        addListView(unHandleView);
        // ================
    }
    
    /**
     * 触摸滑动监听
     */
    protected OnTouchListener mViewLandscapeSlideListener = new OnTouchListener()
                                                          {
                                                              
                                                              @Override
                                                              public boolean onTouch(View v, MotionEvent event)
                                                              {
                                                                  // TODO Auto-generated method stub
                                                                  return false;
                                                              }
                                                          };
    
    /**
     * doc list加载完毕后的监听返回
     */
    protected OnResultListen  mResultlisten               = new OnResultListen()
                                                          {
                                                              
                                                              @Override
                                                              public void updateListView(String docId)
                                                              {
                                                                  // TODO Auto-generated method stub
                                                                  readViewBack(docId);
                                                              }
                                                              
                                                              @Override
                                                              public void setOnResultListen()
                                                              {
                                                                  // TODO Auto-generated method stub
                                                                  setViewResult();
                                                              }
                                                          };
    
    /**
     * 重载方法
     * 
     * @param id
     * @author wang_ling
     */
    public void readViewBack(String id)
    {
        if (unHandleView != null)
        {
            unHandleView.updateListView(id, true);
        }
    }
    
    public void setViewResult()
    {
    }
    
    public Object initOfficalListView(Object view, String keyWords, String typeId)
    {
        if (view == null)
        {
            view =
                new XwzxListView(this, keyWords, xwzxType, mHandleType, mUnReadStatus, parentPath,
                    mViewLandscapeSlideListener, false, typeId, true);
            ((XwzxListView)view).setResultListen(mResultlisten);
        }
        return view;
    }
    
    public void addListView(View view)
    {
        mOfficalListViewRL.removeAllViews();
        mOfficalListViewRL.addView(view);
    }
    
    /**
     * 
     * 获取今日关注列表
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-3-26
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void getDocList()
    {
        if (unHandleView == null)
        {
            unHandleView = (XwzxListView)initOfficalListView(unHandleView, "", "");
        }
        unHandleView.getDocListRefresh("");
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
        // TODO Auto-generated method stub
        
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
    
    /**
     * 获取消息中心请求列表
     */
    protected NetTask mGetMessageListRequst;
    
    private void getMessageList()
    {
        JSONObject body = new JSONObject();
        try
        {
            
            body.put("token", GlobalState.getInstance().getToken());
            body.put("loginName", GlobalState.getInstance().getOpenId());
            mGetMessageListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETMESSAGELIST).create();
            // 消息中心
            NetRequestController.getXwzxList(mGetMessageListRequst,
                mHandler,
                RequestTypeConstants.GETMESSAGELIST,
                body,
                "getMessageList");
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
