/*
 * File name:  DocSendView.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-11-11
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.send.GWLZSend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.artifex.BaseUtils.CompanyAddressListen;
import com.artifex.BaseUtils.NextStepListen;
import com.artifex.BaseUtils.onViewBackListen;
import com.artifex.send.AddressView.CompanyAddressView;
import com.hoperun.manager.components.CustomDialog;
import com.hoperun.manager.components.CustomDialogListener;
import com.hoperun.manager.components.CustomHandler.CustomHanler;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetParam.FileInfo;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.FileUtil;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.mipmanager.model.entityMetaData.GetDocListInfo;
import com.hoperun.mipmanager.model.entityMetaData.PDFNextStepInfo;
import com.hoperun.mipmanager.model.wrapRequest.SendDocWrapRequest;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * 点击“发送”按钮出现的view
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-11-11]
 */
/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2014-2-12]
 */
public class DocSendView extends RelativeLayout
{
    private PMIPBaseActivity    mActivity;
    
    /**
     * “发送”按钮的位置
     */
    private int                 mButton_mid;
    
    private Intent              mIntent_value;
    
    /**
     * 公文流转下，下一步步骤view
     */
    private DocSendNextStepView stepView;
    
    /**
     * 公文流转下，选择联系人的view
     */
    private CompanyAddressView  cantactsV;
    
    /**
     * 发送页面向pdf阅读页面的监听
     */
    private onViewBackListen    mviewbacklistener;
    
    /**
     * 公文id
     */
    private String              mFileId;
    
    /**
     * 公文路径
     */
    private String              mFilePath;
    
    /**
     * 公文名称
     */
    private String              mFileName;
    
    /**
     * 类型，收文/发文
     */
    private String              mType;
    
    /**
     * 待办/已办
     */
    private String              mHandlerType;
    
    /**
     * 发送文件的Task
     */
    private NetTask             mSendDocContentRequst;
    
    /**
     * 获取文件的Task
     */
    private NetTask             mGetFileContentRequst;
    
    /**
     * 已被选的发送联系人
     */
    private String              mSendusersId = "";
    
    /**
     * 发送意见
     */
    private String              mOpinion     = "";
    
    /**
     * 文档ID
     */
    private String              mStepId      = "";
    
    /**
     * 下一步步骤页面和选择联系人页面切换时的监听
     */
    private NextStepListen      mlistener    = new NextStepListen()
                                             {
                                                 
                                                 /**
                                                  * 收到下一步处理步骤，点击某一条目的监听
                                                  * 
                                                  * @param info
                                                  * @author ren_qiujing
                                                  */
                                                 @Override
                                                 public void onNextStepListener(PDFNextStepInfo info)
                                                 {
                                                     // TODO Auto-generated method stub
                                                     removeAllViews();
                                                     if (cantactsV != null)
                                                     {
                                                         cantactsV.clearAll();
                                                         cantactsV = null;
                                                     }
                                                     cantactsV =
                                                         new CompanyAddressView(mActivity, info, mButton_mid,
                                                             mIntent_value);
                                                     cantactsV.setmOnfinishListener(new CompanyAddressListen()
                                                     {
                                                         
                                                         @Override
                                                         public void onCompanyAddressListener(int type)
                                                         {
                                                             // TODO Auto-generated method stub
                                                             switch (type)
                                                             {
                                                                 case ConstState.SEND_CANCEL:
                                                                     removeAllViews();
                                                                     if (stepView == null)
                                                                     {
                                                                         stepView =
                                                                             new DocSendNextStepView(mActivity,
                                                                                 mButton_mid, mIntent_value);
                                                                         stepView.setONNextStepListener(mlistener);
                                                                     }
                                                                     addView(stepView);
                                                                     
                                                                     break;
                                                                 
                                                                 case ConstState.SEND_SUCCESS:
                                                                 case ConstState.SEND_FAILE:
                                                                     removeAllViews();
                                                                     mviewbacklistener.onViewBackListener(type);
                                                                     break;
                                                                 default:
                                                                     break;
                                                             }
                                                         }
                                                         
                                                         @Override
                                                         public void onSendFileBegin(int type, String sendusersId,
                                                             String opinion, String stepId)
                                                         {
                                                             // TODO Auto-generated method stub
                                                             switch (type)
                                                             {
                                                                 case ConstState.SEND_BEGIN:
                                                                     removeAllViews();
                                                                     mviewbacklistener.onViewBackListener(type);
                                                                     mSendusersId = sendusersId;
                                                                     mOpinion = opinion;
                                                                     mStepId = stepId;
                                                                     // sendFileContent(sendusersId, opinion, mStepId);
                                                                     break;
                                                                 
                                                                 default:
                                                                     break;
                                                             }
                                                             
                                                         }
                                                         
                                                     });
                                                     
                                                     addView(cantactsV);
                                                 }
                                                 
                                                 /**
                                                  * 收到下一步步骤的“取消”监听事件
                                                  * 
                                                  * @author ren_qiujing
                                                  */
                                                 @Override
                                                 public void onCancelListener()
                                                 {
                                                     // TODO Auto-generated method stub
                                                     removeAllViews();
                                                     mviewbacklistener.onViewBackListener(ConstState.SEND_CANCEL);
                                                 }
                                                 
                                                 @Override
                                                 public void onSendFile(String option, String setpID)
                                                 {
                                                     // TODO Auto-generated method stub
                                                     mviewbacklistener.onViewBackListener(ConstState.SEND_BEGIN);
                                                     mSendusersId = "";
                                                     mOpinion = option;
                                                     mStepId = setpID;
                                                 }
                                                 
                                             };
    
