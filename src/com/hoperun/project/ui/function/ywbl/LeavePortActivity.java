/*
 * File name:  LeavePortActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-4-10
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.ywbl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.DateTimePickerDialog;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * <一句话功能简述>离港处理界面
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-4-11]
 */
public class LeavePortActivity extends PMIPBaseActivity implements OnClickListener
{
    /** 关闭按钮 **/
    private RelativeLayout closeBtn;
    
    /** 计划进港时间显示文本 **/
    private TextView       plan_in_time;
    
    /** 计划离港时间显示文本 **/
    private TextView       plan_leave_time;
    
    /** 计划执行完毕按钮 **/
    private Button         plan_done_btn;
    
    /** 通知引水时间 **/
    private TextView       tongzhi_time;
    
    /** 计划未完成原因 **/
    private EditText       reason_edit;
    
    /** 离港时间 **/
    private TextView       position_time;
    
    /** 船舶离港按钮 **/
    private Button         leave_btn;
    
    /** 当前时间字符串 **/
    private String         currentDateTime = "";
    
    /** 船舶id **/
    private String         shipId          = "";
    
    /** 计划进港时间 **/
    private String         planInTime      = "";
    
    /** 计划离港时间 **/
    private String         planLeaveTime   = "";
    
    /** 等待框布局 **/
    private RelativeLayout loadLayout;
    
    /** 等待图片 **/
    private ImageView      loadImageView;
    
    /** 网络请求 **/
    protected NetTask      mNetTask;
    
    private void iniView()
    {
        closeBtn = (RelativeLayout)findViewById(R.id.close_btn);
        plan_in_time = (TextView)findViewById(R.id.come_time);
        plan_in_time.setText(planInTime);
        plan_leave_time = (TextView)findViewById(R.id.go_time);
        plan_leave_time.setText(planLeaveTime);
        plan_done_btn = (Button)findViewById(R.id.plan_done_btn);
        tongzhi_time = (TextView)findViewById(R.id.tongzhi_time);
        reason_edit = (EditText)findViewById(R.id.reason_edit);
        position_time = (TextView)findViewById(R.id.position_time);
        leave_btn = (Button)findViewById(R.id.leave_btn);
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
    }
    
    private void setClick()
    {
        closeBtn.setOnClickListener(this);
        plan_done_btn.setOnClickListener(this);
        leave_btn.setOnClickListener(this);
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
        setContentView(R.layout.ligangdeal_layout);
        Intent data = getIntent();
        shipId = data.getStringExtra("shipid");
        planInTime = data.getStringExtra("planintime");
        planLeaveTime = data.getStringExtra("planleavetime");
        iniView();
        setClick();
        tongzhi_time.setOnClickListener(new TongzhiTimeOnClick());
        position_time.setOnClickListener(new LeaveTimeOnClick());
    }
    
    private final class TongzhiTimeOnClick implements OnClickListener
    {
        
        public void onClick(View v)
        {
            
            DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
            
            LeavePortActivity.this);
            
            dateTimePicKDialog.dateTimePicKDialog(tongzhi_time, 0, 1);
            
        }
        
    }
    
    private final class LeaveTimeOnClick implements OnClickListener
    {
        
        public void onClick(View v)
        {
            
            DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
            
            LeavePortActivity.this);
            
            dateTimePicKDialog.dateTimePicKDialog(position_time, 0, 1);
            
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
            case R.id.close_btn:
                finish();
                break;
            case R.id.plan_done_btn:
                sendPlanDoneRequest();
                break;
            case R.id.leave_btn:
                sendShipLeaveRequest();
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
                case RequestTypeConstants.SAVELEAVEPLANCOMPLETE:
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "保存离港处理计划执行完毕数据失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RequestTypeConstants.SAVESHIPLEAVE:
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "保存离港处理船舶离港数据失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
        else
        {
            switch (requestType)
            {
                case RequestTypeConstants.SAVELEAVEPLANCOMPLETE:
                    closeProgressDialog();
                    Toast.makeText(this, "保存离港处理计划执行完毕数据失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.SAVESHIPLEAVE:
                    closeProgressDialog();
                    Toast.makeText(this, "保存离港处理船舶离港数据失败！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * 
     * <一句话功能简述>获取当前时间
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private String getCurrentDateTime()
    {
        String time = "";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = sdf.format(calendar.getTime());
        return time;
    }
    
    /**
     * 
     * <一句话功能简述>保存离港处理计划执行完毕数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-16
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void sendPlanDoneRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            // body.put("completeTime", planLeaveTime);
            body.put("completeTime", "2014-4-16 10:00:00");
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            mNetTask = new HttpNetFactoryCreator(RequestTypeConstants.SAVELEAVEPLANCOMPLETE).create();
            NetRequestController.getYwblList(mNetTask,
                mHandler,
                RequestTypeConstants.SAVELEAVEPLANCOMPLETE,
                body,
                "saveLeavePlanComplete");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>保存离港处理船舶离港数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-16
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void sendShipLeaveRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            
            if ("".equals(tongzhi_time.getText().toString().trim()))
            {
                Toast.makeText(this, "请输入通知引水时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("leadTime", tongzhi_time.getText().toString().trim());
            }
            if ("".equals(reason_edit.getText().toString().trim()))
            {
                Toast.makeText(this, "请输入原因！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("remark", reason_edit.getText().toString().trim());
            }
            if ("".equals(position_time.getText().toString().trim()))
            {
                Toast.makeText(this, "请输入离港时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("leaveTime", position_time.getText().toString().trim());
            }
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            mNetTask = new HttpNetFactoryCreator(RequestTypeConstants.SAVESHIPLEAVE).create();
            NetRequestController.getYwblList(mNetTask,
                mHandler,
                RequestTypeConstants.SAVESHIPLEAVE,
                body,
                "saveShipLeave");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>显示等待框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-11
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
     * <一句话功能简述>关闭等待框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-11
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void closeProgressDialog()
    {
        loadLayout.setVisibility(View.GONE);
    }
}
