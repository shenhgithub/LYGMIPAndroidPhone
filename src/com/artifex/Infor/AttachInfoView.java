/*
 * File name:  InfoView.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-1-28
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.Infor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoperun.manager.components.CustomHandler.CustomHanler;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.model.entityMetaData.GetXwzxDocDetailInfo;
import com.hoperun.mipmanager.model.entityMetaData.XwzxAttachBody;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;

/**
 * 显示信息页面
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-1-28]
 */
public class AttachInfoView extends RelativeLayout implements OnClickListener, OnItemClickListener
{
    
    protected String               mSelectedDocId       = "";
    
    protected String               mSelectedPath        = "";
    
    private String                 mSelectedPathParent  = "";
    
    protected String               mSelectedFileTitle   = "";
    
    protected String               mSelectedFlag        = "";
    
    protected String               mSelectedmType       = "";
    
    protected String               mSelectedmHandleType = "";
    
    private Context                mContext;
    
    private boolean                isDownling           = false;
    
    protected String               mparentPath          = "";
    
    /**
     * 列表view
     */
    private ListView               mListView;
    
    /**
     * 返回按钮
     */
    private TextView               mBack;
    
    /**
     * // * 列表view 的adapter //
     */
    private AttchInfoViewAdapter   mInfoAdapter;
    
    /**
     * 附件列表数据
     */
    private List<XwzxAttachBody>   myList;
    
    /**
     * 获取“信息”的task
     */
    private NetTask                mGetAttchCount;
    
    /**
     * 该view的返回监听
     */
    private onCloseAttchViewListen mviewbacklistener;
    
    /** 等待框布局 **/
    private RelativeLayout         loadLayout;
    
    /** 等待图片 **/
    private ImageView              loadImageView;
    
    /**
     * 网络返回handler
     */
    private CustomHanler           mhandHanler          = new CustomHanler()
                                                        {
                                                            
                                                            @Override
                                                            public void PostHandle(int requestType, Object objHeader,
                                                                Object objBody, boolean error, int errorCode)
                                                            {
                                                                // TODO Auto-generated method stub
                                                                onPostHandle(requestType,
                                                                    objHeader,
                                                                    objBody,
                                                                    error,
                                                                    errorCode);
                                                            }
                                                            
                                                        };
    
