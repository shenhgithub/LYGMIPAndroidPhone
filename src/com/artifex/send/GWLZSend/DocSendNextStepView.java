/*
 * File name:  SendNextStepView.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-10-16
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.send.GWLZSend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.artifex.BaseUtils.NextStepListen;
import com.hoperun.manager.components.CustomHandler.CustomHanler;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mipmanager.model.entityMetaData.PDFDtailInfo;
import com.hoperun.mipmanager.model.entityMetaData.PDFNextStepInfo;
import com.hoperun.mipmanager.model.wrapRequest.GetPdfNextStepWrapRequest;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.miplygphone.R;

/**
 * 下一步步骤view
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-10-16]
 */
@SuppressLint("HandlerLeak")
public class DocSendNextStepView extends RelativeLayout implements OnClickListener
{
    private Context                     mContext;
    
    /**
     * 文件id
     */
    private String                      mFileId;
    
    /**
     * 文件类型，收文/发文
     */
    private String                      mtype;
    
    /**
     * 文件待办/已办
     */
    private String                      mHandleType;
    
    /**
     * "发送"按钮的位置
     */
    private int                         mButton_mid;
    
    /**
     * 下一步步骤listview
     */
    private ListView                    mlistView;
    
    /**
     * 下一步步骤监听
     */
    private NextStepListen              listen;
    
    /**
     * 报文返回的 数据源
     */
    private List<PDFNextStepInfo>       mPdfNextStepinfos      = null;
    
    /**
     * adapter中的listview数据
     */
    private List<PDFNextStepListEntity> mPDFNextStepListEntity = new ArrayList<PDFNextStepListEntity>();
    
    /** 等待框布局 **/
    private RelativeLayout              loadLayout;
    
    /** 等待图片 **/
    private ImageView                   loadImageView;
    
    /**
     * 宽
     */
    private final int                   mViewWith              = 320;
    
    /**
     * 高
     */
    private final int                   mViewHeight            = 400;
    
    /**
     * 处理网络返回的handler
     */
    private CustomHanler                mHandler               = new CustomHanler()
                                                               {
                                                                   
                                                                   @Override
                                                                   public void PostHandle(int requestType,
                                                                       Object objHeader, Object objBody, boolean error,
                                                                       int errorCode)
                                                                   {
                                                                       // TODO Auto-generated method stub
                                                                       onPostHandle(requestType,
                                                                           objHeader,
                                                                           objBody,
                                                                           error,
                                                                           errorCode);
                                                                   }
                                                                   
                                                               };
    