    /**
     * 网络处理handle
     */
    private CustomHanler        mHandler     = new CustomHanler()
                                             {
                                                 @Override
                                                 public void PostHandle(int requestType, Object objHeader,
                                                     Object objBody, boolean error, int errorCode)
                                                 {
                                                     // TODO Auto-generated method stub
                                                     onPostHandle(requestType, objHeader, objBody, error, errorCode);
                                                 }
                                                 
                                             };
    
    @SuppressWarnings("unchecked")
    private void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETFILECONTENT:
                    MetaResponseBody getContentBuzBody = (MetaResponseBody)objBody;
                    if (getContentBuzBody.getRetError().equals("0"))
                    {
                        showMessage("下载文件成功！");
                        mviewbacklistener.onViewBackListener(ConstState.SEND_FAILE);
                    }
                    else
                    {
                        showMessage("下载文件失败！");
                    }
                    break;
                case RequestTypeConstants.SENDDOCCONTENT:
                    
                    MetaResponseBody mSendResponseBuzBody = (MetaResponseBody)objBody;
                    if (mSendResponseBuzBody != null && mSendResponseBuzBody.getRetError().equals("0"))
                    {
                        List<HashMap<String, Object>> ret1 = mSendResponseBuzBody.getBuzList();
                        
                        if (ret1 == null || ret1.size() == 0)
                        {
                            showMessage("文件发送失败！");
                        }
                        else
                        {
                            HashMap<String, Object> map = ret1.get(0);
                            if (map.containsKey("OfficalAdapter"))
                            {
                                HashMap<String, Object> officalMap = (HashMap<String, Object>)map.get("OfficalAdapter");
                                String retStr = (String)officalMap.get("ret");
                                String retMsgStr = (String)officalMap.get("retMsg");
                                String docStatus = (String)officalMap.get("docstatus");
                                
                                if (retStr != null && retStr.equals("0"))
                                {
                                    deleteDBData();
                                    mviewbacklistener.onViewBackListener(ConstState.SEND_SUCCESS);
                                    mUploadPopupWindow.dismiss();
                                    Toast.makeText(mActivity, "发送文件成功！", Toast.LENGTH_SHORT).show();
                                    
                                }
                                else if (retStr != null && retStr.equals("1"))
                                {
                                    mUploadPopupWindow.dismiss();
                                    
                                    if (docStatus != null && docStatus.equals("0"))
                                    {
                                        // dealSendFail("文件被锁定，无法发送！");
                                        final CustomDialog dialog =
                                            CustomDialog.newInstance("文件被锁定，无法发送！",
                                                getResources().getString(R.string.pdf_sendfail_Confirm));
                                        
                                        dialog.show(mActivity.getSupportFragmentManager(), "SendPDFDialog");
                                        dialog.setMidListener(new CustomDialogListener()
                                        {
                                            
                                            @Override
                                            public void Onclick()
                                            {
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                    else if (docStatus != null && docStatus.equals("1"))
                                    {
                                        // dealSendFail("文件有更新，需要更新！");
                                        
                                        final CustomDialog dialog =
                                            CustomDialog.newInstance("文件有更新，是否更新文件？",
                                                getResources().getString(R.string.pdf_sendfail_Cancel),
                                                getResources().getString(R.string.pdf_sendfail_Confirm));
                                        
                                        dialog.show(mActivity.getSupportFragmentManager(), "SendPDFDialog");
                                        dialog.setLeftListener(new CustomDialogListener()
                                        {
                                            
                                            @Override
                                            public void Onclick()
                                            {
                                                // TODO Auto-generated method stub
                                                dialog.dismiss();
                                                
                                            }
                                        });
                                        dialog.setRightListener(new CustomDialogListener()
                                        {
                                            
                                            @Override
                                            public void Onclick()
                                            {
                                                // TODO Auto-generated method stub
                                                dialog.dismiss();
                                                getFileContent(mFileId, mFilePath);
                                            }
                                        });
                                        
                                    }
                                    else
                                    {
                                        showMessage(retMsgStr);
                                    }
                                }
                                else
                                {
                                    showMessage(retMsgStr);
                                }
                            }
                        }
                    }
                    else
                    {
                        showMessage("发送文件失败！");
                    }
                    break;
                case RequestTypeConstants.PROGRESS_BAR:
                    // 返回进度值
                    if (mUploadfilePro != null)
                    {
                        mUploadfilePro.setProgress((Integer)objBody);
                    }
                    
                    if (mDownloadfilePro != null)
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
                case RequestTypeConstants.SENDDOCCONTENT:
                    mUploadPopupWindow.dismiss();
                    if (errorCode != ConstState.CANCEL_THREAD)
                    {
                        showMessage("发送文件失败！");
                    }
                    break;
                case RequestTypeConstants.GETFILECONTENT:
                    mDownloadPopupWindow.dismiss();
                    if (errorCode != ConstState.CANCEL_THREAD)
                    {
                        showMessage("下载文件失败！");
                    }
                    break;
                default:
            }
        }
    }
    
    public DocSendView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    public DocSendView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    public DocSendView(PMIPBaseActivity activity, int button_mid, Intent intent)
    {
        super(activity);
        // TODO Auto-generated constructor stub
        mActivity = activity;
        mButton_mid = button_mid;
        mIntent_value = intent;
        initView();
        initIntentValue(mIntent_value);
    }
    
    /**
     * 获取监听
     * 
     * @Description<功能详细描述>
     * 
     * @return
     * @LastModifiedDate：2013-11-12
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public onViewBackListen getMviewbacklistener()
    {
        return mviewbacklistener;
    }
    
    /**
     * 设置监听
     * 
     * @Description<功能详细描述>
     * 
     * @param mviewbacklistener
     * @LastModifiedDate：2013-11-12
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setMviewbacklistener(onViewBackListen mviewbacklistener)
    {
        this.mviewbacklistener = mviewbacklistener;
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
        LayoutInflater.from(mActivity).inflate(R.layout.docsend_layout, this, true);
        
        if (stepView == null)
        {
            stepView = new DocSendNextStepView(mActivity, mButton_mid, mIntent_value);
            stepView.setONNextStepListener(mlistener);
        }
        addView(stepView);
    }
    
    /**
     * 初始化文件属性
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void initIntentValue(Intent intent)
    {
        mFileId = intent.getStringExtra(ConstState.PDF_FILEMESSAGEID);
        mType = intent.getStringExtra(ConstState.PDF_TYPE);
        mFilePath = intent.getStringExtra(ConstState.PDF_PATH);
        mHandlerType = intent.getStringExtra(ConstState.PDF_HANDLETYPE);
        
        mFileName = intent.getStringExtra(ConstState.PDF_FILENAME) + ".pdf";
    }
    
    public void sendFile()
    {
        sendFileContent(mSendusersId, mOpinion, mStepId);
    }
    
    /**
     * 开始发送
     * 
     * @Description<功能详细描述>
     * 
     * @param sendusersId
     * @param opinion
     * @LastModifiedDate：2013-11-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void sendFileContent(String sendusersId, String opinion, String mStepId)
    {
        showUploadProgress();
        removeAllViews();
        
        try
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            
            JSONObject body = new JSONObject();
            body.put("docid", mFileId);
            body.put("nextpersonid", sendusersId);
            body.put("nextstepid", mStepId);
            body.put("opinion", opinion);
            body.put("type", mType);
            
            map.put(SendDocWrapRequest.BODY, body);
            
            List<FileInfo> infos = new ArrayList<FileInfo>();
            FileInfo info = new FileInfo();
            info.filepath = mFilePath;
            info.filename = mFileName;
            infos.add(info);
            map.put(SendDocWrapRequest.FILEINFOS, infos);
            
            mSendDocContentRequst = new HttpNetFactoryCreator(RequestTypeConstants.SENDDOCCONTENT).create();
            
            NetRequestController.sendDocFileRequest(mSendDocContentRequst,
                mHandler,
                RequestTypeConstants.SENDDOCCONTENT,
                map);
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    
    // 上传进度条的popView
    PopupWindow mUploadPopupWindow;
    
    ProgressBar mUploadfilePro;
    
    /**
     * 
     * 显示上传进度条
     * 
     * @LastModifiedDate：2013-10-9
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    private void showUploadProgress()
    {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.downloadfileprogress, null);
        
        // 输入框一样长，高度自适应
        mUploadPopupWindow =
            new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        
        mUploadPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        
        mUploadfilePro = (ProgressBar)view.findViewById(R.id.downloadprogress);
        
        mUploadfilePro.setProgress(0);
        
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
                
                if (mSendDocContentRequst != null)
                {
                    mSendDocContentRequst.shutDownExecute();
                }
                mUploadPopupWindow.dismiss();
                
            }
        });
        
        mUploadPopupWindow.setOnDismissListener(new OnDismissListener()
        {
            
            public void onDismiss()
            {
                
            }
        });
    }
    
    // 下载进度条的popView
    PopupWindow mDownloadPopupWindow;
    
    ProgressBar mDownloadfilePro;
    
    /**
     * 
     * 显示下载进度条
     * 
     * @LastModifiedDate：2013-10-9
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    private void showDownloadProgress()
    {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.downloadfileprogress, null);
        
        // 输入框一样长，高度自适应
        mDownloadPopupWindow =
            new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        
        mDownloadPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        
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
                mGetFileContentRequst.shutDownExecute();
                
                FileUtil.deleteFile(mFilePath);
                
                mDownloadPopupWindow.dismiss();
                
            }
        });
    }
    
    /**
     * 
     * 获取pdf文件
     * 
     * @Description 获取pdf文件
     * 
     * @LastModifiedDate：2013-10-9
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    private boolean getFileContent(String docid, String path)
    {
        if (!FileUtil.externalMemoryAvailable())
        {
            Toast.makeText(mActivity, "你的手机没有加载SDCard！", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (FileUtil.getAvailableExternalMemorySize() < 100)
        {
            Toast.makeText(mActivity, "你的SDCard空间已满，请释放你的SDCard空间！", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        showDownloadProgress();
        
        JSONObject body = new JSONObject();
        try
        {
            body.put("docid", docid);
            body.put("type", mType);
            
            mGetFileContentRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETFILECONTENT).create();
            
            NetRequestController.getFileContent(mGetFileContentRequst, mHandler, RequestTypeConstants.GETFILECONTENT,
            
            body, path);
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /**
     * 公文流转，删除数据库中的数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-14
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void deleteDBData()
    {
        String wheres =
            GetDocListInfo.l_user + "=?" + " and " + GetDocListInfo.l_type + "=?" + " and "
                + GetDocListInfo.l_handletype + "=?" + " and " + GetDocListInfo.docid + "=?";
        
        String[] selectionArgs = {GlobalState.getInstance().getOpenId(), mType, mHandlerType, mFileId};
        
        GetDocListInfo info = new GetDocListInfo();
        
        info.delete(wheres, selectionArgs);
    }
    
    private void showMessage(String message)
    {
        if (mUploadPopupWindow != null && mUploadPopupWindow.isShowing())
        {
            mUploadPopupWindow.dismiss();
        }
        
        if (mDownloadPopupWindow != null && mDownloadPopupWindow.isShowing())
        {
            mDownloadPopupWindow.dismiss();
        }
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }
    
}
