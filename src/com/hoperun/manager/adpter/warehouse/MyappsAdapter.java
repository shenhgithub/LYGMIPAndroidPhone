/*
 * File name:  MyappsAdapter.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  li_miao
 * Last modified date:  2014-3-26
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.warehouse;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;
import com.hoperun.project.ui.warehouse.AppsModule;

/**
 * 我的应用列表适配器
 * 
 * @Description<功能详细描述>
 * 
 * @author li_miao
 * @Version [版本号, 2014-3-26]
 */
public class MyappsAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context          context;
    
    /** 数据列表 **/
    // private List<HashMap<String, String>> list;
    
    /** 帮助类 **/
    private LayoutInflater   inflater;
    
    /** 选择的item **/
    public int               mSelectedPosition = -1;
    
    public String            id;
    
    private List<AppsModule> list;
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 上下文
     * @param list 列表数据
     */
    public MyappsAdapter(Context context, List<AppsModule> list)
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
        // return list.getBuzList().get(0).get("apptypes").size();
        return list.size() + 1;
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
        return list.size();
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
        // Object funName = list.getBuzList().get(position).get("apptypename");
        // if (funName == null)
        // {
        // return getItemName(position);
        // }
        // else
        // {
        
        return "";
        // }
        
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
            convertView = inflater.inflate(R.layout.myapps_item, null);
            
            viewHold.myappsName = (TextView)convertView.findViewById(R.id.myapps_name);
            viewHold.app_icon = (ImageView)convertView.findViewById(R.id.icon);
            // viewHold.textNum = (TextView)convertView.findViewById(R.id.types_num);
            // viewHold.textId = (TextView)convertView.findViewById(R.id.types_id);
            // // 测试
            // convertView = inflater.inflate(R.layout.app_types_item, null);
            // viewHold.mTvName = (TextView)convertView.findViewById(R.id.menu_list_item_name);
            // viewHold.mRlayout = (RelativeLayout)convertView.findViewById(R.id.file_doc_list_item_layout);
            //
            // viewHold.mImgNew = (ImageView)convertView.findViewById(R.id.file_item_new_icon);
            //
            // viewHold.mImgNew.setVisibility(View.GONE);
            
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
            
        }
        
        if (mSelectedPosition == position)
        {
            
            // viewHold.mRlayout.setBackgroundResource(R.drawable.list_2);
            
        }
        else
        {
            
            // viewHold.mRlayout.setBackgroundResource(R.drawable.list_1);
            
        }
        if (position != list.size() && list.size() > 0)
        {
            Bitmap bitmap =
                BitmapFactory.decodeFile("/sdcard/连云港移动办公/Icon/" + list.get(position).getAppname() + ".png");
            viewHold.app_icon.setImageBitmap(bitmap);
            viewHold.myappsName.setTextColor(context.getResources().getColor(R.color.black));
            viewHold.myappsName.setText(list.get(position).getAppname());
        }
        
        else if (position == list.size())
        {
            viewHold.myappsName.setText("添加应用");
            viewHold.myappsName.setTextColor(context.getResources().getColor(R.color.app_grey));
            viewHold.app_icon.setBackgroundResource(R.drawable.c_icon_5);
        }
        return convertView;
    }
    
    // /**
    // * 获取 id
    // *
    // * @return 返回 id
    // * @author li_miao
    // */
    // public String getId(int position)
    // {
    // return (String)list.get(position).get("apptype");
    // }
    
    // /**
    // * s 设置 id
    // *
    // * @param id 对id进行赋值
    // * @author li_miao
    // */
    // public void setId(String id)
    // {
    // this.id = id;
    // }
    //
    static class ViewHold
    {
        // 一级栏目名字
        TextView  myappsName;
        
        // 应用图标
        ImageView app_icon;
        // // 整个布局
        // RelativeLayout mRlayout;
        //
        // // 是否为新的标记
        // ImageView mImgNew;
        
    }
}
