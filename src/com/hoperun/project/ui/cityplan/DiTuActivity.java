/*
 * File name:  DiTuFragment.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  wen_tao
 * Last modified date:  2013-12-17
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.cityplan;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.hoperun.manager.adpter.cityplan.MapSearchAdapter;
import com.hoperun.manager.adpter.cityplan.MapSearchAdapter.MyMKPoiInfo;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.miplygphone.R;

/**
 * 电子地图的Fragment
 * 
 * @Description 电子地图的Fragment
 * 
 * @author wen_tao
 * @Version [版本号, 2013-12-17]
 */
public class DiTuActivity extends Activity implements OnClickListener
{
    /**
     * 调用百度地图的key
     */
    private static final String  BMAPKEY           = "5kvMPyGn31VlcrRcRhYYxSGS";
    
    /**
     * 当前的上下文
     */
    private static Context       mapContext;
    
    /**
     * 是否手动定位
     */
    private boolean              isRequest         = false;
    
    /**
     * 是否第一次自动定位
     */
    private boolean              isFirstLoc        = true;
    
    /**
     * 初始化百度地图
     */
    private BMapManager          mBMapMan          = null;
    
    /**
     * 百度地图控件
     */
    private MapView              mMapView          = null;
    
    /**
     * 定位监听
     */
    private LocationClient       mLocClient;
    
    /**
     * 定位数据存放对象
     */
    private LocationData         locData           = null;
    
    /**
     * 当前所在城市
     */
    private static String        nowCity           = "北京市";
    
    /**
     * 定位监听类
     */
    public MyLocationListenner   myListener        = new MyLocationListenner();
    
    /**
     * 地图管理类
     */
    private MapController        mMapController    = null;
    
    /**
     * 当前位置的布局层：为了使用dispatchTap()，在当前位置弹出pop
     */
    private locationOverlay      myLocationOverlay = null;
    
    /**
     * 定位按钮
     */
    private ImageButton          requestLocButton  = null;
    
    // 返回按钮
    protected ImageView          mFragmentBack;
    
    /**
     * 弹出的pop：用于显示：1、我的位置2、当前点击的搜索标示建筑
     */
    private PopupOverlay         pop               = null;
    
    /**
     * pop层的text提示
     */
    private TextView             popupText         = null;
    
    /**
     * 弹出pop的xml
     */
    private View                 viewCache         = null;
    
    /**
     * 搜索输入框
     */
    private EditText             searchEdit        = null;
    
    /**
     * 展示搜索到的数据的listView控件
     */
    private ListView             searchListView    = null;
    
    /**
     * 搜索到的信息的适配器
     */
    private MapSearchAdapter     simpleAdapter     = null;
    
    /**
     * 用于地图搜索
     */
    private MKSearch             mSearch           = null;
    
    /**
     * 搜索按钮
     */
    private Button               searchbutton      = null;
    
    /**
     * 搜索布局
     */
    protected RelativeLayout     mSearchRL;
    
    /**
     * 地图页面搜索按钮
     */
    private ImageView            searchView;
    
    /**
     * 等待框布局
     */
    private RelativeLayout       loadLayout;
    
    /**
     * 等待图片
     */
    private ImageView            loadImageView;
    
    /**
     * 正在加载过程中
     */
    private boolean              isLoading         = false;
    
    /**
     * 地图右侧的按钮
     */
    private ImageButton          search_open       = null;
    
    /**
     * 地图右侧的按钮：被拉出
     */
    private ImageButton          search_close      = null;
    
    /**
     * 显示具体记录信息的布局
     */
    private RelativeLayout       search_info       = null;
    
    /**
     * 存放MKPoiInfo点的list
     */
    private ArrayList<MKPoiInfo> listPoint         = new ArrayList<MKPoiInfo>();
    
    /**
     * 显示地图上的坐标信息
     */
    private PoiOverlay           poiOverlay        = null;
    
    /**
     * 卫星图
     */
    private ImageButton          shituButton       = null;
    
    /**
     * 搜索到的结果显示
     */
    private TextView             searchHintView    = null;
    
