/*
 * File name:  MenuAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  shen_feng
 * Last modified date:  2013-9-16
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.Login.LoginModule;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * 一级栏目adpter
 * 
 * @author shen_feng
 * @Version [版本号, 2013-9-16]
 */
public class MenuAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context           context;
    
    /** 数据列表 **/
    private List<LoginModule> list;
    
    /** 帮助类 **/
    private LayoutInflater    inflater;
    
    /** 选择的item **/
    public int                mSelectedPosition = -1;
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 上下文
     * @param list 列表数据
     */
    public MenuAdapter(Context context, List<LoginModule> list)
    {
        super();
        this.context = context;
        
        this.list = list;
        
        this.inflater = LayoutInflater.from(this.context);
        
    }
    
    /**
     * 
     * 获取选中的位置
     * 
     * @Description 获取选中的位置
     * 
     * @return 选中的位置
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
     * 设置选中的位置
     * 
     * @Description 设置选中的位置
     * 
     * @param mSelectedPosition 选中的位置
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
     * @return 列表数量
     * @author shen_feng
     */
    @Override
    public int getCount()
    {
        return list.size();
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
        return list.get(position).getFuncCode();
    }
    
    /**
     * 
     * 获取功能id
     * 
     * @Description 获取功能id
     * 
     * @param position 位置
     * @return 功能id
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public String getFuncId(int position)
    {
        
        return list.get(position).getFuncId();
    }
    
    /**
     * 
     * 获取功能id
     * 
     * @Description 获取功能id
     * 
     * @param position 位置
     * @return 功能id
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public String getFunName(int position)
    {
        
        return list.get(position).getFuncName();
    }
    
    /**
     * 
     * 获取搜索关键字
     * 
     * @Description 获取搜索关键字
     * 
     * @param position 位置
     * @return 关键字
     * @LastModifiedDate：2013-11-15
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public String getSearchKeyWords(int position)
    {
        return list.get(position).getSearchkeywords();
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
        return 0;
    }
    
    public List<LoginModule> getLoginModule()
    {
        return list;
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
        
        ViewHold viewHold;
        if (convertView == null)
        {
            
            viewHold = new ViewHold();
            convertView = inflater.inflate(R.layout.main_menu_list_item, null);
            
            viewHold.mTvName = (TextView)convertView.findViewById(R.id.menu_list_item_name);
            viewHold.mLinearLayout = (RelativeLayout)convertView.findViewById(R.id.menu_layout);
            viewHold.mIcon = (ImageView)convertView.findViewById(R.id.icon);
            viewHold.mSelect = (ImageView)convertView.findViewById(R.id.select);
            viewHold.mUnreadCountRL = (RelativeLayout)convertView.findViewById(R.id.unreadcountrl);
            viewHold.mUnreadCountTV = (TextView)convertView.findViewById(R.id.unreadcounttv);
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
            
        }
        
        viewHold.mTvName.setText(list.get(position).getFuncName());
        
        // if (mSelectedPosition == position)
        // {
        // viewHold.mLinearLayout.setBackgroundResource(R.drawable.set);
        // // viewHold.mSelect.setVisibility(View.VISIBLE);
        // }
        // else
        // {
        // viewHold.mLinearLayout.setBackgroundResource(R.drawable.list_1);
        // // viewHold.mSelect.setVisibility(View.INVISIBLE);
        // }
        
        String unreadCount = list.get(position).getCount();
        if (unreadCount != null && !unreadCount.equals("") && !unreadCount.equals("0"))
        {
            viewHold.mUnreadCountRL.setVisibility(View.VISIBLE);
            viewHold.mUnreadCountTV.setText(unreadCount);
        }
        else
        {
            viewHold.mUnreadCountRL.setVisibility(View.INVISIBLE);
        }
        viewHold.mIcon.setBackgroundResource(list.get(position).getmIcon_BackGround());
        return convertView;
    }
    
    static class ViewHold
    {
        // 一级栏目名字
        TextView       mTvName;
        
        // 一级栏目布局
        RelativeLayout mLinearLayout;
        
        ImageView      mIcon;
        
        // 选中的标志
        ImageView      mSelect;
        
        RelativeLayout mUnreadCountRL;
        
        TextView       mUnreadCountTV;
    }
}
