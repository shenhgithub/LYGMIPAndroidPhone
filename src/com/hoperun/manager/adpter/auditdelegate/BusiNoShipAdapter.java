/*
 * File name:  BusiNohaveAdapter.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  li_miao
 * Last modified date:  2014-4-9
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.auditdelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.components.WaitDialog;
import com.hoperun.manager.components.CustomHandler.CustomHanler;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.StringUtils;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author li_miao
 * @Version [版本号, 2014-4-9]
 */
public class BusiNoShipAdapter extends PMIPBaseAdapter
{
    /** 等待对话框 **/
    private WaitDialog       waitDialog;
    
    /** 上下文 **/
    private FragmentActivity context;
    
    /** 帮助类 **/
    private LayoutInflater   inflater;
    
    public List<HashMap<String, Object>> getListmap()
    {
        return listmap;
    }
    
    public void setListmap(List<HashMap<String, Object>> listmap)
    {
        this.listmap = listmap;
    }
    
    private List<HashMap<String, Object>> listmap;
    
    /** 0-无船||1-有船 **/
    private String                        currentStatus;
    
    /**
     * 审核无船作业申请
     */
    protected NetTask                     auditNonShipJob;
    
    /**
     * 销审
     */
    protected NetTask                     unAuditNonShipJob;
    
    private int                           currentPosition = 0;
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 上下文
     * @param list 列表数据
     */
    public BusiNoShipAdapter(FragmentActivity context, List<HashMap<String, Object>> list, String status)
    {
        super();
        this.context = context;
        
        this.listmap = list;
        
        this.inflater = LayoutInflater.from(this.context);
        
        this.currentStatus = status;
        
        if (null == listmap)
        {
            listmap = new ArrayList<HashMap<String, Object>>();
        }
    }
    
    /**
     * 
     * 关闭loading框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void closeWaitDialog()
    {
        if (null != waitDialog && waitDialog.isShowing())
        {
            waitDialog.dismiss();
        }
    }
    
    /**
     * 重载方法
     * 
     * @return 返回列表的长度
     * @author shen_feng
     */
    @Override
    public int getCount()
    {
        return listmap.size();
    }
    
    /**
     * 重载方法
     * 
     * @param position 位置
     * @return 返回列表的某一项
     * @author shen_feng
     */
    @Override
    public Object getItem(int position)
    {
        return listmap.size();
    }
    
    /**
     * 获取名称
     * 
     * @Description<功能详细描述>
     * 
     * @param position
     * @return
     * @LastModifiedDate：2013-11-4
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public String getItemName(int position)
    {
        return "";
        
    }
    
    /**
     * 重载方法
     * 
     * @param position 位置
     * @return 某一项的id
     * @author shen_feng
     */
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    /**
     * 重载方法
     * 
     * @param position position
     * @param convertView convertView
     * @param parent parent
     * @return view
     * @author shen_feng
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        
        ViewHold viewHold;
        if (convertView == null)
        {
            
            viewHold = new ViewHold();
            convertView = inflater.inflate(R.layout.busi_noship_item, null);
            
            viewHold.text_3 = (TextView)convertView.findViewById(R.id.text_3);
            viewHold.text_4 = (TextView)convertView.findViewById(R.id.text_4);
            viewHold.text_5 = (TextView)convertView.findViewById(R.id.text_5);
            viewHold.text_6 = (TextView)convertView.findViewById(R.id.text_6);
            
            viewHold.checkButton = (Button)convertView.findViewById(R.id.btn_audit);
            viewHold.noCheckBtn = (Button)convertView.findViewById(R.id.btn_hascheck);
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
        }
        String jobcompanyname = (String)listmap.get(position).get("jobcompanyname");
        String shipclientname = (String)listmap.get(position).get("shipclientname");
        String applydate = (String)listmap.get(position).get("applydate");
        String auditmarkname = (String)listmap.get(position).get("auditmarkname");
        
        if (StringUtils.isNull(jobcompanyname))
        {
            jobcompanyname = "";
        }
        if (StringUtils.isNull(shipclientname))
        {
            shipclientname = "";
        }
        if (StringUtils.isNull(applydate))
        {
            applydate = "";
        }
        if (StringUtils.isNull(auditmarkname))
        {
            auditmarkname = "";
        }
        viewHold.text_3.setText(jobcompanyname);
        viewHold.text_4.setText(shipclientname);
        viewHold.text_5.setText(applydate);
        viewHold.text_6.setText(auditmarkname);
        // 未审核
        if ("N".equals(auditmarkname))
        {
            viewHold.checkButton.setVisibility(View.VISIBLE);
            viewHold.noCheckBtn.setVisibility(View.GONE);
        }
        // 已审核
        else if ("Y".equals(auditmarkname))
        {
            viewHold.checkButton.setVisibility(View.GONE);
            viewHold.noCheckBtn.setVisibility(View.VISIBLE);
        }
        
        viewHold.checkButton.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                currentPosition = position;
                String methodName = "";
                // 无船审核
                if ("0".equals(currentStatus))
                {
                    methodName = "auditNonShipJob";
                }
                // 有船审核
                else if ("1".equals(currentStatus))
                {
                    methodName = "auditShipJob";
                }
                String bcnno = (String)listmap.get(position).get("bcno");
                String serial = (String)listmap.get(position).get("serial");
                if (StringUtils.isNull(bcnno))
                {
                    bcnno = "";
                }
                if (StringUtils.isNull(serial))
                {
                    serial = "";
                }
                String auditorId = GlobalState.getInstance().getOpenId();
                String auditorName = GlobalState.getInstance().getUserName();
                checkShipJob(bcnno, serial, auditorId, auditorName, methodName);
            }
        });
        
        viewHold.noCheckBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                currentPosition = position;
                String methodName = "";
                // 无船审核
                if ("0".equals(currentStatus))
                {
                    methodName = "unauditNonShipJob";
                }
                // 有船审核
                else if ("1".equals(currentStatus))
                {
                    methodName = "undoAuditShipJob";
                }
                String bcnno = (String)listmap.get(position).get("bcno");
                String serial = (String)listmap.get(position).get("serial");
                if (StringUtils.isNull(bcnno))
                {
                    bcnno = "";
                }
                if (StringUtils.isNull(serial))
                {
                    serial = "";
                }
                String auditorId = GlobalState.getInstance().getOpenId();
                String auditorName = GlobalState.getInstance().getUserName();
                unCheckShipJob(bcnno, serial, auditorId, auditorName, methodName);
            }
        });
        return convertView;
    }
    
    static class ViewHold
    {
        /** 作业公司 **/
        TextView text_3;
        
