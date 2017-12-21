package com.yan.recyclerviewadsorption;

/**
 * Created by yan on 2017/12/20 0020
 */

public class ItemAdsorptionAdapter<T, V> implements Cloneable {
    public boolean isAdsorption;
    public T itemAdsorption;
    public V itemData;

    public ItemAdsorptionAdapter(boolean isAdsorption, T itemAdsorption) {
        this.isAdsorption = isAdsorption;
        this.itemAdsorption = itemAdsorption;
    }

    public ItemAdsorptionAdapter(boolean isAdsorption, T itemAdsorption, V itemData) {
        this(isAdsorption, itemAdsorption);
        setItemData(itemData);
    }

    public ItemAdsorptionAdapter setItemData(V itemData) {
        this.itemData = itemData;
        return this;
    }

    @Override
    public ItemAdsorptionAdapter clone() {
        try {
            ItemAdsorptionAdapter dataAdapter = (ItemAdsorptionAdapter) super.clone();
            dataAdapter.isAdsorption = false;
            return dataAdapter;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
