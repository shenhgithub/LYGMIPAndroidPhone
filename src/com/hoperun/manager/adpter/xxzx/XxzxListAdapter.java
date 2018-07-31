/*
 * File name:  XxzxListAdapter.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-5-9
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.xxzx;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.xxzx.XxzxItem;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-5-9]
 */
public class XxzxListAdapter extends PMIPBaseAdapter
{
    private Context        context;
    
    private List<XxzxItem> list;
    
    private LayoutInflater inflater;
    
    public XxzxListAdapter(Context context, List<XxzxItem> list)
    {
        super();
        this.context = context;
        
        this.list = list;
        
        this.inflater = LayoutInflater.from(this.context);
        
    }
    
    @Override
    public int getCount()
    {
        return list == null ? 0 : list.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHold viewHold;
        if (convertView == null)
        {
            viewHold = new ViewHold();
            convertView = inflater.inflate(R.layout.xxzx_list_item, null);
            viewHold.content = (TextView)convertView.findViewById(R.id.content_id);
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
        }
        XxzxItem entity = list.get(position);
        viewHold.content.setText(entity.getStringKeyValue(XxzxItem.title));
        return convertView;
    }
    
    static class ViewHold
    {
        TextView content;
    }
}
