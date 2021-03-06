/*
 * File name:  OfficalDocSecondAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-11-5
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.offical;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoperun.mipmanager.model.entityMetaData.GetDocListInfo;
import com.hoperun.mipmanager.utils.ConstState;
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
public class OfficalDocSecondAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context              context;
    
    /** 数据列表 **/
    private List<GetDocListInfo> docLists;
    
    /** 帮助类 **/
    private LayoutInflater       inflater;
    
    /** 选择的item **/
    public int                   mSelectedPosition = -1;
    
    /** 用户 **/
    private String               user;
    
    /** type **/
    private String               type;
    
    /**
     * 
     * @param context 上下文
     * @param list 列表数据
     * @param user 用户
     * @param type 类型
     * @param mUnDo 已办/待办
     */
    public OfficalDocSecondAdapter(Context context, List<GetDocListInfo> list, String user, String type)
    {
        super();
        this.context = context;
        
        this.docLists = list;
        
        this.user = user;
        
        this.type = type;
        
        this.inflater = LayoutInflater.from(this.context);
        
    }
    
    public List<GetDocListInfo> getDocLists()
    {
        return docLists == null ? new ArrayList<GetDocListInfo>() : docLists;
    }
    
    public void setDocLists(List<GetDocListInfo> docLists)
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
            convertView = inflater.inflate(R.layout.file_doc_list_item, null);
            
            viewHold.mTvName = (TextView)convertView.findViewById(R.id.files_item_name);
            
            viewHold.mTvTime = (TextView)convertView.findViewById(R.id.files_item_time);
            
            viewHold.mRlayout = (RelativeLayout)convertView.findViewById(R.id.file_doc_list_item_layout);
            
            viewHold.mImg = (ImageView)convertView.findViewById(R.id.file_item_icon);
            
            viewHold.mImg.setImageResource(R.drawable.icon_nr_1);
            
            viewHold.mImgIsRead = (ImageView)convertView.findViewById(R.id.file_item_new_icon);
            
            viewHold.mImgIsSelected = (ImageView)convertView.findViewById(R.id.im_select);
            
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
            
        }
        
        GetDocListInfo docinfo = docLists.get(position);
        
        if (mSelectedPosition == position)
        {
            viewHold.mImgIsSelected.setVisibility(View.VISIBLE);
            // viewHold.mRlayout.setBackgroundResource(R.drawable.list_2);
            
        }
        else
        {
            viewHold.mImgIsSelected.setVisibility(View.INVISIBLE);
            // viewHold.mRlayout.setBackgroundResource(R.drawable.list_1);
        }
        
        String isRead = docinfo.getStringKeyValue(GetDocListInfo.readstatus);
        String isHandle = docinfo.getStringKeyValue(GetDocListInfo.l_handletype);
        
        if (isRead == null)
        {
            isRead = "";
        }
        
        if (ConstState.HASHANDLEDOCLIST.equals(isHandle))
        {
            viewHold.mImgIsRead.setVisibility(View.GONE);
        }
        else
        {
            if (ConstState.BACK_UNReadDOCLIST.equals(isRead))
            {
                viewHold.mImgIsRead.setVisibility(View.VISIBLE);
            }
            else if (ConstState.BACK_HasReadDOCLIST.equals(isRead))
            {
                viewHold.mImgIsRead.setVisibility(View.GONE);
            }
        }
        
        // else
        // {
        // viewHold.mImgIsRead.setVisibility(View.GONE);
        // }
        
        viewHold.mTvName.setText(docinfo.getStringKeyValue(GetDocListInfo.title));
        
        viewHold.mTvTime.setText(docinfo.getStringKeyValue(GetDocListInfo.createdatetime));
        
        return convertView;
    }
    
    static class ViewHold
    {
        // 一级栏目名字
        TextView       mTvName;
        
        // lastfiledate
        TextView       mTvTime;
        
        // 整个布局
        RelativeLayout mRlayout;
        
        // 图标，区别文件还是目录（文件夹）
        ImageView      mImg;
        
        // 图标，区别已读未读。0为未读，1是已读
        ImageView      mImgIsRead;
        
        /** 是否选中图片：显示（选中），不显示（不选中） **/
        ImageView      mImgIsSelected;
        
    }
}
