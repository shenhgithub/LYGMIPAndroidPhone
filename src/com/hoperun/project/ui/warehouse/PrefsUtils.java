/*
 * File name:  PrefsUtils.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_linxiang
 * Last modified date:  2013-4-17
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.warehouse;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * 
 * 存储本地数据的工具类
 * 
 * @Description将数据以XML的形式存放在/data/data/PACKAGE_NAME/shared_prefs目录下
 * 
 * @author li_miao
 * @Version [版本号, 2014-4-3]
 */
public class PrefsUtils
{
    private static final String TAG                 = "PrefsUtils_HDLAPAD";
    
    /** 存放历史用户名的最大记录数 */
    private static final int    LOCAL_HOSITORY_SIZE = 10;
    
    /**
     * 
     * 写入数据
     * 
     * @Description 将数据写入本地存储
     * 
     * @param context 上下文
     * @param prefsName 要存储的数据名称
     * @param prefsValue 要存储的数据值
     * @LastModifiedDate：2014-4-3
     * @author li_miao
     * @EditHistory：<修改内容><修改人>
     */
    public static void writePrefs(Context context, String prefsName, String prefsValue)
    {
        try
        {
            // 将权限数据存到本地
            SharedPreferences sharedata =
                context.getSharedPreferences(AppConststate.SHARED_ADDEDAPP, Context.MODE_WORLD_READABLE
                    | Context.MODE_WORLD_WRITEABLE);
            SharedPreferences.Editor editor = sharedata.edit();
            editor.putString(prefsName, prefsValue);
            editor.commit();
        }
        catch (Exception e)
        {
            // LogUtil.logExceptionStackTrace(TAG, e);
            Log.i(TAG, e.toString());
        }
    }
    
    /**
     * 
     * 读取本地存储数据
     * 
     * @Description 读取存储在本地的数据
     * 
     * @param context 上下文
     * @param prefsName 读取的数据名称
     * @return 数据值
     * @LastModifiedDate：2014-4-3
     * @author li_miao
     * @EditHistory：<修改内容><修改人>
     */
    public static String readPrefs(Context context, String prefsName)
    {
        String prefsValue = "";
        try
        {
            // 读取本地的数据
            SharedPreferences sharedata =
                context.getSharedPreferences(AppConststate.SHARED_ADDEDAPP, Context.MODE_WORLD_READABLE);
            prefsValue = sharedata.getString(prefsName, "");
        }
        catch (Exception e)
        {
            Log.i(TAG, e.toString());
        }
        return prefsValue;
    }
    
}
