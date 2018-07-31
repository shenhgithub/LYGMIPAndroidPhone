/*
 * File name:  DepartmentSelectActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-5-13
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.txl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.adpter.txl.EnterpriseAdapter;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.txl.EnterpriseEntity;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * <一句话功能简述>企业列表
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-5-13]
 */
public class DepartmentSelectActivity extends PMIPBaseActivity implements OnClickListener, OnItemClickListener
{
    /** 等待框布局 **/
    private RelativeLayout         loadLayout;
    
    /** 等待图片 **/
    private ImageView              loadImageView;
    
    /** 标题 **/
    private TextView               title;
    
    /** 返回按钮 **/
    private RelativeLayout         backBtn;
    
    /** 返回数据 **/
    private List<EnterpriseEntity> enterpriseList;
    
    /** 企业列表adapter **/
    private EnterpriseAdapter      mAdapter;
    
    /** 列表控件 **/
    private ListView               listview;
    
    /** 企业id **/
    private String                 id   = "";
    
    /** 企业名称 **/
    private String                 name = "";
    
    /** 是否向上个页面返回数据 **/
    private boolean                issenddataback;
    
    /** 搜索布局 **/
    
    protected RelativeLayout       mSearchRL;
    
    /** 筛选结果布局 **/
    
    protected RelativeLayout       mSearchResultLayout;
    
    /**
     * 搜索内容输入框
     */
    protected EditText             mSearchEditText;
    
    /**
     * 筛选结果textview
     */
    protected TextView             mSearchResultText;
    
    /**
     * 筛选结果数量
     */
    protected TextView             mSearchResultTextCount;
    
    /**
     * 搜索按钮
     */
    protected ImageView            mSearchButton;
    
    /**
     * 筛选关闭按钮
     */
    protected LinearLayout         mSearchResultCloseButton;
    
    /** 搜索结果数据 **/
    private List<EnterpriseEntity> resultList;
    
    private void iniView()
    {
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        title = (TextView)findViewById(R.id.title_text);
        title.setText("企业列表");
        backBtn = (RelativeLayout)findViewById(R.id.close_btn);
        enterpriseList = new ArrayList<EnterpriseEntity>();
        listview = (ListView)findViewById(R.id.department_listview);
        listview.setOnItemClickListener(this);
        
        mSearchRL = (RelativeLayout)findViewById(R.id.button_search_layout);
        mSearchResultLayout = (RelativeLayout)findViewById(R.id.search_layout);
        mSearchEditText = (EditText)findViewById(R.id.search_edt);
        mSearchResultText = (TextView)findViewById(R.id.search_result_tv);
        mSearchResultTextCount = (TextView)findViewById(R.id.search_result_tv_count);
        mSearchButton = (ImageView)findViewById(R.id.search_btn);
        mSearchResultCloseButton = (LinearLayout)findViewById(R.id.cancel_search_ll);
        
        resultList = new ArrayList<EnterpriseEntity>();
    }
    
    private void setClick()
    {
        backBtn.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
        mSearchResultCloseButton.setOnClickListener(this);
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
        setContentView(R.layout.department_layout);
        iniView();
        setClick();
        getMessageList();
    }
    
    /**
     * 获取全部企业列表
     */
    protected NetTask mEnterpriseListRequst;
    
