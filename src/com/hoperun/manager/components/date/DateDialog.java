package com.hoperun.manager.components.date;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class DateDialog extends LinearLayout
{
    /***/
    private static final int START_YEAR    = 1900;
    
    /***/
    private static final int END_YEAR      = 2100;
    
    /** 添加大小月月份并将其转换为list,方便之后的判断 */
    private String[]         months_big    = {"1", "3", "5", "7", "8", "10", "12"};
    
    /***/
    private String[]         months_little = {"4", "6", "9", "11"};
    
    /***/
    private List<String>     list_big;
    
    /***/
    private List<String>     list_little;
    
    /**
     * 年View
     */
    private WheelView        wv_year;
    
    /**
     * 月View
     */
    private WheelView        wv_month;
    
    /**
     * 日View
     */
    private WheelView        wv_day;
    
    /**
     * 
     * @param context context
     */
    public DateDialog(Context context)
    {
        super(context);
        init(context);
    }
    
    /**
     * @param context context
     * @param attrs attrs
     */
    public DateDialog(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }
    
    /**
     * 
     * <一句话功能简述>
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
        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);
        // 年
        wv_year = new WheelView(context);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setClipWH(210, 90);
        this.addView(wv_year, new LayoutParams(70, 90));
        
        // 月
        wv_month = new WheelView(context);
        wv_month.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
        wv_month.setCyclic(true);
        wv_month.setClipWH(210, 90);
        this.addView(wv_month, new LayoutParams(70, 90));
        
        // 日
        wv_day = new WheelView(context);
        wv_day.setCyclic(true);
        wv_day.setClipWH(210, 90);
        this.addView(wv_day, new LayoutParams(70, 90));
        
        // setDate(date);
        
        // 添加"年"监听
        OnWheelScrollListener wheelSListener_year = new OnWheelScrollListener()
        {
            
            public void onScrollingStarted(WheelView wheel)
            {
            }
            
            public void onScrollingFinished(WheelView wheel)
            {
                if (null == wv_year)
                {
                    return;
                }
                int year_num = wv_year.getCurrentItem() + START_YEAR;
                System.out.println("new----" + year_num);
                
                int oldday = wv_day.getCurrentItem();
                System.out.println("old----" + oldday);
                
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1)))
                {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
                }
                else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1)))
                {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
                    if (oldday > 29)
                    {
                        wv_day.setCurrentItem(29);
                    }
                }
                else
                {
                    if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
                    {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
                        if (oldday > 28)
                        {
                            wv_day.setCurrentItem(28);
                        }
                    }
                    else
                    {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
                        if (oldday > 27)
                        {
                            wv_day.setCurrentItem(27);
                        }
                    }
                }
            }
        };
        // 添加"月"监听
        OnWheelScrollListener wheelSListener_month = new OnWheelScrollListener()
        {
            public void onScrollingStarted(WheelView wheel)
            {
            }
            
            public void onScrollingFinished(WheelView wheel)
            {
                if (null == wv_month)
                {
                    return;
                }
                int month_num = wv_month.getCurrentItem() + 1;
                
                int oldday = wv_day.getCurrentItem();
                System.out.println("old----" + oldday);
                
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num)))
                {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
                }
                else if (list_little.contains(String.valueOf(month_num)))
                {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
                    if (oldday > 29)
                    {
                        wv_day.setCurrentItem(29);
                    }
                }
                else
                {
                    
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year.getCurrentItem() + START_YEAR) % 100 != 0)
                        || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
                    {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
                        if (oldday > 28)
                        {
                            wv_day.setCurrentItem(28);
                        }
                    }
                    else
                    {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
                        if (oldday > 27)
                        {
                            wv_day.setCurrentItem(27);
                        }
                    }
                    
                }
            }
        };
        // 添加"日"监听
        OnWheelScrollListener wheelSListener_day = new OnWheelScrollListener()
        {
            public void onScrollingStarted(WheelView wheel)
            {
            }
            
            public void onScrollingFinished(WheelView wheel)
            {
            }
        };
        
        wv_year.addScrollingListener(wheelSListener_year);
        wv_month.addScrollingListener(wheelSListener_month);
        wv_month.addScrollingListener(wheelSListener_day);
        wv_day.TEXT_SIZE = 18;
        wv_day.ITEM_OFFSET = wv_day.TEXT_SIZE;
        wv_month.TEXT_SIZE = 18;
        wv_month.ITEM_OFFSET = wv_month.TEXT_SIZE;
        wv_year.TEXT_SIZE = 18;
        wv_year.ITEM_OFFSET = wv_year.TEXT_SIZE;
        
        // setVisible(false);
    }
    
    /**
     * 设置是否显示 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param bln true显示， false隐藏
     * @LastModifiedDate：<date>
     * @author wang_bei
     * @EditHistory：<修改内容><修改人>
     */
    public void setVisible(boolean bln)
    {
        if (bln == true)
        {
            setVisibility(View.VISIBLE);
        }
        else
        {
            setVisibility(View.GONE);
        }
    }
    
    /**
     * 清空 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：<date>
     * @author wang_bei
     * @EditHistory：<修改内容><修改人>
     */
    public void cleanRes()
    {
        list_big = null;
        list_little = null;
        wv_year = null;
        wv_month = null;
        wv_day = null;
    }
    
    /**
     * 获取设置的日期 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return 日期 xxxx-xx-xx
     * @LastModifiedDate：<date>
     * @author wang_bei
     * @EditHistory：<修改内容><修改人>
     */
    public String getDate()
    {
        int year = wv_year.getCurrentItem() + START_YEAR;
        int month = wv_month.getCurrentItem() + 1;
        int day = wv_day.getCurrentItem() + 1;
        String date = "";
        String strMonth = String.valueOf(month);
        if (strMonth.length() < 2)
        {
            strMonth = "0" + strMonth;
        }
        String strDay = String.valueOf(day);
        if (strDay.length() < 2)
        {
            strDay = "0" + strDay;
        }
        date = year + "-" + strMonth + "-" + strDay;
        return date;
    }
    
    /**
     * 设置初始时间 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param date date
     * @LastModifiedDate：<date>
     * @author wang_bei
     * @EditHistory：<修改内容><修改人>
     */
    public void setDate(String date)
    {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7)) - 1;
        int day = Integer.parseInt(date.substring(8, 10));
        
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
        wv_month.setCurrentItem(month);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month + 1)))
        {
            wv_day.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
        }
        else if (list_little.contains(String.valueOf(month + 1)))
        {
            wv_day.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
        }
        else
        {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
            {
                wv_day.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
            }
            else
            {
                wv_day.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
            }
        }
        wv_day.setCurrentItem(day - 1);
        
    }
}
