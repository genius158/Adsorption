package com.yan.recyclerviewadsorption;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yan on 2017/11/5
 */
public class AdsorptionAdapter extends BaseDiffAdapter<Object, RecyclerView.ViewHolder> {
    private static final String TAG = "AdsorptionAdapter";

    private final int ADSORPTION_TYPE = 1;
    private final int DATA_TYPE = 2;

    private Context context;

    private RecyclerView recyclerView;

    private View adsorptionView;

    public AdsorptionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (((ItemAdsorptionAdapter) items.get(position)).isAdsorption) {
            return ADSORPTION_TYPE;
        }
        return DATA_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ADSORPTION_TYPE) {
            View typeView = LayoutInflater.from(context).inflate(R.layout.adsorption, parent, false);
            return new RecyclerView.ViewHolder(typeView) {
            };
        }
        return new RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (adsorptionView.getTag() == null) {
            onAdsorptionDataSet(((ItemAdsorptionAdapter) items.get(0)).itemAdsorption);
        }

        final View item = holder.itemView;
        item.setTag(R.id.tag_position, position);

        onItemDataSet(item, position);
    }

    /**
     * 可悬浮条目数据的设置
     *
     * @param objAdsorption
     * @return if data set
     */
    private void onAdsorptionDataSet(Object objAdsorption) {
        ItemAdsorption itemAdsorption = (ItemAdsorption) objAdsorption;
        TextView atv = adsorptionView.findViewById(R.id.tv);
        if (!TextUtils.equals(itemAdsorption.strIndex, atv.getText())) {
            atv.setText(itemAdsorption.strIndex);
        }
    }

    /**
     * RecyclerView内部布局数据的设置
     *
     * @param item
     * @param position
     */
    private void onItemDataSet(View item, int position) {
        ItemAdsorptionAdapter ada = (ItemAdsorptionAdapter) items.get(position);
        if (getItemViewType(position) == ADSORPTION_TYPE) {
            TextView tv = item.findViewById(R.id.tv);
            tv.setText(((ItemAdsorption) ada.itemAdsorption).strIndex);
            return;
        }

        ImageView iv = item.findViewById(R.id.iv);
        iv.setImageDrawable(ContextCompat.getDrawable(context, ((ItemData) ada.itemData).resId));
    }

    @SuppressLint("StaticFieldLeak")
    private void rvScrollTo(int adsorptionPosition) {
        int scrollToPosition = -1;
        ItemAdsorptionAdapter iaa = (ItemAdsorptionAdapter) items.get(adsorptionPosition);
        if (iaa.isAdsorption) {
            scrollToPosition = items.indexOf(iaa);
        } else {
            for (int i = 0; i < items.size(); i++) {
                ItemAdsorptionAdapter ia = (ItemAdsorptionAdapter) items.get(i);
                if (ia.itemData == null && ia.itemAdsorption == iaa.itemAdsorption) {
                    scrollToPosition = i;
                }
            }
        }
        recyclerView.smoothScrollToPosition(scrollToPosition);
    }

    private void onAdsorptionViewLoad() {
        try {
            if (recyclerView.getParent() == null) {
                throw new RuntimeException("RecyclerView must put in a ViewGroup");
            }
            ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
            if (adsorptionView == null) {
                FrameLayout frameLayout = new FrameLayout(context);
                frameLayout.setLayoutParams(recyclerView.getLayoutParams());
                frameLayout.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop()
                        , recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
                viewGroup.addView(frameLayout);

                adsorptionView = LayoutInflater.from(context).inflate(R.layout.adsorption, frameLayout, false);
                adsorptionView.setOnClickListener(onAdsorptionClick);

                frameLayout.addView(adsorptionView);
                frameLayout.setVisibility(View.INVISIBLE);
            }
            recyclerView.addOnScrollListener(onScrollListener);
        } catch (Exception e) {
            Log.w(TAG, "onAttachedToRecyclerView: " + e.getMessage());
        }
    }

    private final View.OnClickListener onAdsorptionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int adsorptionPosition = (int) v.getTag(R.id.tag_position);
            rvScrollTo(adsorptionPosition);
        }
    };

    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            final int offsetY = recyclerView.getPaddingTop();
            View adsorptionOutView = recyclerView.findChildViewUnder(0, offsetY);
            View adsorptionAreaView = recyclerView.findChildViewUnder(0, offsetY + adsorptionView.getHeight());
            if (adsorptionOutView == null || adsorptionAreaView == null) {
                return;
            }
            final int outPosition = (int) adsorptionOutView.getTag(R.id.tag_position);
            final int position = (int) adsorptionAreaView.getTag(R.id.tag_position);
            adsorptionView.setTag(R.id.tag_position, adsorptionOutView.getTag(R.id.tag_position));

            ItemAdsorptionAdapter adsorption = (ItemAdsorptionAdapter) items.get(position);
            if (adsorption.isAdsorption) {
                onAdsorptionDataSet(((ItemAdsorptionAdapter) items.get(outPosition)).itemAdsorption);
                adsorptionView.setTranslationY(adsorptionAreaView.getY() - offsetY - adsorptionView.getHeight());
            } else {
                onAdsorptionDataSet(adsorption.itemAdsorption);
                if (adsorptionView.getTranslationY() != 0) {
                    adsorptionView.setTranslationY(0);
                }
            }
        }
    };

    @Override
    public void replace(List<Object> update) {
        super.replace(update);
        View parent = (View) adsorptionView.getParent();
        parent.setVisibility((update == null || update.isEmpty()) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        onAdsorptionViewLoad();
    }

    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if ((oldItem instanceof ItemAdsorption) && (newItem instanceof ItemAdsorption)) {
            return ((ItemAdsorption) oldItem).strIndex.equals(((ItemAdsorption) newItem).strIndex);

        } else if ((oldItem instanceof ItemData) && (newItem instanceof ItemData)) {
            return ((ItemData) oldItem).resId == ((ItemData) newItem).resId;
        }
        return false;
    }

    @Override
    protected boolean areContentsTheSame(Object oldItem, Object newItem) {
        return areItemsTheSame(oldItem, newItem);
    }
}