        /** 船代 **/
        TextView text_4;
        
        /** 申请日期 **/
        TextView text_5;
        
        /** 批准 **/
        TextView text_6;
        
        /** 审核按钮 **/
        Button   checkButton;
        
        /** 已审核 **/
        Button   noCheckBtn;
    }
    
    /**
     * 
     * 销审
     * 
     * @Description<功能详细描述>
     * 
     * @param bcnno
     * @param serial
     * @param auditorId
     * @param auditorName
     * @param methodname
     * @LastModifiedDate：2014-4-26
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void unCheckShipJob(String bcnno, String serial, String auditorId, String auditorName, String methodname)
    {
        waitDialog = WaitDialog.newInstance();
        waitDialog.show(context.getSupportFragmentManager(), "waitDialog");
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("bcno", bcnno);
            body.put("serial", serial);
            body.put("auditorId", auditorId);
            body.put("auditorName", auditorName);
        }
        catch (org.json.JSONException e)
        {
            e.printStackTrace();
        }
        unAuditNonShipJob = new HttpNetFactoryCreator(RequestTypeConstants.UNCHECKSHIPJOB).create();
        NetRequestController.getYwblList(unAuditNonShipJob,
            mHandler,
            RequestTypeConstants.UNCHECKSHIPJOB,
            body,
            methodname);
    }
    
    /**
     * 
     * 审核
     * 
     * @Description<功能详细描述>
     * 
     * @param bcnno
     * @param serial
     * @param auditorId
     * @param auditorName
     * @LastModifiedDate：2014-4-15
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void checkShipJob(String bcnno, String serial, String auditorId, String auditorName, String methodname)
    {
        waitDialog = WaitDialog.newInstance();
        waitDialog.show(context.getSupportFragmentManager(), "waitDialog");
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("bcno", bcnno);
            body.put("serial", serial);
            body.put("auditorId", auditorId);
            body.put("auditorName", auditorName);
        }
        catch (org.json.JSONException e)
        {
            e.printStackTrace();
        }
        auditNonShipJob = new HttpNetFactoryCreator(RequestTypeConstants.AUDITNONSHIPJOB).create();
        NetRequestController.getYwblList(auditNonShipJob,
            mHandler,
            RequestTypeConstants.AUDITNONSHIPJOB,
            body,
            methodname);
    }
    
    protected CustomHanler   mHandler = new CustomHanler()
                                      {
                                          
                                          @Override
                                          public void PostHandle(int requestType, Object objHeader, Object objBody,
                                              boolean error, int errorCode)
                                          {
                                              onPostHandle(requestType, objHeader, objBody, error, errorCode);
                                          }
                                      };
    
    /** 返回body **/
    private MetaResponseBody responseBuzBody;
    
    private void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            closeWaitDialog();
            switch (requestType)
            {
            // 审核作业
                case RequestTypeConstants.AUDITNONSHIPJOB:
                    if (null != objBody)
                    {
                        responseBuzBody = (MetaResponseBody)objBody;
                        String ret = responseBuzBody.getRetError();
                        List<HashMap<String, Object>> list = responseBuzBody.getBuzList();
                        List<HashMap<String, Object>> map;
                        if ("0".equals(ret))
                        {
                            // listmap.get(currentPosition).put("auditmarkname", "Y");// auditmarkname
                            listmap.remove(currentPosition);
                            notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(context, "审核失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                // 销审作业
                case RequestTypeConstants.UNCHECKSHIPJOB:
                    if (null != objBody)
                    {
                        responseBuzBody = (MetaResponseBody)objBody;
                        String ret = responseBuzBody.getRetError();
                        List<HashMap<String, Object>> list = responseBuzBody.getBuzList();
                        List<HashMap<String, Object>> map;
                        if ("0".equals(ret))
                        {
                            // listmap.get(currentPosition).put("auditmarkname", "N");// auditmarkname
                            listmap.remove(currentPosition);
                            notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(context, "销审失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
            
        }
        else
        {
            closeWaitDialog();
            if (!(errorCode == ConstState.CANCEL_THREAD))
            {
                Toast.makeText(context, "请求服务器数据失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
