/*
 * File name:  PDFSendNextInfo.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-10-16
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.artifex.send.GWLZSend;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-10-16]
 */
public class PDFNextStepListEntity
{
    private String mNextStepName;
    
    private String mNextStepGuid;
    
    public String getmNextStepName()
    {
        return mNextStepName;
    }
    
    public void setmNextStepName(String mNextStepName)
    {
        this.mNextStepName = mNextStepName;
    }
    
    public String getmNextStepGuid()
    {
        return mNextStepGuid;
    }
    
    public void setmNextStepGuid(String mNextStepGuid)
    {
        this.mNextStepGuid = mNextStepGuid;
    }
    
}
