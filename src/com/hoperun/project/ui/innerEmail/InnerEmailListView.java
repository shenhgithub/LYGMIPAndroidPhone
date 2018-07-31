/*
 * File name:  InnerEmailListView.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-2-17
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.innerEmail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.artifex.BaseUtils.OnInnerListViewItemListen;
import com.hoperun.manager.adpter.innerEmail.InnerEmailSecondAdapter;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.model.entityMetaData.GetInnerEmailListInfo;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.BaseListView;

/**
 * 内部邮件列表
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-2-17]
 */
public class InnerEmailListView extends BaseListView
{
    
    /** 文件adapter **/
    private InnerEmailSecondAdapter   mdocListAdapter;
    
    /**
     * 选中邮件的信息
     */
    private GetInnerEmailListInfo     mSelectedInfo;
    
    /**
     * 获取请求列表
     */
    protected NetTask                 mUpdateReadStatusRequst;
    
    /**
     * 详细邮件信息页面
     */
    private DetailInfoView            mDetailInfoView;
    
    /***
     * 邮件列表点击事件返回
     */
    private OnInnerListViewItemListen itembackListen;
    
    /**
     * 获取 itembackListen
     * 
     * @return 返回 itembackListen
     * @author wang_ling
     */
    public OnInnerListViewItemListen getItembackListen()
    {
        return itembackListen;
    }
    
