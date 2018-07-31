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

import android.os.Bundle;

import com.hoperun.manager.components.HeadView;

/**
 * 所有的Fragment触发事件之后将事件传递给MainActivity， 当特定的FragMent有事件向MainActivity中传递时，需要有相应的处理方法
 * 该接口就是提供了这样的接口，每个Fragment可以增加自己的监听，相应的实现方式在MainAcitvity 中实现
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-10-28]
 */
public interface IFragmentToMainViewListen
{
    
    public void setHeadViewValue(HeadView mheadview, String title);
    
    public void onOpenNextFragment(Bundle bd);
    
    public void onCloseThisFragment(Bundle bd);
    
}
