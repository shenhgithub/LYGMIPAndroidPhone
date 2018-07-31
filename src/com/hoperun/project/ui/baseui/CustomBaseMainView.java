package com.hoperun.project.ui.baseui;

import android.app.Activity;
import android.widget.RelativeLayout;

import com.hoperun.manager.components.HeadView;
import com.hoperun.manager.components.CustomHandler.CustomHanler;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.project.ui.baseui.baseInterface.IFragmentToMainViewListen;
import com.hoperun.project.ui.baseui.baseInterface.IMainActivityToFragmentListen;

abstract public class CustomBaseMainView extends RelativeLayout implements IMainActivityToFragmentListen,
    IFragmentToMainViewListen
{
    
    protected PMIPBaseActivity mActivity;
    
    protected HeadView         mHeadView;
    
    protected CustomHanler     mHandler = new CustomHanler()
                                        {
                                            
                                            @Override
                                            public void PostHandle(int requestType, Object objHeader, Object objBody,
                                                boolean error, int errorCode)
                                            {
                                                if (mActivity != null && !mActivity.isActivityDestroyed())
                                                {
                                                    onPostHandle(requestType, objHeader, objBody, error, errorCode);
                                                }
                                                else
                                                {
                                                    LogUtil.i("", "This Activity is destroyed!");
                                                }
                                            }
                                        };
    
    public PMIPBaseActivity getmActivity()
    {
        return mActivity;
    }
    
    public void setmActivity(PMIPBaseActivity mActivity)
    {
        this.mActivity = mActivity;
    }
    
    public HeadView getmHeadView()
    {
        return mHeadView;
    }
    
    public void setmHeadView(HeadView mHeadView)
    {
        this.mHeadView = mHeadView;
    }
    
    abstract public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode);
    
    abstract public void initView();
    
    abstract public void initData();
    
    abstract public void initListener();
    
    /**
     * 该页面返回初始的页面
     */
    abstract public void backToInitState();
    
    public CustomBaseMainView(Activity activity)
    {
        super(activity);
        
    }
}