    /**
     * 设置 itembackListen
     * 
     * @param itembackListen 对itembackListen进行赋值
     * @author wang_ling
     */
    public void setItembackListen(OnInnerListViewItemListen itembackListen)
    {
        this.itembackListen = itembackListen;
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
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETINNEREMAILLISTREQUEST:
                    // 请求公文列表回来
                    MetaResponseBody doresponseBuzBody = (MetaResponseBody)objBody;
                    if (doresponseBuzBody != null && doresponseBuzBody.getRetError().equals("0"))
                    {
                        List<HashMap<String, Object>> ret = doresponseBuzBody.getBuzList();
                        dealgetBackDocList(ret);
                    }
                    else
                    {
                        doGetDocListError(-1);
                    }
                    
                    break;
                // 更新文件的阅读状态
                case RequestTypeConstants.UPDATEEMAILREADSTATUS:
                    break;
                default:
            }
        }
        else
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETINNEREMAILLISTREQUEST:
                    doGetDocListError(errorCode);
                    break;
                // 更新文件的阅读状态
                case RequestTypeConstants.UPDATEEMAILREADSTATUS:
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * <默认构造函数>
     */
    public InnerEmailListView(Activity activity, String keyWords, String gwType, String readStatus,
        String parentDirPath, OnTouchListener listen, Boolean isHomePage)
    {
        super(activity, keyWords, gwType, "", readStatus, parentDirPath, listen, isHomePage, "", false);
    }
    
    /**
     * <默认构造函数>
     */
    public InnerEmailListView(Context context)
    {
        super(context);
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void initListViewAdapter()
    {
        List<GetInnerEmailListInfo> mDocList = new ArrayList<GetInnerEmailListInfo>();
        mdocListAdapter = new InnerEmailSecondAdapter(mActivtiy, mDocList, user);
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
        mLvMain.setAdapter(mdocListAdapter);
        if (selectCount == -1)
        {
            mLvMain.setSelection(mdocListAdapter.getCount());
        }
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
        closeProgressDialog();
        // 文件存储在本地已读、未读标识
        String mReadFlag = "";
        if (mReadStatus.equals(ConstState.HasReadDOCLIST))
        {
            mReadFlag = ConstState.BACK_HasReadDOCLIST; // 已读
        }
        else if (mReadStatus.equals(ConstState.UNReadDOCLIST))
        {
            mReadFlag = ConstState.BACK_UNReadDOCLIST; // 未读
        }
        
        GetInnerEmailListInfo test1 = new GetInnerEmailListInfo();
        
        List<HashMap<String, Object>> queryret;
        if (null == mSearchkeyWords || mSearchkeyWords.equals(""))
        {
            
            String where = "";
            
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                where =
                    GetInnerEmailListInfo.l_user + " = ?" + " and " + GetInnerEmailListInfo.l_type + " = ?" + " and "
                        + GetInnerEmailListInfo.createtime + "< ?";
            }
            else
            {
                where =
                    GetInnerEmailListInfo.l_user + " = ?" + " and " + GetInnerEmailListInfo.l_type + " = ?" + " and "
                        + GetInnerEmailListInfo.createtime + "< ?" + " and " + GetInnerEmailListInfo.readflag + " =? ";
            }
            
            String[] selectionArgs = null;
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                selectionArgs = new String[] {user, mType, time};
            }
            else
            {
                selectionArgs = new String[] {user, mType, time, mReadFlag};
            }
            
            String sortOrder = GetInnerEmailListInfo.createtime + " desc ";
            
            String limitString = " limit " + pageSize;
            
            queryret = test1.query(null, where, selectionArgs, sortOrder + limitString);
        }
        else
        {
            String where = "";
            
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                where =
                    GetInnerEmailListInfo.l_user + " = ?" + " and " + GetInnerEmailListInfo.l_type + " = ?" + " and "
                        + GetInnerEmailListInfo.msgtitle + " like ?" + " and " + GetInnerEmailListInfo.createtime
                        + "< ?";
            }
            else
            {
                where =
                    GetInnerEmailListInfo.l_user + " = ?" + " and " + GetInnerEmailListInfo.l_type + " = ?" + " and "
                        + GetInnerEmailListInfo.msgtitle + " like ?" + " and " + GetInnerEmailListInfo.createtime
                        + "< ?" + " and " + GetInnerEmailListInfo.readflag + " =? ";
            }
            
            String[] selectionArgs = null;
            
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                selectionArgs = new String[] {user, mType, "%" + mSearchkeyWords + "%", time};
            }
            else
            {
                selectionArgs = new String[] {user, mType, "%" + mSearchkeyWords + "%", time, mReadFlag};
            }
            
            String sortOrder = GetInnerEmailListInfo.createtime + " desc ";
            
            String limitString = " limit " + pageSize;
            
            queryret = test1.query(null, where, selectionArgs, sortOrder + limitString);
        }
        
        if (mLvMain.isDone())
        {
            mLvMain.onRefreshComplete();
        }
        
        if (mLvMain.getFooterViewsCount() == 0)
        {
            mLvMain.addFooterView(mFootView);
            mFootView.setVisibility(View.GONE);
        }
        
        if (queryret == null || queryret.size() == 0)
        {
            if (mLvMain.getFooterViewsCount() != 0)
            {
                // if (isHomePage)
                // {
                // mLvMain.setAdapter(mHomePopListAdapter);
                // }
                // else
                // {
                // mLvMain.setAdapter(mdocListAdapter);
                // }
                setListViewAdapter(-1);
                mLvMain.removeFooterView(mFootView);
            }
            if (isGetingDocList == ConstState.UPREFRESH)
            {
                Toast.makeText(mActivtiy, "没有更多数据", Toast.LENGTH_SHORT).show();
                
            }
            else
            {
                Toast.makeText(mActivtiy, "没有数据", Toast.LENGTH_SHORT).show();
                if (mOnResultListener != null)
                {
                    mOnResultListener.setOnResultListen();
                }
            }
            isGetingDocList = IDLE;
        }
        else
        {
            List<GetInnerEmailListInfo> doclists = new ArrayList<GetInnerEmailListInfo>();
            
            for (int i = 0; i < queryret.size(); i++)
            {
                GetInnerEmailListInfo info = new GetInnerEmailListInfo();
                info.convertToObject(queryret.get(i));
                doclists.add(info);
            }
            
            addDataToListAndRefresh(doclists, true);
            isGetingDocList = IDLE;
        }
        
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
            body.put("replaymsgid", "");
            body.put("currenttime", time);
            body.put("pagesize", pageSize);
            body.put("msgtype", mType);
            body.put("keywords", mSearchkeyWords);
            body.put("isread", mReadStatus);
            mGetDocListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETINNEREMAILLISTREQUEST).create();
            
            NetRequestController.getInnerEmailListRequest(mGetDocListRequst,
                mHandler,
                RequestTypeConstants.GETINNEREMAILLISTREQUEST,
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
        List<GetInnerEmailListInfo> lists = DBDataObjectWrite.insertInnerEmailList(ret, user, mType, mParentDirPath);
        return lists;
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
        List<GetInnerEmailListInfo> newList = new ArrayList<GetInnerEmailListInfo>();
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
        List<GetInnerEmailListInfo> lists = (List<GetInnerEmailListInfo>)obj;
        for (int i = 0; i < lists.size(); i++)
        {
            GetInnerEmailListInfo info = lists.get(i);
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
    @SuppressWarnings("unchecked")
    @Override
    public void reFreshList(Object obj)
    {
        List<GetInnerEmailListInfo> mDocLists = (List<GetInnerEmailListInfo>)obj;
        Collections.sort(mDocLists, new DocListDateComparator());
        
        // 当前列表中的列表数量
        mCount = mDocLists.size();
        if (mCount > 0)
        {
            // 获取最后一条的时间
            mRefreshLastTime = mDocLists.get(mCount - 1).getStringKeyValue(GetInnerEmailListInfo.createtime);
        }
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
     * 获取 mDetailInfoView
     * 
     * @return 返回 mDetailInfoView
     * @author wang_ling
     */
    public DetailInfoView getmDetailInfoView()
    {
        return mDetailInfoView;
    }
    
    /**
     * 设置 mDetailInfoView
     * 
     * @param mDetailInfoView 对mDetailInfoView进行赋值
     * @author wang_ling
     */
    public void setmDetailInfoView(DetailInfoView mDetailInfoView)
    {
        this.mDetailInfoView = mDetailInfoView;
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
        mSelectedInfo = (GetInnerEmailListInfo)mdocListAdapter.getItem(position);
        mSelectedDocId = (String)mSelectedInfo.getValue(GetInnerEmailListInfo.msgid);
        if (mType.equals("1") && mReadStatus.equals(ConstState.UNReadDOCLIST))
        {
            updateReadStatus((String)mSelectedInfo.getValue(GetInnerEmailListInfo.msgid));
        }
        else
        {
            mdocListAdapter.setmSelectedPosition(position);
            mdocListAdapter.notifyDataSetChanged();
        }
        
        itembackListen.OnItemBackListen(mSelectedInfo, mType, mParentDirPath); // if (mDetailInfoView != null)
        // {
        // removeView(mDetailInfoView);
        // }
        // mDetailInfoView = null;
        // mDetailInfoView = new DetailInfoView(mActivtiy, 100, mSelectedInfo, mType, mParentDirPath);
        // mDetailInfoView.setMviewbacklistener(mViewBackListener);
        // addView(mDetailInfoView);
    }
    
    // onEmailDetailBackListen mViewBackListener = new onEmailDetailBackListen()
    // {
    //
    // @Override
    // public void onViewBackCancel()
    // {
    // if (mDetailInfoView != null)
    // {
    // removeView(mDetailInfoView);
    // mDetailInfoView = null;
    // }
    //
    // if (mType.equals("1")
    // && mReadStatus.equals(ConstState.UNReadDOCLIST))
    // {
    // dealListView();// /02-20
    // }
    // }
    //
    // @Override
    // public void onViewBackItemClick(
    // List<HashMap<String, Object>> buzList, String msgId,
    // String msgtitle)
    // {
    // saveFileInfo(buzList,
    // user,
    // "",
    // "",
    // msgId,
    // msgtitle,
    // ConstState.NBYJ);
    // }
    // };
    
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
        String wheres =
            GetInnerEmailListInfo.l_user + "=?" + " and " + GetInnerEmailListInfo.l_type + "=?" + " and "
                + GetInnerEmailListInfo.msgid + "=?";
        String[] selectionArgs = {user, mType, mSelectedDocId};
        
        ContentValues values = new ContentValues();
        values.put(GetInnerEmailListInfo.readflag, ConstState.BACK_HasReadDOCLIST);
        
        GetInnerEmailListInfo info = new GetInnerEmailListInfo();
        info.update(values, wheres, selectionArgs);
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
        List<GetInnerEmailListInfo> mDocList = new ArrayList<GetInnerEmailListInfo>();
        if (mdocListAdapter != null)
        {
            mDocList = mdocListAdapter.getDocLists();
        }
        
        if (mDocList != null)
        {
            int i = -1;
            if (mDocList.size() > 0)
            {
                for (i = 0; i < mDocList.size(); i++)
                {
                    GetInnerEmailListInfo info = mDocList.get(i);
                    if (info.getStringKeyValue(GetInnerEmailListInfo.msgid).equals(docId))
                    {
                        if (!flag)
                        {
                            mDocList.remove(i);
                        }
                        break;
                    }
                }
            }
            if (flag && (i == mDocList.size() || mDocList.size() == 0))
            {
                String wheres =
                    GetInnerEmailListInfo.l_user + "=?" + " and " + GetInnerEmailListInfo.l_type + "=?" + " and "
                        + GetInnerEmailListInfo.msgid + "=?";
                String[] selectionArgs = {user, mType, docId};
                
                GetInnerEmailListInfo info = new GetInnerEmailListInfo();
                List<HashMap<String, Object>> ret = info.query(null, wheres, selectionArgs, null);
                
                GetInnerEmailListInfo detail = null;
                if (ret != null && ret.size() > 0)
                {
                    detail = new GetInnerEmailListInfo(user, mType);
                    detail.convertToObject(ret.get(0));
                }
                if (detail != null)
                {
                    mDocList.add(detail);
                }
            }
            Message msg = new Message();
            msg.what = ConstState.REFRESHLIST;
            msg.obj = mDocList;
            
            bHandler.sendMessage(msg);
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
    
    /**
     * 
     * 比较器
     * 
     * @Description 比较器
     * 
     * @author wang_ling
     * @Version [版本号, 2013-10-18]
     */
    class DocListDateComparator implements Comparator<GetInnerEmailListInfo>
    {
        /**
         * 比较器方法 按照里面的 按日期逆排序
         */
        @Override
        public int compare(GetInnerEmailListInfo lhs, GetInnerEmailListInfo rhs)
        {
            return (rhs.getStringKeyValue(GetInnerEmailListInfo.createtime).compareToIgnoreCase(lhs.getStringKeyValue(GetInnerEmailListInfo.createtime)));
        }
    }
    
    /**
     * 
     * 更新文件的阅读状态
     * 
     * @Description<功能详细描述>
     * 
     * @param msgId 邮件的id
     * @LastModifiedDate：2014-2-20
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void updateReadStatus(String msgId)
    {
        if (msgId != null)
        {
            
            JSONObject body = new JSONObject();
            try
            {
                body.put("msgid", msgId);
                mUpdateReadStatusRequst =
                    new HttpNetFactoryCreator(RequestTypeConstants.UPDATEEMAILREADSTATUS).create();
                
                NetRequestController.updateEmailReadStatusRequest(mUpdateReadStatusRequst,
                    mHandler,
                    RequestTypeConstants.UPDATEEMAILREADSTATUS,
                    body);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            // showProgressDialog();
        }
    }
}
