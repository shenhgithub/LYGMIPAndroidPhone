/*
 * File name:  LeaderPiShiListView.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2013-11-19
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.leaderPiShi;

import java.io.File;
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

import com.hoperun.manager.adpter.homePage.HomePiShiAdapter;
import com.hoperun.manager.adpter.leaderPishi.LeaderPiShiAdapter;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.FileUtil;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.model.entityMetaData.GetLeaderPiShiListInfo;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.BaseListView;

/**
 * 领导批示列表
 * 
 * @Description 领导批示列表
 * 
 * @author wang_ling
 * @Version [版本号, 2013-11-19]
 */
public class LeaderPiShiListView extends BaseListView
{
    
    /** 文件adapter **/
    private LeaderPiShiAdapter     mdocListAdapter;
    
    /** 主页弹出框 **/
    private HomePiShiAdapter       mHomePopListAdapter;
    
    private GetLeaderPiShiListInfo mSelectedInfo;
    
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        // TODO Auto-generated method stub
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETLEADERPISHILISTREQUEST:
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
                case RequestTypeConstants.GETLEADERPISHICONTNT:
                    
                    MetaResponseBody getContentBuzBody = (MetaResponseBody)objBody;
                    if (getContentBuzBody.getRetError().equals("0"))
                    {
                        List<HashMap<String, Object>> buzList = getContentBuzBody.getBuzList();
                        
                        if (buzList != null && buzList.size() > 0)
                        {
                            saveFileInfo(buzList, user, "", "", mSelectedDocId, mSelectedFileTitle, ConstState.LDPS);
                        }
                        else
                        {
                            isDownling = false;
                            
                            if (isHomePage)
                            {
                                pdfLoadLayout.setVisibility(View.GONE);
                                mHomePopListAdapter.setmSelectedPosition(-1);
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
                            mHomePopListAdapter.setmSelectedPosition(-1);
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
                case RequestTypeConstants.GETLEADERPISHILISTREQUEST:
                    doGetDocListError(errorCode);
                    break;
                
                case RequestTypeConstants.GETLEADERPISHICONTNT:
                    
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
    
    /**
     * <默认构造函数>
     */
    public LeaderPiShiListView(Context context)
    {
        super(context);
    }
    
    public LeaderPiShiListView(Activity activity, String keyWords, String type, String readStatus,
        String parentDirPath, OnTouchListener listen, boolean isHomePage)
    {
        super(activity, keyWords, type, "", readStatus, parentDirPath, listen, isHomePage, "", false);
    }
    
    @Override
    public void initListViewAdapter()
    {
        // TODO Auto-generated method stub
        List<GetLeaderPiShiListInfo> mDocList = new ArrayList<GetLeaderPiShiListInfo>();
        if (isHomePage)
        {
            mHomePopListAdapter = new HomePiShiAdapter(mActivtiy, mDocList, user);
            mLvMain.setAdapter(mHomePopListAdapter);
        }
        else
        {
            mdocListAdapter = new LeaderPiShiAdapter(mActivtiy, mDocList, user);
            mLvMain.setAdapter(mdocListAdapter);
        }
    }
    
    @Override
    public void setListViewAdapter(int selectCount)
    {
        // TODO Auto-generated method stub
        if (isHomePage)
        {
            mLvMain.setAdapter(mHomePopListAdapter);
            if (selectCount == -1)
            {
                mLvMain.setSelection(mHomePopListAdapter.getCount());
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
    public Object dealDatafromNetWork(List<HashMap<String, Object>> ret)
    {
        // TODO Auto-generated method stub
        List<GetLeaderPiShiListInfo> lists = DBDataObjectWrite.insertLeaderPiShiList(ret, user, mType, mParentDirPath);
        
        return lists;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object addDataToListRefresh(Object obj, boolean isDown)
    {
        // TODO Auto-generated method stub
        List<GetLeaderPiShiListInfo> newList = new ArrayList<GetLeaderPiShiListInfo>();
        if (isDown)
        {
            // mDocList.clear();
            
            if (isHomePage)
            {
                if (mHomePopListAdapter != null && mHomePopListAdapter.getDocLists() != null)
                {
                    for (int i = 0; i < mHomePopListAdapter.getDocLists().size(); i++)
                    {
                        newList.add(mHomePopListAdapter.getDocLists().get(i));
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
        List<GetLeaderPiShiListInfo> lists = (List<GetLeaderPiShiListInfo>)obj;
        for (int i = 0; i < lists.size(); i++)
        {
            GetLeaderPiShiListInfo info = lists.get(i);
            newList.add(info);
        }
        
        return newList;
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
        
        GetLeaderPiShiListInfo test1 = new GetLeaderPiShiListInfo();
        
        List<HashMap<String, Object>> queryret;
        if (null == mSearchkeyWords || mSearchkeyWords.equals(""))
        {
            
            String where = "";
            
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                where =
                    GetLeaderPiShiListInfo.l_user + " = ?" + " and " + GetLeaderPiShiListInfo.l_type + " = ?" + " and "
                        + GetLeaderPiShiListInfo.createdatetime + "< ?";
            }
            else
            {
                where =
                    GetLeaderPiShiListInfo.l_user + " = ?" + " and " + GetLeaderPiShiListInfo.l_type + " = ?" + " and "
                        + GetLeaderPiShiListInfo.createdatetime + "< ?" + " and " + GetLeaderPiShiListInfo.readstatus
                        + " =? ";
            }
            
            String[] selectionArgs = null;
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                selectionArgs = new String[] {user, mType, mtime};
            }
            else
            {
                selectionArgs = new String[] {user, mType, mtime, mReadFlag};
            }
            
            String sortOrder = GetLeaderPiShiListInfo.createdatetime + " desc ";
            
            String limitString = " limit " + pageSize;
            
            queryret = test1.query(null, where, selectionArgs, sortOrder + limitString);
        }
        else
        {
            String where = "";
            
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                where =
                    GetLeaderPiShiListInfo.l_user + " = ?" + " and " + GetLeaderPiShiListInfo.l_type + " = ?" + " and "
                        + GetLeaderPiShiListInfo.title + " like ?" + " and " + GetLeaderPiShiListInfo.createdatetime
                        + "< ?";
            }
            else
            {
                where =
                    GetLeaderPiShiListInfo.l_user + " = ?" + " and " + GetLeaderPiShiListInfo.l_type + " = ?" + " and "
                        + GetLeaderPiShiListInfo.title + " like ?" + " and " + GetLeaderPiShiListInfo.createdatetime
                        + "< ?" + " and " + GetLeaderPiShiListInfo.readstatus + " =? ";
            }
            
            String[] selectionArgs = null;
            
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                selectionArgs = new String[] {user, mType, "%" + mSearchkeyWords + "%", mtime};
            }
            else
            {
                selectionArgs = new String[] {user, mType, "%" + mSearchkeyWords + "%", mtime, mReadFlag};
            }
            
            String sortOrder = GetLeaderPiShiListInfo.createdatetime + " desc ";
            
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
                setNoListDataView(0);
            }
            isGetingDocList = IDLE;
        }
        else
        {
            List<GetLeaderPiShiListInfo> doclists = new ArrayList<GetLeaderPiShiListInfo>();
            
            for (int i = 0; i < queryret.size(); i++)
            {
                GetLeaderPiShiListInfo info = new GetLeaderPiShiListInfo();
                info.convertToObject(queryret.get(i));
                doclists.add(info);
            }
            
            addDataToListAndRefresh(doclists, true);
            isGetingDocList = IDLE;
            setNoListDataView(1);
        }
    }
    
    /**
     * 
     * 从网络获取数据
     * 
     * @Description 从网络获取数据
     * 
     * @param time 最后一条的时间
     * @LastModifiedDate：2013-11-20
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
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
            body.put("type", mType);// ConstState.LDPS_TYPE mType
            body.put("handletype", "");// mType
            body.put("readstatus", mReadStatus);// mReadStatus
            
            mGetDocListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETLEADERPISHILISTREQUEST).create();
            
            NetRequestController.getDocListRequest(mGetDocListRequst,
                mHandler,
                RequestTypeConstants.GETLEADERPISHILISTREQUEST,
                body);
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
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
    class DocListDateComparator implements Comparator<GetLeaderPiShiListInfo>
    {
        /**
         * 比较器方法 按照里面的 按日期逆排序
         */
        @Override
        public int compare(GetLeaderPiShiListInfo lhs, GetLeaderPiShiListInfo rhs)
        {
            return (rhs.getStringKeyValue(GetLeaderPiShiListInfo.createdatetime).compareToIgnoreCase(lhs.getStringKeyValue(GetLeaderPiShiListInfo.createdatetime)));
        }
    }
    
    /**
     * 刷新listview
     * 
     * @Description<功能详细描述>
     * 
     * @param mDocLists
     * @param lists
     * @param isDown
     * @LastModifiedDate：2013-11-6
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    @SuppressWarnings("unchecked")
    @Override
    public void reFreshList(Object obj)
    {
        List<GetLeaderPiShiListInfo> mDocLists = (List<GetLeaderPiShiListInfo>)obj;
        Collections.sort(mDocLists, new DocListDateComparator());
        
        // 当前列表中的列表数量
        mCount = mDocLists.size();
        if (mCount > 0)
        {
            // 获取最后一条的时间
            mRefreshLastTime = mDocLists.get(mCount - 1).getStringKeyValue(GetLeaderPiShiListInfo.createdatetime);
        }
        if (isHomePage)
        {
            mHomePopListAdapter.setDocLists(mDocLists);
            mHomePopListAdapter.notifyDataSetChanged();
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
    
    /**
     * 
     * 打开PDF
     * 
     * @LastModifiedDate：2013-10-10
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    private void openPDFFile()
    {
        intentOpenFile(mSelectedPath,
            mSelectedFileTitle,
            mSelectedDocId,
            "",
            "",
            mSelectedFlag,
            mSelectedLockId,
            mSelectedLockName,
            true,
            ConstState.NoSign,
            ConstState.NoSend,
            false);
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
            GetLeaderPiShiListInfo.l_user + "=?" + " and " + GetLeaderPiShiListInfo.l_type + "=?" + " and "
                + GetLeaderPiShiListInfo.docid + "=?";
        String[] selectionArgs = {user, mType, mSelectedDocId};
        
        ContentValues values = new ContentValues();
        values.put(GetLeaderPiShiListInfo.readstatus, ConstState.BACK_HasReadDOCLIST);
        
        GetLeaderPiShiListInfo info = new GetLeaderPiShiListInfo();
        info.update(values, wheres, selectionArgs);
    }
    
    @Override
    public void onClickLisItem(int position)
    {
        // TODO Auto-generated method stub
        if (isHomePage)
        {
            mSelectedInfo = (GetLeaderPiShiListInfo)mHomePopListAdapter.getItem(position);
        }
        else
        {
            mSelectedInfo = (GetLeaderPiShiListInfo)mdocListAdapter.getItem(position);
        }
        
        mSelectedDocId = mSelectedInfo.getStringKeyValue(GetLeaderPiShiListInfo.docid);
        
        mSelectedFileTitle = mSelectedInfo.getStringKeyValue(GetLeaderPiShiListInfo.title);
        
        mSelectedFlag = mSelectedInfo.getStringKeyValue(GetLeaderPiShiListInfo.lockstatus);
        
        mSelectedLockId = mSelectedInfo.getStringKeyValue(GetLeaderPiShiListInfo.lockuserid);
        
        mSelectedLockName = mSelectedInfo.getStringKeyValue(GetLeaderPiShiListInfo.lockusername);
        
        String docurl = mSelectedInfo.getStringKeyValue(GetLeaderPiShiListInfo.docurl);
        
        mSelectedPath = DBDataObjectWrite.getFilePath(user, mSelectedDocId, ConstState.LDPS);
        
        File file = new File(mSelectedPath);
        
        if (offLine)
        {
            
            if (file.exists())
            {
                if (isHomePage)
                {
                    mHomePopListAdapter.setmSelectedPosition(position);
                    mHomePopListAdapter.notifyDataSetChanged();
                }
                else
                {
                    mdocListAdapter.setmSelectedPosition(position);
                    mdocListAdapter.notifyDataSetChanged();
                }
                openPDFFile();
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
                    mHomePopListAdapter.setmSelectedPosition(position);
                    mHomePopListAdapter.notifyDataSetChanged();
                }
                else
                {
                    mdocListAdapter.setmSelectedPosition(position);
                    mdocListAdapter.notifyDataSetChanged();
                }
                openPDFFile();
            }
            else
            {
                if (isHomePage)
                {
                    mHomePopListAdapter.setmSelectedPosition(position);
                    mHomePopListAdapter.notifyDataSetChanged();
                    if (!getFileContent(mSelectedDocId, docurl, mParentDirPath))
                    {
                        mHomePopListAdapter.setmSelectedPosition(-1);
                        mHomePopListAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    mdocListAdapter.setmSelectedPosition(position);
                    mdocListAdapter.notifyDataSetChanged();
                    if (!getFileContent(mSelectedDocId, docurl, mParentDirPath))
                    {
                        mdocListAdapter.setmSelectedPosition(-1);
                        mdocListAdapter.notifyDataSetChanged();
                    }
                }
                
            }
            
        }
    }
    
    @Override
    public void getFileContentEnd(boolean flag)
    {
        // TODO Auto-generated method stub
        if (flag)
        {
            openPDFFile();
            dealListView();
        }
        else
        {
            Toast.makeText(mActivtiy, "保存数据库失败！", Toast.LENGTH_SHORT).show();
        }
        isDownling = false;
        if (isHomePage)
        {
            pdfLoadLayout.setVisibility(View.GONE);
            // mHomePopListAdapter.setmSelectedPosition(-1);
        }
        else
        {
            mPopupWindow.dismiss();
            // mdocListAdapter.setmSelectedPosition(-1);
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
    public boolean getFileContent(String docid, String docurl, String path)
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
            body.put("docurl", docurl);
            body.put("type", "");
            
            mGetFileContentRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETLEADERPISHICONTNT).create();
            
            NetRequestController.getFileContent(mGetFileContentRequst,
                mHandler,
                RequestTypeConstants.GETLEADERPISHICONTNT,
                body,
                path);
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return false;
        }
        
        return true;
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
    public void updateListView(String docId, boolean flag)
    {
        List<GetLeaderPiShiListInfo> mDocList = new ArrayList<GetLeaderPiShiListInfo>();
        if (isHomePage)
        {
            if (mHomePopListAdapter != null)
            {
                mDocList = mHomePopListAdapter.getDocLists();
            }
        }
        else
        {
            if (mdocListAdapter != null)
            {
                mDocList = mdocListAdapter.getDocLists();
            }
        }
        
        if (mDocList != null)
        {
            int i = -1;
            if (mDocList.size() > 0)
            {
                for (i = 0; i < mDocList.size(); i++)
                {
                    GetLeaderPiShiListInfo info = mDocList.get(i);
                    if (info.getStringKeyValue(GetLeaderPiShiListInfo.docid).equals(docId))
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
                    GetLeaderPiShiListInfo.l_user + "=?" + " and " + GetLeaderPiShiListInfo.l_type + "=?" + " and "
                        + GetLeaderPiShiListInfo.docid + "=?";
                String[] selectionArgs = {user, mType, docId};
                
                GetLeaderPiShiListInfo info = new GetLeaderPiShiListInfo();
                List<HashMap<String, Object>> ret = info.query(null, wheres, selectionArgs, null);
                
                GetLeaderPiShiListInfo detail = null;
                if (ret != null && ret.size() > 0)
                {
                    detail = new GetLeaderPiShiListInfo(user, mType);
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
