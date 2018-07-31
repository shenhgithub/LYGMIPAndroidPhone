package com.hoperun.manager.components.date;

/**
 * 
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author gu_leilei
 * @Version [版本号, 2013-10-18]
 */
public interface WheelAdapter
{
    /**
     * 
     * Gets items count
     * 
     * @Description<功能详细描述>
     * 
     * @return the count of wheel items
     * @LastModifiedDate：2013-10-18
     * @author gu_leilei
     * @EditHistory：<修改内容><修改人>
     */
    public int getItemsCount();
    
    /**
     * Gets a wheel item by index.
     * 
     * @param index the item index
     * @return the wheel item text or null
     */
    public String getItem(int index);
    
    /**
     * Gets maximum item length. It is used to determine the wheel width. If -1 is returned there will be used the
     * default wheel width.
     * 
     * @return the maximum item length or -1
     */
    public int getMaximumLength();
}
