/*
 * File name:  AppWarehouse.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  li_miao
 * Last modified date:  2014-3-25
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.warehouse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.hoperun.manager.adpter.warehouse.AppTypesAdapter;
import com.hoperun.manager.components.WaitDialog;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.FileUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * 应用仓库
 * 
 * @Description<功能详细描述>
 * 
 * @author li_miao
 * @Version [版本号, 2014-3-25]
 */
public class AppWarehouse extends PMIPBaseActivity implements OnClickListener, OnItemClickListener, OnTouchListener
{
    /** 等待对话框 **/
    private WaitDialog       waitDialog;
    
    /** 返回body **/
    private MetaResponseBody responseBuzBody;
    
    private ListView         app_list;
    
    private AppTypesAdapter  appadapter;
    
    private ImageButton      btn_back;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.appwarehouse);
        app_list = (ListView)findViewById(R.id.app_types_list);
        app_list.setOnItemClickListener(this);
        btn_back = (ImageButton)findViewById(R.id.app_back);
        btn_back.setOnClickListener(this);
        
        JSONObject body = new JSONObject();
        
        waitDialog = WaitDialog.newInstance();
        waitDialog.show(getSupportFragmentManager(), "waitDialog");
        NetRequestController.getAppTypes(mHandler, RequestTypeConstants.GETAPPTYPES_REQUEST, body);
        // startApk2("com.hoperun.gacloud");
        // startApk("com.hoperun.gacloud", "com.hoperun.mip.GlobalState");
        // NetRequestController.getAppsByType(mHandler, RequestTypeConstants.GETAPPSBYTYPE_REQUEST, body);
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @param event
     * @return
     * @author li_miao
     */
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
            
            // 获取APP应用分类
                case RequestTypeConstants.GETAPPTYPES_REQUEST:
                    if (null != objBody)
                    {
                        waitDialog.dismiss();
                        responseBuzBody = (MetaResponseBody)objBody;
                        String ret = responseBuzBody.getRetError();
                        List<HashMap<String, Object>> list = responseBuzBody.getBuzList();
                        List<HashMap<String, Object>> map;
                        if ("0".equals(ret))
                        {
                            // String newp = newPassword.getText().toString().trim();
                            // GlobalState.getInstance().updateUserPassword(GlobalState.getInstance().getOpenId(),
                            // AesUtil.encrypt(newp, NetRequestController.MOA_deviceKey),
                            // ConstState.DB_FILEPATH,
                            // ConstState.DB_VERSION);
                            // appadapter = new AppTypesAdapter(this, responseBuzBody);
                            map = (ArrayList<HashMap<String, Object>>)list.get(0).get("apptypes");
                            
                            appadapter = new AppTypesAdapter(this, map);
                            app_list.setAdapter(appadapter);
                            appadapter.notifyDataSetChanged();
                            // Toast.makeText(this, "获取APP列表成功！", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(this, "获取APP列表失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        waitDialog.dismiss();
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                
                default:
                    break;
            }
            
        }
        else
        {
            waitDialog.dismiss();
            if (!(errorCode == ConstState.CANCEL_THREAD))
            {
                Toast.makeText(this, "请求服务器数据失败！", Toast.LENGTH_SHORT).show();
            }
        }
        
    }
    
    /**
     * 重载方法
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @author li_miao
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        String id = appadapter.getId(arg2);
        // JSONObject body = new JSONObject();
        // JSONObject body2 = new JSONObject();
        // try
        // {
        // // body.put("", "");
        // body.put("appType", "5");
        // body.put("size", "5");
        // body.put("queryDate", "2014-3-24 13:20:15");
        //
        // }
        //
        // catch (org.json.JSONException e)
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        
        if (GlobalState.getInstance().isFirstDownPic())
        {
            File file = new File(UpdateManager.imagesavePath);
            if (file.exists())
            {
                FileUtil.deleteDir(file);
            }
            GlobalState.getInstance().setFirstDownPic(false);
        }
        Intent intent = new Intent(AppWarehouse.this, AppDetailWarehouse.class);
        intent.putExtra("id", id);
        startActivity(intent);
        
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @author li_miao
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.app_back:
                this.finish();
                break;
        }
        
    }
    
}
