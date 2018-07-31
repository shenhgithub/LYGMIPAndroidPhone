package com.artifex.mupdf;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

public class PDFLogicUtil
{
    /**
     * 中文字符长度
     */
    private static final int  CHINESE_LENGTH     = 2;
    
    /**
     * 屏幕x密度
     */
    public static final int   SCREEN_XHDPI       = 1001;
    
    /**
     * 屏幕d密度
     */
    public static final int   SCREEN_HDPI        = 1002;
    
    /**
     * 屏幕m密度
     */
    public static final int   SCREEN_MDPI        = 1003;
    
    /**
     * 屏幕l密度
     */
    public static final int   SCREEN_LDPI        = 1004;
    
    /**
     * 字体默认大小
     */
    public static final float FONT_DEFAULT_SIZE  = 15.0f;
    
    /**
     * 字体默认颜色
     */
    public static final int   FONT_DEFAULT_COLOR = 0xFFFF0000;
    
    /**
     * 字体粗细
     */
    public static final float FONT_BASE_SCALE    = .7F;
    
    /**
     * PDFLogicUtil.java
     */
    private Context           mContext;
    
    /**
     * 宽
     */
    public int                mScreenWidth       = 480;
    
    /**
     * 高
     */
    public int                mScreenHeight      = 800;
    
    /**
     * 默认构造函数
     * 
     * @param context Context context
     */
    public PDFLogicUtil(Context context)
    {
        mContext = context;
        getScreenSize();
    }
    
    /**
     * 是否是中文字符
     * 
     * @param c c
     * @return
     */
    public boolean isChinese(char c)
    {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
        {
            
            return true;
        }
        
        return false;
    }
    
    /**
     * 得到登陆用户名
     * 
     * @param context context
     * @return
     */
    public String getUserName(Context context)
    {
        SharedPreferences sharedoPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sharedoPreferences.getString("openId", "");
    }
    
    /**
     * 计算得到指定组件的大小
     * 
     * @param v
     * @param wlength
     * @return
     */
    public Point getViewSize(View v, int wlength)
    {
        int ws = View.MeasureSpec.makeMeasureSpec(wlength, View.MeasureSpec.AT_MOST);
        int hs = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(ws, hs);
        
        int w = v.getMeasuredWidth();
        int h = v.getMeasuredHeight();
        
        return new Point(w, h);
    }
    
    /**
     * 获得指定字体的高度
     * 
     * @param fontSize
     * @return
     */
    public int getFontHeight(float fontSize)
    {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return (int)Math.ceil(fm.descent - fm.ascent) + 2;
        // Math.ceil(fm.descent - fm.ascent)
    }
    
    /**
     * 获得指定字体的字符串宽度
     * 
     * @param content
     * @param fontSize
     * @return
     */
    public float getStringWidth(String content, float fontSize)
    {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        paint.setAntiAlias(true);
        
        return paint.measureText(content);
    }
    
    /**
     * 计算获得内容中存在的最大字符数的<b>行</b>
     * 
     * @param content
     * @return
     */
    public String getMaxString(String content)
    {
        char[] chars = content.toCharArray();
        
        int start = 0; // java字符的索引
        String maxStr = "";
        int max = 0; // 最大的字节数
        int l = 0; // 真实的字节长度
        
        for (int i = 0; i < chars.length; i++)
        {
            l += isChinese(chars[i]) ? CHINESE_LENGTH : 1;
            
            if (chars[i] == '\n')
            {
                --l;
                if (l > max)
                {
                    max = l;
                    maxStr = content.substring(start, i);
                }
                start = i + 1;
                l = 0;
            }
        }
        
        if (l > max)
        {
            maxStr = content.substring(start, content.length());
        }
        
        return maxStr;
    }
    
    /**
     * 获得内容中的行的最大宽度
     * 
     * @param content content
     * @param fontSize fontSize
     * @return
     */
    public float getMaxStringWidth(String content, float fontSize)
    {
        String[] strs = content.split("\n");
        float max = 0f;
        
        for (int i = 0; i < strs.length; i++)
        {
            float lineWidth = this.getStringWidth(strs[i], fontSize);
            
            if (max < lineWidth)
                max = lineWidth;
        }
        
        return max;
    }
    
    /**
     * 计算指定的文本行中的文字宽带度最大的宽度值
     * 
     * @param lines lines
     * @param fontSize fontSize
     * @return
     */
    public float getMaxStringWidthFromLines(List<String> lines, float fontSize)
    {
        float max = 0f;
        
        for (int i = 0; i < lines.size(); i++)
        {
            float w = this.getStringWidth(lines.get(i), fontSize);
            
            if (max < w)
            {
                max = w;
            }
        }
        
        return max;
    }
    
    /**
     * 获得手机屏幕大小，默认480*800
     * 
     * @return
     */
    public void getScreenSize()
    {
        if (mContext != null)
        {
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            mScreenWidth = dm.widthPixels;
            mScreenHeight = dm.heightPixels;
        }
    }
    
    /**
     * 获得屏幕分辨率类型，默认HDPI(480*800)
     * 
     * @return int
     */
    public int getScreenType()
    {
        int screentType = SCREEN_HDPI;
        
        if (mContext != null)
        {
            Activity activity = (Activity)mContext;
            DisplayMetrics metrics = new DisplayMetrics();
            Display display = activity.getWindowManager().getDefaultDisplay();
            display.getMetrics(metrics);
            
            switch (metrics.densityDpi)
            {
                case DisplayMetrics.DENSITY_LOW:
                    screentType = SCREEN_LDPI;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    screentType = SCREEN_MDPI;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    screentType = SCREEN_HDPI;
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    screentType = SCREEN_XHDPI;
                    break;
                
                default:
                    break;
            }
        }
        
        return screentType;
        // if (mScreenWidth == 480 && mScreenHeight == 800) {
        // return SCREEN_HDPI;
        // } else if (mScreenWidth == 720 && mScreenHeight == 1280) {
        // return SCREEN_XHDPI;
        // }
        //
        // return SCREEN_HDPI;
    }
    
    /**
     * 获得初始PDF文件在手机屏幕中的大小
     * 
     * @return Point
     */
    public Point getPDFBaseSize()
    {
        int screenType = getScreenType();
        Point size = null;
        
        if (screenType == SCREEN_HDPI)
        {
            size = new Point(480, 678);
        }
        else if (screenType == SCREEN_XHDPI)
        {
            size = new Point(720, 1018);
        }
        else
        {
            size = new Point(480, 678);
        }
        
        return size;
    }
    
    /**
     * 将指定内容通过计算分隔成适应宽度的多个行
     * 
     * @param content content
     * @param stringMaxWidth stringMaxWidth
     * @param fontSize fontSize
     * @return
     */
    public ArrayList<String> getLines(String content, float stringMaxWidth, float fontSize)
    {
        ArrayList<String> line = new ArrayList<String>();
        
        String[] strs = content.split("\n");
        float charsWidth = 0f;
        int start = 0;
        
        for (int i = 0; i < strs.length; i++)
        {
            for (int ch = 0; ch < strs[i].length(); ch++)
            {
                charsWidth += getMaxStringWidth(strs[i].substring(ch, ch + 1), fontSize);
                
                if (charsWidth > stringMaxWidth)
                {
                    line.add(strs[i].substring(start, ch));
                    --ch;
                    start = ch;
                    charsWidth = 0f;
                }
            }
            
            line.add(strs[i].substring(start));
            start = 0;
            charsWidth = 0f;
        }
        
        return line;
    }
}
