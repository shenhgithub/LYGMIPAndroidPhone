package com.artifex.mupdf;

import java.io.Serializable;

import com.hoperun.mip.utils.LogUtil;

public final class Laf
{
    /**
     * 
     * 输出log日志
     * 
     * @Description 输出log日志
     * @param msg
     */
    public static void i(Serializable msg)
    {
        LogUtil.i("laflog", String.valueOf(msg));
    }
}
