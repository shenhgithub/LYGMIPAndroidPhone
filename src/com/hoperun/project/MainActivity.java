/*
 * File name:  MainActivity2.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  shen_feng
 * Last modified date:  2013-9-12
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.adpter.MenuAdapter;
import com.hoperun.manager.components.AnimRunnable;
import com.hoperun.manager.components.CustomDialog;
import com.hoperun.manager.components.CustomDialogListener;
import com.hoperun.manager.components.WaitDialog;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.exceptions.MIPException;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.sqlUtils.SQLCreator.DBHandler;
import com.hoperun.mip.sqlUtils.Table.TFUNSIONPOWER;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.utils.MessageSQLIdConstants;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityMetaData.GetIndexModuleInfo;
import com.hoperun.mipmanager.model.entityModule.Login.LoginModule;
import com.hoperun.mipmanager.model.entityModule.Login.ModuleNoReadModule;
import com.hoperun.mipmanager.model.entityModule.nettv.VideoListItem;
import com.hoperun.mipmanager.model.entityModule.offical.GWDocModule;
import com.hoperun.mipmanager.model.entityModule.schedule.ScheduleInfo;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.TestlFragment;
import com.hoperun.project.ui.Login.LoginActivity;
import com.hoperun.project.ui.Login.PersonSetView;
import com.hoperun.project.ui.Login.QianDaoActivity;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;
import com.hoperun.project.ui.baseui.baseInterface.IFragmentToMainActivityListen;
import com.hoperun.project.ui.baseui.fragment.PMIPBaseFragment;
import com.hoperun.project.ui.cityplan.CityPlanFragment;
import com.hoperun.project.ui.cityplan.CityPlanSecondFragment;
import com.hoperun.project.ui.fileLibrary.FileLibraryFragment;
import com.hoperun.project.ui.fileLibrary.FileLibrarySecondFragment;
import com.hoperun.project.ui.homeview.DaibanFragment;
import com.hoperun.project.ui.innerEmail.InnerEmailFragment;
import com.hoperun.project.ui.innerEmail.InnerEmailSendFragment;
import com.hoperun.project.ui.leaderPiShi.LeaderPiShiFragment;
import com.hoperun.project.ui.leaderSchedule.LeaderScheduleFragment;
import com.hoperun.project.ui.nettv.NettvFragment;
import com.hoperun.project.ui.nettv.NettvSecondFragment;
import com.hoperun.project.ui.offical.OfficialFragment;
import com.hoperun.project.ui.offical.OfficialSecondFragment;
import com.hoperun.project.ui.xwzx.XwzxFirstFragment;
import com.hoperun.project.ui.xwzx.XwzxSecondFragment;

/*
 *
 * 主页面。
 * 
 * 一级栏目，栏目
 * 
 * @Description<功能详细描述>
 * 
 * @author shen_feng
 * @Version [版本号, 2013-9-12]
 */
@SuppressLint("HandlerLeak")
public class MainActivity extends PMIPBaseActivity implements OnItemClickListener, OnClickListener,
    IFragmentToMainActivityListen, OnTouchListener

