/*
 * File name:  DocInterface.java
 * Copyright:  Copyright (c) 2006-2013 hoperun Inc,  All rights reserved
 * Description:  <描述>
 * Author:  ren_qiujing
 * Last modified date:  2013-12-4
 * Version:  <版本编号>
 * Edit history:  <修改内容><修改人>
 */
package com.hoperun.project.ui.baseui.baseInterface;

/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-12-4]
 */
public class baseUiInterface
{
    public interface OnResultListen
    {
        public void setOnResultListen();
        
        public void updateListView(String docId);
    }
    
    public interface onSearchResultListen
    {
        public void setOnSearchResultListen(int cound);
    }
}
