/*
 * File name:  AppConststate.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  li_miao
 * Last modified date:  2014-3-26
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author li_miao
 * @Version [版本号, 2014-3-26]
 */
public class AppConststate
{
    public static List<HashMap<String, Object>> myAppList       = new ArrayList<HashMap<String, Object>>();
    
    public static boolean                       isDownloaded    = false;
    
    public static List<AppsModule>              appModuleList   = new ArrayList<AppsModule>();
    
    /**
     * 已添加应用程序列表
     */
    public static List<AppsModule>              appAddedList    = new ArrayList<AppsModule>();
    
    /** 存放数据本地存储名 */
    public static final String                  SHARED_ADDEDAPP = "addedapp_shared_prefs";
}
