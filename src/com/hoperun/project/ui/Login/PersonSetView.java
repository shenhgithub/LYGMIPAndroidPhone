/*
 * File name:  PersonSetView.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2013-11-5
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.Login;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.components.CustomDialog;
import com.hoperun.manager.components.CustomDialogListener;
import com.hoperun.manager.components.WaitDialog;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.AesUtil;
import com.hoperun.mip.utils.OsUtils;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;
import com.hoperun.project.ui.baseui.baseInterface.IFragmentToMainActivityListen;

/**
 * 个人设置中心view
 * 
 * @Description 个人设置中心view
 * 
 * @author wang_ling
 * @Version [版本号, 2013-11-5]
 */
public class PersonSetView extends PMIPBaseActivity implements OnClickListener, OnItemClickListener
{
    /**
     * 监听变量，供每个Fragment有事件向MainActivity中传递
     */
    protected IFragmentToMainActivityListen mFragmentTMainActivityListener;
    
    /** 等待对话框 **/
    private WaitDialog                      waitDialog;
    
    /** 返回body **/
    private MetaResponseBody                responseBuzBody;
    
    /** 计时器 */
    private Timer                           timer;
    
    // ============================个人设置 begin============================
    
    private ImageView                       backView;
    
    /** 修改密码布局 **/
    private RelativeLayout                  changePassRel;
    
    /** 个人信息布局 **/
    private RelativeLayout                  personalDetail;
    
    /** 修改主题布局 **/
    private RelativeLayout                  changeThemeRel;
    
    /** 自动登录按钮 **/
    private ImageButton                     autoBtn;
    
    /** 绑定按钮 **/
    private ImageButton                     bindBtn;
    
    /** 检查更新按钮 **/
    private Button                          checkVerBtn;
    
    /** 清除缓存按钮 **/
    private Button                          clearBtn;
    
    /** 是否自动登录 **/
    private boolean                         isAutoLogin;
    
    /** 是否绑定 **/
    private boolean                         isBind;
    
    // ============================个人设置 end============================
    // ============================主题设置 begin============================
    /** 主题布局 **/
    private RelativeLayout                  themeSetLinear_rl;
    
    private RelativeLayout                  themeClose;
    
    // ============================主题设置 end============================
    // =============================密码修改 begin==========================
    /** 旧密码输入框 **/
    private EditText                        oldPassword;
    
    /** 新密码输入框 **/
    private EditText                        newPassword;
    
    /** 确认新密码输入框 **/
    private EditText                        confirmPassword;
    
    /** 确定按钮 **/
    private Button                          confirmBtn;
    
    /** 取消按钮 **/
    private Button                          cancelBtn;
    
    /** 确定按钮 **/
    private Button                          personal_confirmBtn;
    
    /** 取消按钮 **/
    private Button                          modifyBtn;
    
    /** 修改密码对话框 **/
    private Dialog                          changePassDialog;
    
    /** 个人信息对话框 **/
    private Dialog                          personalDetailDialog;
    
    // =============================密码修改 end==========================
    // ==============================设备绑定 begin========================
    /** 绑定用户名 **/
    private TextView                        bindUserNameTv;
    
    /** 设备号 **/
    private TextView                        deviceIdTv;
    
    /** 绑定密码 **/
    private EditText                        bindPassEt;
    
    /** 暂不绑定 **/
    private Button                          bindCancel;
    
    /** 确认绑定 **/
    private Button                          bindConfirm;
    
    /** 绑定对话框 **/
    private Dialog                          bindDeviceDialog;
    
    // ==============================设备绑定 end========================
    
    // ===============================清除缓存 begin=============================
    /** 等待布局 **/
    private LinearLayout                    waitLinear;
    
    /** 清除结束 **/
    private LinearLayout                    okLinear;
    
    /** 等待loading **/
    private ImageView                       waitImage;
    
    /** 确定按钮 **/
    private Button                          okButton;
    
    /** 清楚对话框 **/
    private Dialog                          clearDialog;
    
    /** 名字 **/
    private TextView                        nameTextView;
    
    /** 手机号 **/
    private EditText                        telephone;
    
    /** 邮件地址 **/
    private EditText                        email_edit;
    
    /** 手机号 **/
    private TextView                        phone_text;
    
    /** 部门名称 **/
    private EditText                        departmentname_edit;
    
    private TextView                        departmentname_text;
    
    /** 职务 **/
    private EditText                        duty_edit;
    
