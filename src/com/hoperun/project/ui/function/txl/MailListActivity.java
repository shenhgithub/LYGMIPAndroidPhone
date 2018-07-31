/*
 * File name:  MailListActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  Administrator
 * Last modified date:  2014-5-13
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.txl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author Administrator
 * @Version [版本号, 2014-5-13]
 */
public class MailListActivity extends PMIPBaseActivity
{
    private TextView lv_name, lv_departmentname, lv_duty, lv_tel, lv_baktel, lv_officetel, lv_bakofficetel,
        lv_peremail, lv_markweibo, lv_companyname, lv_mail;
    
    private ImageView iv_show, iv_btn_close;
    
    /**
     * 重载方法
     * 
     * @param savedInstanceState
     * @author Administrator
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_morelist);
        this.init();
        this.Obtain();
    }
    
    /**
     * 初始化 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-13
     * @author Administrator
     * @EditHistory：<修改内容><修改人>
     */
    public void init()
    {
        lv_companyname = (TextView)findViewById(R.id.add_Text);
        lv_name = (TextView)findViewById(R.id.nametext);
        lv_departmentname = (TextView)findViewById(R.id.departmentname_text);
        lv_duty = (TextView)findViewById(R.id.duty_text);
        lv_tel = (TextView)findViewById(R.id.tel_Text);
        lv_baktel = (TextView)findViewById(R.id.baktel_text);
        lv_officetel = (TextView)findViewById(R.id.officetel_text);
        lv_bakofficetel = (TextView)findViewById(R.id.bakofficetel_text);
        lv_peremail = (TextView)findViewById(R.id.peremail_text);
        lv_mail = (TextView)findViewById(R.id.email_Text);
        lv_markweibo = (TextView)findViewById(R.id.markweibo_text);
        iv_show = (ImageView)findViewById(R.id.userset);
        iv_btn_close = (ImageView)findViewById(R.id.btn_close);
        iv_show.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        iv_btn_close.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
    
    /**
     * 获取 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-13
     * @author Administrator
     * @EditHistory：<修改内容><修改人>
     */
    public void Obtain()
    {
        Intent data = getIntent();
        
        String name = data.getStringExtra("name");
        String departmentname = data.getStringExtra("departmentname");
        String duty = data.getStringExtra("duty");
        String tel = data.getStringExtra("tel");
        String baktel = data.getStringExtra("baktel");
        String officetel = data.getStringExtra("officetel");
        String bakofficetel = data.getStringExtra("bakofficetel");
        String peremail = data.getStringExtra("peremail");
        String email = data.getStringExtra("email");
        boolean isE = data.getStringExtra("markweibo").isEmpty();
        // int accp = Integer.valueOf(data.getStringExtra("markweibo"));
        String companyname = data.getStringExtra("companyname");
        String markweibo = data.getStringExtra("markweibo");
        String markweibos = "";
        if (markweibo.equals("False"))
        {
            markweibos = "未开通";
        }
        else
        {
            markweibos = "已开通";
        }
        lv_name.setText(name.trim());
        lv_departmentname.setText(departmentname.trim());
        lv_duty.setText(duty.trim());
        lv_tel.setText(tel.trim());
        lv_baktel.setText(baktel.trim());
        lv_officetel.setText(officetel.trim());
        lv_bakofficetel.setText(bakofficetel.trim());
        lv_peremail.setText(peremail.trim());
        lv_markweibo.setText(markweibos.trim());
        lv_companyname.setText(companyname.trim());
        lv_mail.setText(email.trim());
    }
    
    /**
     * 重载方法
     * 
     * @param requestType
     * @param objHeader
     * @param objBody
     * @param error
     * @param errorCode
     * @author Administrator
     */
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        // TODO Auto-generated method stub
        
    }
}
