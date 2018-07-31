/*
 * File name:  HeaderSetLocalAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wen_tao
 * Last modified date:  2013-12-5
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.uiset;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hoperun.mip.GlobalState;
import com.hoperun.mip.exceptions.MIPException;
import com.hoperun.mip.sqlUtils.SQLCreator.DBHandler;
import com.hoperun.mip.sqlUtils.Table.TFILEINFO;
import com.hoperun.mip.utils.MessageSQLIdConstants;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * 清理文件类型选择的适配器
 * 
 * @Description 清理文件类型选择的适配器
 * 
 * @author wen_tao
 * @Version [版本号, 2013-12-5]
 */
public class ClearLocalCacheDataAdapter extends PMIPBaseAdapter
{
    
    /** 上下文 **/
    private Context                     context;
    
    /** 数据列表 **/
    private List<String>                lists;
    
    /** 数据列表 **/
    private Map<Integer, Float>         listcount;
    
    /** 是否被选中 **/
    private List<Map<Integer, Boolean>> isSelected;
    
    /** 取消/选中 全选监听 **/
    private MyMessageListener           myMessageListener;
    
    /** 查询文件类型和大小 **/
    private DBHandler                   sqlHelper;
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 上下文
     * @param list 列表数据
     */
    public ClearLocalCacheDataAdapter(Context context, MyMessageListener myMessageListener)
    {
        super();
        this.context = context;
        lists = new ArrayList<String>();
        lists.add(".jpg/png");
        lists.add(".pdf");
        lists.add(".txt");
        lists.add("其他");
        this.listcount = new HashMap<Integer, Float>();
        this.isSelected = new ArrayList<Map<Integer, Boolean>>();
        for (int i = 0; i < lists.size(); i++)
        {
            Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
            map.put(i, true);
            this.isSelected.add(i, map);
            this.listcount.put(i, (float)0);
        }
        this.sqlHelper = DBHandler.getInstance(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        this.sqlHelper.DBHandlerDBOpen(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        countNumber((ConstState.MIP_ROOT_DIR + GlobalState.getInstance().getOpenId()).trim());
        this.myMessageListener = myMessageListener;
        this.sqlHelper.DBHandlerDBClose();
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final ViewHold viewHold;
        if (convertView == null)
        {
            viewHold = new ViewHold();
            convertView = LayoutInflater.from(context).inflate(R.layout.clear_cache_item, null);
            viewHold.fileType = (TextView)convertView.findViewById(R.id.file_type);
            viewHold.checkBox = (CheckBox)convertView.findViewById(R.id.file_type_checkbox);
            viewHold.fileSize = (TextView)convertView.findViewById(R.id.file_size);
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
        }
        viewHold.fileType.setText(lists.get(position));
        viewHold.checkBox.setChecked(isSelected.get(position).get(position));
        viewHold.fileSize.setText("" + listcount.get(position));
        viewHold.checkBox.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Map<Integer, Boolean> map = isSelected.get(position);
                if (viewHold.checkBox.isChecked())
                { // 被选中
                    map.put(position, true);
                    viewHold.checkBox.setChecked(true);
                }
                else
                {
                    // 未被选中
                    map.put(position, false);
                    viewHold.checkBox.setChecked(false);
                }
                myMessageListener.sendMessageToSelectAll(isSelected);
            }
        });
        return convertView;
    }
    
    /**
     * 
     * 获取选中的文件类型
     * 
     * @Description<功能详细描述>
     * 
     * @return
     * @LastModifiedDate：2014-1-2
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    public List<String> getFileType()
    {
        List<String> list = new ArrayList<String>();
        int size = lists.size();
        for (int i = 0; i < size; i++)
        {
            if (i == 0 && isSelected.get(i).get(i))
            {
                list.add(".png");
                list.add(".jpg");
            }
            else if (isSelected.get(i).get(i))
            {
                list.add(lists.get(i));
            }
        }
        if (list.contains("其他"))
        {
            List<String> noClearFile = new ArrayList<String>();
            if (!list.contains(".jpg") && !list.contains(".png"))
            {
                noClearFile.add(".jpg");
                noClearFile.add(".png");
            }
            else if (!list.contains(".pdf"))
            {
                noClearFile.add(".pdf");
            }
            else if (!list.contains(".txt"))
            {
                noClearFile.add(".txt");
            }
            noClearFile.add("其他");
            return noClearFile;
        }
        else
        {
            return list;
        }
        
    }
    
    /**
     * 
     * 全选
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-1-2
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    public void setAllSelected()
    {
        int size = isSelected.size();
        for (int i = 0; i < size; i++)
        {
            Map<Integer, Boolean> map = isSelected.get(i);
            if (!map.get(i))
            {
                map.put(i, true);
            }
        }
        notifyDataSetChanged();
    }
    
    /**
     * 
     * 取消全选
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-1-2
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    public void withNoSelected()
    {
        int size = isSelected.size();
        for (int i = 0; i < size; i++)
        {
            Map<Integer, Boolean> map = isSelected.get(i);
            map.put(i, false);
        }
        notifyDataSetChanged();
    }
    
    /**
     * 重载方法
     * 
     * 
     * @return 列表数量
     * @author shen_feng
     */
    @Override
    public int getCount()
    {
        return lists == null ? 0 : lists.size();
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
        return lists.get(position);
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
        return position;
    }
    
    public void clear()
    {
        lists.clear();
        isSelected.clear();
    }
    
    static class ViewHold
    {
        TextView fileType;
        
        TextView fileSize;
        
        CheckBox checkBox;
    }
    
    /**
     * 
     * 统计文件个数
     * 
     * @Description<功能详细描述>
     * 
     * @param delpath
     * @return
     * @LastModifiedDate：2014-1-2
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    private void countNumber(String delpath)
    {
        File file = new File(delpath);
        // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
        if (!file.isDirectory())
        {
            count(delpath);
        }
        else if (file.isDirectory())
        {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++)
            {
                File delfile = new File(delpath + "/" + filelist[i]);
                if (!delfile.isDirectory())
                {
                    count(delpath + "/" + filelist[i]);
                }
                else if (delfile.isDirectory())
                {
                    countNumber(delpath + "/" + filelist[i]);
                }
            }
        }
    }
    
    private void count(String delpath)
    {
        File file = new File(delpath);
        if (!file.exists())
        {
            return;
        }
        if (delpath.contains("."))
        {
            String str = delpath.substring(delpath.lastIndexOf("."));
            NUM(file, delpath, str);
        }
        else
        {
            // 查询数据库
            Cursor mCursor;
            try
            {
                mCursor =
                    this.sqlHelper.query(MessageSQLIdConstants.DB_MESSAGE_FILEINFO, new String[] {TFILEINFO.FILESIZE,
                        TFILEINFO.FILETYPE}, TFILEINFO.FILENAME + "=? and " + TFILEINFO.OPENID + "=?", new String[] {
                        file.getName(), GlobalState.getInstance().getOpenId()}, null);
                
                if (mCursor != null)
                {
                    while (mCursor.moveToNext())
                    {
                        String fileType = mCursor.getString(mCursor.getColumnIndex(TFILEINFO.FILETYPE));
                        NUM(file, delpath, fileType);
                    }
                }
            }
            catch (MIPException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private void NUM(File file, String delpath, String str)
    {
        float count = (float)(file.length() / 1000);
        if (str.equals("png") || str.equals("jpg"))
        {
            if (listcount.containsKey(0))
            {
                count = listcount.get(0) + count;
            }
            listcount.put(0, count);
        }
        else if (str.equals("pdf"))
        {
            if (listcount.containsKey(1))
            {
                count = listcount.get(1) + count;
            }
            listcount.put(1, count);
        }
        else if (str.equals("txt"))
        {
            if (listcount.containsKey(2))
            {
                count = listcount.get(2) + count;
            }
            listcount.put(2, count);
        }
        else
        {
            if (listcount.containsKey(3))
            {
                count = listcount.get(3) + count;
            }
            listcount.put(3, count);
        }
    }
    
    public interface MyMessageListener
    {
        public void sendMessageToSelectAll(List<Map<Integer, Boolean>> isSelected);
    }
}
