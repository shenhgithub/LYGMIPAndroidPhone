/*
 * File name:  DetailInfoView.java
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
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.BaseUtils.onEmailDetailBackListen;
import com.hoperun.manager.adpter.innerEmail.EmailAttachAdapter;
import com.hoperun.manager.components.CustomHandler.CustomHanler;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.FileUtil;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.model.entityMetaData.EmailAttachBody;
import com.hoperun.mipmanager.model.entityMetaData.EmailReceiverBody;
import com.hoperun.mipmanager.model.entityMetaData.GetInnerEmailListInfo;
import com.hoperun.mipmanager.model.wrapRequest.SendInnerEmailWrapRequest;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.miplygphone.R;

/**
 * 具体的信息页面
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-2-19]
 */
public class DetailInfoView extends RelativeLayout implements OnClickListener, OnItemClickListener
{
    /**
     * 文件下载路径
     */
    private String                  mFilePath;
    
    /**
     * 应用上下文
     */
    private Context                 mContext;
    
    /**
     * 用户名
     */
    private String                  user          = "";
    
    /** 是否离线 true为离线，false为在线 **/
    protected boolean               offLine       = true;
    
    /**
     * 距离底部的高度
     */
    private int                     mMarginBottom = 0;
    
    private GetInnerEmailListInfo   mInfo;
    
    private String                  mType;
    
    private TextView                titleTV;
    
    private ImageView               closeIV;
    
    private TextView                contentTV;
    
    private TextView                sendPersonTV;
    
    private TextView                sendTimeTV;
    
    private GridView                attachGV;
    
    private EditText                replayET;
    
    private RelativeLayout          replayRL;
    
    private RelativeLayout          replyBtn;
    
    /** 等待框布局 **/
    private RelativeLayout          loadLayout;
    
    /** 等待图片 **/
    private ImageView               loadImageView;
    
    /**
     * 该view的返回监听
     */
    private onEmailDetailBackListen mviewbacklistener;
    
    private EmailAttachAdapter      emailAttachAdapter;
    
    /**
     * 获取文件的Task
     */
    private NetTask                 mGetAttachRequst;
    
    /**
     * 发送文件的Task
     */
    private NetTask                 mSendInnerEmailRequst;
    
    /**
     * <默认构造函数>
     */
    public DetailInfoView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    public onEmailDetailBackListen getMviewbacklistener()
    {
        return mviewbacklistener;
    }
    
    public void setMviewbacklistener(onEmailDetailBackListen mviewbacklistener)
    {
        this.mviewbacklistener = mviewbacklistener;
    }
    
    public DetailInfoView(Context context, int marginBottom, GetInnerEmailListInfo info, String type, String parentPath)
    {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
        mMarginBottom = marginBottom;
        mInfo = info;
        mType = type;
        mFilePath = parentPath;
        initView();
        initData();
        initListener();
    }
    
    /**
     * 
     * 初始化页面
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-2-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initView()
    {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(lp);
        
        setBackgroundResource(R.color.translucent);
        getBackground().setAlpha(90);
        
        LayoutInflater.from(mContext).inflate(R.layout.detail_info_layout, this, true);
        
        RelativeLayout mInfo_rl = (RelativeLayout)findViewById(R.id.detail_info_rl);
        
        LayoutParams mlp = (LayoutParams)mInfo_rl.getLayoutParams();
        
        // mlp.topMargin = -mMarginBottom;
        // mlp.bottomMargin = mMarginBottom;
        
        mInfo_rl.setLayoutParams(mlp);
        // 设置该方法的目的是，在该布局内部点击之后，不把点击事件向上抛送，在最上层的view点击的效果是让该view消失
        mInfo_rl.setOnTouchListener(new OnTouchListener()
        {
            
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return false;
            }
        });
        titleTV = (TextView)findViewById(R.id.title);
        closeIV = (ImageView)findViewById(R.id.closeIV);
        
        contentTV = (TextView)findViewById(R.id.receiveContent);
        
        sendPersonTV = (TextView)findViewById(R.id.sendpeople);
        
        sendTimeTV = (TextView)findViewById(R.id.time);
        
        attachGV = (GridView)findViewById(R.id.attachGV);
        
        replayET = (EditText)findViewById(R.id.replayTv);
        
        replayRL = (RelativeLayout)findViewById(R.id.replay_rl);
        
        replyBtn = (RelativeLayout)findViewById(R.id.pop_bottom_rl);
        
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        // 收件箱
        if ("1".equals(mType))
        {
            replayRL.setVisibility(View.VISIBLE);
            replyBtn.setVisibility(View.VISIBLE);
        }
        // 发件箱
        else if ("2".equals(mType))
        {
            replayRL.setVisibility(View.INVISIBLE);
            replyBtn.setVisibility(View.GONE);
        }
        
    }
    
    private void initListener()
    {
        closeIV.setOnClickListener(this);
        replyBtn.setOnClickListener(this);
        attachGV.setOnItemClickListener(this);
    }
    
    /**
     * 附件列表
     */
    private List<EmailAttachBody>   attachList;
    
