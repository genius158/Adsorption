package com.yan.recyclerviewadsorption;

/**
 * Created by yan on 2017/12/20 0020
 */

public class AdsorptionDataAdapter<T, V> implements Cloneable {
    public boolean isAdsorption;
    public T adsorptionData;
    public V itemData;

    public AdsorptionDataAdapter(boolean isAdsorption, T adsorptionData) {
        this.isAdsorption = isAdsorption;
        this.adsorptionData = adsorptionData;
    }

    public AdsorptionDataAdapter(boolean isAdsorption, T adsorptionData, V itemData) {
        this(isAdsorption, adsorptionData);
        setItemData(itemData);
    }

    public AdsorptionDataAdapter setItemData(V itemData) {
        this.itemData = itemData;
        return this;
    }

    @Override
    public AdsorptionDataAdapter clone() {
        try {
            AdsorptionDataAdapter dataAdapter = (AdsorptionDataAdapter) super.clone();
            dataAdapter.isAdsorption = false;
            return dataAdapter;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
