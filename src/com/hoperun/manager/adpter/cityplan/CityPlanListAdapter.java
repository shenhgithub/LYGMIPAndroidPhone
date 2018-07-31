/*
 * File name:  OfficalDocSecondAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-11-5
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.cityplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-11-5]
 */
public class CityPlanListAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context          context;
    
    /** 数据列表 **/
    private MetaResponseBody list;
    
    /** 帮助类 **/
    private LayoutInflater   inflater;
    
    /** 选择的item **/
    public int               mSelectedPosition = -1;
    
    /**
     * 
     * @param context 上下文
     * @param list 列表数据
     * @param user 用户
     * @param type 类型
     * @param mUnDo 已办/待办
     */
    public CityPlanListAdapter(Context context, MetaResponseBody list)
    {
        super();
        this.context = context;
        
        this.list = list;
        
        this.inflater = LayoutInflater.from(this.context);
        
    }
    
    /**
     * 
     * 获取选中项的位置
     * 
     * @Description 获取选中项的位置
     * 
     * @return 选中项的位置
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public int getmSelectedPosition()
    {
        return mSelectedPosition;
    }
    
    /**
     * 
     * 设置选中项的位置
     * 
     * @Description 设置选中项的位置
     * 
     * @param mSelectedPosition 选中项的位置
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setmSelectedPosition(int mSelectedPosition)
    {
        this.mSelectedPosition = mSelectedPosition;
    }
    
    /**
     * 重载方法
     * 
     * @return 列表的长度
     * @author shen_feng
     */
    @Override
    public int getCount()
    {
        return list.getBuzList().size();
    }
    
    /**
     * 重载方法
     * 
     * @param position 位置
     * @return 选中位置那项的数据
     * @author shen_feng
     */
    @Override
    public Object getItem(int position)
    {
        return list.getBuzList().get(position);
    }
    
    /**
     * 重载方法
     * 
     * @param position position
     * @return ItemId
     * @author shen_feng
     */
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    /**
     * 重载方法
     * 
     * @param position position
     * @param convertView convertView
     * @param parent parent
     * @return View
     * @author shen_feng
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        
        ViewHold viewHold;
        if (convertView == null)
        {
            
            viewHold = new ViewHold();
            convertView = inflater.inflate(R.layout.nettv_item, null);
            
            viewHold.mTvName = (TextView)convertView.findViewById(R.id.nettv_item_name);
            
            viewHold.mRlayout = (RelativeLayout)convertView.findViewById(R.id.nettv_list_item_layout);
            
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
            
        }
        
        if (mSelectedPosition == position)
        {
            
            viewHold.mRlayout.setBackgroundResource(R.drawable.list_2);
            
        }
        else
        {
            viewHold.mRlayout.setBackgroundResource(R.drawable.list_1);
            
        }
        
        viewHold.mTvName.setText((String)list.getBuzList().get(position).get("funcname"));
        
        return convertView;
    }
    
    static class ViewHold
    {
        // 一级栏目名字
        TextView       mTvName;
        
        // 整个布局
        RelativeLayout mRlayout;
        
        // 是否是新的
        ImageView      mTvNew;
    }
}
