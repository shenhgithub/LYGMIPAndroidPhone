/*
 * File name:  ClearLocalCacheData.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wen_tao
 * Last modified date:  2014-1-2
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.Login;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hoperun.manager.adpter.uiset.ClearLocalCacheDataAdapter;
import com.hoperun.manager.adpter.uiset.ClearLocalCacheDataAdapter.MyMessageListener;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.exceptions.MIPException;
import com.hoperun.mip.sqlUtils.SQLCreator.DBHandler;
import com.hoperun.mip.sqlUtils.Table.TFILEINFO;
import com.hoperun.mip.utils.MessageSQLIdConstants;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.miplygphone.R;

/**
 * 清除缓存dialog
 * 
 * @Description 清除缓存dialog
 * 
 * @author wen_tao
 * @Version [版本号, 2014-1-2]
 */
public class ClearLocalCacheDataDialog implements OnClickListener, android.view.View.OnKeyListener
{
    /** 清理数据对话框 **/
    private Dialog                     clearCacheDataDialog;
    
    /** 上下文 **/
    private Context                    mContext;
    
    /** 数据清理布局 */
    private View                       clearDataView;
    
    /** 文件类型list */
    private ListView                   fileTypeList;
    
    /** 数据清理确认 */
    private Button                     clearButton;
    
    /** 数据清理取消 */
    private Button                     cancelButton;
    
    /** 全选按钮 */
    private CheckBox                   allButton;
    
    /** 当前选中的需要清除的文件类型 */
    private List<String>               listsFile;
    
    /** 文件类型适配器 */
    private ClearLocalCacheDataAdapter adapter;
    
    /** 等待框布局 **/
    private RelativeLayout             loadLayout;
    
    /** 等待图片 **/
    private ImageView                  loadImageView;
    
    /** 查询文件类型和大小 **/
    private DBHandler                  sqlHelper;
    
    public ClearLocalCacheDataDialog(Context mContext)
    {
        this.mContext = mContext;
        onCreate();
    }
    