    private TextView                        duty_text;
    
    /** 备用手机 **/
    private EditText                        baktel_edit;
    
    private TextView                        baktel_text;
    
    /** 办公电话 **/
    private EditText                        officetel_edit;
    
    private TextView                        officetel_text;
    
    /** 备用电话 **/
    private EditText                        bakofficetel_edit;
    
    private TextView                        bakofficetel_text;
    
    /** 个人邮箱 **/
    private EditText                        peremail_edit;
    
    private TextView                        peremail_text;
    
    /** 云之家微博开通标识 **/
    private EditText                        markweibo_edit;
    
    private TextView                        markweibo_text;
    
    private ImageView                       iv_close;
    
    // ===============================清除缓存 end==============================
    // ===============================头像设置 begin=============================
    
    // ===============================头像设置 end==============================
    /**
     * 更换清除对话框
     */
    private Handler                         clearHandler = new Handler()
                                                         {
                                                             @Override
                                                             public void handleMessage(Message msg)
                                                             {
                                                                 switch (msg.what)
                                                                 {
                                                                     case 10:
                                                                     {
                                                                         waitLinear.setVisibility(View.INVISIBLE);
                                                                         okLinear.setVisibility(View.VISIBLE);
                                                                         break;
                                                                     }
                                                                     default:
                                                                         break;
                                                                 }
                                                             }
                                                         };
    
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
                case RequestTypeConstants.BINDDEVICE_REQUEST:
                    if (null != objBody)
                    {
                        waitDialog.dismiss();
                        responseBuzBody = (MetaResponseBody)objBody;
                        String ret = responseBuzBody.getRetError();
                        HashMap<String, Object> result = new HashMap<String, Object>();
                        if (null != responseBuzBody.getBuzList() && responseBuzBody.getBuzList().size() > 0)
                        {
                            result = (HashMap<String, Object>)responseBuzBody.getBuzList().get(0);
                        }
                        if ("0".equals(ret))// 成功
                        {
                            if (null != result)
                            {
                                GlobalState.getInstance().setToken((String)result.get("token"));
                                GlobalState.getInstance().setUserName((String)result.get("username"));
                            }
                            if (isBind)
                            {
                                GlobalState.getInstance().setIsBind(false);
                                isBind = false;
                                // bindBtn.setBackgroundDrawable(this.getResources()
                                // .getDrawable(R.drawable.selector_login_saveno));
                                bindBtn.setBackgroundResource(R.drawable.switch_3);
                                Toast.makeText(this, "解绑成功", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                GlobalState.getInstance().setIsBind(true);
                                isBind = true;
                                // bindBtn.setBackgroundDrawable(this.getResources()
                                // .getDrawable(R.drawable.selector_login_saveyes));
                                bindBtn.setBackgroundResource(R.drawable.switch_2);
                                Toast.makeText(this, "绑定成功", Toast.LENGTH_SHORT).show();
                            }
                            // mFragmentTMainActivityListener.onPersonSetBind();
                            bindDeviceDialog.dismiss();
                        }
                        else
                        {
                            String msg = responseBuzBody.getRetMsg();
                            if (null == msg || "".equals(msg))
                            {
                                msg = "失败";
                            }
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        waitDialog.dismiss();
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                // 密码修改
                case RequestTypeConstants.CHANGEPASSWORD_REQUEST:
                    if (null != objBody)
                    {
                        waitDialog.dismiss();
                        responseBuzBody = (MetaResponseBody)objBody;
                        String ret = responseBuzBody.getRetError();
                        if ("0".equals(ret))
                        {
                            String newp = newPassword.getText().toString().trim();
                            GlobalState.getInstance().updateUserPassword(GlobalState.getInstance().getOpenId(),
                                AesUtil.encrypt(newp, NetRequestController.MOA_deviceKey),
                                ConstState.DB_FILEPATH,
                                ConstState.DB_VERSION);
                            changePassDialog.dismiss();
                            Toast.makeText(this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(this, "密码修改失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        waitDialog.dismiss();
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                // 个人信息修改
                case RequestTypeConstants.UPDATEPERSONAL_REQUEST:
                    if (null != objBody)
                    {
                        waitDialog.dismiss();
                        responseBuzBody = (MetaResponseBody)objBody;
                        String ret = responseBuzBody.getRetError();
                        if ("0".equals(ret))
                        {
                            GlobalState.getInstance().setTelNum(telephone.getText().toString().trim());
                            // GlobalState.getInstance().setEmail(email_edit.getText().toString().trim());
                            
                            // he
                            // GlobalState.getInstance()
                            // .setDepartmentname(departmentname_edit.getText().toString().trim());
                            GlobalState.getInstance().setDuty(duty_edit.getText().toString().trim());
                            GlobalState.getInstance().setBaktel(baktel_edit.getText().toString().trim());
                            GlobalState.getInstance().setOfficetel(officetel_edit.getText().toString().trim());
                            GlobalState.getInstance().setBakofficetel(bakofficetel_edit.getText().toString().trim());
                            GlobalState.getInstance().setPeremail(peremail_edit.getText().toString().trim());
                            // GlobalState.getInstance().setMarkweibo(markweibo_edit.getText().toString().trim());
                            
                            personalDetailDialog.dismiss();
                            Toast.makeText(this, "个人信息修改成功！", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(this, "个人信息修改失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        waitDialog.dismiss();
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
            
        }
        else
        {
            waitDialog.dismiss();
            if (!(errorCode == ConstState.CANCEL_THREAD))
            {
                Toast.makeText(this, "请求服务器数据失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * 重载方法
     * 
     * @param v view
     * @author wang_ling
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        // 返回主页面
            case R.id.userset:
                // Intent intentMain = new Intent(this, MainActivityNewActivity.class);
                // startActivity(intentMain);
                this.finish();
                break;
            // 个人信息
            case R.id.personal:
                showPersonalDialog();
                break;
            // 修改密码
            case R.id.changePass_Rel:
                showPasswordDialog();
                break;
            // 主题设置
            case R.id.selecttheme:
                themeSetLinear_rl.setVisibility(View.VISIBLE);
                themeSetLinear_rl.getBackground().setAlpha(100);
                themeSetLinear_rl.bringToFront();
                themeSetLinear_rl.invalidate();
                break;
            case R.id.button_close:
                themeSetLinear_rl.setVisibility(View.GONE);
                break;
            // 自动登录
            case R.id.autoLogin_Button:
                isAutoLogin = !isAutoLogin;
                if (isAutoLogin)
                {
                    autoBtn.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.switch_2));
                }
                else
                {
                    autoBtn.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.switch_3));
                }
                GlobalState.getInstance().setIsAutoLogin(isAutoLogin);
                break;
            // 设备绑定
            case R.id.bind_Button:
                showBindDialog();
                break;
            // 检查更新
            case R.id.check_button:
                // 已是最新版本
                if ("".equals(GlobalState.getInstance().getNewVersion())
                    || GlobalState.getInstance().getNewVersion().equals(OsUtils.getVersionName(PersonSetView.this)))
                {
                    final CustomDialog dialog =
                        CustomDialog.newInstance(getResources().getString(R.string.UpdDialog_noUpdate),
                            getResources().getString(R.string.Login_Confirm));
                    dialog.show(getSupportFragmentManager(), "checkDialog");
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
                    Intent intent = new Intent(PersonSetView.this, UpdDialogActivity.class);
                    intent.putExtra("Appupdpolicy", "0");
                    startActivity(intent);
                }
                break;
            // 清除数据
            case R.id.clear_button:
                // showClearDialog();
                // TimerTask task = new TimerTask()
                // {
                // public void run()
                // {
                // timer.cancel();
                // Message message = Message.obtain();
                // message.what = 10;
                // clearHandler.sendMessage(message);
                // }
                // };
                // timer = new Timer();
                // timer.schedule(task, 3000);
                
                File dirFile = new File(ConstState.MIP_ROOT_DIR + GlobalState.getInstance().getOpenId());
                
                // if (dirFile.exists())
                // {
                // FileUtil.deleteDir(dirFile);
                // }
                ClearLocalCacheDataDialog clearLocalCacheData = new ClearLocalCacheDataDialog(this);
                clearLocalCacheData.showDialog();
                // Message message = Message.obtain();
                // message.what = 10;
                // clearHandler.sendMessage(message);
                
                break;
            default:
                break;
        }
    }
    
    /**
     * 重载方法
     * 
     * @param savedInstanceState savedInstanceState
     * @author wang_ling
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.person_set);
        
        // 在此添加了布局的适配问题：其中200是手动添加的固定值，以后可能会加以完善（自动获取）
        // RelativeLayout personLayout = (RelativeLayout)findViewById(R.id.person_set_layout);
        // android.widget.FrameLayout.LayoutParams lp =
        // (android.widget.FrameLayout.LayoutParams)personLayout.getLayoutParams();
        // if (dptopx(lp.height) > GlobalState.getInstance().getmScreen_Height() - 100)
        // {
        // lp.height =
        // (int)((GlobalState.getInstance().getmScreen_Height() - 150) * ((float)GlobalState.getInstance()
        // .getmDensityDpi() / 160));
        // }
        
        initSetView();
        // initData();
        initSetData();
        // this.setFinishOnTouchOutside(false);
        // mFragmentTMainActivityListener =
        // (IFragmentToMainActivityListen)GlobalState.getInstance()
        // .findActivitieByName("com.hoperun.project.view.ui.MainActivity");
    }
    
    // private void initData()
    // {
    // // ===================主题设置===========================================
    //
    // imgbackgroud_array =
    // new int[] {R.drawable.bg_1_1, R.drawable.bg_2_1, R.drawable.bg_3_1, R.drawable.bg_4_1, R.drawable.bg_5_1,
    // R.drawable.bg_6_1, R.drawable.bg_7_1};
    //
    // img_array =
    // new int[] {R.drawable.bg_1_2, R.drawable.bg_2_2, R.drawable.bg_3_2, R.drawable.bg_4_2, R.drawable.bg_5_2,
    // R.drawable.bg_6_2, R.drawable.bg_7_2};
    //
    // ThemeAdapter tp = new ThemeAdapter(this, getdata());
    //
    // LayoutParams params =
    // new LayoutParams(img_array.length * (188) + (img_array.length - 1) * 10, LayoutParams.WRAP_CONTENT);
    // themeGridView.setLayoutParams(params);
    // themeGridView.setColumnWidth(188);
    // themeGridView.setHorizontalSpacing(10);
    // themeGridView.setStretchMode(GridView.NO_STRETCH);
    // themeGridView.setNumColumns(GridView.AUTO_FIT);
    // themeGridView.setSelector(android.R.color.transparent);
    // themeGridView.setAdapter(tp);
    // themeGridView.setOnItemClickListener(new OnItemClickListener()
    // {
    //
    // @Override
    // public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    // {
    // GridView lv = (GridView)arg0;
    // @SuppressWarnings("unchecked")
    // HashMap<String, Integer> itemMap = (HashMap<String, Integer>)lv.getItemAtPosition(arg2);
    // int bgId = ((Integer)itemMap.get("background")).intValue();
    // mainLayout.setBackgroundResource(bgId);
    // GlobalState.getInstance().setBackGroundId(bgId);
    //
    // themeSetLinear_rl.setVisibility(View.GONE);
    //
    // }
    //
    // });
    // // ===================主题设置===========================================
    // }
    
    /**
     * dp转换成px
     * 
     * @param dpValue dp值
     * @return px像素值
     * @author wen_tao
     */
    private int dptopx(float dpValue)
    {
        final float scale = PersonSetView.this.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }
    
    /**
     * 
     * 初始化设置界面
     * 
     * @Description 初始化设置界面
     * 
     * @LastModifiedDate：2013-11-5
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initSetView()
    {
        themeClose = (RelativeLayout)findViewById(R.id.button_close);
        themeSetLinear_rl = (RelativeLayout)findViewById(R.id.theme_set_rl);
        backView = (ImageView)findViewById(R.id.userset);
        changePassRel = (RelativeLayout)findViewById(R.id.changePass_Rel);
        personalDetail = (RelativeLayout)findViewById(R.id.personal);
        changeThemeRel = (RelativeLayout)findViewById(R.id.selecttheme);
        autoBtn = (ImageButton)findViewById(R.id.autoLogin_Button);
        bindBtn = (ImageButton)findViewById(R.id.bind_Button);
        checkVerBtn = (Button)findViewById(R.id.check_button);
        clearBtn = (Button)findViewById(R.id.clear_button);
        
        backView.setOnClickListener(this);
        themeClose.setOnClickListener(this);
        // changeThemeRel.setOnClickListener(this);
        changePassRel.setOnClickListener(this);
        personalDetail.setOnClickListener(this);
        autoBtn.setOnClickListener(this);
        bindBtn.setOnClickListener(this);
        checkVerBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        
    }
    
    /**
     * 
     * 初始个人设置中的数据
     * 
     * @Description 初始个人设置中的数据
     * 
     * @LastModifiedDate：2013-11-5
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initSetData()
    {
        isAutoLogin = GlobalState.getInstance().getIsAutoLogin();
        if (isAutoLogin)
        {
            // autoBtn.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selector_login_saveyes));
            autoBtn.setBackgroundResource(R.drawable.switch_2);
        }
        else
        {
            // autoBtn.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selector_login_saveno));
            autoBtn.setBackgroundResource(R.drawable.switch_3);
        }
        isBind = GlobalState.getInstance().getIsBind();
        if (isBind)
        {
            // bindBtn.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selector_login_saveyes));
            bindBtn.setBackgroundResource(R.drawable.switch_2);
        }
        else
        {
            // bindBtn.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selector_login_saveno));
            bindBtn.setBackgroundResource(R.drawable.switch_3);
        }
    }
    
    /**
     * 
     * 修改密码对话框
     * 
     * @Description 修改密码对话框
     * 
     * @LastModifiedDate：2013-11-5
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void showPasswordDialog()
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final View passView = factory.inflate(R.layout.changepass_dialog, null);
        oldPassword = (EditText)passView.findViewById(R.id.oldEdit);
        newPassword = (EditText)passView.findViewById(R.id.newEdit);
        confirmPassword = (EditText)passView.findViewById(R.id.repeatEdit);
        oldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmBtn = (Button)passView.findViewById(R.id.right_btn);
        cancelBtn = (Button)passView.findViewById(R.id.left_btn);
        changePassDialog = new Dialog(PersonSetView.this, R.style.DialogStyle);
        changePassDialog.setContentView(passView);
        changePassDialog.setCancelable(true);
        
        confirmBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                checkPassword();
            }
            
        });
        cancelBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                changePassDialog.dismiss();
            }
            
        });
        changePassDialog.show();// 显示对话框
    }
    
    private void showPersonalDialog()
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final View personalView = factory.inflate(R.layout.personal_details, null);
        // oldPassword = (EditText)passView.findViewById(R.id.oldEdit);
        // newPassword = (EditText)passView.findViewById(R.id.newEdit);
        // confirmPassword = (EditText)passView.findViewById(R.id.repeatEdit);
        // oldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        nameTextView = (TextView)personalView.findViewById(R.id.name);
        personal_confirmBtn = (Button)personalView.findViewById(R.id.confirm_btn);
        modifyBtn = (Button)personalView.findViewById(R.id.modify_btn);
        telephone = (EditText)personalView.findViewById(R.id.phone_Edit);
        phone_text = (TextView)personalView.findViewById(R.id.phone_Text);
        
        final EditText add_edit = (EditText)personalView.findViewById(R.id.add_Edit);
        // 公司
        final TextView add_text = (TextView)personalView.findViewById(R.id.add_Text);
        email_edit = (EditText)personalView.findViewById(R.id.email_Edit);
        final TextView email_text = (TextView)personalView.findViewById(R.id.email_Text);
        
        // he
        /** 部门名称 **/
        departmentname_edit = (EditText)personalView.findViewById(R.id.departmentname_Edit);
        
        departmentname_text = (TextView)personalView.findViewById(R.id.departmentname_text);
        
        /** 职务 **/
        duty_edit = (EditText)personalView.findViewById(R.id.duty_Edit);
        
        duty_text = (TextView)personalView.findViewById(R.id.duty_text);
        
        /** 备用手机 **/
        baktel_edit = (EditText)personalView.findViewById(R.id.baktel_Edit);
        
        baktel_text = (TextView)personalView.findViewById(R.id.baktel_text);
        
        /** 办公电话 **/
        officetel_edit = (EditText)personalView.findViewById(R.id.officetel_Edit);
        
        officetel_text = (TextView)personalView.findViewById(R.id.officetel_text);
        
        /** 备用电话 **/
        bakofficetel_edit = (EditText)personalView.findViewById(R.id.bakofficetel_Edit);
        
        bakofficetel_text = (TextView)personalView.findViewById(R.id.bakofficetel_text);
        
        /** 个人邮箱 **/
        peremail_edit = (EditText)personalView.findViewById(R.id.peremail_Edit);
        
        peremail_text = (TextView)personalView.findViewById(R.id.peremail_text);
        
        /** 云之家微博开通标识 **/
        markweibo_edit = (EditText)personalView.findViewById(R.id.markweibo_edit);
        
        markweibo_text = (TextView)personalView.findViewById(R.id.markweibo_text);
        iv_close = (ImageView)personalView.findViewById(R.id.btn_close);
        // 结束
        // final CustomDialog dialog = CustomDialog.newInstance(null, iv_close.toString());
        iv_close.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // dialog.dismiss();
                // finish();
                personalDetailDialog.dismiss();
            }
        });
        telephone.setInputType(InputType.TYPE_CLASS_NUMBER);
        nameTextView.setText(GlobalState.getInstance().getUserName() + "[" + GlobalState.getInstance().getOpenId()
            + "]");
        add_text.setText(GlobalState.getInstance().getCompanyName());
        phone_text.setText(GlobalState.getInstance().getTelNum());
        email_text.setText(GlobalState.getInstance().getEmail());
        
        telephone.setText(GlobalState.getInstance().getTelNum());
        email_edit.setText(GlobalState.getInstance().getEmail());
        
        // he
        
        departmentname_text.setText(GlobalState.getInstance().getDepartmentname());
        duty_text.setText(GlobalState.getInstance().getDuty());
        baktel_text.setText(GlobalState.getInstance().getBaktel());
        officetel_text.setText(GlobalState.getInstance().getOfficetel());
        bakofficetel_text.setText(GlobalState.getInstance().getBakofficetel());
        peremail_text.setText(GlobalState.getInstance().getPeremail());
        String open = "";
        int accept = Integer.valueOf(GlobalState.getInstance().getMarkweibo());
        if (accept == 0)
        {
            open = "未开通";
        }
        else
        {
            open = "已开通";
        }
        markweibo_text.setText(open);
        
        baktel_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        baktel_edit.setText(GlobalState.getInstance().getBaktel());
        officetel_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        officetel_edit.setText(GlobalState.getInstance().getOfficetel());
        bakofficetel_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        bakofficetel_edit.setText(GlobalState.getInstance().getBakofficetel());
        
        departmentname_edit.setText(GlobalState.getInstance().getDepartmentname());
        duty_edit.setText(GlobalState.getInstance().getDuty());
        peremail_edit.setText(GlobalState.getInstance().getPeremail());
        String opens = "";
        int accepts = Integer.valueOf(GlobalState.getInstance().getMarkweibo());
        if (accepts == 0)
        {
            opens = "未开通";
        }
        else
        {
            opens = "已开通";
        }
        markweibo_edit.setText(opens);
        
        personalDetailDialog = new Dialog(PersonSetView.this, R.style.DialogStyle);
        personalDetailDialog.setContentView(personalView);
        personalDetailDialog.setCancelable(true);
        personal_confirmBtn.setOnClickListener(new OnClickListener()
        {
            //
            @Override
            public void onClick(View v)
            {
                // he
                if ("".equals(telephone.getText()) || "".equals(email_edit.getText())
                    || "".equals(departmentname_edit.getText()) || "".equals(duty_edit.getText())
                    || "".equals(baktel_edit.getText()) || "".equals(officetel_edit.getText())
                    || "".equals(bakofficetel_edit.getText()) || "".equals(peremail_edit.getText())
                    || "".equals(markweibo_edit.getText()))
                {
                    Toast.makeText(PersonSetView.this, "修改信息为空", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject body = new JSONObject();
                    try
                    {
                        
                        body.put("tel", telephone.getText());
                        // body.put("email", email_edit.getText());
                        // he
                        // 　　　　　" loginName"："登录名"，
                        body.put("loginName", GlobalState.getInstance().getOpenId());
                        // body.put("departmentname", departmentname_edit.getText());
                        body.put("duty", duty_edit.getText());
                        body.put("bakTel", baktel_edit.getText());
                        body.put("officeTel", officetel_edit.getText());
                        body.put("bakOfficeTel", bakofficetel_edit.getText());
                        body.put("perEmail", peremail_edit.getText());
                        // body.put("markweibo", markweibo_edit.getText());
                        
                        body.put("token", GlobalState.getInstance().getToken());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    waitDialog = WaitDialog.newInstance();
                    waitDialog.show(getSupportFragmentManager(), "waitDialog");
                    NetRequestController.SendUpdatePersonal(mHandler, RequestTypeConstants.UPDATEPERSONAL_REQUEST, body);
                }
            }
            
        });
        modifyBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // personalDetailDialog.dismiss();
                // EditText telephone = (EditText)findViewById(R.id.phone_Edit);
                // TextView phone_text = (TextView)findViewById(R.id.phone_text);
                telephone.setVisibility(View.VISIBLE);
                phone_text.setVisibility(View.INVISIBLE);
                // add_edit.setVisibility(View.VISIBLE);
                // add_text.setVisibility(View.INVISIBLE);
                // email_edit.setVisibility(View.VISIBLE);
                // email_text.setVisibility(View.INVISIBLE);
                
                // he
                // departmentname_edit.setVisibility(View.VISIBLE);
                // departmentname_text.setVisibility(View.INVISIBLE);
                duty_edit.setVisibility(View.VISIBLE);
                duty_text.setVisibility(View.INVISIBLE);
                baktel_edit.setVisibility(View.VISIBLE);
                baktel_text.setVisibility(View.INVISIBLE);
                officetel_edit.setVisibility(View.VISIBLE);
                officetel_text.setVisibility(View.INVISIBLE);
                bakofficetel_edit.setVisibility(View.VISIBLE);
                bakofficetel_text.setVisibility(View.INVISIBLE);
                peremail_edit.setVisibility(View.VISIBLE);
                peremail_text.setVisibility(View.INVISIBLE);
                // markweibo_edit.setVisibility(View.VISIBLE);
                // markweibo_text.setVisibility(View.INVISIBLE);
            }
            
        });
        personalDetailDialog.show();// 显示对话框
    }
    
    /**
     * 
     * 检查输入的旧密码和新密码是否符合要求
     * 
     * @Description 检查输入的旧密码和新密码是否符合要求
     * 
     * @LastModifiedDate：2013-11-5
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void checkPassword()
    {
        String oldP = oldPassword.getText().toString().trim();
        String newP = newPassword.getText().toString().trim();
        String repeatP = confirmPassword.getText().toString().trim();
        if ("".equals(oldP))
        {
            final CustomDialog dialog =
                CustomDialog.newInstance(getResources().getString(R.string.Change_oldNull),
                    getResources().getString(R.string.Login_Confirm));
            dialog.show(getSupportFragmentManager(), "ChangePassDialog");
            dialog.setMidListener(new CustomDialogListener()
            {
                
                @Override
                public void Onclick()
                {
                    dialog.dismiss();
                }
            });
        }
        else if ("".equals(newP) || newP.length() < 6)
        {
            final CustomDialog dialog =
                CustomDialog.newInstance(getResources().getString(R.string.Change_newNull),
                    getResources().getString(R.string.Login_Confirm));
            dialog.show(getSupportFragmentManager(), "ChangePassDialog");
            dialog.setMidListener(new CustomDialogListener()
            {
                
                @Override
                public void Onclick()
                {
                    dialog.dismiss();
                }
            });
        }
        else if ("".equals(repeatP) || repeatP.length() < 6)
        {
            final CustomDialog dialog =
                CustomDialog.newInstance(getResources().getString(R.string.Change_repeatNull),
                    getResources().getString(R.string.Login_Confirm));
            dialog.show(getSupportFragmentManager(), "ChangePassDialog");
            dialog.setMidListener(new CustomDialogListener()
            {
                
                @Override
                public void Onclick()
                {
                    dialog.dismiss();
                }
            });
        }
        else if (oldP.trim().equals(newP.trim()))
        {
            final CustomDialog dialog =
                CustomDialog.newInstance("新密码和旧密码相同，请重新输入！", getResources().getString(R.string.Login_Confirm));
            dialog.show(getSupportFragmentManager(), "ChangePassDialog");
            dialog.setMidListener(new CustomDialogListener()
            {
                
                @Override
                public void Onclick()
                {
                    dialog.dismiss();
                }
            });
        }
        else if (!newP.equals(repeatP.trim()))
        {
            final CustomDialog dialog =
                CustomDialog.newInstance(getResources().getString(R.string.Change_different),
                    getResources().getString(R.string.Login_Confirm));
            dialog.show(getSupportFragmentManager(), "ChangePassDialog");
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
            JSONObject body = new JSONObject();
            JSONObject bizdata = new JSONObject();
            try
            {
                // body.put("password", oldP);// AesUtil.encrypt(oldP, NetRequestController.MOA_deviceKey)
                // body.put("newpassword", newP);
                
                body.put("newpassword", newP);
                body.put("password", oldP);
                body.put("token", GlobalState.getInstance().getToken());
                // bizdata.put("token", GlobalState.getInstance().getToken());
                // bizdata.put("newpassword", newP);
                // bizdata.put("password ", oldP);
                
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            waitDialog = WaitDialog.newInstance();
            waitDialog.show(getSupportFragmentManager(), "waitDialog");
            NetRequestController.SendChangePassword(mHandler, RequestTypeConstants.CHANGEPASSWORD_REQUEST, body);
        }
    }
    
    /**
     * 
     * 显示绑定对话框
     * 
     * @Description 显示绑定对话框
     * 
     * @LastModifiedDate：2013-11-6
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void showBindDialog()
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final View bindView = factory.inflate(R.layout.binddevice_dialog, null);
        bindUserNameTv = (TextView)bindView.findViewById(R.id.userName);
        bindUserNameTv.setText(GlobalState.getInstance().getUserName());
        deviceIdTv = (TextView)bindView.findViewById(R.id.device);
        deviceIdTv.setText(GlobalState.getInstance().getDeviceId());
        bindPassEt = (EditText)bindView.findViewById(R.id.bindEdit);
        bindPassEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        
        bindConfirm = (Button)bindView.findViewById(R.id.right_btn);
        bindCancel = (Button)bindView.findViewById(R.id.left_btn);
        if (isBind)
        {
            bindConfirm.setText(PersonSetView.this.getResources().getString(R.string.UNBind_confirm));
            bindCancel.setText(PersonSetView.this.getResources().getString(R.string.UNBind_cancel));
        }
        else
        {
            bindConfirm.setText(PersonSetView.this.getResources().getString(R.string.Bind_confirm));
            bindCancel.setText(PersonSetView.this.getResources().getString(R.string.Bind_cancel));
        }
        bindDeviceDialog = new Dialog(PersonSetView.this, R.style.DialogStyle);
        bindDeviceDialog.setContentView(bindView);
        bindDeviceDialog.setCancelable(true);
        
        bindConfirm.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                checkBind();
            }
            
        });
        bindCancel.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                bindDeviceDialog.dismiss();
            }
            
        });
        bindDeviceDialog.show();// 显示对话框
    }
    
    /**
     * 
     * 设备绑定校验
     * 
     * @Description 设备绑定校验
     * 
     * @LastModifiedDate：2013-11-6
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void checkBind()
    {
        String bindPass = bindPassEt.getText().toString().trim();
        if ("".equals(bindPass))
        {
            final CustomDialog dialog =
                CustomDialog.newInstance(getResources().getString(R.string.Bind_passNull),
                    getResources().getString(R.string.Login_Confirm));
            dialog.show(getSupportFragmentManager(), "ChangePassDialog");
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
            waitDialog = WaitDialog.newInstance();
            waitDialog.show(getSupportFragmentManager(), "waitDialog");
            
            JSONObject body = new JSONObject();
            try
            {
                body.put("password", bindPass);// AesUtil.encrypt(bindPass,
                                               // NetRequestController.MOA_deviceKey)
                if (isBind)
                {
                    body.put("flag", "0");
                }
                else
                {
                    body.put("flag", "1");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            
            NetRequestController.SendBindDevice(mHandler, RequestTypeConstants.BINDDEVICE_REQUEST, body);
        }
    }
    
    /**
     * 
     * 清理本地数据
     * 
     * @Description 清理本地数据
     * 
     * @LastModifiedDate：2013-11-7
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void showClearDialog()
    {
        // LayoutInflater factory = LayoutInflater.from(this);
        // final View clearView = factory.inflate(R.layout.cleardata_dialog, null, false);
        // clearDialog = new Dialog(PersonSetView.this, R.style.DialogStyle);
        // clearDialog.setContentView(clearView);
        // clearDialog.setCancelable(false);
        // waitLinear = (LinearLayout)clearView.findViewById(R.id.pre_linear);
        // okLinear = (LinearLayout)clearView.findViewById(R.id.next_linear);
        // waitImage = (ImageView)clearView.findViewById(R.id.waitdialog_img);
        // AnimationDrawable animationDrawable = (AnimationDrawable)waitImage.getBackground();
        // animationDrawable.start();
        // okButton = (Button)clearView.findViewById(R.id.okbutton);
        // okButton.setOnClickListener(new OnClickListener()
        // {
        //
        // @Override
        // public void onClick(View v)
        // {
        // clearDialog.dismiss();
        //
        // }
        // });
        // clearDialog.show();// 显示对话框
    }
    
    /**
     * 重载方法
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @author wen_tao
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        
    }
}
