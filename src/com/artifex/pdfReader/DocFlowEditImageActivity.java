package com.artifex.pdfReader;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.BaseUtils.onViewBackListen;
import com.artifex.Infor.AttachInfoView;
import com.artifex.Infor.AttachInfoView.onCloseAttchViewListen;
import com.artifex.Infor.InfoView;
import com.artifex.handWriteEditor.CustomHandWriteEditor;
import com.artifex.handWriteEditor.HandWriteTool;
import com.artifex.handWriteEditor.SpenView;
import com.artifex.help.HelpView;
import com.artifex.help.HelpView.onCloseThisViewListen;
import com.artifex.mupdf.MuPDFAttr;
import com.artifex.mupdf.MuPDFCore;
import com.artifex.mupdf.MuPDFPageAdapter;
import com.artifex.mupdf.MuPDFPageView;
import com.artifex.mupdf.PDFUtilFromiText;
import com.artifex.mupdf.PageView;
import com.artifex.mupdf.ReaderView;
import com.artifex.send.GWLZSend.DocSendView;
import com.hoperun.manager.components.CustomDialog;
import com.hoperun.manager.components.CustomDialogListener;
import com.hoperun.manager.components.WaitDialog;
import com.hoperun.manager.netHandle.NetRequestController;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.netutils.baseNetEngine.NetTask.NetTask;
import com.hoperun.mip.netutils.model.BaseResponseEntity.Body.MetaResponseBody;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.utils.OsUtils;
import com.hoperun.miplygphone.R;
import com.hoperun.mipmanager.netHandle.netCreator.HttpNetFactoryCreator;
import com.hoperun.mipmanager.utils.ConstState;
import com.hoperun.mipmanager.utils.RequestTypeConstants;
import com.hoperun.project.ui.baseui.PMIPBaseActivity;
import com.hoperun.project.ui.offical.OfficialSecondFragment;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-10-18]
 */
/**
 * phone
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-11-11]
 */
@SuppressLint("HandlerLeak")
public class DocFlowEditImageActivity extends PMIPBaseActivity
{
    /**
     * 向文件传递的value值
     */
    private Intent           mIntent_values;
    
    /******************* PDF Plugin Start *******************************/
    /**
     * pdf边沿距离
     */
    private final static int TAP_PAGE_MARGIN = 5;
    
    /**
     * 是否是处于橡皮擦
     */
    private boolean          isEraser        = false;
    
    /**
     * 重新加载
     */
    private final static int BUTTON_RELOAD   = 202;
    
    /**
     * 点击翻转按钮
     */
    private final static int BUTTON_TURN     = 203;
    
    /**
     * 点击帮助按钮
     */
    private final static int BUTTON_HELP     = 204;
    
    /**
     * 
     */
    private final static int SHOWDIALOG      = 205;
    
    /**
     * 
     */
    private final static int CLOSEDIALOG     = 206;
    
    /* The core rendering instance */
    /**
     * 点击屏幕时，判断是否要隐藏按钮 <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @author ren_qiujing
     * @Version [版本号, 2013-10-18]
     */
    private enum LinkState
    {
        DEFAULT, HIGHLIGHT, INHIBIT
    };
    
    /******************* PDF 变量，供传递intent的值时的value *******************************/
    /**
     * 打开pdf文件的路径
     */
    private String           mPdfPath;
    
    /**
     * 打开的pdf文件的文件名称
     */
    private String           mPdfFilename;
    
    /**
     * 打开的pdf文件的文件ID
     */
    private String           mPdfFilemessageId;
    
    /**
     * 是否有信息显示，true 有； false 无信息显示；
     */
    private boolean          mPdfHasInfo;
    
    /**
     * 是否有附件
     */
    private boolean          mPdfHasAttch;
    
    /**
     * 签批的类型：0：不可以签批；1：签批： 2：签署
     */
    private int              mPdfSignFlag;
    
    /**
     * 发送的类型：0：不可以发送；1：公文流转发送；2：通用发送；
     */
    private int              mPdfSendFLAG;
    
    /**
     * 锁的类型：-1 没有锁机值， 0 没有被锁，1 被锁
     */
    private String           mPdfLockFlag;
    
    /**
     * 锁住人的ID
     */
    private String           mPdfLockID;
    
    /**
     * 锁住人的姓名
     */
    private String           mPdfLockNAME;
    
    /**
     * 要打开pdf的第几页；
     */
    // private int mPdfPageNumber;
    
    /**
     * 待办或已办标识；
     */
    private String           mPdfHandleType;
    
    /**
     * 收文/发文标识；
     */
    private String           mPdfType;
    
    /**
     * 向左消失动画
     */
    private Animation        mLeftOut;
    
    /**
     * 向左进入动画
     */
    private Animation        mLeftIn;
    
    /**
     * 向右消失动画
     */
    private Animation        mRightOut;
    
    /**
     * 向右进入动画
     */
    private Animation        mRightIn;
    
    /******************* PDF 常量 *******************************/
    
    /**
     * 读取pdf核心类
     */
    private MuPDFCore        core;
    
    /**
     * pdf阅读页面
     */
    private ReaderView       mDocView;
    
    /**
     * 默认为不显示状态
     */
    private LinkState        mLinkState             = LinkState.DEFAULT;
    
    /******************* PDF Plugin End *******************************/
    
    /**
     * 上下文
     */
    private Context          mContext;
    
    /**
     * 整体布局的layout，所有的控件均在该layout上呈现
     */
    private RelativeLayout   layout;
    
    /**
     * readerView 返回的handler
     */
    private Handler          mSignHandler;
    
    /**
     * pdf页面布局view
     */
    private View             mButtonsView;
    
    /**
     * 签批右边布局
     */
    private RelativeLayout   mRl_PDFRight;
    
    /**
     * 标题
     */
    private TextView         mTitle;
    
    private TextView         mPageNum;
    
    /**
     * 锁图片
     */
    private ImageView        mIamgeLock;
    
    /**
     * 左侧按钮布局
     */
    private RelativeLayout   mLeftButtons;
    
    /**
     * 左边功能按钮（中间部分）集合
     */
    private LinearLayout     mLeftmidLayout;
    
    /**
     * mLeftmidLayout 布局离顶部的距离
     */
    private int              mLeftmidTop            = -1;
    
    /**
     * 返回按钮
     */
    private RelativeLayout   mBack;
    
    /**
     * 信息按钮
     */
    private ImageView        mInfo;
    
    private RelativeLayout   mInfoRL;
    
    /**
     * 上一页按钮
     */
    private ImageView        mPre;
    
    private RelativeLayout   mPreRL;
    
    /**
     * 下一页按钮
     */
    private ImageView        mNext;
    
    private RelativeLayout   mNextRL;
    
    /**
     * 翻转按钮
     */
    private ImageView        mTurn;
    
    private RelativeLayout   mTurnRL;
    
    //
    // /**
    // * 翻转按钮
    // */
    // private CustomButton mTurn;
    //
    /**
     * 发送按钮
     */
    private ImageView        mSend;
    
    private RelativeLayout   mSendRL;
    
    /**
     * 附件按钮
     */
    private ImageView        mAttch;
    
    private RelativeLayout   mAttchRL;
    
    /**
     * 帮助按钮
     */
    private ImageView        mHelp;
    
    private RelativeLayout   mHelpRL;
    
    /**
     * 签批按钮
     */
    private RelativeLayout   mSign;
    
    /**
     * 签批布局
     */
    private RelativeLayout   mSignRL;
    
    /**
     * 底部布局
     */
    private LinearLayout     mBottom;
    
    private RelativeLayout   mSignColorRL0;
    
    /**
     * 选择颜色
     */
    private RelativeLayout   mSignColorRL;
    
    private ImageView        mSignColorIm;
    
    private RelativeLayout   mSignLineRL0;
    
    /**
     * 选择粗细
     */
    private RelativeLayout   mSignLineRL;
    
    private ImageView        mSignLineIm;
    
    /**
     * 选择橡皮擦
     */
    private RelativeLayout   mSignEraserRL;
    
    /**
     * 选择撤销
     */
    private RelativeLayout   mSignUndoRL;
    
    /**
     * 选择还原
     */
    private RelativeLayout   mSignRedoRL;
    
    /**
     * 选择清空
     */
    private RelativeLayout   mSignEmptyRL;
    
    /**
     * 选择保存
     */
    private RelativeLayout   mSignSaveRL;
    
    /**
     * 供选择的颜色的布局
     */
    private RelativeLayout   mSignColorSelectedRL;
    
    /**
     * 选择绿色
     */
    private RelativeLayout   mSignColorSelectedGreenRL;
    
