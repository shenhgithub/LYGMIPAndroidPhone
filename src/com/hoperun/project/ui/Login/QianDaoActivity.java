/*
 * File name:  LoginActivity.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2013-9-23
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.Login;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.adpter.other.QianDaoAdapter;
import com.hoperun.manager.components.CustomDialog;
import com.hoperun.manager.components.CustomDialogListener;
import com.hoperun.manager.components.HeadView;
import com.hoperun.manager.components.WaitDialog;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.StringUtils;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityMetaData.HistoryRecord;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.GpsUtil;
import com.hoperun.mipmanager.utils.GpsUtil.OnLocalResultListen;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * 签到界面
 * 
 * @Description 签到界面
 * 
 * @author wen_tao
 * @Version [版本号, 2013-9-23]
 */
public class QianDaoActivity extends PMIPBaseActivity implements OnClickListener
{
    
    /** 当前位置 **/
    private TextView            currentPosition;
    
    /** 签到记录listview **/
    private ListView            qianDaoList;
    
    /** 等待对话框 **/
    private WaitDialog          waitDialog;
    
    /** 地理位置适配器 **/
    private QianDaoAdapter      qianDaoAdapter;
    
    /** 数据列表 **/
    private List<HistoryRecord> listdata;
    
    /**
     * 签到按钮
     */
    private ImageView           signIv;
    
    private HeadView            mHeadView;
    
    private TextView            currentTimeTv;
    
