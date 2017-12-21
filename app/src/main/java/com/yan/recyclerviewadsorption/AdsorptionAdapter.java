package com.yan.recyclerviewadsorption;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
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

    private AsyncTask adsorptionPositionTask;

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
        item.setTag(R.id.tag_adsorption, items.get(position));

        onItemDataSet(item, position);
    }

    /**
     * 可悬浮条目数据的设置
     *
     * @param objAdsorption
     * @return if data set
     */
    private boolean onAdsorptionDataSet(Object objAdsorption) {
        ItemAdsorption itemAdsorption = (ItemAdsorption) objAdsorption;
        TextView atv = adsorptionView.findViewById(R.id.tv);
        if (!TextUtils.equals(itemAdsorption.strIndex, atv.getText())) {
            atv.setText(itemAdsorption.strIndex);
            return true;
        }
        return false;
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
    private void setAdsorption(View v) {
        ItemAdsorptionAdapter adsorption = (ItemAdsorptionAdapter) v.getTag(R.id.tag_adsorption);

        if (adsorptionView.getTag() != null && !onAdsorptionDataSet(adsorption.itemAdsorption)) {
            return;
        }

        if (adsorptionPositionTask != null && !adsorptionPositionTask.isCancelled()) {
            adsorptionPositionTask.cancel(true);
        }
        adsorptionPositionTask = new AsyncTask<ItemAdsorptionAdapter, Void, Integer>() {
            @Override
            protected Integer doInBackground(ItemAdsorptionAdapter... datas) {
                ItemAdsorptionAdapter iaa = datas[0];
                if (iaa.isAdsorption) {
                    return items.indexOf(iaa);
                }

                for (int i = 0; i < items.size(); i++) {
                    ItemAdsorptionAdapter ia = (ItemAdsorptionAdapter) items.get(i);
                    if (ia.itemData == null && ia.itemAdsorption == iaa.itemAdsorption) {
                        return i;
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                adsorptionView.setTag(integer);
            }
        }.execute(adsorption);
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
            final int adsorptionPosition = (int) v.getTag();
            recyclerView.smoothScrollToPosition(adsorptionPosition);
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
            ItemAdsorptionAdapter adsorption = (ItemAdsorptionAdapter) adsorptionAreaView.getTag(R.id.tag_adsorption);

            if (adsorption.isAdsorption) {
                setAdsorption(adsorptionOutView);
                adsorptionView.setTranslationY(adsorptionAreaView.getY() - offsetY - adsorptionView.getHeight());
            } else {
                setAdsorption(adsorptionAreaView);
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