    /**
     * 选择红色
     */
    private RelativeLayout   mSignColorSelectedRedRL;
    
    /**
     * 选择黑色
     */
    private RelativeLayout   mSignColorSelectedBlackRL;
    
    /**
     * 选择蓝色
     */
    private RelativeLayout   mSignColorSelectedBlueRL;
    
    /**
     * 供选择的画笔粗细的布局
     */
    private RelativeLayout   mSignLineSelectedRL;
    
    /**
     * 细画笔
     */
    private RelativeLayout   mSignLineSelectedCrudeRL;
    
    /**
     * 普通画笔
     */
    private RelativeLayout   mSignLineSelectedNormalRL;
    
    /**
     * 粗画笔
     */
    private RelativeLayout   mSignLineSelectedFineRL;
    
    //
    // /**
    // * 上一页按钮
    // */
    // private CustomButton mPre;
    //
    // /**
    // * 下一页按钮
    // */
    // private CustomButton mNext;
    
    /**
     * 当前页面页数
     */
    private int              current                = 1;
    
    /**
     * 总页数
     */
    private int              totalPageNums          = -1;
    
    /**
     * 操作pdf的工具类
     */
    private PDFUtilFromiText mPDFUtil;
    
    /**
     * 打开pdf密码
     */
    private String           mPDFPassword           = "Comprise";
    
    /**
     * 是否处于签批状态
     */
    private boolean          isSignState            = false;
    
    // /**
    // * 临时中间button
    // */
    // private CustomButton mSelectButton;
    
    /**
     * 锁状态netTask
     */
    private NetTask          mSetLockStatusNetTask;
    
    /**
     * 帮助页面
     */
    private HelpView         mHelpKnow;
    
    /**
     * 签批时，帮助页面首次显示
     */
    private boolean          isFirstPdfSignHelpSkip = true;
    
    /**
     * 打开pdf时，帮助页面首次显示
     */
    private boolean          isFirstPdfHelpSkip     = true;
    
    // /**
    // * 签批view
    // */
    // private SignView mSignView;
    
    /**
     * 等待loading 框
     */
    private WaitDialog       mWaitDialog;
    
