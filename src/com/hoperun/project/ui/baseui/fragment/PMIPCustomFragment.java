/*
 * File name:  CustomFragment.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-12-5
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.baseui.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.adpter.offical.OfficalSearchAdpater;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.project.ui.baseui.baseInterface.baseUiInterface.OnResultListen;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-12-5]
 */
public abstract class PMIPCustomFragment extends PMIPBaseSecondFragment implements OnClickListener
{
    public static final int  NOMODE            = 0;
    
    public static final int  TWOMODE           = 2;
    
    public static final int  THREEMODE         = 3;
    
    protected ImageView      mFragmentBack;
    
    protected ImageView      mFragmentSearch;
    
    protected RelativeLayout mFragmentLL;
    
    protected RelativeLayout mFirstLL;
    
    protected RelativeLayout mSecondLL;
    
    protected RelativeLayout mThirdLL;
    
    // ------------------------ADD---------------------------
    protected LinearLayout   mheaderLaberLL;
    
    protected RelativeLayout mFirstLabelLL;
    
    protected RelativeLayout mSecondLabelLL;
    
    /**
     * 第一个标签的名称
     */
    protected TextView       mFirstLabelName;
    
    /**
     * 第一个标签的个数
     */
    protected TextView       mSecondLabelName;
    
    // -------------------------ADD---------------------------
    
    /**
     * 最外层布局
     */
    protected RelativeLayout mParentRL;
    
    /**
     * 搜索内容输入框
     */
    protected EditText       mSearchEditText;
    
    /**
     * 请求列表处于待办、已办的标识,0:待办，1:已办
     */
    protected String         mUnHandleStatus   = ConstState.UNHADLEDOCLIST;
    
    /**
     * 请求列表处于已读、未读的标识，0:未读，1:已读
     */
    protected String         mUnReadStatus     = ConstState.UNReadDOCLIST;
    
    /**
     * 装载listview 的布局
     */
    protected RelativeLayout mOfficalListViewRL;
    
    /**
     * 未读布局
     */
    protected RelativeLayout mUnread_rl;
    
    /**
     * 已读布局
     */
    protected RelativeLayout mRead_rl;
    
    /**
     * 草稿布局
     */
    protected RelativeLayout mDrag_rl;
    
    /**
     * 标题 textView
     */
    protected TextView       mTitle_tv;
    
    /**
     * 搜索的字符串
     */
    protected String         mSearchStr;
    
    /**
     * “未读”、“已读”、“草稿”重叠部分的像素
     */
    private int              SPACEING          = 10;
    
    private ImageView        mFirst_choose_line;
    
    private ImageView        mSecond_choose_line;
    
    private ImageView        mThird_choose_line;
    
    private ImageView        mFirst_choose_shadow;
    
    private ImageView        mSecond_choose_shadow_1;
    
    private ImageView        mSecond_choose_shadow_2;
    
    private ImageView        mThird_choose_shadow;
    
    private ImageView        mFragment_shadow;
    
    /**
     * 第一个标签的名称
     */
    protected TextView       mFirst_label_name;
    
    /**
     * 第一个标签的个数
     */
    protected TextView       mFirst_label_number;
    
    /**
     * 第二个标签的名称
     */
    protected TextView       mSecond_label_name;
    
    /**
     * 第二个标签的个数
     */
    protected TextView       mSecond_label_number;
    
    /**
     * 第三个标签的名称
     */
    protected TextView       mThird_label_name;
    
    /**
     * 第三个标签的个数
     */
    protected TextView       mThird_label_number;
    
    /**
     * 搜索布局
     */
    protected RelativeLayout mSearchRL;
    
    /**
     * 筛选关闭按钮
     */
    protected LinearLayout   mSearchResultCloseButton;
    
    /**
     * 筛选结果布局
     */
    protected RelativeLayout mSearchResultLayout;
    
    /**
     * 筛选结果textview
     */
    protected TextView       mSearchResultText;
    
