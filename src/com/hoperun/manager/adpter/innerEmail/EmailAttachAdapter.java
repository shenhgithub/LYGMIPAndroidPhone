/*
 * File name:  EmailAttachAdapter.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-2-19
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.innerEmail;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.mipmanager.model.entityMetaData.EmailAttachBody;
import com.hoperun.mipmanager.utils.FileTypeUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * 附件列表适配器
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-2-19]
 */
public class EmailAttachAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context               context;
    
    /** 数据列表 **/
    private List<EmailAttachBody> list;
    
    /** 帮助类 **/
    private LayoutInflater        inflater;
    
    /** 是否是本地添加的附件 **/
    private boolean               isAddAttach;
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 上下文
     * @param list 列表数据
     */
    public EmailAttachAdapter(Context context, List<EmailAttachBody> list, boolean flag)
    {
        super();
        this.context = context;
        
        this.list = list;
        
        this.inflater = LayoutInflater.from(this.context);
        this.isAddAttach = flag;
    }
    
    public List<EmailAttachBody> getList()
    {
        return list;
    }
    
    public void setList(List<EmailAttachBody> list)
    {
        this.list = list;
    }
    
    /**
     * 重载方法
     * 
     * @return 返回列表的长度
     * @author wang_ling
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
     * @author wang_ling
     */
    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }
    
    /**
     * 重载方法
     * 
     * @param position
     * @param convertView
     * @param parent
     * @return
     * @author wang_ling
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHold viewHold;
        if (convertView == null)
        {
            
            viewHold = new ViewHold();
            convertView = inflater.inflate(R.layout.email_attachlist_item, null);
            
            viewHold.mTvName = (TextView)convertView.findViewById(R.id.filetitle);
            
            viewHold.mImgIcon = (ImageView)convertView.findViewById(R.id.file_icon);
            
            viewHold.mImgDel = (ImageView)convertView.findViewById(R.id.file_delete);
            
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
        }
        
        String fileName = list.get(position).getAttachtitle();
        viewHold.mTvName.setText(fileName);
        String fileType = FileTypeUtil.distinguish(fileName);
        // 判断文件类型
        if (fileType.equals("IMG"))
        {
            viewHold.mImgIcon.setBackgroundResource(R.drawable.icon_pic);
        }
        else if (fileType.equals("DOC"))
        {
            viewHold.mImgIcon.setBackgroundResource(R.drawable.icon_doc);
        }
        else if (fileType.equals("PDF"))
        {
            viewHold.mImgIcon.setBackgroundResource(R.drawable.pdf);
        }
        else if (fileType.equals("TXT"))
        {
            viewHold.mImgIcon.setBackgroundResource(R.drawable.icon_txt);
        }
        else
        {
            viewHold.mImgIcon.setBackgroundResource(R.drawable.pdf);
        }
        if (isAddAttach)
        {
            viewHold.mImgDel.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHold.mImgDel.setVisibility(View.GONE);
        }
        viewHold.mImgDel.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                list.remove(position);
                notifyDataSetChanged();
            }
            
        });
        return convertView;
    }
    
    static class ViewHold
    {
        
        /** icon **/
        ImageView mImgIcon;
        
        /** 文件名字 **/
        TextView  mTvName;
        
        /** 删除图标 **/
        ImageView mImgDel;
        
    }
}
