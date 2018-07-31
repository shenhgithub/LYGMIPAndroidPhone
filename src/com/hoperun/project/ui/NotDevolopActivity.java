package com.hoperun.project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.hoperun.manager.components.HeadView;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;

public class NotDevolopActivity extends PMIPBaseActivity
{
    
    /**
     * 公共的HeadView头
     */
    protected HeadView mHeadView;
    
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.notdevoloplayout);
        
        Intent intent = getIntent();
        String title = intent.getStringExtra("funName");
        
        mHeadView = (HeadView)findViewById(R.id.mobile_header);
        
        mHeadView.setTitle(title);
        
        mHeadView.setLeftVisiable(true);
        mHeadView.setLeftIcon(R.drawable.selector_back_button);
        mHeadView.setLeftOnclickLisen(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                finish();
            }
        });
        
        mHeadView.setRightVisiable(false);
        mHeadView.setRightIcon(-1);
        mHeadView.setRightOnclickLisen(null);
        
    }
    
}
