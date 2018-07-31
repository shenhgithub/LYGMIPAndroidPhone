/*
 * File name:  OfficalListView.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-11-6
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.offical;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.hoperun.manager.adpter.homePage.HomeDaiBanAdapter;
import com.hoperun.manager.adpter.offical.OfficalDocSecondAdapter;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.FileUtil;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.model.entityMetaData.GetDocListInfo;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.BaseListView;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-11-6]
 */
@SuppressLint("HandlerLeak")
public class OfficalListView extends BaseListView
{
    
    /** 文件adapter **/
    private OfficalDocSecondAdapter mdocListAdapter;
    
    /** 主页最新待办adapter **/
    private HomeDaiBanAdapter       mHomeDaiBanAdapter;
    
    protected GetDocListInfo        mSelectedInfo = null;
    
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        switch (requestType)
        {
            case RequestTypeConstants.GETDOCLISTREQUEST:
            case RequestTypeConstants.GETFILECONTENT:
                mListener.onSendGetUnReadCount("", "");
                break;
            
            default:
                break;
        }
        // TODO Auto-generated method stub
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETDOCLISTREQUEST:
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
                case RequestTypeConstants.GETFILECONTENT:
                    
                    MetaResponseBody getContentBuzBody = (MetaResponseBody)objBody;
                    if (getContentBuzBody != null && getContentBuzBody.getRetError().equals("0"))
                    {
                        List<HashMap<String, Object>> buzList = getContentBuzBody.getBuzList();
                        
                        if (buzList != null && buzList.size() > 0)
                        {
                            saveFileInfo(buzList,
                                user,
                                "",
                                mHandleType,
                                mSelectedDocId,
                                mSelectedFileTitle,
                                ConstState.GWLZ);
                        }
                        else
                        {
                            isDownling = false;
                            
                            if (isHomePage)
                            {
                                pdfLoadLayout.setVisibility(View.GONE);
                                mHomeDaiBanAdapter.setmSelectedPosition(-1);
                            }
                            else
                            {
                                mPopupWindow.dismiss();
                                mdocListAdapter.setmSelectedPosition(-1);
                            }
                            
                            Toast.makeText(mActivtiy, "下载文件失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        isDownling = false;
                        
                        if (isHomePage)
                        {
                            pdfLoadLayout.setVisibility(View.GONE);
                            mHomeDaiBanAdapter.setmSelectedPosition(-1);
                        }
                        else
                        {
                            mPopupWindow.dismiss();
                            mdocListAdapter.setmSelectedPosition(-1);
                        }
                        
                        Toast.makeText(mActivtiy, "下载文件失败！", Toast.LENGTH_SHORT).show();
                    }
                    
                    break;
                case RequestTypeConstants.PROGRESS_BAR:
                    // 返回进度值
                    mDownloadfilePro.setProgress((Integer)objBody);
                    
                    break;
                default:
            }
        }
        else
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETDOCLISTREQUEST:
                    doGetDocListError(errorCode);
                    break;
                
                case RequestTypeConstants.GETFILECONTENT:
                    
                    if (errorCode != ConstState.CANCEL_THREAD)
                    {
                        Toast.makeText(mActivtiy, "下载文件失败！", Toast.LENGTH_SHORT).show();
                    }
                    isDownling = false;
                    if (isHomePage)
                    {
                        pdfLoadLayout.setVisibility(View.GONE);
                    }
                    else
                    {
                        mPopupWindow.dismiss();
                    }
                    
                    break;
                
