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

import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityMetaData.XwzxAttachBody;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-1-28]
 */
public class AttchInfoViewAdapter extends PMIPBaseAdapter
{
    
    private Context              mContext;
    
    private List<XwzxAttachBody> mLists;
    
    public AttchInfoViewAdapter(Context context, List<XwzxAttachBody> lists)
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
    public List<XwzxAttachBody> getmLists()
    {
        return mLists;
    }
    
    /**
     * 设置 mLists
     * 
     * @param mLists 对mLists进行赋值
     * @author wang_ling
     */
    public void setmLists(List<XwzxAttachBody> mLists)
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
        return mLists.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.attachinfo_item, null);
            
            holder.attchname = (TextView)convertView.findViewById(R.id.attach_item_name);
            convertView.setTag(holder);
        }
        else
        {
            holder = (InfoViewHolder)convertView.getTag();
        }
        
        String title = mLists.get(position).getName();
        
        holder.attchname.setText(title);
        
        return convertView;
    }
    
    public static class InfoViewHolder
    {
        public TextView attchname;
    }
}
