/*
 * File name:  IMainActivityToFragment.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-10-28
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.baseui.baseInterface;

/**
 * 所有的MainActivity触发事件之后将事件传递给相应Fragment
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-10-28]
 */
public interface IMainActivityToFragmentListen
{
    
    /**
     * 点击back键之后的监听方法
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-28
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public boolean onKeyDown(int keyId);
    
    /**
     * fragment的动画执行完毕
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-28
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void animationOver();
    
}
