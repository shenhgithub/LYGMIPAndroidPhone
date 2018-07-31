/*
 * File name:  CustomHandWriteEditor.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-12-2
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.handWriteEditor;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.artifex.mupdf.PDFUtilFromiText;
import com.artifex.mupdf.RealPDF;
import com.hoperun.mipmanager.utils.ConstState;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-12-2]
 */
public class CustomHandWriteEditor extends HandWriteEditor
{
    /**
     * 点击
     */
    private static final float TOUCH_TOLERANCE = 4;
    
    /**
     * HandWriteEditor.java 记录轨迹的bitmap
     */
    private Bitmap             mBitmap;
    
    /**
     * HandWriteEditor.java 临时画布，将轨迹记录在该画布上，将mBitmap作为mCanvas的内容，这样就可以记录轨迹
     */
    private Canvas             mCanvas;
    
    /**
     * HandWriteEditor.java 笔迹
     */
    private Path               mPath;
    
    /**
     * HandWriteEditor.java 将bitMap附在canvas上时的画笔
     */
    private Paint              mBitmapPaint;
    
    /**
     * HandWriteEditor.java 轨迹的画笔
     */
    private Paint              mPaint;
    
    private Paint              mPaint2;
    
    /**
     * HandWriteEditor.java 每一次绘制的记录
     */
    private PathInfo           pathInfoH;
    
    /**
     * HandWriteEditor.java 路径集合
     */
    private List<PathInfo>     mPathInfo;
    
    /**
     * HandWriteEditor.java 重做的步骤集合
     */
    private List<PathInfo>     mPathRedo;
    
    /**
     * HandWriteEditor.java 是否是橡皮擦
     */
    private boolean            isErase         = false;
    
    /**
     * HandWriteEditor.java 画笔颜色
     */
    private int                paintColor      = ConstState.COLOR_PEN_BLACK;
    
    /**
     * HandWriteEditor.java 画笔粗细
     */
    public int                 mstress         = ConstState.SIGN_STRESS_NORMAL;
    
    /**
     * HandWriteEditor.java x轴缩放因子
     */
    public float               xScale          = 1f;
    
    /**
     * HandWriteEditor.java y轴缩放因子
     */
    public float               yScale          = 1f;
    
    /**
     * HandWriteEditor.java 是否签批
     */
    private boolean            isSign          = false;
    
    /**
     * HandWriteEditor.java pdf最初打开时的宽
     */
    private int                baseW           = 0;
    
    /**
     * HandWriteEditor.java pdf最初打开时的高
     */
    private int                baseH           = 0;
    
    /**
     * HandWriteEditor.java pdf放大缩小时的宽
     */
    private int                bitmapWith;
    
    /**
     * HandWriteEditor.java pdf放大缩小时的高
     */
    private int                bitmapHeight;
    
    /**
     * x，y
     */
    private float              mX, mY;
    
    /**
     * <默认构造函数>
     */
    public CustomHandWriteEditor(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        // mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        init();
    }
    
    /**
     * <默认构造函数>
     */
    public CustomHandWriteEditor(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        init();
    }
    
