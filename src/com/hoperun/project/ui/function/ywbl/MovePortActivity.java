/*
 * File name:  MovePortActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-4-10
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
 * <一句话功能简述>离泊处理界面
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-4-11]
 */
public class MovePortActivity extends PMIPBaseActivity implements OnClickListener
{
    /** 关闭 **/
    private RelativeLayout    closeBtn;
    
    /** 等待框布局 **/
    private RelativeLayout    loadLayout;
    
    /** 等待图片 **/
    private ImageView         loadImageView;
    
    /** 泊位id选择布局 **/
    private RelativeLayout    number_block;
    
    /** 泊位id显示文本 **/
    private TextView          num_text;
    
    /** 泊位公司显示文本 **/
    private TextView          maodi_text;
    
    /** 泊位方向选择布局 **/
    private RelativeLayout    fangxiang_block;
    
    /** 泊位方向显示文本 **/
    private TextView          fangxiang_text;
    
    /** 当前时间字符串 **/
    private String            currentDateTime;
    
    /** 获取泊位列表 **/
    protected NetTask         mGetBoweiListRequst;
    
    /** 泊位列表，包括泊位id和公司名 **/
    private List<BoweiEntity> boweiList   = new ArrayList<BoweiEntity>();
    
    /** 泊位位置列表 **/
    private List<BoweiEntity> postionList = new ArrayList<BoweiEntity>();
    
    /** 泊位列表adapter **/
    BoweiListAdapter          madapter;
    
    /** 泊位位置adapter **/
    PositionListAdapter       nadapter;
    
    /** 弹出框 **/
    PopupWindow               mPopupWindow;
    
    /** 计划执行完毕 **/
    private Button            plan_done_btn;
    
    /** 离开泊位 **/
    private Button            leave_btn;
    
    /** 移泊 **/
    private Button            move_btn;
    
    /** 计划移泊时间 **/
    private TextView          plan_move_time;
    
    /** 计划移泊泊位 **/
    private TextView          plan_bowei;
    
    /** 离开当前泊位时间 **/
    private TextView          move_time;
    
    /** 停靠新的泊位时间 **/
    private TextView          newposition_time;
    
    /** 当前泊位显示文本 **/
    private TextView          currentPositionTextview;
    
    /** 网络请求 **/
    protected NetTask         mNetTask;
    
    /** 船舶Id **/
    private String            shipId;
    
    /** 计划移泊时间 **/
    private String            planMoveTime;
    
    /** 计划移泊泊位 **/
    private String            planMovePosition;
    
    /** 当前停靠泊位 **/
    private String            currentPosition;
    
    /** 作用公司id **/
    private String            jobCompanyId;
    
    /** 当前停靠泊位 **/
    private String            berthno     = "";
    
    private void getData()
    {
        Intent data = getIntent();
        shipId = data.getStringExtra("shipid");
        currentPosition = data.getStringExtra("berthid");
        planMoveTime = data.getStringExtra("planmovetime");
        planMovePosition = data.getStringExtra("planmoveposition");
    }
    
    /**
     * 
     * <一句话功能简述>初始化界面
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-11
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void iniView()
    {
        closeBtn = (RelativeLayout)findViewById(R.id.close_btn);
        currentPositionTextview = (TextView)findViewById(R.id.current_position);
        currentPositionTextview.setText(currentPosition);
        plan_move_time = (TextView)findViewById(R.id.moveport_time);
        plan_move_time.setText(planMoveTime);
        plan_bowei = (TextView)findViewById(R.id.move_position);
        plan_bowei.setText(planMovePosition);
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        number_block = (RelativeLayout)findViewById(R.id.number_block);
        num_text = (TextView)findViewById(R.id.num_text);
        maodi_text = (TextView)findViewById(R.id.maodi_text);
        fangxiang_block = (RelativeLayout)findViewById(R.id.fangxiang_block);
        fangxiang_text = (TextView)findViewById(R.id.fangxiang_text);
        plan_done_btn = (Button)findViewById(R.id.plan_done_btn);
        leave_btn = (Button)findViewById(R.id.leave_btn);
        move_btn = (Button)findViewById(R.id.move_btn);
        move_time = (TextView)findViewById(R.id.move_time);
        newposition_time = (TextView)findViewById(R.id.newposition_time);
    }
    
    /**
     * 
     * <一句话功能简述>设置监听
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-11
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void setClick()
    {
        closeBtn.setOnClickListener(this);
        number_block.setOnClickListener(this);
        fangxiang_block.setOnClickListener(this);
        plan_done_btn.setOnClickListener(this);
        leave_btn.setOnClickListener(this);
        move_btn.setOnClickListener(this);
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
        setContentView(R.layout.moveportdeal_layout);
        getData();
        currentDateTime = getCurrentDateTime();
        iniView();
        setClick();
        move_time.setOnClickListener(new MoveTimeOnClick());
        newposition_time.setOnClickListener(new NewPositionTimeOnClick());
        sendGetListRequest();
        sendGetPosRequest();
        sendDedailRequest();
        
    }
    
    private final class MoveTimeOnClick implements OnClickListener
    {
        
        public void onClick(View v)
        {
            
            DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
            
            MovePortActivity.this);
            
            dateTimePicKDialog.dateTimePicKDialog(move_time, 0, 1);
            
        }
        
    }
    
    private final class NewPositionTimeOnClick implements OnClickListener
    {
        
        public void onClick(View v)
        {
            
            DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
            
            MovePortActivity.this);
            
            dateTimePicKDialog.dateTimePicKDialog(newposition_time, 0, 1);
            
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
            case R.id.number_block:// 泊位id选择
                boweiListPopwindow();
                break;
            case R.id.fangxiang_block:// 泊位方向选择
                positionListPopwindow();
                break;
            case R.id.plan_done_btn:// 计划执行完毕
                sendPlanDoneRequest();
                break;
            case R.id.leave_btn:// 离开泊位
                sendLeavePortRequest();
                break;
            case R.id.move_btn:// 移泊
                sendMoveRequest();
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
                case RequestTypeConstants.GETKAOBODETAIL:
                    closeProgressDialog();
                    MetaResponseBody responseBody = (MetaResponseBody)objBody;
                    List<HashMap<String, Object>> mret = responseBody.getBuzList();
                    // 成功
                    if ("0".equals(responseBody.getRetError()) && (mret != null) && (mret.size() > 0))
                    {
                        // 当前停靠泊位
                        berthno = (String)mret.get(0).get("berthno");
                    }
                    else
                    {
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    break;
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
                    List<HashMap<String, Object>> mmret = responseBuzBody1.getBuzList();
                    if (mmret != null && mmret.size() > 0)
                    {
                        if (!"".equals(mmret.get(0).get("berthposlist")))
                        {
                            
                            List<HashMap<String, Object>> mret1 =
                                (List<HashMap<String, Object>>)mmret.get(0).get("berthposlist");
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
                case RequestTypeConstants.SAVESHIFTINGPLANCOMPLETE:// 计划执行完毕
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "保存移泊处理计划执行完成数据失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RequestTypeConstants.SAVESHIPLEAVEBERTH:// 离开泊位
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "保存移泊处理船舶离开当前泊位数据失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RequestTypeConstants.SAVESHIPSHIFTBERTH:// 移泊
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "保存移泊处理船舶移泊到新泊位数据失败！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "获取位置列表失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.SAVESHIFTINGPLANCOMPLETE:
                    closeProgressDialog();
                    Toast.makeText(this, "保存移泊处理计划执行完成数据失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.SAVESHIPLEAVEBERTH:
                    closeProgressDialog();
                    Toast.makeText(this, "保存移泊处理船舶离开当前泊位数据失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.SAVESHIPSHIFTBERTH:
                    closeProgressDialog();
                    Toast.makeText(this, "保存移泊处理船舶移泊到新泊位数据失败！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
    
    /** 获取详细信息 **/
    protected NetTask mGetDetailRequst;
    