    /**
     * 筛选结果数量
     */
    protected TextView       mSearchResultTextCount;
    
    /**
     * 筛选按钮
     */
    protected RelativeLayout mSearch_funnel_ll;
    
    private ImageView        searchView;
    
    /**
     * 搜索按钮
     */
    protected ImageView      mSearchButton;
    
    /**
     * 搜索按钮布局
     */
    protected RelativeLayout mSearchButtonLayout;
    
    /**
     * "以读"、"未读"、"已办"等标签是否显示:true 显示/false 不显示
     */
    private boolean          isShowMFragmentLL = true;
    
    /**
     * 默认的构造函数
     */
    public PMIPCustomFragment()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        if (container == null)
        {
            return null;
        }
        
        View v = initView(getActivity());
        initListener();
        customThisFragmentView();
        
        return v;
    }
    
    /**
     * 初始化view
     * 
     * @Description<功能详细描述>
     * 
     * @param context
     * @return
     * @LastModifiedDate：2013-12-5
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected View initView(Context context)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_fragment_layout, null);
        mParentRL = (RelativeLayout)v.findViewById(R.id.custom_fragment_rl);
        mFragmentLL = (RelativeLayout)v.findViewById(R.id.fragment_ll);
        mFirstLL = (RelativeLayout)v.findViewById(R.id.fragment_first_ll);
        mSecondLL = (RelativeLayout)v.findViewById(R.id.fragment_second_ll);
        mThirdLL = (RelativeLayout)v.findViewById(R.id.fragment_third_ll);
        
        mheaderLaberLL = (LinearLayout)v.findViewById(R.id.header_label_rl);
        mFirstLabelLL = (RelativeLayout)v.findViewById(R.id.label_first_ll);
        mSecondLabelLL = (RelativeLayout)v.findViewById(R.id.label_second_ll);
        mFirstLabelName = (TextView)v.findViewById(R.id.label_first_ll_tv);
        mSecondLabelName = (TextView)v.findViewById(R.id.label_second_ll_tv);
        
        mFragmentBack = (ImageView)v.findViewById(R.id.fragment_back);
        
        mFragmentSearch = (ImageView)v.findViewById(R.id.fragment_search);
        
        mTitle_tv = (TextView)v.findViewById(R.id.fragment_title);
        
        mOfficalListViewRL = (RelativeLayout)v.findViewById(R.id.custom_listview);
        
        mFirst_choose_line = (ImageView)v.findViewById(R.id.fragment_first_im_choose_lien);
        
        mSecond_choose_line = (ImageView)v.findViewById(R.id.fragment_second_im_choose_lien);
        
        mThird_choose_line = (ImageView)v.findViewById(R.id.fragment_third_im_choose_lien);
        
        mFirst_choose_shadow = (ImageView)v.findViewById(R.id.fragment_first_im_shadow);
        
        mSecond_choose_shadow_1 = (ImageView)v.findViewById(R.id.fragment_second_im_shadow_1);
        
        mSecond_choose_shadow_2 = (ImageView)v.findViewById(R.id.fragment_second_im_shadow_2);
        
        mThird_choose_shadow = (ImageView)v.findViewById(R.id.fragment_third_im_shadow);
        
        mFragment_shadow = (ImageView)v.findViewById(R.id.fragment_shadow);
        
        mSearchRL = (RelativeLayout)v.findViewById(R.id.button_search_layout);
        
        mSearchResultLayout = (RelativeLayout)v.findViewById(R.id.search_layout);
        mSearchResultText = (TextView)v.findViewById(R.id.search_result_tv);
        mSearchResultTextCount = (TextView)v.findViewById(R.id.search_result_tv_count);
        mSearchResultCloseButton = (LinearLayout)v.findViewById(R.id.cancel_search_ll);
        
        mSearch_funnel_ll = (RelativeLayout)v.findViewById(R.id.search_funnel_ll);
        mSearchEditText = (EditText)v.findViewById(R.id.search_edt);
        
        searchView = (ImageView)v.findViewById(R.id.fragment_search);
        mSearchButton = (ImageView)v.findViewById(R.id.search_btn);
        mSearchButtonLayout = (RelativeLayout)v.findViewById(R.id.fragment_search_rl);
        // 获取页面最上层的标签textview
        mFirst_label_name = (TextView)v.findViewById(R.id.fragment_first_ll_tv);
        mSecond_label_name = (TextView)v.findViewById(R.id.fragment_second_ll_tv);
        mThird_label_name = (TextView)v.findViewById(R.id.fragment_third_ll_tv);
        // 获取标签上的个数textview
        mFirst_label_number = (TextView)v.findViewById(R.id.fragment_first_ll_tv_number);
        mSecond_label_number = (TextView)v.findViewById(R.id.fragment_second_ll_tv_number);
        mThird_label_number = (TextView)v.findViewById(R.id.fragment_third_ll_tv_number);
        return v;
    }
    
    /**
     * 初始化监听事件
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-12-5
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected void initListener()
    {
        mFirstLL.setOnClickListener(this);
        mSecondLL.setOnClickListener(this);
        mThirdLL.setOnClickListener(this);
        
        mFirstLabelLL.setOnClickListener(this);
        mSecondLabelLL.setOnClickListener(this);
        
        mFragmentBack.setOnClickListener(this);
        mFragmentSearch.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
        searchView.setOnClickListener(this);
        mSearchResultCloseButton.setOnClickListener(this);
        mSearch_funnel_ll.setOnClickListener(this);
    }
    
    /**
     * 创建且获取目录名
     * 
     * @Description<功能详细描述>
     * 
     * @return
     * @LastModifiedDate：2013-11-8
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected String CreateDirPath(String parentPath)
    {
        
        File file = new File(parentPath);
        if (!file.exists())
        {
            if (file.mkdirs())
            {
                return parentPath;
            }
            else
            {
                return "";
            }
        }
        
        return parentPath;
        
    }
    
    /**
     * 隐藏键盘
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-12-5
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected void hideSoftKey()
    {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        
        if (mSearchEditText != null)
        {
            imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
        }
    }
    
    /**
     * 搜索
     * 
     * @Description<功能详细描述>
     * 
     * @param keyWords
     * @param flag
     * @LastModifiedDate：2013-12-5
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected void searchDocList(String keyWords, String typeId, boolean flag)
    {
        if (flag)
        {
            mSearchRL.setVisibility(View.GONE);
            mSearchResultLayout.setVisibility(View.VISIBLE);
            mSearchResultText.setText(getActivity().getString(R.string.search_key_word, "\"" + keyWords + typeId + "\""));
            mSearchResultTextCount.setText(getActivity().getString(R.string.search_key_word_result, "0"));
            
            addListView(getSearchView(keyWords, typeId));
            
            hideSoftKey();
        }
        else
        {
            mSearchEditText.setText("");
            mSearchRL.setVisibility(View.VISIBLE);
            mSearchResultLayout.setVisibility(View.GONE);
            
            disPlayListView();
        }
    }
    
    /**
     * doc list加载完毕后的监听返回
     */
    protected OnResultListen mResultlisten = new OnResultListen()
                                           {
                                               
                                               @Override
                                               public void updateListView(String docId)
                                               {
                                                   // TODO Auto-generated method stub
                                                   readViewBack(docId);
                                               }
                                               
                                               @Override
                                               public void setOnResultListen()
                                               {
                                                   // TODO Auto-generated method stub
                                                   setViewResult();
                                               }
                                           };
    
    protected PopupWindow    mPopupWindow;
    
    // 显示筛选列表
    protected void showFunnel(String mSearchStr)
    {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.search_funnel_layout, null);
        
        ListView lv = (ListView)view.findViewById(R.id.search_funnel_lv);
        
        String funnels[] = null;
        
        // mSearchStr = "测试1,测试2,cehe1111,测试3,测试4,测试55,测试6,测试7,测试2";
        
        if (mSearchStr != null && !mSearchStr.equals(""))
        {
            funnels = mSearchStr.split(",");
        }
        
        List<String> list = new ArrayList<String>();
        if (funnels != null)
        {
            for (int i = 0; i < funnels.length; i++)
            {
                list.add(funnels[i]);
            }
        }
        
        final OfficalSearchAdpater adatper =
            new OfficalSearchAdpater(getActivity(), list, mSearch_funnel_ll.getHeight());
        lv.setAdapter(adatper);
        
        lv.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                // TODO Auto-generated method stub
                mPopupWindow.dismiss();
                
                mSearchResultLayout.setVisibility(View.VISIBLE);
                
                searchDocList((String)adatper.getItem(arg2), "", true);
                
            }
            
        });
        
        int popViewWith = -1;
        int popViewHeight = -1;
        
        if (list != null && list.size() >= 5)
        {
            popViewHeight = mSearch_funnel_ll.getHeight() * 5;
        }
        else if (list != null && list.size() >= 3)
        {
            popViewHeight = mSearch_funnel_ll.getHeight() * list.size();
        }
        else
        {
            popViewHeight = mSearch_funnel_ll.getHeight() * list.size();
        }
        popViewWith = mSearch_funnel_ll.getWidth();
        
        // 输入框一样长，高度自适应
        mPopupWindow = new PopupWindow(view, popViewWith, popViewHeight);
        
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAsDropDown(mSearch_funnel_ll, 0, -1);
    }
    
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        // 点击时间间隔太短则不触发
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        switch (v.getId())
        {
            case R.id.fragment_back:
                closeThisFragment();
                break;
            
            case R.id.search_btn:
                String mSearchWords = mSearchEditText.getText().toString().trim();
                if (mSearchWords == null || mSearchWords.equals(""))
                {
                    Toast.makeText(getActivity(), "请输入关键字进行查询", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    searchDocList(mSearchWords, "", true);
                }
                break;
            
            case R.id.fragment_first_ll:
            case R.id.fragment_second_ll:
            case R.id.fragment_third_ll:
                switchReadLL(v.getId());
                break;
            
            case R.id.label_first_ll:
            case R.id.label_second_ll:
                switchReadLL(v.getId());
                break;
            
            case R.id.search_funnel_ll:
                showFunnel(mSearchStr);
                break;
            case R.id.cancel_search_ll:
                searchDocList("", "", false);
                break;
            case R.id.fragment_search:
                mSearchEditText.setText("");
                if (mSearchRL.isShown() || mSearchResultLayout.isShown())
                {
                    mSearchRL.setVisibility(View.GONE);
                    mSearchResultLayout.setVisibility(View.GONE);
                    if (isShowMFragmentLL)
                    {
                        mFragmentLL.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    mSearchRL.setVisibility(View.VISIBLE);
                    mFragmentLL.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }
    
    // 将各listview 加入到mOfficalListViewRL中
    public void addListView(View view)
    {
        mOfficalListViewRL.removeAllViews();
        mOfficalListViewRL.addView(view);
    }
    
    public void switchReadLL(int id)
    {
        switch (id)
        {
            case R.id.fragment_first_ll:
                // mThirdLL.bringToFront();
                // mSecondLL.bringToFront();
                // mFirstLL.bringToFront();
                //
                // mFirstLL.setBackgroundResource(R.drawable.switching_1);
                // mSecondLL.setBackgroundResource(R.drawable.switching_1);
                // mThirdLL.setBackgroundResource(R.drawable.switching_1);
                //
                // mFirst_choose_line.setVisibility(View.VISIBLE);
                // mSecond_choose_line.setVisibility(View.INVISIBLE);
                // mThird_choose_line.setVisibility(View.INVISIBLE);
                
                onRelayout(mSelectMode, 1);
                break;
            case R.id.fragment_second_ll:
                // mFirstLL.bringToFront();
                // mThirdLL.bringToFront();
                // mSecondLL.bringToFront();
                //
                // mFirstLL.setBackgroundResource(R.drawable.switching_1);
                // mSecondLL.setBackgroundResource(R.drawable.switching_1);
                // mThirdLL.setBackgroundResource(R.drawable.switching_1);
                //
                // mFirst_choose_line.setVisibility(View.INVISIBLE);
                // mSecond_choose_line.setVisibility(View.VISIBLE);
                // mThird_choose_line.setVisibility(View.INVISIBLE);
                onRelayout(mSelectMode, 2);
                break;
            case R.id.fragment_third_ll:
                // mFirstLL.bringToFront();
                // mSecondLL.bringToFront();
                // mThirdLL.bringToFront();
                //
                // mFirstLL.setBackgroundResource(R.drawable.switching_1);
                // mSecondLL.setBackgroundResource(R.drawable.switching_1);
                // mThirdLL.setBackgroundResource(R.drawable.switching_1);
                //
                // mFirst_choose_line.setVisibility(View.INVISIBLE);
                // mSecond_choose_line.setVisibility(View.INVISIBLE);
                // mThird_choose_line.setVisibility(View.VISIBLE);
                onRelayout(mSelectMode, 3);
                break;
            case R.id.label_first_ll:
                mFirstLabelLL.setBackgroundResource(R.drawable.title_qh_2);
                mSecondLabelLL.setBackgroundResource(0);
                mFirstLabelName.setTextColor(this.getResources().getColor(R.color.white));
                mSecondLabelName.setTextColor(this.getResources().getColor(R.color.title_label));
                // onRelayout(mSelectMode, 1);
                break;
            case R.id.label_second_ll:
                mFirstLabelLL.setBackgroundResource(0);
                mSecondLabelLL.setBackgroundResource(R.drawable.title_qh_2);
                mFirstLabelName.setTextColor(this.getResources().getColor(R.color.title_label));
                mSecondLabelName.setTextColor(this.getResources().getColor(R.color.white));
                // onRelayout(mSelectMode, 2);
                break;
            
            default:
                break;
        }
        // mFragmentLL.invalidate();
        clickRadioButton(id);
    }
    
    int selected_width_third    = -1;
    
    int unselected_width_third  = -1;
    
    int selected_width_second   = -1;
    
    int unselected_width_second = -1;
    
    int mLLHeight               = -1;
    
    int mSelectMode             = -1;
    
    /**
     * 初始化“未读”、“已读”、“草稿”布局
     * 
     * @Description<功能详细描述>
     * 
     * @param first
     * @param second
     * @param third
     * @LastModifiedDate：2014-1-10
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    protected void initReadLineLayout(int selectMode)
    {
        
        if (selectMode == TWOMODE)
        {
            mheaderLaberLL.setVisibility(View.VISIBLE);
            mTitle_tv.setVisibility(View.GONE);
        }
        else
        {
            mheaderLaberLL.setVisibility(View.GONE);
            mTitle_tv.setVisibility(View.VISIBLE);
        }
        
        if (selectMode == NOMODE || selectMode == TWOMODE)
        {
            mFragmentLL.setVisibility(View.GONE);
            mFragment_shadow.setVisibility(View.GONE);
            isShowMFragmentLL = false;
            return;
        }
        int mScreenWidth = GlobalState.getInstance().getmScreen_With();
        
        mLLHeight = (int)(((float)mScreenWidth / 720) * 110);
        
        int mDip = GlobalState.getInstance().getmDensityDpi();
        // int mSpace = (int)(SPACEING * ((float)mDip / 160));
        
        if (selectMode == TWOMODE)
        {
            unselected_width_second = (int)(mScreenWidth * ((float)350 / 720));
            selected_width_second = mScreenWidth - unselected_width_second;
            mSelectMode = TWOMODE;
            
            onRelayout(mSelectMode, 1);
            
        }
        else if (selectMode == THREEMODE)
        {
            unselected_width_third = (int)(mScreenWidth * ((float)220 / 720));
            selected_width_third = mScreenWidth - 2 * unselected_width_third;
            mSelectMode = THREEMODE;
            
            onRelayout(mSelectMode, 1);
        }
        
    }
    
    public void onRelayout(int displayMode, int selected_index)
    {
        if (displayMode == TWOMODE)
        {
            if (selected_index == 1)
            {
                LayoutParams lp1 = (LayoutParams)mFirstLL.getLayoutParams();
                lp1.width = selected_width_second;
                lp1.height = mLLHeight;
                mFirstLL.setLayoutParams(lp1);
                
                LayoutParams lp2 = (LayoutParams)mSecondLL.getLayoutParams();
                lp2.width = unselected_width_second;
                lp2.height = mLLHeight;
                mSecondLL.setLayoutParams(lp2);
                
                mFirst_choose_shadow.setVisibility(View.VISIBLE);
                mSecond_choose_shadow_1.setVisibility(View.GONE);
                mSecond_choose_shadow_2.setVisibility(View.GONE);
                mThird_choose_shadow.setVisibility(View.GONE);
                
                mFirstLL.setVisibility(View.VISIBLE);
                mSecondLL.setVisibility(View.VISIBLE);
                mThirdLL.setVisibility(View.GONE);
                
                mFirst_choose_line.setVisibility(View.VISIBLE);
                mSecond_choose_line.setVisibility(View.INVISIBLE);
                mThird_choose_line.setVisibility(View.INVISIBLE);
            }
            else if (selected_index == 2)
            {
                LayoutParams lp1 = (LayoutParams)mFirstLL.getLayoutParams();
                lp1.width = unselected_width_second;
                lp1.height = mLLHeight;
                mFirstLL.setLayoutParams(lp1);
                
                LayoutParams lp2 = (LayoutParams)mSecondLL.getLayoutParams();
                lp2.width = selected_width_second;
                lp2.height = mLLHeight;
                mSecondLL.setLayoutParams(lp2);
                
                mFirst_choose_shadow.setVisibility(View.GONE);
                mSecond_choose_shadow_1.setVisibility(View.VISIBLE);
                mSecond_choose_shadow_2.setVisibility(View.GONE);
                mThird_choose_shadow.setVisibility(View.GONE);
                
                mFirstLL.setVisibility(View.VISIBLE);
                mSecondLL.setVisibility(View.VISIBLE);
                mThirdLL.setVisibility(View.GONE);
                
                mFirst_choose_line.setVisibility(View.INVISIBLE);
                mSecond_choose_line.setVisibility(View.VISIBLE);
                mThird_choose_line.setVisibility(View.INVISIBLE);
            }
        }
        else if (displayMode == THREEMODE)
        {
            if (selected_index == 1)
            {
                LayoutParams lp1 = (LayoutParams)mFirstLL.getLayoutParams();
                lp1.width = selected_width_third;
                lp1.height = mLLHeight;
                mFirstLL.setLayoutParams(lp1);
                
                LayoutParams lp2 = (LayoutParams)mSecondLL.getLayoutParams();
                lp2.width = unselected_width_third;
                lp2.height = mLLHeight;
                mSecondLL.setLayoutParams(lp2);
                
                LayoutParams lp3 = (LayoutParams)mThirdLL.getLayoutParams();
                lp3.width = unselected_width_third;
                lp3.height = mLLHeight;
                mThirdLL.setLayoutParams(lp3);
                
                mFirstLL.setVisibility(View.VISIBLE);
                mSecondLL.setVisibility(View.VISIBLE);
                mThirdLL.setVisibility(View.VISIBLE);
                
                mFirst_choose_shadow.setVisibility(View.VISIBLE);
                mSecond_choose_shadow_1.setVisibility(View.GONE);
                mSecond_choose_shadow_2.setVisibility(View.VISIBLE);
                mThird_choose_shadow.setVisibility(View.GONE);
                
                mFirst_choose_line.setVisibility(View.VISIBLE);
                mSecond_choose_line.setVisibility(View.INVISIBLE);
                mThird_choose_line.setVisibility(View.INVISIBLE);
            }
            else if (selected_index == 2)
            {
                LayoutParams lp1 = (LayoutParams)mFirstLL.getLayoutParams();
                lp1.width = unselected_width_third;
                lp1.height = mLLHeight;
                mFirstLL.setLayoutParams(lp1);
                
                LayoutParams lp2 = (LayoutParams)mSecondLL.getLayoutParams();
                lp2.width = selected_width_third;
                lp2.height = mLLHeight;
                mSecondLL.setLayoutParams(lp2);
                
                LayoutParams lp3 = (LayoutParams)mThirdLL.getLayoutParams();
                lp3.width = unselected_width_third;
                lp3.height = mLLHeight;
                mThirdLL.setLayoutParams(lp3);
                
                mFirstLL.setVisibility(View.VISIBLE);
                mSecondLL.setVisibility(View.VISIBLE);
                mThirdLL.setVisibility(View.VISIBLE);
                
                mFirst_choose_shadow.setVisibility(View.GONE);
                mSecond_choose_shadow_1.setVisibility(View.VISIBLE);
                mSecond_choose_shadow_2.setVisibility(View.VISIBLE);
                mThird_choose_shadow.setVisibility(View.GONE);
                
                mFirst_choose_line.setVisibility(View.INVISIBLE);
                mSecond_choose_line.setVisibility(View.VISIBLE);
                mThird_choose_line.setVisibility(View.INVISIBLE);
            }
            else if (selected_index == 3)
            {
                LayoutParams lp1 = (LayoutParams)mFirstLL.getLayoutParams();
                lp1.width = unselected_width_third;
                lp1.height = mLLHeight;
                mFirstLL.setLayoutParams(lp1);
                
                LayoutParams lp2 = (LayoutParams)mSecondLL.getLayoutParams();
                lp2.width = unselected_width_third;
                lp2.height = mLLHeight;
                mSecondLL.setLayoutParams(lp2);
                
                LayoutParams lp3 = (LayoutParams)mThirdLL.getLayoutParams();
                lp3.width = selected_width_third;
                lp3.height = mLLHeight;
                mThirdLL.setLayoutParams(lp3);
                
                mFirstLL.setVisibility(View.VISIBLE);
                mSecondLL.setVisibility(View.VISIBLE);
                mThirdLL.setVisibility(View.VISIBLE);
                
                mFirst_choose_shadow.setVisibility(View.GONE);
                mSecond_choose_shadow_1.setVisibility(View.VISIBLE);
                mSecond_choose_shadow_2.setVisibility(View.GONE);
                mThird_choose_shadow.setVisibility(View.VISIBLE);
                
                mFirst_choose_line.setVisibility(View.INVISIBLE);
                mSecond_choose_line.setVisibility(View.INVISIBLE);
                mThird_choose_line.setVisibility(View.VISIBLE);
            }
            
        }
    }
    
    // 重构整体的view，根据各个模块设置个控件的显示与隐藏
    public abstract void customThisFragmentView();
    
    // 根据标识选择显示的listview
    public abstract void disPlayListView();
    
    // 获取搜索view
    public abstract View getSearchView(String keyWords, String typeId);
    
    // 文档被打开后，调用的监听方法
    public abstract void readViewBack(String id);
    
    // 获取列表结束后，调用的监听方法
    public abstract void setViewResult();
    
    // 实例化listview方法
    public abstract Object initOfficalListView(Object view, String keyWords, String typeId);
    
    // 切换待办、已办、未读、已读按钮时的监听
    public abstract void clickRadioButton(int id);
    
}
