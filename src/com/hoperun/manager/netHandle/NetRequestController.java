/*
 * File name:  MyNetSend.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-8-26
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.netHandle;

import org.json.JSONObject;

import android.os.Handler;

import com.hoperun.manager.MipPhoneConstState;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mipmanager.model.wrapRequest.ApkDownLoadRequest;
import com.hoperun.mipmanager.model.wrapRequest.BindDeviceRequest;
import com.hoperun.mipmanager.model.wrapRequest.ChangePassWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.CityPlanListRequest;
import com.hoperun.mipmanager.model.wrapRequest.DocListRequest;
import com.hoperun.mipmanager.model.wrapRequest.EmailContactListRequest;
import com.hoperun.mipmanager.model.wrapRequest.GeDevSuperviseLisRequest;
import com.hoperun.mipmanager.model.wrapRequest.GeEngSuperviseListRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetAppTypesRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetAppsByTypeRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetCityIdRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetCityPlanDocRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetCompanyAddressRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetDocRecordInfoWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetEmailAttachRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetEntertpriseListRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetFastNewsListRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetFileContentRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetFileLibraryContentRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetHistoryRecordWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetLeaderFileRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetNextModuleListRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetPdfNextStepWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetPicListRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetProvinceListRequest;
import com.hoperun.mipmanager.model.wrapRequest.GetXwzxAttchRequest;
import com.hoperun.mipmanager.model.wrapRequest.InnerEmailListRequest;
import com.hoperun.mipmanager.model.wrapRequest.LDRCListWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.LoginWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.ModuleNoReadRequest;
import com.hoperun.mipmanager.model.wrapRequest.MsgVerifyRequest;
import com.hoperun.mipmanager.model.wrapRequest.PersonScheduleDetailRequest;
import com.hoperun.mipmanager.model.wrapRequest.PersonScheduleModifyRequest;
import com.hoperun.mipmanager.model.wrapRequest.QianDaoRequest;
import com.hoperun.mipmanager.model.wrapRequest.RCAPListWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.SendDocWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.SendInnerEmailWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.SetLockStatusWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.TXLListWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.UpdateEmailReadStatusRequest;
import com.hoperun.mipmanager.model.wrapRequest.UpdatePerDataRequest;
import com.hoperun.mipmanager.model.wrapRequest.VerifyRequest;
import com.hoperun.mipmanager.model.wrapRequest.VersionUpdRequest;
import com.hoperun.mipmanager.model.wrapRequest.WDKListWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.XWZXListWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.YWBLWrapRequest;
import com.hoperun.mipmanager.model.wrapRequest.getCityListRequest;
import com.hoperun.mipmanager.model.wrapRequest.getWeatherRequest;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;

/**
 * 所有请求分发
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-8-26]
 */
public class NetRequestController
{
    private static NetRequestController myNetSend      = new NetRequestController();
    
    /**
     * value
     */
    public static String                MOA_osType     = "android";
    
    /**
     * value
     */
    public static String                MOA_osVersion  = "5.0.1";
    
    /**
     * value
     */
    public static String                MOA_isEncrypt  = "1";
    
    /**
     * value
     */
    public static String                MOA_deviceType = "android";
    
    /**
     * value
     */
    public static String                MOA_isDebug    = "1";
    
    // 该变量用于加密及解密报文,始终未“HOPERUN.COM”
    public static String                MOA_deviceKey  = "HOPERUN.COM";
    
    // 该变量用于在报文头中的deviceKey，该值为uuid用MOA_deviceKey加密后的值，该值只是上传，对客户端无意义
    public static String                DEVICEKEY      = "HOPERUN.COM";
    
    /**
     * value
     */
    private static NetTask              testLoginCreator;
    
    /**
     * value
     */
    private static NetTask              loginCreator;
    
    /**
     * value
     */
    private static NetTask              heartBeartCreator;
    
    /**
     * value
     */
    private static NetTask              getVerifyIdCreator;
    
    /**
     * value
     */
    private static NetTask              sendVerifyIdCreator;
    
    /**
     * value
     */
    private static NetTask              sendBindCreator;
    