    private void onCreate()
    {
        // 加载xml布局
        LayoutInflater factory = LayoutInflater.from(mContext);
        this.clearDataView = factory.inflate(R.layout.clearcachedialog, null);
        this.fileTypeList = (ListView)clearDataView.findViewById(R.id.clear_data_list);
        this.clearButton = (Button)clearDataView.findViewById(R.id.clear_ok);
        this.cancelButton = (Button)clearDataView.findViewById(R.id.clear_cancel);
        this.clearButton.setOnClickListener(this);
        this.cancelButton.setOnClickListener(this);
        this.loadLayout = (RelativeLayout)clearDataView.findViewById(R.id.load_layout);
        this.loadImageView = (ImageView)clearDataView.findViewById(R.id.waitdialog_img);
        this.allButton = (CheckBox)clearDataView.findViewById(R.id.all_select);
        this.allButton.setOnClickListener(this);
        adapter = new ClearLocalCacheDataAdapter(mContext, new LocalMessageListener());
        fileTypeList.setAdapter(adapter);
        clearCacheDataDialog = new Dialog(mContext, R.style.DialogStyle);
        clearCacheDataDialog.setContentView(clearDataView);
        clearCacheDataDialog.setCancelable(false);
        
        this.sqlHelper = DBHandler.getInstance(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
    }
    
    /**
     * 
     * 显示清理对话框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-1-2
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    public void showDialog()
    {
        if (clearCacheDataDialog != null)
        {
            clearCacheDataDialog.show();
        }
    }
    
    /**
     * 
     * 关闭清理窗口，清理list数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-1-2
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    public void closeDialog()
    {
        if (clearCacheDataDialog != null)
        {
            closeProgressDialog();
            adapter.clear();
            clearCacheDataDialog.dismiss();
            if (listsFile != null)
            {
                listsFile.clear();
                listsFile = null;
            }
        }
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @author wen_tao
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.clear_ok:
                
                GlobalState.getInstance().setIsAutoLogin(false);
                GlobalState.getInstance().setIsRemember(false);
                
                listsFile = adapter.getFileType();
                if (listsFile == null || listsFile.size() == 0)
                {
                    Toast.makeText(mContext, "请选择需要删除的文件类型", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgressDialog();
                this.sqlHelper.DBHandlerDBOpen(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
                if (deletefile((ConstState.MIP_ROOT_DIR + GlobalState.getInstance().getOpenId()).trim()))
                {
                    closeProgressDialog();
                    Toast.makeText(mContext, "清理完成", Toast.LENGTH_SHORT).show();
                    closeDialog();
                }
                else
                {
                    closeProgressDialog();
                    Toast.makeText(mContext, "清理失败", Toast.LENGTH_SHORT).show();
                }
                this.sqlHelper.DBHandlerDBClose();
                break;
            case R.id.clear_cancel:
                closeDialog();
                break;
            case R.id.all_select:
                if (allButton.isChecked())
                {
                    adapter.setAllSelected();
                }
                else
                {
                    adapter.withNoSelected();
                }
                break;
        }
    }
    
    /**
     * 
     * 删除文件夹
     * 
     * @Description<功能详细描述>
     * 
     * @param delpath
     * @return
     * @LastModifiedDate：2014-1-2
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    private boolean deletefile(String delpath)
    {
        File file = new File(delpath);
        // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
        if (!file.isDirectory())
        {
            if (isNeedDelete(delpath))
            {
                file.delete();
            }
            return true;
        }
        else if (file.isDirectory())
        {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++)
            {
                File delfile = new File(delpath + "/" + filelist[i]);
                if (!delfile.isDirectory() && isNeedDelete(delpath + "/" + filelist[i]))
                {
                    delfile.delete();
                }
                else if (delfile.isDirectory())
                {
                    deletefile(delpath + "/" + filelist[i]);
                    if (new File(delpath + "/" + filelist[i]).list().length == 0)
                    {
                        new File(delpath + "/" + filelist[i]).delete();
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * 
     * 判断当前目录的文件夹是否需要被删除
     * 
     * @Description<功能详细描述>
     * 
     * @param filePath
     * @return
     * @LastModifiedDate：2014-1-2
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    private boolean isNeedDelete(String filePath)
    {
        if (listsFile.contains("其他"))
        {
            if ((filePath.contains(".") && listsFile.contains(filePath.substring(filePath.lastIndexOf("."))))
                || listsFile.contains(fileTYpe(filePath)))
            {
                return false;
            }
            else
            {
                return true;
            }
            
        }
        else
        {
            if ((filePath.contains(".") && listsFile.contains(filePath.substring(filePath.lastIndexOf("."))))
                || listsFile.contains(fileTYpe(filePath)))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 查询数据库 判断文件类型
     */
    @SuppressWarnings("finally")
    private String fileTYpe(String filePath)
    {
        String fileType = "";
        Cursor mCursor;
        try
        {
            mCursor =
                this.sqlHelper.query(MessageSQLIdConstants.DB_MESSAGE_FILEINFO, new String[] {TFILEINFO.FILESIZE,
                    TFILEINFO.FILETYPE}, TFILEINFO.FILENAME + "=? and " + TFILEINFO.OPENID + "=?", new String[] {
                    new File(filePath).getName(), GlobalState.getInstance().getOpenId()}, null);
            if (mCursor != null)
            {
                while (mCursor.moveToNext())
                {
                    fileType = mCursor.getString(mCursor.getColumnIndex(TFILEINFO.FILETYPE));
                }
            }
        }
        catch (MIPException e)
        {
            e.printStackTrace();
        }
        finally
        {
            return fileType;
        }
    }
    
    /**
     * 
     * 显示等待框
     * 
     * @Description 显示等待框
     * 
     * @LastModifiedDate：2013-10-22
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void showProgressDialog()
    {
        loadLayout.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable)loadImageView.getBackground();
        animationDrawable.start();
    }
    
    /**
     * 
     * 关闭等待框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-22
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void closeProgressDialog()
    {
        loadLayout.setVisibility(View.GONE);
    }
    
    /**
     * 
     * 是否选中“全选选择框”的监听类
     * 
     * @Description 是否选中“全选选择框”的监听类
     * 
     * @author wen_tao
     * @Version [版本号, 2014-1-12]
     */
    public class LocalMessageListener implements MyMessageListener
    {
        /**
         * 重载方法
         * 
         * @author wen_tao
         */
        @Override
        public void sendMessageToSelectAll(List<Map<Integer, Boolean>> isSelected)
        {
            if (isSelected == null || isSelected.size() == 0)
            {
                return;
            }
            int size = isSelected.size();
            int i = 0;
            for (; i < size; i++)
            {
                if (!isSelected.get(i).get(i))
                {
                    break;
                }
            }
            if (i == size && !allButton.isChecked())
            {
                allButton.setChecked(true);
            }
            else if (i < size && allButton.isChecked())
            {
                allButton.setChecked(false);
            }
        }
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @param keyCode
     * @param event
     * @return
     * @author wen_tao
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            closeDialog();
            return true;
        }
        return false;
    }
}
