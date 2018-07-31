/*
 * File name:  ContactMainActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-2-10
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.artifex.send.AddressView.SelectAdapter;
import com.artifex.send.AddressView.companyAddress.AddressNode;
import com.artifex.send.AddressView.companyAddress.AddressTreeListView;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.exceptions.MIPException;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.sqlUtils.SQLCreator.DBHandler;
import com.hoperun.mip.sqlUtils.Table.TLINKERLATE;
import com.hoperun.mip.utils.MessageSQLIdConstants;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.mipmanager.model.entityMetaData.AddressBody;
import com.hoperun.mipmanager.model.entityMetaData.DeptAddressBody;
import com.hoperun.mipmanager.model.entityMetaData.NextStepUserInfo;
import com.hoperun.mipmanager.model.entityMetaData.PDFNextStepInfo;
import com.hoperun.mipmanager.model.entityMetaData.UserAddressBody;
import com.hoperun.mipmanager.model.wrapRequest.GetCompanyAddressRequest;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * 通讯录
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-2-10]
 */
public class ContactMainActivity extends PMIPBaseActivity implements OnClickListener
{
    public static final int              TWOMODE                 = 2;
    
    /** 用户id **/
    private String                       openId;
    
    /**
     * 返回按鈕
     */
    private ImageView                    backIV;
    
    /**
     * 部门返回
     */
    private ImageView                    depthBackIV;
    
    /**
     * 通讯录list布局
     */
    private RelativeLayout               address_list;
    
    /**
     * 发送按钮
     */
    private Button                       sendButton;
    
    /** 等待框布局 **/
    private RelativeLayout               loadLayout;
    
    /** 等待图片 **/
    private ImageView                    loadImageView;
    
    /**
     * 可选联系人节点列表
     */
    private List<AddressNode>            mFilterAddressNode      = new ArrayList<AddressNode>();
    
    /**
     * 最近联系人节点列表
     */
    private List<AddressNode>            mLateLinkerNode         = new ArrayList<AddressNode>();
    
    /**
     * 所有节点
     */
    private HashMap<String, AddressNode> mFilterAddressNodeMap   = new HashMap<String, AddressNode>();
    
    /**
     * 已选择的节点列表
     */
    private List<AddressNode>            userSelectedList        = new ArrayList<AddressNode>();
    
    /**
     * 可选联系人的list列表
     */
    private AddressTreeListView          mContactslistView;
    
    /**
     * 已选联系人的list列表
     */
    private ListView                     mSelectedListView;
    
    /**
     * 已选联系人的list列表 adapter
     */
    private SelectAdapter                mSelectedAdapter;
    
    /** 最近联系人 **/
    protected RelativeLayout             mFirstLL;
    
    /**
     * 企业通讯录
     */
    protected RelativeLayout             mSecondLL;
    
    /** tab页的个数 **/
    int                                  mSelectMode             = -1;
    
    int                                  selected_width_second   = -1;
    
    int                                  unselected_width_second = -1;
    
    int                                  mLLHeight               = -1;
    
    private ImageView                    mFirst_choose_line;
    
    private ImageView                    mSecond_choose_line;
    
    private ImageView                    mFirst_choose_shadow;
    
    private ImageView                    mSecond_choose_shadow_1;
    
    private ImageView                    mSecond_choose_shadow_2;
    
    private ImageView                    mFragment_shadow;
    
    /**
     * 当前状态 “0”-最近联系人，“1”-企业通讯录
     */
    private String                       currentStatus           = "0";
    
    /**
     * 公文id
     */
    private String                       mFileId;
    
    /**
     * 公文路径
     */
    private String                       mFilePath;
    
    /**
     * 公文名称
     */
    private String                       mFileName;
    
    /**
     * 类型，收文/发文
     */
    private String                       mType;
    
    /**
     * 请求的部门根ID
     */
    private String                       reqestDepId;
    
    /**
     * 用户部门id
     */
    private String                       rootDepId;
    
