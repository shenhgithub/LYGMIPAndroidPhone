/*
 * File name:  FastNewsListView.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-3-26
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.fastNews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.hoperun.manager.adpter.fastNews.FastNewsSecondAdapter;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.model.entityMetaData.GetFastNewsListInfo;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.BaseListView;
import com.hoperun.project.ui.xwzx.XwzxContentShowActivity;

/**
 * 今日快报列表
 * 
 * @Description 今日快报列表
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-26]
 */
public class FastNewsListView extends BaseListView
{
    /** 文件列表 **/
    private FastNewsSecondAdapter mdocListAdapter;
    
    private GetFastNewsListInfo   mSelectedInfo;
    
    private String                token;
    
    public FastNewsListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
    public FastNewsListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    /**
     * <默认构造函数>
     */
    public FastNewsListView(Activity activity, String keyWords, String gwType, String handleType, String readStatus,
        String parentDirPath, OnTouchListener listen, Boolean isHomePage, String typeId)
    {
        super(activity, keyWords, gwType, handleType, readStatus, parentDirPath, listen, isHomePage, typeId, false);
    }
    
    public FastNewsListView(Activity activity, String keyWords, String fastNewsId, String parentDirPath,
        OnTouchListener listen, boolean isHomePage, String typeId)
    {
        super(activity, keyWords, fastNewsId, "", "", parentDirPath, listen, isHomePage, typeId, false);
        initData();
        initListener();
    }
    
    /**
     * 重载方法
     * 
     * @param requestType
     * @param objHeader
     * @param objBody
     * @param error
     * @param errorCode
     * @author wang_ling
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETXWZXDOCREQUEST:
                    // 请求公文列表回来
                    MetaResponseBody doresponseBuzBody = (MetaResponseBody)objBody;
                    if (doresponseBuzBody != null && doresponseBuzBody.getRetError().equals("0"))
                    {
                        List<HashMap<String, Object>> ret = doresponseBuzBody.getBuzList();
                        
                        // List<HashMap<String, Object>> bizData =
                        // (List<HashMap<String, Object>>)ret.get(0).get("bizdata");
                        
                        if (!"".equals(ret.get(0).get("webitems")))
                        {
                            // List<HashMap<String, Object>> newRet =
                            // (List<HashMap<String, Object>>)bizData.get(0).get("webitems");
                            List<HashMap<String, Object>> newRet =
                                (List<HashMap<String, Object>>)ret.get(0).get("webitems");
                            dealgetBackDocList(newRet);
                        }
                        else
                        {
                            mFootView.setVisibility(View.GONE);
                            if (mLvMain.getFooterViewsCount() > 0)
                            {
                                setListViewAdapter(-1);
                                mLvMain.removeFooterView(mFootView);
                            }
                            closeProgressDialog();
                            isGetingDocList = IDLE;
                            Toast.makeText(mActivtiy, "没有更多的数据", Toast.LENGTH_SHORT).show();
                            
                        }
                    }
                    else
                    {
                        doGetDocListError(-1);
                    }
                    break;
                
                default:
            }
        }
        else
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETXWZXDOCREQUEST:
                    doGetDocListError(errorCode);
                    break;
                
                default:
                    break;
            }
        }
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void initListViewAdapter()
    {
        // List<HashMap<String, Object>> mDocList = new ArrayList<HashMap<String, Object>>();
        List<GetFastNewsListInfo> mDocList = new ArrayList<GetFastNewsListInfo>();
        
        mdocListAdapter = new FastNewsSecondAdapter(mActivtiy, mDocList);
        mLvMain.setAdapter(mdocListAdapter);
        
    }
    
    /**
     * 重载方法
     * 
     * @param selectCount
     * @author wang_ling
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
     * @author wang_ling
     */
    @Override
    public void getDocListFromDBData(String time)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param time
     * @author wang_ling
     */
    @Override
    public void getDocListFromNet(String time)
    {
        LogUtil.i("", "time=" + time);
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("listId", mType);
            mGetDocListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETXWZXDOCREQUEST).create();
            NetRequestController.getFastNewsList(mGetDocListRequst,
                mHandler,
                RequestTypeConstants.GETXWZXDOCREQUEST,
                body);
            
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
     * @author wang_ling
     */
    @Override
    public Object dealDatafromNetWork(List<HashMap<String, Object>> ret)
    {
        List<GetFastNewsListInfo> lists = DBDataObjectWrite.insertFastNewsSecondList(ret, user, mType);
        return lists;
        // return ret;
    }
    