    /**
     * 重载方法
     * 
     * @param requestType 请求id
     * @param Header 返回的头
     * @param obj 返回的body
     * @param error 是否返回成功
     * @param errorCode 返回错误码
     * @author wang_ling
     */
    @Override
    public void onPostHandle(int requestType, Object Header, Object obj, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETHISTORYRECORD_REQUEST:
                    MetaResponseBody doresponseBuzBody = (MetaResponseBody)obj;
                    List<HashMap<String, Object>> ret = doresponseBuzBody.getBuzList();
                    if (ret == null || ret.size() == 0)
                    {
                        Toast.makeText(QianDaoActivity.this, "没有返回数据", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        if (!"".equals(ret.get(0).get("signlist")))
                        {
                            ArrayList<HashMap<String, Object>> map =
                                (ArrayList<HashMap<String, Object>>)ret.get(0).get("signlist");
                            parseRecord(map);
                        }
                    }
                    break;
                case RequestTypeConstants.QIANDAO_REQUEST:
                    Toast.makeText(QianDaoActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
                    NetRequestController.getHistoricalRecordsRequest(mHandler,
                        RequestTypeConstants.GETHISTORYRECORD_REQUEST,
                        null);
                    break;
                default:
                    break;
            }
        }
        else
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETHISTORYRECORD_REQUEST:
                    Toast.makeText(QianDaoActivity.this, "返回数据为空", Toast.LENGTH_SHORT).show();
                    break;
                case RequestTypeConstants.QIANDAO_REQUEST:
                    Toast.makeText(QianDaoActivity.this, "签到失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * 解析签到记录
     */
    private void parseRecord(ArrayList<HashMap<String, Object>> list)
    {
        if (listdata == null)
        {
            listdata = new ArrayList<HistoryRecord>();
        }
        else
        {
            listdata.clear();
        }
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            HashMap<String, Object> object = list.get(i);
            HistoryRecord historyRecord = new HistoryRecord();
            historyRecord.setUserId((String)object.get("userid"));
            historyRecord.setSiginId((String)object.get("signid"));
            String time = (String)object.get("signtime");
            // String time = (String)object.get("signtime").toString().split(" ")[1];
            // if (time.length() > 5)
            // {
            // time = time.substring(0, 5);
            // }
            historyRecord.setSiginTime(time);
            historyRecord.setLocation((String)object.get("location"));
            listdata.add(historyRecord);
        }
        MyComparator myComparator = new MyComparator();
        Collections.sort(listdata, myComparator);
        
        HistoryRecord historyRecord = new HistoryRecord();
        historyRecord.setUserId("");
        historyRecord.setSiginId("");
        historyRecord.setSiginTime("");
        historyRecord.setLocation(list.get(0).get("signtime").toString().split(" ")[0]);
        listdata.add(0, historyRecord);
        
        qianDaoAdapter.setList(listdata);
    }
    
    public static class MyComparator implements Comparator<HistoryRecord>
    {
        /**
         * 重载方法
         * 
         * @param lhs
         * @param rhs
         * @return
         * @author wang_ling
         */
        @Override
        public int compare(HistoryRecord lhs, HistoryRecord rhs)
        {
            // return lhs.getSiginTime().compareTo(rhs.getSiginTime());
            
            if (!"".equals(lhs.getSiginTime()) && !"".equals(rhs.getSiginTime()))
            {
                
                return strdateTolongstr(lhs.getSiginTime()) > strdateTolongstr(rhs.getSiginTime()) ? -1 : 1;
            }
            else
            {
                return -1;
            }
        }
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
    
    /**
     * 
     * 重载方法
     * 
     * @param savedInstanceState savedInstanceState
     * @author wang_ling
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.qiandao_activity);
        initView();
        initData();
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    protected void onResume()
    {
        currentTimeTv.setText(StringUtils.getCurrentTime2());
        super.onResume();
        
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
    }
    
    /**
     * 
     * 初始化界面
     * 
     * @Description 初始化界面
     * 
     * @LastModifiedDate：2013-9-23
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initView()
    {
        mHeadView = (HeadView)findViewById(R.id.mobile_header);
        signIv = (ImageView)findViewById(R.id.signIV);
        currentTimeTv = (TextView)findViewById(R.id.current_time);
        // ok = (Button)findViewById(R.id.ok);
        // cancel = (Button)findViewById(R.id.cancel);
        // back = (ImageView)findViewById(R.id.fragment_back);
        // ok.setOnClickListener(this);
        // cancel.setOnClickListener(this);
        // cancel.setVisibility(View.INVISIBLE);
        // back.setOnClickListener(this);
        signIv.setOnClickListener(this);
        currentPosition = (TextView)findViewById(R.id.current_position);
        qianDaoList = (ListView)findViewById(R.id.position_record);
        qianDaoAdapter = new QianDaoAdapter(getApplicationContext(), null);
        qianDaoList.setAdapter(qianDaoAdapter);
        mHeadView.setTitle(GlobalState.getInstance().getUserName());
        mHeadView.setLeftOnclickLisen(mPersonSetListener);
        mHeadView.setRightOnclickLisen(mExistOnclickListen);
        
    }
    
    private OnClickListener mPersonSetListener  = new OnClickListener()
                                                {
                                                    
                                                    @Override
                                                    public void onClick(View v)
                                                    {
                                                        Intent intent = new Intent();
                                                        intent.setClass(QianDaoActivity.this, PersonSetView.class);
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
                                                                    new Intent(QianDaoActivity.this,
                                                                        LoginActivity.class);
                                                                it.putExtra("biaozhi", "0");
                                                                startActivity(it);
                                                                
                                                                QianDaoActivity.this.finish();
                                                            }
                                                        });
                                                        
                                                    }
                                                };
    
    /**
     * 
     * 初始化数据
     * 
     * @Description 初始化数据
     * 
     * @LastModifiedDate：2013-9-23
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        GpsUtil.getGpsByBaidu(getApplicationContext(), new OnLocalResultListen()
        {
            
            @Override
            public void onLocalResultListener(HashMap<String, String> map)
            {
                String address = map.get("address");
                
                currentPosition.setText(address);
            }
        });
        NetRequestController.getHistoricalRecordsRequest(mHandler, RequestTypeConstants.GETHISTORYRECORD_REQUEST, null);
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @author wen_tao
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.signIV:
                String address = currentPosition.getText().toString().trim();
                if ("".equals(address) || null == address)
                {
                    Toast.makeText(QianDaoActivity.this, "无法定位到当前地点，请稍后重试", Toast.LENGTH_SHORT).show();
                    GpsUtil.getGpsByBaidu(getApplicationContext(), new OnLocalResultListen()
                    {
                        
                        @Override
                        public void onLocalResultListener(HashMap<String, String> map)
                        {
                            String address = map.get("address");
                            currentPosition.setText(address);
                        }
                    });
                    return;
                }
                NetRequestController.qianDaoRequest(mHandler, RequestTypeConstants.QIANDAO_REQUEST, address);
                break;
            case R.id.fragment_back:
                finish();
                break;
            
            default:
                break;
        }
        
    }
    
    private CustomDialog dialog;
    
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
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            
            dialog =
                CustomDialog.newInstance(this.getResources().getString(R.string.main_exit_app), this.getResources()
                    .getString(R.string.Login_Cancel), this.getResources().getString(R.string.Login_Confirm));
            // dialog.show(MainActivityNewActivity.this.getFragmentManager(), "ExitDialog");
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
        return true;
    }
}
