/*
 * File name:  EmailLinkerView.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-2-18
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.innerEmail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.artifex.BaseUtils.EmailContactListen;
import com.artifex.send.AddressView.SelectAdapter;
import com.artifex.send.AddressView.companyAddress.AddressNode;
import com.artifex.send.AddressView.companyAddress.AddressTreeListView;
import com.hoperun.manager.components.CustomHandler.CustomHanler;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.model.entityMetaData.DeptEmailBody;
import com.hoperun.mipmanager.model.entityMetaData.EmailContactBody;
import com.hoperun.mipmanager.model.entityMetaData.LateLinkerInfo;
import com.hoperun.mipmanager.model.entityMetaData.UserEmailBody;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.miplygphone.R;

/**
 * 添加联系人view
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-2-18]
 */
public class EmailLinkerView extends RelativeLayout
{
    /** 用户id **/
    private String              openId;
    
    public static final int     TWOMODE                 = 2;
    
    /** 最近联系人 **/
    protected RelativeLayout    mFirstLL;
    
    /**
     * 企业通讯录
     */
    protected RelativeLayout    mSecondLL;
    
    /** tab页的个数 **/
    int                         mSelectMode             = -1;
    
    int                         selected_width_second   = -1;
    
    int                         unselected_width_second = -1;
    
    int                         mLLHeight               = -1;
    
    private ImageView           mFirst_choose_line;
    
    private ImageView           mSecond_choose_line;
    
    private ImageView           mFirst_choose_shadow;
    
    private ImageView           mSecond_choose_shadow_1;
    
    private ImageView           mSecond_choose_shadow_2;
    
    private ImageView           mFragment_shadow;
    
    /**
     * 最近联系人节点列表
     */
    private List<AddressNode>   mLateLinkerNode         = new ArrayList<AddressNode>();
    
    private List<AddressNode>   nodes;
    
    private String              selectParentName        = "";
    
    /**
     * 当前状态 “0”-最近联系人，“1”-企业通讯录
     */
    private String              currentStatus           = "0";
    
    /**
     * 获取请求列表
     */
    protected NetTask           mGetContactListRequst;
    
    /**
     * 应用上下文
     */
    protected Activity          mActivity;
    
    /**
     * 可选联系人的list列表
     */
    private AddressTreeListView mContactslistView;
    
    /**
     * 已选联系人的list列表 adapter
     */
    private SelectAdapter       mSelectedAdapter;
    
    /**
     * 确定
     */
    private RelativeLayout      mConfirm_button;
    
    /**
     * 取消
     */
    private RelativeLayout      mCancel_button;
    
    /**
     * 请求的部门根ID
     */
    private String              reqestDepId;
    
    /**
     * 用户部门id
     */
    private String              rootDepId;
    
    /** 等待框布局 **/
    private RelativeLayout      loadLayout;
    
    /** 等待图片 **/
    private ImageView           loadImageView;
    
    /**
     * 返回button
     */
    private LinearLayout        mBack;
    
    /**
     * 通讯录list布局
     */
    private RelativeLayout      address_list;
    
    /**
     * 已选联系人的list列表
     */
    private ListView            mSelectedListView;
    
    /**
     * 可选联系人节点列表
     */
    private List<AddressNode>   mFilterAddressNode      = new ArrayList<AddressNode>();
    
    /**
     * 已选择的节点列表
     */
    private List<AddressNode>   userSelectedList        = new ArrayList<AddressNode>();
    
    /**
     * 结束监听
     */
    private EmailContactListen  mOnfinishListener       = null;
    
    /**
     * <默认构造函数>
     */
    public EmailLinkerView(Context context)
    {
        super(context);
    }
    
    public EmailContactListen getmOnfinishListener()
    {
        return mOnfinishListener;
    }
    
    public void setmOnfinishListener(EmailContactListen mOnfinishListener)
    {
        this.mOnfinishListener = mOnfinishListener;
    }
    
    public EmailLinkerView(Activity activity, int button_mid)
    {
        super(activity);
        mActivity = activity;
        initView();
        initData();
    }
    
    /**
     * 网络处理handle
     */
    private CustomHanler mHandler = new CustomHanler()
                                  {
                                      @Override
                                      public void PostHandle(int requestType, Object objHeader, Object objBody,
                                          boolean error, int errorCode)
                                      {
                                          // TODO Auto-generated method stub
                                          onPostHandle(requestType, objHeader, objBody, error, errorCode);
                                      }
                                      
                                  };
    
