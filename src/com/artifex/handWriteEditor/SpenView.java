/*
 * File name:  SPENView.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-12-3
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.handWriteEditor;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.artifex.mupdf.PDFUtilFromiText;
import com.artifex.mupdf.RealPDF;
import com.hoperun.mip.utils.LogUtil;
import com.samsung.spen.settings.SettingStrokeInfo;
import com.samsung.spensdk.SCanvasConstants;
import com.samsung.spensdk.SCanvasView;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-12-3]
 */
public class SpenView extends SCanvasView
{
    
    /**
     * 是否签批
     */
    public boolean isSign = false;
    
    /**
     * HandWriteEditor.java pdf最初打开时的宽
     */
    private int    baseW  = 0;
    
    /**
     * HandWriteEditor.java pdf最初打开时的高
     */
    private int    baseH  = 0;
    
    /**
     * HandWriteEditor.java x轴缩放因子
     */
    public float   xScale = 1f;
    
    /**
     * HandWriteEditor.java y轴缩放因子
     */
    public float   yScale = 1f;
    
    /**
     * 构造函数
     * 
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public SpenView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * 构造函数
     * 
     * @param context context
     * @param attrs attrs
     */
    public SpenView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * 构造函数
     * 
     * @param context context
     */
    public SpenView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
        FrameLayout mLayoutContainer = new FrameLayout(context);
        
        // Resource Map for Layout & Locale
        HashMap<String, Integer> settingResourceMapInt =
            SPenSDKUtils.getSettingLayoutLocaleResourceMap(true, true, true, true);
        // Resource Map for Custom font path
        HashMap<String, String> settingResourceMapString =
            SPenSDKUtils.getSettingLayoutStringResourceMap(true, true, true, true);
        // Create Setting View
        createSettingView(mLayoutContainer, settingResourceMapInt, settingResourceMapString);
        
    }
    
    boolean isPointer_2 = false; ;
    
    @Override
    public boolean onTouchEvent(MotionEvent arg0)
    {
        // TODO Auto-generated method stub
        if (isSign)
        {
            switch (arg0.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    isPointer_2 = false;
                    break;
                case MotionEvent.ACTION_POINTER_2_DOWN:
                    isPointer_2 = true;
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (isPointer_2)
                    {
                        return false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isPointer_2 = false;
                    break;
                default:
                    break;
            }
            return super.onTouchEvent(arg0);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        // TODO Auto-generated method stub
        LogUtil.i("", "onSizeChanged w=" + w + ";h=" + h + ";oldw=" + oldw + ";oldh=" + oldh);
        super.onSizeChanged(w, h, oldw, oldh);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        // TODO Auto-generated method stub
        if (baseW != 0 && baseH != 0)
        {
            xScale = (float)(right - left) / baseW;
            
            yScale = (float)(bottom - top) / baseH;
        }
        setScaleX(xScale);
        setScaleY(yScale);
        
        LogUtil.i("", "onLayout  left=" + left + ";top=" + top + ";r=" + right + ";b=" + bottom);
        super.onLayout(changed, left, top, right, bottom);
    }
    
    Bitmap mBitMap = null;
    
    public void setSign(boolean sign)
    {
        isSign = sign;
        if (isSign)
        {
            mBitMap = null;
        }
        else
        {
            try
            {
                mBitMap = getBitmap(true);
            }
            catch (Exception e)
            {
                System.out.println("getBitmap(true)发生异常");
            }
        }
    }
    
    public int getBaseW()
    {
        return baseW;
    }
    
    public void setBaseW(int baseW)
    {
        this.baseW = baseW;
    }
    
    public int getBaseH()
    {
        return baseH;
    }
    
    public void setBaseH(int baseH)
    {
        this.baseH = baseH;
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
        LogUtil.i("", "createBitmap pageNum =" + pageNum);
        
        if (isSign)
        {
            try
            {
                mBitMap = getBitmap(false);
            }
            catch (Exception e)
            {
                System.out.println("getBitmap(false)发生异常");
            }
        }
        if (mBitMap == null)
        {
            return;
        }
        
        PDFUtilFromiText mPDFUtil = new PDFUtilFromiText();
        mPDFUtil.setPassword("Comprise");
        
        RealPDF pdfR = mPDFUtil.getRealPDF(pdfpath, pageNum + 1);
        
        if (pdfR != null)
        {
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitMap, (int)(pdfR.width), (int)(pdfR.height), true);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            
            byte[] data = baos.toByteArray();
            
            mPDFUtil.drawImage(pdfpath, new PointF(0, 0), data, pageNum + 1);
        }
    }
    
    public boolean hasEditor()
    {
        if (mBitMap == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public void setPaintColor(int paintColor)
    {
        SettingStrokeInfo settingInfo = getSettingViewStrokeInfo();
        if (settingInfo != null)
        {
            settingInfo.setStrokeColor(paintColor);
            setSettingViewStrokeInfo(settingInfo);
        }
    }
    
    public void setStress(int stress)
    {
        SettingStrokeInfo settingInfo = getSettingViewStrokeInfo();
        if (settingInfo != null)
        {
            settingInfo.setStrokeWidth(stress);
            setSettingViewStrokeInfo(settingInfo);
        }
    }
    
    public void setPen()
    {
        if (getCanvasMode() != SCanvasConstants.SCANVAS_MODE_INPUT_PEN)
        {
            setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
            showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);
        }
    }
    
    public void setErase()
    {
        if (getCanvasMode() != SCanvasConstants.SCANVAS_MODE_INPUT_ERASER)
        {
            setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
            showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
        }
    }
    
    public void setRedo()
    {
        redo();
    }
    
    public void setUndo()
    {
        undo();
        
    }
    
    public void setEmpty()
    {
        clearScreen();
    }
}
