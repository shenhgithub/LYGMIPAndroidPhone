/*
 * File name:  MenuAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  shen_feng
 * Last modified date:  2013-9-16
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.devSupervise;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * 设备运行一级栏目adpter
 * 
 * @author shen_feng
 * @Version [版本号, 2013-9-16]
 */
public class DevSuperviceListAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context                       context;
    
    /** 数据列表 **/
    private List<HashMap<String, Object>> list;
    
    /** 帮助类 **/
    private LayoutInflater                inflater;
    
    /** 选择的item **/
    public int                            mSelectedPosition = -1;
    
    public List<HashMap<String, Object>> getList()
    {
        return list;
    }
    
    public void setList(List<HashMap<String, Object>> list)
    {
        this.list = list;
    }
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 上下文
     * @param list 列表数据
     */
    public DevSuperviceListAdapter(Context context, List<HashMap<String, Object>> list)
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
        return list.size();
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
        return list.get(position);
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
        Object funName = list.get(position).get("name");
        if (funName == null)
        {
            return "";
        }
        else
        {
            return (String)funName;
        }
        
    }
    
    public String getItemDocId(int position)
    {
        Object docid = list.get(position).get("id");
        if (docid == null)
        {
            return "";
        }
        else
        {
            return (String)docid;
        }
    }
    
    public String getItemUrl(int position)
    {
        Object url = list.get(position).get("url");
        if (url == null)
        {
            return "";
        }
        else
        {
            return (String)url;
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
        return position;
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
            convertView = inflater.inflate(R.layout.file_main_list, null);
            
            viewHold.mTvName = (TextView)convertView.findViewById(R.id.files_item_name);
            viewHold.mImagIcon = (ImageView)convertView.findViewById(R.id.file_item_icon);
            
            viewHold.mRlayout = (RelativeLayout)convertView.findViewById(R.id.file_doc_list_item_layout);
            
            viewHold.mImgNew = (ImageView)convertView.findViewById(R.id.file_item_new_icon);
            
            viewHold.mImgNew.setVisibility(View.GONE);
            
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
            
        }
        
        String name = (String)list.get(position).get("name");
        viewHold.mTvName.setText(name);
        
        return convertView;
    }
    
    static class ViewHold
    {
        // 一级栏目名字
        TextView       mTvName;
        
        /** 图标 **/
        ImageView      mImagIcon;
        
        // 整个布局
        RelativeLayout mRlayout;
        
        // 是否为新的标记
        ImageView      mImgNew;
        
    }
}
