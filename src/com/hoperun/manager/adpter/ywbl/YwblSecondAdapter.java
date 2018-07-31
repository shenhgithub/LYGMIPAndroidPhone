/*
 * File name:  YwblSecondAdapter.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-4-3
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.ywbl;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.ywbl.KaoboEntity;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;
import com.hoperun.project.ui.function.ywbl.KaoBoDealActivity;
import com.hoperun.project.ui.function.ywbl.LeavePortActivity;
import com.hoperun.project.ui.function.ywbl.MovePortActivity;
import com.hoperun.project.ui.function.ywbl.ShipDetailActivity;
import com.hoperun.project.ui.function.ywbl.ZuoYeDealActivity;

/**
 * <一句话功能简述>靠泊处理列表adapter
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-4-3]
 */
public class YwblSecondAdapter extends PMIPBaseAdapter
{
    private Context           context;
    
    private List<KaoboEntity> list;
    
    private LayoutInflater    inflater;
    
    private int               mSelectedBusinessId;
    
    public YwblSecondAdapter(Context context, List<KaoboEntity> list, int id)
    {
        super();
        this.context = context;
        
        this.list = list;
        
        this.inflater = LayoutInflater.from(this.context);
        this.mSelectedBusinessId = id;
    }
    
    public List<KaoboEntity> getList()
    {
        return list;
    }
    
    public void setList(List<KaoboEntity> list)
    {
        this.list = list;
    }
    
    public int getmSelectedBusinessId()
    {
        return mSelectedBusinessId;
    }
    
    public void setmSelectedBusinessId(int mSelectedBusinessId)
    {
        this.mSelectedBusinessId = mSelectedBusinessId;
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
            convertView = inflater.inflate(R.layout.kaobo_list_item, null);
            viewHold.ship_name = (TextView)convertView.findViewById(R.id.ship_name);
            viewHold.arrive_time = (TextView)convertView.findViewById(R.id.arrive_time);
            viewHold.ship_represent = (TextView)convertView.findViewById(R.id.ship_represent);
            viewHold.bring_water = (TextView)convertView.findViewById(R.id.bring_water);
            viewHold.ship_plan = (TextView)convertView.findViewById(R.id.ship_plan);
            viewHold.detail = (Button)convertView.findViewById(R.id.detail_btn);
            viewHold.deal = (Button)convertView.findViewById(R.id.deal_btn);
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
        }
        
        KaoboEntity entity = list.get(position);
        viewHold.ship_name.setText(entity.getStringKeyValue(KaoboEntity.shipname));
        viewHold.arrive_time.setText(entity.getStringKeyValue(KaoboEntity.arriveanchortime));
        viewHold.ship_represent.setText(entity.getStringKeyValue(KaoboEntity.shipclientname));
        viewHold.bring_water.setText(entity.getStringKeyValue(KaoboEntity.leadmarkname));
        viewHold.ship_plan.setText(entity.getStringKeyValue(KaoboEntity.planmarkname));
        
        viewHold.detail.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, ShipDetailActivity.class);
                intent.putExtra("shipid", list.get(position).getStringKeyValue(KaoboEntity.shipid));
                intent.putExtra("blocknum", mSelectedBusinessId);
                context.startActivity(intent);
            }
        });
        viewHold.deal.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (2 == mSelectedBusinessId)
                {
                    Intent intent = new Intent(context, KaoBoDealActivity.class);
                    intent.putExtra("shipid", list.get(position).getStringKeyValue(KaoboEntity.shipid));
                    intent.putExtra("planintime", list.get(position).getStringKeyValue(KaoboEntity.planinporttime));
                    intent.putExtra("planleavetime", list.get(position).getStringKeyValue(KaoboEntity.planoutporttime));
                    context.startActivity(intent);
                }
                else if (3 == mSelectedBusinessId)
                {
                    Intent intent = new Intent(context, ZuoYeDealActivity.class);
                    intent.putExtra("shipid", list.get(position).getStringKeyValue(KaoboEntity.shipid));
                    intent.putExtra("berthid", list.get(position).getStringKeyValue(KaoboEntity.berthid));
                    context.startActivity(intent);
                }
                else if (4 == mSelectedBusinessId)
                {
                    Intent intent = new Intent(context, MovePortActivity.class);
                    intent.putExtra("shipid", list.get(position).getStringKeyValue(KaoboEntity.shipid));
                    intent.putExtra("berthid", list.get(position).getStringKeyValue(KaoboEntity.berthid));
                    intent.putExtra("planmovetime", list.get(position).getStringKeyValue(KaoboEntity.planmoveberthtime));
                    intent.putExtra("planmoveposition",
                        list.get(position).getStringKeyValue(KaoboEntity.planmoveberthno));
                    context.startActivity(intent);
                }
                else if (5 == mSelectedBusinessId)
                {
                    Intent intent = new Intent(context, LeavePortActivity.class);
                    intent.putExtra("shipid", list.get(position).getStringKeyValue(KaoboEntity.shipid));
                    intent.putExtra("planintime", list.get(position).getStringKeyValue(KaoboEntity.planinporttime));
                    intent.putExtra("planleavetime", list.get(position).getStringKeyValue(KaoboEntity.planoutporttime));
                    context.startActivity(intent);
                }
                else
                {
                    return;
                }
                
            }
        });
        return convertView;
    }
    
    static class ViewHold
    {
        TextView ship_name;
        
        TextView arrive_time;
        
        TextView ship_represent;
        
        TextView bring_water;
        
        TextView ship_plan;
        
        Button   detail;
        
        Button   deal;
    }
}
