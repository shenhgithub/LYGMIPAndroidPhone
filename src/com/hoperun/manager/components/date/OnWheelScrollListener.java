package com.hoperun.manager.components.date;

/**
 * Wheel scrolled listener interface.
 */
public interface OnWheelScrollListener
{
    
    /**
     * 
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param wheel wheel
     * @LastModifiedDate：2013-10-18
     * @author gu_leilei
     * @EditHistory：<修改内容><修改人>
     */
    void onScrollingStarted(WheelView wheel);
    
    /**
     * 
     * <一句话功能简述>
     * 
     * @Description<功能详细描述>
     * 
     * @param wheel wheel
     * @LastModifiedDate：2013-10-18
     * @author gu_leilei
     * @EditHistory：<修改内容><修改人>
     */
    void onScrollingFinished(WheelView wheel);
}
