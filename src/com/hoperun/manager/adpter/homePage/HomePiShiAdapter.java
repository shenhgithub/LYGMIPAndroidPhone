/*
 * File name:  HomePiShiAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2013-11-27
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.homePage;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoperun.mipmanager.model.entityMetaData.GetLeaderPiShiListInfo;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2013-11-27]
 */
public class HomePiShiAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context                      context;
    
    /** 数据列表 **/
    private List<GetLeaderPiShiListInfo> docLists;
    
    /** 帮助类 **/
    private LayoutInflater               inflater;
    
    /** 选择的item **/
    public int                           mSelectedPosition = -1;
    
    /** 用户 **/
    private String                       user;
    
    /**
     * 
     * @param context 上下文
     * @param list 列表数据
     * @param user 用户
     * @param type 类型
     * @param mUnDo 已办/待办
     */
    public HomePiShiAdapter(Context context, List<GetLeaderPiShiListInfo> list, String user)
    {
        super();
        this.context = context;
        
        this.docLists = list;
        
        this.user = user;
        
        this.inflater = LayoutInflater.from(this.context);
        
    }
    
    public List<GetLeaderPiShiListInfo> getDocLists()
    {
        return docLists;
    }
    
    public void setDocLists(List<GetLeaderPiShiListInfo> docLists)
    {
        this.docLists = docLists;
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
        return docLists == null ? 0 : docLists.size();
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
        return docLists.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.homepoplist_item, null);
            
            viewHold.mPosition = (TextView)convertView.findViewById(R.id.posionTv);
            viewHold.mName = (TextView)convertView.findViewById(R.id.itemtitletv);
            viewHold.mDate = (TextView)convertView.findViewById(R.id.datetv);
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
            
        }
        
        viewHold.mPosition.setText((position + 1) + "");
        GetLeaderPiShiListInfo docinfo = docLists.get(position);
        viewHold.mName.setText(docinfo.getStringKeyValue(GetLeaderPiShiListInfo.title));
        if (docinfo.getStringKeyValue(GetLeaderPiShiListInfo.createdatetime).length() > 10)
        {
            viewHold.mDate.setText(docinfo.getStringKeyValue(GetLeaderPiShiListInfo.createdatetime).substring(0, 10));
        }
        else
        {
            viewHold.mDate.setText(docinfo.getStringKeyValue(GetLeaderPiShiListInfo.createdatetime));
        }
        return convertView;
    }
    
    static class ViewHold
    {
        /** 位置 **/
        TextView mPosition;
        
        /** 标题名称 **/
        TextView mName;
        
        /** 日期 **/
        TextView mDate;
        
    }
}
