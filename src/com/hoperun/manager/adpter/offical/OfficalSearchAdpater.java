/*
 * File name:  OfficalSearchAdpater.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-11-9
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.offical;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-11-9]
 */
public class OfficalSearchAdpater extends PMIPBaseAdapter
{
    
    private List<String> mfunnels = new ArrayList<String>();
    
    private Context      mContext;
    
    private int          mSelect  = -1;
    
    private int          mHeight  = -1;
    
    public OfficalSearchAdpater(Context context, List<String> list, int height)
    {
        super();
        // TODO Auto-generated constructor stub
        mContext = context;
        mfunnels = list;
        mHeight = height;
    }
    
    public int getmSelect()
    {
        return mSelect;
    }
    
    public void setmSelect(int mSelect)
    {
        this.mSelect = mSelect;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mfunnels.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mfunnels.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHoldSearch viewSearchHold;
        if (convertView == null)
        {
            
            viewSearchHold = new ViewHoldSearch();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.text_layout, null);
            convertView.setBackgroundResource(R.drawable.selector_funnel);
            
            viewSearchHold.mName = (TextView)convertView.findViewById(R.id.funnel_tv);
            viewSearchHold.mName.setHeight(mHeight);
            convertView.setTag(viewSearchHold);
        }
        else
        {
            viewSearchHold = (ViewHoldSearch)convertView.getTag();
            
        }
        
        viewSearchHold.mName.setText(mfunnels.get(position));
        
        return convertView;
    }
    
    static class ViewHoldSearch
    {
        
        TextView mName;
        
    }
    
}
