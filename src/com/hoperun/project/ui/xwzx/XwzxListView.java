/*
 * File name:  XwzxListView.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-3-18
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.xwzx;

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
import android.content.Intent;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.hoperun.manager.adpter.xwzx.XwzxSecondAdapter;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.utils.StringUtils;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.model.entityMetaData.GetXwzxDocDetailInfo;
import com.hoperun.mipmanager.model.entityMetaData.GetXwzxDocListInfo;
import com.hoperun.mipmanager.model.entityMetaData.NewsTypeInfo;
import com.hoperun.mipmanager.model.entityMetaData.XwzxAttachBody;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.BaseListView;
import com.hoperun.project.ui.nettv.NettvPlayActivity;

/**
 * 新闻中心列表
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-18]
 */
public class XwzxListView extends BaseListView
{
    /** 文件adapter **/
    private XwzxSecondAdapter  mdocListAdapter;
    
    private GetXwzxDocListInfo mSelectedInfo;
    
    private String             token;
    
    private NetTask            mGetFileAttachRequst;
    
    /** 是否有附件 **/
    private boolean            hasAttach;
    
    private List<NewsTypeInfo> newsTypeList = new ArrayList<NewsTypeInfo>();
    
    /**
     * 第一个附件的名字，作为docid存入数据库
     */
    private String             firstName;
    
    public List<NewsTypeInfo> getNewsTypeList()
    {
        return newsTypeList;
    }
    
    public void setNewsTypeList(List<NewsTypeInfo> newsTypeList)
    {
        this.newsTypeList = newsTypeList;
    }
    
    public XwzxListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    
    public XwzxListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    public XwzxListView(Activity activity, String keyWords, String xwzxType, String handleType, String readStatus,
        String parentDirPath, OnTouchListener listen, Boolean isHomePage, String typeId, boolean isActivity)
    {
        super(activity, keyWords, xwzxType, handleType, readStatus, parentDirPath, listen, isHomePage, typeId,
            isActivity);
        // TODO Auto-generated constructor stub
    }
    
