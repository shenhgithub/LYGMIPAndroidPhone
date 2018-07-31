package com.hoperun.manager.components;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hoperun.miplygphone.R;

/**
 * 
 * 等待框，采用3.0版本后新的組件DialogFragment替代Dialog
 * 
 * CustomDialog dialog = CustomDialog.newInstance("请检查你的网络", "确定", mHandler); dialog.show(getFragmentManager());
 * 
 * 
 * @author shen_feng
 * @Version [版本号, 2013-9-29]
 */
public class WaitDialog extends DialogFragment
{
    private boolean isKeybackEnable = true;
    
    /**
     * 
     * 实例一个对话框
     * 
     * @Description 实例一个对话框
     * 
     * @return 对话框
     * @LastModifiedDate：2013-10-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static WaitDialog newInstance()
    {
        return new WaitDialog();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new OnKeyListener()
        {
            
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                // TODO Auto-generated method stub
                if (!isKeybackEnable && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
        
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        
        View view = inflater.inflate(R.layout.waitdialog, null, false);
        
        ImageView mImg = (ImageView)view.findViewById(R.id.waitdialog_img);
        
        AnimationDrawable animationDrawable = (AnimationDrawable)mImg.getBackground();
        
        animationDrawable.start();
        
        dialog.setContentView(view);
        
        return dialog;
        
    }
    
    @Override
    public void show(FragmentManager manager, String tag)
    {
        super.show(manager, tag);
    }
    
    /**
     * 
     * 显示对话框
     * 
     * @Description 显示对话框
     * 
     * @param manager 管理器
     * @LastModifiedDate：2013-10-25
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void show(FragmentManager manager)
    {
        super.show(manager, "waitDialog");
    }
    
    /**
     * 是否显示
     * 
     * @Description<功能详细描述>
     * 
     * @return
     * @LastModifiedDate：2013-12-2
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public boolean isShowing()
    {
        if (getDialog() != null)
        {
            return getDialog().isShowing();
        }
        return false;
    }
    
    public void setKeybackEnable(boolean flag)
    {
        this.isKeybackEnable = flag;
    }
    
}
