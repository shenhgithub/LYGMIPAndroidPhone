package com.hoperun.project.ui.warehouse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class InstallApkReceiver extends BroadcastReceiver
{
    
    private Handler                   handler;
    
    private String                    packageName;
    
    private static InstallApkReceiver installApkReceiver;
    
    public static InstallApkReceiver newInstallApkReceiver(Handler handler)
    {
        if (installApkReceiver == null)
        {
            installApkReceiver = new InstallApkReceiver(handler);
        }
        return installApkReceiver;
    }
    
    private InstallApkReceiver(Handler handler)
    {
        this.handler = handler;
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED"))
        {
            // if (packageName != null && intent.getDataString() != null)
            // if (packageName.equals(intent.getDataString().replaceAll("package:", "").trim()))
            // {
            handler.sendEmptyMessage(200);
            // }
        }
    }
    
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }
}