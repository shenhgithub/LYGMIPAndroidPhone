/*
 * File name:  HeaderSetLocalAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wen_tao
 * Last modified date:  2013-12-5
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.cityplan;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.MKPoiInfo;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * 地图功能搜索到的信息适配器
 * 
 * @Description 地图功能搜索到的信息适配器
 * 
 * @author wen_tao
 * @Version [版本号, 2013-12-18]
 */
public class MapSearchAdapter extends PMIPBaseAdapter
{
    /**
     * 上下文环境
     */
    private Context           context;
    
    /** 数据列表 **/
    private List<MyMKPoiInfo> lists;
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 上下文
     * @param list 列表数据
     */
    public MapSearchAdapter(Context context)
    {
        super();
        this.context = context;
        lists = new ArrayList<MyMKPoiInfo>();
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
        return lists == null ? 0 : lists.size();
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
        return lists.get(position).mKPoiInfo;
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
        return position;
    }
    
    public void addInfo(MyMKPoiInfo mKPoiInfo)
    {
        lists.add(mKPoiInfo);
        
    }
    
    public void setSequence(int position, int sequence)
    {
        lists.get(position).sequence = sequence;
        notifyDataSetChanged();
    }
    
    public boolean getSequence(int position)
    {
        if (lists.get(position).sequence == -1)
        {
            return true;
        }
        else
        {
            return false;
        }
        
    }
    
    public void clearInfo()
    {
        lists.clear();
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
        if (lists == null || lists.size() == 0)
        {
            return null;
        }
        ViewHold viewHold;
        if (convertView == null)
        {
            viewHold = new ViewHold();
            convertView = LayoutInflater.from(context).inflate(R.layout.ditu_search_item, null);
            
            viewHold.textView_name = (TextView)convertView.findViewById(R.id.search_item_name);
            viewHold.textView_phone = (TextView)convertView.findViewById(R.id.search_item_phone);
            viewHold.textView_address = (TextView)convertView.findViewById(R.id.search_item_address);
            viewHold.textView_number = (TextView)convertView.findViewById(R.id.search_item_number);
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
        }
        viewHold.textView_name.setText(lists.get(position).mKPoiInfo.name);
        viewHold.textView_phone.setText(lists.get(position).mKPoiInfo.phoneNum);
        viewHold.textView_address.setText(lists.get(position).mKPoiInfo.address);
        
        int number = lists.get(position).sequence;
        if (number != -1)
        {
            viewHold.textView_number.setText("" + number);
            viewHold.textView_number.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHold.textView_number.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }
    
    public static class ViewHold
    {
        TextView textView_name;
        
        TextView textView_phone;
        
        TextView textView_address;
        
        TextView textView_number;
    }
    
    public void onDestory()
    {
        lists.clear();
    }
    
    public class MyMKPoiInfo
    {
        public MyMKPoiInfo(int sequence, MKPoiInfo mKPoiInfo)
        {
            this.mKPoiInfo = mKPoiInfo;
            this.sequence = sequence;
        }
        
        /**
         * 当前子空间的索引值
         */
        public int       sequence;
        
        /**
         * 百度地图提供的坐标点信息
         */
        public MKPoiInfo mKPoiInfo;
    }
}
