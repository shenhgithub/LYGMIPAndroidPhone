/*
 * File name:  DocInfoViewAdapter.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-1-28
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.Infor;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoperun.mipmanager.model.entityMetaData.GetDocInfoResponseBody;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-1-28]
 */
public class DocInfoViewAdapter extends PMIPBaseAdapter
{
    
    private Context                      mContext;
    
    private List<GetDocInfoResponseBody> mLists;
    
    public DocInfoViewAdapter(Context context, List<GetDocInfoResponseBody> lists)
    {
        mContext = context;
        this.mLists = lists;
    }
    
    /**
     * 获取 mLists
     * 
     * @return 返回 mLists
     * @author wang_ling
     */
    public List<GetDocInfoResponseBody> getmLists()
    {
        return mLists;
    }
    
    /**
     * 设置 mLists
     * 
     * @param mLists 对mLists进行赋值
     * @author wang_ling
     */
    public void setmLists(List<GetDocInfoResponseBody> mLists)
    {
        this.mLists = mLists;
    }
    
    @Override
    public int getCount()
    {
        return mLists == null ? 0 : mLists.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return null;
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        
        InfoViewHolder holder = null;
        if (convertView == null)
        {
            holder = new InfoViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.pdf_info_list_item, null);
            
            holder.signStep = (TextView)convertView.findViewById(R.id.pdf_info_list_item_step);
            holder.person = (TextView)convertView.findViewById(R.id.pdf_info_list_item_person);
            holder.message = (TextView)convertView.findViewById(R.id.pdf_info_list_item_message);
            holder.getTime = (TextView)convertView.findViewById(R.id.pdf_info_list_item_gettime);
            holder.finishTime = (TextView)convertView.findViewById(R.id.pdf_info_list_item_finishtime);
            
            convertView.setTag(holder);
        }
        else
        {
            holder = (InfoViewHolder)convertView.getTag();
        }
        
        String step = mLists.get(position).getStringKeyValue(GetDocInfoResponseBody.stepname);
        String userName = mLists.get(position).getStringKeyValue(GetDocInfoResponseBody.username);
        String receivTime = mLists.get(position).getStringKeyValue(GetDocInfoResponseBody.receivedate);
        String dealTime = mLists.get(position).getStringKeyValue(GetDocInfoResponseBody.operatdate);
        String option = mLists.get(position).getStringKeyValue(GetDocInfoResponseBody.option);
        
        if (receivTime.equals(""))
        {
            receivTime = "无";
        }
        
        if (dealTime.equals(""))
        {
            dealTime = "无";
        }
        
        if (option.equals(""))
        {
            option = "无";
        }
        
        holder.signStep.setText(step);
        holder.person.setText(userName);
        holder.getTime.setText(receivTime);
        holder.finishTime.setText(dealTime);
        holder.message.setText(option);
        
        return convertView;
    }
    
    public static class InfoViewHolder
    {
        public TextView signStep, person, message, getTime, finishTime;
    }
}
