/*
 * File name:  FunctionMainView.java
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

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hoperun.manager.adpter.MenuAdapter;
import com.hoperun.manager.components.HeadView;
import com.hoperun.manager.components.WaitDialog;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.exceptions.MIPException;
import com.hoperun.mip.sqlUtils.SQLCreator.DBHandler;
import com.hoperun.mip.sqlUtils.Table.TFUNSIONPOWER;
import com.hoperun.mip.utils.MessageSQLIdConstants;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityModule.Login.LoginModule;
import com.hoperun.mipmanager.model.entityModule.nettv.VideoListItem;
import com.hoperun.mipmanager.model.entityModule.offical.GWDocModule;
import com.hoperun.mipmanager.model.entityModule.schedule.ScheduleInfo;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.project.ui.baseui.CustomBaseMainView;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;
import com.hoperun.project.ui.baseui.baseInterface.IFragmentToMainActivityListen;
import com.hoperun.project.ui.baseui.fragment.PMIPBaseFragment;
import com.hoperun.project.ui.xwzx.XwzxFirstFragment;
import com.hoperun.project.ui.xwzx.XwzxSecondFragment;

/**
 * 功能列表一级菜单
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-24]
 */
public class FunctionMainView extends CustomBaseMainView implements OnItemClickListener, IFragmentToMainActivityListen,
    OnTouchListener
{
    /** 一级列表数据 **/
    private ArrayList<LoginModule> modules;
    
    /**
     * 一级布局列表
     */
    private MenuAdapter            mMenuAdapter;
    
    private WaitDialog             waitDialog;
    
    /**
     * 布局1，用于存放下一级页面
     */
    private FrameLayout            mFramLayout1;
    
    /**
     * 布局2，用于存放下下一级页面
     */
    private FrameLayout            mFramLayout2;
    
    /**
     * Fragment 管理器
     */
    private FragmentManager        mFragmentManager;
    
    private FragmentTransaction    fragmentTransaction;
    
    /**
     * 第一级Fragment置于顶层时的临时变量
     */
    private PMIPBaseFragment       mTopFragment;
    
    // /**
    // * 第二级Fragment置于顶层时的临时变量
    // */
    // private PMIPBaseFragment mTopFragmentSecond;
    
    /**
     * 选择的二级菜单的ID，（通知公告，审批管理等等）
     */
    private int                    currentSelectID       = 0;
    
    private Animation              mRightIn;
    
    private RelativeLayout         mNoDataRl;
    
    /**
     * 主栏目布局
     */
    private ListView               mLvMenu;
    
    /** 选择哪个功能 **/
    private String                 selectFuncCode        = "";
    
    /**
     * 点击功能列表中某功能的功能ID；
     */
    private String                 mFuncId               = "";
    
    /**
     * 点击功能列表中某功能的功能名称；
     */
    private String                 mFunName              = "";
    
    /**
     * 点击功能列表中某功能的功能的根路径
     */
    private String                 mParentPath           = "";
    
    /**
     * 点击功能列表中某共嫩的功能的搜索关键字
     */
    private String                 mSearchKeywodrs       = "";
    
    /** 新闻中心一级菜单 **/
    private XwzxFirstFragment      mXwzxFirstFragment;
    
    /** 新闻中心二级菜单 **/
    private XwzxSecondFragment     mXwzxSecondFragment;
    
    /** 滑动效果 **/
    private View.OnTouchListener   mViewLandscapeSlideListener;
    
    private boolean                isFragmentCanMove;
    
    /** 二级布局动画 **/
    private Animation              mSecondRightInAnim;
    
    /** 三级布局动画 **/
    private Animation              mThreeRightInAnim;
    
    /**
     * 是否第二层布局滑动
     */
    private boolean                isSecondLayoutonTouch = true;
    
    /** 公文流转，点击一级页面上选项，弹出 二级选项时的参数 **/
    private GWDocModule            mModule               = null;
    
    /** 公文流转，点击一级页面上选项，弹出 二级选项时的参数，文件路径 **/
    private String                 path2;
    
    /**
     * <默认构造函数>
     */
    public FunctionMainView(Activity activity)
    {
        super(activity);
        mActivity = (PMIPBaseActivity)activity;
        initView();
        initData();
        
    }
    
    /**
     * 重载方法
     * 
     * @param keyId
     * @return
     * @author wang_ling
     */
    @Override
    public boolean onKeyDown(int keyId)
    {
        if (keyId == KeyEvent.KEYCODE_BACK)
        {
            if (mTopFragment != null)
            {
                mTopFragment.onKeyDown(keyId);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void animationOver()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param mheadview
     * @param title
     * @author wang_ling
     */
    @Override
    public void setHeadViewValue(HeadView mheadview, String title)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param bd
     * @author wang_ling
     */
    @Override
    public void onOpenNextFragment(Bundle bd)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param bd
     * @author wang_ling
     */
    @Override
    public void onCloseThisFragment(Bundle bd)
    {
        if (mTopFragment != null)
        {
            mFramLayout2.removeAllViews();
            mTopFragment = null;
            
            mFramLayout1.bringToFront();
            mFramLayout1.invalidate();
            
        }
        else if (mTopFragment != null)
        {
            mFramLayout1.removeAllViews();
            mTopFragment = null;
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
        // if (selectFuncCode != null && selectFuncCode.equals((String)mMenuAdapter.getItem(arg2)))
        // {
        // return;
        // }
        // mMenuAdapter.setmSelectedPosition(arg2);
        //
        // mMenuAdapter.notifyDataSetChanged();
        if (!isSecondLayoutonTouch)
        {
            releaseTheThirdFramgeLayout();
        }
        
        releaseTheSecondFramgeLayout();
        
        selectFuncCode = (String)mMenuAdapter.getItem(arg2);
        
        mFuncId = (String)mMenuAdapter.getFuncId(arg2);
        
        mFunName = (String)mMenuAdapter.getFunName(arg2);
        
        mParentPath = openId + "/" + selectFuncCode;
        mSearchKeywodrs = (String)mMenuAdapter.getSearchKeyWords(arg2);
        
        // showTheSecondLayoutFragment();
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
        
        if (!isSecondLayoutonTouch)
        {
            releaseTheThirdFramgeLayout();
        }
        
        releaseTheSecondFramgeLayout();
        isSecondLayoutonTouch = true;
        
        mFramLayout2.setBackgroundResource(R.color.transparent);
        
        mFramLayout1.removeAllViews();
        
        mFramLayout1.setVisibility(View.VISIBLE);
        mFramLayout1.bringToFront();
        
        // mSecondRightInAnim结束之后会将fragment调用其中的animationOver方法，为了保证动画滑动的顺畅性,不要再动画
        // 过程中加载数据
        mFramLayout1.startAnimation(mSecondRightInAnim);
        mFramLayout1.setOnTouchListener(mViewLandscapeSlideListener);
        
        showTheSecondLayoutFragment();
        
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
        
        // 新闻中心
        if (ConstState.XWZX.equals(selectFuncCode))
        {
            mXwzxFirstFragment = null;
            mXwzxFirstFragment = XwzxFirstFragment.newInstance(mFuncId, mFunName, mParentPath);
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.fragment_1, mXwzxFirstFragment);
            
            fragmentTransaction.commit();
            mXwzxFirstFragment.setmFragmentTMainActivityListener(this);
            
            mTopFragment = mXwzxFirstFragment;
        }
    }
    
    private void releaseTheThirdFramgeLayout()
    {
        mTopFragment = null;
        // 新闻中心
        if (ConstState.XWZX.equals(selectFuncCode))
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
        // 新闻中心
        if (ConstState.XWZX.equals(selectFuncCode))
        {
            if (null != mXwzxFirstFragment)
            {
                mXwzxFirstFragment = null;
            }
            
        }
    }
    
    //
    // private void showTheSecondLayoutFragment()
    // {
    // mTopFragment = null;
    // mTopFragment = null;
    // mFramLayout1.removeAllViews();
    // mFramLayout2.removeAllViews();
    // // 新闻中心
    // if (ConstState.XWZX.equals(selectFuncCode))
    // {
    // // mXwzxFirstFragment = null;
    // // mXwzxFirstFragment = XwzxFirstFragment.newInstance(mFuncId, mFunName, mParentPath);
    // //
    // // fragmentTransaction = mFragmentManager.beginTransaction();
    // //
    // // fragmentTransaction.replace(R.id.main_second_layout, mXwzxFirstFragment);
    // //
    // // fragmentTransaction.commit();
    // //
    // // mTopFragment = mXwzxFirstFragment;
    // Intent xwzxIntent = new Intent(mActivity, XwzxFirstActivity.class);
    // xwzxIntent.putExtra("funcId", mFuncId);
    // xwzxIntent.putExtra("funName", mFunName);
    // xwzxIntent.putExtra("parentsPath", mParentPath);
    // mActivity.startActivity(xwzxIntent);
    // }
    // }
    
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
    
    // protected OnTouchListener mViewLandscapeSlideListener = new OnTouchListener()
    // {
    //
    // @Override
    // public boolean onTouch(View v, MotionEvent event)
    // {
    // // TODO Auto-generated method stub
    // return false;
    // }
    // };
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void initView()
    {
        LayoutInflater.from(mActivity).inflate(R.layout.function_main, this, true);
        
        mHeadView = (HeadView)findViewById(R.id.mobile_header);
        
        mNoDataRl = (RelativeLayout)findViewById(R.id.no_datarl);
        mLvMenu = (ListView)findViewById(R.id.lv_menu);
        mFramLayout1 = (FrameLayout)findViewById(R.id.fragment_1);
        mFramLayout2 = (FrameLayout)findViewById(R.id.fragment_2);
        mViewLandscapeSlideListener = this;
        mFramLayout1.setOnTouchListener(mViewLandscapeSlideListener);
        // mFramLayout2.setOnTouchListener(mViewLandscapeSlideListener);
        mLvMenu.setOnItemClickListener(this);
    }
    
    /** 用户id **/
    private String openId;
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void initData()
    {
        waitDialog = WaitDialog.newInstance();
        mFragmentManager = mActivity.getSupportFragmentManager();
        openId = GlobalState.getInstance().getOpenId();
        // for (int i = 0; i < TempStrings.YDZWUncount.length; i++)
        // {
        // YDZWModuleEntity entity = new YDZWModuleEntity();
        // entity.setCount(TempStrings.YDZWUncount[i]);
        // entity.setFlag(TempStrings.YDZWTAG[i]);
        // entity.setName(TempStrings.YDZWName[i]);
        // entity.setIcon_id(TempStrings.YDZWICONID[i]);
        // modules.add(entity);
        // }
        // mTongZhiGGentitys =
        // DataBaseWriteUtil.queryAllListItems(mActivity.getContentResolver(),
        // TZGGInfoTable.USER_ID + "=?",
        // new String[] {GlobalState.getInstance().getUserId(),},
        // null);
        
        mSecondRightInAnim = AnimationUtils.loadAnimation(mActivity, R.anim.fromrightin);
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
                if (mTopFragment != null)
                {
                    mTopFragment.animationOver();
                }
            }
        });
        
        mThreeRightInAnim = AnimationUtils.loadAnimation(mActivity, R.anim.fromrightin);
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
        
        modules = getModuleFromTable();
        MyComparator myComparator = new MyComparator();
        Collections.sort(modules, myComparator);
        
        mMenuAdapter = new MenuAdapter(mActivity, modules);
        
        mLvMenu.setAdapter(mMenuAdapter);
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void initListener()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void backToInitState()
    {
        // TODO Auto-generated method stub
        
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
    
    /**
     * 重载方法
     * 
     * @param v
     * @param event
     * @return
     * @author wang_ling
     */
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * 重载方法
     * 
     * @param index
     * @param mModlue
     * @param path
     * @author wang_ling
     */
    @Override
    public void onOfficialFragmentSelected(int index, GWDocModule mModlue, String path)
    {
        this.mModule = mModlue;
        this.path2 = path;
        isSecondLayoutonTouch = false;
        
        mFramLayout2.removeAllViews();
        
        showTheThirdLevelLayout();
        
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
        mFramLayout1.setVisibility(View.VISIBLE);
        mFramLayout1.setOnTouchListener(mViewLandscapeSlideListener);
        
        // mAnimationRunnable =
        // new AnimRunnable(-2, mSecondToLeftAfterThird, mSecondLevelMarginLayoutParams, mMainHandler,
        // AnimRunnable.SECOND_FRAGMENT);
        // new Thread(mAnimationRunnable).start();
        
        mFramLayout2.setVisibility(View.VISIBLE);
        mFramLayout2.bringToFront();
        // mThreeRightInAnim结束之后会将fragment中的animationOver方法，为了保证动画滑动的顺畅性
        mFramLayout2.startAnimation(mThreeRightInAnim);
        mFramLayout2.setOnTouchListener(mViewLandscapeSlideListener);
        
        showTheThirdLayoutFragment();
        
    }
    
    private void showTheThirdLayoutFragment()
    {
        mTopFragment = null;
        // 新闻中心
        if (ConstState.XWZX.equals(selectFuncCode))
        {
            mXwzxSecondFragment = null;
            mXwzxSecondFragment =
                XwzxSecondFragment.newInstance(mModule.getFunccode(),
                    mModule.getSearchkeywords(),
                    path2,
                    mModule.getFuncName());
            
            fragmentTransaction = mFragmentManager.beginTransaction();
            
            fragmentTransaction.replace(R.id.fragment_2, mXwzxSecondFragment);
            
            fragmentTransaction.commit();
            mXwzxSecondFragment.setmFragmentTMainActivityListener(this);
            mTopFragment = mXwzxSecondFragment;
            
        }
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onOfficialFragmentCloseSelected()
    {
        // TODO Auto-generated method stub
        closeTheThirdLevelLayout();
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
        if (mFramLayout2 != null)
        {
            mFramLayout2.removeAllViews();
        }
        
        // 新闻中心
        if (ConstState.XWZX.equals(selectFuncCode))
        {
            if (mXwzxFirstFragment != null)
            {
                mXwzxFirstFragment.theThirdFragmentClose();
                mTopFragment = mXwzxFirstFragment;
            }
        }
    }
    
    /**
     * 重载方法
     * 
     * @param index
     * @param list_info
     * @param date
     * @author wang_ling
     */
    @Override
    public void onSchedulePlanSelected(int index, ScheduleInfo list_info, String date)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onScheduleTimeFragmentCloseSelected()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onScheduleChangedListener()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onSecondFragmentClose()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onPersonSetBind()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param bitmap
     * @author wang_ling
     */
    @Override
    public void onPersonSetHeader(Bitmap bitmap)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param isFull
     * @author wang_ling
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
     * @author wang_ling
     */
    @Override
    public void onVideoMonitorFragmentSelected(VideoListItem item)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param funId
     * @param funcode
     * @author wang_ling
     */
    @Override
    public void onSendGetUnReadCount(String funId, String funcode)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void onCloseHomeViewFragment()
    {
        // TODO Auto-generated method stub
        closeTheSecondLevelLayout();
    }
    
    private void closeTheSecondLevelLayout()
    {
        if (mFramLayout1 != null)
        {
            mFramLayout1.removeAllViews();
        }
        if (!(mFramLayout2.getVisibility() == View.VISIBLE))
        {
            // mSecondLevelMarginLayoutParams.leftMargin = mScreenWidth;
            // mSecondLevelLayout.setLayoutParams(mSecondLevelMarginLayoutParams);
            // mAnimationRunnable =
            // new AnimRunnable(10, mScreenWidth, mSecondLevelMarginLayoutParams, mMainHandler,
            // AnimRunnable.SECOND_FRAGMENT);
            // new Thread(mAnimationRunnable).start();
            selectFuncCode = "-1";
            
            mMenuAdapter.setmSelectedPosition(-1);
            mMenuAdapter.notifyDataSetChanged();
            
            mTopFragment = null;
        }
    }
}