    /**
     * value
     */
    private static NetTask              sendAppVersionUpdCreator;
    
    /**
     * value
     */
    private static NetTask              sendModuleNoReadCreator;
    
    /**
     * value
     */
    private static NetTask              sendScheduleAllCreator;
    
    /**
     * value
     */
    private static NetTask              sendScheduleDetailCreator;
    
    /**
     * value
     */
    private static NetTask              sendScheduleModifyCreator;
    
    /**
     * value
     */
    private static NetTask              unHandDocRequestCreator;
    
    /**
     * value
     */
    // private static NetTask docListRequestCreator;
    
    /**
     * value
     */
    private static NetTask              changePasswordCreator;
    
    private static NetTask              updatePersonalCreator;
    
    /**
     * 获取app应用分类
     */
    private static NetTask              getAppTypesCreator;
    
    /**
     * 按照分类获取APP列表
     */
    private static NetTask              getAppsByTypeCreator;
    
    /**
     * value
     */
    private static NetTask              downloadApkCreator;
    
    /**
     * value
     */
    private static NetTask              companyAddressCreator;
    
    /**
     * value
     */
    private static NetTask              sendNextStepCreator;
    
    /** 天气和城市 add by wentao 2013-11-27 begin */
    /**
     * value
     */
    private static NetTask              sendGetWeatherCreator;
    
    /**
     * value
     */
    private static NetTask              sendGetHoldCityList;
    
    /**
     * value
     */
    private static NetTask              sendGetProvinceList;
    
    /**
     * value
     */
    private static NetTask              sendGetCityId;
    
    /**
     * value
     */
    private static NetTask              sendGetNormalCityList;
    
    /**
     * value
     */
    private static NetTask              sendGetHistoryRecordList;
    
    /**
     * value
     */
    private static NetTask              sendQianDao;
    
    /** 天气和城市 add by wentao 2013-11-27 end */
    
