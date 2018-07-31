/*
 * File name:  ShipDealActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-4-8
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.ywbl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.adpter.ywbl.BoweiListAdapter;
import com.hoperun.manager.adpter.ywbl.PositionListAdapter;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.ywbl.BoweiEntity;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.DateTimePickerDialog;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * <一句话功能简述>靠泊处理页面
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-4-8]
 */
public class KaoBoDealActivity extends PMIPBaseActivity implements OnClickListener
{
    /** 船舶id **/
    private String            shipId          = "";
    
    /** 计划进港时间 **/
    private String            planInTime      = "";
    
    /** 计划离港时间 **/
    private String            planLeaveTime   = "";
    
    /** 关闭按钮 **/
    private RelativeLayout    closeBtn;
    
    /** 计划离港时间 **/
    private TextView          plan_in_time;
    
    /** 计划离港时间 **/
    private TextView          plan_leave_time;
    
    /** 计划执行完毕按钮 **/
    private Button            planDoneBtn;
    
    /** 原因输入框 **/
    private EditText          reasonEdit;
    
    /** 泊位id文本控件 **/
    private TextView          numTextview;
    
    /** 泊位id选择布局 **/
    private RelativeLayout    numImage;
    
    /** 方向选择小三角 **/
    private RelativeLayout    directionImage;
    
    /** 方向文本控件 **/
    private TextView          directionTextview;
    
    /** 泊位公司显示文本 **/
    private TextView          boweiCompany;
    
    /** 停靠泊位时间 **/
    private TextView          stopTime;
    
    /** 船舶靠泊按钮 **/
    private Button            stopBtn;
    
    /** 获取泊位列表 **/
    protected NetTask         mGetBoweiListRequst;
    
    /** 等待框布局 **/
    private RelativeLayout    loadLayout;
    
    /** 等待图片 **/
    private ImageView         loadImageView;
    
    /** 泊位列表，包括泊位id和公司名 **/
    private List<BoweiEntity> boweiList       = new ArrayList<BoweiEntity>();
    
    /** 弹出框 **/
    PopupWindow               mPopupWindow;
    
    /** 泊位列表adapter **/
    BoweiListAdapter          madapter;
    
    /** 泊位位置adapter **/
    PositionListAdapter       nadapter;
    
    /** 当前时间字符串 **/
    private String            currentDateTime = "";
    
    /** 泊位位置列表 **/
    private List<BoweiEntity> postionList     = new ArrayList<BoweiEntity>();
    
    /** 作业公司id **/
    private String            jobCompanyId    = "";
    
