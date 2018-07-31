package com.hoperun.manager.components.date;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class TimeDialog extends LinearLayout
{
    /***/
    private WheelView wv_time;
    
    /***/
    private WheelView wv_minute;
    
    /**
     * @param context context
     */
    public TimeDialog(Context context)
    {
        super(context);
        init(context);
    }
    
    /**
     * @param context context
     * @param attrs attrs
     */
    public TimeDialog(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }
    
    /**
     * 初始化 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param context context
     * @LastModifiedDate：2013-10-18
     * @author gu_leilei
     * @EditHistory：<修改内容><修改人>
     */
    private void init(Context context)
    {
        // 时
        wv_time = new WheelView(context);
        wv_time.setAdapter(new NumericWheelAdapter(0, 23, "%02d"));
        wv_time.setCyclic(true);
        wv_time.setClipWH(140, 90);
        // wv_time.setLabel("时");
        this.addView(wv_time, new LayoutParams(70, 90));
        // 分
        wv_minute = new WheelView(context);
        wv_minute.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
        wv_minute.setCyclic(true);
        wv_minute.setClipWH(140, 90);
        // wv_minute.setLabel("分");
        this.addView(wv_minute, new LayoutParams(70, 90));
        
        // 添加"时"监听
        OnWheelScrollListener wheelSListener_time = new OnWheelScrollListener()
        {
            public void onScrollingStarted(WheelView wheel)
            {
            }
            
            public void onScrollingFinished(WheelView wheel)
            {
            }
        };
        // 添加"分"监听
        OnWheelScrollListener wheelSListener_minute = new OnWheelScrollListener()
        {
            public void onScrollingStarted(WheelView wheel)
            {
            }
            
            public void onScrollingFinished(WheelView wheel)
            {
            }
        };
        wv_time.addScrollingListener(wheelSListener_time);
        wv_minute.addScrollingListener(wheelSListener_minute);
        wv_time.TEXT_SIZE = 18;
        wv_time.ITEM_OFFSET = wv_time.TEXT_SIZE;
        wv_minute.TEXT_SIZE = 18;
        wv_minute.ITEM_OFFSET = wv_minute.TEXT_SIZE;
        
    }
    
    /**
     * 
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return date
     * @LastModifiedDate：2013-10-18
     * @author gu_leilei
     * @EditHistory：<修改内容><修改人>
     */
    public String getTime()
    {
        String date = "";
        int time = wv_time.getCurrentItem();
        int minute = wv_minute.getCurrentItem();
        String strTime = String.valueOf(time);
        if (strTime.length() < 2)
        {
            strTime = "0" + strTime;
        }
        String strMinute = String.valueOf(minute);
        if (strMinute.length() < 2)
        {
            strMinute = "0" + strMinute;
        }
        date = strTime + ":" + strMinute;
        // end
        return date;
    }
    
    /**
     * 
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param date date
     * @LastModifiedDate：2013-10-18
     * @author gu_leilei
     * @EditHistory：<修改内容><修改人>
     */
    public void setDate(String date)
    {
        int time = Integer.parseInt(date.substring(0, 2));
        int minute = Integer.parseInt(date.substring(3));
        wv_time.setCurrentItem(time);
        wv_minute.setCurrentItem(minute);
    }
}
