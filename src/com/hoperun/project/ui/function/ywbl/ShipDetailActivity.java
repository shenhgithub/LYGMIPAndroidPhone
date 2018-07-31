/*
 * File name:  ShipDetailActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-4-4
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.ywbl;

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

import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * <一句话功能简述>船舶明细
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-4-4]
 */
public class ShipDetailActivity extends PMIPBaseActivity implements OnClickListener
{
    /** 船舶id **/
    private String         shipId = "";
    
    /** 关闭按钮 **/
    private RelativeLayout close;
    
    /** 获取详细信息 **/
    protected NetTask      mGetDetailRequst;
    
    /** 等待框布局 **/
    private RelativeLayout loadLayout;
    
    /** 等待图片 **/
    private ImageView      loadImageView;
    
    /** 中文船名 **/
    private TextView       ship_chname;
    
    /** 英文船名 **/
    private TextView       ship_egname;
    
    /** 性质 **/
    private TextView       ship_nature;
    
    /** 进口航次 **/
    private TextView       entry_turn;
    
    /** 出口航次 **/
    private TextView       exit_turn;
    
    /** 内外贸 **/
    private TextView       trate_category;
    
    /** 来港 **/
    private TextView       come_port;
    
    /** 去港 **/
    private TextView       go_port;
    
    /** 预计到港时间 **/
    private TextView       estimate_arrive_time;
    
    /** 进口吃水 **/
    private TextView       come_water;
    
    /** 出口吃水 **/
    private TextView       go_water;
    
    /** 速/滞费 **/
    private TextView       stop_fee;
    
    /** 国籍 **/
    private TextView       ship_country;
    
    /** 呼号 **/
    private TextView       ship_huhao;
    
    /** 航速 **/
    private TextView       ship_hangsu;
    
    /** 总吨 **/
    private TextView       ship_zongdun;
    
    /** 净吨 **/
    private TextView       ship_jingdun;
    
    /** 载重吨 **/
    private TextView       ship_zaizhongdun;
    
    /** 长度 **/
    private TextView       ship_changdu;
    
    /** 宽度 **/
    private TextView       ship_kuandu;
    
    /** 公分吨 **/
    private TextView       ship_gongfendun;
    
    /** 船舶类型 **/
    private TextView       ship_leixing;
    
    /** 舱口数 **/
    private TextView       ship_cangkoushu;
    
    /** 最大吃水 **/
    private TextView       ship_zuidachishui;
    
    /** 船公司 **/
    private TextView       ship_company;
    
    /** 建造日期 **/
    private TextView       ship_madedate;
    
    /** 模块 **/
    private int            blockNum;
    
