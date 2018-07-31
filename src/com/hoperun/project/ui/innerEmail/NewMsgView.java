/*
 * File name:  NewMsgView.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-2-18
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.innerEmail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import com.artifex.BaseUtils.EmailContactListen;
import com.artifex.BaseUtils.onViewBackListen;
import com.hoperun.manager.adpter.innerEmail.EmailAttachAdapter;
import com.hoperun.manager.components.CustomHandler.CustomHanler;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.exceptions.MIPException;
import com.hoperun.mip.netutils.baseNetEngine.NetParam.FileInfo;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.sqlUtils.SQLCreator.DBHandler;
import com.hoperun.mip.sqlUtils.Table.TFILEINFO;
import com.hoperun.mip.utils.MessageSQLIdConstants;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.mipmanager.model.entityMetaData.EmailAttachBody;
import com.hoperun.mipmanager.model.wrapRequest.SendInnerEmailWrapRequest;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.miplygphone.R;

/**
 * 
 * @Description 新建消息view
 * 
 * @author wang_ling
 * @Version [版本号, 2014-2-18]
 */
public class NewMsgView extends RelativeLayout implements OnClickListener
{
    /**
     * 应用上下文
     */
    private Context            mContext;
    
    /**
     * 距离底部的高度
     */
    private int                mMarginBottom = 0;
    
    /** 等待框布局 **/
    private RelativeLayout     loadLayout;
    
    /** 等待图片 **/
    private ImageView          loadImageView;
    
    /** 收件人 **/
    private TextView           receivePerson;
    
    /** 添加收件人按钮 **/
    private TextView           addReceiveBtn;
    
    /** 邮件标题 **/
    private EditText           titleEt;
    
    /** 邮件内容 **/
    private EditText           contentEt;
    
    /** 添加图库 **/
    // private RelativeLayout addPicRl;
    
    /** 添加附件 **/
    private RelativeLayout     addAttachRl;
    
    /** 附件列表 **/
    private GridView           attachGv;
    
    /** 发送按钮 **/
    private RelativeLayout     sendRl;
    
    /** 关闭按钮 **/
    private ImageView          closeIV;
    
    /** 添加联系人页面 **/
    private EmailLinkerView    mLinkerView;
    
    private EmailAttachAdapter emailAttachAdapter;
    
    /** 计时器 */
    private Timer              timer;
    
    /**
     * 该view的返回监听
     */
    private onViewBackListen   mviewbacklistener;
    
    public onViewBackListen getMviewbacklistener()
    {
        return mviewbacklistener;
    }
    
    public void setMviewbacklistener(onViewBackListen mviewbacklistener)
    {
        this.mviewbacklistener = mviewbacklistener;
    }
    
    /**
     * <默认构造函数>
     */
    public NewMsgView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    public NewMsgView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    public NewMsgView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    public NewMsgView(Context context, int marginBottom)
    {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
        mMarginBottom = marginBottom;
        initView();
        initData();
        initListener();
    }
    
    private void initView()
    {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(lp);
        
        setBackgroundResource(R.color.translucent);
        getBackground().setAlpha(90);
        
        LayoutInflater.from(mContext).inflate(R.layout.newmsg_layout, this, true);
        
        RelativeLayout mInfo_rl = (RelativeLayout)findViewById(R.id.newmsg_rl);
        
        LayoutParams mlp = (LayoutParams)mInfo_rl.getLayoutParams();
        
        // mlp.topMargin = mMarginBottom;
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
        
        receivePerson = (TextView)findViewById(R.id.receivePeople);
        
        addReceiveBtn = (TextView)findViewById(R.id.addReceive);
        titleEt = (EditText)findViewById(R.id.titleEt);
        
        contentEt = (EditText)findViewById(R.id.contentEt);
        // addPicRl = (RelativeLayout)findViewById(R.id.addPicRL);
        addAttachRl = (RelativeLayout)findViewById(R.id.addDocRL);
        attachGv = (GridView)findViewById(R.id.attachGV);
        closeIV = (ImageView)findViewById(R.id.closeIV);
        sendRl = (RelativeLayout)findViewById(R.id.pop_bottom_rl);
        
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        
    }
    