    /**
     * 当前窗口是否被关闭：当用户发送查询请求、关闭该窗口后，不再显示百度地图连接的返回信息
     */
    private boolean              isshow;
    
    /**
     * 显示查询信息布局
     */
    private RelativeLayout       layout            = null;
    
    /**
     * 百度地图返回的网络超时时长:ms
     */
    private final long           overTime          = 15 * 1000;
    
    /**
     * 记录查询列表子控件索引的记录数
     */
    private static int           itemSequence      = -1;
    
    /**
     * 网络超时提示权限
     */
    private boolean              isAllowShowWarm;
    
    /**
     * 网络超时定时器
     */
    private MyCount              myTick            = new MyCount(overTime, 1000);
    
    @Override
    public void onDestroy()
    {
        mMapView.destroy();
        if (mBMapMan != null)
        {
            mBMapMan.destroy();
            mBMapMan = null;
            mLocClient.stop();
        }
        super.onDestroy();
    }
    
    @Override
    public void onPause()
    {
        mMapView.onPause();
        if (mBMapMan != null)
        {
            mBMapMan.stop();
        }
        super.onPause();
    }
    
    @Override
    public void onResume()
    {
        mMapView.onResume();
        if (mBMapMan != null)
        {
            mBMapMan.start();
        }
        super.onResume();
    }
    
    /**
     * 重载方法
     * 
     * @param savedInstanceState
     * @author wen_tao
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mapContext = this.getApplicationContext();
        // 获取百度地图管理对象
        mBMapMan = new BMapManager(mapContext);
        // 注册并初始化百度地图控件
        mBMapMan.init(BMAPKEY, new MyGeneralListener());
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.ditu, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(v);
        isshow = true;
        
        layout = (RelativeLayout)v.findViewById(R.id.relative);
        // 不包括搜索的相关控件
        initView(v);
        
        initMapInfo(v);
        
        initRequestLocation();
        
        initSearch(v);
    }
    
    //
    // /**
    // * 重载方法
    // *
    // * @param inflater
    // * @param container
    // * @param savedInstanceState
    // * @return
    // * @author wen_tao
    // */
    // @Override
    // public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    // {
    // mapContext = mActivity.getApplicationContext();
    // // 获取百度地图管理对象
    // mBMapMan = new BMapManager(mapContext);
    // // 注册并初始化百度地图控件
    // mBMapMan.init(BMAPKEY, new MyGeneralListener());
    //
    // View v = inflater.inflate(R.layout.ditu, null);
    //
    // isshow = true;
    //
    // layout = (RelativeLayout)v.findViewById(R.id.relative);
    // // 不包括搜索的相关控件
    // initView(v);
    //
    // initMapInfo(v);
    //
    // initRequestLocation();
    //
    // initSearch(v);
    // return v;
    // }
    
