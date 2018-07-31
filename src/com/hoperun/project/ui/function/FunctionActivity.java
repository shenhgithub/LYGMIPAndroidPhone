/*
 * File name:  FunctionActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-3-24
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hoperun.manager.adpter.MenuAdapter;
import com.hoperun.manager.components.AnimRunnable;
import com.hoperun.manager.components.CustomDialog;
import com.hoperun.manager.components.CustomDialogListener;
import com.hoperun.manager.components.HeadView;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.exceptions.MIPException;
import com.hoperun.mip.sqlUtils.SQLCreator.DBHandler;
import com.hoperun.mip.sqlUtils.Table.TFUNSIONPOWER;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.utils.MessageSQLIdConstants;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.Login.LoginModule;
import com.hoperun.mipmanager.model.entityModule.nettv.VideoListItem;
import com.hoperun.mipmanager.model.entityModule.offical.GWDocModule;
import com.hoperun.mipmanager.model.entityModule.schedule.ScheduleInfo;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.project.ui.Login.LoginActivity;
import com.hoperun.project.ui.Login.PersonSetView;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;
import com.hoperun.project.ui.baseui.baseInterface.IFragmentToMainActivityListen;
import com.hoperun.project.ui.baseui.fragment.PMIPBaseFragment;
import com.hoperun.project.ui.function.devSupervise.DevSuperviseFragement;
import com.hoperun.project.ui.function.fastNews.FaseNewsFragement;
import com.hoperun.project.ui.function.lowcport.LowcPortFragment;
import com.hoperun.project.ui.function.pagk.PagkFragment;
import com.hoperun.project.ui.function.txl.TxlFirstActivity;
import com.hoperun.project.ui.function.xtbg.XtbgActivity;
import com.hoperun.project.ui.function.ygfw.YgfwFragment;
import com.hoperun.project.ui.function.ygsj.YgsjFragment;
import com.hoperun.project.ui.function.ywbl.ywblFirstFragment;
import com.hoperun.project.ui.xwzx.XwzxFragment;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-24]
 */
