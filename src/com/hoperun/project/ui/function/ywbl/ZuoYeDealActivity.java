/*
 * File name:  KaoboShipDealActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-4-8
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.ywbl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * <一句话功能简述>作业处理页面
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-4-8]
 */
public class ZuoYeDealActivity extends PMIPBaseActivity implements OnClickListener
{
    
    /** 船舶id **/
    private String         shipId           = "";
    
    /** 关闭 **/
    private RelativeLayout closeBtn;
    
    /** 余吨保存 **/
    private Button         save_left;
    
    /** 确认考核信息 **/
    private Button         makesure_info;
    
    /** 卸货开始确认 **/
    private LinearLayout   load_start;
    
    /** 卸货结束确认 **/
    private LinearLayout   load_end;
    
    /** 装货开始确认 **/
    private LinearLayout   upload_start;
    
    /** 装货结束确认 **/
    private LinearLayout   upload_end;
    
    /** 出错处理 **/
    private Button         wrong_btn;
    
    /** 不是装卸作业船舶 **/
    private Button         not_ship_btn;
    
    /** 卸货开始小图片 **/
    private ImageView      load_start_pic;
    
    /** 卸货结束小图片 **/
    private ImageView      load_end_pic;
    
    /** 装货开始小图片 **/
    private ImageView      upload_start_pic;
    
    /** 装货结束小图片 **/
    private ImageView      upload_end_pic;
    
    /** 卸货开始确认标志位 **/
    private boolean        load_start_bln   = false;
    
    /** 卸货结束确认标志位 **/
    private boolean        load_end_bln     = false;
    
    /** 装货开始确认标志位 **/
    private boolean        upload_start_bln = false;
    
    /** 装货结束确认标志位 **/
    private boolean        upload_end_bln   = false;
    
    /** 当前停靠泊位 **/
    private TextView       current_position;
    
    /** 卸货余吨 **/
    private TextView       left_tons;
    
    /** 卸船数 **/
    private TextView       load_ships;
    
    /** 总停时 **/
    private TextView       total_ships;
    
    /** 今日卸船数 **/
    private EditText       today_load_ships;
    
    /** 今日停时 **/
    private EditText       today_total_ships;
    
    /** 卸货开始时间 **/
    private TextView       load_start_time;
    
    /** 卸货结束时间 **/
    private TextView       load_end_time;
    
    /** 装货开始时间 **/
    private TextView       upload_start_time;
    
    /** 装货结束时间 **/
    private TextView       upload_end_time;
    
    /** 卸货开始按钮 **/
    private Button         load_start_btn;
    
    /** 卸货结束按钮 **/
    private Button         load_end_btn;
    
    /** 装货开始按钮 **/
    private Button         upload_start_btn;
    
    /** 装货结束按钮 **/
    private Button         upload_end_btn;
    
    /** 网络请求 **/
    protected NetTask      mNetTask;
    
    /** 等待框布局 **/
    private RelativeLayout loadLayout;
    
    /** 等待图片 **/
    private ImageView      loadImageView;
    
    /** 当前停靠泊位 **/
    private String         currentPosition;
    
    // /** 卸船数 **/
    // private String loadShips;
    //
    // /** 总停时 **/
    // private String totalShips;
    
    // /** 今日卸船数 **/
    // private String todayLoadShips;
    //
    // /** 今日停时 **/
    // private String todayTotalLoadShips;
    
    private String         berthno          = "";
    
    private void getData()
    {
        Intent data = getIntent();
        shipId = data.getStringExtra("shipid");
        currentPosition = data.getStringExtra("berthid");
    }
    
