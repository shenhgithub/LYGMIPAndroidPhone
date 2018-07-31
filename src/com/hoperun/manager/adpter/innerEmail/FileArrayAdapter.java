/*
 * File name:  FileArrayAdapter.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-2-21
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.innerEmail;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.mipmanager.model.entityModule.InnerEmail.FileItemEntity;
import com.hoperun.mipmanager.utils.FileTypeUtil;
import com.hoperun.miplygphone.R;

/**
 * 文件列表适配器
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-2-21]
 */
public class FileArrayAdapter extends ArrayAdapter<FileItemEntity>
{
    private List<FileItemEntity> attachList;
    
    /** 布局扩展器 **/
    private LayoutInflater       layoutInflater;
    
    public FileArrayAdapter(Context context, int textViewResourceId, List<FileItemEntity> objects)
    {
        super(context, textViewResourceId, objects);
        this.attachList = objects;
        layoutInflater = LayoutInflater.from(context);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHold viewHolder = new ViewHold();
        convertView = layoutInflater.inflate(R.layout.email_filelist_item, null);
        convertView.setTag(viewHolder);
        viewHolder.fileSelect = (ImageView)convertView.findViewById(R.id.select_iv);
        viewHolder.fileIcon = (ImageView)convertView.findViewById(R.id.file_icon);
        viewHolder.fileTitle = (TextView)convertView.findViewById(R.id.filetitle);
        File file = (File)this.getItem(position).getFile();
        boolean isSelect = this.getItem(position).isSelect();
        String fileName = file.getName();
        String fileType = FileTypeUtil.distinguish(fileName);
        
        if (file.isDirectory())
        {
            viewHolder.fileSelect.setVisibility(View.GONE);
            viewHolder.fileIcon.setBackgroundResource(R.drawable.icon_5);
            
        }
        else
        {
            viewHolder.fileSelect.setVisibility(View.VISIBLE);
            // 判断文件类型
            if (fileType.equals("IMG"))
            {
                viewHolder.fileIcon.setBackgroundResource(R.drawable.icon_pic);
            }
            else if (fileType.equals("DOC"))
            {
                viewHolder.fileIcon.setBackgroundResource(R.drawable.icon_doc);
            }
            else if (fileType.equals("PDF"))
            {
                viewHolder.fileIcon.setBackgroundResource(R.drawable.pdf);
            }
            else if (fileType.equals("TXT"))
            {
                viewHolder.fileIcon.setBackgroundResource(R.drawable.icon_txt);
            }
            else
            {
                viewHolder.fileIcon.setBackgroundResource(R.drawable.pdf);
            }
        }
        viewHolder.fileTitle.setText(fileName);
        if (isSelect)
        {
            viewHolder.fileSelect.setBackgroundResource(R.drawable.choose_r);
        }
        else
        {
            viewHolder.fileSelect.setBackgroundResource(R.drawable.choose_box);
        }
        return convertView;
    }
    
    public List<FileItemEntity> getAttachList()
    {
        return this.attachList;
    }
    
    class ViewHold
    {
        /** 选择图片 **/
        private ImageView fileSelect;
        
        /** 文件icon **/
        private ImageView fileIcon;
        
        /** 文件标题 **/
        private TextView  fileTitle;
        
    }
    
}
