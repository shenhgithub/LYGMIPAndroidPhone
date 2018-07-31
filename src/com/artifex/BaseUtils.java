/*
 * File name:  DocUtils.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-11-12
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex;

import java.util.HashMap;
import java.util.List;

import android.view.ViewGroup.MarginLayoutParams;

import com.hoperun.mip.GlobalState;
import com.hoperun.mipmanager.model.entityMetaData.GetInnerEmailListInfo;
import com.hoperun.mipmanager.model.entityMetaData.PDFNextStepInfo;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-11-12]
 */
public class BaseUtils
{
    
    // /**
    // * pdf页面上弹出的view 被取消
    // */
    // public static final int CANCEL = 0;
    //
    // /**
    // * pdf发送成功
    // */
    // public static final int SEND_SUCCESS = 1;
    //
    // /**
    // * pdf发送失败
    // */
    // public static final int SEND_FAULT = 2;
    
    /**
     * 计算view所处的位置
     * 
     * @Description<功能详细描述>
     * 
     * @param mlp
     * @param button_mid
     * @param height
     * @return
     * @LastModifiedDate：2013-11-12
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public static MarginLayoutParams justViewRelativeLayout(MarginLayoutParams mlp, int button_mid, int height, int with)
    {
        int mScreen_with = GlobalState.getInstance().getmScreen_With();
        
        int mScreen_height = GlobalState.getInstance().getmScreen_Height();
        
        int mDensityDpi = GlobalState.getInstance().getmDensityDpi();
        
        int min_marginTopOrBottom = (int)(20 * ((float)mDensityDpi / 160));
        
        // view的高
        mlp.height = (int)(height * ((float)mDensityDpi / 160));
        
        mlp.width = (int)(with * ((float)mDensityDpi / 160));
        
        // 按键的中心点 + 显示的view 的一半高度，离底部的距离
        int DistanceToBottom = button_mid + mlp.height / 2;
        
        // 按键的中心点 - 显示的view 的一半高度，离顶部的距离
        int DistanceToTop = button_mid - mlp.height / 2;
        
        // 显示的view正好可以以按键为中心点显示在屏幕上
        if ((DistanceToBottom <= (mScreen_height - min_marginTopOrBottom)) && (DistanceToTop >= min_marginTopOrBottom))
        {
            mlp.topMargin = button_mid - mlp.height / 2;
        }
        // 如果显示的view以按键为中心点显示，则该view最顶部超出范围
        else if (DistanceToBottom < (mScreen_height - min_marginTopOrBottom))
        {
            mlp.topMargin = min_marginTopOrBottom;
        }
        // 如果显示的view以按键为中心点显示，则该view最底部超出范围
        else if (DistanceToTop > min_marginTopOrBottom)
        {
            mlp.topMargin = mScreen_height - min_marginTopOrBottom - mlp.height;
        }
        else
        {
            mlp.topMargin = min_marginTopOrBottom;
            mlp.height = mScreen_with - 2 * min_marginTopOrBottom;
        }
        return mlp;
    }
    
    /**
     * view返回到pdf阅读页面的监听
     * 
     * @Description<功能详细描述>
     * 
     * @author ren_qiujing
     * @Version [版本号, 2013-11-12]
     */
    public interface onViewBackListen
    {
        public void onViewBackListener(int type);
    }
    
    /**
     * 下一步步骤页面，监听事件
     * 
     * @Description<功能详细描述>
     * 
     * @author ren_qiujing
     * @Version [版本号, 2013-11-12]
     */
    public interface NextStepListen
    {
        /**
         * 点击列表某一项
         * 
         * @Description<功能详细描述>
         * 
         * @param info
         * @LastModifiedDate：2013-11-12
         * @author ren_qiujing
         * @EditHistory：<修改内容><修改人>
         */
        public void onNextStepListener(PDFNextStepInfo info);
        
        /**
         * 取消
         * 
         * @Description<功能详细描述>
         * 
         * @LastModifiedDate：2013-11-12
         * @author ren_qiujing
         * @EditHistory：<修改内容><修改人>
         */
        public void onCancelListener();
        
        /**
         * 发送文件
         * 
         * @Description<功能详细描述>
         * 
         * @LastModifiedDate：2013-11-18
         * @author ren_qiujing
         * @EditHistory：<修改内容><修改人>
         */
        public void onSendFile(String option, String setpID);
    }
    
    /**
     * 通讯录页面到下一步步骤页面监听
     * 
     * @Description<功能详细描述>
     * 
     * @author ren_qiujing
     * @Version [版本号, 2013-11-12]
     */
    public interface CompanyAddressListen
    {
        public void onCompanyAddressListener(int type);
        
        public void onSendFileBegin(int type, String sendusersId, String opinion, String mStepId);
    }
    
    /**
     * 
     * 从内部邮件详细信息页面返回到列表页面的监听
     * 
     * @Description<功能详细描述>
     * 
     * @author wang_ling
     * @Version [版本号, 2014-2-20]
     */
    public interface onEmailDetailBackListen
    {
        public void onViewBackCancel();
        
        public void onViewBackItemClick(List<HashMap<String, Object>> buzList, String msgId, String msgtitle);
    }
    
    /**
     * 
     * 邮件联系人监听
     * 
     * @Description<功能详细描述>
     * 
     * @author wang_ling
     * @Version [版本号, 2014-2-18]
     */
    public interface EmailContactListen
    {
        public void onContactCancelListener();
        
        public void onContactSend(String senduserId, String sendusername);
        
        public void onAttachBack(List<String> list);
    }
    
    /**
     * 邮件列表点击事件返回 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @author wang_ling
     * @Version [版本号, 2014-2-25]
     */
    public interface OnInnerListViewItemListen
    {
        public void OnItemBackListen(GetInnerEmailListInfo info, String mType, String path);
    }
    
}
