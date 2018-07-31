/*
 * File name:  IFragementToMainActivity.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description: 所有的Fragment触发事件之后将事件传递给MainActivity
 * Author:  ren_qiujing
 * Last modified date:  2013-10-28
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.baseui.baseInterface;

import android.graphics.Bitmap;

import com.hoperun.mipmanager.model.entityModule.nettv.VideoListItem;
import com.hoperun.mipmanager.model.entityModule.offical.GWDocModule;
import com.hoperun.mipmanager.model.entityModule.schedule.ScheduleInfo;

/**
 * 所有的Fragment触发事件之后将事件传递给MainActivity， 当特定的FragMent有事件向MainActivity中传递时，需要有相应的处理方法
 * 该接口就是提供了这样的接口，每个Fragment可以增加自己的监听，相应的实现方式在MainAcitvity 中实现
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-10-28]
 */
public interface IFragmentToMainActivityListen
{
    /**
     * 
     * 弹出公文流转二级菜单
     * 
     * @Description 弹出二级菜单（详细列表菜单）
     * 
     * @param index index
     * @param funccode 功能code
     * @param path2 路径
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void onOfficialFragmentSelected(int index, GWDocModule mModlue, String path);
    
    /**
     * 
     * 公文流转关闭二级菜单
     * 
     * @Description 关闭三级布局（详细列表菜单）
     * 
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void onOfficialFragmentCloseSelected();
    
    /**
     * 
     * 日程安排打开三级菜单（时间轴菜单）
     * 
     * @Description<功能详细描述>
     * 
     * @param index index
     * @param list_info 日历信息
     * @param date 有计划的日期
     * @LastModifiedDate：2013-10-18
     * @author gu_leilei
     * @EditHistory：<修改内容><修改人>
     */
    public void onSchedulePlanSelected(int index, ScheduleInfo list_info, String date);
    
    /**
     * 日程安排通知 Activity关闭三级菜单（时间轴菜单）
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author gu_leilei
     * @EditHistory：<修改内容><修改人>
     */
    public void onScheduleTimeFragmentCloseSelected();
    
    /**
     * 日程安排中的日程（二级菜单）有修改后，需要对日程安排中一级菜单中的日历控件的数据刷新 该方法即是二级菜单中有变化后请求一级菜单做数据刷新
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author gu_leilei
     * @EditHistory：<修改内容><修改人>
     */
    public void onScheduleChangedListener();
    
    /**
     * 所有模块中的一级菜单（二级布局）被关闭
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-4
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void onSecondFragmentClose();
    
    /**
     * 
     * 个人设置中心绑定/解绑后通知MainActivity中显示是否已绑定
     * 
     * @Description 个人设置中心绑定/解绑后通知MainActivity中显示是否已绑定
     * 
     * @LastModifiedDate：2013-11-7
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void onPersonSetBind();
    
    /**
     * 
     * 头像设置
     * 
     * @Description 头像设置
     * 
     * @LastModifiedDate：2013-12-6
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    public void onPersonSetHeader(Bitmap bitmap);
    
    /**
     * 电视新闻中全屏设置
     * 
     * @Description<功能详细描述>
     * 
     * @param isFull 是否全屏
     * @LastModifiedDate：2013-12-17
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void onNetTVShowFullScreen(boolean isFull);
    
    /**
     * 视频监控选择后到mainActivity的方法
     * 
     * @Description<功能详细描述>
     * 
     * @param item
     * @param path
     * @LastModifiedDate：2013-12-23
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void onVideoMonitorFragmentSelected(VideoListItem item);
    
    /**
     * 请求获取未读数的接口
     * 
     * @Description<功能详细描述>
     * 
     * @param funId
     * @param funcode
     * @LastModifiedDate：2013-12-23
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void onSendGetUnReadCount(String funId, String funcode);
    
    /**
     * 关闭主页的待办事宜
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-2-24
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void onCloseHomeViewFragment();
    
}
