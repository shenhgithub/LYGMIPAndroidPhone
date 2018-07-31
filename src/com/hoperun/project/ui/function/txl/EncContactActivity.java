/*
 * File name:  TxlFirstActivity.java
 * Copyright:  Copyright (c) 2006-2014 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wang_ling
 * Last modified date:  2014-3-31
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.function.txl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hoperun.manager.adpter.txl.EncContactsAdapter;
import com.hoperun.manager.components.WaitDialog;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityMetaData.GetTxlEncContactsInfo;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;

/**
 * 企业通讯录页面
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-31]
 */
public class EncContactActivity extends PMIPCustomSecondActivity implements OnItemClickListener
{
    
    /** 企业通讯录列表 **/
    private ListView                    companyListView;
    
    /** 等待对话框 **/
    private WaitDialog                  waitDialog;
    
    /**
     * 获取请求列表
     */
    protected NetTask                   mGetPersonListRequst;
    
    /**
     * 获取请求列表
     */
    protected NetTask                   mGetCompanyListRequst;
    
    /**
     * 请求返回数据
     */
    private MetaResponseBody            responseBuzBody;
    
    /**
     * 部门列表
     */
    private List<GetTxlEncContactsInfo> encContactList;
    
    private EncContactsAdapter          encContactsAdapter;
    
    /** 上个页面传过来的公司名 **/
    private String                      selCompany;
    
    /** 是否是搜索 **/
    private boolean                     isSearch = false;
    
    /**
     * 重载方法
     * 
     * @param savedInstanceState
     * @author wang_ling
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        selCompany = this.getIntent().getStringExtra("company");
        companyListView = (ListView)findViewById(R.id.companyListView);
        companyListView.setVisibility(View.VISIBLE);
        mTitle_tv.setText(selCompany);
        mSearch_funnel_ll.setVisibility(View.GONE);
        waitDialog = WaitDialog.newInstance();
        initLocalData();
        getEncContacts(selCompany, "", "", "");
    }
    
    private void initLocalData()
    {
        encContactList = new ArrayList<GetTxlEncContactsInfo>();
        encContactsAdapter = new EncContactsAdapter(this, encContactList);
        companyListView.setAdapter(encContactsAdapter);
    }
    
    /**
     * 搜索结果
     */
    
    /**
     * 重载方法
     * 
     * @param keyWords
     * @param typeId
     * @param flag
     * @author wang_ling
     */
    @Override
    protected void searchDocList(String keyWords, String typeId, boolean flag)
    {
        
        if (flag)
        {
            mSearchRL.setVisibility(View.GONE);
            mSearchResultLayout.setVisibility(View.VISIBLE);
            mSearchResultText.setText(this.getString(R.string.search_key_word, "\"" + keyWords + typeId + "\""));
            mSearchResultTextCount.setText(this.getString(R.string.search_key_word_result, "0"));
            
            // isSearch = true;
            // getEncContacts("", "", "", keyWords);
            List<GetTxlEncContactsInfo> temEnc = new ArrayList<GetTxlEncContactsInfo>();
            for (int i = 0; i < encContactList.size(); i++)
            {
                String department = (String)encContactList.get(i).getStringKeyValue(GetTxlEncContactsInfo.department);
                String type = (String)encContactList.get(i).getStringKeyValue(GetTxlEncContactsInfo.l_type);
                
                if ("1".equals(type) && department.contains(keyWords))
                {
                    encContactList.get(i).getLocaldatalist().put(GetTxlEncContactsInfo.l_selected, "0");
                    temEnc.add(encContactList.get(i));
                }
            }
            mSearchResultTextCount.setText(this.getString(R.string.search_key_word_result, temEnc.size()));
            EncComparator myComparator = new EncComparator();
            Collections.sort(temEnc, myComparator);
            encContactsAdapter.setDocLists(temEnc, true);
            encContactsAdapter.notifyDataSetChanged();
            hideSoftKey();
        }
        else
        {
            mSearchEditText.setText("");
            mSearchRL.setVisibility(View.VISIBLE);
            mSearchResultLayout.setVisibility(View.GONE);
            List<GetTxlEncContactsInfo> temEnc = new ArrayList<GetTxlEncContactsInfo>();
            for (int i = 0; i < encContactList.size(); i++)
            {
                String type = (String)encContactList.get(i).getStringKeyValue(GetTxlEncContactsInfo.l_type);
                if (type.equals("1"))
                {
                    encContactList.get(i).getLocaldatalist().put(GetTxlEncContactsInfo.l_selected, "0");
                    temEnc.add(encContactList.get(i));
                }
            }
            encContactsAdapter.setDocLists(temEnc, true);
            encContactsAdapter.notifyDataSetChanged();
        }
    }
    
