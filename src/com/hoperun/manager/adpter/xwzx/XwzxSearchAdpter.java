/*
 * File name:  XwzxSearchAdpter.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-3-20
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.xwzx;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityMetaData.NewsTypeInfo;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * 筛选适配器
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-20]
 */
public class XwzxSearchAdpter extends PMIPBaseAdapter
{
    private List<NewsTypeInfo> mfunnels = new ArrayList<NewsTypeInfo>();
    
    private Context            mContext;
    
    private int                mSelect  = -1;
    
    private int                mHeight  = -1;
    
    public XwzxSearchAdpter(Context context, List<NewsTypeInfo> list, int height)
    {
        super();
        // TODO Auto-generated constructor stub
        mContext = context;
        mfunnels = list;
        mHeight = height;
    }
    
    public int getmSelect()
    {
        return mSelect;
    }
    
    public void setmSelect(int mSelect)
    {
        this.mSelect = mSelect;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mfunnels.size();
    }
    
    @Override
    public NewsTypeInfo getItem(int position)
    {
        // TODO Auto-generated method stub
        return mfunnels.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHoldSearch viewSearchHold;
        if (convertView == null)
        {
            
            viewSearchHold = new ViewHoldSearch();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.text_layout, null);
            convertView.setBackgroundResource(R.drawable.selector_funnel);
            
            viewSearchHold.mName = (TextView)convertView.findViewById(R.id.funnel_tv);
            viewSearchHold.mName.setHeight(mHeight);
            convertView.setTag(viewSearchHold);
        }
        else
        {
            viewSearchHold = (ViewHoldSearch)convertView.getTag();
            
        }
        
        viewSearchHold.mName.setText(mfunnels.get(position).getName());
        
        return convertView;
    }
    
    static class ViewHoldSearch
    {
        
        TextView mName;
        
    }
}