    /**
     * 接收人列表
     */
    private List<EmailReceiverBody> receiverList;
    
    /**
     * 收件人姓名
     */
    private String                  receiverName = "";
    
    /**
     * 收件人id
     */
    private String                  receiverIds  = "";
    
    /**
     * 标题
     */
    private String                  titleString  = "";
    
    private String                  msgID        = "";
    
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
        user = GlobalState.getInstance().getOpenId();
        offLine = GlobalState.getInstance().getOfflineLogin();
        attachList = new ArrayList<EmailAttachBody>();
        attachList = mInfo.getAttachment();
        emailAttachAdapter = new EmailAttachAdapter(mContext, attachList, false);
        attachGV.setAdapter(emailAttachAdapter);
        
        receiverList = new ArrayList<EmailReceiverBody>();
        receiverList = mInfo.getReceivers();
        titleString = (String)mInfo.getValue(GetInnerEmailListInfo.msgtitle);
        receiverName = (String)mInfo.getValue(GetInnerEmailListInfo.createusername);
        receiverIds = (String)mInfo.getValue(GetInnerEmailListInfo.createuserid);
        msgID = (String)mInfo.getValue(GetInnerEmailListInfo.msgid);
        titleTV.setText(titleString);
        contentTV.setText((String)mInfo.getValue(GetInnerEmailListInfo.msgcontent));
        sendPersonTV.setText(receiverName);
        sendTimeTV.setText((String)mInfo.getValue(GetInnerEmailListInfo.createtime));
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
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        switch (v.getId())
        {
        // 点击关闭按钮，请求关闭
            case R.id.closeIV:
                if (mviewbacklistener != null)
                {
                    // mviewbacklistener.onViewBackListener(ConstState.SEND_CANCEL);
                    mviewbacklistener.onViewBackCancel();
                }
                break;
            // 回复
            case R.id.pop_bottom_rl:
                sendEmail();
                break;
            default:
                break;
        }
        
    }
    
    private EmailAttachBody selectedAttach;
    
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
        selectedAttach = (EmailAttachBody)emailAttachAdapter.getItem(arg2);
        String url = selectedAttach.getAttachurl();
        String mSelectedPath = DBDataObjectWrite.getFilePath(user, selectedAttach.getAttachid(), ConstState.NBYJ);
        
        File file = new File(mSelectedPath);
        
        if (offLine)
        {
            
            if (file.exists())
            {
                // openPDFFile();
                Toast.makeText(mContext, "本地已有！", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(mContext, "本地不存在该文件！", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if (file.exists())
            {
                // openPDFFile();
                Toast.makeText(mContext, "本地已有！", Toast.LENGTH_SHORT).show();
            }
            else
            {
                getAttach(url, mFilePath);
            }
            
        }
        
    }
    
    /**
     * 
     * 下载附件
     * 
     * @Description<功能详细描述>
     * 
     * @param attachurl
     * @return
     * @LastModifiedDate：2014-2-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private boolean getAttach(String attachurl, String path)
    {
        if (!FileUtil.externalMemoryAvailable())
        {
            Toast.makeText(mContext, "你的手机没有加载SDCard！", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (FileUtil.getAvailableExternalMemorySize() < 100)
        {
            Toast.makeText(mContext, "你的SDCard空间已满，请释放你的SDCard空间！", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        showDownloadProgress();
        
        JSONObject body = new JSONObject();
        try
        {
            body.put("attachurl", attachurl);
            
            mGetAttachRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETINNEREMAILATTACH).create();
            
            NetRequestController.getInnerEmailAttach(mGetAttachRequst,
                mHandler,
                RequestTypeConstants.GETINNEREMAILATTACH,
                
                body,
                path);
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /**
     * 网络处理handle
     */
    private CustomHanler mHandler = new CustomHanler()
                                  {
                                      @Override
                                      public void PostHandle(int requestType, Object objHeader, Object objBody,
                                          boolean error, int errorCode)
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
                case RequestTypeConstants.GETINNEREMAILATTACH:
                    MetaResponseBody getContentBuzBody = (MetaResponseBody)objBody;
                    if (getContentBuzBody.getRetError().equals("0"))
                    {
                        showMessage("下载文件成功！");
                        
                        List<HashMap<String, Object>> buzList = getContentBuzBody.getBuzList();
                        
                        if (buzList != null && buzList.size() > 0)
                        {
                            // saveFileInfo(buzList, user, "", "", mSelectedDocId, mSelectedFileTitle, ConstState.LDPS);
                            // mviewbacklistener.onViewBackListener(ConstState.SEND_SUCCESS);
                            String msgId = selectedAttach.getAttachid();
                            String msgTitle = selectedAttach.getAttachtitle();
                            mviewbacklistener.onViewBackItemClick(buzList, msgId, msgTitle);
                        }
                        
                    }
                    else
                    {
                        showMessage("下载文件失败！");
                    }
                    break;
                
                case RequestTypeConstants.SENDDOCCONTENT:
                    mUploadPopupWindow.dismiss();
                    MetaResponseBody mSendResponseBuzBody = (MetaResponseBody)objBody;
                    if (mSendResponseBuzBody != null && mSendResponseBuzBody.getRetError().equals("0"))
                    {
                        Toast.makeText(mContext, "发送文件成功！", Toast.LENGTH_SHORT).show();
                        mviewbacklistener.onViewBackCancel();
                    }
                    else
                    {
                        showMessage("发送文件失败！");
                    }
                    break;
                case RequestTypeConstants.PROGRESS_BAR:
                    // 返回进度值
                    
                    if (mDownloadfilePro != null)
                    {
                        mDownloadfilePro.setProgress((Integer)objBody);
                    }
                    
                    if (mUploadfilePro != null)
                    {
                        mUploadfilePro.setProgress((Integer)objBody);
                    }
                    break;
                default:
            }
        }
        else
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETINNEREMAILATTACH:
                    mDownloadPopupWindow.dismiss();
                    if (errorCode != ConstState.CANCEL_THREAD)
                    {
                        showMessage("下载文件失败！");
                    }
                    break;
                case RequestTypeConstants.SENDDOCCONTENT:
                    mUploadPopupWindow.dismiss();
                    if (errorCode != ConstState.CANCEL_THREAD)
                    {
                        showMessage("发送文件失败！");
                    }
                    break;
                default:
            }
        }
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
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 下载进度条的popView
     */
    PopupWindow mDownloadPopupWindow;
    
    /**
     * 下载进度
     */
    ProgressBar mDownloadfilePro;
    
    /**
     * 上传进度条的popView
     */
    PopupWindow mUploadPopupWindow;
    
    /**
     * 上传进度
     */
    ProgressBar mUploadfilePro;
    
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.downloadfileprogress, null);
        
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
                mGetAttachRequst.shutDownExecute();
                
                FileUtil.deleteFile(mFilePath);
                
                mDownloadPopupWindow.dismiss();
                
            }
        });
    }
    
    private void sendEmail()
    {
        String content = replayET.getText().toString();
        if (receiverIds.equals(""))
        {
            Toast.makeText(mContext, "收件人不能为空", Toast.LENGTH_LONG).show();
        }
        else if (titleString.equals(""))
        {
            Toast.makeText(mContext, "标题不能为空", Toast.LENGTH_LONG).show();
        }
        else if ("".equals(content))
        {
            Toast.makeText(mContext, "请输入回复内容", Toast.LENGTH_LONG).show();
        }
        else
        {
            sendInnerEmail(titleString, content, msgID, receiverIds);
        }
    }
    
    /**
     * 
     * 发送内部邮件
     * 
     * @Description<功能详细描述>
     * 
     * @param msgtitle
     * @param msgcontent
     * @param replaymsgid
     * @param receiverids
     * @LastModifiedDate：2014-2-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void sendInnerEmail(String msgtitle, String msgcontent, String replaymsgid, String receiverids)
    {
        showUploadProgress();
        
        try
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            
            JSONObject body = new JSONObject();
            body.put("msgtitle", msgtitle);
            body.put("msgcontent", msgcontent);
            body.put("replaymsgid", replaymsgid);
            body.put("receiverids", receiverids);
            
            map.put(SendInnerEmailWrapRequest.BODY, body);
            
            mSendInnerEmailRequst = new HttpNetFactoryCreator(RequestTypeConstants.SENDDOCCONTENT).create();
            
            NetRequestController.sendInnerEmailRequest(mSendInnerEmailRequst,
                mHandler,
                RequestTypeConstants.SENDDOCCONTENT,
                map);
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * 显示上传进度条
     * 
     * @LastModifiedDate：2013-10-9
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    @SuppressWarnings("unused")
    private void showUploadProgress()
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.downloadfileprogress, null);
        
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
                
                if (mSendInnerEmailRequst != null)
                {
                    mSendInnerEmailRequst.shutDownExecute();
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
    
}