    private void iniView()
    {
        closeBtn = (RelativeLayout)findViewById(R.id.close_btn);
        save_left = (Button)findViewById(R.id.save_left);
        makesure_info = (Button)findViewById(R.id.makesure_info);
        load_start = (LinearLayout)findViewById(R.id.load_start);
        load_end = (LinearLayout)findViewById(R.id.load_end);
        upload_start = (LinearLayout)findViewById(R.id.upload_start);
        upload_end = (LinearLayout)findViewById(R.id.upload_end);
        wrong_btn = (Button)findViewById(R.id.wrong_btn);
        not_ship_btn = (Button)findViewById(R.id.not_ship_btn);
        load_start_pic = (ImageView)findViewById(R.id.load_start_pic);
        load_end_pic = (ImageView)findViewById(R.id.load_end_pic);
        upload_start_pic = (ImageView)findViewById(R.id.upload_start_pic);
        upload_end_pic = (ImageView)findViewById(R.id.upload_end_pic);
        
        current_position = (TextView)findViewById(R.id.current_position);
        
        left_tons = (TextView)findViewById(R.id.left_tons);
        load_ships = (TextView)findViewById(R.id.load_ships);
        total_ships = (TextView)findViewById(R.id.total_ships);
        today_load_ships = (EditText)findViewById(R.id.today_load_ships);
        // 设置光标位置
        Editable e1 = today_load_ships.getText();
        int postion1 = e1.length();
        Selection.setSelection(e1, postion1);
        today_total_ships = (EditText)findViewById(R.id.today_total_ships);
        // 设置光标位置
        Editable e2 = today_total_ships.getText();
        int postion2 = e2.length();
        Selection.setSelection(e2, postion2);
        load_start_time = (TextView)findViewById(R.id.load_start_time);
        load_end_time = (TextView)findViewById(R.id.load_end_time);
        upload_start_time = (TextView)findViewById(R.id.upload_start_time);
        upload_end_time = (TextView)findViewById(R.id.upload_end_time);
        
        load_start_btn = (Button)findViewById(R.id.load_start_btn);
        load_end_btn = (Button)findViewById(R.id.load_end_btn);
        upload_start_btn = (Button)findViewById(R.id.upload_start_btn);
        upload_end_btn = (Button)findViewById(R.id.upload_end_btn);
        
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        
    }
    
    private void setClick()
    {
        closeBtn.setOnClickListener(this);
        save_left.setOnClickListener(this);
        makesure_info.setOnClickListener(this);
        load_start.setOnClickListener(this);
        load_end.setOnClickListener(this);
        upload_end.setOnClickListener(this);
        upload_start.setOnClickListener(this);
        wrong_btn.setOnClickListener(this);
        not_ship_btn.setOnClickListener(this);
        load_end_btn.setOnClickListener(this);
        load_start_btn.setOnClickListener(this);
        upload_end_btn.setOnClickListener(this);
        upload_start_btn.setOnClickListener(this);
        
        load_start_time.setOnClickListener(new loadStartTimeOnClick());
        load_end_time.setOnClickListener(new loadEndTimeOnClick());
        upload_start_time.setOnClickListener(new uploadStartTimeOnClick());
        upload_end_time.setOnClickListener(new uploadEndTimeOnClick());
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
        setContentView(R.layout.kaoboshipdeal_layout);
        getData();
        iniView();
        setClick();
        sendDedailRequest();
    }
    
    private final class loadStartTimeOnClick implements OnClickListener
    {
        
        public void onClick(View v)
        {
            
            DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
            
            ZuoYeDealActivity.this);
            
