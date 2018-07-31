/*
 * File name:  FileLibrarySecondAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2013-11-19
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.txl.perContactDetailEntity;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * 个人通讯录列表详细列表适配器
 * 
 * @Description 个人通讯录列表详细列表适配器
 * 
 * @author wang_ling
 * @Version [版本号, 2014-03-31]
 */
public class PersonDetailAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context                      context;
    
    /** 数据列表 **/
    private List<perContactDetailEntity> docLists;
    
    /** 帮助类 **/
    private LayoutInflater               inflater;
    
    /** 选择的item **/
    public int                           mSelectedPosition = -1;
    
    /**
     * 
     * @param context 上下文
     * @param list 列表数据
     * @param user 用户
     * @param type 类型
     * @param mUnDo 已办/待办
     */
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 上下文
     * @param list 列表数据
     * @param user 用户
     * @param type 类型
     */
    public PersonDetailAdapter(Context context, List<perContactDetailEntity> list)
    {
        super();
        this.context = context;
        
        this.docLists = list;
        
        this.inflater = LayoutInflater.from(this.context);
        
    }
    
    /**
     * 
     * 获取数据列表
     * 
     * @Description 获取数据列表
     * 
     * @return 数据列表
     * @LastModifiedDate：2013-11-13
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public List<perContactDetailEntity> getDocLists()
    {
        return docLists;
    }
    
    /**
     * 
     * 设置数据列表
     * 
     * @Description 设置数据列表
     * 
     * @param docLists 数据列表
     * @LastModifiedDate：2013-11-13
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setDocLists(List<perContactDetailEntity> docLists)
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
     * @return position
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
     * @author wang_ling
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        
        ViewHold viewHold;
        if (convertView == null)
        {
            
            viewHold = new ViewHold();
            convertView = inflater.inflate(R.layout.personlink_detail_item, null);
            viewHold.parentRl = (RelativeLayout)convertView.findViewById(R.id.file_doc_list_item_layout);
            viewHold.mtypeName = (TextView)convertView.findViewById(R.id.files_item_type_name);
            
            viewHold.mTvName = (TextView)convertView.findViewById(R.id.files_item_name);
            viewHold.mImg = (ImageView)convertView.findViewById(R.id.file_item_icon);
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
            
        }
        // he
        perContactDetailEntity docinfo = docLists.get(position);
        
        String type = docinfo.getType();
        // 手机号码
        if ("0".equals(type))
        {
            viewHold.mtypeName.setText(context.getResources().getString(R.string.txl_tel));
            viewHold.mTvName.setText(docinfo.getName());
            viewHold.mImg.setImageResource(R.drawable.icon_tl);
        }
        // 办公室号码
        else if ("1".equals(type))
        {
            viewHold.mtypeName.setText(context.getResources().getString(R.string.txl_office));
            viewHold.mTvName.setText(docinfo.getName());
            viewHold.mImg.setImageResource(R.drawable.icon_tp);
        }
        // 邮件
        else if ("2".equals(type))
        {
            viewHold.mtypeName.setText(context.getResources().getString(R.string.txl_email));
            viewHold.mTvName.setText(docinfo.getName());
            viewHold.mImg.setImageResource(R.drawable.icon_tm);
        }// 更多
        else if ("3".equals(type))
        {
            // viewHold.mtypeName.setText(context.getResources().getString(R.string.txl_more));
            viewHold.mTvName.setText(docinfo.getName());
            viewHold.mImg.setVisibility(View.INVISIBLE);
            // viewHold.mImg.setImageResource(R.drawable.more);
        }
        
        return convertView;
    }
    
    static class ViewHold
    {
        /** 外层布局 **/
        RelativeLayout parentRl;
        
        TextView       mtypeName;
        
        /** 联系人名字 **/
        TextView       mTvName;
        
        /** icon **/
        ImageView      mImg;
    }
}