    @SuppressWarnings("unchecked")
    private void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        closeProgressDialog();
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETADDRESS:
                    MetaResponseBody mGetNextStepResponseBuzBody = (MetaResponseBody)objBody;
                    List<HashMap<String, Object>> ret = new ArrayList<HashMap<String, Object>>();
                    if (mGetNextStepResponseBuzBody != null)
                    {
                        ret = mGetNextStepResponseBuzBody.getBuzList();
                    }
                    dealBackData(ret);
                    break;
                default:
            }
        }
        else
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETNEXTSTEP:
                    Toast.makeText(mActivity, "数据返回错误！", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    }
    
    private void initView()
    {
        LayoutInflater.from(mActivity).inflate(R.layout.email_linker_layout, this, true);
        
        RelativeLayout mCompany_rl = (RelativeLayout)findViewById(R.id.company_rl);
        // LayoutParams mlp = (LayoutParams)mCompany_rl.getLayoutParams();
        // mlp = (LayoutParams)BaseUtils.justViewRelativeLayout(mlp, mButton_mid, mViewHeight, mViewWith);
        // mCompany_rl.setLayoutParams(mlp);
        mCompany_rl.setOnTouchListener(new OnTouchListener()
        {
            
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                // TODO Auto-generated method stub
                return true;
            }
        });
        
        mFirstLL = (RelativeLayout)this.findViewById(R.id.fragment_first_ll);
        mSecondLL = (RelativeLayout)this.findViewById(R.id.fragment_second_ll);
        mFirst_choose_line = (ImageView)findViewById(R.id.fragment_first_im_choose_lien);
        
        mSecond_choose_line = (ImageView)findViewById(R.id.fragment_second_im_choose_lien);
        
        mFirst_choose_shadow = (ImageView)findViewById(R.id.fragment_first_im_shadow);
        
        mSecond_choose_shadow_1 = (ImageView)findViewById(R.id.fragment_second_im_shadow_1);
        
        mSecond_choose_shadow_2 = (ImageView)findViewById(R.id.fragment_second_im_shadow_2);
        
        mFragment_shadow = (ImageView)findViewById(R.id.fragment_shadow);
        
        address_list = (RelativeLayout)findViewById(R.id.address_list_rlayout);
        
        mBack = (LinearLayout)findViewById(R.id.address_title_back);
        
        mSelectedListView = (ListView)findViewById(R.id.address_bottom_selector);
        
        mConfirm_button = (RelativeLayout)findViewById(R.id.address_bottom_button_send);
        mCancel_button = (RelativeLayout)findViewById(R.id.address_bottom_button_cancel);
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        
        mSelectMode = TWOMODE;
        int mScreenWidth = GlobalState.getInstance().getmScreen_With();
        
        mLLHeight = (int)(((float)mScreenWidth / 720) * 110);
        
        if (mSelectMode == TWOMODE)
        {
            unselected_width_second = (int)(mScreenWidth * ((float)350 / 720));
            selected_width_second = mScreenWidth - unselected_width_second;
            mSelectMode = TWOMODE;
            
            onRelayout(mSelectMode, 1);
            
        }
        
        setBtClick();
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-2-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        openId = GlobalState.getInstance().getOpenId();
        SharedPreferences share = mActivity.getSharedPreferences("data", Context.MODE_PRIVATE);
        rootDepId = share.getString("depId", "");
        reqestDepId = rootDepId;
        createLinkerData();
        
        mContactslistView = new AddressTreeListView(mActivity, null, rootDepId);
        address_list.addView(mContactslistView);
        mContactslistView.setOnItemClickListener(topLisener);
        // mContactslistView.setOnItemClickListener(new OnItemClickListener()
        // {
        //
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        // {
        //
        // AddressNode node = (AddressNode)((BaseAdapter)parent.getAdapter()).getItem(position);
        // if (node != null)
        // {
        // if (node.isDept())
        // {
        // if (node.getChildrens() == null || node.getChildrens().size() == 0)
        // {
        // getAddress(node.getCurId());
        // }
        // else
        // {
        // reqestDepId = node.getCurId();
        // mContactslistView.setNotification(reqestDepId);
        // }
        // reqestDepId = node.getCurId();
        // }
        // else
        // {
        // CheckBox cb = (CheckBox)view.findViewById(R.id.address_checkbox);
        // cb.setChecked(!node.isChecked());
        //
        // node.setChecked(!node.isChecked());
        //
        // if (node.isChecked())
        // {
        // userSelectedList.add(node);
        // }
        // else
        // {
        // userSelectedList.remove(node);
        // }
        // mSelectedAdapter.setSelectedList(userSelectedList);
        // }
        // }
        // }
        // });
        mSelectedAdapter = new SelectAdapter(mActivity);
        mSelectedListView.setAdapter(mSelectedAdapter);
        
        mSelectedListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                AddressNode node = (AddressNode)((BaseAdapter)arg0.getAdapter()).getItem(arg2);
                node.setChecked(false);
                if (currentStatus.equals("0"))
                {
                    for (AddressNode temp : mFilterAddressNode)
                    {
                        if (temp.getCurId().equals(node.getCurId()))
                        {
                            temp.setChecked(false);
                        }
                    }
                }
                else
                {
                    for (AddressNode temp : nodes)
                    {
                        if (temp.getCurId().equals(node.getCurId()))
                        {
                            temp.setChecked(false);
                        }
                    }
                }
                
                userSelectedList.remove(node);
                mSelectedAdapter.setSelectedList(userSelectedList);
                
                mContactslistView.setNotification();
            }
            
        });
        
        if (mFilterAddressNode != null && mFilterAddressNode.size() != 0)
        {
            for (int i = 0; i < mFilterAddressNode.size(); i++)
            {
                mContactslistView.initAddressNode(mFilterAddressNode.get(i));
            }
            mContactslistView.setNotification(reqestDepId);
        }
        
        // getAddress(rootDepId);
        mSelectedAdapter.setSelectedList(userSelectedList);
        
    }
    
    /**
     * 
     * 给按钮添加监听
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-2-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void setBtClick()
    {
        mBack.setOnClickListener(headBtOnclick);
        
        mConfirm_button.setOnClickListener(headBtOnclick);
        mCancel_button.setOnClickListener(headBtOnclick);
        
        mFirstLL.setOnClickListener(headBtOnclick);
        mSecondLL.setOnClickListener(headBtOnclick);
    }
    
    private OnClickListener headBtOnclick = new OnClickListener()
                                          {
                                              @Override
                                              public void onClick(View v)
                                              {
                                                  // TODO Auto-generated method stub
                                                  Message msg = new Message();
                                                  msg.what = v.getId();
                                                  bHandler.sendMessage(msg);
                                              }
                                          };
    
    /**
     * 按钮点击事件handler
     */
    private Handler         bHandler      = new Handler()
                                          {
                                              
                                              @Override
                                              public void handleMessage(Message msg)
                                              {
                                                  // TODO Auto-generated method stub
                                                  switch (msg.what)
                                                  {
                                                      case R.id.address_title_back:
                                                          mContactslistView.backToPre();
                                                          break;
                                                      case R.id.address_bottom_button_cancel:
                                                          
                                                          if (mOnfinishListener != null)
                                                          {
                                                              mOnfinishListener.onContactCancelListener();
                                                          }
                                                          break;
                                                      case R.id.address_bottom_button_send:
                                                          
                                                          if (mOnfinishListener != null)
                                                          {
                                                              saveLateLinker(openId, userSelectedList);
                                                              String sendIds = "";
                                                              String sendNames = "";
                                                              if (userSelectedList != null)
                                                              {
                                                                  for (int i = 0; i < userSelectedList.size(); i++)
                                                                  {
                                                                      sendIds +=
                                                                          userSelectedList.get(i).getCurId() + ",";
                                                                      sendNames =
                                                                          userSelectedList.get(i).getName() + ",";
                                                                  }
                                                                  if (sendIds.endsWith(","))
                                                                  {
                                                                      sendIds =
                                                                          sendIds.substring(0, sendIds.length() - 1);
                                                                      sendNames =
                                                                          sendNames.substring(0, sendNames.length() - 1);
                                                                  }
                                                              }
                                                              LogUtil.i("", "sendIds=" + sendIds);
                                                              // if (sendIds.equals(""))
                                                              // {
                                                              // final CustomDialog dialog =
                                                              // CustomDialog.newInstance("请先选择要发送的联系人！",
                                                              // getResources().getString(R.string.pdf_lock_close));
                                                              //
                                                              // dialog.show(mActivity.getFragmentManager(),
                                                              // "SendPDFDialog");
                                                              // dialog.setMidListener(new CustomDialogListener()
                                                              // {
                                                              //
                                                              // @Override
                                                              // public void Onclick()
                                                              // {
                                                              // dialog.dismiss();
                                                              // }
                                                              // });
                                                              // }
                                                              // else
                                                              // {
                                                              mOnfinishListener.onContactSend(sendIds, sendNames);
                                                              // }
                                                              // saveLateLinker(userSelectedList);
                                                              
                                                          }
                                                          break;
                                                      case R.id.fragment_first_ll:
                                                      case R.id.fragment_second_ll:
                                                          switchReadLL(msg.what);
                                                          break;
                                                      default:
                                                          break;
                                                  }
                                                  super.handleMessage(msg);
                                              }
                                              
                                          };
    
    /**
     * 
     * 根据部门id获取联系人
     * 
     * @Description<功能详细描述>
     * 
     * @param depId
     * @LastModifiedDate：2014-2-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void getAddress(String depId)
    {
        if (depId != null)
        {
            reqestDepId = depId;
            
            JSONObject body = new JSONObject();
            try
            {
                body.put("deptid", depId);
                mGetContactListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETADDRESS).create();
                
                NetRequestController.getInnerEmailContactRequest(mGetContactListRequst,
                    mHandler,
                    RequestTypeConstants.GETADDRESS,
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
    }
    
    private void dealBackData(List<HashMap<String, Object>> list)
    {
        if (list == null || list.size() == 0)
        {// 没有数据
            Toast.makeText(mActivity, "没有更多数据！", Toast.LENGTH_SHORT).show();
        }
        else
        {
            List<AddressNode> nodes = new ArrayList<AddressNode>();
            EmailContactBody body = new EmailContactBody();
            body.convertToObject(list.get(0));
            if (body != null)
            {
                if (body.getDepts() != null)
                {
                    for (int i = 0; i < body.getDepts().size(); i++)
                    {
                        DeptEmailBody body_temp = body.getDepts().get(i);
                        
                        body_temp.getDeptid();
                        
                        AddressNode node =
                            new AddressNode(body_temp.getDeptname(), reqestDepId, body_temp.getDeptid(), true);
                        nodes.add(node);
                    }
                }
                
                if (body.getUsers() != null)
                {
                    for (int i = 0; i < body.getUsers().size(); i++)
                    {
                        UserEmailBody body_temp = body.getUsers().get(i);
                        body_temp.getUserid();
                        AddressNode node =
                            new AddressNode(body_temp.getUsername(), reqestDepId, body_temp.getUserid(), false);
                        // node.setParentName(selectParentName);
                        for (AddressNode temp : userSelectedList)
                        {
                            if (temp.getCurId().equals(node.getCurId()))
                            {
                                node.setChecked(true);
                            }
                        }
                        nodes.add(node);
                        
                    }
                }
            }
            
            for (int i = 0; i < nodes.size(); i++)
            {
                mContactslistView.initAddressNode(nodes.get(i));
            }
            mContactslistView.setNotification(reqestDepId);
        }
        
    }
    
    public void onRelayout(int displayMode, int selected_index)
    {
        if (displayMode == TWOMODE)
        {
            if (selected_index == 1)
            {
                LayoutParams lp1 = (LayoutParams)mFirstLL.getLayoutParams();
                lp1.width = selected_width_second;
                lp1.height = mLLHeight;
                mFirstLL.setLayoutParams(lp1);
                
                LayoutParams lp2 = (LayoutParams)mSecondLL.getLayoutParams();
                lp2.width = unselected_width_second;
                lp2.height = mLLHeight;
                mSecondLL.setLayoutParams(lp2);
                
                mFirst_choose_shadow.setVisibility(View.VISIBLE);
                mSecond_choose_shadow_1.setVisibility(View.GONE);
                mSecond_choose_shadow_2.setVisibility(View.GONE);
                
                mFirstLL.setVisibility(View.VISIBLE);
                mSecondLL.setVisibility(View.VISIBLE);
                
                mFirst_choose_line.setVisibility(View.VISIBLE);
                mSecond_choose_line.setVisibility(View.INVISIBLE);
            }
            else if (selected_index == 2)
            {
                LayoutParams lp1 = (LayoutParams)mFirstLL.getLayoutParams();
                lp1.width = unselected_width_second;
                lp1.height = mLLHeight;
                mFirstLL.setLayoutParams(lp1);
                
                LayoutParams lp2 = (LayoutParams)mSecondLL.getLayoutParams();
                lp2.width = selected_width_second;
                lp2.height = mLLHeight;
                mSecondLL.setLayoutParams(lp2);
                
                mFirst_choose_shadow.setVisibility(View.GONE);
                mSecond_choose_shadow_1.setVisibility(View.VISIBLE);
                mSecond_choose_shadow_2.setVisibility(View.GONE);
                
                mFirstLL.setVisibility(View.VISIBLE);
                mSecondLL.setVisibility(View.VISIBLE);
                
                mFirst_choose_line.setVisibility(View.INVISIBLE);
                mSecond_choose_line.setVisibility(View.VISIBLE);
            }
        }
    }
    
    public void switchReadLL(int id)
    {
        switch (id)
        {
            case R.id.fragment_first_ll:
                if (currentStatus.equals("0"))
                {
                    return;
                }
                onRelayout(mSelectMode, 1);
                break;
            case R.id.fragment_second_ll:
                if (currentStatus.equals("1"))
                {
                    return;
                }
                onRelayout(mSelectMode, 2);
                break;
            
            default:
                break;
        }
        clickRadioButton(id);
    }
    
    public void clickRadioButton(int id)
    {
        switch (id)
        {
            case R.id.fragment_first_ll:
                currentStatus = "0";
                // depthBackIV.setVisibility(View.GONE);
                break;
            case R.id.fragment_second_ll:
                currentStatus = "1";
                // depthBackIV.setVisibility(View.VISIBLE);
                break;
            
            default:
                break;
        }
        
        disPlayListView();
    }
    
    public void disPlayListView()
    {
        // 最近联系人
        if ("0".equals(currentStatus))
        {
            // createLinkerData();
            mFilterAddressNode.clear();
            resetLateLinker();
            for (int i = 0; i < mLateLinkerNode.size(); i++)
            {
                mFilterAddressNode.add(mLateLinkerNode.get(i));
            }
            address_list.removeAllViews();
            mContactslistView = null;
            mContactslistView = new AddressTreeListView(mActivity, null, rootDepId);
            address_list.addView(mContactslistView);
            mContactslistView.setOnItemClickListener(topLisener);
            if (mFilterAddressNode != null && mFilterAddressNode.size() != 0)
            {
                for (int i = 0; i < mFilterAddressNode.size(); i++)
                {
                    mContactslistView.initAddressNode(mFilterAddressNode.get(i));
                }
                mContactslistView.setNotification("");
            }
        }
        // 企业通讯录
        else if ("1".equals(currentStatus))
        {
            address_list.removeAllViews();
            mContactslistView = null;
            mContactslistView = new AddressTreeListView(mActivity, null, rootDepId);
            address_list.addView(mContactslistView);
            mContactslistView.setOnItemClickListener(topLisener);
            getAddress(rootDepId);
        }
    }
    
    /**
     * 
     * 根据已选联系人设置最近联系人的状态
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-2-12
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void resetLateLinker()
    {
        for (AddressNode temp : mLateLinkerNode)
        {
            temp.setChecked(false);
            for (int i = 0; i < userSelectedList.size(); i++)
            {
                if (temp.getCurId().equals(userSelectedList.get(i).getCurId()))
                {
                    temp.setChecked(true);
                    break;
                }
            }
        }
    }
    
    /**
     * 可选联系人列表点击事件
     */
    private OnItemClickListener topLisener = new OnItemClickListener()
                                           {
                                               
                                               @Override
                                               public void onItemClick(AdapterView<?> parent, View view, int position,
                                                   long id)
                                               {
                                                   AddressNode node =
                                                       (AddressNode)((BaseAdapter)parent.getAdapter()).getItem(position);
                                                   if (node != null)
                                                   {
                                                       if (node.isDept())
                                                       {
                                                           if (node.getChildrens() == null
                                                               || node.getChildrens().size() == 0)
                                                           {
                                                               getAddress(node.getCurId());
                                                           }
                                                           else
                                                           {
                                                               reqestDepId = node.getCurId();
                                                               mContactslistView.setNotification(reqestDepId);
                                                           }
                                                           reqestDepId = node.getCurId();
                                                           selectParentName = node.getName();
                                                       }
                                                       else
                                                       {
                                                           CheckBox cb =
                                                               (CheckBox)view.findViewById(R.id.address_checkbox);
                                                           cb.setChecked(!node.isChecked());
                                                           
                                                           node.setChecked(!node.isChecked());
                                                           
                                                           if (node.isChecked())
                                                           {
                                                               userSelectedList.add(node);
                                                           }
                                                           else
                                                           {
                                                               // userSelectedList.remove(node);
                                                               removeFromSelectedList(node);
                                                           }
                                                           mSelectedAdapter.setSelectedList(userSelectedList);
                                                       }
                                                   }
                                               }
                                           };
    
    /**
     * 
     * 从已选列表中移除
     * 
     * @Description<功能详细描述>
     * 
     * @param node
     * @LastModifiedDate：2014-2-12
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void removeFromSelectedList(AddressNode node)
    {
        for (int i = 0; i < userSelectedList.size(); i++)
        {
            if (node.getCurId().equals(userSelectedList.get(i).getCurId()))
            {
                userSelectedList.remove(i);
                return;
            }
        }
    }
    
    /**
     * 
     * <一句话功能简述>
     * 
     * @Description 从表中获取最近联系人
     * 
     * @return
     * @LastModifiedDate：2014-2-12
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public List<AddressNode> getLateLinkerFromTable()
    {
        LateLinkerInfo test1 = new LateLinkerInfo();
        
        List<HashMap<String, Object>> queryret;
        
        String where = "";
        
        where = LateLinkerInfo.l_openid + " = ?" + " and " + LateLinkerInfo.l_type + " = ?";
        
        String[] selectionArgs = null;
        selectionArgs = new String[] {openId, ConstState.NBYJ};
        
        queryret = test1.query(null, where, selectionArgs, null);
        List<AddressNode> linkerList = new ArrayList<AddressNode>();
        if (queryret != null)
        {
            for (int i = 0; i < queryret.size(); i++)
            {
                LateLinkerInfo info = new LateLinkerInfo();
                info.convertToObject(queryret.get(i));
                AddressNode temp = new AddressNode();
                temp.setName((String)info.getValue(LateLinkerInfo.l_username));
                temp.setCurId((String)info.getValue(LateLinkerInfo.l_userid));
                temp.setChecked(false);
                temp.setDept(false);
                temp.setParentId(rootDepId);
                temp.setParent(null);
                linkerList.add(temp);
            }
        }
        return linkerList;
    }
    
    /**
     * 
     * <一句话功能简述>
     * 
     * @Description 获取最近联系人
     * 
     * @LastModifiedDate：2014-2-12
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void createLinkerData()
    {
        mFilterAddressNode.clear();
        mLateLinkerNode = getLateLinkerFromTable();
        for (int i = 0; i < mLateLinkerNode.size(); i++)
        {
            mFilterAddressNode.add(mLateLinkerNode.get(i));
        }
    }
    
    /**
     * 
     * 保存最近联系人
     * 
     * @Description<功能详细描述>
     * 
     * @param openID
     * @param lateLinkerList
     * @LastModifiedDate：2014-2-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void saveLateLinker(String openID, List<AddressNode> lateLinkerList)
    {
        List<LateLinkerInfo> newList = new ArrayList<LateLinkerInfo>();
        for (int i = 0; i < lateLinkerList.size(); i++)
        {
            
            String userName = lateLinkerList.get(i).getName() == null ? "" : lateLinkerList.get(i).getName();
            String parentName =
                lateLinkerList.get(i).getParentName() == null ? "" : lateLinkerList.get(i).getParentName();
            String parentId = lateLinkerList.get(i).getParentId() == null ? "" : lateLinkerList.get(i).getParentId();
            String userid = lateLinkerList.get(i).getCurId() == null ? "" : lateLinkerList.get(i).getCurId();
            String type = ConstState.NBYJ;
            
            LateLinkerInfo linkerInfo = new LateLinkerInfo(openID, userName, userid, parentName, parentId, type);
            newList.add(linkerInfo);
        }
        
        DBDataObjectWrite.saveLateLinker(openID, newList, ConstState.NBYJ);
        
    }
}