    private void sendDedailRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            mGetDetailRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETKAOBODETAIL).create();
            NetRequestController.getYwblList(mGetDetailRequst,
                mHandler,
                RequestTypeConstants.GETKAOBODETAIL,
                body,
                "getTodoJobDetail");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>发送获取泊位列表信息请求
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
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
     * @LastModifiedDate：2014-4-14
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
     * <一句话功能简述>弹出泊位id选择列表
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
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
                
                num_text.setText(boweiList.get(arg2).getStringKeyValue(BoweiEntity.berthno));
                if ("00".equals(boweiList.get(arg2).getStringKeyValue(BoweiEntity.berthno)))
                {
                    maodi_text.setText("锚地");
                }
                else
                {
                    maodi_text.setText(boweiList.get(arg2).getStringKeyValue(BoweiEntity.department));
                    jobCompanyId = boweiList.get(arg2).getStringKeyValue(BoweiEntity.deptcode);
                }
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation(closeBtn, Gravity.CENTER, 0, 0);
    }
    
    /**
     * 
     * <一句话功能简述>泊位位置弹出框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
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
                fangxiang_text.setText(postionList.get(arg2).getStringKeyValue(BoweiEntity.position));
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation(closeBtn, Gravity.CENTER, 0, 0);
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
    
    /**
     * 
     * <一句话功能简述>获取当前时间
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-11
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
     * <一句话功能简述>计划执行完毕请求
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
            body.put("completeTime", getCurrentDateTime());
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            mGetBoweiListRequst = new HttpNetFactoryCreator(RequestTypeConstants.SAVESHIFTINGPLANCOMPLETE).create();
            NetRequestController.getYwblList(mGetBoweiListRequst,
                mHandler,
                RequestTypeConstants.SAVESHIFTINGPLANCOMPLETE,
                body,
                "saveShiftingPlanComplete");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>离开泊位请求
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-16
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void sendLeavePortRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            if ("".equals(move_time.getText().toString().trim()))
            {
                Toast.makeText(this, "请输入离开泊位时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("leaveTime", move_time.getText().toString().trim());
            }
            body.put("berthId", berthno);
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            mGetBoweiListRequst = new HttpNetFactoryCreator(RequestTypeConstants.SAVESHIPLEAVEBERTH).create();
            NetRequestController.getYwblList(mGetBoweiListRequst,
                mHandler,
                RequestTypeConstants.SAVESHIPLEAVEBERTH,
                body,
                "saveShipLeaveBerth");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>移泊请求
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-16
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void sendMoveRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            if ("".equals(newposition_time.getText().toString().trim()))
            {
                Toast.makeText(this, "请输入抵达新的泊位时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("arriveNewBerthTime", newposition_time.getText().toString().trim());
            }
            if ("".equals(num_text.getText().toString().trim()))
            {
                Toast.makeText(this, "请选择泊位Id！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("berthId", num_text.getText().toString().trim());
                body.put(" jobCompanyId", jobCompanyId);
            }
            if ("".equals(fangxiang_text.getText().toString().trim()))
            {
                Toast.makeText(this, "请选择泊位方向！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("berthPos", fangxiang_text.getText().toString().trim());
            }
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            mGetBoweiListRequst = new HttpNetFactoryCreator(RequestTypeConstants.SAVESHIPSHIFTBERTH).create();
            NetRequestController.getYwblList(mGetBoweiListRequst,
                mHandler,
                RequestTypeConstants.SAVESHIPSHIFTBERTH,
                body,
                "saveShipShiftBerth");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
}
