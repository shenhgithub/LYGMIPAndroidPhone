/*
 * File name:  MenuAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  shen_feng
 * Last modified date:  2013-9-16
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.nettv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * 网络电视adpter
 * 
 * @author 文涛
 * @Version [版本号, 2013-9-16]
 */
public class NettvListAdapter extends PMIPBaseAdapter
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
     * <默认构造函数>
     * 
     * @param context 上下文
     * @param list 列表数据
     */
    public NettvListAdapter(Context context, MetaResponseBody list)
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
     * @return 返回列表的长度
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
     * @return 返回列表的某一项
     * @author shen_feng
     */
    @Override
    public Object getItem(int position)
    {
        return list.getBuzList().get(position);
    }
    
    /**
     * 获取名称
     * 
     * @Description<功能详细描述>
     * 
     * @param position
     * @return
     * @LastModifiedDate：2013-11-4
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public String getItemName(int position)
    {
        Object funName = list.getBuzList().get(position);
        if (funName == null)
        {
            return getItemName(position);
        }
        else
        {
            return (String)funName;
        }
        
    }
    
    /**
     * 重载方法
     * 
     * @param position 位置
     * @return 某一项的id
     * @author shen_feng
     */
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /**
     * 重载方法
     * 
     * @param position position
     * @param convertView convertView
     * @param parent parent
     * @return view
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
    }
}
