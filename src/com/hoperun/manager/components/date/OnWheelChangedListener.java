package com.hoperun.manager.components.date;

/**
 * Wheel changed listener interface.
 * <p>
 * The currentItemChanged() method is called whenever current wheel positions is changed:
 * <li>New Wheel position is set
 * <li>Wheel view is scrolled
 */
public interface OnWheelChangedListener
{
    /**
     * 
     * Callback method to be invoked when current item changed
     * 
     * @Description<功能详细描述>
     * 
     * @param wheel the wheel view whose state has changed
     * @param oldValue the old value of current item
     * @param newValue the new value of current item
     * @LastModifiedDate：2013-10-18
     * @author gu_leilei
     * @EditHistory：<修改内容><修改人>
     */
    void onChanged(WheelView wheel, int oldValue, int newValue);
}