    private static JSONObject getWrapHeader()
    {
        JSONObject jsonObj = new JSONObject();
        try
        {
            jsonObj.put("messageId", "");// 请求Id
            jsonObj.put("appId", ConstState.ANDROID_APPID);
            jsonObj.put("appVersion", GlobalState.getInstance().getAppVersion());
            jsonObj.put("deviceId", GlobalState.getInstance().getDeviceId());
            jsonObj.put("deviceType", MipPhoneConstState.deviceType);
            jsonObj.put("isEncrypt", ConstState.isEncrypt);// 是否加密
            jsonObj.put("osVersion", GlobalState.getInstance().getOsVersion());// 系统版本
            jsonObj.put("userDept", GlobalState.getInstance().getNameSpace());// 所属部门
            jsonObj.put("userId", GlobalState.getInstance().getOpenId());// 用户Id
            jsonObj.put("sessionId", GlobalState.getInstance().getHeaderSessionId());// 回话标示
            jsonObj.put("requestDateTime", System.currentTimeMillis());// 请求时间
            jsonObj.put("requestDescription", "");// 请求描述
            jsonObj.put("uiId", "");// 页面ID（可选）
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
        
        return jsonObj;
        
    }
    
    /**
     * 
     * 构造
     * 
     * @Description 构造
     * 
     * @return 对象
     * @LastModifiedDate：16 Sep,2013
     * @author ma_zhicheng
     * @EditHistory：<修改内容><修改人>
     */
    public static synchronized NetRequestController getInstance()
    {
        if (myNetSend == null)
        {
            myNetSend = new NetRequestController();
        }
        return myNetSend;
    }
    
    /**
     * 
     * 停止具体请求的线程
     * 
     * @Description 停止具体请求的线程
     * 
     * @param requestTyep 请求id
     * @LastModifiedDate：28 Sep,2013
     * @author ma_zhicheng
     * @EditHistory：<修改内容><修改人>
     */
    public static void stopCurrentRequest(int requestTyep)
    {
        switch (requestTyep)
        {
            case RequestTypeConstants.LOGIN_REQUEST:
                loginCreator.shutDownExecute();
                break;
            case RequestTypeConstants.APKDOWNLOAD_REQUEST:
                downloadApkCreator.shutDownExecute();
                break;
            default:
                break;
        }
    }
    
    /**
     * 关闭当网路请求
     * 
     * @Description<功能详细描述>
     * 
     * @param task task
     * @LastModifiedDate：2013-11-5
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public static void stopCurrentNetTask(NetTask task)
    {
        if (task != null)
        {
            task.shutDownExecute();
        }
    }
    
    /**
     * 
     * 登录
     * 
     * @Description 登录
     * 
     * @param handler handler
     * @param requestType 请求id
     * @param object body中要传的参数
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void testSendLogin(Handler handler, int requestType, Object object)
    {
        testLoginCreator = new HttpNetFactoryCreator(requestType).create();
        LoginWrapRequest wrapRequest = new LoginWrapRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, testLoginCreator, object);
    }
    
    /**
     * 
     * 发送登录报文
     * 
     * @Description 发送登录报文
     * 
     * @param handler handler
     * @param requestType 请求id
     * @param obj 传递的参数
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void SendLogin(Handler handler, int requestType, Object obj)
    {
        loginCreator = new HttpNetFactoryCreator(requestType).create();
        LoginWrapRequest wrapRequest = new LoginWrapRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, loginCreator, obj);
    }
    
    /**
     * 
     * 发送心跳验证
     * 
     * @Description 发送登录报文
     * 
     * @param handler handler
     * @param requestType 请求id
     * @param obj 传递的参数
     * @LastModifiedDate：
     * @author ma_kaiyuan
     * @EditHistory：<修改内容><修改人>
     */
    public static void SendHeartBeat(Handler handler, int requestType, Object obj)
    {
        // heartBeartCreator = new HttpNetFactoryCreator(requestType).create();
        // HeartBeatRequest wrapRequest = new HeartBeatRequest(requestType);
        // wrapRequest.wrapRequest(handler, requestType, heartBeartCreator, obj);
    }
    
    /**
     * 
     * 发送短信验证,获取验证码
     * 
     * @Description 发送登录报文
     * 
     * @param handler handler
     * @param requestType 请求id
     * @param obj 传递的参数
     * @LastModifiedDate：
     * @author ma_kaiyuan
     * @EditHistory：<修改内容><修改人>
     */
    public static void SendGetMsgVerify(Handler handler, int requestType, Object obj)
    {
        getVerifyIdCreator = new HttpNetFactoryCreator(requestType).create();
        MsgVerifyRequest wrapRequest = new MsgVerifyRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, getVerifyIdCreator, obj);
    }
    