public class FunctionActivity extends PMIPBaseActivity implements OnItemClickListener, OnTouchListener,
    IFragmentToMainActivityListen
{
    /** 一级列表数据 **/
    private ArrayList<LoginModule> modules;
    
    /**
     * 一级布局列表
     */
    private MenuAdapter            mMenuAdapter;
    
    /** 暂无数据 **/
    private RelativeLayout         mNoDataRl;
    
    /**
     * 主栏目布局
     */
    private ListView               mLvMenu;
    
    /** 选择哪个功能 **/
    private String                 selectFuncCode                  = "";
    
    /**
     * 点击功能列表中某功能的功能ID；
     */
    private String                 mFuncId                         = "";
    
    /**
     * 点击功能列表中某功能的功能名称；
     */
    private String                 mFunName                        = "";
    
    /**
     * 点击功能列表中某功能的功能的根路径
     */
    private String                 mParentPath                     = "";
    
    /**
     * 点击功能列表中某共嫩的功能的搜索关键字
     */
    private String                 mSearchKeywodrs                 = "";
    
    /** 头部 **/
    private HeadView               mHeadView;
    
    /** openId **/
    private String                 openId;
    
    /**
     * 左侧功能菜单栏
     */
    private FrameLayout            mFrameLayoutFirst;
    
    private MarginLayoutParams     mFrameMarginFirs;
    
    /** AnimRunnable **/
    private AnimRunnable           mAnimationRunnable;
    
    private boolean                isSecondLayoutonTouch           = true;
    
    /** 二级布局，三级布局滑动标记 **/
    public static final int        HANDLER_MESSAGE_UPDATE_POSITION = 101;
    
    public static final int        HANDLER_MESSAGE_CHANGELINE      = 102;
    
    /** 新闻中心 **/
    private XwzxFragment           mXwzxFragment;
    
    /** 今日快报 **/
    private FaseNewsFragement      mFastNewsFragemnt;
    
    /** 业务办理 **/
    private ywblFirstFragment      mYwblFragment;
    
    /** 平安港口 **/
    private PagkFragment           mPagkFragment;
    
    /** 员工社交 **/
    private YgsjFragment           mYgsjFragment;
    
    /** 员工服务 **/
    private YgfwFragment           mYgfwFragment;
    
    /** 设备运行 **/
    private DevSuperviseFragement  mDevSuperviseFragment;
    
    /** 设备运行 **/
    private LowcPortFragment       mLowcPortFragment;
    
    /**
     * fragment管理器
     */
    private FragmentManager        mFragmentManager;
    
    /**
     * fragment事务管理
     */
    private FragmentTransaction    fragmentTransaction;
    
    private int                    mFirstLayouToLeft               = 80;
    
    private PMIPBaseFragment       mTopFragment;
    
    private RelativeLayout         mTempRl;
    
    /**
     * 向左消失动画
     */
    private Animation              mLeftOut;
    
    /**
     * 向左进入动画
     */
    private Animation              mLeftIn;
    
    /**
     * 向右消失动画
     */
    private Animation              mRightOut;
    
    /**
     * 向右进入动画
     */
    private Animation              mRightIn;
    
    private Handler                mMainHandler                    = new Handler()
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
                                                                                   
                                                                                   mFrameLayoutFirst.setLayoutParams(mFrameMarginFirs);
                                                                                   mFrameLayoutFirst.requestLayout();
                                                                                   // if (mFrameMarginFirs.leftMargin ==
                                                                                   // GlobalState.getInstance()
                                                                                   // .getmScreen_With())
                                                                                   // {
                                                                                   // onSecondFragmentClose();
                                                                                   // }
                                                                                   
                                                                                   break;
                                                                               
                                                                               default:
                                                                                   break;
                                                                           }
                                                                       }
                                                                   };
    
    private OnClickListener        mPersonSetListener              = new OnClickListener()
                                                                   {
                                                                       
                                                                       @Override
                                                                       public void onClick(View v)
                                                                       {
                                                                           
                                                                           Intent intent = new Intent();
                                                                           intent.setClass(FunctionActivity.this,
                                                                               PersonSetView.class);
                                                                           startActivity(intent);
                                                                       }
                                                                       
                                                                   };
    
    private OnClickListener        mExistOnclickListen             = new OnClickListener()
                                                                   {
                                                                       
                                                                       @Override
                                                                       public void onClick(View v)
                                                                       {
                                                                           if (null != mTopFragment)
                                                                           {
                                                                               return;
                                                                           }
                                                                           final CustomDialog dialog =
                                                                               CustomDialog.newInstance("确定注销登录？",
                                                                                   "取消",
                                                                                   "确定");
                                                                           
                                                                           dialog.show(getSupportFragmentManager(),
                                                                               "logoutDialog");
                                                                           
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
                                                                                   GlobalState.getInstance()
                                                                                       .clearValues();
                                                                                   Intent it =
                                                                                       new Intent(
                                                                                           FunctionActivity.this,
                                                                                           LoginActivity.class);
                                                                                   it.putExtra("biaozhi", "0");
                                                                                   startActivity(it);
                                                                                   
                                                                                   FunctionActivity.this.finish();
                                                                               }
                                                                           });
                                                                           
                                                                       }
                                                                   };
    
    /**
     * 重载方法
     * 
     * @param savedInstanceState
     * @author wang_ling
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function_main);
        initView();
        initData();
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-3-27
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void initView()
    {
        
        mHeadView = (HeadView)findViewById(R.id.mobile_header);
        mNoDataRl = (RelativeLayout)findViewById(R.id.no_datarl);
        mLvMenu = (ListView)findViewById(R.id.lv_menu);
        mLvMenu.setOnItemClickListener(this);
        
        mFrameLayoutFirst = (FrameLayout)findViewById(R.id.fragment_1);
        mFrameLayoutFirst.setOnTouchListener(this);
        
        mFrameMarginFirs = (MarginLayoutParams)mFrameLayoutFirst.getLayoutParams();
        
        mTempRl = (RelativeLayout)findViewById(R.id.temp_rl);
        
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-3-27
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @SuppressWarnings("static-access")
    public void initData()
    {
        // title_2
        
        openId = GlobalState.getInstance().getOpenId();
        mHeadView.setTitle(GlobalState.getInstance().getUserName());
        mHeadView.setLeftOnclickLisen(mPersonSetListener);
        mHeadView.setRightOnclickLisen(mExistOnclickListen);
        
        modules = getModuleFromTable();
        
        MyComparator myComparator = new MyComparator();
        Collections.sort(modules, myComparator);
        
        mMenuAdapter = new MenuAdapter(this, modules);
        
        mLvMenu.setAdapter(mMenuAdapter);
        
        if (modules.size() == 0)
        {
            mNoDataRl.setVisibility(View.VISIBLE);
        }
        
        mLeftOut = new AnimationUtils().loadAnimation(this, R.anim.fromleftout);
        mLeftIn = new AnimationUtils().loadAnimation(this, R.anim.fromleftin);
        
        mRightOut = new AnimationUtils().loadAnimation(this, R.anim.fromrightout);
        mRightIn = new AnimationUtils().loadAnimation(this, R.anim.fromrightin);
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
        
        if (funCode.equals(ConstState.XWZX) || funCode.equals(ConstState.JRKB) || funCode.equals(ConstState.SBYX)
            || funCode.equals(ConstState.DTGK) || funCode.equals(ConstState.TXL) || funCode.equals(ConstState.YWBL)
            || funCode.equals(ConstState.GWCK) || funCode.equals(ConstState.YGFW) || funCode.equals(ConstState.YGSJ)
            || funCode.equals(ConstState.SPJK))
        {
            return true;
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
            
            if (funName.equals(ConstState.XWZX))
            {
                // backgroud = R.drawable.menu_official_selector;
                // pressed_backgroud = R.drawable.button_pink;
                icon_backgroud = R.drawable.icon_1;
            }
            else if (funName.equals(ConstState.JRKB))
            {
                // backgroud = R.drawable.menu_schedule_selector;
                // pressed_backgroud = R.drawable.button_orange;
                icon_backgroud = R.drawable.icon_2;
            }
            else if (funName.equals(ConstState.YWBL))
            {
                // backgroud = R.drawable.menu_schedule_selector;
                // pressed_backgroud = R.drawable.button_blue;
                icon_backgroud = R.drawable.icon_3;
            }
            else if (funName.equals(ConstState.TXL))
            {
                // backgroud = R.drawable.menu_official_selector;
                // pressed_backgroud = R.drawable.button_yellow;
                icon_backgroud = R.drawable.icon_4;
            }
            else if (funName.equals(ConstState.SPJK))
            {
                // backgroud = R.drawable.menu_official_selector;
                // pressed_backgroud = R.drawable.button_orange;
                icon_backgroud = R.drawable.icon_5;
            }
            else if (funName.equals(ConstState.SBYX))// ConstState.SBJGZX
            {
                icon_backgroud = R.drawable.icon_6;
            }
            else if (funName.equals(ConstState.DTGK))// ConstState.NYJGZX
            {
                icon_backgroud = R.drawable.icon_7;
            }
            else if (funName.equals(ConstState.CYGN))
            {
                icon_backgroud = R.drawable.icon_8;
            }
            else if (funName.equals(ConstState.GWCK))
            {
                icon_backgroud = R.drawable.c_icon_6;
            }
            else if (funName.equals(ConstState.YGFW))// 员工服务
            {
                icon_backgroud = R.drawable.icon_ygfw;
            }
            else if (funName.equals(ConstState.YGSJ))// 员工社交
            {
                icon_backgroud = R.drawable.icon_ygsj;
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
            if (!"".equals(lhs.getSort()) && !"".equals(rhs.getSort()))
            {
                
                return Integer.parseInt(lhs.getSort()) > Integer.parseInt(rhs.getSort()) ? 1 : -1;
            }
            else
            {
                return -1;
            }
            // return lhs.getSort().compareTo(rhs.getSort());
        }
    }
    
    /**
     * 重载方法
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @author wang_ling
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        
        if (((String)mMenuAdapter.getItem(arg2)).equals(ConstState.XWZX)
            || ((String)mMenuAdapter.getItem(arg2)).equals(ConstState.JRKB)
            || ((String)mMenuAdapter.getItem(arg2)).equals(ConstState.YWBL)
            || ((String)mMenuAdapter.getItem(arg2)).equals(ConstState.SPJK)
            || ((String)mMenuAdapter.getItem(arg2)).equals(ConstState.YGFW)
            || ((String)mMenuAdapter.getItem(arg2)).equals(ConstState.YGSJ)
            || ((String)mMenuAdapter.getItem(arg2)).equals(ConstState.SBYX)
            || ((String)mMenuAdapter.getItem(arg2)).equals(ConstState.DTGK))
        {
            if (selectFuncCode != null && selectFuncCode.equals((String)mMenuAdapter.getItem(arg2)))
            {
                return;
            }
            mTempRl.setVisibility(View.VISIBLE);
        }
        else
        {
            mTempRl.setVisibility(View.GONE);
        }
        
        mFrameLayoutFirst.startAnimation(mRightOut);
        selectFuncCode = (String)mMenuAdapter.getItem(arg2);
        
        mFuncId = (String)mMenuAdapter.getFuncId(arg2);
        
        mFunName = (String)mMenuAdapter.getFunName(arg2);
        
        mParentPath = openId + "/" + selectFuncCode;
        mSearchKeywodrs = (String)mMenuAdapter.getSearchKeyWords(arg2);
        
        mFirstLayouToLeft = mHeadView.getLeftRLWith();
        
        isSecondLayoutonTouch = true;
        
        mFrameLayoutFirst.removeAllViews();
        
        showTheSecondLayoutActivity();
        
        mFrameMarginFirs.leftMargin = mFirstLayouToLeft;
        mFrameMarginFirs.width = GlobalState.getInstance().getmScreen_With() - mFirstLayouToLeft;
        mFrameLayoutFirst.setLayoutParams(mFrameMarginFirs);
        
        // mTempRl.setVisibility(View.VISIBLE);
        mFrameLayoutFirst.startAnimation(mRightIn);
    }
    
    /**
     * 
     * 显示功能二级菜单
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-3-27
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void showTheSecondLayoutActivity()
    {
        // 新闻中心
        if (ConstState.XWZX.equals(selectFuncCode))
        {
            // Intent xwzxIntent = new Intent(this, XwzxFirstActivity.class);
            // xwzxIntent.putExtra("funcId", mFuncId);
            // xwzxIntent.putExtra("funName", mFunName);
            // xwzxIntent.putExtra("parentsPath", mParentPath);
            // this.startActivity(xwzxIntent);
            mXwzxFragment = null;
            mXwzxFragment = XwzxFragment.newInstance(mParentPath, mFuncId, mFunName);
            mXwzxFragment.setmTouchListener(this);
            
            mFragmentManager = this.getSupportFragmentManager();
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.fragment_1, mXwzxFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mXwzxFragment;
            
        }
        // 今日快报
        else if (ConstState.JRKB.equals(selectFuncCode))
        {
            // Intent fastNewsIntent = new Intent(this, FastNewsFirstActivity.class);
            // fastNewsIntent.putExtra("funcId", mFuncId);
            // fastNewsIntent.putExtra("funName", mFunName);
            // fastNewsIntent.putExtra("parentsPath", mParentPath);
            // this.startActivity(fastNewsIntent);
            mFastNewsFragemnt = null;
            mFastNewsFragemnt = FaseNewsFragement.newInstance(mParentPath, mFuncId, mFunName);
            mFastNewsFragemnt.setmTouchListener(this);
            
            mFragmentManager = this.getSupportFragmentManager();
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.fragment_1, mFastNewsFragemnt);
            
            fragmentTransaction.commit();
            
            mTopFragment = mFastNewsFragemnt;
        }
        // 设备运行
        else if (ConstState.SBYX.equals(selectFuncCode))
        {
            // Intent fastNewsIntent = new Intent(this, DevSuperviseActivity.class);
            // fastNewsIntent.putExtra("funcCode", selectFuncCode);
            // fastNewsIntent.putExtra("funName", mFunName);
            // this.startActivity(fastNewsIntent);
            mDevSuperviseFragment = null;
            mDevSuperviseFragment = DevSuperviseFragement.newInstance(mParentPath, mFuncId, mFunName);
            mDevSuperviseFragment.setmTouchListener(this);
            
            mFragmentManager = this.getSupportFragmentManager();
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.fragment_1, mDevSuperviseFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mDevSuperviseFragment;
        }
        // 低碳港口
        else if (ConstState.DTGK.equals(selectFuncCode))
        {
            // Intent fastNewsIntent = new Intent(this, LowcPortActivity.class);
            // fastNewsIntent.putExtra("funcCode", selectFuncCode);
            // fastNewsIntent.putExtra("funName", mFunName);
            // this.startActivity(fastNewsIntent);
            mLowcPortFragment = null;
            mLowcPortFragment = LowcPortFragment.newInstance(mParentPath, mFuncId, mFunName);
            mLowcPortFragment.setmTouchListener(this);
            
            mFragmentManager = this.getSupportFragmentManager();
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.fragment_1, mLowcPortFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mLowcPortFragment;
        }
        // 通讯录
        else if (ConstState.TXL.equals(selectFuncCode))
        {
            Intent noDevelop = new Intent(this, TxlFirstActivity.class);// NotDevolopActivity
            noDevelop.putExtra("funcCode", selectFuncCode);
            noDevelop.putExtra("funName", mFunName);
            this.startActivity(noDevelop);
        }
        // 业务办理
        else if (ConstState.YWBL.equals(selectFuncCode))
        {
            // Intent ywblIntent = new Intent(this, NotDevolopActivity.class);
            // ywblIntent.putExtra("funcId", mFuncId);
            // ywblIntent.putExtra("funName", mFunName);
            // ywblIntent.putExtra("parentsPath", mParentPath);
            // this.startActivity(ywblIntent);
            
            mYwblFragment = null;
            mYwblFragment = ywblFirstFragment.newInstance(mParentPath, mFuncId, mFunName);
            mYwblFragment.setmTouchListener(this);
            
            mFragmentManager = this.getSupportFragmentManager();
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.fragment_1, mYwblFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mYwblFragment;
        }
        // 公文查看|协同办公
        else if (ConstState.GWCK.equals(selectFuncCode))
        {
            // String url = "http://10.20.108.12:8080/mipserv/Forwarding/sollen.jsp";
            String url = "http://218.92.115.51/portal/m/index_sso.jsp?userId=" + GlobalState.getInstance().getOpenId();
            
            Intent oaintent = new Intent(this, XtbgActivity.class);
            oaintent.putExtra("ISURL", true);
            oaintent.putExtra("url", url);
            oaintent.putExtra("title", mFunName);
            this.startActivity(oaintent);
        }
        // 平安港口
        else if (ConstState.SPJK.equals(selectFuncCode))
        {
            // Intent ywblIntent = new Intent(this, NotDevolopActivity.class);
            // ywblIntent.putExtra("funcId", mFuncId);
            // ywblIntent.putExtra("funName", mFunName);
            // ywblIntent.putExtra("parentsPath", mParentPath);
            // this.startActivity(ywblIntent);
            mPagkFragment = null;
            mPagkFragment = PagkFragment.newInstance(mParentPath, mFuncId, mFunName);
            mPagkFragment.setmTouchListener(this);
            
            mFragmentManager = this.getSupportFragmentManager();
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.fragment_1, mPagkFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mPagkFragment;
        }
        // 员工服务
        else if (ConstState.YGFW.equals(selectFuncCode))
        {
            // Intent ywblIntent = new Intent(this, NotDevolopActivity.class);
            // ywblIntent.putExtra("funcId", mFuncId);
            // ywblIntent.putExtra("funName", mFunName);
            // ywblIntent.putExtra("parentsPath", mParentPath);
            // this.startActivity(ywblIntent);
            mYgfwFragment = null;
            mYgfwFragment = YgfwFragment.newInstance(mParentPath, mFuncId, mFunName);
            mYgfwFragment.setmTouchListener(this);
            
            mFragmentManager = this.getSupportFragmentManager();
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.fragment_1, mYgfwFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mYgfwFragment;
        }
        // 员工社交
        else if (ConstState.YGSJ.equals(selectFuncCode))
        {
            // Intent ywblIntent = new Intent(this, NotDevolopActivity.class);
            // ywblIntent.putExtra("funcId", mFuncId);
            // ywblIntent.putExtra("funName", mFunName);
            // ywblIntent.putExtra("parentsPath", mParentPath);
            // this.startActivity(ywblIntent);
            mYgsjFragment = null;
            mYgsjFragment = YgsjFragment.newInstance(mParentPath, mFuncId, mFunName);
            mYgsjFragment.setmTouchListener(this);
            
            mFragmentManager = this.getSupportFragmentManager();
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.fragment_1, mYgsjFragment);
            
            fragmentTransaction.commit();
            
            mTopFragment = mYgsjFragment;
        }
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
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param keyCode
     * @param event
     * @return
     * @author wang_ling
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (mTopFragment != null)
            {
                mTopFragment.onKeyDown(keyCode);
                return true;
            }
            exitDialog();
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /** 退出应用对话框 **/
    private CustomDialog dialog;
    
    /**
     * 
     * 退出应用
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-3-27
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void exitDialog()
    {
        dialog =
            CustomDialog.newInstance(this.getResources().getString(R.string.main_exit_app), this.getResources()
                .getString(R.string.Login_Cancel), this.getResources().getString(R.string.Login_Confirm));
        dialog.show(this.getSupportFragmentManager(), "ExitDialog");
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
    
    /**
     * 二级布局，三级布局是否在滑动，在滑动则屏蔽点击事件
     */
    private boolean isTouch;
    
    /**
     * onTouchEvent触发时上一次的x位置
     */
    private int     originalX;
    
    /**
     * onTouchEvent触发时当前的x位置
     */
    private int     currentX;
    
    /**
     * onTouchEvent触发时当前的x位置与上一次x轴位置差值
     */
    private int     ditanceX;
    
    /**
     * onTouchEvent 第一次Downy位置
     */
    private int     originalY;
    
    /**
     * onTouchEvent触发时当前的y位置
     */
    private int     currentY;
    
    /**
     * onTouchEvent触发时当前的y位置与第一次y轴位置差值
     */
    private int     ditanceY;
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                LogUtil.i("hoperun", "onTouch  ACTION_DOWN");
                isTouch = false;
                originalX = (int)event.getRawX();
                
                originalY = (int)event.getRawY();
                
                return false;
                
            case MotionEvent.ACTION_MOVE:
                LogUtil.i("hoperun", "onTouch  ACTION_MOVE");
                
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
                    mFrameMarginFirs.leftMargin += ditanceX;
                    mFrameMarginFirs.rightMargin = -mFrameMarginFirs.leftMargin;
                    mFrameLayoutFirst.setLayoutParams(mFrameMarginFirs);
                    mFrameLayoutFirst.requestLayout();
                }
                return isTouch;
            case MotionEvent.ACTION_UP:
                LogUtil.i("hoperun", "onTouch  ACTION_UP");
                if (isTouch)
                {
                    // mAnimationRunnable =
                    // new AnimRunnable(2, mFirstLayouToLeft, mFrameMarginFirs, mMainHandler,
                    // AnimRunnable.SECOND_FRAGMENT);
                    // new Thread(mAnimationRunnable).start();
                    
                    if (mFrameMarginFirs.leftMargin < mFirstLayouToLeft)
                    {
                        mAnimationRunnable =
                            new AnimRunnable(2, mFirstLayouToLeft, mFrameMarginFirs, mMainHandler,
                                AnimRunnable.SECOND_FRAGMENT);
                        new Thread(mAnimationRunnable).start();
                    }
                    else if (mFrameMarginFirs.leftMargin > mFirstLayouToLeft
                        && mFrameMarginFirs.leftMargin <= (mFirstLayouToLeft + mFirstLayouToLeft))
                    {
                        mAnimationRunnable =
                            new AnimRunnable(-2, mFirstLayouToLeft, mFrameMarginFirs, mMainHandler,
                                AnimRunnable.SECOND_FRAGMENT);
                        new Thread(mAnimationRunnable).start();
                    }
                    else if (mFrameMarginFirs.leftMargin > (mFirstLayouToLeft + mFirstLayouToLeft))
                    {
                        mAnimationRunnable =
                            new AnimRunnable(8, GlobalState.getInstance().getmScreen_With(), mFrameMarginFirs,
                                mMainHandler, AnimRunnable.SECOND_FRAGMENT);
                        mMenuAdapter.setmSelectedPosition(-1);
                        mMenuAdapter.notifyDataSetChanged();
                        new Thread(mAnimationRunnable).start();
                        selectFuncCode = "";
                        mTempRl.setVisibility(View.GONE);
                        
                        mTopFragment = null;
                    }
                    // else if (mFrameMarginFirs.leftMargin < -(mFirstLayouToLeft + mFirstLayouToLeft))
                    // {
                    // mAnimationRunnable =
                    // new AnimRunnable(-2, -GlobalState.getInstance().getmScreen_With(), mFrameMarginFirs,
                    // mMainHandler, AnimRunnable.SECOND_FRAGMENT);
                    // mMenuAdapter.setmSelectedPosition(-1);
                    // mMenuAdapter.notifyDataSetChanged();
                    // new Thread(mAnimationRunnable).start();
                    // onSecondFragmentClose();
                    // }
                }
                return isTouch;
            default:
                return false;
        }
    }
    
    @Override
    public void onOfficialFragmentSelected(int index, GWDocModule mModlue, String path)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onOfficialFragmentCloseSelected()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onSchedulePlanSelected(int index, ScheduleInfo list_info, String date)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onScheduleTimeFragmentCloseSelected()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onScheduleChangedListener()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onSecondFragmentClose()
    {
        
        mTopFragment = null;
        mTempRl.setVisibility(View.GONE);
        mFrameLayoutFirst.startAnimation(mRightOut);
        selectFuncCode = "";
        mFrameLayoutFirst.removeAllViews();
        // closeTheSecondLevelLayout();
    }
    
    private void closeTheSecondLevelLayout()
    {
        mAnimationRunnable =
            new AnimRunnable(2, GlobalState.getInstance().getmScreen_With(), mFrameMarginFirs, mMainHandler,
                AnimRunnable.SECOND_FRAGMENT);
        new Thread(mAnimationRunnable).start();
        selectFuncCode = "";
        mMenuAdapter.setmSelectedPosition(-1);
        mMenuAdapter.notifyDataSetChanged();
        mTopFragment = null;
        mTempRl.setVisibility(View.GONE);
    }
    
    @Override
    public void onPersonSetBind()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onPersonSetHeader(Bitmap bitmap)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onNetTVShowFullScreen(boolean isFull)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onVideoMonitorFragmentSelected(VideoListItem item)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onSendGetUnReadCount(String funId, String funcode)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onCloseHomeViewFragment()
    {
        // TODO Auto-generated method stub
        
    }
    
}