    /**
     * 点击按钮后的处理Handler
     */
    private Handler          bHandler               = new Handler()
                                                    {
                                                        
                                                        @Override
                                                        public void handleMessage(Message msg)
                                                        {
                                                            // TODO Auto-generated method stub
                                                            switch (msg.what)
                                                            {
                                                                case BUTTON_TURN:
                                                                    mDocView.clearAdaptePageView();
                                                                    mPDFUtil.rotate(mPdfPath, current);
                                                                    restartMe();
                                                                    mTitle.requestFocus();
                                                                    break;
                                                                
                                                                case BUTTON_RELOAD:
                                                                    if (HandWriteTool.mEditor != null)
                                                                    {
                                                                        for (int i = 0; i < HandWriteTool.mEditor.length; i++)
                                                                        {
                                                                            
                                                                            if (HandWriteTool.mEditor[i] != null)
                                                                            {
                                                                                
                                                                                if (HandWriteTool.type == 0)
                                                                                {
                                                                                    ((CustomHandWriteEditor)HandWriteTool.mEditor[i]).clearAll();
                                                                                    
                                                                                }
                                                                                // else if (HandWriteTool.type == 1)
                                                                                // {
                                                                                //
                                                                                // }
                                                                                
                                                                            }
                                                                            
                                                                        }
                                                                    }
                                                                    mDocView.clearAdaptePageView();
                                                                    restartMe();
                                                                    
                                                                    break;
                                                                
                                                                case BUTTON_HELP:
                                                                    
                                                                    boolean isShow = (Boolean)msg.obj;
                                                                    
                                                                    if (mHelpKnow != null)
                                                                    {
                                                                        if (isShow)
                                                                        {
                                                                            layout.removeView(mHelpKnow);
                                                                            layout.addView(mHelpKnow);
                                                                            mHelpKnow.bringToFront();
                                                                            mHelpKnow.invalidate();
                                                                        }
                                                                        else
                                                                        {
                                                                            layout.removeView(mHelpKnow);
                                                                            mHelpKnow = null;
                                                                        }
                                                                    }
                                                                    
                                                                    break;
                                                                
                                                                case SHOWDIALOG:
                                                                    showLoadingDialog();
                                                                    break;
                                                                case CLOSEDIALOG:
                                                                    boolean isReload = (Boolean)msg.obj;
                                                                    closeLoadingDialog();
                                                                    if (isReload)
                                                                    {
                                                                        bHandler.sendEmptyMessage(BUTTON_RELOAD);
                                                                        if (sendView != null)
                                                                        {
                                                                            sendView.sendFile();
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        saveSignStatus();
                                                                        finish();
                                                                    }
                                                                    break;
                                                                
                                                                default:
                                                                    break;
                                                            }
                                                            super.handleMessage(msg);
                                                        }
                                                        
                                                    };
    
    /**
     * 点击按钮的监听
     */
    private OnClickListener  btnLisener             = new OnClickListener()
                                                    {
                                                        @Override
                                                        public void onClick(View v)
                                                        {
                                                            switch (v.getId())
                                                            {
                                                                case R.id.pdf_back:
                                                                    backPdfReader();
                                                                    break;
                                                                // 信息
                                                                case R.id.pdf_info_im:
                                                                    if (!isSignState)
                                                                    {
                                                                        showInfoView();
                                                                    }
                                                                    else
                                                                    {
                                                                        showToastMessage("文件处于签批状态，请先结束签批！");
                                                                        
                                                                    }
                                                                    break;
                                                                // 翻转
                                                                case R.id.pdf_turn_im:
                                                                    if (!isSignState)
                                                                    {
                                                                        final CustomDialog dialog =
                                                                            CustomDialog.newInstance(getResources().getString(R.string.pdf_turn_info),
                                                                                getResources().getString(R.string.pdf_Cancel),
                                                                                getResources().getString(R.string.pdf_Confirm));
                                                                        
                                                                        dialog.setRightListener(new CustomDialogListener()
                                                                        {
                                                                            
                                                                            @Override
                                                                            public void Onclick()
                                                                            {
                                                                                dialog.dismiss();
                                                                                Message msg = new Message();
                                                                                msg.what = BUTTON_TURN;
                                                                                bHandler.sendMessage(msg);
                                                                            }
                                                                        });
                                                                        
                                                                        dialog.setLeftListener(new CustomDialogListener()
                                                                        {
                                                                            
                                                                            @Override
                                                                            public void Onclick()
                                                                            {
                                                                                dialog.dismiss();
                                                                                
                                                                            }
                                                                        });
                                                                        
                                                                        if (isCurrentHasHistory())
                                                                        {
                                                                            dialog.show(DocFlowEditImageActivity.this.getSupportFragmentManager(),
                                                                                "CustomDialog");
                                                                        }
                                                                        else
                                                                        {
                                                                            Message msg = new Message();
                                                                            msg.what = BUTTON_TURN;
                                                                            bHandler.sendMessage(msg);
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        showToastMessage("文件处于签批状态，请先结束签批！");
                                                                    }
                                                                    break;
                                                                // 发送
                                                                case R.id.pdf_send_im:
                                                                    if (mPdfLockFlag.equals(ConstState.LOCK))
                                                                    {
                                                                        showLockInfo();
                                                                    }
                                                                    else if (!isSignState)
                                                                    {
                                                                        showView();
                                                                    }
                                                                    else
                                                                    {
                                                                        showToastMessage("文件处于签批状态，请先结束签批！");
                                                                    }
                                                                    break;
                                                                // 帮助
                                                                case R.id.pdf_help_im:
                                                                    showHelpView(true, isSignState);
                                                                    break;
                                                                // 签批
                                                                case R.id.pdf_sign_im:
                                                                    // showHelpView(true, isSignState);
                                                                    
                                                                    if (mPdfLockFlag.equals(ConstState.LOCK))
                                                                    {
                                                                        showLockInfo();
                                                                    }
                                                                    else
                                                                    {
                                                                        beginSign();
                                                                        if (isFirstPdfSignHelpSkip)
                                                                        {
                                                                            showHelpView(false, true);
                                                                            isFirstPdfSignHelpSkip = false;
                                                                        }
                                                                        
                                                                        if (mPdfLockFlag.equals(ConstState.NOLOCK))
                                                                        {
                                                                            setLockStatus(true);
                                                                        }
                                                                        // signStatus(true);
                                                                        
                                                                    }
                                                                    break;
                                                                // 结束签批
                                                                // case R.id.pdf_bottom_endsign:
                                                                // endSign();
                                                                // break;
                                                                // 上页
                                                                case R.id.pdf_page_pre_im:
                                                                    if (!isSignState)
                                                                    {
                                                                        mDocView.moveToPrevious();
                                                                    }
                                                                    else
                                                                    {
                                                                        showToastMessage("文件处于签批状态，请先结束签批！");
                                                                    }
                                                                    break;
                                                                // 下页
                                                                case R.id.pdf_page_next_im:
                                                                    if (!isSignState)
                                                                    {
                                                                        mDocView.moveToNext();
                                                                    }
                                                                    else
                                                                    {
                                                                        showToastMessage("文件处于签批状态，请先结束签批！");
                                                                    }
                                                                    break;
                                                                // 附件
                                                                case R.id.pdf_attach_im:
                                                                    if (!isSignState)
                                                                    {
                                                                        showAttchView();
                                                                    }
                                                                    else
                                                                    {
                                                                        showToastMessage("文件处于签批状态，请先结束签批！");
                                                                    }
                                                                    break;
                                                                
                                                                default:
                                                                    break;
                                                            }
                                                        }
                                                    };
    
    /** 显示“附件view” **/
    AttachInfoView           mAttchInfoView;
    
    /**
     * 
     * 显示附件
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-3-20
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void showAttchView()
    {
        
        if (mAttchInfoView != null)
        {
            layout.removeView(mAttchInfoView);
        }
        mAttchInfoView = null;
        // Intent intent = new Intent();
        // intent.putExtra("padID", mPdfFilemessageId);
        // intent.putExtra("padtype", mPdfType);
        // intent.putExtra(ConstState.PDF_PATH, mPdfPath);
        mAttchInfoView = new AttachInfoView(mContext, mIntent_values, mBottom.getHeight());
        mAttchInfoView.setMviewbacklistener(attchListen);
        layout.addView(mAttchInfoView);
        mAttchInfoView.bringToFront();
        mAttchInfoView.invalidate();
    }
    
    private OnClickListener btnSignLisener  = new OnClickListener()
                                            {
                                                
                                                @Override
                                                public void onClick(View v)
                                                {
                                                    
                                                    MuPDFPageView view = (MuPDFPageView)mDocView.getDisplayedView();
                                                    switch (v.getId())
                                                    {
                                                        case R.id.pdf_sign_color_rl:
                                                            mSignLineSelectedRL.setVisibility(View.GONE);
                                                            setSignBackground();
                                                            mSignColorRL.setBackgroundResource(R.drawable.choose_qp);
                                                            
                                                            if (!isEraser
                                                                && mSignColorSelectedRL.getVisibility() != View.VISIBLE)
                                                            {
                                                                LayoutParams mColorSelectedLp =
                                                                    (LayoutParams)mSignColorSelectedRL.getLayoutParams();
                                                                mColorSelectedLp.leftMargin =
                                                                    mSignColorRL0.getLeft() + mSignLineRL.getLeft();
                                                                mSignColorSelectedRL.setLayoutParams(mColorSelectedLp);
                                                                
                                                                mSignColorSelectedRL.setVisibility(View.VISIBLE);
                                                            }
                                                            else
                                                            {
                                                                mSignColorSelectedRL.setVisibility(View.GONE);
                                                            }
                                                            
                                                            setHandWriteEditorPen(view);
                                                            break;
                                                        case R.id.pdf_sign_line_rl:
                                                            mSignColorSelectedRL.setVisibility(View.GONE);
                                                            
                                                            if (mSignLineSelectedRL.getVisibility() != View.VISIBLE)
                                                            {
                                                                LayoutParams mLineSelectedLp =
                                                                    (LayoutParams)mSignLineSelectedRL.getLayoutParams();
                                                                mLineSelectedLp.leftMargin =
                                                                    mSignLineRL0.getLeft() + mSignLineRL.getLeft();
                                                                mSignLineSelectedRL.setLayoutParams(mLineSelectedLp);
                                                                mSignLineSelectedRL.setVisibility(View.VISIBLE);
                                                            }
                                                            else
                                                            {
                                                                mSignLineSelectedRL.setVisibility(View.GONE);
                                                            }
                                                            
                                                            break;
                                                        
                                                        case R.id.pdf_sign_eraser_rl:
                                                            setSignSelectedClose();
                                                            setSignBackground();
                                                            mSignEraserRL.setBackgroundResource(R.drawable.choose_qp);
                                                            setHandWriteEditorErase(view);
                                                            break;
                                                        case R.id.pdf_sign_undo_rl:
                                                            setSignSelectedClose();
                                                            setHandWriteEditorUndo(view);
                                                            break;
                                                        case R.id.pdf_sign_redo_rl:
                                                            setSignSelectedClose();
                                                            setHandWriteEditorRedo(view);
                                                            break;
                                                        case R.id.pdf_sign_empty_rl:
                                                            setSignSelectedClose();
                                                            setHandWriteEditorEmpty(view);
                                                            break;
                                                        case R.id.pdf_sign_save_rl:
                                                            setSignSelectedClose();
                                                            endSign();
                                                            break;
                                                    }
                                                }
                                                
                                            };
    
    private OnClickListener btnColorLisener = new OnClickListener()
                                            {
                                                
                                                @Override
                                                public void onClick(View arg0)
                                                {
                                                    MuPDFPageView view = (MuPDFPageView)mDocView.getDisplayedView();
                                                    switch (arg0.getId())
                                                    {
                                                        case R.id.pen_sign_color_green:
                                                            view.setPaintColor(ConstState.COLOR_PEN_GREED);
                                                            mColorSelected = R.drawable.pen_color_green;
                                                            break;
                                                        case R.id.pen_sign_color_red:
                                                            view.setPaintColor(ConstState.COLOR_PEN_RED);
                                                            mColorSelected = R.drawable.pen_color_red;
                                                            break;
                                                        case R.id.pen_sign_color_black:
                                                            view.setPaintColor(ConstState.COLOR_PEN_BLACK);
                                                            mColorSelected = R.drawable.pen_color_black;
                                                            break;
                                                        case R.id.pen_sign_color_blue:
                                                            view.setPaintColor(ConstState.COLOR_PEN_BLUE);
                                                            mColorSelected = R.drawable.pen_color_blue;
                                                            break;
                                                    }
                                                    if (mColorSelected != -1)
                                                    {
                                                        mSignColorIm.setBackgroundResource(mColorSelected);
                                                    }
                                                    mSignColorSelectedRL.setVisibility(View.GONE);
                                                }
                                                
                                            };
    
    private OnClickListener btnLineLisener  = new OnClickListener()
                                            {
                                                
                                                @Override
                                                public void onClick(View arg0)
                                                {
                                                    MuPDFPageView view = (MuPDFPageView)mDocView.getDisplayedView();
                                                    switch (arg0.getId())
                                                    {
                                                        case R.id.pen_sign_line_crude:
                                                            view.setStress(ConstState.SIGN_STRESS_CRUDE);
                                                            mLineSelected = R.drawable.pen_crude;
                                                            break;
                                                        case R.id.pen_sign_line_normal:
                                                            view.setStress(ConstState.SIGN_STRESS_NORMAL);
                                                            mLineSelected = R.drawable.pen_normal;
                                                            break;
                                                        case R.id.pen_sign_line_fine:
                                                            view.setStress(ConstState.SIGN_STRESS_FINDE);
                                                            mLineSelected = R.drawable.pen_fine;
                                                            break;
                                                    }
                                                    if (mLineSelected != -1)
                                                    {
                                                        mSignLineIm.setBackgroundResource(mLineSelected);
                                                    }
                                                    mSignLineSelectedRL.setVisibility(View.GONE);
                                                }
                                                
                                            };
    
    /**
     * 网络请求返回处理方法 重载方法
     * 
     * @param requestType requestType
     * @param objHeader objHeader
     * @param objBody objBody
     * @param error error
     * @param errorCode errorCode
     * @author ren_qiujing
     */
    @Override
    public void onPostHandle(int requestType, Object objHeader, Object objBody, boolean error, int errorCode)
    {
        // TODO Auto-generated method stub
        LogUtil.i("", "onPostHandle");
        if (errorCode == ConstState.SUCCESS)
        {
            switch (requestType)
            {
                case RequestTypeConstants.SETLOCKSTATUSLOCK:
                    // 请求公文列表回来
                    MetaResponseBody responseBuzBody = (MetaResponseBody)objBody;
                    if (responseBuzBody.getRetError().equals("0"))
                    {
                        mPdfLockFlag = ConstState.LOCK_OWM;
                    }
                    break;
                case RequestTypeConstants.SETLOCKSTATUSUNLOCK:
                    // 请求公文列表回来
                    MetaResponseBody responseBuzBody1 = (MetaResponseBody)objBody;
                    if (responseBuzBody1.getRetError().equals("0"))
                    {
                        mPdfLockFlag = ConstState.NOLOCK;
                    }
                    break;
            }
        }
    }
    
    /**
     * 打开pdf文件
     * 
     * @Description<功能详细描述>
     * 
     * @param path path
     * @return MuPDFCore
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private MuPDFCore openFile(String path)
    {
        try
        {
            core = new MuPDFCore(path);
        }
        catch (Exception e)
        {
            return null;
        }
        
        return core;
    }
    
    /** Called when the activity is first created. */
    /**
     * 重载方法
     * 
     * @param savedInstanceState savedInstanceState
     * @author ren_qiujing
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getIntentValue();
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        this.mContext = this;
        
        mSignHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case MuPDFAttr.STATE_PAGE_CHANGE:
                        if (MuPDFAttr.s_Current != MuPDFAttr.STATE_IDLE)
                        {
                            current = MuPDFAttr.s_Current;
                        }
                        String pageNum = current + "/" + totalPageNums;
                        mPageNum.setText(pageNum);
                        
                        // buttonShow();
                        break;
                    default:
                        break;
                }
            }
        };
        
        // getUserName();
        
        File pdfFile = new File(mPdfPath);
        if (!pdfFile.exists())
        {
            fileBad();
            return;
        }
        
        if (core == null)
        {
            core = (MuPDFCore)getLastNonConfigurationInstance();
            
            if (savedInstanceState != null && savedInstanceState.containsKey("FileName"))
            {
                mPdfFilename = savedInstanceState.getString("FileName");
            }
        }
        if (core == null)
        {
            core = openFile(mPdfPath);
        }
        
        if (core == null)
        {
            fileBad();
            
            return;
        }
        
        if (core != null)
        {
            mPDFUtil = new PDFUtilFromiText();
            
            if (core.needsPassword())
            {
                core.authenticatePassword(mPDFPassword);
                mPDFUtil.setPassword(mPDFPassword);
            }
            
            if (mPDFUtil.checkPdfFile(mPdfPath) == -1)
            {
                fileBad();
                
                return;
            }
            
            totalPageNums = core.countPages();
            
            initHandWriteEditor();
        }
        
        createUI(savedInstanceState);
        
        if (mDocView != null)
        {
            mDocView.setDisplayedViewIndex(current - 1);
            
            String pageNum = current + "/" + totalPageNums;
            mPageNum.setText(pageNum);
            
            // buttonShow();
        }
        
    }
    
    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
    }
    
    /**
     * pdf异常时提示
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void fileBad()
    {
        new AlertDialog.Builder(DocFlowEditImageActivity.this).setTitle("错误")
            .setMessage("PDF文件损坏，无法显示!")
            .setCancelable(false)
            .setPositiveButton("关闭", new DialogInterface.OnClickListener()
            {
                
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    // closePDFReaderView();
                    finish();
                }
            })
            .show();
    }
    
    /**
     * 获取intent里面传递的参数
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void getIntentValue()
    {
        mIntent_values = getIntent();
        mPdfPath = mIntent_values.getStringExtra(ConstState.PDF_PATH);
        mPdfFilename = mIntent_values.getStringExtra(ConstState.PDF_FILENAME);
        mPdfFilemessageId = mIntent_values.getStringExtra(ConstState.PDF_FILEMESSAGEID);
        mPdfHandleType = mIntent_values.getStringExtra(ConstState.PDF_HANDLETYPE);
        mPdfType = mIntent_values.getStringExtra(ConstState.PDF_TYPE);
        
        mPdfHasInfo = mIntent_values.getBooleanExtra(ConstState.PDF_HASINFO, false);
        mPdfHasAttch = mIntent_values.getBooleanExtra(ConstState.PDF_HASATTACH, false);
        
        mPdfSignFlag = mIntent_values.getIntExtra(ConstState.PDF_SIGNFLAG, ConstState.NoSign);
        mPdfSendFLAG = mIntent_values.getIntExtra(ConstState.PDF_SENDFLAG, ConstState.NoSend);
        
        mPdfLockFlag = mIntent_values.getStringExtra(ConstState.PDF_LOCKFLAG);
        mPdfLockID = mIntent_values.getStringExtra(ConstState.PDF_LOCKID);
        mPdfLockNAME = mIntent_values.getStringExtra(ConstState.PDF_LOCKNAME);
        
        current = mIntent_values.getIntExtra(ConstState.PDF_PAGENUMBER, 1);
        
        if (mPdfPath == null)
        {
            mPdfPath = "";
        }
        if (mPdfFilename == null)
        {
            mPdfFilename = "";
        }
        if (mPdfFilemessageId == null)
        {
            mPdfFilemessageId = "";
        }
        
        if (mPdfHandleType == null)
        {
            mPdfHandleType = "";
        }
        
        if (mPdfLockFlag == null || mPdfLockFlag.equals(""))
        {
            mPdfLockFlag = ConstState.NOLOCKMACHINE;
        }
        
        if (mPdfLockID == null)
        {
            mPdfLockID = "";
        }
        
        if (mPdfLockNAME == null)
        {
            mPdfLockNAME = "";
        }
        
        if (mPdfLockFlag.equals(ConstState.LOCK) && mPdfLockID.equals(GlobalState.getInstance().getOpenId()))
        {
            mPdfLockFlag = ConstState.LOCK_OWM;
        }
    }
    
    /**
     * 初始化各个UI页面
     * 
     * @Description<功能详细描述>
     * 
     * @param savedInstanceState savedInstanceState
     * @LastModifiedDate：2013-10-9
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void createUI(Bundle savedInstanceState)
    {
        if (core == null)
        {
            return;
        }
        mDocView = new ReaderView(this, mSignHandler)
        {
            private boolean showButtonsDisabled;
            
            public boolean onSingleTapUp(MotionEvent e)
            {
                
                if (e.getX() < (float)super.getWidth() / TAP_PAGE_MARGIN)
                {
                    // if (!this.isSign)
                    // {
                    // super.moveToPrevious();
                    // }
                }
                else if (e.getX() > (float)super.getWidth() * (TAP_PAGE_MARGIN - 1) / TAP_PAGE_MARGIN)
                {
                    // if (!this.isSign)
                    // {
                    // super.moveToNext();
                    // }
                }
                // else if (!showButtonsDisabled)
                // {
                //
                // }
                
                return super.onSingleTapUp(e);
            }
            
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
            {
                
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
            
            public boolean onTouchEvent(MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    showButtonsDisabled = false;
                }
                return super.onTouchEvent(event);
            }
            
            protected void onSettle(View v)
            {
                ((PageView)v).addHq();
            }
            
            protected void onUnsettle(View v)
            {
                ((PageView)v).removeHq();
            }
        }; // end mDocView
        
        mDocView.setAdapter(new MuPDFPageAdapter(this, core));
        
        initView();
        
        layout = new RelativeLayout(this);
        layout.addView(mDocView);
        layout.addView(mButtonsView);
        
        if (mPdfSignFlag != ConstState.NoSign)
        {
            // mSignView = new SignView(mContext, mSignButtonListen, mPdfSignFlag);
            // layout.addView(mSignView);
        }
        
        layout.setBackgroundResource(R.drawable.tiled_background);
        
        setContentView(layout);
        
        if (isFirstPdfHelpSkip)
        {
            showHelpView(false, false);
            isFirstPdfHelpSkip = false;
        }
    }
    
    /**
     * 重载方法
     * 
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data data
     * @author ren_qiujing
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode >= 0)
        {
            mDocView.setDisplayedViewIndex(resultCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    /**
     * 重载方法
     * 
     * @param outState
     * @author ren_qiujing
     */
    /**
     * 重载方法
     * 
     * @param outState outState
     * @author ren_qiujing
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        
        if (mPdfFilename != null && mDocView != null)
        {
            outState.putString("FileName", mPdfFilename);
            SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("page" + mPdfFilename, mDocView.getDisplayedViewIndex());
            edit.commit();
        }
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        
        if (mPdfFilename != null && mDocView != null)
        {
            SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("page" + mPdfFilename, mDocView.getDisplayedViewIndex());
            edit.commit();
        }
    }
    
    /**
     * 重载方法
     * 
     * @author ren_qiujing
     */
    @Override
    public void onDestroy()
    {
        if (core != null)
        {
            core.onDestroy();
        }
        core = null;
        
        clearHandWriteEditor();
        
        if (mPdfLockFlag.equals(ConstState.LOCK_OWM))
        {
            setLockStatus(false);
        }
        
        super.onDestroy();
    }
    
    /**
     * 初始化
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-9
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void initButtons()
    {
        mButtonsView = getLayoutInflater().inflate(R.layout.pdf_button, null);
        
        mTitle = (TextView)mButtonsView.findViewById(R.id.pdf_title);
        
        mBack = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_back);
        
        mPageNum = (TextView)mButtonsView.findViewById(R.id.pdf_number);
        
        mHelp = (ImageView)mButtonsView.findViewById(R.id.pdf_help_im);
        mHelpRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_help);
        
        mInfo = (ImageView)mButtonsView.findViewById(R.id.pdf_info_im);
        mInfoRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_info);
        
        mSign = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_sign_im);
        
        mSend = (ImageView)mButtonsView.findViewById(R.id.pdf_send_im);
        mSendRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_send);
        
        mTurn = (ImageView)mButtonsView.findViewById(R.id.pdf_turn_im);
        mTurnRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_turn);
        
        mPre = (ImageView)mButtonsView.findViewById(R.id.pdf_page_pre_im);
        mPreRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_page_pre);
        
        mAttch = (ImageView)mButtonsView.findViewById(R.id.pdf_attach_im);
        mAttchRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_attach);
        
        mNext = (ImageView)mButtonsView.findViewById(R.id.pdf_page_next_im);
        mNextRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_page_next);
        
        mSignRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_sign_rl);
        mSignColorRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_sign_color_rl);
        mSignColorRL0 = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_sign_color_rl0);
        mSignColorIm = (ImageView)mButtonsView.findViewById(R.id.pdf_sign_color);
        mSignLineRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_sign_line_rl);
        mSignLineRL0 = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_sign_line_rl0);
        mSignLineIm = (ImageView)mButtonsView.findViewById(R.id.pdf_sign_line);
        mSignEraserRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_sign_eraser_rl);
        mSignUndoRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_sign_undo_rl);
        mSignRedoRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_sign_redo_rl);
        mSignEmptyRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_sign_empty_rl);
        mSignSaveRL = (RelativeLayout)mButtonsView.findViewById(R.id.pdf_sign_save_rl);
        
        mSignColorSelectedRL = (RelativeLayout)mButtonsView.findViewById(R.id.pen_sign_color_seleced_rl);
        mSignColorSelectedGreenRL = (RelativeLayout)mButtonsView.findViewById(R.id.pen_sign_color_green);
        mSignColorSelectedRedRL = (RelativeLayout)mButtonsView.findViewById(R.id.pen_sign_color_red);
        mSignColorSelectedBlackRL = (RelativeLayout)mButtonsView.findViewById(R.id.pen_sign_color_black);
        mSignColorSelectedBlueRL = (RelativeLayout)mButtonsView.findViewById(R.id.pen_sign_color_blue);
        
        mSignLineSelectedRL = (RelativeLayout)mButtonsView.findViewById(R.id.pen_sign_line_seleced_rl);
        mSignLineSelectedCrudeRL = (RelativeLayout)mButtonsView.findViewById(R.id.pen_sign_line_crude);
        mSignLineSelectedNormalRL = (RelativeLayout)mButtonsView.findViewById(R.id.pen_sign_line_normal);
        mSignLineSelectedFineRL = (RelativeLayout)mButtonsView.findViewById(R.id.pen_sign_line_fine);
        
        mBottom = (LinearLayout)mButtonsView.findViewById(R.id.pdf_bottom);
        
        mBack.setOnClickListener(btnLisener);
        mInfo.setOnClickListener(btnLisener);
        mSign.setOnClickListener(btnLisener);
        mSend.setOnClickListener(btnLisener);
        mHelp.setOnClickListener(btnLisener);
        mTurn.setOnClickListener(btnLisener);
        mPre.setOnClickListener(btnLisener);
        mNext.setOnClickListener(btnLisener);
        mAttch.setOnClickListener(btnLisener);
        
        mSignColorRL.setOnClickListener(btnSignLisener);
        mSignLineRL.setOnClickListener(btnSignLisener);
        mSignEraserRL.setOnClickListener(btnSignLisener);
        mSignUndoRL.setOnClickListener(btnSignLisener);
        mSignRedoRL.setOnClickListener(btnSignLisener);
        mSignEmptyRL.setOnClickListener(btnSignLisener);
        mSignSaveRL.setOnClickListener(btnSignLisener);
        
        mSignColorSelectedGreenRL.setOnClickListener(btnColorLisener);
        mSignColorSelectedRedRL.setOnClickListener(btnColorLisener);
        mSignColorSelectedBlackRL.setOnClickListener(btnColorLisener);
        mSignColorSelectedBlueRL.setOnClickListener(btnColorLisener);
        
        mSignLineSelectedCrudeRL.setOnClickListener(btnLineLisener);
        mSignLineSelectedNormalRL.setOnClickListener(btnLineLisener);
        mSignLineSelectedFineRL.setOnClickListener(btnLineLisener);
        
        if (mPdfFilename != null && !"".equals(mPdfFilename))
        {
            mTitle.setText(mPdfFilename);
        }
        
        if (!mPdfHasInfo)
        {
            mInfoRL.setVisibility(View.GONE);
        }
        
        if (!mPdfHasAttch)
        {
            mAttchRL.setVisibility(View.GONE);
        }
        if (mPdfSendFLAG == ConstState.NoSend)
        {
            mSendRL.setVisibility(View.GONE);
        }
        
        if (mPdfSignFlag == ConstState.NoSign)
        {
            mSign.setVisibility(View.GONE);
        }
    }
    
    /**
     * activity初始化
     */
    @SuppressWarnings("static-access")
    private void initView()
    {
        initButtons();
        
        mLeftOut = new AnimationUtils().loadAnimation(this, R.anim.fromleftout);
        mLeftIn = new AnimationUtils().loadAnimation(this, R.anim.fromleftin);
        
        mRightOut = new AnimationUtils().loadAnimation(this, R.anim.fromrightout);
        mRightIn = new AnimationUtils().loadAnimation(this, R.anim.fromrightin);
        
    }
    
    /**
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    @SuppressWarnings("deprecation")
    private void restartMe()
    {
        if (core != null)
        {
            core.onDestroy();
            core = null;
        }
        
        if (layout != null)
        {
            layout.removeAllViews();
            layout = null;
        }
        
        if (mDocView != null)
        {
            mDocView.setAdapter(null);
            mDocView = null;
            
            core = (MuPDFCore)getLastNonConfigurationInstance();
            
            if (core == null)
            {
                core = openFile(mPdfPath);
                if (core == null)
                {
                    fileBad();
                    
                    return;
                }
            }
            
            mPDFUtil = new PDFUtilFromiText();
            
            if (core.needsPassword())
            {
                core.authenticatePassword(mPDFPassword);
                mPDFUtil.setPassword(mPDFPassword);
            }
            
            if (mPDFUtil.checkPdfFile(mPdfPath) == -1)
            {
                fileBad();
                
                return;
            }
            
            mButtonsView = null;
            createUI(null);
            mDocView.setDisplayedViewIndex(current - 1);
        }
        
        if (HandWriteTool.mEditor != null && (current > 0 && HandWriteTool.mEditor[current - 1] != null))
        {
            HandWriteTool.mEditor[current - 1] = null;
        }
        
    }
    
    /**
     * 重载方法
     * 
     * @param keyCode
     * @param event
     * @return
     * @author ren_qiujing
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (mHelpKnow != null && mHelpKnow.getVisibility() == View.VISIBLE)
            {
                layout.removeView(mHelpKnow);
                mHelpKnow = null;
                return false;
            }
            else if (mInfoView != null && mInfoView.getVisibility() == View.VISIBLE)
            {
                layout.removeView(mInfoView);
                mInfoView = null;
                return false;
            }
            else
            {
                
                // closePDFReaderView();
                backPdfReader();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    /**
     * 隐藏左边按钮是否可以点击
     * 
     * @Description<功能详细描述>
     * 
     * @param flag
     * @LastModifiedDate：2013-11-29
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void dismissLeftButtons(boolean flag)
    {
        // mInfo.setButtonSignClickedEable(flag);
        // mTurn.setButtonSignClickedEable(flag);
        // mSend.setButtonSignClickedEable(flag);
    }
    
    /**
     * 初始化签批的信息
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void initHandWriteEditor()
    {
        String supportSPEN =
            GlobalState.getInstance()
                .getFromConfigerTable("supportSPEN", ConstState.DB_FILEPATH, ConstState.DB_VERSION);
        if (supportSPEN != null && !"".equals(supportSPEN) && OsUtils.isSAMsung())
        {
            String[] spens = supportSPEN.split(";");
            int i = 0;
            LogUtil.i("", "OsUtils.getMODEL() = " + OsUtils.getMODEL());
            for (i = 0; i < spens.length; i++)
            {
                if (OsUtils.getMODEL().contains(spens[i]))
                {
                    HandWriteTool.type = 1;
                    break;
                }
            }
            
            if (i == spens.length)
            {
                HandWriteTool.type = 0;
            }
            
        }
        else
        {
            HandWriteTool.type = 0;
        }
        
        if (HandWriteTool.mEditor != null)
        {
            for (int i = 0; i < HandWriteTool.mEditor.length; i++)
            {
                if (HandWriteTool.mEditor[i] != null)
                {
                    HandWriteTool.mEditor[i] = null;
                }
            }
            HandWriteTool.setmEditor(null);
        }
        
        if (HandWriteTool.type == 0)
        {
            HandWriteTool.setmEditor(new CustomHandWriteEditor[core.countPages()]);
        }
        else if (HandWriteTool.type == 1)
        {
            HandWriteTool.setmEditor(new SpenView[core.countPages()]);
        }
        HandWriteTool.setmSignColor(ConstState.COLOR_PEN_BLACK);
        HandWriteTool.setmSignStress(ConstState.SIGN_STRESS_NORMAL);
    }
    
    /**
     * 退出时，清空HandWriteEditor的缓存
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void clearHandWriteEditor()
    {
        if (HandWriteTool.mEditor != null)
        {
            for (int i = 0; i < HandWriteTool.mEditor.length; i++)
            {
                if (HandWriteTool.mEditor[i] != null)
                {
                    HandWriteTool.mEditor[i] = null;
                }
            }
            HandWriteTool.setmEditor(null);
        }
    }
    
    /**
     * 合并签批
     * 
     * @Description<功能详细描述>
     * @flag false 退出保存， true：发送保存
     * @LastModifiedDate：2013-10-18
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void saveBitmap(boolean flag)
    {
        bHandler.sendEmptyMessage(SHOWDIALOG);
        long start = SystemClock.currentThreadTimeMillis();
        
        if (HandWriteTool.mEditor != null)
        {
            for (int i = 0; i < HandWriteTool.mEditor.length; i++)
            {
                
                if (HandWriteTool.mEditor[i] != null)
                {
                    if (HandWriteTool.type == 0)
                    {
                        ((CustomHandWriteEditor)HandWriteTool.mEditor[i]).createBitmap(mPdfPath, i);
                        
                    }
                    else if (HandWriteTool.type == 1)
                    {
                        ((SpenView)HandWriteTool.mEditor[i]).createBitmap(mPdfPath, i);
                    }
                }
                
            }
        }
        long end = SystemClock.currentThreadTimeMillis();
        LogUtil.i("hoperun", "********saveBitmap time=" + (end - start));
        
        Message msg = new Message();
        msg.what = CLOSEDIALOG;
        msg.obj = flag;
        bHandler.sendMessage(msg);
    }
    
    public boolean isCurrentHasHistory()
    {
        if (HandWriteTool.mEditor != null && (current > 0 && HandWriteTool.mEditor[current - 1] != null))
        {
            if (HandWriteTool.type == 0)
            {
                
                if (((CustomHandWriteEditor)HandWriteTool.mEditor[current - 1]).hasEditor())
                {
                    return true;
                }
            }
            else if (HandWriteTool.type == 1)
            {
                if (((SpenView)HandWriteTool.mEditor[current - 1]).hasEditor())
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    DocSendView sendView;
    
    /**
     * 点击“发送”、“信息”按钮时，弹出的页面
     * 
     * @Description<功能详细描述>
     * 
     * @param button
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void showView()
    {
        
        sendView = null;
        sendView = new DocSendView(DocFlowEditImageActivity.this, 0, mIntent_values);
        sendView.setMviewbacklistener(mViewBacklisten);
        layout.addView(sendView);
        sendView.bringToFront();
        sendView.invalidate();
        
    }
    
    /** 显示“信息view” **/
    PopupWindow  mPopupWindow;
    
    InfoView     mInfoView;
    
    CustomDialog confirmDialog;
    
    /**
     * 
     * 显示“信息view”
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-1-28
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void showInfoView()
    {
        // mInfoView = null;
        // mInfoView = new InfoView(mContext, mIntent_values);
        // mInfoView.setMviewbacklistener(mViewBacklisten);
        // mPopupWindow = null;
        // mPopupWindow = new PopupWindow(mInfoView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        // mPopupWindow.setFocusable(true);
        // mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        // mPopupWindow.setOutsideTouchable(true);
        
        if (mInfoView != null)
        {
            layout.removeView(mInfoView);
        }
        mInfoView = null;
        mInfoView = new InfoView(mContext, mIntent_values, mBottom.getHeight());
        mInfoView.setMviewbacklistener(mViewBacklisten);
        layout.addView(mInfoView);
        mInfoView.bringToFront();
        mInfoView.invalidate();
        
    }
    
    // // 显示“信息view”和“发送view”
    // PopupWindow mPopupWindow;
    //
    // View view;
    //
    // RelativeLayout pdf_popview;
    //
    // InfoView mInfoView;
    //
    // DocSendView sendView;
    //
    // CustomDialog confirmDialog;
    //
    // “发送view”和“信息view”的监听
    onViewBackListen mViewBacklisten = new onViewBackListen()
                                     {
                                         
                                         @Override
                                         public void onViewBackListener(int type)
                                         {
                                             // TODO Auto-generated method stub
                                             switch (type)
                                             {
                                                 case ConstState.SEND_CANCEL:
                                                     // if (mPopupWindow != null)
                                                     // {
                                                     // mPopupWindow.dismiss();
                                                     // }
                                                     if (mInfoView != null)
                                                     {
                                                         layout.removeView(mInfoView);
                                                         mInfoView = null;
                                                     }
                                                     break;
                                                 
                                                 case ConstState.SEND_SUCCESS:
                                                     if (mPopupWindow != null)
                                                     {
                                                         mPopupWindow.dismiss();
                                                     }
                                                     Intent intent = new Intent();
                                                     intent.setAction(OfficialSecondFragment.docSendSuccess);
                                                     intent.putExtra("docid", mPdfFilemessageId);
                                                     sendBroadcast(intent);
                                                     
                                                     finish();
                                                     break;
                                                 case ConstState.SEND_BEGIN:
                                                     if (mPopupWindow != null)
                                                     {
                                                         mPopupWindow.dismiss();
                                                     }
                                                     final Thread th = new Thread()
                                                     {
                                                         @Override
                                                         public void run()
                                                         {
                                                             // TODO Auto-generated method stub
                                                             saveBitmap(true);
                                                             super.run();
                                                         }
                                                     };
                                                     
                                                     confirmDialog =
                                                         CustomDialog.newInstance(getResources().getString(R.string.pdf_send_confirm),
                                                             getResources().getString(R.string.pdf_Confirm),
                                                             getResources().getString(R.string.pdf_Cancel));
                                                     
                                                     confirmDialog.show(getSupportFragmentManager(), "BindDeviceDialog");
                                                     confirmDialog.setLeftListener(new CustomDialogListener()
                                                     {
                                                         
                                                         @Override
                                                         public void Onclick()
                                                         {
                                                             confirmDialog.dismiss();
                                                             th.start();
                                                         }
                                                     });
                                                     confirmDialog.setRightListener(new CustomDialogListener()
                                                     {
                                                         
                                                         @Override
                                                         public void Onclick()
                                                         {
                                                             confirmDialog.dismiss();
                                                         }
                                                     });
                                                     break;
                                                 case ConstState.SEND_FAILE:
                                                     if (mPopupWindow != null)
                                                     {
                                                         mPopupWindow.dismiss();
                                                     }
                                                     break;
                                                 
                                                 default:
                                                     break;
                                             }
                                         }
                                     };
    
    // /**
    // * 点击“发送”、“信息”按钮时，弹出的页面
    // *
    // * @Description<功能详细描述>
    // *
    // * @param button
    // * @LastModifiedDate：2013-12-2
    // * @author ren_qiujing
    // * @EditHistory：<修改内容><修改人>
    // */
    // private void showView(CustomButton button)
    // {
    // mSelectButton = button;
    // if (view == null)
    // {
    // view = LayoutInflater.from(mContext).inflate(R.layout.pdf_popviewlayout, null);
    // }
    // if (pdf_popview == null)
    // {
    // pdf_popview = (RelativeLayout)view.findViewById(R.id.pdf_popview);
    // }
    // ImageView mArrowView = (ImageView)view.findViewById(R.id.pdf_left_arrow_image);
    //
    // mLeftmidTop = mLeftmidLayout.getTop();
    // int top = button.getTop();
    // int button_midTop = mLeftmidTop + top + button.getHeight() / 2;
    //
    // // 计算箭头的具体位置
    // MarginLayoutParams lp = (MarginLayoutParams)mArrowView.getLayoutParams();
    // lp.topMargin = button_midTop - mArrowView.getHeight() / 2;
    // mArrowView.setLayoutParams(lp);
    //
    // button.setButtonClickedEable(true);
    // mPopupWindow = null;
    // mPopupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    // mPopupWindow.setFocusable(true);
    //
    // mPopupWindow.setOnDismissListener(new OnDismissListener()
    // {
    // @Override
    // public void onDismiss()
    // {
    // // TODO Auto-generated method stub
    // if (mSelectButton != null)
    // {
    // mSelectButton.setPressed(false);
    // mSelectButton.setButtonClickedEable(false);
    // }
    // }
    // });
    //
    // pdf_popview.removeAllViews();
    // switch (button.getId())
    // {
    // case R.id.pdf_info:
    // mInfoView = null;
    // mInfoView = new InfoView(mContext, button_midTop, mIntent_values);
    // mInfoView.setMviewbacklistener(mViewBacklisten);
    //
    // pdf_popview.addView(mInfoView);
    // // mPopupWindow.setOutsideTouchable(false);
    // mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    // mPopupWindow.setOutsideTouchable(true);
    // break;
    //
    // case R.id.pdf_send:
    // sendView = null;
    // sendView = new DocSendView(DocFlowEditImageActivity.this, button_midTop, mIntent_values);
    // sendView.setMviewbacklistener(mViewBacklisten);
    //
    // pdf_popview.addView(sendView);
    // // mPopupWindow.setOutsideTouchable(false);
    // mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    // mPopupWindow.setOutsideTouchable(true);
    // break;
    //
    // default:
    // break;
    // }
    // view.setOnClickListener(new OnClickListener()
    // {
    //
    // @Override
    // public void onClick(View v)
    // {
    // // TODO Auto-generated method stub
    // mPopupWindow.dismiss();
    // }
    // });
    // mPopupWindow.showAtLocation(mLeftButtons, Gravity.LEFT | Gravity.TOP, mLeftButtons.getWidth(), 0);
    // }
    
    /**
     * 加锁、解锁
     * 
     * @Description<功能详细描述>
     * 
     * @param flag
     * @LastModifiedDate：2013-11-13
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void setLockStatus(boolean flag)
    {
        int requestType = -1;
        JSONObject body = new JSONObject();
        try
        {
            body.put("docid", mPdfFilemessageId);
            if (flag)
            {
                body.put("lockstatus", "1");
                requestType = RequestTypeConstants.SETLOCKSTATUSLOCK;
            }
            else
            {
                body.put("lockstatus", "0");
                requestType = RequestTypeConstants.SETLOCKSTATUSUNLOCK;
            }
            body.put("type", mPdfType);
            
            mSetLockStatusNetTask = new HttpNetFactoryCreator(requestType).create();
            
            NetRequestController.setLockStatusRequest(mSetLockStatusNetTask, mHandler, requestType, body);
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 有锁时，弹出的对话框
     */
    Dialog mLockInfoDialog;
    
    Dialog mLockInfoDetailDialog;
    
    /**
     * 有锁时，弹出的对话框
     */
    private void showLockInfo()
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lockinfo_layout, null);
        
        mLockInfoDialog = new Dialog(mContext, R.style.DialogStyle);
        mLockInfoDialog.setContentView(view);
        mLockInfoDialog.setCancelable(false);
        
        Button leftCancel = (Button)view.findViewById(R.id.left_btn);
        Button rightCheck = (Button)view.findViewById(R.id.right_btn);
        
        leftCancel.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                mLockInfoDialog.dismiss();
            }
        });
        
        rightCheck.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                mLockInfoDialog.dismiss();
                showLockInfoDetail();
                
            }
        });
        mLockInfoDialog.show();
        
    }
    
    /**
     * 有锁时，弹出的锁住人详细信息的对话框
     */
    private void showLockInfoDetail()
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lockinfo_layout_detail, null);
        
        TextView tv = (TextView)view.findViewById(R.id.lockinfo_detail_tv);
        
        tv.setText(mPdfLockNAME + "，正在签批！");
        
        mLockInfoDetailDialog = new Dialog(mContext, R.style.DialogStyle);
        mLockInfoDetailDialog.setContentView(view);
        mLockInfoDetailDialog.setCancelable(false);
        Button leftCancel = (Button)view.findViewById(R.id.left_btn);
        
        leftCancel.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // 点击时间间隔太短则不触发
                mLockInfoDetailDialog.dismiss();
            }
        });
        
        mLockInfoDetailDialog.show();
        
    }
    
    /**
     * 显示提示信息
     * 
     * @Description<功能详细描述>
     * 
     * @param msg
     * @LastModifiedDate：2013-11-14
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void showToastMessage(String msg)
    {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
    
    // /**
    // * 上下页按钮的显示与隐藏
    // *
    // * @Description<功能详细描述>
    // *
    // * @LastModifiedDate：2013-11-29
    // * @author ren_qiujing
    // * @EditHistory：<修改内容><修改人>
    // */
    // private void buttonShow()
    // {
    //
    // if (totalPageNums == 1)
    // {
    // mPre.setVisibility(View.INVISIBLE);
    // mNext.setVisibility(View.INVISIBLE);
    // }
    // else if (current == totalPageNums)
    // {
    // mPre.setVisibility(View.VISIBLE);
    // mNext.setVisibility(View.INVISIBLE);
    // }
    // else if (current == 1)
    // {
    // mPre.setVisibility(View.INVISIBLE);
    // mNext.setVisibility(View.VISIBLE);
    // }
    // else
    // {
    // mPre.setVisibility(View.VISIBLE);
    // mNext.setVisibility(View.VISIBLE);
    // }
    // }
    onCloseAttchViewListen attchListen = new onCloseAttchViewListen()
                                       {
                                           
                                           @Override
                                           public void onShowThisViewListener(String selectFilePath)
                                           {
                                               if (mAttchInfoView != null)
                                               {
                                                   layout.removeView(mAttchInfoView);
                                                   mAttchInfoView = null;
                                               }
                                               mPdfPath = selectFilePath;
                                               mDocView.clearAdaptePageView();
                                               restartMe();
                                               // Toast.makeText(DocFlowEditImageActivity.this,
                                               // selectFilePath,
                                               // Toast.LENGTH_LONG).show();
                                           }
                                           
                                           @Override
                                           public void onCloseThisViewListener()
                                           {
                                               if (mAttchInfoView != null)
                                               {
                                                   layout.removeView(mAttchInfoView);
                                                   mAttchInfoView = null;
                                               }
                                           }
                                       };
    
    /**
     * 帮助的监听
     */
    onCloseThisViewListen  helpListen  = new onCloseThisViewListen()
                                       {
                                           
                                           @Override
                                           public void onShowThisViewListener()
                                           {
                                               Message msg = new Message();
                                               msg.what = BUTTON_HELP;
                                               msg.obj = true;
                                               
                                               bHandler.sendMessage(msg);
                                           }
                                           
                                           @Override
                                           public void onCloseThisViewListener()
                                           {
                                               Message msg = new Message();
                                               msg.what = BUTTON_HELP;
                                               msg.obj = false;
                                               
                                               bHandler.sendMessage(msg);
                                           }
                                       };
    
    /**
     * 显示帮助
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2013-11-29
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void showHelpView(boolean isClickButton, boolean isSignHelp)
    {
        if (mHelpKnow != null)
        {
            layout.removeView(mHelpKnow);
        }
        mHelpKnow = null;
        mHelpKnow = new HelpView(mContext, isClickButton, isSignHelp, helpListen);
    }
    
    /**
     * 签批，结束签批
     * 
     * @Description<功能详细描述>
     * 
     * @param false 结束签批，true 开始签批
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void signStatus(boolean flag)
    {
        if (flag)
        {
            // mSignEnd.setVisibility(View.VISIBLE);
            // mBottom.setVisibility(View.GONE);
            mSignRL.setVisibility(View.VISIBLE);
        }
        else
        {
            // mSignEnd.setVisibility(View.GONE);
            // mBottom.setVisibility(View.VISIBLE);
            mSignRL.setVisibility(View.GONE);
        }
        isSignState = flag;
        mDocView.setHandWriteEditorSign(isSignState);
    }
    
    // 显示loading 对话框
    private void showLoadingDialog()
    {
        if (mWaitDialog != null && mWaitDialog.isShowing())
        {
            mWaitDialog.dismiss();
        }
        mWaitDialog = WaitDialog.newInstance();
        mWaitDialog.setKeybackEnable(false);
        mWaitDialog.show(getSupportFragmentManager(), "waitDialog");
    }
    
    // 关闭loading 对话框
    private void closeLoadingDialog()
    {
        if (mWaitDialog != null)
        {
            mWaitDialog.dismiss();
        }
    }
    
    // 保存笔的颜色，粗细
    public void saveSignStatus()
    {
        SharedPreferences sharePreferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        
        Editor ed = sharePreferences.edit();
        ed.putInt("signColor", HandWriteTool.mSignColor);
        ed.putInt("signStress", HandWriteTool.mSignStress);
        
        ed.commit();
    }
    
    public void beginSign()
    {
        MuPDFPageView view = (MuPDFPageView)mDocView.getDisplayedView();
        if (!view.getHandWriteSignEnable())
        {
            return;
        }
        
        signStatus(true);
        signController(view, true);
    }
    
    public void endSign()
    {
        MuPDFPageView view = (MuPDFPageView)mDocView.getDisplayedView();
        signStatus(false);
        signController(view, false);
    }
    
    private int mColorSelected;
    
    private int mLineSelected;
    
    public void signController(final MuPDFPageView view, boolean flag)
    {
        
        if (!view.setHandWriteSign(flag))
        {
            return;
        }
        
        if (flag)
        {
            SharedPreferences sharePreferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
            HandWriteTool.mSignColor = sharePreferences.getInt("signColor", HandWriteTool.mSignColor);
            HandWriteTool.mSignStress = sharePreferences.getInt("signStress", HandWriteTool.mSignStress);
            
            switch (HandWriteTool.mSignColor)
            {
                case ConstState.COLOR_PEN_RED:
                    mColorSelected = R.drawable.pen_color_red;
                    break;
                case ConstState.COLOR_PEN_GREED:
                    mColorSelected = R.drawable.pen_color_green;
                    break;
                case ConstState.COLOR_PEN_BLACK:
                    mColorSelected = R.drawable.pen_color_black;
                    break;
                case ConstState.COLOR_PEN_BLUE:
                    mColorSelected = R.drawable.pen_color_blue;
                    break;
                default:
                    break;
            }
            if (mColorSelected != -1)
            {
                mSignColorIm.setBackgroundResource(mColorSelected);
                
                setSignBackground();
                mSignColorRL.setBackgroundResource(R.drawable.choose_qp);
            }
            switch (HandWriteTool.mSignStress)
            {
                case ConstState.SIGN_STRESS_CRUDE:
                    mLineSelected = R.drawable.pen_crude;
                    break;
                case ConstState.SIGN_STRESS_NORMAL:
                    mLineSelected = R.drawable.pen_normal;
                    break;
                case ConstState.SIGN_STRESS_FINDE:
                    mLineSelected = R.drawable.pen_fine;
                    break;
                
                default:
                    break;
            }
            
            if (mLineSelected != -1)
            {
                mSignLineIm.setBackgroundResource(mLineSelected);
            }
            
            for (int i = 0; i < HandWriteTool.mEditor.length; i++)
            {
                if (HandWriteTool.mEditor[i] != null)
                {
                    if (HandWriteTool.type == 0)
                    {
                        ((CustomHandWriteEditor)HandWriteTool.mEditor[i]).setPaintColor(HandWriteTool.mSignColor);
                        ((CustomHandWriteEditor)HandWriteTool.mEditor[i]).setStress(HandWriteTool.mSignStress);
                        ((CustomHandWriteEditor)HandWriteTool.mEditor[i]).setPen();
                    }
                    else if (HandWriteTool.type == 1)
                    {
                        ((SpenView)HandWriteTool.mEditor[i]).setPaintColor(HandWriteTool.mSignColor);
                        ((SpenView)HandWriteTool.mEditor[i]).setStress(HandWriteTool.mSignStress);
                        ((SpenView)HandWriteTool.mEditor[i]).setPen();
                    }
                }
            }
            setHandWriteEditorPen(view);
        }
        else
        {
        }
    }
    
    /**
     * 设置签批布局上所有的按钮背景
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-2-10
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void setSignBackground()
    {
        mSignColorRL.setBackgroundResource(R.drawable.selector_pdf_sign_background);
        mSignLineRL.setBackgroundResource(R.drawable.selector_pdf_sign_background);
        mSignEraserRL.setBackgroundResource(R.drawable.selector_pdf_sign_background);
        mSignUndoRL.setBackgroundResource(R.drawable.selector_pdf_sign_background);
        mSignRedoRL.setBackgroundResource(R.drawable.selector_pdf_sign_background);
        mSignEmptyRL.setBackgroundResource(R.drawable.selector_pdf_sign_background);
        mSignSaveRL.setBackgroundResource(R.drawable.selector_pdf_sign_background);
        
    }
    
    /**
     * 还原界面上橡皮擦图标
     * 
     * @Description<功能详细描述>
     * 
     * @param view view
     * @LastModifiedDate：2013-10-9
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void setHandWriteEditorPen(MuPDFPageView view)
    {
        // 如果是橡皮擦状态时，取消橡皮擦功能
        if (isEraser)
        {
            view.setPen();
            isEraser = false;
        }
    }
    
    /**
     * 设置橡皮擦
     * 
     * @Description<功能详细描述>
     * 
     * @param view
     * @LastModifiedDate：2013-9-27
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setHandWriteEditorErase(MuPDFPageView view)
    {
        if (!isEraser)
        {
            view.setErase();
            isEraser = true;
        }
    }
    
    /**
     * 清空
     * 
     * @Description<功能详细描述>
     * 
     * @param view view
     * @LastModifiedDate：2013-9-27
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setHandWriteEditorEmpty(MuPDFPageView view)
    {
        view.setEmpty();
    }
    
    /**
     * 重做
     * 
     * @Description<功能详细描述>
     * 
     * @param view view
     * @LastModifiedDate：2013-9-27
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setHandWriteEditorRedo(MuPDFPageView view)
    {
        view.setRedo();
    }
    
    /**
     * 还原
     * 
     * @Description<功能详细描述>
     * 
     * @param view view
     * @LastModifiedDate：2013-9-27
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public void setHandWriteEditorUndo(MuPDFPageView view)
    {
        view.setUndo();
    }
    
    /**
     * 设置颜色选择界面和粗细页面消失
     * 
     * @Description<功能详细描述>
     * 
     * @LastModifiedDate：2014-2-11
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    private void setSignSelectedClose()
    {
        mSignColorSelectedRL.setVisibility(View.GONE);
        mSignLineSelectedRL.setVisibility(View.GONE);
    }
    
    public boolean hasSignHistory()
    {
        if (HandWriteTool.type == 0)
        {
            
            if (HandWriteTool.getmEditor() != null && HandWriteTool.getmEditor().length > 0)
            {
                for (int i = 0; i < HandWriteTool.getmEditor().length; i++)
                {
                    CustomHandWriteEditor item = (CustomHandWriteEditor)HandWriteTool.getmEditor()[i];
                    if (item != null && item.hasEditor())
                    {
                        return true;
                    }
                }
                return false;
            }
            else
            {
                return false;
            }
            
        }
        else if (HandWriteTool.type == 1)
        {
            if (HandWriteTool.getmEditor() != null && HandWriteTool.getmEditor().length > 0)
            {
                for (int i = 0; i < HandWriteTool.getmEditor().length; i++)
                {
                    SpenView item = (SpenView)HandWriteTool.getmEditor()[i];
                    if (item != null && item.hasEditor())
                    {
                        return true;
                    }
                }
                return false;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    
    private void backPdfReader()
    {
        if (!hasSignHistory())
        {
            finish();
            return;
        }
        
        final Thread th = new Thread()
        {
            
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                saveBitmap(false);
                super.run();
            }
        };
        
        confirmDialog =
            CustomDialog.newInstance(("是否合并签批的内容？"),
                getResources().getString(R.string.pdf_Confirm),
                getResources().getString(R.string.pdf_Cancel));
        
        confirmDialog.show(getSupportFragmentManager(), "BindDeviceDialog");
        confirmDialog.setLeftListener(new CustomDialogListener()
        {
            
            @Override
            public void Onclick()
            {
                confirmDialog.dismiss();
                th.start();
            }
        });
        confirmDialog.setRightListener(new CustomDialogListener()
        {
            
            @Override
            public void Onclick()
            {
                confirmDialog.dismiss();
                finish();
            }
        });
    }
    
}
