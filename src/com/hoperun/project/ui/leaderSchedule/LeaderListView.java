/*
 * File name:  LeaderListView.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2013-11-13
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.leaderSchedule;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.hoperun.manager.adpter.leaderSchedule.LeaderDocAdapter;
import com.hoperun.manager.components.CustomHandler.CustomHanler;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.FileUtil;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.model.entityMetaData.GetLeaderDocListInfo;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.BaseListView;

/**
 * 领导日程列表
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2013-11-13]
 */
public class LeaderListView extends BaseListView
{
    
    /**
     * 正在加载过程中
     */
    private boolean              isLoading  = false;
    
    /**
     * 时间周期（0全部、1周、2月、3季度、4年）
     */
    private String               cycletime  = "0";
    
    /** 文件adapter **/
    private LeaderDocAdapter     mdocListAdapter;
    
    /**
     * 正在下载
     */
    private boolean              isDownling = false;
    
    private GetLeaderDocListInfo mSelectedInfo;
    
    private CustomHanler         mHandler   = new CustomHanler()
                                            {
                                                
                                                @Override
                                                public void PostHandle(int requestType, Object objHeader,
                                                    Object objBody, boolean error, int errorCode)
                                                {
                                                    onPostHandle(requestType, objHeader, objBody, error, errorCode);
                                                }
                                                
                                            };
    
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {// 获取领导日程列表
                case RequestTypeConstants.GETLEADERDOCREQUEST:
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
                case RequestTypeConstants.GETLEADERFILECONTENT:
                    mdocListAdapter.setmSelectedPosition(-1);
                    mdocListAdapter.notifyDataSetChanged();
                    
                    MetaResponseBody getContentBuzBody = (MetaResponseBody)objBody;
                    if (getContentBuzBody.getRetError().equals("0"))
                    {
                        List<HashMap<String, Object>> buzList = getContentBuzBody.getBuzList();
                        
                        if (buzList != null && buzList.size() > 0)
                        {
                            saveFileInfo(buzList, user, "", "", mSelectedDocId, mSelectedFileTitle, ConstState.LDRC);
                        }
                        else
                        {
                            isDownling = false;
                            mPopupWindow.dismiss();
                            Toast.makeText(mActivtiy, "下载文件失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        isDownling = false;
                        mPopupWindow.dismiss();
                        Toast.makeText(mActivtiy, "下载文件失败！", Toast.LENGTH_SHORT).show();
                    }
                    
                    break;
                case RequestTypeConstants.PROGRESS_BAR:
                    // 返回进度值
                    mDownloadfilePro.setProgress((Integer)objBody);
                    
                    break;
                default:
                    break;
            }
        }
        else
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETLEADERDOCREQUEST:
                    if (isGetingDocList == ConstState.UPREFRESH)
                    {
                        mFootView.setVisibility(GONE);
                        if (errorCode != ConstState.CANCEL_THREAD)
                        {
                            Toast.makeText(mActivtiy, "获取列表失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        if (mLvMain.isDone())
                        {
                            mLvMain.onRefreshComplete();
                        }
                        if (errorCode != ConstState.CANCEL_THREAD)
                        {
                            Toast.makeText(mActivtiy, "获取列表失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    closeProgressDialog();
                    isGetingDocList = IDLE;
                    break;
                
                case RequestTypeConstants.GETLEADERFILECONTENT:
                    mdocListAdapter.setmSelectedPosition(-1);
                    mdocListAdapter.notifyDataSetChanged();
                    if (errorCode != ConstState.CANCEL_THREAD)
                    {
                        Toast.makeText(mActivtiy, "下载文件失败！", Toast.LENGTH_SHORT).show();
                    }
                    isDownling = false;
                    mPopupWindow.dismiss();
                    break;
                
                default:
                    break;
            }
        }
    }
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context context
     * @param attrs attrs
     */
    public LeaderListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    /**
     * <默认构造函数>
     * 
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public LeaderListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 应用上下文
     * @param keyWords 搜索关键字
     * @param readStatus 已读/未读标记
     * @param parentDirPath 路径
     * @param listen listener
     */
    public LeaderListView(Activity activity, String keyWords, String readStatus, String parentDirPath,
        OnTouchListener listen)
    {
        super(activity, keyWords, "", "", readStatus, parentDirPath, listen, false, "", false);
    }
    
    @Override
    public void initListViewAdapter()
    {
        // TODO Auto-generated method stub
        List<GetLeaderDocListInfo> mDocList = new ArrayList<GetLeaderDocListInfo>();
        
        mdocListAdapter = new LeaderDocAdapter(mActivtiy, mDocList, user, mReadStatus.equals("1"));
        mLvMain.setAdapter(mdocListAdapter);
    }
    
    @Override
    public void setListViewAdapter(int selectCount)
    {
        // TODO Auto-generated method stub
        mLvMain.setAdapter(mdocListAdapter);
        if (selectCount == -1)
        {
            mLvMain.setSelection(mdocListAdapter.getCount());
        }
        
    }
    
    @Override
    public Object dealDatafromNetWork(List<HashMap<String, Object>> ret)
    {
        List<GetLeaderDocListInfo> lists = DBDataObjectWrite.insertLeaderDocList(ret, user, mParentDirPath);
        return lists;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object addDataToListRefresh(Object obj, boolean isDown)
    {
        List<GetLeaderDocListInfo> newList = new ArrayList<GetLeaderDocListInfo>();
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
        
        List<GetLeaderDocListInfo> lists = (List<GetLeaderDocListInfo>)obj;
        for (int i = 0; i < lists.size(); i++)
        {
            GetLeaderDocListInfo info = lists.get(i);
            newList.add(info);
        }
        
        return newList;
    }
    
    /**
     * 
     * 刷新列表
     * 
     * @Description 刷新列表
     * 
     * @param mDocLists 列表数据
     * @LastModifiedDate：2013-11-13
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @SuppressWarnings("unchecked")
    @Override
    public void reFreshList(Object obj)
    {
        List<GetLeaderDocListInfo> mDocLists = (List<GetLeaderDocListInfo>)obj;
        Collections.sort(mDocLists, new DocListDateComparator());
        
        // 当前列表中的列表数量
        mCount = mDocLists.size();
        if (mCount > 0)
        {
            // 获取最后一条的时间
            mRefreshLastTime = mDocLists.get(mCount - 1).getStringKeyValue(GetLeaderDocListInfo.scheduledate);
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
     * 
     * 比较器
     * 
     * @Description 比较器
     * 
     * @author wang_ling
     * @Version [版本号, 2013-10-18]
     */
    class DocListDateComparator implements Comparator<GetLeaderDocListInfo>
    {
        /**
         * 比较器方法 按照里面的 按日期逆排序
         */
        @Override
        public int compare(GetLeaderDocListInfo lhs, GetLeaderDocListInfo rhs)
        {
            return (rhs.getStringKeyValue(GetLeaderDocListInfo.scheduledate).compareToIgnoreCase(lhs.getStringKeyValue(GetLeaderDocListInfo.scheduledate)));
        }
    }
    
    /**
     * "cycletime": "0", 　　　　　　"currendatetime": "2013-10-30 11:17:30", "pagesize": "10" "keyword": "" 通过网络获取领导日程列表
     * 
     * @Description 通过网络获取领导日程列表
     * 
     * @param time
     * @LastModifiedDate：2013-11-13
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
            body.put("keyword", mSearchkeyWords);
            body.put("currendatetime", time);
            body.put("pagesize", pageSize);
            body.put("cycletime", cycletime);
            
            mGetDocListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETLEADERDOCREQUEST).create();
            
            NetRequestController.getLeaderFileList(mGetDocListRequst,
                mHandler,
                RequestTypeConstants.GETLEADERDOCREQUEST,
                body);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
    }
    
    /**
     * 
     * 获取本地列表
     * 
     * @Description 获取本地列表
     * 
     * @param mtime mtime
     * @LastModifiedDate：2013-11-13
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public void getDocListFromDBData(String mtime)// GetDocListInfo
    {
        closeProgressDialog();
        GetLeaderDocListInfo test1 = new GetLeaderDocListInfo();
        
        List<HashMap<String, Object>> queryret;
        if (null == mSearchkeyWords || mSearchkeyWords.equals(""))
        {
            // GetLeaderDocListInfo.l_read + " = ?" + " and "+
            String where = GetLeaderDocListInfo.l_user + " = ?" + " and " + GetLeaderDocListInfo.scheduledate + "< ?";
            
            String[] selectionArgs = {user, mtime};
            
            String sortOrder = GetLeaderDocListInfo.scheduledate + " desc ";
            
            String limitString = " limit " + pageSize;
            
            queryret = test1.query(null, where, selectionArgs, sortOrder + limitString);
        }
        else
        {
            // GetLeaderDocListInfo.l_read + " = ?" + " and "+
            
            String where =
                GetLeaderDocListInfo.l_user + " = ?" + " and " + GetLeaderDocListInfo.scheduletitle + " like ?"
                    + " and " + GetLeaderDocListInfo.scheduledate + "< ?";
            
            String[] selectionArgs = {user, "%" + mSearchkeyWords + "%", mtime};
            
            String sortOrder = GetLeaderDocListInfo.scheduledate + " desc ";
            
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
            List<GetLeaderDocListInfo> doclists = new ArrayList<GetLeaderDocListInfo>();
            
            for (int i = 0; i < queryret.size(); i++)
            {
                GetLeaderDocListInfo info = new GetLeaderDocListInfo();
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
        mSelectedInfo = (GetLeaderDocListInfo)mdocListAdapter.getItem(position);
        
        mSelectedDocId = mSelectedInfo.getStringKeyValue(GetLeaderDocListInfo.scheduleid);
        
        mSelectedScheduleUrl = mSelectedInfo.getStringKeyValue(GetLeaderDocListInfo.scheduleurl);
        
        mSelectedFileTitle = mSelectedInfo.getStringKeyValue(GetLeaderDocListInfo.scheduletitle);
        
        mSelectedPath = DBDataObjectWrite.getFilePath(user, mSelectedDocId, ConstState.LDRC);
        
        File file = new File(mSelectedPath);
        
        if (offLine)
        {
            if (file.exists())
            {
                mdocListAdapter.setmSelectedPosition(position);
                mdocListAdapter.notifyDataSetChanged();
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
                mdocListAdapter.setmSelectedPosition(position);
                mdocListAdapter.notifyDataSetChanged();
                openPDFFile();
            }
            else
            {
                mdocListAdapter.setmSelectedPosition(position);
                mdocListAdapter.notifyDataSetChanged();
                if (!getFileContent(mSelectedDocId, mSelectedScheduleUrl, mParentDirPath))
                {
                    mdocListAdapter.setmSelectedPosition(-1);
                    mdocListAdapter.notifyDataSetChanged();
                }
                
            }
            
        }
    }
    
    /**
     * 获取 isLoading
     * 
     * @return 返回 isLoading
     * @author wang_ling
     */
    public boolean isLoading()
    {
        return isLoading;
    }
    
    /**
     * 设置 isLoading
     * 
     * @param isLoading 对isLoading进行赋值
     * @author wang_ling
     */
    public void setLoading(boolean isLoading)
    {
        this.isLoading = isLoading;
    }
    
    /**
     * 
     * 获取pdf文件
     * 
     * @Description 获取pdf文件
     * 
     * @param docid 文件id
     * @param path 文件路径
     * @return boolean
     * @LastModifiedDate：2013-11-14
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public boolean getFileContent(String scheduleid, String scheduleurl, String path)
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
        
        showDownloadProgress();
        
        JSONObject body = new JSONObject();
        
        try
        {
            body.put("scheduleid", scheduleid);
            body.put("scheduleurl", scheduleurl);
            body.put("schedutype", "0");
            
            mGetFileContentRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETLEADERFILECONTENT).create();
            
            NetRequestController.getLeaderFileContent(mGetFileContentRequst,
                mHandler,
                RequestTypeConstants.GETLEADERFILECONTENT,
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
        
        // mdocListAdapter.setmSelectedPosition(-1);
        mPopupWindow.dismiss();
        
    }
    
    /**
     * 
     * 文件打开后更新数据库为已读
     * 
     * @Description<功能详细描述>
     * 
     * @param mSelectedInfo
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public void updataDatabase()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                DBDataObjectWrite.updateLeaderDocList(mSelectedInfo);
            }
            
        }.start();
    }
    
    /**
     * 
     * 更新列表
     * 
     * @Description 更新列表
     * 
     * @LastModifiedDate：2013-11-20
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public void updateListView(String docId, boolean flag)
    {
        List<GetLeaderDocListInfo> newLists = new ArrayList<GetLeaderDocListInfo>();
        if (mdocListAdapter != null && mdocListAdapter.getDocLists() != null)
        {
            int size = mdocListAdapter.getDocLists().size();
            if (mdocListAdapter.getDocLists().size() > 0)
            {
                for (int n = 0; n < size; n++)
                {
                    GetLeaderDocListInfo info = mdocListAdapter.getDocLists().get(n);
                    if (info.getStringKeyValue(GetLeaderDocListInfo.scheduleid).equals(docId))
                    {
                        info.getLocaldatalist().put(GetLeaderDocListInfo.l_read, "1");
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
            "",
            "",
            "",
            false,
            ConstState.NoSign,
            ConstState.NoSend,
            false);
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
