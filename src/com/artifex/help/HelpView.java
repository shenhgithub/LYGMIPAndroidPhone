/*
 * File name:  HelpView.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-11-29
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.help;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.hoperun.miplygphone.R;

/**
 * 帮助页面
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-11-29]
 */
public class HelpView extends RelativeLayout
{
    
    /**
     * pdf帮助字段
     */
    private final static String   PDF_HELPSKIP      = "pdfhelpskip";
    
    /**
     * pdf签批时帮助字段
     */
    private final static String   PDF_SIGNHELPSKIP  = "pdfsignhelpskip";
    
    /**
     * 上下文
     */
    Context                       mContext;
    
    /**
     * 帮助页面上的按钮
     */
    private ImageButton           mHelpSkip;
    
    /**
     * 帮助页面上知道提示控件
     */
    private RelativeLayout        mHelpKnow;
    
    /**
     * 帮助页面上的按钮
     */
    private RelativeLayout        mPdfHelpLayout;
    
    /**
     * 是否是点击帮助按钮出现该页面 false 不是， true 是
     */
    private boolean               isClickHelpButton = false;
    
    /**
     * 是否是签批时的帮助，false 不是， true 是
     */
    private boolean               isSignHelp        = false;
    
    /**
     * pdf阅读是否跳过
     * 
     */
    private boolean               isPdfHelpSkip     = false;
    
    /**
     * 签批帮助是否跳过
     */
    private boolean               isPdfSignHelpSkip = false;
    
    private onCloseThisViewListen listener;
    
    /**
     * 点击按钮监听
     */
    private OnClickListener       helpLisener       = new OnClickListener()
                                                    {
                                                        
                                                        @Override
                                                        public void onClick(View v)
                                                        {
                                                            // TODO Auto-generated method stub
                                                            switch (v.getId())
                                                            {
                                                                case R.id.pdf_help_skip:
                                                                    HelpSkipClick();
                                                                    break;
                                                                case R.id.pdf_help_ok:
                                                                    HelpKnowClick();
                                                                    break;
                                                                default:
                                                                    break;
                                                            }
                                                            
                                                        }
                                                    };
    
    public HelpView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    public HelpView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    public HelpView(Context context, boolean isClickButton, boolean isSigned, onCloseThisViewListen listen)
    {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
        isClickHelpButton = isClickButton;
        isSignHelp = isSigned;
        listener = listen;
        initView();
        initData();
        
        if (!isClickHelpButton)
        {
            initShowHelp();
        }
        else
        {
            ShowHelp();
        }
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
        LayoutInflater.from(mContext).inflate(R.layout.pdf_help, this, true);
        
        mPdfHelpLayout = (RelativeLayout)findViewById(R.id.pdf_help_rlayout);
        
        mHelpSkip = (ImageButton)findViewById(R.id.pdf_help_skip);
        mHelpKnow = (RelativeLayout)findViewById(R.id.pdf_help_ok);
        
        mHelpSkip.setOnClickListener(helpLisener);
        mHelpKnow.setOnClickListener(helpLisener);
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
        SharedPreferences share = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        
        isPdfHelpSkip = share.getBoolean(PDF_HELPSKIP, false);
        isPdfSignHelpSkip = share.getBoolean(PDF_SIGNHELPSKIP, false);
    }
    
    /**
     * 初始化，打开pdf时或者点击签批时如何显示帮助提示
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-9
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void initShowHelp()
    {
        if (isSignHelp)
        {
            if (!isPdfSignHelpSkip)
            {
                ShowHelp();
            }
            else
            {
                if (listener != null)
                {
                    listener.onCloseThisViewListener();
                }
            }
        }
        else
        {
            if (!isPdfHelpSkip)
            {
                ShowHelp();
            }
            else
            {
                if (listener != null)
                {
                    listener.onCloseThisViewListener();
                }
            }
        }
    }
    
    /**
     * 显示帮助
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-9
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void ShowHelp()
    {
        if (isSignHelp)
        {
            mPdfHelpLayout.setBackgroundResource(R.drawable.help_2);
            if (isPdfSignHelpSkip)
            {
                mHelpSkip.setBackgroundResource(R.drawable.help_skip2);
            }
            else
            {
                mHelpSkip.setBackgroundResource(R.drawable.help_skip1);
            }
        }
        else
        {
            mPdfHelpLayout.setBackgroundResource(R.drawable.help_1);
            if (isPdfHelpSkip)
            {
                mHelpSkip.setBackgroundResource(R.drawable.help_skip2);
            }
            else
            {
                mHelpSkip.setBackgroundResource(R.drawable.help_skip1);
            }
        }
        
        if (listener != null)
        {
            listener.onShowThisViewListener();
        }
    }
    
    /**
     * 点击帮助上“不再提示”按钮之后的操作
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-9
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void HelpSkipClick()
    {
        if (isSignHelp)
        {
            if (isPdfSignHelpSkip)
            {
                isPdfSignHelpSkip = false;
                mHelpSkip.setBackgroundResource(R.drawable.help_skip1);
            }
            else
            {
                isPdfSignHelpSkip = true;
                mHelpSkip.setBackgroundResource(R.drawable.help_skip2);
            }
        }
        else
        {
            if (isPdfHelpSkip)
            {
                isPdfHelpSkip = false;
                mHelpSkip.setBackgroundResource(R.drawable.help_skip1);
            }
            else
            {
                isPdfHelpSkip = true;
                mHelpSkip.setBackgroundResource(R.drawable.help_skip2);
            }
        }
        
    }
    
    /**
     * 点击“我知道了”的操作
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-9
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void HelpKnowClick()
    {
        SharedPreferences share = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        Editor ed = share.edit();
        if (isSignHelp)
        {
            ed.putBoolean(PDF_SIGNHELPSKIP, isPdfSignHelpSkip);
            ed.commit();
        }
        else
        {
            ed.putBoolean(PDF_HELPSKIP, isPdfHelpSkip);
            ed.commit();
        }
        
        if (listener != null)
        {
            listener.onCloseThisViewListener();
        }
    }
    
    /**
     * 关闭help view 的监听
     * 
     * @Description<功能详细描述>
     * 
     * @author ren_qiujing
     * @Version [版本号, 2013-12-2]
     */
    public interface onCloseThisViewListen
    {
        // 显示view help
        public void onShowThisViewListener();
        
        // 关闭view help
        public void onCloseThisViewListener();
    }
    
}
