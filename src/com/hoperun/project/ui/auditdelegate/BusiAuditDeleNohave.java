/*
 * File name:  BusiAuditDeleNohave.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  li_miao
 * Last modified date:  2014-4-4
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.auditdelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.adpter.auditdelegate.BusiHaveShipAdapter;
import com.hoperun.manager.adpter.auditdelegate.BusiNoShipAdapter;
import com.hoperun.manager.components.CustomBaseListView;
import com.hoperun.manager.components.CustomBaseListView.mCustomBaseListViewInterface;
import com.hoperun.manager.components.WaitDialog;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.StringUtils;
import com.hoperun.mip.utils.SystemTools;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.DateTimePickerDialog;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * 无船作业
 * 
 * @Description<功能详细描述>
 * 
 * @author li_miao
 * @Version [版本号, 2014-4-4]
 */
public class BusiAuditDeleNohave extends PMIPBaseActivity implements OnClickListener
{
    
    private int                           pageSize          = 10;
    
    /** 有船作頁作业列表标题 **/
    private LinearLayout                  haveShipTitleLL;
    
    /** 无船作頁作业列表标题 **/
    private LinearLayout                  noShipTitleLL;
    
    /** 作业公司下拉框 **/
    private Spinner                       company_spinner;
    
    /** 审核下拉框：未审核/审核 **/
    private Spinner                       goods_spinner;
    
    /** 作业公司 **/
    private ArrayAdapter<String>          company_adapter;
    
    /** 审核类型 **/
    private ArrayAdapter<String>          goods_adapter;
    
    /** 作业公司 **/
    private static String[]               companyArray      = {"所有公司"};
    
    /** 审核类型 **/
    private static String[]               goodsArray        = {"未审核", "审核"};
    
    /** 确定按钮 **/
    private Button                        btn_confirm;
    
    /**
     * 获取无船作业申请列表
     */
    private NetTask                       getNoShipJobList;
    
    /**
     * 获取部门列表
     */
    private NetTask                       getDepartList;
    
    /**
     * 获取有船作业申请列表
     */
    private NetTask                       getShipJobList;
    
    /** 等待对话框 **/
    private WaitDialog                    waitDialog;
    
    /** 返回body **/
    private MetaResponseBody              responseBuzBody;
    
    /****/
    private RelativeLayout                btn_ship_have;
    
    /**
     * 无船布局
     */
    private RelativeLayout                btn_nohave;
    
    /** 开始时间 **/
    private TextView                      begin_time;
    
    /** 结束时间 **/
    private TextView                      end_time;
    
    /** 无船列表 **/
    private CustomBaseListView            noShipListView;
    
    /** 有船列表 **/
    private CustomBaseListView            haveShipListView;
    
    /** 无船列表适配器 **/
    private BusiNoShipAdapter             noShipAdapter;
    
    /*** 有船列表适配器 **/
    private BusiHaveShipAdapter           haveShipAdapter;
    
    /** 返回按钮 **/
    private ImageView                     image_back;
    
    /**
     * 无船按钮显示文字
     */
    private TextView                      btn_nohave_text;
    
    /**
     * 有船按钮显示文字
     */
    private TextView                      btn_shiphave_text;
    
    /****/
    private TextView                      title_promote;
    
    /** 标记 0-无船，1-有船 **/
    private String                        flag              = "1";
    
    /****/
    private LinearLayout                  ship_linearLayput;
    
    /****/
    private RelativeLayout                ship_line;
    
    /****/
    private List<HashMap<String, Object>> company_list;
    
    /** 船名输入框 **/
    private EditText                      ship_name;
    
    /** 航次输入框 **/
    private EditText                      ship_voyage;
    
    /**
     * 有船作业最后一条数据时间
     */
    private String                        mHaveShipLastTime = "";
    
