/*
 * File name:  AddressTreeAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-10-15
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.send.AddressView.companyAddress;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.miplygphone.R;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-10-15]
 */
public class AddressTreeAdapter extends BaseAdapter
{
    private Context                      mContext;
    
    private AddressTreeAdapter           mAddressAdapter;
    
    private HashMap<String, AddressNode> mMapNodes = new HashMap<String, AddressNode>();
    
    private List<AddressNode>            allNodes;
    
    public AddressTreeAdapter(Context context)
    {
        super();
        this.mContext = context;
        mAddressAdapter = this;
        // TODO Auto-generated constructor stub
    }
    
    public void setAllNodes(List<AddressNode> nodes)
    {
        if (allNodes != null)
        {
            allNodes.clear();
            allNodes = null;
            
        }
        
        allNodes = nodes;
        
        if (mMapNodes != null)
        {
            mMapNodes.clear();
            
            if (allNodes != null)
            {
                for (int i = 0; i < allNodes.size(); i++)
                {
                    mMapNodes.put(allNodes.get(i).getCurId(), allNodes.get(i));
                }
            }
        }
        
        this.notifyDataSetChanged();
    }
    
    public List<AddressNode> getAllNodes()
    {
        return allNodes;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        if (allNodes != null)
        {
            return allNodes.size();
        }
        else
        {
            return 0;
        }
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return allNodes.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        ViewItem vi = null;
        if (view == null)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.address_list_item, null);
            vi = new ViewItem();
            vi.mCheckBox = (CheckBox)view.findViewById(R.id.address_checkbox);
            vi.mIcon = (ImageView)view.findViewById(R.id.address_icon);
            vi.mName = (TextView)view.findViewById(R.id.address_name_text);
            vi.mArrow = (ImageView)view.findViewById(R.id.address_arrow);
            
            vi.mCheckBox.setClickable(false);
            view.setTag(vi);
        }
        else
        {
            vi = (ViewItem)view.getTag();
            if (vi == null)
                System.out.println();
        }
        
        AddressNode node = allNodes.get(position);
        
        if (node != null)
        {
            if (vi == null || vi.mCheckBox == null)
                System.out.println();
            // 叶节点不显示展开收缩图标
            if (node.isDept())
            {
                vi.mIcon.setImageResource(R.drawable.contant_icon_3);
                vi.mArrow.setVisibility(View.VISIBLE);
                vi.mCheckBox.setVisibility(View.INVISIBLE);
            }
            else
            {
                vi.mIcon.setImageResource(R.drawable.pop_1);
                vi.mArrow.setVisibility(View.INVISIBLE);
                vi.mCheckBox.setVisibility(View.VISIBLE);
                
                vi.mCheckBox.setChecked(node.isChecked());
            }
            
            // 显示文本
            vi.mName.setText(node.getName());
            // 控制缩进
            if (node.isDept())
            {
                view.setPadding(0, 3, 3, 3);
            }
            else
            {
                view.setPadding(30, 3, 3, 3);
            }
        }
        
        return view;
    }
    
    /**
     * 某节点进行选中or取消操作
     * 
     */
    public void checkNode(AddressNode node, boolean isChecked)
    {
        node.setChecked(isChecked);
    }
    
    public class ViewItem
    {
        private CheckBox  mCheckBox;
        
        private ImageView mIcon;
        
        private ImageView mArrow;
        
        private TextView  mName;
    }
    
}
