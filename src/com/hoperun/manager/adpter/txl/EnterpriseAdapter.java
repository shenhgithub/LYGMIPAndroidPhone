/*
 * File name:  EnterpriseAdapter.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-5-13
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.txl;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.txl.EnterpriseEntity;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * <一句话功能简述>企业列表adapter
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-5-13]
 */
public class EnterpriseAdapter extends PMIPBaseAdapter
{
    private Context                context;
    
    private List<EnterpriseEntity> list;
    
    private LayoutInflater         inflater;
    
    public List<EnterpriseEntity> getList()
    {
        return list;
    }
    
    public void setList(List<EnterpriseEntity> list)
    {
        this.list = list;
    }
    
    public EnterpriseAdapter(Context context, List<EnterpriseEntity> list)
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
            convertView = inflater.inflate(R.layout.enterprise_item, null);
            viewHold.enterpriseName = (TextView)convertView.findViewById(R.id.enterprise_name);
            viewHold.selected_pic = (ImageView)convertView.findViewById(R.id.select_pic);
            viewHold.not_selected_pic = (ImageView)convertView.findViewById(R.id.not_select_pic);
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
        }
        if (list.get(position).getIsSelected() == 0)
        {
            viewHold.selected_pic.setVisibility(View.VISIBLE);
        }
        EnterpriseEntity entity = list.get(position);
        viewHold.enterpriseName.setText(entity.getStringKeyValue(EnterpriseEntity.companyname));
        return convertView;
    }
    
    static class ViewHold
    {
        TextView  enterpriseName;
        
        ImageView selected_pic;
        
        ImageView not_selected_pic;
    }
    
}
