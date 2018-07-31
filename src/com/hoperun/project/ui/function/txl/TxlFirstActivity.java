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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hoperun.manager.adpter.txl.EncContactsAdapter;
import com.hoperun.manager.adpter.txl.PersonTxlAdapter;
import com.hoperun.manager.components.WaitDialog;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.model.entityMetaData.GetTxlEncContactsInfo;
import com.hoperun.mipmanager.model.entityMetaData.GetTxlPersonListInfo;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;

/**
 * 通讯录主页面
 * 
 * @Description<功能详细描述>
 * 
 * @author wang_ling
 * @Version [版本号, 2014-3-31]
 */
public class TxlFirstActivity extends PMIPCustomSecondActivity implements OnItemClickListener
{
    /** 个人通讯录列表 **/
    private ListView                    personListView;
    
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
    
    /** 获取请求列表 **/
    protected NetTask                   mGetNamecheckListRequest;
    
    /**
     * 请求返回数据
     */
    private MetaResponseBody            responseBuzBody;
    
    /** 个人通讯录列表 **/
    private List<GetTxlPersonListInfo>  personList;
    
    /** 个人通讯录适配器 **/
    private PersonTxlAdapter            perosnAdapter;
    
    private List<GetTxlEncContactsInfo> encContactList;
    
    private List<GetTxlPersonListInfo>  namecheckList;
    
    private EncContactsAdapter          encContactsAdapter;
    
    private OnClickListener             mDepartmentListener = new OnClickListener()
                                                            {
                                                                
                                                                @Override
                                                                public void onClick(View v)
                                                                {
                                                                    Intent intent = new Intent();
                                                                    intent.setClass(TxlFirstActivity.this,
                                                                        DepartmentSelectActivity.class);
                                                                    startActivityForResult(intent, 0);
                                                                }
                                                                
                                                            };
    
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
        personListView = (ListView)findViewById(R.id.personListv);
        companyListView = (ListView)findViewById(R.id.companyListView);
        personListView.setVisibility(View.VISIBLE);
        companyListView.setVisibility(View.INVISIBLE);
        mTitle_tv.setText("通讯录");
        mSearch_funnel_ll.setVisibility(View.GONE);// 改
        // mSearch_funnel_ll.setOnClickListener(mDepartmentListener);//改
        waitDialog = WaitDialog.newInstance();
        initLocalData();
        getPersonList(GlobalState.getInstance().getCompamyId());
        // getEncContacts("", "", "", "");
        pull_down_arrow.setVisibility(View.GONE);
        
        companyName.setVisibility(View.GONE);
        