            dateTimePicKDialog.dateTimePicKDialog(load_start_time, 0, 1);
            
        }
        
    }
    
    private final class loadEndTimeOnClick implements OnClickListener
    {
        
        public void onClick(View v)
        {
            
            DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
            
            ZuoYeDealActivity.this);
            
            dateTimePicKDialog.dateTimePicKDialog(load_end_time, 0, 1);
            
        }
        
    }
    
    private final class uploadStartTimeOnClick implements OnClickListener
    {
        
        public void onClick(View v)
        {
            
            DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
            
            ZuoYeDealActivity.this);
            
            dateTimePicKDialog.dateTimePicKDialog(upload_start_time, 0, 1);
            
        }
        
    }
    
    private final class uploadEndTimeOnClick implements OnClickListener
    {
        
        public void onClick(View v)
        {
            
            DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
            
            ZuoYeDealActivity.this);
            
            dateTimePicKDialog.dateTimePicKDialog(upload_end_time, 0, 1);
            
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
            case R.id.save_left:// 余吨保存
                saveJobRestTonRequest();
                break;
            case R.id.makesure_info:// 确认考核信息
                saveJobCheckRequest();
                break;
            case R.id.load_start:// 卸货开始确认
                if (null == load_start_time.getText().toString().trim()
                    || "".equals(load_start_time.getText().toString().trim()))
                {
                    Toast.makeText(this, "请填写卸货开始时间！", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    load_start_bln = !load_start_bln;
                    showPic(load_start_bln, load_start_pic);
                    showBtnBackground(load_start_bln, load_start_btn);
                }
                break;
            case R.id.load_end:// 卸货结束确认
                if (null == load_end_time.getText().toString().trim()
                    || "".equals(load_end_time.getText().toString().trim()))
                {
                    Toast.makeText(this, "请填写卸货结束时间！", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    load_end_bln = !load_end_bln;
                    showPic(load_end_bln, load_end_pic);
                    showBtnBackground(load_end_bln, load_end_btn);
                }
                break;
            case R.id.upload_start:// 装货开始确认
                if (null == upload_start_time.getText().toString().trim()
                    || "".equals(upload_start_time.getText().toString().trim()))
                {
                    Toast.makeText(this, "请填写装货开始时间！", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    upload_start_bln = !upload_start_bln;
                    showPic(upload_start_bln, upload_start_pic);
                    showBtnBackground(upload_start_bln, upload_start_btn);
                }
                break;
            case R.id.upload_end:// 装货结束确认
                if (null == upload_end_time.getText().toString().trim()
                    || "".equals(upload_end_time.getText().toString().trim()))
                {
                    Toast.makeText(this, "请填写装货结束时间！", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    upload_end_bln = !upload_end_bln;
                    showPic(upload_end_bln, upload_end_pic);
                    showBtnBackground(upload_end_bln, upload_end_btn);
                }
                break;
            case R.id.wrong_btn:// 出错处理
                saveJobErrorProcessRequest();
                break;
            case R.id.not_ship_btn:// 不是装卸作业船舶
                saveJobNonLoadUnloadShipRequest();
                break;
            case R.id.load_start_btn:// 卸货开始
                if (load_start_bln)
                {
                    saveJobUnloadStartRequest();
                }
                // load_start_time.setText(getCurrentDateTime());
                break;
            case R.id.load_end_btn:// 卸货结束
                if (load_end_bln)
                {
                    saveJobUnloadEndRequest();
                }
                // load_end_time.setText(getCurrentDateTime());
                break;
            case R.id.upload_start_btn:// 装货开始
                if (upload_start_bln)
                {
                    saveJobLoadStartRequest();
                }
                // upload_start_time.setText(getCurrentDateTime());
                break;
            case R.id.upload_end_btn:// 装货结束
                if (upload_end_bln)
                {
                    saveJobLoadEndRequest();
                }
                // upload_end_time.setText(getCurrentDateTime());
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
                case RequestTypeConstants.GETKAOBODETAIL:// 船舶详细
                    closeProgressDialog();
                    MetaResponseBody responseBuzBody = (MetaResponseBody)objBody;
                    List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                    // 成功
                    if ("0".equals(responseBuzBody.getRetError()) && (ret != null) && (ret.size() > 0))
                    {
                        // 当前停靠泊位
                        current_position.setText((CharSequence)ret.get(0).get("berthno"));
                        berthno = (String)ret.get(0).get("berthno");
                        // 卸货余吨
                        if (ret.get(0).get("currestton") != null && !"".equals(ret.get(0).get("currestton")))
                        {
                            left_tons.setText((CharSequence)ret.get(0).get("currestton"));
                        }
                        else
                        {
                            left_tons.setText("0");
                        }
                        // 卸船数
                        load_ships.setText((CharSequence)ret.get(0).get("loadunloadweight"));
                        // 总停时
                        total_ships.setText((CharSequence)ret.get(0).get("shipremaintime"));
                        // 卸货开始时间
                        load_start_time.setText((CharSequence)ret.get(0).get("unloadbegintime"));
                        // 卸货结束时间
                        load_end_time.setText((CharSequence)ret.get(0).get("unloadendtime"));
                        // 装货开始时间
                        upload_start_time.setText((CharSequence)ret.get(0).get("loadbegintime"));
                        // 装货结束时间
                        upload_end_time.setText((CharSequence)ret.get(0).get("loadendtime"));
                    }
                    else
                    {
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RequestTypeConstants.SAVEJOBRESTTON:// 余吨保存
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "余吨保存失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RequestTypeConstants.SAVEJOBChECK:// 确认考核信息
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "确认考核信息失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RequestTypeConstants.SAVEShIPShIFTBERTH:// 装货开始
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "保存装货开始时间失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RequestTypeConstants.SAVEJOBLOADEND:// 装货结束
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "保存装货结束时间失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RequestTypeConstants.SAVEJOBUNLOADSTART:// 卸货开始
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "保存卸货开始时间失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RequestTypeConstants.SAVEJOBUNLOADEND:// 卸货结束
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "保存卸货结束时间失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RequestTypeConstants.SAVEJOBERRORPROCESS:// 出错处理
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "保存出错处理信息失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RequestTypeConstants.SAVEJOBNONLOADUNLOADSHIP:// 不是装卸作业船舶
                    closeProgressDialog();
                    if ("0".equals(((MetaResponseBody)objBody).getRetError()))
                    {
                        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "保存作业处理非装卸作业船舶数据失败！", Toast.LENGTH_SHORT).show();
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
                case RequestTypeConstants.SAVEJOBRESTTON:// 余吨保存
                    closeProgressDialog();
                    Toast.makeText(this, "保存余吨数据失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.SAVEJOBChECK:// 确认考核信息
                    closeProgressDialog();
                    Toast.makeText(this, "保存考核信息失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.SAVEShIPShIFTBERTH:// 装货开始
                    closeProgressDialog();
                    Toast.makeText(this, "保存装货开始时间失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.SAVEJOBLOADEND:// 装货结束
                    closeProgressDialog();
                    Toast.makeText(this, "保存装货结束时间失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.SAVEJOBUNLOADSTART:// 卸货开始
                    closeProgressDialog();
                    Toast.makeText(this, "保存卸货开始时间失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.SAVEJOBUNLOADEND:// 卸货结束
                    closeProgressDialog();
                    Toast.makeText(this, "保存卸货结束时间失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.SAVEJOBERRORPROCESS:// 出错处理
                    closeProgressDialog();
                    Toast.makeText(this, "保存出错处理信息失败！", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.SAVEJOBNONLOADUNLOADSHIP:// 不是装卸作业船舶
                    closeProgressDialog();
                    Toast.makeText(this, "保存作业处理非装卸作业船舶数据失败！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * 
     * <一句话功能简述>显示图片
     * 
     * @Description<功能详细描述>
     * 
     * @param bln
     * @param view
     * @LastModifiedDate：2014-4-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void showPic(boolean bln, ImageView view)
    {
        if (bln)
        {
            view.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.xz_2));
        }
        else
        {
            view.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.xz_1));
        }
    }
    
    /**
     * 
     * <一句话功能简述>显示按钮背景图片
     * 
     * @Description<功能详细描述>
     * 
     * @param bln
     * @param btn
     * @LastModifiedDate：2014-5-30
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void showBtnBackground(boolean bln, Button btn)
    {
        if (bln)
        {
            btn.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.ship_button_1));
        }
        else
        {
            btn.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.ship_button_2));
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
     * <一句话功能简述>保存作业处理船舶余吨数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void saveJobRestTonRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            body.put("restTon", left_tons.getText().toString().trim());// 余吨
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            mNetTask = new HttpNetFactoryCreator(RequestTypeConstants.SAVEJOBRESTTON).create();
            NetRequestController.getYwblList(mNetTask,
                mHandler,
                RequestTypeConstants.SAVEJOBRESTTON,
                body,
                "saveJobRestTon");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>保存作业处理考核确认数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void saveJobCheckRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            body.put("unloadAmount", today_load_ships.getText().toString().trim());// 今日卸船数
            body.put("stopHour", today_total_ships.getText().toString().trim());// 今日停时
            body.put("berthNo", berthno);
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            mNetTask = new HttpNetFactoryCreator(RequestTypeConstants.SAVEJOBChECK).create();
            NetRequestController.getYwblList(mNetTask,
                mHandler,
                RequestTypeConstants.SAVEJOBChECK,
                body,
                "saveJobCheck");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>保存作业处理装货开始时间
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void saveJobLoadStartRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            if ("".equals(upload_start_time.getText().toString().trim()))
            {
                Toast.makeText(this, "装货开始时间不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("jobTime", upload_start_time.getText().toString().trim());
            }
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            body.put("berthNo", berthno);
            
            mNetTask = new HttpNetFactoryCreator(RequestTypeConstants.SAVEShIPShIFTBERTH).create();
            NetRequestController.getYwblList(mNetTask,
                mHandler,
                RequestTypeConstants.SAVEShIPShIFTBERTH,
                body,
                "saveJobLoadStart");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>保存作业处理装货结束时间
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void saveJobLoadEndRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            body.put("berthNo", berthno);
            body.put("beginTime", upload_start_time.getText().toString().trim());
            if ("".equals(upload_end_time.getText().toString().trim()))
            {
                Toast.makeText(this, "装货结束时间不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("endTime", upload_end_time.getText().toString().trim());
            }
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            
            mNetTask = new HttpNetFactoryCreator(RequestTypeConstants.SAVEJOBLOADEND).create();
            NetRequestController.getYwblList(mNetTask,
                mHandler,
                RequestTypeConstants.SAVEJOBLOADEND,
                body,
                "saveJobLoadEnd");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>保存作业处理卸货开始时间
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void saveJobUnloadStartRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            if ("".equals(load_start_time.getText().toString().trim()))
            {
                Toast.makeText(this, "卸货开始时间不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("jobTime", load_start_time.getText().toString().trim());
            }
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            body.put("berthNo", berthno);
            
            mNetTask = new HttpNetFactoryCreator(RequestTypeConstants.SAVEJOBUNLOADSTART).create();
            NetRequestController.getYwblList(mNetTask,
                mHandler,
                RequestTypeConstants.SAVEJOBUNLOADSTART,
                body,
                "saveJobUnloadStart");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>保存作业处理卸货结束时间
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void saveJobUnloadEndRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            body.put("beginTime", load_start_time.getText().toString().trim());
            if ("".equals(load_end_time.getText().toString().trim()))
            {
                Toast.makeText(this, "卸货结束时间不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                body.put("endTime", load_end_time.getText().toString().trim());
                
            }
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            
            body.put("berthNo", berthno);
            mNetTask = new HttpNetFactoryCreator(RequestTypeConstants.SAVEJOBUNLOADEND).create();
            NetRequestController.getYwblList(mNetTask,
                mHandler,
                RequestTypeConstants.SAVEJOBUNLOADEND,
                body,
                "saveJobUnloadEnd");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>保存作业处理错误处理数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void saveJobErrorProcessRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            body.put("berthNo", berthno);
            mNetTask = new HttpNetFactoryCreator(RequestTypeConstants.SAVEJOBERRORPROCESS).create();
            NetRequestController.getYwblList(mNetTask,
                mHandler,
                RequestTypeConstants.SAVEJOBERRORPROCESS,
                body,
                "saveJobErrorProcess");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        showProgressDialog();
    }
    
    /**
     * 
     * <一句话功能简述>保存作业处理非装卸作业船舶数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void saveJobNonLoadUnloadShipRequest()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("shipId", shipId);
            body.put("operatorId", GlobalState.getInstance().getUserid());
            body.put("operatorName", GlobalState.getInstance().getUserName());
            body.put("operateTime", getCurrentDateTime());
            mNetTask = new HttpNetFactoryCreator(RequestTypeConstants.SAVEJOBNONLOADUNLOADSHIP).create();
            NetRequestController.getYwblList(mNetTask,
                mHandler,
                RequestTypeConstants.SAVEJOBNONLOADUNLOADSHIP,
                body,
                "saveJobNonLoadUnloadShip");
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
     * @LastModifiedDate：2014-4-14
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
     * @LastModifiedDate：2014-4-14
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
}