    /**
     * 网络返回处理函数
     * 
     * @Description<功能详细描述>
     * 
     * @param requestType 请求ID
     * @param objHeader header
     * @param objBody 返回body
     * @param error 是否错误
     * @param errorCode 错误标识
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        // TODO Auto-generated method stub
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETXWZXATTACHREQUEST:
                    
                    if (error)
                    {
                        closeDownloadProgress();
                        // mviewbacklistener.onShowThisViewListener(mSelectedPath);
                        parseResponse(objBody);
                        // 打开文件
                    }
                    break;
                case RequestTypeConstants.PROGRESS_BAR:
                    // // 返回进度值
                    if (objBody instanceof Integer)
                    {
                        mDownloadfilePro.setProgress((Integer)objBody);
                    }
                    break;
                default:
            }
        }
        else
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETXWZXATTACHREQUEST:
                    closeDownloadProgress();
                    break;
                default:
                    break;
            }
        }
    }
    
    private void parseResponse(Object objBody)
    {
        MetaResponseBody contentBody = (MetaResponseBody)objBody;
        
        List<HashMap<String, Object>> buzList = contentBody.getBuzList();
        
        if (buzList != null && buzList.size() > 0)
        {
            // mSelectedmType
            // saveFileInfo(buzList, user, , mHandleType, firstName, mSelectedFileTitle, ConstState.XWZX);
            
            String filename = (String)buzList.get(0).get("filename").toString().trim();
            int index = filename.lastIndexOf(";");
            String filetype = "";
            String mParentDirPath = new File(mSelectedPathParent).getParentFile().getAbsolutePath() + "/";
            if (index == -1)
            {
                mSelectedPath = mParentDirPath + filename;
            }
            else
            {
                String mFilename = filename.substring(0, index);
                mSelectedPath = mParentDirPath + mFilename;
                filetype = filename.substring(index + 1, filename.length());
            }
            boolean retDBData =
                DBDataObjectWrite.DocSaveFileRecord(GlobalState.getInstance().getOpenId(),
                    mSelectedmType,
                    mSelectedmHandleType,
                    mSelectedDocId,
                    mSelectedFileTitle,
                    mSelectedPath,
                    ConstState.XWZX,
                    filetype);
            mviewbacklistener.onShowThisViewListener(mSelectedPath);
        }
        
    }
    
    public AttachInfoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
    public AttachInfoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public AttachInfoView(Context context, Intent intent, int marginBottom)
    {
        super(context);
        mContext = context;
        initIntentData(intent);
        initView();
        initData();
    }
    
    /**
     * 初始化相关的数据，获取该文件的属性
     * 
     * @Description<功能详细描述>
     * 
     * @param intent
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void initIntentData(Intent intent)
    {
        if (intent != null)
        {
            mSelectedDocId = intent.getStringExtra(ConstState.PDF_FILEMESSAGEID);
            mSelectedmType = intent.getStringExtra(ConstState.PDF_TYPE);
            mSelectedmHandleType = intent.getStringExtra(ConstState.PDF_HANDLETYPE);
            mSelectedPathParent = intent.getStringExtra(ConstState.PDF_PATH);
        }
    }
    
    /**
     * 初始化view
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-11
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void initView()
    {
        
        LayoutInflater.from(mContext).inflate(R.layout.attachinfo, this, true);
        
        mListView = (ListView)findViewById(R.id.attch_info_list);
        mListView.setOnItemClickListener(this);
        mBack = (TextView)findViewById(R.id.attch_info_back);
        mBack.setOnClickListener(this);
        
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
    }
    
    /**
     * 初始化数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-12
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        
        myList =
            getXwzxAttchInfoFromDBData(GlobalState.getInstance().getOpenId(),
                mSelectedmType,
                mSelectedmHandleType,
                mSelectedDocId);
        mInfoAdapter = new AttchInfoViewAdapter(mContext, myList);
        mListView.setAdapter(mInfoAdapter);
        
        closeProgressDialog();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        return true;
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
        // 点击时间间隔太短则不触发
        if (OnClickUtil.isMostPost())
        {
            
            return;
        }
        switch (v.getId())
        {// 返回
            case R.id.attch_info_back:
                if (mviewbacklistener != null)
                {
                    mviewbacklistener.onCloseThisViewListener();
                }
        }
    }
    
    public onCloseAttchViewListen getMviewbacklistener()
    {
        return mviewbacklistener;
    }
    
    public void setMviewbacklistener(onCloseAttchViewListen mviewbacklistener)
    {
        this.mviewbacklistener = mviewbacklistener;
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
    
    /** 选中附件的名字 id+名字 作为存储的docid **/
    private String selectName = "";
    