                default:
                    break;
            }
        }
    }
    
    public OfficalListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    public OfficalListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    public OfficalListView(Activity activity, String keyWords, String gwType, String handleType, String readStatus,
        String parentDirPath, OnTouchListener listen, Boolean isHomePage)
    {
        super(activity, keyWords, gwType, handleType, readStatus, parentDirPath, listen, isHomePage, "", false);
        // TODO Auto-generated constructor stub
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object addDataToListRefresh(Object obj, boolean isDown)
    {
        // TODO Auto-generated method stub
        List<GetDocListInfo> newList = new ArrayList<GetDocListInfo>();
        if (isDown)
        {
            // mDocList.clear();
            if (isHomePage)
            {
                if (mHomeDaiBanAdapter != null && mHomeDaiBanAdapter.getDocLists() != null)
                {
                    for (int i = 0; i < mHomeDaiBanAdapter.getDocLists().size(); i++)
                    {
                        newList.add(mHomeDaiBanAdapter.getDocLists().get(i));
                    }
                }
            }
            else
            {
                if (mdocListAdapter != null && mdocListAdapter.getDocLists() != null)
                {
                    for (int i = 0; i < mdocListAdapter.getDocLists().size(); i++)
                    {
                        newList.add(mdocListAdapter.getDocLists().get(i));
                    }
                }
            }
            
        }
        
        List<GetDocListInfo> lists = (List<GetDocListInfo>)obj;
        
        for (int i = 0; i < lists.size(); i++)
        {
            GetDocListInfo info = lists.get(i);
            newList.add(info);
        }
        
        return newList;
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void reFreshList(Object obj)
    {
        // TODO Auto-generated method stub
        List<GetDocListInfo> mDocLists = (List<GetDocListInfo>)obj;
        Collections.sort(mDocLists, new DocListDateComparator());
        
        // 当前列表中的列表数量
        mCount = mDocLists.size();
        if (mCount > 0)
        {
            // 获取最后一条的时间
            mRefreshLastTime = mDocLists.get(mCount - 1).getStringKeyValue(GetDocListInfo.createdatetime);
        }
        
        if (isHomePage)
        {
            mHomeDaiBanAdapter.setDocLists(mDocLists);
            mHomeDaiBanAdapter.notifyDataSetChanged();
        }
        else
        {
            mdocListAdapter.setDocLists(mDocLists);
            mdocListAdapter.notifyDataSetChanged();
        }
        
        if (mOnResultListener != null)
        {
            mOnResultListener.setOnResultListen();
        }
        
        if (mOnSearchResultListener != null)
        {
            mOnSearchResultListener.setOnSearchResultListen(mCount);
        }
    }
    
    // /**
    // *获取网络中的列表
    // * 获得列表 "keyWord": "关键字,搜索条件", "currentDatetime": "当前时间 格式yyyy-MM-dd HH:mm:ss ", "pageSize ": "分页条数", "type ":
    // * "流程类型，例如：收文/发文", "handleType": "检索待办还是已办1为已办，0为待办"
    // *
    // * @LastModifiedDate：2013-9-28
    // * @author shen_feng
    // * @EditHistory：<修改内容><修改人>
    // */
    @Override
    public void getDocListFromNet(String time)
    {
        LogUtil.i("", "time=" + time);
        JSONObject body = new JSONObject();
        try
        {
            body.put("keywords", mSearchkeyWords);
            body.put("currendatetime", time);
            body.put("pagesize", pageSize);
            body.put("type", mType);
            body.put("handletype", mHandleType);
            body.put("readstatus", mReadStatus);
            
            mGetDocListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETUnDOCLISTREQUEST).create();
            
            NetRequestController.getDocListRequest(mGetDocListRequst,
                mHandler,
                RequestTypeConstants.GETDOCLISTREQUEST,
                body);
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
    }
    
    /**
     * 获取本地列表
     * 
     * @Description<功能详细描述>
     * 
     * @param mtime
     * @LastModifiedDate：2013-11-8
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public void getDocListFromDBData(String mtime)
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
        
        GetDocListInfo test1 = new GetDocListInfo();
        
        List<HashMap<String, Object>> queryret;
        if (null == mSearchkeyWords || mSearchkeyWords.equals(""))
        {
            String where = "";
            
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                where =
                    GetDocListInfo.l_user + " = ?" + " and " + GetDocListInfo.l_type + " = ?" + " and "
                        + GetDocListInfo.l_handletype + " = ?" + " and " + GetDocListInfo.createdatetime + "< ?";
            }
            else
            {
                where =
                    GetDocListInfo.l_user + " = ?" + " and " + GetDocListInfo.l_type + " = ?" + " and "
                        + GetDocListInfo.l_handletype + " = ?" + " and " + GetDocListInfo.createdatetime + "< ?"
                        + " and " + GetDocListInfo.readstatus + " =? ";
            }
            
            String[] selectionArgs = null;
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                selectionArgs = new String[] {user, mType, mHandleType, mtime};
            }
            else
            {
                selectionArgs = new String[] {user, mType, mHandleType, mtime, mReadFlag};
            }
            
            String sortOrder = GetDocListInfo.createdatetime + " desc ";
            
            String limitString = " limit " + pageSize;
            
            queryret = test1.query(null, where, selectionArgs, sortOrder + limitString);
        }
        else
        {
            String where = "";
            
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                where =
                    GetDocListInfo.l_user + " = ?" + " and " + GetDocListInfo.l_type + " = ?" + " and "
                        + GetDocListInfo.l_handletype + " = ?" + " and " + GetDocListInfo.title + " like ?" + " and "
                        + GetDocListInfo.createdatetime + "< ?";
            }
            else
            {
                where =
                    GetDocListInfo.l_user + " = ?" + " and " + GetDocListInfo.l_type + " = ?" + " and "
                        + GetDocListInfo.l_handletype + " = ?" + " and " + GetDocListInfo.title + " like ?" + " and "
                        + GetDocListInfo.createdatetime + "< ?" + " and " + GetDocListInfo.readstatus + " =? ";
            }
            
            String[] selectionArgs = null;
            
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                selectionArgs = new String[] {user, mType, mHandleType, "%" + mSearchkeyWords + "%", mtime};
            }
            else
            {
                selectionArgs = new String[] {user, mType, mHandleType, "%" + mSearchkeyWords + "%", mtime, mReadFlag};
            }
            
            String sortOrder = GetDocListInfo.createdatetime + " desc ";
            
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
            if (isHomePage)
            {
                mLvMain.setAdapter(mHomeDaiBanAdapter);
            }
            else
            {
                mLvMain.setAdapter(mdocListAdapter);
            }
            
        }
        
        if (queryret == null || queryret.size() == 0)
        {
            if (mLvMain.getFooterViewsCount() != 0)
            {
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
                setNoListDataView(0);
            }
            isGetingDocList = IDLE;
        }
        else
        {
            List<GetDocListInfo> doclists = new ArrayList<GetDocListInfo>();
            
            for (int i = 0; i < queryret.size(); i++)
            {
                GetDocListInfo info = new GetDocListInfo();
                info.convertToObject(queryret.get(i));
                doclists.add(info);
            }
            
            addDataToListAndRefresh(doclists, true);
            isGetingDocList = IDLE;
            setNoListDataView(1);
        }
    }
    
    @Override
    public void onClickLisItem(int position)
    {
        // TODO Auto-generated method stub
        if (isHomePage)
        {
            mSelectedInfo = (GetDocListInfo)mHomeDaiBanAdapter.getItem(position);
        }
        else
        {
            mSelectedInfo = (GetDocListInfo)mdocListAdapter.getItem(position);
        }
        mSelectedDocId = mSelectedInfo.getStringKeyValue(GetDocListInfo.docid);
        
        mSelectedFileTitle = mSelectedInfo.getStringKeyValue(GetDocListInfo.title);
        
        mSelectedFlag = mSelectedInfo.getStringKeyValue(GetDocListInfo.lockstatus);
        
        mSelectedLockId = mSelectedInfo.getStringKeyValue(GetDocListInfo.lockuserid);
        
        mSelectedLockName = mSelectedInfo.getStringKeyValue(GetDocListInfo.lockusername);
        
        mSelectedPath = DBDataObjectWrite.getFilePath(user, mSelectedDocId, ConstState.GWLZ);
        
        File file = new File(mSelectedPath);
        
        if (offLine)
        {
            
            if (file.exists())
            {
                if (isHomePage)
                {
                    mHomeDaiBanAdapter.setmSelectedPosition(position);
                    mHomeDaiBanAdapter.notifyDataSetChanged();
                }
                else
                {
                    mdocListAdapter.setmSelectedPosition(position);
                    mdocListAdapter.notifyDataSetChanged();
                }
                OpenPDFFile();
            }
            else
            {
                Toast.makeText(mActivtiy, "本地不存在该文件！", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if (file.exists())
            {
                if (isHomePage)
                {
                    mHomeDaiBanAdapter.setmSelectedPosition(position);
                    mHomeDaiBanAdapter.notifyDataSetChanged();
                }
                else
                {
                    mdocListAdapter.setmSelectedPosition(position);
                    mdocListAdapter.notifyDataSetChanged();
                }
                OpenPDFFile();
            }
            else
            {
                if (isHomePage)
                {
                    mHomeDaiBanAdapter.setmSelectedPosition(position);
                    mHomeDaiBanAdapter.notifyDataSetChanged();
                    if (!getFileContent(mSelectedDocId, "", mParentDirPath))
                    {
                        mHomeDaiBanAdapter.setmSelectedPosition(-1);
                        mHomeDaiBanAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    mdocListAdapter.setmSelectedPosition(position);
                    mdocListAdapter.notifyDataSetChanged();
                    if (!getFileContent(mSelectedDocId, "", mParentDirPath))
                    {
                        mdocListAdapter.setmSelectedPosition(-1);
                        mdocListAdapter.notifyDataSetChanged();
                    }
                }
            }
            
        }
    }
    
    /**
     * 
     * 获取pdf文件
     * 
     * @Description 获取pdf文件
     * 
     * @LastModifiedDate：2013-10-9
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public boolean getFileContent(String docid, String url, String path)
    {
        if (!FileUtil.externalMemoryAvailable())
        {
            Toast.makeText(mActivtiy, "你的手机没有加载SDCard！", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (FileUtil.getAvailableExternalMemorySize() < 100)
        {
            Toast.makeText(mActivtiy, "你的SDCard空间已满，请释放你的SDCard空间！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isHomePage)
        {
            showDownloadProgressHome();
        }
        else
        {
            showDownloadProgress();
        }
        
        JSONObject body = new JSONObject();
        try
        {
            body.put("docid", docid);
            body.put("type", mType);
            
            mGetFileContentRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETFILECONTENT).create();
            
            NetRequestController.getFileContent(mGetFileContentRequst, mHandler, RequestTypeConstants.GETFILECONTENT,
            
            body, path);
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /**
     * 
     * 打开PDF
     * 
     * @LastModifiedDate：2013-10-10
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    private void OpenPDFFile()
    {
        if (mHandleType.equals(ConstState.UNHADLEDOCLIST))
        {
            intentOpenFile(mSelectedPath,
                mSelectedFileTitle,
                mSelectedDocId,
                mType,
                mHandleType,
                mSelectedFlag,
                mSelectedLockId,
                mSelectedLockName,
                true,
                ConstState.Sign,
                ConstState.GWLZSend,
                false);
        }
        else
        {
            intentOpenFile(mSelectedPath,
                mSelectedFileTitle,
                mSelectedDocId,
                mType,
                mHandleType,
                mSelectedFlag,
                mSelectedLockId,
                mSelectedLockName,
                true,
                ConstState.NoSign,
                ConstState.NoSend,
                false);
        }
    }
    
    /**
     * 修改列表数据，删除或更新列表
     * 
     * @Description<功能详细描述>
     * 
     * @param docId
     * @param flag true 增加， false ： 删除
     * @LastModifiedDate：2013-11-14
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public void updateListView(String docId, boolean flag)
    {
        List<GetDocListInfo> newLists = new ArrayList<GetDocListInfo>();
        List<GetDocListInfo> oldLists = new ArrayList<GetDocListInfo>();
        if (isHomePage)
        {
            if (mHomeDaiBanAdapter != null)
            {
                oldLists = mHomeDaiBanAdapter.getDocLists();
            }
        }
        else
        {
            if (mdocListAdapter != null)
            {
                oldLists = mdocListAdapter.getDocLists();
            }
        }
        if (oldLists != null)
        {
            int i = -1;
            int size = oldLists.size();
            if (oldLists.size() > 0)
            {
                for (int n = 0; n < size; n++)
                {
                    
                    GetDocListInfo info = oldLists.get(n);
                    if (info.getStringKeyValue(GetDocListInfo.docid).equals(docId))
                    {
                        info.getLocaldatalist().put(GetDocListInfo.readstatus, "0");
                    }
                    newLists.add(info);
                }
                
                for (i = 0; i < size; i++)
                {
                    GetDocListInfo info = newLists.get(i);
                    if (info.getStringKeyValue(GetDocListInfo.docid).equals(docId))
                    {
                        if (!flag)
                        {
                            newLists.remove(i);
                        }
                        break;
                    }
                }
            }
            if (flag && (i == size))
            {
                String wheres =
                    GetDocListInfo.l_user + "=?" + " and " + GetDocListInfo.l_type + "=?" + " and "
                        + GetDocListInfo.l_handletype + "=?" + " and " + GetDocListInfo.docid + "=?";
                String[] selectionArgs = {user, mType, mHandleType, docId};
                
                GetDocListInfo info = new GetDocListInfo();
                List<HashMap<String, Object>> ret = info.query(null, wheres, selectionArgs, null);
                
                GetDocListInfo detail = null;
                if (ret != null && ret.size() > 0)
                {
                    detail = new GetDocListInfo(user, mType, mHandleType);
                    detail.convertToObject(ret.get(0));
                }
                if (detail != null)
                {
                    newLists.add(detail);
                }
            }
            
            Message msg = new Message();
            msg.what = ConstState.REFRESHLIST;
            msg.obj = newLists;
            
            bHandler.sendMessage(msg);
        }
    }
    
    /**
     * 文件被打开后，更新文件的已读未读状态标识
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-14
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public void updataDatabase()
    {
        
        String wheres =
            GetDocListInfo.l_user + "=?" + " and " + GetDocListInfo.l_type + "=?" + " and "
                + GetDocListInfo.l_handletype + "=?" + " and " + GetDocListInfo.docid + "=?";
        String[] selectionArgs = {user, mType, mHandleType, mSelectedDocId};
        
        ContentValues values = new ContentValues();
        values.put(GetDocListInfo.readstatus, ConstState.BACK_HasReadDOCLIST);
        
        GetDocListInfo info = new GetDocListInfo();
        info.update(values, wheres, selectionArgs);
    }
    
    @Override
    public int getDocCount()
    {
        if (mdocListAdapter != null)
        {
            return mdocListAdapter.getCount();
        }
        
        return 0;
    }
    
    @Override
    public void initListViewAdapter()
    {
        // TODO Auto-generated method stub
        List<GetDocListInfo> mDocList = new ArrayList<GetDocListInfo>();
        if (isHomePage)
        {
            mHomeDaiBanAdapter = new HomeDaiBanAdapter(mActivtiy, mDocList, user);
            mLvMain.setAdapter(mHomeDaiBanAdapter);
        }
        else
        {
            mdocListAdapter = new OfficalDocSecondAdapter(mActivtiy, mDocList, user, mType);
            mLvMain.setAdapter(mdocListAdapter);
        }
    }
    
    @Override
    public void setListViewAdapter(int selectCount)
    {
        // TODO Auto-generated method stub
        if (isHomePage)
        {
            mLvMain.setAdapter(mHomeDaiBanAdapter);
            if (selectCount == -1)
            {
                mLvMain.setSelection(mHomeDaiBanAdapter.getCount());
            }
        }
        else
        {
            mLvMain.setAdapter(mdocListAdapter);
            if (selectCount == -1)
            {
                mLvMain.setSelection(mdocListAdapter.getCount());
            }
        }
    }
    
    @Override
    public void getFileContentEnd(boolean flag)
    {
        // TODO Auto-generated method stub
        if (flag)
        {
            OpenPDFFile();
            dealListView();
        }
        else
        {
            Toast.makeText(mActivtiy, "保存数据库失败！", Toast.LENGTH_SHORT).show();
        }
        isDownling = false;
        
        if (isHomePage)
        {
            mHomeDaiBanAdapter.setmSelectedPosition(-1);
            pdfLoadLayout.setVisibility(View.GONE);
        }
        else
        {
            mdocListAdapter.setmSelectedPosition(-1);
            mPopupWindow.dismiss();
        }
    }
    
    @Override
    public Object dealDatafromNetWork(List<HashMap<String, Object>> ret)
    {
        // TODO Auto-generated method stub
        List<GetDocListInfo> lists = DBDataObjectWrite.insertDocList(ret, user, mType, mHandleType, mParentDirPath);
        return lists;
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
    class DocListDateComparator implements Comparator<GetDocListInfo>
    {
        /**
         * 比较器方法 按照里面的 按日期逆排序
         */
        @Override
        public int compare(GetDocListInfo lhs, GetDocListInfo rhs)
        {
            return (rhs.getStringKeyValue(GetDocListInfo.createdatetime).compareToIgnoreCase(lhs.getStringKeyValue(GetDocListInfo.createdatetime)));
        }
    }
    
}
