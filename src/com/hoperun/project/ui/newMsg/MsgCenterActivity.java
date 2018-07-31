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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.adpter.xxzx.XxzxListAdapter;
import com.hoperun.manager.components.CustomDialog;
import com.hoperun.manager.components.CustomDialogListener;
import com.hoperun.manager.components.HeadView;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.SystemTools;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.offical.GWDocModule;
import com.hoperun.mipmanager.model.entityModule.xxzx.XxzxItem;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.Login.LoginActivity;
import com.hoperun.project.ui.Login.PersonSetView;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;
import com.hoperun.project.ui.baseui.baseInterface.baseUiInterface.OnResultListen;
import com.hoperun.project.ui.function.xtbg.XtbgActivity;
import com.hoperun.project.ui.xwzx.XwzxDetailListActivity;
import com.hoperun.project.ui.xwzx.XwzxListView;

/**
 * 消息中心
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-26]
 */
public class MsgCenterActivity extends PMIPBaseActivity implements OnItemClickListener, OnClickListener
{
    /**
     * 头部
     */
    protected HeadView      mHeadView;
    
    /** 今日关注 **/
    private XwzxListView    unHandleView        = null;
    
    /** 今日代办 **/
    private LinearLayout    jrdbLayout;
    
    /** RTX消息 **/
    private LinearLayout    rtxLayout;
    
    /** 通知公告 **/
    private LinearLayout    tzggLayout;
    
    /** 云之家 **/
    private LinearLayout    yzjLayout;
    
    /** 集团要闻 **/
    private LinearLayout    jtywLayout;
    
    /** 网上公文 **/
    private RelativeLayout  wsgwLayout;
    
    /** 等待框布局 **/
    private RelativeLayout  loadLayout;
    
    /** 等待图片 **/
    private ImageView       loadImageView;
    
    /** 今日待办未读数 **/
    private TextView        jrdbNum;
    
    /** 今日待办列表 **/
    private ListView        jrdbListview;
    
    /** 通知公告未读数 **/
    private TextView        wdtgNum;
    
    /** 通知公告列表 **/
    // private ListView tzggListview;
    
    /** 集团要闻未读数 **/
    private TextView        jtywNum;
    
    /** 集团要闻列表 **/
    // private ListView jtywListview;
    
    /** 通知公告列表适配器 **/
    private XxzxListAdapter tzggAdapter;
    
    /** 集团要闻列表适配器 **/
    private XxzxListAdapter jtywAdapter;
    
    /** 通知公告列表 **/
    private List<XxzxItem>  tzggList;
    
    /** 我的粉丝列表 **/
    private List<XxzxItem>  fsList;
    
    /** 我的关注列表 **/
    private List<XxzxItem>  gzhuList;
    
    /** 我的微博列表 **/
    private List<XxzxItem>  wbList;
    
    /** 集团要闻列表 **/
    private List<XxzxItem>  jtywList;
    
    /** 包名 **/
    private String          packageName         = "";
    
    /** 通知公告条目1 **/
    private TextView        tzggItem1;
    
    /** 通知公告条目2 **/
    private TextView        tzggItem2;
    
    /** 通知公告条目3 **/
    private TextView        tzggItem3;
    
    /** 集团要闻条目1 **/
    private TextView        jtywItem1;
    
    /** 集团要闻条目2 **/
    private TextView        jtywItem2;
    
    /** 集团要闻条目3 **/
    private TextView        jtywItem3;
    
    /** 集团要闻4 **/
    private TextView        jtywItem4;
    
    /** 我的粉丝 **/
    private LinearLayout    fsLayout;
    
    /** 我的关注 **/
    private LinearLayout    gzhuLayout;
    
    /** 我的微博 **/
    private LinearLayout    wbLayout;
    
    /** 我的粉丝未读数 **/
    private TextView        fsNum;
    
    /** 我的关注未读数 **/
    private TextView        gzhuNum;
    
    /** 我的微博未读数 **/
    private TextView        wbNum;
    
    /** 今日待办条目1 **/
    private TextView        jrdbItem1;
    
    /** 今日待办条目2 **/
    private TextView        jrdbItem2;
    
    /** 今日待办条目3 **/
    private TextView        jrdbItem3;
    
    /** 今日待办条目4 **/
    private TextView        jrdbItem4;
    
    /** 今日待办列表 **/
    private List<XxzxItem>  jrdbList;
    