    /**
     * 无船作业最后一条数据时间
     */
    private String                        mNoShipLastTime   = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.business_auditdele_nohave);
        initView();
        initListener();
        initData();
        getCompanyList();
    }
    
    private void initView()
    {
        haveShipTitleLL = (LinearLayout)findViewById(R.id.haveShip_title_ll);
        noShipTitleLL = (LinearLayout)findViewById(R.id.noship_title_ll);
        company_spinner = (Spinner)findViewById(R.id.company_pinner);
        goods_spinner = (Spinner)findViewById(R.id.currency_pinner);
        btn_confirm = (Button)findViewById(R.id.business_confirm);
        btn_nohave = (RelativeLayout)findViewById(R.id.btn_nohave);
        btn_nohave_text = (TextView)findViewById(R.id.btn_nohave_text);
        btn_ship_have = (RelativeLayout)findViewById(R.id.btn_shiphave);
        btn_shiphave_text = (TextView)findViewById(R.id.btn_shiphave_text);
        begin_time = (TextView)findViewById(R.id.beginTime);
        end_time = (TextView)findViewById(R.id.endTime);
        noShipListView = (CustomBaseListView)findViewById(R.id.business_nohave_list);
        haveShipListView = (CustomBaseListView)findViewById(R.id.business_shiphave_list);
        image_back = (ImageView)findViewById(R.id.fragment_back);
        title_promote = (TextView)findViewById(R.id.title_prompte);
        ship_linearLayput = (LinearLayout)findViewById(R.id.ship_linearlayout);
        ship_line = (RelativeLayout)findViewById(R.id.line_ship);
        ship_name = (EditText)findViewById(R.id.have_ship_name);
        ship_voyage = (EditText)findViewById(R.id.ship_voyage);
    }
    
    /**
     * 
     * 初始化监听
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-14
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initListener()
    {
        btn_confirm.setOnClickListener(this);
        btn_nohave.setOnClickListener(this);
        btn_ship_have.setOnClickListener(this);
        image_back.setOnClickListener(this);
        begin_time.setOnClickListener(new DateTimeOnClick());
        end_time.setOnClickListener(new DateTimeOnClick());
        
        haveShipListView.setmListener(new mCustomBaseListViewInterface()
        {
            
            @Override
            public void onUpRefreshList()
            {
                System.out.println("-----------current--" + "上拉");
                haveShipListView.setIsRefreshListState(CustomBaseListView.UPREFRESH);
                getShipJobList(mHaveShipLastTime);
            }
            
            @Override
            public void onDownRefreshList()
            {
                System.out.println("-----------current--" + "下拉");
                haveShipListView.setIsRefreshListState(CustomBaseListView.DOWNREFRESH);
                getShipJobList("");
            }
        });
        
        noShipListView.setmListener(new mCustomBaseListViewInterface()
        {
            
            @Override
            public void onUpRefreshList()
            {
                noShipListView.setIsRefreshListState(CustomBaseListView.UPREFRESH);
                getNonShipJobList(mNoShipLastTime);
            }
            
            @Override
            public void onDownRefreshList()
            {
                noShipListView.setIsRefreshListState(CustomBaseListView.DOWNREFRESH);
                getNonShipJobList("");
            }
        });
    }
    
    private final class DateTimeOnClick implements OnClickListener
    {
        
        public void onClick(View v)
        {
            
            switch (v.getId())
            {
                case R.id.beginTime:
                    DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
                    
                    BusiAuditDeleNohave.this);
                    
                    dateTimePicKDialog.dateTimePicKDialog(begin_time, 1, 1);
                    break;
                case R.id.endTime:
                    DateTimePickerDialog endDateTimePicKDialog = new DateTimePickerDialog(
                    
                    BusiAuditDeleNohave.this);
                    
                    endDateTimePicKDialog.dateTimePicKDialog(end_time, 1, 1);
                    break;
                
                default:
                    break;
            }
            
        }
        
    }
    
    public void initData()
    {
        goods_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, goodsArray);
        goods_spinner.setAdapter(goods_adapter);
        
        company_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, companyArray);
        company_spinner.setAdapter(company_adapter);
        company_list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> allCompany = new HashMap<String, Object>();
        allCompany.put("code_department", "");
        allCompany.put("department", "所有公司");
        company_list.add(allCompany);
        
        begin_time.setText(StringUtils.getCurrentDate());
        end_time.setText(StringUtils.getCurrentDate());
        haveShipAdapter = new BusiHaveShipAdapter(this, haveShipListData, "1");
        haveShipListView.setAdapter(haveShipAdapter);
        noShipAdapter = new BusiNoShipAdapter(this, noShipListData, "0");
        noShipListView.setAdapter(noShipAdapter);
    }
    
    /**
     * 
     * 获取作业公司名称
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-15
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void getCompanyList()
    {
        JSONObject body = new JSONObject();
        getDepartList = new HttpNetFactoryCreator(RequestTypeConstants.GETDEPTLISTT).create();
        NetRequestController.getYwblList(getDepartList,
            mHandler,
            RequestTypeConstants.GETDEPTLISTT,
            body,
            "getdeptList");
        waitDialog = WaitDialog.newInstance();
        waitDialog.show(getSupportFragmentManager(), "waitDialog");
    }
    
    /**
     * 
     * 获取无船作业列表
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-15
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void getNonShipJobList(String time)
    {
        if (SystemTools.isDateAfter(begin_time.getText().toString(), end_time.getText().toString()))
        {
            closeWaitDialog();
            Toast.makeText(this, "结束日期在开始日期之前，请重新输入日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNull(time))
        {
            time = StringUtils.getCurrentDate();
        }
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("querydate", time);
            body.put("size", pageSize + "");
            body.put("beginDate", begin_time.getText());
            body.put("endDate", end_time.getText());
            body.put("jobCompanyId", company_list.get(company_spinner.getSelectedItemPosition()).get("code_department"));
            String auditMark = (String)goods_spinner.getSelectedItem();
            if ("未审核".equals(auditMark))
            {
                body.put("auditMark", "0");
            }
            else
            {
                body.put("auditMark", "1");
            }
            
        }
        
        catch (org.json.JSONException e)
        {
            e.printStackTrace();
        }
        getNoShipJobList = new HttpNetFactoryCreator(RequestTypeConstants.GETNOSHIPJOBLIST).create();
        NetRequestController.getYwblList(getNoShipJobList,
            mHandler,
            RequestTypeConstants.GETNOSHIPJOBLIST,
            body,
            "getNonShipJobList");
    }
    
    /**
     * 
     * 获取有船作业列表
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-15
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void getShipJobList(String time)
    {
        if (SystemTools.isDateAfter(begin_time.getText().toString(), end_time.getText().toString()))
        {
            closeWaitDialog();
            Toast.makeText(this, "结束日期在开始日期之前，请重新输入日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNull(time))
        {
            time = StringUtils.getCurrentDate();
        }
        JSONObject body = new JSONObject();
        try
        {
            
            body.put("token", GlobalState.getInstance().getToken());
            
            body.put("querydate", time);
            body.put("size", pageSize + "");
            body.put("beginDate", begin_time.getText());
            body.put("endDate", end_time.getText());
            body.put("shipName", ship_name.getText());
            body.put("voyage", ship_voyage.getText());
            // company_list.get(company_spinner.getSelectedItemPosition()).get("code_department")
            body.put("jobCompanyId", company_list.get(company_spinner.getSelectedItemPosition()).get("code_department"));
            String auditMark = (String)goods_spinner.getSelectedItem();
            if ("未审核".equals(auditMark))
            {
                body.put("auditMark", "0");
            }
            else
            {
                body.put("auditMark", "1");
            }
        }
        
        catch (org.json.JSONException e)
        {
            e.printStackTrace();
        }
        getShipJobList = new HttpNetFactoryCreator(RequestTypeConstants.GETSHIPJOBLIST).create();
        NetRequestController.getYwblList(getShipJobList,
            mHandler,
            RequestTypeConstants.GETSHIPJOBLIST,
            body,
            "getShipJobList");
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @author li_miao
     */
    @SuppressLint("NewApi")
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.business_confirm:
                // 无船
                if ("0".equals(flag))
                {
                    noShipListView.setIsRefreshListState(CustomBaseListView.REFRESH);
                    waitDialog = WaitDialog.newInstance();
                    waitDialog.show(getSupportFragmentManager(), "waitDialog");
                    getNonShipJobList("");
                }
                // 有船
                else if ("1".equals(flag))
                {
                    System.out.println("-----------current--" + "刷新");
                    haveShipListView.setIsRefreshListState(CustomBaseListView.REFRESH);
                    waitDialog = WaitDialog.newInstance();
                    waitDialog.show(getSupportFragmentManager(), "waitDialog");
                    getShipJobList("");
                }
                break;
            case R.id.fragment_back:
                this.finish();
                break;
            // 无船
            case R.id.btn_nohave:
                btn_ship_have.setBackground(null);
                btn_nohave.setBackgroundResource(R.drawable.title_qh_2);
                btn_nohave_text.setTextColor(getResources().getColor(R.color.white));
                btn_shiphave_text.setTextColor(getResources().getColor(R.color.gray));
                
                title_promote.setText("无船作业申请");
                noShipListView.setVisibility(View.VISIBLE);
                haveShipListView.setVisibility(View.GONE);
                ship_linearLayput.setVisibility(View.GONE);
                ship_line.setVisibility(View.GONE);
                haveShipTitleLL.setVisibility(View.GONE);
                noShipTitleLL.setVisibility(View.VISIBLE);
                flag = "0";
                break;
            // 有船
            case R.id.btn_shiphave:
                btn_ship_have.setBackgroundResource(R.drawable.title_qh_2);
                btn_nohave.setBackground(null);
                btn_nohave_text.setTextColor(getResources().getColor(R.color.gray));
                btn_shiphave_text.setTextColor(getResources().getColor(R.color.white));
                title_promote.setText("有船作业申请");
                noShipListView.setVisibility(View.GONE);
                haveShipListView.setVisibility(View.VISIBLE);
                ship_linearLayput.setVisibility(View.VISIBLE);
                ship_line.setVisibility(View.VISIBLE);
                haveShipTitleLL.setVisibility(View.VISIBLE);
                noShipTitleLL.setVisibility(View.GONE);
                flag = "1";
                break;
            default:
                break;
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
        if (null != waitDialog)
        {
            waitDialog.dismiss();
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
     * @author li_miao
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
            // 获取作业公司
                case RequestTypeConstants.GETDEPTLISTT:
                    if (null != objBody)
                    {
                        
                        closeWaitDialog();
                        responseBuzBody = (MetaResponseBody)objBody;
                        String ret = responseBuzBody.getRetError();
                        List<HashMap<String, Object>> list = responseBuzBody.getBuzList();
                        List<HashMap<String, Object>> map;
                        company_list = new ArrayList<HashMap<String, Object>>();
                        if ("0".equals(ret) && !"".equals(list.get(0).get("deptlist")))
                        {
                            map = (ArrayList<HashMap<String, Object>>)list.get(0).get("deptlist");
                            companyArray = new String[map.size() + 1];
                            companyArray[0] = "所有公司";
                            HashMap<String, Object> allCompany = new HashMap<String, Object>();
                            allCompany.put("code_department", "");
                            allCompany.put("department", "所有公司");
                            company_list.add(allCompany);
                            for (int i = 0; i < map.size(); i++)
                            {
                                companyArray[i + 1] = (String)map.get(i).get("department");
                                company_list.add(i + 1, map.get(i));
                            }
                            company_adapter =
                                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, companyArray);
                            company_spinner.setAdapter(company_adapter);
                            
                        }
                        else
                        {
                            Toast.makeText(this, "获取部门列表失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        closeWaitDialog();
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                // 获取无船作业申请列表
                case RequestTypeConstants.GETNOSHIPJOBLIST:
                    if (null != objBody)
                    {
                        closeWaitDialog();
                        responseBuzBody = (MetaResponseBody)objBody;
                        parseNoShipList(responseBuzBody);
                    }
                    else
                    {
                        closeWaitDialog();
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                        dealNetErrorNo();
                    }
                    break;
                // 获取有船作业申请列表
                case RequestTypeConstants.GETSHIPJOBLIST:
                    if (null != objBody)
                    {
                        closeWaitDialog();
                        responseBuzBody = (MetaResponseBody)objBody;
                        parseHaveShipList(responseBuzBody);
                    }
                    else
                    {
                        closeWaitDialog();
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                        dealNetErrorHave();
                    }
                    break;
                default:
                    break;
            }
            
        }
        else
        {
            closeWaitDialog();
            
            switch (requestType)
            {
            // 获取作业公司
                case RequestTypeConstants.GETDEPTLISTT:
                    if (!(errorCode == ConstState.CANCEL_THREAD))
                    {
                        Toast.makeText(this, "请求服务器数据失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                // 获取有船作业列表
                case RequestTypeConstants.GETSHIPJOBLIST:
                    Toast.makeText(this, "获取数据失败！", Toast.LENGTH_LONG).show();
                    dealNetErrorHave();
                    break;
                case RequestTypeConstants.GETNOSHIPJOBLIST:
                    Toast.makeText(this, "获取数据失败！", Toast.LENGTH_LONG).show();
                    dealNetErrorNo();
                    
                    break;
            
            }
        }
        
    }
    
    /** 有船作业列表 **/
    private List<HashMap<String, Object>> haveShipListData = new ArrayList<HashMap<String, Object>>();
    
    /** 无船作业列表 **/
    private List<HashMap<String, Object>> noShipListData   = new ArrayList<HashMap<String, Object>>();
    
    /**
     * 
     * 处理有船作业返回失败
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-17
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void dealNetErrorHave()
    {
        if (haveShipListView.getIsRefreshListState() == CustomBaseListView.UPREFRESH)
        {
            haveShipListView.setFootviewVisiable(false);
            
        }
        else
        {
            if (haveShipListView.isDone())
            {
                haveShipListView.onRefreshComplete();
            }
        }
        haveShipListView.setIsRefreshListState(CustomBaseListView.IDLE);
    }
    
    /**
     * 
     * 处理无船作业列表失败
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-17
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void dealNetErrorNo()
    {
        if (noShipListView.getIsRefreshListState() == CustomBaseListView.UPREFRESH)
        {
            noShipListView.setFootviewVisiable(false);
            
        }
        else
        {
            if (noShipListView.isDone())
            {
                noShipListView.onRefreshComplete();
            }
        }
        noShipListView.setIsRefreshListState(CustomBaseListView.IDLE);
    }
    
    @SuppressWarnings("unchecked")
    private void parseHaveShipList(MetaResponseBody responseBuzBody)
    {
        String ret = responseBuzBody.getRetError();
        List<HashMap<String, Object>> list = responseBuzBody.getBuzList();
        List<HashMap<String, Object>> map = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> newMap = new ArrayList<HashMap<String, Object>>();
        int mListCount = 0;
        if ("0".equals(ret))
        {
            if (!"".equals(list.get(0).get("jobinfos")))
            {
                
                newMap = (ArrayList<HashMap<String, Object>>)list.get(0).get("jobinfos");
                // if (!"".equals(map.get(0).get("jobinfo")))
                // {
                // newMap = (ArrayList<HashMap<String, Object>>)map.get(0).get("jobinfo");
                
                if (newMap != null)
                {
                    mListCount = newMap.size();
                    if (mListCount > 0)
                    {
                        mHaveShipLastTime = (String)newMap.get(mListCount - 1).get("applydate");
                    }
                }
                // }
            }
            System.out.println("-----------current--" + haveShipListView.getIsRefreshListState() + "||"
                + CustomBaseListView.UPREFRESH);
            if (haveShipListView.getIsRefreshListState() == CustomBaseListView.UPREFRESH)
            {
                if (mListCount < pageSize)
                {
                    haveShipListView.removeFootview();
                }
                haveShipListData = haveShipAdapter.getListmap();
                for (int i = 0; i < newMap.size(); i++)
                {
                    haveShipListData.add(newMap.get(i));
                }
            }
            else
            {
                haveShipListView.onRefreshComplete();
                if (mListCount >= pageSize)
                {
                    haveShipListView.addFootview();
                }
                else
                {
                    haveShipListView.removeFootview();
                }
                haveShipListData.clear();
                haveShipListData = newMap;
            }
            
            if (haveShipListData != null && haveShipAdapter != null && haveShipListData.size() > 0)
            {
                haveShipAdapter.setListmap(haveShipListData);
                haveShipAdapter.notifyDataSetChanged();
                haveShipListView.setmAllListCount(haveShipListData.size());
            }
            else
            {
                haveShipListView.setmAllListCount(0);
                Toast.makeText(this, "没有数据！", Toast.LENGTH_LONG).show();
            }
            haveShipListView.setIsRefreshListState(CustomBaseListView.IDLE);
        }
        else
        {
            Toast.makeText(this, "获取有船列表失败！", Toast.LENGTH_SHORT).show();
            if (haveShipListView.getIsRefreshListState() == CustomBaseListView.UPREFRESH)
            {
                haveShipListView.setFootviewVisiable(false);
                
            }
            else
            {
                if (haveShipListView.isDone())
                {
                    haveShipListView.onRefreshComplete();
                }
            }
            haveShipListView.setIsRefreshListState(CustomBaseListView.IDLE);
        }
    }
    
    /**
     * 
     * 解析无船作业列表
     * 
     * @Description<功能详细描述>
     * 
     * @param objBody
     * @LastModifiedDate：2014-4-17
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @SuppressWarnings("unchecked")
    private void parseNoShipList(MetaResponseBody responseBuzBody)
    {
        String ret = responseBuzBody.getRetError();
        List<HashMap<String, Object>> list = responseBuzBody.getBuzList();
        List<HashMap<String, Object>> map = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> newMap = new ArrayList<HashMap<String, Object>>();
        int mListCount = 0;
        if ("0".equals(ret))
        {
            if (!"".equals(list.get(0).get("jobinfos")))
            {
                
                newMap = (ArrayList<HashMap<String, Object>>)list.get(0).get("jobinfos");
                // if (!"".equals(map.get(0).get("jobinfo")))
                // {
                // newMap = (ArrayList<HashMap<String, Object>>)map.get(0).get("jobinfo");
                
                if (newMap != null)
                {
                    mListCount = newMap.size();
                    if (mListCount > 0)
                    {
                        mNoShipLastTime = (String)newMap.get(mListCount - 1).get("applydate");
                    }
                }
                // }
            }
            
            if (noShipListView.getIsRefreshListState() == CustomBaseListView.UPREFRESH)
            {
                if (mListCount < pageSize)
                {
                    noShipListView.removeFootview();
                }
                noShipListData = noShipAdapter.getListmap();
                for (int i = 0; i < newMap.size(); i++)
                {
                    noShipListData.add(newMap.get(i));
                }
            }
            else
            {
                noShipListView.onRefreshComplete();
                if (mListCount >= pageSize)
                {
                    noShipListView.addFootview();
                }
                else
                {
                    noShipListView.removeFootview();
                }
                noShipListData.clear();
                noShipListData = newMap;
            }
            
            if (noShipListData != null && noShipAdapter != null && noShipListData.size() > 0)
            {
                noShipAdapter.setListmap(noShipListData);
                noShipAdapter.notifyDataSetChanged();
                noShipListView.setmAllListCount(noShipListData.size());
            }
            else
            {
                noShipListView.setmAllListCount(0);
                Toast.makeText(this, "没有数据！", Toast.LENGTH_LONG).show();
            }
            noShipListView.setIsRefreshListState(CustomBaseListView.IDLE);
        }
        else
        {
            Toast.makeText(this, "获取有船列表失败！", Toast.LENGTH_SHORT).show();
            if (noShipListView.getIsRefreshListState() == CustomBaseListView.UPREFRESH)
            {
                noShipListView.setFootviewVisiable(false);
                
            }
            else
            {
                if (noShipListView.isDone())
                {
                    noShipListView.onRefreshComplete();
                }
            }
            noShipListView.setIsRefreshListState(CustomBaseListView.IDLE);
        }
    }
}
