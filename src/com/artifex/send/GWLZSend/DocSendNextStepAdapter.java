/*
 * File name:  NextStepAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-10-16
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.send.GWLZSend;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.miplygphone.R;

/**
 * 下一步步骤的adapter
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-10-16]
 */
public class DocSendNextStepAdapter extends BaseAdapter
{
    List<PDFNextStepListEntity> stepList;
    
    private Context             mContext;
    
    public DocSendNextStepAdapter(Context context, List<PDFNextStepListEntity> list)
    {
        super();
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.stepList = list;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return stepList == null ? 0 : stepList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        if (stepList != null)
        {
            return stepList.get(position);
        }
        else
        {
            return null;
        }
        
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        ViewItem vi = null;
        if (view == null)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.nextsteplist, null);
            vi = new ViewItem();
            vi.mName = (TextView)view.findViewById(R.id.nextsteplist_namet);
            view.setTag(vi);
        }
        else
        {
            vi = (ViewItem)view.getTag();
            if (vi == null)
                System.out.println();
        }
        
        PDFNextStepListEntity entity = stepList.get(position);
        if (entity != null)
        {
            vi.mName.setText(entity.getmNextStepName());
        }
        return view;
    }
    
    public class ViewItem
    {
        private ImageView mIcon;
        
        private TextView  mName;
    }
    
}
