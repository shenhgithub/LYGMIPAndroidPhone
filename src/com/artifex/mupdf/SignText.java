package com.artifex.mupdf;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;

public class SignText
{
    private String       content;
    
    private List<String> textLines = new ArrayList<String>();
    
    private String       fontName;
    
    private float        fontSize;
    
    private int          fontColor;
    
    private PointF       location;
    
    private String       datename;
    
    private int          pageNum;
    
    public boolean       isEnd     = false;
    
    public SignText()
    {
        
    }
    
    /**
     * 
     * 获取content
     * 
     * @Description 获取content
     * 
     * @return
     */
    public String getContent()
    {
        return this.content;
    }
    
    /**
     * 
     * 设置content
     * 
     * @Description 设置content
     * 
     * @param content
     * @return
     */
    public SignText setContent(String content)
    {
        this.content = content;
        return this;
    }
    
    /**
     * 
     * 获取location
     * 
     * @Description 获取location
     * 
     * @return
     */
    public PointF getLocation()
    {
        return location;
    }
    
    /**
     * 
     * 设置location
     * 
     * @Description 设置location
     * 
     * @param location
     * @return
     */
    public SignText setLocation(PointF location)
    {
        this.location = location;
        return this;
    }
    
    /**
     * 
     * 获取fontName
     * 
     * @Description 获取fontName
     * 
     * @return
     */
    public String getFontName()
    {
        return fontName;
    }
    
    /**
     * 
     * 设置fontName
     * 
     * @Description 设置fontName
     * 
     * @param fontName
     * @return
     */
    public SignText setFontName(String fontName)
    {
        this.fontName = fontName;
        return this;
    }
    
    /**
     * 
     * 获取fontSize
     * 
     * @Description 获取fontSize
     * 
     * @return
     */
    public float getFontSize()
    {
        return fontSize;
    }
    
    /**
     * 
     * 设置fontSize
     * 
     * @Description 设置fontSize
     * 
     * @param fontSize
     * @return
     */
    public SignText setFontSize(float fontSize)
    {
        this.fontSize = fontSize;
        return this;
    }
    
    /**
     * 
     * 获取fontColor
     * 
     * @Description 获取fontColor
     * 
     * @return
     */
    public int getFontColor()
    {
        return fontColor;
    }
    
    /**
     * 
     * 设置fontColor
     * 
     * @Description 设置fontColor
     * 
     * @param fontColor
     * @return
     */
    public SignText setFontColor(int fontColor)
    {
        this.fontColor = fontColor;
        return this;
    }
    
    /**
     * 
     * 获取textLines
     * 
     * @Description 获取textLines
     * 
     * @return
     */
    public List<String> getTextLines()
    {
        return textLines;
    }
    
    /**
     * 
     * 设置textLines
     * 
     * @Description 设置textLines
     * 
     * @param textLines
     * @return
     */
    public SignText setTextLines(List<String> textLines)
    {
        this.textLines = textLines;
        return this;
    }
    
    /**
     * 
     * 获取datename
     * 
     * @Description 获取datename
     * 
     * @return
     */
    public String getDatename()
    {
        return datename;
    }
    
    /**
     * 
     * 设置datename
     * 
     * @Description 设置datename
     * 
     * @param datename
     * @return
     */
    public SignText setDatename(String dataname)
    {
        this.datename = dataname;
        return this;
    }
    
    /**
     * 
     * 获取pageNum
     * 
     * @Description 获取pageNum
     * 
     * @return
     */
    public int getPageNum()
    {
        return this.pageNum;
    }
    
    /**
     * 
     * 设置pageNum
     * 
     * @Description 设置pageNum
     * 
     * @param pageNum
     * @return
     */
    public SignText setPageNum(int pageNum)
    {
        this.pageNum = pageNum;
        return this;
    }
}