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
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityMetaData.GetTxlPersonListInfo;
import com.hoperun.mipmanager.model.entityModule.txl.perContactDetailEntity;
import com.hoperun.project.ui.baseui.PMIPBaseAdapter;
import com.hoperun.project.ui.function.txl.ListViewUtils;
import com.hoperun.project.ui.function.txl.MailListActivity;

/**
 * 个人通讯录列表适配器
 * 
 * @Description 个人通讯录列表适配器
 * 
 * @author wang_ling
 * @Version [版本号, 2014-03-31]
 */
public class PersonTxlAdapter extends PMIPBaseAdapter
{
    /** 上下文 **/
    private Context                    context;
    
    /** 数据列表 **/
    private List<GetTxlPersonListInfo> docLists;
    
    /** 帮助类 **/
    private LayoutInflater             inflater;
    
    /** 选择的item **/
    public int                         mSelectedPosition = -1;
    
    private int                        currentItemId;
    
    private GetTxlPersonListInfo       enty;
    
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
    public PersonTxlAdapter(Context context, List<GetTxlPersonListInfo> list)
    {
        super();
        this.context = context;
        
        this.docLists = list;
        
        this.inflater = LayoutInflater.from(this.context);
        currentItemId = -1;
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
    public List<GetTxlPersonListInfo> getDocLists()
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
    public void setDocLists(List<GetTxlPersonListInfo> docLists)
    {
        this.docLists = docLists;
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
        
        final ViewHold viewHold;
        if (convertView == null)
        {
            // he
            viewHold = new ViewHold();
            convertView = inflater.inflate(R.layout.personlink_mian_item, null);
            viewHold.parentRl = (RelativeLayout)convertView.findViewById(R.id.file_doc_list_item_layout);
            viewHold.mTvName = (TextView)convertView.findViewById(R.id.files_item_name);
            
            viewHold.mImg = (ImageView)convertView.findViewById(R.id.im_select);
            viewHold.mListView = (ListView)convertView.findViewById(R.id.detail_lv);
            
            convertView.setTag(viewHold);
        }
        else
        {
            viewHold = (ViewHold)convertView.getTag();
            
        }
        
        GetTxlPersonListInfo docinfo = docLists.get(position);
        viewHold.mTvName.setText(docinfo.getStringKeyValue(GetTxlPersonListInfo.name));
        
        if (position != currentItemId)
        {
            viewHold.mListView.setVisibility(View.GONE);
            viewHold.mImg.setImageResource(R.drawable.arrow_1);
        }
        else
        {
            // he7
            // /** 部门名称 **/
            // String departmentname =
            // (String)docLists.get(position).getStringKeyValue(GetTxlPersonListInfo.departmentname);
            // /** 职务 **/
            // String duty = (String)docLists.get(position).getStringKeyValue(GetTxlPersonListInfo.duty);
            // /** 备用手机 **/
            // String baktel = (String)docLists.get(position).getStringKeyValue(GetTxlPersonListInfo.baktel);
            // /** 办公电话 **/
            String officetel = (String)docLists.get(position).getStringKeyValue(GetTxlPersonListInfo.officetel);
            // /** 备用电话 **/
            // String bakofficetel =
            // (String)docLists.get(position).getStringKeyValue(GetTxlPersonListInfo.bakofficetel);
            // /** 个人邮箱 **/
            // String peremail = (String)docLists.get(position).getStringKeyValue(GetTxlPersonListInfo.peremail);
            // /** 云之家微博开通标识 **/
            // String markweibo = (String)docLists.get(position).getStringKeyValue(GetTxlPersonListInfo.markweibo);
            String more = GetTxlPersonListInfo.more;
            
            String tel = (String)docLists.get(position).getStringKeyValue(GetTxlPersonListInfo.tel);
            String email = (String)docLists.get(position).getStringKeyValue(GetTxlPersonListInfo.email);
            List<perContactDetailEntity> temp = new ArrayList<perContactDetailEntity>();
            
            perContactDetailEntity entity = new perContactDetailEntity();
            enty = docLists.get(position);
            // entity.setName(enty.getValue(""));
            if (!"".equals(tel))
            {
                entity.setName(tel);
            }
            else
            {
                entity.setName("");
            }
            entity.setType("0");
            temp.add(entity);
            // 办公电话
            perContactDetailEntity entity1 = new perContactDetailEntity();
            if (!"".equals(officetel))
            {
                entity1.setName(officetel);
            }
            else
            {
                entity1.setName("");
            }
            entity1.setType("1");
            temp.add(entity1);
            perContactDetailEntity entity2 = new perContactDetailEntity();
            if (!"".equals(email))
            {
                entity2.setName(email);
            }
            else
            {
                entity2.setName("");
            }
            entity2.setType("2");
            temp.add(entity2);
            // 更多
            perContactDetailEntity entity3 = new perContactDetailEntity();
            if (!"".equals(more))
            {
                entity3.setName(more);
            }
            else
            {
                entity3.setName("");
            }
            entity3.setType("3");
            temp.add(entity3);
            if (temp.size() > 0)
            {
                PersonDetailAdapter detailAdapter = new PersonDetailAdapter(context, temp);
                viewHold.mListView.setAdapter(detailAdapter);
                ListViewUtils.setListViewHeightBasedOnChildren(viewHold.mListView);
                viewHold.mListView.setVisibility(View.VISIBLE);
                viewHold.mImg.setImageResource(R.drawable.arrow_2);
            }
            else
            {
                Toast.makeText(context, "没有详细信息", Toast.LENGTH_LONG).show();
            }
        }
        viewHold.mListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                perContactDetailEntity entity = (perContactDetailEntity)arg0.getItemAtPosition(arg2);
                // 手机
                if (entity.getType().equals("0") && !entity.getName().equals(""))
                {
                    gotoCall(context, entity.getName());
                }
                // 办公电话
                else if (entity.getType().equals("1") && !entity.getName().equals(""))
                {
                    gotoCall(context, entity.getName());
                }// 邮件
                else if (entity.getType().equals("2") && !entity.getName().equals(""))
                {
                    gotoEmail(context, entity.getName());
                }
                // 更多
                else if (entity.getType().equals("3") && !entity.getName().equals(""))
                {
                    gotoMore(context, entity.getName());
                }
            }
            
        });
        viewHold.parentRl.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                String isSelect = docLists.get(position).getStringKeyValue(GetTxlPersonListInfo.l_selected);
                // if ("1".equals(isSelect))
                // {
                // docLists.get(position).getLocaldatalist().put(GetTxlPersonListInfo.l_selected, "0");
                // viewHold.mListView.setVisibility(View.GONE);
                // viewHold.mImg.setImageResource(R.drawable.arrow_1);
                // }
                // else
                // {
                // docLists.get(position).getLocaldatalist().put(GetTxlPersonListInfo.l_selected, "1");
                // String tel = (String)docLists.get(position).getStringKeyValue(GetTxlPersonListInfo.tel);
                // String email = (String)docLists.get(position).getStringKeyValue(GetTxlPersonListInfo.email);
                // List<perContactDetailEntity> temp = new ArrayList<perContactDetailEntity>();
                // if (!"".equals(tel))
                // {
                // perContactDetailEntity entity = new perContactDetailEntity();
                // entity.setName(tel);
                // entity.setType("0");
                // temp.add(entity);
                // }
                // if (!"".equals(email))
                // {
                // perContactDetailEntity entity2 = new perContactDetailEntity();
                // entity2.setName(email);
                // entity2.setType("2");
                // temp.add(entity2);
                // }
                // if (temp.size() > 0)
                // {
                // PersonDetailAdapter detailAdapter = new PersonDetailAdapter(context, temp);
                // viewHold.mListView.setAdapter(detailAdapter);
                // ListViewUtils.setListViewHeightBasedOnChildren(viewHold.mListView);
                // viewHold.mListView.setVisibility(View.VISIBLE);
                // viewHold.mImg.setImageResource(R.drawable.arrow_2);
                // }
                // else
                // {
                // Toast.makeText(context, "没有详细信息", Toast.LENGTH_LONG).show();
                // }
                // }
                
                if (position == currentItemId)
                {
                    currentItemId = -1;
                }
                else
                {
                    currentItemId = position;
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    
    /**
     * 
     * 大电话
     * 
     * @Description<功能详细描述>
     * 
     * @param activity
     * @param phoneNum
     * @LastModifiedDate：2014-4-28
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    // he
    public void gotoCall(Context activity, String phoneNum)
    {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");// Intent.ACTION_CALL
        intent.setData(Uri.parse("tel:" + phoneNum));
        activity.startActivity(intent);
        
    }
    
    /**
     * 
     * 发送邮件
     * 
     * @Description<功能详细描述>
     * 
     * @param activity
     * @param emailAdrress
     * @LastModifiedDate：2014-4-28
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void gotoEmail(Context activity, String emailAdrress)
    {
        Intent email = new Intent(android.content.Intent.ACTION_SEND);
        email.setType("plain/text");
        String[] emailReciver = new String[] {emailAdrress};
        
        // String emailSubject = "从问道分享来的文章";
        // String emailBody = internetpath;
        // 设置邮件默认地址
        email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
        // // 设置邮件默认标题
        // email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);
        // // 设置要默认发送的内容
        // email.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);
        // 调用系统的邮件系统
        activity.startActivity(Intent.createChooser(email, "请选择邮件发送软件"));
    }
    
    // 更多
    public void gotoMore(Context activity, String mailMore)
    {
        Intent in = new Intent(context, MailListActivity.class);
        in.putExtra("name", enty.getStringKeyValue(GetTxlPersonListInfo.name));
        in.putExtra("departmentname", enty.getStringKeyValue(GetTxlPersonListInfo.departmentname));
        in.putExtra("duty", enty.getStringKeyValue(GetTxlPersonListInfo.duty));
        in.putExtra("tel", enty.getStringKeyValue(GetTxlPersonListInfo.tel));
        in.putExtra("baktel", enty.getStringKeyValue(GetTxlPersonListInfo.baktel));
        in.putExtra("officetel", enty.getStringKeyValue(GetTxlPersonListInfo.officetel));
        in.putExtra("bakofficetel", enty.getStringKeyValue(GetTxlPersonListInfo.bakofficetel));
        in.putExtra("peremail", enty.getStringKeyValue(GetTxlPersonListInfo.peremail));
        in.putExtra("email", enty.getStringKeyValue(GetTxlPersonListInfo.email));
        in.putExtra("markweibo", enty.getStringKeyValue(GetTxlPersonListInfo.markweibo));
        in.putExtra("companyname", enty.getStringKeyValue(GetTxlPersonListInfo.companyname));
        context.startActivity(in);
    }
    
    static class ViewHold
    {
        /** 外层布局 **/
        RelativeLayout parentRl;
        
        /** 联系人名字 **/
        TextView       mTvName;
        
        /** 右侧箭头 **/
        ImageView      mImg;
        
        /** 具体信息列表 **/
        ListView       mListView;
        
    }
}