    private void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        closeProgressDialog();
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETNEXTSTEP:
                    MetaResponseBody mGetNextStepResponseBuzBody = (MetaResponseBody)objBody;
                    List<HashMap<String, Object>> ret = mGetNextStepResponseBuzBody.getBuzList();
                    dealBackData(ret);
                    break;
                default:
            }
        }
        else
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETNEXTSTEP:
                    Toast.makeText(mContext, "数据返回错误！", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    }
    
    /**
     * 处理完了返回数据
     * 
     * @Description<功能详细描述>
     * 
     * @param ret
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void dealBackData(List<HashMap<String, Object>> ret)
    {
        if (ret == null || ret.size() == 0)
        {// 没有数据
            Toast.makeText(mContext, "没有处理步骤", Toast.LENGTH_SHORT).show();
        }
        else
        {
            LogUtil.d("size", ret.size() + "");
            
            PDFDtailInfo pdfDetailInfo = new PDFDtailInfo("");
            pdfDetailInfo.convertToObject(ret.get(0));
            
            mPdfNextStepinfos = pdfDetailInfo.getNextStepInfos();
            for (int i = 0; i < mPdfNextStepinfos.size(); i++)
            {
                PDFNextStepListEntity info = new PDFNextStepListEntity();
                info.setmNextStepGuid(mPdfNextStepinfos.get(i).getStepguid());
                info.setmNextStepName(mPdfNextStepinfos.get(i).getStepname());
                
                mPDFNextStepListEntity.add(info);
            }
            
            DocSendNextStepAdapter adapter = new DocSendNextStepAdapter(mContext, mPDFNextStepListEntity);
            mlistView.setAdapter(adapter);
        }
    }
    
    public DocSendNextStepView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        mContext = context;
        initView();
        initData();
    }
    
    public DocSendNextStepView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        initView();
        initData();
    }
    
    public DocSendNextStepView(Context context, int button_mid, Intent intent)
    {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
        mButton_mid = button_mid;
        initIntentData(intent);
        initView();
        initData();
    }
    
    /**
     * 初始化view
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void initView()
    {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(lp);
        
        setBackgroundResource(R.color.translucent);
        getBackground().setAlpha(90);
        
        // 设置该方法的目的是，在该布局内部点击之后，不把点击事件向上抛送，在最上层的view点击的效果是让该view消失
        LayoutInflater.from(mContext).inflate(R.layout.pdfsendnextstep, this, true);
        
        RelativeLayout pdfSendNextView_rl = (RelativeLayout)findViewById(R.id.pdfsendnext);
        
        pdfSendNextView_rl.setOnTouchListener(new OnTouchListener()
        {
            
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                // TODO Auto-generated method stub
                return true;
            }
        });
        
        // LayoutParams mlp = (LayoutParams)pdfSendNextView_rl.getLayoutParams();
        // mlp = (LayoutParams)DocUtils.justViewRelativeLayout(mlp, mButton_mid, mViewHeight, mViewWith);
        // pdfSendNextView_rl.setLayoutParams(mlp);
        
        mlistView = (ListView)findViewById(R.id.pdfsendnextsteplist);
        RelativeLayout cancelRl = (RelativeLayout)findViewById(R.id.pop_bottom_rl);
        
        loadLayout = (RelativeLayout)findViewById(R.id.load_layout);
        loadImageView = (ImageView)findViewById(R.id.waitdialog_img);
        
        cancelRl.setOnClickListener(this);
    }
    
    /**
     * 初始化数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        if (GlobalState.getInstance().getOfflineLogin())
        {
            Toast.makeText(mContext, "网络没有连接！", Toast.LENGTH_SHORT).show();
            return;
        }
        getPdfNextStep(mFileId, mtype, mHandleType);
        mlistView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                // TODO Auto-generated method stub
                PDFNextStepInfo setpInfo = mPdfNextStepinfos.get(arg2);
                
                String mStepId = setpInfo.getStringKeyValue(PDFNextStepInfo.F_stepid);
                
                String choosenextpersion = setpInfo.getStringKeyValue(PDFNextStepInfo.F_choosenextpersion);
                
                if (choosenextpersion.equals("1"))
                {
                    listen.onSendFile("", mStepId);
                }
                else
                {
                    if (listen != null)
                    {
                        listen.onNextStepListener(setpInfo);
                    }
                }
                
            }
            
        });
    }
    
    /**
     * 向服务器发出请求获取下一步步骤的请求
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void getPdfNextStep(String messageId, String type, String handleType)
    {
        if (messageId != null && handleType != null && (mPdfNextStepinfos == null || mPdfNextStepinfos.size() == 0))
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(GetPdfNextStepWrapRequest.DOCID, messageId);
            map.put(GetPdfNextStepWrapRequest.TYPE, type);
            map.put(GetPdfNextStepWrapRequest.HANDLETYPE, handleType);
            
            NetRequestController.getSendNextStep(mHandler, RequestTypeConstants.GETNEXTSTEP, map);
            showProgressDialog();
        }
    }
    
    public void setONNextStepListener(NextStepListen l)
    {
        this.listen = l;
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.pop_bottom_rl:
                if (listen != null)
                {
                    // 取消该view
                    listen.onCancelListener();
                }
                break;
            
            default:
                break;
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
     * 初始化文件属性
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void initIntentData(Intent intent)
    {
        if (intent != null)
        {
            mFileId = intent.getStringExtra(ConstState.PDF_FILEMESSAGEID);
            mtype = intent.getStringExtra(ConstState.PDF_TYPE);
            mHandleType = intent.getStringExtra(ConstState.PDF_HANDLETYPE);
        }
        
        if (mFileId == null)
        {
            mFileId = "";
        }
        
        if (mHandleType == null)
        {
            mHandleType = "";
        }
    }
    
}