    /**
     * 重载方法
     * 
     * @param obj
     * @param isDown
     * @return
     * @author wang_ling
     */
    @Override
    public Object addDataToListRefresh(Object obj, boolean isDown)
    {
        List<GetFastNewsListInfo> newList = new ArrayList<GetFastNewsListInfo>();
        if (isDown)
        {
            if (mdocListAdapter != null && mdocListAdapter.getDocLists() != null)
            {
                for (int i = 0; i < mdocListAdapter.getDocLists().size(); i++)
                {
                    newList.add(mdocListAdapter.getDocLists().get(i));
                }
            }
            
        }
        List<GetFastNewsListInfo> lists = (List<GetFastNewsListInfo>)obj;
        for (int i = 0; i < lists.size(); i++)
        {
            GetFastNewsListInfo info = lists.get(i);
            newList.add(info);
        }
        return newList;
    }
    
    /**
     * 重载方法
     * 
     * @param obj
     * @author wang_ling
     */
    @Override
    public void reFreshList(Object obj)
    {
        // TODO Auto-generated method stub
        List<GetFastNewsListInfo> mDocLists = (List<GetFastNewsListInfo>)obj;
        
        // List<HashMap<String, Object>> mDocLists = (List<HashMap<String, Object>>)obj;
        if (mDocLists == null)
        {
            return;
        }
        mCount = mDocLists.size();
        mdocListAdapter.setDocLists(mDocLists);
        mdocListAdapter.notifyDataSetChanged();
        if (mOnResultListener != null)
        {
            mOnResultListener.setOnResultListen();
        }
        
        if (mOnSearchResultListener != null)
        {
            mOnSearchResultListener.setOnSearchResultListen(mCount);
        }
    }
    
    /**
     * 重载方法
     * 
     * @param position
     * @author wang_ling
     */
    @Override
    public void onClickLisItem(int position)
    {
        
        mSelectedInfo = (GetFastNewsListInfo)mdocListAdapter.getItem(position);
        mSelectedDocId = mSelectedInfo.getStringKeyValue(GetFastNewsListInfo.id);
        // String url = mdocListAdapter.getItemUrl(position);
        String url = mSelectedInfo.getStringKeyValue(GetFastNewsListInfo.url);
        String title = mSelectedInfo.getStringKeyValue(GetFastNewsListInfo.name);
        dealListView();
        if (url != null && !"".equals(url))
        {
            Intent intent = new Intent(mActivtiy, XwzxContentShowActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("ISURL", true);
            intent.putExtra("title", title);
            mActivtiy.startActivity(intent);
        }
        else
        {
            Toast.makeText(mActivtiy, "url为空", Toast.LENGTH_LONG).show();
        }
        // updataDatabase();
        
    }
    
    /**
     * 重载方法
     * 
     * @param docid
     * @param docUrl
     * @param path
     * @return
     * @author wang_ling
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
     * @author wang_ling
     */
    @Override
    public void getFileContentEnd(boolean flag)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void updataDatabase()
    {
        DBDataObjectWrite.updateFastNewsDocList(mSelectedInfo);
    }
    
    /**
     * 重载方法
     * 
     * @param docId
     * @param flag
     * @author wang_ling
     */
    @Override
    public void updateListView(String docId, boolean flag)
    {
        List<GetFastNewsListInfo> newLists = new ArrayList<GetFastNewsListInfo>();
        if (mdocListAdapter != null && mdocListAdapter.getDocLists() != null)
        {
            int size = mdocListAdapter.getDocLists().size();
            if (mdocListAdapter.getDocLists().size() > 0)
            {
                for (int n = 0; n < size; n++)
                {
                    
                    GetFastNewsListInfo info = mdocListAdapter.getDocLists().get(n);
                    if (info.getStringKeyValue(GetFastNewsListInfo.id).equals(docId))
                    {
                        info.getLocaldatalist().put(GetFastNewsListInfo.l_read, "1");
                    }
                    newLists.add(info);
                }
                Message msg = new Message();
                msg.what = ConstState.REFRESHLIST;
                msg.obj = newLists;
                
                bHandler.sendMessage(msg);
            }
        }
        
    }
    
    /**
     * 重载方法
     * 
     * @return
     * @author wang_ling
     */
    @Override
    public int getDocCount()
    {
        if (mdocListAdapter != null)
        {
            return mdocListAdapter.getCount();
        }
        
        return 0;
    }
    
}
