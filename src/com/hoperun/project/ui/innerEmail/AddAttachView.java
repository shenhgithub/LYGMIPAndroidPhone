/*
 * File name:  AddAttachView.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-2-19
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.innerEmail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.BaseUtils.EmailContactListen;
import com.hoperun.manager.adpter.innerEmail.FileArrayAdapter;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.exceptions.MIPException;
import com.hoperun.mip.sqlUtils.SQLCreator.DBHandler;
import com.hoperun.mip.sqlUtils.Table.TFILEINFO;
import com.hoperun.mip.utils.FileUtil;
import com.hoperun.mip.utils.MessageSQLIdConstants;
import com.hoperun.mipmanager.model.entityModule.InnerEmail.FileItemEntity;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.miplygphone.R;

/**
 * 添加发件页面
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-2-19]
 */
public class AddAttachView extends RelativeLayout implements OnClickListener, OnItemClickListener
{
    protected Activity         mActivity;
    
    /** 列表适配器 **/
    private FileArrayAdapter   fileAdapter;
    
    /** 返回上一级目录 **/
    private LinearLayout       backPreFile;
    
    /** 文件路径 **/
    private TextView           filePathTV;
    
    /** 文件列表 **/
    private ListView           fileListView;
    
    /** 确认 **/
    private LinearLayout       confirmLayout;
    
    /** 取消 **/
    private LinearLayout       cancelLayout;
    
    /** 返回按钮 **/
    // private Button backBtn;
    
    String                     filePath;
    
    private File               rootFile;
    
    /**
     * 结束监听
     */
    private EmailContactListen mOnfinishListener = null;
    
    public EmailContactListen getmOnfinishListener()
    {
        return mOnfinishListener;
    }
    
    public void setmOnfinishListener(EmailContactListen mOnfinishListener)
    {
        this.mOnfinishListener = mOnfinishListener;
    }
    
    /**
     * <默认构造函数>
     */
    public AddAttachView(Context context)
    {
        super(context);
    }
    
    public AddAttachView(Activity activity, int button_mid)
    {
        super(activity);
        mActivity = activity;
        initView();
        initListener();
        initData();
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：<date>
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        filePath = FileUtil.getSDPath();// ConstState.MIP_ROOT_DIR;
        if (null == filePath)
        {
            Toast.makeText(mActivity, "请检查sd卡", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // 显示根目录下的文件和文件夹
            rootFile = new File(filePath);
            // 得到当前目录的所有文件
            List<FileItemEntity> listFiles = listFiles(rootFile);
            // 显示界面
            fileAdapter = new FileArrayAdapter(mActivity, android.R.layout.simple_list_item_1, listFiles);
            fileListView.setAdapter(fileAdapter);
        }
    }
    
    /**
     * 显示当前目录下的所有文件以及目录
     * 
     * @param currentFile
     * @return
     */
    private List<FileItemEntity> listFiles(File currentFile)
    {
        // List<File> listFiles = null;
        
        List<FileItemEntity> newListFiles = new ArrayList<FileItemEntity>();
        // 得到文件的数组
        File files[] = currentFile.listFiles();
        
        if (files != null)
        {
            filePathTV.setText(currentFile.getAbsolutePath());
            // listFiles = new ArrayList<File>();
            for (File f : files)
            {
                FileItemEntity entity = new FileItemEntity();
                entity.setFile(f);
                entity.setSelect(false);
                newListFiles.add(entity);
            }
        }
        return newListFiles;
    }
    
    private void initView()
    {
        LayoutInflater.from(mActivity).inflate(R.layout.email_attach, this, true);
        
        RelativeLayout mCompany_rl = (RelativeLayout)findViewById(R.id.email_attach_rl);
        LayoutParams mlp = (LayoutParams)mCompany_rl.getLayoutParams();
        setBackgroundResource(R.color.translucent);
        getBackground().setAlpha(90);
        // mlp = (LayoutParams)BaseUtils.justViewRelativeLayout(mlp, mButton_mid, mViewHeight, mViewWith);
        // mlp.topMargin = 50;
        // mlp.bottomMargin = 50;
        // mlp.leftMargin = 10;
        // mlp.rightMargin = 10;
        
        mCompany_rl.setLayoutParams(mlp);
        mCompany_rl.setOnTouchListener(new OnTouchListener()
        {
            
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                // TODO Auto-generated method stub
                return true;
            }
        });
        
        // backBtn = (Button)findViewById(R.id.file_back);
        backPreFile = (LinearLayout)findViewById(R.id.filePath_ll);
        filePathTV = (TextView)findViewById(R.id.filePathTV);
        fileListView = (ListView)findViewById(R.id.filelist);
        confirmLayout = (LinearLayout)findViewById(R.id.confirm);
        cancelLayout = (LinearLayout)findViewById(R.id.cancel);
    }
    
