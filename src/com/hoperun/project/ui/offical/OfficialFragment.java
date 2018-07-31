package com.hoperun.project.ui.offical;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoperun.manager.adpter.offical.OfficalDocListAdapter;
import com.hoperun.manager.components.DropDownRefreshListView;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.mipmanager.model.entityMetaData.GetNextModuleListInfo;
import com.hoperun.mipmanager.model.entityModule.offical.GWDocModule;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.MessageIdConstants;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.miplygphone.R;
import com.hoperun.project.ui.baseui.fragment.PMIPBaseSecondFragment;

/**
 * 
 * 公文流转，一级菜单
 * 
 * @Description<功能详细描述>
 * 
 * @author shen_feng
 * @Version [版本号, 2013-9-12]
 */
public class OfficialFragment extends PMIPBaseSecondFragment implements OnItemClickListener, OnClickListener
{
    /**
     * 公文流转2级菜单
     */
    private DropDownRefreshListView mLvMain;
    
    /** activity **/
    private Activity                mActivity;
    
    /** 公文流转一级栏目adpter **/
    private OfficalDocListAdapter   fileListAdapter;
    
    // 所在模块的文件路径
    private String                  parentsPath;
    
    // 所在模块的id
    private String                  funcId;
    
    // 所在模块的id
    private String                  funName;
    
    // 文件主目录
    private String                  path;
    
    // 查询回来的list数据
    private MetaResponseBody        responseBuzBody;
    
    // 是否离线 true为离线，false为在线
    private boolean                 offLine                  = false;
    
    // 用户名
    private String                  user                     = "用户";
    
    /**
     * 该类栏目的标题
     */
    private TextView                tv_title;
    
    /**
     * 关闭按钮
     */
    private RelativeLayout          ll_colse;
    
    /** 等待框布局 **/
    private RelativeLayout          loadLayout;
    
    /** 等待图片 **/
    private ImageView               loadImageView;
    
    /**
     * 正在加载过程中
     */
    private boolean                 isLoading                = false;
    
    /**
     * 该页面是否可以关闭该主页面
     */
    private boolean                 isCanClose               = true;
    
    /**
     * 获取公文流转一级列表的task实例
     */
    private NetTask                 mGetDocModuleHttpCreator = null;
    
    /**
     * 返回按钮
     */
    protected ImageView             mFragmentBack;
    