    /**
     * 初始化控件布局控件
     */
    private void initView(View v)
    {
        mMapView = (MapView)v.findViewById(R.id.bmapsView);
        // 定位按钮
        requestLocButton = (ImageButton)v.findViewById(R.id.dingwei);
        requestLocButton.setOnClickListener(this);
        
        mSearchRL = (RelativeLayout)v.findViewById(R.id.search_layout);
        
        searchEdit = (EditText)v.findViewById(R.id.search_edt);
        searchView = (ImageView)v.findViewById(R.id.fragment_search);
        searchView.setOnClickListener(this);
        mFragmentBack = (ImageView)v.findViewById(R.id.fragment_back);
        mFragmentBack.setOnClickListener(this);
        searchListView = (ListView)v.findViewById(R.id.searchInfo);
        simpleAdapter = new MapSearchAdapter(mapContext);
        // 设置自动定位的城市
        searchListView.setAdapter(simpleAdapter);
        searchListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                if (OnClickUtil.isMostPost() || loadLayout.isShown())
                {
                    return;
                }
                // 设置当前的子控件在地图上的索引值
                if (simpleAdapter.getSequence(arg2))
                {
                    simpleAdapter.setSequence(arg2, itemSequence++);
                }
                
                MKPoiInfo mKPoiInfo = (MKPoiInfo)simpleAdapter.getItem(arg2);
                if (!listPoint.contains(mKPoiInfo))
                {
                    listPoint.add(mKPoiInfo);
                }
                if (!mMapView.getOverlays().contains(poiOverlay))
                {
                    poiOverlay = new MyLocalPoiOverlay(DiTuActivity.this, mMapView);
                    mMapView.getOverlays().add(poiOverlay);
                }
                poiOverlay.setData(listPoint);
                if (mKPoiInfo.pt != null)
                {
                    if (pop == null)
                    {
                        createPaopao();
                    }
                    mMapController.animateTo(mKPoiInfo.pt);
                    popupText.setText(mKPoiInfo.name);
                    pop.showPopup(getBitmapFromView(popupText), new GeoPoint((int)(mKPoiInfo.pt.getLatitudeE6()),
                        (int)(mKPoiInfo.pt.getLongitudeE6())), 40);
                }
                mMapView.refresh();
            }
        });
        // 搜索按钮
        searchbutton = (Button)v.findViewById(R.id.search_btn);
        searchbutton.setOnClickListener(this);
        
        search_open = (ImageButton)v.findViewById(R.id.open_list);
        search_open.setOnClickListener(this);
        search_close = (ImageButton)v.findViewById(R.id.close_list);
        search_close.setOnClickListener(this);
        search_info = (RelativeLayout)v.findViewById(R.id.huchu_list);
        
        shituButton = (ImageButton)v.findViewById(R.id.shitu);
        shituButton.setOnClickListener(this);
        searchHintView = (TextView)v.findViewById(R.id.search_hint);
        
        // 等待
        loadLayout = (RelativeLayout)v.findViewById(R.id.load_layout);
        loadImageView = (ImageView)v.findViewById(R.id.waitdialog_img);
        
    }
    
    /**
     * 初始化地图控件信息
     */
    private void initMapInfo(View v)
    {
        
        mMapView.setBuiltInZoomControls(true); // 设置启用内置的缩放控件
        mMapView.getController().enableClick(true);
        
        mMapController = mMapView.getController(); // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
        GeoPoint point = new GeoPoint((int)(31.980108 * 1E6), (int)(118.765791 * 1E6)); // 用给定的经纬度构造一个GeoPoint，单位是微度 (度
                                                                                        // * 1E6)
        mMapController.setCenter(point); // 设置地图中心点
        mMapController.setZoom(14); // 设置地图zoom级别
        // 双击地图放大
        mMapView.setDoubleClickZooming(true);
    }
    
    /**
     * 初始化百度定位：包括首次进入的自动定位
     */
    private void initRequestLocation()
    {
        // 以下部分是定位用的
        mLocClient = new LocationClient(DiTuActivity.this);
        locData = new LocationData();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
        mLocClient.start();
        // 定位图层初始化
        myLocationOverlay = new locationOverlay(mMapView);
        // 设置定位数据
        myLocationOverlay.setData(locData);
        // 添加定位图层
        mMapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableCompass();
        
        // 当传入marker为null时，使用默认图标绘制
        // myLocationOverlay.setMarker(getResources().getDrawable(R.drawable.arrow_down));
        // 修改定位数据后刷新图层生效
        mMapView.refresh();
    }
    
    /**
     * 初始化搜素相关的控件及空间监听
     */
    private void initSearch(View v)
    {
        // 初始化搜索模块，注册搜索事件监听
        mSearch = new MKSearch();
        mSearch.init(mBMapMan, new MKSearchListener()
        {
            // 在此处理详情页结果
            @Override
            public void onGetPoiDetailSearchResult(int type, int error)
            {
                // 当前窗口不在最前端或当前窗口被关闭
                if (!isshow || !isAllowShowWarm)
                {
                    return;
                }
                myTick.cancel();
                closeProgressDialog();
                if (error != 0)
                {
                    Toast.makeText(mapContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(mapContext, "成功，查看详情页面", Toast.LENGTH_SHORT).show();
                }
            }
            
            /**
             * 在此处理poi搜索结果
             */
            public void onGetPoiResult(MKPoiResult res, int type, int error)
            {
                // 当前窗口不在最前端或当前窗口被关闭
                if (!isshow || !isAllowShowWarm)
                {
                    return;
                }
                myTick.cancel();
                closeProgressDialog();
                // 错误号可参考MKEvent中的定义
                if (error != 0 || res == null)
                {
                    searchHintView.setText("共搜索到0条结果");
                    return;
                }
                
                simpleAdapter.clearInfo();
                if (res.getCurrentNumPois() > 0)
                {
                    for (int i = 0; i < res.getCurrentNumPois(); i++)
                    {
                        MyMKPoiInfo info = simpleAdapter.new MyMKPoiInfo(-1, res.getPoi(i));
                        simpleAdapter.addInfo(info);
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                searchHintView.setText("共搜索到" + res.getCurrentNumPois() + "条结果");
            }
            
            public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error)
            {
            }
            
            public void onGetTransitRouteResult(MKTransitRouteResult res, int error)
            {
            }
            
            public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error)
            {
            }
            
            public void onGetAddrResult(MKAddrInfo res, int error)
            {
            }
            
            public void onGetBusDetailResult(MKBusLineResult result, int iError)
            {
            }
            
            /**
             * 更新建议列表
             */
            @Override
            public void onGetSuggestionResult(MKSuggestionResult res, int arg1)
            {
            }
            
            @Override
            public void onGetShareUrlResult(MKShareUrlResult result, int type, int error)
            {
            }
        });
    }
    
    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    class MyGeneralListener implements MKGeneralListener
    {
        
        @Override
        public void onGetNetworkState(int iError)
        {
            // 当前窗口不在最前端或当前窗口被关闭
            if (!isshow || !isAllowShowWarm)
            {
                return;
            }
            myTick.cancel();
            closeProgressDialog();
            if (iError == MKEvent.ERROR_NETWORK_CONNECT)
            {
                Toast.makeText(mapContext, "网络连接错误，请稍候再试！", Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA)
            {
                Toast.makeText(mapContext, "输入正确的检索条件！", Toast.LENGTH_LONG).show();
            }
        }
        
        @Override
        public void onGetPermissionState(int iError)
        {
            // 当前窗口不在最前端或当前窗口被关闭
            if (!isshow || !isAllowShowWarm)
            {
                return;
            }
            myTick.cancel();
            closeProgressDialog();
            if (iError == MKEvent.ERROR_PERMISSION_DENIED)
            {
                // 授权Key错误：
                Toast.makeText(mapContext, "授权Key错误", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(mapContext, "无法建立与服务器的连接", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    // 继承MyLocationOverlay重写dispatchTap实现点击处理
    public class locationOverlay extends MyLocationOverlay
    {
        
        public locationOverlay(MapView mapView)
        {
            super(mapView);
        }
        
        @Override
        protected boolean dispatchTap()
        {
            // 处理点击事件,弹出泡泡
            
            if (pop == null)
            {
                createPaopao();
            }
            popupText.setBackgroundResource(R.drawable.popup);
            popupText.setText("我的位置");
            pop.showPopup(getBitmapFromView(popupText), new GeoPoint((int)(locData.latitude * 1e6),
                (int)(locData.longitude * 1e6)), 8);
            return true;
        }
        
    }
    
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener
    {
        
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            if (location == null)
                return;
            
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            // 如果不显示定位精度圈，将accuracy赋值为0即可
            locData.accuracy = location.getRadius();
            // 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
            locData.direction = location.getDerect();
            nowCity = location.getCity();
            // 更新定位数据
            myLocationOverlay.setData(locData);
            // 更新图层数据执行刷新后生效
            // 是手动触发请求或首次定位时，移动到定位点
            if (isRequest || isFirstLoc)
            {
                // 移动地图到定位点
                mMapView.refresh();
                Log.d("LocationOverlay", "receive location, animate to it");
                mMapController.animateTo(new GeoPoint((int)(locData.latitude * 1e6), (int)(locData.longitude * 1e6)));
                isRequest = false;
            }
            // 首次定位完成
            isFirstLoc = false;
        }
        
        @Override
        public void onReceivePoi(BDLocation poiLocation)
        {
            if (poiLocation == null)
            {
                return;
            }
        }
    }
    
    /**
     * 
     * 获取一个view的背景图片
     * 
     * @Description 获取一个view的背景图片
     * 
     * @param view
     * @return
     * @LastModifiedDate：2014-1-12
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    public static Bitmap getBitmapFromView(View view)
    {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }
    
    /**
     * 创建弹出泡泡图层
     */
    public void createPaopao()
    {
        viewCache = DiTuActivity.this.getLayoutInflater().inflate(R.layout.ditu_pop, null);
        popupText = (TextView)viewCache.findViewById(R.id.textcache);
        // 泡泡点击响应回调
        PopupClickListener popListener = new PopupClickListener()
        {
            boolean isOpean = true;
            
            @Override
            public void onClickedPopup(int index)
            {
                if (isOpean)
                {
                    pop.hidePop();
                }
                else
                {
                    pop.showPopup(getBitmapFromView(popupText), new GeoPoint((int)(locData.latitude * 1e6),
                        (int)(locData.longitude * 1e6)), 8);
                }
            }
        };
        pop = new PopupOverlay(mMapView, popListener);
    }
    
    /**
     * 手动触发一次定位请求
     */
    public void requestLocClick()
    {
        isRequest = true;
        mLocClient.requestLocation();
        Toast.makeText(DiTuActivity.this, "正在定位……", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 重载方法
     * 
     * @param v
     * @author wen_tao
     */
    @Override
    public void onClick(View v)
    {
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        
        switch (v.getId())
        {
            case R.id.dingwei:
                requestLocClick();
                break;
            
            case R.id.search_btn:
                simpleAdapter.clearInfo();
                simpleAdapter.notifyDataSetChanged();
                isAllowShowWarm = true;
                itemSequence = 1;
                myTick.start();
                hideSoftKey();
                if (pop != null)
                {
                    pop.hidePop();
                }
                String searchStr = searchEdit.getText().toString().trim();
                if (searchStr == null || "".equals(searchStr))
                {
                    Toast.makeText(DiTuActivity.this, "请输入搜索信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    mSearch.poiSearchInCity(nowCity, searchStr);
                    
                    if (mMapView.getOverlays().contains(poiOverlay))
                    {
                        mMapView.getOverlays().remove(poiOverlay);
                    }
                    searchHintView.setText("查询结果");
                    listPoint.clear();
                    if (!search_info.isShown())
                    {
                        openSearchList();
                    }
                    showProgressDialog();
                }
                
                break;
            case R.id.open_list:
                // 此处弹出右侧的列表
                openSearchList();
                break;
            case R.id.close_list:
                // 此处关闭右侧的列表
                closeSearchList();
                break;
            case R.id.shitu:
                if (mMapView.isSatellite())
                {
                    mMapView.setSatellite(false);
                    shituButton.setImageResource(R.drawable.weix);
                }
                else
                {
                    mMapView.setSatellite(true);
                    shituButton.setImageResource(R.drawable.normal);
                }
                break;
            case R.id.fragment_back:
                closeThisActivity();
                break;
            case R.id.fragment_search:
                if (mSearchRL.isShown())
                {
                    mSearchRL.setVisibility(View.GONE);
                }
                else
                {
                    searchEdit.setText("");
                    mSearchRL.setVisibility(View.VISIBLE);
                    // if (layout.isShown())
                    // {
                    // MarginLayoutParams marginLayou = (MarginLayoutParams)layout.getLayoutParams();
                    // marginLayou.bottomMargin = 0;
                    // marginLayou.height = layout.getLayoutParams().height - mSearchRL.getLayoutParams().height;
                    // layout.setLayoutParams(marginLayou);
                    // }
                }
                break;
        }
    }
    
    /**
     * 
     * 打开搜索到的列表
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-12-19
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    private void openSearchList()
    {
        // 播放打开动画
        Animation mbottomtOut = new AnimationUtils().loadAnimation(mapContext, R.anim.fromrightin);
        search_info.setAnimation(mbottomtOut);
        search_info.setVisibility(View.VISIBLE);
        search_close.setVisibility(View.VISIBLE);
        // if (mSearchRL.isShown())
        // {
        // int weight = mSearchRL.getLayoutParams().height;
        // MarginLayoutParams marginLayou = (MarginLayoutParams)layout.getLayoutParams();
        // marginLayou.bottomMargin = 0;
        // marginLayou.height = marginLayou.height - weight;
        // layout.setLayoutParams(marginLayou);
        // }
        layout.setVisibility(View.VISIBLE);
        search_open.setVisibility(View.GONE);
    }
    
    /**
     * 
     * 关闭搜索到的列表
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-12-19
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    private void closeSearchList()
    {
        // 播放关闭动画
        Animation mbottomtOut = new AnimationUtils().loadAnimation(mapContext, R.anim.fromrightout);
        search_info.setAnimation(mbottomtOut);
        search_info.setVisibility(View.GONE);
        search_close.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        search_open.setVisibility(View.VISIBLE);
    }
    
    /**
     * 
     * 本地自行创建的地图视图层
     * 
     * @Description<功能详细描述>
     * 
     * @author wen_tao
     * @Version [版本号, 2013-12-20]
     */
    class MyLocalPoiOverlay extends PoiOverlay
    {
        
        /**
         * <默认构造函数>
         */
        public MyLocalPoiOverlay(Activity arg0, MapView arg1)
        {
            super(arg0, arg1);
            
        }
        
        /**
         * 重载方法
         * 
         * @param arg0
         * @return
         * @author wen_tao
         */
        @Override
        protected boolean onTap(int arg0)
        {
            if (pop == null)
            {
                createPaopao();
            }
            MKPoiInfo mkPoiInfo = this.getPoi(arg0);
            popupText.setBackgroundResource(R.drawable.popup);
            popupText.setText(mkPoiInfo.name);
            pop.showPopup(getBitmapFromView(popupText), new GeoPoint((int)(mkPoiInfo.pt.getLatitudeE6()),
                (int)(mkPoiInfo.pt.getLongitudeE6())), 40);
            return true;
        }
    }
    
    /**
     * 重载方法
     * 
     * @param keyId
     * @return
     * @author wen_tao
     */
    @Override
    public void onBackPressed()
    {
        if (isLoading)
        {
            closeProgressDialog();
        }
        closeThisActivity();
    }
    
    /**
     * 重载方法
     * 
     * @author wen_tao
     */
    public void closeThisActivity()
    {
        setResult(11);
        finish();
        isshow = false;
        myTick.cancel();
    }
    
    /**
     * 
     * 显示等待框
     * 
     * @Description 显示等待框
     * 
     * @LastModifiedDate：2013-10-23
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    private void showProgressDialog()
    {
        loadLayout.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable)loadImageView.getBackground();
        animationDrawable.start();
        isLoading = true;
    }
    
    /**
     * 
     * 关闭等待框
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-23
     * @author wen_tao
     * @EditHistory：<修改内容><修改人>
     */
    private void closeProgressDialog()
    {
        loadLayout.setVisibility(View.GONE);
        isLoading = false;
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
        InputMethodManager imm = (InputMethodManager)DiTuActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        
        if (searchEdit != null)
        {
            imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
        }
    }
    
    /**
     * 定时器类
     */
    public class MyCount extends CountDownTimer
    {
        public MyCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }
        
        @Override
        public void onTick(long millisUntilFinished)
        {
        }
        
        @Override
        public void onFinish()
        {
            if (!isshow)
            {
                return;
            }
            Toast.makeText(DiTuActivity.this, "网络连接超时!", Toast.LENGTH_SHORT).show();
            isAllowShowWarm = false;
            closeProgressDialog();
        }
    }
}