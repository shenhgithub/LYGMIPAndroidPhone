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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.BaseUtils.onViewBackListen;
import com.hoperun.manager.components.CustomHandler.CustomHanler;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.mipmanager.model.entityMetaData.GetDocInfoResponseBody;
import com.hoperun.mipmanager.model.wrapRequest.GetDocRecordInfoWrapRequest;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.miplygphone.R;

/**
 * 显示信息页面
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-1-28]
 */
public class InfoView extends RelativeLayout implements OnClickListener
{
    private Context                      mContext;
    
    /**
     * 文件ID
     */
    private String                       mFileId;
    
    /**
     * 文件type : 收文/发文
     */
    private String                       mType;
    
    /**
     * 文件标题
     */
    private String                       mTitle;
    
    /**
     * 列表view
     */
    private ListView                     mListView;
    
    /**
     * 没有信息的布局
     */
    private LinearLayout                 mPdfInfoNoMessage;
    
    /**
     * 该view 的宽度
     */
    private final int                    mViewWith    = 720;
    
    // /**
    // * 该view 的高度
    // */
    // private final int mViewHeight = 520;
    //
    // // 距离底部的高度
    // private int mMarginBottom = 0;
    
    /** 等待框布局 **/
    private RelativeLayout               loadLayout;
    
    /** 等待图片 **/
    private ImageView                    loadImageView;
    
    /**
     * 该view的返回监听
     */
    private onViewBackListen             mviewbacklistener;
    
    /**
     * 返回的信息记录数据
     */
    private List<GetDocInfoResponseBody> mRecordLists = new ArrayList<GetDocInfoResponseBody>();
    
    /**
     * 列表view 的adapter
     */
    private DocInfoViewAdapter           mInfoAdapter;
    
    /**
     * 获取“信息”的task
     */
    private NetTask                      mGetDocrecordInfo;
    
    /**
     * 网络返回handler
     */
    private CustomHanler                 mhandHanler  = new CustomHanler()
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
        closeProgressDialog();
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETDOCRECORDINFO:
                    
                    MetaResponseBody getContentBuzBody = (MetaResponseBody)objBody;
                    if (getContentBuzBody.getRetError().equals("0"))
                    {
                        List<HashMap<String, Object>> ret1 = getContentBuzBody.getBuzList();
                        dealGetDocInfoData(ret1);
                        
                        if (mRecordLists != null && mRecordLists.size() > 0)
                        {
                            mInfoAdapter.setmLists(mRecordLists);
                            mInfoAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            mPdfInfoNoMessage.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        mPdfInfoNoMessage.setVisibility(View.VISIBLE);
                    }
                    
                    break;
            }
        }
    }
    
    public InfoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    public InfoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    public InfoView(Context context, Intent intent, int marginBottom)
    {
        super(context);
        // TODO Auto-generated constructor stub
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
            mFileId = intent.getStringExtra(ConstState.PDF_FILEMESSAGEID);
            mType = intent.getStringExtra(ConstState.PDF_TYPE);
            mTitle = intent.getStringExtra(ConstState.PDF_FILENAME);
        }
        
        if (mFileId == null)
        {
            mFileId = "";
        }
        
        if (mType == null)
        {
            mType = "";
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
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(lp);
        
        setBackgroundResource(R.color.translucent);
        getBackground().setAlpha(90);
        
        LayoutInflater.from(mContext).inflate(R.layout.info_layout, this, true);
        
        TextView tv = (TextView)findViewById(R.id.title);
        
        tv.setText(mTitle);
        
        mListView = (ListView)findViewById(R.id.pdf_info_list);
        
        mPdfInfoNoMessage = (LinearLayout)findViewById(R.id.pdf_info_no_message);
        
        RelativeLayout mInfo_rl = (RelativeLayout)findViewById(R.id.info_rl);
        
        // LayoutParams mlp = (LayoutParams)mInfo_rl.getLayoutParams();
        //
        // mlp.topMargin = mMarginBottom;
        // mlp.bottomMargin = mMarginBottom;
        //
        // mInfo_rl.setLayoutParams(mlp);
        
        // 设置该方法的目的是，在该布局内部点击之后，不把点击事件向上抛送，在最上层的view点击的效果是让该view消失
        mInfo_rl.setOnTouchListener(new OnTouchListener()
        {
            
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                // TODO Auto-generated method stub
                return false;
            }
        });
        
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
     * @LastModifiedDate：2013-11-12
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        if (GlobalState.getInstance().getOfflineLogin())
        {
            Toast.makeText(mContext, "网络没有连接！", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if ((mRecordLists == null || mRecordLists.size() == 0) && loadLayout.getVisibility() != View.VISIBLE)
            {
                getDocRecordInfo();
                showProgressDialog();
            }
        }
        
        mInfoAdapter = new DocInfoViewAdapter(mContext, mRecordLists);
        mListView.setAdapter(mInfoAdapter);
    }
    
    /**
     * 向服务器请求，该文档的历史记录
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-12
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void getDocRecordInfo()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(GetDocRecordInfoWrapRequest.DOCID, mFileId);
        map.put(GetDocRecordInfoWrapRequest.TYPE, mType);
        
        mGetDocrecordInfo = new HttpNetFactoryCreator(RequestTypeConstants.GETDOCRECORDINFO).create();
        NetRequestController.getDocRecordInfo(mGetDocrecordInfo,
            mhandHanler,
            RequestTypeConstants.GETDOCRECORDINFO,
            map);
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
            case R.id.pop_bottom_rl:
                if (mviewbacklistener != null)
                {
                    mviewbacklistener.onViewBackListener(ConstState.SEND_CANCEL);
                }
                break;
            
            default:
                break;
        }
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
     * 处理网络返回的数据
     * 
     * @Description<功能详细描述>
     * 
     * @param lists
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void dealGetDocInfoData(List<HashMap<String, Object>> lists)
    {
        if (lists != null && lists.size() > 0)
        {
            for (int i = 0; i < lists.size(); i++)
            {
                GetDocInfoResponseBody body = new GetDocInfoResponseBody();
                body.convertToObject(lists.get(i));
                
                mRecordLists.add(body);
            }
        }
        
        Collections.sort(mRecordLists, new RecordListDateComparator());
    }
    
    /**
     * 
     * 比较器
     * 
     * @Description 比较器
     * 
     * @author wang_ling
     * @Version [版本号, 2013-10-18]
     */
    class RecordListDateComparator implements Comparator<GetDocInfoResponseBody>
    {
        /**
         * 比较器方法 按照里面的 按日期顺排序
         */
        @Override
        public int compare(GetDocInfoResponseBody lhs, GetDocInfoResponseBody rhs)
        {
            return (lhs.getStringKeyValue(GetDocInfoResponseBody.operatdate).compareToIgnoreCase(rhs.getStringKeyValue(GetDocInfoResponseBody.operatdate)));
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        return true;
    }
    
}