    public XwzxListView(Activity activity, String keyWords, String xwzxType, String parentDirPath,
        OnTouchListener listen, boolean isHomePage, String typeId)
    {
        super(activity, keyWords, xwzxType, "", "", parentDirPath, listen, isHomePage, typeId, false);
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
                        
                        if (!"".equals(ret.get(0).get("newstitles")))
                        {
                            @SuppressWarnings("unchecked")
                            List<HashMap<String, Object>> newRet =
                                (List<HashMap<String, Object>>)ret.get(0).get("newstitles");
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
                case RequestTypeConstants.GETXWZXDOCDETAILREQUEST:
                    closeProgressDialog();
                    MetaResponseBody response = (MetaResponseBody)objBody;
                    if (response != null && response.getRetError().equals("0"))
                    {
                        List<HashMap<String, Object>> ret = response.getBuzList();
                        GetXwzxDocDetailInfo detailInfo =
                            new GetXwzxDocDetailInfo(user, mType, mHandleType, mSelectedDocId);
                        detailInfo.convertToObject(ret.get(0));
                        // 电子港报
                        if (mType.equals(ConstState.XWZX_DZGB))
                        {
                            parseDZGBresponse(detailInfo);
                        }
                        
                        // 集团要闻
                        else if (mType.equals(ConstState.XWZX_XWXX) || mType.equals(ConstState.XWZX_JCDT)
                            || mType.equals(ConstState.XWZX_GHZX) || mType.equals(ConstState.XWZX_LQSM)
                            || mType.equals(ConstState.ZXXX) || mType.equals(ConstState.XWZX_QZLX))
                        {
                            
                            String content = detailInfo.getStringKeyValue("content");
                            String author = detailInfo.getStringKeyValue("author");
                            if (content != null && !"".equals(content))
                            {
                                Intent intent = new Intent(mActivtiy, XwzxContentShowActivity.class);
                                intent.putExtra("author", author);
                                intent.putExtra("content", content);
                                intent.putExtra("title", mSelectedFileTitle);
                                mActivtiy.startActivity(intent);
                            }
                            // updataDatabase();
                            dealListView();
                        }
                        else
                        {
                            parseTZGGresponse(detailInfo);
                        }
                    }
                    else
                    {
                        closeProgressDialog();
                    }
                    break;
                
                case RequestTypeConstants.GETXWZXATTACHREQUEST:
                    closeDownLoadProgress();
                    MetaResponseBody contentBody = (MetaResponseBody)objBody;
                    // getFileContentEnd(true);
                    
                    List<HashMap<String, Object>> buzList = contentBody.getBuzList();
                    
                    if (buzList != null && buzList.size() > 0)
                    {
                        saveFileInfo(buzList, user, mType, mHandleType, firstName, mSelectedFileTitle, ConstState.XWZX);
                    }
                    else
                    {
                        isDownling = false;
                        mPopupWindow.dismiss();
                        mdocListAdapter.setmSelectedPosition(-1);
                        Toast.makeText(mActivtiy, "下载文件失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                // 获取新闻列表
                case RequestTypeConstants.GETNEWSTYPEREQUEST:
                    MetaResponseBody typeRes = (MetaResponseBody)objBody;
                    if (typeRes != null && typeRes.getRetError().equals("0"))
                    {
                        @SuppressWarnings("unchecked")
                        List<HashMap<String, Object>> mapList =
                            (List<HashMap<String, Object>>)typeRes.getBuzList().get(0).get("newstypes");
                        newsTypeList = new ArrayList<NewsTypeInfo>();
                        if (null != mapList)
                        {
                            for (int i = 0; i < mapList.size(); i++)
                            {
                                NewsTypeInfo info = new NewsTypeInfo();
                                info.convertToObject(mapList.get(i));
                                newsTypeList.add(info);
                            }
                        }
                    }
                    break;
                case RequestTypeConstants.PROGRESS_BAR:
                    // // 返回进度值
                    if (objBody instanceof Integer)
                    {
                        mDownloadfilePro.setProgress((Integer)objBody);
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
                case RequestTypeConstants.GETXWZXDOCDETAILREQUEST:
                    mdocListAdapter.setmSelectedPosition(-1);
                    mdocListAdapter.notifyDataSetChanged();
                    closeProgressDialog();
                    if (errorCode != ConstState.CANCEL_THREAD)
                    {
                        Toast.makeText(mActivtiy, "下载文件失败！", Toast.LENGTH_SHORT).show();
                    }
                    
                    isDownling = false;
                    
                    break;
                case RequestTypeConstants.GETXWZXATTACHREQUEST:
                    closeDownLoadProgress();
                    Toast.makeText(mActivtiy, "下载文件失败！", Toast.LENGTH_SHORT).show();
                    break;
                
                default:
                    break;
            }
        }
        
    }
    
    /**
     * 
     * 解析电子港报返回数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-3-20
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void parseDZGBresponse(GetXwzxDocDetailInfo detailInfo)
    {
        List<XwzxAttachBody> attachments = detailInfo.getAttachmenturls();
        if (attachments != null && attachments.size() > 0)
        {
            if (attachments.size() > 1)
            {
                hasAttach = true;
            }
            String firsturl = detailInfo.getAttachmenturls().get(0).getUrl();
            firstName = mSelectedDocId + detailInfo.getAttachmenturls().get(0).getName();
            
            // mSelectedPath = mParentDirPath + firstName;
            mSelectedPath = DBDataObjectWrite.getFilePath(user, firstName, ConstState.XWZX);
            File file = new File(mSelectedPath);
            if (file.exists())
            {
                openPDFFile();
            }
            else
            {
                getFileAttachWithUrl(firsturl, mParentDirPath, firstName);
            }
        }
        else
        {
            closeProgressDialog();
            Toast.makeText(mActivtiy, "没有附件", Toast.LENGTH_LONG).show();
        }
        DBDataObjectWrite.insertXwzxDetailInfo(detailInfo, user, mType, mHandleType, mSelectedDocId);
        
    }
    
    /**
     * 
     * 通知公告
     * 
     * @Description<功能详细描述>
     * 
     * @param detailInfo
     * @LastModifiedDate：2014-3-24
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void parseTZGGresponse(GetXwzxDocDetailInfo detailInfo)
    {
        List<XwzxAttachBody> attachments = detailInfo.getAttachmenturls();
        if (attachments != null && attachments.size() > 0)
        {
            String firsturl = detailInfo.getAttachmenturls().get(0).getUrl();
            firstName = mSelectedDocId + mSelectedFileTitle;
            
            // mSelectedPath = mParentDirPath + firstName;
            
            mSelectedPath = DBDataObjectWrite.getFilePath(user, firstName, ConstState.XWZX);
            File file = new File(mSelectedPath);
            if (file.exists())
            {
                openPDFFile();
            }
            else
            {
                getFileAttachWithUrl(firsturl, mParentDirPath, firstName);
            }
        }
        else
        {
            closeProgressDialog();
            Toast.makeText(mActivtiy, "没有附件", Toast.LENGTH_LONG).show();
        }
        DBDataObjectWrite.insertXwzxDetailInfo(detailInfo, user, mType, mHandleType, mSelectedDocId);
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void initListViewAdapter()
    {
        // TODO Auto-generated method stub
        List<GetXwzxDocListInfo> mDocList = new ArrayList<GetXwzxDocListInfo>();
        mdocListAdapter = new XwzxSecondAdapter(mActivtiy, mDocList, user, mType);
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
        JSONObject qzlxbody = new JSONObject();
        try
        {
            
            body.put("token", GlobalState.getInstance().getToken());
            body.put("querydate", time);
            body.put("size", pageSize);
            body.put("keyWord", mSearchkeyWords);
            
            qzlxbody.put("token", GlobalState.getInstance().getToken());
            qzlxbody.put("queryDate", time);
            qzlxbody.put("count", pageSize);
            qzlxbody.put("filter", mSearchkeyWords);
            
            mGetDocListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETXWZXDOCREQUEST).create();
            // 电子港报
            if (mType.equals(ConstState.XWZX_DZGB))
            {
                NetRequestController.getXwzxList(mGetDocListRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCREQUEST,
                    body,
                    "getPortNewsList");
            }
            // 通知公告
            else if (mType.equals(ConstState.XWZX_TZGG))
            {
                NetRequestController.getXwzxList(mGetDocListRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCREQUEST,
                    body,
                    "getNoticeList");
            }
            // 安全日报
            else if (mType.equals(ConstState.XWZX_AQRB))
            {
                NetRequestController.getXwzxList(mGetDocListRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCREQUEST,
                    body,
                    "getSafeNewsList");
            }
            // 项目督察
            else if (mType.equals(ConstState.XWZX_XMDC))
            {
                NetRequestController.getXwzxList(mGetDocListRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCREQUEST,
                    body,
                    "getInspectList");
            }
            // 最新消息
            else if (mType.equals(ConstState.ZXXX))
            {
                NetRequestController.getXwzxList(mGetDocListRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCREQUEST,
                    body,
                    "getHotNewsList");
            }
            // 新闻信息
            else if (mType.equals(ConstState.XWZX_XWXX))
            {
                // if (mHandleType.equals(ConstState.UNHADLEDOCLIST))
                // {
                // NetRequestController.getXwzxList(mGetDocListRequst,
                // mHandler,
                // RequestTypeConstants.GETXWZXDOCREQUEST,
                // body,
                // "getHotNewsList");
                // }
                // else if (mHandleType.equals(ConstState.HASHANDLEDOCLIST))
                // {
                // body.put("typeId", mSearchTypeId);//
                // NetRequestController.getXwzxList(mGetDocListRequst,
                // mHandler,
                // RequestTypeConstants.GETXWZXDOCREQUEST,
                // body,
                // "getAllNewsList");
                // }
                NetRequestController.getXwzxList(mGetDocListRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCREQUEST,
                    body,
                    "getImptNewsList");
            }
            // 视频新闻
            else if (mType.equals(ConstState.XWZX_SPXW))
            {
                NetRequestController.getXwzxList(mGetDocListRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCREQUEST,
                    body,
                    "getViewNewsList");
            }
            // 基层动态
            else if (mType.equals(ConstState.XWZX_JCDT))
            {
                NetRequestController.getXwzxList(mGetDocListRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCREQUEST,
                    body,
                    "getBaseNewsList");
            }
            // 港行资讯
            else if (mType.equals(ConstState.XWZX_GHZX))
            {
                NetRequestController.getXwzxList(mGetDocListRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCREQUEST,
                    body,
                    "getSailNewsList");
            }
            // 陆桥扫描
            else if (mType.equals(ConstState.XWZX_LQSM))
            {
                NetRequestController.getXwzxList(mGetDocListRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCREQUEST,
                    body,
                    "getBridgeNewsList");
            }
            // 群众路线
            else if (mType.equals(ConstState.XWZX_QZLX))
            {
                NetRequestController.getXwzxList(mGetDocListRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCREQUEST,
                    qzlxbody,
                    "getPeopleNewsList");
            }
            
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
        // TODO Auto-generated method stub
        List<GetXwzxDocListInfo> lists =
            DBDataObjectWrite.insertXwzxList(ret, user, mType, mHandleType, mParentDirPath);
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
        List<GetXwzxDocListInfo> newList = new ArrayList<GetXwzxDocListInfo>();
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
        List<GetXwzxDocListInfo> lists = (List<GetXwzxDocListInfo>)obj;
        for (int i = 0; i < lists.size(); i++)
        {
            GetXwzxDocListInfo info = lists.get(i);
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
        List<GetXwzxDocListInfo> mDocLists = (List<GetXwzxDocListInfo>)obj;
        
        Collections.sort(mDocLists, new DocListDateComparator());
        
        // 当前列表中的列表数量
        mCount = mDocLists.size();
        if (mCount > 0)
        {
            // 获取最后一条的时间
            mRefreshLastTime = mDocLists.get(mCount - 1).getStringKeyValue(GetXwzxDocListInfo.regdate);
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
     * 重载方法
     * 
     * @param position
     * @author wang_ling
     */
    @Override
    public void onClickLisItem(int position)
    {
        mSelectedInfo = (GetXwzxDocListInfo)mdocListAdapter.getItem(position);
        
        mSelectedDocId = mSelectedInfo.getStringKeyValue(GetXwzxDocListInfo.id);
        
        mSelectedFileTitle = mSelectedInfo.getStringKeyValue(GetXwzxDocListInfo.name);
        
        mSelectedPath = DBDataObjectWrite.getFilePath(user, mSelectedDocId, ConstState.XWZX);
        
        // File file = new File(mSelectedPath);
        // 视频新闻
        if (mType.equals(ConstState.XWZX_SPXW))
        {
            String url = mSelectedInfo.getStringKeyValue(GetXwzxDocListInfo.url);
            // url = "http://10.20.108.111:8080/files/test.wmv";
            
            if (offLine)
            {
                
                Toast.makeText(mActivtiy, "请先打开数据连接！", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String msgTypeId = mSelectedInfo.getStringKeyValue(GetXwzxDocListInfo.msgtypeid);
                String msgId = mSelectedInfo.getStringKeyValue(GetXwzxDocListInfo.id);
                refreshVideoList(msgTypeId, msgId);
                mdocListAdapter.setmSelectedPosition(position);
                mdocListAdapter.notifyDataSetChanged();
                if (!StringUtils.isNull(url))
                {
                    mdocListAdapter.setmSelectedPosition(-1);
                    mdocListAdapter.notifyDataSetChanged();
                    Intent intent = new Intent();
                    intent.setClass(mActivtiy, NettvPlayActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url", url);
                    mActivtiy.startActivity(intent);
                }
                else
                {
                    Toast.makeText(mActivtiy, "网络电视请求地址错误，请在服务器重新配置！", Toast.LENGTH_SHORT).show();
                }
                // updataDatabase();
                dealListView();
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
    
    /**
     * 
     * 根据url下载文件
     * 
     * @Description<功能详细描述>
     * 
     * @param docUrl
     * @param path
     * @LastModifiedDate：2014-3-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void getFileAttachWithUrl(String docUrl, String path, String fileName)
    {
        showDownloadProgress();
        mGetFileAttachRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETXWZXATTACHREQUEST).create();
        JSONObject body = new JSONObject();
        NetRequestController.getXwzxAttchFile(mGetFileAttachRequst,
            mHandler,
            RequestTypeConstants.GETXWZXATTACHREQUEST,
            body,
            docUrl,
            path,
            fileName);
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
        // if (!FileUtil.externalMemoryAvailable())
        // {
        // Toast.makeText(mActivtiy, "你的手机没有加载SDCard！", Toast.LENGTH_SHORT).show();
        // return false;
        // }
        //
        // if (FileUtil.getAvailableExternalMemorySize() < 100)
        // {
        // Toast.makeText(mActivtiy, "你的SDCard空间已满，请释放你的SDCard空间！", Toast.LENGTH_SHORT).show();
        // return false;
        // }
        //
        // showDownloadProgress();
        showProgressDialog();
        JSONObject body = new JSONObject();
        
        try
        {
            
            body.put("token", GlobalState.getInstance().getToken());
            // 通知公告
            if (mType.equals(ConstState.XWZX_TZGG))
            {
                body.put("noticeId", docid);
                NetRequestController.getXwzxList(mGetFileContentRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCDETAILREQUEST,
                    body,
                    "getNoticeDetail");
            }
            // 最新消息
            else if (mType.equals(ConstState.ZXXX))
            {
                body.put("newsId", docid);
                NetRequestController.getXwzxList(mGetFileContentRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCDETAILREQUEST,
                    body,
                    "getHotNewsDetail");
            }
            // 新闻信息
            else if (mType.equals(ConstState.XWZX_XWXX))
            {
                body.put("newsId", docid);
                // // 今日关注
                // if (mHandleType.equals(ConstState.UNHADLEDOCLIST))
                // {
                // NetRequestController.getXwzxList(mGetFileContentRequst,
                // mHandler,
                // RequestTypeConstants.GETXWZXDOCDETAILREQUEST,
                // body,
                // "getHotNewsDetail");
                // }
                // // 全部新闻
                // else if (mHandleType.equals(ConstState.HASHANDLEDOCLIST))
                // {
                // NetRequestController.getXwzxList(mGetFileContentRequst,
                // mHandler,
                // RequestTypeConstants.GETXWZXDOCDETAILREQUEST,
                // body,
                // "getAllNewsDetail");
                // }
                NetRequestController.getXwzxList(mGetFileContentRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCDETAILREQUEST,
                    body,
                    "getImptNewsDetail");
            }
            // 安全日报
            else if (mType.equals(ConstState.XWZX_AQRB))
            {
                body.put("newsId", docid);
                NetRequestController.getXwzxList(mGetFileContentRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCDETAILREQUEST,
                    body,
                    "getSafeNewsDetail");
            }
            // 电子港报
            else if (mType.equals(ConstState.XWZX_DZGB))
            {
                body.put("newsId", docid);
                NetRequestController.getXwzxList(mGetFileContentRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCDETAILREQUEST,
                    body,
                    "getPortNewsDetail");
            }
            // 项目督察
            else if (mType.equals(ConstState.XWZX_XMDC))
            {
                body.put("newsId", docid);
                NetRequestController.getXwzxList(mGetFileContentRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCDETAILREQUEST,
                    body,
                    "getInspectDetail");
            }
            // 基层动态
            else if (mType.equals(ConstState.XWZX_JCDT))
            {
                body.put("newsId", docid);
                NetRequestController.getXwzxList(mGetFileContentRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCDETAILREQUEST,
                    body,
                    "getBaseNewsDetail");
            }
            // 港行咨询
            else if (mType.equals(ConstState.XWZX_GHZX))
            {
                body.put("newsId", docid);
                NetRequestController.getXwzxList(mGetFileContentRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCDETAILREQUEST,
                    body,
                    "getSailNewsDetail");
            }
            // 陆桥扫描
            else if (mType.equals(ConstState.XWZX_LQSM))
            {
                body.put("newsId", docid);
                NetRequestController.getXwzxList(mGetFileContentRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCDETAILREQUEST,
                    body,
                    "getBridgeNewsDetail");
            }
            // 群众路线
            else if (mType.equals(ConstState.XWZX_QZLX))
            {
                body.put("newsId", docid);
                NetRequestController.getXwzxList(mGetFileContentRequst,
                    mHandler,
                    RequestTypeConstants.GETXWZXDOCDETAILREQUEST,
                    body,
                    "getPeopleNewsDetail");
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return false;
        }
        
        return true;
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
        
        closeProgressDialog();
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void updataDatabase()
    {
        // TODO Auto-generated method stub
        DBDataObjectWrite.updateXwzxDocList(mSelectedInfo);
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
        List<GetXwzxDocListInfo> newLists = new ArrayList<GetXwzxDocListInfo>();
        if (mdocListAdapter != null && mdocListAdapter.getDocLists() != null)
        {
            int size = mdocListAdapter.getDocLists().size();
            if (mdocListAdapter.getDocLists().size() > 0)
            {
                for (int n = 0; n < size; n++)
                {
                    
                    GetXwzxDocListInfo info = mdocListAdapter.getDocLists().get(n);
                    if (info.getStringKeyValue(GetXwzxDocListInfo.id).equals(docId))
                    {
                        info.getLocaldatalist().put(GetXwzxDocListInfo.newmark, "0");
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
    
    /**
     * 
     * 比较器
     * 
     * @Description 比较器
     * 
     * @author wang_ling
     * @Version [版本号, 2013-10-18]
     */
    class DocListDateComparator implements Comparator<GetXwzxDocListInfo>
    {
        /**
         * 比较器方法 按照里面的 按日期逆排序
         */
        @Override
        public int compare(GetXwzxDocListInfo lhs, GetXwzxDocListInfo rhs)
        {
            return (rhs.getStringKeyValue(GetXwzxDocListInfo.regdate).compareToIgnoreCase(lhs.getStringKeyValue(GetXwzxDocListInfo.regdate)));
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
            mHandleType,
            "",
            "",
            "",
            false,
            ConstState.NoSign,
            ConstState.NoSend,
            hasAttach);
    }
    
    protected NetTask mGetNewsTypeRequest;
    
    /**
     * 
     * 获取新闻分类
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-3-20
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void getNewsType()
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            NetRequestController.getXwzxList(mGetNewsTypeRequest,
                mHandler,
                RequestTypeConstants.GETNEWSTYPEREQUEST,
                body,
                "getNewsType");
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    protected NetTask mRefreshRequest;
    
    public void refreshVideoList(String msgTypeId, String msgId)
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("loginName", GlobalState.getInstance().getOpenId());
            body.put("msgTypeId", msgTypeId);
            body.put("msgId", msgId);
            NetRequestController.refreshVideoList(mRefreshRequest,
                mHandler,
                RequestTypeConstants.REFRESHVIDEOLIST,
                body,
                "updateHaveReadState");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
