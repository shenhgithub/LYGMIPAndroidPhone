package com.artifex.handWriteEditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * 手写签批的view
 * 
 * @author shen_feng
 * @version [版本号, 2013-8-27]
 */
@SuppressLint("DrawAllocation")
abstract public class HandWriteEditor extends View
{
    
    public HandWriteEditor(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    public HandWriteEditor(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    public HandWriteEditor(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * 撤销 ，还原按钮
     */
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    abstract public void undo();
    
    /**
     * 
     * 还原，重做按钮
     */
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    abstract public void redo();
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    // abstract private void redrawOnBitmap();
    
    /**
     * 
     * 清空画布 最后更新时间：<date> 修改履历：<修改内容>
     */
    abstract public void clearAll();
    
    /**
     * 橡皮擦
     */
    abstract public void setErase();
    
    /**
     * 
     * 恢复画笔
     */
    abstract public void setPen();
    
    /**
     * 
     * 设置画笔颜色
     * 
     * @param paintColor 最后更新时间：<date> 修改履历：<修改内容>
     */
    abstract public void setPaintColor(int paintColor);
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param stress stress
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    abstract public void setStress(int stress);
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param pdfpath 路径
     * @param pageNum 页码
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    abstract public void createBitmap(String pdfpath, int pageNum);
    
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
    abstract public int getBaseW();
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param baseW
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setBaseW(int baseW)
    {
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
    abstract public int getBaseH();
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param baseH baseH
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    abstract public void setBaseH(int baseH);
    
    abstract public boolean isSign();
    
    abstract public void setSign(boolean isSign);
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    abstract public void clearValue();
    
    abstract public boolean hasEditor();
}
