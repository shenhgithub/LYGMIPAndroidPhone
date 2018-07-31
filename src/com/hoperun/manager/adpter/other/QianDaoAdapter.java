/*
 * File name:  MenuAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  shen_feng
 * Last modified date:  2013-9-16
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.other;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityMetaData.HistoryRecord;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * 一级栏目adpter
 * 
 * @author shen_feng
 * @Version [版本号, 2013-9-16]
 */
public class QianDaoAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context             context;
    
    /** 数据列表 **/
    private List<HistoryRecord> list;
    
    /** 帮助类 **/
    private LayoutInflater      inflater;
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 上下文
     * @param list 列表数据
     */
    public QianDaoAdapter(Context context, List<HistoryRecord> list)
    {
        super();
        this.context = context;
        
        this.list = list;
        
        this.inflater = LayoutInflater.from(this.context);
        
    }
    
    /**
     * 重载方法
     * 
     * @return 列表数量
     * @author shen_feng
     */
    @Override
    public int getCount()
    {
        return list == null ? 0 : list.size();
    }
    
    /**
     * 重载方法
     * 
     * @param position 位置
     * @return 列表的某一项
     * @author shen_feng
     */
    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }
    
    /**
     * 重载方法
     * 
     * @param position 位置
     * @return itemid
     * @author shen_feng
     */
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public int getViewTypeCount()
    {
        return 2;
    }
    
    @Override
    public int getItemViewType(int position)
    {
        return "".equals(list.get(position).getUserId()) ? 0 : 1;
    }
    
    @Override
    public boolean isEnabled(int position)
    {
        return false;
    };
    
    /**
     * 获取 list
     * 
     * @return 返回 list
     * @author wen_tao
     */
    public List<HistoryRecord> getList()
    {
        return list;
    }
    
    /**
     * 设置 list
     * 
     * @param list 对list进行赋值
     * @author wen_tao
     */
    public void setList(List<HistoryRecord> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }
    
    /**
     * 重载方法
     * 
     * @param position 位置
     * @param convertView convertView
     * @param parent parent
     * @return convertView
     * @author shen_feng
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHold holder = new ViewHold();
        int viewType = getItemViewType(position);
        switch (viewType)
        {
        // 日期节点
            case 0:
                if (convertView == null)
                {
                    convertView = inflater.inflate(R.layout.qiandao_item_isparent, null);
                    holder.positionordate = (TextView)convertView.findViewById(R.id.recoed_data);
                    holder.line = (ImageView)convertView.findViewById(R.id.icon_line);
                    convertView.setTag(holder);
                }
                else
                {
                    holder = (ViewHold)convertView.getTag();
                }
                
                if (position == 0)
                {
                    holder.line.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.line.setVisibility(View.VISIBLE);
                }
                break;
            // 地理位置节点
            case 1:
                
                if (convertView == null)
                {
                    convertView = inflater.inflate(R.layout.qiandao_item_ischild, null);
                    holder.positionordate = (TextView)convertView.findViewById(R.id.recoed_position);
                    holder.recoedTime = (TextView)convertView.findViewById(R.id.recoed_time);
                    convertView.setTag(holder);
                }
                else
                {
                    holder = (ViewHold)convertView.getTag();
                }
                String time = list.get(position).getSiginTime().toString().split(" ")[1];
                if (time.length() > 5)
                {
                    time = time.substring(0, 5);
                }
                holder.recoedTime.setText(time);
                
                if (position == 1)
                {
                    holder.positionordate.setTextColor(context.getResources().getColor(R.color.qiandao_position));
                    holder.recoedTime.setTextColor(context.getResources().getColor(R.color.qiandao_position));
                }
                else
                {
                    holder.positionordate.setTextColor(context.getResources().getColor(R.color.gray_9));
                    holder.recoedTime.setTextColor(context.getResources().getColor(R.color.qiandao_date));
                }
                break;
        }
        holder.positionordate.setText(list.get(position).getLocation());
        return convertView;
    }
    
    static class ViewHold
    {
        // 地理位置或日期
        TextView  positionordate;
        
        // 时间
        TextView  recoedTime;
        
        // 线条
        ImageView line;
    }
    
}