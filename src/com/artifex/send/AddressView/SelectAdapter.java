/*
 * File name:  SelectAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-10-16
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.send.AddressView;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.artifex.send.AddressView.companyAddress.AddressNode;
import com.hoperun.miplygphone.R;

/**
 * 已选择联系人的adapter
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-10-16]
 */
public class SelectAdapter extends BaseAdapter
{
    private List<AddressNode> selectedNode;
    
    private Context           mContext;
    
    public SelectAdapter(Context context)
    {
        super();
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }
    
    public void setSelectedList(List<AddressNode> list)
    {
        selectedNode = list;
        this.notifyDataSetChanged();
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return selectedNode == null ? 0 : selectedNode.size();
    }
    
    @Override
    public Object getItem(int arg0)
    {
        // TODO Auto-generated method stub
        if (selectedNode != null)
        {
            return selectedNode.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.companyselected, null);
            vi = new ViewItem();
            vi.mName = (TextView)view.findViewById(R.id.address_select_name_text);
            vi.mCompany = (TextView)view.findViewById(R.id.address_select_unit_text);
            view.setTag(vi);
        }
        else
        {
            vi = (ViewItem)view.getTag();
            if (vi == null)
                System.out.println();
        }
        
        AddressNode node = selectedNode.get(position);
        if (node != null)
        {
            vi.mName.setText(node.getName());
            if (null != node.getParentName())
            {
                vi.mCompany.setText(node.getParentName());
            }
            else
            {
                vi.mCompany.setText("");
            }
        }
        return view;
    }
    
    public class ViewItem
    {
        private TextView mName;
        
        private TextView mCompany;
    }
    
}