    /**
     * 重载方法
     * 
     * @param requestType 请求id
     * @param objHeader 返回的头
     * @param obj 返回的body
     * @param error 是否返回成功
     * @param errorCode 错误码
     * @author shen_feng
     */
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object obj, boolean error, int errorCode)
    {
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.GETDOCMODULELIST:
                    closeProgressDialog();
                    if (obj != null)
                    {
                        responseBuzBody = (MetaResponseBody)obj;
                        GlobalState.getInstance().setmGwlzFirstModule(responseBuzBody);
                        
                        List<HashMap<String, Object>> ret = responseBuzBody.getBuzList();
                        
                        fileListAdapter = new OfficalDocListAdapter(mActivity, responseBuzBody);
                        
                        mLvMain.setAdapter(fileListAdapter);
                        
                        new Thread(new InsertList(ret)).start();
                        
                    }
                    else
                    {
                        Toast.makeText(mActivity, "服务器没有数据！", Toast.LENGTH_SHORT).show();
                    }
                    
                    break;
                
                default:
                    break;
            }
            
        }
        else
        {
            closeProgressDialog();
            if (!(errorCode == ConstState.CANCEL_THREAD))
            {
                Toast.makeText(mActivity, "请求服务器数据失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.mActivity = getActivity();
    }
    
    /**
     * 
     * 实例化
     * 
     * @Description 实例化
     * 
     * @param funcId 功能id
     * @param parentsPath 路径
     * @return fragment
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static OfficialFragment newInstance(String funcId, String funName, String parentsPath)
    {
        OfficialFragment details = new OfficialFragment();
        Bundle args = new Bundle();
        args.putString("funcId", funcId);
        args.putString("funName", funName);
        args.putString("parentsPath", parentsPath);
        details.setArguments(args);
        return details;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (container == null)
        {
            return null;
        }
        View v = inflater.inflate(R.layout.office_doc_main, null);
        loadLayout = (RelativeLayout)v.findViewById(R.id.load_layout);
        loadImageView = (ImageView)v.findViewById(R.id.waitdialog_img);
        
        tv_title = (TextView)v.findViewById(R.id.office_title);
        
        // ll_colse = (RelativeLayout)v.findViewById(R.id.official_top_close);
        // if (isCanClose)
        // {
        // ll_colse.setVisibility(View.VISIBLE);
        // }
        // else
        // {
        // ll_colse.setVisibility(View.GONE);
        // }
        
        mLvMain = (DropDownRefreshListView)v.findViewById(R.id.office_lv);
        
        mLvMain.setOnTouchListener(mViewLandscapeSlideListener);
        
        mFragmentBack = (ImageView)v.findViewById(R.id.office_back);
        mFragmentBack.setOnClickListener(this);
        initData();
        return v;
    }
    
    /**
     * 重载方法
     * 
     * @author shen_feng
     */
    @Override
    public void onResume()
    {
        super.onResume();
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description 初始化数据
     * 
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        offLine = GlobalState.getInstance().getOfflineLogin();
        
        user = GlobalState.getInstance().getOpenId();
        
        this.parentsPath = getArguments().getString("parentsPath");
        
        this.funcId = getArguments().getString("funcId");
        
        this.funName = getArguments().getString("funName");
        
        path = ConstState.MIP_ROOT_DIR + parentsPath + "/";
        
        tv_title.setText(funName);
        
        mLvMain.setOnItemClickListener(this);
        
        // ll_colse.setOnClickListener(this);
        
    }
    
    /**
     * 重载方法
     * 
     * @author shen_feng
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }
    
    /**
     * 重载方法
     * 
     * @param arg0 arg0
     * @param arg1 arg1
     * @param arg2 arg2
     * @param arg3 arg3
     * @author shen_feng
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        // 点击时间间隔太短则不触发
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        
        // 点击栏目模快
        
        HashMap<String, String> item = (HashMap<String, String>)fileListAdapter.getItem(arg2 - 1);
        
        String funcName = (String)item.get("funcname");
        String funcCode = (String)item.get("funccode");
        String flowtype = (String)item.get("flowType");
        String searchKeywords = (String)item.get("searchkeywords");
        String searchtype = (String)item.get("searchtype");
        
        GWDocModule mModlue = new GWDocModule();
        
        if (funcName == null)
        {
            funcName = "";
        }
        
        if (funcCode == null)
        {
            funcCode = "";
        }
        
        if (flowtype == null)
        {
            flowtype = "";
        }
        
        if (searchKeywords == null)
        {
            searchKeywords = "";
        }
        
        if (searchtype == null)
        {
            searchtype = "";
        }
        
        mModlue.setFunccode(funcCode);
        mModlue.setFuncName(funcName);
        mModlue.setFlowtype(flowtype);
        mModlue.setSearchkeywords(searchKeywords);
        mModlue.setSearchtype(searchtype);
        
        // String path2 = path + funcName + "/";
        String path2 = path;
        
        mFragmentTMainActivityListener.onOfficialFragmentSelected(arg2 - 1, mModlue, path2);
        
        fileListAdapter.setmSelectedPosition(arg2 - 1);
        fileListAdapter.notifyDataSetChanged();
        
    }
    
    /**
     * 
     * 保存列表
     * 
     * @author shen_feng
     * @Version [版本号, 2013-10-9]
     */
    private class InsertList implements Runnable
    {
        private List<HashMap<String, Object>> listMap;
        
        public InsertList(List<HashMap<String, Object>> listMap)
        {
            this.listMap = listMap;
        }
        
        @Override
        public void run()
        {
            GetNextModuleListInfo test1 = new GetNextModuleListInfo(MessageIdConstants.GETDOCMODULELIST, user, funName);
            String[] str = {user, funName};
            
            String where = GetNextModuleListInfo.l_user + " = ? and " + GetNextModuleListInfo.l_funcname + " = ?";
            
            test1.delete(where, str);
            
            for (int i = 0; i < listMap.size(); i++)
            {
                GetNextModuleListInfo test =
                    new GetNextModuleListInfo(MessageIdConstants.GETDOCMODULELIST, user, funName);
                test.convertToObject(listMap.get(i));
                test.insert();
            }
        }
    }
    
    /**
     * 
     * 显示等待框
     * 
     * @Description 显示等待框
     * 
     * @LastModifiedDate：2013-10-22
     * @author wang_ling
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
     * @LastModifiedDate：2013-10-22
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void closeProgressDialog()
    {
        loadLayout.setVisibility(View.GONE);
        isLoading = false;
    }
    
    private void closeNetRequest(int requestType)
    {
        NetRequestController.stopCurrentNetTask(mGetDocModuleHttpCreator);
    }
    
    @Override
    public void onClick(View v)
    {
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        
        switch (v.getId())
        {
        // case R.id.official_top_close:
        // if (isLoading)
        // {
        // closeNetRequest(RequestTypeConstants.GETDOCMODULELIST);
        // closeProgressDialog();
        // }
        // closeThisFragment();
        // break;
        
            case R.id.office_back:
                closeThisFragment();
                break;
            default:
                break;
        }
    }
    
    /**
     * 二级布局关闭
     * 
     * @author ren_qiujing
     */
    @Override
    public void closeThisFragment()
    {
        // TODO Auto-generated method stub
        if (mFragmentTMainActivityListener != null)
        {
            mFragmentTMainActivityListener.onSecondFragmentClose();
        }
    }
    
    /**
     * 三级菜单被关闭
     * 
     * @author ren_qiujing
     */
    @Override
    public void theThirdFragmentClose()
    {
        // TODO Auto-generated method stub
        fileListAdapter.setmSelectedPosition(-1);
        fileListAdapter.notifyDataSetChanged();
    }
    
    /**
     * 点击back键
     * 
     * @author ren_qiujing
     */
    @Override
    public boolean onKeyDown(int keyId)
    {
        // TODO Auto-generated method stub
        if (keyId == KeyEvent.KEYCODE_BACK && isCanClose)
        {
            if (isLoading)
            {
                closeNetRequest(RequestTypeConstants.GETDOCMODULELIST);
                closeProgressDialog();
            }
            closeThisFragment();
            return true;
        }
        else
        {
            return false;
        }
        
    }
    
    /**
     * fragment进入时动态画面结束时请求函数
     * 
     * @return
     * @author ren_qiujing
     */
    @Override
    public void animationOver()
    {
        // TODO Auto-generated method stub
        getDocModuleList();
    }
    
    private void getDocModuleList()
    {
        // // 如果之前已经有了“公文流转”的分组目录，则不再从服务器获取
        if (GlobalState.getInstance().getmGwlzFirstModule() != null
            && GlobalState.getInstance().getmGwlzFirstModule().getBuzList() != null
            && GlobalState.getInstance().getmGwlzFirstModule().getBuzList().size() != 0)
        {
            MetaResponseBody responseBuzBody = GlobalState.getInstance().getmGwlzFirstModule();
            
            fileListAdapter = new OfficalDocListAdapter(mActivity, responseBuzBody);
            
            mLvMain.setAdapter(fileListAdapter);
            return;
        }
        LogUtil.i("", "******animationOver()");
        
        if (offLine)
        {
            // 从数据库中获取相数据对象,查询所有的数据
            GetNextModuleListInfo testObjectfromDB =
                new GetNextModuleListInfo(MessageIdConstants.GETDOCMODULELIST, user, funName);
            String[] str = {user, funName};
            
            String where = GetNextModuleListInfo.l_user + " = ? and " + GetNextModuleListInfo.l_funcname + " = ?";
            
            List<HashMap<String, Object>> queryret =
                (List<HashMap<String, Object>>)testObjectfromDB.query(null, where, str, null);
            
            MetaResponseBody m = new MetaResponseBody();
            
            if (queryret != null && queryret.size() != 0)
            {
                m.setBuzList(queryret);
                GlobalState.getInstance().setmGwlzFirstModule(m);
                fileListAdapter = new OfficalDocListAdapter(mActivity, m);
                mLvMain.setAdapter(fileListAdapter);
            }
            else
            {
                Toast.makeText(mActivity, "本地没有数据！", Toast.LENGTH_SHORT).show();
            }
            
        }
        else
        {
            mGetDocModuleHttpCreator = new HttpNetFactoryCreator(RequestTypeConstants.GETDOCMODULELIST).create();
            JSONObject body = new JSONObject();
            try
            {
                body.put("funcid", funcId);
                body.put("type", ConstState.MENU);
                NetRequestController.getNextModuleList(mGetDocModuleHttpCreator,
                    mHandler,
                    RequestTypeConstants.GETDOCMODULELIST,
                    body);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            showProgressDialog();
        }
    }
}
