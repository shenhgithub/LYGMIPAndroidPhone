/*
 * File name:  OfficalListView.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-11-6
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.cityplan;

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

import com.hoperun.manager.adpter.cityplan.CityPlanSecondAdapter;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.FileUtil;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.model.entityMetaData.GetNettvListInfo;
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
public class CityPlanListView extends BaseListView
{
    
    /** 文件adapter **/
    private CityPlanSecondAdapter cityPlanListAdapter;
    
    protected GetNettvListInfo    cityPlan = null;
    
    private String                funId;
    
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        // TODO Auto-generated method stub
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETCITYPLANSECONDLIST:
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
                                ConstState.CSGH);
                        }
                        else
                        {
                            isDownling = false;
                            
                            mPopupWindow.dismiss();
                            cityPlanListAdapter.setmSelectedPosition(-1);
                            
                            Toast.makeText(mActivtiy, "下载文件失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        isDownling = false;
                        mPopupWindow.dismiss();
                        cityPlanListAdapter.setmSelectedPosition(-1);
                        
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
                case RequestTypeConstants.GETCITYPLANSECONDLIST:
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
    
    public CityPlanListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    public CityPlanListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    public CityPlanListView(Activity activity, String keyWords, String gwType, String handleType, String readStatus,
        String parentDirPath, OnTouchListener listen, Boolean isHomePage, String funId)
    {
        super(activity, keyWords, gwType, handleType, readStatus, parentDirPath, listen, isHomePage, "", false);
        this.funId = funId;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object addDataToListRefresh(Object obj, boolean isDown)
    {
        // TODO Auto-generated method stub
        List<GetNettvListInfo> newList = new ArrayList<GetNettvListInfo>();
        if (isDown)
        {
            if (cityPlanListAdapter != null && cityPlanListAdapter.getDocLists() != null)
            {
                for (int i = 0; i < cityPlanListAdapter.getDocLists().size(); i++)
                {
                    newList.add(cityPlanListAdapter.getDocLists().get(i));
                }
            }
            
        }
        
        List<GetNettvListInfo> lists = (List<GetNettvListInfo>)obj;
        
        for (int i = 0; i < lists.size(); i++)
        {
            GetNettvListInfo info = lists.get(i);
            newList.add(info);
        }
        
        return newList;
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void reFreshList(Object obj)
    {
        // TODO Auto-generated method stub
        List<GetNettvListInfo> mDocLists = (List<GetNettvListInfo>)obj;
        Collections.sort(mDocLists, new DocListDateComparator());
        
        // 当前列表中的列表数量
        mCount = mDocLists.size();
        if (mCount > 0)
        {
            // 获取最后一条的时间
            mRefreshLastTime = mDocLists.get(mCount - 1).getStringKeyValue(GetNettvListInfo.createdatetime);
        }
        
        cityPlanListAdapter.setDocLists(mDocLists);
        cityPlanListAdapter.notifyDataSetChanged();
        
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
     * 获取网络中的列表 获得列表 "keyWord": "关键字,搜索条件", "currentDatetime": "当前时间 格式yyyy-MM-dd HH:mm:ss ", "pageSize ": "分页条数",
     * "type ": "流程类型，例如：收文/发文", "handleType": "检索待办还是已办1为已办，0为待办"
     * 
     * @LastModifiedDate：2013-9-28
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    @Override
    public void getDocListFromNet(String time)
    {
        LogUtil.i("", "time=" + time);
        JSONObject body = new JSONObject();
        try
        {
            body.put("funcid", funId);
            body.put("currendatetime", time);
            body.put("pagesize", pageSize);
            body.put("type", 1);
            mGetDocListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETCITYPLANSECONDLIST).create();
            
            NetRequestController.getCityPlanListRequest(mGetDocListRequst,
                mHandler,
                RequestTypeConstants.GETCITYPLANSECONDLIST,
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
        mReadStatus = ConstState.ALLDOCLIST;
        // if (mReadStatus.equals(ConstState.HasReadDOCLIST))
        // {
        // mReadFlag = ConstState.BACK_HasReadDOCLIST; // 已读
        // }
        // else if (mReadStatus.equals(ConstState.UNReadDOCLIST))
        // {
        // mReadFlag = ConstState.BACK_UNReadDOCLIST; // 未读
        // }
        
        GetNettvListInfo test1 = new GetNettvListInfo();
        
        List<HashMap<String, Object>> queryret;
        if (null == mSearchkeyWords || mSearchkeyWords.equals(""))
        {
            String where = "";
            
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                where =
                    GetNettvListInfo.l_user + " = ?" + " and " + GetNettvListInfo.l_type + " = ?" + " and "
                        + GetNettvListInfo.funcid + " = ?" + " and " + GetNettvListInfo.createdatetime + "< ?";
            }
            else
            {
                where =
                    GetNettvListInfo.l_user + " = ?" + " and " + GetNettvListInfo.l_type + " = ?" + " and "
                        + GetNettvListInfo.createdatetime + "< ?" + " and " + GetNettvListInfo.l_read + " =? ";
            }
            
            String[] selectionArgs = null;
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                selectionArgs = new String[] {user, mType, funId, mtime};
            }
            else
            {
                selectionArgs = new String[] {user, mType, mHandleType, mtime, mReadFlag};
            }
            
            String sortOrder = GetNettvListInfo.createdatetime + " desc ";
            
            String limitString = " limit " + pageSize;
            
            queryret = test1.query(null, where, selectionArgs, sortOrder + limitString);
        }
        else
        {
            String where = "";
            
            if (mReadStatus.equals(ConstState.ALLDOCLIST))
            {
                where =
                    GetNettvListInfo.l_user + " = ?" + " and " + GetNettvListInfo.l_type + " = ?" + " and "
                        + GetNettvListInfo.title + " like ?" + " and " + GetNettvListInfo.createdatetime + "< ?";
            }
            else
            {
                where =
                    GetNettvListInfo.l_user + " = ?" + " and " + GetNettvListInfo.l_type + " = ?" + " and "
                        + GetNettvListInfo.title + " like ?" + " and " + GetNettvListInfo.createdatetime + "< ?"
                        + " and " + GetNettvListInfo.l_read + " =? ";
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
            
            String sortOrder = GetNettvListInfo.createdatetime + " desc ";
            
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
            mLvMain.setAdapter(cityPlanListAdapter);
            
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
            }
            isGetingDocList = IDLE;
        }
        else
        {
            List<GetNettvListInfo> doclists = new ArrayList<GetNettvListInfo>();
            
            for (int i = 0; i < queryret.size(); i++)
            {
                GetNettvListInfo info = new GetNettvListInfo();
                info.convertToObject(queryret.get(i));
                doclists.add(info);
            }
            
            addDataToListAndRefresh(doclists, true);
            isGetingDocList = IDLE;
        }
    }
    
    @Override
    public void onClickLisItem(int position)
    {
        // TODO Auto-generated method stub
        cityPlan = (GetNettvListInfo)cityPlanListAdapter.getItem(position);
        mSelectedDocId = cityPlan.getStringKeyValue(GetNettvListInfo.planid);
        
        mSelectedFileTitle = cityPlan.getStringKeyValue(GetNettvListInfo.title);
        
        mSelectedPath = DBDataObjectWrite.getFilePath(user, mSelectedDocId, ConstState.CSGH);
        
        File file = new File(mSelectedPath);
        
        if (offLine)
        {
            
            if (file.exists())
            {
                cityPlanListAdapter.setmSelectedPosition(position);
                cityPlanListAdapter.notifyDataSetChanged();
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
                cityPlanListAdapter.setmSelectedPosition(position);
                cityPlanListAdapter.notifyDataSetChanged();
                OpenPDFFile();
            }
            else
            {
                cityPlanListAdapter.setmSelectedPosition(position);
                cityPlanListAdapter.notifyDataSetChanged();
                if (!getFileContent(mSelectedDocId, "", mParentDirPath))
                {
                    cityPlanListAdapter.setmSelectedPosition(-1);
                    cityPlanListAdapter.notifyDataSetChanged();
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
            body.put("planid", docid);
            
            mGetFileContentRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETFILECONTENT).create();
            
            NetRequestController.getCityPlanContent(mGetFileContentRequst,
                mHandler,
                RequestTypeConstants.GETFILECONTENT,
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
        List<GetNettvListInfo> newLists = new ArrayList<GetNettvListInfo>();
        List<GetNettvListInfo> oldLists = new ArrayList<GetNettvListInfo>();
        if (cityPlanListAdapter != null)
        {
            oldLists = cityPlanListAdapter.getDocLists();
        }
        if (oldLists != null)
        {
            int i = -1;
            int size = oldLists.size();
            if (oldLists.size() > 0)
            {
                for (int n = 0; n < size; n++)
                {
                    
                    GetNettvListInfo info = oldLists.get(n);
                    if (info.getStringKeyValue(GetNettvListInfo.planid).equals(docId))
                    {
                        info.getLocaldatalist().put(GetNettvListInfo.l_read, "0");
                    }
                    
                    newLists.add(info);
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
        
        String wheres = GetNettvListInfo.l_user + "=?" + " and " + GetNettvListInfo.planid + "=?";
        String[] selectionArgs = {user, mSelectedDocId};
        
        ContentValues values = new ContentValues();
        values.put(GetNettvListInfo.l_read, ConstState.BACK_HasReadDOCLIST);
        
        GetNettvListInfo info = new GetNettvListInfo();
        info.update(values, wheres, selectionArgs);
    }
    
    @Override
    public int getDocCount()
    {
        if (cityPlanListAdapter != null)
        {
            return cityPlanListAdapter.getCount();
        }
        
        return 0;
    }
    
    @Override
    public void initListViewAdapter()
    {
        // TODO Auto-generated method stub
        List<GetNettvListInfo> mDocList = new ArrayList<GetNettvListInfo>();
        cityPlanListAdapter = new CityPlanSecondAdapter(mActivtiy, mDocList);
        mLvMain.setAdapter(cityPlanListAdapter);
    }
    
    @Override
    public void setListViewAdapter(int selectCount)
    {
        // TODO Auto-generated method stub
        mLvMain.setAdapter(cityPlanListAdapter);
        if (selectCount == -1)
        {
            mLvMain.setSelection(cityPlanListAdapter.getCount());
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
        
        cityPlanListAdapter.setmSelectedPosition(-1);
        mPopupWindow.dismiss();
    }
    
    @Override
    public Object dealDatafromNetWork(List<HashMap<String, Object>> ret)
    {
        // TODO Auto-generated method stub
        List<GetNettvListInfo> lists = DBDataObjectWrite.insertCityPlanList(ret, user, funId, mParentDirPath);
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
    class DocListDateComparator implements Comparator<GetNettvListInfo>
    {
        /**
         * 比较器方法 按照里面的 按日期逆排序
         */
        @Override
        public int compare(GetNettvListInfo lhs, GetNettvListInfo rhs)
        {
            return (rhs.getStringKeyValue(GetNettvListInfo.createdatetime).compareToIgnoreCase(lhs.getStringKeyValue(GetNettvListInfo.createdatetime)));
        }
    }
    
}