    /** 方法名 **/
    private String         method = "";
    
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
        close = (RelativeLayout)findViewById(R.id.close_btn);
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        ship_chname = (TextView)findViewById(R.id.ship_chname);
        ship_egname = (TextView)findViewById(R.id.ship_egname);
        ship_nature = (TextView)findViewById(R.id.ship_nature);
        entry_turn = (TextView)findViewById(R.id.entry_turn);
        exit_turn = (TextView)findViewById(R.id.exit_turn);
        trate_category = (TextView)findViewById(R.id.trade_category);
        come_port = (TextView)findViewById(R.id.come_port);
        go_port = (TextView)findViewById(R.id.go_port);
        estimate_arrive_time = (TextView)findViewById(R.id.estimate_arrive_time);
        come_water = (TextView)findViewById(R.id.come_water);
        go_water = (TextView)findViewById(R.id.go_water);
        stop_fee = (TextView)findViewById(R.id.stop_fee);
        ship_country = (TextView)findViewById(R.id.ship_country);
        ship_huhao = (TextView)findViewById(R.id.ship_huhao);
        ship_hangsu = (TextView)findViewById(R.id.ship_hangsu);
        ship_zongdun = (TextView)findViewById(R.id.ship_zongdun);
        ship_jingdun = (TextView)findViewById(R.id.ship_jingdun);
        ship_zaizhongdun = (TextView)findViewById(R.id.ship_zaizhongdun);
        ship_changdu = (TextView)findViewById(R.id.ship_changdu);
        ship_kuandu = (TextView)findViewById(R.id.ship_kuandu);
        ship_gongfendun = (TextView)findViewById(R.id.ship_gongfendun);
        ship_leixing = (TextView)findViewById(R.id.ship_leixing);
        ship_cangkoushu = (TextView)findViewById(R.id.ship_cangkoushu);
        ship_zuidachishui = (TextView)findViewById(R.id.ship_zuidachishui);
        ship_company = (TextView)findViewById(R.id.ship_company);
        ship_madedate = (TextView)findViewById(R.id.ship_madedate);
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
        close.setOnClickListener(this);
    }
    
    /**
     * 
     * <一句话功能简述>发送明细请求
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-11
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void sendDedailRequest(String method)
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
                method);
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
     * @param savedInstanceState
     * @author chen_wei3
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ship_detail_layout);
        Intent data = getIntent();
        shipId = data.getStringExtra("shipid");
        blockNum = data.getIntExtra("blocknum", 0);
        setMethod(blockNum);
        iniView();
        setClick();
        sendDedailRequest(method);
    }
    
    private void setMethod(int num)
    {
        switch (num)
        {
            case 2:
                method = "getTodoBerthingDetail";
                break;
            case 3:
                method = "getTodoJobDetail";
                break;
            case 4:
                method = "getTodoShiftingDetail";
                break;
            case 5:
                method = "getTodoLeaveDetail";
                break;
            default:
                break;
        }
    }
    
    /**
     * 重载方法
     * 
     * @param arg0
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
                    MetaResponseBody responseBuzBody = (MetaResponseBody)objBody;
                    List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                    // 成功
                    if ("0".equals(responseBuzBody.getRetError()) && (ret != null) && (ret.size() > 0))
                    {
                        ship_chname.setText((CharSequence)ret.get(0).get("shipnamecn"));
                        ship_egname.setText((CharSequence)ret.get(0).get("shipnameen"));
                        ship_nature.setText((CharSequence)ret.get(0).get("linetype"));
                        entry_turn.setText((CharSequence)ret.get(0).get("invoyage"));
                        exit_turn.setText((CharSequence)ret.get(0).get("outvoyage"));
                        trate_category.setText((CharSequence)ret.get(0).get("tradename"));
                        come_port.setText((CharSequence)ret.get(0).get("fromport"));
                        go_port.setText((CharSequence)ret.get(0).get("toport"));
                        estimate_arrive_time.setText((CharSequence)ret.get(0).get("etaarrivetime"));
                        come_water.setText((CharSequence)ret.get(0).get("indraft"));
                        go_water.setText((CharSequence)ret.get(0).get("outdraft"));
                        stop_fee.setText((CharSequence)ret.get(0).get("fastfee"));
                        ship_country.setText((CharSequence)ret.get(0).get("nation"));
                        ship_huhao.setText((CharSequence)ret.get(0).get("callno"));
                        ship_hangsu.setText((CharSequence)ret.get(0).get("speed"));
                        ship_zongdun.setText((CharSequence)ret.get(0).get("totalton"));
                        ship_jingdun.setText((CharSequence)ret.get(0).get("actualton"));
                        ship_zaizhongdun.setText((CharSequence)ret.get(0).get("loadton"));
                        ship_changdu.setText((CharSequence)ret.get(0).get("shiplength"));
                        ship_kuandu.setText((CharSequence)ret.get(0).get("shipwidth"));
                        ship_gongfendun.setText((CharSequence)ret.get(0).get("metreton"));
                        ship_leixing.setText((CharSequence)ret.get(0).get("shiptype"));
                        ship_cangkoushu.setText((CharSequence)ret.get(0).get("hatchcount"));
                        ship_zuidachishui.setText((CharSequence)ret.get(0).get("maxdeep"));
                        ship_company.setText((CharSequence)ret.get(0).get("shipcompany"));
                        ship_madedate.setText((CharSequence)ret.get(0).get("builddate"));
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
            if (!(errorCode == ConstState.CANCEL_THREAD))
            {
                Toast.makeText(this, "请求服务器数据失败！", Toast.LENGTH_SHORT).show();
            }
        }
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
