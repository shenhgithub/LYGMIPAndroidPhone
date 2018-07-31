/*
 * File name:  PMIPBaseFragment.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-9-26
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.baseui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.hoperun.manager.components.CustomHandler.CustomHanler;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.view.MIPBaseFragment;
import com.hoperun.project.ui.baseui.baseInterface.IFragmentToMainActivityListen;
import com.hoperun.project.ui.baseui.baseInterface.IMainActivityToFragmentListen;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-9-26]
 */
public abstract class PMIPBaseFragment extends MIPBaseFragment implements IMainActivityToFragmentListen
{
    /**
     * 监听变量，供每个Fragment有事件向MainActivity中传递
     */
    protected IFragmentToMainActivityListen mFragmentTMainActivityListener;
    
    public IFragmentToMainActivityListen getmFragmentTMainActivityListener()
    {
        return mFragmentTMainActivityListener;
    }
    
    public void setmFragmentTMainActivityListener(IFragmentToMainActivityListen mFragmentTMainActivityListener)
    {
        this.mFragmentTMainActivityListener = mFragmentTMainActivityListener;
    }
    
    /**
     * 触摸滑动监听
     */
    protected OnTouchListener mViewLandscapeSlideListener = new OnTouchListener()
                                                          {
                                                              
                                                              @Override
                                                              public boolean onTouch(View v, MotionEvent event)
                                                              {
                                                                  // TODO Auto-generated method stub
                                                                  return false;
                                                              }
                                                          };
    
    protected boolean         isFragementDestroyed        = false;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public void onAttach(Activity activity)
    {
        // TODO Auto-generated method stub
        try
        {
            mFragmentTMainActivityListener = (IFragmentToMainActivityListen)activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + "must implement IFragmentToMainActivityListen");
        }
        
        try
        {
            mViewLandscapeSlideListener = (OnTouchListener)activity;
        }
        catch (ClassCastException e)
        {
            // TODO: handle exception
            throw new ClassCastException(activity.toString() + "must implement OnTouchListener");
        }
        
        super.onAttach(activity);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
    }
    
    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
    }
    
    @Override
    public void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
    }
    
    @Override
    public void onStop()
    {
        // TODO Auto-generated method stub
        super.onStop();
    }
    
    @Override
    public void startActivity(Intent intent)
    {
        // TODO Auto-generated method stub
        super.startActivity(intent);
    }
    
    protected CustomHanler mHandler = new CustomHanler()
                                    {
                                        
                                        @Override
                                        public void PostHandle(int requestType, Object objHeader, Object objBody,
                                            boolean error, int errorCode)
                                        {
                                            if (!isFragementDestroyed)
                                            {
                                                onPostHandle(requestType, objHeader, objBody, error, errorCode);
                                            }
                                            else
                                            {
                                                LogUtil.i("", "This fragment is destroyed!");
                                            }
                                        }
                                        
                                    };
    
    abstract public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode);
    
    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        isFragementDestroyed = true;
        super.onDestroy();
    }
    
    public void setmTouchListener(OnTouchListener listener)
    {
        mViewLandscapeSlideListener = listener;
    }
    
}