    /**
     * 
     * <一句话功能简述>初始化界面
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-10
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void iniView()
    {
        closeBtn = (RelativeLayout)findViewById(R.id.close_btn);
        plan_in_time = (TextView)findViewById(R.id.come_time);
        plan_in_time.setText(planInTime);
        plan_leave_time = (TextView)findViewById(R.id.go_time);
        plan_leave_time.setText(planLeaveTime);
        planDoneBtn = (Button)findViewById(R.id.plan_done_btn);
        reasonEdit = (EditText)findViewById(R.id.reason_edit);
        numTextview = (TextView)findViewById(R.id.num_text);
        numImage = (RelativeLayout)findViewById(R.id.number_block);
        directionTextview = (TextView)findViewById(R.id.fangxiang_text);
        directionImage = (RelativeLayout)findViewById(R.id.fangxiang_block);
        stopTime = (TextView)findViewById(R.id.position_time);
        stopTime.setText(currentDateTime);
        stopBtn = (Button)findViewById(R.id.kaobo_btn);
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        boweiCompany = (TextView)findViewById(R.id.maodi_text);
        
    }
    
    /**
     * 
     * <一句话功能简述>设置监听
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-10
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void setClick()
    {
        closeBtn.setOnClickListener(this);
        planDoneBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        numImage.setOnClickListener(this);
        directionImage.setOnClickListener(this);
        
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
        setContentView(R.layout.maodishipdeal_layout);
        Intent data = getIntent();
        shipId = data.getStringExtra("shipid");
        planInTime = data.getStringExtra("planintime");
        planLeaveTime = data.getStringExtra("planleavetime");
        currentDateTime = getCurrentDateTime();
        iniView();
        setClick();
        stopTime.setOnClickListener(new StopDateTimeOnClick());
        sendGetListRequest();
        sendGetPosRequest();
    }
    
    private final class StopDateTimeOnClick implements OnClickListener
    {
        
        public void onClick(View v)
        {
            
            DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
            
            KaoBoDealActivity.this);
            
            dateTimePicKDialog.dateTimePicKDialog(stopTime, 0, 1);
            
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
            case R.id.close_btn:// 关闭
                finish();
                break;
            case R.id.plan_done_btn:// 计划执行完毕
                sendSaveBerthPlanRequest();
                break;
            case R.id.kaobo_btn:// 船舶靠泊
                sendSaveShipBerthRequest();
                break;
            case R.id.number_block:// 泊位id选择
                boweiListPopwindow();
                break;
            case R.id.fangxiang_block:// 方向选择
                positionListPopwindow();
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
                case RequestTypeConstants.GETBOWEILIST:
                    closeProgressDialog();
                    MetaResponseBody responseBuzBody = (MetaResponseBody)objBody;
                    List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                    if (ret != null && ret.size() > 0)
                    {
                        if (!"".equals(ret.get(0).get("berthlist")))
                        {
                            List<HashMap<String, Object>> ret1 =
                                (List<HashMap<String, Object>>)ret.get(0).get("berthlist");
                            for (int i = 0; i < ret1.size(); i++)
                            {
                                BoweiEntity entity = new BoweiEntity("", "");
                                entity.convertToObject(ret1.get(i));
                                boweiList.add(entity);
                            }
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
                case RequestTypeConstants.GETPOSITIONLIST:
                    closeProgressDialog();
                    MetaResponseBody responseBuzBody1 = (MetaResponseBody)objBody;
                    List<HashMap<String, Object>> mret = responseBuzBody1.getBuzList();
                    if (mret != null & mret.size() > 0)
                    {
                        if (!"".equals(mret.get(0).get("berthposlist")))
                        {
                            List<HashMap<String, Object>> mret1 =
                                (List<HashMap<String, Object>>)mret.get(0).get("berthposlist");
                            for (int i = 0; i < mret1.size(); i++)
                            {
                                BoweiEntity entity = new BoweiEntity("", "");
                                entity.convertToObject(mret1.get(i));
                                postionList.add(entity);
                            }
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
                case RequestTypeConstants.SAVEBERTHPLAN:
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, ((MetaResponseBody)objBody).getRetMsg(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RequestTypeConstants.SAVESHIPBERGH:
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, ((MetaResponseBody)objBody).getRetMsg(), Toast.LENGTH_SHORT).show();
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
                case RequestTypeConstants.GETBOWEILIST:
                    closeProgressDialog();
                    Toast.makeText(this, "获取泊位列表失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.GETPOSITIONLIST:
                    closeProgressDialog();
                    Toast.makeText(this, "获取泊位方向数据失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.SAVEBERTHPLAN:
                    closeProgressDialog();
                    Toast.makeText(this, "保存船舶靠泊处理计划完成数据失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.SAVESHIPBERGH:
                    closeProgressDialog();
                    Toast.makeText(this, "保存船舶靠泊数据失败！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * 
     * <一句话功能简述>发送获取泊位列表信息请求
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-10
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void sendGetListRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("", "");
            mGetBoweiListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETBOWEILIST).create();
            NetRequestController.getYwblList(mGetBoweiListRequst,
                mHandler,
                RequestTypeConstants.GETBOWEILIST,
                body,
                "getBerthList");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>发送获取泊位位置列表请求
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-11
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void sendGetPosRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("", "");
            mGetBoweiListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETPOSITIONLIST).create();
            NetRequestController.getYwblList(mGetBoweiListRequst,
                mHandler,
                RequestTypeConstants.GETPOSITIONLIST,
                body,
                "getBerthPosList");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>保存船舶靠泊处理计划完成数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-11
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void sendSaveBerthPlanRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            body.put("completeTime", getCurrentDateTime());
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            mGetBoweiListRequst = new HttpNetFactoryCreator(RequestTypeConstants.SAVEBERTHPLAN).create();
            NetRequestController.getYwblList(mGetBoweiListRequst,
                mHandler,
                RequestTypeConstants.SAVEBERTHPLAN,
                body,
                "saveBerthingPlanComplete");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>保存船舶靠泊数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-11
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void sendSaveShipBerthRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            if ("".equals(reasonEdit.getText().toString().trim()))
            {
                Toast.makeText(this, "请输入计划未兑现原因！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("remark", reasonEdit.getText().toString().trim());
            }
            if ("".equals(numTextview.getText().toString().trim()))
            {
                Toast.makeText(this, "请选择泊位！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("berthId", numTextview.getText().toString().trim());
                body.put("jobCompanyId", jobCompanyId);
            }
            if ("".equals(directionTextview.getText().toString().trim()))
            {
                Toast.makeText(this, "请选择方向！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("berthPos", directionTextview.getText().toString().trim());
            }
            if ("".equals(stopTime.getText().toString().trim()))
            {
                Toast.makeText(this, "请输入停靠泊位时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                
                body.put("berthTime", stopTime.getText().toString().trim());
            }
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            mGetBoweiListRequst = new HttpNetFactoryCreator(RequestTypeConstants.SAVESHIPBERGH).create();
            NetRequestController.getYwblList(mGetBoweiListRequst,
                mHandler,
                RequestTypeConstants.SAVESHIPBERGH,
                body,
                "saveShipBerthing");
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
     * @LastModifiedDate：2014-4-10
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
     * @LastModifiedDate：2014-4-10
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void closeProgressDialog()
    {
        loadLayout.setVisibility(View.GONE);
    }
    
    /**
     * 
     * <一句话功能简述>弹出泊位id选择列表
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-10
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void boweiListPopwindow()
    {
        View view = LayoutInflater.from(this).inflate(R.layout.select_bowei_dialog, null);
        mPopupWindow =
            new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ListView mListview = (ListView)view.findViewById(R.id.bowei_listview);
        TextView title = (TextView)view.findViewById(R.id.titletv);
        title.setText("泊位选择");
        madapter = new BoweiListAdapter(this, boweiList);
        mListview.setAdapter(madapter);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setContentView(view);
        mListview.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                
                numTextview.setText(boweiList.get(arg2).getStringKeyValue(BoweiEntity.berthno));
                if ("00".equals(boweiList.get(arg2).getStringKeyValue(BoweiEntity.berthno)))
                {
                    boweiCompany.setText("锚地");
                }
                else
                {
                    boweiCompany.setText(boweiList.get(arg2).getStringKeyValue(BoweiEntity.department));
                    jobCompanyId = boweiList.get(arg2).getStringKeyValue(BoweiEntity.deptcode);
                }
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation(reasonEdit, Gravity.CENTER, 0, 0);
    }
    
    /**
     * 
     * <一句话功能简述>泊位位置弹出框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-10
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void positionListPopwindow()
    {
        View view = LayoutInflater.from(this).inflate(R.layout.select_bowei_dialog, null);
        mPopupWindow =
            new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ListView mListview = (ListView)view.findViewById(R.id.bowei_listview);
        TextView title = (TextView)view.findViewById(R.id.titletv);
        title.setText("方向选择");
        nadapter = new PositionListAdapter(this, postionList);
        mListview.setAdapter(nadapter);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setContentView(view);
        mListview.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                directionTextview.setText(postionList.get(arg2).getStringKeyValue(BoweiEntity.position));
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation(reasonEdit, Gravity.CENTER, 0, 0);
    }
    
    /**
     * 
     * <一句话功能简述>获取当前时间
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-10
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
}
