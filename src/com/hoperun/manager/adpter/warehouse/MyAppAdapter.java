/*
 * File name:  MyAppAdapter.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  li_miao
 * Last modified date:  2014-3-31
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.warehouse;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;
import com.hoperun.project.ui.warehouse.PrefsUtils;
import com.hoperun.project.ui.warehouse.UpdateManager;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author li_miao
 * @Version [版本号, 2014-3-31]
 */
public class MyAppAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context        context;
    
    /** 数据列表 **/
    // private List<HashMap<String, String>> list;
    
    /** 帮助类 **/
    private LayoutInflater inflater;
    
    /** 选择的item **/
    public int             mSelectedPosition = -1;
    
    public String          id;
    
    private List<String>   list              = new ArrayList<String>();
    
    private String         string;
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 上下文
     * @param list 列表数据
     */
    public MyAppAdapter(Context context, String string)
    {
        super();
        this.context = context;
        String app_saved = PrefsUtils.readPrefs(context, "collect");
        for (int i = 0; i < app_saved.split(",").length; i++)
        {
            String item = app_saved.split(",")[i];
            if (!item.equals(""))
            {
                if (!isInstalled(item.split("@")[1]))
                {
                    if (app_saved.startsWith(item) && app_saved.contains(","))
                    {
                        app_saved = app_saved.replace(item + ",", "");
                        // PrefsUtils.writePrefs("collect", label_saved);
                    }
                    else if (app_saved.startsWith(item) && !app_saved.contains(","))
                    {
                        // label_saved = label_saved.replaceAll(ProjectUIID.rowTitle_list.get(i), "");
                        app_saved = app_saved.replace(item, "");
                        
                        // PrefsUtils.writePrefs("collect", label_saved);
                    }
                    else
                    {
                        app_saved = app_saved.replace("," + item, "");
                        // PrefsUtils.writePrefs("collect", label_saved);
                    }
                    PrefsUtils.writePrefs(context, "collect", app_saved);
                }
            }
            
        }
        
        this.string = PrefsUtils.readPrefs(context, "collect");
        String[] str_array = this.string.split(",");
        for (int i = 0; i < str_array.length; i++)
        {
            if (!str_array[i].equals(""))
                list.add(str_array[i]);
        }
        
        this.inflater = LayoutInflater.from(this.context);
        
    }
    
    /**
     * 根据包名判断程序是否安装 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param packageName
     * @return
     * @LastModifiedDate：2014-3-26
     * @author li_miao
     * @EditHistory：<修改内容><修改人>
     */
    public boolean isInstalled(String packageName)
    {
        PackageInfo packageInfo;
        if (null == packageName || "".equals(packageName))
        {
            return false;
        }
        try
        {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        }
        catch (NameNotFoundException e)
        {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null)
        {
            System.out.println("没有安装");
            return false;
            
        }
        else
        {
            
            System.out.println("已经安装");
            return true;
            
        }
        
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
        if (position < list.size() && list.size() > 0)
        {
            // if ("".equals(list.get(position).split("@")))
            // {
            // Bitmap bitmap =
            // BitmapFactory.decodeFile(ConstState.myappsPicPath + list.get(position).split("@")[0] + ".png");
            // viewHold.app_icon.setImageBitmap(bitmap);
            // }
            
            String imagePath = UpdateManager.imagesavePath + list.get(position).split("@")[0] + ".png";
            
            Bitmap bitmap = getBitmapByWidth(imagePath, 40, 10);
            if (null == bitmap)
            {
                viewHold.app_icon.setImageResource(R.drawable.ic_launcher);
            }
            else
            {
                viewHold.app_icon.setImageBitmap(bitmap);
            }
            // viewHold.app_icon.setBackgroundResource(R.drawable.y_icon_4);
            viewHold.myappsName.setTextColor(context.getResources().getColor(R.color.black));
            viewHold.myappsName.setText(list.get(position).split("@")[0]);
        }
        
        // else if (position == list.size())
        // {
        // viewHold.myappsName.setText("协同办公");
        // viewHold.myappsName.setTextColor(context.getResources().getColor(R.color.item_color));
        // viewHold.app_icon.setBackgroundResource(R.drawable.c_icon_6);
        // }
        else if (position == list.size())
        {
            viewHold.myappsName.setText("添加应用");
            viewHold.myappsName.setTextColor(context.getResources().getColor(R.color.app_grey));
            viewHold.app_icon.setBackgroundResource(R.drawable.c_icon_5);
        }
        return convertView;
    }
    
    /**
     * 根据宽度从本地图片路径获取该图片的缩略图
     * 
     * @param localImagePath 本地图片的路径
     * @param width 缩略图的宽
     * @param addedScaling 额外可以加的缩放比例
     * @return bitmap 指定宽高的缩略图
     */
    private Bitmap getBitmapByWidth(String localImagePath, int width, int addedScaling)
    {
        if (TextUtils.isEmpty(localImagePath))
        {
            return null;
        }
        
        Bitmap temBitmap = null;
        
        BitmapFactory.Options outOptions = new BitmapFactory.Options();
        
        // 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中。
        outOptions.inJustDecodeBounds = true;
        
        // 加载获取图片的宽高
        BitmapFactory.decodeFile(localImagePath, outOptions);
        
        int height = outOptions.outHeight;
        
        if (outOptions.outWidth > width)
        {
            // 根据宽设置缩放比例
            outOptions.inSampleSize = outOptions.outWidth / width + 1 + addedScaling;
            outOptions.outWidth = width;
            
            // 计算缩放后的高度
            height = outOptions.outHeight / outOptions.inSampleSize;
            outOptions.outHeight = height;
        }
        
        // 重新设置该属性为false，加载图片返回
        outOptions.inJustDecodeBounds = false;
        outOptions.inPurgeable = true;
        outOptions.inInputShareable = true;
        // temBitmap = BitmapFactory.decodeFile(localImagePath, outOptions);
        temBitmap = BitmapFactory.decodeFile(localImagePath);
        return temBitmap;
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