    private void initListener()
    {
        sendRl.setOnClickListener(this);
        closeIV.setOnClickListener(this);
        addReceiveBtn.setOnClickListener(this);
        // addPicRl.setOnClickListener(this);
        addAttachRl.setOnClickListener(this);
    }
    
    private List<EmailAttachBody> attachList;
    
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
        attachList = new ArrayList<EmailAttachBody>();
        emailAttachAdapter = new EmailAttachAdapter(mContext, attachList, true);
        attachGv.setAdapter(emailAttachAdapter);
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
        hideSoftKey();
        switch (v.getId())
        {
        // 点击关闭按钮，请求关闭
            case R.id.closeIV:
                if (mviewbacklistener != null)
                {
                    mviewbacklistener.onViewBackListener(ConstState.SEND_CANCEL);
                }
                break;
            case R.id.pop_bottom_rl:
                sendEmail();
                break;
            case R.id.addReceive:
                if (mLinkerView != null)
                {
                    this.removeView(mLinkerView);
                }
                mLinkerView = null;
                mLinkerView = new EmailLinkerView((Activity)mContext, 0);
                mLinkerView.setmOnfinishListener(emailLinkerListener);
                // mMsgView.setMviewbacklistener(mViewBacklisten);
                addView(mLinkerView);
                mLinkerView.bringToFront();
                mLinkerView.invalidate();
                break;
            // 添加图库
            // case R.id.addPicRL:
            // break;
            // 添加附件
            case R.id.addDocRL:
                
                if (maddAttachView != null)
                {
                    this.removeView(maddAttachView);
                }
                maddAttachView = null;
                maddAttachView = new AddAttachView((Activity)mContext, 0);
                maddAttachView.setmOnfinishListener(emailLinkerListener);
                // mMsgView.setMviewbacklistener(mViewBacklisten);
                addView(maddAttachView);
                maddAttachView.bringToFront();
                maddAttachView.invalidate();
                break;
            
            default:
                break;
        }
    }
    
    /** 接受人名字 **/
    private String        sendUserName        = "";
    
    /** 接收人名字 ***/
    private String        sendUserIds         = "";
    
    EmailContactListen    emailLinkerListener = new EmailContactListen()
                                              {
                                                  
                                                  @Override
                                                  public void onContactCancelListener()
                                                  {
                                                      if (mLinkerView != null)
                                                      {
                                                          removeView(mLinkerView);
                                                      }
                                                      
                                                      if (maddAttachView != null)
                                                      {
                                                          removeView(maddAttachView);
                                                      }
                                                      
                                                  }
                                                  
                                                  @Override
                                                  public void onContactSend(String senduserId, String sendusername)
                                                  {
                                                      if (mLinkerView != null)
                                                      {
                                                          removeView(mLinkerView);
                                                      }
                                                      sendUserName = sendusername;
                                                      sendUserIds = senduserId;
                                                      receivePerson.setText(sendUserName);
                                                  }
                                                  
                                                  @Override
                                                  public void onAttachBack(List<String> list)
                                                  {
                                                      // TODO Auto-generated method stub
                                                      if (maddAttachView != null)
                                                      {
                                                          removeView(maddAttachView);
                                                      }
                                                      attachPathList = list;
                                                      
                                                      for (String item : attachPathList)
                                                      {
                                                          EmailAttachBody body = new EmailAttachBody();
                                                          // FileDataSource fds = new FileDataSource(item);
                                                          body.setAttachtitle(new File(item).getName());
                                                          attachList.add(body);
                                                      }
                                                      emailAttachAdapter.setList(attachList);
                                                      emailAttachAdapter.notifyDataSetChanged();
                                                  }
                                                  
                                              };
    
    private List<String>  attachPathList      = new ArrayList<String>();
    
    private String        mFilePath           = "";
    
    private String        mFileName           = "";
    
    /** 添加附件页面 **/
    private AddAttachView maddAttachView;
    
    /**
     * 获取 maddAttachView
     * 
     * @return 返回 maddAttachView
     * @author wang_ling
     */
    public AddAttachView getMaddAttachView()
    {
        return maddAttachView;
    }
    
    /**
     * 设置 maddAttachView
     * 
     * @param maddAttachView 对maddAttachView进行赋值
     * @author wang_ling
     */
    public void setMaddAttachView(AddAttachView maddAttachView)
    {
        this.maddAttachView = maddAttachView;
    }
    
    /**
     * 获取 mLinkerView
     * 
     * @return 返回 mLinkerView
     * @author wang_ling
     */
    public EmailLinkerView getmLinkerView()
    {
        return mLinkerView;
    }
    
    /**
     * 设置 mLinkerView
     * 
     * @param mLinkerView 对mLinkerView进行赋值
     * @author wang_ling
     */
    public void setmLinkerView(EmailLinkerView mLinkerView)
    {
        this.mLinkerView = mLinkerView;
    }
    
    /**
     * 发送文件的Task
     */
    private NetTask mSendInnerEmailRequst;
    
    /**
     * 上传进度条的popView
     */
    PopupWindow     mUploadPopupWindow;
    
    /**
     * 上传进度
     */
    ProgressBar     mUploadfilePro;
    
    private void sendEmail()
    {
        
        String title = titleEt.getText().toString();
        String content = contentEt.getText().toString();
        // String receiveIds = receivePerson.getText().toString();
        if (sendUserIds.equals(""))
        {
            Toast.makeText(mContext, "收件人不能为空", Toast.LENGTH_LONG).show();
        }
        else if (title.equals(""))
        {
            Toast.makeText(mContext, "标题不能为空", Toast.LENGTH_LONG).show();
        }
        else
        {
            sendInnerEmail(title, content, "", sendUserIds);
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
            
            List<FileInfo> infos = new ArrayList<FileInfo>();
            
            for (int i = 0; i < attachPathList.size(); i++)
            {
                
                File fds = new File(attachPathList.get(i));
                FileInfo info = new FileInfo();
                info.filepath = attachPathList.get(i);
                
                String type = fileTYpe(attachPathList.get(i));
                if ("".equals(type))
                {
                    
                    if (attachPathList.get(i).lastIndexOf(".") != -1)
                    {
                        type = attachPathList.get(i).substring(attachPathList.get(i).lastIndexOf("."));
                    }
                    info.filename = fds.getName() + type;
                }
                else
                {
                    info.filename = fds.getName();
                }
                info.filetype = type;
                infos.add(info);
            }
            map.put(SendInnerEmailWrapRequest.FILEINFOS, infos);
            
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
     * 更换清除对话框
     */
    private Handler      dialogHandler = new Handler()
                                       {
                                           @Override
                                           public void handleMessage(Message msg)
                                           {
                                               switch (msg.what)
                                               {
                                                   case 0:// 成功
                                                   {
                                                       mPopupWindow.dismiss();
                                                       mviewbacklistener.onViewBackListener(ConstState.SEND_SUCCESS);
                                                       break;
                                                   }
                                                   case 1:// 失败
                                                       mPopupWindow.dismiss();
                                                       break;
                                                   default:
                                                       break;
                                               }
                                           }
                                       };
    
    /**
     * 网络处理handle
     */
    private CustomHanler mHandler      = new CustomHanler()
                                       {
                                           @Override
                                           public void PostHandle(int requestType, Object objHeader, Object objBody,
                                               boolean error, int errorCode)
                                           {
                                               // TODO Auto-generated method stub
                                               onPostHandle(requestType, objHeader, objBody, error, errorCode);
                                           }
                                           
                                       };
    
    int                  flag;
    
    TimerTask            task          = new TimerTask()
                                       {
                                           public void run()
                                           {
                                               timer.cancel();
                                               Message message = Message.obtain();
                                               message.what = flag;
                                               dialogHandler.sendMessage(message);
                                           }
                                       };
    
    @SuppressWarnings("unchecked")
    private void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.SENDDOCCONTENT:
                    mUploadPopupWindow.dismiss();
                    MetaResponseBody mSendResponseBuzBody = (MetaResponseBody)objBody;
                    if (mSendResponseBuzBody != null && mSendResponseBuzBody.getRetError().equals("0"))
                    {
                        // mviewbacklistener.onViewBackListener(ConstState.SEND_SUCCESS);
                        // Toast.makeText(mContext, "发送文件成功！", Toast.LENGTH_SHORT).show();
                        showSussessOrFail(true);
                        flag = 0;
                    }
                    else
                    {
                        flag = 1;
                        // showMessage("发送文件失败！");
                        showSussessOrFail(false);
                    }
                    timer = new Timer();
                    timer.schedule(task, 3000);
                    break;
                case RequestTypeConstants.PROGRESS_BAR:
                    // 返回进度值
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
    
    private void showMessage(String message)
    {
        if (mUploadPopupWindow != null && mUploadPopupWindow.isShowing())
        {
            mUploadPopupWindow.dismiss();
        }
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 隐藏键盘
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-12-5
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected void hideSoftKey()
    {
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        
        if (contentEt != null)
        {
            imm.hideSoftInputFromWindow(contentEt.getWindowToken(), 0);
        }
        if (titleEt != null)
        {
            imm.hideSoftInputFromWindow(titleEt.getWindowToken(), 0);
        }
    }
    
    private PopupWindow mPopupWindow;
    
    public void showSussessOrFail(Boolean flag)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.messagetip_layout, null);
        mPopupWindow =
            new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView persontv = (TextView)view.findViewById(R.id.rightTV);
        ImageView flagIV = (ImageView)view.findViewById(R.id.flagIV);
        TextView flagTV = (TextView)view.findViewById(R.id.flagTv);
        TextView tiptv = (TextView)view.findViewById(R.id.tip2);
        persontv.setText(sendUserName);
        if (flag)
        {// 成功
            flagIV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.message_6));
            flagTV.setText("发送成功");
            flagTV.setTextColor(mContext.getResources().getColor(R.color.green));
            tiptv.setText("如果发送失败，这里的文字提示显示“发送失败”，并配有发送失败的icon");
        }
        else
        {
            flagIV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.message_7));
            flagTV.setText("发送失败");
            flagTV.setTextColor(mContext.getResources().getColor(R.color.red));
            tiptv.setText("如果发送成功，这里的文字提示显示“发送成功”，并配有发送成功的icon");
        }
        // mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setContentView(view);
        mPopupWindow.showAtLocation(this, Gravity.CENTER, 0, 0);
        //
        // mPopupWindow.showAtLocation(docAddLL, Gravity.LEFT | Gravity.BOTTOM, docAddLL.getLeft(),
        // docAddLL.getHeight());// Gravity.NO_GRAVITY
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
            // mCursor =
            // hander.query(MessageSQLIdConstants.DB_MESSAGE_FILEINFO, null, TFILEINFO.FILENAME + "=? and "
            // + TFILEINFO.OPENID + "=?", new String[] {new File(filePath).getName(),
            // GlobalState.getInstance().getOpenId()}, null);
            
            mCursor =
                hander.query(MessageSQLIdConstants.DB_MESSAGE_FILEINFO,
                    null,
                    TFILEINFO.FILENAME + "=?",
                    new String[] {new File(filePath).getName()},
                    null);
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
