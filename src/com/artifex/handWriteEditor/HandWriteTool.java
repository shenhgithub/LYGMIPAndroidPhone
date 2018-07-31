/*
 * File name:  HandWriteTool.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-9-27
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.handWriteEditor;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-9-27]
 */
public class HandWriteTool
{
    // ===============签批时使用的变量==============================
    
    /**
     * GlobalState.java 记录签批的hand View
     */
    
    public static Object mEditor[]   = null;
    
    public static int    mSignColor  = -1;
    
    public static int    mSignStress = -1;
    
    /**
     * 使用spen签批和使用笔记签批的类型区别，0：为普通签批，1：spen签批
     */
    public static int    type        = -1;
    
    public static Object[] getmEditor()
    {
        return mEditor;
    }
    
    public static void setmEditor(Object[] mEditor)
    {
        HandWriteTool.mEditor = mEditor;
    }
    
    public static int getmSignColor()
    {
        return mSignColor;
    }
    
    public static void setmSignColor(int mSignColor)
    {
        HandWriteTool.mSignColor = mSignColor;
    }
    
    public static int getmSignStress()
    {
        return mSignStress;
    }
    
    public static void setmSignStress(int mSignStress)
    {
        HandWriteTool.mSignStress = mSignStress;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
}
