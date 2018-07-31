package com.hoperun.manager.components.date;

/**
 * The simple Array wheel adapter
 * 
 * @param <T> the element type
 */
public class ArrayWheelAdapter<T> implements WheelAdapter
{
    /** The default items length */
    public static final int DEFAULT_LENGTH = -1;
    
    /** items */
    private T               items[];
    
    /** length */
    private int             length;
    
    /**
     * Constructor
     * 
     * @param items the items
     * @param length the max items length
     */
    public ArrayWheelAdapter(T items[], int length)
    {
        this.items = items;
        this.length = length;
    }
    
    /**
     * Contructor
     * 
     * @param items the items
     */
    public ArrayWheelAdapter(T items[])
    {
        this(items, DEFAULT_LENGTH);
    }
    
    /**
     * 
     * 重载方法
     * 
     * @param index index
     * @return null
     * @author gu_leilei
     */
    @Override
    public String getItem(int index)
    {
        if (index >= 0 && index < items.length)
        {
            return items[index].toString();
        }
        return null;
    }
    
    /**
     * 
     * 重载方法
     * 
     * @return items.length
     * @author gu_leilei
     */
    @Override
    public int getItemsCount()
    {
        return items.length;
    }
    
    /**
     * 
     * 重载方法
     * 
     * @return length
     * @author gu_leilei
     */
    @Override
    public int getMaximumLength()
    {
        return length;
    }
    
}