    /**
     * 
     * 初始化监听器
     * 
     * @Description 初始化监听器
     * 
     * @LastModifiedDate：<date>
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initListener()
    {
        // backBtn.setOnClickListener(this);
        backPreFile.setOnClickListener(this);
        confirmLayout.setOnClickListener(this);
        cancelLayout.setOnClickListener(this);
        fileListView.setOnItemClickListener(this);
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @author wang_ling
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        // case R.id.file_back:
        // if (mOnfinishListener != null)
        // {
        // mOnfinishListener.onContactCancelListener();
        // }
        // break;
        // 返回上一级目录
            case R.id.filePath_ll:
                File newFile = new File(filePathTV.getText().toString());
                File parentFile = newFile.getParentFile();
                if (parentFile.isDirectory() && !newFile.getAbsolutePath().equals(rootFile.getAbsolutePath()))
                {
                    List<FileItemEntity> files = listFiles(parentFile);
                    if (files != null)
                    {
                        fileAdapter = new FileArrayAdapter(mActivity, android.R.layout.simple_list_item_1, files);
                        fileListView.setAdapter(fileAdapter);
                    }
                }
                break;
            // 确定
            case R.id.confirm:
                List<FileItemEntity> fileList = fileAdapter.getAttachList();
                ArrayList<String> newList = new ArrayList<String>();
                
                for (int i = 0; i < fileList.size(); i++)
                {
                    if (fileList.get(i).isSelect())
                    {
                        // String type = fileTYpe(fileList.get(i).getFile().getAbsolutePath());
                        newList.add(fileList.get(i).getFile().getAbsolutePath());
                    }
                }
                // Intent intent = new Intent();
                // intent.putExtra("ATTACHLIST", newList);
                // setResult(RESULT_OK, intent);
                // finish();
                
                if (mOnfinishListener != null)
                {
                    mOnfinishListener.onAttachBack(newList);
                }
                break;
            // 取消
            case R.id.cancel:
                if (mOnfinishListener != null)
                {
                    mOnfinishListener.onContactCancelListener();
                }
                break;
            
            default:
                break;
        }
    }
    
    /**
     * 重载方法
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @author wang_ling
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        File file = fileAdapter.getItem(arg2).getFile();
        boolean flag = fileAdapter.getItem(arg2).isSelect();
        if (file.isDirectory())
        {
            List<FileItemEntity> files = listFiles(file);
            if (files != null)
            {
                fileAdapter = new FileArrayAdapter(mActivity, android.R.layout.simple_list_item_1, files);
                fileListView.setAdapter(fileAdapter);
            }
        }
        else
        {
            fileAdapter.getItem(arg2).setSelect(!flag);
            fileAdapter.notifyDataSetChanged();
        }
    }
    
    public static String distinguish(File file)
    {
        String name = file.getName();
        if (file.isDirectory())
        {
            return "DIR";
        }
        else
        {
            if (name != null && name != "")
            {
                if (name.endsWith("jpg") || name.endsWith("png") || name.endsWith("jpeg") || name.endsWith("JPG")
                    || name.endsWith("PNG") || name.endsWith("JPEG"))
                {
                    return "IMG";
                }
                else if (name.endsWith("doc") || name.endsWith("DOC"))
                {
                    return "DOC";
                }
                else if (name.endsWith("mp4") || name.endsWith("MP4"))
                {
                    return "MP4";
                }
                else if (name.endsWith("pdf") || name.endsWith("PDF"))
                {
                    return "PDF";
                }
                else if (name.endsWith("xls") || name.endsWith("XLS"))
                {
                    return "XLS";
                }
                else if (name.endsWith("zip") || name.endsWith("rar") || name.endsWith("ZIP") || name.endsWith("RAR"))
                {
                    return "ZIP";
                }
                else if (name.endsWith("ppt") || name.endsWith("PPT"))
                {
                    return "PPT";
                }
                else if (name.endsWith("txt") || name.endsWith("TXT"))
                {
                    
                    return "TXT";
                }
            }
            
        }
        return "OTHER";
        
    }
    
    /**
     * 查询数据库 判断文件类型
     */
    @SuppressWarnings("finally")
    private String fileTYpe(String filePath)
    {
        DBHandler hander = new DBHandler(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        hander.DBHandlerDBOpen(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        
        String fileType = "";
        Cursor mCursor = null;
        try
        {
            mCursor =
                hander.query(MessageSQLIdConstants.DB_MESSAGE_FILEINFO, null, TFILEINFO.FILENAME + "=? and "
                    + TFILEINFO.OPENID + "=?", new String[] {new File(filePath).getName(),
                    GlobalState.getInstance().getOpenId()}, null);
            if (mCursor != null)
            {
                while (mCursor.getCount() == 1 && mCursor.moveToNext())
                {
                    fileType = mCursor.getString(mCursor.getColumnIndex(TFILEINFO.FILETYPE));
                }
                mCursor.close();
            }
        }
        catch (MIPException e)
        {
            e.printStackTrace();
        }
        hander.DBHandlerDBClose();
        return fileType;
    }
}
