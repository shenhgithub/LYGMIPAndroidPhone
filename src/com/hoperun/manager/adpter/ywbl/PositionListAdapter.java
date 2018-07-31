/*
 * File name:  PositionListAdapter.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-4-10
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.ywbl;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.ywbl.BoweiEntity;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * <一句话功能简述>泊位位置列表adapter
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-4-10]
 */
public class PositionListAdapter extends PMIPBaseAdapter
{
    private Context           context;
    
    private List<BoweiEntity> list;
    
    private LayoutInflater    inflater;
    
    public PositionListAdapter(Context context, List<BoweiEntity> list)
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
            convertView = inflater.inflate(R.layout.select_bowei_item, null);
            viewHold.bowei_id = (TextView)convertView.findViewById(R.id.bowei_id);
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
        }
        BoweiEntity entity = list.get(position);
        viewHold.bowei_id.setText(entity.getStringKeyValue(BoweiEntity.position));
        return convertView;
    }
    
    static class ViewHold
    {
        TextView bowei_id;
    }
}
