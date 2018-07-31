/*
 * File name:  FileLibrarySecondAdapter.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2013-11-19
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.manager.adpter.txl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.components.CustomHandler.CustomHanler;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.StringUtils;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.metaDataHandle.DBDataObjectWrite;
import com.hoperun.mipmanager.model.entityMetaData.GetTxlEncContactsInfo;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;
import com.hoperun.project.ui.function.txl.TxlFirstActivity.EncComparator;

/**
 * 企业通讯录列表适配器
 * 
 * @Description 企业通讯录列表适配器
 * 
 * @author wang_ling
 * @Version [版本号, 2014-03-31]
 */
public class EncContactsAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context                     context;
    
    /** 数据列表 **/
    private List<GetTxlEncContactsInfo> docLists;
    
    /** 帮助类 **/
    private LayoutInflater              inflater;
    
    /** 选择的item **/
    public int                          mSelectedPosition = -1;
    
    public int                          currentPosition;
    
    /** 列表項是否可以点击 **/
    public boolean                      isClick;
    
    protected CustomHanler              mHandler          = new CustomHanler()
                                                          {
                                                              
                                                              @Override
                                                              public void PostHandle(int requestType, Object objHeader,
                                                                  Object objBody, boolean error, int errorCode)
                                                              {
                                                                  onPostHandle(requestType,
                                                                      objHeader,
                                                                      objBody,
                                                                      error,
                                                                      errorCode);
                                                              }
                                                          };
    
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
            // 企业通讯录
                case RequestTypeConstants.GETTXLCOMPANYLISTREQUEST:
                    if (objBody != null)
                    {
                        MetaResponseBody responseBuzBody = (MetaResponseBody)objBody;
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        List<HashMap<String, Object>> contactlists =
                            (List<HashMap<String, Object>>)ret.get(0).get("contactlist");
                        if (contactlists != null && !"".equals(contactlists))
                        {
                            List<GetTxlEncContactsInfo> tempList =
                                DBDataObjectWrite.insertEncContactList(contactlists, GlobalState.getInstance()
                                    .getOpenId(), "2");
                            // List<GetTxlEncContactsInfo> tempList = parseEncContacts(contactlists);
                            
                            if (tempList.size() == 0)
                            {
                                Toast.makeText(context, "没有数据", Toast.LENGTH_LONG).show();
                            }
                            if (currentPosition != -1)
                            {
                                for (int i = 0; i < tempList.size(); i++)
                                {
                                    GetTxlEncContactsInfo element = tempList.get(i);
                                    docLists.add(currentPosition + i + 1, element);
                                }
                                notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Toast.makeText(context, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                    else
                    {
                        Toast.makeText(context, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    
                    break;
                
                default:
                    break;
            }
            
        }
        else
        {
            if (!(errorCode == ConstState.CANCEL_THREAD))
            {
                Toast.makeText(context, "请求服务器数据失败！", Toast.LENGTH_SHORT).show();
            }
        }
        
    }
    
    private List<GetTxlEncContactsInfo> parseEncContacts(List<HashMap<String, Object>> Objects)
    {
        List<GetTxlEncContactsInfo> doclists = new ArrayList<GetTxlEncContactsInfo>();
        for (int i = 0; i < Objects.size(); i++)
        {
            GetTxlEncContactsInfo docInfo = new GetTxlEncContactsInfo(GlobalState.getInstance().getOpenId(), "", "2");
            docInfo.convertToObject(Objects.get(i));
            doclists.add(docInfo);
        }
        EncComparator myComparator = new EncComparator();
        Collections.sort(doclists, myComparator);
        return doclists;
    }
    
    private void getEncContacts(String company, String department, String duties, String keyword)
    {
        JSONObject body = new JSONObject();
        try
        {
            
            body.put("company", company);
            body.put("department", department);
            body.put("duties", duties);
            body.put("keyWord", keyword);
            NetTask mGetCompanyListRequst =
                new HttpNetFactoryCreator(RequestTypeConstants.GETTXLCOMPANYLISTREQUEST).create();
            NetRequestController.getTXLList(mGetCompanyListRequst,
                mHandler,
                RequestTypeConstants.GETTXLCOMPANYLISTREQUEST,
                body,
                "getEncContacts");
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param context 上下文
     * @param list 列表数据
     * @param user 用户
     * @param type 类型
     * @param mUnDo 已办/待办
     */
    
    /**
     * 
     * <默认构造函数>
     * 
     * @param context 上下文
     * @param list 列表数据
     * @param user 用户
     * @param type 类型
     */
    public EncContactsAdapter(Context context, List<GetTxlEncContactsInfo> list)
    {
        super();
        this.context = context;
        
        this.docLists = list;
        isClick = true;
        
        this.inflater = LayoutInflater.from(this.context);
        
    }
    
    /**
     * 
     * 获取数据列表
     * 
     * @Description 获取数据列表
     * 
     * @return 数据列表
     * @LastModifiedDate：2013-11-13
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public List<GetTxlEncContactsInfo> getDocLists()
    {
        return docLists;
    }
    
    /**
     * 
     * 设置数据列表
     * 
     * @Description 设置数据列表
     * 
     * @param docLists 数据列表
     * @LastModifiedDate：2013-11-13
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setDocLists(List<GetTxlEncContactsInfo> docLists, boolean isclick)
    {
        this.docLists = docLists;
        this.isClick = isclick;
        // if (docLists.size() > 1)
        // {
        // if (!docLists.get(0)
        // .getLocaldatalist()
        // .get(GetTxlEncContactsInfo.l_type)
        // .equals(docLists.get(1).getLocaldatalist().get(GetTxlEncContactsInfo.l_type)))
        // {
        // isClick = false;
        // }
        // else
        // {
        // isClick = true;
        // }
        // }
    }
    
    /**
     * 
     * 获取选中项的位置
     * 
     * @Description 获取选中项的位置
     * 
     * @return 选中项的位置
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public int getmSelectedPosition()
    {
        return mSelectedPosition;
    }
    
    /**
     * 
     * 设置选中项的位置
     * 
     * @Description 设置选中项的位置
     * 
     * @param mSelectedPosition 选中项的位置
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setmSelectedPosition(int mSelectedPosition)
    {
        this.mSelectedPosition = mSelectedPosition;
    }
    
    /**
     * 重载方法
     * 
     * @return 列表的长度
     * @author shen_feng
     */
    @Override
    public int getCount()
    {
        return docLists == null ? 0 : docLists.size();
    }
    
    /**
     * 重载方法
     * 
     * @param position 位置
     * @return 选中位置那项的数据
     * @author shen_feng
     */
    @Override
    public Object getItem(int position)
    {
        return docLists.get(position);
    }
    
    /**
     * 重载方法
     * 
     * @param position position
     * @return position
     * @author shen_feng
     */
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    /**
     * 重载方法
     * 
     * @param position position
     * @param convertView convertView
     * @param parent parent
     * @return View
     * @author wang_ling
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        int viewType = getItemViewType(position);
        switch (viewType)
        {
            case 0:
                ViewHold viewHold;
                if (convertView == null)
                {
                    
                    viewHold = new ViewHold();
                    convertView = inflater.inflate(R.layout.enc_list_item, null);
                    
                    viewHold.mTvName = (TextView)convertView.findViewById(R.id.files_item_name);
                    viewHold.mImagIcon = (ImageView)convertView.findViewById(R.id.file_item_icon);
                    
                    viewHold.mRlayout = (RelativeLayout)convertView.findViewById(R.id.file_doc_list_item_layout);
                    
                    viewHold.mImgNew = (ImageView)convertView.findViewById(R.id.file_item_new_icon);
                    
                    viewHold.mImgNew.setVisibility(View.GONE);
                    viewHold.mImgArrow = (ImageView)convertView.findViewById(R.id.rightarrow);
                    
                    convertView.setTag(viewHold);
                }
                else
                {
                    viewHold = (ViewHold)convertView.getTag();
                    
                }
                
                String type = docLists.get(position).getStringKeyValue(GetTxlEncContactsInfo.l_type);
                if ("0".equals(type))
                {
                    viewHold.mTvName.setText(docLists.get(position).getStringKeyValue(GetTxlEncContactsInfo.company));
                }
                else if ("1".equals(type))
                {
                    String select = docLists.get(position).getStringKeyValue(GetTxlEncContactsInfo.l_selected);
                    if ("1".equals(select))
                    {
                        viewHold.mImgArrow.setImageResource(R.drawable.arrow_2);
                    }
                    else
                    {
                        viewHold.mImgArrow.setImageResource(R.drawable.arrow_1);
                    }
                    viewHold.mTvName.setText(docLists.get(position).getStringKeyValue(GetTxlEncContactsInfo.department));
                }
                
                if ("1".equals(type))
                {
                    convertView.setOnClickListener(new TreeElementIconClickListener(context, docLists, this, position,
                        viewHold.mImgArrow));
                    
                }
                break;
            case 1:
                ViewHold2 viewHold2;
                if (convertView == null)
                {
                    
                    viewHold2 = new ViewHold2();
                    convertView = inflater.inflate(R.layout.enclink_detail_item, null);
                    viewHold2.parentRl = (RelativeLayout)convertView.findViewById(R.id.file_doc_list_item_layout);
                    viewHold2.mtypeName = (TextView)convertView.findViewById(R.id.files_item_type_name);
                    
                    viewHold2.mTvName = (TextView)convertView.findViewById(R.id.files_item_name);
                    viewHold2.mImg = (ImageView)convertView.findViewById(R.id.file_item_icon);
                    convertView.setTag(viewHold2);
                }
                else
                {
                    viewHold2 = (ViewHold2)convertView.getTag();
                }
                viewHold2.mImg.setImageResource(R.drawable.icon_tp);
                viewHold2.mtypeName.setText(docLists.get(position).getStringKeyValue(GetTxlEncContactsInfo.duties));
                viewHold2.mTvName.setText(docLists.get(position).getStringKeyValue(GetTxlEncContactsInfo.tel));
                viewHold2.parentRl.setOnClickListener(new OnClickListener()
                {
                    
                    @Override
                    public void onClick(View v)
                    {
                        String telNum = docLists.get(position).getStringKeyValue(GetTxlEncContactsInfo.tel);
                        if (!StringUtils.isNull(telNum))
                        {
                            gotoCall(context, telNum);
                        }
                        
                    }
                    
                });
                
                break;
            default:
                break;
        
        }
        
        return convertView;
    }
    
    public void gotoCall(Context activity, String phoneNum)
    {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");// Intent.ACTION_CALL
        intent.setData(Uri.parse("tel:" + phoneNum));
        activity.startActivity(intent);
    }
    
    static class ViewHold
    {
        TextView       mTvName;
        
        /** 图标 **/
        ImageView      mImagIcon;
        
        // 整个布局
        RelativeLayout mRlayout;
        
        // 是否为新的标记
        ImageView      mImgNew;
        
        ImageView      mImgArrow;
        
    }
    
    static class ViewHold2
    {
        /** 外层布局 **/
        RelativeLayout parentRl;
        
        TextView       mtypeName;
        
        /** 联系人名字 **/
        TextView       mTvName;
        
        /** icon **/
        ImageView      mImg;
    }
    
    /**
     * 父亲节点的点击事件
     * 
     * @author wen_tao
     * 
     */
    public class TreeElementIconClickListener implements OnClickListener
    {
        // 适配器数据列表
        private List<GetTxlEncContactsInfo> treeElementList;
        
        // 当前适配器
        private EncContactsAdapter          treeViewAdapter;
        
        // 当前点击位置
        private int                         position;
        
        private ImageView                   rightView;
        
        public TreeElementIconClickListener(Context mContext, List<GetTxlEncContactsInfo> mTreeElementList,
            EncContactsAdapter mTreeViewAdapter, int position, ImageView rightImg)
        {
            this.treeElementList = mTreeElementList;
            this.treeViewAdapter = mTreeViewAdapter;
            this.position = position;
            this.rightView = rightImg;
        }
        
        @Override
        public void onClick(View v)
        {
            
            if (!isClick)
            {
                return;
            }
            String isSelect = treeElementList.get(position).getStringKeyValue(GetTxlEncContactsInfo.l_selected);
            // // 已經展開
            // if ("1".equals(isSelect))
            // {
            // treeElementList.get(position).getLocaldatalist().put(GetTxlEncContactsInfo.l_selected, "0");
            // }
            // else
            // {
            // int prePosition = position;
            // for (int i = 0; i < treeElementList.size(); i++)
            // {
            // if ("1".equals(treeElementList.get(i).getLocaldatalist().get(GetTxlEncContactsInfo.l_selected)))
            // {
            // prePosition = i;
            // break;
            // }
            // }
            //
            // ArrayList<GetTxlEncContactsInfo> temp = new ArrayList<GetTxlEncContactsInfo>();
            // for (int i = prePosition + 1; i < treeElementList.size(); i++)
            // {
            // if ("1".equals(treeElementList.get(i).getStringKeyValue(GetTxlEncContactsInfo.l_type)))
            // {
            // break;
            // }
            // temp.add(treeElementList.get(i));
            // }
            // treeElementList.removeAll(temp);
            // treeElementList.get(position).getLocaldatalist().put(GetTxlEncContactsInfo.l_selected, "1");
            // }
            
            // 已经展开
            if ("1".equals(isSelect))
            {
                rightView.setImageResource(R.drawable.arrow_1);
                treeElementList.get(position).getLocaldatalist().put(GetTxlEncContactsInfo.l_selected, "0");
                GetTxlEncContactsInfo element = treeElementList.get(position);
                ArrayList<GetTxlEncContactsInfo> temp = new ArrayList<GetTxlEncContactsInfo>();
                for (int i = position + 1; i < treeElementList.size(); i++)
                {
                    if ("1".equals(treeElementList.get(i).getStringKeyValue(GetTxlEncContactsInfo.l_type)))
                    {
                        break;
                    }
                    temp.add(treeElementList.get(i));
                }
                
                treeElementList.removeAll(temp);
                treeViewAdapter.notifyDataSetChanged();
            }
            else
            {
                rightView.setImageResource(R.drawable.arrow_2);
                GetTxlEncContactsInfo obj = treeElementList.get(position);
                obj.getLocaldatalist().put(GetTxlEncContactsInfo.l_selected, "1");
                String company = obj.getStringKeyValue(GetTxlEncContactsInfo.company);
                String department = obj.getStringKeyValue(GetTxlEncContactsInfo.department);
                currentPosition = position;
                
                // ---------------add----------------------
                int prePosition = 0;
                for (int i = 0; i < treeElementList.size(); i++)
                {
                    if ("1".equals(treeElementList.get(i).getLocaldatalist().get(GetTxlEncContactsInfo.l_selected))
                        && i != position)
                    {
                        prePosition = i;
                        treeElementList.get(i).getLocaldatalist().put(GetTxlEncContactsInfo.l_selected, "0");
                        break;
                    }
                }
                
                ArrayList<GetTxlEncContactsInfo> temp = new ArrayList<GetTxlEncContactsInfo>();
                for (int i = prePosition + 1; i < treeElementList.size(); i++)
                {
                    if ("1".equals(treeElementList.get(i).getStringKeyValue(GetTxlEncContactsInfo.l_type)))
                    {
                        break;
                    }
                    temp.add(treeElementList.get(i));
                }
                if (currentPosition > prePosition)
                {
                    currentPosition = position - temp.size();
                }
                treeElementList.removeAll(temp);
                treeViewAdapter.notifyDataSetChanged();
                // ---------------add----------------------
                
                List<GetTxlEncContactsInfo> tempList =
                    getEncContactList(GlobalState.getInstance().getOpenId(), company, department, "2");
                if (tempList.size() == 0)
                {
                    getEncContacts(company, department, "", "");
                }
                else
                {
                    for (int i = 0; i < tempList.size(); i++)
                    {
                        GetTxlEncContactsInfo element = tempList.get(i);
                        docLists.add(currentPosition + i + 1, element);
                    }
                    currentPosition = -1;
                    getEncContacts(company, department, "", "");
                    notifyDataSetChanged();
                }
                
            }
        }
    }
    
    /**
     * 重载方法
     * 
     * @return
     * @author wang_ling
     */
    @Override
    public int getViewTypeCount()
    {
        return 2;
    }
    
    /**
     * 重载方法
     * 
     * @param position
     * @return
     * @author wang_ling
     */
    @Override
    public int getItemViewType(int position)
    {
        String type = docLists.get(position).getStringKeyValue(GetTxlEncContactsInfo.l_type);
        if ("0".equals(type) || "1".equals(type))
        {
            return 0;
        }
        else if ("2".equals(type))
        {
            return 1;
        }
        return 0;
    }
    
    /**
     * 
     * 获取某个部门下的职位
     * 
     * @Description<功能详细描述>
     * 
     * @param username
     * @param company
     * @param department
     * @return
     * @LastModifiedDate：<date>
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    @SuppressWarnings("unused")
    private List<GetTxlEncContactsInfo> getEncContactList(String username, String company, String department,
        String type)
    {
        GetTxlEncContactsInfo test1 = new GetTxlEncContactsInfo();
        
        List<HashMap<String, Object>> queryret;
        String where = "";
        
        where =
            GetTxlEncContactsInfo.l_user + " = ?" + " and " + GetTxlEncContactsInfo.company + " = ?" + " and "
                + GetTxlEncContactsInfo.department + " = ?" + " and " + GetTxlEncContactsInfo.l_type + " = ?";
        
        String[] selectionArgs = null;
        
        selectionArgs = new String[] {username, company, department, type};
        
        queryret = test1.query(null, where, selectionArgs, "");
        List<GetTxlEncContactsInfo> doclists = new ArrayList<GetTxlEncContactsInfo>();
        if (null != queryret)
        {
            for (int i = 0; i < queryret.size(); i++)
            {
                GetTxlEncContactsInfo info = new GetTxlEncContactsInfo();
                info.convertToObject(queryret.get(i));
                doclists.add(info);
            }
        }
        return doclists;
    }
}
