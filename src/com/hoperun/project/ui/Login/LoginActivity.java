/*
 * File name:  LoginActivity.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2013-9-23
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.Login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.components.CustomDialog;
import com.hoperun.manager.components.CustomDialogListener;
import com.hoperun.manager.components.WaitDialog;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.exceptions.MIPException;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.NoMetaResponseBody;
import com.hoperun.mip.sqlUtils.SQLCreator.DBHandler;
import com.hoperun.mip.sqlUtils.Table.TFUNSIONPOWER;
import com.hoperun.mip.sqlUtils.Table.TUSERINFO;
import com.hoperun.mip.utils.AesUtil;
import com.hoperun.mip.utils.MessageSQLIdConstants;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.mip.utils.OsUtils;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.model.entityModule.Login.LoginModule;
import com.hoperun.mipmanager.model.entityModule.Login.UserInfo;
import com.hoperun.mipmanager.model.entityResponseBody.ResponseLoginBody;
import com.hoperun.mipmanager.model.entityResponseBody.ResponseVersionUpdBody;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.GetLocalPic;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.MainActivityNewActivity;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

/**
 * 登陆界面
 * 
 * @Description 登陆界面
 * 
 * @author wang_ling
 * @Version [版本号, 2013-9-23]
 */
public class LoginActivity extends PMIPBaseActivity implements OnClickListener
{
    /**
     * 布局
     */
    private RelativeLayout mLogin_layout;
    
    /** 用户名输入框 **/
    private EditText       userNameEdit;
    
    /** 密码输入框 **/
    private EditText       passwordEdit;
    
    /** 登录按钮 **/
    private Button         loginButton;
    
    /** 是否记住用户名按钮 **/
    private ImageView      rememberButton;
    
    /** 是否自动登录 **/
    private ImageView      autoLoginButton;
    
    /** 头像 **/
    private ImageView      headImageV;
    
    /** 找回密码按钮 **/
    private ImageButton    getCodeBtn;
    
    /** 显示最新版本号布局 **/
    private RelativeLayout newVersionRel;
    
    /** 最新版本号 **/
    private TextView       newVersionTv;
    
    /** 版本号 **/
    private TextView       versionTV;
    
    /** 是否记住用户名 true-记住；false-不记住 **/
    private boolean        isRemember;
    
    /** 是否自动登录 **/
    private boolean        isAutoLogin;
    
    /** 等待对话框 **/
    private WaitDialog     waitDialog;
    
    /** 绑定提示对话框 **/
    CustomDialog           binddialog;
    
