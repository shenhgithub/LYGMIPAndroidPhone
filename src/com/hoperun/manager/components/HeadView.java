package com.hoperun.manager.components;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoperun.miplygphone.R;

public class HeadView extends RelativeLayout
{
    
    private FragmentActivity mActivity;
    
    private RelativeLayout   mLeftRL;
    
    private RelativeLayout   mLeftRLClick;
    
    private ImageView        mLeftIcon;
    
    private RelativeLayout   mRightRl;
    
    private RelativeLayout   mRightRlClick;
    
    private ImageView        mRigthIcon;
    
    private TextView         mTitle;
    
    /**
     * @param context
     */
    public HeadView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
        mActivity = (FragmentActivity)context;
        initView();
    }
    
    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public HeadView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        mActivity = (FragmentActivity)context;
        initView();
    }
    
    /**
     * @param context
     * @param attrs
     */
    public HeadView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mActivity = (FragmentActivity)context;
        initView();
    }
    
    public void initView()
    {
        LayoutInflater.from(mActivity).inflate(R.layout.main_header, this, true);
        
        mLeftRL = (RelativeLayout)findViewById(R.id.main_left);
        mLeftRLClick = (RelativeLayout)findViewById(R.id.main_left_rl);
        mLeftIcon = (ImageView)findViewById(R.id.main_left_icon);
        
        mRightRl = (RelativeLayout)findViewById(R.id.main_right);
        mRightRlClick = (RelativeLayout)findViewById(R.id.main_right_rl);
        mRigthIcon = (ImageView)findViewById(R.id.main_right_icon);
        
        mTitle = (TextView)findViewById(R.id.header_title);
    }
    
    public void setTitle(String title)
    {
        mTitle.setText(title);
    }
    
    public String getTitle()
    {
        return mTitle.getEditableText().toString();
    }
    
    public void setLeftVisiable(boolean flag)
    {
        if (flag)
        {
            mLeftRL.setVisibility(View.VISIBLE);
        }
        else
        {
            mLeftRL.setVisibility(View.GONE);
        }
    }
    
    public void setRightVisiable(boolean flag)
    {
        if (flag)
        {
            mRightRl.setVisibility(View.VISIBLE);
        }
        else
        {
            mRightRl.setVisibility(View.GONE);
        }
    }
    
    public void setLeftIcon(int id)
    {
        if (id != -1)
        {
            mLeftIcon.setBackgroundResource(id);
        }
    }
    
    public void setRightIcon(int id)
    {
        if (id != -1)
        {
            mRigthIcon.setBackgroundResource(id);
        }
    }
    
    public void setLeftOnclickLisen(OnClickListener listen)
    {
        if (listen != null)
            mLeftRLClick.setOnClickListener(listen);
    }
    
    public void setRightOnclickLisen(OnClickListener listen)
    {
        if (listen != null)
            mRightRlClick.setOnClickListener(listen);
    }
    
    /**
     * 初始化head view
     * 
     * @param view
     */
    public static void initHeadView(HeadView view, String mTitle, boolean leftVisiable, boolean rightVisiable,
        int leftIcon, int righIcon, OnClickListener leftOnclClickListener, OnClickListener rightOnclickListener)
    {
        view.setTitle(mTitle);
        
        view.setLeftVisiable(leftVisiable);
        view.setLeftIcon(leftIcon);
        view.setLeftOnclickLisen(leftOnclClickListener);
        
        view.setRightVisiable(rightVisiable);
        view.setRightIcon(righIcon);
        view.setRightOnclickLisen(rightOnclickListener);
    }
    
    public int getLeftRLWith()
    {
        if (mLeftRL != null)
        {
            
            return mLeftRL.getWidth();
        }
        return 0;
    }
    
}
