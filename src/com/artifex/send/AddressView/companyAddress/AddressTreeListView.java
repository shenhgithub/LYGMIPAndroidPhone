/*
 * File name:  AddressTreeListView.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-10-15
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.send.AddressView.companyAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.widget.ListView;

import com.hoperun.miplygphone.R;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-10-15]
 */
public class AddressTreeListView extends ListView
{
    private ListView           mTreelist    = null;
    
    private AddressTreeAdapter mTreeAdapter = null;
    
    // private HashMap<String, AddressNode> allNodes = new HashMap<String, AddressNode>();
    
    private List<AddressNode>  allNodes     = new ArrayList<AddressNode>();
    
    private List<AddressNode>  rootNodes    = new ArrayList<AddressNode>();
    
    private String             rootId;
    
    private String             mCurrentId;
    
    public AddressTreeListView(final Context context, List<AddressNode> res, String rootId)
    {
        super(context);
        this.rootId = rootId;
        mCurrentId = rootId;
        mTreelist = this;
        mTreelist.setFocusable(false);
        mTreelist.setBackgroundColor(0xffffff);
        mTreelist.setFadingEdgeLength(0);
        mTreelist.setLayoutParams(new LayoutParams(ListView.LayoutParams.FILL_PARENT,
            ListView.LayoutParams.WRAP_CONTENT));
        mTreelist.setDrawSelectorOnTop(false);
        mTreelist.setCacheColorHint(0xffffff);
        mTreelist.setDivider(getResources().getDrawable(R.drawable.divider_list));
        mTreelist.setDividerHeight(2);
        mTreelist.setFastScrollEnabled(true);
        mTreelist.setScrollBarStyle(NO_ID);
        
        initNode(context);
    }
    
    public void initAddressNode(AddressNode node)
    {
        allNodes.add(node);
        
        if (node.getParentId().equals(rootId))
        {
            rootNodes.add(node);
        }
        else
        {
            AddressNode pNode = getNode(node.getParentId());
            if (null != pNode)
            {
                pNode.addNode(node);
            }
            
            node.setParent(pNode);
        }
    }
    
    public List<AddressNode> getChildNodes(String currentId)
    {
        List<AddressNode> nodes = new ArrayList<AddressNode>();
        if (currentId != null && currentId.equals(rootId))
        {
            for (int i = 0; i < rootNodes.size(); i++)
            {
                nodes.add(rootNodes.get(i));
            }
        }
        else
        {
            AddressNode node = getNode(currentId);
            if (node.isDept() && node.getChildrens() != null)
            {
                for (Entry<String, AddressNode> entry : node.getChildrens().entrySet())
                {
                    nodes.add(entry.getValue());
                }
            }
        }
        
        return nodes;
    }
    
    /**
     * 叶子初始化
     */
    private void initNode(Context context)
    {
        mTreeAdapter = new AddressTreeAdapter(context);
        
        this.setAdapter(mTreeAdapter);
    }
    
    public void setNotification(String currentId)
    {
        mCurrentId = currentId;
        mTreeAdapter.setAllNodes(getChildNodes(currentId));
    }
    
    public void setNotification()
    {
        mTreeAdapter.notifyDataSetChanged();
    }
    
    public List<AddressNode> getAllNodes()
    {
        return allNodes;
    }
    
    public void backToPre()
    {
        if (!mCurrentId.equals(rootId))
        {
            AddressNode node = getNode(mCurrentId);
            if (node.getParent() == null)
            {
                setNotification(rootId);
            }
            else
            {
                setNotification(node.getParent().getCurId());
            }
        }
    }
    
    private AddressNode getNode(String curId)
    {
        for (int i = 0; i < allNodes.size(); i++)
        {
            AddressNode node = allNodes.get(i);
            if (curId.equals(node.getCurId()))
            {
                return node;
            }
        }
        return null;
    }
    
    private AddressNode getCurrentNode(String parentId, String curId)
    {
        for (int i = 0; i < allNodes.size(); i++)
        {
            AddressNode node = allNodes.get(i);
            if (parentId.equals(node.getParent().getCurId()) && curId.equals(node.getCurId()))
            {
                return node;
            }
        }
        return null;
    }
    
}
