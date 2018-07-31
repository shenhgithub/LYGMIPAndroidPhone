package com.hoperun.project.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.project.ui.baseui.fragment.PMIPCustomFragment;

/**
 * 
 * 公文流转，一级菜单
 * 
 * @Description<功能详细描述>
 * 
 * @author shen_feng
 * @Version [版本号, 2013-9-12]
 */
public class TestlFragment extends PMIPCustomFragment
{
    Activity mActivity;
    
    /**
     * 重载方法
     * 
     * @param requestType 请求id
     * @param objHeader 返回的头
     * @param obj 返回的body
     * @param error 是否返回成功
     * @param errorCode 错误码
     * @author shen_feng
     */
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object obj, boolean error, int errorCode)
    {
        
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.mActivity = getActivity();
    }
    
    /**
     * 
     * 实例化
     * 
     * @Description 实例化
     * 
     * @param funcId 功能id
     * @param parentsPath 路径
     * @return fragment
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static TestlFragment newInstance()
    {
        TestlFragment details = new TestlFragment();
        return details;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // if (container == null)
        // {
        // return null;
        // }
        // View v = inflater.inflate(R.layout.testfragment, null);
        //
        // return v;
        View v = super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }
    
    /**
     * 重载方法
     * 
     * @author shen_feng
     */
    @Override
    public void onResume()
    {
        super.onResume();
    }
    
    /**
     * 重载方法
     * 
     * @author shen_feng
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }
    
    /**
     * 重载方法
     * 
     * @param keyId
     * @return
     * @author ren_qiujing
     */
    @Override
    public boolean onKeyDown(int keyId)
    {
        // TODO Auto-generated method stub
        if (keyId == KeyEvent.KEYCODE_BACK)
        {
            closeThisFragment();
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void animationOver()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void closeThisFragment()
    {
        // TODO Auto-generated method stub
        if (mFragmentTMainActivityListener != null)
        {
            mFragmentTMainActivityListener.onSecondFragmentClose();
        }
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void theThirdFragmentClose()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void customThisFragmentView()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void disPlayListView()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param keyWords
     * @return
     * @author ren_qiujing
     */
    @Override
    public View getSearchView(String keyWords, String typeId)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 重载方法
     * 
     * @param id
     * @author ren_qiujing
     */
    @Override
    public void readViewBack(String id)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void setViewResult()
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 重载方法
     * 
     * @param view
     * @param keyWords
     * @return
     * @author ren_qiujing
     */
    @Override
    public Object initOfficalListView(Object view, String keyWords, String typeId)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 重载方法
     * 
     * @param id
     * @author ren_qiujing
     */
    @Override
    public void clickRadioButton(int id)
    {
        // TODO Auto-generated method stub
        switch (id)
        {
            case R.id.fragment_first_ll:
                mUnReadStatus = ConstState.UNReadDOCLIST;
                break;
            case R.id.fragment_second_ll:
                mUnReadStatus = ConstState.HasReadDOCLIST;
                break;
            case R.id.fragment_third_ll:
                break;
            
            default:
                break;
        }
    }
    
}