    /**
     * <默认构造函数>
     */
    public CustomHandWriteEditor(Context c)
    {
        super(c);
        
        // mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        init();
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void init()
    {
        mPathInfo = new ArrayList<PathInfo>();
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(HandWriteTool.mSignColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        
        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setDither(true);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeJoin(Paint.Join.ROUND);
        mPaint2.setStrokeCap(Paint.Cap.ROUND);
        // mPaint2.setStrokeWidth(1);
        // mPaint2.setColor(Color.BLUE);
        mPaint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }
    
    /**
     * 重载方法
     * 
     * @param w w
     * @param h h
     * @param oldw oldw
     * @param oldh oldh
     * @author ren_qiujing
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        
        this.bitmapWith = w;
        this.bitmapHeight = h;
        if (mBitmap == null)
        {
            mBitmap = Bitmap.createBitmap(baseW, baseH, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        }
        
    }
    
    /**
     * 重载方法
     * 
     * @param canvas canvas
     * @author ren_qiujing
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.scale(xScale, yScale);
        canvas.drawColor(Color.TRANSPARENT);
        
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        
        if (mPath != null)
        {
            if (!isErase)
            {
                mPaint.setColor(getPaintColor());
                mPaint.setStrokeWidth(getStress());
                canvas.drawPath(mPath, mPaint);
            }
            else
            {
                canvas.drawCircle(mX, mY, 10, mPaint2);
            }
            
        }
        
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param x x
     * @param y y
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void touch_start(float x, float y)
    {
        if (mPath != null)
        {
            mPath.moveTo(x, y);
            
            mX = x;
            mY = y;
        }
        if (isErase)
        {
            mCanvas.drawPoint(x, y, mPaint);
        }
        
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param x y
     * @param y y
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void touch_move(float x, float y)
    {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)
        {
            if (mPath != null)
            {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }
        
        if (isErase)
        {
            if (mPath != null)
            {
                mCanvas.drawPath(mPath, mPaint);
            }
        }
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void touch_up()
    {
        if (mPath != null)
        {
            mPath.lineTo(mX, mY);
            
            pathInfoH.setPath(mPath);
            pathInfoH.setColor(mPaint.getColor());
            pathInfoH.setXfermode(mPaint.getXfermode());
            pathInfoH.setStrokeWidth(mPaint.getStrokeWidth());
            
            mPathInfo.add(pathInfoH);
        }
        if (!isErase)
        {
            if (mPath != null)
            {
                mCanvas.drawPath(mPath, mPaint);
            }
        }
        else
        {
            mCanvas.drawPoint(mX, mY, mPaint);
        }
        
        mPath = null;
    }
    
    /**
     * 重载方法
     * 
     * @param event event
     * @return boolean
     * @author ren_qiujing
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (!isSign)
        {
            return super.onTouchEvent(event);
        }
        float x = event.getX() / xScale;
        float y = event.getY() / yScale;
        
        // float pressD = event.getPressure();
        
        // mPaint.setStrokeWidth(getStress());
        
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                pathInfoH = new PathInfo();
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
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
    public void undo()
    {
        if (mPathInfo != null && mPathInfo.size() > 0)
        {
            if (mPathRedo == null)
            {
                mPathRedo = new ArrayList<PathInfo>();
            }
            mPathRedo.add(mPathInfo.get(mPathInfo.size() - 1));
            
            mPathInfo.remove(mPathInfo.size() - 1);
            redrawOnBitmap();
        }
        
        // setPen();
    }
    
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
    public void redo()
    {
        if (mPathRedo != null && mPathRedo.size() > 0)
        {
            mPathInfo.add(mPathRedo.get(mPathRedo.size() - 1));
            
            mPathRedo.remove(mPathRedo.size() - 1);
            
            redrawOnBitmap();
        }
        
        // setPen();
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void redrawOnBitmap()
    {
        mBitmap = Bitmap.createBitmap(bitmapWith, bitmapHeight, Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(mBitmap);// 重新设置画布，相当于清空画布
        Iterator<PathInfo> iter = mPathInfo.iterator();
        while (iter.hasNext())
        {
            PathInfo drawPath = iter.next();
            mPath = drawPath.getPath();
            mPaint.setXfermode(drawPath.getXfermode());
            
            mPaint.setColor(drawPath.getColor());
            mPaint.setStrokeWidth(drawPath.getStrokeWidth());
            mCanvas.drawPath(mPath, mPaint);
            
            invalidate();// 刷新
        }
        
        mPath = null;
        if (mPathInfo.size() == 0)
        {
            invalidate();// 刷新
        }
        
    }
    
    /**
     * 
     * 清空画布 最后更新时间：<date> 修改履历：<修改内容>
     */
    public void clearAll()
    {
        mBitmap = Bitmap.createBitmap(bitmapWith, bitmapHeight, Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(mBitmap);// 重新设置画布，相当于清空画布
        mPathInfo.clear();
        if (mPathRedo != null)
        {
            mPathRedo.clear();
        }
        
        mPath = null;
        setPen();
        invalidate();
    }
    
    /**
     * 橡皮擦
     */
    public void setErase()
    {
        isErase = true;
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(20);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }
    
    /**
     * 
     * 恢复画笔
     */
    public void setPen()
    {
        isErase = false;
        mPaint.setColor(paintColor);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    }
    
    /**
     * 重载方法
     * 
     * @param changed changed
     * @param left left
     * @param top top
     * @param right right
     * @param bottom bottom
     * @author ren_qiujing
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        // TODO Auto-generated method stub
        if (baseW != 0 && baseH != 0)
        {
            xScale = (float)(right - left) / baseW;
            
            yScale = (float)(bottom - top) / baseH;
        }
        // LogUtil.i("hoperun", "onLayout:" + "l=" + left + ";top=" + top + ";right=" + right + ";bottom=" + bottom
        // + ";xScale=" + xScale + ";yScale=" + yScale);
        // LogUtil.i("hoperun", "onLayout:" + "baseW=" + baseW + ";baseH=" + baseH);
        super.onLayout(changed, left, top, right, bottom);
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param press press
     * @return float
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private float getEventPress(float press)
    {
        // 0.05~0.25,normal 0.15
        float width = 8 * press;
        // if (width < 8)
        // {
        // width = 8;
        // }
        // LogUtil.i("hoperun", "*********getEventPress width=" + width);
        return width;
    }
    
    /**
     * 
     * 设置画笔颜色
     * 
     * @param paintColor 最后更新时间：<date> 修改履历：<修改内容>
     */
    public void setPaintColor(int paintColor)
    {
        this.paintColor = paintColor;
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
    private int getPaintColor()
    {
        return paintColor;
    }
    
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
    public void setStress(int stress)
    {
        this.mstress = stress;
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
    private int getStress()
    {
        return this.mstress;
    }
    
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
    public void createBitmap(String pdfpath, int pageNum)
    {
        
        if (mBitmap == null || (mPathInfo == null || mPathInfo.size() == 0))
        {
            return;
        }
        
        PDFUtilFromiText mPDFUtil = new PDFUtilFromiText();
        mPDFUtil.setPassword("Comprise");
        
        RealPDF pdfR = mPDFUtil.getRealPDF(pdfpath, pageNum + 1);
        
        if (pdfR != null)
        {
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, (int)(pdfR.width), (int)(pdfR.height), true);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            
            byte[] data = baos.toByteArray();
            
            mPDFUtil.drawImage(pdfpath, new PointF(0, 0), data, pageNum + 1);
        }
        
        // File file = new File("/mnt/sdcard/PDFTEST/pdftest_" + pageNum + ".png");
        // FileOutputStream out;
        // try
        // {
        // out = new FileOutputStream(file);
        // try
        // {
        // out.write(data);
        // out.flush();
        // out.close();
        // }
        // catch (IOException e)
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        //
        // }
        // catch (FileNotFoundException e)
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
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
    public int getBaseW()
    {
        return baseW;
    }
    
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
        this.baseW = baseW;
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
    public int getBaseH()
    {
        return baseH;
    }
    
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
    public void setBaseH(int baseH)
    {
        this.baseH = baseH;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return boolean
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public boolean isSign()
    {
        return isSign;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param isSign isSign
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setSign(boolean isSign)
    {
        this.isSign = isSign;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void clearValue()
    {
        if (mPathInfo != null)
        {
            mPathInfo.clear();
        }
        
        if (mPathRedo != null)
        {
            mPathRedo.clear();
        }
    }
    
    public boolean hasEditor()
    {
        if (mBitmap == null || (mPathInfo == null || mPathInfo.size() == 0))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