    /**
     * 
     * 发送短信验证code
     * 
     * @Description 发送登录报文
     * 
     * @param handler handler
     * @param requestType 请求id
     * @param obj 传递的参数
     * @LastModifiedDate：
     * @author ma_kaiyuan
     * @EditHistory：<修改内容><修改人>
     */
    public static void SendMsgVerify(Handler handler, int requestType, Object obj)
    {
        sendVerifyIdCreator = new HttpNetFactoryCreator(requestType).create();
        VerifyRequest wrapRequest = new VerifyRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, sendVerifyIdCreator, obj);
    }
    
    /**
     * 
     * 发送设备绑定报文
     * 
     * @Description 发送设备绑定报文
     * 
     * @param handler handler
     * @param requestType 设备绑定的id
     * @param obj Object
     * @LastModifiedDate：2013-9-26
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void SendBindDevice(Handler handler, int requestType, Object obj)
    {
        sendBindCreator = new HttpNetFactoryCreator(requestType).create();
        BindDeviceRequest wrapRequest = new BindDeviceRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, sendBindCreator, obj);
    }
    
    /**
     * 
     * 发送版本更新报文
     * 
     * @Description 发送版本更新报文
     * 
     * @param handler handler
     * @param requestType 版本更新的id
     * @param obj Object
     * @LastModifiedDate：2013-9-26
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void SendAppVersionUpd(Handler handler, int requestType, Object obj)
    {
        sendAppVersionUpdCreator = new HttpNetFactoryCreator(requestType).create();
        VersionUpdRequest wrapRequest = new VersionUpdRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, sendAppVersionUpdCreator, obj);
    }
    
    /**
     * 
     * 获取指定模块下的目录
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-9-27
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    public static void getNextModuleList(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        
        GetNextModuleListRequest wrapRequest = new GetNextModuleListRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 获取收发文
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-9-27
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    public static void getDocListRequest(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        DocListRequest wrapRequest = new DocListRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 锁文件
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-9-27
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    public static void setLockStatusRequest(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        SetLockStatusWrapRequest wrapRequest = new SetLockStatusWrapRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 发送文件
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-9-27
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    public static void sendDocFileRequest(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        SendDocWrapRequest wrapRequest = new SendDocWrapRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 获取文件的最后修改日期
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-9-29
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    public static void getArchiveUpdateTime(Handler handler, int requestType, Object object)
    {
        // docListRequestCreator = new HttpNetFactoryCreator(requestType).create();
        // GetArchiveLastTimeRequest wrapRequest = new GetArchiveLastTimeRequest(requestType);
        // wrapRequest.wrapRequest(handler, requestType, docListRequestCreator, object);
    }
    
    /**
     * 
     * 获得文件
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @param path 文件路径
     * @LastModifiedDate：2013-9-30
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    public static void getFileContent(NetTask task, Handler handler, int requestType, Object object, String path)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        GetFileContentRequest wrapRequest = new GetFileContentRequest(requestType, path);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    public static void getDocRecordInfo(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        GetDocRecordInfoWrapRequest wrapRequst = new GetDocRecordInfoWrapRequest(requestType);
        wrapRequst.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 获取领导日程安排列表
     * 
     * @Description 获取领导日程安排列表
     * 
     * @param task task
     * @param handler handler
     * @param requestType requestType
     * @param object object
     * @LastModifiedDate：2013-11-13
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getLeaderFileList(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        LDRCListWrapRequest wrapRequst = new LDRCListWrapRequest(requestType);
        wrapRequst.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 下载领导日程pdf文件
     * 
     * @Description 下载领导日程pdf文件
     * 
     * @param task task
     * @param handler handler
     * @param requestType 请求类型
     * @param object bizadata
     * @param path 文件路径
     * @LastModifiedDate：2013-11-14
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getLeaderFileContent(NetTask task, Handler handler, int requestType, Object object, String path)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        GetLeaderFileRequest wrapRequest = new GetLeaderFileRequest(requestType, path);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 获取文档库列表
     * 
     * @Description 获取文档库列表
     * 
     * @param task task
     * @param handler handler
     * @param requestType requestType
     * @param object object
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getFileLibraryList(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        WDKListWrapRequest wrapRequst = new WDKListWrapRequest(requestType);
        wrapRequst.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 获取文档库的pdf文件
     * 
     * @Description 获取文档库的pdf文件
     * 
     * @param task task
     * @param handler handler
     * @param requestType requestType
     * @param object object
     * @param path path
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getFileLibraryContent(NetTask task, Handler handler, int requestType, Object object, String dir)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        GetFileLibraryContentRequest wrapRequest = new GetFileLibraryContentRequest(requestType, dir);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 根据月份获取有个人日程安排的日期列表
     * 
     * @Description 根据月份获取有个人日程安排的日期列表
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-10-8
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getPersonalScheduleYM(Handler handler, int requestType, Object object)
    {
        sendScheduleAllCreator = new HttpNetFactoryCreator(requestType).create();
        RCAPListWrapRequest wrapRequest = new RCAPListWrapRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, sendScheduleAllCreator, object);
    }
    
    /**
     * 获取具体一天个人日程安排列表 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-10-9
     * @author gu_leilei
     * @EditHistory：<修改内容><修改人>
     */
    public static void getPersonalScheduleList(Handler handler, int requestType, Object object)
    {
        sendScheduleDetailCreator = new HttpNetFactoryCreator(requestType).create();
        PersonScheduleDetailRequest wrapRequest = new PersonScheduleDetailRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, sendScheduleDetailCreator, object);
    }
    
    /**
     * 
     * 维护个人日程安排信息
     * 
     * @Description<功能详细描述>
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-10-10
     * @author gu_leilei
     * @EditHistory：<修改内容><修改人>
     */
    public static void modifyPersonalScheduleInfo(Handler handler, int requestType, Object object)
    {
        sendScheduleModifyCreator = new HttpNetFactoryCreator(requestType).create();
        PersonScheduleModifyRequest wrapRequest = new PersonScheduleModifyRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, sendScheduleModifyCreator, object);
    }
    
    /**
     * 
     * 修改密码
     * 
     * @Description 修改密码
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param obj obj
     * @LastModifiedDate：2013-10-9
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void SendChangePassword(Handler handler, int requestType, Object obj)
    {
        changePasswordCreator = new HttpNetFactoryCreator(requestType).create();
        ChangePassWrapRequest wrapRequest = new ChangePassWrapRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, changePasswordCreator, obj);
    }
    
    /**
     * 修改个人信息 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param handler
     * @param requestType
     * @param obj
     * @LastModifiedDate：2014-3-20
     * @author li_miao
     * @EditHistory：<修改内容><修改人>
     */
    public static void SendUpdatePersonal(Handler handler, int requestType, Object obj)
    {
        updatePersonalCreator = new HttpNetFactoryCreator(requestType).create();
        UpdatePerDataRequest wrapRequest = new UpdatePerDataRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, updatePersonalCreator, obj);
    }
    
    /**
     * 
     * 获取App应用分类
     * 
     * @Description<功能详细描述>
     * 
     * @param handler
     * @param requestType
     * @param obj
     * @LastModifiedDate：2014-3-21
     * @author li_miao
     * @EditHistory：<修改内容><修改人>
     */
    public static void getAppTypes(Handler handler, int requestType, Object obj)
    {
        getAppTypesCreator = new HttpNetFactoryCreator(requestType).create();
        GetAppTypesRequest wrapRequest = new GetAppTypesRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, getAppTypesCreator, obj);
    }
    
    /**
     * 
     * 按照分类获取APP列表
     * 
     * @Description<功能详细描述>
     * 
     * @param handler
     * @param requestType
     * @param obj
     * @LastModifiedDate：2014-3-24
     * @author li_miao
     * @EditHistory：<修改内容><修改人>
     */
    public static void getAppsByType(Handler handler, int requestType, Object obj)
    {
        getAppsByTypeCreator = new HttpNetFactoryCreator(requestType).create();
        GetAppsByTypeRequest wrapRequest = new GetAppsByTypeRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, getAppsByTypeCreator, obj);
    }
    
    /**
     * 
     * apk包下载
     * 
     * @Description<功能详细描述>
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param obj obj
     * @LastModifiedDate：2013-10-10
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void sendDownloadApk(NetTask task, Handler handler, int requestType, Object obj)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        // downloadApkCreator = new HttpNetFactoryCreator(requestType).create();
        ApkDownLoadRequest wrapRequest = new ApkDownLoadRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, obj);
    }
    
    /**
     * 获取企业通讯录列表
     * 
     * @Description<功能详细描述>
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object obj
     * @LastModifiedDate：2013-10-11
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public static void getCompanyAddress(Handler handler, int requestType, Object object)
    {
        companyAddressCreator = new HttpNetFactoryCreator(requestType).create();
        GetCompanyAddressRequest request = new GetCompanyAddressRequest(requestType);
        request.wrapRequest(handler, getWrapHeader(), requestType, companyAddressCreator, object);
    }
    
    /**
     * 
     * 获取待办工作项详细信息
     * 
     * @Description<功能详细描述>
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getSendNextStep(Handler handler, int requestType, Object object)
    {
        sendNextStepCreator = new HttpNetFactoryCreator(requestType).create();
        GetPdfNextStepWrapRequest request = new GetPdfNextStepWrapRequest(requestType);
        request.wrapRequest(handler, getWrapHeader(), requestType, sendNextStepCreator, object);
    }
    
    /**
     * 
     * 获取天气信息
     * 
     * @Description<功能详细描述>
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-11-27
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    public static void getSendWeather(Handler handler, int requestType, Object object)
    {
        sendGetWeatherCreator = new HttpNetFactoryCreator(requestType).create();
        getWeatherRequest request = new getWeatherRequest(requestType);
        request.wrapRequest(handler, getWrapHeader(), requestType, sendGetWeatherCreator, object);
    }
    
    /**
     * 
     * 获取热门城市列表
     * 
     * @Description<功能详细描述>
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-11-27
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    public static void getHoldCityList(Handler handler, int requestType, Object object)
    {
        sendGetHoldCityList = new HttpNetFactoryCreator(requestType).create();
        getCityListRequest request = new getCityListRequest(requestType);
        request.wrapRequest(handler, getWrapHeader(), requestType, sendGetHoldCityList, object);
    }
    
    /**
     * 
     * 获取省份城市列表
     * 
     * @Description<功能详细描述>
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-11-27
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    public static void getProvinceList(Handler handler, int requestType, Object object)
    {
        sendGetProvinceList = new HttpNetFactoryCreator(requestType).create();
        GetProvinceListRequest request = new GetProvinceListRequest(requestType);
        request.wrapRequest(handler, getWrapHeader(), requestType, sendGetProvinceList, object);
    }
    
    /**
     * 
     * 获取普通城市列表
     * 
     * @Description<功能详细描述>
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-11-27
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    public static void getNormalCityList(Handler handler, int requestType, Object object)
    {
        sendGetNormalCityList = new HttpNetFactoryCreator(requestType).create();
        GetProvinceListRequest request = new GetProvinceListRequest(requestType);
        request.wrapRequest(handler, getWrapHeader(), requestType, sendGetNormalCityList, object);
    }
    
    /**
     * 
     * 获取城市的天气编码
     * 
     * @Description<功能详细描述>
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-11-27
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    public static void getCityId(Handler handler, int requestType, Object object)
    {
        sendGetCityId = new HttpNetFactoryCreator(requestType).create();
        GetCityIdRequest request = new GetCityIdRequest(requestType);
        request.wrapRequest(handler, getWrapHeader(), requestType, sendGetCityId, object);
    }
    
    /**
     * 
     * 下载城市规划pdf文件
     * 
     * @Description 下载城市规划pdf文件
     * 
     * @param task task
     * @param handler handler
     * @param requestType 请求类型
     * @param object bizadata
     * @param path 文件路径
     * @LastModifiedDate：2013-11-14
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getCityPlanContent(NetTask task, Handler handler, int requestType, Object object, String path)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        GetCityPlanDocRequest wrapRequest = new GetCityPlanDocRequest(requestType, path);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 城市规划2级列表
     * 
     * @param handler handler
     * @param requestType 请求的id
     * @param object object
     * @LastModifiedDate：2013-9-27
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    public static void getCityPlanListRequest(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        CityPlanListRequest wrapRequest = new CityPlanListRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 获取模块未读数
     * 
     * @Description 获取模块未读数
     * 
     * @param handler handler
     * @param requestType 登录的id
     * @param obj Object
     * @LastModifiedDate：2013-9-26
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void SendModuleNoRead(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        ModuleNoReadRequest wrapRequest = new ModuleNoReadRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 发送内部邮件
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @LastModifiedDate：2014-2-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void sendInnerEmailRequest(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        SendInnerEmailWrapRequest wrapRequest = new SendInnerEmailWrapRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 下载内部邮件附件
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @param path
     * @LastModifiedDate：2014-2-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getInnerEmailAttach(NetTask task, Handler handler, int requestType, Object object, String path)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        GetEmailAttachRequest wrapRequest = new GetEmailAttachRequest(requestType, path);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 获取内部邮件列表
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @LastModifiedDate：2014-2-17
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getInnerEmailListRequest(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        InnerEmailListRequest wrapRequest = new InnerEmailListRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 更新邮件的阅读状态
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @LastModifiedDate：2014-2-20
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void updateEmailReadStatusRequest(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        UpdateEmailReadStatusRequest wrapRequest = new UpdateEmailReadStatusRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 获取内部邮件联系人
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @LastModifiedDate：2014-2-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getInnerEmailContactRequest(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        EmailContactListRequest wrapRequest = new EmailContactListRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 获取新闻中心二级列表
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @LastModifiedDate：2014-3-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getXwzxList(NetTask task, Handler handler, int requestType, Object object, String methodName)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        XWZXListWrapRequest wrapRequst = new XWZXListWrapRequest(requestType, methodName);
        wrapRequst.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 获取业务办理二级列表 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @param methodName
     * @LastModifiedDate：2014-4-2
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    public static void getYwblList(NetTask task, Handler handler, int requestType, Object object, String methodName)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        YWBLWrapRequest wrapRequst = new YWBLWrapRequest(requestType, methodName);
        wrapRequst.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 下载新闻中心详细信息的附件
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @param url
     * @param path
     * @LastModifiedDate：2014-3-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getXwzxAttchFile(NetTask task, Handler handler, int requestType, Object object, String url,
        String path, String fileName)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        GetXwzxAttchRequest wrapRequest = new GetXwzxAttchRequest(requestType, url, path, fileName);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 获取历史记录
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @LastModifiedDate：2014-2-20
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getHistoricalRecordsRequest(Handler handler, int requestType, Object object)
    {
        sendGetHistoryRecordList = new HttpNetFactoryCreator(requestType).create();
        GetHistoryRecordWrapRequest wrapRequest = new GetHistoryRecordWrapRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, sendGetHistoryRecordList, object);
    }
    
    /**
     * 
     * 签到
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @LastModifiedDate：2014-2-20
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void qianDaoRequest(Handler handler, int requestType, Object object)
    {
        sendQianDao = new HttpNetFactoryCreator(requestType).create();
        QianDaoRequest wrapRequest = new QianDaoRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, sendQianDao, object);
    }
    
    /**
     * 
     * 获取今日快报列表
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @LastModifiedDate：2014-3-26
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getFastNewsList(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        
        GetFastNewsListRequest wrapRequest = new GetFastNewsListRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 获取设备运行
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @LastModifiedDate：2014-3-26
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getDevSuperviseList(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        
        GeDevSuperviseLisRequest wrapRequest = new GeDevSuperviseLisRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 获取低碳港口
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @LastModifiedDate：2014-3-26
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getEngSuperviseList(NetTask task, Handler handler, int requestType, Object object)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        
        GeEngSuperviseListRequest wrapRequest = new GeEngSuperviseListRequest(requestType);
        wrapRequest.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 获取通讯录列表
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @LastModifiedDate：2014-3-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static void getTXLList(NetTask task, Handler handler, int requestType, Object object, String methodName)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        TXLListWrapRequest wrapRequst = new TXLListWrapRequest(requestType, methodName);
        wrapRequst.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * <一句话功能简述>获取首页广告图片
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @param methodName
     * @LastModifiedDate：2014-5-4
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    public static void getPicList(NetTask task, Handler handler, int requestType, Object object, String methodName)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        GetPicListRequest wrapRequst = new GetPicListRequest(requestType, methodName);
        wrapRequst.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 获取全部企业列表
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @LastModifiedDate：2014-5-13
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    public static void getEnterpriseLsit(NetTask task, Handler handler, int requestType, Object object,
        String methodName)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        GetEntertpriseListRequest wrapRequst = new GetEntertpriseListRequest(requestType, methodName);
        wrapRequst.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
    
    /**
     * 
     * 刷新视频新闻列表
     * 
     * @Description<功能详细描述>
     * 
     * @param task
     * @param handler
     * @param requestType
     * @param object
     * @LastModifiedDate：2014-5-15
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    public static void refreshVideoList(NetTask task, Handler handler, int requestType, Object object, String methodName)
    {
        if (task == null)
        {
            task = new HttpNetFactoryCreator(requestType).create();
        }
        YWBLWrapRequest wrapRequst = new YWBLWrapRequest(requestType, methodName);
        wrapRequst.wrapRequest(handler, getWrapHeader(), requestType, task, object);
    }
}
