package com.yan.recyclerviewadsorption;

/**
 * Created by yan on 2017/12/20 0020
 */

public class ItemAdsorptionAdapter<T, V> implements Cloneable {
    public boolean isAdsorption;
    public T adsorptionData;
    public V itemData;

    public ItemAdsorptionAdapter(boolean isAdsorption, T adsorptionData) {
        this.isAdsorption = isAdsorption;
        this.adsorptionData = adsorptionData;
    }

    public ItemAdsorptionAdapter(boolean isAdsorption, T adsorptionData, V itemData) {
        this(isAdsorption, adsorptionData);
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