    /**
     * 重载方法
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @author wen_tao
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        XwzxAttachBody item = (XwzxAttachBody)mInfoAdapter.getItem(arg2);
        String selectUrl = item.getUrl();
        selectName = mSelectedDocId + item.getName();
        String parentPath = new File(mSelectedPathParent).getParentFile().getAbsolutePath() + "/";
        
        // mSelectedPath = parentPath + selectName;
        mSelectedPath =
            DBDataObjectWrite.getFilePath(GlobalState.getInstance().getOpenId(), selectName, ConstState.XWZX);
        File file = new File(mSelectedPath);
        if (file.exists())
        {
            // openPDFFile();
            mviewbacklistener.onShowThisViewListener(mSelectedPath);
        }
        else
        {
            showDownloadProgress();
            getFileAttachWithUrl(selectUrl, parentPath, selectName);
        }
    }
    
    private NetTask mGetFileAttachRequst;
    
    /**
     * 
     * 根据url下载文件
     * 
     * @Description<功能详细描述>
     * 
     * @param docUrl
     * @param path
     * @LastModifiedDate：2014-3-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void getFileAttachWithUrl(String docUrl, String path, String fileName)
    {
        mGetFileAttachRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETXWZXATTACHREQUEST).create();
        JSONObject body = new JSONObject();
        NetRequestController.getXwzxAttchFile(mGetFileAttachRequst,
            mhandHanler,
            RequestTypeConstants.GETXWZXATTACHREQUEST,
            body,
            docUrl,
            path,
            fileName);
    }
    
    protected PopupWindow mPopupWindow;
    
    protected ProgressBar mDownloadfilePro;
    
    protected void closeDownloadProgress()
    {
        if (null != mPopupWindow)
        {
            mPopupWindow.dismiss();
        }
    }
    
    /**
     * 
     * 显示下载进度条
     * 
     * @LastModifiedDate：2013-10-9
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    protected void showDownloadProgress()
    {
        isDownling = true;
        View view = LayoutInflater.from(mContext).inflate(R.layout.downloadfileprogress, null);
        
        // 输入框一样长，高度自适应
        mPopupWindow =
            new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        
        mPopupWindow.showAtLocation(mListView, Gravity.CENTER, 0, 0);
        
        mDownloadfilePro = (ProgressBar)view.findViewById(R.id.downloadprogress);
        
        mDownloadfilePro.setProgress(0);
        
        Button mBtnCancel = (Button)view.findViewById(R.id.cancel_download);
        
        mBtnCancel.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // 点击时间间隔太短则不触发
                if (OnClickUtil.isMostPost())
                {
                    return;
                }
                stopGetFileContentNetTask();
                if (isDownling)
                {
                    isDownling = false;
                }
                mPopupWindow.dismiss();
                
            }
        });
        
        mPopupWindow.setOnDismissListener(new OnDismissListener()
        {
            
            public void onDismiss()
            {
                
            }
        });
    }
    
    /**
     * 停止获取文件任务
     * 
     * @Description<功能详细描述>
     * 
     * @EditHistory：<修改内容><修改人>
     */
    public void stopGetFileContentNetTask()
    {
        // TODO Auto-generated method stub
        if (mGetAttchCount != null)
            mGetAttchCount.shutDownExecute();
    }
    
    protected Handler bHandler = new Handler()
                               {
                                   
                                   @SuppressWarnings("unchecked")
                                   @Override
                                   public void handleMessage(Message msg)
                                   {
                                       // TODO Auto-generated method stub
                                       super.handleMessage(msg);
                                       
                                       switch (msg.what)
                                       {
                                           case ConstState.GETFILECONTENT:
                                               boolean retDBData = (Boolean)msg.obj;
                                               
                                               // getFileContentEnd(retDBData);
                                               break;
                                           default:
                                               break;
                                       }
                                   }
                                   
                               };
    
    public List<XwzxAttachBody> getXwzxAttchInfoFromDBData(String username, String typeCode, String handleType,
        String docid)
    {
        GetXwzxDocDetailInfo test1 = new GetXwzxDocDetailInfo();
        List<XwzxAttachBody> doclists = new ArrayList<XwzxAttachBody>();
        List<HashMap<String, Object>> queryret;
        String where =
            GetXwzxDocDetailInfo.l_user + " = ?" + " and " + GetXwzxDocDetailInfo.l_funcode + " = ?" + " and "
                + GetXwzxDocDetailInfo.l_handletype + "= ?" + " and " + GetXwzxDocDetailInfo.l_docid + "= ?";
        
        String[] selectionArgs = null;
        
        selectionArgs = new String[] {username, typeCode, handleType, docid};
        
        queryret = test1.query(null, where, selectionArgs, "");
        
        if (queryret == null || queryret.size() == 0)
        {
            // Toast.makeText(mActivtiy, "没有更多数据", Toast.LENGTH_SHORT).show();
        }
        else
        {
            GetXwzxDocDetailInfo info = new GetXwzxDocDetailInfo();
            info.convertToObject(queryret.get(0));
            List<XwzxAttachBody> attachdtolist = info.getAttachmenturls();
            int size = attachdtolist.size();
            for (int i = 0; i < size; i++)
            {
                XwzxAttachBody body = attachdtolist.get(i);
                doclists.add(body);
            }
        }
        return doclists;
    }
    
    /**
     * 
     * 关闭附件页面
     * 
     * @Description<功能详细描述>
     * 
     * @author wang_ling
     * @Version [版本号, 2014-3-20]
     */
    public interface onCloseAttchViewListen
    {
        // 显示view help
        public void onShowThisViewListener(String selectFilePath);
        
        // 关闭view attch
        public void onCloseThisViewListener();
    }
}