    /**
     * 
     * 获取企业通讯录
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-1
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void getEncContacts(String company, String department, String duties, String keyword)
    {
        waitDialog = WaitDialog.newInstance();
        waitDialog.show(getSupportFragmentManager(), "waitDialog");
        JSONObject body = new JSONObject();
        try
        {
            
            body.put("company", company);
            body.put("department", department);
            body.put("duties", duties);
            body.put("keyWord", keyword);
            mGetCompanyListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETTXLCOMPANYLISTREQUEST).create();
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
     * 重载方法
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @author wang_ling
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        switch (arg1.getId())
        {
        //
            case R.id.companyListView:
                
            default:
                break;
        }
        
    }
    
    /**
     * 重载方法
     * 
     * @param requestType
     * @param objHeader
     * @param objBody
     * @param error
     * @param errorCode
     * @author wang_ling
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
            // 企业通讯录
                case RequestTypeConstants.GETTXLCOMPANYLISTREQUEST:
                    waitDialog.dismiss();
                    if (objBody != null)
                    {
                        responseBuzBody = (MetaResponseBody)objBody;
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        List<GetTxlEncContactsInfo> lists = new ArrayList<GetTxlEncContactsInfo>();
                        if (null != ret && ret.size() > 0)
                        {
                            if (ret.get(0).get("contactlist") != null && !"".equals(ret.get(0).get("contactlist")))
                            {
                                List<HashMap<String, Object>> contactlists =
                                    (List<HashMap<String, Object>>)ret.get(0).get("contactlist");
                                // List<GetTxlEncContactsInfo> lists =
                                // DBDataObjectWrite.insertEncContactList(contactlists, GlobalState.getInstance()
                                // .getOpenId(), "1");
                                lists = parseEncContacts(contactlists);
                                if (isSearch)
                                {
                                    mSearchResultTextCount.setText(this.getString(R.string.search_key_word_result,
                                        lists.size()));
                                    isSearch = false;
                                    if (lists.size() > 0)
                                    {
                                        GetTxlEncContactsInfo temp =
                                            new GetTxlEncContactsInfo(GlobalState.getInstance().getOpenId(), "", "2");
                                        temp.getDatalist().put(GetTxlEncContactsInfo.company,
                                            lists.get(0).getStringKeyValue(GetTxlEncContactsInfo.company));
                                        temp.getDatalist().put(GetTxlEncContactsInfo.department,
                                            lists.get(0).getStringKeyValue(GetTxlEncContactsInfo.department));
                                        temp.getDatalist().put(GetTxlEncContactsInfo.duties,
                                            lists.get(0).getStringKeyValue(GetTxlEncContactsInfo.duties));
                                        temp.getDatalist().put(GetTxlEncContactsInfo.tel,
                                            lists.get(0).getStringKeyValue(GetTxlEncContactsInfo.tel));
                                        temp.getDatalist().put(GetTxlEncContactsInfo.num,
                                            lists.get(0).getStringKeyValue(GetTxlEncContactsInfo.num));
                                        lists.add(1, temp);
                                        lists.get(0).getLocaldatalist().put(GetTxlEncContactsInfo.l_selected, "1");
                                    }
                                    encContactsAdapter.setDocLists(lists, false);
                                }
                                else
                                {
                                    encContactList = lists;
                                    encContactsAdapter.setDocLists(lists, true);
                                }
                                // encContactsAdapter.setDocLists(lists);
                                encContactsAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                encContactsAdapter.setDocLists(lists, true);
                                encContactsAdapter.notifyDataSetChanged();
                                Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            encContactsAdapter.setDocLists(lists, true);
                            encContactsAdapter.notifyDataSetChanged();
                            Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                    else
                    {
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    
                    break;
                
                default:
                    break;
            }
            
        }
        else
        {
            waitDialog.dismiss();
            if (!(errorCode == ConstState.CANCEL_THREAD))
            {
                Toast.makeText(this, "请求服务器数据失败！", Toast.LENGTH_SHORT).show();
            }
        }
        
    }
    
    /**
     * 
     * 解析企业通讯录
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-4-2
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private List<GetTxlEncContactsInfo> parseEncContacts(List<HashMap<String, Object>> Objects)
    {
        List<GetTxlEncContactsInfo> doclists = new ArrayList<GetTxlEncContactsInfo>();
        HashMap<String, GetTxlEncContactsInfo> map = new HashMap<String, GetTxlEncContactsInfo>();
        for (int i = 0; i < Objects.size(); i++)
        {
            GetTxlEncContactsInfo docInfo = new GetTxlEncContactsInfo(GlobalState.getInstance().getOpenId(), "", "1");
            docInfo.convertToObject(Objects.get(i));
            doclists.add(docInfo);
            // map.put(docInfo.getStringKeyValue(GetTxlEncContactsInfo.company), docInfo);
        }
        // for (GetTxlEncContactsInfo info : map.values())
        // {
        // doclists.add(info);
        // }
        EncComparator myComparator = new EncComparator();
        Collections.sort(doclists, myComparator);
        // mSearchResultTextCount.setText(this.getString(R.string.search_key_word_result, doclists.size()));
        return doclists;
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void customThisFragmentView()
    {
        initReadLineLayout(NOMODE);
        // mFragmentBack.setBackgroundResource(R.drawable.back);
    }
    
    /**
     * 重载方法
     * 
     * @author wang_ling
     */
    @Override
    public void disPlayListView()
    {
    }
    