        // companyName.setText(GlobalState.getInstance().getCompanyName());
    }
    
    private void initLocalData()
    {
        personList = new ArrayList<GetTxlPersonListInfo>();
        perosnAdapter = new PersonTxlAdapter(this, personList);
        personListView.setAdapter(perosnAdapter);
        encContactList = new ArrayList<GetTxlEncContactsInfo>();
        encContactsAdapter = new EncContactsAdapter(this, encContactList);
        namecheckList = new ArrayList<GetTxlPersonListInfo>();
        companyListView.setAdapter(encContactsAdapter);
        companyListView.setOnItemClickListener(this);
    }
    
    /**
     * 
     * 获取个人通讯录
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-3-31
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void getPersonList(String companyid)
    {
        waitDialog = WaitDialog.newInstance();
        waitDialog.show(getSupportFragmentManager(), "waitDialog");
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("companyid", companyid);
            mGetPersonListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETTXLPERSONLISTREQUEST).create();
            NetRequestController.getTXLList(mGetPersonListRequst,
                mHandler,
                RequestTypeConstants.GETTXLPERSONLISTREQUEST,
                body,
                "getPerContacts");
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    
    // he7
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
        switch (arg0.getId())
        {
        //
            case R.id.companyListView:
                GetTxlEncContactsInfo selInfo = (GetTxlEncContactsInfo)encContactsAdapter.getItem(arg2);
                String selCompany = selInfo.getStringKeyValue(GetTxlEncContactsInfo.company);
                Intent intent = new Intent();
                intent.setClass(this, EncContactActivity.class);
                intent.putExtra("company", selCompany);
                this.startActivity(intent);
                
                break;
            
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
            // 个人通讯录
                case RequestTypeConstants.GETTXLPERSONLISTREQUEST:
                    waitDialog.dismiss();
                    if (objBody != null)
                    {
                        responseBuzBody = (MetaResponseBody)objBody;
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        
                        if (ret.get(0).get("percontacts") != null && !"".equals(ret.get(0).get("percontacts")))
                        {
                            List<HashMap<String, Object>> perContacts =
                                (List<HashMap<String, Object>>)ret.get(0).get("percontacts");
                            // List<GetTxlPersonListInfo> lists =
                            // DBDataObjectWrite.insertPersonContactList(perContacts, GlobalState.getInstance()
                            // .getOpenId());
                            List<GetTxlPersonListInfo> lists = parsePersonContacts(perContacts);
                            personList = lists;
                            perosnAdapter.setDocLists(lists);
                            perosnAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                            // personList.clear();
                            // perosnAdapter.setDocLists(personList);
                            // perosnAdapter.notifyDataSetChanged();
                        }
                        
                    }
                    else
                    {
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                        // personList.clear();
                        // perosnAdapter.setDocLists(personList);
                        // perosnAdapter.notifyDataSetChanged();
                    }
                    
                    break;
                // 企业通讯录
                case RequestTypeConstants.GETTXLCOMPANYLISTREQUEST:
                    waitDialog.dismiss();
                    if (objBody != null)
                    {
                        responseBuzBody = (MetaResponseBody)objBody;
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        if (ret != null && ret.size() > 0)
                        {
                            if (ret.get(0).get("contactlist") != null && !"".equals(ret.get(0).get("contactlist")))
                            {
                                List<HashMap<String, Object>> contactlists =
                                    (List<HashMap<String, Object>>)ret.get(0).get("contactlist");
                                // List<GetTxlEncContactsInfo> lists =
                                // DBDataObjectWrite.insertEncContactList(contactlists, GlobalState.getInstance()
                                // .getOpenId(), "0");
                                
                                List<GetTxlEncContactsInfo> lists = parseEncContacts(contactlists);
                                encContactList = lists;
                                encContactsAdapter.setDocLists(lists, true);
                                encContactsAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                    else
                    {
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    
                    break;
                // 根据企业id检索通讯录
                case RequestTypeConstants.SEARCHBYENTERPRISE:
                    waitDialog.dismiss();
                    if (objBody != null)
                    {
                        responseBuzBody = (MetaResponseBody)objBody;
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        
                        if (ret.get(0).get("percontacts") != null && !"".equals(ret.get(0).get("percontacts")))
                        {
                            List<HashMap<String, Object>> perContacts =
                                (List<HashMap<String, Object>>)ret.get(0).get("percontacts");
                            List<GetTxlPersonListInfo> lists = parsePersonContacts(perContacts);
                            perosnAdapter.setDocLists(lists);
                            perosnAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                            List<GetTxlPersonListInfo> tempList = new ArrayList<GetTxlPersonListInfo>();
                            perosnAdapter.setDocLists(tempList);
                            perosnAdapter.notifyDataSetChanged();
                        }
                        
                    }
                    else
                    {
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                        List<GetTxlPersonListInfo> tempList = new ArrayList<GetTxlPersonListInfo>();
                        perosnAdapter.setDocLists(tempList);
                        perosnAdapter.notifyDataSetChanged();
                    }
                    break;
                // 根据姓名检索通讯录
                case RequestTypeConstants.NAMECHECKTXL:
                    waitDialog.dismiss();
                    if (objBody != null)
                    {
                        responseBuzBody = (MetaResponseBody)objBody;
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        
                        if (ret.get(0).get("percontacts") != null && !"".equals(ret.get(0).get("percontacts")))
                        {
                            List<HashMap<String, Object>> perContacts =
                                (List<HashMap<String, Object>>)ret.get(0).get("percontacts");
                            List<GetTxlPersonListInfo> lists = parsePersonContacts(perContacts);
                            namecheckList = lists;
                            perosnAdapter.setDocLists(lists);
                            perosnAdapter.notifyDataSetChanged();
                            mSearchResultTextCount.setText(this.getString(R.string.search_key_word_result,
                                namecheckList.size()));
                        }
                        else
                        {
                            Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                            List<GetTxlPersonListInfo> tempList = new ArrayList<GetTxlPersonListInfo>();
                            perosnAdapter.setDocLists(tempList);
                            perosnAdapter.notifyDataSetChanged();
                        }
                        
                    }
                    else
                    {
                        Toast.makeText(this, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                        List<GetTxlPersonListInfo> tempList = new ArrayList<GetTxlPersonListInfo>();
                        perosnAdapter.setDocLists(tempList);
                        perosnAdapter.notifyDataSetChanged();
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
    
    private List<GetTxlPersonListInfo> parsePersonContacts(List<HashMap<String, Object>> Objects)
    {
        List<GetTxlPersonListInfo> doclists = new ArrayList<GetTxlPersonListInfo>();
        for (int i = 0; i < Objects.size(); i++)
        {
            GetTxlPersonListInfo docInfo = new GetTxlPersonListInfo(GlobalState.getInstance().getOpenId(), "");
            docInfo.convertToObject(Objects.get(i));
            doclists.add(docInfo);
        }
        return doclists;
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
            GetTxlEncContactsInfo docInfo = new GetTxlEncContactsInfo(GlobalState.getInstance().getOpenId(), "", "0");
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
        // mFirst_label_name.setText("个人通讯录");
        // mSecond_label_name.setText("企业通讯录");
        mFirstLabelName.setText("员工通讯录");
        mSecondLabelName.setText("机构通讯录");
        initReadLineLayout(TWOMODE);
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
        if (mUnHandleStatus.equals(ConstState.UNHADLEDOCLIST))
        {
            if (null != perosnAdapter && perosnAdapter.getDocLists().size() == 0)
            {
                getPersonList(GlobalState.getInstance().getCompamyId());
            }
            else if (null != perosnAdapter && personList.size() != perosnAdapter.getDocLists().size())
            {
                perosnAdapter.setDocLists(personList);
                perosnAdapter.notifyDataSetChanged();
            }
        }
        else
        {
            if (null != encContactsAdapter && encContactsAdapter.getDocLists().size() == 0)
            {
                getEncContacts("", "", "", "");
            }
            else if (null != encContactsAdapter && encContactList.size() != encContactsAdapter.getDocLists().size())
            {
                encContactsAdapter.setDocLists(encContactList, true);
                encContactsAdapter.notifyDataSetChanged();
            }
            
        }
        hideSoftKey();
    }
    
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
            companyName.setVisibility(View.GONE);
            mSearchRL.setVisibility(View.GONE);
            mSearchResultLayout.setVisibility(View.VISIBLE);
            mSearchResultText.setText(this.getString(R.string.search_key_word, "\"" + keyWords + typeId + "\""));
            mSearchResultTextCount.setText(this.getString(R.string.search_key_word_result, "0"));
            nameSearch();
            // // 员工通讯录
            // if (mUnHandleStatus.equals(ConstState.UNHADLEDOCLIST))
            // {
            // List<GetTxlPersonListInfo> temp = new ArrayList<GetTxlPersonListInfo>();
            // for (int i = 0; i < personList.size(); i++)
            // {
            // String name = (String)personList.get(i).getStringKeyValue(GetTxlPersonListInfo.name);
            // if (name.contains(keyWords))
            // {
            // temp.add(personList.get(i));
            // }
            // }
            // mSearchResultTextCount.setText(this.getString(R.string.search_key_word_result, temp.size()));
            // perosnAdapter.setDocLists(temp);
            // perosnAdapter.notifyDataSetChanged();
            // }
            // // 机构通讯录
            // else
            // {
            // // getEncContacts("", "", "", keyWords);
            // List<GetTxlEncContactsInfo> temEnc = new ArrayList<GetTxlEncContactsInfo>();
            // for (int i = 0; i < encContactList.size(); i++)
            // {
            // String company = (String)encContactList.get(i).getStringKeyValue(GetTxlEncContactsInfo.company);
            // if (company.contains(keyWords))
            // {
            // temEnc.add(encContactList.get(i));
            // }
            // }
            // mSearchResultTextCount.setText(this.getString(R.string.search_key_word_result, temEnc.size()));
            // EncComparator myComparator = new EncComparator();
            // Collections.sort(temEnc, myComparator);
            // encContactsAdapter.setDocLists(temEnc, true);
            // encContactsAdapter.notifyDataSetChanged();
            // }
            hideSoftKey();
        }
        else
        {
            mSearchEditText.setText("");
            mSearchRL.setVisibility(View.VISIBLE);
            mSearchResultLayout.setVisibility(View.GONE);
            
            // disPlayListView();
            //
            if (mUnHandleStatus.equals(ConstState.UNHADLEDOCLIST))
            {
                personListView.setVisibility(View.VISIBLE);
                perosnAdapter.setDocLists(personList);
                perosnAdapter.notifyDataSetChanged();
                companyName.setVisibility(View.GONE);
            }
            // 企业通讯录
            else
            {
                companyListView.setVisibility(View.VISIBLE);
                personListView.setVisibility(View.INVISIBLE);
                encContactsAdapter.setDocLists(encContactList, true);
                encContactsAdapter.notifyDataSetChanged();
            }
        }
    }
    
    /**
     * 
     * <一句话功能简述>根据姓名检索通讯录
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-5-19
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void nameSearch()
    {
        personListView.setVisibility(View.VISIBLE);
        companyListView.setVisibility(View.INVISIBLE);
        waitDialog = WaitDialog.newInstance();
        waitDialog.show(getSupportFragmentManager(), "waitDialog");
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("filter", mSearchEditText.getText().toString().trim());
            mGetNamecheckListRequest = new HttpNetFactoryCreator(RequestTypeConstants.NAMECHECKTXL).create();
            NetRequestController.getTXLList(mGetNamecheckListRequest,
                mHandler,
                RequestTypeConstants.NAMECHECKTXL,
                body,
                "getStaffContactList");
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
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
        switch (id)
        {
        // 员工通讯录
            case R.id.fragment_first_ll:
            case R.id.label_first_ll:
                mUnHandleStatus = ConstState.UNHADLEDOCLIST;
                personListView.setVisibility(View.VISIBLE);
                companyListView.setVisibility(View.INVISIBLE);
                // personListView.setVisibility(View.INVISIBLE);
                // companyListView.setVisibility(View.VISIBLE);
                mSearch_funnel_ll.setVisibility(View.GONE);// 改
                companyName.setVisibility(View.GONE);
                // companyName.setText(GlobalState.getInstance().getCompanyName());
                break;
            // 机构通讯录
            case R.id.fragment_second_ll:
            case R.id.label_second_ll:
                mUnHandleStatus = ConstState.HASHANDLEDOCLIST;
                personListView.setVisibility(View.INVISIBLE);
                companyListView.setVisibility(View.VISIBLE);
                // personListView.setVisibility(View.VISIBLE);
                // companyListView.setVisibility(View.INVISIBLE);
                mSearch_funnel_ll.setVisibility(View.GONE);
                companyName.setVisibility(View.GONE);
                break;
            
            default:
                break;
        }
        // mSearch_funnel_ll.setVisibility(View.GONE);
        disPlayListView();
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
            // return lhs.getStringKeyValue(GetTxlEncContactsInfo.num)
            // .compareTo(rhs.getStringKeyValue(GetTxlEncContactsInfo.num));
        }
    }
    
    private String enterpriseId;
    
    private String enterpriseName;
    
    /**
     * 重载方法
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @author chen_wei3
     */
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2)
    {
        super.onActivityResult(arg0, arg1, arg2);
        enterpriseId = arg2.getStringExtra("id");
        enterpriseName = arg2.getStringExtra("name");
        if (!"".equals(enterpriseId))
        {
            searchTxlByEnterprise(enterpriseId);
            companyName.setVisibility(View.GONE);
            // companyName.setText(enterpriseName);
        }
    }
    
    /**
     * 
     * <一句话功能简述>根据企业列表页面返回的企业id来检索通讯录
     * 
     * @Description<功能详细描述>
     * 
     * @param companyid
     * @LastModifiedDate：2014-5-20
     * @author chen_wei3
     * @EditHistory：<修改内容><修改人>
     */
    private void searchTxlByEnterprise(String companyid)
    {
        waitDialog = WaitDialog.newInstance();
        waitDialog.show(getSupportFragmentManager(), "waitDialog");
        JSONObject body = new JSONObject();
        try
        {
            body.put("token", GlobalState.getInstance().getToken());
            body.put("companyid", companyid);
            mGetPersonListRequst = new HttpNetFactoryCreator(RequestTypeConstants.GETTXLPERSONLISTREQUEST).create();
            NetRequestController.getTXLList(mGetPersonListRequst,
                mHandler,
                RequestTypeConstants.SEARCHBYENTERPRISE,
                body,
                "getPerContacts");
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