    /** 云之家列表 **/
    private List<XxzxItem>  yzjList;
    
    private TextView        yzjNum;
    
    private TextView        yzjItme1;
    
    private TextView        yzjItme2;
    
    private TextView        yzjItme3;
    
    private TextView        yzjItme4;
    
    private TextView        yzjItme5;
    
    private TextView        yzjItme6;
    
    private LinearLayout    mailLayout;
    
    /** scrollview控件 **/
    private ScrollView      msgScroll;
    
    private OnClickListener mPersonSetListener  = new OnClickListener()
                                                {
                                                    
                                                    @Override
                                                    public void onClick(View v)
                                                    {
                                                        Intent intent = new Intent();
                                                        intent.setClass(MsgCenterActivity.this, PersonSetView.class);
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
                                                                    new Intent(MsgCenterActivity.this,
                                                                        LoginActivity.class);
                                                                it.putExtra("biaozhi", "0");
                                                                startActivity(it);
                                                                
                                                                MsgCenterActivity.this.finish();
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
        setContentView(R.layout.msgcenter_main);
        initView();
        initData();
        setClickListener();
        
    }
    
    /**
     * 重载方法
     * 
     * @author chen_wei3
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        getMessageList();
    }
    
    public void initView()
    {
        
        mHeadView = (HeadView)findViewById(R.id.mobile_header);
        jrdbLayout = (LinearLayout)findViewById(R.id.jrdb_layout);
        rtxLayout = (LinearLayout)findViewById(R.id.rtx_layout);
        tzggLayout = (LinearLayout)findViewById(R.id.wdtg_layout);
        yzjLayout = (LinearLayout)findViewById(R.id.yzj_layout);
        jtywLayout = (LinearLayout)findViewById(R.id.jtxw_layout);
        wsgwLayout = (RelativeLayout)findViewById(R.id.wsgw_layout);
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        jrdbNum = (TextView)findViewById(R.id.jrdb_num);
        // jrdbListview = (ListView)findViewById(R.id.jrdb_listview);
        // rtxNum = (TextView)findViewById(R.id.rtx_num);
        wdtgNum = (TextView)findViewById(R.id.wdtg_num);
        // tzggListview = (ListView)findViewById(R.id.tzgg_listview);
        jtywNum = (TextView)findViewById(R.id.jtxw_num);
        // jtywListview = (ListView)findViewById(R.id.jtyw_listview);
        
        // tzggItem1 = (TextView)findViewById(R.id.tzgg_item1);
        // tzggItem2 = (TextView)findViewById(R.id.tzgg_item2);
        // tzggItem3 = (TextView)findViewById(R.id.tzgg_item3);
        
        jtywItem1 = (TextView)findViewById(R.id.jtxw_item1);
        jtywItem2 = (TextView)findViewById(R.id.jtxw_item2);
        jtywItem3 = (TextView)findViewById(R.id.jtxw_item3);
        jtywItem4 = (TextView)findViewById(R.id.jtxw_item4);
        
        fsLayout = (LinearLayout)findViewById(R.id.fs_layout);
        gzhuLayout = (LinearLayout)findViewById(R.id.gzhu_layout);
        wbLayout = (LinearLayout)findViewById(R.id.wb_layout);
        fsNum = (TextView)findViewById(R.id.fs_num);
        gzhuNum = (TextView)findViewById(R.id.gzhu_num);
        wbNum = (TextView)findViewById(R.id.wb_num);
        
        jrdbItem1 = (TextView)findViewById(R.id.jrdb_item1);
        jrdbItem2 = (TextView)findViewById(R.id.jrdb_item2);
        jrdbItem3 = (TextView)findViewById(R.id.jrdb_item3);
        jrdbItem4 = (TextView)findViewById(R.id.jrdb_item4);
        
        yzjNum = (TextView)findViewById(R.id.yzj_num);
        yzjItme1 = (TextView)findViewById(R.id.yzj_itme1);
        yzjItme2 = (TextView)findViewById(R.id.yzj_itme2);
        yzjItme3 = (TextView)findViewById(R.id.yzj_itme3);
        yzjItme4 = (TextView)findViewById(R.id.yzj_itme4);
        yzjItme5 = (TextView)findViewById(R.id.yzj_itme5);
        yzjItme6 = (TextView)findViewById(R.id.yzj_itme6);
        mailLayout = (LinearLayout)findViewById(R.id.mail_layout);
        
        msgScroll = (ScrollView)findViewById(R.id.msg_scroll);
    }
    
    float startY;
    
    float endY;
    
    int   scrollY;
    
    private class TouchListenerImpl implements OnTouchListener
    {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent)
        {
            switch (motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    startY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    
                    // startRefresh();
                    scrollY = view.getScrollY();
                    break;
                case MotionEvent.ACTION_UP:
                    endY = motionEvent.getY();
                    if (endY - startY > 0 && scrollY == 0)
                    {
                        // Toast.makeText(MsgCenterActivity.this, "滑动到了顶端 ！", Toast.LENGTH_SHORT).show();
                        // System.out.println("滑动到了顶端 view.getScrollY()=" + scrollY);
                        // 执行下拉刷新代码
                        getMessageList();
                        startRefresh();
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
        
    };
    
    public void setClickListener()
    {
        jrdbLayout.setOnClickListener(this);
        rtxLayout.setOnClickListener(this);
        tzggLayout.setOnClickListener(this);
        yzjLayout.setOnClickListener(this);
        jtywLayout.setOnClickListener(this);
        wsgwLayout.setOnClickListener(this);
        fsLayout.setOnClickListener(this);
        gzhuLayout.setOnClickListener(this);
        wbLayout.setOnClickListener(this);
        mailLayout.setOnClickListener(this);
        msgScroll.setOnTouchListener(new TouchListenerImpl());
    }
    
    public void initData()
    {
        
        mHeadView.setTitle(GlobalState.getInstance().getUserName());
        mHeadView.setLeftOnclickLisen(mPersonSetListener);
        mHeadView.setRightOnclickLisen(mExistOnclickListen);
        jtywList = new ArrayList<XxzxItem>();
        tzggList = new ArrayList<XxzxItem>();
        fsList = new ArrayList<XxzxItem>();
        gzhuList = new ArrayList<XxzxItem>();
        wbList = new ArrayList<XxzxItem>();
        jrdbList = new ArrayList<XxzxItem>();
        yzjList = new ArrayList<XxzxItem>();
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
                case RequestTypeConstants.GETMESSAGELIST:
                    closeProgressDialog();
                    endRefresh();
                    MetaResponseBody responseBuzBody = (MetaResponseBody)objBody;
                    List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                    if (ret != null && ret.size() > 0)
                    {
                        if (!ret.get(0).get("msgitems").equals(""))
                        {
                            List<HashMap<String, Object>> ret1 =
                                (List<HashMap<String, Object>>)ret.get(0).get("msgitems");
                            clearData();
                            for (int i = 0; i < ret1.size(); i++)
                            {
                                XxzxItem entity = new XxzxItem("", "");
                                entity.convertToObject(ret1.get(i));
                                if (entity.getStringKeyValue(entity.classid).equals("1"))// 集团要闻
                                {
                                    jtywList.add(entity);
                                }
                                else if (entity.getStringKeyValue(entity.classid).equals("2"))// 通知公告
                                {
                                    tzggList.add(entity);
                                }
                                else if (entity.getStringKeyValue(entity.classid).equals("4"))
                                {
                                    yzjList.add(entity);
                                }
                                else if (entity.getStringKeyValue(entity.classid).equals("5"))// 今日待办
                                {
                                    jrdbList.add(entity);
                                }
                                else if (entity.getStringKeyValue(entity.classid).equals("6"))// 我的粉丝
                                {
                                    // fsList.add(entity);
                                    fsNum.setText("我的粉丝 (" + entity.getStringKeyValue(XxzxItem.title) + ")");
                                }
                                else if (entity.getStringKeyValue(entity.classid).equals("7"))// 我的关注
                                {
                                    // gzhuList.add(entity);
                                    gzhuNum.setText("我的关注 (" + entity.getStringKeyValue(XxzxItem.title) + ")");
                                }
                                else if (entity.getStringKeyValue(entity.classid).equals("8"))// 我的微博
                                {
                                    // wbList.add(entity);
                                    wbNum.setText("我的微博 (" + entity.getStringKeyValue(XxzxItem.title) + ")");
                                }
                            }
                            // jtywAdapter = new XxzxListAdapter(this, jtywList);
                            // jtywListview.setAdapter(jtywAdapter);
                            // tzggAdapter = new XxzxListAdapter(this, tzggList);
                            // tzggListview.setAdapter(tzggAdapter);
                            wdtgNum.setText(String.valueOf(tzggList.size()));
                            jrdbNum.setText(String.valueOf(jrdbList.size()));
                            jtywNum.setText(String.valueOf(jtywList.size()));
                            yzjNum.setText(String.valueOf(yzjList.size()));
                            sortList(jrdbList);
                            sortList(yzjList);
                            sortList(tzggList);
                            iniItems();
                            
                        }
                        else
                        {
                            Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
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
            endRefresh();
            Toast.makeText(this, "请求服务器数据失败！", Toast.LENGTH_SHORT).show();
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
        showProgressDialog();
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
            case R.id.mail_layout:// 邮件
                String url = ConstState.comnHttp + GlobalState.getInstance().getOpenId();
                
                Intent oaintent = new Intent(this, XtbgActivity.class);
                oaintent.putExtra("ISURL", true);
                oaintent.putExtra("url", url);
                oaintent.putExtra("title", "协同办公");
                this.startActivity(oaintent);
                break;
            case R.id.jrdb_layout:// 今日待办（跳转到协同办公）
                url = ConstState.comnHttp + GlobalState.getInstance().getOpenId();
                
                oaintent = new Intent(this, XtbgActivity.class);
                oaintent.putExtra("ISURL", true);
                oaintent.putExtra("url", url);
                oaintent.putExtra("title", "协同办公");
                this.startActivity(oaintent);
                break;
            case R.id.rtx_layout:// rtx消息
                packageName = "com.emoa.mobile";
                externalSkiip(packageName);
                break;
            case R.id.wdtg_layout:// 通知公告
                innerSkip("TZGG", "通知公告");
                break;
            case R.id.yzj_layout:// 云之家
                packageName = "com.kdweibo.client";
                externalSkiip(packageName);
                break;
            case R.id.jtxw_layout:// 集团要闻
                innerSkip("XWXX", "集团要闻");
                break;
            case R.id.wsgw_layout:// 网上公文
                String url2 = ConstState.comnHttp + GlobalState.getInstance().getOpenId();
                
                Intent oaintent2 = new Intent(this, XtbgActivity.class);
                oaintent2.putExtra("ISURL", true);
                oaintent2.putExtra("url", url2);
                oaintent2.putExtra("title", "协同办公");
                this.startActivity(oaintent2);
                break;
            case R.id.fs_layout:// 粉丝（跳转到云之家）
                packageName = "com.kdweibo.client";
                externalSkiip(packageName);
                break;
            case R.id.gzhu_layout:// 关注（跳转到云之家）
                packageName = "com.kdweibo.client";
                externalSkiip(packageName);
                break;
            case R.id.wb_layout:// 微博（跳转到云之家）
                packageName = "com.kdweibo.client";
                externalSkiip(packageName);
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     * <一句话功能简述>显示下载等待框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-10
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
     * <一句话功能简述>关闭下载等待框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-10
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void closeProgressDialog()
    {
        loadLayout.setVisibility(View.GONE);
    }
    
    /**
     * 
     * <一句话功能简述>跳转到内部功能模块
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-10
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void innerSkip(String funcCode, String funcName)
    {
        GWDocModule mModlue = new GWDocModule();
        mModlue.setFunccode(funcCode);
        mModlue.setFuncName(funcName);
        String path2 = ConstState.MIP_ROOT_DIR + GlobalState.getInstance().getOpenId() + "/XWZX/";
        Bundle bd = new Bundle();
        bd.putSerializable("module", mModlue);
        
        Intent intent = new Intent();
        intent.putExtra("module", bd);
        intent.putExtra("path", path2);
        
        intent.setClass(MsgCenterActivity.this, XwzxDetailListActivity.class);
        startActivity(intent);
    }
    
    /**
     * 
     * <一句话功能简述>跳转到外部应用
     * 
     * @Description<功能详细描述>
     * 
     * @param packageName
     * @LastModifiedDate：2014-5-10
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void externalSkiip(String packageName)
    {
        boolean isInstal = SystemTools.isInstalled(MsgCenterActivity.this, packageName);
        if (isInstal)
        {
            SystemTools.startApp(MsgCenterActivity.this, packageName);
        }
        else
        {
            // Toast.makeText(MsgCenterActivity.this, "暂未安装请到我的应用中下载安装该应用", Toast.LENGTH_SHORT).show();
            final CustomDialog dialog =
                CustomDialog.newInstance("暂未安装该应用,请到" + "\n" + "我的应用中下载安装",
                    getResources().getString(R.string.Login_Confirm));
            dialog.show(getSupportFragmentManager(), "checkDialog");
            dialog.setMidListener(new CustomDialogListener()
            {
                
                @Override
                public void Onclick()
                {
                    dialog.dismiss();
                }
            });
        }
    }
    
    private void iniItems()
    {
        if (yzjList != null)
        {
            if (yzjList.size() == 1)
            {
                XxzxItem entity = yzjList.get(0);
                yzjItme1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
            }
            else if (yzjList.size() == 2)
            {
                XxzxItem entity = yzjList.get(0);
                yzjItme1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
                XxzxItem entity1 = yzjList.get(1);
                yzjItme2.setText("●" + entity1.getStringKeyValue(XxzxItem.title));
            }
            else if (yzjList.size() == 3)
            {
                XxzxItem entity = yzjList.get(0);
                yzjItme1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
                XxzxItem entity1 = yzjList.get(1);
                yzjItme2.setText("●" + entity1.getStringKeyValue(XxzxItem.title));
                XxzxItem entity2 = yzjList.get(2);
                yzjItme3.setText("●" + entity2.getStringKeyValue(XxzxItem.title));
            }
            else if (yzjList.size() == 4)
            {
                XxzxItem entity = yzjList.get(0);
                yzjItme1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
                XxzxItem entity1 = yzjList.get(1);
                yzjItme2.setText("●" + entity1.getStringKeyValue(XxzxItem.title));
                XxzxItem entity2 = yzjList.get(2);
                yzjItme3.setText("●" + entity2.getStringKeyValue(XxzxItem.title));
                XxzxItem entity3 = yzjList.get(3);
                yzjItme4.setText("●" + entity3.getStringKeyValue(XxzxItem.title));
            }
            else if (yzjList.size() == 5)
            {
                XxzxItem entity = yzjList.get(0);
                yzjItme1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
                XxzxItem entity1 = yzjList.get(1);
                yzjItme2.setText("●" + entity1.getStringKeyValue(XxzxItem.title));
                XxzxItem entity2 = yzjList.get(2);
                yzjItme3.setText("●" + entity2.getStringKeyValue(XxzxItem.title));
                XxzxItem entity3 = yzjList.get(3);
                yzjItme4.setText("●" + entity3.getStringKeyValue(XxzxItem.title));
                XxzxItem entity4 = yzjList.get(4);
                yzjItme5.setText("●" + entity4.getStringKeyValue(XxzxItem.title));
            }
            else if (yzjList.size() >= 6)
            {
                XxzxItem entity = yzjList.get(0);
                yzjItme1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
                XxzxItem entity1 = yzjList.get(1);
                yzjItme2.setText("●" + entity1.getStringKeyValue(XxzxItem.title));
                XxzxItem entity2 = yzjList.get(2);
                yzjItme3.setText("●" + entity2.getStringKeyValue(XxzxItem.title));
                XxzxItem entity3 = yzjList.get(3);
                yzjItme4.setText("●" + entity3.getStringKeyValue(XxzxItem.title));
                XxzxItem entity4 = yzjList.get(4);
                yzjItme5.setText("●" + entity4.getStringKeyValue(XxzxItem.title));
                XxzxItem entity5 = yzjList.get(5);
                yzjItme6.setText("●" + entity5.getStringKeyValue(XxzxItem.title));
            }
            
        }
        
        if (jtywList != null)
        {
            if (jtywList.size() == 1)
            {
                XxzxItem entity = jtywList.get(0);
                jtywItem1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
            }
            else if (jtywList.size() == 2)
            {
                XxzxItem entity = jtywList.get(0);
                jtywItem1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
                XxzxItem entity1 = jtywList.get(1);
                jtywItem2.setText("●" + entity1.getStringKeyValue(XxzxItem.title));
            }
            else if (jtywList.size() == 3)
            {
                XxzxItem entity = jtywList.get(0);
                jtywItem1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
                XxzxItem entity1 = jtywList.get(1);
                jtywItem2.setText("●" + entity1.getStringKeyValue(XxzxItem.title));
                XxzxItem entity2 = jtywList.get(2);
                jtywItem3.setText("●" + entity2.getStringKeyValue(XxzxItem.title));
            }
            else if (jtywList.size() >= 4)
            {
                XxzxItem entity = jtywList.get(0);
                jtywItem1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
                XxzxItem entity1 = jtywList.get(1);
                jtywItem2.setText("●" + entity1.getStringKeyValue(XxzxItem.title));
                XxzxItem entity2 = jtywList.get(2);
                jtywItem3.setText("●" + entity2.getStringKeyValue(XxzxItem.title));
                XxzxItem entity3 = jtywList.get(3);
                jtywItem4.setText("●" + entity3.getStringKeyValue(XxzxItem.title));
            }
            
        }
        
        if (jrdbList != null)
        {
            if (jrdbList.size() == 1)
            {
                XxzxItem entity = jrdbList.get(0);
                jrdbItem1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
            }
            else if (jrdbList.size() == 2)
            {
                XxzxItem entity = jrdbList.get(0);
                jrdbItem1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
                XxzxItem entity1 = jrdbList.get(1);
                jrdbItem2.setText("●" + entity1.getStringKeyValue(XxzxItem.title));
            }
            else if (jrdbList.size() == 3)
            {
                XxzxItem entity = jrdbList.get(0);
                jrdbItem1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
                XxzxItem entity1 = jrdbList.get(1);
                jrdbItem2.setText("●" + entity1.getStringKeyValue(XxzxItem.title));
                XxzxItem entity2 = jrdbList.get(2);
                jrdbItem3.setText("●" + entity2.getStringKeyValue(XxzxItem.title));
            }
            else if (jrdbList.size() >= 4)
            {
                XxzxItem entity = jrdbList.get(0);
                jrdbItem1.setText("●" + entity.getStringKeyValue(XxzxItem.title));
                XxzxItem entity1 = jrdbList.get(1);
                jrdbItem2.setText("●" + entity1.getStringKeyValue(XxzxItem.title));
                XxzxItem entity2 = jrdbList.get(2);
                jrdbItem3.setText("●" + entity2.getStringKeyValue(XxzxItem.title));
                XxzxItem entity3 = jrdbList.get(3);
                jrdbItem4.setText("●" + entity3.getStringKeyValue(XxzxItem.title));
            }
            
        }
    }
    
    /**
     * 
     * <一句话功能简述>清除之前的数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void clearData()
    {
        jtywList.clear();
        jrdbList.clear();
        yzjList.clear();
        tzggList.clear();
    }
    
    /**
     * 
     * <一句话功能简述>开始刷新
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void startRefresh()
    {
        mHeadView.setTitle("刷新中...");
        mHeadView.setLeftVisiable(false);
        mHeadView.setRightVisiable(false);
    }
    
    /**
     * 
     * <一句话功能简述>结束刷新
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void endRefresh()
    {
        mHeadView.setTitle(GlobalState.getInstance().getUserName());
        mHeadView.setLeftVisiable(true);
        mHeadView.setRightVisiable(true);
    }
    
    public static Date strToDate(String str)
    {
        Date mdate = null;
        if (null == str)
            return mdate;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try
        {
            mdate = formatter.parse(str);
            return mdate;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return mdate;
    }
    
    public static long dateToLong(Date date)
    {
        return date.getTime();
    }
    
    public static long strdateTolongstr(String str)
    {
        long longstr = 0;
        if (!"".equals(str))
        {
            Date mdate = strToDate(str);
            longstr = dateToLong(mdate);
        }
        return longstr;
    }
    
    private void sortList(List<XxzxItem> list)
    {
        MyComparator myComparator = new MyComparator();
        Collections.sort(list, myComparator);
    }
    
    /**
     * 
     * 比较器
     * 
     * @Description<功能详细描述>
     * 
     * @author chen_wei3
     * @Version [版本号, 2014-5-19]
     */
    public static class MyComparator implements Comparator<XxzxItem>
    {
        /**
         * 重载方法
         * 
         * @param lhs
         * @param rhs
         * @return
         * @author chen_wei3
         */
        @Override
        public int compare(XxzxItem lhs, XxzxItem rhs)
        {
            if (!"".equals(lhs.getStringKeyValue(XxzxItem.posttime))
                && !"".equals(rhs.getStringKeyValue(XxzxItem.posttime)))
            {
                
                return strdateTolongstr(lhs.getStringKeyValue(XxzxItem.posttime)) > strdateTolongstr(rhs.getStringKeyValue(XxzxItem.posttime)) ? -1
                    : 1;
            }
            else
            {
                return -1;
            }
        }
    }
}
