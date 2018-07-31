/*
 * File name:  AddressNode.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-10-15
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.send.AddressView.companyAddress;

import java.util.HashMap;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-10-15]
 */
public class AddressNode
{
    /**
     * 父节点
     */
    private AddressNode                  parent      = null;                              // 父节点
                                                                                           
    /**
     * 子节点
     */
    private HashMap<String, AddressNode> childrens   = new HashMap<String, AddressNode>(); // 子节点
                                                                                           
    /**
     * 名称
     */
    private String                       name;                                            // 名称
                                                                                           
    /**
     * 是否是部门
     */
    private boolean                      isDept;                                          // 是否是部门
                                                                                           
    /**
     * 父节点的ID
     */
    private String                       parentId    = null;                              // 父节点的ID
                                                                                           
    /**
     * 父节点的名字
     */
    private String                       parentName  = null;
    
    /**
     * 当前节点的ID
     */
    private String                       curId       = null;                              // 当前节点的ID
                                                                                           
    /**
     * 当前节点的ID
     */
    private boolean                      isChecked   = false;                             // 当前节点的ID
                                                                                           
    /**
     * 是否处于扩展状态
     */
    private boolean                      isExpand    = true;                              // 是否处于扩展状态
                                                                                           
    /**
     * 是否有复选框
     */
    private boolean                      hasCheckBox = true;                              // 是否有复选框
                                                                                           
    /**
     * 是否显示
     */
    private boolean                      isVisiable  = true;
    
    //
    // private String title; // 节点显示文字
    //
    // private String value; // 节点显示值
    //
    // private int icon = -1; // icon(R.drawable的id)
    
    /**
     * <默认构造函数>
     */
    public AddressNode()
    {
        
    }
    
    /**
     * 获取 parentName
     * 
     * @return 返回 parentName
     * @author wang_ling
     */
    public String getParentName()
    {
        return parentName;
    }
    
    /**
     * 设置 parentName
     * 
     * @param parentName 对parentName进行赋值
     * @author wang_ling
     */
    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }
    
    /**
     * <默认构造函数>
     */
    public AddressNode(String name, String parentId, String curId, boolean isDept)
    {
        // TODO Auto-generated constructor stub
        this.name = name;
        this.parentId = parentId;
        this.curId = curId;
        this.isDept = isDept;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return AddressNode
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public AddressNode getParent()
    {
        return parent;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param parent parent
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setParent(AddressNode parent)
    {
        this.parent = parent;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return HashMap<String, AddressNode>
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public HashMap<String, AddressNode> getChildrens()
    {
        return childrens;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param childrens childrens
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setChildrens(HashMap<String, AddressNode> childrens)
    {
        this.childrens = childrens;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return String
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param name name
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return boolean
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public boolean isDept()
    {
        return isDept;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param isDept isDept
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setDept(boolean isDept)
    {
        this.isDept = isDept;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return String
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public String getParentId()
    {
        return parentId;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param parentId parentId
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return String
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public String getCurId()
    {
        return curId;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param curId curId
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setCurId(String curId)
    {
        this.curId = curId;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return boolean
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public boolean isChecked()
    {
        return isChecked;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param isChecked isChecked
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return boolean
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public boolean isExpand()
    {
        return isExpand;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param isExpand isExpand
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setExpand(boolean isExpand)
    {
        this.isExpand = isExpand;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return boolean
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public boolean isHasCheckBox()
    {
        return hasCheckBox;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param hasCheckBox hasCheckBox
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setHasCheckBox(boolean hasCheckBox)
    {
        this.hasCheckBox = hasCheckBox;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return boolean
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public boolean isVisiable()
    {
        return isVisiable;
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param isVisiable isVisiable
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setVisiable(boolean isVisiable)
    {
        this.isVisiable = isVisiable;
    }
    
    /**
     * 增加一个子节点
     * 
     * @param node
     * 
     */
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param node node
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void addNode(AddressNode node)
    {
        if (!childrens.containsKey(node.getCurId()))
        {
            childrens.put(node.getCurId(), node);
        }
    }
    
    /**
     * 移除一个子节点
     * 
     * @param node
     * 
     */
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param node node
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void removeNode(AddressNode node)
    {
        if (childrens.containsKey(node))
            childrens.remove(node);
    }
    
    /**
     * 移除指定位置的子节点
     * 
     * @param location
     * 
     */
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param location location
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void removeNode(int location)
    {
        childrens.remove(location);
    }
    
    /**
     * 清除所有子节点
     * 
     */
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void clears()
    {
        childrens.clear();
    }
    
    /**
     * 递归获取当前节点级别
     * 
     * @return
     * 
     */
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return int
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public int getLevel()
    {
        return parent == null ? 0 : parent.getLevel() + 1;
    }
    
    /**
     * 父节点是否处于折叠的状态
     * 
     * @return
     * 
     */
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return boolean
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public boolean isParentCollapsed()
    {
        if (parent == null)
            return false;
        if (!parent.isExpand())
            return true;
        return parent.isParentCollapsed();
    }
    
    /**
     * 是否叶节点（没有展开下级的几点）
     * 
     * @return
     * 
     */
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @return boolean
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public boolean isLeaf()
    {
        return childrens.size() < 1 ? true : false;
    }
    
}
