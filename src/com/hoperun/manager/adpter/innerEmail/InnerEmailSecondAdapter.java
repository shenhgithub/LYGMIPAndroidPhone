/*
 * File name:  OfficalDocSecondAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-11-5
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.innerEmail;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoperun.mipmanager.model.entityMetaData.EmailAttachBody;
import com.hoperun.mipmanager.model.entityMetaData.GetInnerEmailListInfo;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * 内部邮件收件箱/发件箱列表
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-11-5]
 */
public class InnerEmailSecondAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context                     context;
    
    /** 数据列表 **/
    private List<GetInnerEmailListInfo> docLists;
    
    /** 帮助类 **/
    private LayoutInflater              inflater;
    
    /** 选择的item **/
    public int                          mSelectedPosition = -1;
    
    /** 用户 **/
    private String                      user;
    
    /**
     * 
     * @param context 上下文
     * @param list 列表数据
     * @param user 用户
     * @param type 类型
     * @param mUnDo 已办/待办
     */
    public InnerEmailSecondAdapter(Context context, List<GetInnerEmailListInfo> list, String user)
    {
        super();
        this.context = context;
        
        this.docLists = list;
        
        this.user = user;
        
        this.inflater = LayoutInflater.from(this.context);
        
    }
    
    public List<GetInnerEmailListInfo> getDocLists()
    {
        return docLists;
    }
    
    public void setDocLists(List<GetInnerEmailListInfo> docLists)
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
            convertView = inflater.inflate(R.layout.inner_email_second_item, null);
            
            viewHold.mTvName = (TextView)convertView.findViewById(R.id.files_item_name);
            viewHold.mPeopleName = (TextView)convertView.findViewById(R.id.files_item_people);
            
            viewHold.mTvTime = (TextView)convertView.findViewById(R.id.files_item_time);
            
            viewHold.mRlayout = (RelativeLayout)convertView.findViewById(R.id.file_doc_list_item_layout);
            
            viewHold.mImg = (ImageView)convertView.findViewById(R.id.file_item_icon);
            
            viewHold.mImgIsRead = (ImageView)convertView.findViewById(R.id.file_item_new_icon);
            
            viewHold.mImgIsSelected = (ImageView)convertView.findViewById(R.id.im_select);
            viewHold.mAttachIV = (ImageView)convertView.findViewById(R.id.attach);
            
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
            
        }
        
        GetInnerEmailListInfo docinfo = docLists.get(position);
        
        if (mSelectedPosition == position)
        {
            
            // viewHold.mRlayout.setBackgroundResource(R.drawable.list_2);
            viewHold.mImgIsSelected.setVisibility(View.VISIBLE);
        }
        else
        {
            // viewHold.mRlayout.setBackgroundResource(R.drawable.list_1);
            viewHold.mImgIsSelected.setVisibility(View.INVISIBLE);
        }
        
        String isRead = docinfo.getStringKeyValue(GetInnerEmailListInfo.readflag);
        
        if (isRead == null)
        {
            isRead = "";
        }
        
        if (ConstState.BACK_UNReadDOCLIST.equals(isRead))
        {
            viewHold.mImgIsRead.setVisibility(View.VISIBLE);
            viewHold.mImg.setImageResource(R.drawable.message_1);
        }
        else if (ConstState.BACK_HasReadDOCLIST.equals(isRead))
        {
            viewHold.mImgIsRead.setVisibility(View.GONE);
            viewHold.mImg.setImageResource(R.drawable.message_2);
        }
        else
        {
            viewHold.mImgIsRead.setVisibility(View.GONE);
        }
        
        viewHold.mTvName.setText(docinfo.getStringKeyValue(GetInnerEmailListInfo.msgtitle));
        viewHold.mPeopleName.setText(docinfo.getStringKeyValue(GetInnerEmailListInfo.createusername));
        viewHold.mTvTime.setText(docinfo.getStringKeyValue(GetInnerEmailListInfo.createtime));
        // List<HashMap<String, Object>> attachList =
        // (List<HashMap<String, Object>>)docinfo.getValue(GetInnerEmailListInfo.attachment);
        List<EmailAttachBody> attachList = docinfo.getAttachment();
        if (attachList != null && attachList.size() > 0)
        {
            attachList.get(0).getAttachurl();
            viewHold.mAttachIV.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHold.mAttachIV.setVisibility(View.GONE);
        }
        return convertView;
    }
    
    static class ViewHold
    {
        /**
         * 一级栏目名字
         */
        TextView       mTvName;
        
        /**
         * 发件人名
         */
        TextView       mPeopleName;
        
        /**
         * lastfiledate
         * 
         */
        TextView       mTvTime;
        
        /**
         * 整个布局
         * 
         */
        RelativeLayout mRlayout;
        
        /**
         * 图标，区别文件还是目录（文件夹）
         */
        ImageView      mImg;
        
        /** 图标，区别已读未读。0为未读，1是已读 **/
        ImageView      mImgIsRead;
        
        /** 是否选中图片：显示（选中），不显示（不选中） **/
        ImageView      mImgIsSelected;
        
        ImageView      mAttachIV;
    }
}
