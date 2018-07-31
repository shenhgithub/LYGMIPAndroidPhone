/*
 * File name:  YwblListView.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  chen_wei3
 * Last modified date:  2014-4-2
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.ywbl;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.BaseListView;

/**
 * <一句话功能简述>业务办理列表
 * 
 * @Description<功能详细描述>
 * 
 * @author chen_wei3
 * @Version [版本号, 2014-4-2]
 */
public class YwblListView extends BaseListView
{
    
    /**
     * <默认构造函数>
     */
    public YwblListView(Activity activity, String keyWords, String gwType, String handleType, String readStatus,
        String parentDirPath, OnTouchListener listen, Boolean isHomePage, String typeId, boolean isActivity)
    {
        super(activity, keyWords, gwType, handleType, readStatus, parentDirPath, listen, isHomePage, typeId, isActivity);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * 重载方法
     * 
     * @param requestType
     * @param objHeader
     * @param objBody
     * @param error
     * @param errorCode
     * @author chen_wei3
     */
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETYWBLLISTREQUEST:
                    MetaResponseBody doresponseBuzBody = (MetaResponseBody)objBody;
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * 重载方法
     * 
     * @author chen_wei3
     */
    @Override
    public void initListViewAdapter()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param selectCount
     * @author chen_wei3
     */
    @Override
    public void setListViewAdapter(int selectCount)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param time
     * @author chen_wei3
     */
    @Override
    public void getDocListFromDBData(String time)
    {
        
    }
    
    /**
     * 重载方法
     * 
     * @param time
     * @author chen_wei3
     */
    @Override
    public void getDocListFromNet(String time)
    {
        LogUtil.i("", "time=" + time);
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            mGetDocListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETYWBLLISTREQUEST).create();
            NetRequestController.getYwblList(mGetDocListRequst,
                mHandler,
                RequestTypeConstants.GETYWBLLISTREQUEST,
                body,
                "getTodoBerthingList");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 重载方法
     * 
     * @param ret
     * @return
     * @author chen_wei3
     */
    @Override
    public Object dealDatafromNetWork(List<HashMap<String, Object>> ret)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 重载方法
     * 
     * @param obj
     * @param isDown
     * @return
     * @author chen_wei3
     */
    @Override
    public Object addDataToListRefresh(Object obj, boolean isDown)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 重载方法
     * 
     * @param obj
     * @author chen_wei3
     */
    @Override
    public void reFreshList(Object obj)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param position
     * @author chen_wei3
     */
    @Override
    public void onClickLisItem(int position)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param docid
     * @param docUrl
     * @param path
     * @return
     * @author chen_wei3
     */
    @Override
    public boolean getFileContent(String docid, String docUrl, String path)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * 重载方法
     * 
     * @param flag
     * @author chen_wei3
     */
    @Override
    public void getFileContentEnd(boolean flag)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author chen_wei3
     */
    @Override
    public void updataDatabase()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param docId
     * @param flag
     * @author chen_wei3
     */
    @Override
    public void updateListView(String docId, boolean flag)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @return
     * @author chen_wei3
     */
    @Override
    public int getDocCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
