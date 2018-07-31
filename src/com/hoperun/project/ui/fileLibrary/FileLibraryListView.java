/*
 * File name:  FileLibraryListView.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2013-11-18
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.fileLibrary;

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
import android.view.View;
import android.widget.Toast;

import com.hoperun.manager.adpter.fileLibrary.FileLibrarySecondAdapter;
import com.hoperun.manager.adpter.homePage.HomeKanWuAdapter;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.FileUtil;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.model.entityMetaData.GetFileLibraryDocListInfo;
import com.hoperun.mipmanager.model.entityMetaData.GetLeaderDocListInfo;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.BaseListView;

/**
 * 文档库列表
 * 
 * @Description 文档库列表
 * 
 * @author wang_ling
 * @Version [版本号, 2013-11-18]
 */
public class FileLibraryListView extends BaseListView
{
    
    /** 文件adapter **/
    private FileLibrarySecondAdapter  mdocListAdapter;
    
    /** 主页最新刊物adapter ***/
    private HomeKanWuAdapter          mHomeKanWuAdapter;
    
    private GetFileLibraryDocListInfo mSelectedInfo;
    
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETFILELIBRARYDOCREQUEST:
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
                case RequestTypeConstants.GETFILELIBRARYCONTENT:
                    MetaResponseBody getContentBuzBody = (MetaResponseBody)objBody;
                    