    private void getMessageList()
    {
        JSONObject body = new JSONObject();
        try
        {
            
            body.put("", "");
            mEnterpriseListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETENTERPRICEList).create();
            NetRequestController.getEnterpriseLsit(mEnterpriseListRequst,
                mHandler,
                RequestTypeConstants.GETENTERPRICEList,
                body,
                "getEnterList");
            
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
            case R.id.close_btn:
                issenddataback = false;
                backIntent();
                break;
            case R.id.search_btn:
                String mSearchWords = mSearchEditText.getText().toString().trim();
                if (mSearchWords == null || mSearchWords.equals(""))
                {
                    Toast.makeText(this, "请输入关键字进行查询", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    searchDocList(mSearchWords, "", true);
                }
                break;
            case R.id.cancel_search_ll:
                searchDocList("", "", false);
                break;
            default:
                break;
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
     * @author chen_wei3
     */
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETENTERPRICEList:
                    closeProgressDialog();
                    MetaResponseBody responseBuzBody = (MetaResponseBody)objBody;
                    List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                    if (ret != null && ret.size() > 0)
                    {
                        if (!ret.get(0).get("enterlist").equals(""))
                        {
                            List<HashMap<String, Object>> ret1 =
                                (List<HashMap<String, Object>>)ret.get(0).get("enterlist");
                            for (int i = 0; i < ret1.size(); i++)
                            {
                                EnterpriseEntity entity = new EnterpriseEntity("", "");
                                entity.convertToObject(ret1.get(i));
                                enterpriseList.add(entity);
                            }
                            mAdapter = new EnterpriseAdapter(this, enterpriseList);
                            listview.setAdapter(mAdapter);
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
            Toast.makeText(this, "请求服务器数据失败！", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 
     * <一句话功能简述>显示下载等待框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-13
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
     * @LastModifiedDate：2014-5-13
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void closeProgressDialog()
    {
        loadLayout.setVisibility(View.GONE);
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
        EnterpriseEntity entity = mAdapter.getList().get(arg2);
        entity.setIsSelected(0);
        mAdapter.notifyDataSetChanged();
        issenddataback = true;
        id = entity.getStringKeyValue(EnterpriseEntity.companyid);
        name = entity.getStringKeyValue(EnterpriseEntity.companyname);
        backIntent();
    }
    
    /**
     * 
     * <一句话功能简述>返回上层页面 ，并带回数据
     * 
     * @Description<功能详细描述>
     * 
     * @param id
     * @LastModifiedDate：2014-5-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void backIntent()
    {
        Intent intent = getIntent();
        if (issenddataback)
        {
            intent.putExtra("id", id);
            intent.putExtra("name", name);
        }
        else
        {
            intent.putExtra("id", "");
            intent.putExtra("name", "");
        }
        setResult(RESULT_OK, intent);
        finish();
    }
    
    /**
     * 隐藏键盘
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-14
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected void hideSoftKey()
    {
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        
        if (mSearchEditText != null)
        {
            imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
        }
    }
    
    /**
     * 搜索
     * 
     * @Description<功能详细描述>
     * 
     * @param keyWords
     * @param flag
     * @LastModifiedDate：2014-5-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    protected void searchDocList(String keyWords, String typeId, boolean flag)
    {
        if (flag)
        {
            mSearchRL.setVisibility(View.INVISIBLE);
            mSearchResultLayout.setVisibility(View.VISIBLE);
            mSearchResultText.setText(this.getString(R.string.search_key_word, "\"" + keyWords + typeId + "\""));
            mSearchResultTextCount.setText(this.getString(R.string.search_key_word_result, "0"));
            for (int i = 0; i < enterpriseList.size(); i++)
            {
                EnterpriseEntity entity = enterpriseList.get(i);
                if (entity.getStringKeyValue(EnterpriseEntity.companyname).contains(keyWords))
                {
                    resultList.add(entity);
                }
            }
            mSearchResultTextCount.setText(this.getString(R.string.search_key_word_result, resultList.size()));
            mAdapter.setList(resultList);
            mAdapter.notifyDataSetChanged();
            hideSoftKey();
        }
        else
        {
            mSearchEditText.setText("");
            mSearchRL.setVisibility(View.VISIBLE);
            mSearchResultLayout.setVisibility(View.INVISIBLE);
            mAdapter.setList(enterpriseList);
            mAdapter.notifyDataSetChanged();
            
        }
    }
    
    /**
     * 重载方法
     * 
     * @param keyCode
     * @param event
     * @return
     * @author chen_wei3
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            issenddataback = false;
            backIntent();
            return true;
        }
        else
        {
            return false;
        }
        
    }
}