{
    // ==================主题===========================
    /**
     * 查询回来的list数据
     */
    private MetaResponseBody             responseBuzBody;
    
    // /** 显示是否在线 **/
    // private TextView onLineTv;
    //
    // /** 显示是否绑定 **/
    // private ImageView bindTv;
    
    /** 二级布局，三级布局滑动标记 **/
    public static final int              HANDLER_MESSAGE_UPDATE_POSITION = 101;
    
    public static final int              HANDLER_MESSAGE_CHANGELINE      = 102;
    
    /**
     * 日期
     */
    private TextView                     dateAndWeekTv;
    
    /**
     * 几号
     */
    private TextView                     dayOfMonthTv;
    
    /**
     * 版本
     */
    private TextView                     versionTv;
    
    /**
     * 主栏目布局
     */
    private ListView                     mLvMenu;
    
    /**
     * 用户名
     */
    private TextView                     fragment_title;
    
    /**
     * 用户设置
     */
    private ImageView                    userSet;
    
    /**
     * 左侧功能菜单栏
     */
    private RelativeLayout               mFirstLayout;
    
    /**
     * 头像
     */
    private ImageView                    mImFace;
    
    /**
     * 二级布局
     */
    private FrameLayout                  mSecondLevelLayout;
    
    /**
     * 三级布局
     */
    private FrameLayout                  mThirdLevelLayout;
    
    /**
     * 二级布局位置
     */
    private ViewGroup.MarginLayoutParams mSecondLevelMarginLayoutParams;
    
    /**
     * 三级布局位置
     */
    private ViewGroup.MarginLayoutParams mThirdLevelMarginLayoutParams;
    
    /** 二级布局动画 **/
    private Animation                    mSecondRightInAnim;
    
    /** 三级布局动画 **/
    private Animation                    mThreeRightInAnim;
    
    /** 滑动效果 **/
    private View.OnTouchListener         mViewLandscapeSlideListener;
    
    /** AnimRunnable **/
    private AnimRunnable                 mAnimationRunnable;
    
    private int                          mDip;
    
    /**
     * 屏幕宽度
     */
    private int                          mScreenWidth;
    
    /**
     * 三级布局左边距
     */
    private int                          mThirdToLeft;
    
    /**
     * 滑动三级菜单时，右滑出屏幕最小左边距
     */
    private int                          mThirdDisappearToLeft;
    
    /**
     * 二级布局左边距
     */
    private int                          mSecondToLeft;
    
    /**
     * 出现三级菜单后，二级布局左边距
     */
    private int                          mSecondToLeftAfterThird;
    
    /**
     * 是否第二层布局滑动
     */
    private boolean                      isSecondLayoutonTouch           = true;
    
    /**
     * 二级布局，三级布局是否在滑动，在滑动则屏蔽点击事件
     */
    private boolean                      isTouch;
    
    /**
     * 注销按钮
     */
    private ImageView                    mBtnLogout;
    
    /**
     * 主题和设置的布局
     */
    private LinearLayout                 themeLayout, setLayout;
    
    /**
     * 一级布局列表
     */
    private MenuAdapter                  mMenuAdapter;
    
    /**
     * fragment管理器
     */
    private FragmentManager              mFragmentManager;
    
    /**
     * fragment事务管理
     */
    private FragmentTransaction          fragmentTransaction;
    
    /**
     * 用户名称
     */
    private String                       userName                        = "用户名称";
    
    /** 一级列表数据 **/
    private ArrayList<LoginModule>       modules;
    
    /** 主页模块功能列表 **/
    private ArrayList<LoginModule>       indexModules;
    
    /** 用户id **/
    private String                       openId;
    
    /** 对话框 **/
    private CustomDialog                 dialog;
    
    /** 选择哪个功能 **/
    private String                       selectFuncCode                  = "";
    
    /**
     * 点击功能列表中某功能的功能ID；
     */
    private String                       mFuncId                         = "";
    
    /**
     * 点击功能列表中某功能的功能名称；
     */
    private String                       mFunName                        = "";
    
    /**
     * 点击功能列表中某功能的功能的根路径
     */
    private String                       mParentPath                     = "";
    
    /**
     * 点击功能列表中某共嫩的功能的搜索关键字
     */
    private String                       mSearchKeywodrs                 = "";
    
    /**
     * 待办事项子集的type， “待办事项”、“最新通知”，“最新刊物”,"最新批示"
     */
    private String                       mHomeViewFunType                = "";
    
    /**
     * 待办事项的type,请求列表时的type
     */
    private String                       mType                           = "";
    
    /** 公文流转，点击一级页面上选项，弹出 二级选项时的参数 **/
    // private String officalfunccode = "";
    private GWDocModule                  mModule                         = null;
    
    /** 公文流转，点击一级页面上选项，弹出 二级选项时的参数，文件路径 **/
    private String                       path2;
    
    /** 日程安排，点击一级页面上选项，弹出 二级选项时的参数 **/
    private ScheduleInfo                 mInfo                           = null;
    
    /** 日程安排，点击一级页面上选项，弹出 二级选项时的参数，日期 **/
    private String                       mDate                           = "";
    
    /** 内部邮件一级菜单fragment **/
    private InnerEmailFragment           mInnerEmailFragment;
    
    /** 内部邮件二级菜单fragment **/
    private InnerEmailSendFragment       mInnerEmailSecondFragment;
    
    /**
     * onTouchEvent触发时上一次的x位置
     */
    private int                          originalX;
    
    /**
     * onTouchEvent触发时当前的x位置
     */
    private int                          currentX;
    
    /**
     * onTouchEvent触发时当前的x位置与上一次x轴位置差值
     */
    private int                          ditanceX;
    
    /**
     * onTouchEvent 第一次Downy位置
     */
    private int                          originalY;
    
    /**
     * onTouchEvent触发时当前的y位置
     */
    private int                          currentY;
    
    /**
     * onTouchEvent触发时当前的y位置与第一次y轴位置差值
     */
    private int                          ditanceY;
    
    private PMIPBaseFragment             mTopFragment;
    
    private WaitDialog                   waitDialog;
    
    private boolean                      isFragmentCanMove               = false;
    
    /** 新闻中心一级菜单 **/
    private XwzxFirstFragment            mXwzxFirstFragment;
    
    /** 新闻中心二级菜单 **/
    private XwzxSecondFragment           mXwzxSecondFragment;
    
    private OfficialFragment             mOfficialFirstFragment;
    
    private OfficialSecondFragment       mOfficialSecondFragment;
    
    /** 文档库一级菜单fragment **/
    private FileLibraryFragment          mFileLibraryFragment;
    
    /** 文档库二级级菜单fragment **/
    private FileLibrarySecondFragment    mFileLibrarySecondFragment;
    
    private LeaderPiShiFragment          mLeaderPiShiFragment;
    
    private TestlFragment                mTestlFragment;
    
    private LeaderScheduleFragment       leaderScheduleFragment;
    
    /**
     * 网络电视一级菜单fragment
     */
    private NettvFragment                nettvFirstFragment;
    
    /**
     * 网络电视二级菜单fragment（时间轴页面）
     */
    private NettvSecondFragment          nettvSecondFragment;
    
    /**
     * 城市规划一级菜单fragment
     */
    private CityPlanFragment             cityPlanFirstFragment;
    
    /**
     * 城市规划二级菜单fragment（时间轴页面）
     */
    private CityPlanSecondFragment       cityPlanSecondFragment;
    
    private RelativeLayout               mDaibanCountRL;
    
    private TextView                     mDaibanCountTV;
    
    private NetTask                      mSendGetUnreadCountTask         = null;
    
    private RelativeLayout               mLastesttipsdaiban;
    
    private LoginModule                  mDaibanModule                   = null;
    
    private DaibanFragment               mDaibanFragment                 = null;
    
    /**
     * 签到布局
     */
    private RelativeLayout               qianDaoLayout;
    
    private Handler                      mMainHandler                    = new Handler()
                                                                         {
                                                                             @Override
                                                                             public void handleMessage(Message msg)
                                                                             {
                                                                                 if (isActivityDestroyed)
                                                                                 {
                                                                                     LogUtil.i("",
                                                                                         "This Activity is destroyed!");
                                                                                     return;
                                                                                 }
                                                                                 switch (msg.what)
                                                                                 {
                                                                                     case HANDLER_MESSAGE_UPDATE_POSITION:
                                                                                         
                                                                                         int type = msg.arg1;
                                                                                         
                                                                                         if (type == AnimRunnable.SECOND_FRAGMENT)
                                                                                         {
                                                                                             // 二级栏目滑動
                                                                                             mSecondLevelLayout.setLayoutParams(mSecondLevelMarginLayoutParams);
                                                                                             mSecondLevelLayout.requestLayout();
                                                                                         }
                                                                                         else if (type == AnimRunnable.THIRD_FRAGMENT)
                                                                                         {
                                                                                             // 三级级栏目滑動
                                                                                             mThirdLevelLayout.setLayoutParams(mThirdLevelMarginLayoutParams);
                                                                                             mThirdLevelLayout.requestLayout();
                                                                                         }
                                                                                         
                                                                                         break;
                                                                                     case HANDLER_MESSAGE_CHANGELINE:
                                                                                         
                                                                                         boolean flag =
                                                                                             (Boolean)msg.obj;
                                                                                         
                                                                                         if (waitDialog != null)
                                                                                         {
                                                                                             waitDialog.dismiss();
                                                                                         }
                                                                                         if (flag)
                                                                                         {
                                                                                             GlobalState.getInstance()
                                                                                                 .setOfflineLogin(false);
                                                                                             // onLineTv.setText(getResources().getString(R.string.main_online));
                                                                                         }
                                                                                         else
                                                                                         {
                                                                                             Toast.makeText(MainActivity.this,
                                                                                                 "无法切换为在线！",
                                                                                                 Toast.LENGTH_SHORT)
                                                                                                 .show();
                                                                                         }
                                                                                         break;
                                                                                     
                                                                                     default:
                                                                                         break;
                                                                                 }
                                                                             }
                                                                         };
    
    /**
     * 二级布局被关闭
     * 
     * @author ren_qiujing
     */
    @Override
    public void onSecondFragmentClose()
    {
        // TODO Auto-generated method stub
        closeTheSecondLevelLayout();
    }
    
    /**
     * 
     * 网络返回处理方法
     * 
     * @param requestType 请求id
     * @param objHeader objHeader
     * @param objBody objBody
     * @param error 是否返回成功
     * @param errorCode 错误码
     * @author wang_ling
     */
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        // TODO Auto-generated method stub
        
        switch (requestType)
        {// 获取模块未读数
            case RequestTypeConstants.MODULENOREAD_REQUEST:
                mSendGetUnreadCountTask = null;
                if (error)
                {
                    List<ModuleNoReadModule> listData = new ArrayList<ModuleNoReadModule>();
                    
                    if (objBody != null)
                    {
                        responseBuzBody = (MetaResponseBody)objBody;
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        for (int i = 0; i < ret.size(); i++)
                        {
                            ModuleNoReadModule module = new ModuleNoReadModule();
                            module.setFuncId((String)ret.get(i).get("funcid"));
                            module.setCount((String)ret.get(i).get("funccode"));
                            module.setCount((String)ret.get(i).get("count"));
                            listData.add(module);
                        }
                    }
                    System.out.println("--->>refresh ui--" + listData.size());
                    // ----refresh ui
                    // ConstState.DAIBANUNREADCOUNT = (Integer.valueOf(ConstState.DAIBANUNREADCOUNT) - 1) + "";
                    // ConstState.GWLZUNREADCOUNT = (Integer.valueOf(ConstState.GWLZUNREADCOUNT) - 1) + "";
                    ConstState.DAIBANUNREADCOUNT = "";
                    ConstState.GWLZUNREADCOUNT = "";
                    
                    updateUnreadCount();
                    updateDaibanUnreadCount();
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 重载方法
     * 
     * @param savedInstanceState savedInstanceState
     * @author shen_feng
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.main_layout);
        
        initScreenAttribute();
        initView();
        initData();
        
    }
    
    /**
     * 重载方法
     * 
     * @author shen_feng
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        
        // 添加头像照片 add by wen_tao begin
        // Bitmap bitmap = GetLocalPic.getHeader();
        // if (null == bitmap)
        // {
        // mImFace.setBackgroundResource(R.drawable.photo_default);
        // }
        // else
        // {
        // mImFace.setImageBitmap(bitmap);
        // }
        //
        // if (mMenuAdapter != null && selectFuncCode.equals(ConstState.DZDT))
        // {
        // mMenuAdapter.setmSelectedPosition(-1);
        // mMenuAdapter.notifyDataSetChanged();
        // }
        // 添加头像照片 add by wen_tao end
        
    }
    
    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        GlobalState.getInstance().clearValues();
        super.onDestroy();
    }
    
    /**
     * 
     * 初始化屏幕参数
     * 
     * @Description 初始化屏幕参数
     * 
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initScreenAttribute()
    {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        GlobalState.getInstance().setmScreen_With(dm.widthPixels);
        GlobalState.getInstance().setmScreen_Height(dm.heightPixels);
        GlobalState.getInstance().setmDensityDpi(dm.densityDpi);
        
        mDip = dm.densityDpi;
        
        mScreenWidth = dm.widthPixels;
        
        mSecondToLeft = mScreenWidth;
        
        mThirdToLeft = 0;
        
        mThirdDisappearToLeft = mScreenWidth * 3 / 4;
        
        // 三级栏目存在时，二级栏目的左边距
        mSecondToLeftAfterThird = 0;
        
    }
    
    /**
     * 
     * 初始化界面
     * 
     * @Description 初始化界面
     * 
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initView()
    {
        openId = GlobalState.getInstance().getOpenId();
        // mainLayout = (RelativeLayout)findViewById(R.id.main_layout);
        //
        // onLineTv = (TextView)findViewById(R.id.tv_line);
        // bindTv = (ImageView)findViewById(R.id.tv_bind);
        
        mLastesttipsdaiban = (RelativeLayout)findViewById(R.id.lastestdaiban);
        
        fragment_title = (TextView)findViewById(R.id.fragment_title);
        fragment_title.setText(GlobalState.getInstance().getUserName());
        fragment_title.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, QianDaoActivity.class);
                startActivity(intent);
                
            }
        });
        mLvMenu = (ListView)findViewById(R.id.lv_menu);
        userSet = (ImageView)findViewById(R.id.userset);
        // mFirstLayout = (RelativeLayout)findViewById(R.id.main_first_layout);
        // mImFace = (ImageView)findViewById(R.id.face);
        mSecondLevelLayout = (FrameLayout)findViewById(R.id.main_second_layout);
        mThirdLevelLayout = (FrameLayout)findViewById(R.id.main_third_layout);
        mBtnLogout = (ImageView)findViewById(R.id.btn_logout);
        // museHistory = (Button)findViewById(R.id.usehistory);
        // nameTV = (TextView)findViewById(R.id.name);
        // dateAndWeekTv = (TextView)findViewById(R.id.dateandweek);
        // dayOfMonthTv = (TextView)findViewById(R.id.dayofmonth);
        // versionTv = (TextView)findViewById(R.id.mainVersion);
        //
        // themeLayout = (LinearLayout)findViewById(R.id.private_set);
        // setLayout = (LinearLayout)findViewById(R.id.system_set);
        
        mDaibanCountRL = (RelativeLayout)findViewById(R.id.numbertip);
        mDaibanCountTV = (TextView)findViewById(R.id.numbertotal);
        mDaibanCountRL.setVisibility(View.INVISIBLE);
        
        mSecondLevelMarginLayoutParams = (MarginLayoutParams)mSecondLevelLayout.getLayoutParams();
        mThirdLevelMarginLayoutParams = (MarginLayoutParams)mThirdLevelLayout.getLayoutParams();
        //
        mViewLandscapeSlideListener = this;
        mSecondLevelLayout.setOnTouchListener(mViewLandscapeSlideListener);
        
        mLastesttipsdaiban.setOnClickListener(this);
        
        mLvMenu.setOnItemClickListener(this);
        userSet.setOnClickListener(this);
        //
        mBtnLogout.setOnClickListener(this);
        // museHistory.setOnClickListener(this);
        // themeLayout.setOnClickListener(this);
        // setLayout.setOnClickListener(this);
        //
        // mImFace.setOnClickListener(this);
        fragment_title.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent0 = new Intent(MainActivity.this, QianDaoActivity.class);
                startActivity(intent0);
            }
        });
        qianDaoLayout = (RelativeLayout)findViewById(R.id.btn_qiandao);
        qianDaoLayout.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, QianDaoActivity.class);
                startActivity(intent);
            }
        });
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if (!isFragmentCanMove)
        {
            return super.onTouchEvent(event);
        }
        
        if (mThirdLevelLayout.getVisibility() == View.GONE || mThirdLevelMarginLayoutParams.leftMargin >= mScreenWidth)
        {
            isSecondLayoutonTouch = true;
        }
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                // LogUtil.i("hoperun", "onTouch  ACTION_DOWN");
                isTouch = false;
                originalX = (int)event.getRawX();
                
                originalY = (int)event.getRawY();
                
                return false;
                
            case MotionEvent.ACTION_MOVE:
                // LogUtil.i("hoperun", "onTouch  ACTION_MOVE");
                
                currentX = (int)event.getRawX();
                currentY = (int)event.getRawY();
                
                ditanceX = currentX - originalX;
                ditanceY = currentY - originalY;
                
                if ((ditanceX > 5 || ditanceX < -5) && ditanceY < 5 && ditanceY > -5)
                {
                    // 如果距离大时，且Y距离小。则认为listview在滑动
                    isTouch = true;
                }
                originalX = currentX;
                
                if (isTouch)
                {
                    if (isSecondLayoutonTouch)
                    {
                        if (mSecondLevelMarginLayoutParams.leftMargin == mSecondToLeft)
                        {
                            if (ditanceX < 0)
                            {
                                mSecondLevelMarginLayoutParams.leftMargin += ditanceX;
                                mSecondLevelMarginLayoutParams.rightMargin = -mSecondLevelMarginLayoutParams.leftMargin;
                                mSecondLevelLayout.setLayoutParams(mSecondLevelMarginLayoutParams);
                                mSecondLevelLayout.requestLayout();
                            }
                        }
                        else if (mSecondLevelMarginLayoutParams.leftMargin == 0)
                        {
                            if (ditanceX > 0)
                            {
                                mSecondLevelMarginLayoutParams.leftMargin += ditanceX;
                                mSecondLevelMarginLayoutParams.rightMargin = -mSecondLevelMarginLayoutParams.leftMargin;
                                mSecondLevelLayout.setLayoutParams(mSecondLevelMarginLayoutParams);
                                mSecondLevelLayout.requestLayout();
                            }
                        }
                        else
                        {
                            if (mSecondLevelMarginLayoutParams.leftMargin > 0
                                && mSecondLevelMarginLayoutParams.leftMargin < mSecondToLeft)
                            {
                                int leftMargin = mSecondLevelMarginLayoutParams.leftMargin + ditanceX;
                                if (leftMargin < 0)
                                {
                                    mSecondLevelMarginLayoutParams.leftMargin = 0;
                                }
                                else if (leftMargin > mSecondToLeft)
                                {
                                    mSecondLevelMarginLayoutParams.leftMargin = mSecondToLeft;
                                }
                                else
                                {
                                    mSecondLevelMarginLayoutParams.leftMargin += ditanceX;
                                    mSecondLevelMarginLayoutParams.rightMargin =
                                        -mSecondLevelMarginLayoutParams.leftMargin;
                                    mSecondLevelLayout.setLayoutParams(mSecondLevelMarginLayoutParams);
                                    mSecondLevelLayout.requestLayout();
                                }
                            }
                            
                        }
                    }
                    else
                    {
                        mThirdLevelMarginLayoutParams.leftMargin += ditanceX;
                        mThirdLevelMarginLayoutParams.rightMargin = -mThirdLevelMarginLayoutParams.leftMargin;
                        mThirdLevelLayout.setLayoutParams(mThirdLevelMarginLayoutParams);
                        mThirdLevelLayout.requestLayout();
                    }
                }
                return isTouch;
            case MotionEvent.ACTION_UP:
                // LogUtil.i("hoperun", "onTouch  ACTION_UP");
                if (isSecondLayoutonTouch)
                {
                    
                    if (mSecondLevelMarginLayoutParams.leftMargin != 0
                        && mSecondLevelMarginLayoutParams.leftMargin != mSecondToLeft)
                    {
                        if (mSecondLevelMarginLayoutParams.leftMargin > mScreenWidth / 2
                            && mSecondLevelMarginLayoutParams.leftMargin < mSecondToLeft)
                        {
                            mAnimationRunnable =
                                new AnimRunnable(2, mSecondToLeft, mSecondLevelMarginLayoutParams, mMainHandler,
                                    AnimRunnable.SECOND_FRAGMENT);
                            new Thread(mAnimationRunnable).start();
                            // 一级菜单滑出
                            // closeTheSecondLevelLayout();
                        }
                        else
                        {
                            mAnimationRunnable =
                                new AnimRunnable(2, 0, mSecondLevelMarginLayoutParams, mMainHandler,
                                    AnimRunnable.SECOND_FRAGMENT);
                            new Thread(mAnimationRunnable).start();
                        }
                    }
                    
                }
                else
                {
                    // 二级菜单滑出屏幕时做的处理
                    if (mThirdLevelMarginLayoutParams.leftMargin > mThirdDisappearToLeft)
                    {
                        mAnimationRunnable =
                            new AnimRunnable(2, mScreenWidth, mThirdLevelMarginLayoutParams, mMainHandler,
                                AnimRunnable.THIRD_FRAGMENT);
                        new Thread(mAnimationRunnable).start();
                        // 通知一级菜单，二级菜单滑出屏幕
                        closeTheThirdLevelLayout();
                        
                    }
                    else
                    {
                        mAnimationRunnable =
                            new AnimRunnable(2, mThirdToLeft, mThirdLevelMarginLayoutParams, mMainHandler,
                                AnimRunnable.THIRD_FRAGMENT);
                        new Thread(mAnimationRunnable).start();
                    }
                }
                
                return isTouch;
            default:
                return false;
        }
        
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description 初始化数据
     * 
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        if (GlobalState.getInstance().getOfflineLogin())
        {
            // onLineTv.setText(getResources().getString(R.string.main_offline));
        }
        else
        {
            // onLineTv.setText(getResources().getString(R.string.main_online));
        }
        
        if (GlobalState.getInstance().getIsBind())
        {
            // bindTv.setText(getResources().getString(R.string.main_bidn));
            // bindTv.setVisibility(View.VISIBLE);
        }
        else
        {
            // bindTv.setText(getResources().getString(R.string.main_notbind));
            // bindTv.setVisibility(View.INVISIBLE);
        }
        
        userName = GlobalState.getInstance().getOpenId();
        
        // versionTv.setText("v " + OsUtils.getVersionName(MainActivity.this));
        
        mFragmentManager = this.getSupportFragmentManager();
        
        mSecondRightInAnim = AnimationUtils.loadAnimation(this, R.anim.second_level_right_in2);
        
        mThreeRightInAnim = AnimationUtils.loadAnimation(this, R.anim.second_level_right_in2);
        
        modules = getModuleFromTable();
        MyComparator myComparator = new MyComparator();
        Collections.sort(modules, myComparator);
        
        mMenuAdapter = new MenuAdapter(this, modules);
        
        mLvMenu.setAdapter(mMenuAdapter);
        
        indexModules = getIndextModuleFromTalbe(openId);
        Collections.sort(indexModules, myComparator);
        if (indexModules != null)
        {
            for (int i = 0; i < indexModules.size(); i++)
            {
                LoginModule module = indexModules.get(i);
                if (module.getFuncCode().equals(ConstState.HOME_DB))
                {
                    mDaibanModule = module;
                    break;
                }
            }
        }
        
        mSecondRightInAnim.setAnimationListener(new AnimationListener()
        {
            
            @Override
            public void onAnimationStart(Animation animation)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onAnimationRepeat(Animation animation)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onAnimationEnd(Animation animation)
            {
                // TODO Auto-generated method stub
                
                if (mTopFragment != null)
                {
                    mTopFragment.animationOver();
                }
            }
        });
        
        mThreeRightInAnim.setAnimationListener(new AnimationListener()
        {
            
            @Override
            public void onAnimationStart(Animation animation)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onAnimationRepeat(Animation animation)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onAnimationEnd(Animation animation)
            {
                if (mTopFragment != null)
                {
                    mTopFragment.animationOver();
                }
            }
        });
        
        sendGetUnReadCount("", "");
    }
    
    /**
     * 
     * 从function表中获取模块数据
     * 
     * @Description 从function表中获取模块数据
     * 
     * @LastModifiedDate：2013-9-29
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private ArrayList<LoginModule> getModuleFromTable()
    {
        DBHandler hander = new DBHandler(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        hander.DBHandlerDBOpen(ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        ArrayList<LoginModule> tempModules = new ArrayList<LoginModule>();
        try
        {
            Cursor cursor =
                hander.query(MessageSQLIdConstants.DB_MESSAGE_FUNCTIONPOWER,
                    null,
                    TFUNSIONPOWER.OPENID + "=?",
                    new String[] {openId},
                    null);
            while (cursor.moveToNext())
            {
                LoginModule temp = new LoginModule();
                temp.setFuncName(cursor.getString(cursor.getColumnIndex(TFUNSIONPOWER.NAME)));
                temp.setCount(cursor.getString(cursor.getColumnIndex(TFUNSIONPOWER.COUNT)));
                temp.setFuncCode(cursor.getString(cursor.getColumnIndex(TFUNSIONPOWER.FUNTIONCODE)));
                
                // 暂时这样处理，过滤掉没有开发的模块
                if (!addThisModule(temp.getFuncCode()))
                {
                    continue;
                }
                
                temp.setFuncId(cursor.getString(cursor.getColumnIndex(TFUNSIONPOWER.FUNTIONID)));
                temp.setSort(cursor.getString(cursor.getColumnIndex(TFUNSIONPOWER.SORT)));
                temp.setFlowtype(cursor.getString(cursor.getColumnIndex(TFUNSIONPOWER.FLOWTYPE)));
                temp.setSearchkeywords(cursor.getString(cursor.getColumnIndex(TFUNSIONPOWER.SEARCHKEYWORDS)));
                temp.setSearchtype(cursor.getString(cursor.getColumnIndex(TFUNSIONPOWER.SEARCHTYPE)));
                
                setModuleBackground(temp); // 设置该功能项按下和未按下时的背景图
                tempModules.add(temp);
            }
            cursor.close();
        }
        catch (MIPException e)
        {
            new MIPException().printStackTrace();
        }
        
        hander.DBHandlerDBClose();
        return tempModules;
    }
    
    // 判断该模块是否已经开发
    private boolean addThisModule(String funCode)
    {
        // funCode.equals(ConstState.WDZM) || funCode.equals(ConstState.JRKB) || funCode.equals(ConstState.YWBL)
        // || funCode.equals(ConstState.TXL) || funCode.equals(ConstState.SPJK) || funCode.equals(ConstState.SBJGZX)
        // || funCode.equals(ConstState.NYJGZX) || funCode.equals(ConstState.GWCK) || funCode.equals(ConstState.CYGN)
        // ||
        
        if (funCode.equals(ConstState.XWZX))
        {
            return true;
        }
        return false;
    }
    
    /**
     * 
     * 从表里获取主页底部功能模块
     * 
     * @Description<功能详细描述>
     * 
     * @param userName 用户id
     * @return 主页模块功能列表
     * @LastModifiedDate：2013-11-27
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private ArrayList<LoginModule> getIndextModuleFromTalbe(String userName)
    {
        ArrayList<LoginModule> indextModuleList = new ArrayList<LoginModule>();
        GetIndexModuleInfo testObjectfromDB = new GetIndexModuleInfo(userName);
        String[] str = {userName};
        String where = GetIndexModuleInfo.l_user + " = ?";
        
        List<HashMap<String, Object>> queryret =
            (List<HashMap<String, Object>>)testObjectfromDB.query(null, where, str, null);
        if (queryret != null)
        {
            for (int i = 0; i < queryret.size(); i++)
            {
                LoginModule module = new LoginModule();
                module.setCount((String)queryret.get(i).get("count"));
                module.setFuncCode((String)queryret.get(i).get("funccode"));
                module.setFuncId((String)queryret.get(i).get("funcid"));
                module.setFuncName((String)queryret.get(i).get("funcname"));
                module.setSort((String)queryret.get(i).get("sort"));
                module.setFlowtype((String)queryret.get(i).get("flowtype"));
                module.setSearchkeywords((String)queryret.get(i).get("searchkeywords"));
                module.setSearchtype((String)queryret.get(i).get("searchtype"));
                module.setFuncdes((String)queryret.get(i).get("funcdes"));
                module.setType((String)queryret.get(i).get("type"));
                indextModuleList.add(module);
            }
        }
        
        return indextModuleList;
    }
    
    /**
     * 重载方法
     * 
     * @param arg0 parent相当于listview
     * @param arg1 item的view的句柄
     * @param arg2 适配器里的位置
     * @param arg3 适配器里的位置
     * @author shen_feng
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        
        if (selectFuncCode != null && selectFuncCode.equals((String)mMenuAdapter.getItem(arg2)))
        {
            return;
        }
        
        if (!isSecondLayoutonTouch)
        {
            releaseTheThirdFramgeLayout();
        }
        
        releaseTheSecondFramgeLayout();
        
        mMenuAdapter.setmSelectedPosition(arg2);
        
        mMenuAdapter.notifyDataSetChanged();
        
        selectFuncCode = (String)mMenuAdapter.getItem(arg2);
        
        mFuncId = (String)mMenuAdapter.getFuncId(arg2);
        
        mFunName = (String)mMenuAdapter.getFunName(arg2);
        
        mParentPath = userName + "/" + selectFuncCode;
        mSearchKeywodrs = (String)mMenuAdapter.getSearchKeyWords(arg2);
        
        openFirstFragment();
        
    }
    
    /**
     * 打开一级目录
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-2-24
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void openFirstFragment()
    {
        LogUtil.i("hoperun", "****mSecondToLeft=" + mSecondToLeft);
        
        if (!isSecondLayoutonTouch)
        {
            releaseTheThirdFramgeLayout();
        }
        
        releaseTheSecondFramgeLayout();
        isSecondLayoutonTouch = true;
        
        mThirdLevelLayout.setBackgroundResource(R.color.transparent);
        
        mSecondLevelLayout.removeAllViews();
        
        mThirdLevelMarginLayoutParams.leftMargin = mScreenWidth;
        mThirdLevelMarginLayoutParams.rightMargin = -mScreenWidth;
        mThirdLevelLayout.setLayoutParams(mThirdLevelMarginLayoutParams);
        
        setSecondLayoutWith(mSecondLevelMarginLayoutParams);
        
        mSecondLevelLayout.setLayoutParams(mSecondLevelMarginLayoutParams);
        mSecondLevelLayout.setVisibility(View.VISIBLE);
        mSecondLevelLayout.bringToFront();
        
        // mSecondRightInAnim结束之后会将fragment调用其中的animationOver方法，为了保证动画滑动的顺畅性,不要再动画
        // 过程中加载数据
        mSecondLevelLayout.startAnimation(mSecondRightInAnim);
        mSecondLevelLayout.setOnTouchListener(mViewLandscapeSlideListener);
        
        showTheSecondLayoutFragment();
        
    }
    
    /**
     * 
     * 显示三级栏目布局
     * 
     * @LastModifiedDate：2013-9-26
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    private void showTheThirdLevelLayout()
    {
        // mSecondToLeftAfterThird = mImFace.getWidth();
        mSecondToLeftAfterThird = 0;
        
        // mSecondLevelMarginLayoutParams.leftMargin = mSecondToLeftAfterThird;
        mSecondLevelLayout.setLayoutParams(mSecondLevelMarginLayoutParams);
        mSecondLevelLayout.setVisibility(View.VISIBLE);
        mSecondLevelLayout.setOnTouchListener(mViewLandscapeSlideListener);
        
        mAnimationRunnable =
            new AnimRunnable(-2, mSecondToLeftAfterThird, mSecondLevelMarginLayoutParams, mMainHandler,
                AnimRunnable.SECOND_FRAGMENT);
        new Thread(mAnimationRunnable).start();
        
        mThirdLevelMarginLayoutParams.leftMargin = mThirdToLeft;
        mThirdLevelMarginLayoutParams.width = mScreenWidth - mThirdToLeft;
        mThirdLevelLayout.setLayoutParams(mThirdLevelMarginLayoutParams);
        mThirdLevelLayout.setVisibility(View.VISIBLE);
        mThirdLevelLayout.bringToFront();
        // mThreeRightInAnim结束之后会将fragment中的animationOver方法，为了保证动画滑动的顺畅性
        mThirdLevelLayout.startAnimation(mThreeRightInAnim);
        mThirdLevelLayout.setOnTouchListener(mViewLandscapeSlideListener);
        
        showTheThirdLayoutFragment();
        
    }
    
    /**
     * 执行 弹出某功能时，只有在完全弹出该layout，当动画结束时再加载数据，防止卡顿现象的出现
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-29
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    @SuppressWarnings("static-access")
    private void showTheSecondLayoutFragment()
    {
        mTopFragment = null;
        
        // if (true)
        // {
        // mTestlFragment = null;
        // mTestlFragment = TestlFragment.newInstance();
        //
        // mFragmentManager = this.getFragmentManager();
        // fragmentTransaction = mFragmentManager.beginTransaction();
        //
        // fragmentTransaction.replace(R.id.main_second_layout, mTestlFragment);
        //
        // fragmentTransaction.commit();
        //
        // mTopFragment = mTestlFragment;
        //
        // return;
        // }
        
        // 根据id判断哪个模块
        // 公文流转
        if (ConstState.GWLZ.equals(selectFuncCode))
        {
            mOfficialFirstFragment = null;
            mOfficialFirstFragment = OfficialFragment.newInstance(mFuncId, mFunName, mParentPath);
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_second_layout, mOfficialFirstFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mOfficialFirstFragment;
        }
        // 日程安排
        else if (ConstState.RCAP.equals(selectFuncCode))
        {
            // mSchedulPlanFirstFragment = null;
            //
            // mSchedulPlanFirstFragment = SchedulePlanFragment.newInstance(mFuncId, mParentPath);
            //
            // mSchedulPlanFirstFragment.setTouchListener(mViewLandscapeSlideListener);
            //
            // fragmentTransaction = mFragmentManager.beginTransaction();
            //
            // fragmentTransaction.replace(R.id.main_second_layout, mSchedulPlanFirstFragment);
            //
            // fragmentTransaction.commit();
            //
            // mTopFragment = mSchedulPlanFirstFragment;
        }
        // 领导日程
        else if (ConstState.LDRC.equals(selectFuncCode))
        {
            // mLeaderScheduleFragment = null;
            // mLeaderScheduleFragment =
            // LeaderScheduleFragment.newInstance(mFuncId, mFunName, mParentPath, mSearchKeywodrs);
            //
            // fragmentTransaction = mFragmentManager.beginTransaction();
            //
            // fragmentTransaction.replace(R.id.main_second_layout, mLeaderScheduleFragment);
            //
            // fragmentTransaction.commit();
            //
            // mTopFragment = mLeaderScheduleFragment;
            
            leaderScheduleFragment = null;
            leaderScheduleFragment =
                LeaderScheduleFragment.newInstance(mFuncId, mFunName, mParentPath, mSearchKeywodrs);
            // TestlFragment.newInstance();
            
            mFragmentManager = this.getSupportFragmentManager();
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_second_layout, leaderScheduleFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = leaderScheduleFragment;
        }
        // 文档库
        else if (ConstState.WDK.equals(selectFuncCode))
        {
            mFileLibraryFragment = null;
            mFileLibraryFragment = FileLibraryFragment.newInstance(mFuncId, mFunName, mParentPath);
            mFragmentManager = this.getSupportFragmentManager();
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_second_layout, mFileLibraryFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mFileLibraryFragment;
            
            // mTestlFragment = null;
            // mTestlFragment = TestlFragment.newInstance();
            
            // mFragmentManager = this.getFragmentManager();
            // fragmentTransaction = mFragmentManager.beginTransaction();
            
            // fragmentTransaction.replace(R.id.main_second_layout, mTestlFragment);
            
            // fragmentTransaction.commit();
            
            // mTopFragment = mTestlFragment;
        }
        // 领导批示
        else if (ConstState.LDPS.equals(selectFuncCode))
        {
            String type = ConstState.LDPS_TYPE + "";
            mLeaderPiShiFragment = null;
            mLeaderPiShiFragment =
                LeaderPiShiFragment.newInstance(mFuncId, mFunName, mParentPath, mSearchKeywodrs, type);
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_second_layout, mLeaderPiShiFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mLeaderPiShiFragment;
        }
        // 电子地图
        else if (ConstState.DZDT.equals(selectFuncCode))
        {
            // DiTuFragment diTuFragment = null;
            // diTuFragment = DiTuFragment.newInstance();
            // fragmentTransaction = mFragmentManager.beginTransaction();
            //
            // fragmentTransaction.replace(R.id.main_second_layout, diTuFragment);
            //
            // fragmentTransaction.commit();
            //
            // mTopFragment = diTuFragment;
        }
        // 电视新闻
        else if (ConstState.DSXW.equals(selectFuncCode))
        {
            // mNetTVFragement = null;
            // mNetTVFragement = NetTVFragMent.newInstance(mSecondLevelMarginLayoutParams.width);
            //
            // fragmentTransaction = mFragmentManager.beginTransaction();
            //
            // fragmentTransaction.replace(R.id.main_second_layout, mNetTVFragement);
            //
            // fragmentTransaction.commit();
            // mTopFragment = mNetTVFragement;
        }
        // 视频监控
        else if (ConstState.SPJK.equals(selectFuncCode))
        {
            // mVideoMonitorFragment = null;
            // mVideoMonitorFragment = VideoMonitorFragment.newInstance("", "", "");
            //
            // fragmentTransaction = mFragmentManager.beginTransaction();
            //
            // fragmentTransaction.replace(R.id.main_second_layout, mVideoMonitorFragment);
            //
            // fragmentTransaction.commit();
            // mTopFragment = mVideoMonitorFragment;
        }
        // 内部邮件
        else if (ConstState.NBYJ.equals(selectFuncCode))
        {
            mInnerEmailFragment = null;
            mInnerEmailFragment = InnerEmailFragment.newInstance(mFuncId, mFunName, mParentPath);
            mFragmentManager = getSupportFragmentManager();
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_second_layout, mInnerEmailFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mInnerEmailFragment;
        }
        
        // 网络电视
        else if (ConstState.WLDS.equals(selectFuncCode))
        {
            nettvFirstFragment = null;
            nettvFirstFragment = NettvFragment.newInstance(mFuncId, mFunName, mParentPath);
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_second_layout, nettvFirstFragment);
            
            fragmentTransaction.commit();
            mTopFragment = nettvFirstFragment;
        }
        // 城市规划
        else if (ConstState.CSGH.equals(selectFuncCode))
        {
            cityPlanFirstFragment = null;
            cityPlanFirstFragment = CityPlanFragment.newInstance(mFuncId, mFunName, mParentPath);
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_second_layout, cityPlanFirstFragment);
            
            fragmentTransaction.commit();
            mTopFragment = cityPlanFirstFragment;
        }
        else if (ConstState.DBSY.equals(selectFuncCode))
        {
            mDaibanFragment = null;
            mDaibanFragment = DaibanFragment.newInstance(mHomeViewFunType, mType, mSearchKeywodrs, mParentPath);
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_second_layout, mDaibanFragment);
            
            fragmentTransaction.commit();
            mTopFragment = mDaibanFragment;
        }
        // 新闻中心
        else if (ConstState.XWZX.equals(selectFuncCode))
        {
            mXwzxFirstFragment = null;
            mXwzxFirstFragment = XwzxFirstFragment.newInstance(mFuncId, mFunName, mParentPath);
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_second_layout, mXwzxFirstFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mXwzxFirstFragment;
        }
    }
    
    /**
     * 执行 弹出某功能的第二菜单时，只有在完全弹出该layout，当动画结束时再加载数据，防止卡顿现象的出现
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-29
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void showTheThirdLayoutFragment()
    {
        mTopFragment = null;
        if (ConstState.GWLZ.equals(selectFuncCode))
        {
            String type = "";
            
            if (mModule.getFunccode().equals("SW"))
            {
                type = "0";
            }
            else if (mModule.getFunccode().equals("FW"))
            {
                type = "1";
            }
            
            if (mModule.getFlowtype().equals("0"))
            {
                // 公文流转二级菜单
                mOfficialSecondFragment = null;
                mOfficialSecondFragment =
                    OfficialSecondFragment.newInstance(type, mModule.getSearchkeywords(), path2, mModule.getFuncName());
            }
            else
            {
                // 公文流转二级菜单 默认情况下
                mOfficialSecondFragment = null;
                mOfficialSecondFragment =
                    OfficialSecondFragment.newInstance(type, mModule.getSearchkeywords(), path2, mModule.getFuncName());
            }
            
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_third_layout, mOfficialSecondFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mOfficialSecondFragment;
        }
        else if (ConstState.RCAP.equals(selectFuncCode))
        {
            // 日程安排二级菜单
            // mScheduleTimeSecondeFragement = null;
            // mScheduleTimeSecondeFragement = ScheduleTimeShaftFragement.newInstance(mInfo, mDate);
            //
            // fragmentTransaction = mFragmentManager.beginTransaction();
            //
            // fragmentTransaction.replace(R.id.main_third_layout, mScheduleTimeSecondeFragement);
            //
            // fragmentTransaction.commit();
            //
            // mTopFragment = mScheduleTimeSecondeFragement;
        }
        // 文档库
        else if (ConstState.WDK.equals(selectFuncCode))
        {
            String type = "";
            
            if (mModule.getFunccode().equals("QK"))
            {
                type = "2";
            }
            else if (mModule.getFunccode().equals("TZ"))
            {
                type = "3";
            }
            else if (mModule.getFunccode().equals("GDWJ"))
            {
                type = "5";
            }
            
            if (mModule.getFlowtype().equals("0"))
            {
                // 文档库
            }
            else if (mModule.getFlowtype().equals("1"))
            {
                // 文档库二级菜单
                mFileLibrarySecondFragment = null;
                mFileLibrarySecondFragment =
                    FileLibrarySecondFragment.newInstance(type,
                        mModule.getSearchkeywords(),
                        path2,
                        mModule.getFuncName());
            }
            else if (mModule.getFlowtype().equals("2"))
            {
                // 领导批示二级菜单
            }
            else
            {
                // 公文流转二级菜单 默认情况下
                mFileLibrarySecondFragment = null;
                mFileLibrarySecondFragment =
                    FileLibrarySecondFragment.newInstance(type,
                        mModule.getSearchkeywords(),
                        path2,
                        mModule.getFuncName());
            }
            
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_third_layout, mFileLibrarySecondFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mFileLibrarySecondFragment;
        }
        else if (ConstState.SPJK.equals(selectFuncCode))
        {
            // mVideoMonitorSecondFragment = null;
            // mVideoMonitorSecondFragment = VideoMonitorSecondFragment.newInstance(videoListItem, mParentPath);
            //
            // fragmentTransaction = mFragmentManager.beginTransaction();
            //
            // fragmentTransaction.replace(R.id.main_third_layout, mVideoMonitorSecondFragment);
            //
            // fragmentTransaction.commit();
            //
            // mTopFragment = mVideoMonitorSecondFragment;
        }
        // 内部邮件
        else if (ConstState.NBYJ.equals(selectFuncCode))
        {
            String type = "";
            
            if (mModule.getFunccode().equals("SJX"))
            {
                type = "1";
            }
            else if (mModule.getFunccode().equals("FJX"))
            {
                type = "2";
            }
            
            if (mModule.getFlowtype().equals("0"))
            {
                // 内部邮件二级菜单
                mInnerEmailSecondFragment = null;
                mInnerEmailSecondFragment =
                    InnerEmailSendFragment.newInstance(type, mModule.getSearchkeywords(), path2, mModule.getFuncName());
            }
            else
            {
                // 内部邮件二级菜单 默认情况下
                mInnerEmailSecondFragment = null;
                mInnerEmailSecondFragment =
                    InnerEmailSendFragment.newInstance(type, mModule.getSearchkeywords(), path2, mModule.getFuncName());
            }
            
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_third_layout, mInnerEmailSecondFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mInnerEmailSecondFragment;
        }
        else if (ConstState.WLDS.equals(selectFuncCode))
        {
            // 网络电视二级菜单
            nettvSecondFragment = null;
            nettvSecondFragment =
                NettvSecondFragment.newInstance("",
                    mModule.getSearchkeywords(),
                    path2,
                    mModule.getFuncName(),
                    mModule.getFuncId());
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_third_layout, nettvSecondFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = nettvSecondFragment;
        }
        else if (ConstState.CSGH.equals(selectFuncCode))
        {
            // 城市规划二级菜单
            cityPlanSecondFragment = null;
            cityPlanSecondFragment =
                CityPlanSecondFragment.newInstance("",
                    mModule.getSearchkeywords(),
                    path2,
                    mModule.getFuncName(),
                    mModule.getFuncId());
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_third_layout, cityPlanSecondFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = cityPlanSecondFragment;
        }
        // 新闻中心
        else if (ConstState.XWZX.equals(selectFuncCode))
        {
            mXwzxSecondFragment = null;
            mXwzxSecondFragment =
                XwzxSecondFragment.newInstance(mModule.getFunccode(),
                    mModule.getSearchkeywords(),
                    path2,
                    mModule.getFuncName());
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.main_third_layout, mXwzxSecondFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mXwzxSecondFragment;
            
        }
    }
    
    /**
     * 
     * 关闭三级布局
     * 
     * @LastModifiedDate：2013-9-26
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    private void closeTheThirdLevelLayout()
    {
        if (mThirdLevelLayout != null)
        {
            mThirdLevelLayout.removeAllViews();
        }
        
        if (ConstState.GWLZ.equals(selectFuncCode))
        {
            if (mOfficialFirstFragment != null)
            {
                mOfficialFirstFragment.theThirdFragmentClose();
                mTopFragment = mOfficialFirstFragment;
            }
        }
        else if (ConstState.RCAP.equals(selectFuncCode))
        {
            // if (mSchedulPlanFirstFragment != null)
            // {
            // mSchedulPlanFirstFragment.theThirdFragmentClose();
            // mTopFragment = mSchedulPlanFirstFragment;
            // }
        }
        else if (ConstState.CSGH.equals(selectFuncCode))
        {
            if (cityPlanFirstFragment != null)
            {
                cityPlanFirstFragment.theThirdFragmentClose();
                mTopFragment = cityPlanFirstFragment;
            }
        }
        else if (ConstState.WLDS.equals(selectFuncCode))
        {
            if (nettvFirstFragment != null)
            {
                nettvFirstFragment.theThirdFragmentClose();
                mTopFragment = nettvFirstFragment;
            }
        }
        // 文档库
        else if (ConstState.WDK.equals(selectFuncCode))
        {
            if (mFileLibraryFragment != null)
            {
                mFileLibraryFragment.theThirdFragmentClose();
                mTopFragment = mFileLibraryFragment;
                
                mFileLibrarySecondFragment = null;
            }
        }
        else if (ConstState.SPJK.equals(selectFuncCode))
        {
            // if (mVideoMonitorFragment != null)
            // {
            // mVideoMonitorFragment.theThirdFragmentClose();
            // mTopFragment = mVideoMonitorFragment;
            //
            // mVideoMonitorSecondFragment = null;
            // }
        }
        // 内部邮件
        else if (ConstState.NBYJ.equals(selectFuncCode))
        {
            if (mInnerEmailFragment != null)
            {
                mInnerEmailFragment.theThirdFragmentClose();
                mTopFragment = mInnerEmailFragment;
                
                mInnerEmailSecondFragment = null;
            }
        }
        // 新闻中心
        else if (ConstState.XWZX.equals(selectFuncCode))
        {
            if (mXwzxFirstFragment != null)
            {
                mXwzxFirstFragment.theThirdFragmentClose();
                mTopFragment = mXwzxFirstFragment;
            }
        }
        mThirdLevelMarginLayoutParams.leftMargin = mScreenWidth;
        mThirdLevelLayout.setLayoutParams(mThirdLevelMarginLayoutParams);
        
        AnimRunnable mThirdAnimationRunnable =
            new AnimRunnable(10, mScreenWidth, mThirdLevelMarginLayoutParams, mMainHandler, AnimRunnable.THIRD_FRAGMENT);
        new Thread(mThirdAnimationRunnable).start();
        
        // mSecondLevelMarginLayoutParams.leftMargin = mSecondToLeft;
        // mSecondLevelLayout.setLayoutParams(mSecondLevelMarginLayoutParams);
        mAnimationRunnable =
            new AnimRunnable(2, 0, mSecondLevelMarginLayoutParams, mMainHandler, AnimRunnable.SECOND_FRAGMENT);
        new Thread(mAnimationRunnable).start();
    }
    
    /**
     * 二级布局被关闭
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-4
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    @SuppressWarnings("static-access")
    private void closeTheSecondLevelLayout()
    {
        if (mSecondLevelLayout != null)
        {
            mSecondLevelLayout.removeAllViews();
        }
        if (mThirdLevelMarginLayoutParams.leftMargin == mScreenWidth
            || !(mThirdLevelLayout.getVisibility() == View.VISIBLE))
        {
            // mSecondLevelMarginLayoutParams.leftMargin = mScreenWidth;
            // mSecondLevelLayout.setLayoutParams(mSecondLevelMarginLayoutParams);
            mAnimationRunnable =
                new AnimRunnable(10, mScreenWidth, mSecondLevelMarginLayoutParams, mMainHandler,
                    AnimRunnable.SECOND_FRAGMENT);
            new Thread(mAnimationRunnable).start();
            selectFuncCode = "-1";
            
            mMenuAdapter.setmSelectedPosition(-1);
            mMenuAdapter.notifyDataSetChanged();
            
            mTopFragment = null;
        }
    }
    
    /**
     * 重载方法
     * 
     * @param v view
     * @author shen_feng
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_logout:
                // 注销按钮
                // final float scale = this.getResources().getDisplayMetrics().density;
                final CustomDialog dialog = CustomDialog.newInstance("确定注销登录？", "取消", "确定");
                
                dialog.show(mFragmentManager, "logoutDialog");
                
                dialog.setLeftListener(new CustomDialogListener()
                {
                    
                    @Override
                    public void Onclick()
                    {
                        // MainActivity.this.finish();
                        dialog.dismiss();
                    }
                });
                dialog.setRightListener(new CustomDialogListener()
                {
                    
                    @Override
                    public void Onclick()
                    {
                        dialog.dismiss();
                        GlobalState.getInstance().setIsAutoLogin(false);
                        
                        Intent it = new Intent(MainActivity.this, LoginActivity.class);
                        
                        startActivity(it);
                        
                        MainActivity.this.finish();
                    }
                });
                break;
            // case R.id.usehistory:
            // Intent intent = new Intent();
            // intent.setClass(this, UseSummary.class);
            // startActivity(intent);
            // 设置
            // case R.id.system_set:
            // Intent intentoset = new Intent();
            // intentoset.setClass(MainActivity.this, PersonSetView.class);
            // this.startActivity(intentoset);
            // break;
            // 主题设置
            // case R.id.private_set:
            // themeSetLinear_rl.setVisibility(View.VISIBLE);
            //
            // themeSetLinear_rl.getBackground().setAlpha(100);
            // themeSetLinear_rl.bringToFront();
            // themeSetLinear_rl.invalidate();
            // break;
            // case R.id.button_close:
            // themeSetLinear_rl.setVisibility(View.GONE);
            // break;
            
            // case R.id.tv_line:
            //
            // if (GlobalState.getInstance().getOfflineLogin())
            // {
            // GlobalState.getInstance().setOfflineLogin(false);
            // onLineTv.setText(getResources().getString(R.string.main_online));
            // }
            // else
            // {
            // GlobalState.getInstance().setOfflineLogin(true);
            // onLineTv.setText(getResources().getString(R.string.main_offline));
            // }
            //
            // break;
            // case R.id.face:
            // showView(mImFace);
            // break;
            case R.id.userset:
                Intent intent = new Intent();
                intent.setClass(this, PersonSetView.class);
                startActivity(intent);
                break;
            
            case R.id.lastestdaiban:
                
                if (mDaibanModule == null)
                {
                    LogUtil.i("", "服务器没有分配待办事宜模块");
                    return;
                }
                
                if (selectFuncCode != null && selectFuncCode.equals(ConstState.DBSY))
                {
                    return;
                }
                
                if (!isSecondLayoutonTouch)
                {
                    releaseTheThirdFramgeLayout();
                }
                
                releaseTheSecondFramgeLayout();
                
                selectFuncCode = ConstState.DBSY;
                
                mHomeViewFunType = ConstState.HOME_DB;
                
                mFuncId = mDaibanModule.getFuncId();
                
                mFunName = mDaibanModule.getFuncName();
                
                mType = mDaibanModule.getType();
                
                mParentPath = ConstState.MIP_ROOT_DIR + userName + "/";
                
                mSearchKeywodrs = mDaibanModule.getSearchkeywords();
                
                openFirstFragment();
                
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     * 比较器
     * 
     * @Description<功能详细描述>
     * 
     * @author wang_ling
     * @Version [版本号, 2013-9-29]
     */
    public static class MyComparator implements Comparator<LoginModule>
    {
        /**
         * 重载方法
         * 
         * @param lhs
         * @param rhs
         * @return
         * @author wang_ling
         */
        @Override
        public int compare(LoginModule lhs, LoginModule rhs)
        {
            return lhs.getSort().compareTo(rhs.getSort());
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            
            if (mTopFragment != null)
            {
                if (mTopFragment.onKeyDown(keyCode))
                {
                    return false;
                }
                
            }
            
            dialog =
                CustomDialog.newInstance(this.getResources().getString(R.string.main_exit_app), this.getResources()
                    .getString(R.string.Login_Cancel), this.getResources().getString(R.string.Login_Confirm));
            dialog.show(mFragmentManager, "ExitDialog");
            
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
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            if (mTopFragment != null)
            {
                mTopFragment.onKeyDown(keyCode);
            }
        }
        
        return false;
    }
    
    /**
     * 设置功能列表项的背景图
     * 
     * @Description<功能详细描述>
     * 
     * @param module 列表
     * @LastModifiedDate：2013-10-25
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void setModuleBackground(LoginModule module)
    {
        if (module != null)
        {
            int backgroud = -1;
            int pressed_backgroud = -1;
            int unpressed_backgroud = -1;
            int icon_backgroud = -1;
            String funName = module.getFuncCode();
            
            if (funName.equals(ConstState.GWLZ))
            {
                // backgroud = R.drawable.menu_official_selector;
                // pressed_backgroud = R.drawable.button_pink;
                icon_backgroud = R.drawable.icon_1;
            }
            else if (funName.equals(ConstState.RCAP))
            {
                // backgroud = R.drawable.menu_schedule_selector;
                // pressed_backgroud = R.drawable.button_orange;
                // icon_backgroud = R.drawable.icon_leader;
            }
            // 领导日程
            else if (funName.equals(ConstState.LDRC))
            {
                // backgroud = R.drawable.menu_schedule_selector;
                // pressed_backgroud = R.drawable.button_blue;
                icon_backgroud = R.drawable.icon_6;
            }
            // 文档库
            else if (funName.equals(ConstState.WDK))
            {
                // backgroud = R.drawable.menu_official_selector;
                // pressed_backgroud = R.drawable.button_yellow;
                icon_backgroud = R.drawable.icon_5;
            }
            // 领导批示
            else if (funName.equals(ConstState.LDPS))
            {
                // backgroud = R.drawable.menu_official_selector;
                // pressed_backgroud = R.drawable.button_orange;
                icon_backgroud = R.drawable.icon_4;
            }
            else if (funName.equals(ConstState.NBYJ))
            {
                icon_backgroud = R.drawable.icon_8;
            }
            else if (funName.equals(ConstState.WLDS))
            {
                icon_backgroud = R.drawable.icon_9;
            }
            else if (funName.equals(ConstState.CSGH))
            {
                icon_backgroud = R.drawable.icon_7;
            }
            else
            {
                // backgroud = R.drawable.menu_official_selector;
                // pressed_backgroud = R.drawable.button_pink;
                icon_backgroud = R.drawable.icon_3;
            }
            // unpressed_backgroud = R.drawable.button_toumin;
            // module.setmBackGround(backgroud);
            // module.setPressed_BackGround(pressed_backgroud);
            // module.setUnPressed_BackGround(unpressed_backgroud);
            module.setmIcon_BackGround(icon_backgroud);
        }
    }
    
    /**
     * 
     * 设置二级栏目的宽度
     * 
     * @Description 设置二级栏目的宽度
     * 
     * @param params
     * @LastModifiedDate：2013-11-19
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void setSecondLayoutWith(ViewGroup.MarginLayoutParams params)
    {
        
        // if (ConstState.LDRC.equals(selectFuncCode) || ConstState.LDPS.equals(selectFuncCode))
        // {
        // mSecondToLeft = (int)(mFirstLayout.getWidth() - 20 * ((float)mDip / 160));
        //
        // params.width = mScreenWidth - mSecondToLeft;
        // }
        // else if (ConstState.DSXW.equals(selectFuncCode) || ConstState.DZDT.equals(selectFuncCode))
        // {
        // mSecondToLeft = mImFace.getWidth();
        // params.width = mScreenWidth - mSecondToLeft;
        //
        // }
        // else
        // {
        // mSecondToLeft = (int)(mFirstLayout.getWidth() - 20 * ((float)mDip / 160));
        // params.width = (int)(512 * ((float)mDip / 160));
        // }
        params.width = mScreenWidth;
        params.leftMargin = 0;
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onPersonSetBind()
    {
        if (GlobalState.getInstance().getIsBind())
        {
            // bindTv.setText(getResources().getString(R.string.main_bidn));
            // bindTv.setVisibility(View.VISIBLE);
        }
        else
        {
            // bindTv.setText(getResources().getString(R.string.main_notbind));
            // bindTv.setVisibility(View.INVISIBLE);
        }
        
    }
    
    /**
     * 重载方法
     * 
     * @author wen_tao
     */
    @Override
    public void onPersonSetHeader(Bitmap bitmap)
    {
        // 此处应该对图片进行缩放
        // 原始大小
        int bmpWidth = bitmap.getWidth();
        
        int bmpHeight = bitmap.getHeight();
        // 缩放比例 ：缩放后的宽度为80
        float scaleWidth = (float)80 / bmpWidth; // 按固定大小缩放 sWidth 写多大就多大
        
        float scaleHeight = (float)80 / bmpHeight; //
        
        Matrix matrix = new Matrix();
        
        matrix.postScale(scaleWidth, scaleHeight);// 产生缩放后的Bitmap对象
        
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, false);
        mImFace.setImageBitmap(bitmap);
    }
    
    private void releaseTheThirdFramgeLayout()
    {
        mTopFragment = null;
        if (ConstState.GWLZ.equals(selectFuncCode))
        {
            if (mOfficialSecondFragment != null)
            {
                mOfficialSecondFragment.closeThisFragment();
                mOfficialSecondFragment = null;
            }
        }
        else if (ConstState.RCAP.equals(selectFuncCode))
        {
            // 日程安排二级菜单
            // if (mScheduleTimeSecondeFragement != null)
            // {
            // mScheduleTimeSecondeFragement.closeThisFragment();
            // mScheduleTimeSecondeFragement = null;
            // }
        }
        // 文档库
        else if (ConstState.WDK.equals(selectFuncCode))
        {
            if (mFileLibrarySecondFragment != null)
            {
                mFileLibrarySecondFragment.closeThisFragment();
                mFileLibrarySecondFragment = null;
            }
        }
        else if (ConstState.SPJK.equals(selectFuncCode))
        {
            // if (mVideoMonitorSecondFragment != null)
            // {
            // mVideoMonitorSecondFragment.closeThisFragment();
            // mVideoMonitorSecondFragment = null;
            //
            // if (mVideoMonitorFragment != null)
            // {
            // mVideoMonitorFragment.theThirdFragmentClose();
            // }
            // mTopFragment = mVideoMonitorFragment;
            //
            // }
        }
        // 内部邮件
        else if (ConstState.NBYJ.equals(selectFuncCode))
        {
            if (mInnerEmailSecondFragment != null)
            {
                mInnerEmailSecondFragment.closeThisFragment();
                mInnerEmailSecondFragment = null;
            }
        }
        // 新闻中心
        else if (ConstState.XWZX.equals(selectFuncCode))
        {
            if (mXwzxSecondFragment != null)
            {
                mXwzxSecondFragment.closeThisFragment();
                mXwzxFirstFragment = null;
            }
        }
    }
    
    private void releaseTheSecondFramgeLayout()
    {
        mTopFragment = null;
        if (ConstState.GWLZ.equals(selectFuncCode))
        {
            if (mOfficialFirstFragment != null)
            {
                mOfficialFirstFragment = null;
            }
        }
        else if (ConstState.RCAP.equals(selectFuncCode))
        {
            // 日程安排二级菜单
            // if (mSchedulPlanFirstFragment != null)
            // {
            // mSchedulPlanFirstFragment = null;
            // }
        }
        // 文档库
        else if (ConstState.WDK.equals(selectFuncCode))
        {
            if (mFileLibraryFragment != null)
            {
                mFileLibraryFragment = null;
            }
        }
        else if (ConstState.LDRC.equals(selectFuncCode))
        {
            // if (mLeaderScheduleFragment != null)
            // {
            // mLeaderScheduleFragment = null;
            // }
        }
        else if (ConstState.LDRC.equals(selectFuncCode))
        {
            if (mLeaderPiShiFragment != null)
            {
                mLeaderPiShiFragment = null;
            }
        }
        else if (ConstState.SPJK.equals(selectFuncCode))
        {
            // if (mVideoMonitorFragment != null)
            // {
            // mVideoMonitorFragment = null;
            // }
        }
        // 内部邮件
        else if (ConstState.NBYJ.equals(selectFuncCode))
        {
            if (mInnerEmailFragment != null)
            {
                mInnerEmailFragment = null;
            }
        }
        // 新闻中心
        else if (ConstState.XWZX.equals(selectFuncCode))
        {
            if (null != mXwzxFirstFragment)
            {
                mXwzxFirstFragment = null;
            }
            
        }
    }
    
    /**
     * 重载方法
     * 
     * @param index
     * @param mModlue
     * @param path
     * @author ren_qiujing
     */
    @Override
    public void onOfficialFragmentSelected(int index, GWDocModule mModlue, String path)
    {
        // TODO Auto-generated method stub
        this.mModule = mModlue;
        this.path2 = path;
        isSecondLayoutonTouch = false;
        
        mThirdLevelLayout.removeAllViews();
        
        showTheThirdLevelLayout();
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void onOfficialFragmentCloseSelected()
    {
        // TODO Auto-generated method stub
        closeTheThirdLevelLayout();
    }
    
    /**
     * 重载方法
     * 
     * @param index
     * @param list_info
     * @param date
     * @author ren_qiujing
     */
    @Override
    public void onSchedulePlanSelected(int index, ScheduleInfo list_info, String date)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void onScheduleTimeFragmentCloseSelected()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void onScheduleChangedListener()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param isFull
     * @author ren_qiujing
     */
    @Override
    public void onNetTVShowFullScreen(boolean isFull)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param item
     * @author ren_qiujing
     */
    @Override
    public void onVideoMonitorFragmentSelected(VideoListItem item)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void onCloseHomeViewFragment()
    {
        // TODO Auto-generated method stub
        closeTheSecondLevelLayout();
    }
    
    /**
     * 重载方法
     * 
     * @param requestCode
     * @param resultCode
     * @param data
     * @author wen_tao
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 11 && resultCode == 11)
        {
            if (cityPlanFirstFragment != null)
            {
                cityPlanFirstFragment.theThirdFragmentClose();
            }
        }
    }
    
    public void sendGetUnReadCount(String funId, String funtype)
    {
        if (mSendGetUnreadCountTask == null)
        {
            mSendGetUnreadCountTask = new HttpNetFactoryCreator(RequestTypeConstants.MODULENOREAD_REQUEST).create();
            
            JSONObject body = new JSONObject();
            try
            {
                body.put("funcid", funId);
                body.put("funccode", funtype);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            NetRequestController.SendModuleNoRead(mSendGetUnreadCountTask,
                mHandler,
                RequestTypeConstants.MODULENOREAD_REQUEST,
                body);
        }
    }
    
    /**
     * 重载方法
     * 
     * @param funId
     * @param funcode
     * @author ren_qiujing
     */
    @Override
    public void onSendGetUnReadCount(String funId, String funcode)
    {
        // TODO Auto-generated method stub
        sendGetUnReadCount(funId, funcode);
    }
    
    public void updateUnreadCount()
    {
        if (mMenuAdapter != null)
        {
            List<LoginModule> modules = mMenuAdapter.getLoginModule();
            if (modules != null)
            {
                for (int i = 0; i < modules.size(); i++)
                {
                    LoginModule module = modules.get(i);
                    if (module.getFuncCode().equals(ConstState.GWLZ))
                    {
                        module.setCount(ConstState.GWLZUNREADCOUNT);
                    }
                }
            }
            mMenuAdapter.notifyDataSetChanged();
        }
    }
    
    public void updateDaibanUnreadCount()
    {
        if (ConstState.DAIBANUNREADCOUNT != null && !ConstState.DAIBANUNREADCOUNT.equals("")
            && !ConstState.DAIBANUNREADCOUNT.equals("0"))
        {
            mDaibanCountRL.setVisibility(View.VISIBLE);
            mDaibanCountTV.setText(ConstState.DAIBANUNREADCOUNT);
        }
        else
        {
            mDaibanCountRL.setVisibility(View.INVISIBLE);
        }
        
    }
}