                    if (getContentBuzBody.getRetError().equals("0"))
                    {
                        List<HashMap<String, Object>> buzList = getContentBuzBody.getBuzList();
                        
                        if (buzList != null && buzList.size() > 0)
                        {
                            
                            saveFileInfo(buzList, user, mType, "", mSelectedDocId, mSelectedFileTitle, ConstState.WDK);
                        }
                        else
                        {
                            isDownling = false;
                            
                            if (isHomePage)
                            {
                                pdfLoadLayout.setVisibility(View.GONE);
                                mHomeKanWuAdapter.setmSelectedPosition(-1);
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
                            mHomeKanWuAdapter.setmSelectedPosition(-1);
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
                case RequestTypeConstants.GETFILELIBRARYDOCREQUEST:
                    doGetDocListError(errorCode);
                    break;
                
                case RequestTypeConstants.GETFILELIBRARYCONTENT:
                    if (isHomePage)
                    {
                        mHomeKanWuAdapter.setmSelectedPosition(-1);
                        mHomeKanWuAdapter.notifyDataSetChanged();
                        pdfLoadLayout.setVisibility(View.GONE);
                    }
                    else
                    {
                        mdocListAdapter.setmSelectedPosition(-1);
                        mdocListAdapter.notifyDataSetChanged();
                        mPopupWindow.dismiss();
                    }
                    
                    if (errorCode != ConstState.CANCEL_THREAD)
                    {
                        Toast.makeText(mActivtiy, "下载文件失败！", Toast.LENGTH_SHORT).show();
                    }
                    
                    isDownling = false;
                    
                    break;
                
                default:
                    break;
            }
        }
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
    // private void updateDatabase(final GetFileLibraryDocListInfo mSelectedInfo)
    // {
    // new Thread()
    // {
    //
    // /**
    // * 重载方法
    // *
    // * @author wang_ling
    // */
    // @Override
    // public void run()
    // {
    // DBDataObjectWrite.updateFileLibraryDocList(mSelectedInfo);
    // }
    //
    // }.start();
    // }
    
    /**
     * 
     * 处理返回的报文
     * 
     * @Description 处理返回的报文
     * 
     * @param ret 返回报文
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    // private void dealgetBackDocList(List<HashMap<String, Object>> ret)
    // {
    // // 列表增量更新
    // if (isGetingDocList == UPREFRESH)
    // {
    // mFootView.setVisibility(View.GONE);
    //
    // if (ret == null || ret.size() == 0)
    // {// 没有数据
    // if (isHomePage)
    // {
    // if (mLvMain.getFooterViewsCount() != 0)
    // {
    // mLvMain.setAdapter(mHomeKanWuAdapter);
    // mLvMain.setSelection(mHomeKanWuAdapter.getCount());
    // mLvMain.removeFooterView(mFootView);
    // }
    // }
    // else
    // {
    // if (mLvMain.getFooterViewsCount() != 0)
    // {
    // mLvMain.setAdapter(mdocListAdapter);
    // mLvMain.setSelection(mdocListAdapter.getCount());
    // mLvMain.removeFooterView(mFootView);
    // }
    // }
    //
    // Toast.makeText(mActivtiy, "没有更多的数据", Toast.LENGTH_SHORT).show();
    // }
    // else
    // {
    // LogUtil.d("size", ret.size() + "");
    // dealBackNetData(ret, true);
    // }
    // }
    // // 先清除列表数据，再刷新
    // else
    // {
    // if (mLvMain.isDone())
    // {
    // mLvMain.onRefreshComplete();
    // }
    // if (mLvMain.getFooterViewsCount() == 0)
    // {
    // mLvMain.addFooterView(mFootView);
    // mFootView.setVisibility(View.GONE);
    // }
    //
    // if (ret == null || ret.size() == 0)
    // {// 没有数据
    // if (mLvMain.getFooterViewsCount() != 0)
    // {
    // if (isHomePage)
    // {
    // mLvMain.setAdapter(mHomeKanWuAdapter);
    // }
    // else
    // {
    // mLvMain.setAdapter(mdocListAdapter);
    // }
    //
    // mLvMain.removeFooterView(mFootView);
    // }
    // if (mOnResultListener != null)
    // {
    // mOnResultListener.setOnResultListen();
    // }
    // Toast.makeText(mActivtiy, "没有数据", Toast.LENGTH_SHORT).show();
    // }
    // else
    // {
    // dealBackNetData(ret, false);
    // }
    // }
    // closeProgressDialog();
    // isGetingDocList = IDLE;
    // }
    
    /**
     * 处理网络返回的列表数据
     * 
     * @Description<功能详细描述>
     * 
     * @param ret
     * @param isDown
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    // private void dealBackNetData(List<HashMap<String, Object>> ret, boolean isDown)
    // {
    // if (ret != null && ret.size() > 0)
    // {
    
    // addDataToListAndRefresh(lists, isDown);
    // }
    // }
    
    @Override
    public Object dealDatafromNetWork(List<HashMap<String, Object>> ret)
    {
        // TODO Auto-generated method stub
        List<GetFileLibraryDocListInfo> lists =
            DBDataObjectWrite.insertFileLibraryList(ret, user, mType, mParentDirPath);
        return lists;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object addDataToListRefresh(Object obj, boolean isDown)
    {
        // TODO Auto-generated method stub
        List<GetFileLibraryDocListInfo> newList = new ArrayList<GetFileLibraryDocListInfo>();
        if (isDown)
        {
            // mDocList.clear();
            if (isHomePage)
            {
                if (mHomeKanWuAdapter != null && mHomeKanWuAdapter.getDocLists() != null)
                {
                    for (int i = 0; i < mHomeKanWuAdapter.getDocLists().size(); i++)
                    {
                        newList.add(mHomeKanWuAdapter.getDocLists().get(i));
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
        List<GetFileLibraryDocListInfo> lists = (List<GetFileLibraryDocListInfo>)obj;
        for (int i = 0; i < lists.size(); i++)
        {
            GetFileLibraryDocListInfo info = lists.get(i);
            newList.add(info);
        }
        return newList;
    }
    
    /**
     * 
     * 将处理后的列表数据，加载到相应的listview上
     * 
     * @Description<功能详细描述>
     * 
     * @param lists 列表数据
     * @param isDown 是否是增量刷新
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    // private void addDataToListAndRefresh(List<GetFileLibraryDocListInfo> lists, boolean isDown)
    // {
    // if (lists == null)
    // {
    // return;
    // }
    //
    // List<GetFileLibraryDocListInfo> newList = new ArrayList<GetFileLibraryDocListInfo>();
    // if (isDown)
    // {
    // // mDocList.clear();
    // if (isHomePage)
    // {
    // if (mHomeKanWuAdapter != null && mHomeKanWuAdapter.getDocLists() != null)
    // {
    // for (int i = 0; i < mHomeKanWuAdapter.getDocLists().size(); i++)
    // {
    // newList.add(mHomeKanWuAdapter.getDocLists().get(i));
    // }
    // }
    // }
    // else
    // {
    // if (mdocListAdapter != null && mdocListAdapter.getDocLists() != null)
    // {
    // for (int i = 0; i < mdocListAdapter.getDocLists().size(); i++)
    // {
    // newList.add(mdocListAdapter.getDocLists().get(i));
    // }
    // }
    // }
    //
    // }
    // for (int i = 0; i < lists.size(); i++)
    // {
    // GetFileLibraryDocListInfo info = lists.get(i);
    // newList.add(info);
    // }
    // reFreshList(newList);
    // }
    
    /**
     * 
     * 刷新列表
     * 
     * @Description 刷新列表
     * 
     * @param mDocLists 列表数据
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @SuppressWarnings("unchecked")
    @Override
    public void reFreshList(Object obj)
    {
        // TODO Auto-generated method stub
        List<GetFileLibraryDocListInfo> mDocLists = (List<GetFileLibraryDocListInfo>)obj;
        
        Collections.sort(mDocLists, new DocListDateComparator());
        
        // 当前列表中的列表数量
        mCount = mDocLists.size();
        if (mCount > 0)
        {
            // 获取最后一条的时间
            mRefreshLastTime = mDocLists.get(mCount - 1).getStringKeyValue(GetFileLibraryDocListInfo.modifydatetime);
        }
        
        if (isHomePage)
        {
            mHomeKanWuAdapter.setDocLists(mDocLists);
            mHomeKanWuAdapter.notifyDataSetChanged();
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
     * 比较器
     * 
     * @Description 比较器
     * 
     * @author wang_ling
     * @Version [版本号, 2013-10-18]
     */
    class DocListDateComparator implements Comparator<GetFileLibraryDocListInfo>
    {
        /**
         * 比较器方法 按照里面的 按日期逆排序
         */
        @Override
        public int compare(GetFileLibraryDocListInfo lhs, GetFileLibraryDocListInfo rhs)
        {
            return (rhs.getStringKeyValue(GetFileLibraryDocListInfo.createdatetime).compareToIgnoreCase(lhs.getStringKeyValue(GetFileLibraryDocListInfo.createdatetime)));
        }
    }
    
    /**
     * <默认构造函数>
     * 
     * @param context 应用上下文
     */
    public FileLibraryListView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param activity 应用上下文
     * @param keyWords 搜索关键字
     * @param wdkType 文档库类型
     * @param parentDirPath 路径
     * @param listen 监听器
     */
    public FileLibraryListView(Activity activity, String keyWords, String wdkType, String parentDirPath,
        OnTouchListener listen, boolean isHomePage)
    {
        super(activity, keyWords, wdkType, "", "", parentDirPath, listen, isHomePage, "", false);
        
        initData();
        initListener();
    }
    
    @Override
    public void initListViewAdapter()
    {
        // TODO Auto-generated method stub
        List<GetFileLibraryDocListInfo> mDocList = new ArrayList<GetFileLibraryDocListInfo>();
        if (isHomePage)
        {
            mHomeKanWuAdapter = new HomeKanWuAdapter(mActivtiy, mDocList, user);
            mLvMain.setAdapter(mHomeKanWuAdapter);
        }
        else
        {
            mdocListAdapter = new FileLibrarySecondAdapter(mActivtiy, mDocList, user, mType);
            mLvMain.setAdapter(mdocListAdapter);
        }
    }
    
    @Override
    public void setListViewAdapter(int selectCount)
    {
        // TODO Auto-generated method stub
        if (isHomePage)
        {
            mLvMain.setAdapter(mHomeKanWuAdapter);
            if (selectCount == -1)
            {
                mLvMain.setSelection(mHomeKanWuAdapter.getCount());
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
    
    /**
     * 
     * 从网络获取数据
     * 
     * @Description 从网络获取数据
     * 
     * @param time 最后一条的时间
     * @LastModifiedDate：2013-11-19
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
            body.put("type", mType);
            mGetDocListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETFILELIBRARYDOCREQUEST).create();
            
            NetRequestController.getFileLibraryList(mGetDocListRequst,
                mHandler,
                RequestTypeConstants.GETFILELIBRARYDOCREQUEST,
                body);
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * 从本地获取数据
     * 
     * @Description 从本地获取数据
     * 
     * @param mtime 时间
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public void getDocListFromDBData(String mtime)
    {
        
        closeProgressDialog();
        GetFileLibraryDocListInfo test1 = new GetFileLibraryDocListInfo();
        
        List<HashMap<String, Object>> queryret;
        if (null == mSearchkeyWords || mSearchkeyWords.equals(""))
        {
            
            String where = "";
            
            where =
                GetFileLibraryDocListInfo.l_user + " = ?" + " and " + GetFileLibraryDocListInfo.l_type + " = ?"
                    + " and " + GetFileLibraryDocListInfo.modifydatetime + "< ?";
            
            String[] selectionArgs = null;
            selectionArgs = new String[] {user, mType, mtime};
            
            String sortOrder = GetFileLibraryDocListInfo.modifydatetime + " desc ";
            
            String limitString = " limit " + pageSize;
            
            queryret = test1.query(null, where, selectionArgs, sortOrder + limitString);
        }
        else
        {
            String where = "";
            
            where =
                GetFileLibraryDocListInfo.l_user + " = ?" + " and " + GetFileLibraryDocListInfo.l_type + " = ?"
                    + " and " + GetFileLibraryDocListInfo.title + " like ?" + " and "
                    + GetFileLibraryDocListInfo.modifydatetime + "< ?";
            
            String[] selectionArgs = null;
            
            selectionArgs = new String[] {user, mType, "%" + mSearchkeyWords + "%", mtime};
            
            String sortOrder = GetFileLibraryDocListInfo.modifydatetime + " desc ";
            
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
                // mLvMain.setAdapter(mHomeKanWuAdapter);
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
            List<GetFileLibraryDocListInfo> doclists = new ArrayList<GetFileLibraryDocListInfo>();
            
            for (int i = 0; i < queryret.size(); i++)
            {
                GetFileLibraryDocListInfo info = new GetFileLibraryDocListInfo();
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
            mSelectedInfo = (GetFileLibraryDocListInfo)mHomeKanWuAdapter.getItem(position);
        }
        else
        {
            mSelectedInfo = (GetFileLibraryDocListInfo)mdocListAdapter.getItem(position);
        }
        
        mSelectedDocId = mSelectedInfo.getStringKeyValue(GetFileLibraryDocListInfo.docid);
        
        mSelectedScheduleUrl = mSelectedInfo.getStringKeyValue(GetFileLibraryDocListInfo.docurl);
        
        mSelectedFileTitle = mSelectedInfo.getStringKeyValue(GetFileLibraryDocListInfo.title);
        
        mSelectedPath = DBDataObjectWrite.getFilePath(user, mSelectedDocId, ConstState.WDK);
        
        File file = new File(mSelectedPath);
        
        if (offLine)
        {
            
            if (file.exists())
            {
                if (isHomePage)
                {
                    mHomeKanWuAdapter.setmSelectedPosition(position);
                    mHomeKanWuAdapter.notifyDataSetChanged();
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
                    mHomeKanWuAdapter.setmSelectedPosition(position);
                    mHomeKanWuAdapter.notifyDataSetChanged();
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
                    mHomeKanWuAdapter.setmSelectedPosition(position);
                    mHomeKanWuAdapter.notifyDataSetChanged();
                    if (!getFileContent(mSelectedDocId, "", mParentDirPath))
                    {
                        mHomeKanWuAdapter.setmSelectedPosition(-1);
                        mHomeKanWuAdapter.notifyDataSetChanged();
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
     * 打开pdf
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void openPDFFile()
    {
        
        intentOpenFile(mSelectedPath,
            mSelectedFileTitle,
            mSelectedDocId,
            mType,
            "",
            "",
            "",
            "",
            false,
            ConstState.NoSign,
            ConstState.NoSend,
            false);
    }
    
    /**
     * 
     * 打开文件后更新他的状态为已读
     * 
     * @Description 打开文件后更新他的状态为已读
     * 
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void refreshLocalList()
    {
        if (isHomePage)
        {
            mSelectedInfo.getLocaldatalist().put(GetLeaderDocListInfo.l_read, "1");
            mHomeKanWuAdapter.notifyDataSetChanged();
        }
        else
        {
            mSelectedInfo.getLocaldatalist().put(GetLeaderDocListInfo.l_read, "1");
            mdocListAdapter.notifyDataSetChanged();
        }
        
    }
    
    /**
     * 
     * 获取pdf文件
     * 
     * @Description<功能详细描述>
     * 
     * @param docid 文件id
     * @param path 路径
     * @return 是否成功
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public boolean getFileContent(String docid, String url, String dir)
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
            
            mGetFileContentRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETFILELIBRARYCONTENT).create();
            NetRequestController.getFileLibraryContent(mGetFileContentRequst,
                mHandler,
                RequestTypeConstants.GETFILELIBRARYCONTENT,
                body,
                dir);
            
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
        
        if (isHomePage)
        {
            pdfLoadLayout.setVisibility(View.GONE);
            // mHomeKanWuAdapter.setmSelectedPosition(-1);
        }
        else
        {
            mPopupWindow.dismiss();
            // mdocListAdapter.setmSelectedPosition(-1);
        }
        
    }
    
    @Override
    public void updataDatabase()
    {
        // TODO Auto-generated method stub
        DBDataObjectWrite.updateFileLibraryDocList(mSelectedInfo);
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
        List<GetFileLibraryDocListInfo> newLists = new ArrayList<GetFileLibraryDocListInfo>();
        if (isHomePage)
        {
            if (mHomeKanWuAdapter != null && mHomeKanWuAdapter.getDocLists() != null)
            {
                int size = mHomeKanWuAdapter.getDocLists().size();
                if (mHomeKanWuAdapter.getDocLists().size() > 0)
                {
                    for (int n = 0; n < size; n++)
                    {
                        
                        GetFileLibraryDocListInfo info = mHomeKanWuAdapter.getDocLists().get(n);
                        if (info.getStringKeyValue(GetFileLibraryDocListInfo.docid).equals(docId))
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
        else
        {
            if (mdocListAdapter != null && mdocListAdapter.getDocLists() != null)
            {
                int size = mdocListAdapter.getDocLists().size();
                if (mdocListAdapter.getDocLists().size() > 0)
                {
                    for (int n = 0; n < size; n++)
                    {
                        
                        GetFileLibraryDocListInfo info = mdocListAdapter.getDocLists().get(n);
                        if (info.getStringKeyValue(GetFileLibraryDocListInfo.docid).equals(docId))
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
