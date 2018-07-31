/*
 * File name:  CustomHanler.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-10-17
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.components.CustomHandler;

import android.os.Handler;
import android.os.Message;

import com.hoperun.mip.netutils.model.BaseResponseEntity.ParseResponse;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Header.ResponseHeader;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.utils.MIPConstState;
import com.hoperun.mipmanager.utils.ConstState;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-10-17]
 */
public class CustomHanler extends Handler implements IPostHandler
{
    
    @Override
    public void handleMessage(Message msg)
    {
        int requestType = msg.what;
        int errorCode = msg.arg1;
        
        if (errorCode == MIPConstState.SUCCESS)
        {
            
            Object header = null;
            Object body = null;
            
            if (msg.obj == null)
            {
                LogUtil.d("", "网络请求返回正常，但返回数据为NULL!");
                header = "报文不是预定义的格式";
                MetaResponseBody resBody = new MetaResponseBody();
                resBody.setRetError("1");
                body = resBody;
            }
            else if (msg.obj instanceof ParseResponse)
            {
                try
                {
                    ParseResponse parse = null;
                    parse = (ParseResponse)msg.obj;
                    if (parse != null)
                    {
                        header = parse.getHeader();
                        body = parse.getBody();
                        LogUtil.d("", "请求业务返回数据正确!");
                    }
                    
                    if (header instanceof ResponseHeader)
                    {
                        ResponseHeader newheader = (ResponseHeader)header;
                        if ("3".equals(newheader.getRetCode()))
                        {
                            // Intent intent = new Intent();
                            // intent.setClass(GlobalState.getInstance().getApplicationContext(),
                            // DeviceLostActivity.class);
                            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            // GlobalState.getInstance().startActivity(intent);
                            return;
                        }
                    }
                    
                }
                catch (Exception e)
                {
                    LogUtil.d("", "网络请求返回正常，但返回数据异常!");
                    e.printStackTrace();
                }
            }
            else
            {
                header = "报文不是预定义的格式";
                body = msg.obj;
            }
            
            errorCode = ConstState.SUCCESS;
            PostHandle(requestType, header, body, true, errorCode);
        }
        else
        {
            if (msg.obj == null || !(msg.obj instanceof String))
            {
                msg.obj = "";
            }
            
            LogUtil.d("", "请求业务返回数据错误，错误的类别是 ：" + errorCode + "错误的信息是：" + msg.obj);
            
            switch (errorCode)
            {
                case MIPConstState.NO_NET:
                    errorCode = ConstState.NO_NET;
                    break;
                case MIPConstState.TIME_OUT_ERROR:
                    errorCode = ConstState.NET_ERROR;
                    break;
                case MIPConstState.CANCEL_REQUEST:
                    errorCode = ConstState.CANCEL_THREAD;
                    break;
                default:
                    errorCode = ConstState.DATA_ERROR;
                    break;
            }
            PostHandle(requestType, "", msg.obj, false, errorCode);
        }
        super.handleMessage(msg);
    }
    
    @Override
    public void PostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        
    }
}