    /**
     * 是否可以从部门通讯录中获取可选联系人 , 0 不允许， 1 ；允许
     */
    private String                       mChangenextpersion;
    
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
        this.setContentView(R.layout.contactmain_layout);
        initIntentData();
        initView();
        initListener();
        initData();
    }
    
    /**
     * 该步骤节点的相关信息
     */
    private PDFNextStepInfo mInfo;
    
    /**
     * 该步骤节点的id
     */
    private String          mStepId;
    
    /**
     * 初始化文件属性
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void initIntentData()
    {
        Intent intent = this.getIntent();
        if (intent != null)
        {
            mFileId = intent.getStringExtra(ConstState.PDF_FILEMESSAGEID);
            mType = intent.getStringExtra(ConstState.PDF_TYPE);
            mFilePath = intent.getStringExtra(ConstState.PDF_PATH);
            
            mFileName = intent.getStringExtra(ConstState.PDF_FILENAME) + ".pdf";
            
        }
        if (mFileId == null)
        {
            mFileId = "";
        }
        if (mType == null)
        {
            mType = "";
        }
        if (mFilePath == null)
        {
            mFilePath = "";
        }
        
    }
    
    /**
     * 
     * 初始化页面
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-2-10
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initView()
    {
        backIV = (ImageView)findViewById(R.id.fragment_back);
        depthBackIV = (ImageView)findViewById(R.id.depth_back);
        depthBackIV.setVisibility(View.GONE);
        mFirstLL = (RelativeLayout)this.findViewById(R.id.fragment_first_ll);
        mSecondLL = (RelativeLayout)this.findViewById(R.id.fragment_second_ll);
        mFirst_choose_line = (ImageView)findViewById(R.id.fragment_first_im_choose_lien);
        
        mSecond_choose_line = (ImageView)findViewById(R.id.fragment_second_im_choose_lien);
        
        mFirst_choose_shadow = (ImageView)findViewById(R.id.fragment_first_im_shadow);
        
        mSecond_choose_shadow_1 = (ImageView)findViewById(R.id.fragment_second_im_shadow_1);
        
        mSecond_choose_shadow_2 = (ImageView)findViewById(R.id.fragment_second_im_shadow_2);
        
        mFragment_shadow = (ImageView)findViewById(R.id.fragment_shadow);
        address_list = (RelativeLayout)findViewById(R.id.custom_listview);
        mSelectedListView = (ListView)findViewById(R.id.address_bottom_selector);
        sendButton = (Button)findViewById(R.id.confirm_send);
        
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
     * 初始化数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-2-10
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        openId = GlobalState.getInstance().getOpenId();
        SharedPreferences share = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        rootDepId = share.getString("depId", "");
        reqestDepId = rootDepId;
        
        // getValueFromInfo();
        
        createLinkerData();
        
        mContactslistView = new AddressTreeListView(this, null, rootDepId);
        address_list.addView(mContactslistView);
        
        mContactslistView.setOnItemClickListener(topLisener);
        mSelectedAdapter = new SelectAdapter(this);
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
        
        // else if (mChangenextpersion.equals("1"))
        // {
        // getAddress(rootDepId);
        // }
        
        mSelectedAdapter.setSelectedList(userSelectedList);
    }
    
    /**
     * 初始化，该节点的信息
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-13
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void getValueFromInfo()
    {
        this.mStepId = mInfo.getStepguid();
        
        this.mChangenextpersion = mInfo.getChangenextpersion();
        
        if (mInfo.getmFilterhandleuser() == null)
        {
            mFilterAddressNode = null;
        }
        else
        {
            for (int i = 0; i < mInfo.getmFilterhandleuser().size(); i++)
            {
                NextStepUserInfo handleuser = mInfo.getmFilterhandleuser().get(i);
                
                AddressNode node = new AddressNode();
                
                node.setChecked(false);
                node.setDept(false);
                node.setName(handleuser.getUsername());
                node.setCurId(handleuser.getUserid());
                node.setParentId(rootDepId);
                node.setParent(null);
                
                mFilterAddressNode.add(node);
                mFilterAddressNodeMap.put(node.getCurId(), node);
            }
        }
    }
    
    /**
     * 获取通讯录列表<一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param depId
     * @LastModifiedDate：2013-10-16
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void getAddress(String depId)
    {
        if (depId != null)
        {
            reqestDepId = depId;
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(GetCompanyAddressRequest.PARENTID, depId);
            NetRequestController.getCompanyAddress(mHandler, RequestTypeConstants.GETADDRESS, map);
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
    
    /**
     * 
     * 初始化监听器
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-2-10
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    protected void initListener()
    {
        mFirstLL.setOnClickListener(this);
        mSecondLL.setOnClickListener(this);
        backIV.setOnClickListener(this);
        depthBackIV.setOnClickListener(this);
        sendButton.setOnClickListener(this);
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
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
        // 点击时间间隔太短则不触发
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        
        switch (v.getId())
        {
        
            case R.id.fragment_first_ll:
            case R.id.fragment_second_ll:
                switchReadLL(v.getId());
                break;
            
            case R.id.fragment_back:
                this.finish();
                // mContactslistView.backToPre();
                break;
            case R.id.depth_back:
                mContactslistView.backToPre();
                break;
            // 确认发送
            case R.id.confirm_send:
                saveLateLinker(userSelectedList);
                break;
            
            default:
                break;
        }
    }
    
    /**
     * 
     * 保存最近联系人
     * 
     * @Description<功能详细描述>
     * 
     * @param lateLinkerList
     * @LastModifiedDate：2014-2-12
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void saveLateLinker(List<AddressNode> lateLinkerList)
    {
        DBHandler hander = new DBHandler(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        hander.DBHandlerDBOpen(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        try
        {
            hander.delete(MessageSQLIdConstants.DB_MESSAGE_LINKERLATE, TLINKERLATE.OPENID + "=?", new String[] {openId});
        }
        catch (MIPException e1)
        {
            e1.printStackTrace();
        }
        hander.getDb().beginTransaction();
        
        try
        {
            for (int i = 0; i < lateLinkerList.size(); i++)
            {
                ContentValues contentValue = new ContentValues();
                contentValue.put(TLINKERLATE.OPENID, openId);
                contentValue.put(TLINKERLATE.USERID, lateLinkerList.get(i).getCurId());
                contentValue.put(TLINKERLATE.USERNAME, lateLinkerList.get(i).getName());
                contentValue.put(TLINKERLATE.PARENTID, lateLinkerList.get(i).getParentId());
                
                Cursor cursor =
                    hander.query(MessageSQLIdConstants.DB_MESSAGE_LINKERLATE, null, TLINKERLATE.OPENID + "=? and "
                        + TLINKERLATE.USERID + "=?", new String[] {openId, lateLinkerList.get(i).getCurId()}, null);
                if (cursor.moveToNext())
                {
                    hander.update(MessageSQLIdConstants.DB_MESSAGE_LINKERLATE,
                        contentValue,
                        TLINKERLATE.OPENID + "=? and " + TLINKERLATE.USERID + "=?",
                        new String[] {openId, lateLinkerList.get(i).getCurId()});
                }
                else
                {
                    hander.insert(MessageSQLIdConstants.DB_MESSAGE_LINKERLATE, contentValue);
                }
                cursor.close();
            }
            hander.getDb().setTransactionSuccessful();
        }
        catch (MIPException e)
        {
            new MIPException().printStackTrace();
        }
        finally
        {
            hander.getDb().endTransaction();
            
        }
        
        hander.DBHandlerDBClose();
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
        DBHandler hander = new DBHandler(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        hander.DBHandlerDBOpen(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        ArrayList<AddressNode> tempLinker = new ArrayList<AddressNode>();
        try
        {
            Cursor cursor =
                hander.query(MessageSQLIdConstants.DB_MESSAGE_LINKERLATE,
                    null,
                    TLINKERLATE.OPENID + "=?",
                    new String[] {openId},
                    null);
            while (cursor.moveToNext())
            {
                AddressNode temp = new AddressNode();
                temp.setName(cursor.getString(cursor.getColumnIndex(TLINKERLATE.USERNAME)));
                temp.setCurId(cursor.getString(cursor.getColumnIndex(TLINKERLATE.USERID)));
                temp.setChecked(false);
                temp.setDept(false);
                temp.setParentId(rootDepId);
                temp.setParent(null);
                tempLinker.add(temp);
            }
            cursor.close();
        }
        catch (MIPException e)
        {
            new MIPException().printStackTrace();
        }
        
        hander.DBHandlerDBClose();
        return tempLinker;
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
    
    public void clickRadioButton(int id)
    {
        switch (id)
        {
            case R.id.fragment_first_ll:
                currentStatus = "0";
                depthBackIV.setVisibility(View.GONE);
                break;
            case R.id.fragment_second_ll:
                currentStatus = "1";
                depthBackIV.setVisibility(View.VISIBLE);
                break;
            
            default:
                break;
        }
        
        disPlayListView();
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
            mContactslistView = new AddressTreeListView(this, null, rootDepId);
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
            mContactslistView = new AddressTreeListView(this, null, rootDepId);
            address_list.addView(mContactslistView);
            mContactslistView.setOnItemClickListener(topLisener);
            getAddress(rootDepId);
        }
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
                    Toast.makeText(this, "数据返回错误！", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    }
    
    private List<AddressNode> nodes;
    
    private String            selectParentName = "";
    
    /**
     * 处理网络返回数据
     * 
     * @Description<功能详细描述>
     * 
     * @param list
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void dealBackData(List<HashMap<String, Object>> list)
    {
        if (list == null || list.size() == 0)
        {// 没有数据
            Toast.makeText(this, "没有更多数据！", Toast.LENGTH_SHORT).show();
        }
        else
        {
            nodes = new ArrayList<AddressNode>();
            AddressBody body = new AddressBody();
            body.convertToObject(list.get(0));
            if (body != null)
            {
                if (body.getDeptlist() != null)
                {
                    for (int i = 0; i < body.getDeptlist().size(); i++)
                    {
                        DeptAddressBody body_temp = body.getDeptlist().get(i);
                        
                        body_temp.getDeptid();
                        
                        AddressNode node =
                            new AddressNode(body_temp.getDeptname(), reqestDepId, body_temp.getDeptid(), true);
                        nodes.add(node);
                    }
                }
                
                if (body.getUserlist() != null)
                {
                    for (int i = 0; i < body.getUserlist().size(); i++)
                    {
                        UserAddressBody body_temp = body.getUserlist().get(i);
                        body_temp.getUserid();
                        AddressNode node =
                            new AddressNode(body_temp.getUsername(), reqestDepId, body_temp.getUserid(), false);
                        node.setParentName(selectParentName);
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
    
}
