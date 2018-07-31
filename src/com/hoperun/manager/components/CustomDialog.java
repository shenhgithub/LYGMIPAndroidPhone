package com.hoperun.manager.components;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hoperun.mip.utils.LogUtil;
import com.hoperun.miplygphone.R;

/**
 * 
 * 自定义对话框，采用3.0版本后新的組件DialogFragment替代Dialog
 * 
 * CustomDialog dialog = CustomDialog.newInstance("请检查你的网络", "确定", mHandler,handlertype);
 * dialog.show(getFragmentManager(),"netfailDialog");
 * 
 * 
 * @author shen_feng
 * @Version [版本号, 2013-9-24]
 */
public class CustomDialog extends DialogFragment
{
    // /** 对话框标记 **/
    // private String tag;
    
    /** 左边按钮的监听器 **/
    CustomDialogListener leftListener;
    
    /** 右边按钮的监听器 **/
    CustomDialogListener rightLitener;
    
    /** 中间按钮的监听器 **/
    CustomDialogListener midLitener;
    
    /**
     * 
     * 
     * @Description 实例化对话框
     * 
     * @return 对话框
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static CustomDialog newInstance()
    {
        return new CustomDialog();
    }
    
    /**
     * 
     * 实例化对话框
     * 
     * @Description 实例化对话框
     * 
     * @param message 消息
     * @param leftbtn_string 左边按钮文字
     * @param rightbtn_string 右边按钮文字
     * @return 对话框
     * @LastModifiedDate：2013-10-16
     * @author shen_feng
     * @EditHistory：<修改内容><修改人>
     */
    public static CustomDialog newInstance(String message, String leftbtn_string, String rightbtn_string)
    {
        CustomDialog frag = new CustomDialog();
        Bundle b = new Bundle();
        b.putString("message", message);
        b.putString("leftbtn_string", leftbtn_string);
        b.putString("rightbtn_string", rightbtn_string);
        frag.setArguments(b);
        return frag;
    }
    
    /**
     * 
     * 实例化对话框
     * 
     * @Description<功能详细描述>
     * 
     * @param message 消息
     * @param midbtn_string 中间按钮文字
     * @return 对话框
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public static CustomDialog newInstance(String message, String midbtn_string)
    {
        CustomDialog frag = new CustomDialog();
        Bundle b = new Bundle();
        b.putString("message", message);
        b.putString("midbtn_string", midbtn_string);
        frag.setArguments(b);
        return frag;
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
        
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        
        View view = inflater.inflate(R.layout.customdialog, null, false);
        
        // String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        
        if (message != null && message.length() > 0)
        {
            // 对话框信息
            TextView m = (TextView)view.findViewById(R.id.dialog_message);
            m.setText(message);
        }
        
        // 对话框按鈕
        Button leftbtn = (Button)view.findViewById(R.id.left_btn);
        Button rightbtn = (Button)view.findViewById(R.id.right_btn);
        // 中间单个按钮
        Button mid_btn = (Button)view.findViewById(R.id.mid_btn);
        
        // 左边按钮
        String leftbtn_string = getArguments().getString("leftbtn_string");
        
        // 右边按钮
        String rightbtn_string = getArguments().getString("rightbtn_string");
        
        // 中间单个按钮
        String midbtn_string = getArguments().getString("midbtn_string");
        
        if (leftbtn_string != null && leftbtn_string.length() > 0)
        {
            leftbtn.setText(leftbtn_string);
        }
        if (rightbtn_string != null && rightbtn_string.length() > 0)
        {
            rightbtn.setText(rightbtn_string);
        }
        
        // 如果显示一个按钮，这另外2个按钮布局不显示
        if (midbtn_string != null && midbtn_string.length() > 0)
        {
            mid_btn.setText(midbtn_string);
            
            LinearLayout bottom_2btn_layout = (LinearLayout)view.findViewById(R.id.bottom_2btn_layout);
            
            LinearLayout bottom_1btn_layout = (LinearLayout)view.findViewById(R.id.bottom_1btn_layout);
            
            bottom_2btn_layout.setVisibility(View.GONE);
            bottom_1btn_layout.setVisibility(View.VISIBLE);
        }
        
        // 右边按钮
        rightbtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                rightLitener.Onclick();
            }
            
        });
        
        leftbtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                
                leftListener.Onclick();
                
            }
            
        });
        
        mid_btn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                midLitener.Onclick();
                
                dialog.dismiss();
            }
        });
        
        dialog.setContentView(view);
        
        return dialog;
        
    }
    
    @Override
    public void show(FragmentManager manager, String tag)
    {
        super.show(manager, tag);
        // this.tag = tag;
        LogUtil.i("", "tag=" + tag);
    }
    
    /**
     * 
     * 设置左边按钮的listener
     * 
     * @Description<功能详细描述>
     * 
     * @param listen listen
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setLeftListener(CustomDialogListener listen)
    {
        this.leftListener = listen;
    }
    
    /**
     * 
     * 设置右边按钮的listener
     * 
     * @Description<功能详细描述>
     * 
     * @param listen listen
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setRightListener(CustomDialogListener listen)
    {
        this.rightLitener = listen;
    }
    
    /**
     * 
     * 设置中间按钮的listener
     * 
     * @Description<功能详细描述>
     * 
     * @param listen listen
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    public void setMidListener(CustomDialogListener listen)
    {
        this.midLitener = listen;
    }
}
