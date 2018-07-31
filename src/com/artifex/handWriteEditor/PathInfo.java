package com.artifex.handWriteEditor;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Xfermode;

public class PathInfo
{
    Path     path;
    
    Rect     rect;
    
    int      color;
    
    float    press;
    
    Xfermode xfermode;
    
    Paint    mPaint;
    
    /**
     * 带3个参数的构造函数
     * 
     * @param pth Path pth
     * @param rc Rect rc
     * @param prss float prss
     */
    public PathInfo(Path pth, Rect rc, float prss)
    {
        path = pth;
        rect = new Rect(rc);
        color = Color.RED;
        press = prss;
    }
    
    /**
     * 默认的构造函数
     */
    public PathInfo()
    {
    }
    
    /**
     * 带5个参数的构造函数
     * 
     * @param pth Path pth
     * @param left int left
     * @param top int top
     * @param right int right
     * @param bottom int bottom
     */
    public PathInfo(Path pth, int left, int top, int right, int bottom)
    {
        path = pth;
        // rect = new Rect(left, top, right, bottom);
        // path.computeBounds(rect, true);
    }
    
    /**
     * 获取path
     * 
     * @Description 获取path
     * @return
     */
    public Path getPath()
    {
        return path;
    }
    
    /**
     * 设置path
     * 
     * @Description 设置path
     * @param path
     */
    public void setPath(Path path)
    {
        this.path = path;
    }
    
    /**
     * 获取path
     * 
     * @Description 获取path
     * @return
     */
    public Rect getRect()
    {
        return rect;
    }
    
    /**
     * 设置rect
     * 
     * @Description 设置rect
     * @param rect
     */
    public void setRect(Rect rect)
    {
        this.rect = rect;
    }
    
    /**
     * 获取path
     * 
     * @Description 获取path
     * @return
     */
    public int getColor()
    {
        return color;
    }
    
    /**
     * 设置color
     * 
     * @Description 设置color
     * @param color
     */
    public void setColor(int color)
    {
        this.color = color;
    }
    
    /**
     * 获取path
     * 
     * @Description 获取path
     * @return
     */
    public float getPress()
    {
        return press;
    }
    
    /**
     * 设置press
     * 
     * @Description 设置press
     * @param press
     */
    public void setPress(float press)
    {
        this.press = press;
    }
    
    /**
     * 获取path
     * 
     * @Description 获取path
     * @return
     */
    public Xfermode getXfermode()
    {
        return xfermode;
    }
    
    /**
     * 设置xfermode
     * 
     * @Description 设置xfermode
     * @param xfermode
     */
    public void setXfermode(Xfermode xfermode)
    {
        this.xfermode = xfermode;
    }
    
    /**
     * 获取path
     * 
     * @Description 获取path
     * @return
     */
    public Paint getmPaint()
    {
        return mPaint;
    }
    
    /**
     * 设置mPaint
     * 
     * @Description 设置mPaint
     * @param mPaint
     */
    public void setmPaint(Paint mPaint)
    {
        this.mPaint = mPaint;
    }
    
    /**
     * 设置press
     * 
     * @Description 设置press
     * @param press
     */
    public void setStrokeWidth(float mStrokeWidth)
    {
        this.press = mStrokeWidth;
    }
    
    /**
     * 获取press
     * 
     * @Description 获取press
     * @return
     */
    public float getStrokeWidth()
    {
        return this.press;
    }
}
