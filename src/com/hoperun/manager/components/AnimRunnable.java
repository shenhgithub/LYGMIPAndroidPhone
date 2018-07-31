/*
 * File name:  AnimRunnable2.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  shen_feng
 * Last modified date:  2013-9-25
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.components;

import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;

/**
 * 动画
 * 
 * @Description 动画
 * 
 * @author shen_feng
 * @Version [版本号, 2013-9-25]
 */
public class AnimRunnable implements Runnable
{
    /**
     * 二级fragment的滑动
     */
    public static final int              SECOND_FRAGMENT                 = 0;
    
    /**
     * 三级fragment的滑动
     */
    public static final int              THIRD_FRAGMENT                  = 1;
    
    /** 移动中 **/
    public static final int              HANDLER_MESSAGE_UPDATE_POSITION = 101;
    
    // public static final int HANDLER_MESSAGE_UPDATE_THIRD_POSITION = 102;
    
    /** 线程休眠时间 **/
    public static final int              STEP_MS                         = 2;
    
    /**
     * 哪一级菜单滑动
     */
    public int                           mType;
    
    /** 每次移动距离 **/
    private int                          stepDistance;
    
    /** 距离左边距离 **/
    private int                          lastMarginLeft;
    
    /** 布局参数 **/
    private ViewGroup.MarginLayoutParams marginLayoutParams;
    
    /** handler **/
    private Handler                      handler;
    
    /**
     * 
     * <默认构造函数>
     */
    public AnimRunnable()
    {
        
    }
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param stepDistance 每次移动的距离
     * @param lastMarginLeft 离左边的距离
     * @param marginLayoutParams marginLayoutParams
     * @param handler handler
     */
    public AnimRunnable(int stepDistance, int lastMarginLeft, MarginLayoutParams marginLayoutParams, Handler handler,
        int type)
    {
        super();
        this.stepDistance = stepDistance;
        this.lastMarginLeft = lastMarginLeft;
        this.marginLayoutParams = marginLayoutParams;
        this.handler = handler;
        this.mType = type;
    }
    
    /**
     * 
     * 获取每次移动的距离
     * 
     * @Description 获取每次移动的距离
     * 
     * @return 每次移动的距离
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public int getStepDistance()
    {
        return stepDistance;
    }
    
    /**
     * 
     * 获取 距离左边的距离
     * 
     * @Description 获取 距离左边的距离
     * 
     * @return 距离左边的距离
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public int getLastMarginLeft()
    {
        return lastMarginLeft;
    }
    
    /**
     * 
     * 布局参数
     * 
     * @Description 布局参数
     * 
     * @return 布局参数
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public ViewGroup.MarginLayoutParams getMarginLayoutParams()
    {
        return marginLayoutParams;
    }
    
    /**
     * 
     * 布局参数
     * 
     * @Description 布局参数
     * 
     * @param stepDistance 距离左边的距离
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setStepDistance(int stepDistance)
    {
        this.stepDistance = stepDistance;
    }
    
    /**
     * 
     * 设置距离左边的距离
     * 
     * @Description 设置距离左边的距离
     * 
     * @param lastMarginLeft 距离左边的距离
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setLastMarginLeft(int lastMarginLeft)
    {
        this.lastMarginLeft = lastMarginLeft;
    }
    
    /**
     * 
     * 布局参数
     * 
     * @Description 布局参数
     * 
     * @param marginLayoutParams 布局参数
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setMarginLayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams)
    {
        this.marginLayoutParams = marginLayoutParams;
    }
    
    /**
     * 
     * handler
     * 
     * @Description<功能详细描述>
     * 
     * @return handler
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public Handler getHandler()
    {
        return handler;
    }
    
    /**
     * 
     * handler
     * 
     * @Description handler
     * 
     * @param handler handler
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }
    
    @Override
    public void run()
    {
        // int with = marginLayoutParams.width;
        if (stepDistance > 0)
        {
            while (marginLayoutParams.leftMargin < lastMarginLeft)
            {
                
                marginLayoutParams.leftMargin += stepDistance;
                marginLayoutParams.rightMargin = -marginLayoutParams.leftMargin;
                
                // LogUtil.i("", "left=" + marginLayoutParams.leftMargin + ";right=" + marginLayoutParams.rightMargin);
                Message msg = handler.obtainMessage();
                msg.what = HANDLER_MESSAGE_UPDATE_POSITION;
                msg.arg1 = mType;
                handler.sendMessage(msg);
                
                try
                {
                    Thread.sleep(STEP_MS);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            marginLayoutParams.leftMargin = lastMarginLeft;
            marginLayoutParams.rightMargin = -marginLayoutParams.leftMargin;
            Message msg = handler.obtainMessage();
            msg.what = HANDLER_MESSAGE_UPDATE_POSITION;
            msg.arg1 = mType;
            handler.sendMessage(msg);
            
        }
        else if (stepDistance < 0)
        {
            while (marginLayoutParams.leftMargin > lastMarginLeft)
            {
                marginLayoutParams.leftMargin += stepDistance;
                marginLayoutParams.rightMargin = -marginLayoutParams.leftMargin;
                Message msg = handler.obtainMessage();
                msg.what = HANDLER_MESSAGE_UPDATE_POSITION;
                msg.arg1 = mType;
                handler.sendMessage(msg);
                try
                {
                    Thread.sleep(STEP_MS);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            marginLayoutParams.leftMargin = lastMarginLeft;
            marginLayoutParams.rightMargin = -marginLayoutParams.leftMargin;
            Message msg = handler.obtainMessage();
            msg.what = HANDLER_MESSAGE_UPDATE_POSITION;
            msg.arg1 = mType;
            handler.sendMessage(msg);
        }
    }
}
