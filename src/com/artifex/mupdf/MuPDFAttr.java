package com.artifex.mupdf;

import android.graphics.Point;

public final class MuPDFAttr
{
    /**
     * MuPDFAttr.java
     */
    public static final int STATE_IDLE        = -1;
    
    /**
     * MuPDFAttr.java
     */
    public static final int STATE_PAGE_CHANGE = 1234;
    
    /**
     * MuPDFAttr.java
     */
    public static int       s_Current         = STATE_IDLE;
    
    /**
     * MuPDFAttr.java
     */
    public static Point[]   s_PageSizes       = null;
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public static void Blank()
    {
        s_Current = STATE_IDLE;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param length length
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public synchronized static void SetPageSizes(int length)
    {
        if (s_PageSizes != null)
        {
            s_PageSizes = null;
        }
        
        s_PageSizes = new Point[length + 1];
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return int
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public static int getS_Current()
    {
        return s_Current;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param s_Current s_Current
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public static void setS_Current(int s_Current)
    {
        MuPDFAttr.s_Current = s_Current;
    }
    
}