    /**
     * 重载方法
     * 
     * @param keyWords
     * @param typeId
     * @return
     * @author wang_ling
     */
    @Override
    public View getSearchView(String keyWords, String typeId)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 重载方法
     * 
     * @param view
     * @param keyWords
     * @param typeId
     * @return
     * @author wang_ling
     */
    @Override
    public Object initOfficalListView(Object view, String keyWords, String typeId)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 重载方法
     * 
     * @param id
     * @author wang_ling
     */
    @Override
    public void clickRadioButton(int id)
    {
    }
    
    /**
     * 
     * 企业通讯录排序
     * 
     * @Description<功能详细描述>
     * 
     * @author wang_ling
     * @Version [版本号, 2014-4-2]
     */
    public static class EncComparator implements Comparator<GetTxlEncContactsInfo>
    {
        
        /**
         * 重载方法
         * 
         * @param lhs
         * @param rhs
         * @return
         * @author wang_ling
         */
        @Override
        public int compare(GetTxlEncContactsInfo lhs, GetTxlEncContactsInfo rhs)
        {
            String lNum = (String)lhs.getStringKeyValue(GetTxlEncContactsInfo.num);
            String rNum = (String)rhs.getStringKeyValue(GetTxlEncContactsInfo.num);
            if (!"".equals(lNum) && !"".equals(rNum))
            {
                return Integer.parseInt(lNum) > Integer.parseInt(rNum) ? 1 : -1;
            }
            else
            {
                return -1;
            }
        }
    }
    
}