    /**
     * 重载方法
     * 
     * @param requestType 请求id
     * @param Header 返回的头
     * @param obj 返回的body
     * @param error 是否返回成功
     * @param errorCode 返回错误码
     * @author wang_ling
     */
    @Override
    public void onPostHandle(int requestType, Object Header, Object obj, boolean error, int errorCode)
    {
        switch (requestType)
        {
            case RequestTypeConstants.LOGIN_REQUEST:
                waitDialog.dismiss();
                if (error)
                {
                    // NoMetaResponseBody loginBody = new NoMetaResponseBody();
                    // if (obj instanceof NoMetaResponseBody)
                    // {
                    // loginBody = (NoMetaResponseBody)obj;
                    // }
                    // parseLogin(loginBody);
                    if (null != obj)
                    {
                        parseLoginMeta((MetaResponseBody)obj);
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "返回数据为空", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {// 网络错误
                    if (errorCode == 1)
                    {
                        final CustomDialog dialog =
                            CustomDialog.newInstance(getResources().getString(R.string.Login_network_error),
                                getResources().getString(R.string.Login_Confirm));
                        dialog.show(getSupportFragmentManager(), "ErrorDialog");
                        dialog.setMidListener(new CustomDialogListener()
                        {
                            
                            @Override
                            public void Onclick()
                            {
                                dialog.dismiss();
                            }
                        });
                    }
                    // 数据错误
                    else if (errorCode == 2)
                    {
                        final CustomDialog dialog =
                            CustomDialog.newInstance(getResources().getString(R.string.Login_data_error),
                                getResources().getString(R.string.Login_Confirm));
                        dialog.show(getSupportFragmentManager(), "ErrorDialog");
                        dialog.setMidListener(new CustomDialogListener()
                        {
                            
                            @Override
                            public void Onclick()
                            {
                                dialog.dismiss();
                            }
                        });
                    }
                    // 离线登录
                    else if (errorCode == 3)
                    {
                        final CustomDialog dialog =
                            CustomDialog.newInstance(getResources().getString(R.string.Login_offline),
                                getResources().getString(R.string.Login_Confirm),
                                getResources().getString(R.string.Login_Cancel));
                        
                        dialog.show(getSupportFragmentManager(), "OfflineLogin");
                        dialog.setLeftListener(new CustomDialogListener()
                        {
                            
                            @Override
                            public void Onclick()
                            {
                                offlineLogin();
                                dialog.dismiss();
                            }
                        });
                        dialog.setRightListener(new CustomDialogListener()
                        {
                            
                            @Override
                            public void Onclick()
                            {
                                GlobalState.getInstance().setOfflineLogin(false);
                                dialog.dismiss();
                            }
                        });
                    }
                }
                break;
            
            case RequestTypeConstants.BINDDEVICE_REQUEST:
                
                if (error)
                {
                    waitDialog.dismiss();
                    NoMetaResponseBody bindBody = new NoMetaResponseBody();
                    if (obj instanceof NoMetaResponseBody)
                    {
                        bindBody = (NoMetaResponseBody)obj;
                    }
                    // 成功
                    if ("0".equals(bindBody.getRet()))
                    {
                        binddialog.dismiss();
                        changeUI();
                    }
                    else
                    {// 失败
                        Toast.makeText(LoginActivity.this, "bind fail", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            // 版本更新请求
            case RequestTypeConstants.APPVERSIONUPD_REQUEST:
                if (error)
                {
                    ResponseVersionUpdBody resBodyUPD = (ResponseVersionUpdBody)obj;
                    if (null != resBodyUPD)
                    {
                        GlobalState.getInstance().setAppVersion(resBodyUPD.getAppversion());
                        
                        // 提示更新
                        if ("0".equals(resBodyUPD.getRet()) && "0".equals(resBodyUPD.getAppupdpolicy()))
                        {
                            GlobalState.getInstance().setMIP_BASE_UPDATE_VERSION_URL(resBodyUPD.getApppath());
                            // GlobalState.getInstance()
                            // .setMIP_BASE_UPDATE_VERSION_URL("http://10.20.108.212:8080/mipinstall/app/1FDE7B5C8FC54864899C47CBF13CB15A/android/funambol-android-sync-client-10.0.4.apk");
                            
                            // GlobalState.getInstance()
                            // .setMIP_BASE_UPDATE_VERSION_ADDRESS(Environment.getExternalStorageDirectory() + "/"
                            // + this.getResources().getString(R.string.app_name) + "_"
                            // + resBodyUPD.getAppversion() + ".apk");
                            
                            // 这里来检测版本是否需要更新
                            // UpdateManager mUpdateManager = new UpdateManager(this);
                            // mUpdateManager.checkUpdateInfo();
                            
                            Intent intent = new Intent(LoginActivity.this, UpdDialogActivity.class);
                            intent.putExtra("Appupdpolicy", "0");
                            startActivity(intent);
                        }
                        // 强制更新
                        else if ("0".equals(resBodyUPD.getRet()) && "1".equals(resBodyUPD.getAppupdpolicy()))
                        {
                            GlobalState.getInstance().setMIP_BASE_UPDATE_VERSION_URL(resBodyUPD.getApppath());
                            // GlobalState.getInstance()
                            // .setMIP_BASE_UPDATE_VERSION_ADDRESS(Environment.getExternalStorageDirectory() + "/"
                            // + this.getResources().getString(R.string.app_name) + "_"
                            // + resBodyUPD.getAppversion() + ".apk");
                            
                            Intent intent = new Intent(LoginActivity.this, UpdDialogActivity.class);
                            intent.putExtra("Appupdpolicy", "1");
                            startActivity(intent);
                        }
                        // 静默更新
                        else if ("0".equals(resBodyUPD.getRet()) && "2".equals(resBodyUPD.getAppupdpolicy()))
                        {
                            System.out.println("---------------->>>------" + "静默更新");
                        }
                    }
                }
                break;
            
            default:
                break;
        }
    }
    
    /**
     * 
     * 离线登录判断
     * 
     * @Description 离线登录判断
     * 
     * @LastModifiedDate：2013-10-22
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void offlineLogin()
    {
        String offUser = userNameEdit.getText().toString().trim();
        String offPasswordString = passwordEdit.getText().toString().trim();
        ContentValues contentvalues =
            GlobalState.getInstance().getFromUerInfoTable(offUser, ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        if (null != contentvalues)
        {
            if (offPasswordString.equals(AesUtil.decrypt((String)contentvalues.get(TUSERINFO.PASSWORD),
                NetRequestController.MOA_deviceKey)))
            {
                GlobalState.getInstance().setOfflineLogin(true);
                changeUI();
            }
            else
            {
                // 提示密码错误
                final CustomDialog dialog =
                    CustomDialog.newInstance(getResources().getString(R.string.Login_passError),
                        getResources().getString(R.string.Login_Confirm));
                dialog.show(getSupportFragmentManager(), "LoginDialog");
                dialog.setMidListener(new CustomDialogListener()
                {
                    
                    @Override
                    public void Onclick()
                    {
                        dialog.dismiss();
                    }
                });
            }
        }
        else
        {
            // 提示用户不存在
            final CustomDialog dialog =
                CustomDialog.newInstance(getResources().getString(R.string.Login_noUser),
                    getResources().getString(R.string.Login_Confirm));
            dialog.show(getSupportFragmentManager(), "LoginDialog");
            dialog.setMidListener(new CustomDialogListener()
            {
                
                @Override
                public void Onclick()
                {
                    dialog.dismiss();
                }
            });
        }
    }
    
    /**
     * 
     * 解析登录返回（meta）
     * 
     * @Description 解析登录返回（meta）
     * 
     * @param response
     * @LastModifiedDate：2013-11-6
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @SuppressWarnings("unchecked")
    private void parseLoginMeta(MetaResponseBody response)
    {
        List<HashMap<String, Object>> ret = response.getBuzList();
        // 成功
        if ("0".equals(response.getRetError()) && (ret != null) && (ret.size() > 0))
        {
            ResponseLoginBody responseLoginBody = new ResponseLoginBody();
            responseLoginBody.setUserid((String)ret.get(0).get("userid"));
            responseLoginBody.setIsBindRemind((String)ret.get(0).get("isbindremind"));
            responseLoginBody.setUserName((String)ret.get(0).get("username"));
            responseLoginBody.setNewAppPath((String)ret.get(0).get("newapppath"));
            responseLoginBody.setNewUdpolicy((String)ret.get(0).get("newupdpolicy"));
            responseLoginBody.setNewVersion(((String)ret.get(0).get("newversion")).trim());
            responseLoginBody.setPreVersion((String)ret.get(0).get("preversion"));
            responseLoginBody.setUpDateLog((String)ret.get(0).get("updatelog"));
            responseLoginBody.setTel((String)ret.get(0).get("tel"));
            responseLoginBody.setToken((String)ret.get(0).get("token"));
            responseLoginBody.setEmail((String)ret.get(0).get("email"));
            responseLoginBody.setCompanyName((String)ret.get(0).get("companyname"));
            responseLoginBody.setCompanyId((String)ret.get(0).get("companyid"));
            
            // he
            // responseLoginBody.setLoginName((String)ret.get(0).get("loginName"));
            responseLoginBody.setDepartmentname((String)ret.get(0).get("departmentname"));
            responseLoginBody.setDuty((String)ret.get(0).get("duty"));
            responseLoginBody.setBaktel((String)ret.get(0).get("baktel"));
            responseLoginBody.setOfficetel((String)ret.get(0).get("officetel"));
            responseLoginBody.setBakofficetel((String)ret.get(0).get("bakofficetel"));
            responseLoginBody.setPeremail((String)ret.get(0).get("peremail"));
            responseLoginBody.setMarkweibo((String)ret.get(0).get("markweibo"));
            
            if (ret.get(0).get("module").equals(""))
            {
                Toast.makeText(this, "没有数据", Toast.LENGTH_LONG).show();
                return;
            }
            List<HashMap<String, Object>> moduleRet = (List<HashMap<String, Object>>)ret.get(0).get("module");
            
            List<HashMap<String, Object>> indexModuleRet = (List<HashMap<String, Object>>)ret.get(0).get("indexmodule");
            
            ArrayList<LoginModule> moduleList = new ArrayList<LoginModule>();
            if (moduleRet != null)
            {
                for (int i = 0; i < moduleRet.size(); i++)
                {
                    LoginModule module = new LoginModule();
                    module.setCount((String)moduleRet.get(i).get("count"));
                    module.setFuncCode((String)moduleRet.get(i).get("funccode"));
                    module.setFuncId((String)moduleRet.get(i).get("funcid"));
                    module.setFuncName((String)moduleRet.get(i).get("funcname"));
                    module.setSort((String)moduleRet.get(i).get("sort"));
                    module.setFlowtype((String)moduleRet.get(i).get("flowtype"));
                    module.setSearchkeywords((String)moduleRet.get(i).get("searchkeywords"));
                    module.setSearchtype((String)moduleRet.get(i).get("searchtype"));
                    moduleList.add(module);
                }
            }
            
            // 存表存数据
            SharedPreferences sharedoPreferences =
                GlobalState.getInstance().getSharedPreferences("data", Context.MODE_PRIVATE);
            Editor e = sharedoPreferences.edit();
            e.putString("userid", responseLoginBody.getUserid());
            e.putString("userName", responseLoginBody.getUserName());
            e.putString("openId", userNameEdit.getText().toString().trim());
            e.putString("preVersion", responseLoginBody.getPreVersion());
            e.putString("newApppath", responseLoginBody.getNewAppPath());
            e.putString("newUpdpolicy", responseLoginBody.getNewUdpolicy());
            e.putString("newVersion", responseLoginBody.getNewVersion().trim());
            e.putString("upDateLog", responseLoginBody.getUpDateLog());
            e.putString("tel", responseLoginBody.getTel());
            e.putString("companyname", responseLoginBody.getCompanyName());
            e.putString("token", responseLoginBody.getToken());
            e.putString("email", responseLoginBody.getEmail());
            e.putString("companyId", responseLoginBody.getCompanyId());
            
            // he
            // e.putString("loginName", responseLoginBody.getLoginName());
            e.putString("departmentname", responseLoginBody.getDepartmentname());
            e.putString("duty", responseLoginBody.getDuty());
            e.putString("baktel", responseLoginBody.getBaktel());
            e.putString("officetel", responseLoginBody.getOfficetel());
            e.putString("bakofficetel", responseLoginBody.getBakofficetel());
            e.putString("peremail", responseLoginBody.getPeremail());
            e.putString("markweibo", responseLoginBody.getMarkweibo());
            
            e.commit();
            GlobalState.getInstance().setIsRemember(isRemember);
            GlobalState.getInstance().setIsAutoLogin(isAutoLogin);
            
            if (!GlobalState.getInstance().getNewVersion().equals(responseLoginBody.getNewVersion()))
            {
                GlobalState.getInstance().setShowDialog(false);
            }
            
            GlobalState.getInstance().setNewVersion(responseLoginBody.getNewVersion());
            GlobalState.getInstance().setUpDateLog(responseLoginBody.getUpDateLog());
            saveUserInfoTable(userNameEdit.getText().toString().trim(),
                AesUtil.encrypt(passwordEdit.getText().toString().trim(), NetRequestController.MOA_deviceKey),
                responseLoginBody);
            saveFunsionPowerTable(userNameEdit.getText().toString().trim(), moduleList);
            DBDataObjectWrite.insertIndextModuleInfo(indexModuleRet, userNameEdit.getText().toString().trim());
            
            // 设备被其他人绑定
            if ("3".equals(responseLoginBody.getIsBindRemind()))
            {
                final CustomDialog dialog =
                    CustomDialog.newInstance(getResources().getString(R.string.Login_other_bind),
                        getResources().getString(R.string.Login_Confirm));
                dialog.show(getSupportFragmentManager(), "LoginDialog");
                dialog.setMidListener(new CustomDialogListener()
                {
                    
                    @Override
                    public void Onclick()
                    {
                        dialog.dismiss();
                    }
                });
            }
            else
            {
                
                // 设备已经绑定
                if ("2".equals(responseLoginBody.getIsBindRemind()))
                {
                    GlobalState.getInstance().setIsBind(true);
                }
                // 设备未绑定
                else if ("0".equals(responseLoginBody.getIsBindRemind())
                    || "1".equals(responseLoginBody.getIsBindRemind()))
                {
                    GlobalState.getInstance().setIsBind(false);
                }
                
                String mLocalVersionName = OsUtils.getVersionName(this);
                
                if (!"".equals(responseLoginBody.getNewVersion())
                    && mLocalVersionName.compareToIgnoreCase(responseLoginBody.getNewVersion()) < 0)// !responseLoginBody.getNewVersion().equals(OsUtils.getVersionName(this))
                {
                    
                    // 提示更新
                    if ("1".equals(responseLoginBody.getNewUdpolicy()))
                    {
                        if (!GlobalState.getInstance().isShowDialog())
                        {
                            GlobalState.getInstance().setShowDialog(true);
                            Intent intent = new Intent(LoginActivity.this, UpdDialogActivity.class);
                            intent.putExtra("Appupdpolicy", "0");
                            intent.putExtra("Login", true);
                            startActivity(intent);
                        }
                        else
                        {
                            changeUI();
                        }
                    }
                    // 强制更新
                    else if ("2".equals(responseLoginBody.getNewUdpolicy()))
                    {
                        Intent intent = new Intent(LoginActivity.this, UpdDialogActivity.class);
                        intent.putExtra("Appupdpolicy", "1");
                        startActivity(intent);
                    }// 不更新
                    else
                    {
                        changeUI();
                    }
                }
                else
                {
                    changeUI();
                }
            }
        }
        else
        {
            String tipMessge = "";
            // 用户不存在
            if ("2".equals(response.getRetError()))
            {
                tipMessge = getResources().getString(R.string.Login_noUser);
            }
            // 密码错误
            else if ("3".equals(response.getRetError()))
            {
                tipMessge = getResources().getString(R.string.Login_passError);
            }
            // 用户被其他设备绑定
            else if ("6".equals(response.getRetError()))
            {
                tipMessge = getResources().getString(R.string.Login_other_bind);
            }
            else if ("1".equals(response.getRetError()))
            {
                tipMessge = getResources().getString(R.string.Login_fail);
            }
            else
            {
                tipMessge = response.getRetMsg();
            }
            
            final CustomDialog dialog =
                CustomDialog.newInstance(tipMessge, getResources().getString(R.string.Login_Confirm));
            dialog.show(getSupportFragmentManager(), "LoginDialog");
            dialog.setMidListener(new CustomDialogListener()
            {
                
                @Override
                public void Onclick()
                {
                    dialog.dismiss();
                }
            });
        }
    }
    
    // /**
    // *
    // * 处理登录返回
    // *
    // * @Description 处理登录返回
    // *
    // * @LastModifiedDate：2013-9-27
    // * @author wang_ling
    // * @EditHistory：<修改内容><修改人>
    // */
    // private void parseLogin(NoMetaResponseBody response)
    // {
    // ResponseLoginBody resBody = new ResponseLoginBody();
    // if (response instanceof ResponseLoginBody)
    // {
    // resBody = (ResponseLoginBody)response;
    // }
    // if ("0".equals(response.getRet()))
    // {
    // ArrayList<LoginModule> moduleList = resBody.getModules();
    // saveUserInfoTable(userNameEdit.getText().toString().trim(),
    // AesUtil.encrypt(passwordEdit.getText().toString().trim(), NetRequestController.MOA_deviceKey),
    // resBody);
    // saveFunsionPowerTable(userNameEdit.getText().toString().trim(), moduleList);
    // // saveUserName();
    //
    // SharedPreferences sharedoPreferences =
    // GlobalState.getInstance().getSharedPreferences("data", Context.MODE_PRIVATE);
    // Editor e = sharedoPreferences.edit();
    // e.putString("nameSpace", resBody.getDeptName());
    // e.putString("userName", resBody.getUserName());
    // e.putString("openId", userNameEdit.getText().toString().trim());
    // e.putString("depId", resBody.getDeptId());
    // e.commit();
    // GlobalState.getInstance().setIsRemember(isRemember);
    // GlobalState.getInstance().setIsAutoLogin(isAutoLogin);
    // GlobalState.getInstance().setNameSpace(resBody.getDeptName());
    // GlobalState.getInstance().setUserName(resBody.getUserName());
    // // 设备未绑定
    // if ("0".equals(resBody.getIsBindRemind()))
    // {
    // binddialog =
    // CustomDialog.newInstance(getResources().getString(R.string.Login_bind_tip),
    // getResources().getString(R.string.Login_Confirm),
    // getResources().getString(R.string.Login_Cancel));
    //
    // binddialog.show(getSupportFragmentManager(), "BindDeviceDialog");
    // binddialog.setLeftListener(new CustomDialogListener()
    // {
    //
    // @Override
    // public void Onclick()
    // {
    // waitDialog = WaitDialog.newInstance();
    // waitDialog.show(getSupportFragmentManager(), "waitDialog");
    // NetRequestController.SendBindDevice(mHandler, RequestTypeConstants.BINDDEVICE_REQUEST, "1");
    //
    // }
    // });
    // binddialog.setRightListener(new CustomDialogListener()
    // {
    //
    // @Override
    // public void Onclick()
    // {
    // binddialog.dismiss();
    // changeUI();
    // }
    // });
    // }
    // else
    // {
    // changeUI();
    // }
    // }
    // // 密码错误
    // else if ("3".equals(response.getRet()))
    // {
    // final CustomDialog dialog =
    // CustomDialog.newInstance(getResources().getString(R.string.Login_passError),
    // getResources().getString(R.string.Login_Confirm));
    // dialog.show(getSupportFragmentManager(), "LoginDialog");
    // dialog.setMidListener(new CustomDialogListener()
    // {
    //
    // @Override
    // public void Onclick()
    // {
    // dialog.dismiss();
    // }
    // });
    // }
    // // 用户不存在
    // else if ("2".equals(response.getRet()))
    // {
    // final CustomDialog dialog =
    // CustomDialog.newInstance(getResources().getString(R.string.Login_noUser),
    // getResources().getString(R.string.Login_Confirm));
    // dialog.show(getSupportFragmentManager(), "LoginDialog");
    // dialog.setMidListener(new CustomDialogListener()
    // {
    //
    // @Override
    // public void Onclick()
    // {
    // dialog.dismiss();
    // }
    // });
    // }
    // // 用户在其他设备被绑定，在本设备上不允许使用
    // else if ("6".equals(response.getRet()) && "3".equals(resBody.getIsBindRemind()))
    // {
    // final CustomDialog dialog =
    // CustomDialog.newInstance(getResources().getString(R.string.Login_other_bind),
    // getResources().getString(R.string.Login_Confirm));
    // dialog.show(getSupportFragmentManager(), "LoginDialog");
    // dialog.setMidListener(new CustomDialogListener()
    // {
    //
    // @Override
    // public void Onclick()
    // {
    // dialog.dismiss();
    // }
    // });
    // // NetRequestController.SendBindDevice(mHandler, RequestTypeConstants.BINDDEVICE_REQUEST, "0");
    // }
    // }
    
    /**
     * 
     * 页面跳转
     * 
     * @Description 页面跳转
     * 
     * @LastModifiedDate：2013-9-26
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void changeUI()
    {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivityNewActivity.class);
        startActivity(intent);
        this.finish();
    }
    
    /**
     * 
     * 将用户信息存到TUSERINFO表中
     * 
     * @Description 将用户信息存到TUSERINFO表中
     * 
     * @param openId 用户id
     * @param password 密码
     * @param resBody 返回报文的body
     * @LastModifiedDate：2013-9-26
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void saveUserInfoTable(String openId, String password, ResponseLoginBody resBody)
    {
        DBHandler hander = new DBHandler(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        hander.DBHandlerDBOpen(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        ContentValues contentValue = new ContentValues();
        contentValue.put(TUSERINFO.OPENID, openId);
        contentValue.put(TUSERINFO.PASSWORD, password);
        contentValue.put(TUSERINFO.DEPID, resBody.getDeptId());
        contentValue.put(TUSERINFO.USERNAME, resBody.getUserName());
        contentValue.put(TUSERINFO.ISBINDREMIND, resBody.getIsBindRemind());
        contentValue.put(TUSERINFO.ISLOST, "");
        contentValue.put(TUSERINFO.FAXID, "");
        contentValue.put(TUSERINFO.SESSIONID, "");
        
        try
        {
            Cursor cursor =
                hander.query(MessageSQLIdConstants.DB_MESSAGE_USERINFO,
                    null,
                    TUSERINFO.OPENID + "=?",
                    new String[] {openId},
                    null);
            if (cursor.moveToNext())
            {
                hander.update(MessageSQLIdConstants.DB_MESSAGE_USERINFO,
                    contentValue,
                    TUSERINFO.OPENID + "=?",
                    new String[] {openId});
            }
            else
            {
                hander.insert(MessageSQLIdConstants.DB_MESSAGE_USERINFO, contentValue);
            }
            cursor.close();
        }
        catch (MIPException e)
        {
            new MIPException().printStackTrace();
        }
        hander.DBHandlerDBClose();
    }
    
    /**
     * 
     * 将功能模块信息存到TFUNCTIONPOWER
     * 
     * @Description 将功能模块信息存到TFUNCTIONPOWER
     * 
     * @param openId 用户id
     * @param moduleList 模块列表
     * @LastModifiedDate：2013-9-26
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void saveFunsionPowerTable(String openId, ArrayList<LoginModule> moduleList)
    {
        DBHandler hander = new DBHandler(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        hander.DBHandlerDBOpen(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        try
        {
            hander.delete(MessageSQLIdConstants.DB_MESSAGE_FUNCTIONPOWER,
                TFUNSIONPOWER.OPENID + "=?",
                new String[] {openId});
        }
        catch (MIPException e1)
        {
            e1.printStackTrace();
        }
        for (int i = 0; i < moduleList.size(); i++)
        {
            ContentValues contentValue = new ContentValues();
            contentValue.put(TFUNSIONPOWER.OPENID, openId);
            contentValue.put(TFUNSIONPOWER.FUNTIONID, moduleList.get(i).getFuncId());
            contentValue.put(TFUNSIONPOWER.NAME, moduleList.get(i).getFuncName());
            contentValue.put(TFUNSIONPOWER.COUNT, moduleList.get(i).getCount());
            contentValue.put(TFUNSIONPOWER.SORT, moduleList.get(i).getSort());
            contentValue.put(TFUNSIONPOWER.FUNTIONCODE, moduleList.get(i).getFuncCode());
            contentValue.put(TFUNSIONPOWER.FLOWTYPE, moduleList.get(i).getFlowtype());
            contentValue.put(TFUNSIONPOWER.SEARCHKEYWORDS, moduleList.get(i).getSearchkeywords());
            contentValue.put(TFUNSIONPOWER.SEARCHTYPE, moduleList.get(i).getSearchtype());
            contentValue.put(TFUNSIONPOWER.REFLECTNAME, "");
            try
            {
                
                Cursor cursor =
                    hander.query(MessageSQLIdConstants.DB_MESSAGE_FUNCTIONPOWER, null, TFUNSIONPOWER.OPENID + "=? and "
                        + TFUNSIONPOWER.FUNTIONID + "=?", new String[] {openId, moduleList.get(i).getFuncId()}, null);
                if (cursor.moveToNext())
                {
                    hander.update(MessageSQLIdConstants.DB_MESSAGE_FUNCTIONPOWER, contentValue, TFUNSIONPOWER.OPENID
                        + "=? and " + TFUNSIONPOWER.FUNTIONID + "=?", new String[] {openId,
                        moduleList.get(i).getFuncId()});
                }
                else
                {
                    hander.insert(MessageSQLIdConstants.DB_MESSAGE_FUNCTIONPOWER, contentValue);
                }
                cursor.close();
            }
            catch (MIPException e)
            {
                new MIPException().printStackTrace();
            }
        }
        
        hander.DBHandlerDBClose();
    }
    
    Timer mtimer = new Timer();
    
    /**
     * 
     * 重载方法
     * 
     * @param savedInstanceState savedInstanceState
     * @author wang_ling
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.login_mian_new);
        initView();
        imgbackgroud_array = new int[] {R.drawable.background_3, R.drawable.background_2, R.drawable.background_1};
        // 删除我的应用中的应用图标
        // File file = new File(UpdateManager.imagesavePath);
        // if (file.exists())
        // {
        // FileUtil.deleteDir(file);
        // }
        GlobalState.getInstance().setFirstDownPic(true);
        // 删除我的应用中的应用图标
        
        // currentNum = 0;
        // timer = new Timer();
        // timer.schedule(task, 3000, 1000 * 3);
        
        userNameEdit.setFocusable(true);
        userNameEdit.setFocusableInTouchMode(true);
        userNameEdit.requestFocus();
        
        mtimer.schedule(new TimerTask()
        {
            public void run()
            {
                InputMethodManager inputManager =
                    (InputMethodManager)userNameEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(userNameEdit, 0);
            }
        },
            998);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        // TODO Auto-generated method stub
        // super.onSaveInstanceState(outState);
    }
    
    /** 计时器 */
    private Timer   timer;
    
    TimerTask       task           = new TimerTask()
                                   {
                                       public void run()
                                       {
                                           Message message = Message.obtain();
                                           message.what = 1001;
                                           changeBgHandle.sendMessage(message);
                                       }
                                   };
    
    /** 程序背景图片 */
    private int     imgbackgroud_array[];
    
    /** 当前是第几张背景图 **/
    private int     currentNum     = 0;
    
    /**
     * 更换背景图
     */
    private Handler changeBgHandle = new Handler()
                                   {
                                       @Override
                                       public void handleMessage(Message msg)
                                       {
                                           switch (msg.what)
                                           {
                                               case 1001:
                                                   if (currentNum >= imgbackgroud_array.length)
                                                   {
                                                       currentNum = 0;
                                                   }
                                                   mLogin_layout.setBackgroundResource(imgbackgroud_array[currentNum]);
                                                   currentNum++;
                                                   break;
                                               default:
                                                   break;
                                           }
                                       }
                                   };
    
    @Override
    protected void onResume()
    {
        super.onResume();
        initData();
        Intent intent = getIntent();
        String biaozhi = intent.getStringExtra("biaozhi");
        if (isAutoLogin && biaozhi.equals("1"))
        {
            loginButton.performClick();
        }
        // loginButton.performClick();
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    protected void onDestroy()
    {
        // 停止timer
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }
    
    /**
     * 
     * 发送更新请求
     * 
     * @Description 发送更新请求
     * 
     * @LastModifiedDate：2013-9-27
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @SuppressWarnings("unused")
    private void requestUpdateRequest()
    {
        NetRequestController.SendAppVersionUpd(mHandler, RequestTypeConstants.APPVERSIONUPD_REQUEST, null);
    }
    
    /**
     * 
     * 初始化界面
     * 
     * @Description 初始化界面
     * 
     * @LastModifiedDate：2013-9-23
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initView()
    {
        
        mLogin_layout = (RelativeLayout)findViewById(R.id.login_layout);
        
        userNameEdit = (EditText)this.findViewById(R.id.login_username);
        passwordEdit = (EditText)this.findViewById(R.id.login_password);
        passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        
        versionTV = (TextView)this.findViewById(R.id.version);
        
        loginButton = (Button)this.findViewById(R.id.login_button);
        rememberButton = (ImageView)this.findViewById(R.id.rememberImg);
        
        // headImageV = (ImageView)this.findViewById(R.id.headImage);
        newVersionRel = (RelativeLayout)this.findViewById(R.id.newRel);
        newVersionTv = (TextView)this.findViewById(R.id.newversion);
        autoLoginButton = (ImageView)this.findViewById(R.id.autoImg);
        getCodeBtn = (ImageButton)this.findViewById(R.id.getCode);
        
        loginButton.setOnClickListener(this);
        rememberButton.setOnClickListener(this);
        
        newVersionRel.setOnClickListener(this);
        autoLoginButton.setOnClickListener(this);
        getCodeBtn.setOnClickListener(this);
        
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description 初始化数据
     * 
     * @LastModifiedDate：2013-9-23
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        // int bgId = GlobalState.getInstance().getBackGroundId(R.drawable.bg_1_1);
        // mLogin_layout.setBackgroundResource(bgId);
        
        // add by wen_tao 获取本地头像
        Bitmap bitmap = GetLocalPic.getHeader();
        // if (null == bitmap)
        // {
        // headImageV.setBackgroundResource(R.drawable.photo_default);
        // }
        // else
        // {
        // headImageV.setImageBitmap(bitmap);
        // }
        
        String userNameString = GlobalState.getInstance().getOpenId();
        ContentValues values =
            GlobalState.getInstance()
                .getFromUerInfoTable(userNameString, ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        String password = "";
        if (null != values)
        {
            password = (String)values.get(TUSERINFO.PASSWORD);
            password = AesUtil.decrypt(password, NetRequestController.MOA_deviceKey);
        }
        
        isRemember = GlobalState.getInstance().getIsRemember();
        isAutoLogin = GlobalState.getInstance().getIsAutoLogin();
        
        if (isAutoLogin)
        {
            autoLoginButton.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selected_s));
            rememberButton.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selected_s));
            userNameEdit.setText(userNameString);
            passwordEdit.setText(password);
            userNameEdit.setSelection(userNameString.length());
            passwordEdit.setSelection(password.length());
        }
        else
        {
            autoLoginButton.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selector_login_saveno));
            if (isRemember)
            {
                rememberButton.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selected_s));
                userNameEdit.setText(userNameString);
                passwordEdit.setText(password);
                userNameEdit.setSelection(userNameString.length());
                passwordEdit.setSelection(password.length());
            }
            else
            {
                rememberButton.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selector_login_saveno));
            }
        }
        
        versionTV.setText("v " + OsUtils.getVersionName(LoginActivity.this));
        String mLocalVersionName = OsUtils.getVersionName(this);
        if (!"".equals(GlobalState.getInstance().getNewVersion())
            && mLocalVersionName.compareToIgnoreCase(GlobalState.getInstance().getNewVersion()) < 0)// !GlobalState.getInstance().getNewVersion().equals(OsUtils.getVersionName(this))
        {
            newVersionRel.setVisibility(View.VISIBLE);
            newVersionTv.setText("v " + GlobalState.getInstance().getNewVersion());
            GlobalState.getInstance().setShowDialog(true);
        }
        else
        {
            GlobalState.getInstance().setShowDialog(false);
            newVersionRel.setVisibility(View.GONE);
        }
        
        // userNameEdit.setText("js_123");
        // passwordEdit.setText("123456");
    }
    
    /**
     * 
     * 保存用户信息
     * 
     * @Description 保存用户信息
     * 
     * @LastModifiedDate：2013-9-24
     * @author wang_ling
     * @EditHistory：<修改内容><修改人> //
     */
    // private void saveUserName()
    // {
    // SharedPreferences sharedoPreferences =
    // GlobalState.getInstance().getSharedPreferences("data", Context.MODE_PRIVATE);
    // Editor e = sharedoPreferences.edit();
    // if (isRemember)
    // {
    // e.putString("username", userNameEdit.getText().toString().trim());
    // e.putString("password",
    // AesUtil.encrypt(passwordEdit.getText().toString().trim(), NetRequestController.MOA_deviceKey));
    //
    // }
    // else
    // {
    // e.putString("password", "");
    // e.putString("username", "");
    // }
    // e.commit();
    // GlobalState.getInstance().setIsRemember(isRemember);
    // GlobalState.getInstance().setIsAutoLogin(isAutoLogin);
    // }
    
    // private void getpackgename()
    // {
    // String archiveFilePath = Environment.getExternalStorageDirectory() + "/apk/androidmobile_enter (1).apk";// 安装包路径
    // PackageManager pm = getPackageManager();
    // PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
    // if (info != null)
    // {
    // ApplicationInfo appInfo = info.applicationInfo;
    // String appName = pm.getApplicationLabel(appInfo).toString();
    // String packageName = appInfo.packageName; // 得到安装包名称
    // String version = info.versionName; // 得到版本信息
    // System.out.println("--------packageName:" + packageName + ";version:" + version);
    // Toast.makeText(this, "packageName:" + packageName + ";version:" + version, Toast.LENGTH_LONG).show();
    // // Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息
    // // TextView tv = (TextView)findViewById(R.id.tv); //显示图标
    // // tv.setBackgroundDrawable(icon);
    // }
    // }
    
    /**
     * 重载方法
     * 
     * @param v 视图
     * @author wang_ling
     */
    @Override
    public void onClick(View v)
    {
        // 点击时间间隔太短则不触发
        if (OnClickUtil.isMostPost())
        {
            
            return;
        }
        switch (v.getId())
        {// 登录按钮
            case R.id.login_button:
                // getpackgename();
                if (!userNameEdit.getText().toString().equals("") && !passwordEdit.getText().toString().equals(""))
                {
                    
                    GlobalState.getInstance().setOpenId(userNameEdit.getText().toString().trim());
                    waitDialog = WaitDialog.newInstance();
                    waitDialog.show(getSupportFragmentManager(), "waitDialog");
                    
                    UserInfo userInfo = new UserInfo();
                    userInfo.setOpenId(userNameEdit.getText().toString().trim());// userNameEdit.getText().toString().trim()
                    userInfo.setPassword(passwordEdit.getText().toString().trim());// passwordEdit.getText().toString().trim()
                    NetRequestController.SendLogin(mHandler, RequestTypeConstants.LOGIN_REQUEST, userInfo);
                }
                else if (userNameEdit.getText().toString().equals(""))
                {
                    // 用户名不能为空
                    final CustomDialog dialog =
                        CustomDialog.newInstance(getResources().getString(R.string.Login_nameNull),
                            getResources().getString(R.string.Login_Confirm));
                    dialog.show(getSupportFragmentManager(), "LoginDialog");
                    dialog.setMidListener(new CustomDialogListener()
                    {
                        
                        @Override
                        public void Onclick()
                        {
                            dialog.dismiss();
                        }
                    });
                }
                else
                {
                    // 密码不能为空
                    final CustomDialog dialog =
                        CustomDialog.newInstance(getResources().getString(R.string.Login_passNull),
                            getResources().getString(R.string.Login_Confirm));
                    dialog.show(getSupportFragmentManager(), "LoginDialog");
                    dialog.setMidListener(new CustomDialogListener()
                    {
                        
                        @Override
                        public void Onclick()
                        {
                            dialog.dismiss();
                        }
                    });
                }
                
                break;
            // 是否记住用户名按钮
            case R.id.rememberImg:
                isRemember = !isRemember;
                if (isRemember)
                {
                    rememberButton.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selected_s));
                }
                else
                {
                    rememberButton.setBackgroundDrawable(this.getResources()
                        .getDrawable(R.drawable.selector_login_saveno));
                    autoLoginButton.setBackgroundDrawable(this.getResources()
                        .getDrawable(R.drawable.selector_login_saveno));
                    isAutoLogin = isRemember;
                }
                break;
            // 是否自动登录
            case R.id.autoImg:
                isAutoLogin = !isAutoLogin;
                
                if (isAutoLogin)
                {
                    autoLoginButton.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selected_s));
                    rememberButton.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selected_s));
                    isRemember = isAutoLogin;
                }
                else
                {
                    autoLoginButton.setBackgroundDrawable(this.getResources()
                        .getDrawable(R.drawable.selector_login_saveno));
                }
                break;
            // 检查最新版本
            case R.id.newRel:
                String policy = "";
                if ("1".equals(GlobalState.getInstance().getNewUdpolicy())
                    || "0".equals(GlobalState.getInstance().getNewUdpolicy()))
                {
                    policy = "0";
                }
                else if ("2".equals(GlobalState.getInstance().getNewUdpolicy()))
                {
                    policy = "1";
                }
                Intent intent = new Intent(LoginActivity.this, UpdDialogActivity.class);
                intent.putExtra("Appupdpolicy", policy);
                startActivity(intent);
                break;
            // 找回密码按钮
            case R.id.getCode:
                break;
            default:
                break;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            final CustomDialog dialog =
                CustomDialog.newInstance(this.getResources().getString(R.string.main_exit_app), this.getResources()
                    .getString(R.string.Login_Cancel), this.getResources().getString(R.string.Login_Confirm));
            dialog.show(LoginActivity.this.getSupportFragmentManager(), "ExitDialog");
            
            dialog.setRightListener(new CustomDialogListener()
            {
                
                @Override
                public void Onclick()
                {
                    dialog.dismiss();
                    GlobalState.getInstance().exitApplication();
                }
            });
            
            dialog.setLeftListener(new CustomDialogListener()
            {
                
                @Override
                public void Onclick()
                {
                    dialog.dismiss();
                    
                }
            });
            
        }
        return false;
    }
    
    /**
     * 获取手机验证码
     * 
     * @Description<功能详细描述>
     * 
     * @param phoneNum
     * @LastModifiedDate：2014-2-26
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void getMsgVerify(String phoneNum)
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("phoneNum", phoneNum);
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        NetRequestController.SendGetMsgVerify(mHandler, RequestTypeConstants.GETVFYCODE_REQUEST, body);
    }
    
    /**
     * 发送手机收到的验证码
     * 
     * @Description<功能详细描述>
     * 
     * @param phoneNum
     * @param randomCode
     * @LastModifiedDate：2014-2-26
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void sendMsgVerify(String phoneNum, String randomCode)
    {
        JSONObject body = new JSONObject();
        try
        {
            body.put("phoneNum", phoneNum);
            body.put("randomCode", randomCode);
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        NetRequestController.SendMsgVerify(mHandler, RequestTypeConstants.SENDVFYCODE_REQUEST, body);
    }
}
