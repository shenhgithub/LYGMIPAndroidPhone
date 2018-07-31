/*
 * File name:  KbclListActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-4-3
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.ywbl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.adpter.ywbl.YwblSecondAdapter;
import com.hoperun.manager.components.DropDownRefreshListView;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.ywbl.KaoboEntity;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * <一句话功能简述>船舶列表界面
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-4-3]
 */
public class KbclListActivity extends PMIPBaseActivity implements OnClickListener
{
    
    /** 所在模块的名字 **/
    private String                  funName;
    
    /** 所在模块的code **/
    private String                  funCode;
    
    /** 标题 **/
    private TextView                title_text;
    
    /** 关闭按钮 **/
    private RelativeLayout          close;
    
    /** 列表控件 **/
    private DropDownRefreshListView listview;
    
    /** 获取列表 **/
    protected NetTask               mGetDocListRequst;
    
    /** 列表adapter **/
    private YwblSecondAdapter       madapter;
    
    /** 返回数据 **/
    private List<KaoboEntity>       list       = new ArrayList<KaoboEntity>();
    
    /** 等待框布局 **/
    private RelativeLayout          loadLayout;
    
    /** 等待图片 **/
    private ImageView               loadImageView;
    
    /** 当前模块 **/
    private int                     blockNum;
    
    /** 请求方法名 **/
    private String                  methodName = "";
    
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
            
                case RequestTypeConstants.GETYWBLLISTREQUEST:
                    closeProgressDialog();
                    MetaResponseBody responseBuzBody = (MetaResponseBody)objBody;
                    List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                    if (ret != null && ret.size() > 0)
                    {
                        
                        if (!"".equals(ret.get(0).get("todoshiptitles")))
                        {
                            List<HashMap<String, Object>> ret1 =
                                (List<HashMap<String, Object>>)ret.get(0).get("todoshiptitles");
                            for (int i = 0; i < ret1.size(); i++)
                            {
                                
                                KaoboEntity entity = new KaoboEntity("", "");
                                entity.convertToObject(ret1.get(i));
                                list.add(entity);
                            }
                            madapter = new YwblSecondAdapter(this, list, blockNum);
                            listview.setAdapter(madapter);
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
     * <一句话功能简述>获取上层ui传来的数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-3
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void getIntentData()
    {
        Intent data = getIntent();
        funName = data.getStringExtra("funname");
        funCode = data.getStringExtra("funcode");
        blockNum = data.getIntExtra("blokcNum", 0);
    }
    
    /**
     * 
     * <一句话功能简述>初始化页面
     * 
     * @Description<功能详细描述>
     * @LastModifiedDate：2014-4-3
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void iniview()
    {
        title_text = (TextView)findViewById(R.id.title_text);
        iniTitle(blockNum);
        close = (RelativeLayout)findViewById(R.id.close_btn);
        listview = (DropDownRefreshListView)findViewById(R.id.kaobo_listview);
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
    }
    
    private void iniTitle(int blockNum)
    {
        switch (blockNum)
        {
            case 2:
                title_text.setText("靠泊处理-锚地船");
                break;
            case 3:
                title_text.setText("作业处理-靠泊船");
                break;
            case 4:
                title_text.setText("离泊处理-可移泊船舶");
                break;
            case 5:
                title_text.setText("离港处理-可离港船舶");
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     * <一句话功能简述>设置监听
     * 
     * @Description<功能详细描述>
     * @LastModifiedDate：2014-4-3
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void setClick()
    {
        close.setOnClickListener(this);
        
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
        setContentView(R.layout.kaobo_layout);
        getIntentData();
        iniview();
        setClick();
        sendHttp(blockNum);
    }
    
    private void sendHttp(int num)
    {
        switch (num)
        {
            case 1:// 业务大委托
                break;
            case 2:// 靠泊处理
                methodName = "getTodoBerthingList";
                sendGetListRequest(methodName);
                break;
            case 3:// 作业处理
                methodName = "getTodoJobList";
                sendGetListRequest(methodName);
                break;
            case 4:// 离泊处理
                methodName = "getTodoShiftingList";
                sendGetListRequest(methodName);
                break;
            case 5:// 离港处理
                methodName = "getTodoLeaveList";
                sendGetListRequest(methodName);
                break;
            default:
                break;
        }
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
            case R.id.close_btn://
                finish();
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     * <一句话功能简述>列表请求
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-3
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void sendGetListRequest(String methodName)
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            mGetDocListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETYWBLLISTREQUEST).create();
            NetRequestController.getYwblList(mGetDocListRequst,
                mHandler,
                RequestTypeConstants.GETYWBLLISTREQUEST,
                body,
                methodName);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>显示下载等待框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-3
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
     * @LastModifiedDate：2014-4-3
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void closeProgressDialog()
    {
        loadLayout.setVisibility(View.GONE);
    }
}
