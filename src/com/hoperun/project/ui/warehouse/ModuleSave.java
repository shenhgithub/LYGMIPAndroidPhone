/*
 * File name:  ModuleSave.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  li_miao
 * Last modified date:  2014-3-27
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.warehouse;

import java.util.HashMap;
import java.util.List;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author li_miao
 * @Version [版本号, 2014-3-27]
 */
public class ModuleSave
{
    /**
     * 保存实体 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param json
     * @LastModifiedDate：2014-1-15
     * @author li_miao
     * @EditHistory：<修改内容><修改人>
     */
    public static void saveAppsModule(HashMap<String, Object> map)
    {
        AppsModule entity = new AppsModule();
        entity.setAppcode((String)map.get("appcode"));
        entity.setAppicon((String)map.get("appicon"));
        entity.setAppid((String)map.get("appid"));
        entity.setAppname((String)map.get("appname"));
        entity.setAttachurl((String)map.get("attachurl"));
        entity.setCreatetime((String)map.get("createtime"));
        entity.setSn((String)map.get("sn"));
        entity.setUrltype((String)map.get("urltype"));
        entity.setVersion((String)map.get("version"));
        entity.setDelated(false);
        entity.setAdded(false);
        entity.setInstalled(false);
        entity.setIsdownload(false);
        AppConststate.appModuleList.add(entity);
        System.out.println("实体保存测试" + entity.toString());
        
    }
    
    public static void saveAppsModuleTolist(HashMap<String, Object> map, List<AppsModule> appsList)
    {
        AppsModule entity = new AppsModule();
        entity.setAppcode((String)map.get("appcode"));
        entity.setAppicon((String)map.get("appicon"));
        entity.setAppid((String)map.get("appid"));
        entity.setAppname((String)map.get("appname"));
        entity.setAttachurl((String)map.get("attachurl"));
        entity.setCreatetime((String)map.get("createtime"));
        entity.setSn((String)map.get("sn"));
        entity.setUrltype((String)map.get("urltype"));
        entity.setVersion((String)map.get("version"));
        entity.setDelated(false);
        entity.setAdded(false);
        entity.setInstalled(false);
        entity.setIsdownload(false);
        appsList.add(entity);
        System.out.println("实体保存测试" + entity.toString());
        
    }
}
